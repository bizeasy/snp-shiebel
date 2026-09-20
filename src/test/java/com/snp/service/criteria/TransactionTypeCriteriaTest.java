package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransactionTypeCriteriaTest {

    @Test
    void newTransactionTypeCriteriaHasAllFiltersNullTest() {
        var transactionTypeCriteria = new TransactionTypeCriteria();
        assertThat(transactionTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transactionTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var transactionTypeCriteria = new TransactionTypeCriteria();

        setAllFilters(transactionTypeCriteria);

        assertThat(transactionTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transactionTypeCriteriaCopyCreatesNullFilterTest() {
        var transactionTypeCriteria = new TransactionTypeCriteria();
        var copy = transactionTypeCriteria.copy();

        assertThat(transactionTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionTypeCriteria)
        );
    }

    @Test
    void transactionTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transactionTypeCriteria = new TransactionTypeCriteria();
        setAllFilters(transactionTypeCriteria);

        var copy = transactionTypeCriteria.copy();

        assertThat(transactionTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transactionTypeCriteria = new TransactionTypeCriteria();

        assertThat(transactionTypeCriteria).hasToString("TransactionTypeCriteria{}");
    }

    private static void setAllFilters(TransactionTypeCriteria transactionTypeCriteria) {
        transactionTypeCriteria.id();
        transactionTypeCriteria.name();
        transactionTypeCriteria.description();
        transactionTypeCriteria.categoryId();
        transactionTypeCriteria.distinct();
    }

    private static Condition<TransactionTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransactionTypeCriteria> copyFiltersAre(
        TransactionTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
