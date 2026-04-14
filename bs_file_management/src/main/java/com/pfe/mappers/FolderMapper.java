package com.pfe.mappers;

import com.pfe.domain.Folder;
import com.pfe.dto.FolderDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface FolderMapper extends EntityMapper<FolderDto, Folder> {

  FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);


  @Override
  FolderDto toDto(Folder folder);

  List<FolderDto> toDtoList(List<Folder> folders);


}
