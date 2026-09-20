package com.snp.domain;

import static com.snp.domain.ProductPriceTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPriceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPriceType.class);
        ProductPriceType productPriceType1 = getProductPriceTypeSample1();
        ProductPriceType productPriceType2 = new ProductPriceType();
        assertThat(productPriceType1).isNotEqualTo(productPriceType2);

        productPriceType2.setId(productPriceType1.getId());
        assertThat(productPriceType1).isEqualTo(productPriceType2);

        productPriceType2 = getProductPriceTypeSample2();
        assertThat(productPriceType1).isNotEqualTo(productPriceType2);
    }
}
