package com.snp.domain;

import static com.snp.domain.ProductFeatureApplTestSamples.*;
import static com.snp.domain.ProductFeatureApplTypeTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureApplTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureAppl.class);
        ProductFeatureAppl productFeatureAppl1 = getProductFeatureApplSample1();
        ProductFeatureAppl productFeatureAppl2 = new ProductFeatureAppl();
        assertThat(productFeatureAppl1).isNotEqualTo(productFeatureAppl2);

        productFeatureAppl2.setId(productFeatureAppl1.getId());
        assertThat(productFeatureAppl1).isEqualTo(productFeatureAppl2);

        productFeatureAppl2 = getProductFeatureApplSample2();
        assertThat(productFeatureAppl1).isNotEqualTo(productFeatureAppl2);
    }

    @Test
    void productTest() {
        ProductFeatureAppl productFeatureAppl = getProductFeatureApplRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productFeatureAppl.setProduct(productBack);
        assertThat(productFeatureAppl.getProduct()).isEqualTo(productBack);

        productFeatureAppl.product(null);
        assertThat(productFeatureAppl.getProduct()).isNull();
    }

    @Test
    void productFeatureTest() {
        ProductFeatureAppl productFeatureAppl = getProductFeatureApplRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        productFeatureAppl.setProductFeature(productFeatureBack);
        assertThat(productFeatureAppl.getProductFeature()).isEqualTo(productFeatureBack);

        productFeatureAppl.productFeature(null);
        assertThat(productFeatureAppl.getProductFeature()).isNull();
    }

    @Test
    void featureApplTypeTest() {
        ProductFeatureAppl productFeatureAppl = getProductFeatureApplRandomSampleGenerator();
        ProductFeatureApplType productFeatureApplTypeBack = getProductFeatureApplTypeRandomSampleGenerator();

        productFeatureAppl.setFeatureApplType(productFeatureApplTypeBack);
        assertThat(productFeatureAppl.getFeatureApplType()).isEqualTo(productFeatureApplTypeBack);

        productFeatureAppl.featureApplType(null);
        assertThat(productFeatureAppl.getFeatureApplType()).isNull();
    }
}
