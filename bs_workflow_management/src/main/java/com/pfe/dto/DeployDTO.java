package com.pfe.dto;

import java.util.Date;
import java.util.Objects;

public class DeployDTO {
    private String id;
    private String name;
    private Date deploymentTime;
    private String source;
    private String tenantId;
    private Integer version;

    public DeployDTO() {
    }

    public DeployDTO(String id, String name, Date deploymentTime, String source, String tenantId, Integer version) {
        this.id = id;
        this.name = name;
        this.deploymentTime = deploymentTime;
        this.source = source;
        this.tenantId = tenantId;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeployDTO deployDTO = (DeployDTO) o;
        return Objects.equals(getId(), deployDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

