package com.pfe.service.dto;

import com.pfe.domain.EventNotification;
import com.pfe.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link EventNotification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventNotificationDTO extends  AbstractAuditingEntityDto implements Serializable {

    private UUID id;

    @NotNull
    private NotificationDTO notification;

    private String taskId;

    private String taskName;

    @NotNull
    private Status status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventNotificationDTO)) {
            return false;
        }

        EventNotificationDTO eventNotificationDTO = (EventNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "EventNotificationDTO{" +
            "id=" + id +
            ", notification=" + notification +
            ", taskId='" + taskId + '\'' +
            ", taskName='" + taskName + '\'' +
            ", status=" + status +
            "} " + super.toString();
    }
}
