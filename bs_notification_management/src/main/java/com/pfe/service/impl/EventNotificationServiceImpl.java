package com.pfe.service.impl;

import com.pfe.domain.EventNotification;
import com.pfe.domain.enums.Status;
import com.pfe.repository.EventNotificationRepository;
import com.pfe.service.EventNotificationService;
import com.pfe.service.dto.EventNotificationDTO;
import com.pfe.service.mapper.EventNotificationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventNotification}.
 */
@Service
@Transactional
public class EventNotificationServiceImpl implements EventNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EventNotificationServiceImpl.class);

    private final EventNotificationRepository eventNotificationRepository;

    private final EventNotificationMapper eventNotificationMapper;

    public EventNotificationServiceImpl(
        EventNotificationRepository eventNotificationRepository,
        EventNotificationMapper eventNotificationMapper
    ) {
        this.eventNotificationRepository = eventNotificationRepository;
        this.eventNotificationMapper = eventNotificationMapper;
    }

    @Override
    public EventNotificationDTO save(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to save EventNotification : {}", eventNotificationDTO);
        EventNotification eventNotification = eventNotificationMapper.toEntity(eventNotificationDTO);
        eventNotification = eventNotificationRepository.save(eventNotification);
        return eventNotificationMapper.toDto(eventNotification);
    }

    @Override
    public EventNotificationDTO saveAndFlushEventNotification(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to save EventNotification : {}", eventNotificationDTO);
        EventNotification eventNotification = eventNotificationMapper.toEntity(eventNotificationDTO);
        eventNotification = eventNotificationRepository.saveAndFlush(eventNotification);
        return eventNotificationMapper.toDto(eventNotification);
    }

    @Override
    public EventNotificationDTO update(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to update EventNotification : {}", eventNotificationDTO);
        EventNotification eventNotification = eventNotificationMapper.toEntity(eventNotificationDTO);
        eventNotification = eventNotificationRepository.save(eventNotification);
        return eventNotificationMapper.toDto(eventNotification);
    }

    @Override
    public Optional<EventNotificationDTO> partialUpdate(EventNotificationDTO eventNotificationDTO) {
        LOG.debug("Request to partially update EventNotification : {}", eventNotificationDTO);

        return eventNotificationRepository
            .findById(eventNotificationDTO.getId())
            .map(existingEventNotification -> {
                eventNotificationMapper.partialUpdate(existingEventNotification, eventNotificationDTO);

                return existingEventNotification;
            })
            .map(eventNotificationRepository::save)
            .map(eventNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventNotificationDTO> findOne(UUID id) {
        LOG.debug("Request to get EventNotification : {}", id);
        return eventNotificationRepository.findById(id).map(eventNotificationMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete EventNotification : {}", id);
        eventNotificationRepository.deleteById(id);
    }

    @Override
    public Optional<EventNotificationDTO> findByNotificationIdAndTaskId(UUID notificationId,String taskId){
        LOG.debug("Request to get EventNotification By notificationId {} and taskId: {}", notificationId, taskId);
        return eventNotificationRepository.findByNotificationIdAndTaskIdAndDeletedFalse(notificationId,taskId).map(eventNotificationMapper::toDto);
    }

    @Override
    public List<EventNotificationDTO> findByStatus(Status status){
        LOG.debug("Request to get EventNotification By Status: {}", status);
        return eventNotificationMapper.toDto(eventNotificationRepository.findByStatusAndDeletedFalse(status));
    }
}
