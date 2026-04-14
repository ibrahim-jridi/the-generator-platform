package com.pfe.service.impl;

import com.pfe.domain.NotificationPushed;
import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.feignServices.UserFeignClient;
import com.pfe.repository.INotificationPushedRepository;
import com.pfe.service.NotificationPushedService;
import com.pfe.service.dto.NotificationPushedDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.mapper.NotificationPushedMapper;

import com.pfe.web.rest.errors.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NotificationPushed}.
 */
@Service
@Transactional
public class NotificationPushedServiceImpl implements NotificationPushedService {

    private final Logger log = LoggerFactory.getLogger(NotificationPushedServiceImpl.class);

    private final INotificationPushedRepository notificationPushedRepository;

    private final NotificationPushedMapper notificationPushedMapper;

    private final UserFeignClient userFeignClient;

    public NotificationPushedServiceImpl(
        INotificationPushedRepository notificationPushedRepository, NotificationPushedMapper notificationPushedMapper, UserFeignClient userFeignClient
    ) {
        this.notificationPushedRepository = notificationPushedRepository;
        this.notificationPushedMapper = notificationPushedMapper;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public NotificationPushedDTO save(NotificationPushedDTO notificationPushedDTO) {
        log.debug("Request to save NotificationPushed : {}", notificationPushedDTO);
        NotificationPushed notificationPushed = notificationPushedMapper.toEntity(notificationPushedDTO);
        notificationPushed = notificationPushedRepository.save(notificationPushed);
        return notificationPushedMapper.toDto(notificationPushed);
    }

    @Override
    public NotificationPushedDTO update(NotificationPushedDTO notificationPushedDTO) {
        log.debug("Request to update NotificationPushed : {}", notificationPushedDTO);
        NotificationPushed notificationPushed = notificationPushedMapper.toEntity(notificationPushedDTO);
        notificationPushed = notificationPushedRepository.save(notificationPushed);
        return notificationPushedMapper.toDto(notificationPushed);
    }

    @Override
    public Optional<NotificationPushedDTO> partialUpdate(NotificationPushedDTO notificationPushedDTO) {
        log.debug("Request to partially update NotificationPushed : {}", notificationPushedDTO);

        return notificationPushedRepository
            .findById(notificationPushedDTO.getId())
            .map(existingNotificationPushed -> {
                notificationPushedMapper.partialUpdate(existingNotificationPushed, notificationPushedDTO);

                return existingNotificationPushed;
            })
            .map(notificationPushedRepository::save)
            .map(notificationPushedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationPushedDTO> findOne(UUID id) {
        log.debug("Request to get NotificationPushed : {}", id);
        return notificationPushedRepository.findById(id).map(notificationPushedMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete NotificationPushed : {}", id);
        notificationPushedRepository.deleteById(id);
    }

    @Override
    public List<NotificationPushedDTO> findByRecipient(UUID recipientId) {
        // Récupérer les informations de l'utilisateur
        UserDTO userDTO = userFeignClient.getUser(recipientId).getBody();
        if (userDTO == null) {
            throw new ValidationException("User not found for recipientId: " + recipientId);
        }

        // Récupérer les notifications
        List<NotificationPushedDTO> notificationPushedDTOS= notificationPushedMapper.toDto(notificationPushedRepository.findByRecipientIdAndEventNotification_Notification_BroadcastChannelNotAndDeletedFalse(recipientId, BroadcastChannel.EMAIL, Sort.by(Sort.Direction.DESC, "notificationDate")));

        // Mettre à jour le nom du destinataire pour chaque notification
        String recipientName = userDTO.getFirstName() + " " + userDTO.getLastName();
        notificationPushedDTOS.forEach(notification -> notification.setRecipientName(recipientName));

        return notificationPushedDTOS;
    }

    @Override
    public Page<NotificationPushedDTO> findByTaskId(String taskId, Pageable pageable) {
        // Fetch notifications for the task ID with pagination
        Page<NotificationPushedDTO> notificationsPage = notificationPushedRepository.findByTaskId(taskId, pageable);

        // Transform each notification to enrich it with recipient name
        return notificationsPage.map(notification -> {
            // Fetch user details for the recipient
            ResponseEntity<UserDTO> response = userFeignClient.getUser(notification.getRecipientId());
            UserDTO userDTO = response != null ? response.getBody() : null;

            if (userDTO == null) {
                throw new ValidationException("User not found for recipientId: " + notification.getRecipientId());
            }

            // Update recipient name for each notification
            String recipientName = userDTO.getFirstName() + " " + userDTO.getLastName();
            notification.setRecipientName(recipientName);
            return notification;
        });
    }

}
