package com.pfe.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.Validator.INotificationValidator;
import com.pfe.domain.Frequency;
import com.pfe.domain.Notification;
import com.pfe.domain.NotificationDestination;
import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.feignServices.ReportFeignClient;
import com.pfe.feignServices.UserFeignClient;
import com.pfe.repository.IFrequencyRepository;
import com.pfe.repository.INotificationDestinationRepository;
import com.pfe.repository.INotificationRepository;
import com.pfe.service.IFrequencyService;
import com.pfe.service.INotificationDestinationService;
import com.pfe.service.INotificationService;
import com.pfe.service.dto.*;
import com.pfe.service.dto.request.NotificationRequest;
import com.pfe.service.mapper.*;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.mapper.INotificationMapper;
import com.pfe.domain.enums.DestinationTypeEnum;
import com.pfe.service.dto.Base64FileDTO;
import com.pfe.service.dto.NotificationDestinationDTO;
import com.pfe.service.mapper.INotificationRequestMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements INotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final INotificationRepository notificationRepository;

    private final INotificationMapper notificationMapper;

    private final INotificationValidator iNotificationValidator;

    private final INotificationDestinationRepository iNotificationDestinationRepository;

    private final INotificationDestinationService iNotificationDestinationService;

    private final IFrequencyService iFrequencyService;

    private final IFrequencyRepository iFrequencyRepository;

    private final UserFeignClient userFeignClient;

    private final INotificationRequestMapper iNotificationRequestMapper;

    private final ReportFeignClient reportFeignClient;

    public NotificationServiceImpl(INotificationRepository notificationRepository, INotificationMapper notificationMapper, INotificationValidator iNotificationValidator, INotificationDestinationRepository iNotificationDestinationRepository, INotificationDestinationService iNotificationDestinationService, IFrequencyService iFrequencyService, IFrequencyRepository iFrequencyRepository, UserFeignClient userFeignClient, INotificationRequestMapper iNotificationRequestMapper,
        ReportFeignClient reportFeignClient) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.iNotificationValidator = iNotificationValidator;
        this.iNotificationDestinationRepository = iNotificationDestinationRepository;
        this.iNotificationDestinationService = iNotificationDestinationService;
        this.iFrequencyService = iFrequencyService;
        this.iFrequencyRepository = iFrequencyRepository;
        this.userFeignClient = userFeignClient;
        this.iNotificationRequestMapper = iNotificationRequestMapper;
      this.reportFeignClient = reportFeignClient;
    }

    @Override
    @Transactional
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        this.iNotificationValidator.beforeSave(notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        if (notificationDTO.getFileBase64() != null) {
            reportFeignClient.uploadNotificationFile(
                new Base64FileDTO(convertBase64(notificationDTO.getFileBase64()),
                    notificationDTO.getFileName(), "notification")
            ).getBody();

            notification.setFileName(notificationDTO.getFileName());
        }
        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    @Override
    public NotificationDTO update(NotificationDTO notificationDTO) {
        log.debug("Request to update Notification : {}", notificationDTO);
        this.iNotificationValidator.beforeUpdate(notificationDTO);
        Notification notification = getNotificationById(notificationDTO.getId());
        notification.setFileName ( this.processNotificationAttachment(notification, notificationDTO));

        updateNotificationDetails(notification, notificationDTO);

        iNotificationDestinationService.updateNotificationDestinations(notification, notificationDTO.getNotificationDestinations());
        iFrequencyService.updateNotificationFrequencies(notification, notificationDTO.getFrequencies());

        notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    private String processNotificationAttachment(Notification notification, NotificationDTO notificationDTO) {
        if (notification.getFileName() == null && notificationDTO.getFileName() == null) {
            return null;
        }

        if (notificationDTO.getFileName() == null && notification.getFileName() != null) {
            if (notificationDTO.getBroadcastChannel().equals(BroadcastChannel.EMAIL) || notificationDTO.getBroadcastChannel().equals(BroadcastChannel.BOTH)){
                return  notification.getFileName();
            }
            deleteNotificationFile(notification.getFileName());
            return null;
        }

        if (!notificationDTO.getName().equals(notification.getName()) && notification.getFileName() != null) {
            deleteNotificationFile(notification.getFileName());
        }

        if (notificationDTO.getFileName() != null && notificationDTO.getFileBase64() != null) {
            reportFeignClient.uploadNotificationFile(
                new Base64FileDTO(
                    convertBase64(notificationDTO.getFileBase64()),
                    notificationDTO.getFileName(),
                    "notification"
                )
            );
            return notificationDTO.getFileName();
        }

        return notification.getFileName();
    }


    private void deleteNotificationFile(String fileName) {
        log.info("Request to delete notification file: {}", fileName);
        this.reportFeignClient.deleteOldNotificationFile(fileName, "notification");
    }

    private void deleteNotificationFile(UUID id) {
        log.info("Request to delete notification file by notification ID: {}", id);
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification with id not found"));
        if (notification.getFileName() != null){
            deleteNotificationFile(notification.getFileName());
        }
    }

    public static byte[] convertBase64(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    private Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
    }

    private void updateNotificationDetails(Notification notification, NotificationDTO notificationDTO) {
        notification.setName(notificationDTO.getName());
        notification.setDescription(notificationDTO.getDescription());
        notification.setBroadcastChannel(notificationDTO.getBroadcastChannel());
        notification.setDestinationType(notificationDTO.getDestinationType());
        notification.setTopic(notificationDTO.getTopic());
        notification.setFrequencyEnum(notificationDTO.getFrequencyEnum());
        notification.setIsFromTask(notificationDTO.getIsFromTask());
    }


    @Override
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationRequest> findOne(UUID id) {
        log.debug("Request to get Notification : {}", id);

        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if(notificationOptional.isPresent()) {

            Notification notification = notificationOptional.orElseThrow(() -> new NoSuchElementException("notification not present with id"+ id));

            NotificationRequest notificationRequest = iNotificationRequestMapper.toDto(notification);

            if(Boolean.FALSE.equals(notification.getIsFromTask())){

                Set<UUID> ids = notificationRequest.getNotificationDestinations().stream()
                    .map(NotificationDestinationDTO::getSenderId)
                    .collect(Collectors.toSet());
                if(notificationRequest.getDestinationType()!=null ) {
                    switch (notificationRequest.getDestinationType()) {
                        case USER:
                            getLabels(notificationRequest, ids, userFeignClient::getUser);
                            break;

                        case ROLE:
                            getLabels(notificationRequest, ids, userFeignClient::getRole);
                            break;
//DestinationTypeEnum.GROUP
                        case GROUP:
                            getLabels(notificationRequest, ids, userFeignClient::getGroup);
                            break;

                        default:
                            break;
                    }
                }

            }

            return Optional.of(notificationRequest);
        }

        return Optional.empty();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOneNotification(UUID id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }


    private void getLabels(
        NotificationRequest notificationRequest,
        Set<UUID> ids,
        Function<UUID, ResponseEntity<?>> fetchFunction
    ) {
        List<String> list = new ArrayList<>();

        for (UUID id : ids) {
            try {
                ResponseEntity<?> response = fetchFunction.apply(id);

                if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                    ObjectMapper objectMapper = new ObjectMapper();

                    Map<String, Object> responseBody = objectMapper.convertValue(response.getBody(), new TypeReference<Map<String, Object>>() {});


                    if (responseBody != null) {
                        if (responseBody.containsKey("firstName") && responseBody.containsKey("lastName")) {
                            String firstname = responseBody.get("firstName").toString();
                            String lastname = responseBody.get("lastName").toString();
                            list.add(firstname + " " + lastname);
                        } else if (responseBody.containsKey("label")) {
                            String label = responseBody.get("label").toString();
                            list.add(label);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while calling the service: ", e);
            }
        }

        notificationRequest.setExtractedLabel(String.join(", ", list));
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Notification : {}", id);

        //delete notifications destination
        deleteNotificationDestination(id);

        //delete frequencies
        deleteFrequencies(id);

        deleteNotificationFile(id);

        notificationRepository.deleteById(id);
    }

    private void deleteNotificationDestination(UUID id) {
        List<NotificationDestination> notificationDestinations = iNotificationDestinationRepository.findByNotificationIdAndDeletedFalse(id);
        notificationDestinations.forEach(notificationDestination -> {
            iNotificationDestinationService.delete(notificationDestination.getId());
        });
    }

    private void deleteFrequencies(UUID id) {
        List<Frequency> frequencies = iFrequencyRepository.findByNotificationIdAndDeletedFalse(id);
        frequencies.forEach(frequency -> {
            iFrequencyService.delete(frequency.getId());
        });
    }
}
