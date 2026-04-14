package com.pfe.dto;

import java.util.List;
import java.util.Map;

public class AiBpmnRefineRequestDTO {

  private String sessionId;
  private String refinementPrompt;
  private String currentXml;

  /** Full conversation history as list of {role, content} maps */
  private List<Map<String, String>> conversationHistory;

  private String model;

  public String getSessionId() { return sessionId; }
  public void setSessionId(String sessionId) { this.sessionId = sessionId; }

  public String getRefinementPrompt() { return refinementPrompt; }
  public void setRefinementPrompt(String refinementPrompt) { this.refinementPrompt = refinementPrompt; }

  public String getCurrentXml() { return currentXml; }
  public void setCurrentXml(String currentXml) { this.currentXml = currentXml; }

  public List<Map<String, String>> getConversationHistory() { return conversationHistory; }
  public void setConversationHistory(List<Map<String, String>> conversationHistory) { this.conversationHistory = conversationHistory; }

  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }
}
