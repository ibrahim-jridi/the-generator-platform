package com.pfe.service.criteria;

import com.pfe.domain.Notification;
import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.domain.enums.DestinationTypeEnum;
import com.pfe.domain.enums.FrequencyEnum;
import com.pfe.domain.enums.Topic;
import com.pfe.web.rest.NotificationResource;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Notification} entity. This class is used
 * in {@link NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BroadcastChannel
     */
    public static class BroadcastChannelFilter extends Filter<BroadcastChannel> {

        public BroadcastChannelFilter() {}

        public BroadcastChannelFilter(BroadcastChannelFilter filter) {
            super(filter);
        }

        @Override
        public BroadcastChannelFilter copy() {
            return new BroadcastChannelFilter(this);
        }
    }

    /**
     * Class for filtering DestinationType
     */
    public static class DestinationTypeFilter extends Filter<DestinationTypeEnum> {

        public DestinationTypeFilter() {}

        public DestinationTypeFilter(DestinationTypeFilter filter) {
            super(filter);
        }

        @Override
        public DestinationTypeFilter copy() {
            return new DestinationTypeFilter(this);
        }
    }

    /**
     * Class for filtering Topic
     */
    public static class TopicFilter extends Filter<Topic> {

        public TopicFilter() {}

        public TopicFilter(TopicFilter filter) {
            super(filter);
        }

        @Override
        public TopicFilter copy() {
            return new TopicFilter(this);
        }
    }

    /**
     * Class for filtering FrequencyEnum
     */
    public static class FrequencyEnumFilter extends Filter<FrequencyEnum> {

        public FrequencyEnumFilter() {}

        public FrequencyEnumFilter(FrequencyEnumFilter filter) {
            super(filter);
        }

        @Override
        public FrequencyEnumFilter copy() {
            return new FrequencyEnumFilter(this);
        }
    }

    private static final long serialVersionUID = -1606478944448379789L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private BroadcastChannelFilter broadcastChannel;

    private DestinationTypeFilter destinationType;

    private TopicFilter topic;

    private FrequencyEnumFilter frequencyEnum;

    private BooleanFilter isSeen;

    private UUIDFilter notificationDestinationId;

    private UUIDFilter frequencyId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.broadcastChannel = other.broadcastChannel == null ? null : other.broadcastChannel.copy();
        this.destinationType = other.destinationType == null ? null : other.destinationType.copy();
        this.topic = other.topic == null ? null : other.topic.copy();
        this.frequencyEnum = other.frequencyEnum == null ? null : other.frequencyEnum.copy();
        this.isSeen = other.isSeen == null ? null : other.isSeen.copy();
        this.notificationDestinationId = other.notificationDestinationId == null ? null : other.notificationDestinationId.copy();
        this.frequencyId = other.frequencyId == null ? null : other.frequencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BroadcastChannelFilter getBroadcastChannel() {
        return broadcastChannel;
    }

    public BroadcastChannelFilter broadcastChannel() {
        if (broadcastChannel == null) {
            broadcastChannel = new BroadcastChannelFilter();
        }
        return broadcastChannel;
    }

    public void setBroadcastChannel(BroadcastChannelFilter broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public DestinationTypeFilter getDestinationType() {
        return destinationType;
    }

    public DestinationTypeFilter destinationType() {
        if (destinationType == null) {
            destinationType = new DestinationTypeFilter();
        }
        return destinationType;
    }

    public void setDestinationType(DestinationTypeFilter destinationType) {
        this.destinationType = destinationType;
    }

    public TopicFilter getTopic() {
        return topic;
    }

    public TopicFilter topic() {
        if (topic == null) {
            topic = new TopicFilter();
        }
        return topic;
    }

    public void setTopic(TopicFilter topic) {
        this.topic = topic;
    }

    public FrequencyEnumFilter getFrequencyEnum() {
        return frequencyEnum;
    }

    public FrequencyEnumFilter frequencyEnum() {
        if (frequencyEnum == null) {
            frequencyEnum = new FrequencyEnumFilter();
        }
        return frequencyEnum;
    }

    public void setFrequencyEnum(FrequencyEnumFilter frequencyEnum) {
        this.frequencyEnum = frequencyEnum;
    }

    public BooleanFilter getIsSeen() {
        return isSeen;
    }

    public BooleanFilter isSeen() {
        if (isSeen == null) {
            isSeen = new BooleanFilter();
        }
        return isSeen;
    }

    public void setIsSeen(BooleanFilter isSeen) {
        this.isSeen = isSeen;
    }

    public UUIDFilter getNotificationDestinationId() {
        return notificationDestinationId;
    }

    public UUIDFilter notificationDestinationId() {
        if (notificationDestinationId == null) {
            notificationDestinationId = new UUIDFilter();
        }
        return notificationDestinationId;
    }

    public void setNotificationDestinationId(UUIDFilter notificationDestinationId) {
        this.notificationDestinationId = notificationDestinationId;
    }

    public UUIDFilter getFrequencyId() {
        return frequencyId;
    }

    public UUIDFilter frequencyId() {
        if (frequencyId == null) {
            frequencyId = new UUIDFilter();
        }
        return frequencyId;
    }

    public void setFrequencyId(UUIDFilter frequencyId) {
        this.frequencyId = frequencyId;
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
        final NotificationCriteria that = (NotificationCriteria) o;
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
        return "NotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (broadcastChannel != null ? "broadcastChannel=" + broadcastChannel + ", " : "") +
            (destinationType != null ? "destinationType=" + destinationType + ", " : "") +
            (topic != null ? "topic=" + topic + ", " : "") +
            (frequencyEnum != null ? "frequencyEnum=" + frequencyEnum + ", " : "") +
            (isSeen != null ? "isSeen=" + isSeen + ", " : "") +
            (notificationDestinationId != null ? "notificationDestinationId=" + notificationDestinationId + ", " : "") +
            (frequencyId != null ? "frequencyId=" + frequencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
