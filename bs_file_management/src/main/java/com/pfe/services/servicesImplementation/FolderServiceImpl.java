package com.pfe.services.servicesImplementation;

import com.pfe.config.Constants;
import com.pfe.domain.Folder;
import com.pfe.dto.FolderDto;
import com.pfe.dto.requests.CreateFolderRequest;
import com.pfe.mappers.FolderMapper;
import com.pfe.repository.FolderRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.services.FolderService;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {
    private static final Logger log = LoggerFactory.getLogger(FolderServiceImpl.class);

    private final MinioClient minioClient;
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    public FolderServiceImpl(MinioClient minioClient, FolderRepository folderRepository,
                             FolderMapper folderMapper) {
        this.minioClient = minioClient;
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    //@PostConstruct
    public void initializeBucketsAndFolders() throws Exception {
        createBucketAndFolders("bs-bucket", null, null);
        createBucketAndFolders("config", "templates", "traduction");
        createBucketAndFolders("reports", null, null);
    }

    private void createBucketAndFolders(String bucketName, String folder1, String folder2) throws Exception {
        try {

            boolean isExist = this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            Optional<Folder> existingBucketFolder = this.folderRepository.findByName(bucketName);
            if (existingBucketFolder.isEmpty()) {
                FolderDto bucketFolder = new FolderDto();
                bucketFolder.setFolderMinioId(UUID.nameUUIDFromBytes(bucketName.getBytes()));
                bucketFolder.setName(bucketName);
                bucketFolder.setPath("/" + bucketName);

                this.folderRepository.save(this.folderMapper.toEntity(bucketFolder));
            }


            if (folder1 != null) {
                createFolderInBucket(bucketName, folder1);
            }
            if (folder2 != null) {
                createFolderInBucket(bucketName, folder2);
            }

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error creating the bucket or folders", e);
            throw new Exception("Error creating bucket or folders: " + e.getMessage());
        }
    }

    private void createFolderInBucket(String bucketName, String folderName) throws Exception {
        try {
            String folderPath = folderName + "/";
            Iterable<Result<Item>> objects = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(folderPath).build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(folderPath)) {
                    return;
                }
            }
            this.minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(folderPath)
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            Optional<Folder> existingFolder = this.folderRepository.findByName(folderName);
            FolderDto folderParent = this.folderMapper.toDto(this.folderRepository.findByName(bucketName).orElseThrow());
            if (existingFolder.isEmpty()) {
                FolderDto bucketFolder = new FolderDto();
                bucketFolder.setName(folderName);
                bucketFolder.setFolderMinioId(UUID.nameUUIDFromBytes(folderName.getBytes()));
                bucketFolder.setPath("/" + bucketName + "/" + folderPath);
                bucketFolder.setParent(folderParent);

                this.folderRepository.save(this.folderMapper.toEntity(bucketFolder));
            }
        } catch (MinioException e) {
            log.error("Error creating the folder", e);
            throw new Exception("Error creating folder: " + e.getMessage());
        }
    }

    @Override
    public FolderDto createFolder(CreateFolderRequest request) throws IOException {
        try {

            // Check if the bucket exists in minio
            if (!this.minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(Constants.BUCKET_NAME).build())) {
                throw new FileNotFoundException("This bucket doesn't exist: " + Constants.BUCKET_NAME);
            }

            // Check if the folder already exists
            Iterable<Result<Item>> objects = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(Constants.BUCKET_NAME).build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(request.getNewFolderName())) {
                    throw new IllegalArgumentException("Folder already exists.");
                }
            }

            UUID folderId = UUID.nameUUIDFromBytes(request.getNewFolderName().getBytes());

            // Create a new folder in the bucket
            this.minioClient.putObject(PutObjectArgs.builder()
                .bucket(Constants.BUCKET_NAME)
                .object(request.getNewFolderName() + "/")
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            FolderDto folderDto = new FolderDto();
            folderDto.setName(request.getNewFolderName());
            folderDto.setPath("/" + Constants.BUCKET_NAME + "/" + request.getNewFolderName());

            Folder folderEntity = this.folderMapper.toEntity(folderDto);
            folderEntity.setFolderMinioId(folderId);
            folderEntity.setUserId(request.getParentId());

            Folder savedFolder = this.folderRepository.save(folderEntity);
            return this.folderMapper.toDto(savedFolder);

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error creating folder: " + e.getMessage());
        }
    }

    @Override
    public FolderDto createFolderInBucket(UUID parentId, String newFolderName)
        throws Exception {
        try {

            Folder parentFolder = this.folderRepository.findByFolderMinioId(parentId).orElseThrow();
            UUID newFolderUuid = UUID.nameUUIDFromBytes(newFolderName.getBytes());
            String fullPath =
                !parentFolder.getPath().isEmpty() ? parentFolder.getPath() + "/" + newFolderUuid
                    : "/" + newFolderUuid;

            boolean isBucketExist = this.minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(String.valueOf(parentFolder.getFolderMinioId()))
                    .build());
            if (!isBucketExist) {
                throw new Exception("Bucket does not exist.");
            }

            Iterable<Result<Item>> objects = this.minioClient.listObjects(
                ListObjectsArgs.builder().bucket(String.valueOf(parentFolder.getFolderMinioId()))
                    .prefix(fullPath).build());
            for (Result<Item> result : objects) {
                if (result.get().objectName().equals(fullPath)) {
                    throw new Exception("Folder already exists.");
                }
            }

            this.minioClient.putObject(PutObjectArgs.builder()
                .bucket(String.valueOf(parentFolder.getFolderMinioId()))
                .object(fullPath + "/")
                .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                .build());

            FolderDto folder = new FolderDto();
            folder.setName(newFolderName);
            folder.setPath(fullPath);
            folder.setParent(FolderMapper.INSTANCE.toDto(parentFolder));
            Folder folderEntity = this.folderMapper.toEntity(folder);
            folderEntity.setFolderMinioId(newFolderUuid);

            Folder savedFolder = this.folderRepository.save(folderEntity);
            return this.folderMapper.toDto(savedFolder);


        } catch (MinioException e) {
            throw new Exception("Error creating folder: " + e.getMessage());
        }
    }

    @Override
    public List<FolderDto> listFolders() throws Exception {
        List<Folder> folders = this.folderRepository.findAll();
        return this.folderMapper.toDtoList(folders);
    }

    @Override
    public void removeFolder(String folderName) throws Exception {
        try {
            this.minioClient.removeBucket(RemoveBucketArgs.builder().bucket(folderName).build());
        } catch (MinioException e) {
            throw new Exception("Error removing folder: " + e.getMessage());
        }
    }

    @Override
    public Optional<FolderDto> getFolderByConnectedUser() {

        UUID userConnected = UUID.fromString(SecurityUtils.getUserIdFromCurrentUser());
        Optional<Folder> folderOpt = this.folderRepository.findByUserId(userConnected);
        return folderOpt.map(this.folderMapper::toDto);
    }

    @Override
    public List<FolderDto> getFoldersByParentFolderMinioId(UUID id) {

        List<Folder> folders = this.folderRepository.findByParentFolderMinioId(id);
        return folders.stream().map(this.folderMapper::toDto).collect(Collectors.toList());
    }

}
