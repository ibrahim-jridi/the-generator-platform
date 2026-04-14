package com.pfe.service.dto;

import java.io.Serializable;

public class Base64FileDTO implements Serializable {

  private static final long serialVersionUID = -6604048152186524057L;

  private byte[] fileBase64;
  private String fileName;
  private String bucketName;

  public Base64FileDTO() {
  }

  public Base64FileDTO(byte[] fileBase64, String fileName, String bucketName) {
    this.fileBase64 = fileBase64;
    this.fileName = fileName;
    this.bucketName = bucketName;
  }

  public byte[] getFileBase64() {
    return fileBase64;
  }

  public void setFileBase64(byte[] fileBase64) {
    this.fileBase64 = fileBase64;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }
}
