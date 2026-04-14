package com.pfe.service.mapper;

import com.pfe.domain.Role;
import com.pfe.service.dto.RoleDTO;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {AuthorityMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Override
    @Mapping(target = "authorities", source = "authorities")
    RoleDTO toDto(Role s);

    @Override
    @Mapping(target = "removeAuthority", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    Set<Role> toEntity(Set<RoleDTO> roles);
}
