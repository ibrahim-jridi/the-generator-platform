package com.pfe.service.mapper;

import com.pfe.domain.Group;
import com.pfe.service.dto.GroupDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;


class GroupMapperTest {

    private final GroupMapper groupMapper = Mappers.getMapper(GroupMapper.class);

    @Test
    void testToDto() {
        Group group = new Group();
        group.setId(UUID.randomUUID());
        group.setLabel("Test Group");
        group.setDescription("Group Description");

        GroupDTO groupDTO = groupMapper.toDto(group);

        Assertions.assertEquals(group.getId(), groupDTO.getId());
        Assertions.assertEquals(group.getLabel(), groupDTO.getLabel());
        Assertions.assertEquals(group.getDescription(), groupDTO.getDescription());
    }

    @Test
    void testToEntity() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(UUID.randomUUID());
        groupDTO.setLabel("DTO Group");
        groupDTO.setDescription("DTO Description");

        Group group = groupMapper.toEntity(groupDTO);

        Assertions.assertEquals(groupDTO.getId(), group.getId());
        Assertions.assertEquals(groupDTO.getLabel(), group.getLabel());
        Assertions.assertEquals(groupDTO.getDescription(), group.getDescription());
    }
}
