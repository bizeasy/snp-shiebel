package com.snp.domain;

import static com.snp.domain.OrderItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemType.class);
        OrderItemType orderItemType1 = getOrderItemTypeSample1();
        OrderItemType orderItemType2 = new OrderItemType();
        assertThat(orderItemType1).isNotEqualTo(orderItemType2);

        orderItemType2.setId(orderItemType1.getId());
        assertThat(orderItemType1).isEqualTo(orderItemType2);

        orderItemType2 = getOrderItemTypeSample2();
        assertThat(orderItemType1).isNotEqualTo(orderItemType2);
    }
}
