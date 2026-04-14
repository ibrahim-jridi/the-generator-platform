package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "bs_activity_type")
public class ActivityType extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = 2227847530426064695L;
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;
  @Column(name = "code")
  private String code;
  @Column(name = "name")
  private String name;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  public ActivityType(UUID id, String code, String name, User user) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public ActivityType() {
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityType that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.id);
  }

  @Override
  public String toString() {
    return "ActivityType{" +
        "id=" + this.id +
        ", code='" + this.code + '\'' +
        ", name='" + this.name + '\'' +
        '}';
  }
}
