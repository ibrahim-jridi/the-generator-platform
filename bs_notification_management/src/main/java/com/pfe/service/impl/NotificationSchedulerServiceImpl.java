package com.pfe.service.impl;

import com.pfe.domain.enums.ConfigurationDayEnum;
import com.pfe.domain.enums.FrequencyConfigEnum;
import com.pfe.domain.enums.FrequencyEnum;
import com.pfe.domain.enums.Status;
import com.pfe.feignServices.ReportFeignClient;
import com.pfe.feignServices.UserFeignClient;
import com.pfe.feignServices.WorkflowFeignClient;
import com.pfe.repository.EventNotificationRepository;
import com.pfe.service.*;
import com.pfe.service.dto.*;
import com.pfe.service.EventNotificationService;
import com.pfe.service.INotificationSchedulerService;
import com.pfe.service.INotificationService;
import com.pfe.service.MailService;
import com.pfe.service.NotificationPushedService;
import com.pfe.service.dto.EventNotificationDTO;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.web.rest.errors.ValidationException;
import com.pfe.service.dto.NotificationPushedDTO;
import com.pfe.service.dto.SignInRequest;
import com.pfe.service.dto.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.TriggerContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
public class NotificationSchedulerServiceImpl implements INotificationSchedulerService {

    private final Logger log = LoggerFactory.getLogger(NotificationSchedulerServiceImpl.class);

    @Value("${spring.security.oauth2.client.login-user-admin}")
    private String adminLogin;

    @Value("${spring.security.oauth2.client.password-user-admin}")
    private String adminPassword;

    private final TaskScheduler taskScheduler;
    private final UserFeignClient userFeignClient;
    private final MailService mailService;
    private final NotificationPushedService notificationPushedService;
    private final Map<String, LocalDate> lastNotificationSent = new ConcurrentHashMap<>();
    private final INotificationService notificationService;
    private final EventNotificationService eventNotificationService;
    private final EventNotificationRepository eventNotificationRepository;
    private final WorkflowFeignClient workflowFeignClient;
    private final ReportFeignClient minioFeignClient;


    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
    private final SimpMessagingTemplate template;
    private UUID assigneeUuid;

    @Value(value = "${bs-app.endpoint.gateway-url}")
    private String gatewayUrl;
    public NotificationSchedulerServiceImpl(TaskScheduler taskScheduler, UserFeignClient userFeignClient, MailService mailService, NotificationPushedService notificationPushedService, INotificationService notificationService, EventNotificationService eventNotificationService, EventNotificationRepository eventNotificationRepository, WorkflowFeignClient workflowFeignClient, ReportFeignClient minioFeignClient, SimpMessagingTemplate template) {
        this.taskScheduler = taskScheduler;
        this.userFeignClient = userFeignClient;
        this.mailService = mailService;
        this.notificationPushedService = notificationPushedService;
        this.notificationService = notificationService;
        this.eventNotificationService = eventNotificationService;
        this.eventNotificationRepository = eventNotificationRepository;
        this.workflowFeignClient = workflowFeignClient;
        this.minioFeignClient = minioFeignClient;
        this.template = template;
    }

    @Override
    public void startScheduleNotification(UUID notificationId, String taskId) {
        log.info("Request to configure scheduled notification: {}", notificationId);

        // Retrieve the notification details
        NotificationDTO notificationDTO = notificationService.findOneNotification(notificationId)
                .orElseThrow(() -> new ValidationException("Notification not found"));

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String token = ((BearerTokenAuthentication) securityContextHolder).getToken().getTokenValue();

        // Fetch TaskDTO using the taskId and token
        TaskDTO taskDTO = workflowFeignClient.getTaskById(taskId, "Bearer " + token).getBody();
        if (taskDTO == null) {
            throw new ValidationException("TaskDTO is null for taskId: " + taskId);
        }

        // Retrieve the file data if a file is associated with the notification
        byte[] fileData = null;
        if (notificationDTO.getFileName() != null && !notificationDTO.getFileName().isEmpty()) {
            fileData = minioFeignClient.getUploadedFile(notificationDTO.getFileName(), "notification", "Bearer " + token).getBody();
        }

        // Populate notificationDTO with additional data
        notificationDTO.setTaskDTO(taskDTO);
        notificationDTO.setFileData(fileData);

        // Start notification and scheduling
        startEventNotification(notificationDTO);
        startFrequancySchedule(notificationDTO, token);
    }

