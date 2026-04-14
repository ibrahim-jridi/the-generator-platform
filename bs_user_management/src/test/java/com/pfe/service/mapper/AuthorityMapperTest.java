package com.pfe.service.mapper;

import com.pfe.domain.Authority;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthorityMapperTest {

    private final AuthorityMapper mapper = Mappers.getMapper(AuthorityMapper.class);

    @Test
    void testToDto() {
        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setLabel("Test Label");

        AuthorityDTO dto = this.mapper.toDto(authority);

        assertEquals(authority.getId(), dto.getId());
        assertEquals(authority.getLabel(), dto.getLabel());
    }

    @Test
    void testToEntity() {
        AuthorityDTO dto = new AuthorityDTO();
        dto.setId(UUID.randomUUID());
        dto.setLabel("Test Label");

        Authority entity = this.mapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getLabel(), entity.getLabel());
    }

    @Test
    void testToDtoInternalUserId() {
        User user = new User();
        user.setId(UUID.randomUUID());

        UserDTO userDto = this.mapper.toDtoUserId(user);

        assertEquals(user.getId(), userDto.getId());
    }

    @Test
    void testToDtoRoleId() {
        Role role = new Role();
        role.setId(UUID.randomUUID());

        RoleDTO roleDto = this.mapper.toDtoRoleId(role);

        assertEquals(role.getId(), roleDto.getId());
    }

    @Test
    void testToDtoRoleIdSet() {
        Set<Role> roles = Stream.of(new Role(), new Role())
            .peek(role -> role.setId(UUID.randomUUID()))
            .collect(Collectors.toSet());

        Set<RoleDTO> roleDtos = this.mapper.toDtoRoleIdSet(roles);

        assertEquals(roles.size(), roleDtos.size());
        roleDtos.forEach(dto -> assertNotNull(dto.getId()));
    }
}
