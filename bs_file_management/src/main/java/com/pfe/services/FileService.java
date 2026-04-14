package com.pfe.services;

import com.pfe.dto.FileDto;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  FileDto uploadFile(MultipartFile file, String path);

  //    List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
  FileDto uploadFileFromReport(MultipartFile file);

  String downloadFile(String fileName) throws Exception;

  void removeFile(UUID fileId) throws Exception;

  InputStream getFile(String fileName) throws Exception;

  List<FileDto> getFiles() throws Exception;

  List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws Exception;

  void renameFile(UUID fileId, String newName) throws Exception;

  FileDto getFileById(UUID fileId);
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
