package com.pfe.services;

import com.pfe.dto.FolderDto;
import com.pfe.dto.requests.CreateFolderRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderService {

  FolderDto createFolder(CreateFolderRequest request) throws IOException;

  FolderDto createFolderInBucket(UUID parentId, String newFolderName) throws Exception;

  List<FolderDto> listFolders() throws Exception;

  void removeFolder(String folderName) throws Exception;

  Optional<FolderDto> getFolderByConnectedUser();

  List<FolderDto> getFoldersByParentFolderMinioId(UUID id);

}
