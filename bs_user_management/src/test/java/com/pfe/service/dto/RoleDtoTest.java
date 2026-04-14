package com.pfe.service.dto;

import com.pfe.domain.Role;
import com.pfe.service.mapper.AuthorityMapper;
import com.pfe.service.mapper.RoleMapper;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class RoleDtoTest {


    @InjectMocks
    private RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    @Mock
    private AuthorityMapper authorityMapper;

    @Test
    void testToDto() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setIsActive(true);
        role.setLabel("Admin");
        role.setDescription("Administrator role with all permissions.");
        role.setVersion(1);
        role.setDeleted(false);

        RoleDTO roleDTO = this.roleMapper.toDto(role);

        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals(role.getId(), roleDTO.getId());
        Assertions.assertEquals(role.getLabel(), roleDTO.getLabel());
        Assertions.assertEquals(role.getDescription(), roleDTO.getDescription());
        Assertions.assertEquals(role.getIsActive(), roleDTO.getIsActive());
        Assertions.assertEquals(role.getVersion(), roleDTO.getVersion());
    }

    @Test
    void testToEntity() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(UUID.randomUUID());
        roleDTO.setIsActive(true);
        roleDTO.setLabel("Admin");
        roleDTO.setDescription("Administrator role with all permissions.");
        roleDTO.setVersion(1);

        Role role = this.roleMapper.toEntity(roleDTO);

        Assertions.assertNotNull(role);
        Assertions.assertEquals(roleDTO.getId(), role.getId());
        Assertions.assertEquals(roleDTO.getLabel(), role.getLabel());
        Assertions.assertEquals(roleDTO.getDescription(), role.getDescription());
        Assertions.assertEquals(roleDTO.getIsActive(), role.getIsActive());
        Assertions.assertEquals(roleDTO.getVersion(), role.getVersion());
    }

}
