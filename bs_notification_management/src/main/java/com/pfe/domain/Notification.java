package com.pfe.domain;

import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.domain.enums.DestinationTypeEnum;
import com.pfe.domain.enums.FrequencyEnum;
import com.pfe.domain.enums.Topic;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Notification.
 */
@Entity
@Table(name = "bs_notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 2141251201074793621L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "broadcast_channel")
    private BroadcastChannel broadcastChannel;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type")
    private DestinationTypeEnum destinationTypeEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic")
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_enum")
    private FrequencyEnum frequencyEnum;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnoreProperties(value = { "notification" }, allowSetters = true)
    private Set<NotificationDestination> notificationDestinations = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notification", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties(value = { "notification", "days" }, allowSetters = true)
    private Set<Frequency> frequencies = new HashSet<>();

    @Column(name = "file_name")
    private String fileName;

    @Column(name="is_from_task")
    private  Boolean isFromTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Notification id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Notification name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Notification description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BroadcastChannel getBroadcastChannel() {
        return this.broadcastChannel;
    }

    public Notification broadcastChannel(BroadcastChannel broadcastChannel) {
        this.setBroadcastChannel(broadcastChannel);
        return this;
    }

    public void setBroadcastChannel(BroadcastChannel broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public DestinationTypeEnum getDestinationType() {
        return this.destinationTypeEnum;
    }

    public Notification destinationType(DestinationTypeEnum destinationTypeEnum) {
        this.setDestinationType(destinationTypeEnum);
        return this;
    }

    public void setDestinationType(DestinationTypeEnum destinationTypeEnum) {
        this.destinationTypeEnum = destinationTypeEnum;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public Notification topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public FrequencyEnum getFrequencyEnum() {
        return this.frequencyEnum;
    }

    public Notification frequencyEnum(FrequencyEnum frequencyEnum) {
        this.setFrequencyEnum(frequencyEnum);
        return this;
    }

    public void setFrequencyEnum(FrequencyEnum frequencyEnum) {
        this.frequencyEnum = frequencyEnum;
    }

    public Set<NotificationDestination> getNotificationDestinations() {
        return this.notificationDestinations;
    }

    public void setNotificationDestinations(Set<NotificationDestination> notificationDestinations) {
        if (this.notificationDestinations != null) {
            this.notificationDestinations.forEach(i -> i.setNotification(null));
        }
        if (notificationDestinations != null) {
            notificationDestinations.forEach(i -> i.setNotification(this));
        }
        this.notificationDestinations = notificationDestinations;
    }

    public Notification notificationDestinations(Set<NotificationDestination> notificationDestinations) {
        this.setNotificationDestinations(notificationDestinations);
        return this;
    }

    public Notification addNotificationDestination(NotificationDestination notificationDestination) {
        this.notificationDestinations.add(notificationDestination);
        notificationDestination.setNotification(this);
        return this;
    }

    public Notification removeNotificationDestination(NotificationDestination notificationDestination) {
        this.notificationDestinations.remove(notificationDestination);
        notificationDestination.setNotification(null);
        return this;
    }

    public Set<Frequency> getFrequencies() {
        return this.frequencies;
    }

    public void setNotificationFrequencies(Set<Frequency> frequencies) {
        if (this.frequencies != null) {
            this.frequencies.forEach(i -> i.setNotification(null));
        }
        if (frequencies != null) {
            frequencies.forEach(i -> i.setNotification(this));
        }
        this.frequencies = frequencies;
    }

    public Notification frequencies(Set<Frequency> frequencies) {
        this.setNotificationFrequencies(frequencies);
        return this;
    }

    public Notification addNotificationFrequency(Frequency frequency) {
        this.frequencies.add(frequency);
        frequency.setNotification(this);
        return this;
    }

    public Notification removeNotificationDestination(Frequency frequency) {
        this.frequencies.remove(frequency);
        frequency.setNotification(null);
        return this;
    }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(getId());
    }

    public Boolean getIsFromTask() {
        return isFromTask;
    }

    public void setIsFromTask(Boolean isFromTask) {
        this.isFromTask = isFromTask;
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", broadcastChannel=" + broadcastChannel +
            ", destinationTypeEnum=" + destinationTypeEnum +
            ", topic=" + topic +
            ", frequencyEnum=" + frequencyEnum +
            ", notificationDestinations=" + notificationDestinations +
            ", frequencies=" + frequencies +
            ", fileName='" + fileName + '\'' +
            ", isFromTask=" + isFromTask +
            "} " + super.toString();
    }
}
