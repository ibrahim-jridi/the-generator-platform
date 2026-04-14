package com.pfe.service.criteria;

import com.pfe.domain.NotificationPushed;
import com.pfe.web.rest.NotificationPushedResource;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link NotificationPushed} entity. This class is used
 * in {@link NotificationPushedResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-pusheds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPushedCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter message;

    private InstantFilter notificationDate;

    private UUIDFilter recipientId;

    private BooleanFilter isSeen;

    private Boolean distinct;

    public NotificationPushedCriteria() {}

    public NotificationPushedCriteria(NotificationPushedCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.notificationDate = other.optionalNotificationDate().map(InstantFilter::copy).orElse(null);
        this.recipientId = other.optionalRecipientId().map(UUIDFilter::copy).orElse(null);
        this.isSeen = other.optionalIsSeen().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationPushedCriteria copy() {
        return new NotificationPushedCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public InstantFilter getNotificationDate() {
        return notificationDate;
    }

    public Optional<InstantFilter> optionalNotificationDate() {
        return Optional.ofNullable(notificationDate);
    }

    public InstantFilter notificationDate() {
        if (notificationDate == null) {
            setNotificationDate(new InstantFilter());
        }
        return notificationDate;
    }

    public void setNotificationDate(InstantFilter notificationDate) {
        this.notificationDate = notificationDate;
    }

    public UUIDFilter getRecipientId() {
        return recipientId;
    }

    public Optional<UUIDFilter> optionalRecipientId() {
        return Optional.ofNullable(recipientId);
    }

    public UUIDFilter recipientId() {
        if (recipientId == null) {
            setRecipientId(new UUIDFilter());
        }
        return recipientId;
    }

    public void setRecipientId(UUIDFilter recipientId) {
        this.recipientId = recipientId;
    }

    public BooleanFilter getIsSeen() {
        return isSeen;
    }

    public Optional<BooleanFilter> optionalIsSeen() {
        return Optional.ofNullable(isSeen);
    }

    public BooleanFilter isSeen() {
        if (isSeen == null) {
            setIsSeen(new BooleanFilter());
        }
        return isSeen;
    }

    public void setIsSeen(BooleanFilter isSeen) {
        this.isSeen = isSeen;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
        final NotificationPushedCriteria that = (NotificationPushedCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(message, that.message) &&
            Objects.equals(notificationDate, that.notificationDate) &&
            Objects.equals(recipientId, that.recipientId) &&
            Objects.equals(isSeen, that.isSeen) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, message, notificationDate, recipientId, isSeen, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationPushedCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalNotificationDate().map(f -> "notificationDate=" + f + ", ").orElse("") +
            optionalRecipientId().map(f -> "recipientId=" + f + ", ").orElse("") +
            optionalIsSeen().map(f -> "isSeen=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
