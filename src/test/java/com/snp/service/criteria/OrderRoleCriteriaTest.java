package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderRoleCriteriaTest {

    @Test
    void newOrderRoleCriteriaHasAllFiltersNullTest() {
        var orderRoleCriteria = new OrderRoleCriteria();
        assertThat(orderRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var orderRoleCriteria = new OrderRoleCriteria();

        setAllFilters(orderRoleCriteria);

        assertThat(orderRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderRoleCriteriaCopyCreatesNullFilterTest() {
        var orderRoleCriteria = new OrderRoleCriteria();
        var copy = orderRoleCriteria.copy();

        assertThat(orderRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderRoleCriteria)
        );
    }

    @Test
    void orderRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderRoleCriteria = new OrderRoleCriteria();
        setAllFilters(orderRoleCriteria);

        var copy = orderRoleCriteria.copy();

        assertThat(orderRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderRoleCriteria = new OrderRoleCriteria();

        assertThat(orderRoleCriteria).hasToString("OrderRoleCriteria{}");
    }

    private static void setAllFilters(OrderRoleCriteria orderRoleCriteria) {
        orderRoleCriteria.id();
        orderRoleCriteria.orderId();
        orderRoleCriteria.orderItemId();
        orderRoleCriteria.partyId();
        orderRoleCriteria.roleTypeId();
        orderRoleCriteria.distinct();
    }

    private static Condition<OrderRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderRoleCriteria> copyFiltersAre(OrderRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
