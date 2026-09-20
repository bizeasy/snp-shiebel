package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderAdjustmentCriteriaTest {

    @Test
    void newOrderAdjustmentCriteriaHasAllFiltersNullTest() {
        var orderAdjustmentCriteria = new OrderAdjustmentCriteria();
        assertThat(orderAdjustmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderAdjustmentCriteriaFluentMethodsCreatesFiltersTest() {
        var orderAdjustmentCriteria = new OrderAdjustmentCriteria();

        setAllFilters(orderAdjustmentCriteria);

        assertThat(orderAdjustmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderAdjustmentCriteriaCopyCreatesNullFilterTest() {
        var orderAdjustmentCriteria = new OrderAdjustmentCriteria();
        var copy = orderAdjustmentCriteria.copy();

        assertThat(orderAdjustmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderAdjustmentCriteria)
        );
    }

    @Test
    void orderAdjustmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderAdjustmentCriteria = new OrderAdjustmentCriteria();
        setAllFilters(orderAdjustmentCriteria);

        var copy = orderAdjustmentCriteria.copy();

        assertThat(orderAdjustmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderAdjustmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderAdjustmentCriteria = new OrderAdjustmentCriteria();

        assertThat(orderAdjustmentCriteria).hasToString("OrderAdjustmentCriteria{}");
    }

    private static void setAllFilters(OrderAdjustmentCriteria orderAdjustmentCriteria) {
        orderAdjustmentCriteria.id();
        orderAdjustmentCriteria.description();
        orderAdjustmentCriteria.comments();
        orderAdjustmentCriteria.amount();
        orderAdjustmentCriteria.sourcePercentage();
        orderAdjustmentCriteria.sourceRefId();
        orderAdjustmentCriteria.exemptAmount();
        orderAdjustmentCriteria.includeInTax();
        orderAdjustmentCriteria.includeInShipping();
        orderAdjustmentCriteria.includedInPrice();
        orderAdjustmentCriteria.entryDate();
        orderAdjustmentCriteria.orderId();
        orderAdjustmentCriteria.orderItemId();
        orderAdjustmentCriteria.adjustmentTypeId();
        orderAdjustmentCriteria.productId();
        orderAdjustmentCriteria.productFeatureId();
        orderAdjustmentCriteria.taxAuthorityId();
        orderAdjustmentCriteria.distinct();
    }

    private static Condition<OrderAdjustmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getSourcePercentage()) &&
                condition.apply(criteria.getSourceRefId()) &&
                condition.apply(criteria.getExemptAmount()) &&
                condition.apply(criteria.getIncludeInTax()) &&
                condition.apply(criteria.getIncludeInShipping()) &&
                condition.apply(criteria.getIncludedInPrice()) &&
                condition.apply(criteria.getEntryDate()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getAdjustmentTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getTaxAuthorityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderAdjustmentCriteria> copyFiltersAre(
        OrderAdjustmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getSourcePercentage(), copy.getSourcePercentage()) &&
                condition.apply(criteria.getSourceRefId(), copy.getSourceRefId()) &&
                condition.apply(criteria.getExemptAmount(), copy.getExemptAmount()) &&
                condition.apply(criteria.getIncludeInTax(), copy.getIncludeInTax()) &&
                condition.apply(criteria.getIncludeInShipping(), copy.getIncludeInShipping()) &&
                condition.apply(criteria.getIncludedInPrice(), copy.getIncludedInPrice()) &&
                condition.apply(criteria.getEntryDate(), copy.getEntryDate()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getAdjustmentTypeId(), copy.getAdjustmentTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getTaxAuthorityId(), copy.getTaxAuthorityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
