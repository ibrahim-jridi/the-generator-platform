package com.pfe.mapper;

import com.pfe.dto.DeployDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.camunda.bpm.engine.repository.Deployment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeployMapper {

    DeployMapper INSTANCE = Mappers.getMapper(DeployMapper.class);

    DeployDTO toDto(Deployment deployment);
}
