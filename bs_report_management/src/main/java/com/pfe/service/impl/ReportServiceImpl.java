package com.pfe.service.impl;

import com.pfe.domain.ReportTemplate;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.ConfigurationReportService;
import com.pfe.service.MinioReportService;
import com.pfe.service.ReportService;
import com.pfe.service.dto.ConfigurationReportDTO;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {


    private final MinioReportService minioService;

    private final ReportTemplateRepository reportTemplateRepository;

    private final ConfigurationReportService configurationReportService;

    public ReportServiceImpl(MinioReportService minioService, ReportTemplateRepository reportTemplateRepository, ConfigurationReportService configurationReportService) {
        this.minioService = minioService;
        this.reportTemplateRepository = reportTemplateRepository;
        this.configurationReportService = configurationReportService;
    }


    @Override
    public void generateReport(UUID templateId, Map<String, Object> processVariables, String instanceId, String processName) throws Exception {
        ReportTemplate reportTemplate = reportTemplateRepository.findById(templateId).orElseThrow();
        ConfigurationReportDTO configurationReport = configurationReportService.findCurrentConfigReport();
        InputStream inputStream = getInputStreamFromURL(minioService.getTemplateReportUrl(reportTemplate.getType() + ".jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        Map<String, Object> parameters = initializeParameters(configurationReport);
        processVariables.forEach((key, value) -> parameters.put(key, value.toString()));
        InputStream logoStream = new ByteArrayInputStream(minioService.getLogoReport(configurationReport.getLogo()));
        parameters.put("logo", logoStream);
        parameters.put("net.sf.jasperreports.awt.ignore.missing.font", "true");
        parameters.put("net.sf.jasperreports.default.font.name", "Arial");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        byte[] pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);
        minioService.uploadFile(processName, instanceId, pdfReport, reportTemplate.getType());
    }

    public InputStream getInputStreamFromURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream inputStream;

        // Check if the URL is a file URL
        if ("file".equals(url.getProtocol())) {
            inputStream = url.openStream(); // Use openStream for file URLs
        } else if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            inputStream = httpConn.getInputStream(); // For HTTP URLs, use HttpURLConnection
        } else {
            throw new IOException("Unsupported protocol: " + url.getProtocol());
        }

        if (inputStream == null) {
            throw new FileNotFoundException("Unable to open InputStream for URL: " + urlString);
        }

        return inputStream;
    }

    private Map<String, Object> initializeParameters(ConfigurationReportDTO configurationReportDTO) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        InputStream logoStream = new ByteArrayInputStream(minioService.getLogoReport(configurationReportDTO.getLogo()));
        parameters.put("logo", logoStream);
        if ("ar".equals(configurationReportDTO.getLang())) {
            configurationReportDTO.setLang("ar");
        } else {
            configurationReportDTO.setLang("fr");
        }
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
}
