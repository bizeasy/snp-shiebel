package com.snp.domain;

import static com.snp.domain.PaymentGatewayConfigTestSamples.*;
import static com.snp.domain.PaymentGatewayResponseTestSamples.*;
import static com.snp.domain.PaymentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentGatewayResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGatewayResponse.class);
        PaymentGatewayResponse paymentGatewayResponse1 = getPaymentGatewayResponseSample1();
        PaymentGatewayResponse paymentGatewayResponse2 = new PaymentGatewayResponse();
        assertThat(paymentGatewayResponse1).isNotEqualTo(paymentGatewayResponse2);

        paymentGatewayResponse2.setId(paymentGatewayResponse1.getId());
        assertThat(paymentGatewayResponse1).isEqualTo(paymentGatewayResponse2);

        paymentGatewayResponse2 = getPaymentGatewayResponseSample2();
        assertThat(paymentGatewayResponse1).isNotEqualTo(paymentGatewayResponse2);
    }

    @Test
    void paymentGatewayConfigTest() {
        PaymentGatewayResponse paymentGatewayResponse = getPaymentGatewayResponseRandomSampleGenerator();
        PaymentGatewayConfig paymentGatewayConfigBack = getPaymentGatewayConfigRandomSampleGenerator();

        paymentGatewayResponse.setPaymentGatewayConfig(paymentGatewayConfigBack);
        assertThat(paymentGatewayResponse.getPaymentGatewayConfig()).isEqualTo(paymentGatewayConfigBack);

        paymentGatewayResponse.paymentGatewayConfig(null);
        assertThat(paymentGatewayResponse.getPaymentGatewayConfig()).isNull();
    }

    @Test
    void paymentMethodTypeTest() {
        PaymentGatewayResponse paymentGatewayResponse = getPaymentGatewayResponseRandomSampleGenerator();
        PaymentMethodType paymentMethodTypeBack = getPaymentMethodTypeRandomSampleGenerator();

        paymentGatewayResponse.setPaymentMethodType(paymentMethodTypeBack);
        assertThat(paymentGatewayResponse.getPaymentMethodType()).isEqualTo(paymentMethodTypeBack);

        paymentGatewayResponse.paymentMethodType(null);
        assertThat(paymentGatewayResponse.getPaymentMethodType()).isNull();
    }
}
