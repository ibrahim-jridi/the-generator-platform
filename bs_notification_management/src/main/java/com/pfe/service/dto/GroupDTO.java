package com.pfe.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class GroupDTO implements Serializable {

    private static final long serialVersionUID = 2047485755034343886L;

    private UUID id;

    @Size(max = 25)
    @NotNull(message = "LABEL_REQUIRED")
    @Pattern(regexp = "^[a-zA-Z_-]+$", message = "INVALID_PATTERN")
    private String label;

    @Size(max = 150)
    private String description;

    private Boolean isActive;

    private GroupDTO parent;

    private int numberOfUsers;

    private UUID createdBy;

    private Instant createdDate;

    public GroupDTO() {
    }

    public GroupDTO(UUID id, String label, String description, GroupDTO parent,
                    int numberOfUsers, Boolean isActive, UUID createdBy, Instant createdDate) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.parent = parent;
        this.numberOfUsers = numberOfUsers;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getActive() {
        return this.isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupDTO getParent() {
        return this.parent;
    }

    public void setParent(GroupDTO parent) {
        this.parent = parent;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean active) {
        this.isActive = active;
    }

    public int getNumberOfUsers() {
        return this.numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupDTO groupDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupDTO{" +
            "id='" + getId() + "'" +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