    private void startFrequancySchedule(NotificationDTO notificationDTO, String token) {
        // Handle frequency-based scheduling
        if (notificationDTO.getFrequencyEnum() != null && notificationDTO.getFrequencyEnum().equals(FrequencyEnum.REAL_TIME)) {
            scheduleRealTimeNotification(notificationDTO, token);
        } else {
            if (!notificationDTO.getFrequencies().isEmpty()) {
                notificationDTO.getFrequencies().forEach(frequencyDTO -> {
                    switch (frequencyDTO.getFrequencyConfig()) {
                        case DAY -> handleDayFrequency(frequencyDTO, notificationDTO, token);
                        case HOUR -> scheduleHourlyNotification(frequencyDTO, notificationDTO, token);
                        case MINUTE -> scheduleMinuteNotification(frequencyDTO, notificationDTO, token);
                        default ->
                                throw new ValidationException("Unknown frequency configuration: " + frequencyDTO.getFrequencyConfig());
                    }
                });
            }
        }
    }

    private List<UserDTO> listDestinationUsersFromTask(NotificationDTO notificationDTO, String token) {
        List<UserDTO> users = new ArrayList<>();
        String assignee = notificationDTO.getTaskDTO().getAssignee();
        UUID assigneeUuid;

        try {
            assigneeUuid = UUID.fromString(assignee);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format for assignee: {}", assignee, e);
            throw new ValidationException("Assignee is not a valid UUID");
        }

        // Récupérer l'utilisateur via l'assignee
        users = List.of(Objects.requireNonNull(userFeignClient.getUserWithAuthorization(assigneeUuid, "Bearer " + token).getBody()));

        // Si la liste est vide, essayer de récupérer les utilisateurs par groupe
        if (users.isEmpty()) {
            users = userFeignClient.getUserByGroupId(assigneeUuid).getBody();
        }

        // Si la liste est toujours vide, essayer de récupérer les utilisateurs par rôle
        if (users.isEmpty()) {
            users = userFeignClient.getUsersByRoleId(assigneeUuid).getBody();
        }

        if (users.isEmpty()) {
            throw new ValidationException("Empty List users ");
        }
        return users;
    }

    private List<UserDTO> listDestinationUsers(NotificationDTO notificationDTO, UUID senderId, String token) {
        return switch (notificationDTO.getDestinationType()) {
            case USER ->
                    List.of(Objects.requireNonNull(userFeignClient.getUserWithAuthorization(senderId, "Bearer " + token).getBody()));
            case GROUP -> userFeignClient.getUserByGroupId(senderId).getBody();
            case ROLE -> userFeignClient.getUsersByRoleId(senderId).getBody();
        };
    }

    private void scheduleRealTimeNotification(NotificationDTO notificationDTO, String token) {
        Instant now = Instant.now();
        if (Boolean.TRUE.equals(notificationDTO.getIsFromTask())) {
            List<UserDTO> destinationUsers = listDestinationUsersFromTask(notificationDTO, token);
            destinationUsers.forEach(user -> taskScheduler.schedule(() -> sendNotification(notificationDTO, user), Date.from(now)));
        } else {
            notificationDTO.getNotificationDestinations().forEach(destination -> {
                List<UserDTO> destinationUsers = listDestinationUsers(notificationDTO, destination.getSenderId(), token);
                destinationUsers.forEach(user -> taskScheduler.schedule(() -> sendNotification(notificationDTO, user), Date.from(now)));
            });
        }
    }

    private void handleDayFrequency(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        switch (frequencyDTO.getConfigurationDay()) {
            case DAILY -> scheduleDailyNotification(frequencyDTO, notificationDTO, token);
            case WEEKLY -> scheduleWeeklyNotification(frequencyDTO, notificationDTO, token);
            case CUSTOM -> scheduleCustomNotification(frequencyDTO, notificationDTO, token);
            default ->
                    throw new ValidationException("Unknown day configuration: " + frequencyDTO.getConfigurationDay());
        }
    }

