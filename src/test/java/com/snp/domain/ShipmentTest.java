package com.snp.domain;

import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ShipmentMethodTypeTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static com.snp.domain.ShipmentTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipment.class);
        Shipment shipment1 = getShipmentSample1();
        Shipment shipment2 = new Shipment();
        assertThat(shipment1).isNotEqualTo(shipment2);

        shipment2.setId(shipment1.getId());
        assertThat(shipment1).isEqualTo(shipment2);

        shipment2 = getShipmentSample2();
        assertThat(shipment1).isNotEqualTo(shipment2);
    }

    @Test
    void shipmentTypeTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        ShipmentType shipmentTypeBack = getShipmentTypeRandomSampleGenerator();

        shipment.setShipmentType(shipmentTypeBack);
        assertThat(shipment.getShipmentType()).isEqualTo(shipmentTypeBack);

        shipment.shipmentType(null);
        assertThat(shipment.getShipmentType()).isNull();
    }

    @Test
    void shipmentMethodTypeTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        ShipmentMethodType shipmentMethodTypeBack = getShipmentMethodTypeRandomSampleGenerator();

        shipment.setShipmentMethodType(shipmentMethodTypeBack);
        assertThat(shipment.getShipmentMethodType()).isEqualTo(shipmentMethodTypeBack);

        shipment.shipmentMethodType(null);
        assertThat(shipment.getShipmentMethodType()).isNull();
    }

    @Test
    void statusTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        shipment.setStatus(statusBack);
        assertThat(shipment.getStatus()).isEqualTo(statusBack);

        shipment.status(null);
        assertThat(shipment.getStatus()).isNull();
    }

    @Test
    void orderTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shipment.setOrder(orderBack);
        assertThat(shipment.getOrder()).isEqualTo(orderBack);

        shipment.order(null);
        assertThat(shipment.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        shipment.setOrderItem(orderItemBack);
        assertThat(shipment.getOrderItem()).isEqualTo(orderItemBack);

        shipment.orderItem(null);
        assertThat(shipment.getOrderItem()).isNull();
    }

    @Test
    void originFacilityTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        shipment.setOriginFacility(facilityBack);
        assertThat(shipment.getOriginFacility()).isEqualTo(facilityBack);

        shipment.originFacility(null);
        assertThat(shipment.getOriginFacility()).isNull();
    }

    @Test
    void destinationFacilityTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        shipment.setDestinationFacility(facilityBack);
        assertThat(shipment.getDestinationFacility()).isEqualTo(facilityBack);

        shipment.destinationFacility(null);
        assertThat(shipment.getDestinationFacility()).isNull();
    }

    @Test
    void fromPartyTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        shipment.setFromParty(partyBack);
        assertThat(shipment.getFromParty()).isEqualTo(partyBack);

        shipment.fromParty(null);
        assertThat(shipment.getFromParty()).isNull();
    }

    @Test
    void toPartyTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        shipment.setToParty(partyBack);
        assertThat(shipment.getToParty()).isEqualTo(partyBack);

        shipment.toParty(null);
        assertThat(shipment.getToParty()).isNull();
    }

    @Test
    void carrierPartyTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        shipment.setCarrierParty(partyBack);
        assertThat(shipment.getCarrierParty()).isEqualTo(partyBack);

        shipment.carrierParty(null);
        assertThat(shipment.getCarrierParty()).isNull();
    }

    @Test
    void contactMechTest() {
        Shipment shipment = getShipmentRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        shipment.setContactMech(contactMechBack);
        assertThat(shipment.getContactMech()).isEqualTo(contactMechBack);

        shipment.contactMech(null);
        assertThat(shipment.getContactMech()).isNull();
    }
}
