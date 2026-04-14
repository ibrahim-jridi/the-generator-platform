package com.pfe.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bs_designations_list")
public class DesignationsList extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = 3177540389644049449L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "keycloackId", nullable = false, unique = true)
  private UUID keycloackId;

  @OneToOne
  @JoinColumn(name = "pm_user_id")
  private User pmUser;

  @OneToOne
  @JoinColumn(name = "designated_user_id", nullable = false)
  private User designatedUser;

  @OneToOne
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;
  @OneToOne
  @JoinColumn(name = "laboratory_id")
  private User laboratoryUser;

  public DesignationsList() {
  }

  public DesignationsList(User pmUser, User designatedUser, Role role,UUID keycloackId,User laboratoryUser) {
    this.pmUser = pmUser;
    this.designatedUser = designatedUser;
    this.role = role;
    this.keycloackId=keycloackId;
    this.laboratoryUser=laboratoryUser;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getPmUser() {
    return pmUser;
  }

  public void setPmUser(User pmUser) {
    this.pmUser = pmUser;
  }

  public User getDesignatedUser() {
    return designatedUser;
  }

  public void setDesignatedUser(User designatedUser) {
    this.designatedUser = designatedUser;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DesignationsList that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "DesignationsList{" +
        "id=" + id +
        ", pmUser=" + pmUser +
        ", designatedUser=" + designatedUser +
        ", role=" + role +
        ", keycloackId="+ keycloackId +
        ", laboratoryUser=" + laboratoryUser +
        '}';
  }
}
