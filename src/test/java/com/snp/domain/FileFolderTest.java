package com.snp.domain;

import static com.snp.domain.FileFolderTestSamples.*;
import static com.snp.domain.FileFolderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileFolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileFolder.class);
        FileFolder fileFolder1 = getFileFolderSample1();
        FileFolder fileFolder2 = new FileFolder();
        assertThat(fileFolder1).isNotEqualTo(fileFolder2);

        fileFolder2.setId(fileFolder1.getId());
        assertThat(fileFolder1).isEqualTo(fileFolder2);

        fileFolder2 = getFileFolderSample2();
        assertThat(fileFolder1).isNotEqualTo(fileFolder2);
    }

    @Test
    void parentTest() {
        FileFolder fileFolder = getFileFolderRandomSampleGenerator();
        FileFolder fileFolderBack = getFileFolderRandomSampleGenerator();

        fileFolder.setParent(fileFolderBack);
        assertThat(fileFolder.getParent()).isEqualTo(fileFolderBack);

        fileFolder.parent(null);
        assertThat(fileFolder.getParent()).isNull();
    }

    @Test
    void ownerTest() {
        FileFolder fileFolder = getFileFolderRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        fileFolder.setOwner(partyBack);
        assertThat(fileFolder.getOwner()).isEqualTo(partyBack);

        fileFolder.owner(null);
        assertThat(fileFolder.getOwner()).isNull();
    }
}
