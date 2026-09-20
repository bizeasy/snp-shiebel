package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.FileFolderTestSamples.*;
import static com.snp.domain.FileShareTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileShareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileShare.class);
        FileShare fileShare1 = getFileShareSample1();
        FileShare fileShare2 = new FileShare();
        assertThat(fileShare1).isNotEqualTo(fileShare2);

        fileShare2.setId(fileShare1.getId());
        assertThat(fileShare1).isEqualTo(fileShare2);

        fileShare2 = getFileShareSample2();
        assertThat(fileShare1).isNotEqualTo(fileShare2);
    }

    @Test
    void folderTest() {
        FileShare fileShare = getFileShareRandomSampleGenerator();
        FileFolder fileFolderBack = getFileFolderRandomSampleGenerator();

        fileShare.setFolder(fileFolderBack);
        assertThat(fileShare.getFolder()).isEqualTo(fileFolderBack);

        fileShare.folder(null);
        assertThat(fileShare.getFolder()).isNull();
    }

    @Test
    void attachmentTest() {
        FileShare fileShare = getFileShareRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        fileShare.setAttachment(attachmentBack);
        assertThat(fileShare.getAttachment()).isEqualTo(attachmentBack);

        fileShare.attachment(null);
        assertThat(fileShare.getAttachment()).isNull();
    }

    @Test
    void sharedWithTest() {
        FileShare fileShare = getFileShareRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        fileShare.setSharedWith(partyBack);
        assertThat(fileShare.getSharedWith()).isEqualTo(partyBack);

        fileShare.sharedWith(null);
        assertThat(fileShare.getSharedWith()).isNull();
    }

    @Test
    void sharedByTest() {
        FileShare fileShare = getFileShareRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        fileShare.setSharedBy(partyBack);
        assertThat(fileShare.getSharedBy()).isEqualTo(partyBack);

        fileShare.sharedBy(null);
        assertThat(fileShare.getSharedBy()).isNull();
    }
}
