package com.pfe.service.dto.request;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AssignRoleApplication implements Serializable {

    private static final long serialVersionUID = -9061109062276828208L;

    private UUID keycloakId;
    private String ectdRole;

    private List<String> applicationsId;

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getEctdRole() {
        return ectdRole;
    }

    public void setEctdRole(String ectdRole) {
        this.ectdRole = ectdRole;
    }

    public List<String> getApplicationsId() {
        return applicationsId;
    }

    public void setApplicationsId(List<String> applicationsId) {
        this.applicationsId = applicationsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssignRoleApplication that = (AssignRoleApplication) o;
        return Objects.equals(keycloakId, that.keycloakId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keycloakId);
    }

    @Override
    public String toString() {
        return "AssignRoleApplication{" +
            "keycloakId=" + keycloakId +
            ", ectdRole='" + ectdRole + '\'' +
            ", applicationsId=" + applicationsId +
            '}';
    }
}
