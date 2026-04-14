package com.pfe.service.mapper;

import com.pfe.domain.Authority;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {

    @Override
    AuthorityDTO toDto(Authority s);

    @Override
    Authority toEntity(AuthorityDTO authorityDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> role) {
        return role.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }

    @Named("createdBy")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userId")
    default UUID map(UUID value) {
        return value;
    }
}
