package com.pfe.service.mapper;

import com.pfe.domain.Group;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.service.dto.GroupDTO;
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
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper(componentModel = "spring", uses = {GroupMapper.class, RoleMapper.class,
    ActivityTypeMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

  @Override
  @Mapping(target = "roles", source = "roles")
  @Mapping(target = "groups", source = "groups")
  @Mapping(target = "activitiesType", source = "activitiesType")
  UserDTO toDto(User s);

  @Override
  @Mapping(target = "removeRole", ignore = true)
  @Mapping(target = "removeGroup", ignore = true)
  User toEntity(UserDTO userDTO);

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

  @Named("groupId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  GroupDTO toDtoGroupId(Group group);

  @Named("groupIdSet")
  default Set<GroupDTO> toDtoGroupIdSet(Set<Group> group) {
    return group.stream().map(this::toDtoGroupId).collect(Collectors.toSet());
  }

  @Named("createdBy")
  default UUID map(UUID value) {
    return value;
  }
}
