package com.pfe.services.servicesImplementation;


import com.pfe.config.Constants;
import com.pfe.domain.File;
import com.pfe.domain.Folder;
import com.pfe.dto.FileDto;
import com.pfe.mappers.FileMapper;
import com.pfe.mappers.FolderMapper;
import com.pfe.repository.FileRepository;
import com.pfe.repository.FolderRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.services.FileService;
import com.pfe.services.FolderService;
import io.minio.BucketExistsArgs;
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
import io.minio.messages.Item;
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
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);


    final FolderService folderService;

    final FolderMapper folderMapper;

    private final FileMapper fileMapper;

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    private MinioClient minioClient;

    public FileServiceImpl(FolderService folderService, FolderMapper folderMapper,
        FileMapper fileMapper, FileRepository fileRepository, MinioClient minioClient,
        FolderRepository folderRepository) {
        this.folderService = folderService;
        this.folderMapper = folderMapper;
        this.fileMapper = fileMapper;
        this.fileRepository = fileRepository;
        this.minioClient = minioClient;
        this.folderRepository = folderRepository;
    }

    @Value("${bs-app.minio.accessKey}")
    private String minioUsername;
    @Value("${bs-app.minio.endpoint}")
    private String minioUrl;

    public void createBucketIfNotExists(UUID bucketName)
        throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        boolean isExist = this.minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(String.valueOf(bucketName)).build());
        if (!isExist) {
            this.minioClient.makeBucket(
                MakeBucketArgs.builder().bucket(String.valueOf(bucketName)).build());
        }
    }

    @Override
    public FileDto uploadFile(MultipartFile file, String path) {
        try {
            UUID userId = UUID.fromString(SecurityUtils.getUserIdFromCurrentUser());
            Folder folder = this.folderRepository.findByName(Constants.BUCKET_NAME).orElseThrow();
            createBucketIfNotExists(folder.getFolderMinioId());

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null) {
                throw new IOException("File name is missing.");
            }
            UUID fileUuid = UUID.nameUUIDFromBytes(originalFileName.getBytes());

            String fullPath = path + "/" + fileUuid;

            this.minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(folder.getName())
                    .object(fullPath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            FileDto fileDto = new FileDto();
            fileDto.setFileMinioId(fileUuid);
            fileDto.setName(originalFileName);
            fileDto.setPath(fullPath);
            fileDto.setExtension(getFileExtension(originalFileName));
            fileDto.setType(file.getContentType());
            fileDto.setSize(String.valueOf(file.getSize()));
            fileDto.setInWorkflow(false);
            File fileEntity = this.fileMapper.toEntity(fileDto);

            File savedFile = this.fileRepository.save(fileEntity);
            return this.fileMapper.toDto(savedFile);
        }
       catch (IOException | InvalidKeyException | NoSuchAlgorithmException | MinioException e) {
                System.err.println("Error uploading file: " + e.getMessage());
                throw new RuntimeException("Failed to upload file to MinIO", e);
            }
    }

    @Override
    public FileDto uploadFileFromReport(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IOException("File name is missing.");
            }
                UUID fileUuid = UUID.nameUUIDFromBytes(originalFileName.getBytes());

                String fullPath = Constants.FOLDER_TEMPLATES + "/ " + file.getOriginalFilename();
                    this.minioClient.putObject(
                        PutObjectArgs.builder()
                            .bucket(Constants.BUCKET_CONFIG)
                            .object(fullPath)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
                    );
                FileDto fileDto = new FileDto();
                fileDto.setFileMinioId(fileUuid);
                fileDto.setName(originalFileName);
                fileDto.setPath(fullPath);
                fileDto.setExtension(getFileExtension(originalFileName));
                fileDto.setType(file.getContentType());
                fileDto.setSize(String.valueOf(file.getSize()));
                fileDto.setInWorkflow(false);
                File fileEntity = this.fileMapper.toEntity(fileDto);

                File savedFile = this.fileRepository.save(fileEntity);
                return this.fileMapper.toDto(savedFile);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | MinioException e) {
            System.err.println("Error uploading file: " + e.getMessage());
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

//    @Override
//    public List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        String bucketName = minioUsername;
//        createBucketIfNotExists(bucketName);
//
//        List<FileDto> fileDtos = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String fileName = file.getOriginalFilename();
//            try {
//                minioClient.putObject(
//                    PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(fileName)
//                        .stream(file.getInputStream(), file.getSize(), -1)
//                        .contentType(file.getContentType())
//                        .build()
//                );
//
//                // Construct the file URL
//                String fileUrl = constructFileUrl(bucketName, fileName);
//
//                FileDto fileDto = FileDto.builder()
//                    .name(fileName)
//                    .path(fileUrl)
//                    .build();
//
//                fileDtos.add(fileDto);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                // Handle exception properly in your actual code
//            }
//        }
//        return fileDtos;
//    }

    @Override
    public String downloadFile(String fileName) throws Exception {
        String objectName = findFile(fileName);
        if (objectName == null) {
            throw new Exception("File not found in MinIO");
        }

        try (InputStream inputStream = this.minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(this.minioUsername)
                .object(objectName)
                .build())) {

            byte[] fileBytes = inputStream.readAllBytes();

            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (MinioException e) {
            log.error("Minio error when downloading the file: {}", e.getMessage(), e);
            throw new MinioException("MinIO error when downloading the file: ", e.getMessage());
        } catch (IOException e) {
            log.error("I/O error when downloading the file: {}", e.getMessage(), e);
            throw new IOException("I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error when downloading the file: {}", e.getMessage(), e);
            throw new Exception("Unexpected error when downloading the file: " + e.getMessage(), e);
        }
    }


    @Override
    public void removeFile(UUID fileId) throws Exception {
        try {
            File file = this.fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with ID: " + fileId));

            this.fileRepository.delete(file);

            this.minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(this.minioUsername)
                    .object(fileId.toString())
                    .build()
            );
        } catch (FileNotFoundException e) {
            log.error("Error when removing the file: {}", e.getMessage(), e);
            throw new Exception("Error when removing the file: " + e.getMessage(), e);
        } catch (MinioException e) {
            log.error("MinIO error: {}", e.getMessage(), e);
            throw new Exception("MinIO error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new Exception("Unexpected error: " + e.getMessage(), e);
        }

    }

    @Override
    public InputStream getFile(String fileName) throws Exception {
        try {
            Map<String, Object> hierarchy = new HashMap<>();
            listObjects(this.minioClient, this.minioUsername, "", hierarchy);
            printHierarchy(hierarchy, 0);

            Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(this.minioUsername).recursive(true).build()
            );

            Optional<Item> optionalItem = Optional.empty();
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.objectName().endsWith("/" + fileName) || item.objectName()
                    .equals(fileName)) {
                    optionalItem = Optional.of(item);
                    break;
                }
            }

            Item foundItem = optionalItem.orElseThrow(() -> new Exception("File not found"));

            return this.minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(this.minioUsername)
                    .object(foundItem.objectName())
                    .build()
            );
        } catch (MinioException e) {
            log.error("Error getting file from MinIO: {}", e.getMessage(), e);
            throw new Exception("Error getting file from MinIO: " + e.getMessage());
        }
    }

    @Override
    public List<FileDto> getFiles() throws Exception {
        List<FileDto> fileDtoList = new ArrayList<>();

        try {
            Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(this.minioUsername).recursive(true).build());

            for (Result<Item> result : results) {
                Item item = result.get();

                if (!item.isDir()) {
                    String[] parts = item.objectName().split("/");
                    String fileUUID = parts[parts.length - 1];

                    FileDto fileDto = new FileDto();
                    fileDto.setId(UUID.fromString(fileUUID));
                    fileDto.setName(item.objectName());
                    fileDto.setPath(item.objectName());
                    fileDto.setExtension(getFileExtension(item.objectName()));

                    try {
                        StatObjectResponse stat = this.minioClient.statObject(
                            StatObjectArgs.builder()
                                .bucket(this.minioUsername)
                                .object(item.objectName())
                                .build());
                        fileDto.setType(stat.contentType());
                    } catch (MinioException e) {
                        log.error("Error retrieving metadata for file {}: {}", item.objectName(),
                            e.getMessage(), e);
                        fileDto.setType("unknown");
                    }

                    fileDto.setSize(String.valueOf(item.size()));
                    fileDto.setInWorkflow(false);

                    fileDtoList.add(fileDto);
                }
            }
        } catch (MinioException e) {
            log.error("Error listing files from MinIO: {}", e.getMessage(), e);
            throw new Exception("Error listing files from MinIO: " + e.getMessage());
        }

        return fileDtoList;
    }


    @Override
    public List<FileDto> uploadFiles(List<MultipartFile> files, String path) throws Exception {
        List<FileDto> filesDto = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                FileDto uploadedFileDto = uploadFile(file, path);
                if (uploadedFileDto != null) {
                    filesDto.add(uploadedFileDto);
                } else {
                    throw new FileUploadException(
                        "Failed to upload file: " + file.getOriginalFilename());
                }
            }
        } catch (FileUploadException e) {
            log.error("Error uploading files : {}", e.getMessage(), e);
            throw new FileUploadException("Error uploading files: ", e);
        }

        return filesDto;
    }

    private String findFile(String fileName) throws Exception {
        try {
            Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(this.minioUsername).recursive(true).build());

            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir() && item.objectName().endsWith("/" + fileName)) {
                    return item.objectName();
                }
            }
        } catch (MinioException e) {
            log.error("Error listing objects in MinIO: {}", e.getMessage(), e);
            throw new Exception("Error listing objects in MinIO: " + e.getMessage());
        }
        return null;
    }

    private String findFileByName(String fileName) throws Exception {
        try {
            Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(this.minioUsername)
                    .recursive(true)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                String itemName = item.objectName();

                if (itemName.equals(fileName)) {
                    return itemName;
                }

                if (itemName.endsWith("/")) {
                    String nestedFileName = findFileByName(fileName);
                    if (nestedFileName != null) {
                        return nestedFileName;
                    }
                }
            }
        } catch (MinioException e) {
            throw new Exception("MinIO error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Unexpected error: " + e.getMessage(), e);
        }

        return null;
    }

    private String cleanEtag(String etag) {
        if (etag != null && etag.length() > 2 && etag.startsWith("\"") && etag.endsWith("\"")) {
            return etag.substring(1, etag.length() - 1);
        }
        return etag;
    }

    @Override
    public void renameFile(UUID fileId, String newName) throws Exception {
        File file = this.fileRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("File not found with ID: " + fileId));

        file.setName(newName);

        this.fileRepository.save(file);
    }


    private String constructFileUrl(String bucketName, String fileName) {
        // Construct the URL based on the MinIO server URL, bucket name, and file name
        return String.format("%s/%s/%s", this.minioUrl, bucketName, fileName);
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
        for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
            for (int i = 0; i < level; i++) {
                log.info("  ");
            }
            log.info(entry.getKey());
            if (entry.getValue() instanceof Map) {
                printHierarchy((Map<String, Object>) entry.getValue(), level + 1);
            }
        }
    }

    @Override
    public FileDto getFileById(UUID fileId) {
        File file = this.fileRepository.findById(fileId).orElseThrow(null);
        return this.fileMapper.toDto(file);
    }
