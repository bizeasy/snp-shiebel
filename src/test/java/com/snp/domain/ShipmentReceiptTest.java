package com.snp.domain;

import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ShipmentItemTestSamples.*;
import static com.snp.domain.ShipmentReceiptTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentReceipt.class);
        ShipmentReceipt shipmentReceipt1 = getShipmentReceiptSample1();
        ShipmentReceipt shipmentReceipt2 = new ShipmentReceipt();
        assertThat(shipmentReceipt1).isNotEqualTo(shipmentReceipt2);

        shipmentReceipt2.setId(shipmentReceipt1.getId());
        assertThat(shipmentReceipt1).isEqualTo(shipmentReceipt2);

        shipmentReceipt2 = getShipmentReceiptSample2();
        assertThat(shipmentReceipt1).isNotEqualTo(shipmentReceipt2);
    }

    @Test
    void shipmentTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        shipmentReceipt.setShipment(shipmentBack);
        assertThat(shipmentReceipt.getShipment()).isEqualTo(shipmentBack);

        shipmentReceipt.shipment(null);
        assertThat(shipmentReceipt.getShipment()).isNull();
    }

    @Test
    void shipmentItemTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        ShipmentItem shipmentItemBack = getShipmentItemRandomSampleGenerator();

        shipmentReceipt.setShipmentItem(shipmentItemBack);
        assertThat(shipmentReceipt.getShipmentItem()).isEqualTo(shipmentItemBack);

        shipmentReceipt.shipmentItem(null);
        assertThat(shipmentReceipt.getShipmentItem()).isNull();
    }

    @Test
    void orderTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shipmentReceipt.setOrder(orderBack);
        assertThat(shipmentReceipt.getOrder()).isEqualTo(orderBack);

        shipmentReceipt.order(null);
        assertThat(shipmentReceipt.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        shipmentReceipt.setOrderItem(orderItemBack);
        assertThat(shipmentReceipt.getOrderItem()).isEqualTo(orderItemBack);

        shipmentReceipt.orderItem(null);
        assertThat(shipmentReceipt.getOrderItem()).isNull();
    }

    @Test
    void inventoryItemTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        shipmentReceipt.setInventoryItem(inventoryItemBack);
        assertThat(shipmentReceipt.getInventoryItem()).isEqualTo(inventoryItemBack);

        shipmentReceipt.inventoryItem(null);
        assertThat(shipmentReceipt.getInventoryItem()).isNull();
    }

    @Test
    void productTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        shipmentReceipt.setProduct(productBack);
        assertThat(shipmentReceipt.getProduct()).isEqualTo(productBack);

        shipmentReceipt.product(null);
        assertThat(shipmentReceipt.getProduct()).isNull();
    }

    @Test
    void receivedByTest() {
        ShipmentReceipt shipmentReceipt = getShipmentReceiptRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        shipmentReceipt.setReceivedBy(partyBack);
        assertThat(shipmentReceipt.getReceivedBy()).isEqualTo(partyBack);

        shipmentReceipt.receivedBy(null);
        assertThat(shipmentReceipt.getReceivedBy()).isNull();
    }
}
