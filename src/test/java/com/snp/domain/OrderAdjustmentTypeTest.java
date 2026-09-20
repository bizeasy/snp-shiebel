package com.snp.domain;

import static com.snp.domain.OrderAdjustmentTypeTestSamples.*;
import static com.snp.domain.OrderAdjustmentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderAdjustmentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderAdjustmentType.class);
        OrderAdjustmentType orderAdjustmentType1 = getOrderAdjustmentTypeSample1();
        OrderAdjustmentType orderAdjustmentType2 = new OrderAdjustmentType();
        assertThat(orderAdjustmentType1).isNotEqualTo(orderAdjustmentType2);

        orderAdjustmentType2.setId(orderAdjustmentType1.getId());
        assertThat(orderAdjustmentType1).isEqualTo(orderAdjustmentType2);

        orderAdjustmentType2 = getOrderAdjustmentTypeSample2();
        assertThat(orderAdjustmentType1).isNotEqualTo(orderAdjustmentType2);
    }

    @Test
    void parentTest() {
        OrderAdjustmentType orderAdjustmentType = getOrderAdjustmentTypeRandomSampleGenerator();
        OrderAdjustmentType orderAdjustmentTypeBack = getOrderAdjustmentTypeRandomSampleGenerator();

        orderAdjustmentType.setParent(orderAdjustmentTypeBack);
        assertThat(orderAdjustmentType.getParent()).isEqualTo(orderAdjustmentTypeBack);

        orderAdjustmentType.parent(null);
        assertThat(orderAdjustmentType.getParent()).isNull();
    }
}
