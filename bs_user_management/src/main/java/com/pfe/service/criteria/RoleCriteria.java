package com.pfe.service.criteria;

import com.pfe.domain.Role;
import com.pfe.web.rest.RoleResource;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link Role} entity. This class is used in
 * {@link RoleResource} to receive all the possible filtering options from
 * the Http GET request parameters. For example the following could be a valid request:
 * {@code /roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false} As Spring is
 * unable to properly convert the types, unless specific {@link Filter} class are used, we need to
 * use fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter updatedDate;

    private BooleanFilter isActive;

    private StringFilter label;

    private StringFilter description;

    private UUIDFilter createdById;

    private UUIDFilter authorityId;

    private UUIDFilter userId;

    private Boolean distinct;

    public RoleCriteria() {
    }

    public RoleCriteria(RoleCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedDate = other.optionalUpdatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.label = other.optionalLabel().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(UUIDFilter::copy).orElse(null);
        this.authorityId = other.optionalAuthorityId().map(UUIDFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RoleCriteria copy() {
        return new RoleCriteria(this);
    }

    public UUIDFilter getId() {
        return this.id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(this.id);
    }

    public UUIDFilter id() {
        if (this.id == null) {
            setId(new UUIDFilter());
        }
        return this.id;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (this.createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return this.createdDate;
    }

    public ZonedDateTimeFilter getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(ZonedDateTimeFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedDate() {
        return Optional.ofNullable(this.updatedDate);
    }

    public ZonedDateTimeFilter updatedDate() {
        if (this.updatedDate == null) {
            setUpdatedDate(new ZonedDateTimeFilter());
        }
        return this.updatedDate;
    }

    public BooleanFilter getIsActive() {
        return this.isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(this.isActive);
    }

    public BooleanFilter isActive() {
        if (this.isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return this.isActive;
    }

    public StringFilter getLabel() {
        return this.label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public Optional<StringFilter> optionalLabel() {
        return Optional.ofNullable(this.label);
    }

    public StringFilter label() {
        if (this.label == null) {
            setLabel(new StringFilter());
        }
        return this.label;
    }

    public StringFilter getDescription() {
        return this.description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(this.description);
    }

    public StringFilter description() {
        if (this.description == null) {
            setDescription(new StringFilter());
        }
        return this.description;
    }

    public UUIDFilter getCreatedById() {
        return this.createdById;
    }

    public void setCreatedById(UUIDFilter createdById) {
        this.createdById = createdById;
    }

    public Optional<UUIDFilter> optionalCreatedById() {
        return Optional.ofNullable(this.createdById);
    }

    public UUIDFilter createdById() {
        if (this.createdById == null) {
            setCreatedById(new UUIDFilter());
        }
        return this.createdById;
    }

    public UUIDFilter getAuthorityId() {
        return this.authorityId;
    }

    public void setAuthorityId(UUIDFilter authorityId) {
        this.authorityId = authorityId;
    }

    public Optional<UUIDFilter> optionalAuthorityId() {
        return Optional.ofNullable(this.authorityId);
    }

    public UUIDFilter authorityId() {
        if (this.authorityId == null) {
            setAuthorityId(new UUIDFilter());
        }
        return this.authorityId;
    }

    public UUIDFilter getUserId() {
        return this.userId;
    }

    public void setUserId(UUIDFilter userId) {
        this.userId = userId;
    }

    public Optional<UUIDFilter> optionalUserId() {
        return Optional.ofNullable(this.userId);
    }

    public UUIDFilter userId() {
        if (this.userId == null) {
            setUserId(new UUIDFilter());
        }
        return this.userId;
    }

    public Boolean getDistinct() {
        return this.distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(this.distinct);
    }

    public Boolean distinct() {
        if (this.distinct == null) {
            setDistinct(true);
        }
        return this.distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RoleCriteria that = (RoleCriteria) o;
        return (
            Objects.equals(this.id, that.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalUpdatedDate().map(f -> "updatedDate=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLabel().map(f -> "label=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalAuthorityId().map(f -> "authorityId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}";
    }
}
