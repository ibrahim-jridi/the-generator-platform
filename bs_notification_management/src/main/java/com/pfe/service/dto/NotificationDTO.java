package com.pfe.service.dto;

import com.pfe.domain.enums.*;

import com.pfe.domain.Notification;
import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.domain.enums.DestinationTypeEnum;
import com.pfe.domain.enums.FrequencyEnum;
import com.pfe.domain.enums.Topic;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private UUID id;

    private String name;

    private String description;

    private BroadcastChannel broadcastChannel;

    private DestinationTypeEnum destinationTypeEnum;

    private Topic topic;

    private FrequencyEnum frequencyEnum;

    private Set<NotificationDestinationDTO> notificationDestinations;

    private Set<FrequencyDTO> frequencies;

    private String fileName;

    private Instant createdDate;

    private String fileBase64;

    private byte[] fileData;

    private TaskDTO taskDTO;

    private Boolean isFromTask;

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

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

    public BroadcastChannel getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(BroadcastChannel broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public DestinationTypeEnum getDestinationType() {
        return destinationTypeEnum;
    }

    public void setDestinationType(DestinationTypeEnum destinationTypeEnum) {
        this.destinationTypeEnum = destinationTypeEnum;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public FrequencyEnum getFrequencyEnum() {
        return frequencyEnum;
    }

    public void setFrequencyEnum(FrequencyEnum frequencyEnum) {
        this.frequencyEnum = frequencyEnum;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
    public Instant getCreatedDate() {return createdDate;}

    public void setCreatedDate(Instant createdDate) {this.createdDate = createdDate;}

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public Boolean getIsFromTask() {
        return isFromTask;
    }

    public void setIsFromTask(Boolean isFromTask) {
        this.isFromTask = isFromTask;
    }

    public Set<FrequencyDTO> getFrequencies() { return frequencies; }

    public void setFrequencies(Set<FrequencyDTO> frequencies) { this.frequencies = frequencies; }

    public Set<NotificationDestinationDTO> getNotificationDestinations() {return notificationDestinations;}

    public void setNotificationDestinations(Set<NotificationDestinationDTO> notificationDestinations) {this.notificationDestinations = notificationDestinations;}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", broadcastChannel='" + getBroadcastChannel() + "'" +
            ", destinationType='" + getDestinationType() + "'" +
            ", topic='" + getTopic() + "'" +
            ", frequencyEnum='" + getFrequencyEnum() + "'" +
            ", taskDTO='" + taskDTO + '\'' +
            ", isFromTask='" + isFromTask + '\'' +
            "}";
    }
}

