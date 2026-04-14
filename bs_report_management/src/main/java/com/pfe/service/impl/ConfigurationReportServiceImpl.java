package com.pfe.service.impl;

import com.pfe.domain.ConfigurationReport;
import com.pfe.repository.ConfigurationReportRepository;
import com.pfe.service.ConfigurationReportService;
import com.pfe.service.MinioReportService;
import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.service.mapper.ConfigurationReportMapper;
import com.pfe.validator.IConfigurationReportValidator;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * Service Implementation for managing {@link ConfigurationReport}.
 */
@Service
@Transactional
public class ConfigurationReportServiceImpl implements ConfigurationReportService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationReportServiceImpl.class);

    private final ConfigurationReportRepository configurationReportRepository;

    private final ConfigurationReportMapper configurationReportMapper;

    private final MinioReportService minioService;

    private final IConfigurationReportValidator configurationReportValidator;

    public ConfigurationReportServiceImpl(
        ConfigurationReportRepository configurationReportRepository,
        ConfigurationReportMapper configurationReportMapper, MinioReportService minioService,
        IConfigurationReportValidator configurationReportValidator) {
        this.configurationReportRepository = configurationReportRepository;
        this.configurationReportMapper = configurationReportMapper;
        this.minioService = minioService;
        this.configurationReportValidator = configurationReportValidator;
    }

    @Override
    public ConfigurationReportDTO save(ConfigurationReportDTO configurationReportDTO) {
        log.debug("Request to save ConfigurationReport : {}", configurationReportDTO);
        configurationReportValidator.beforeSave(configurationReportDTO);
        ConfigurationReport configurationReport = configurationReportMapper.toEntity(configurationReportDTO);
        configurationReport = configurationReportRepository.save(configurationReport);
        return configurationReportMapper.toDto(configurationReport);
    }

    @Override
    @Transactional
    public ConfigurationReportDTO update(ConfigurationReportDTO configurationReportDTO) {
        log.debug("Request to update ConfigurationReport : {}", configurationReportDTO);
        configurationReportValidator.beforeSave(configurationReportDTO);
        ConfigurationReport configurationReport = configurationReportMapper.toEntity(configurationReportDTO);
        configurationReport = configurationReportRepository.save(configurationReport);
        return configurationReportMapper.toDto(configurationReport);
    }

    @Override
    public Optional<ConfigurationReportDTO> partialUpdate(ConfigurationReportDTO configurationReportDTO) {
        log.debug("Request to partially update ConfigurationReport : {}", configurationReportDTO);

        return configurationReportRepository
            .findById(configurationReportDTO.getId())
            .map(existingConfigurationReport -> {
                configurationReportMapper.partialUpdate(existingConfigurationReport, configurationReportDTO);

                return existingConfigurationReport;
            })
            .map(configurationReportRepository::save)
            .map(configurationReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigurationReportDTO> findOne(UUID id) {
        log.debug("Request to get ConfigurationReport : {}", id);
        return configurationReportRepository.findById(id).map(configurationReportMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ConfigurationReport : {}", id);
        configurationReportRepository.deleteById(id);
    }


    @Override
    public byte[] generateReport(ConfigurationReportDTO configurationReportDTO) throws Exception {
        // Créer une instance de JasperReportsContext
        JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

        // Enregistrer la police de caractères dans le contexte de JasperReports
        jasperReportsContext.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        jasperReportsContext.setProperty("net.sf.jasperreports.default.font.name", "Arial");

        InputStream inputStream = getClass().getResourceAsStream("/config/report/demoReport.jrxml");

        try {
            // Compile the template
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //Create parameters Report
            Map<String, Object> parameters =  initializeParameters(configurationReportDTO);

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } finally {
            inputStream.close();
        }
    }

    private Map<String, Object> initializeParameters(ConfigurationReportDTO configurationReportDTO) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        InputStream logoStream = new ByteArrayInputStream(minioService.getLogoReport(configurationReportDTO.getLogo()));
        parameters.put("logo", logoStream);

        // Charger le fichier de ressources à partir du dossier i18n
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages", new Locale(configurationReportDTO.getLang()));
        parameters.put("alignment", resourceBundle.getString("alignment"));
        parameters.put("address", MessageFormat.format(resourceBundle.getString("address"), configurationReportDTO.getAddress(), configurationReportDTO.getPostalCode()));
        parameters.put("phone", MessageFormat.format(resourceBundle.getString("phone"), configurationReportDTO.getPhone(), configurationReportDTO.getFax()));
        parameters.put("email", MessageFormat.format(resourceBundle.getString("email"), configurationReportDTO.getEmail()));
        parameters.put("details", MessageFormat.format(resourceBundle.getString("details"), configurationReportDTO.getAddress()));
        parameters.put("title_report", resourceBundle.getString("title_report"));
        parameters.put("footer", MessageFormat.format(resourceBundle.getString("footer"), configurationReportDTO.getFooter()));
        parameters.put("textField", resourceBundle.getString("textField"));

        return parameters;
    }
    @Override
    public ConfigurationReportDTO findCurrentConfigReport() {
        log.debug("Request to get ConfigurationReport");
        List<ConfigurationReport> configurations = configurationReportRepository.findAll();

        if (configurations == null || configurations.isEmpty()) {
            log.debug("No ConfigurationReport found in the repository");
            return null;
        }

        ConfigurationReport configurationReport = configurations.get(0);
        return configurationReportMapper.toDto(configurationReport);
    }
}
