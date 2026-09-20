package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteRoleCriteriaTest {

    @Test
    void newQuoteRoleCriteriaHasAllFiltersNullTest() {
        var quoteRoleCriteria = new QuoteRoleCriteria();
        assertThat(quoteRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteRoleCriteria = new QuoteRoleCriteria();

        setAllFilters(quoteRoleCriteria);

        assertThat(quoteRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteRoleCriteriaCopyCreatesNullFilterTest() {
        var quoteRoleCriteria = new QuoteRoleCriteria();
        var copy = quoteRoleCriteria.copy();

        assertThat(quoteRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteRoleCriteria)
        );
    }

    @Test
    void quoteRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteRoleCriteria = new QuoteRoleCriteria();
        setAllFilters(quoteRoleCriteria);

        var copy = quoteRoleCriteria.copy();

        assertThat(quoteRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteRoleCriteria = new QuoteRoleCriteria();

        assertThat(quoteRoleCriteria).hasToString("QuoteRoleCriteria{}");
    }

    private static void setAllFilters(QuoteRoleCriteria quoteRoleCriteria) {
        quoteRoleCriteria.id();
        quoteRoleCriteria.quoteId();
        quoteRoleCriteria.quoteItemId();
        quoteRoleCriteria.partyId();
        quoteRoleCriteria.roleTypeId();
        quoteRoleCriteria.distinct();
    }

    private static Condition<QuoteRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getQuoteItemId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteRoleCriteria> copyFiltersAre(QuoteRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getQuoteItemId(), copy.getQuoteItemId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
