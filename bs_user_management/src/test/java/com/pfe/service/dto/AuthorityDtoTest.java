package com.pfe.service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityDtoTest {

    private final Validator validator;

    public AuthorityDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testGetterAndSetter() {
        UUID id = UUID.randomUUID();
        AuthorityDTO dto = new AuthorityDTO();

        dto.setId(id);
        dto.setLabel("Test Label");
        dto.setDescription("Test Description");
        dto.setIsActive(true);
        dto.setCreatedBy(UUID.randomUUID());
        dto.setCreatedDate(Instant.now());
        dto.setLastModifiedBy(UUID.randomUUID());
        dto.setLastModifiedDate(Instant.now());
        dto.setVersion(1);
        dto.setDeleted(false);

        assertEquals(id, dto.getId());
        assertEquals("Test Label", dto.getLabel());
        assertEquals("Test Description", dto.getDescription());
        assertTrue(dto.getIsActive());
        assertNotNull(dto.getCreatedBy());
        assertNotNull(dto.getCreatedDate());
        assertNotNull(dto.getLastModifiedBy());
        assertNotNull(dto.getLastModifiedDate());
        assertEquals(1, dto.getVersion());
        assertFalse(dto.getDeleted());
    }

    @Test
    void testValidation() {
        AuthorityDTO dto = new AuthorityDTO();

        Set<ConstraintViolation<AuthorityDTO>> violations = this.validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "id".equals(v.getPropertyPath().toString())));

        // Test @Size validation on label and description
        dto.setId(UUID.randomUUID());
        dto.setLabel("A label that is way too long and exceeds 25 characters");
        dto.setDescription("A description that exceeds 150 characters by adding a lot of additional text to test the length constraint and make sure validation catches it. so we are injecting over 150 characters in this test");

        violations = this.validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> "label".equals(v.getPropertyPath().toString())));
        assertTrue(violations.stream().anyMatch(v -> "description".equals(v.getPropertyPath().toString())));
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        AuthorityDTO dto1 = new AuthorityDTO();
        dto1.setId(id);

        AuthorityDTO dto2 = new AuthorityDTO();
        dto2.setId(id);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AuthorityDTO dto = new AuthorityDTO();
        dto.setId(UUID.randomUUID());
        dto.setLabel("Label");
        dto.setDescription("Description");
        dto.setIsActive(true);

        String toString = dto.toString();
        assertTrue(toString.contains("id"));
        assertTrue(toString.contains("Label"));
        assertTrue(toString.contains("Description"));
        assertTrue(toString.contains("isActive"));
    }

}
