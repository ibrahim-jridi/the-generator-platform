package com.pfe.Validator.impl;

import com.pfe.Validator.INotificationValidator;
import com.pfe.Validator.ValidationConstants;
import com.pfe.domain.Notification;
import com.pfe.domain.enums.*;
import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.feignServices.UserFeignClient;
import com.pfe.repository.INotificationRepository;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.dto.NotificationDestinationDTO;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.RegexConstants;
import com.pfe.web.rest.errors.ValidationException;
import com.pfe.domain.enums.ConfigurationDayEnum;
import com.pfe.domain.enums.FrequencyConfigEnum;
import com.pfe.domain.enums.FrequencyEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("notificationValidator")
public class NotificationValidatorImpl implements INotificationValidator {

    private static final String NAME_ALREADY_EXISTS = "Name already exists";

    private static final String NAME_MANDATORY = "Name is mandatory";
    private static final String DESCRIPTION_MANDATORY = "Description is mandatory";
    private static final String DESCRIPTION_TOO_LONG = "Description must be 250 characters or less";
    private static final String DESTINATIONS_MANDATORY = "Notification must have at least one destination";
    private static final String FREQUENCIES_MUST_BE_NULL = "Frequencies must be null when FrequencyEnum is REAL_TIME";
    private static final String FREQUENCIES_MUST_NOT_BE_EMPTY = "Frequencies must not be empty when FrequencyEnum is CONFIGURE";
    private static final String START_DATE_IS_AFTER_END_DATE = "Start date is after end date";

    private static final String FILE_PRESENT_FOR_PUSHED_NOTIFICATION = "file present for pushed notification";

    private static final String START_DATE_IS_BEFORE_NOW = "Start date should be greater or equal to now";

    private static final String END_DATE_IS_EQUAL_NOW = "End date should be greater than now";
    private static final String END_DATE_IS_EQUAL_START_DATE = "Start date should be greater than now";

    private static final String TIMING_DOES_NOT_MATCH_HOUR_FORMAT = "Timing does not match hour format!";
    private static final String NO_DAYS_PRESENT_FOR_WEEKLY_NOTIFICATION = "No days present for weekly notification";

    private static final String START_DATE_SHOULD_NOT_BE_NULL_IN_DAILY_NOTIFICATION = "Start date should not be null in daily notification";
    private static final String END_DATE_SHOULD_NOT_BE_NULL_IN_DAILY_NOTIFICATION = "Start date should not be null in daily notification";
    private static final String DAYS_PRESENT_FOR_NON_WEEKLY_NOTIFICATION = "Days present for non weekly notification";
    private static final String START_DATE_SHOULD_NOT_BE_NULL_IN_CUSTOM_NOTIFICATION = "Start date should not be null in daily notification";
    private static final String INVALID_TIMING_FOR_NOTIFICATION_PER_MINUTE = "Timing must be a number between 1 and 59!";

    private static final String START_DATE_MUST_BE_NULL_IN_WEEKLY_NOTIFICATION = "Start date must be null in weekly notification!";
    private static final String END_DATE_MUST_BE_NULL_IN_WEEKLY_NOTIFICATION = "End date must be null in weekly notification!";
    private static final String IS_REPEAT_SHOULD_BE_FALSE_IN_CUSTOM_NOTIFICATION = "Is repeat should be false in custom notification";
    private static final String IS_REPEAT_SHOULD_BE_TRUE_IN_DAILY_NOTIFICATION = "Is repeat should be true in daily notification";
    private static final String START_DATES_SHOULD_BE_DIFFERENT_IN_CUSTOM_NOTIFICATION = "Start dates should be different in custom notification!";
    private static final String USER_NOT_FOUND = "user not found";
    private static final String ROLE_NOT_FOUND = "role not found";
    private static final String GROUP_NOT_FOUND = "group not found";
    public static final String DESTINATIONS_NOT_ALLOWED = "Destinations should not be provided when the notification is from a task";
    public static final String DESTINATION_TYPE_NOT_ALLOWED = "Destination type should not be specified for this notification.";

    private final INotificationRepository iNotificationRepository;
    private final UserFeignClient userFeignClient;

