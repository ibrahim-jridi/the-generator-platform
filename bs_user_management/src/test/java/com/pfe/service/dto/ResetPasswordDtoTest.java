package com.pfe.service.dto;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResetPasswordDtoTest {

    private ResetPasswordDTO resetPasswordDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.resetPasswordDTO = new ResetPasswordDTO();
    }

    @Test
    void testSetters() {
        this.resetPasswordDTO.setNewPassword("newPassword123");
        this.resetPasswordDTO.setConfirmPassword("resetToken123");

        Assertions.assertEquals("newPassword123", this.resetPasswordDTO.getNewPassword());
        Assertions.assertEquals("resetToken123", this.resetPasswordDTO.getConfirmPassword());
    }

    @Test
    void testGetters() {
        this.resetPasswordDTO.setNewPassword("newPassword456");
        this.resetPasswordDTO.setConfirmPassword("resetToken456");

        Assertions.assertEquals("newPassword456", this.resetPasswordDTO.getNewPassword());
        Assertions.assertEquals("resetToken456", this.resetPasswordDTO.getConfirmPassword());
    }
}
