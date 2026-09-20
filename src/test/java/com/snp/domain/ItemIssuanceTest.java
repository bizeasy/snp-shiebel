package com.snp.domain;

import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.ItemIssuanceTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ShipmentItemTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemIssuanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemIssuance.class);
        ItemIssuance itemIssuance1 = getItemIssuanceSample1();
        ItemIssuance itemIssuance2 = new ItemIssuance();
        assertThat(itemIssuance1).isNotEqualTo(itemIssuance2);

        itemIssuance2.setId(itemIssuance1.getId());
        assertThat(itemIssuance1).isEqualTo(itemIssuance2);

        itemIssuance2 = getItemIssuanceSample2();
        assertThat(itemIssuance1).isNotEqualTo(itemIssuance2);
    }

    @Test
    void inventoryItemTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        itemIssuance.setInventoryItem(inventoryItemBack);
        assertThat(itemIssuance.getInventoryItem()).isEqualTo(inventoryItemBack);

        itemIssuance.inventoryItem(null);
        assertThat(itemIssuance.getInventoryItem()).isNull();
    }

    @Test
    void orderTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        itemIssuance.setOrder(orderBack);
        assertThat(itemIssuance.getOrder()).isEqualTo(orderBack);

        itemIssuance.order(null);
        assertThat(itemIssuance.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        itemIssuance.setOrderItem(orderItemBack);
        assertThat(itemIssuance.getOrderItem()).isEqualTo(orderItemBack);

        itemIssuance.orderItem(null);
        assertThat(itemIssuance.getOrderItem()).isNull();
    }

    @Test
    void shipmentTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        itemIssuance.setShipment(shipmentBack);
        assertThat(itemIssuance.getShipment()).isEqualTo(shipmentBack);

        itemIssuance.shipment(null);
        assertThat(itemIssuance.getShipment()).isNull();
    }

    @Test
    void shipmentItemTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        ShipmentItem shipmentItemBack = getShipmentItemRandomSampleGenerator();

        itemIssuance.setShipmentItem(shipmentItemBack);
        assertThat(itemIssuance.getShipmentItem()).isEqualTo(shipmentItemBack);

        itemIssuance.shipmentItem(null);
        assertThat(itemIssuance.getShipmentItem()).isNull();
    }

    @Test
    void issuedByPartyTest() {
        ItemIssuance itemIssuance = getItemIssuanceRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        itemIssuance.setIssuedByParty(partyBack);
        assertThat(itemIssuance.getIssuedByParty()).isEqualTo(partyBack);

        itemIssuance.issuedByParty(null);
        assertThat(itemIssuance.getIssuedByParty()).isNull();
    }
}
