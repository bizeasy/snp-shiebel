package com.snp.domain;

import static com.snp.domain.ProductCategoryTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductCategoryTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategoryType.class);
        ProductCategoryType productCategoryType1 = getProductCategoryTypeSample1();
        ProductCategoryType productCategoryType2 = new ProductCategoryType();
        assertThat(productCategoryType1).isNotEqualTo(productCategoryType2);

        productCategoryType2.setId(productCategoryType1.getId());
        assertThat(productCategoryType1).isEqualTo(productCategoryType2);

        productCategoryType2 = getProductCategoryTypeSample2();
        assertThat(productCategoryType1).isNotEqualTo(productCategoryType2);
    }
}
