package com.snp.domain;

import static com.snp.domain.OrderStatusTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStatus.class);
        OrderStatus orderStatus1 = getOrderStatusSample1();
        OrderStatus orderStatus2 = new OrderStatus();
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);

        orderStatus2.setId(orderStatus1.getId());
        assertThat(orderStatus1).isEqualTo(orderStatus2);

        orderStatus2 = getOrderStatusSample2();
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);
    }

    @Test
    void orderTest() {
        OrderStatus orderStatus = getOrderStatusRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderStatus.setOrder(orderBack);
        assertThat(orderStatus.getOrder()).isEqualTo(orderBack);

        orderStatus.order(null);
        assertThat(orderStatus.getOrder()).isNull();
    }

    @Test
    void statusTest() {
        OrderStatus orderStatus = getOrderStatusRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        orderStatus.setStatus(statusBack);
        assertThat(orderStatus.getStatus()).isEqualTo(statusBack);

        orderStatus.status(null);
        assertThat(orderStatus.getStatus()).isNull();
    }
}
