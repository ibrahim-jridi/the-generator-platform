package com.pfe.service.mapper;

import com.pfe.domain.ActivityType;
import com.pfe.service.dto.ActivityTypeDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityTypeMapper extends EntityMapper<ActivityTypeDTO, ActivityType> {

  @Override
  @Mapping(target = "id", source = "id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "code", source = "code")
  @Mapping(target = "name", source = "name")
  ActivityTypeDTO toDto(ActivityType activityType);

  Set<ActivityTypeDTO> toDto(Set<ActivityType> activityTypes);

  @Override
  default List<ActivityTypeDTO> toDto(List<ActivityType> activityTypes) {
    return activityTypes.stream().map(this::toDto).collect(Collectors.toList());
  }
}
