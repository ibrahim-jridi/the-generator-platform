package com.pfe.service.criteria;

import com.pfe.domain.Frequency;
import com.pfe.domain.enums.ConfigurationDayEnum;
import com.pfe.domain.enums.FrequencyConfigEnum;
import com.pfe.web.rest.FrequencyResource;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Frequency} entity. This class is used
 * in {@link FrequencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /frequencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrequencyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ConfigurationDay
     */
    public static class ConfigurationDayFilter extends Filter<ConfigurationDayEnum> {

        public ConfigurationDayFilter() {}

        public ConfigurationDayFilter(ConfigurationDayFilter filter) {
            super(filter);
        }

        @Override
        public ConfigurationDayFilter copy() {
            return new ConfigurationDayFilter(this);
        }
    }

    /**
     * Class for filtering FrequencyConfigEnum
     */
    public static class FrequencyConfigEnumFilter extends Filter<FrequencyConfigEnum> {

        public FrequencyConfigEnumFilter() {}

        public FrequencyConfigEnumFilter(FrequencyConfigEnumFilter filter) {
            super(filter);
        }

        @Override
        public FrequencyConfigEnumFilter copy() {
            return new FrequencyConfigEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 6096120773623646964L;

    private UUIDFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter isRepeat;

    private StringFilter timing;

    private ConfigurationDayFilter configurationDay;

    private FrequencyConfigEnumFilter frequencyConfig;

    private UUIDFilter notificationId;

    private Boolean distinct;

    public FrequencyCriteria() {}

    public FrequencyCriteria(FrequencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.isRepeat = other.isRepeat == null ? null : other.isRepeat.copy();
        this.timing = other.timing == null ? null : other.timing.copy();
        this.configurationDay = other.configurationDay == null ? null : other.configurationDay.copy();
        this.frequencyConfig = other.frequencyConfig == null ? null : other.frequencyConfig.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FrequencyCriteria copy() {
        return new FrequencyCriteria(this);
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsRepeat() {
        return isRepeat;
    }

    public BooleanFilter isRepeat() {
        if (isRepeat == null) {
            isRepeat = new BooleanFilter();
        }
        return isRepeat;
    }

    public void setIsRepeat(BooleanFilter isRepeat) {
        this.isRepeat = isRepeat;
    }

    public StringFilter getTiming() {
        return timing;
    }

    public StringFilter timing() {
        if (timing == null) {
            timing = new StringFilter();
        }
        return timing;
    }

    public void setTiming(StringFilter timing) {
        this.timing = timing;
    }

    public ConfigurationDayFilter getConfigurationDay() {
        return configurationDay;
    }

    public ConfigurationDayFilter configurationDay() {
        if (configurationDay == null) {
            configurationDay = new ConfigurationDayFilter();
        }
        return configurationDay;
    }

    public void setConfigurationDay(ConfigurationDayFilter configurationDay) {
        this.configurationDay = configurationDay;
    }

    public FrequencyConfigEnumFilter getFrequencyConfig() {
        return frequencyConfig;
    }

    public FrequencyConfigEnumFilter frequencyConfig() {
        if (frequencyConfig == null) {
            frequencyConfig = new FrequencyConfigEnumFilter();
        }
        return frequencyConfig;
    }

    public void setFrequencyConfig(FrequencyConfigEnumFilter frequencyConfig) {
        this.frequencyConfig = frequencyConfig;
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
        final FrequencyCriteria that = (FrequencyCriteria) o;
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
        return "FrequencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (isRepeat != null ? "isRepeat=" + isRepeat + ", " : "") +
            (timing != null ? "timing=" + timing + ", " : "") +
            (configurationDay != null ? "configurationDay=" + configurationDay + ", " : "") +
            (frequencyConfig != null ? "frequencyConfig=" + frequencyConfig + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

