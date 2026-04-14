package com.pfe.domain;

import com.pfe.domain.enumeration.RolesType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Role.
 */
@Entity
@Table(name = "bs_role")
public class Role extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = 1858668004737527699L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "label", length = 25, nullable = false, unique = true)
  private String label;


  @Column(name = "description", length = 150)
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "bs_rel_role_authority",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "authority_id")
  )
  private Set<Authority> authorities = new HashSet<>();

  @Column(name = "role_type", length = 50)
  @Enumerated(EnumType.STRING)
  private RolesType roleType;

  @Override
  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Role id(UUID id) {
    this.setId(id);
    return this;
  }

  public Role createdDate(Instant createdDate) {
    this.setCreatedDate(createdDate);
    return this;
  }

  public Boolean getIsActive() {
    return this.isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Role isActive(Boolean isActive) {
    this.setIsActive(isActive);
    return this;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Role label(String label) {
    this.setLabel(label);
    return this;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Role description(String description) {
    this.setDescription(description);
    return this;
  }

  public Set<Authority> getAuthorities() {
    return this.authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public Role authorities(Set<Authority> authorities) {
    this.setAuthorities(authorities);
    return this;
  }

  public Role addAuthority(Authority authority) {
    this.authorities.add(authority);
    return this;
  }

  public Role removeAuthority(Authority authority) {
    this.authorities.remove(authority);
    return this;
  }

  public RolesType getRoleType() {
    return roleType;
  }

  public void setRoleType(RolesType roleType) {
    this.roleType = roleType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Role role = (Role) o;

    return Objects.equals(this.id, role.id);
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Role{" +
        "id=" + this.id +
        ", isActive=" + this.isActive +
        ", label='" + this.label + '\'' +
        ", description='" + this.description + '\'' +
        ", authorities=" + this.authorities +
        ", roleType=" + this.roleType +
        '}';
  }
}
