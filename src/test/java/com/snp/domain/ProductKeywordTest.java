package com.snp.domain;

import static com.snp.domain.KeywordTypeTestSamples.*;
import static com.snp.domain.ProductKeywordTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductKeywordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductKeyword.class);
        ProductKeyword productKeyword1 = getProductKeywordSample1();
        ProductKeyword productKeyword2 = new ProductKeyword();
        assertThat(productKeyword1).isNotEqualTo(productKeyword2);

        productKeyword2.setId(productKeyword1.getId());
        assertThat(productKeyword1).isEqualTo(productKeyword2);

        productKeyword2 = getProductKeywordSample2();
        assertThat(productKeyword1).isNotEqualTo(productKeyword2);
    }

    @Test
    void productTest() {
        ProductKeyword productKeyword = getProductKeywordRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productKeyword.setProduct(productBack);
        assertThat(productKeyword.getProduct()).isEqualTo(productBack);

        productKeyword.product(null);
        assertThat(productKeyword.getProduct()).isNull();
    }

    @Test
    void keywordTypeTest() {
        ProductKeyword productKeyword = getProductKeywordRandomSampleGenerator();
        KeywordType keywordTypeBack = getKeywordTypeRandomSampleGenerator();

        productKeyword.setKeywordType(keywordTypeBack);
        assertThat(productKeyword.getKeywordType()).isEqualTo(keywordTypeBack);

        productKeyword.keywordType(null);
        assertThat(productKeyword.getKeywordType()).isNull();
    }
}
