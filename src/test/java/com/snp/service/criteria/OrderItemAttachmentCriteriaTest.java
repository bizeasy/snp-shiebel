package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderItemAttachmentCriteriaTest {

    @Test
    void newOrderItemAttachmentCriteriaHasAllFiltersNullTest() {
        var orderItemAttachmentCriteria = new OrderItemAttachmentCriteria();
        assertThat(orderItemAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderItemAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var orderItemAttachmentCriteria = new OrderItemAttachmentCriteria();

        setAllFilters(orderItemAttachmentCriteria);

        assertThat(orderItemAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderItemAttachmentCriteriaCopyCreatesNullFilterTest() {
        var orderItemAttachmentCriteria = new OrderItemAttachmentCriteria();
        var copy = orderItemAttachmentCriteria.copy();

        assertThat(orderItemAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderItemAttachmentCriteria)
        );
    }

    @Test
    void orderItemAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderItemAttachmentCriteria = new OrderItemAttachmentCriteria();
        setAllFilters(orderItemAttachmentCriteria);

        var copy = orderItemAttachmentCriteria.copy();

        assertThat(orderItemAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderItemAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderItemAttachmentCriteria = new OrderItemAttachmentCriteria();

        assertThat(orderItemAttachmentCriteria).hasToString("OrderItemAttachmentCriteria{}");
    }

    private static void setAllFilters(OrderItemAttachmentCriteria orderItemAttachmentCriteria) {
        orderItemAttachmentCriteria.id();
        orderItemAttachmentCriteria.name();
        orderItemAttachmentCriteria.orderItemId();
        orderItemAttachmentCriteria.attachmentId();
        orderItemAttachmentCriteria.distinct();
    }

    private static Condition<OrderItemAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderItemAttachmentCriteria> copyFiltersAre(
        OrderItemAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
