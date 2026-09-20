package com.snp.domain;

import static com.snp.domain.ProductFeatureGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureGroup.class);
        ProductFeatureGroup productFeatureGroup1 = getProductFeatureGroupSample1();
        ProductFeatureGroup productFeatureGroup2 = new ProductFeatureGroup();
        assertThat(productFeatureGroup1).isNotEqualTo(productFeatureGroup2);

        productFeatureGroup2.setId(productFeatureGroup1.getId());
        assertThat(productFeatureGroup1).isEqualTo(productFeatureGroup2);

        productFeatureGroup2 = getProductFeatureGroupSample2();
        assertThat(productFeatureGroup1).isNotEqualTo(productFeatureGroup2);
    }
}
