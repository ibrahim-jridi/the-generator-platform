package com.pfe.service.dto;

import java.util.UUID;

public class DesignationsListDtoTest {

    private UUID id;
    private String designationName;
    private UUID pmUserId;

    public DesignationsListDtoTest() {
    }

    public DesignationsListDtoTest(UUID id, String designationName, UUID pmUserId) {
        this.id = id;
        this.designationName = designationName;
        this.pmUserId = pmUserId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public UUID getPmUserId() {
        return pmUserId;
    }

    public void setPmUserId(UUID pmUserId) {
        this.pmUserId = pmUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DesignationsListDtoTest that = (DesignationsListDtoTest) o;

        if (!id.equals(that.id)) return false;
        if (!designationName.equals(that.designationName)) return false;
        return pmUserId.equals(that.pmUserId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + designationName.hashCode();
        result = 31 * result + pmUserId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DesignationsListDtoTest{" +
            "id=" + id +
            ", designationName='" + designationName + '\'' +
            ", pmUserId=" + pmUserId +
            '}';
    }
}
