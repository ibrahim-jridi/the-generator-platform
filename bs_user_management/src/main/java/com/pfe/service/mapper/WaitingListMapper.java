package com.pfe.service.mapper;

import com.pfe.domain.WaitingList;
import com.pfe.service.dto.WaitingListDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link WaitingList} and its DTO {@link WaitingListDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaitingListMapper extends EntityMapper<WaitingListDTO, WaitingList>{
}
