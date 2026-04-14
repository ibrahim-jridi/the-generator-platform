package com.pfe.service.dto;

import com.pfe.domain.DesignationsList;
import com.pfe.domain.Role;
import com.pfe.domain.User;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class DesignationsListDTO implements Serializable  {
    private static final long serialVersionUID = 2518410547295431671L;
    private UUID id;
    private UUID keycloackId;
    private User pmUser;
    private User designatedUser;
    private Role role;
    private User laboratoryUser;

    public DesignationsListDTO() {}

    public DesignationsListDTO(UUID id, User pmUser, User designatedUser, Role role,UUID keycloackId, User laboratoryUser ) {
        this.id = id;
        this.pmUser = pmUser;
        this.designatedUser = designatedUser;
        this.role = role;
        this.keycloackId = keycloackId;
        this.laboratoryUser=laboratoryUser;
    }

    public DesignationsListDTO(DesignationsList designationsList) {
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getPmUser() { return pmUser; }
    public void setPmUser(User pmUser) { this.pmUser = pmUser; }
    public User getDesignatedUser() { return designatedUser; }
    public void setDesignatedUser(User designatedUser) { this.designatedUser = designatedUser; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UUID getKeycloackId() {
        return keycloackId;
    }

    public void setKeycloackId(UUID keycloackId) {
        this.keycloackId = keycloackId;
    }
    public User getLaboratoryUser() {
        return laboratoryUser;
    }

    public void setLaboratoryUser(User laboratoryUser) {
        this.laboratoryUser = laboratoryUser;
    }

    @Override
    public String toString() {
        return "DesignationsListDTO{" +
            "id=" + id +
            ", pmUserId=" + pmUser +
            ", designatedUserId=" + designatedUser +
            ", roleId=" + role +
            ", keycloackId="+ keycloackId +
            ", laboratoryUser=" + laboratoryUser +
            '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DesignationsListDTO designationsListDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, designationsListDTO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
