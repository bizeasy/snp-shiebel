package com.snp.domain;

import static com.snp.domain.KeywordTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KeywordTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeywordType.class);
        KeywordType keywordType1 = getKeywordTypeSample1();
        KeywordType keywordType2 = new KeywordType();
        assertThat(keywordType1).isNotEqualTo(keywordType2);

        keywordType2.setId(keywordType1.getId());
        assertThat(keywordType1).isEqualTo(keywordType2);

        keywordType2 = getKeywordTypeSample2();
        assertThat(keywordType1).isNotEqualTo(keywordType2);
    }
}
