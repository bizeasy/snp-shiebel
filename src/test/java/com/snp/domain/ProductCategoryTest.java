package com.snp.domain;

import static com.snp.domain.ProductCategoryTestSamples.*;
import static com.snp.domain.ProductCategoryTestSamples.*;
import static com.snp.domain.ProductCategoryTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategory.class);
        ProductCategory productCategory1 = getProductCategorySample1();
        ProductCategory productCategory2 = new ProductCategory();
        assertThat(productCategory1).isNotEqualTo(productCategory2);

        productCategory2.setId(productCategory1.getId());
        assertThat(productCategory1).isEqualTo(productCategory2);

        productCategory2 = getProductCategorySample2();
        assertThat(productCategory1).isNotEqualTo(productCategory2);
    }

    @Test
    void productCategoryTypeTest() {
        ProductCategory productCategory = getProductCategoryRandomSampleGenerator();
        ProductCategoryType productCategoryTypeBack = getProductCategoryTypeRandomSampleGenerator();

        productCategory.setProductCategoryType(productCategoryTypeBack);
        assertThat(productCategory.getProductCategoryType()).isEqualTo(productCategoryTypeBack);

        productCategory.productCategoryType(null);
        assertThat(productCategory.getProductCategoryType()).isNull();
    }

    @Test
    void parentTest() {
        ProductCategory productCategory = getProductCategoryRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        productCategory.setParent(productCategoryBack);
        assertThat(productCategory.getParent()).isEqualTo(productCategoryBack);

        productCategory.parent(null);
        assertThat(productCategory.getParent()).isNull();
    }
}
