package com.pfe.dto;

import java.io.Serializable;
import java.util.Objects;

public class DmnActivityDecisionDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 5364604345665607878L;

    private String id;
    private String category;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;
    private String resourceName;
    private String decisionRequirementsDefinitionKey;
    private String decisionRequirementsDefinitionId;
    private Integer historyTimeToLive;

    public DmnActivityDecisionDefinitionDTO() {
    }

    public DmnActivityDecisionDefinitionDTO(String id, Integer historyTimeToLive, String decisionRequirementsDefinitionId, String decisionRequirementsDefinitionKey, String resourceName, String deploymentId, Integer version, String key, String name, String category) {
        this.id = id;
        this.historyTimeToLive = historyTimeToLive;
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
        this.resourceName = resourceName;
        this.deploymentId = deploymentId;
        this.version = version;
        this.key = key;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDecisionRequirementsDefinitionKey() {
        return decisionRequirementsDefinitionKey;
    }

    public void setDecisionRequirementsDefinitionKey(String decisionRequirementsDefinitionKey) {
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
    }

    public String getDecisionRequirementsDefinitionId() {
        return decisionRequirementsDefinitionId;
    }

    public void setDecisionRequirementsDefinitionId(String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }

    public Integer getHistoryTimeToLive() {
        return historyTimeToLive;
    }

    public void setHistoryTimeToLive(Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DmnActivityDecisionDefinitionDTO that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DmnActivityDecisionDefinitionDTO{" +
            "id='" + id + '\'' +
            ", category='" + category + '\'' +
            ", name='" + name + '\'' +
            ", key='" + key + '\'' +
            ", version=" + version +
            ", deploymentId='" + deploymentId + '\'' +
            ", resourceName='" + resourceName + '\'' +
            ", decisionRequirementsDefinitionKey='" + decisionRequirementsDefinitionKey + '\'' +
            ", decisionRequirementsDefinitionId='" + decisionRequirementsDefinitionId + '\'' +
            ", historyTimeToLive=" + historyTimeToLive +
            '}';
    }

}
