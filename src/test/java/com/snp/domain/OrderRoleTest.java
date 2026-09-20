package com.snp.domain;

import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderRoleTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderRole.class);
        OrderRole orderRole1 = getOrderRoleSample1();
        OrderRole orderRole2 = new OrderRole();
        assertThat(orderRole1).isNotEqualTo(orderRole2);

        orderRole2.setId(orderRole1.getId());
        assertThat(orderRole1).isEqualTo(orderRole2);

        orderRole2 = getOrderRoleSample2();
        assertThat(orderRole1).isNotEqualTo(orderRole2);
    }

    @Test
    void orderTest() {
        OrderRole orderRole = getOrderRoleRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderRole.setOrder(orderBack);
        assertThat(orderRole.getOrder()).isEqualTo(orderBack);

        orderRole.order(null);
        assertThat(orderRole.getOrder()).isNull();
    }

    @Test
    void orderItemTest() {
        OrderRole orderRole = getOrderRoleRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderRole.setOrderItem(orderItemBack);
        assertThat(orderRole.getOrderItem()).isEqualTo(orderItemBack);

        orderRole.orderItem(null);
        assertThat(orderRole.getOrderItem()).isNull();
    }

    @Test
    void partyTest() {
        OrderRole orderRole = getOrderRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        orderRole.setParty(partyBack);
        assertThat(orderRole.getParty()).isEqualTo(partyBack);

        orderRole.party(null);
        assertThat(orderRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        OrderRole orderRole = getOrderRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        orderRole.setRoleType(roleTypeBack);
        assertThat(orderRole.getRoleType()).isEqualTo(roleTypeBack);

        orderRole.roleType(null);
        assertThat(orderRole.getRoleType()).isNull();
    }
}
