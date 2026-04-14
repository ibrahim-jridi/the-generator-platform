package com.pfe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A NotificationPushed.
 */
@Entity
@Table(name = "bs_notification_pushed")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPushed extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "notification_date", nullable = false)
    private Instant notificationDate;

    @NotNull
    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @NotNull
    @Column(name = "is_seen", nullable = false)
    private Boolean isSeen;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventNotification eventNotification;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public NotificationPushed id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public NotificationPushed name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public NotificationPushed description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationPushed message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getNotificationDate() {
        return this.notificationDate;
    }

    public NotificationPushed notificationDate(Instant notificationDate) {
        this.setNotificationDate(notificationDate);
        return this;
    }

    public void setNotificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
    }

    public UUID getRecipientId() {
        return this.recipientId;
    }

    public NotificationPushed recipientId(UUID recipientId) {
        this.setRecipientId(recipientId);
        return this;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsSeen() {
        return this.isSeen;
    }

    public NotificationPushed isSeen(Boolean isSeen) {
        this.setIsSeen(isSeen);
        return this;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }


    public EventNotification getEventNotification() {
        return eventNotification;
    }

    public void setEventNotification(EventNotification eventNotification) {
        this.eventNotification = eventNotification;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationPushed)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationPushed) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationPushed{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", message='" + message + '\'' +
            ", notificationDate=" + notificationDate +
            ", recipientId=" + recipientId +
            ", isSeen=" + isSeen +
            '}';
    }
}
