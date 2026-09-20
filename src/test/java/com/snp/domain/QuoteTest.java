package com.snp.domain;

import static com.snp.domain.CustRequestTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static com.snp.domain.QuoteTypeTestSamples.*;
import static com.snp.domain.SalesChannelTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomConversionTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quote.class);
        Quote quote1 = getQuoteSample1();
        Quote quote2 = new Quote();
        assertThat(quote1).isNotEqualTo(quote2);

        quote2.setId(quote1.getId());
        assertThat(quote1).isEqualTo(quote2);

        quote2 = getQuoteSample2();
        assertThat(quote1).isNotEqualTo(quote2);
    }

    @Test
    void quoteTypeTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        QuoteType quoteTypeBack = getQuoteTypeRandomSampleGenerator();

        quote.setQuoteType(quoteTypeBack);
        assertThat(quote.getQuoteType()).isEqualTo(quoteTypeBack);

        quote.quoteType(null);
        assertThat(quote.getQuoteType()).isNull();
    }

    @Test
    void custRequestTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        CustRequest custRequestBack = getCustRequestRandomSampleGenerator();

        quote.setCustRequest(custRequestBack);
        assertThat(quote.getCustRequest()).isEqualTo(custRequestBack);

        quote.custRequest(null);
        assertThat(quote.getCustRequest()).isNull();
    }

    @Test
    void partyTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        quote.setParty(partyBack);
        assertThat(quote.getParty()).isEqualTo(partyBack);

        quote.party(null);
        assertThat(quote.getParty()).isNull();
    }

    @Test
    void statusTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        quote.setStatus(statusBack);
        assertThat(quote.getStatus()).isEqualTo(statusBack);

        quote.status(null);
        assertThat(quote.getStatus()).isNull();
    }

    @Test
    void productStoreTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        ProductStore productStoreBack = getProductStoreRandomSampleGenerator();

        quote.setProductStore(productStoreBack);
        assertThat(quote.getProductStore()).isEqualTo(productStoreBack);

        quote.productStore(null);
        assertThat(quote.getProductStore()).isNull();
    }

    @Test
    void salesChannelTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        SalesChannel salesChannelBack = getSalesChannelRandomSampleGenerator();

        quote.setSalesChannel(salesChannelBack);
        assertThat(quote.getSalesChannel()).isEqualTo(salesChannelBack);

        quote.salesChannel(null);
        assertThat(quote.getSalesChannel()).isNull();
    }

    @Test
    void currencyUomTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        quote.setCurrencyUom(uomBack);
        assertThat(quote.getCurrencyUom()).isEqualTo(uomBack);

        quote.currencyUom(null);
        assertThat(quote.getCurrencyUom()).isNull();
    }

    @Test
    void currencyConversionTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        UomConversion uomConversionBack = getUomConversionRandomSampleGenerator();

        quote.setCurrencyConversion(uomConversionBack);
        assertThat(quote.getCurrencyConversion()).isEqualTo(uomConversionBack);

        quote.currencyConversion(null);
        assertThat(quote.getCurrencyConversion()).isNull();
    }
}
