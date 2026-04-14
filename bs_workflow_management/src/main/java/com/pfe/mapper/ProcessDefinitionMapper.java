package com.pfe.mapper;


import com.pfe.dto.ProcessDefinitionDTO;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProcessDefinitionMapper {
    ProcessDefinitionMapper INSTANCE = Mappers.getMapper(ProcessDefinitionMapper.class);

    @Mapping(target = "startableInTasklist", source = "startableInTasklist")
    @Mapping(target = "suspensionState", source = "suspended")
    ProcessDefinitionDTO toDto(ProcessDefinition processDefinition);

    default Page<ProcessDefinitionDTO> toDto(Page<ProcessDefinition> page) {
        return page.map(this::toDto);
    }
}
