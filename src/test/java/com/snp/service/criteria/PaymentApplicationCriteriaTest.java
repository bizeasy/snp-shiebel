package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentApplicationCriteriaTest {

    @Test
    void newPaymentApplicationCriteriaHasAllFiltersNullTest() {
        var paymentApplicationCriteria = new PaymentApplicationCriteria();
        assertThat(paymentApplicationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentApplicationCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentApplicationCriteria = new PaymentApplicationCriteria();

        setAllFilters(paymentApplicationCriteria);

        assertThat(paymentApplicationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentApplicationCriteriaCopyCreatesNullFilterTest() {
        var paymentApplicationCriteria = new PaymentApplicationCriteria();
        var copy = paymentApplicationCriteria.copy();

        assertThat(paymentApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentApplicationCriteria)
        );
    }

    @Test
    void paymentApplicationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentApplicationCriteria = new PaymentApplicationCriteria();
        setAllFilters(paymentApplicationCriteria);

        var copy = paymentApplicationCriteria.copy();

        assertThat(paymentApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentApplicationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentApplicationCriteria = new PaymentApplicationCriteria();

        assertThat(paymentApplicationCriteria).hasToString("PaymentApplicationCriteria{}");
    }

    private static void setAllFilters(PaymentApplicationCriteria paymentApplicationCriteria) {
        paymentApplicationCriteria.id();
        paymentApplicationCriteria.amountApplied();
        paymentApplicationCriteria.paymentId();
        paymentApplicationCriteria.toPaymentId();
        paymentApplicationCriteria.invoiceId();
        paymentApplicationCriteria.invoiceItemId();
        paymentApplicationCriteria.orderId();
        paymentApplicationCriteria.orderItemId();
        paymentApplicationCriteria.distinct();
    }

    private static Condition<PaymentApplicationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAmountApplied()) &&
                condition.apply(criteria.getPaymentId()) &&
                condition.apply(criteria.getToPaymentId()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentApplicationCriteria> copyFiltersAre(
        PaymentApplicationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAmountApplied(), copy.getAmountApplied()) &&
                condition.apply(criteria.getPaymentId(), copy.getPaymentId()) &&
                condition.apply(criteria.getToPaymentId(), copy.getToPaymentId()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId(), copy.getInvoiceItemId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
