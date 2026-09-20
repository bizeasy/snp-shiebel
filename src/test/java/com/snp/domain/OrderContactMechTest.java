package com.snp.domain;

import static com.snp.domain.ContactMechPurposeTestSamples.*;
import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.OrderContactMechTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderContactMechTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderContactMech.class);
        OrderContactMech orderContactMech1 = getOrderContactMechSample1();
        OrderContactMech orderContactMech2 = new OrderContactMech();
        assertThat(orderContactMech1).isNotEqualTo(orderContactMech2);

        orderContactMech2.setId(orderContactMech1.getId());
        assertThat(orderContactMech1).isEqualTo(orderContactMech2);

        orderContactMech2 = getOrderContactMechSample2();
        assertThat(orderContactMech1).isNotEqualTo(orderContactMech2);
    }

    @Test
    void orderTest() {
        OrderContactMech orderContactMech = getOrderContactMechRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderContactMech.setOrder(orderBack);
        assertThat(orderContactMech.getOrder()).isEqualTo(orderBack);

        orderContactMech.order(null);
        assertThat(orderContactMech.getOrder()).isNull();
    }

    @Test
    void contactMechTest() {
        OrderContactMech orderContactMech = getOrderContactMechRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        orderContactMech.setContactMech(contactMechBack);
        assertThat(orderContactMech.getContactMech()).isEqualTo(contactMechBack);

        orderContactMech.contactMech(null);
        assertThat(orderContactMech.getContactMech()).isNull();
    }

    @Test
    void contactMechPurposeTest() {
        OrderContactMech orderContactMech = getOrderContactMechRandomSampleGenerator();
        ContactMechPurpose contactMechPurposeBack = getContactMechPurposeRandomSampleGenerator();

        orderContactMech.setContactMechPurpose(contactMechPurposeBack);
        assertThat(orderContactMech.getContactMechPurpose()).isEqualTo(contactMechPurposeBack);

        orderContactMech.contactMechPurpose(null);
        assertThat(orderContactMech.getContactMechPurpose()).isNull();
    }
}