    public NotificationValidatorImpl(INotificationRepository iNotificationRepository, UserFeignClient userFeignClient) {
        this.iNotificationRepository = iNotificationRepository;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public void beforeUpdate(NotificationDTO updateRequest) {
        Set<FrequencyDTO> frequencies = updateRequest.getFrequencies();
        List<Reason<?>> reasons = new ArrayList<>();

        reasons.addAll(this.validateUniqueName(updateRequest));
        reasons.addAll(this.validateMandatoryName(updateRequest));
        reasons.addAll(this.validateDescriptionLength(updateRequest));
        reasons.addAll(this.validateFrequenciesBasedOnEnum(updateRequest));
        reasons.addAll(this.validateNoFileForPushedNotification(updateRequest));
        reasons.addAll(this.validateUserExists(updateRequest));
        reasons.addAll(this.validateMandatoryStartDateAndEndDateInDailyNotification(frequencies));
        reasons.addAll(this.validateDateInFrequencyPerDay(frequencies));
        reasons.addAll(this.validateHourFormat(frequencies));
        reasons.addAll(this.validateNoDaysInNonWeeklyNotification(frequencies));
        reasons.addAll(this.validateDaysInWeeklyFrequency(frequencies));
        reasons.addAll(this.validateTimingFormatForNotificationPerMinute(frequencies));
        reasons.addAll(this.validateIsRepeatInDailyNotification(frequencies));
        reasons.addAll(this.validateStartDateAndEndDateInWeeklyFrequency(frequencies));
        reasons.addAll(this.validateIsRepeatInCustomNotification(frequencies));
        reasons.addAll(this.validateDifferentStartDatesInCustomNotification(frequencies));
        reasons.addAll(this.validateNotificationDestinations(updateRequest));

        if(!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
    }

    @Override
    public void beforeSave(NotificationDTO createRequest) {
        Set<FrequencyDTO> frequencies = createRequest.getFrequencies();
        List<Reason<?>> reasons = new ArrayList<>();

        reasons.addAll(this.validateUniqueName(createRequest));
        reasons.addAll(this.validateMandatoryName(createRequest));
        reasons.addAll(this.validateDescriptionLength(createRequest));
        reasons.addAll(this.validateFrequenciesBasedOnEnum(createRequest));
        reasons.addAll(this.validateNoFileForPushedNotification(createRequest));
        reasons.addAll(this.validateUserExists(createRequest));
        reasons.addAll(this.validateMandatoryStartDateAndEndDateInDailyNotification(frequencies));
        reasons.addAll(this.validateDateInFrequencyPerDay(frequencies));
        reasons.addAll(this.validateHourFormat(frequencies));
        reasons.addAll(this.validateNoDaysInNonWeeklyNotification(frequencies));
        reasons.addAll(this.validateDaysInWeeklyFrequency(frequencies));
        reasons.addAll(this.validateTimingFormatForNotificationPerMinute(frequencies));
        reasons.addAll(this.validateIsRepeatInDailyNotification(frequencies));
        reasons.addAll(this.validateStartDateAndEndDateInWeeklyFrequency(frequencies));
        reasons.addAll(this.validateIsRepeatInCustomNotification(frequencies));
        reasons.addAll(this.validateDifferentStartDatesInCustomNotification(frequencies));
        reasons.addAll(this.validateNotificationDestinations(createRequest));

        if(!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
    }

    @Override
    public List<Reason<?>> validateUniqueName(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();

        Optional<Notification> existingNotification = iNotificationRepository.findByNameAndDeletedFalse(notificationDTO.getName());
        existingNotification.ifPresent(notification -> {
            if(!notification.getId().equals(notificationDTO.getId())) {
                reasons.add(new Reason<>(ValidationConstants.NAME_ALREADY_EXISTS, NAME_ALREADY_EXISTS));
            }
        });

        return reasons;
    }

    @Override
    public List<Reason<?>> validateMandatoryName(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();

        if (notificationDTO.getName() == null || notificationDTO.getName().trim().isEmpty()) {
            reasons.add(new Reason<>(ValidationConstants.NAME_MANDATORY, NAME_MANDATORY));
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateDescriptionLength(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();

        if (notificationDTO.getDescription() == null || notificationDTO.getDescription().trim().isEmpty()) {
            reasons.add(new Reason<>(ValidationConstants.DESCRIPTION_MANDATORY, DESCRIPTION_MANDATORY));
        } else if ((notificationDTO.getDescription().length() > 250) && ((notificationDTO.getBroadcastChannel()== BroadcastChannel.NOTIFICATION_PUSH)||(notificationDTO.getBroadcastChannel()== BroadcastChannel.BOTH))) {
            reasons.add(new Reason<>(ValidationConstants.DESCRIPTION_TOO_LONG, DESCRIPTION_TOO_LONG));
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateNotificationDestinations(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();
        if(!notificationDTO.getIsFromTask()){
            if (notificationDTO.getNotificationDestinations() == null || notificationDTO.getNotificationDestinations().isEmpty()) {
                reasons.add(new Reason<>(ValidationConstants.DESTINATIONS_MANDATORY, DESTINATIONS_MANDATORY));
            }else{
                validateUserExists(notificationDTO);
            }
        }else{
            if (notificationDTO.getNotificationDestinations() != null && !notificationDTO.getNotificationDestinations().isEmpty()) {
                reasons.add(new Reason<>(ValidationConstants.DESTINATIONS_NOT_ALLOWED, DESTINATIONS_NOT_ALLOWED));
            }
            if (notificationDTO.getDestinationType() != null) {
                reasons.add(new Reason<>(ValidationConstants.DESTINATION_TYPE_NOT_ALLOWED, DESTINATION_TYPE_NOT_ALLOWED));
            }
        }
        return reasons;
    }

    @Override
    public List<Reason<?>> validateFrequenciesBasedOnEnum(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();

        if (notificationDTO.getFrequencyEnum() == FrequencyEnum.REAL_TIME) {
            if (notificationDTO.getFrequencies() != null) {
                reasons.add(new Reason<>(ValidationConstants.FREQUENCIES_MUST_BE_NULL, FREQUENCIES_MUST_BE_NULL));
            }
        } else if (notificationDTO.getFrequencyEnum() == FrequencyEnum.CONFIGURE) {
            if (notificationDTO.getFrequencies() == null || notificationDTO.getFrequencies().isEmpty()) {
                reasons.add(new Reason<>(ValidationConstants.FREQUENCIES_MUST_NOT_BE_EMPTY, FREQUENCIES_MUST_NOT_BE_EMPTY));
            }
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateNoFileForPushedNotification(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(BroadcastChannel.NOTIFICATION_PUSH.equals(notificationDTO.getBroadcastChannel())) {
            if(notificationDTO.getFileName() != null && !notificationDTO.getFileName().isEmpty()) {
                reasons.add(new Reason<>(ValidationConstants.FILE_PRESENT_FOR_PUSHED_NOTIFICATION, FILE_PRESENT_FOR_PUSHED_NOTIFICATION));
            }
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateUserExists(NotificationDTO notificationDTO) {
        List<Reason<?>> reasons = new ArrayList<>();
        Set<UUID> ids = notificationDTO.getNotificationDestinations().stream()
            .map(NotificationDestinationDTO::getSenderId)
            .collect(Collectors.toSet());
 if(Boolean.FALSE.equals(notificationDTO.getIsFromTask()) && notificationDTO.getDestinationType() !=null) {
     switch (notificationDTO.getDestinationType()) {

         case USER:
             checkExistence(ids, reasons, userFeignClient::getUser,
                 ValidationConstants.USER_NOT_FOUND, USER_NOT_FOUND);
             break;

         case ROLE:
             checkExistence(ids, reasons, userFeignClient::getRole,
                 ValidationConstants.ROLE_NOT_FOUND, ROLE_NOT_FOUND);
             break;

         case GROUP:
             checkExistence(ids, reasons, userFeignClient::getGroup,
                 ValidationConstants.GROUP_NOT_FOUND, GROUP_NOT_FOUND);
             break;

         default:
             break;
     }
 }
        return reasons;
    }

    private void checkExistence(Set<UUID> ids, List<Reason<?>> reasons,
                                Function<UUID, ResponseEntity<?>> fetchFunction,
                                String validationConstant, String errorMessage) {
        ids.forEach(id -> {
            try {
                ResponseEntity<?> response = fetchFunction.apply(id);
                if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
                    reasons.add(new Reason<>(validationConstant, errorMessage));
                }
            } catch (Exception e) {
                reasons.add(new Reason<>(validationConstant, errorMessage));
            }
        });
    }

    @Override
    public List<Reason<?>> validateDateInFrequencyPerDay(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();
        if(frequencies != null){
            frequencies.forEach(frequencyDTO -> {
                if(FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig())) {

                    if(ConfigurationDayEnum.DAILY.equals(frequencyDTO.getConfigurationDay()) ) {

                        if(frequencyDTO.getStartDate() != null && frequencyDTO.getStartDate().isBefore(LocalDate.now())) {
                            reasons.add(new Reason<>(ValidationConstants.START_DATE_IS_BEFORE_NOW, START_DATE_IS_BEFORE_NOW));
                        }

                        if(frequencyDTO.getStartDate() != null && frequencyDTO.getStartDate().equals(LocalDate.now())) {
                            validateTiming(frequencyDTO.getTiming(), reasons);
                        }

                        if(frequencyDTO.getEndDate() != null && (frequencyDTO.getEndDate().isEqual(LocalDate.now()) || frequencyDTO.getEndDate().isBefore(LocalDate.now()))) {
                            reasons.add(new Reason<>(ValidationConstants.END_DATE_IS_EQUAL_NOW, END_DATE_IS_EQUAL_NOW));
                        }

                        if(frequencyDTO.getEndDate() != null && frequencyDTO.getEndDate().isEqual(frequencyDTO.getStartDate())) {
                            reasons.add(new Reason<>(ValidationConstants.END_DATE_IS_EQUAL_START_DATE, END_DATE_IS_EQUAL_START_DATE));
                        }

                        if (frequencyDTO.getStartDate() != null && frequencyDTO.getEndDate() != null
                            && frequencyDTO.getStartDate().isAfter(frequencyDTO.getEndDate())) {
                            reasons.add(
                                new Reason<>(ValidationConstants.START_DATE_IS_AFTER_END_DATE,
                                    START_DATE_IS_AFTER_END_DATE));
                        }
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateHourFormat(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null) {
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig()) || FrequencyConfigEnum.HOUR.equals(frequencyDTO.getFrequencyConfig())) {
                    if(!Pattern.matches(RegexConstants.HOUR_PATTERN,frequencyDTO.getTiming())) {
                        reasons.add(new Reason<>(ValidationConstants.TIMING_DOES_NOT_MATCH_HOUR_FORMAT, TIMING_DOES_NOT_MATCH_HOUR_FORMAT));
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateMandatoryStartDateAndEndDateInDailyNotification(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null) {
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig())) {

                    if (ConfigurationDayEnum.DAILY.equals(frequencyDTO.getConfigurationDay())) {
                        if(frequencyDTO.getStartDate() == null) {
                            reasons.add(new Reason<>(ValidationConstants.START_DATE_SHOULD_NOT_BE_NULL_IN_DAILY_NOTIFICATION, START_DATE_SHOULD_NOT_BE_NULL_IN_DAILY_NOTIFICATION));
                        }
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateIsRepeatInDailyNotification(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null) {
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig())) {

                    if (ConfigurationDayEnum.DAILY.equals(frequencyDTO.getConfigurationDay())) {
                        if(Boolean.FALSE.equals(frequencyDTO.getIsRepeat())) {
                            reasons.add(new Reason<>(ValidationConstants.IS_REPEAT_SHOULD_BE_TRUE_IN_DAILY_NOTIFICATION, IS_REPEAT_SHOULD_BE_TRUE_IN_DAILY_NOTIFICATION));
                        }
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateDaysInWeeklyFrequency(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null){
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig()) && ConfigurationDayEnum.WEEKLY.equals(frequencyDTO.getConfigurationDay())) {
                    if(frequencyDTO.getDays() == null || frequencyDTO.getDays().isEmpty()) {
                        reasons.add(new Reason<>(ValidationConstants.NO_DAYS_PRESENT_FOR_WEEKLY_NOTIFICATION, NO_DAYS_PRESENT_FOR_WEEKLY_NOTIFICATION));
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateStartDateAndEndDateInWeeklyFrequency(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null){
            frequencies.forEach(frequencyDTO -> {
                if (ConfigurationDayEnum.WEEKLY.equals(frequencyDTO.getConfigurationDay())) {

                    if(frequencyDTO.getStartDate() != null) {
                        reasons.add(new Reason<>(ValidationConstants.START_DATE_MUST_BE_NULL_IN_WEEKLY_NOTIFICATION, START_DATE_MUST_BE_NULL_IN_WEEKLY_NOTIFICATION));
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateNoDaysInNonWeeklyNotification(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null){
            frequencies.forEach(frequencyDTO -> {

                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig()) && !ConfigurationDayEnum.WEEKLY.equals(frequencyDTO.getConfigurationDay())) {
                    if(frequencyDTO.getDays() != null) {
                        reasons.add(new Reason<>(ValidationConstants.DAYS_PRESENT_FOR_NON_WEEKLY_NOTIFICATION, DAYS_PRESENT_FOR_NON_WEEKLY_NOTIFICATION));
                    }
                }

                if (FrequencyConfigEnum.HOUR.equals(frequencyDTO.getFrequencyConfig()) || FrequencyConfigEnum.MINUTE.equals(frequencyDTO.getFrequencyConfig())) {
                    if(frequencyDTO.getDays() != null) {
                        reasons.add(new Reason<>(ValidationConstants.DAYS_PRESENT_FOR_NON_WEEKLY_NOTIFICATION, DAYS_PRESENT_FOR_NON_WEEKLY_NOTIFICATION));
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateDifferentStartDatesInCustomNotification(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null) {
            Set<LocalDate> startDates = new HashSet<>();
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig())) {

                    if (ConfigurationDayEnum.CUSTOM.equals(frequencyDTO.getConfigurationDay())) {

                        if(frequencyDTO.getStartDate() == null) {
                            reasons.add(new Reason<>(ValidationConstants.START_DATE_SHOULD_NOT_BE_NULL_IN_CUSTOM_NOTIFICATION, START_DATE_SHOULD_NOT_BE_NULL_IN_CUSTOM_NOTIFICATION));
                        }

                        if(frequencyDTO.getStartDate() != null && frequencyDTO.getStartDate().isBefore(LocalDate.now())) {
                            reasons.add(new Reason<>(ValidationConstants.START_DATE_IS_BEFORE_NOW, START_DATE_IS_BEFORE_NOW));
                        }

                        if(frequencyDTO.getStartDate() != null && frequencyDTO.getStartDate().equals(LocalDate.now())) {
                            validateTiming(frequencyDTO.getTiming(), reasons);
                        }

                        if(!startDates.add(frequencyDTO.getStartDate())) {
                            reasons.add(new Reason<>(ValidationConstants.START_DATES_SHOULD_BE_DIFFERENT_IN_CUSTOM_NOTIFICATION, START_DATES_SHOULD_BE_DIFFERENT_IN_CUSTOM_NOTIFICATION));
                        }
                    }
                }
            });
        }

        return reasons;
    }

    private void validateTiming(String time, List<Reason<?>> reasons) {
        try {
            LocalTime providedTime = LocalTime.parse(time);
            if(providedTime.isBefore(LocalTime.now())) {
                reasons.add(new Reason<>(ValidationConstants.START_DATE_IS_BEFORE_NOW, START_DATE_IS_BEFORE_NOW));
            }

        }catch (DateTimeParseException e) {
            reasons.add(new Reason<>(ValidationConstants.TIMING_DOES_NOT_MATCH_HOUR_FORMAT, TIMING_DOES_NOT_MATCH_HOUR_FORMAT));
        }
    }

    @Override
    public List<Reason<?>> validateIsRepeatInCustomNotification(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if(frequencies != null) {
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.DAY.equals(frequencyDTO.getFrequencyConfig())) {

                    if (ConfigurationDayEnum.CUSTOM.equals(frequencyDTO.getConfigurationDay())) {
                        if(Boolean.TRUE.equals(frequencyDTO.getIsRepeat())) {
                            reasons.add(new Reason<>(ValidationConstants.IS_REPEAT_SHOULD_BE_FALSE_IN_CUSTOM_NOTIFICATION, IS_REPEAT_SHOULD_BE_FALSE_IN_CUSTOM_NOTIFICATION));
                        }
                    }
                }
            });
        }

        return reasons;
    }

    @Override
    public List<Reason<?>> validateTimingFormatForNotificationPerMinute(Set<FrequencyDTO> frequencies) {
        List<Reason<?>> reasons = new ArrayList<>();

        if (frequencies != null) {
            frequencies.forEach(frequencyDTO -> {
                if (FrequencyConfigEnum.MINUTE.equals(frequencyDTO.getFrequencyConfig())) {
                    String timing = frequencyDTO.getTiming();
                    if (timing == null || !isValidTiming(timing)) {
                        reasons.add(new Reason<>(ValidationConstants.INVALID_TIMING_FOR_NOTIFICATION_PER_MINUTE, INVALID_TIMING_FOR_NOTIFICATION_PER_MINUTE));
                    }
                }
            });
        }

        return reasons;
    }

    private boolean isValidTiming(String timing) {
        try {
            int minute = Integer.parseInt(timing);
            return minute >= 1 && minute <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
