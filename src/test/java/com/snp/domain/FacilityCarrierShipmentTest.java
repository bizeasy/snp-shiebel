package com.snp.domain;

import static com.snp.domain.FacilityCarrierShipmentTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static com.snp.domain.ShipmentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityCarrierShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityCarrierShipment.class);
        FacilityCarrierShipment facilityCarrierShipment1 = getFacilityCarrierShipmentSample1();
        FacilityCarrierShipment facilityCarrierShipment2 = new FacilityCarrierShipment();
        assertThat(facilityCarrierShipment1).isNotEqualTo(facilityCarrierShipment2);

        facilityCarrierShipment2.setId(facilityCarrierShipment1.getId());
        assertThat(facilityCarrierShipment1).isEqualTo(facilityCarrierShipment2);

        facilityCarrierShipment2 = getFacilityCarrierShipmentSample2();
        assertThat(facilityCarrierShipment1).isNotEqualTo(facilityCarrierShipment2);
    }

    @Test
    void facilityTest() {
        FacilityCarrierShipment facilityCarrierShipment = getFacilityCarrierShipmentRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facilityCarrierShipment.setFacility(facilityBack);
        assertThat(facilityCarrierShipment.getFacility()).isEqualTo(facilityBack);

        facilityCarrierShipment.facility(null);
        assertThat(facilityCarrierShipment.getFacility()).isNull();
    }

    @Test
    void partyTest() {
        FacilityCarrierShipment facilityCarrierShipment = getFacilityCarrierShipmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        facilityCarrierShipment.setParty(partyBack);
        assertThat(facilityCarrierShipment.getParty()).isEqualTo(partyBack);

        facilityCarrierShipment.party(null);
        assertThat(facilityCarrierShipment.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        FacilityCarrierShipment facilityCarrierShipment = getFacilityCarrierShipmentRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        facilityCarrierShipment.setRoleType(roleTypeBack);
        assertThat(facilityCarrierShipment.getRoleType()).isEqualTo(roleTypeBack);

        facilityCarrierShipment.roleType(null);
        assertThat(facilityCarrierShipment.getRoleType()).isNull();
    }

    @Test
    void shipmentMethodTypeTest() {
        FacilityCarrierShipment facilityCarrierShipment = getFacilityCarrierShipmentRandomSampleGenerator();
        ShipmentMethodType shipmentMethodTypeBack = getShipmentMethodTypeRandomSampleGenerator();

        facilityCarrierShipment.setShipmentMethodType(shipmentMethodTypeBack);
        assertThat(facilityCarrierShipment.getShipmentMethodType()).isEqualTo(shipmentMethodTypeBack);

        facilityCarrierShipment.shipmentMethodType(null);
        assertThat(facilityCarrierShipment.getShipmentMethodType()).isNull();
    }
}
