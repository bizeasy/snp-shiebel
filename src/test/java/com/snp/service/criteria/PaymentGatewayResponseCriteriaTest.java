package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentGatewayResponseCriteriaTest {

    @Test
    void newPaymentGatewayResponseCriteriaHasAllFiltersNullTest() {
        var paymentGatewayResponseCriteria = new PaymentGatewayResponseCriteria();
        assertThat(paymentGatewayResponseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentGatewayResponseCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentGatewayResponseCriteria = new PaymentGatewayResponseCriteria();

        setAllFilters(paymentGatewayResponseCriteria);

        assertThat(paymentGatewayResponseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentGatewayResponseCriteriaCopyCreatesNullFilterTest() {
        var paymentGatewayResponseCriteria = new PaymentGatewayResponseCriteria();
        var copy = paymentGatewayResponseCriteria.copy();

        assertThat(paymentGatewayResponseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentGatewayResponseCriteria)
        );
    }

    @Test
    void paymentGatewayResponseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentGatewayResponseCriteria = new PaymentGatewayResponseCriteria();
        setAllFilters(paymentGatewayResponseCriteria);

        var copy = paymentGatewayResponseCriteria.copy();

        assertThat(paymentGatewayResponseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentGatewayResponseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentGatewayResponseCriteria = new PaymentGatewayResponseCriteria();

        assertThat(paymentGatewayResponseCriteria).hasToString("PaymentGatewayResponseCriteria{}");
    }

    private static void setAllFilters(PaymentGatewayResponseCriteria paymentGatewayResponseCriteria) {
        paymentGatewayResponseCriteria.id();
        paymentGatewayResponseCriteria.referenceNumber();
        paymentGatewayResponseCriteria.amount();
        paymentGatewayResponseCriteria.gatewayMessage();
        paymentGatewayResponseCriteria.paymentGatewayConfigId();
        paymentGatewayResponseCriteria.paymentMethodTypeId();
        paymentGatewayResponseCriteria.distinct();
    }

    private static Condition<PaymentGatewayResponseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReferenceNumber()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getGatewayMessage()) &&
                condition.apply(criteria.getPaymentGatewayConfigId()) &&
                condition.apply(criteria.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentGatewayResponseCriteria> copyFiltersAre(
        PaymentGatewayResponseCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReferenceNumber(), copy.getReferenceNumber()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getGatewayMessage(), copy.getGatewayMessage()) &&
                condition.apply(criteria.getPaymentGatewayConfigId(), copy.getPaymentGatewayConfigId()) &&
                condition.apply(criteria.getPaymentMethodTypeId(), copy.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
