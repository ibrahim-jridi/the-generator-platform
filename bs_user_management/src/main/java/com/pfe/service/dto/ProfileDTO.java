package com.pfe.service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProfileDTO implements Serializable {
    private static final long serialVersionUID = 8134123075853310852L;



    private UUID id;

    Map<String, String> profileData;

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
