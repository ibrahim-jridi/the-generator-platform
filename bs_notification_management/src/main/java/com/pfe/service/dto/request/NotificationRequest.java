package com.pfe.service.dto.request;

import com.pfe.domain.enums.BroadcastChannel;
import com.pfe.domain.enums.DestinationTypeEnum;
import com.pfe.domain.enums.FrequencyEnum;
import com.pfe.domain.enums.Topic;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.dto.NotificationDestinationDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class NotificationRequest implements Serializable {

    private static final long serialVersionUID = -279779832984742039L;
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

    private String extractedLabel;

    private Boolean isFromTask;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public BroadcastChannel getBroadcastChannel() { return broadcastChannel; }

    public void setBroadcastChannel(BroadcastChannel broadcastChannel) { this.broadcastChannel = broadcastChannel; }

    public DestinationTypeEnum getDestinationType() { return destinationTypeEnum; }

    public void setDestinationType(DestinationTypeEnum destinationTypeEnum) { this.destinationTypeEnum = destinationTypeEnum; }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) { this.topic = topic; }

    public FrequencyEnum getFrequencyEnum() { return frequencyEnum; }

    public void setFrequencyEnum(FrequencyEnum frequencyEnum) { this.frequencyEnum = frequencyEnum; }

    public Set<NotificationDestinationDTO> getNotificationDestinations() {return notificationDestinations;}

    public void setNotificationDestinations(Set<NotificationDestinationDTO> notificationDestinations) {this.notificationDestinations = notificationDestinations;}

    public Set<FrequencyDTO> getFrequencies() {return frequencies;}

    public void setFrequencies(Set<FrequencyDTO> frequencies) {this.frequencies = frequencies;}

    public String getFileName() { return fileName;}

    public void setFileName(String fileName) {this.fileName = fileName; }

    public String getExtractedLabel() {return extractedLabel;}

    public void setExtractedLabel(String extractedLabel) {this.extractedLabel = extractedLabel;}

    public Boolean getIsFromTask() {
        return isFromTask;
    }

    public void setIsFromTask(Boolean isFromTask) {
        this.isFromTask = isFromTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRequest that = (NotificationRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
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
            ", extractedLabel='" + extractedLabel + '\'' +
            ", isFromTask='" + isFromTask + '\'' +
            '}';
    }
}
