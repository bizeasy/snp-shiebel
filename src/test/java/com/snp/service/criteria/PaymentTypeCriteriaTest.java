package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentTypeCriteriaTest {

    @Test
    void newPaymentTypeCriteriaHasAllFiltersNullTest() {
        var paymentTypeCriteria = new PaymentTypeCriteria();
        assertThat(paymentTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentTypeCriteria = new PaymentTypeCriteria();

        setAllFilters(paymentTypeCriteria);

        assertThat(paymentTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentTypeCriteriaCopyCreatesNullFilterTest() {
        var paymentTypeCriteria = new PaymentTypeCriteria();
        var copy = paymentTypeCriteria.copy();

        assertThat(paymentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentTypeCriteria)
        );
    }

    @Test
    void paymentTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentTypeCriteria = new PaymentTypeCriteria();
        setAllFilters(paymentTypeCriteria);

        var copy = paymentTypeCriteria.copy();

        assertThat(paymentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentTypeCriteria = new PaymentTypeCriteria();

        assertThat(paymentTypeCriteria).hasToString("PaymentTypeCriteria{}");
    }

    private static void setAllFilters(PaymentTypeCriteria paymentTypeCriteria) {
        paymentTypeCriteria.id();
        paymentTypeCriteria.name();
        paymentTypeCriteria.description();
        paymentTypeCriteria.distinct();
    }

    private static Condition<PaymentTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentTypeCriteria> copyFiltersAre(PaymentTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
