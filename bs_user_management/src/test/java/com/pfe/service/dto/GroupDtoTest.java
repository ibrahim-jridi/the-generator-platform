package com.pfe.service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

class GroupDtoTest {

    private Validator validator;
    private GroupDTO groupDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();

        this.groupDTO = new GroupDTO();
        this.groupDTO.setId(UUID.randomUUID());
        this.groupDTO.setLabel("Test Label");
        this.groupDTO.setDescription("Test Description");
    }

    @Test
    void testLabelMaxSizeValidation() {
        this.groupDTO.setLabel("This label is more than 25 characters long");

        Set<ConstraintViolation<GroupDTO>> violations = this.validator.validate(this.groupDTO);
        Assertions.assertTrue(violations.stream().anyMatch(v -> "label".equals(v.getPropertyPath().toString())));
    }

    @Test
    void testDescriptionMaxSizeValidation() {
        this.groupDTO.setDescription("This description is intentionally made longer than 150 characters. " +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore " +
            "et dolore magna aliqua.");

        Set<ConstraintViolation<GroupDTO>> violations = this.validator.validate(this.groupDTO);
        Assertions.assertTrue(violations.stream().anyMatch(v -> "description".equals(v.getPropertyPath().toString())));
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        this.groupDTO.setId(id);

        GroupDTO sameGroupDTO = new GroupDTO();
        sameGroupDTO.setId(id);

        Assertions.assertEquals(this.groupDTO, sameGroupDTO);
        Assertions.assertEquals(this.groupDTO.hashCode(), sameGroupDTO.hashCode());
    }
}
