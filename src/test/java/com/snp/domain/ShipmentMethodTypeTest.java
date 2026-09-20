package com.snp.domain;

import static com.snp.domain.ShipmentMethodTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentMethodTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentMethodType.class);
        ShipmentMethodType shipmentMethodType1 = getShipmentMethodTypeSample1();
        ShipmentMethodType shipmentMethodType2 = new ShipmentMethodType();
        assertThat(shipmentMethodType1).isNotEqualTo(shipmentMethodType2);

        shipmentMethodType2.setId(shipmentMethodType1.getId());
        assertThat(shipmentMethodType1).isEqualTo(shipmentMethodType2);

        shipmentMethodType2 = getShipmentMethodTypeSample2();
        assertThat(shipmentMethodType1).isNotEqualTo(shipmentMethodType2);
    }
}
