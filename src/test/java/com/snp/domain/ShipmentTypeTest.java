package com.snp.domain;

import static com.snp.domain.ShipmentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentType.class);
        ShipmentType shipmentType1 = getShipmentTypeSample1();
        ShipmentType shipmentType2 = new ShipmentType();
        assertThat(shipmentType1).isNotEqualTo(shipmentType2);

        shipmentType2.setId(shipmentType1.getId());
        assertThat(shipmentType1).isEqualTo(shipmentType2);

        shipmentType2 = getShipmentTypeSample2();
        assertThat(shipmentType1).isNotEqualTo(shipmentType2);
    }
}
