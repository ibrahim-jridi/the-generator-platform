package com.pfe.service.dto.request;

import java.io.Serializable;
import java.util.Objects;

public class ApplicantListUserResponse implements Serializable {

    private static final long serialVersionUID = 8076361571837098958L;

    private String username;
    private String applicants;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApplicants() {
        return applicants;
    }

    public void setApplicants(String applicants) {
        this.applicants = applicants;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicantListUserResponse that = (ApplicantListUserResponse) o;
        return Objects.equals(username, that.username) && Objects.equals(applicants, that.applicants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, applicants);
    }

    @Override
    public String toString() {
        return "ApplicantListUserResponse{" +
            "username='" + username + '\'' +
            ", applicants='" + applicants + '\'' +
            '}';
    }
}
