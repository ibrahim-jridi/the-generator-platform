package com.pfe.mapper;

import com.pfe.dto.ProcessInstanceDTO;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProcessInstanceMapper {

    ProcessInstanceMapper INSTANCE = Mappers.getMapper(ProcessInstanceMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "processInstanceId", target = "processInstanceId")
    @Mapping(source = "businessKey", target = "businessKey")
    @Mapping(source = "processDefinitionId", target = "processDefinitionId")
    @Mapping(source = "tenantId", target = "tenantId")
    @Mapping(expression = "java(processInstance.isSuspended() ? \"SUSPENDED\" : \"ACTIVE\")", target = "state")
    ProcessInstanceDTO toDto(ProcessInstance processInstance);
}
