package com.snp.domain;

import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ShipmentItemTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentItem.class);
        ShipmentItem shipmentItem1 = getShipmentItemSample1();
        ShipmentItem shipmentItem2 = new ShipmentItem();
        assertThat(shipmentItem1).isNotEqualTo(shipmentItem2);

        shipmentItem2.setId(shipmentItem1.getId());
        assertThat(shipmentItem1).isEqualTo(shipmentItem2);

        shipmentItem2 = getShipmentItemSample2();
        assertThat(shipmentItem1).isNotEqualTo(shipmentItem2);
    }

    @Test
    void shipmentTest() {
        ShipmentItem shipmentItem = getShipmentItemRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        shipmentItem.setShipment(shipmentBack);
        assertThat(shipmentItem.getShipment()).isEqualTo(shipmentBack);

        shipmentItem.shipment(null);
        assertThat(shipmentItem.getShipment()).isNull();
    }

    @Test
    void productTest() {
        ShipmentItem shipmentItem = getShipmentItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        shipmentItem.setProduct(productBack);
        assertThat(shipmentItem.getProduct()).isEqualTo(productBack);

        shipmentItem.product(null);
        assertThat(shipmentItem.getProduct()).isNull();
    }
}
