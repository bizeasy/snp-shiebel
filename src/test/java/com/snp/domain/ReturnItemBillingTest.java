package com.snp.domain;

import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.ReturnItemBillingTestSamples.*;
import static com.snp.domain.ReturnItemTestSamples.*;
import static com.snp.domain.ShipmentReceiptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnItemBillingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnItemBilling.class);
        ReturnItemBilling returnItemBilling1 = getReturnItemBillingSample1();
        ReturnItemBilling returnItemBilling2 = new ReturnItemBilling();
        assertThat(returnItemBilling1).isNotEqualTo(returnItemBilling2);

        returnItemBilling2.setId(returnItemBilling1.getId());
        assertThat(returnItemBilling1).isEqualTo(returnItemBilling2);

        returnItemBilling2 = getReturnItemBillingSample2();
        assertThat(returnItemBilling1).isNotEqualTo(returnItemBilling2);
    }

    @Test
    void returnItemTest() {
        ReturnItemBilling returnItemBilling = getReturnItemBillingRandomSampleGenerator();
        ReturnItem returnItemBack = getReturnItemRandomSampleGenerator();

        returnItemBilling.setReturnItem(returnItemBack);
        assertThat(returnItemBilling.getReturnItem()).isEqualTo(returnItemBack);

        returnItemBilling.returnItem(null);
        assertThat(returnItemBilling.getReturnItem()).isNull();
    }

    @Test
    void orderItemTest() {
        ReturnItemBilling returnItemBilling = getReturnItemBillingRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        returnItemBilling.setOrderItem(orderItemBack);
        assertThat(returnItemBilling.getOrderItem()).isEqualTo(orderItemBack);

        returnItemBilling.orderItem(null);
        assertThat(returnItemBilling.getOrderItem()).isNull();
    }

    @Test
    void invoiceItemTest() {
        ReturnItemBilling returnItemBilling = getReturnItemBillingRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        returnItemBilling.setInvoiceItem(invoiceItemBack);
        assertThat(returnItemBilling.getInvoiceItem()).isEqualTo(invoiceItemBack);

        returnItemBilling.invoiceItem(null);
        assertThat(returnItemBilling.getInvoiceItem()).isNull();
    }

    @Test
    void shipmentReceiptTest() {
        ReturnItemBilling returnItemBilling = getReturnItemBillingRandomSampleGenerator();
        ShipmentReceipt shipmentReceiptBack = getShipmentReceiptRandomSampleGenerator();

        returnItemBilling.setShipmentReceipt(shipmentReceiptBack);
        assertThat(returnItemBilling.getShipmentReceipt()).isEqualTo(shipmentReceiptBack);

        returnItemBilling.shipmentReceipt(null);
        assertThat(returnItemBilling.getShipmentReceipt()).isNull();
    }
}
