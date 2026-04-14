package com.pfe.service.impl;

import com.pfe.Validator.IFrequencyValidator;
import com.pfe.domain.Days;
import com.pfe.domain.Frequency;
import com.pfe.domain.Notification;
import com.pfe.repository.IFrequencyRepository;
import com.pfe.service.IFrequencyService;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.mapper.IDaysMapper;
import com.pfe.service.mapper.IFrequencyMapper;
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
 * Service Implementation for managing {@link Frequency}.
 */
@Service
@Transactional
public class FrequencyServiceImpl implements IFrequencyService {

    private final Logger log = LoggerFactory.getLogger(FrequencyServiceImpl.class);

    private final IFrequencyRepository frequencyRepository;

    private final IFrequencyMapper frequencyMapper;

    private final IFrequencyValidator iFrequencyValidator;

    private final IDaysMapper iDaysMapper;

    public FrequencyServiceImpl(IFrequencyRepository frequencyRepository, IFrequencyMapper frequencyMapper, IFrequencyValidator iFrequencyValidator, IDaysMapper iDaysMapper) {
        this.frequencyRepository = frequencyRepository;
        this.frequencyMapper = frequencyMapper;
        this.iFrequencyValidator = iFrequencyValidator;
        this.iDaysMapper = iDaysMapper;
    }

    @Override
    public FrequencyDTO save(FrequencyDTO frequencyDTO) {
        log.debug("Request to save Frequency : {}", frequencyDTO);
        this.iFrequencyValidator.beforeSave(frequencyDTO);
        Frequency frequency = frequencyMapper.toEntity(frequencyDTO);
        frequency = frequencyRepository.save(frequency);
        return frequencyMapper.toDto(frequency);
    }

    @Override
    public FrequencyDTO update(FrequencyDTO frequencyDTO) {
        log.debug("Request to update Frequency : {}", frequencyDTO);
        Frequency frequency = frequencyMapper.toEntity(frequencyDTO);
        frequency = frequencyRepository.save(frequency);
        return frequencyMapper.toDto(frequency);
    }

    @Override
    public Optional<FrequencyDTO> partialUpdate(FrequencyDTO frequencyDTO) {
        log.debug("Request to partially update Frequency : {}", frequencyDTO);

        return frequencyRepository
            .findById(frequencyDTO.getId())
            .map(existingFrequency -> {
                frequencyMapper.partialUpdate(existingFrequency, frequencyDTO);

                return existingFrequency;
            })
            .map(frequencyRepository::save)
            .map(frequencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrequencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frequencies");
        return frequencyRepository.findAll(pageable).map(frequencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrequencyDTO> findOne(UUID id) {
        log.debug("Request to get Frequency : {}", id);
        return frequencyRepository.findById(id).map(frequencyMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Frequency : {}", id);
        frequencyRepository.deleteById(id);
    }

    @Override
    public void updateNotificationFrequencies(Notification notification, Set<FrequencyDTO> frequencyDTOs) {
        if (frequencyDTOs == null) {
            notification.getFrequencies().clear();
            return;
        }

        Map<UUID, Frequency> existingFrequencies = notification.getFrequencies().stream()
            .collect(Collectors.toMap(Frequency::getId, Function.identity()));
        Set<Frequency> updatedFrequencies = new HashSet<>();

        for (FrequencyDTO frequencyDTO : frequencyDTOs) {
            Frequency frequency = existingFrequencies.get(frequencyDTO.getId());
            if (frequency == null) {
                frequency = frequencyMapper.toEntity(frequencyDTO);
            } else {
                frequencyMapper.partialUpdateWithNull(frequency, frequencyDTO);
            }
            frequency.setNotification(notification);

            if (frequencyDTO.getDays() != null) {
                Set<Days> daysSet = frequencyDTO.getDays().stream()
                    .map(iDaysMapper::toEntity)
                    .collect(Collectors.toSet());
                frequency.setDays(daysSet);
            }

            updatedFrequencies.add(frequency);
        }

        Set<UUID> oldFrequencies = notification.getFrequencies().stream()
            .filter(freq -> !updatedFrequencies.contains(freq))
            .map(Frequency :: getId)
            .collect(Collectors.toSet());
        frequencyRepository.deleteOldNotificationFrequenciesByID(oldFrequencies);

        notification.getFrequencies().clear();
        notification.getFrequencies().addAll(updatedFrequencies);
    }
}
