package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderContactMechCriteriaTest {

    @Test
    void newOrderContactMechCriteriaHasAllFiltersNullTest() {
        var orderContactMechCriteria = new OrderContactMechCriteria();
        assertThat(orderContactMechCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderContactMechCriteriaFluentMethodsCreatesFiltersTest() {
        var orderContactMechCriteria = new OrderContactMechCriteria();

        setAllFilters(orderContactMechCriteria);

        assertThat(orderContactMechCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderContactMechCriteriaCopyCreatesNullFilterTest() {
        var orderContactMechCriteria = new OrderContactMechCriteria();
        var copy = orderContactMechCriteria.copy();

        assertThat(orderContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderContactMechCriteria)
        );
    }

    @Test
    void orderContactMechCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderContactMechCriteria = new OrderContactMechCriteria();
        setAllFilters(orderContactMechCriteria);

        var copy = orderContactMechCriteria.copy();

        assertThat(orderContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderContactMechCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderContactMechCriteria = new OrderContactMechCriteria();

        assertThat(orderContactMechCriteria).hasToString("OrderContactMechCriteria{}");
    }

    private static void setAllFilters(OrderContactMechCriteria orderContactMechCriteria) {
        orderContactMechCriteria.id();
        orderContactMechCriteria.fromDate();
        orderContactMechCriteria.thruDate();
        orderContactMechCriteria.orderId();
        orderContactMechCriteria.contactMechId();
        orderContactMechCriteria.contactMechPurposeId();
        orderContactMechCriteria.distinct();
    }

    private static Condition<OrderContactMechCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderContactMechCriteria> copyFiltersAre(
        OrderContactMechCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId(), copy.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
