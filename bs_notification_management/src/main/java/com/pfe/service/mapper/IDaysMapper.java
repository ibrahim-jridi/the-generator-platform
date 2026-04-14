package com.pfe.service.mapper;

import com.pfe.domain.Days;
import com.pfe.service.dto.DaysDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Days} and its DTO {@link DaysDTO}.
 */
@Mapper(componentModel = "spring")
public interface IDaysMapper extends EntityMapper<DaysDTO, Days> {}
