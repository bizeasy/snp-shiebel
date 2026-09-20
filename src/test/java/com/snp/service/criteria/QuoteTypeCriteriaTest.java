package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteTypeCriteriaTest {

    @Test
    void newQuoteTypeCriteriaHasAllFiltersNullTest() {
        var quoteTypeCriteria = new QuoteTypeCriteria();
        assertThat(quoteTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteTypeCriteria = new QuoteTypeCriteria();

        setAllFilters(quoteTypeCriteria);

        assertThat(quoteTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteTypeCriteriaCopyCreatesNullFilterTest() {
        var quoteTypeCriteria = new QuoteTypeCriteria();
        var copy = quoteTypeCriteria.copy();

        assertThat(quoteTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteTypeCriteria)
        );
    }

    @Test
    void quoteTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteTypeCriteria = new QuoteTypeCriteria();
        setAllFilters(quoteTypeCriteria);

        var copy = quoteTypeCriteria.copy();

        assertThat(quoteTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteTypeCriteria = new QuoteTypeCriteria();

        assertThat(quoteTypeCriteria).hasToString("QuoteTypeCriteria{}");
    }

    private static void setAllFilters(QuoteTypeCriteria quoteTypeCriteria) {
        quoteTypeCriteria.id();
        quoteTypeCriteria.name();
        quoteTypeCriteria.description();
        quoteTypeCriteria.distinct();
    }

    private static Condition<QuoteTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteTypeCriteria> copyFiltersAre(QuoteTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
