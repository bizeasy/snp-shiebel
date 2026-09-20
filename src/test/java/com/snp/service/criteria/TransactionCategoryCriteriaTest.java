package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransactionCategoryCriteriaTest {

    @Test
    void newTransactionCategoryCriteriaHasAllFiltersNullTest() {
        var transactionCategoryCriteria = new TransactionCategoryCriteria();
        assertThat(transactionCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transactionCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var transactionCategoryCriteria = new TransactionCategoryCriteria();

        setAllFilters(transactionCategoryCriteria);

        assertThat(transactionCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transactionCategoryCriteriaCopyCreatesNullFilterTest() {
        var transactionCategoryCriteria = new TransactionCategoryCriteria();
        var copy = transactionCategoryCriteria.copy();

        assertThat(transactionCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionCategoryCriteria)
        );
    }

    @Test
    void transactionCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transactionCategoryCriteria = new TransactionCategoryCriteria();
        setAllFilters(transactionCategoryCriteria);

        var copy = transactionCategoryCriteria.copy();

        assertThat(transactionCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transactionCategoryCriteria = new TransactionCategoryCriteria();

        assertThat(transactionCategoryCriteria).hasToString("TransactionCategoryCriteria{}");
    }

    private static void setAllFilters(TransactionCategoryCriteria transactionCategoryCriteria) {
        transactionCategoryCriteria.id();
        transactionCategoryCriteria.name();
        transactionCategoryCriteria.description();
        transactionCategoryCriteria.distinct();
    }

    private static Condition<TransactionCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransactionCategoryCriteria> copyFiltersAre(
        TransactionCategoryCriteria copy,
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
