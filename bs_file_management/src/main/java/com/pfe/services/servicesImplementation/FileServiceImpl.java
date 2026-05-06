package com.pfe.services.servicesImplementation;

import com.pfe.config.Constants;
import com.pfe.domain.File;
import com.pfe.domain.Folder;
import com.pfe.dto.FileDto;
import com.pfe.mappers.FileMapper;
import com.pfe.mappers.FolderHierarchyMapper;
import com.pfe.mappers.FolderMapper;
import com.pfe.repository.FileRepository;
import com.pfe.repository.FolderRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.services.FileService;
import com.pfe.services.FolderService;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FolderService folderService;

    @Autowired
    FolderMapper folderMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    MinioClient minioClient;
    @Autowired
    private FolderHierarchyMapper folderHierarchyMapper;

    @Value("${bs-app.minio.accessKey}")
    private String minioUsername;

    @Value("${bs-app.minio.endpoint}")
    private String minioUrl;

    /*** Create bucket in MinIO if it does not exist ***/
    public void createBucketIfNotExists(UUID bucketName)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        boolean isExist = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(String.valueOf(bucketName)).build());
        if (!isExist) {
            minioClient.makeBucket(
                MakeBucketArgs.builder().bucket(String.valueOf(bucketName)).build());
        }
    }

    /*** Upload file to MinIO && save in DB ***/
    @Override
    public FileDto uploadFile(MultipartFile file, String path)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        LOGGER.info("Uploading file '{}' to path: '{}'", file.getOriginalFilename(), path);

        // Parse path
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String[] parts = cleanPath.split("/", 2);
        String bucketName = parts[0];
        String folderPath = parts.length > 1 ? parts[1] : "";

        if (!folderPath.isEmpty() && !folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }

        // Verify bucket exists
        boolean bucketExists = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            throw new RuntimeException("Bucket does not exist: " + bucketName);
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IOException("File name is missing.");
        }

        // Generate unique filename
        String fileExtension = getFileExtension(originalFileName);
        String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String uniqueFileName = fileNameWithoutExt + "_" + System.currentTimeMillis() + "." + fileExtension;

        // Build MinIO object path
        String objectPath = folderPath + uniqueFileName;
        String fullPath = "/" + bucketName + "/" + objectPath;

        // Upload to MinIO
        try (InputStream targetStream = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath)
                    .stream(targetStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        }

        // Create File entity WITHOUT circular reference
        File fileEntity = new File();
        fileEntity.setId(UUID.randomUUID());
        fileEntity.setFileMinioId(UUID.nameUUIDFromBytes(uniqueFileName.getBytes()));
        fileEntity.setName(uniqueFileName);
        fileEntity.setPath(fullPath);
        fileEntity.setExtension(fileExtension);
        fileEntity.setType(file.getContentType());
        fileEntity.setSize(String.valueOf(file.getSize()));
        fileEntity.setInWorkflow(false);

        // Find and set parent folder if exists (to maintain relationship, but mapper will ignore it when converting back)
        String folderDbPath = "/" + bucketName + "/" + folderPath;
        Optional<Folder> folderOpt = folderRepository.findByPath(folderDbPath);
        if (folderOpt.isPresent()) {
            fileEntity.setFolder(folderOpt.orElseThrow());
        }

        // Save to database
        File savedFile = fileRepository.save(fileEntity);
        LOGGER.info("File uploaded successfully: {}", savedFile.getId());

        // Convert to DTO without circular reference
        FileDto fileDto = new FileDto();
        fileDto.setId(savedFile.getId());
        fileDto.setFileMinioId(savedFile.getFileMinioId());
        fileDto.setName(savedFile.getName());
        fileDto.setPath(savedFile.getPath());
        fileDto.setExtension(savedFile.getExtension());
        fileDto.setType(savedFile.getType());
        fileDto.setSize(savedFile.getSize());
        // Don't set folder in DTO to avoid circular reference

        return fileDto;
    }



    /*** Upload file from report to MinIO && save in DB ***/
    @Override
    public FileDto uploadFileFromReport(MultipartFile file)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IOException("File name is missing.");
        }

        UUID fileUuid = UUID.nameUUIDFromBytes(originalFileName.getBytes());
        String fullPath = Constants.FOLDER_TEMPLATES + "/" + originalFileName;

        InputStream targetStream = file.getInputStream();
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(Constants.BUCKET_CONFIG)
                .object(fullPath)
                .stream(targetStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        FileDto fileDto = new FileDto();
        fileDto.setFileMinioId(fileUuid);
        fileDto.setName(originalFileName);
        fileDto.setPath(fullPath);
        fileDto.setExtension(getFileExtension(originalFileName));
        fileDto.setType(file.getContentType());
        fileDto.setSize(String.valueOf(file.getSize()));
        fileDto.setInWorkflow(false);
        File fileEntity = fileMapper.toEntity(fileDto);

        File savedFile = fileRepository.save(fileEntity);
        return fileMapper.toDto(savedFile);
    }

    /*** Upload multiple files to MinIO && save in DB ***/
    @Override
    public List<FileDto> uploadFiles(List<MultipartFile> files, String path)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, FileUploadException {
        List<FileDto> filesDto = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDto uploadedFileDto = uploadFile(file, path);
            if (uploadedFileDto != null) {
                filesDto.add(uploadedFileDto);
            } else {
                throw new FileUploadException("Failed to upload file: " + file.getOriginalFilename());
            }
        }
        return filesDto;
    }

    /*** Download file from MinIO as Base64 ***/
    @Override
    public String downloadFile(String fileName)
        throws Exception {
        String objectName = findFile(fileName);
        if (objectName == null) {
            throw new FileNotFoundException("File not found in MinIO: " + fileName);
        }

        try (InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minioUsername)
                .object(objectName)
                .build())) {
            byte[] fileBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (MinioException e) {
            LOGGER.error("MinIO error when downloading the file: {}", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            LOGGER.error("I/O error when downloading the file: {}", e.getMessage(), e);
            throw e;
        }
    }

    /*** Delete file from MinIO && DB ***/
    @Override
    public void removeFile(String fileName) throws Exception {
        LOGGER.info("Removing file: {}", fileName);

        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucket.name())
                    .recursive(true)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir() && item.objectName().endsWith("/" + fileName)) {
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(bucket.name())
                            .object(item.objectName())
                            .build());
                    LOGGER.info("File deleted successfully: {}", fileName);
                    return;
                }
            }
        }

        throw new RuntimeException("File not found: " + fileName);
    }

    /*** Get file InputStream from MinIO ***/
    @Override
    public InputStream getFile(String fileName)
        throws Exception {
        Map<String, Object> hierarchy = new HashMap<>();
        listObjects(minioClient, minioUsername, "", hierarchy);
        printHierarchy(hierarchy, 0);

        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(minioUsername).recursive(true).build());

        Optional<Item> optionalItem = Optional.empty();
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                if (item.objectName().endsWith("/" + fileName) || item.objectName().equals(fileName)) {
                    optionalItem = Optional.of(item);
                    break;
                }
            } catch (Exception e) {
                LOGGER.error("Error reading MinIO result: {}", e.getMessage(), e);
            }
        }

        Item foundItem = optionalItem
            .orElseThrow(() -> new FileNotFoundException("File not found: " + fileName));

        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minioUsername)
                .object(foundItem.objectName())
                .build());
    }

    /*** Get all files from MinIO ***/
    @Override
    public List<FileDto> getFiles()
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        List<FileDto> fileDtoList = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(minioUsername).recursive(true).build());

        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                if (!item.isDir()) {
                    String[] parts = item.objectName().split("/");
                    String fileUUID = parts[parts.length - 1];

                    FileDto fileDto = new FileDto();
                    try {
                        fileDto.setId(UUID.fromString(fileUUID));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Skipping file with non-UUID name: {}", fileUUID);
                        continue;
                    }
                    fileDto.setName(item.objectName());
                    fileDto.setPath(item.objectName());
                    fileDto.setExtension(getFileExtension(item.objectName()));

                    try {
                        StatObjectResponse stat = minioClient.statObject(
                            StatObjectArgs.builder()
                                .bucket(minioUsername)
                                .object(item.objectName())
                                .build());
                        fileDto.setType(stat.contentType());
                    } catch (MinioException e) {
                        LOGGER.error("Error retrieving metadata for file {}: {}", item.objectName(), e.getMessage(), e);
                        fileDto.setType("unknown");
                    }

                    fileDto.setSize(String.valueOf(item.size()));
                    fileDto.setInWorkflow(false);
                    fileDtoList.add(fileDto);
                }
            } catch (Exception e) {
                LOGGER.error("Error reading MinIO item: {}", e.getMessage(), e);
            }
        }

        return fileDtoList;
    }

    /*** Rename file in DB ***/
    @Override
    public void renameFile(String oldFileName, String newFileName) throws Exception {
        LOGGER.info("Renaming file from '{}' to '{}'", oldFileName, newFileName);

        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucket.name())
                    .recursive(true)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir() && item.objectName().endsWith("/" + oldFileName)) {
                    String oldPath = item.objectName();
                    String newPath = oldPath.replace(oldFileName, newFileName);
                    String bucketName = bucket.name();

                    // Copy to new name
                    minioClient.copyObject(
                        CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newPath)
                            .source(CopySource.builder()
                                .bucket(bucketName)
                                .object(oldPath)
                                .build())
                            .build());

                    // Delete old file
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(oldPath)
                            .build());

                    LOGGER.info("File renamed successfully");
                    return;
                }
            }
        }

        throw new RuntimeException("File not found: " + oldFileName);
    }

    /*** Move file to another folder in MinIO && DB ***/
    @Override
    public FileDto moveFile(UUID fileId, UUID targetFolderId)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
        NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

        Folder targetFolder = folderRepository.findById(targetFolderId)
            .orElseThrow(() -> new RuntimeException("Target folder not found with ID: " + targetFolderId));

        String oldPath = file.getPath();
        String fileName = oldPath.substring(oldPath.lastIndexOf("/") + 1);
        String newPath = targetFolderId + "/" + fileName;

        // Copy object to new path in MinIO
        try (InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minioUsername)
                .object(oldPath)
                .build())) {
            byte[] fileBytes = inputStream.readAllBytes();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioUsername)
                    .object(newPath)
                    .stream(new ByteArrayInputStream(fileBytes), fileBytes.length, -1)
                    .build());
        }

        // Remove old object from MinIO
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(minioUsername)
                .object(oldPath)
                .build());

        // Update DB record
        file.setPath(newPath);
        file.setFolder(targetFolder);
        File savedFile = fileRepository.save(file);
        return fileMapper.toDto(savedFile);
    }

    /*** Get file by ID — look up path in DB, then stream metadata from MinIO ***/
    @Override
    public FileDto getFileById(UUID fileId)
        throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // We still use DB only to resolve the stored path, then fetch live metadata from MinIO
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

        StatObjectResponse stat = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(minioUsername)
                .object(file.getPath())
                .build());

        FileDto fileDto = new FileDto();
        fileDto.setId(fileId);
        fileDto.setName(file.getName());
        fileDto.setPath(file.getPath());
        fileDto.setExtension(getFileExtension(file.getName()));
        fileDto.setType(stat.contentType());
        fileDto.setSize(String.valueOf(stat.size()));
        fileDto.setInWorkflow(false);
        return fileDto;
    }

    /*** Get all files under a folder prefix directly from MinIO ***/
    @Override
    public List<FileDto> getFilesByFolderId(UUID folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        // Derive bucket and prefix from the folder path (e.g. /bucket/sub/folder)
        String path = folder.getPath();
        String[] parts = path.split("/", 3); // ["", "bucket", "rest"]
        String bucket = parts[1];
        String prefix = parts.length > 2 ? parts[2] + "/" : "";

        List<FileDto> fileDtoList = new ArrayList<>();
        // delimiter("/") is required so MinIO returns common prefixes as virtual
        // directories and only exposes real objects at this level as files.
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
            .bucket(bucket)
            .delimiter("/")
            .recursive(false);
        if (prefix != null && !prefix.isEmpty()) {
            builder = builder.prefix(prefix);
        }
        Iterable<Result<Item>> results = minioClient.listObjects(builder.build());

        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                // Skip sub-folder prefixes and zero-byte folder placeholder objects
                if (!item.isDir() && !(item.objectName().endsWith("/") && item.size() == 0)) {
                    String objectName = item.objectName();
                    String fileName = objectName.contains("/")
                        ? objectName.substring(objectName.lastIndexOf("/") + 1)
                        : objectName;

                    FileDto fileDto = new FileDto();
                    fileDto.setName(fileName);
                    fileDto.setPath("/" + bucket + "/" + objectName);
                    fileDto.setExtension(getFileExtension(fileName));
                    fileDto.setSize(String.valueOf(item.size()));
                    fileDto.setInWorkflow(false);

                    try {
                        StatObjectResponse stat = minioClient.statObject(
                            StatObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .build());
                        fileDto.setType(stat.contentType());
                    } catch (MinioException e) {
                        LOGGER.error("Error retrieving metadata for {}: {}", objectName, e.getMessage(), e);
                        fileDto.setType("unknown");
                    }

                    fileDtoList.add(fileDto);
                }
            } catch (Exception e) {
                LOGGER.error("Error reading MinIO item: {}", e.getMessage(), e);
            }
        }
        return fileDtoList;
    }

    public int countOccurrences(String path, char character) {
        return (int) path.chars().filter(ch -> ch == character).count();
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private String constructFileUrl(String bucketName, String fileName) {
        return String.format("%s/%s/%s", minioUrl, bucketName, fileName);
    }

    private String findFile(String fileName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(minioUsername).recursive(true).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            if (!item.isDir() && item.objectName().endsWith("/" + fileName)) {
                return item.objectName();
            }
        }
        return null;
    }

    private String findFileByName(String fileName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(minioUsername)
                .recursive(true)
                .build());
        for (Result<Item> result : results) {
            Item item = result.get();
            String itemName = item.objectName();
            if (!item.isDir() && (itemName.equals(fileName) || itemName.endsWith("/" + fileName))) {
                return itemName;
            }
        }
        return null;
    }

    private String cleanEtag(String etag) {
        if (etag != null && etag.length() > 2 && etag.startsWith("\"") && etag.endsWith("\"")) {
            return etag.substring(1, etag.length() - 1);
        }
        return etag;
    }

    private static void listObjects(MinioClient minioClient, String bucketName, String prefix,
        Map<String, Object> hierarchy) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(false).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            if (item.isDir()) {
                Map<String, Object> subHierarchy = new HashMap<>();
                hierarchy.put(objectName, subHierarchy);
                listObjects(minioClient, bucketName, objectName, subHierarchy);
            } else {
                hierarchy.put(objectName, null);
            }
        }
    }

    private static void printHierarchy(Map<String, Object> hierarchy, int level) {
        Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
        for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("  ");
            }
            logger.info("{}{}", indent, entry.getKey());
            if (entry.getValue() instanceof Map) {
                printHierarchy((Map<String, Object>) entry.getValue(), level + 1);
            }
        }
    }

}