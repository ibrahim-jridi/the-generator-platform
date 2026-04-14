package com.pfe.mapper;
import com.pfe.dto.TaskDTO;
import org.camunda.bpm.engine.task.Task;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "executionId", source = "executionId"),
        @Mapping(target = "processInstanceId", source = "processInstanceId"),
        @Mapping(target = "processDefinitionId", source = "processDefinitionId"),
        @Mapping(target = "parentTaskId", source = "parentTaskId"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "taskDefinitionKey", source = "taskDefinitionKey"),
        @Mapping(target = "owner", source = "owner"),
        @Mapping(target = "assignee", source = "assignee"),
        @Mapping(target = "priority", source = "priority"),
        @Mapping(target = "tenantId", source = "tenantId"),
        @Mapping(target = "formKey", source = "formKey"),
        @Mapping(target = "name", source = "name"),
    })
    TaskDTO toDto(Task task);

    default Page<TaskDTO> toDto(Page<Task> page) {
        return page.map(this::toDto);
    }


//    @Mappings({
//        // Map all necessary fields
//        @Mapping(target = "id", source = "id"),
//        @Mapping(target = "executionId", source = "executionId"),
//        @Mapping(target = "processInstanceId", source = "processInstanceId"),
//        @Mapping(target = "processDefinitionId", source = "processDefinitionId"),
//        @Mapping(target = "parentTaskId", source = "parentTaskId"),
//        @Mapping(target = "description", source = "description"),
//        @Mapping(target = "taskDefinitionKey", source = "taskDefinitionKey"),
//        @Mapping(target = "owner", source = "owner"),
//        @Mapping(target = "assignee", source = "assignee"),
//        @Mapping(target = "priority", source = "priority"),
//        @Mapping(target = "tenantId", source = "tenantId"),
//        @Mapping(target = "formKey", source = "formKey")
//    })
//    Task toEntity(TaskDTO taskDTO);
}
