package com.snp.domain;

import static com.snp.domain.CarrierShipmentMethodTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static com.snp.domain.ShipmentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarrierShipmentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarrierShipmentMethod.class);
        CarrierShipmentMethod carrierShipmentMethod1 = getCarrierShipmentMethodSample1();
        CarrierShipmentMethod carrierShipmentMethod2 = new CarrierShipmentMethod();
        assertThat(carrierShipmentMethod1).isNotEqualTo(carrierShipmentMethod2);

        carrierShipmentMethod2.setId(carrierShipmentMethod1.getId());
        assertThat(carrierShipmentMethod1).isEqualTo(carrierShipmentMethod2);

        carrierShipmentMethod2 = getCarrierShipmentMethodSample2();
        assertThat(carrierShipmentMethod1).isNotEqualTo(carrierShipmentMethod2);
    }

    @Test
    void shipmentMethodTypeTest() {
        CarrierShipmentMethod carrierShipmentMethod = getCarrierShipmentMethodRandomSampleGenerator();
        ShipmentMethodType shipmentMethodTypeBack = getShipmentMethodTypeRandomSampleGenerator();

        carrierShipmentMethod.setShipmentMethodType(shipmentMethodTypeBack);
        assertThat(carrierShipmentMethod.getShipmentMethodType()).isEqualTo(shipmentMethodTypeBack);

        carrierShipmentMethod.shipmentMethodType(null);
        assertThat(carrierShipmentMethod.getShipmentMethodType()).isNull();
    }

    @Test
    void partyTest() {
        CarrierShipmentMethod carrierShipmentMethod = getCarrierShipmentMethodRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        carrierShipmentMethod.setParty(partyBack);
        assertThat(carrierShipmentMethod.getParty()).isEqualTo(partyBack);

        carrierShipmentMethod.party(null);
        assertThat(carrierShipmentMethod.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        CarrierShipmentMethod carrierShipmentMethod = getCarrierShipmentMethodRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        carrierShipmentMethod.setRoleType(roleTypeBack);
        assertThat(carrierShipmentMethod.getRoleType()).isEqualTo(roleTypeBack);

        carrierShipmentMethod.roleType(null);
        assertThat(carrierShipmentMethod.getRoleType()).isNull();
    }
}
