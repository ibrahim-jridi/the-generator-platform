package com.pfe.domain;

import com.pfe.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * A EventNotification.
 */
@Entity
@Table(name = "bs_event_notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventNotification extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1087894409538193126L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    public Notification notification;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public EventNotification id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public EventNotification notification(Notification notification) {
        this.setNotification(notification);
        return this;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public EventNotification taskId(String taskId) {
        this.setTaskId(taskId);
        return this;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return this.status;
    }

    public EventNotification status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventNotification)) {
            return false;
        }
        return getId() != null && getId().equals(((EventNotification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EventNotification{" +
            "id=" + id +
            ", notification=" + notification +
            ", taskId='" + taskId + '\'' +
            ", taskName='" + taskName + '\'' +
            ", status=" + status +
            "} " + super.toString();
    }

}