//    @Override
//    public void createFolder(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//
//    }
//
//    @Override
//    public Set<FolderDto> getFoldersbyConnectedUser(String folder) throws XmlParserException, ServerException, InvalidResponseException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException {
//        return null;
//    }
//
//    @Override
//    public void removeObject(String filename) throws Exception {
//        Path path = Path.of(filename);
//        var metadata = minioService.getMetadata(path);
//        minioService.remove(path);
//    }
//
//    @Override
//    public void downloadObject(String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//
//    }
//
//    @Override
//    public void createFile(String path, String xhtml) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//
//    }
//
//    @Override
//    public byte[] openFile(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        return new byte[0];
//    }
//
//    @Override
//    public byte[] openFileById(Long id) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        return new byte[0];
//    }
//
//    @Override
//    public byte[] openFileByPathUuid(String pathUuid) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        return new byte[0];
//    }
//
//    @Override
//    public FileDownloadDto downloadFile(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        return null;
//    }
//
//    @Override
//    public FileDownloadDto downloadFileByPath(String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
//        return null;
//    }
//
//    @Override
//    public Boolean checkFileNameExistence(String path, String name) {
//        return null;
//    }


    public int countOccurrences(String path, char character) {
        return (int) path.chars().filter(ch -> ch == character).count();
    }

