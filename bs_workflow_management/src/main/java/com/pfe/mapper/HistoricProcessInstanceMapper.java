package com.pfe.mapper;

import com.pfe.dto.HistoricProcessInstanceDTO;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface HistoricProcessInstanceMapper {

    HistoricProcessInstanceMapper INSTANCE = Mappers.getMapper(HistoricProcessInstanceMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "processInstanceId", source = "superProcessInstanceId"),
        @Mapping(target = "businessKey", source = "businessKey"),
        @Mapping(target = "processDefinitionId", source = "processDefinitionId"),
        @Mapping(target = "duration", source = "durationInMillis"),
        @Mapping(target = "startUserId", source = "startUserId"),
        @Mapping(target = "deleteReason", source = "deleteReason"),
        @Mapping(target = "tenantId", source = "tenantId"),
        @Mapping(target = "processDefinitionVersion", ignore = true),
        @Mapping(target = "processDefinitionName", source = "processDefinitionName")
    })
    HistoricProcessInstanceDTO toDto(HistoricProcessInstance historicProcessInstance);
}
