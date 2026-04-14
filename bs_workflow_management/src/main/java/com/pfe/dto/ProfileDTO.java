package com.pfe.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProfileDTO implements Serializable {
    private static final long serialVersionUID = 3148307085208009850L;

    private UUID id;
    Map<String, String> profileData;
    private String companyName;
    private String webSite;
    private String productCategory;

    private String anotherCategories;
    private UUID keycloakId;

    public Map<String, String> getProfileData() {
        return profileData;
    }

    public void setProfileData(Map<String, String> profileData) {
        this.profileData = profileData;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    public String getProductCategory() {
        return productCategory;
    }

    public String getAnotherCategories() {
        return anotherCategories;
    }

    public void setAnotherCategories(String anotherCategories) {
        this.anotherCategories = anotherCategories;
    }


    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO profileDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + id +
            ", profileData=" + profileData +
            '}';
    }
}
