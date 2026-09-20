package com.snp.domain;

import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderShipmentTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.ShipmentItemTestSamples.*;
import static com.snp.domain.ShipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderShipment.class);
        OrderShipment orderShipment1 = getOrderShipmentSample1();
        OrderShipment orderShipment2 = new OrderShipment();
        assertThat(orderShipment1).isNotEqualTo(orderShipment2);

        orderShipment2.setId(orderShipment1.getId());
        assertThat(orderShipment1).isEqualTo(orderShipment2);

        orderShipment2 = getOrderShipmentSample2();
        assertThat(orderShipment1).isNotEqualTo(orderShipment2);
    }

    @Test
    void orderTest() {
        OrderShipment orderShipment = getOrderShipmentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderShipment.setOrder(orderBack);
        assertThat(orderShipment.getOrder()).isEqualTo(orderBack);

        orderShipment.order(null);
        assertThat(orderShipment.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        OrderShipment orderShipment = getOrderShipmentRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderShipment.setOrderItem(orderItemBack);
        assertThat(orderShipment.getOrderItem()).isEqualTo(orderItemBack);

        orderShipment.orderItem(null);
        assertThat(orderShipment.getOrderItem()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderShipment orderShipment = getOrderShipmentRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        orderShipment.setShipment(shipmentBack);
        assertThat(orderShipment.getShipment()).isEqualTo(shipmentBack);

        orderShipment.shipment(null);
        assertThat(orderShipment.getShipment()).isNull();
    }

    @Test
    void shipmentItemTest() {
        OrderShipment orderShipment = getOrderShipmentRandomSampleGenerator();
        ShipmentItem shipmentItemBack = getShipmentItemRandomSampleGenerator();

        orderShipment.setShipmentItem(shipmentItemBack);
        assertThat(orderShipment.getShipmentItem()).isEqualTo(shipmentItemBack);

        orderShipment.shipmentItem(null);
        assertThat(orderShipment.getShipmentItem()).isNull();
    }
}
