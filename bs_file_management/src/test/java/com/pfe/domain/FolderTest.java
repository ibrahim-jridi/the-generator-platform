package com.pfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Folder.class);
        Folder folder1 = FolderTestSamples.getFolderSample1();
        Folder folder2 = new Folder();
        assertThat(folder1).isNotEqualTo(folder2);

        folder2.setId(folder1.getId());
        assertThat(folder1).isEqualTo(folder2);

        folder2 = FolderTestSamples.getFolderSample2();
        assertThat(folder1).isNotEqualTo(folder2);
    }

    @Test
    void filesTest() {
        Folder folder = FolderTestSamples.getFolderRandomSampleGenerator();
        File fileBack = FileTestSamples.getFileRandomSampleGenerator();

        folder.addFiles(fileBack);
        assertThat(folder.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getFolder()).isEqualTo(folder);

        folder.removeFiles(fileBack);
        assertThat(folder.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getFolder()).isNull();

        folder.files(new HashSet<>(Set.of(fileBack)));
        assertThat(folder.getFiles()).containsOnly(fileBack);
        assertThat(fileBack.getFolder()).isEqualTo(folder);

        folder.setFiles(new HashSet<>());
        assertThat(folder.getFiles()).doesNotContain(fileBack);
        assertThat(fileBack.getFolder()).isNull();
    }

    @Test
    void parentTest() {
        Folder folder = FolderTestSamples.getFolderRandomSampleGenerator();
        Folder folderBack = FolderTestSamples.getFolderRandomSampleGenerator();

        folder.setParent(folderBack);
        assertThat(folder.getParent()).isEqualTo(folderBack);

        folder.parent(null);
        assertThat(folder.getParent()).isNull();
    }
    
}
