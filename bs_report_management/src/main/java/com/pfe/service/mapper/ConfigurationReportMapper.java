package com.pfe.service.mapper;

import com.pfe.domain.ConfigurationReport;
import com.pfe.service.dto.ConfigurationReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigurationReport} and its DTO {@link ConfigurationReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigurationReportMapper extends EntityMapper<ConfigurationReportDTO, ConfigurationReport> {}
