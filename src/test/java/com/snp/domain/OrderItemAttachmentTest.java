package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.OrderItemAttachmentTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemAttachment.class);
        OrderItemAttachment orderItemAttachment1 = getOrderItemAttachmentSample1();
        OrderItemAttachment orderItemAttachment2 = new OrderItemAttachment();
        assertThat(orderItemAttachment1).isNotEqualTo(orderItemAttachment2);

        orderItemAttachment2.setId(orderItemAttachment1.getId());
        assertThat(orderItemAttachment1).isEqualTo(orderItemAttachment2);

        orderItemAttachment2 = getOrderItemAttachmentSample2();
        assertThat(orderItemAttachment1).isNotEqualTo(orderItemAttachment2);
    }

    @Test
    void orderItemTest() {
        OrderItemAttachment orderItemAttachment = getOrderItemAttachmentRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderItemAttachment.setOrderItem(orderItemBack);
        assertThat(orderItemAttachment.getOrderItem()).isEqualTo(orderItemBack);

        orderItemAttachment.orderItem(null);
        assertThat(orderItemAttachment.getOrderItem()).isNull();
    }

    @Test
    void attachmentTest() {
        OrderItemAttachment orderItemAttachment = getOrderItemAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        orderItemAttachment.setAttachment(attachmentBack);
        assertThat(orderItemAttachment.getAttachment()).isEqualTo(attachmentBack);

        orderItemAttachment.attachment(null);
        assertThat(orderItemAttachment.getAttachment()).isNull();
    }
}
