package com.pfe.service.criteria;

import com.pfe.service.dto.DesignationsListDTO;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.UUIDFilter;

import java.awt.print.Pageable;
import java.io.Serializable;
import java.util.Objects;

public class DesignationsListCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;
    private UUIDFilter pmUserId;
    private UUIDFilter designatedUserId;
    private UUIDFilter roleId;

    private Pageable pageable;

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public DesignationsListCriteria() {}

    public DesignationsListCriteria(DesignationsListCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pmUserId = other.pmUserId == null ? null : other.pmUserId.copy();
        this.designatedUserId = other.designatedUserId == null ? null : other.designatedUserId.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.pageable = other.pageable;
    }

    @Override
    public DesignationsListCriteria copy() {
        return new DesignationsListCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public UUIDFilter getPmUserId() {
        return pmUserId;
    }

    public void setPmUserId(UUIDFilter pmUserId) {
        this.pmUserId = pmUserId;
    }

    public UUIDFilter getDesignatedUserId() {
        return designatedUserId;
    }

    public void setDesignatedUserId(UUIDFilter designatedUserId) {
        this.designatedUserId = designatedUserId;
    }

    public UUIDFilter getRoleId() {
        return roleId;
    }

    public void setRoleId(UUIDFilter roleId) {
        this.roleId = roleId;
    }

    public Pageable getPageable() {
        return pageable;
    }
    public void setPageable(org.springframework.data.domain.Pageable pageable) {
        this.pageable = this.pageable;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DesignationsListDTO designationsListDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, designationsListDTO.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "DesignationsListCriteria{" +
            "id=" + id +
            ", pmUserId=" + pmUserId +
            ", designatedUserId=" + designatedUserId +
            ", roleId=" + roleId +
            ", pageable=" + pageable +
            '}';
    }
}
