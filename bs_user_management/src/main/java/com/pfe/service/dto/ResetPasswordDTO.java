package com.pfe.service.dto;

import java.io.Serializable;

public class ResetPasswordDTO implements Serializable {

    private static final long serialVersionUID = -5352970619569802669L;

    private String newPassword;
    private String username;
    private String confirmPassword;

    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(String newPassword, String username, String confirmPassword) {
        this.newPassword = newPassword;
        this.username = username;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
