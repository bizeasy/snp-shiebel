package com.snp.domain;

import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductReviewTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReview.class);
        ProductReview productReview1 = getProductReviewSample1();
        ProductReview productReview2 = new ProductReview();
        assertThat(productReview1).isNotEqualTo(productReview2);

        productReview2.setId(productReview1.getId());
        assertThat(productReview1).isEqualTo(productReview2);

        productReview2 = getProductReviewSample2();
        assertThat(productReview1).isNotEqualTo(productReview2);
    }

    @Test
    void productTest() {
        ProductReview productReview = getProductReviewRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productReview.setProduct(productBack);
        assertThat(productReview.getProduct()).isEqualTo(productBack);

        productReview.product(null);
        assertThat(productReview.getProduct()).isNull();
    }

    @Test
    void partyTest() {
        ProductReview productReview = getProductReviewRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        productReview.setParty(partyBack);
        assertThat(productReview.getParty()).isEqualTo(partyBack);

        productReview.party(null);
        assertThat(productReview.getParty()).isNull();
    }

    @Test
    void orderItemTest() {
        ProductReview productReview = getProductReviewRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        productReview.setOrderItem(orderItemBack);
        assertThat(productReview.getOrderItem()).isEqualTo(orderItemBack);

        productReview.orderItem(null);
        assertThat(productReview.getOrderItem()).isNull();
    }
}
