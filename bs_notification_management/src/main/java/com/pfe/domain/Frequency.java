package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pfe.domain.enums.ConfigurationDayEnum;
import com.pfe.domain.enums.FrequencyConfigEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Frequency.
 */
@Entity
@Table(name = "bs_frequency")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Frequency extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = -8391907832508424474L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_repeat")
    private Boolean isRepeat;

    @Column(name = "timing")
    private String timing;

    @Enumerated(EnumType.STRING)
    @Column(name = "configuration_day")
    private ConfigurationDayEnum configurationDayEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_config")
    private FrequencyConfigEnum frequencyConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "notificationDestinations", "frequency" }, allowSetters = true)
    private Notification notification;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "bs_frequency_days",
        joinColumns = @JoinColumn(name = "frequency_id"),
        inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private Set<Days> days = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Frequency id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Frequency startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Frequency endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsRepeat() {
        return this.isRepeat;
    }

    public Frequency isRepeat(Boolean isRepeat) {
        this.setIsRepeat(isRepeat);
        return this;
    }

    public void setIsRepeat(Boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getTiming() {
        return this.timing;
    }

    public Frequency timing(String timing) {
        this.setTiming(timing);
        return this;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public ConfigurationDayEnum getConfigurationDay() {
        return this.configurationDayEnum;
    }

    public Frequency configurationDay(ConfigurationDayEnum configurationDayEnum) {
        this.setConfigurationDay(configurationDayEnum);
        return this;
    }

    public void setConfigurationDay(ConfigurationDayEnum configurationDayEnum) {
        this.configurationDayEnum = configurationDayEnum;
    }

    public FrequencyConfigEnum getFrequencyConfig() {
        return this.frequencyConfig;
    }

    public Frequency frequencyConfig(FrequencyConfigEnum frequencyConfig) {
        this.setFrequencyConfig(frequencyConfig);
        return this;
    }

    public void setFrequencyConfig(FrequencyConfigEnum frequencyConfig) {
        this.frequencyConfig = frequencyConfig;
    }

    public Notification getNotification() {return this.notification;}

    public void setNotification(Notification notification) { this.notification = notification;}

    public Frequency notification(Notification notification) {
        this.setNotification(notification);
        return this;
    }

    public Set<Days> getDays() {
        return this.days;
    }

    public void setDays(Set<Days> days) {
        this.days = days;
    }

    public Frequency days(Set<Days> days) {
        this.setDays(days);
        return this;
    }

    public Frequency addDays(Days days) {
        this.days.add(days);
        return this;
    }

    public Frequency removeDays(Days days) {
        this.days.remove(days);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Frequency)) {
            return false;
        }
        return getId() != null && getId().equals(((Frequency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return Objects.hash(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Frequency{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isRepeat='" + getIsRepeat() + "'" +
            ", timing='" + getTiming() + "'" +
            ", configurationDay='" + getConfigurationDay() + "'" +
            ", frequencyConfig='" + getFrequencyConfig() + "'" +
            "}";
    }
}
