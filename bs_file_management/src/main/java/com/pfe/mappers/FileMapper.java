package com.pfe.mappers;

import com.pfe.domain.File;
import com.pfe.dto.FileDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface FileMapper extends EntityMapper<FileDto, File> {

  @Override
  FileDto toDto(File file);

  List<FileDto> toDtoList(List<File> files);

}
