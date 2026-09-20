package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.ShipmentAttachmentTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentAttachment.class);
        ShipmentAttachment shipmentAttachment1 = getShipmentAttachmentSample1();
        ShipmentAttachment shipmentAttachment2 = new ShipmentAttachment();
        assertThat(shipmentAttachment1).isNotEqualTo(shipmentAttachment2);

        shipmentAttachment2.setId(shipmentAttachment1.getId());
        assertThat(shipmentAttachment1).isEqualTo(shipmentAttachment2);

        shipmentAttachment2 = getShipmentAttachmentSample2();
        assertThat(shipmentAttachment1).isNotEqualTo(shipmentAttachment2);
    }

    @Test
    void shipmentTest() {
        ShipmentAttachment shipmentAttachment = getShipmentAttachmentRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        shipmentAttachment.setShipment(shipmentBack);
        assertThat(shipmentAttachment.getShipment()).isEqualTo(shipmentBack);

        shipmentAttachment.shipment(null);
        assertThat(shipmentAttachment.getShipment()).isNull();
    }

    @Test
    void attachmentTest() {
        ShipmentAttachment shipmentAttachment = getShipmentAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        shipmentAttachment.setAttachment(attachmentBack);
        assertThat(shipmentAttachment.getAttachment()).isEqualTo(attachmentBack);

        shipmentAttachment.attachment(null);
        assertThat(shipmentAttachment.getAttachment()).isNull();
    }
}
