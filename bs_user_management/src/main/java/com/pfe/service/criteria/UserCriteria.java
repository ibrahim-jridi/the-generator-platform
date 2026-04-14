package com.pfe.service.criteria;

import com.pfe.domain.enumeration.Gender;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.web.rest.UserResource;
import java.io.Serial;
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
 * Criteria class for the {@link User} entity. This class is used in {@link UserResource} to receive
 * all the possible filtering options from the Http GET request parameters. For example the
 * following could be a valid request:
 * {@code /user?id.greaterThan=5&attr1.contains=something&attr2.specified=false} As Spring is unable
 * to properly convert the types, unless specific {@link Filter} class are used, we need to use fix
 * type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private UUIDFilter id;
    private StringFilter email;
    private BooleanFilter isActive;
    private GenderFilter gender;
    private StringFilter firstName;
    private StringFilter lastName;
    private StringFilter username;
    private BooleanFilter emailVerified;
    private BooleanFilter deleted;
    private UUIDFilter createdById;
    private UUIDFilter roleId;
    private UUIDFilter groupId;
    private StringFilter denomination;

    private RegistryStatusFilter registryStatus;
    private Boolean distinct;

    public UserCriteria() {
    }

    public UserCriteria(UserCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.emailVerified = other.optionalEmailVerified().map(BooleanFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.roleId = other.optionalRoleId().map(UUIDFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(UUIDFilter::copy).orElse(null);
        this.groupId = other.optionalGroupId().map(UUIDFilter::copy).orElse(null);
        this.denomination = other.optionalDenomination().map(StringFilter::copy).orElse(null);
        this.registryStatus = other.optionalRegistryStatus().map(RegistryStatusFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
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

    public StringFilter getEmail() {
        return this.email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(this.email);
    }

    public StringFilter email() {
        if (this.email == null) {
            setEmail(new StringFilter());
        }
        return this.email;
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

    public GenderFilter getGender() {
        return this.gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(this.gender);
    }

    public GenderFilter gender() {
        if (this.gender == null) {
            setGender(new GenderFilter());
        }
        return this.gender;
    }

    public StringFilter getFirstName() {
        return this.firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(this.firstName);
    }

    public StringFilter firstName() {
        if (this.firstName == null) {
            setFirstName(new StringFilter());
        }
        return this.firstName;
    }

    public StringFilter getLastName() {
        return this.lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(this.lastName);
    }

    public StringFilter lastName() {
        if (this.lastName == null) {
            setLastName(new StringFilter());
        }
        return this.lastName;
    }

    public StringFilter getUsername() {
        return this.username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(this.username);
    }

    public StringFilter username() {
        if (this.username == null) {
            setUsername(new StringFilter());
        }
        return this.username;
    }

    public BooleanFilter getEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(BooleanFilter emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Optional<BooleanFilter> optionalEmailVerified() {
        return Optional.ofNullable(this.emailVerified);
    }

    public BooleanFilter emailVerified() {
        if (this.emailVerified == null) {
            setEmailVerified(new BooleanFilter());
        }
        return this.emailVerified;
    }

    public BooleanFilter getDeleted() {
        return this.deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public Optional<BooleanFilter> optionalDeleted() {
        return Optional.ofNullable(this.deleted);
    }

    public BooleanFilter deleted() {
        if (this.deleted == null) {
            setDeleted(new BooleanFilter());
        }
        return this.deleted;
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

    public UUIDFilter getGroupId() {
        return this.groupId;
    }

    public void setGroupId(UUIDFilter groupId) {
        this.groupId = groupId;
    }

    public Optional<UUIDFilter> optionalGroupId() {
        return Optional.ofNullable(this.groupId);
    }

    public UUIDFilter groupId() {
        if (this.groupId == null) {
            setGroupId(new UUIDFilter());
        }
        return this.groupId;
    }

    public StringFilter getDenomination() {return denomination;}

    public void setDenomination(StringFilter denomination) {this.denomination = denomination;}

    public Optional<StringFilter> optionalDenomination() {
        return Optional.ofNullable(this.denomination);
    }
    public StringFilter denomination() {
        if(this.denomination == null) {
            setDenomination(new StringFilter());
        }
        return this.denomination;
    }

    public RegistryStatusFilter getRegistryStatus() { return registryStatus;}

    public void setRegistryStatus(RegistryStatusFilter registryStatus) {this.registryStatus = registryStatus;}

    public Optional<RegistryStatusFilter> optionalRegistryStatus() {
        return Optional.ofNullable(this.registryStatus);
    }
    public RegistryStatusFilter registryStatusFilter() {
        if(this.registryStatus == null) {
            setRegistryStatus(new RegistryStatusFilter());
        }
        return this.registryStatus;
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
        final UserCriteria that = (UserCriteria) o;
        return (
            Objects.equals(this.id, that.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalEmailVerified().map(f -> "emailVerified=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalRoleId().map(f -> "roleId=" + f + ", ").orElse("") +
            optionalGroupId().map(f -> "groupId=" + f + ", ").orElse("") +
            optionalDenomination().map(f -> "denomination=" + f + ", ").orElse("") +
            optionalRegistryStatus().map(f -> "registryStatus=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}";
    }

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        @Serial
        private static final long serialVersionUID = -3273529862536607933L;

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering Gender
     */
    public static class RegistryStatusFilter extends Filter<RegistryStatus> {

        @Serial
        private static final long serialVersionUID = -5925617552058091554L;

        public RegistryStatusFilter() {
        }

        public RegistryStatusFilter(RegistryStatusFilter filter) {
            super(filter);
        }

        @Override
        public RegistryStatusFilter copy() {
            return new RegistryStatusFilter(this);
        }
    }
}
