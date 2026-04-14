package com.pfe.service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class UserDtoTest {
    private Validator validator;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.userDTO = new UserDTO();
    }

    @Test
    void testValidUserDTO() {
        this.userDTO.setUsername("ValidUsername");
        this.userDTO.setEmail("valid@example.com");
        Set<ConstraintViolation<UserDTO>> violations = this.validator.validate(this.userDTO);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        this.userDTO.setEmail("invalidEmail");
        Set<ConstraintViolation<UserDTO>> violations = this.validator.validate(this.userDTO);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void testUsernameSize() {
        this.userDTO.setUsername("a");
        Set<ConstraintViolation<UserDTO>> violations = this.validator.validate(this.userDTO);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void testUserDTO() {
        UUID userId = UUID.randomUUID();
        Map<String, String> profileData = new HashMap<>();
        profileData.put("key1", "value1");
        profileData.put("key2", "value2");

        userDTO.setId(userId);
        userDTO.setProfile(profileData);

        assertThat(userDTO.getId()).isEqualTo(userId);
        assertThat(userDTO.getProfile()).isNotNull();
        assertThat(userDTO.getProfile()).hasSize(2);
        assertThat(userDTO.getProfile()).containsEntry("key1", "value1");
        assertThat(userDTO.getProfile()).containsEntry("key2", "value2");
    }
}
