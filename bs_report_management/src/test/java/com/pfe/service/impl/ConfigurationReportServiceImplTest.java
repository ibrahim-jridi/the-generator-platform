package com.pfe.service.impl;

import com.pfe.domain.ConfigurationReport;
import com.pfe.repository.ConfigurationReportRepository;
import com.pfe.service.MinioReportService;
import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.service.mapper.ConfigurationReportMapper;
import com.pfe.validator.IConfigurationReportValidator;
import net.sf.jasperreports.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationReportServiceImplTest {

    @InjectMocks
    private ConfigurationReportServiceImpl configurationReportService;

    @Mock
    private ConfigurationReportRepository configurationReportRepository;

    @Mock
    private ConfigurationReportMapper configurationReportMapper;

    @Mock
    private MinioReportService minioService;


    @Mock
    private ResourceBundle resourceBundle;

    @Mock
    private IConfigurationReportValidator configurationReportValidator;


    private ConfigurationReportDTO configurationReportDTO;
    private ConfigurationReport configurationReport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configurationReportDTO = new ConfigurationReportDTO();
        configurationReportDTO.setId(UUID.randomUUID());
        configurationReportDTO.setLogo("logo_Safer.svg");
        configurationReportDTO.setLang("en");
        configurationReportDTO.setAddress("123 Test St");
        configurationReportDTO.setPostalCode("12345");
        configurationReportDTO.setPhone("123-456-7890");
        configurationReportDTO.setFax("123-456-7891");
        configurationReportDTO.setEmail("test@example.com");
        configurationReportDTO.setFooter("Test Footer");

        configurationReport = new ConfigurationReport();
        configurationReport.setId(configurationReportDTO.getId());
    }

    @Test
    void testSave() {
        when(configurationReportMapper.toEntity(configurationReportDTO)).thenReturn(configurationReport);
        when(configurationReportRepository.save(configurationReport)).thenReturn(configurationReport);
        when(configurationReportMapper.toDto(configurationReport)).thenReturn(configurationReportDTO);

        ConfigurationReportDTO result = configurationReportService.save(configurationReportDTO);

        assertNotNull(result);
        assertEquals(configurationReportDTO.getId(), result.getId());
        verify(configurationReportValidator).beforeSave(configurationReportDTO);
        verify(configurationReportRepository).save(configurationReport);
    }

    @Test
    void testUpdate() {
        when(configurationReportMapper.toEntity(configurationReportDTO)).thenReturn(configurationReport);
        when(configurationReportRepository.save(configurationReport)).thenReturn(configurationReport);
        when(configurationReportMapper.toDto(configurationReport)).thenReturn(configurationReportDTO);

        ConfigurationReportDTO result = configurationReportService.update(configurationReportDTO);

        assertNotNull(result);
        assertEquals(configurationReportDTO.getId(), result.getId());
        verify(configurationReportValidator).beforeSave(configurationReportDTO);
        verify(configurationReportRepository).save(configurationReport);
    }

    @Test
    void testFindOne() {
        UUID testId = UUID.randomUUID();
        configurationReportDTO.setId(testId);
        when(configurationReportRepository.findById(testId))
            .thenReturn(Optional.of(configurationReport));
        when(configurationReportMapper.toDto(configurationReport))
            .thenReturn(configurationReportDTO);

        ConfigurationReportDTO result = configurationReportService.findOne(testId)
            .orElseThrow(() -> new RuntimeException("ConfigurationReport not found"));

        assertEquals(testId, result.getId(), "Returned ID does not match the expected value");
        verify(configurationReportRepository).findById(testId);
        verify(configurationReportMapper).toDto(configurationReport);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        configurationReportService.delete(id);

        verify(configurationReportRepository).deleteById(id);
    }

    @Test
    void testGenerateReport() throws Exception {
        ConfigurationReportDTO configurationReportDTO = new ConfigurationReportDTO();
        configurationReportDTO.setLogo("logo-safer.svg");
        configurationReportDTO.setLang("fr");
        configurationReportDTO.setAddress("123 Test Street");
        configurationReportDTO.setPostalCode("75001");
        configurationReportDTO.setPhone("0123456789");
        configurationReportDTO.setFax("0123456780");
        configurationReportDTO.setEmail("test@example.com");
        configurationReportDTO.setFooter("Test Footer");

        InputStream logoStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        when(minioService.getLogoReport(configurationReportDTO.getLogo())).thenReturn(new byte[]{1, 2, 3});

        try (MockedStatic<ResourceBundle> resourceBundleMock = mockStatic(ResourceBundle.class)) {
            ResourceBundle resourceBundle = mock(ResourceBundle.class);
            resourceBundleMock.when(() -> ResourceBundle.getBundle("i18n/messages", new Locale("fr"))).thenReturn(resourceBundle);

            when(resourceBundle.getString("alignment")).thenReturn("center");
            when(resourceBundle.getString("address")).thenReturn("Address: {0}, {1}");
            when(resourceBundle.getString("phone")).thenReturn("Phone: {0}, Fax: {1}");
            when(resourceBundle.getString("email")).thenReturn("Email: {0}");
            when(resourceBundle.getString("details")).thenReturn("Details: {0}");
            when(resourceBundle.getString("title_report")).thenReturn("Test Report");
            when(resourceBundle.getString("footer")).thenReturn("Footer: {0}");
            when(resourceBundle.getString("textField")).thenReturn("Text Field");

            try (MockedStatic<JasperCompileManager> compileManagerMock = mockStatic(JasperCompileManager.class);
                 MockedStatic<JasperFillManager> fillManagerMock = mockStatic(JasperFillManager.class);
                 MockedStatic<JasperExportManager> exportManagerMock = mockStatic(JasperExportManager.class)) {

                JasperReport jasperReport = mock(JasperReport.class);
                compileManagerMock.when(() -> JasperCompileManager.compileReport(any(InputStream.class)))
                    .thenReturn(jasperReport);

                JasperPrint jasperPrint = mock(JasperPrint.class);
                fillManagerMock.when(() -> JasperFillManager.fillReport(eq(jasperReport), any(Map.class), any(JREmptyDataSource.class)))
                    .thenReturn(jasperPrint);

                byte[] pdfBytes = new byte[]{1, 2, 3};
                exportManagerMock.when(() -> JasperExportManager.exportReportToPdf(jasperPrint)).thenReturn(pdfBytes);

                byte[] result = configurationReportService.generateReport(configurationReportDTO);

                verify(minioService).getLogoReport(configurationReportDTO.getLogo());
                verify(resourceBundle).getString("alignment");
                verify(resourceBundle).getString("address");
                verify(resourceBundle).getString("phone");
                verify(resourceBundle).getString("email");
                verify(resourceBundle).getString("details");
                verify(resourceBundle).getString("title_report");
                verify(resourceBundle).getString("footer");
                verify(resourceBundle).getString("textField");

                assertNotNull(result);
                assertEquals(3, result.length);
            }
        }
    }
}
