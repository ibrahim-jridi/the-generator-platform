package com.pfe.service.dto.request;

import java.io.Serializable;
import java.util.UUID;

public class CreateFolderRequest implements Serializable {

    private static final long serialVersionUID = -9061100062276828208L;
    private UUID parentId;
    private String newFolderName;

    public CreateFolderRequest() {
    }

    public CreateFolderRequest(UUID parentId, String newFolderName) {
        this.parentId = parentId;
        this.newFolderName = newFolderName;
    }

    public UUID getParentId() {
        return this.parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getNewFolderName() {
        return this.newFolderName;
    }

    public void setNewFolderName(String newFolderName) {
        this.newFolderName = newFolderName;
    }
}

