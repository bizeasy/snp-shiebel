package com.snp.domain;

import static com.snp.domain.ProductFeatureTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureType.class);
        ProductFeatureType productFeatureType1 = getProductFeatureTypeSample1();
        ProductFeatureType productFeatureType2 = new ProductFeatureType();
        assertThat(productFeatureType1).isNotEqualTo(productFeatureType2);

        productFeatureType2.setId(productFeatureType1.getId());
        assertThat(productFeatureType1).isEqualTo(productFeatureType2);

        productFeatureType2 = getProductFeatureTypeSample2();
        assertThat(productFeatureType1).isNotEqualTo(productFeatureType2);
    }
}
