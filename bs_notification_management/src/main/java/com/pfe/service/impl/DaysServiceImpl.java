package com.pfe.service.impl;

import com.pfe.domain.Days;
import com.pfe.repository.IDaysRepository;
import com.pfe.service.IDaysService;
import com.pfe.service.dto.DaysDTO;
import com.pfe.service.mapper.IDaysMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Days}.
 */
@Service
@Transactional
public class DaysServiceImpl implements IDaysService {

    private final Logger log = LoggerFactory.getLogger(DaysServiceImpl.class);

    private final IDaysRepository daysRepository;

    private final IDaysMapper daysMapper;

    public DaysServiceImpl(IDaysRepository daysRepository, IDaysMapper daysMapper) {
        this.daysRepository = daysRepository;
        this.daysMapper = daysMapper;
    }

    @Override
    public DaysDTO save(DaysDTO daysDTO) {
        log.debug("Request to save Days : {}", daysDTO);
        Days days = daysMapper.toEntity(daysDTO);
        days = daysRepository.save(days);
        return daysMapper.toDto(days);
    }

    @Override
    public DaysDTO update(DaysDTO daysDTO) {
        log.debug("Request to update Days : {}", daysDTO);
        Days days = daysMapper.toEntity(daysDTO);
        days = daysRepository.save(days);
        return daysMapper.toDto(days);
    }

    @Override
    public Optional<DaysDTO> partialUpdate(DaysDTO daysDTO) {
        log.debug("Request to partially update Days : {}", daysDTO);

        return daysRepository
            .findById(daysDTO.getId())
            .map(existingDays -> {
                daysMapper.partialUpdate(existingDays, daysDTO);

                return existingDays;
            })
            .map(daysRepository::save)
            .map(daysMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DaysDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Days");
        return daysRepository.findAll(pageable).map(daysMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DaysDTO> findOne(UUID id) {
        log.debug("Request to get Days : {}", id);
        return daysRepository.findById(id).map(daysMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Days : {}", id);
        daysRepository.deleteById(id);
    }
}
