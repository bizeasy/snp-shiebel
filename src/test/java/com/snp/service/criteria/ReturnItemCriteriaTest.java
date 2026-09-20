package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReturnItemCriteriaTest {

    @Test
    void newReturnItemCriteriaHasAllFiltersNullTest() {
        var returnItemCriteria = new ReturnItemCriteria();
        assertThat(returnItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void returnItemCriteriaFluentMethodsCreatesFiltersTest() {
        var returnItemCriteria = new ReturnItemCriteria();

        setAllFilters(returnItemCriteria);

        assertThat(returnItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void returnItemCriteriaCopyCreatesNullFilterTest() {
        var returnItemCriteria = new ReturnItemCriteria();
        var copy = returnItemCriteria.copy();

        assertThat(returnItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(returnItemCriteria)
        );
    }

    @Test
    void returnItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var returnItemCriteria = new ReturnItemCriteria();
        setAllFilters(returnItemCriteria);

        var copy = returnItemCriteria.copy();

        assertThat(returnItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(returnItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var returnItemCriteria = new ReturnItemCriteria();

        assertThat(returnItemCriteria).hasToString("ReturnItemCriteria{}");
    }

    private static void setAllFilters(ReturnItemCriteria returnItemCriteria) {
        returnItemCriteria.id();
        returnItemCriteria.sequenceNo();
        returnItemCriteria.description();
        returnItemCriteria.returnQuantity();
        returnItemCriteria.receivedQuantity();
        returnItemCriteria.returnPrice();
        returnItemCriteria.returnItemPurpose();
        returnItemCriteria.returnHeaderId();
        returnItemCriteria.returnTypeId();
        returnItemCriteria.productId();
        returnItemCriteria.orderItemId();
        returnItemCriteria.statusId();
        returnItemCriteria.distinct();
    }

    private static Condition<ReturnItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getReturnQuantity()) &&
                condition.apply(criteria.getReceivedQuantity()) &&
                condition.apply(criteria.getReturnPrice()) &&
                condition.apply(criteria.getReturnItemPurpose()) &&
                condition.apply(criteria.getReturnHeaderId()) &&
                condition.apply(criteria.getReturnTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReturnItemCriteria> copyFiltersAre(ReturnItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getReturnQuantity(), copy.getReturnQuantity()) &&
                condition.apply(criteria.getReceivedQuantity(), copy.getReceivedQuantity()) &&
                condition.apply(criteria.getReturnPrice(), copy.getReturnPrice()) &&
                condition.apply(criteria.getReturnItemPurpose(), copy.getReturnItemPurpose()) &&
                condition.apply(criteria.getReturnHeaderId(), copy.getReturnHeaderId()) &&
                condition.apply(criteria.getReturnTypeId(), copy.getReturnTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
