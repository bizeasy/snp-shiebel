package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.ContentTypeTestSamples.*;
import static com.snp.domain.FileFolderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachment.class);
        Attachment attachment1 = getAttachmentSample1();
        Attachment attachment2 = new Attachment();
        assertThat(attachment1).isNotEqualTo(attachment2);

        attachment2.setId(attachment1.getId());
        assertThat(attachment1).isEqualTo(attachment2);

        attachment2 = getAttachmentSample2();
        assertThat(attachment1).isNotEqualTo(attachment2);
    }

    @Test
    void contentTypeTest() {
        Attachment attachment = getAttachmentRandomSampleGenerator();
        ContentType contentTypeBack = getContentTypeRandomSampleGenerator();

        attachment.setContentType(contentTypeBack);
        assertThat(attachment.getContentType()).isEqualTo(contentTypeBack);

        attachment.contentType(null);
        assertThat(attachment.getContentType()).isNull();
    }

    @Test
    void folderTest() {
        Attachment attachment = getAttachmentRandomSampleGenerator();
        FileFolder fileFolderBack = getFileFolderRandomSampleGenerator();

        attachment.setFolder(fileFolderBack);
        assertThat(attachment.getFolder()).isEqualTo(fileFolderBack);

        attachment.folder(null);
        assertThat(attachment.getFolder()).isNull();
    }

    @Test
    void ownerTest() {
        Attachment attachment = getAttachmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        attachment.setOwner(partyBack);
        assertThat(attachment.getOwner()).isEqualTo(partyBack);

        attachment.owner(null);
        assertThat(attachment.getOwner()).isNull();
    }
}
