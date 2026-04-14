package com.pfe.dto;

import java.util.List;
import java.util.Map;

public class AiBpmnGenerateResponseDTO {

  private String sessionId;
  private String xml;
  private boolean valid;
  private List<String> errors;
  private List<String> warnings;
  private Map<String, Object> qualityAnalysis;
  private Map<String, Object> complexityAnalysis;
  private String message;

  public String getSessionId() { return sessionId; }
  public void setSessionId(String sessionId) { this.sessionId = sessionId; }

  public String getXml() { return xml; }
  public void setXml(String xml) { this.xml = xml; }

  public boolean isValid() { return valid; }
  public void setValid(boolean valid) { this.valid = valid; }

  public List<String> getErrors() { return errors; }
  public void setErrors(List<String> errors) { this.errors = errors; }

  public List<String> getWarnings() { return warnings; }
  public void setWarnings(List<String> warnings) { this.warnings = warnings; }

  public Map<String, Object> getQualityAnalysis() { return qualityAnalysis; }
  public void setQualityAnalysis(Map<String, Object> qualityAnalysis) { this.qualityAnalysis = qualityAnalysis; }

  public Map<String, Object> getComplexityAnalysis() { return complexityAnalysis; }
  public void setComplexityAnalysis(Map<String, Object> complexityAnalysis) { this.complexityAnalysis = complexityAnalysis; }

  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
}