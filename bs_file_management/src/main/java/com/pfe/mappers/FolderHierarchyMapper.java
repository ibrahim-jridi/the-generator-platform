package com.pfe.mappers;

import com.pfe.domain.Folder;
import com.pfe.dto.FileDto;
import com.pfe.dto.FolderDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FolderHierarchyMapper {

  // Manual mapping to avoid circular references
  public FolderDto toHierarchyDto(Folder folder) {
    if (folder == null) return null;

    FolderDto dto = new FolderDto();
    dto.setId(folder.getId());
    dto.setFolderMinioId(folder.getFolderMinioId());
    dto.setName(folder.getName());
    dto.setPath(folder.getPath());
    dto.setRecurrence(
        Long.valueOf(folder.getRecurrence() != null ? folder.getRecurrence().intValue() : null));
    dto.setUserId(folder.getUserId());
    dto.setCreatedBy(folder.getCreatedBy());
    dto.setCreatedDate(folder.getCreatedDate());
    dto.setLastModifiedBy(folder.getLastModifiedBy());
    dto.setLastModifiedDate(folder.getLastModifiedDate());
    dto.setVersion(folder.getVersion());
    dto.setDeleted(folder.getDeleted());

    // Map files without folder reference
    if (folder.getFiles() != null) {
      List<FileDto> fileDtos = folder.getFiles().stream()
          .map(file -> {
            FileDto fileDto = new FileDto();
            fileDto.setId(file.getId());
            fileDto.setFileMinioId(file.getFileMinioId());
            fileDto.setName(file.getName());
            fileDto.setPath(file.getPath());
            fileDto.setExtension(file.getExtension());
            fileDto.setType(file.getType());
            fileDto.setSize(file.getSize());
            fileDto.setCreatedBy(file.getCreatedBy());
            fileDto.setCreatedDate(file.getCreatedDate());
            // Don't set folder reference back to avoid circular reference
            return fileDto;
          })
          .collect(Collectors.toList());
      dto.setFiles(fileDtos);
    }

    // Since Folder entity doesn't have a children collection,
    // we need to query for children separately or leave folders as null
    // The folders will be loaded separately through the service

    // Map parent (only basic info to avoid recursion)
    if (folder.getParent() != null) {
      FolderDto parentDto = new FolderDto();
      parentDto.setId(folder.getParent().getId());
      parentDto.setName(folder.getParent().getName());
      parentDto.setPath(folder.getParent().getPath());
      parentDto.setFolderMinioId(folder.getParent().getFolderMinioId());
      dto.setParent(parentDto);
    }

    return dto;
  }

  public List<FolderDto> toHierarchyDtoList(List<Folder> folders) {
    if (folders == null) return new ArrayList<>();
    return folders.stream()
        .map(this::toHierarchyDto)
        .collect(Collectors.toList());
  }
}