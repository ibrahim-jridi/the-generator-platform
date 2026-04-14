package com.pfe.dto.requests;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class CreateFolderRequest implements Serializable {

  private static final long serialVersionUID = 6284018689213684698L;
  private UUID parentId;
  private String newFolderName;

  public CreateFolderRequest() {
  }

  public CreateFolderRequest(UUID parentId, String newFolderName) {
    this.parentId = parentId;
    this.newFolderName = newFolderName;
  }

  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  public String getNewFolderName() {
    return newFolderName;
  }

  public void setNewFolderName(String newFolderName) {
    this.newFolderName = newFolderName;
  }
}

