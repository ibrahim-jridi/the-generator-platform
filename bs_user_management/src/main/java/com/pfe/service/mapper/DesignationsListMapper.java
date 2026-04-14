package com.pfe.service.mapper;

import com.pfe.domain.DesignationsList;
import com.pfe.service.dto.DesignationsListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupMapper.class, RoleMapper.class})
public interface DesignationsListMapper extends EntityMapper<DesignationsListDTO, DesignationsList> {
    @Override
    @Mapping(source = "pmUser", target = "pmUser")
    @Mapping(source = "designatedUser", target = "designatedUser")
    @Mapping(source = "role", target = "role")
    DesignationsListDTO toDto(DesignationsList entity);

    @Override
    DesignationsList toEntity(DesignationsListDTO dto);
}
