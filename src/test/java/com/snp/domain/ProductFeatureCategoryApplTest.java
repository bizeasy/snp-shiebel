package com.snp.domain;

import static com.snp.domain.ProductCategoryTestSamples.*;
import static com.snp.domain.ProductFeatureCategoryApplTestSamples.*;
import static com.snp.domain.ProductFeatureCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeatureCategoryApplTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeatureCategoryAppl.class);
        ProductFeatureCategoryAppl productFeatureCategoryAppl1 = getProductFeatureCategoryApplSample1();
        ProductFeatureCategoryAppl productFeatureCategoryAppl2 = new ProductFeatureCategoryAppl();
        assertThat(productFeatureCategoryAppl1).isNotEqualTo(productFeatureCategoryAppl2);

        productFeatureCategoryAppl2.setId(productFeatureCategoryAppl1.getId());
        assertThat(productFeatureCategoryAppl1).isEqualTo(productFeatureCategoryAppl2);

        productFeatureCategoryAppl2 = getProductFeatureCategoryApplSample2();
        assertThat(productFeatureCategoryAppl1).isNotEqualTo(productFeatureCategoryAppl2);
    }

    @Test
    void productCategoryTest() {
        ProductFeatureCategoryAppl productFeatureCategoryAppl = getProductFeatureCategoryApplRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        productFeatureCategoryAppl.setProductCategory(productCategoryBack);
        assertThat(productFeatureCategoryAppl.getProductCategory()).isEqualTo(productCategoryBack);

        productFeatureCategoryAppl.productCategory(null);
        assertThat(productFeatureCategoryAppl.getProductCategory()).isNull();
    }

    @Test
    void productFeatureCategoryTest() {
        ProductFeatureCategoryAppl productFeatureCategoryAppl = getProductFeatureCategoryApplRandomSampleGenerator();
        ProductFeatureCategory productFeatureCategoryBack = getProductFeatureCategoryRandomSampleGenerator();

        productFeatureCategoryAppl.setProductFeatureCategory(productFeatureCategoryBack);
        assertThat(productFeatureCategoryAppl.getProductFeatureCategory()).isEqualTo(productFeatureCategoryBack);

        productFeatureCategoryAppl.productFeatureCategory(null);
        assertThat(productFeatureCategoryAppl.getProductFeatureCategory()).isNull();
    }
}
