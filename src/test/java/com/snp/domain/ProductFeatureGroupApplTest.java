package com.snp.domain;

import static com.snp.domain.ProductFeatureGroupApplTestSamples.*;
import static com.snp.domain.ProductFeatureGroupTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureGroupApplTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureGroupAppl.class);
        ProductFeatureGroupAppl productFeatureGroupAppl1 = getProductFeatureGroupApplSample1();
        ProductFeatureGroupAppl productFeatureGroupAppl2 = new ProductFeatureGroupAppl();
        assertThat(productFeatureGroupAppl1).isNotEqualTo(productFeatureGroupAppl2);

        productFeatureGroupAppl2.setId(productFeatureGroupAppl1.getId());
        assertThat(productFeatureGroupAppl1).isEqualTo(productFeatureGroupAppl2);

        productFeatureGroupAppl2 = getProductFeatureGroupApplSample2();
        assertThat(productFeatureGroupAppl1).isNotEqualTo(productFeatureGroupAppl2);
    }

    @Test
    void productFeatureGroupTest() {
        ProductFeatureGroupAppl productFeatureGroupAppl = getProductFeatureGroupApplRandomSampleGenerator();
        ProductFeatureGroup productFeatureGroupBack = getProductFeatureGroupRandomSampleGenerator();

        productFeatureGroupAppl.setProductFeatureGroup(productFeatureGroupBack);
        assertThat(productFeatureGroupAppl.getProductFeatureGroup()).isEqualTo(productFeatureGroupBack);

        productFeatureGroupAppl.productFeatureGroup(null);
        assertThat(productFeatureGroupAppl.getProductFeatureGroup()).isNull();
    }

    @Test
    void productFeatureTest() {
        ProductFeatureGroupAppl productFeatureGroupAppl = getProductFeatureGroupApplRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        productFeatureGroupAppl.setProductFeature(productFeatureBack);
        assertThat(productFeatureGroupAppl.getProductFeature()).isEqualTo(productFeatureBack);

        productFeatureGroupAppl.productFeature(null);
        assertThat(productFeatureGroupAppl.getProductFeature()).isNull();
    }
}
