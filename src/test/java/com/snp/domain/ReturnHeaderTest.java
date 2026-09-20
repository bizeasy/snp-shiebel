package com.snp.domain;

import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PaymentMethodTestSamples.*;
import static com.snp.domain.ReturnHeaderTestSamples.*;
import static com.snp.domain.ReturnHeaderTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnHeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnHeader.class);
        ReturnHeader returnHeader1 = getReturnHeaderSample1();
        ReturnHeader returnHeader2 = new ReturnHeader();
        assertThat(returnHeader1).isNotEqualTo(returnHeader2);

        returnHeader2.setId(returnHeader1.getId());
        assertThat(returnHeader1).isEqualTo(returnHeader2);

        returnHeader2 = getReturnHeaderSample2();
        assertThat(returnHeader1).isNotEqualTo(returnHeader2);
    }

    @Test
    void returnTypeTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        ReturnHeaderType returnHeaderTypeBack = getReturnHeaderTypeRandomSampleGenerator();

        returnHeader.setReturnType(returnHeaderTypeBack);
        assertThat(returnHeader.getReturnType()).isEqualTo(returnHeaderTypeBack);

        returnHeader.returnType(null);
        assertThat(returnHeader.getReturnType()).isNull();
    }

    @Test
    void statusTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        returnHeader.setStatus(statusBack);
        assertThat(returnHeader.getStatus()).isEqualTo(statusBack);

        returnHeader.status(null);
        assertThat(returnHeader.getStatus()).isNull();
    }

    @Test
    void fromPartyTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        returnHeader.setFromParty(partyBack);
        assertThat(returnHeader.getFromParty()).isEqualTo(partyBack);

        returnHeader.fromParty(null);
        assertThat(returnHeader.getFromParty()).isNull();
    }

    @Test
    void toPartyTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        returnHeader.setToParty(partyBack);
        assertThat(returnHeader.getToParty()).isEqualTo(partyBack);

        returnHeader.toParty(null);
        assertThat(returnHeader.getToParty()).isNull();
    }

    @Test
    void paymentMethodTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        returnHeader.setPaymentMethod(paymentMethodBack);
        assertThat(returnHeader.getPaymentMethod()).isEqualTo(paymentMethodBack);

        returnHeader.paymentMethod(null);
        assertThat(returnHeader.getPaymentMethod()).isNull();
    }

    @Test
    void originContactMechTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        returnHeader.setOriginContactMech(contactMechBack);
        assertThat(returnHeader.getOriginContactMech()).isEqualTo(contactMechBack);

        returnHeader.originContactMech(null);
        assertThat(returnHeader.getOriginContactMech()).isNull();
    }

    @Test
    void destinationFacilityTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        returnHeader.setDestinationFacility(facilityBack);
        assertThat(returnHeader.getDestinationFacility()).isEqualTo(facilityBack);

        returnHeader.destinationFacility(null);
        assertThat(returnHeader.getDestinationFacility()).isNull();
    }

    @Test
    void currencyUomTest() {
        ReturnHeader returnHeader = getReturnHeaderRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        returnHeader.setCurrencyUom(uomBack);
        assertThat(returnHeader.getCurrencyUom()).isEqualTo(uomBack);

        returnHeader.currencyUom(null);
        assertThat(returnHeader.getCurrencyUom()).isNull();
    }
}
