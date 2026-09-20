package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.OrderAttachmentTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderAttachment.class);
        OrderAttachment orderAttachment1 = getOrderAttachmentSample1();
        OrderAttachment orderAttachment2 = new OrderAttachment();
        assertThat(orderAttachment1).isNotEqualTo(orderAttachment2);

        orderAttachment2.setId(orderAttachment1.getId());
        assertThat(orderAttachment1).isEqualTo(orderAttachment2);

        orderAttachment2 = getOrderAttachmentSample2();
        assertThat(orderAttachment1).isNotEqualTo(orderAttachment2);
    }

    @Test
    void orderTest() {
        OrderAttachment orderAttachment = getOrderAttachmentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderAttachment.setOrder(orderBack);
        assertThat(orderAttachment.getOrder()).isEqualTo(orderBack);

        orderAttachment.order(null);
        assertThat(orderAttachment.getOrder()).isNull();
    }

    @Test
    void attachmentTest() {
        OrderAttachment orderAttachment = getOrderAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        orderAttachment.setAttachment(attachmentBack);
        assertThat(orderAttachment.getAttachment()).isEqualTo(attachmentBack);

        orderAttachment.attachment(null);
        assertThat(orderAttachment.getAttachment()).isNull();
    }
}
