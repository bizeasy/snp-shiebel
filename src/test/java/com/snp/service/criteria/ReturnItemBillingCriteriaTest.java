package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReturnItemBillingCriteriaTest {

    @Test
    void newReturnItemBillingCriteriaHasAllFiltersNullTest() {
        var returnItemBillingCriteria = new ReturnItemBillingCriteria();
        assertThat(returnItemBillingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void returnItemBillingCriteriaFluentMethodsCreatesFiltersTest() {
        var returnItemBillingCriteria = new ReturnItemBillingCriteria();

        setAllFilters(returnItemBillingCriteria);

        assertThat(returnItemBillingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void returnItemBillingCriteriaCopyCreatesNullFilterTest() {
        var returnItemBillingCriteria = new ReturnItemBillingCriteria();
        var copy = returnItemBillingCriteria.copy();

        assertThat(returnItemBillingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(returnItemBillingCriteria)
        );
    }

    @Test
    void returnItemBillingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var returnItemBillingCriteria = new ReturnItemBillingCriteria();
        setAllFilters(returnItemBillingCriteria);

        var copy = returnItemBillingCriteria.copy();

        assertThat(returnItemBillingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(returnItemBillingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var returnItemBillingCriteria = new ReturnItemBillingCriteria();

        assertThat(returnItemBillingCriteria).hasToString("ReturnItemBillingCriteria{}");
    }

    private static void setAllFilters(ReturnItemBillingCriteria returnItemBillingCriteria) {
        returnItemBillingCriteria.id();
        returnItemBillingCriteria.quantity();
        returnItemBillingCriteria.amount();
        returnItemBillingCriteria.returnItemId();
        returnItemBillingCriteria.orderItemId();
        returnItemBillingCriteria.invoiceItemId();
        returnItemBillingCriteria.shipmentReceiptId();
        returnItemBillingCriteria.distinct();
    }

    private static Condition<ReturnItemBillingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getReturnItemId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getInvoiceItemId()) &&
                condition.apply(criteria.getShipmentReceiptId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReturnItemBillingCriteria> copyFiltersAre(
        ReturnItemBillingCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getReturnItemId(), copy.getReturnItemId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getInvoiceItemId(), copy.getInvoiceItemId()) &&
                condition.apply(criteria.getShipmentReceiptId(), copy.getShipmentReceiptId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
