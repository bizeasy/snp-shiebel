package com.snp.domain;

import static com.snp.domain.ShipmentStatusTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentStatus.class);
        ShipmentStatus shipmentStatus1 = getShipmentStatusSample1();
        ShipmentStatus shipmentStatus2 = new ShipmentStatus();
        assertThat(shipmentStatus1).isNotEqualTo(shipmentStatus2);

        shipmentStatus2.setId(shipmentStatus1.getId());
        assertThat(shipmentStatus1).isEqualTo(shipmentStatus2);

        shipmentStatus2 = getShipmentStatusSample2();
        assertThat(shipmentStatus1).isNotEqualTo(shipmentStatus2);
    }

    @Test
    void shipmentTest() {
        ShipmentStatus shipmentStatus = getShipmentStatusRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        shipmentStatus.setShipment(shipmentBack);
        assertThat(shipmentStatus.getShipment()).isEqualTo(shipmentBack);

        shipmentStatus.shipment(null);
        assertThat(shipmentStatus.getShipment()).isNull();
    }

    @Test
    void statusTest() {
        ShipmentStatus shipmentStatus = getShipmentStatusRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        shipmentStatus.setStatus(statusBack);
        assertThat(shipmentStatus.getStatus()).isEqualTo(statusBack);

        shipmentStatus.status(null);
        assertThat(shipmentStatus.getStatus()).isNull();
    }
}
