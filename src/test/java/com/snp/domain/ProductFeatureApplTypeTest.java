package com.snp.domain;

import static com.snp.domain.ProductFeatureApplTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureApplTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureApplType.class);
        ProductFeatureApplType productFeatureApplType1 = getProductFeatureApplTypeSample1();
        ProductFeatureApplType productFeatureApplType2 = new ProductFeatureApplType();
        assertThat(productFeatureApplType1).isNotEqualTo(productFeatureApplType2);

        productFeatureApplType2.setId(productFeatureApplType1.getId());
        assertThat(productFeatureApplType1).isEqualTo(productFeatureApplType2);

        productFeatureApplType2 = getProductFeatureApplTypeSample2();
        assertThat(productFeatureApplType1).isNotEqualTo(productFeatureApplType2);
    }
}
