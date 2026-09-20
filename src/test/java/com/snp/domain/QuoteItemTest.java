package com.snp.domain;

import static com.snp.domain.CustRequestItemTestSamples.*;
import static com.snp.domain.CustRequestTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.QuoteItemTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteItem.class);
        QuoteItem quoteItem1 = getQuoteItemSample1();
        QuoteItem quoteItem2 = new QuoteItem();
        assertThat(quoteItem1).isNotEqualTo(quoteItem2);

        quoteItem2.setId(quoteItem1.getId());
        assertThat(quoteItem1).isEqualTo(quoteItem2);

        quoteItem2 = getQuoteItemSample2();
        assertThat(quoteItem1).isNotEqualTo(quoteItem2);
    }

    @Test
    void quoteTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        quoteItem.setQuote(quoteBack);
        assertThat(quoteItem.getQuote()).isEqualTo(quoteBack);

        quoteItem.quote(null);
        assertThat(quoteItem.getQuote()).isNull();
    }

    @Test
    void productTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        quoteItem.setProduct(productBack);
        assertThat(quoteItem.getProduct()).isEqualTo(productBack);

        quoteItem.product(null);
        assertThat(quoteItem.getProduct()).isNull();
    }

    @Test
    void productFeatureTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        quoteItem.setProductFeature(productFeatureBack);
        assertThat(quoteItem.getProductFeature()).isEqualTo(productFeatureBack);

        quoteItem.productFeature(null);
        assertThat(quoteItem.getProductFeature()).isNull();
    }

    @Test
    void uomTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        quoteItem.setUom(uomBack);
        assertThat(quoteItem.getUom()).isEqualTo(uomBack);

        quoteItem.uom(null);
        assertThat(quoteItem.getUom()).isNull();
    }

    @Test
    void statusTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        quoteItem.setStatus(statusBack);
        assertThat(quoteItem.getStatus()).isEqualTo(statusBack);

        quoteItem.status(null);
        assertThat(quoteItem.getStatus()).isNull();
    }

    @Test
    void custRequestTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        CustRequest custRequestBack = getCustRequestRandomSampleGenerator();

        quoteItem.setCustRequest(custRequestBack);
        assertThat(quoteItem.getCustRequest()).isEqualTo(custRequestBack);

        quoteItem.custRequest(null);
        assertThat(quoteItem.getCustRequest()).isNull();
    }

    @Test
    void custRequestItemTest() {
        QuoteItem quoteItem = getQuoteItemRandomSampleGenerator();
        CustRequestItem custRequestItemBack = getCustRequestItemRandomSampleGenerator();

        quoteItem.setCustRequestItem(custRequestItemBack);
        assertThat(quoteItem.getCustRequestItem()).isEqualTo(custRequestItemBack);

        quoteItem.custRequestItem(null);
        assertThat(quoteItem.getCustRequestItem()).isNull();
    }
}
