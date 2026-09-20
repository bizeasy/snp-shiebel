package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestItemCriteriaTest {

    @Test
    void newCustRequestItemCriteriaHasAllFiltersNullTest() {
        var custRequestItemCriteria = new CustRequestItemCriteria();
        assertThat(custRequestItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestItemCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestItemCriteria = new CustRequestItemCriteria();

        setAllFilters(custRequestItemCriteria);

        assertThat(custRequestItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestItemCriteriaCopyCreatesNullFilterTest() {
        var custRequestItemCriteria = new CustRequestItemCriteria();
        var copy = custRequestItemCriteria.copy();

        assertThat(custRequestItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestItemCriteria)
        );
    }

    @Test
    void custRequestItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestItemCriteria = new CustRequestItemCriteria();
        setAllFilters(custRequestItemCriteria);

        var copy = custRequestItemCriteria.copy();

        assertThat(custRequestItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestItemCriteria = new CustRequestItemCriteria();

        assertThat(custRequestItemCriteria).hasToString("CustRequestItemCriteria{}");
    }

    private static void setAllFilters(CustRequestItemCriteria custRequestItemCriteria) {
        custRequestItemCriteria.id();
        custRequestItemCriteria.sequenceNo();
        custRequestItemCriteria.priority();
        custRequestItemCriteria.requiredByDate();
        custRequestItemCriteria.quantity();
        custRequestItemCriteria.selectedAmount();
        custRequestItemCriteria.maxAmount();
        custRequestItemCriteria.description();
        custRequestItemCriteria.story();
        custRequestItemCriteria.custRequestId();
        custRequestItemCriteria.statusId();
        custRequestItemCriteria.productId();
        custRequestItemCriteria.uomId();
        custRequestItemCriteria.shoppingListItemId();
        custRequestItemCriteria.distinct();
    }

    private static Condition<CustRequestItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getRequiredByDate()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getSelectedAmount()) &&
                condition.apply(criteria.getMaxAmount()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStory()) &&
                condition.apply(criteria.getCustRequestId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getShoppingListItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestItemCriteria> copyFiltersAre(
        CustRequestItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getRequiredByDate(), copy.getRequiredByDate()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getSelectedAmount(), copy.getSelectedAmount()) &&
                condition.apply(criteria.getMaxAmount(), copy.getMaxAmount()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStory(), copy.getStory()) &&
                condition.apply(criteria.getCustRequestId(), copy.getCustRequestId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getShoppingListItemId(), copy.getShoppingListItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
