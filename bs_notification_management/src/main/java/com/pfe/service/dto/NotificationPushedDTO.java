package com.pfe.service.dto;

import com.pfe.domain.NotificationPushed;
import com.pfe.domain.enums.BroadcastChannel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link NotificationPushed} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPushedDTO extends AbstractAuditingEntityDto implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String message;

    @NotNull
    private Instant notificationDate;

    @NotNull
    private UUID recipientId;

    @NotNull
    private String recipientName;

    private String taskId;

    private String fileName;

    private BroadcastChannel broadcastChannel;

    @NotNull
    private Boolean isSeen;

    private EventNotificationDTO eventNotification;

    public NotificationPushedDTO() {
    }

    public NotificationPushedDTO(Instant notificationDate, UUID recipientId, String name, String description, String message,String taskId, String fileName, Boolean isSeen, BroadcastChannel broadcastChannel) {
        this.notificationDate = notificationDate;
        this.recipientId = recipientId;
        this.name = name;
        this.taskId = taskId;
        this.fileName = fileName;
        this.description = description;
        this.message = message;
        this.isSeen = isSeen;
        this.broadcastChannel = broadcastChannel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public EventNotificationDTO getEventNotification() {
        return eventNotification;
    }

    public BroadcastChannel getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(BroadcastChannel broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setEventNotification(EventNotificationDTO eventNotification) {
        this.eventNotification = eventNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationPushedDTO)) {
            return false;
        }

        NotificationPushedDTO notificationPushedDTO = (NotificationPushedDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationPushedDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "NotificationPushedDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", notificationDate=" + notificationDate +
                ", recipientId=" + recipientId +
                ", isSeen=" + isSeen +
                ", eventNotificationDTO=" + eventNotification +
                "} " + super.toString();
    }
}
