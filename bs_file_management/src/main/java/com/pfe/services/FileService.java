package com.pfe.services;

import com.pfe.dto.FileDto;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  FileDto uploadFile(MultipartFile file, String path)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  //    List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
  FileDto uploadFileFromReport(MultipartFile file)
      throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  String downloadFile(String fileName) throws Exception;

  void removeFile(String fileId) throws Exception;

  InputStream getFile(String fileName) throws Exception;

  List<FileDto> getFiles() throws Exception;

  List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws Exception;

  void renameFile(String oldFileName, String newFileName) throws Exception;

  FileDto moveFile(UUID fileId, UUID targetFolderId)
      throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
      NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

  FileDto getFileById(UUID fileId)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
  List<FileDto> getFilesByFolderId(UUID folderId);
//    void createFolder(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
//
//    Set<FolderDto> getFoldersbyConnectedUser(String folder) throws XmlParserException, ServerException, InvalidResponseException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException;
//    void removeObject(String objectName) throws Exception;
//
//    void downloadObject(String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
//    void createFile(String path,String xhtml) throws  IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
//
//    byte[] openFile(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException ;
//
//    byte[] openFileById(  Long id) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException ;
//    byte[] openFileByPathUuid(  String pathUuid) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException ;
//
//    FileDownloadDto downloadFile(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException ;
//    FileDownloadDto downloadFileByPath(String path) throws IOException, InvalidKeyException,
//        InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
//        InternalException, XmlParserException, ErrorResponseException;
//    Boolean checkFileNameExistence(String path, String name);
}
