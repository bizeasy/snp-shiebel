package com.snp.domain;

import static com.snp.domain.PaymentGatewayConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentGatewayConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGatewayConfig.class);
        PaymentGatewayConfig paymentGatewayConfig1 = getPaymentGatewayConfigSample1();
        PaymentGatewayConfig paymentGatewayConfig2 = new PaymentGatewayConfig();
        assertThat(paymentGatewayConfig1).isNotEqualTo(paymentGatewayConfig2);

        paymentGatewayConfig2.setId(paymentGatewayConfig1.getId());
        assertThat(paymentGatewayConfig1).isEqualTo(paymentGatewayConfig2);

        paymentGatewayConfig2 = getPaymentGatewayConfigSample2();
        assertThat(paymentGatewayConfig1).isNotEqualTo(paymentGatewayConfig2);
    }
}
