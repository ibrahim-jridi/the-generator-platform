package com.pfe.mappers;

import com.pfe.domain.File;
import com.pfe.dto.FileDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface FileMapper extends EntityMapper<FileDto, File> {

  @Override
  @Mapping(target = "folder", ignore = true)  // IMPORTANT: Ignore folder to break circular reference
  FileDto toDto(File file);

  @Override
  @Mapping(target = "folder", ignore = true)  // IMPORTANT: Ignore folder to break circular reference
  File toEntity(FileDto fileDto);

  List<FileDto> toDtoList(List<File> files);

}
