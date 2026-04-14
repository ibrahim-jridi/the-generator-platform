package com.pfe.web.rest;

import com.pfe.dto.FolderDto;
import com.pfe.dto.requests.CreateFolderRequest;
import com.pfe.services.FolderService;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/open-api")
public class OpenResource {

  private final FolderService folderService;

  public OpenResource(FolderService folderService) {
    this.folderService = folderService;
  }

    @PostMapping("/folder/create")
    public ResponseEntity<FolderDto> createFolder(@RequestBody CreateFolderRequest request) throws IOException {

        if (request.getNewFolderName() == null ||
            request.getNewFolderName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "folderName cannot be null or empty: " +
                    request.getNewFolderName());
        }

        FolderDto folder = this.folderService.createFolder(request);
        return ResponseEntity.ok(folder);

    }
}
