package com.snp.domain;

import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PaymentApplicationTestSamples.*;
import static com.snp.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentApplication.class);
        PaymentApplication paymentApplication1 = getPaymentApplicationSample1();
        PaymentApplication paymentApplication2 = new PaymentApplication();
        assertThat(paymentApplication1).isNotEqualTo(paymentApplication2);

        paymentApplication2.setId(paymentApplication1.getId());
        assertThat(paymentApplication1).isEqualTo(paymentApplication2);

        paymentApplication2 = getPaymentApplicationSample2();
        assertThat(paymentApplication1).isNotEqualTo(paymentApplication2);
    }

    @Test
    void paymentTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        paymentApplication.setPayment(paymentBack);
        assertThat(paymentApplication.getPayment()).isEqualTo(paymentBack);

        paymentApplication.payment(null);
        assertThat(paymentApplication.getPayment()).isNull();
    }

    @Test
    void toPaymentTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        paymentApplication.setToPayment(paymentBack);
        assertThat(paymentApplication.getToPayment()).isEqualTo(paymentBack);

        paymentApplication.toPayment(null);
        assertThat(paymentApplication.getToPayment()).isNull();
    }

    @Test
    void invoiceTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        paymentApplication.setInvoice(invoiceBack);
        assertThat(paymentApplication.getInvoice()).isEqualTo(invoiceBack);

        paymentApplication.invoice(null);
        assertThat(paymentApplication.getInvoice()).isNull();
    }

    @Test
    void invoiceItemTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        paymentApplication.setInvoiceItem(invoiceItemBack);
        assertThat(paymentApplication.getInvoiceItem()).isEqualTo(invoiceItemBack);

        paymentApplication.invoiceItem(null);
        assertThat(paymentApplication.getInvoiceItem()).isNull();
    }

    @Test
    void orderTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        paymentApplication.setOrder(orderBack);
        assertThat(paymentApplication.getOrder()).isEqualTo(orderBack);

        paymentApplication.order(null);
        assertThat(paymentApplication.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        PaymentApplication paymentApplication = getPaymentApplicationRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        paymentApplication.setOrderItem(orderItemBack);
        assertThat(paymentApplication.getOrderItem()).isEqualTo(orderItemBack);

        paymentApplication.orderItem(null);
        assertThat(paymentApplication.getOrderItem()).isNull();
    }
}
