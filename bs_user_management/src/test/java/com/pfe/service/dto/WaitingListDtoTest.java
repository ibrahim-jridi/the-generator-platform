package com.pfe.service.dto;

import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WaitingListDtoTest {

    @Test
    void testGetterAndSetter() {
        UUID id = UUID.randomUUID();
        WaitingListDTO dto = new WaitingListDTO();

        dto.setId(id);
        dto.setIdUser(UUID.fromString("c2102141-dc4e-4c13-b52e-35e665af9d7d"));
        dto.setRank(1);
        dto.setGovernorate(Governorate.ARIANA);
        dto.setDelegation("TUNIS");
        dto.setCategory(Category.CATEGORY_A);
        dto.setStatus(StatusWaitingList.REGISTRED);
        dto.setCreatedBy(UUID.randomUUID());
        dto.setCreatedDate(Instant.now());
        dto.setLastModifiedBy(UUID.randomUUID());
        dto.setLastModifiedDate(Instant.now());
        dto.setVersion(1);
        dto.setDeleted(false);

        assertEquals(id, dto.getId());
        assertEquals(UUID.fromString("c2102141-dc4e-4c13-b52e-35e665af9d7d"), dto.getIdUser());
        assertEquals(1, dto.getRank());
        assertEquals(Governorate.ARIANA, dto.getGovernorate());
        assertEquals("TUNIS", dto.getDelegation());
        assertEquals(Category.CATEGORY_A, dto.getCategory());
        assertEquals(StatusWaitingList.REGISTRED, dto.getStatus());
        assertNotNull(dto.getCreatedBy());
        assertNotNull(dto.getCreatedDate());
        assertNotNull(dto.getLastModifiedBy());
        assertNotNull(dto.getLastModifiedDate());
        assertEquals(1, dto.getVersion());
        assertFalse(dto.getDeleted());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        WaitingListDTO dto1 = new WaitingListDTO();
        dto1.setId(id);

        WaitingListDTO dto2 = new WaitingListDTO();
        dto2.setId(id);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
