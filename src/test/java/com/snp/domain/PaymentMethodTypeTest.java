package com.snp.domain;

import static com.snp.domain.PaymentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentMethodTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodType.class);
        PaymentMethodType paymentMethodType1 = getPaymentMethodTypeSample1();
        PaymentMethodType paymentMethodType2 = new PaymentMethodType();
        assertThat(paymentMethodType1).isNotEqualTo(paymentMethodType2);

        paymentMethodType2.setId(paymentMethodType1.getId());
        assertThat(paymentMethodType1).isEqualTo(paymentMethodType2);

        paymentMethodType2 = getPaymentMethodTypeSample2();
        assertThat(paymentMethodType1).isNotEqualTo(paymentMethodType2);
    }
}
