package com.pfe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Event.
 */
@Entity
@Table(name = "bs_event")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = 7654948602869051059L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "assign", length = 50)
  @Size(max = 50)
  private String assign;

  @Column(name = "display", length = 50)
  @Size(max = 50)
  private String display;

  @Column(name = "event_type", length = 25)
  @Size(max = 25)
  private String eventType;

  @Column(name = "message", length = 50)
  @Size(max = 50)
  private String message;

  @Column(name = "notification_type", length = 50)
  @Size(max = 50)
  private String notificationType;

  @Column(name = "subject", length = 50)
  @Size(max = 50)
  private String subject;

  @Column(name = "recipiant_id")
  private UUID recipiantId;

  @Override
  public UUID getId() {
    return this.id;
  }

  public Event id(UUID id) {
    this.setId(id);
    return this;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getAssign() {
    return this.assign;
  }

  public Event assign(String assign) {
    this.setAssign(assign);
    return this;
  }

  public void setAssign(String assign) {
    this.assign = assign;
  }

  public String getDisplay() {
    return this.display;
  }

  public Event display(String display) {
    this.setDisplay(display);
    return this;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getEventType() {
    return this.eventType;
  }

  public Event eventType(String eventType) {
    this.setEventType(eventType);
    return this;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getMessage() {
    return this.message;
  }

  public Event message(String message) {
    this.setMessage(message);
    return this;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getNotificationType() {
    return this.notificationType;
  }

  public Event notificationType(String notificationType) {
    this.setNotificationType(notificationType);
    return this;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public String getSubject() {
    return this.subject;
  }

  public Event subject(String subject) {
    this.setSubject(subject);
    return this;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public UUID getRecipiantId() {
    return this.recipiantId;
  }

  public Event recipiantId(UUID recipiantId) {
    this.setRecipiantId(recipiantId);
    return this;
  }

  public void setRecipiantId(UUID recipiantId) {
    this.recipiantId = recipiantId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Event that)) {
      return false;
    }
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }


  @Override
  public String toString() {
    return "Event{" +
        "id=" + getId() +
        ", assign='" + getAssign() + "'" +
        ", display='" + getDisplay() + "'" +
        ", eventType='" + getEventType() + "'" +
        ", message='" + getMessage() + "'" +
        ", notificationType='" + getNotificationType() + "'" +
        ", subject='" + getSubject() + "'" +
        ", recipiantId='" + getRecipiantId() + "'" +
        "}";
  }
}
