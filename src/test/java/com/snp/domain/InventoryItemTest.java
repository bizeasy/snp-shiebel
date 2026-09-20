package com.snp.domain;

import static com.snp.domain.FacilityLocationTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.InventoryItemTypeTestSamples.*;
import static com.snp.domain.LotTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItem.class);
        InventoryItem inventoryItem1 = getInventoryItemSample1();
        InventoryItem inventoryItem2 = new InventoryItem();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);

        inventoryItem2.setId(inventoryItem1.getId());
        assertThat(inventoryItem1).isEqualTo(inventoryItem2);

        inventoryItem2 = getInventoryItemSample2();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);
    }

    @Test
    void inventoryItemTypeTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        InventoryItemType inventoryItemTypeBack = getInventoryItemTypeRandomSampleGenerator();

        inventoryItem.setInventoryItemType(inventoryItemTypeBack);
        assertThat(inventoryItem.getInventoryItemType()).isEqualTo(inventoryItemTypeBack);

        inventoryItem.inventoryItemType(null);
        assertThat(inventoryItem.getInventoryItemType()).isNull();
    }

    @Test
    void productTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        inventoryItem.setProduct(productBack);
        assertThat(inventoryItem.getProduct()).isEqualTo(productBack);

        inventoryItem.product(null);
        assertThat(inventoryItem.getProduct()).isNull();
    }

    @Test
    void supplierTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        inventoryItem.setSupplier(partyBack);
        assertThat(inventoryItem.getSupplier()).isEqualTo(partyBack);

        inventoryItem.supplier(null);
        assertThat(inventoryItem.getSupplier()).isNull();
    }

    @Test
    void ownerPartyTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        inventoryItem.setOwnerParty(partyBack);
        assertThat(inventoryItem.getOwnerParty()).isEqualTo(partyBack);

        inventoryItem.ownerParty(null);
        assertThat(inventoryItem.getOwnerParty()).isNull();
    }

    @Test
    void statusTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        inventoryItem.setStatus(statusBack);
        assertThat(inventoryItem.getStatus()).isEqualTo(statusBack);

        inventoryItem.status(null);
        assertThat(inventoryItem.getStatus()).isNull();
    }

    @Test
    void facilityTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        inventoryItem.setFacility(facilityBack);
        assertThat(inventoryItem.getFacility()).isEqualTo(facilityBack);

        inventoryItem.facility(null);
        assertThat(inventoryItem.getFacility()).isNull();
    }

    @Test
    void facilityLocationTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        FacilityLocation facilityLocationBack = getFacilityLocationRandomSampleGenerator();

        inventoryItem.setFacilityLocation(facilityLocationBack);
        assertThat(inventoryItem.getFacilityLocation()).isEqualTo(facilityLocationBack);

        inventoryItem.facilityLocation(null);
        assertThat(inventoryItem.getFacilityLocation()).isNull();
    }

    @Test
    void uomTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        inventoryItem.setUom(uomBack);
        assertThat(inventoryItem.getUom()).isEqualTo(uomBack);

        inventoryItem.uom(null);
        assertThat(inventoryItem.getUom()).isNull();
    }

    @Test
    void currencyUomTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        inventoryItem.setCurrencyUom(uomBack);
        assertThat(inventoryItem.getCurrencyUom()).isEqualTo(uomBack);

        inventoryItem.currencyUom(null);
        assertThat(inventoryItem.getCurrencyUom()).isNull();
    }

    @Test
    void lotTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Lot lotBack = getLotRandomSampleGenerator();

        inventoryItem.setLot(lotBack);
        assertThat(inventoryItem.getLot()).isEqualTo(lotBack);

        inventoryItem.lot(null);
        assertThat(inventoryItem.getLot()).isNull();
    }
}
