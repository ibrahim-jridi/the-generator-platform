package com.pfe.dto.request;

import org.apache.commons.text.translate.UnicodeUnescaper;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class StartProcessRequestDTO implements Serializable {

  private static final long serialVersionUID = 5295952036947090538L;

  private String processId;
  private Map<String, String> variables;
  private String processInstanceId;

  private String processDefinitionKey;

  private String userId;


    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    private String processKey;


  public StartProcessRequestDTO() {
  }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public StartProcessRequestDTO(String processId, Map<String, String> variables, String processInstanceId, String processDefinitionKey, String userId) {
        this.processId = processId;
        this.variables = variables;
        this.processInstanceId = processInstanceId;
        this.processDefinitionKey = processDefinitionKey;
        this.userId = userId;
    }

  public Map<String, String> getVariables() {
    return variables;
  }


  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }

  public String getProcessId() {
    return processId;
  }

    public String getProcessInstanceId() {
    return processInstanceId;
  }



    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "StartProcessRequestDTO{" +
            "processId='" + processId + '\'' +
            ", variables=" + variables +
            ", processInstanceId='" + processInstanceId + '\'' +
            ", processDefinitionKey='" + processDefinitionKey + '\'' +
            ", userId=" + userId +
            '}';
    }
}

