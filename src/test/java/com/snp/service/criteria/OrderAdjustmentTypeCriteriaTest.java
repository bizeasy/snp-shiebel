package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderAdjustmentTypeCriteriaTest {

    @Test
    void newOrderAdjustmentTypeCriteriaHasAllFiltersNullTest() {
        var orderAdjustmentTypeCriteria = new OrderAdjustmentTypeCriteria();
        assertThat(orderAdjustmentTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderAdjustmentTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var orderAdjustmentTypeCriteria = new OrderAdjustmentTypeCriteria();

        setAllFilters(orderAdjustmentTypeCriteria);

        assertThat(orderAdjustmentTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderAdjustmentTypeCriteriaCopyCreatesNullFilterTest() {
        var orderAdjustmentTypeCriteria = new OrderAdjustmentTypeCriteria();
        var copy = orderAdjustmentTypeCriteria.copy();

        assertThat(orderAdjustmentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderAdjustmentTypeCriteria)
        );
    }

    @Test
    void orderAdjustmentTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderAdjustmentTypeCriteria = new OrderAdjustmentTypeCriteria();
        setAllFilters(orderAdjustmentTypeCriteria);

        var copy = orderAdjustmentTypeCriteria.copy();

        assertThat(orderAdjustmentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderAdjustmentTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderAdjustmentTypeCriteria = new OrderAdjustmentTypeCriteria();

        assertThat(orderAdjustmentTypeCriteria).hasToString("OrderAdjustmentTypeCriteria{}");
    }

    private static void setAllFilters(OrderAdjustmentTypeCriteria orderAdjustmentTypeCriteria) {
        orderAdjustmentTypeCriteria.id();
        orderAdjustmentTypeCriteria.name();
        orderAdjustmentTypeCriteria.description();
        orderAdjustmentTypeCriteria.parentId();
        orderAdjustmentTypeCriteria.distinct();
    }

    private static Condition<OrderAdjustmentTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderAdjustmentTypeCriteria> copyFiltersAre(
        OrderAdjustmentTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
