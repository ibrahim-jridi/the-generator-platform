package com.pfe.service.impl;

import com.pfe.domain.ReportTemplate;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.ConfigurationReportService;
import com.pfe.service.MinioReportService;
import com.pfe.service.dto.ConfigurationReportDTO;
import net.sf.jasperreports.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private MinioReportService minioService;

    @Mock
    private ReportTemplateRepository reportTemplateRepository;

    @Mock
    private ConfigurationReportService configurationReportService;


    private UUID templateId;
    private Map<String, Object> processVariables;
    private String instanceId;
    private String processName;
    private ReportTemplate reportTemplate;
    private ConfigurationReportDTO configurationReportDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        templateId = UUID.randomUUID();
        processVariables = new HashMap<>();
        processVariables.put("key1", "value1");
        processVariables.put("key2", "value2");
        instanceId = "instance123";
        processName = "TestProcess";

        reportTemplate = new ReportTemplate();
        reportTemplate.setId(templateId);
        reportTemplate.setType("demo");

        configurationReportDTO = new ConfigurationReportDTO();
        configurationReportDTO.setLogo("logo-safer.svg");
        configurationReportDTO.setLang("fr");
        configurationReportDTO.setAddress("123 Test Street");
        configurationReportDTO.setPostalCode("75001");
        configurationReportDTO.setPhone("0123456789");
        configurationReportDTO.setFax("0123456780");
        configurationReportDTO.setEmail("test@example.com");
        configurationReportDTO.setFooter("Test Footer");
    }

    @Test
    void testGenerateReport() throws Exception {
        ReportServiceImpl spyReportService = spy(reportService);

        when(reportTemplateRepository.findById(templateId)).thenReturn(Optional.of(reportTemplate));
        when(configurationReportService.findCurrentConfigReport()).thenReturn(configurationReportDTO);

        InputStream logoStream = this.getClass().getResourceAsStream("/config/report/logo-safer.svg"); // Ensure the path starts with '/'
        assertNotNull(logoStream, "Logo should exist in test resources");
        when(minioService.getLogoReport(configurationReportDTO.getLogo()))
            .thenReturn(logoStream.readAllBytes());

        String fileUrl = "file:/C:/bs2/bs_report_management/src/test/resources/config/report/demoReport.jrxml";
        when(minioService.getTemplateReportUrl(reportTemplate.getType() + ".jrxml"))
            .thenReturn(fileUrl);

        InputStream templateStream = new ByteArrayInputStream("Fake JRXML content".getBytes());
        doReturn(templateStream).when(spyReportService).getInputStreamFromURL(fileUrl); // Mock with spy

        try (MockedStatic<JasperCompileManager> compileManagerMock = mockStatic(JasperCompileManager.class);
             MockedStatic<JasperFillManager> fillManagerMock = mockStatic(JasperFillManager.class);
             MockedStatic<JasperExportManager> exportManagerMock = mockStatic(JasperExportManager.class)) {

            JasperReport jasperReport = mock(JasperReport.class);
            compileManagerMock.when(() -> JasperCompileManager.compileReport(templateStream)).thenReturn(jasperReport);

            JasperPrint jasperPrint = mock(JasperPrint.class);
            fillManagerMock.when(() -> JasperFillManager.fillReport(eq(jasperReport), anyMap(), any(JRDataSource.class)))
                .thenReturn(jasperPrint);

            byte[] pdfBytes = new byte[]{1, 2, 3};
            exportManagerMock.when(() -> JasperExportManager.exportReportToPdf(jasperPrint)).thenReturn(pdfBytes);

            spyReportService.generateReport(templateId, processVariables, instanceId, processName);

            verify(reportTemplateRepository).findById(templateId);
            verify(configurationReportService).findCurrentConfigReport();
            verify(minioService).getTemplateReportUrl(reportTemplate.getType() + ".jrxml");
            verify(minioService, atLeast(1)).getLogoReport("logo-safer.svg");
            verify(minioService).uploadFile(eq(processName), eq(instanceId), eq(pdfBytes), eq(reportTemplate.getType()));
        }
    }
}
