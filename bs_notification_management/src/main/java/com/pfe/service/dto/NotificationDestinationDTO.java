package com.pfe.service.dto;

import com.pfe.domain.NotificationDestination;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link NotificationDestination} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDestinationDTO implements Serializable {

    private UUID id;

    private UUID senderId;

    private UUID notificationId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getNotificationId() {return notificationId;}

    public void setNotificationId(UUID notificationId) {this.notificationId = notificationId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDestinationDTO)) {
            return false;
        }

        NotificationDestinationDTO notificationDestinationDTO = (NotificationDestinationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDestinationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDestinationDTO{" +
            "id='" + getId() + "'" +
            ", senderId='" + getSenderId() + "'" +
            ", notificationId=" + getNotificationId() +
            "}";
    }
}
