package com.snp.domain;

import static com.snp.domain.FacilityLocationTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.InventoryTransferTestSamples.*;
import static com.snp.domain.ItemIssuanceTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryTransfer.class);
        InventoryTransfer inventoryTransfer1 = getInventoryTransferSample1();
        InventoryTransfer inventoryTransfer2 = new InventoryTransfer();
        assertThat(inventoryTransfer1).isNotEqualTo(inventoryTransfer2);

        inventoryTransfer2.setId(inventoryTransfer1.getId());
        assertThat(inventoryTransfer1).isEqualTo(inventoryTransfer2);

        inventoryTransfer2 = getInventoryTransferSample2();
        assertThat(inventoryTransfer1).isNotEqualTo(inventoryTransfer2);
    }

    @Test
    void inventoryItemTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        inventoryTransfer.setInventoryItem(inventoryItemBack);
        assertThat(inventoryTransfer.getInventoryItem()).isEqualTo(inventoryItemBack);

        inventoryTransfer.inventoryItem(null);
        assertThat(inventoryTransfer.getInventoryItem()).isNull();
    }

    @Test
    void statusTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        inventoryTransfer.setStatus(statusBack);
        assertThat(inventoryTransfer.getStatus()).isEqualTo(statusBack);

        inventoryTransfer.status(null);
        assertThat(inventoryTransfer.getStatus()).isNull();
    }

    @Test
    void facilityTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        inventoryTransfer.setFacility(facilityBack);
        assertThat(inventoryTransfer.getFacility()).isEqualTo(facilityBack);

        inventoryTransfer.facility(null);
        assertThat(inventoryTransfer.getFacility()).isNull();
    }

    @Test
    void facilityLocationTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        FacilityLocation facilityLocationBack = getFacilityLocationRandomSampleGenerator();

        inventoryTransfer.setFacilityLocation(facilityLocationBack);
        assertThat(inventoryTransfer.getFacilityLocation()).isEqualTo(facilityLocationBack);

        inventoryTransfer.facilityLocation(null);
        assertThat(inventoryTransfer.getFacilityLocation()).isNull();
    }

    @Test
    void facilityToTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        inventoryTransfer.setFacilityTo(facilityBack);
        assertThat(inventoryTransfer.getFacilityTo()).isEqualTo(facilityBack);

        inventoryTransfer.facilityTo(null);
        assertThat(inventoryTransfer.getFacilityTo()).isNull();
    }

    @Test
    void facilityLocationToTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        FacilityLocation facilityLocationBack = getFacilityLocationRandomSampleGenerator();

        inventoryTransfer.setFacilityLocationTo(facilityLocationBack);
        assertThat(inventoryTransfer.getFacilityLocationTo()).isEqualTo(facilityLocationBack);

        inventoryTransfer.facilityLocationTo(null);
        assertThat(inventoryTransfer.getFacilityLocationTo()).isNull();
    }

    @Test
    void issuanceTest() {
        InventoryTransfer inventoryTransfer = getInventoryTransferRandomSampleGenerator();
        ItemIssuance itemIssuanceBack = getItemIssuanceRandomSampleGenerator();

        inventoryTransfer.setIssuance(itemIssuanceBack);
        assertThat(inventoryTransfer.getIssuance()).isEqualTo(itemIssuanceBack);

        inventoryTransfer.issuance(null);
        assertThat(inventoryTransfer.getIssuance()).isNull();
    }
}
