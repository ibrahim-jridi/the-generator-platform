package com.pfe.dto.requests;

public class CreateFolderByPathRequest {

    private String parentPath;
    private String folderName;
    // getters + setters

  public CreateFolderByPathRequest() {
  }

  public CreateFolderByPathRequest(String parentPath, String folderName) {
    this.parentPath = parentPath;
    this.folderName = folderName;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }
}
