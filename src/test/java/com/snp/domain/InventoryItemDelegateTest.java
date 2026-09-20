package com.snp.domain;

import static com.snp.domain.InventoryItemDelegateTestSamples.*;
import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.ItemIssuanceTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.ShipmentItemTestSamples.*;
import static com.snp.domain.ShipmentReceiptTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemDelegateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItemDelegate.class);
        InventoryItemDelegate inventoryItemDelegate1 = getInventoryItemDelegateSample1();
        InventoryItemDelegate inventoryItemDelegate2 = new InventoryItemDelegate();
        assertThat(inventoryItemDelegate1).isNotEqualTo(inventoryItemDelegate2);

        inventoryItemDelegate2.setId(inventoryItemDelegate1.getId());
        assertThat(inventoryItemDelegate1).isEqualTo(inventoryItemDelegate2);

        inventoryItemDelegate2 = getInventoryItemDelegateSample2();
        assertThat(inventoryItemDelegate1).isNotEqualTo(inventoryItemDelegate2);
    }

    @Test
    void inventoryItemTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        inventoryItemDelegate.setInventoryItem(inventoryItemBack);
        assertThat(inventoryItemDelegate.getInventoryItem()).isEqualTo(inventoryItemBack);

        inventoryItemDelegate.inventoryItem(null);
        assertThat(inventoryItemDelegate.getInventoryItem()).isNull();
    }

    @Test
    void orderTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        inventoryItemDelegate.setOrder(orderBack);
        assertThat(inventoryItemDelegate.getOrder()).isEqualTo(orderBack);

        inventoryItemDelegate.order(null);
        assertThat(inventoryItemDelegate.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        inventoryItemDelegate.setOrderItem(orderItemBack);
        assertThat(inventoryItemDelegate.getOrderItem()).isEqualTo(orderItemBack);

        inventoryItemDelegate.orderItem(null);
        assertThat(inventoryItemDelegate.getOrderItem()).isNull();
    }

    @Test
    void invoiceTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        inventoryItemDelegate.setInvoice(invoiceBack);
        assertThat(inventoryItemDelegate.getInvoice()).isEqualTo(invoiceBack);

        inventoryItemDelegate.invoice(null);
        assertThat(inventoryItemDelegate.getInvoice()).isNull();
    }

    @Test
    void invoiceItemTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        inventoryItemDelegate.setInvoiceItem(invoiceItemBack);
        assertThat(inventoryItemDelegate.getInvoiceItem()).isEqualTo(invoiceItemBack);

        inventoryItemDelegate.invoiceItem(null);
        assertThat(inventoryItemDelegate.getInvoiceItem()).isNull();
    }

    @Test
    void shipmentTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        inventoryItemDelegate.setShipment(shipmentBack);
        assertThat(inventoryItemDelegate.getShipment()).isEqualTo(shipmentBack);

        inventoryItemDelegate.shipment(null);
        assertThat(inventoryItemDelegate.getShipment()).isNull();
    }

    @Test
    void shipmentItemTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        ShipmentItem shipmentItemBack = getShipmentItemRandomSampleGenerator();

        inventoryItemDelegate.setShipmentItem(shipmentItemBack);
        assertThat(inventoryItemDelegate.getShipmentItem()).isEqualTo(shipmentItemBack);

        inventoryItemDelegate.shipmentItem(null);
        assertThat(inventoryItemDelegate.getShipmentItem()).isNull();
    }

    @Test
    void itemIssuanceTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        ItemIssuance itemIssuanceBack = getItemIssuanceRandomSampleGenerator();

        inventoryItemDelegate.setItemIssuance(itemIssuanceBack);
        assertThat(inventoryItemDelegate.getItemIssuance()).isEqualTo(itemIssuanceBack);

        inventoryItemDelegate.itemIssuance(null);
        assertThat(inventoryItemDelegate.getItemIssuance()).isNull();
    }

    @Test
    void receiptTest() {
        InventoryItemDelegate inventoryItemDelegate = getInventoryItemDelegateRandomSampleGenerator();
        ShipmentReceipt shipmentReceiptBack = getShipmentReceiptRandomSampleGenerator();

        inventoryItemDelegate.setReceipt(shipmentReceiptBack);
        assertThat(inventoryItemDelegate.getReceipt()).isEqualTo(shipmentReceiptBack);

        inventoryItemDelegate.receipt(null);
        assertThat(inventoryItemDelegate.getReceipt()).isNull();
    }
}
