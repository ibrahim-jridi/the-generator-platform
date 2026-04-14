package com.pfe.service.impl;

import com.pfe.domain.ReportTemplate;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.ReportTemplateService;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.service.mapper.ReportTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link ReportTemplate}.
 */
@Service
@Transactional
public class ReportTemplateServiceImpl implements ReportTemplateService {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateServiceImpl.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    public ReportTemplateServiceImpl(ReportTemplateRepository reportTemplateRepository, ReportTemplateMapper reportTemplateMapper) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
    }

    @Override
    public ReportTemplateDTO save(ReportTemplateDTO reportTemplateDTO) {
        log.debug("Request to save ReportTemplate : {}", reportTemplateDTO);

        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(reportTemplateDTO);
        reportTemplate = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toDto(reportTemplate);
    }

    @Override
    public ReportTemplateDTO update(ReportTemplateDTO reportTemplateDTO) {
        log.debug("Request to update ReportTemplate : {}", reportTemplateDTO);
        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(reportTemplateDTO);
        reportTemplate = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toDto(reportTemplate);
    }

    @Override
    public Optional<ReportTemplateDTO> partialUpdate(ReportTemplateDTO reportTemplateDTO) {
        log.debug("Request to partially update ReportTemplate : {}", reportTemplateDTO);

        return reportTemplateRepository
            .findById(reportTemplateDTO.getId())
            .map(existingReportTemplate -> {
                reportTemplateMapper.partialUpdate(existingReportTemplate, reportTemplateDTO);

                return existingReportTemplate;
            })
            .map(reportTemplateRepository::save)
            .map(reportTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTemplateDTO> findOne(UUID id) {
        log.debug("Request to get ReportTemplate : {}", id);
        return reportTemplateRepository.findById(id).map(reportTemplateMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ReportTemplate : {}", id);
        reportTemplateRepository.deleteById(id);
    }

    @Override
    public  Optional<ReportTemplateDTO> findByType(String reportType){
        log.debug("Request to get ReportTemplate by Type: {}", reportType);
        return reportTemplateRepository.findByTypeAndDeletedFalse(reportType).map(reportTemplateMapper::toDto);
    }
}
