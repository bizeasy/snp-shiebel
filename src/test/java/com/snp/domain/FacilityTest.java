package com.snp.domain;

import static com.snp.domain.FacilityGroupTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.FacilityTypeTestSamples.*;
import static com.snp.domain.GeoPointTestSamples.*;
import static com.snp.domain.InventoryItemTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductStoreTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facility.class);
        Facility facility1 = getFacilitySample1();
        Facility facility2 = new Facility();
        assertThat(facility1).isNotEqualTo(facility2);

        facility2.setId(facility1.getId());
        assertThat(facility1).isEqualTo(facility2);

        facility2 = getFacilitySample2();
        assertThat(facility1).isNotEqualTo(facility2);
    }

    @Test
    void facilityTypeTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        FacilityType facilityTypeBack = getFacilityTypeRandomSampleGenerator();

        facility.setFacilityType(facilityTypeBack);
        assertThat(facility.getFacilityType()).isEqualTo(facilityTypeBack);

        facility.facilityType(null);
        assertThat(facility.getFacilityType()).isNull();
    }

    @Test
    void parentFacilityTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facility.setParentFacility(facilityBack);
        assertThat(facility.getParentFacility()).isEqualTo(facilityBack);

        facility.parentFacility(null);
        assertThat(facility.getParentFacility()).isNull();
    }

    @Test
    void ownerPartyTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        facility.setOwnerParty(partyBack);
        assertThat(facility.getOwnerParty()).isEqualTo(partyBack);

        facility.ownerParty(null);
        assertThat(facility.getOwnerParty()).isNull();
    }

    @Test
    void facilityGroupTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        FacilityGroup facilityGroupBack = getFacilityGroupRandomSampleGenerator();

        facility.setFacilityGroup(facilityGroupBack);
        assertThat(facility.getFacilityGroup()).isEqualTo(facilityGroupBack);

        facility.facilityGroup(null);
        assertThat(facility.getFacilityGroup()).isNull();
    }

    @Test
    void sizeUomTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        facility.setSizeUom(uomBack);
        assertThat(facility.getSizeUom()).isEqualTo(uomBack);

        facility.sizeUom(null);
        assertThat(facility.getSizeUom()).isNull();
    }

    @Test
    void geoPointTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        GeoPoint geoPointBack = getGeoPointRandomSampleGenerator();

        facility.setGeoPoint(geoPointBack);
        assertThat(facility.getGeoPoint()).isEqualTo(geoPointBack);

        facility.geoPoint(null);
        assertThat(facility.getGeoPoint()).isNull();
    }

    @Test
    void inventoryItemTypeTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        InventoryItemType inventoryItemTypeBack = getInventoryItemTypeRandomSampleGenerator();

        facility.setInventoryItemType(inventoryItemTypeBack);
        assertThat(facility.getInventoryItemType()).isEqualTo(inventoryItemTypeBack);

        facility.inventoryItemType(null);
        assertThat(facility.getInventoryItemType()).isNull();
    }

    @Test
    void productStoreTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        ProductStore productStoreBack = getProductStoreRandomSampleGenerator();

        facility.setProductStore(productStoreBack);
        assertThat(facility.getProductStore()).isEqualTo(productStoreBack);

        facility.productStore(null);
        assertThat(facility.getProductStore()).isNull();
    }
}
