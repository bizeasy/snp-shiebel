package com.snp.domain;

import static com.snp.domain.ProductAssocTestSamples.*;
import static com.snp.domain.ProductAssocTypeTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAssocTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAssoc.class);
        ProductAssoc productAssoc1 = getProductAssocSample1();
        ProductAssoc productAssoc2 = new ProductAssoc();
        assertThat(productAssoc1).isNotEqualTo(productAssoc2);

        productAssoc2.setId(productAssoc1.getId());
        assertThat(productAssoc1).isEqualTo(productAssoc2);

        productAssoc2 = getProductAssocSample2();
        assertThat(productAssoc1).isNotEqualTo(productAssoc2);
    }

    @Test
    void productTest() {
        ProductAssoc productAssoc = getProductAssocRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productAssoc.setProduct(productBack);
        assertThat(productAssoc.getProduct()).isEqualTo(productBack);

        productAssoc.product(null);
        assertThat(productAssoc.getProduct()).isNull();
    }

    @Test
    void productToTest() {
        ProductAssoc productAssoc = getProductAssocRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productAssoc.setProductTo(productBack);
        assertThat(productAssoc.getProductTo()).isEqualTo(productBack);

        productAssoc.productTo(null);
        assertThat(productAssoc.getProductTo()).isNull();
    }

    @Test
    void productAssocTypeTest() {
        ProductAssoc productAssoc = getProductAssocRandomSampleGenerator();
        ProductAssocType productAssocTypeBack = getProductAssocTypeRandomSampleGenerator();

        productAssoc.setProductAssocType(productAssocTypeBack);
        assertThat(productAssoc.getProductAssocType()).isEqualTo(productAssocTypeBack);

        productAssoc.productAssocType(null);
        assertThat(productAssoc.getProductAssocType()).isNull();
    }
}
