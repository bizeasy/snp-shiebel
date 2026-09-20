package com.snp.domain;

import static com.snp.domain.ProductPricePurposeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPricePurposeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPricePurpose.class);
        ProductPricePurpose productPricePurpose1 = getProductPricePurposeSample1();
        ProductPricePurpose productPricePurpose2 = new ProductPricePurpose();
        assertThat(productPricePurpose1).isNotEqualTo(productPricePurpose2);

        productPricePurpose2.setId(productPricePurpose1.getId());
        assertThat(productPricePurpose1).isEqualTo(productPricePurpose2);

        productPricePurpose2 = getProductPricePurposeSample2();
        assertThat(productPricePurpose1).isNotEqualTo(productPricePurpose2);
    }
}
