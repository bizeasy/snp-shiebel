package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentGatewayConfigCriteriaTest {

    @Test
    void newPaymentGatewayConfigCriteriaHasAllFiltersNullTest() {
        var paymentGatewayConfigCriteria = new PaymentGatewayConfigCriteria();
        assertThat(paymentGatewayConfigCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentGatewayConfigCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentGatewayConfigCriteria = new PaymentGatewayConfigCriteria();

        setAllFilters(paymentGatewayConfigCriteria);

        assertThat(paymentGatewayConfigCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentGatewayConfigCriteriaCopyCreatesNullFilterTest() {
        var paymentGatewayConfigCriteria = new PaymentGatewayConfigCriteria();
        var copy = paymentGatewayConfigCriteria.copy();

        assertThat(paymentGatewayConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentGatewayConfigCriteria)
        );
    }

    @Test
    void paymentGatewayConfigCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentGatewayConfigCriteria = new PaymentGatewayConfigCriteria();
        setAllFilters(paymentGatewayConfigCriteria);

        var copy = paymentGatewayConfigCriteria.copy();

        assertThat(paymentGatewayConfigCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentGatewayConfigCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentGatewayConfigCriteria = new PaymentGatewayConfigCriteria();

        assertThat(paymentGatewayConfigCriteria).hasToString("PaymentGatewayConfigCriteria{}");
    }

    private static void setAllFilters(PaymentGatewayConfigCriteria paymentGatewayConfigCriteria) {
        paymentGatewayConfigCriteria.id();
        paymentGatewayConfigCriteria.name();
        paymentGatewayConfigCriteria.authUrl();
        paymentGatewayConfigCriteria.releaseUrl();
        paymentGatewayConfigCriteria.refundUrl();
        paymentGatewayConfigCriteria.distinct();
    }

    private static Condition<PaymentGatewayConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAuthUrl()) &&
                condition.apply(criteria.getReleaseUrl()) &&
                condition.apply(criteria.getRefundUrl()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentGatewayConfigCriteria> copyFiltersAre(
        PaymentGatewayConfigCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAuthUrl(), copy.getAuthUrl()) &&
                condition.apply(criteria.getReleaseUrl(), copy.getReleaseUrl()) &&
                condition.apply(criteria.getRefundUrl(), copy.getRefundUrl()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
