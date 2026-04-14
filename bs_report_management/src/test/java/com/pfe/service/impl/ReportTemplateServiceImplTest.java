package com.pfe.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.pfe.domain.ReportTemplate;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.service.mapper.ReportTemplateMapper;
import com.pfe.validator.IReportValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;

class ReportTemplateServiceImplTest {

    @Mock
    private ReportTemplateRepository reportTemplateRepository;

    @Mock
    private ReportTemplateMapper reportTemplateMapper;

    @InjectMocks
    private ReportTemplateServiceImpl reportTemplateService;

    private ReportTemplate reportTemplate;
    private ReportTemplateDTO reportTemplateDTO;

    @Mock
    private IReportValidator reportValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID id = UUID.randomUUID();

        reportTemplate = new ReportTemplate();
        reportTemplate.setId(id);
        reportTemplate.setType("Test Type");

        reportTemplateDTO = new ReportTemplateDTO();
        reportTemplateDTO.setId(id);
        reportTemplateDTO.setType("Test Type");
    }

    @Test
    void save_ShouldSaveAndReturnReportTemplateDTO() {
        when(reportTemplateMapper.toEntity(reportTemplateDTO)).thenReturn(reportTemplate);
        when(reportTemplateRepository.save(reportTemplate)).thenReturn(reportTemplate);
        when(reportTemplateMapper.toDto(reportTemplate)).thenReturn(reportTemplateDTO);

        ReportTemplateDTO result = reportTemplateService.save(reportTemplateDTO);

        assertNotNull(result);
        assertEquals(reportTemplateDTO.getId(), result.getId());
        verify(reportTemplateRepository, times(1)).save(reportTemplate);
    }

    @Test
    void update_ShouldUpdateAndReturnReportTemplateDTO() {
        when(reportTemplateMapper.toEntity(reportTemplateDTO)).thenReturn(reportTemplate);
        when(reportTemplateRepository.save(reportTemplate)).thenReturn(reportTemplate);
        when(reportTemplateMapper.toDto(reportTemplate)).thenReturn(reportTemplateDTO);

        ReportTemplateDTO result = reportTemplateService.update(reportTemplateDTO);

        assertNotNull(result);
        assertEquals(reportTemplateDTO.getId(), result.getId());
        verify(reportTemplateRepository, times(1)).save(reportTemplate);
    }

    @Test
    void partialUpdate_ShouldPartiallyUpdateAndReturnReportTemplateDTO() {
        when(reportTemplateRepository.findById(reportTemplateDTO.getId())).thenReturn(Optional.of(reportTemplate));
        doNothing().when(reportTemplateMapper).partialUpdate(reportTemplate, reportTemplateDTO);
        when(reportTemplateRepository.save(reportTemplate)).thenReturn(reportTemplate);
        when(reportTemplateMapper.toDto(reportTemplate)).thenReturn(reportTemplateDTO);

        ReportTemplateDTO result = reportTemplateService.partialUpdate(reportTemplateDTO)
            .orElseThrow(() -> new RuntimeException("ReportTemplate not found"));

        assertEquals(reportTemplateDTO.getId(), result.getId(), "Returned ID does not match the expected value");
        verify(reportTemplateRepository, times(1)).save(reportTemplate);
    }

    @Test
    void findOne_ShouldReturnReportTemplateDTO() {
        when(reportTemplateRepository.findById(reportTemplateDTO.getId())).thenReturn(Optional.of(reportTemplate));
        when(reportTemplateMapper.toDto(reportTemplate)).thenReturn(reportTemplateDTO);

        ReportTemplateDTO result = reportTemplateService.findOne(reportTemplateDTO.getId())
            .orElseThrow(() -> new RuntimeException("ReportTemplate not found"));

        assertEquals(reportTemplateDTO.getId(), result.getId(), "Returned ID does not match the expected value");
    }

    @Test
    void delete_ShouldDeleteReportTemplate() {
        UUID id = reportTemplateDTO.getId();

        doNothing().when(reportTemplateRepository).deleteById(id);

        reportTemplateService.delete(id);

        verify(reportTemplateRepository, times(1)).deleteById(id);
    }

    @Test
    void findByType_ShouldReturnReportTemplateDTO() {
        String reportType = "Test Type";
        when(reportTemplateRepository.findByTypeAndDeletedFalse(reportType)).thenReturn(Optional.of(reportTemplate));
        when(reportTemplateMapper.toDto(reportTemplate)).thenReturn(reportTemplateDTO);

        ReportTemplateDTO result = reportTemplateService.findByType(reportType)
            .orElseThrow(() -> new RuntimeException("ReportTemplate not found"));

        assertEquals(reportTemplateDTO.getType(), result.getType(), "Returned type does not match the expected value");
    }
}

