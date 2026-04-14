package com.pfe.service.dto.request;

import com.pfe.service.dto.ProfileDTO;
import com.pfe.service.dto.UserDTO;

import java.io.Serializable;
import java.util.Objects;

public class UpdateProfileRequest implements Serializable {

    private static final long serialVersionUID = -9061100062276828208L;

    private UserDTO userDTO;
    private ProfileDTO profileDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ProfileDTO getProfileDTO() {
        return profileDTO;
    }

    public void setProfileDTO(ProfileDTO profileDTO) {
        this.profileDTO = profileDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateProfileRequest that)) return false;
        return Objects.equals(userDTO, that.userDTO) && Objects.equals(profileDTO, that.profileDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDTO, profileDTO);
    }

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
            "userDTO=" + userDTO +
            ", profileDTO=" + profileDTO +
            '}';
    }
}
