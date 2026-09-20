package com.snp.domain;

import static com.snp.domain.ProductFeatureCategoryTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductFeatureTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeature.class);
        ProductFeature productFeature1 = getProductFeatureSample1();
        ProductFeature productFeature2 = new ProductFeature();
        assertThat(productFeature1).isNotEqualTo(productFeature2);

        productFeature2.setId(productFeature1.getId());
        assertThat(productFeature1).isEqualTo(productFeature2);

        productFeature2 = getProductFeatureSample2();
        assertThat(productFeature1).isNotEqualTo(productFeature2);
    }

    @Test
    void productFeatureTypeTest() {
        ProductFeature productFeature = getProductFeatureRandomSampleGenerator();
        ProductFeatureType productFeatureTypeBack = getProductFeatureTypeRandomSampleGenerator();

        productFeature.setProductFeatureType(productFeatureTypeBack);
        assertThat(productFeature.getProductFeatureType()).isEqualTo(productFeatureTypeBack);

        productFeature.productFeatureType(null);
        assertThat(productFeature.getProductFeatureType()).isNull();
    }

    @Test
    void productFeatureCategoryTest() {
        ProductFeature productFeature = getProductFeatureRandomSampleGenerator();
        ProductFeatureCategory productFeatureCategoryBack = getProductFeatureCategoryRandomSampleGenerator();

        productFeature.setProductFeatureCategory(productFeatureCategoryBack);
        assertThat(productFeature.getProductFeatureCategory()).isEqualTo(productFeatureCategoryBack);

        productFeature.productFeatureCategory(null);
        assertThat(productFeature.getProductFeatureCategory()).isNull();
    }
}
