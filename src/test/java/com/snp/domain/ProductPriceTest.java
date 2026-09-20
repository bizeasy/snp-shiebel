package com.snp.domain;

import static com.snp.domain.ProductPricePurposeTestSamples.*;
import static com.snp.domain.ProductPriceTestSamples.*;
import static com.snp.domain.ProductPriceTypeTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPrice.class);
        ProductPrice productPrice1 = getProductPriceSample1();
        ProductPrice productPrice2 = new ProductPrice();
        assertThat(productPrice1).isNotEqualTo(productPrice2);

        productPrice2.setId(productPrice1.getId());
        assertThat(productPrice1).isEqualTo(productPrice2);

        productPrice2 = getProductPriceSample2();
        assertThat(productPrice1).isNotEqualTo(productPrice2);
    }

    @Test
    void productTest() {
        ProductPrice productPrice = getProductPriceRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productPrice.setProduct(productBack);
        assertThat(productPrice.getProduct()).isEqualTo(productBack);

        productPrice.product(null);
        assertThat(productPrice.getProduct()).isNull();
    }

    @Test
    void productPriceTypeTest() {
        ProductPrice productPrice = getProductPriceRandomSampleGenerator();
        ProductPriceType productPriceTypeBack = getProductPriceTypeRandomSampleGenerator();

        productPrice.setProductPriceType(productPriceTypeBack);
        assertThat(productPrice.getProductPriceType()).isEqualTo(productPriceTypeBack);

        productPrice.productPriceType(null);
        assertThat(productPrice.getProductPriceType()).isNull();
    }

    @Test
    void productPricePurposeTest() {
        ProductPrice productPrice = getProductPriceRandomSampleGenerator();
        ProductPricePurpose productPricePurposeBack = getProductPricePurposeRandomSampleGenerator();

        productPrice.setProductPricePurpose(productPricePurposeBack);
        assertThat(productPrice.getProductPricePurpose()).isEqualTo(productPricePurposeBack);

        productPrice.productPricePurpose(null);
        assertThat(productPrice.getProductPricePurpose()).isNull();
    }

    @Test
    void currencyUomTest() {
        ProductPrice productPrice = getProductPriceRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        productPrice.setCurrencyUom(uomBack);
        assertThat(productPrice.getCurrencyUom()).isEqualTo(uomBack);

        productPrice.currencyUom(null);
        assertThat(productPrice.getCurrencyUom()).isNull();
    }
}
