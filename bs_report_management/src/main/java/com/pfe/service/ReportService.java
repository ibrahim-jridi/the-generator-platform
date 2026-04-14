package com.pfe.service;

import java.util.Map;
import java.util.UUID;

public interface ReportService {

    void generateReport(UUID templateId, Map<String, Object> processVariables, String instanceId, String processName) throws Exception;

}
