package com.pfe.service.mapper;

import com.pfe.domain.ReportTemplate;
import com.pfe.service.dto.ReportTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportTemplate} and its DTO {@link ReportTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportTemplateMapper extends EntityMapper<ReportTemplateDTO, ReportTemplate> {}