    private void scheduleDailyNotification(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        scheduleNotification(frequencyDTO, notificationDTO, triggerContext -> calculateDailyNextRun(frequencyDTO), token);
    }

    private void scheduleWeeklyNotification(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        if (Boolean.TRUE.equals(frequencyDTO.getIsRepeat())) {
            scheduleNotification(frequencyDTO, notificationDTO,
                    triggerContext -> calculateWeeklyNextRun(frequencyDTO), token);
        } else {
            scheduleNotification(frequencyDTO, notificationDTO,
                    triggerContext -> calculateNonRepeatingRun(frequencyDTO), token);
        }
    }

    public LocalDate getStartDateOrDefault(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.now();
    }

    private Instant calculateNonRepeatingRun(FrequencyDTO frequencyDTO) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault()); // Date et heure actuelles
        LocalDate startDate = getStartDateOrDefault(frequencyDTO.getStartDate());

        validateTiming(frequencyDTO.getTiming());
        String[] timeParts = frequencyDTO.getTiming().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Set<DayOfWeek> daysOfWeek = frequencyDTO.getDays() == null ?
            Collections.emptySet() :
            frequencyDTO.getDays().stream()
                .map(daysDTO -> DayOfWeek.valueOf(daysDTO.getName()))
                .collect(Collectors.toSet());

        if (daysOfWeek.isEmpty()) {
            return null;
        }

        ZonedDateTime startDateTime = startDate.atTime(hour, minute).atZone(ZoneId.systemDefault());

        while (!daysOfWeek.contains(startDateTime.getDayOfWeek())) {
            startDateTime = startDateTime.plusDays(1);
        }

        if (now.isBefore(startDateTime)) {
            return startDateTime.toInstant();
        }
        return null;
    }


    private void scheduleNotification(FrequencyDTO frequencyDTO, NotificationDTO
            notificationDTO, Function<TriggerContext, Instant> nextRunCalculator, String token) {
        if (Boolean.TRUE.equals(notificationDTO.getIsFromTask())) {
            List<UserDTO> destinationUsers = listDestinationUsersFromTask(notificationDTO, token);
            destinationUsers.forEach(user -> {
                String userKey = user.getId() + "-" + notificationDTO.getId();
                EventNotificationDTO eventNotificationDTO = eventNotificationService.findByNotificationIdAndTaskId(notificationDTO.getId(), notificationDTO.getTaskDTO().getId()).orElseThrow(() ->
                        new IllegalStateException("Event notification not found for notificationId: " + notificationDTO.getId() + " and taskId: " + notificationDTO.getTaskDTO().getId()));
                if (eventNotificationDTO.getStatus().equals(Status.STOPPED)) {
                    log.info("Notification for user {} and notification {} is already completed. Skipping scheduling.", user.getId(), notificationDTO.getId());
                    return;
                }
                taskScheduler.schedule(() -> processNotification(userKey, frequencyDTO.getEndDate(), notificationDTO, user), nextRunCalculator::apply);
            });
        } else {
            notificationDTO.getNotificationDestinations().forEach(destination -> {
                List<UserDTO> destinationUsers = listDestinationUsers(notificationDTO, destination.getSenderId(), token);
                destinationUsers.forEach(user -> {
                    String userKey = user.getId() + "-" + notificationDTO.getId();
                    EventNotificationDTO eventNotificationDTO = eventNotificationService.findByNotificationIdAndTaskId(notificationDTO.getId(), notificationDTO.getTaskDTO().getId()).orElseThrow(() ->
                            new IllegalStateException("Event notification not found for notificationId: " + notificationDTO.getId() + " and taskId: " + notificationDTO.getTaskDTO().getId()));
                    if (eventNotificationDTO.getStatus().equals(Status.STOPPED)) {
                        log.info("Notification for user {} and notification {} is already completed. Skipping scheduling.", user.getId(), notificationDTO.getId());
                        return;
                    }
                    taskScheduler.schedule(() -> processNotification(userKey, frequencyDTO.getEndDate(), notificationDTO, user), nextRunCalculator::apply);
                });
            });
        }
    }

    private void processNotification(String userKey, LocalDate endInstant, NotificationDTO notificationDTO, UserDTO user) {
        try {
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            if (lastNotificationSent.containsKey(userKey) && lastNotificationSent.get(userKey).isEqual(today)) {
                return;
            }
            if (endInstant == null || today.isBefore(endInstant)) {
                sendNotification(notificationDTO, user);
                lastNotificationSent.put(userKey, today);
            } else {
                stopScheduleNotification(notificationDTO.getId(), notificationDTO.getTaskDTO().getId());
            }
        } catch (Exception e) {
            log.error("Error processing notification for user {}: {}", user.getId(), e.getMessage(), e);
        }
    }

    private Instant calculateDailyNextRun(FrequencyDTO frequencyDTO) {
        validateTiming(frequencyDTO.getTiming());
        String[] timeParts = frequencyDTO.getTiming().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(0);

        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }
        return nextRun.toInstant();
    }

    private Instant calculateWeeklyNextRun(FrequencyDTO frequencyDTO) {
        validateTiming(frequencyDTO.getTiming());
        String[] timeParts = frequencyDTO.getTiming().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(0);
        Set<DayOfWeek> daysOfWeek = frequencyDTO.getDays() == null ? Collections.emptySet() : frequencyDTO.getDays().stream().map(daysDTO -> DayOfWeek.valueOf(daysDTO.getName())).collect(Collectors.toSet());

        while (!daysOfWeek.contains(nextRun.getDayOfWeek()) || nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }
        return nextRun.toInstant();
    }

    private void scheduleCustomNotification(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        Instant startTime = getStartTime(frequencyDTO);
        if (Boolean.TRUE.equals(notificationDTO.getIsFromTask())) {
            List<UserDTO> destinationUsers = listDestinationUsersFromTask(notificationDTO, token);
            destinationUsers.forEach(user -> taskScheduler.schedule(() -> sendNotification(notificationDTO, user), Date.from(startTime)));
        } else {
            notificationDTO.getNotificationDestinations().forEach(destination -> {
                List<UserDTO> destinationUsers = listDestinationUsers(notificationDTO, destination.getSenderId(), token);
                destinationUsers.forEach(user -> taskScheduler.schedule(() -> sendNotification(notificationDTO, user), Date.from(startTime)));
            });
        }
    }

    private Instant getStartTime(FrequencyDTO frequencyDTO) {
        LocalDate startDate = getStartDateOrDefault(frequencyDTO.getStartDate());

        LocalTime time = parseTimingOrDefault(frequencyDTO.getTiming());

        return startDate.atTime(time).atZone(ZoneId.systemDefault()).toInstant();
    }

    private LocalTime parseTimingOrDefault(String timing) {
        try {
            return LocalTime.parse(timing, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException | NullPointerException e) {
            return LocalTime.MIDNIGHT;
        }
    }

    private void scheduleHourlyNotification(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        if (Boolean.TRUE.equals(frequencyDTO.getIsRepeat())) {
            scheduleFixedRateNotification(getStartDateOrDefault(frequencyDTO.getStartDate()), frequencyDTO.getEndDate(), parseHourlyTiming(frequencyDTO.getTiming()), notificationDTO, token);
        } else {
            scheduleCustomNotification(frequencyDTO, notificationDTO, token);
        }
    }

    private void scheduleMinuteNotification(FrequencyDTO frequencyDTO, NotificationDTO notificationDTO, String token) {
        if (Boolean.TRUE.equals(frequencyDTO.getIsRepeat())) {
            scheduleFixedRateNotification(getStartDateOrDefault(frequencyDTO.getStartDate()), frequencyDTO.getEndDate(), parseMinuteTiming(frequencyDTO.getTiming()), notificationDTO, token);
        } else {
            scheduleCustomNotification(frequencyDTO, notificationDTO, token);
        }
    }

    private void scheduleFixedRateNotification(LocalDate startDate, LocalDate endDate, Duration interval,
                                               NotificationDTO notificationDTO, String token) {
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = (endDate != null) ? endDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

        if (Boolean.TRUE.equals(notificationDTO.getIsFromTask())) {
            handleTaskNotification(notificationDTO, token, startInstant, endInstant, interval);
        } else {
            handleNonTaskNotification(notificationDTO, token, startInstant, endInstant, interval);
        }
    }

    private void handleTaskNotification(NotificationDTO notificationDTO, String token, Instant startInstant,
                                        Instant endInstant, Duration interval) {
        List<UserDTO> destinationUsers = listDestinationUsersFromTask(notificationDTO, token);
        destinationUsers.forEach(user -> scheduleNotificationTask(notificationDTO, startInstant, endInstant, interval, user));
    }

    private void handleNonTaskNotification(NotificationDTO notificationDTO, String token, Instant startInstant,
                                           Instant endInstant, Duration interval) {
        notificationDTO.getNotificationDestinations().forEach(destination -> {
            List<UserDTO> destinationUsers = listDestinationUsers(notificationDTO, destination.getSenderId(), token);
            destinationUsers.forEach(user -> scheduleNotificationTask(notificationDTO, startInstant, endInstant, interval, user));
        });
    }

    private void scheduleNotificationTask(NotificationDTO notificationDTO, Instant startInstant, Instant endInstant,
                                          Duration interval, UserDTO user) {
        taskScheduler.scheduleAtFixedRate(() -> {
            try {
                if (shouldSendNotification(notificationDTO, endInstant)) {
                    sendNotification(notificationDTO, user);
                } else {
                    stopScheduleNotification(notificationDTO.getId(), notificationDTO.getTaskDTO().getId());
                }
            } catch (Exception e) {
                log.error("Error while processing scheduled notification: {}", e.getMessage(), e);
            }
        }, startInstant, interval);
    }

    private boolean shouldSendNotification(NotificationDTO notificationDTO, Instant endInstant) {
        EventNotificationDTO eventNotificationDTO = eventNotificationService
                .findByNotificationIdAndTaskId(notificationDTO.getId(), notificationDTO.getTaskDTO().getId())
                .orElseThrow(() -> new IllegalStateException("Event notification not found"));

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        return (endInstant == null || today.isBefore(endInstant.atZone(ZoneId.systemDefault()).toLocalDate()))
                && eventNotificationDTO.getStatus().equals(Status.STARTED);
    }

    private void sendNotification(NotificationDTO notificationDTO, UserDTO userDTO) {
        saveNotificationPushed(notificationDTO, userDTO.getId());
        switch (notificationDTO.getBroadcastChannel()) {
            case EMAIL -> sendEmailToUser(notificationDTO, userDTO);
            case NOTIFICATION_PUSH -> sendNotificationToUser(notificationDTO, userDTO);
            case BOTH -> {
                sendEmailToUser(notificationDTO, userDTO);
                sendNotificationToUser(notificationDTO, userDTO);
            }
            default ->
                    throw new ValidationException("Unknown broadcast channel: " + notificationDTO.getBroadcastChannel());
        }
    }

    private void sendEmailToUser(NotificationDTO notificationDTO, UserDTO userDTO) {
        String subject = notificationDTO.getName() + " - " + notificationDTO.getTaskDTO().getName();
        if (notificationDTO.getFileName() == null || notificationDTO.getFileName().isEmpty()) {
            mailService.sendSimpleNotificationEmail(userDTO, subject, notificationDTO.getDescription());
        } else {
            mailService.sendSimpleNotificationEmailWithFile(userDTO, subject, notificationDTO.getDescription(), notificationDTO.getFileData(), notificationDTO.getFileName());
        }
    }

    private Duration parseHourlyTiming(String timing) {
        return parseTiming(timing, "hours:minutes");
    }

    private Duration parseMinuteTiming(String timing) {
        return parseTiming(timing, "minutes:seconds");
    }

    private Duration parseTiming(String timing, String format) {
        if (timing == null || timing.isEmpty()) {
            throw new IllegalArgumentException("Timing cannot be null or empty");
        }
        String[] parts = timing.split(":");

        if (parts.length == 1) {
            parts = new String[]{parts[0], "00"};
        }

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid timing format. Expected '" + format + "'");
        }

        try {
            int firstPart = Integer.parseInt(parts[0]);
            int secondPart = Integer.parseInt(parts[1]);

            return format.equals("hours:minutes")
                    ? Duration.ofHours(firstPart).plusMinutes(secondPart)
                    : Duration.ofMinutes(firstPart).plusSeconds(secondPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid timing format. Expected '" + format + "'", e);
        }
    }

    private void validateTiming(String timing) {
        if (!timing.matches("\\d{1,2}:\\d{1,2}")) {
            throw new ValidationException("Invalid timing format: " + timing);
        }
    }


    private void startEventNotification(NotificationDTO notificationDTO) {
        log.info("Request to save event schedule Notification started : {}", notificationDTO.getId());
        String taskId = notificationDTO.getTaskDTO().getId();
        Optional<EventNotificationDTO> existingEventNotification =
                eventNotificationService.findByNotificationIdAndTaskId(notificationDTO.getId(), taskId);

        if (existingEventNotification.isPresent()) {
            EventNotificationDTO eventNotificationDTO = existingEventNotification.orElseThrow();
            eventNotificationDTO.setStatus(Status.STARTED);
            eventNotificationService.save(eventNotificationDTO);
            eventNotificationRepository.flush();
            log.info("Existing EventNotification updated with status STARTED.");
        } else {
            EventNotificationDTO newEventNotificationDTO = new EventNotificationDTO();
            newEventNotificationDTO.setNotification(notificationDTO);
            newEventNotificationDTO.setTaskId(taskId);
            if (notificationDTO.getTaskDTO().getName() != null) {
                newEventNotificationDTO.setTaskName(notificationDTO.getTaskDTO().getName());
            } else {
                throw new ValidationException("Task not found " + taskId);
            }
            newEventNotificationDTO.setStatus(Status.STARTED);
            eventNotificationService.save(newEventNotificationDTO);
            eventNotificationRepository.flush();
            log.info("New EventNotification created with status STARTED. {} ", newEventNotificationDTO);

        }
    }

    private void saveNotificationPushed(NotificationDTO notification, UUID userId) {
        log.info("Request to save event notification pushed with notificationID {} and  taskId  {}", notification.getId(), notification.getTaskDTO().getId());
        EventNotificationDTO currentEventNotification =
                eventNotificationService.findByNotificationIdAndTaskId(notification.getId(), notification.getTaskDTO().getId()).orElseThrow();
        if(notification.getFrequencyEnum().equals(FrequencyEnum.REAL_TIME) ){
            currentEventNotification.setStatus(Status.STOPPED);
            eventNotificationService.save(currentEventNotification);
        }
        if (notification.getFrequencyEnum().equals(FrequencyEnum.CONFIGURE)) {
            boolean hasCustomConfigDay = notification.getFrequencies().stream()
                .anyMatch(frequencyDTO -> frequencyDTO.getFrequencyConfig().equals(FrequencyConfigEnum.DAY) && frequencyDTO.getConfigurationDay().equals(ConfigurationDayEnum.CUSTOM));

            if (hasCustomConfigDay || !(notification.getFrequencies().stream()
                .anyMatch(frequencyDTO -> frequencyDTO.getIsRepeat()))) {
                currentEventNotification.setStatus(Status.STOPPED);
                eventNotificationService.save(currentEventNotification);
            }
        }
        NotificationPushedDTO notificationPushed = new NotificationPushedDTO();
        notificationPushed.setNotificationDate(Instant.now());
        notificationPushed.setDescription(notification.getDescription());
        notificationPushed.setName(notification.getName());
        notificationPushed.setMessage(notification.getDescription());
        notificationPushed.setIsSeen(false);
        notificationPushed.setRecipientId(userId);
        notificationPushed.setEventNotification(currentEventNotification);
        notificationPushedService.save(notificationPushed);
    }

    /**
     * Send notification to users subscribed on channel "/user/topic".
     * <p>
     * The message will be sent only to the user with the given UUID.
     *
     * @param notification The notification that will be sent to the user
     * @param userDTO      The user to whom the notification will be sent
     */
    @Override
    public void sendNotificationToUser(NotificationDTO notification, UserDTO userDTO) {
        log.info("Request to send notification to user '{}' with task '{}': {}", userDTO.getId(), notification.getTaskDTO().getName(), notification);
        this.template.convertAndSendToUser(userDTO.getId().toString(), notification.getTopic().toString(), notification);
    }

    @Override
    public void stopScheduleNotification(UUID notificationId, String taskId) {
        log.info("Request to stop schedule Notification : {}", notificationId);
        EventNotificationDTO eventNotificationDTO = eventNotificationService.findByNotificationIdAndTaskId(notificationId, taskId).orElseThrow();
        eventNotificationDTO.setStatus(Status.STOPPED);
        eventNotificationService.save(eventNotificationDTO);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startAllNotificationOnApplicationStart() {
        log.info("L'application est prête. Démarrage des tâches de notification planifiées with EventListener.");
        try {
            List<EventNotificationDTO> eventNotificationDTOS = eventNotificationService.findByStatus(Status.STARTED);
            for (EventNotificationDTO eventNotification : eventNotificationDTOS) {
                // Démarrer une notification planifiée
                String token = getFreshToken();
                startScheduleNotificationOnApplicationStart(eventNotification.getNotification().getId(), eventNotification.getTaskId(), token);
                log.info("Tâche de notification planifiée démarrée avec succès : NotificationId={}, TaskId={}", eventNotification.getNotification().getId(), eventNotification.getTaskId());
            }
        } catch (Exception e) {
            log.error("Erreur lors du démarrage des tâches planifiées au démarrage de l'application", e);
        }
    }

    private String getFreshToken() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername(adminLogin);
        signInRequest.setPassword(adminPassword);

        // Use RestTemplate to fetch a fresh token
        RestTemplate restTemplate = new RestTemplate();
        return getTokenFromGateway(signInRequest, restTemplate);
    }

    public void startScheduleNotificationOnApplicationStart(UUID notificationId, String taskId, String token) {
        log.info("Request to configure scheduled notification: {}", notificationId);

        // Retrieve the notification details
        NotificationDTO notificationDTO = notificationService.findOneNotification(notificationId)
                .orElseThrow(() -> new ValidationException("Notification not found"));

        if (notificationDTO.getFrequencyEnum().equals(FrequencyEnum.CONFIGURE)) {
            boolean hasCustomConfigDay = notificationDTO.getFrequencies().stream()
                .anyMatch(frequencyDTO -> frequencyDTO.getFrequencyConfig().equals(FrequencyConfigEnum.DAY));

            if (hasCustomConfigDay) {

            }
        }

        // Fetch TaskDTO using the taskId and token
        TaskDTO taskDTO = workflowFeignClient.getTaskById(taskId, "Bearer " + token).getBody();
        if (taskDTO == null) {
            throw new ValidationException("TaskDTO is null for taskId: " + taskId);
        }

        // Retrieve the file data if a file is associated with the notification
        byte[] fileData = null;
        if (notificationDTO.getFileName() != null && !notificationDTO.getFileName().isEmpty()) {
            fileData = minioFeignClient.getUploadedFile(notificationDTO.getFileName(), "notification", "Bearer " + token).getBody();
        }

        // Populate notificationDTO with additional data
        notificationDTO.setTaskDTO(taskDTO);
        notificationDTO.setFileData(fileData);

        // Start notification and scheduling
        startEventNotification(notificationDTO);
        startFrequancySchedule(notificationDTO, token);
    }

    private String getTokenFromGateway(SignInRequest signInRequest, RestTemplate restTemplate) {
        try {
            // Set up headers and entity
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SignInRequest> entity = new HttpEntity<>(signInRequest, headers);
            log.info("********{}",gatewayUrl);
            // Send the POST request
            ResponseEntity<Map> response = restTemplate.exchange(
                gatewayUrl+"/api/v1/oauth/token",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // Check response status
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("access_token")) {
                    log.info("Successfully retrieved token from gateway.");
                    return (String) responseBody.get("access_token");
                } else {
                    log.error("Response body does not contain an access token: {}", responseBody);
                    throw new ValidationException("Token not found in the response.");
                }
            } else {
                // Log and throw exception for non-OK responses
                log.error("Failed to authenticate. Response code: {}. Response body: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Error during authentication with gateway: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Log specific HTTP exceptions with detailed diagnostics
            log.error("HTTP error during authentication with gateway: {}. Response body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("HTTP error during authentication with gateway", e);
        } catch (Exception e) {
            // Log any other exceptions
            log.error("Unexpected error during authentication with gateway: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during authentication with gateway", e);
        }
    }
}
