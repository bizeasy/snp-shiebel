package com.snp.domain;

import static com.snp.domain.ProductAssocTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAssocTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAssocType.class);
        ProductAssocType productAssocType1 = getProductAssocTypeSample1();
        ProductAssocType productAssocType2 = new ProductAssocType();
        assertThat(productAssocType1).isNotEqualTo(productAssocType2);

        productAssocType2.setId(productAssocType1.getId());
        assertThat(productAssocType1).isEqualTo(productAssocType2);

        productAssocType2 = getProductAssocTypeSample2();
        assertThat(productAssocType1).isNotEqualTo(productAssocType2);
    }
}
