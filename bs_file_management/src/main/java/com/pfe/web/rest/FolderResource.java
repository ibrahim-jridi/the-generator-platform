package com.pfe.web.rest;

import com.pfe.dto.FolderDto;
import com.pfe.dto.requests.CreateFolderRequest;
import com.pfe.repository.FolderRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.security.SecurityUtils;
import com.pfe.services.FolderService;
import com.pfe.domain.Folder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link Folder}.
 */
@RestController
@RequestMapping("/api/v1/folder")
@Transactional
public class FolderResource {

  private static final Logger log = LoggerFactory.getLogger(FolderResource.class);

  private static final String ENTITY_NAME = "bsFileManagementFolder";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;
  private final FolderService folderService;

  private final FolderRepository folderRepository;

  public FolderResource(FolderRepository folderRepository, FolderService folderService) {
    this.folderRepository = folderRepository;
    this.folderService = folderService;
  }

  /**
   * {@code POST /create} : Create a new folder.
   *
   * @param request the dto of the folder to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   * folder, or with status {@code 400 (Bad Request)} if the input is invalid.
   * @throws IOException if an I/O error occurs.
   */
  @PostMapping("/create")
//@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
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

  /**
   * {@code POST /createChildFolder} : Create a new folder under a parent folder.
   *

   * @param request      the dto of the parent folder.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   * folder, or with status {@code 400 (Bad Request)} if the input is invalid.
   * @throws Exception if an error occurs during folder creation.
   */
  @PostMapping("/createChildFolder")
//@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<FolderDto> createFolderUnderParent(
        @RequestBody CreateFolderRequest request) throws Exception {

      if (request.getNewFolderName() == null ||
          request.getNewFolderName().trim().isEmpty() ||
          request.getParentId() == null) {
          throw new IllegalArgumentException(
              "newFolderName , userId and parentId cannot be null or empty:" + request.getNewFolderName());

      }
      FolderDto folder = this.folderService.createFolderInBucket(request.getParentId(), request.getNewFolderName());
      return ResponseEntity.ok(folder);

  }

  /**
   * {@code GET /} : Get all folders.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of folders.
   */
  @GetMapping("")
//@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<FolderDto>> listFolders() {
    try {
      List<FolderDto> folderList = this.folderService.listFolders();
      return ResponseEntity.ok(folderList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  /**
   * {@code DELETE /delete/{folderName}} : Delete the specified folder.
   *
   * @param folderName the name of the folder to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} if the folder was
   * deleted.
   */
  @DeleteMapping("/delete/{folderName}")
//@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<String> removeFolder(@PathVariable String folderName) {
    try {
      this.folderService.removeFolder(folderName);
      return ResponseEntity.ok("Folder removed successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Folder removal failed: " + e.getMessage());
    }
  }

  @GetMapping("/user")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<FolderDto> getFolderByUserID() throws Exception {
    try {
      FolderDto folderDto = this.folderService.getFolderByConnectedUser()
          .orElseThrow(() -> new Exception("Folder not found for this user "));
      return ResponseEntity.ok(folderDto);
    } catch (Exception e) {
      log.error("Error retrieving folder by user ID", e);
      throw e;
    }
  }

  @GetMapping("/by-minio-id/{id}")
//  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""
//      + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<FolderDto>> getFoldersByParentFolderMinioId(@PathVariable UUID id) {

    UUID userConnected = UUID.fromString(SecurityUtils.getUserIdFromCurrentUser());
    UUID folderMinioId = this.folderRepository.findFolderMinioIdByUserId(userConnected);
    if (id.equals(folderMinioId)) {
      List<FolderDto> folderDtos = this.folderService.getFoldersByParentFolderMinioId(id);
      if (folderDtos.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(folderDtos);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
    }
  }


}
