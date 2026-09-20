package com.snp.domain;

import static com.snp.domain.OrderAdjustmentTypeTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.QuoteAdjustmentTestSamples.*;
import static com.snp.domain.QuoteItemTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static com.snp.domain.TaxAuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteAdjustmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteAdjustment.class);
        QuoteAdjustment quoteAdjustment1 = getQuoteAdjustmentSample1();
        QuoteAdjustment quoteAdjustment2 = new QuoteAdjustment();
        assertThat(quoteAdjustment1).isNotEqualTo(quoteAdjustment2);

        quoteAdjustment2.setId(quoteAdjustment1.getId());
        assertThat(quoteAdjustment1).isEqualTo(quoteAdjustment2);

        quoteAdjustment2 = getQuoteAdjustmentSample2();
        assertThat(quoteAdjustment1).isNotEqualTo(quoteAdjustment2);
    }

    @Test
    void quoteTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        quoteAdjustment.setQuote(quoteBack);
        assertThat(quoteAdjustment.getQuote()).isEqualTo(quoteBack);

        quoteAdjustment.quote(null);
        assertThat(quoteAdjustment.getQuote()).isNull();
    }

    @Test
    void quoteItemTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        QuoteItem quoteItemBack = getQuoteItemRandomSampleGenerator();

        quoteAdjustment.setQuoteItem(quoteItemBack);
        assertThat(quoteAdjustment.getQuoteItem()).isEqualTo(quoteItemBack);

        quoteAdjustment.quoteItem(null);
        assertThat(quoteAdjustment.getQuoteItem()).isNull();
    }

    @Test
    void adjustmentTypeTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        OrderAdjustmentType orderAdjustmentTypeBack = getOrderAdjustmentTypeRandomSampleGenerator();

        quoteAdjustment.setAdjustmentType(orderAdjustmentTypeBack);
        assertThat(quoteAdjustment.getAdjustmentType()).isEqualTo(orderAdjustmentTypeBack);

        quoteAdjustment.adjustmentType(null);
        assertThat(quoteAdjustment.getAdjustmentType()).isNull();
    }

    @Test
    void productTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        quoteAdjustment.setProduct(productBack);
        assertThat(quoteAdjustment.getProduct()).isEqualTo(productBack);

        quoteAdjustment.product(null);
        assertThat(quoteAdjustment.getProduct()).isNull();
    }

    @Test
    void productFeatureTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        quoteAdjustment.setProductFeature(productFeatureBack);
        assertThat(quoteAdjustment.getProductFeature()).isEqualTo(productFeatureBack);

        quoteAdjustment.productFeature(null);
        assertThat(quoteAdjustment.getProductFeature()).isNull();
    }

    @Test
    void taxAuthorityTest() {
        QuoteAdjustment quoteAdjustment = getQuoteAdjustmentRandomSampleGenerator();
        TaxAuthority taxAuthorityBack = getTaxAuthorityRandomSampleGenerator();

        quoteAdjustment.setTaxAuthority(taxAuthorityBack);
        assertThat(quoteAdjustment.getTaxAuthority()).isEqualTo(taxAuthorityBack);

        quoteAdjustment.taxAuthority(null);
        assertThat(quoteAdjustment.getTaxAuthority()).isNull();
    }
}
