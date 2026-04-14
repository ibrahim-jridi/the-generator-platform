package com.pfe.service.mapper;

import com.pfe.domain.WaitingList;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.service.dto.WaitingListDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;



public class WaitingListMapperTest {
    private final WaitingListMapper waitingListMapper = Mappers.getMapper(WaitingListMapper.class);

    @Test
    void testToDto() {
        WaitingList waitingList = new WaitingList();
        waitingList.setId(UUID.randomUUID());
        waitingList.setIdUser(UUID.fromString("c2102141-dc4e-4c13-b52e-35e665af9d7d"));
        waitingList.setRank(1);
        waitingList.setGovernorate(Governorate.ARIANA);
        waitingList.setDelegation("TUNIS");
        waitingList.setCategory(Category.CATEGORY_A);
        waitingList.setStatus(StatusWaitingList.REGISTRED);

        WaitingListDTO waitingListDTO = waitingListMapper.toDto(waitingList);

        Assertions.assertEquals(waitingList.getId(), waitingListDTO.getId());
        Assertions.assertEquals(waitingList.getIdUser(), waitingListDTO.getIdUser());
        Assertions.assertEquals(waitingList.getRank(), waitingListDTO.getRank());
        Assertions.assertEquals(waitingList.getGovernorate(), waitingListDTO.getGovernorate());
        Assertions.assertEquals(waitingList.getDelegation(), waitingListDTO.getDelegation());
        Assertions.assertEquals(waitingList.getCategory(), waitingListDTO.getCategory());
        Assertions.assertEquals(waitingList.getStatus(), waitingListDTO.getStatus());

    }

    @Test
    void testToEntity() {
        WaitingListDTO waitingListDTO = new WaitingListDTO();
        waitingListDTO.setId(UUID.randomUUID());
        waitingListDTO.setIdUser(UUID.fromString("c2102141-dc4e-4c13-b52e-35e665af9d7d"));
        waitingListDTO.setRank(1);
        waitingListDTO.setGovernorate(Governorate.ARIANA);
        waitingListDTO.setDelegation("TUNIS");
        waitingListDTO.setCategory(Category.CATEGORY_A);
        waitingListDTO.setStatus(StatusWaitingList.REGISTRED);

        WaitingList waitingList = waitingListMapper.toEntity(waitingListDTO);

        Assertions.assertEquals(waitingListDTO.getId(), waitingList.getId());
        Assertions.assertEquals(waitingListDTO.getIdUser(), waitingList.getIdUser());
        Assertions.assertEquals(waitingListDTO.getRank(), waitingList.getRank());
        Assertions.assertEquals(waitingListDTO.getGovernorate(), waitingList.getGovernorate());
        Assertions.assertEquals(waitingListDTO.getDelegation(), waitingList.getDelegation());
        Assertions.assertEquals(waitingListDTO.getCategory(), waitingList.getCategory());
        Assertions.assertEquals(waitingListDTO.getStatus(), waitingList.getStatus());

    }
}
