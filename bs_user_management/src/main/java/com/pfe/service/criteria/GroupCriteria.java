package com.pfe.service.criteria;

import com.pfe.domain.Group;
import com.pfe.web.rest.GroupResource;
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
 * Criteria class for the {@link Group} entity. This class is used in
 * {@link GroupResource} to receive all the possible filtering options from
 * the Http GET request parameters. For example the following could be a valid request:
 * {@code /groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false} As Spring is
 * unable to properly convert the types, unless specific {@link Filter} class are used, we need to
 * use fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter updatedDate;

    private BooleanFilter isActive;

    private StringFilter label;

    private StringFilter description;

    private UUIDFilter childrenId;

    private UUIDFilter createdById;

    private UUIDFilter parentId;

    private UUIDFilter userId;

    private Boolean distinct;

    public GroupCriteria() {
    }

    public GroupCriteria(GroupCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedDate = other.optionalUpdatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.label = other.optionalLabel().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(UUIDFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(UUIDFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(UUIDFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GroupCriteria copy() {
        return new GroupCriteria(this);
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

    public UUIDFilter getChildrenId() {
        return this.childrenId;
    }

    public void setChildrenId(UUIDFilter childrenId) {
        this.childrenId = childrenId;
    }

    public Optional<UUIDFilter> optionalChildrenId() {
        return Optional.ofNullable(this.childrenId);
    }

    public UUIDFilter childrenId() {
        if (this.childrenId == null) {
            setChildrenId(new UUIDFilter());
        }
        return this.childrenId;
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

    public UUIDFilter getParentId() {
        return this.parentId;
    }

    public void setParentId(UUIDFilter parentId) {
        this.parentId = parentId;
    }

    public Optional<UUIDFilter> optionalParentId() {
        return Optional.ofNullable(this.parentId);
    }

    public UUIDFilter parentId() {
        if (this.parentId == null) {
            setParentId(new UUIDFilter());
        }
        return this.parentId;
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
        final GroupCriteria that = (GroupCriteria) o;
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
        return "GroupCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalUpdatedDate().map(f -> "updatedDate=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLabel().map(f -> "label=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}";
    }
}
