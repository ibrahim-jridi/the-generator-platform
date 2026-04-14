package com.pfe.service.criteria;

import com.pfe.domain.Authority;
import com.pfe.web.rest.AuthorityResource;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link Authority} entity. This class is used in
 * {@link AuthorityResource} to receive all the possible filtering options
 * from the Http GET request parameters. For example the following could be a valid request:
 * {@code /authorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false} As Spring is
 * unable to properly convert the types, unless specific {@link Filter} class are used, we need to
 * use fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private UUIDFilter id;
  private BooleanFilter isActive;

  private StringFilter label;

  private StringFilter description;

  private UUIDFilter createdById;

  private UUIDFilter roleId;

  private Boolean distinct;

  public AuthorityCriteria() {
  }

  public AuthorityCriteria(AuthorityCriteria other) {
    this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
    this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
    this.label = other.optionalLabel().map(StringFilter::copy).orElse(null);
    this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
    this.createdById = other.optionalCreatedById().map(UUIDFilter::copy).orElse(null);
    this.roleId = other.optionalRoleId().map(UUIDFilter::copy).orElse(null);
    this.distinct = other.distinct;
  }

  @Override
  public AuthorityCriteria copy() {
    return new AuthorityCriteria(this);
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

  public UUIDFilter getRoleId() {
    return this.roleId;
  }

  public void setRoleId(UUIDFilter roleId) {
    this.roleId = roleId;
  }

  public Optional<UUIDFilter> optionalRoleId() {
    return Optional.ofNullable(this.roleId);
  }

  public UUIDFilter roleId() {
    if (this.roleId == null) {
      setRoleId(new UUIDFilter());
    }
    return this.roleId;
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
    final AuthorityCriteria that = (AuthorityCriteria) o;
    return (
        Objects.equals(this.id, that.id) &&
            Objects.equals(this.isActive, that.isActive) &&
            Objects.equals(this.label, that.label) &&
            Objects.equals(this.description, that.description) &&
            Objects.equals(this.createdById, that.createdById) &&
            Objects.equals(this.roleId, that.roleId) &&
            Objects.equals(this.distinct, that.distinct)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.isActive, this.label, this.description, this.createdById,
        this.roleId, this.distinct);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "AuthorityCriteria{" +
        optionalId().map(f -> "id=" + f + ", ").orElse("") +
        optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
        optionalLabel().map(f -> "label=" + f + ", ").orElse("") +
        optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
        optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
        optionalRoleId().map(f -> "roleId=" + f + ", ").orElse("") +
        optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
  }
}
