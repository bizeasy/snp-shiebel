package com.snp.domain;

import static com.snp.domain.ProductFeatureCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureCategory.class);
        ProductFeatureCategory productFeatureCategory1 = getProductFeatureCategorySample1();
        ProductFeatureCategory productFeatureCategory2 = new ProductFeatureCategory();
        assertThat(productFeatureCategory1).isNotEqualTo(productFeatureCategory2);

        productFeatureCategory2.setId(productFeatureCategory1.getId());
        assertThat(productFeatureCategory1).isEqualTo(productFeatureCategory2);

        productFeatureCategory2 = getProductFeatureCategorySample2();
        assertThat(productFeatureCategory1).isNotEqualTo(productFeatureCategory2);
    }
}
