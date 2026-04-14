package com.pfe.mapper;

import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DmnDecisionDefinitionMapper {

    DmnDecisionDefinitionMapper INSTANCE = Mappers.getMapper(DmnDecisionDefinitionMapper.class);
    DmnActivityDecisionDefinitionDTO toDto(DecisionDefinition decisionDefinition);
}
