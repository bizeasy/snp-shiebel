package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PaymentMethodTestSamples.*;
import static com.snp.domain.PaymentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = getPaymentMethodSample1();
        PaymentMethod paymentMethod2 = new PaymentMethod();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);

        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);

        paymentMethod2 = getPaymentMethodSample2();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    void paymentMethodTypeTest() {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        PaymentMethodType paymentMethodTypeBack = getPaymentMethodTypeRandomSampleGenerator();

        paymentMethod.setPaymentMethodType(paymentMethodTypeBack);
        assertThat(paymentMethod.getPaymentMethodType()).isEqualTo(paymentMethodTypeBack);

        paymentMethod.paymentMethodType(null);
        assertThat(paymentMethod.getPaymentMethodType()).isNull();
    }

    @Test
    void partyTest() {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        paymentMethod.setParty(partyBack);
        assertThat(paymentMethod.getParty()).isEqualTo(partyBack);

        paymentMethod.party(null);
        assertThat(paymentMethod.getParty()).isNull();
    }
}
