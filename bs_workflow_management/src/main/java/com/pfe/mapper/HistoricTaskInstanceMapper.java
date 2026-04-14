package com.pfe.mapper;

import com.pfe.dto.HistoricTaskInstanceDTO;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface HistoricTaskInstanceMapper {


    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "rootProcessInstanceId", source = "rootProcessInstanceId"),
        @Mapping(target = "processInstanceId", source = "processInstanceId"),
        @Mapping(target = "executionId", source = "executionId"),
        @Mapping(target = "processDefinitionId", source = "processDefinitionId"),
        @Mapping(target = "processDefinitionKey", source = "processDefinitionKey"),
        @Mapping(target = "processDefinitionVersion", ignore = true),
        @Mapping(target = "processDefinitionName", ignore = true),
        @Mapping(target = "caseInstanceId", source = "caseInstanceId"),
        @Mapping(target = "assignee", source = "assignee"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "taskDefinitionKey", source = "taskDefinitionKey"),
//        @Mapping(target = "activityInstanceId", source = "activityInstanceId"),
        @Mapping(target = "startTime", expression = "java(historicTaskInstance.getStartTime() != null ? ZonedDateTime.ofInstant(historicTaskInstance.getStartTime().toInstant(), ZoneId.systemDefault()) : null)"),
        @Mapping(target = "endTime", expression = "java(historicTaskInstance.getEndTime() != null ? ZonedDateTime.ofInstant(historicTaskInstance.getEndTime().toInstant(), ZoneId.systemDefault()) : null)"),
        @Mapping(target = "form", ignore = true),
        @Mapping(target = "submissionDTO", ignore = true)
    })
    HistoricTaskInstanceDTO toDto(HistoricTaskInstance historicTaskInstance);

    default Page<HistoricTaskInstanceDTO> toDto(Page<HistoricTaskInstance> page) {
        return page.map(this::toDto);
    }

}

