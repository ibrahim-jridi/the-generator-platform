package com.pfe.dto;

import java.util.Objects;

public class ProcessDefinitionDTO {
    private String id;
    private String key;
    private String name;
    private Integer version;
    private String deploymentId;
    private String resourceName;
    private boolean suspensionState;
    private Boolean startableInTasklist;

    // Constructor
    public ProcessDefinitionDTO() {
    }

    public ProcessDefinitionDTO(String id, String key, String name, Integer version, String deploymentId, String resourceName, boolean suspensionState, Boolean startableInTasklist) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.version = version;
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.suspensionState = suspensionState;
        this.startableInTasklist = startableInTasklist;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(boolean suspensionState) {
        this.suspensionState = suspensionState;
    }

    public Boolean getStartableInTasklist() {
        return startableInTasklist;
    }

    public void setStartableInTasklist(Boolean startableInTasklist) {
        this.startableInTasklist = startableInTasklist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessDefinitionDTO that = (ProcessDefinitionDTO) o;
        return suspensionState == that.suspensionState && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProcessDefinitionDTO{" +
            "id='" + id + '\'' +
            ", key='" + key + '\'' +
            ", name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", deploymentId='" + deploymentId + '\'' +
            ", resourceName='" + resourceName + '\'' +
            ", suspensionState=" + suspensionState +
            ", startableInTasklist=" + startableInTasklist +
            '}';
    }
}
