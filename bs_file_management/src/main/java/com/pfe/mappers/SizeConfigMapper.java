package com.pfe.mappers;

import com.pfe.domain.SizeConfig;
import com.pfe.dto.SizeConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface SizeConfigMapper extends EntityMapper<SizeConfigDto, SizeConfig> {

}
