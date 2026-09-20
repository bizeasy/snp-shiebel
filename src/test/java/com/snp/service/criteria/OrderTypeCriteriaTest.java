package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderTypeCriteriaTest {

    @Test
    void newOrderTypeCriteriaHasAllFiltersNullTest() {
        var orderTypeCriteria = new OrderTypeCriteria();
        assertThat(orderTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var orderTypeCriteria = new OrderTypeCriteria();

        setAllFilters(orderTypeCriteria);

        assertThat(orderTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderTypeCriteriaCopyCreatesNullFilterTest() {
        var orderTypeCriteria = new OrderTypeCriteria();
        var copy = orderTypeCriteria.copy();

        assertThat(orderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderTypeCriteria)
        );
    }

    @Test
    void orderTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderTypeCriteria = new OrderTypeCriteria();
        setAllFilters(orderTypeCriteria);

        var copy = orderTypeCriteria.copy();

        assertThat(orderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderTypeCriteria = new OrderTypeCriteria();

        assertThat(orderTypeCriteria).hasToString("OrderTypeCriteria{}");
    }

    private static void setAllFilters(OrderTypeCriteria orderTypeCriteria) {
        orderTypeCriteria.id();
        orderTypeCriteria.name();
        orderTypeCriteria.description();
        orderTypeCriteria.distinct();
    }

    private static Condition<OrderTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderTypeCriteria> copyFiltersAre(OrderTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