//    public String createDirectoryTree(String path) throws ServerException, InsufficientDataException, ErrorResponseException,
//        IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
//        InternalException {
//
//        String firstMissingFolder = getFirstMissingFolder(path);
//        String currentPath = firstMissingFolder.substring(0, firstMissingFolder.length() - 1);
//        String newPath = currentPath;
//        String existingPath = currentPath.substring(0, currentPath.lastIndexOf("/") + 1);
//
//        while (!existingPath.equals(path.substring(0, path.lastIndexOf("/") + 1))) {
//            FolderDto existingFolder = folderService.findFolderByPath("reports/" + existingPath);
//            FolderDto createdFolder = createFolderInDb(newPath + "/");
//            newPath = createdFolder.getPath().substring(0, createdFolder.getPath().length() - 1);
//            newPath = newPath.substring(newPath.indexOf("/") + 1);
//            String remainingPath = path.replace(currentPath + "/", "");
//            existingPath = currentPath + "/";
//
//            if (countOccurrences(remainingPath, '/') != 0) {
//                currentPath = currentPath + "/" + remainingPath.substring(0, remainingPath.indexOf("/"));
//                newPath = newPath + "/" + remainingPath.substring(0, remainingPath.indexOf("/"));
//            }
//        }
//
//        return newPath;
//    }
//    public String getFirstMissingFolder(String path) {
//        int segmentIndex = 1;
//        FolderDto folder = null;
//        String currentPath = path.split("/")[0] + "/";
//        String originalPath = username.getConnectedUsername() + "/";
//
//        while (segmentIndex < countOccurrences(path, '/') && folderService.findFolderByPath("reports/" + currentPath) != null) {
//            currentPath = currentPath + path.split("/")[segmentIndex] + "/";
//            originalPath = originalPath + path.split("/")[segmentIndex] + "/";
//            FolderDto originalFolder = folderService.findFolderByPath("reports/" + originalPath);
//            folder = folderService.findFolderByPath("reports/" + currentPath);
//
//            if (folder != null && !folder.getUuid().toString().equals(originalFolder.getUuid().toString())) {
//                folder = null;
//            }
//
//            segmentIndex++;
//        }
//
//        return currentPath;
//    }
//
//    public FolderDto createFolderInDb(String path) throws IOException, InvalidKeyException, InvalidResponseException,
//        InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
//        ErrorResponseException {
//
//        FolderDto folderDto;
//        String folderName;
//
//        // Check if it's a user directory
//        if (path.split("/").length == 1) {
//            folderDto = folderService.createFolder(path.split("/")[0], "reports/" + path);
//        } else {
//            String parentPath = path.substring(0, path.lastIndexOf("/"));
//            Folder existingFolder = folderRepository.findByPath("reports/" + path);
//
//            if (existingFolder != null) {
//                // Folder already exists, handle recurrence
//                int recurrence = (int) (existingFolder.getRecurrence() + 1);
//                folderName = parentPath.substring(parentPath.lastIndexOf("/") + 1) + "(" + recurrence + ")";
//                path = parentPath.substring(0, parentPath.lastIndexOf("/")) + "/" + folderName + "/";
//                existingFolder.setRecurrence((long) recurrence);
//                folderRepository.save(existingFolder);
//            } else {
//                folderName = parentPath.substring(parentPath.lastIndexOf("/") + 1);
//            }
//
//            folderDto = folderService.createFolder(folderName, path);
//            Folder newFolderEntity = folderMapper.toEntity(folderDto);
//
//            // Handle parent-child relationship
//            if (!parentPath.equals(username.getConnectedUsername())) {
//                Folder parentFolder = folderRepository.findByPath("reports/" + parentPath);
//                List<Folder> subfolders = parentFolder.getFolders();
//
//                if (subfolders == null) {
//                    subfolders = new ArrayList<>();
//                    parentFolder.setFolders(subfolders);
//                }
//
//                newFolderEntity.setParent(parentFolder);
//                subfolders.add(newFolderEntity);
//                folderRepository.save(parentFolder);
//            }
//        }
//
//        return folderDto;
//    }


}
