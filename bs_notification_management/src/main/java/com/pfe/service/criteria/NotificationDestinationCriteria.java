package com.pfe.service.criteria;

import com.pfe.domain.NotificationDestination;
import com.pfe.web.rest.NotificationDestinationResource;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.UUIDFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link NotificationDestination} entity. This class is used
 * in {@link NotificationDestinationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-destinations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDestinationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 5620173991882653086L;

    private UUIDFilter id;

    private UUIDFilter senderId;

    private UUIDFilter notificationId;

    private Boolean distinct;

    public NotificationDestinationCriteria() {}

    public NotificationDestinationCriteria(NotificationDestinationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationDestinationCriteria copy() {
        return new NotificationDestinationCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public UUIDFilter getSenderId() {
        return senderId;
    }

    public UUIDFilter senderId() {
        if (senderId == null) {
            senderId = new UUIDFilter();
        }
        return senderId;
    }

    public void setSenderId(UUIDFilter senderId) {
        this.senderId = senderId;
    }

    public UUIDFilter getNotificationId() {
        return notificationId;
    }

    public UUIDFilter notificationId() {
        if (notificationId == null) {
            notificationId = new UUIDFilter();
        }
        return notificationId;
    }

    public void setNotificationId(UUIDFilter notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationDestinationCriteria that = (NotificationDestinationCriteria) o;
        return (
            Objects.equals(id, that.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDestinationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (senderId != null ? "senderId=" + senderId + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
