package com.pfe.services.servicesImplementation;

import static com.google.common.io.Files.getFileExtension;

import com.pfe.config.Constants;
import com.pfe.domain.Folder;
import com.pfe.dto.FileDto;
import com.pfe.dto.FolderDto;
import com.pfe.dto.requests.CreateFolderRequest;
import com.pfe.mappers.FolderHierarchyMapper;
import com.pfe.mappers.FolderMapper;
import com.pfe.repository.FolderRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.services.FolderService;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderServiceImpl.class);

    @Autowired
    MinioClient minioClient;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FolderMapper folderMapper;
    @Autowired
    private FolderHierarchyMapper folderHierarchyMapper;

    /*** Initialize default buckets and folders ***/
    public void initializeBucketsAndFolders() throws Exception {
        createBucketAndFolders("bs-bucket", null, null);
        createBucketAndFolders("config", "templates", "traduction");
        createBucketAndFolders("reports", null, null);
    }

    private void createBucketAndFolders(String bucketName, String folder1, String folder2)
        throws Exception {
        try {
            boolean isExist = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            Optional<Folder> existingBucketFolder = folderRepository.findByName(bucketName);
            if (existingBucketFolder.isEmpty()) {
                FolderDto bucketFolder = new FolderDto();
                bucketFolder.setFolderMinioId(UUID.nameUUIDFromBytes(bucketName.getBytes()));
                bucketFolder.setName(bucketName);
                bucketFolder.setPath("/" + bucketName);
                folderRepository.save(folderMapper.toEntity(bucketFolder));
            }

            if (folder1 != null) {
                createFolderInBucketHelper(bucketName, folder1);
            }
            if (folder2 != null) {
                createFolderInBucketHelper(bucketName, folder2);
            }
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            LOGGER.error("Error creating the bucket or folders", e);
            throw new Exception("Error creating bucket or folders: " + e.getMessage());
        }
    }

    private void createFolderInBucketHelper(String bucketName, String folderName) throws Exception {
        try {
            String folderPath = folderName + "/";
            Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(folderPath).build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(folderPath)) {
                    return;
                }
            }

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(folderPath)
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            Optional<Folder> existingFolder = folderRepository.findByName(folderName);
            FolderDto folderParent = folderMapper.toDto(
                folderRepository.findByName(bucketName).orElseThrow());
            if (existingFolder.isEmpty()) {
                FolderDto bucketFolder = new FolderDto();
                bucketFolder.setName(folderName);
                bucketFolder.setFolderMinioId(UUID.nameUUIDFromBytes(folderName.getBytes()));
                bucketFolder.setPath("/" + bucketName + "/" + folderPath);
                bucketFolder.setParent(folderParent);
                folderRepository.save(folderMapper.toEntity(bucketFolder));
            }
        } catch (MinioException e) {
            LOGGER.error("Error creating the folder", e);
            throw new Exception("Error creating folder: " + e.getMessage());
        }
    }

    /*** Create new folder in default bucket ***/
    @Override
    public FolderDto createFolder(CreateFolderRequest request) throws IOException {
        try {
            if (!minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(Constants.BUCKET_NAME).build())) {
                throw new FileNotFoundException("This bucket doesn't exist: " + Constants.BUCKET_NAME);
            }

            Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(Constants.BUCKET_NAME).build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(request.getNewFolderName())) {
                    throw new IllegalArgumentException("Folder already exists.");
                }
            }

            UUID folderId = UUID.nameUUIDFromBytes(request.getNewFolderName().getBytes());

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(Constants.BUCKET_NAME)
                .object(request.getNewFolderName() + "/")
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            FolderDto folderDto = new FolderDto();
            folderDto.setName(request.getNewFolderName());
            folderDto.setPath("/" + Constants.BUCKET_NAME + "/" + request.getNewFolderName());

            Folder folderEntity = folderMapper.toEntity(folderDto);
            folderEntity.setFolderMinioId(folderId);
            folderEntity.setUserId(request.getParentId());

            Folder savedFolder = folderRepository.save(folderEntity);
            return folderMapper.toDto(savedFolder);
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error creating folder: " + e.getMessage());
        }
    }

    /*** Create new folder inside a parent bucket ***/
    @Override
    public FolderDto createFolderInBucket(UUID parentId, String newFolderName) throws Exception {
        try {
            Folder parentFolder = folderRepository.findByFolderMinioId(parentId).orElseThrow();
            UUID newFolderUuid = UUID.nameUUIDFromBytes(newFolderName.getBytes());
            String fullPath = !parentFolder.getPath().isEmpty()
                ? parentFolder.getPath() + "/" + newFolderUuid
                : "/" + newFolderUuid;

            boolean isBucketExist = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(String.valueOf(parentFolder.getFolderMinioId()))
                    .build());
            if (!isBucketExist) {
                throw new Exception("Bucket does not exist.");
            }

            Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(String.valueOf(parentFolder.getFolderMinioId()))
                    .prefix(fullPath)
                    .build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(fullPath)) {
                    throw new Exception("Folder already exists.");
                }
            }

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(String.valueOf(parentFolder.getFolderMinioId()))
                .object(fullPath + "/")
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            FolderDto folder = new FolderDto();
            folder.setName(newFolderName);
            folder.setPath(fullPath);
            folder.setParent(FolderMapper.INSTANCE.toDto(parentFolder));
            Folder folderEntity = folderMapper.toEntity(folder);
            folderEntity.setFolderMinioId(newFolderUuid);

            Folder savedFolder = folderRepository.save(folderEntity);
            return folderMapper.toDto(savedFolder);
        } catch (MinioException e) {
            throw new Exception("Error creating folder: " + e.getMessage());
        }
    }

    /*** List all top-level buckets from MinIO — no recursive loading ***/
    /*** Use getFolderContentsByPath() to load the contents of any folder  ***/
    @Override
    public List<FolderDto> listFolders() throws Exception {
        return minioClient.listBuckets().stream().map(bucket -> {
            FolderDto dto = new FolderDto();
            dto.setName(bucket.name());
            dto.setPath("/" + bucket.name());
            dto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucket.name()).getBytes()));
            return dto;
        }).collect(Collectors.toList());
    }

    /*** Recursively list sub-folders under a given prefix inside a bucket ***/
    /*** Extract bucket name from a stored path like /bucket/sub/folder ***/
    private String bucketFromPath(String path) {
        // path = /bucket/... or bucket/...
        String stripped = path.startsWith("/") ? path.substring(1) : path;
        int slash = stripped.indexOf("/");
        return slash == -1 ? stripped : stripped.substring(0, slash);
    }

    /*** Extract MinIO prefix from a stored path like /bucket/sub/folder -> sub/folder/ ***/
    private String prefixFromPath(String path) {
        String stripped = path.startsWith("/") ? path.substring(1) : path;
        int slash = stripped.indexOf("/");
        if (slash == -1 || slash == stripped.length() - 1) {
            return ""; // root of the bucket
        }
        String prefix = stripped.substring(slash + 1);
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    /*** List sub-folders under a given prefix inside a bucket ***/
    private List<FolderDto> listFolderChildren(String bucket, String prefix) throws Exception {
        List<FolderDto> children = new ArrayList<>();
        // delimiter("/") makes MinIO group objects by common prefixes and return them
        // as directory entries (isDir() == true). Without it, all objects are returned
        // flat and isDir() is never true.
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
            .bucket(bucket)
            .delimiter("/")
            .recursive(false);
        if (prefix != null && !prefix.isEmpty()) {
            builder = builder.prefix(prefix);
        }
        Iterable<Result<Item>> objects = minioClient.listObjects(builder.build());

        for (Result<Item> result : objects) {
            Item item = result.get();
            if (item.isDir()) {
                String fullPath = item.objectName(); // e.g. "ext-9238155/"
                // strip trailing slash to get the clean name
                String withoutTrailing = fullPath.endsWith("/")
                    ? fullPath.substring(0, fullPath.length() - 1)
                    : fullPath;
                String name = withoutTrailing.contains("/")
                    ? withoutTrailing.substring(withoutTrailing.lastIndexOf("/") + 1)
                    : withoutTrailing;

                FolderDto folderDto = new FolderDto();
                folderDto.setName(name);
                folderDto.setPath("/" + bucket + "/" + fullPath);
                folderDto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucket + "/" + fullPath).getBytes()));
                // Recursively populate sub-folders and direct files
                folderDto.setFolders(listFolderChildren(bucket, fullPath));
                folderDto.setFiles(listFileChildren(bucket, fullPath));
                children.add(folderDto);
            }
        }
        return children;
    }

    /*** List files directly under a given prefix inside a bucket ***/
    private List<FileDto> listFileChildren(String bucket, String prefix) throws Exception {
        List<FileDto> files = new ArrayList<>();
        // delimiter("/") ensures we only see objects at this level, not in sub-prefixes
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
            .bucket(bucket)
            .delimiter("/")
            .recursive(false);
        if (prefix != null && !prefix.isEmpty()) {
            builder = builder.prefix(prefix);
        }
        Iterable<Result<Item>> objects = minioClient.listObjects(builder.build());

        for (Result<Item> result : objects) {
            try {
                Item item = result.get();
                // isDir() == true means it is a common prefix (sub-folder) — skip it
                // size == 0 && name ends with "/" means it is a folder placeholder — skip it
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
                    files.add(fileDto);
                }
            } catch (Exception e) {
                LOGGER.error("Error reading MinIO item: {}", e.getMessage(), e);
            }
        }
        return files;
    }

    /*** Remove folder from MinIO ***/
    @Override
    public void removeFolder(String folderPath) throws Exception {
        LOGGER.info("Removing folder at path: {}", folderPath);

        // Normalize path
        String cleanPath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        if (!cleanPath.endsWith("/")) {
            cleanPath = cleanPath + "/";
        }

        String[] parts = cleanPath.split("/", 2);
        String bucketName = parts[0];
        String folderPrefix = parts.length > 1 ? parts[1] : "";

        // List all objects in the folder
        Iterable<Result<Item>> objects = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(folderPrefix)
                .recursive(true)
                .build());

        // Delete all objects in the folder
        for (Result<Item> result : objects) {
            Item item = result.get();
            LOGGER.debug("Deleting object: {}", item.objectName());
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(item.objectName())
                    .build());
        }

        LOGGER.info("Folder deleted successfully: {}", folderPath);
    }

    /*** Get folder of currently connected user directly from MinIO ***/
    @Override
    public Optional<FolderDto> getFolderByConnectedUser() {
        try {
            UUID userConnected = UUID.fromString(SecurityUtils.getUserIdFromCurrentUser());
            // The user's root folder is stored in DB only for the userId mapping;
            // we fetch the path from DB then verify/enrich from MinIO.
            Optional<Folder> folderOpt = folderRepository.findByUserId(userConnected);
            if (folderOpt.isEmpty()) {
                return Optional.empty();
            }
            Folder dbFolder = folderOpt.orElseThrow();
            // Re-hydrate from MinIO to get live hierarchy
            String bucket = dbFolder.getPath().split("/")[1];
            String prefix = dbFolder.getPath().substring(bucket.length() + 2); // strip /bucket/
            FolderDto folderDto = new FolderDto();
            folderDto.setName(dbFolder.getName());
            folderDto.setPath(dbFolder.getPath());
            folderDto.setFolderMinioId(dbFolder.getFolderMinioId());
            folderDto.setFolders(listFolderChildren(bucket, prefix.isEmpty() ? "" : prefix + "/"));
            return Optional.of(folderDto);
        } catch (Exception e) {
            LOGGER.error("Error fetching folder for connected user from MinIO: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /*** Get contents (sub-folders + files) of a folder by its MinIO path ***/
    /*** Path format: /bucket/prefix/subfolder/ or just /bucket               ***/
    @Override
    public FolderDto getFolderContentsByPath(String encodedPath) throws Exception {
        String path = java.net.URLDecoder.decode(encodedPath, java.nio.charset.StandardCharsets.UTF_8);

        LOGGER.info("Getting folder contents for path: {}", path);

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String[] parts = cleanPath.split("/", 2);
        String bucketName = parts[0];
        String prefix = parts.length > 1 ? parts[1] : "";

        if (!prefix.isEmpty() && !prefix.endsWith("/")) {
            prefix = prefix + "/";
        }

        FolderDto folderDto = new FolderDto();
        folderDto.setName(getNameFromPath(cleanPath));
        folderDto.setPath(path);
        folderDto.setFolderMinioId(UUID.nameUUIDFromBytes(path.getBytes()));

        // Get sub-folders from MinIO
        folderDto.setFolders(listSubFolders(bucketName, prefix));

        // Get files from MinIO
        folderDto.setFiles(listFilesInFolder(bucketName, prefix));

        return folderDto;
    }
    private List<FolderDto> listAllBuckets() throws Exception {
        List<FolderDto> buckets = new ArrayList<>();
        List<Bucket> minioBuckets = minioClient.listBuckets();

        for (Bucket bucket : minioBuckets) {
            FolderDto dto = new FolderDto();
            dto.setName(bucket.name());
            dto.setPath("/" + bucket.name());
            dto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucket.name()).getBytes()));
            buckets.add(dto);
            LOGGER.debug("Found bucket: {}", bucket.name());
        }

        return buckets;
    }

    private List<FolderDto> listSubFolders(String bucketName, String prefix) throws Exception {
        List<FolderDto> subFolders = new ArrayList<>();

        LOGGER.debug("Listing sub-folders in bucket: '{}', prefix: '{}'", bucketName, prefix);

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .delimiter("/")
                    .recursive(false)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();

                if (item.isDir()) {
                    String folderPath = item.objectName(); // e.g., "MyNewFolder/" or "ext-9238155/"
                    // Remove trailing slash
                    String cleanFolderPath = folderPath.endsWith("/")
                        ? folderPath.substring(0, folderPath.length() - 1)
                        : folderPath;

                    // Extract just the folder name from the path
                    String folderName = cleanFolderPath.contains("/")
                        ? cleanFolderPath.substring(cleanFolderPath.lastIndexOf("/") + 1)
                        : cleanFolderPath;

                    if (!folderName.isEmpty()) {
                        FolderDto folderDto = new FolderDto();
                        folderDto.setName(folderName);  // This should be the actual folder name
                        folderDto.setPath("/" + bucketName + "/" + folderPath);
                        folderDto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucketName + "/" + folderPath).getBytes()));
                        subFolders.add(folderDto);
                        LOGGER.debug("Found sub-folder: '{}' at path: '{}'", folderName, folderPath);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error listing sub-folders for bucket {} prefix {}: {}", bucketName, prefix, e.getMessage(), e);
        }

        return subFolders;
    }
    private List<FileDto> listFilesInFolder(String bucketName, String prefix) throws Exception {
        List<FileDto> files = new ArrayList<>();

        LOGGER.debug("Listing files in bucket: '{}', prefix: '{}'", bucketName, prefix);

        try {
            // List objects at this level only (not recursive)
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .delimiter("/")
                    .recursive(false)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();

                // Skip directories/folders (isDir=true)
                if (!item.isDir()) {
                    String objectName = item.objectName();

                    // Extract just the filename
                    String fileName = objectName.contains("/")
                        ? objectName.substring(objectName.lastIndexOf("/") + 1)
                        : objectName;

                    if (!fileName.isEmpty()) {
                        FileDto fileDto = new FileDto();
                        fileDto.setId(UUID.randomUUID());
                        fileDto.setName(fileName);
                        fileDto.setPath("/" + bucketName + "/" + objectName);
                        fileDto.setExtension(getFileExtension(fileName));
                        fileDto.setSize(String.valueOf(item.size()));
                        fileDto.setInWorkflow(false);

                        // Try to get content type
                        try {
                            StatObjectResponse stat = minioClient.statObject(
                                StatObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .build());
                            fileDto.setType(stat.contentType());
                        } catch (Exception e) {
                            // Default content type
                            fileDto.setType("application/octet-stream");
                        }

                        files.add(fileDto);
                        LOGGER.debug("Found file: '{}' (size: {} bytes)", fileName, item.size());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error listing files for bucket {} prefix {}: {}", bucketName, prefix, e.getMessage(), e);
        }

        return files;
    }

    private String getNameFromPath(String path) {
        if (path == null || path.isEmpty()) return "Root";
        if (!path.contains("/")) return path;
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1);
        }
        return "";
    }
    /*** List all top-level buckets (no recursive loading) ***/
    @Override
    public List<FolderDto> getFoldersByParentFolderMinioId(UUID parentId) {
        // parentId is ignored — this method now lists MinIO buckets as top-level folders.
        // Navigation into sub-folders is done via getFolderContentsByPath().
        try {
            return minioClient.listBuckets().stream().map(bucket -> {
                FolderDto dto = new FolderDto();
                dto.setName(bucket.name());
                dto.setPath("/" + bucket.name());
                // Use a stable UUID derived from the path so the frontend can pass it back
                dto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucket.name()).getBytes()));
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error listing buckets from MinIO: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }



    @Override
    public FolderDto createFolderInBucket(String parentPath, String folderName) throws Exception {
        try {
            LOGGER.info("Creating folder '{}' in parent path: '{}'", folderName, parentPath);

            // Normalise path
            if (!parentPath.startsWith("/")) {
                parentPath = "/" + parentPath;
            }

            // Extract bucket and prefix from path
            String stripped = parentPath.substring(1);
            int firstSlash = stripped.indexOf("/");
            String bucket;
            String prefix;
            if (firstSlash == -1) {
                bucket = stripped;
                prefix = "";
            } else {
                bucket = stripped.substring(0, firstSlash);
                prefix = stripped.substring(firstSlash + 1);
                if (!prefix.isEmpty() && !prefix.endsWith("/")) {
                    prefix = prefix + "/";
                }
            }

            // Verify bucket exists
            boolean isBucketExist = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build());
            if (!isBucketExist) {
                throw new Exception("Bucket does not exist: " + bucket);
            }

            // IMPORTANT: Use the actual folder name, not UUID
            String sanitizedFolderName = folderName.trim().replaceAll("[/\\\\:*?\"<>|]", "_");
            String objectPath = prefix + sanitizedFolderName + "/";
            String fullStoredPath = "/" + bucket + "/" + objectPath;

            LOGGER.debug("Creating folder with object path: {}, stored path: {}", objectPath, fullStoredPath);

            // Check for duplicates
            Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucket)
                    .prefix(objectPath)
                    .build());

            for (Result<Item> result : objects) {
                Item item = result.get();
                if (item.objectName().equals(objectPath)) {
                    throw new Exception("Folder already exists: " + sanitizedFolderName);
                }
            }

            // Create the placeholder object in MinIO
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectPath)
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            // Generate UUID for database reference only
            UUID newFolderUuid = UUID.nameUUIDFromBytes(fullStoredPath.getBytes());

            // Create folder entity in database
            FolderDto folderDto = new FolderDto();
            folderDto.setName(sanitizedFolderName);  // Use the actual folder name
            folderDto.setPath(fullStoredPath);
            folderDto.setFolderMinioId(newFolderUuid);

            // Find parent folder
            Optional<Folder> parentFolderOpt = folderRepository.findByPath(parentPath);
            if (parentFolderOpt.isPresent()) {
                Folder parentFolder = parentFolderOpt.orElseThrow();
                Folder folderEntity = folderMapper.toEntity(folderDto);
                folderEntity.setParent(parentFolder);
                Folder savedFolder = folderRepository.save(folderEntity);
                LOGGER.info("Folder '{}' created successfully with parent", sanitizedFolderName);
                return folderMapper.toDto(savedFolder);
            } else {
                // No parent found (root level folder)
                Folder folderEntity = folderMapper.toEntity(folderDto);
                Folder savedFolder = folderRepository.save(folderEntity);
                LOGGER.info("Folder '{}' created successfully at root level", sanitizedFolderName);
                return folderMapper.toDto(savedFolder);
            }

        } catch (MinioException e) {
            LOGGER.error("MinIO error creating folder: {}", e.getMessage(), e);
            throw new Exception("Error creating folder: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error creating folder: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public FolderDto renameFolder(String folderPath, String newName) throws Exception {
        LOGGER.info("Renaming folder from '{}' to '{}'", folderPath, newName);

        // Normalize paths
        String cleanPath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        if (!cleanPath.endsWith("/")) {
            cleanPath = cleanPath + "/";
        }

        String[] parts = cleanPath.split("/", 2);
        String bucketName = parts[0];
        String oldFolderPath = parts.length > 1 ? parts[1] : "";

        // Get the parent path and old folder name
        String oldFolderName = oldFolderPath.substring(0, oldFolderPath.length() - 1);
        String oldFolderNameOnly = oldFolderName.contains("/")
            ? oldFolderName.substring(oldFolderName.lastIndexOf("/") + 1)
            : oldFolderName;
        String parentPath = oldFolderPath.substring(0, oldFolderPath.length() - oldFolderNameOnly.length());

        String sanitizedNewName = newName.trim().replaceAll("[/\\\\:*?\"<>|]", "_");
        String newFolderPath = parentPath + sanitizedNewName + "/";

        LOGGER.info("Renaming in bucket '{}' from '{}' to '{}'", bucketName, oldFolderPath, newFolderPath);

        // List all objects in the old folder
        Iterable<Result<Item>> objects = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(oldFolderPath)
                .recursive(true)
                .build());

        List<Item> itemsToMove = new ArrayList<>();
        for (Result<Item> result : objects) {
            itemsToMove.add(result.get());
        }

        if (itemsToMove.isEmpty()) {
            // Create new empty folder
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(newFolderPath)
                .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                .build());
        } else {
            // Copy and delete each object
            for (Item item : itemsToMove) {
                String oldObjectPath = item.objectName();
                String newObjectPath = oldObjectPath.replace(oldFolderPath, newFolderPath);

                LOGGER.debug("Moving object from '{}' to '{}'", oldObjectPath, newObjectPath);

                // Copy to new location
                minioClient.copyObject(
                    CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newObjectPath)
                        .source(CopySource.builder()
                            .bucket(bucketName)
                            .object(oldObjectPath)
                            .build())
                        .build());

                // Delete old object
                minioClient.removeObject(
                    RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(oldObjectPath)
                        .build());
            }
        }

        // Create response DTO
        FolderDto folderDto = new FolderDto();
        folderDto.setName(sanitizedNewName);
        folderDto.setPath("/" + bucketName + "/" + newFolderPath);
        folderDto.setFolderMinioId(UUID.nameUUIDFromBytes(("/" + bucketName + "/" + newFolderPath).getBytes()));

        LOGGER.info("Folder renamed successfully to: {}", newFolderPath);
        return folderDto;
    }
}