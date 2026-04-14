package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


/**
 * A Group.
 */
@Entity
@Table(name = "bs_group")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Group extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = -4117968273608723400L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "label", length = 25)
    @Size(max = 25)
    private String label;

    @Column(name = "description", length = 150)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"createdBy", "parent",
        "users"}, allowSetters = true)
    private Group parent;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Group() {
    }

    public Group(UUID id, String label, String description, Boolean isActive, Group parent) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.isActive = isActive;
        this.parent = parent;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Group id(UUID id) {
        this.setId(id);
        return this;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Group isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Group label(String label) {
        this.setLabel(label);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group description(String description) {
        this.setDescription(description);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Group getParent() {
        return this.parent;
    }

    public void setParent(Group group) {
        this.parent = group;
    }

    public Group parent(Group group) {
        this.setParent(group);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group that = (Group) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
