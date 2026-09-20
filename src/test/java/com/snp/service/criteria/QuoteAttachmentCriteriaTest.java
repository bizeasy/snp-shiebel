package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteAttachmentCriteriaTest {

    @Test
    void newQuoteAttachmentCriteriaHasAllFiltersNullTest() {
        var quoteAttachmentCriteria = new QuoteAttachmentCriteria();
        assertThat(quoteAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteAttachmentCriteria = new QuoteAttachmentCriteria();

        setAllFilters(quoteAttachmentCriteria);

        assertThat(quoteAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteAttachmentCriteriaCopyCreatesNullFilterTest() {
        var quoteAttachmentCriteria = new QuoteAttachmentCriteria();
        var copy = quoteAttachmentCriteria.copy();

        assertThat(quoteAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteAttachmentCriteria)
        );
    }

    @Test
    void quoteAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteAttachmentCriteria = new QuoteAttachmentCriteria();
        setAllFilters(quoteAttachmentCriteria);

        var copy = quoteAttachmentCriteria.copy();

        assertThat(quoteAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteAttachmentCriteria = new QuoteAttachmentCriteria();

        assertThat(quoteAttachmentCriteria).hasToString("QuoteAttachmentCriteria{}");
    }

    private static void setAllFilters(QuoteAttachmentCriteria quoteAttachmentCriteria) {
        quoteAttachmentCriteria.id();
        quoteAttachmentCriteria.name();
        quoteAttachmentCriteria.quoteId();
        quoteAttachmentCriteria.attachmentId();
        quoteAttachmentCriteria.distinct();
    }

    private static Condition<QuoteAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteAttachmentCriteria> copyFiltersAre(
        QuoteAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
