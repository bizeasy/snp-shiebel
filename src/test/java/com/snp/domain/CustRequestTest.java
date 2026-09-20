package com.snp.domain;

import static com.snp.domain.CustRequestCategoryTestSamples.*;
import static com.snp.domain.CustRequestTestSamples.*;
import static com.snp.domain.CustRequestTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static com.snp.domain.SalesChannelTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequest.class);
        CustRequest custRequest1 = getCustRequestSample1();
        CustRequest custRequest2 = new CustRequest();
        assertThat(custRequest1).isNotEqualTo(custRequest2);

        custRequest2.setId(custRequest1.getId());
        assertThat(custRequest1).isEqualTo(custRequest2);

        custRequest2 = getCustRequestSample2();
        assertThat(custRequest1).isNotEqualTo(custRequest2);
    }

    @Test
    void custRequestTypeTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        CustRequestType custRequestTypeBack = getCustRequestTypeRandomSampleGenerator();

        custRequest.setCustRequestType(custRequestTypeBack);
        assertThat(custRequest.getCustRequestType()).isEqualTo(custRequestTypeBack);

        custRequest.custRequestType(null);
        assertThat(custRequest.getCustRequestType()).isNull();
    }

    @Test
    void custRequestCategoryTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        CustRequestCategory custRequestCategoryBack = getCustRequestCategoryRandomSampleGenerator();

        custRequest.setCustRequestCategory(custRequestCategoryBack);
        assertThat(custRequest.getCustRequestCategory()).isEqualTo(custRequestCategoryBack);

        custRequest.custRequestCategory(null);
        assertThat(custRequest.getCustRequestCategory()).isNull();
    }

    @Test
    void statusTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        custRequest.setStatus(statusBack);
        assertThat(custRequest.getStatus()).isEqualTo(statusBack);

        custRequest.status(null);
        assertThat(custRequest.getStatus()).isNull();
    }

    @Test
    void fromPartyTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        custRequest.setFromParty(partyBack);
        assertThat(custRequest.getFromParty()).isEqualTo(partyBack);

        custRequest.fromParty(null);
        assertThat(custRequest.getFromParty()).isNull();
    }

    @Test
    void productStoreTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        ProductStore productStoreBack = getProductStoreRandomSampleGenerator();

        custRequest.setProductStore(productStoreBack);
        assertThat(custRequest.getProductStore()).isEqualTo(productStoreBack);

        custRequest.productStore(null);
        assertThat(custRequest.getProductStore()).isNull();
    }

    @Test
    void salesChannelTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        SalesChannel salesChannelBack = getSalesChannelRandomSampleGenerator();

        custRequest.setSalesChannel(salesChannelBack);
        assertThat(custRequest.getSalesChannel()).isEqualTo(salesChannelBack);

        custRequest.salesChannel(null);
        assertThat(custRequest.getSalesChannel()).isNull();
    }

    @Test
    void currencyUomTest() {
        CustRequest custRequest = getCustRequestRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        custRequest.setCurrencyUom(uomBack);
        assertThat(custRequest.getCurrencyUom()).isEqualTo(uomBack);

        custRequest.currencyUom(null);
        assertThat(custRequest.getCurrencyUom()).isNull();
    }
}
