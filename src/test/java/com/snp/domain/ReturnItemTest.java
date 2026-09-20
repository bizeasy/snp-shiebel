package com.snp.domain;

import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ReturnHeaderTestSamples.*;
import static com.snp.domain.ReturnHeaderTypeTestSamples.*;
import static com.snp.domain.ReturnItemTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnItem.class);
        ReturnItem returnItem1 = getReturnItemSample1();
        ReturnItem returnItem2 = new ReturnItem();
        assertThat(returnItem1).isNotEqualTo(returnItem2);

        returnItem2.setId(returnItem1.getId());
        assertThat(returnItem1).isEqualTo(returnItem2);

        returnItem2 = getReturnItemSample2();
        assertThat(returnItem1).isNotEqualTo(returnItem2);
    }

    @Test
    void returnHeaderTest() {
        ReturnItem returnItem = getReturnItemRandomSampleGenerator();
        ReturnHeader returnHeaderBack = getReturnHeaderRandomSampleGenerator();

        returnItem.setReturnHeader(returnHeaderBack);
        assertThat(returnItem.getReturnHeader()).isEqualTo(returnHeaderBack);

        returnItem.returnHeader(null);
        assertThat(returnItem.getReturnHeader()).isNull();
    }

    @Test
    void returnTypeTest() {
        ReturnItem returnItem = getReturnItemRandomSampleGenerator();
        ReturnHeaderType returnHeaderTypeBack = getReturnHeaderTypeRandomSampleGenerator();

        returnItem.setReturnType(returnHeaderTypeBack);
        assertThat(returnItem.getReturnType()).isEqualTo(returnHeaderTypeBack);

        returnItem.returnType(null);
        assertThat(returnItem.getReturnType()).isNull();
    }

    @Test
    void productTest() {
        ReturnItem returnItem = getReturnItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        returnItem.setProduct(productBack);
        assertThat(returnItem.getProduct()).isEqualTo(productBack);

        returnItem.product(null);
        assertThat(returnItem.getProduct()).isNull();
    }

    @Test
    void orderItemTest() {
        ReturnItem returnItem = getReturnItemRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        returnItem.setOrderItem(orderItemBack);
        assertThat(returnItem.getOrderItem()).isEqualTo(orderItemBack);

        returnItem.orderItem(null);
        assertThat(returnItem.getOrderItem()).isNull();
    }

    @Test
    void statusTest() {
        ReturnItem returnItem = getReturnItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        returnItem.setStatus(statusBack);
        assertThat(returnItem.getStatus()).isEqualTo(statusBack);

        returnItem.status(null);
        assertThat(returnItem.getStatus()).isNull();
    }
}
