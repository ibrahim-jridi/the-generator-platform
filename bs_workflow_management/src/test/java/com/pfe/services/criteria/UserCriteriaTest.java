package com.pfe.services.criteria;


import org.junit.jupiter.api.Test;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserCriteriaTest {

    @Test
    void testDefaultConstructor() {
        UserCriteria criteria = new UserCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getId());
        assertNull(criteria.getEmail());
        assertNull(criteria.getCreatedDate());
        assertNull(criteria.getUpdatedDate());
        assertNull(criteria.getIsActive());
        assertNull(criteria.getGender());
        assertNull(criteria.getFirstName());
        assertNull(criteria.getLastName());
        assertNull(criteria.getUsername());
        assertNull(criteria.getEmailVerified());
        assertNull(criteria.getDeleted());
        assertNull(criteria.getCreatedById());
        assertNull(criteria.getRoleId());
        assertNull(criteria.getGroupId());
        assertNull(criteria.getDistinct());
    }

    @Test
    void testCopyConstructor() {
        UserCriteria original = new UserCriteria();
        original.setEmail((StringFilter) new StringFilter().setEquals("test@example.com"));
        original.setIsActive((BooleanFilter) new BooleanFilter().setEquals(true));

        UserCriteria copy = new UserCriteria(original);

        assertEquals(original.getEmail(), copy.getEmail());
        assertEquals(original.getIsActive(), copy.getIsActive());
    }

    @Test
    void testSetAndGetMethods() {
        UserCriteria criteria = new UserCriteria();
        StringFilter emailFilter = new StringFilter();
        emailFilter.setEquals("test@example.com");

        criteria.setEmail(emailFilter);
        assertEquals("test@example.com", criteria.getEmail().getEquals());

        BooleanFilter isActiveFilter = new BooleanFilter();
        isActiveFilter.setEquals(true);

        criteria.setIsActive(isActiveFilter);
        assertTrue(criteria.getIsActive().getEquals());
    }

    @Test
    void testEqualsAndHashCode() {
        UserCriteria criteria1 = new UserCriteria();
        UserCriteria criteria2 = new UserCriteria();

        // Test if two instances with the same data are equal
        assertTrue(criteria1.equals(criteria2));
        assertEquals(criteria1.hashCode(), criteria2.hashCode());

        // Modify one object and check equality again
        criteria1.setFirstName((StringFilter) new StringFilter().setEquals("John"));
        assertFalse(criteria1.equals(criteria2));
    }

    @Test
    void testToString() {
        UserCriteria criteria = new UserCriteria();
        criteria.setFirstName((StringFilter) new StringFilter().setEquals("John"));
        criteria.setLastName((StringFilter) new StringFilter().setEquals("Doe"));

        String toStringResult = criteria.toString();
        assertTrue(toStringResult.contains("firstName=StringFilter [equals=John, ]"));
        assertTrue(toStringResult.contains("lastName=StringFilter [equals=Doe, ]"));
    }

    @Test
    void testOptionalMethods() {
        UserCriteria criteria = new UserCriteria();
        assertFalse(criteria.optionalEmail().isPresent());

        criteria.setEmail((StringFilter) new StringFilter().setEquals("test@example.com"));
        assertTrue(criteria.optionalEmail().isPresent());
        assertEquals("test@example.com", criteria.optionalEmail().orElseThrow().getEquals());
    }

    @Test
    void testDistinct() {
        UserCriteria criteria = new UserCriteria();
        assertTrue(criteria.distinct());
        criteria.setDistinct(false);
        assertFalse(criteria.distinct());
    }

    @Test
    void testGetCreatedById() {
        UUID uuid = UUID.randomUUID();
        UserCriteria criteria = new UserCriteria();
        UUIDFilter createdByIdFilter = new UUIDFilter();
        createdByIdFilter.setEquals(uuid);
        criteria.setCreatedById(createdByIdFilter);

        assertEquals(uuid, criteria.getCreatedById().getEquals());
    }

    @Test
    void testGetRoleId() {
        UUID uuid = UUID.randomUUID();
        UserCriteria criteria = new UserCriteria();
        UUIDFilter roleIdFilter = new UUIDFilter();
        roleIdFilter.setEquals(uuid);
        criteria.setRoleId(roleIdFilter);

        assertEquals(uuid, criteria.getRoleId().getEquals());
    }

    @Test
    void testGetGroupId() {
        UUID uuid = UUID.randomUUID();

        UserCriteria criteria = new UserCriteria();
        UUIDFilter groupIdFilter = new UUIDFilter();
        groupIdFilter.setEquals(uuid);
        criteria.setGroupId(groupIdFilter);

        assertEquals(uuid, criteria.getGroupId().getEquals());
    }

    @Test
    void testEmptyOptionalMethodsForRoleIdAndGroupId() {
        UserCriteria criteria = new UserCriteria();
        assertFalse(criteria.optionalRoleId().isPresent());
        assertFalse(criteria.optionalGroupId().isPresent());
    }

    @Test
    void testCopy() {
        UserCriteria original = new UserCriteria();
        original.setEmail((StringFilter) new StringFilter().setEquals("email@example.com"));
        original.setFirstName((StringFilter) new StringFilter().setEquals("John"));

        UserCriteria copy = original.copy();

        assertEquals(original.getEmail(), copy.getEmail());
        assertEquals(original.getFirstName(), copy.getFirstName());
        assertNotSame(original, copy);
    }
}
