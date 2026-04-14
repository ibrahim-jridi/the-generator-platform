package com.pfe.services.servicesImplementation;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import com.pfe.mappers.FolderMapper;
import com.pfe.services.FolderService;
import io.minio.MinioClient;
import io.minio.errors.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileServiceImpl.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceImplTest {

    private FileServiceImpl fileServiceImpl;

    public FileServiceImplTest(FileServiceImpl fileServiceImpl) {
        this.fileServiceImpl = fileServiceImpl;
    }

    @MockBean
    private FolderMapper folderMapper;

    @MockBean
    private FolderService folderService;

    @MockBean
    private MinioClient minioClient;

    /**
     * Method under test: {@link FileServiceImpl#uploadFile(MultipartFile, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testUploadFile()
        throws ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
        ServerException, XmlParserException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        // TODO: Complete this test.


//        fileServiceImpl.uploadFile(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))),
//            "Path");
    }

    /**
     * Method under test: {@link FileServiceImpl#uploadFile(MultipartFile, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testUploadFile2()
        throws MinioException,
        IOException, InvalidKeyException, NoSuchAlgorithmException {
        // TODO: Complete this test.


        DataInputStream contentStream = mock(DataInputStream.class);
//        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        fileServiceImpl.uploadFile(new MockMultipartFile("Name", contentStream), "Path");
    }

    /**
     * Method under test: {@link FileServiceImpl#downloadFile(String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testDownloadFile() throws Exception {
        // TODO: Complete this test.


        fileServiceImpl.downloadFile("foo.txt");
    }

    /**
     * Method under test: {@link FileServiceImpl#removeFile(UUID)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testRemoveFile() throws Exception {
        // TODO: Complete this test.


        fileServiceImpl.removeFile(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
    }

    /**
     * Method under test: {@link FileServiceImpl#getFile(String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetFile() throws Exception {
        // TODO: Complete this test.


        fileServiceImpl.getFile("foo.txt");
    }

    /**
     * Method under test: {@link FileServiceImpl#getFiles()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testGetFiles() throws Exception {
        // TODO: Complete this test.


        fileServiceImpl.getFiles();
    }

    /**
     * Method under test: {@link FileServiceImpl#uploadFiles(List, String)}
     */
    @Test
    public void testUploadFiles() throws Exception {


//        FileServiceImpl fileServiceImpl = new FileServiceImpl();
        fileServiceImpl.uploadFiles(new ArrayList<>(), "Path");
        assertNull(fileServiceImpl.folderMapper);
        assertNull(fileServiceImpl.folderService);
    }

    /**
     * Method under test: {@link FileServiceImpl#uploadFiles(List, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testUploadFiles2() throws Exception {


        // TODO: Complete this test.


//        FileServiceImpl fileServiceImpl = new FileServiceImpl();

//        ArrayList<MultipartFile> files = new ArrayList<>();
//        files.add(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
//        fileServiceImpl.uploadFiles(files, "Path");
    }

    /**
     * Method under test: {@link FileServiceImpl#uploadFiles(List, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testUploadFiles3() throws Exception {


        // TODO: Complete this test.


//        FileServiceImpl fileServiceImpl = new FileServiceImpl();

        ArrayList<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("Name", new ByteArrayInputStream(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1})));
//        files.add(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        fileServiceImpl.uploadFiles(files, "Path");
    }

    /**
     * Method under test: {@link FileServiceImpl#uploadFiles(List, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testUploadFiles4() throws Exception {


        // TODO: Complete this test.


//        FileServiceImpl fileServiceImpl = new FileServiceImpl();

        ArrayList<MultipartFile> files = new ArrayList<>();
        files.add(mock(MockMultipartFile.class));
        fileServiceImpl.uploadFiles(files, "Path");
    }
}

