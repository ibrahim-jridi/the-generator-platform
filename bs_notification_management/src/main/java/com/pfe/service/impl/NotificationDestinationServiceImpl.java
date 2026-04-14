package com.pfe.service.impl;

import com.pfe.domain.Notification;
import com.pfe.domain.NotificationDestination;
import com.pfe.repository.INotificationDestinationRepository;
import com.pfe.service.INotificationDestinationService;
import com.pfe.service.dto.NotificationDestinationDTO;
import com.pfe.service.mapper.INotificationDestinationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link NotificationDestination}.
 */
@Service
@Transactional
public class NotificationDestinationServiceImpl implements INotificationDestinationService {

    private final Logger log = LoggerFactory.getLogger(NotificationDestinationServiceImpl.class);

    private final INotificationDestinationRepository notificationDestinationRepository;

    private final INotificationDestinationMapper notificationDestinationMapper;

    public NotificationDestinationServiceImpl(
        INotificationDestinationRepository notificationDestinationRepository,
        INotificationDestinationMapper notificationDestinationMapper
    ) {
        this.notificationDestinationRepository = notificationDestinationRepository;
        this.notificationDestinationMapper = notificationDestinationMapper;
    }

    @Override
    public NotificationDestinationDTO save(NotificationDestinationDTO notificationDestinationDTO) {
        log.debug("Request to save NotificationDestination : {}", notificationDestinationDTO);
        NotificationDestination notificationDestination = notificationDestinationMapper.toEntity(notificationDestinationDTO);
        notificationDestination = notificationDestinationRepository.save(notificationDestination);
        return notificationDestinationMapper.toDto(notificationDestination);
    }

    @Override
    public NotificationDestinationDTO update(NotificationDestinationDTO notificationDestinationDTO) {
        log.debug("Request to update NotificationDestination : {}", notificationDestinationDTO);
        NotificationDestination notificationDestination = notificationDestinationMapper.toEntity(notificationDestinationDTO);
        notificationDestination = notificationDestinationRepository.save(notificationDestination);
        return notificationDestinationMapper.toDto(notificationDestination);
    }

    @Override
    public Optional<NotificationDestinationDTO> partialUpdate(NotificationDestinationDTO notificationDestinationDTO) {
        log.debug("Request to partially update NotificationDestination : {}", notificationDestinationDTO);

        return notificationDestinationRepository
            .findById(notificationDestinationDTO.getId())
            .map(existingNotificationDestination -> {
                notificationDestinationMapper.partialUpdate(existingNotificationDestination, notificationDestinationDTO);

                return existingNotificationDestination;
            })
            .map(notificationDestinationRepository::save)
            .map(notificationDestinationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDestinationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationDestinations");
        return notificationDestinationRepository.findAll(pageable).map(notificationDestinationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDestinationDTO> findOne(UUID id) {
        log.debug("Request to get NotificationDestination : {}", id);
        return notificationDestinationRepository.findById(id).map(notificationDestinationMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete NotificationDestination : {}", id);
        notificationDestinationRepository.deleteById(id);
    }

    @Override
    public void updateNotificationDestinations(Notification notification, Set<NotificationDestinationDTO> destinationDTOs) {
        if (destinationDTOs == null) {
            notification.getNotificationDestinations().clear();
            return;
        }
        
        Map<UUID, NotificationDestination> existingDestinations = notification.getNotificationDestinations().stream()
            .collect(Collectors.toMap(NotificationDestination::getId, Function.identity()));
        Set<NotificationDestination> updatedDestinations = new HashSet<>();

        for (NotificationDestinationDTO destinationDTO : destinationDTOs) {
            NotificationDestination destination = existingDestinations.get(destinationDTO.getId());
            if (destination == null) {
                destination = notificationDestinationMapper.toEntity(destinationDTO);
            } else {
                notificationDestinationMapper.partialUpdate(destination, destinationDTO);
            }
            destination.setNotification(notification);
            updatedDestinations.add(destination);
        }
        Set<UUID> oldNotificationDestinationIds = notification.getNotificationDestinations().stream()
            .map(NotificationDestination::getId)
            .collect(Collectors.toSet());

        notificationDestinationRepository.deleteoldNotificationDestinationByID(oldNotificationDestinationIds);


        notification.getNotificationDestinations().clear();
        notification.getNotificationDestinations().addAll(updatedDestinations);
    }
}
