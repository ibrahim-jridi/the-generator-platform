package com.pfe.mappers;

import com.pfe.domain.Folder;
import com.pfe.dto.FolderDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface FolderMapper extends EntityMapper<FolderDto, Folder> {

  FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);


  @Override
  @Mapping(target = "files", ignore = true)    // IMPORTANT: Ignore files to break circular reference
  @Mapping(target = "parent", ignore = true)   // IMPORTANT: Ignore parent to break circular reference
  FolderDto toDto(Folder folder);

  @Override
  @Mapping(target = "files", ignore = true)
  @Mapping(target = "parent", ignore = true)
  Folder toEntity(FolderDto folderDto);

  List<FolderDto> toDtoList(List<Folder> folders);


}
