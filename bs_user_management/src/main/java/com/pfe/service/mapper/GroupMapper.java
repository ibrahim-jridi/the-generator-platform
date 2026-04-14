package com.pfe.service.mapper;

import com.pfe.domain.Group;
import com.pfe.domain.User;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.dto.UserDTO;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Group} and its DTO {@link GroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {

    @Override
    @Mapping(target = "parent", source = "parent", qualifiedByName = "groupIdOrNull")
    GroupDTO toDto(Group s);

    @Override
    @Mapping(target = "parent", source = "parent", qualifiedByName = "groupDtoIdOrNull")
    Group toEntity(GroupDTO groupDTO);

    @Named("groupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupDTO toDtoGroupId(Group group);

    @Named("groupIdOrNull")
    default GroupDTO toDtoGroupIdOrNull(Group group) {
        if (group == null) {
            return null;
        }
        return toDtoGroupId(group);
    }

    @Named("groupDtoIdOrNull")
    default Group toEntityGroupIdOrNull(GroupDTO group) {
        if (group == null) {
            return null;
        }
        return toEntity(group);
    }

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }

    @Named("createdBy")
    default UUID map(UUID value) {
        return value;
    }

    Set<Group> toEntity(Set<GroupDTO> groups);
}
