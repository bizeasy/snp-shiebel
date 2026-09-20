package com.snp.domain;

import static com.snp.domain.ProductCategoryMemberTestSamples.*;
import static com.snp.domain.ProductCategoryTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductCategoryMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategoryMember.class);
        ProductCategoryMember productCategoryMember1 = getProductCategoryMemberSample1();
        ProductCategoryMember productCategoryMember2 = new ProductCategoryMember();
        assertThat(productCategoryMember1).isNotEqualTo(productCategoryMember2);

        productCategoryMember2.setId(productCategoryMember1.getId());
        assertThat(productCategoryMember1).isEqualTo(productCategoryMember2);

        productCategoryMember2 = getProductCategoryMemberSample2();
        assertThat(productCategoryMember1).isNotEqualTo(productCategoryMember2);
    }

    @Test
    void productTest() {
        ProductCategoryMember productCategoryMember = getProductCategoryMemberRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productCategoryMember.setProduct(productBack);
        assertThat(productCategoryMember.getProduct()).isEqualTo(productBack);

        productCategoryMember.product(null);
        assertThat(productCategoryMember.getProduct()).isNull();
    }

    @Test
    void productCategoryTest() {
        ProductCategoryMember productCategoryMember = getProductCategoryMemberRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        productCategoryMember.setProductCategory(productCategoryBack);
        assertThat(productCategoryMember.getProductCategory()).isEqualTo(productCategoryBack);

        productCategoryMember.productCategory(null);
        assertThat(productCategoryMember.getProductCategory()).isNull();
    }
}
