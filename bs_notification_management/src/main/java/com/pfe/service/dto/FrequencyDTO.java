package com.pfe.service.dto;

import com.pfe.domain.Frequency;
import com.pfe.domain.enums.ConfigurationDayEnum;
import com.pfe.domain.enums.FrequencyConfigEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Frequency} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrequencyDTO implements Serializable {

    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isRepeat;

    private String timing;

    private ConfigurationDayEnum configurationDayEnum;

    private FrequencyConfigEnum frequencyConfig;

    private UUID notificationId;

    private Set<DaysDTO> days;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public ConfigurationDayEnum getConfigurationDay() {
        return configurationDayEnum;
    }

    public void setConfigurationDay(ConfigurationDayEnum configurationDayEnum) {
        this.configurationDayEnum = configurationDayEnum;
    }

    public FrequencyConfigEnum getFrequencyConfig() {
        return frequencyConfig;
    }

    public void setFrequencyConfig(FrequencyConfigEnum frequencyConfig) {
        this.frequencyConfig = frequencyConfig;
    }

    public UUID getNotificationId() {return notificationId;}

    public void setNotificationId(UUID notificationId) {this.notificationId = notificationId;}

    public Set<DaysDTO> getDays() {return days;}

    public void setDays(Set<DaysDTO> days) {this.days = days;}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrequencyDTO)) {
            return false;
        }

        FrequencyDTO frequencyDTO = (FrequencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, frequencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrequencyDTO{" +
            "id='" + getId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isRepeat='" + getIsRepeat() + "'" +
            ", timing='" + getTiming() + "'" +
            ", configurationDay='" + getConfigurationDay() + "'" +
            ", frequencyConfig='" + getFrequencyConfig() + "'" +
            ", notificationId='" + getNotificationId() + "'" +
            "}";
    }
}
