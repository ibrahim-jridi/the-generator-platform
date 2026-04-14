package com.pfe.services.servicesImplementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pfe.dto.FolderDto;
import io.minio.BucketExistsArgs;
import io.minio.ListBucketsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.messages.Bucket;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {FolderServiceImpl.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FolderServiceImplTest {
    @Autowired
    private FolderServiceImpl folderServiceImpl;

    @MockBean
    private MinioClient minioClient;

    /**
     * Method under test: {@link FolderServiceImpl#createFolder(String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testCreateFolder() throws Exception {
        // TODO: Complete this test.

//        folderServiceImpl.createFolder("Folder Name");
    }

    /**
     * Method under test: {@link FolderServiceImpl#createFolder(String)}
     */
    @Test
    public void testCreateFolder2() throws Exception {
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(true);
//        assertThrows(Exception.class, () -> folderServiceImpl.createFolder("lll"));
        verify(minioClient).bucketExists(Mockito.<BucketExistsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#createFolder(String)}
     */
    @Test
    public void testCreateFolder3() throws Exception {
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(false);
        doNothing().when(minioClient).makeBucket(Mockito.<MakeBucketArgs>any());
//        folderServiceImpl.createFolder("lll");
        verify(minioClient).bucketExists(Mockito.<BucketExistsArgs>any());
        verify(minioClient).makeBucket(Mockito.<MakeBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#createFolder(String)}
     */
    @Test
    public void testCreateFolder4() throws Exception {
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(true);
//        assertThrows(Exception.class, () -> folderServiceImpl.createFolder("llllll"));
        verify(minioClient).bucketExists(Mockito.<BucketExistsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#createFolder(String)}
     */
    @Test
    public void testCreateFolder5() throws Exception {
        when(minioClient.bucketExists(Mockito.<BucketExistsArgs>any())).thenReturn(true);
//        assertThrows(Exception.class, () -> folderServiceImpl.createFolder("lll42"));
        verify(minioClient).bucketExists(Mockito.<BucketExistsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#listFolders()}
     */
    @Test
    public void testListFolders() throws Exception {
        when(minioClient.listBuckets(Mockito.<ListBucketsArgs>any())).thenReturn(new ArrayList<>());
        assertTrue(folderServiceImpl.listFolders().isEmpty());
        verify(minioClient).listBuckets(Mockito.<ListBucketsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#listFolders()}
     */
    @Test
    public void testListFolders2() throws Exception {
        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(new Bucket());
        when(minioClient.listBuckets(Mockito.<ListBucketsArgs>any())).thenReturn(bucketList);
        List<FolderDto> actualListFoldersResult = folderServiceImpl.listFolders();
        assertEquals(1, actualListFoldersResult.size());
        assertNull(actualListFoldersResult.get(0));
        verify(minioClient).listBuckets(Mockito.<ListBucketsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#listFolders()}
     */
    @Test
    public void testListFolders3() throws Exception {
        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(new Bucket());
        bucketList.add(new Bucket());
        when(minioClient.listBuckets(Mockito.<ListBucketsArgs>any())).thenReturn(bucketList);
        List<FolderDto> actualListFoldersResult = folderServiceImpl.listFolders();
        assertEquals(2, actualListFoldersResult.size());
        assertNull(actualListFoldersResult.get(0));
        assertNull(actualListFoldersResult.get(1));
        verify(minioClient).listBuckets(Mockito.<ListBucketsArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#listFolders()}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testListFolders4() throws Exception {
        // TODO: Complete this test.

        ArrayList<Bucket> bucketList = new ArrayList<>();
        bucketList.add(null);
        when(minioClient.listBuckets(Mockito.<ListBucketsArgs>any())).thenReturn(bucketList);
        folderServiceImpl.listFolders();
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testRemoveFolder() throws Exception {
        // TODO: Complete this test.

        folderServiceImpl.removeFolder("Folder Name");
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder2() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("lll");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder3() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("llllll");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder4() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("lll42");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder5() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("42lll");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder6() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("4242");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder7() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("lllllllll");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder8() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("llllll42");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder9() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("lll42lll");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }

    /**
     * Method under test: {@link FolderServiceImpl#removeFolder(String)}
     */
    @Test
    public void testRemoveFolder10() throws Exception {
        doNothing().when(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
        folderServiceImpl.removeFolder("lll4242");
        verify(minioClient).removeBucket(Mockito.<RemoveBucketArgs>any());
    }
}

