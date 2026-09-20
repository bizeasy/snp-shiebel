package com.snp.domain;

import static com.snp.domain.ContentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentType.class);
        ContentType contentType1 = getContentTypeSample1();
        ContentType contentType2 = new ContentType();
        assertThat(contentType1).isNotEqualTo(contentType2);

        contentType2.setId(contentType1.getId());
        assertThat(contentType1).isEqualTo(contentType2);

        contentType2 = getContentTypeSample2();
        assertThat(contentType1).isNotEqualTo(contentType2);
    }
}
