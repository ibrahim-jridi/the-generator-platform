package com.pfe.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class ProfileDtoTest implements Serializable {
    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        Map<String, String> profileData = new HashMap<>();
        profileData.put("key", "value");

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(id);
        profileDTO.setProfileData(profileData);

        assertThat(profileDTO.getId()).isEqualTo(id);
        assertThat(profileDTO.getProfileData()).isEqualTo(profileData);
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        ProfileDTO profile1 = new ProfileDTO();
        profile1.setId(id);

        ProfileDTO profile2 = new ProfileDTO();
        profile2.setId(id);

        assertThat(profile1).isEqualTo(profile2);
        assertThat(profile1.hashCode()).isEqualTo(profile2.hashCode());
    }

    @Test
    void testNotEqualsForDifferentId() {
        ProfileDTO profile1 = new ProfileDTO();
        profile1.setId(UUID.randomUUID());

        ProfileDTO profile2 = new ProfileDTO();
        profile2.setId(UUID.randomUUID());

        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        Map<String, String> profileData = new HashMap<>();
        profileData.put("key", "value");

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(id);
        profileDTO.setProfileData(profileData);

        String expectedString = "ProfileDTO{" +
            "id=" + id +
            ", profileData=" + profileData +
            '}';

        assertThat(profileDTO.toString()).isEqualTo(expectedString);
    }
}
