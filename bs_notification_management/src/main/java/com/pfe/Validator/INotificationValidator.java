package com.pfe.Validator;

import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.web.rest.errors.Reason;

import java.util.List;
import java.util.Set;

public interface INotificationValidator extends IGenericValidator<NotificationDTO, NotificationDTO>{

    List<Reason<?>> validateUniqueName(NotificationDTO notificationDTO);
    List<Reason<?>> validateMandatoryName(NotificationDTO notificationDTO);
    List<Reason<?>> validateDescriptionLength(NotificationDTO notificationDTO);
    List<Reason<?>> validateNotificationDestinations(NotificationDTO notificationDTO);
    List<Reason<?>> validateFrequenciesBasedOnEnum(NotificationDTO notificationDTO);
    List<Reason<?>> validateNoFileForPushedNotification(NotificationDTO notificationDTO);
    List<Reason<?>> validateUserExists(NotificationDTO notificationDTO);

    List<Reason<?>> validateDateInFrequencyPerDay(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateHourFormat(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateMandatoryStartDateAndEndDateInDailyNotification(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateIsRepeatInDailyNotification(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateDaysInWeeklyFrequency(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateStartDateAndEndDateInWeeklyFrequency(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateNoDaysInNonWeeklyNotification(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateDifferentStartDatesInCustomNotification(Set<FrequencyDTO> frequencies);


    List<Reason<?>> validateIsRepeatInCustomNotification(Set<FrequencyDTO> frequencies);

    List<Reason<?>> validateTimingFormatForNotificationPerMinute(Set<FrequencyDTO> frequencies);

}
