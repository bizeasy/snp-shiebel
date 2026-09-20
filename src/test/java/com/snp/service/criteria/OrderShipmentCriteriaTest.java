package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderShipmentCriteriaTest {

    @Test
    void newOrderShipmentCriteriaHasAllFiltersNullTest() {
        var orderShipmentCriteria = new OrderShipmentCriteria();
        assertThat(orderShipmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderShipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var orderShipmentCriteria = new OrderShipmentCriteria();

        setAllFilters(orderShipmentCriteria);

        assertThat(orderShipmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderShipmentCriteriaCopyCreatesNullFilterTest() {
        var orderShipmentCriteria = new OrderShipmentCriteria();
        var copy = orderShipmentCriteria.copy();

        assertThat(orderShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderShipmentCriteria)
        );
    }

    @Test
    void orderShipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderShipmentCriteria = new OrderShipmentCriteria();
        setAllFilters(orderShipmentCriteria);

        var copy = orderShipmentCriteria.copy();

        assertThat(orderShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderShipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderShipmentCriteria = new OrderShipmentCriteria();

        assertThat(orderShipmentCriteria).hasToString("OrderShipmentCriteria{}");
    }

    private static void setAllFilters(OrderShipmentCriteria orderShipmentCriteria) {
        orderShipmentCriteria.id();
        orderShipmentCriteria.quantity();
        orderShipmentCriteria.orderId();
        orderShipmentCriteria.orderItemId();
        orderShipmentCriteria.shipmentId();
        orderShipmentCriteria.shipmentItemId();
        orderShipmentCriteria.distinct();
    }

    private static Condition<OrderShipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderShipmentCriteria> copyFiltersAre(
        OrderShipmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId(), copy.getShipmentItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
