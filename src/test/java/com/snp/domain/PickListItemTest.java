package com.snp.domain;

import static com.snp.domain.FacilityLocationTestSamples.*;
import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PickListItemTestSamples.*;
import static com.snp.domain.PickListTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PickListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PickListItem.class);
        PickListItem pickListItem1 = getPickListItemSample1();
        PickListItem pickListItem2 = new PickListItem();
        assertThat(pickListItem1).isNotEqualTo(pickListItem2);

        pickListItem2.setId(pickListItem1.getId());
        assertThat(pickListItem1).isEqualTo(pickListItem2);

        pickListItem2 = getPickListItemSample2();
        assertThat(pickListItem1).isNotEqualTo(pickListItem2);
    }

    @Test
    void pickListTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        PickList pickListBack = getPickListRandomSampleGenerator();

        pickListItem.setPickList(pickListBack);
        assertThat(pickListItem.getPickList()).isEqualTo(pickListBack);

        pickListItem.pickList(null);
        assertThat(pickListItem.getPickList()).isNull();
    }

    @Test
    void orderTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        pickListItem.setOrder(orderBack);
        assertThat(pickListItem.getOrder()).isEqualTo(orderBack);

        pickListItem.order(null);
        assertThat(pickListItem.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        pickListItem.setOrderItem(orderItemBack);
        assertThat(pickListItem.getOrderItem()).isEqualTo(orderItemBack);

        pickListItem.orderItem(null);
        assertThat(pickListItem.getOrderItem()).isNull();
    }

    @Test
    void inventoryItemTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        pickListItem.setInventoryItem(inventoryItemBack);
        assertThat(pickListItem.getInventoryItem()).isEqualTo(inventoryItemBack);

        pickListItem.inventoryItem(null);
        assertThat(pickListItem.getInventoryItem()).isNull();
    }

    @Test
    void productTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        pickListItem.setProduct(productBack);
        assertThat(pickListItem.getProduct()).isEqualTo(productBack);

        pickListItem.product(null);
        assertThat(pickListItem.getProduct()).isNull();
    }

    @Test
    void facilityLocationTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        FacilityLocation facilityLocationBack = getFacilityLocationRandomSampleGenerator();

        pickListItem.setFacilityLocation(facilityLocationBack);
        assertThat(pickListItem.getFacilityLocation()).isEqualTo(facilityLocationBack);

        pickListItem.facilityLocation(null);
        assertThat(pickListItem.getFacilityLocation()).isNull();
    }

    @Test
    void statusTest() {
        PickListItem pickListItem = getPickListItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        pickListItem.setStatus(statusBack);
        assertThat(pickListItem.getStatus()).isEqualTo(statusBack);

        pickListItem.status(null);
        assertThat(pickListItem.getStatus()).isNull();
    }
}
