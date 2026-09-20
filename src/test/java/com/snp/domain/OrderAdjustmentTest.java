package com.snp.domain;

import static com.snp.domain.OrderAdjustmentTestSamples.*;
import static com.snp.domain.OrderAdjustmentTypeTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.TaxAuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderAdjustmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderAdjustment.class);
        OrderAdjustment orderAdjustment1 = getOrderAdjustmentSample1();
        OrderAdjustment orderAdjustment2 = new OrderAdjustment();
        assertThat(orderAdjustment1).isNotEqualTo(orderAdjustment2);

        orderAdjustment2.setId(orderAdjustment1.getId());
        assertThat(orderAdjustment1).isEqualTo(orderAdjustment2);

        orderAdjustment2 = getOrderAdjustmentSample2();
        assertThat(orderAdjustment1).isNotEqualTo(orderAdjustment2);
    }

    @Test
    void orderTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderAdjustment.setOrder(orderBack);
        assertThat(orderAdjustment.getOrder()).isEqualTo(orderBack);

        orderAdjustment.order(null);
        assertThat(orderAdjustment.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderAdjustment.setOrderItem(orderItemBack);
        assertThat(orderAdjustment.getOrderItem()).isEqualTo(orderItemBack);

        orderAdjustment.orderItem(null);
        assertThat(orderAdjustment.getOrderItem()).isNull();
    }

    @Test
    void adjustmentTypeTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        OrderAdjustmentType orderAdjustmentTypeBack = getOrderAdjustmentTypeRandomSampleGenerator();

        orderAdjustment.setAdjustmentType(orderAdjustmentTypeBack);
        assertThat(orderAdjustment.getAdjustmentType()).isEqualTo(orderAdjustmentTypeBack);

        orderAdjustment.adjustmentType(null);
        assertThat(orderAdjustment.getAdjustmentType()).isNull();
    }

    @Test
    void productTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderAdjustment.setProduct(productBack);
        assertThat(orderAdjustment.getProduct()).isEqualTo(productBack);

        orderAdjustment.product(null);
        assertThat(orderAdjustment.getProduct()).isNull();
    }

    @Test
    void productFeatureTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        orderAdjustment.setProductFeature(productFeatureBack);
        assertThat(orderAdjustment.getProductFeature()).isEqualTo(productFeatureBack);

        orderAdjustment.productFeature(null);
        assertThat(orderAdjustment.getProductFeature()).isNull();
    }

    @Test
    void taxAuthorityTest() {
        OrderAdjustment orderAdjustment = getOrderAdjustmentRandomSampleGenerator();
        TaxAuthority taxAuthorityBack = getTaxAuthorityRandomSampleGenerator();

        orderAdjustment.setTaxAuthority(taxAuthorityBack);
        assertThat(orderAdjustment.getTaxAuthority()).isEqualTo(taxAuthorityBack);

        orderAdjustment.taxAuthority(null);
        assertThat(orderAdjustment.getTaxAuthority()).isNull();
    }
}
