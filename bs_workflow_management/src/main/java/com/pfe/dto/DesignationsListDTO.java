package com.pfe.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class DesignationsListDTO implements Serializable  {
    private static final long serialVersionUID = 2518410547295431671L;
    private UUID id;
    private UserDTO pmUser;
    private UserDTO designatedUser;
    private RoleDTO role;

    public DesignationsListDTO() {}

    public DesignationsListDTO(UUID id, UserDTO pmUser, UserDTO designatedUser, RoleDTO role) {
        this.id = id;
        this.pmUser = pmUser;
        this.designatedUser = designatedUser;
        this.role = role;
    }

    public DesignationsListDTO(DesignationsListDTO designationsList) {
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UserDTO getPmUser() { return pmUser; }
    public void setPmUser(UserDTO pmUser) { this.pmUser = pmUser; }
    public UserDTO getDesignatedUser() { return designatedUser; }
    public void setDesignatedUser(UserDTO designatedUser) { this.designatedUser = designatedUser; }
    public RoleDTO getRole() { return role; }
    public void setRole(RoleDTO role) { this.role = role; }
    @Override
    public String toString() {
        return "DesignationsListDTO{" +
            "id=" + id +
            ", pmUserId=" + pmUser +
            ", designatedUserId=" + designatedUser +
            ", roleId=" + role +
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
