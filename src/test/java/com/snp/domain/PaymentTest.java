package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PaymentGatewayResponseTestSamples.*;
import static com.snp.domain.PaymentMethodTestSamples.*;
import static com.snp.domain.PaymentMethodTypeTestSamples.*;
import static com.snp.domain.PaymentTestSamples.*;
import static com.snp.domain.PaymentTypeTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void paymentTypeTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentType paymentTypeBack = getPaymentTypeRandomSampleGenerator();

        payment.setPaymentType(paymentTypeBack);
        assertThat(payment.getPaymentType()).isEqualTo(paymentTypeBack);

        payment.paymentType(null);
        assertThat(payment.getPaymentType()).isNull();
    }

    @Test
    void paymentMethodTypeTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentMethodType paymentMethodTypeBack = getPaymentMethodTypeRandomSampleGenerator();

        payment.setPaymentMethodType(paymentMethodTypeBack);
        assertThat(payment.getPaymentMethodType()).isEqualTo(paymentMethodTypeBack);

        payment.paymentMethodType(null);
        assertThat(payment.getPaymentMethodType()).isNull();
    }

    @Test
    void paymentMethodTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        payment.setPaymentMethod(paymentMethodBack);
        assertThat(payment.getPaymentMethod()).isEqualTo(paymentMethodBack);

        payment.paymentMethod(null);
        assertThat(payment.getPaymentMethod()).isNull();
    }

    @Test
    void paymentGatewayResponseTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentGatewayResponse paymentGatewayResponseBack = getPaymentGatewayResponseRandomSampleGenerator();

        payment.setPaymentGatewayResponse(paymentGatewayResponseBack);
        assertThat(payment.getPaymentGatewayResponse()).isEqualTo(paymentGatewayResponseBack);

        payment.paymentGatewayResponse(null);
        assertThat(payment.getPaymentGatewayResponse()).isNull();
    }

    @Test
    void statusTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        payment.setStatus(statusBack);
        assertThat(payment.getStatus()).isEqualTo(statusBack);

        payment.status(null);
        assertThat(payment.getStatus()).isNull();
    }

    @Test
    void fromPartyTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        payment.setFromParty(partyBack);
        assertThat(payment.getFromParty()).isEqualTo(partyBack);

        payment.fromParty(null);
        assertThat(payment.getFromParty()).isNull();
    }

    @Test
    void toPartyTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        payment.setToParty(partyBack);
        assertThat(payment.getToParty()).isEqualTo(partyBack);

        payment.toParty(null);
        assertThat(payment.getToParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        payment.setRoleType(roleTypeBack);
        assertThat(payment.getRoleType()).isEqualTo(roleTypeBack);

        payment.roleType(null);
        assertThat(payment.getRoleType()).isNull();
    }

    @Test
    void currencyUomTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        payment.setCurrencyUom(uomBack);
        assertThat(payment.getCurrencyUom()).isEqualTo(uomBack);

        payment.currencyUom(null);
        assertThat(payment.getCurrencyUom()).isNull();
    }
}
