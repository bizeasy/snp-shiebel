package com.snp.domain;

import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.ItemIssuanceTestSamples.*;
import static com.snp.domain.OrderItemBillingTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.ShipmentReceiptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemBillingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemBilling.class);
        OrderItemBilling orderItemBilling1 = getOrderItemBillingSample1();
        OrderItemBilling orderItemBilling2 = new OrderItemBilling();
        assertThat(orderItemBilling1).isNotEqualTo(orderItemBilling2);

        orderItemBilling2.setId(orderItemBilling1.getId());
        assertThat(orderItemBilling1).isEqualTo(orderItemBilling2);

        orderItemBilling2 = getOrderItemBillingSample2();
        assertThat(orderItemBilling1).isNotEqualTo(orderItemBilling2);
    }

    @Test
    void orderItemTest() {
        OrderItemBilling orderItemBilling = getOrderItemBillingRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderItemBilling.setOrderItem(orderItemBack);
        assertThat(orderItemBilling.getOrderItem()).isEqualTo(orderItemBack);

        orderItemBilling.orderItem(null);
        assertThat(orderItemBilling.getOrderItem()).isNull();
    }

    @Test
    void invoiceItemTest() {
        OrderItemBilling orderItemBilling = getOrderItemBillingRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        orderItemBilling.setInvoiceItem(invoiceItemBack);
        assertThat(orderItemBilling.getInvoiceItem()).isEqualTo(invoiceItemBack);

        orderItemBilling.invoiceItem(null);
        assertThat(orderItemBilling.getInvoiceItem()).isNull();
    }

    @Test
    void shipmentReceiptTest() {
        OrderItemBilling orderItemBilling = getOrderItemBillingRandomSampleGenerator();
        ShipmentReceipt shipmentReceiptBack = getShipmentReceiptRandomSampleGenerator();

        orderItemBilling.setShipmentReceipt(shipmentReceiptBack);
        assertThat(orderItemBilling.getShipmentReceipt()).isEqualTo(shipmentReceiptBack);

        orderItemBilling.shipmentReceipt(null);
        assertThat(orderItemBilling.getShipmentReceipt()).isNull();
    }

    @Test
    void itemIssuanceTest() {
        OrderItemBilling orderItemBilling = getOrderItemBillingRandomSampleGenerator();
        ItemIssuance itemIssuanceBack = getItemIssuanceRandomSampleGenerator();

        orderItemBilling.setItemIssuance(itemIssuanceBack);
        assertThat(orderItemBilling.getItemIssuance()).isEqualTo(itemIssuanceBack);

        orderItemBilling.itemIssuance(null);
        assertThat(orderItemBilling.getItemIssuance()).isNull();
    }
}
