package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A NotificationDestination.
 */
@Entity
@Table(name = "bs_notification_destination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDestination extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1087894409538193009L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "sender_id")
    private UUID senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notificationDestinations", "frequency" }, allowSetters = true)
    private Notification notification;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public NotificationDestination id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSenderId() {
        return this.senderId;
    }

    public NotificationDestination senderId(UUID senderId) {
        this.setSenderId(senderId);
        return this;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public NotificationDestination notification(Notification notification) {
        this.setNotification(notification);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDestination)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationDestination) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDestination{" +
            "id=" + getId() +
            ", senderId='" + getSenderId() + "'" +
            "}";
    }
}
