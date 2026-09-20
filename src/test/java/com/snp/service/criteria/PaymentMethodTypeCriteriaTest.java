package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentMethodTypeCriteriaTest {

    @Test
    void newPaymentMethodTypeCriteriaHasAllFiltersNullTest() {
        var paymentMethodTypeCriteria = new PaymentMethodTypeCriteria();
        assertThat(paymentMethodTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentMethodTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentMethodTypeCriteria = new PaymentMethodTypeCriteria();

        setAllFilters(paymentMethodTypeCriteria);

        assertThat(paymentMethodTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentMethodTypeCriteriaCopyCreatesNullFilterTest() {
        var paymentMethodTypeCriteria = new PaymentMethodTypeCriteria();
        var copy = paymentMethodTypeCriteria.copy();

        assertThat(paymentMethodTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentMethodTypeCriteria)
        );
    }

    @Test
    void paymentMethodTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentMethodTypeCriteria = new PaymentMethodTypeCriteria();
        setAllFilters(paymentMethodTypeCriteria);

        var copy = paymentMethodTypeCriteria.copy();

        assertThat(paymentMethodTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentMethodTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentMethodTypeCriteria = new PaymentMethodTypeCriteria();

        assertThat(paymentMethodTypeCriteria).hasToString("PaymentMethodTypeCriteria{}");
    }

    private static void setAllFilters(PaymentMethodTypeCriteria paymentMethodTypeCriteria) {
        paymentMethodTypeCriteria.id();
        paymentMethodTypeCriteria.name();
        paymentMethodTypeCriteria.description();
        paymentMethodTypeCriteria.distinct();
    }

    private static Condition<PaymentMethodTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentMethodTypeCriteria> copyFiltersAre(
        PaymentMethodTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
