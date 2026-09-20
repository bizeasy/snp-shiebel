package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteAdjustmentCriteriaTest {

    @Test
    void newQuoteAdjustmentCriteriaHasAllFiltersNullTest() {
        var quoteAdjustmentCriteria = new QuoteAdjustmentCriteria();
        assertThat(quoteAdjustmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteAdjustmentCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteAdjustmentCriteria = new QuoteAdjustmentCriteria();

        setAllFilters(quoteAdjustmentCriteria);

        assertThat(quoteAdjustmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteAdjustmentCriteriaCopyCreatesNullFilterTest() {
        var quoteAdjustmentCriteria = new QuoteAdjustmentCriteria();
        var copy = quoteAdjustmentCriteria.copy();

        assertThat(quoteAdjustmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteAdjustmentCriteria)
        );
    }

    @Test
    void quoteAdjustmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteAdjustmentCriteria = new QuoteAdjustmentCriteria();
        setAllFilters(quoteAdjustmentCriteria);

        var copy = quoteAdjustmentCriteria.copy();

        assertThat(quoteAdjustmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteAdjustmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteAdjustmentCriteria = new QuoteAdjustmentCriteria();

        assertThat(quoteAdjustmentCriteria).hasToString("QuoteAdjustmentCriteria{}");
    }

    private static void setAllFilters(QuoteAdjustmentCriteria quoteAdjustmentCriteria) {
        quoteAdjustmentCriteria.id();
        quoteAdjustmentCriteria.description();
        quoteAdjustmentCriteria.comments();
        quoteAdjustmentCriteria.amount();
        quoteAdjustmentCriteria.sourcePercentage();
        quoteAdjustmentCriteria.sourceRefId();
        quoteAdjustmentCriteria.exemptAmount();
        quoteAdjustmentCriteria.includeInTax();
        quoteAdjustmentCriteria.includeInShipping();
        quoteAdjustmentCriteria.includedInPrice();
        quoteAdjustmentCriteria.entryDate();
        quoteAdjustmentCriteria.quoteId();
        quoteAdjustmentCriteria.quoteItemId();
        quoteAdjustmentCriteria.adjustmentTypeId();
        quoteAdjustmentCriteria.productId();
        quoteAdjustmentCriteria.productFeatureId();
        quoteAdjustmentCriteria.taxAuthorityId();
        quoteAdjustmentCriteria.distinct();
    }

    private static Condition<QuoteAdjustmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getSourcePercentage()) &&
                condition.apply(criteria.getSourceRefId()) &&
                condition.apply(criteria.getExemptAmount()) &&
                condition.apply(criteria.getIncludeInTax()) &&
                condition.apply(criteria.getIncludeInShipping()) &&
                condition.apply(criteria.getIncludedInPrice()) &&
                condition.apply(criteria.getEntryDate()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getQuoteItemId()) &&
                condition.apply(criteria.getAdjustmentTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getTaxAuthorityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteAdjustmentCriteria> copyFiltersAre(
        QuoteAdjustmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getSourcePercentage(), copy.getSourcePercentage()) &&
                condition.apply(criteria.getSourceRefId(), copy.getSourceRefId()) &&
                condition.apply(criteria.getExemptAmount(), copy.getExemptAmount()) &&
                condition.apply(criteria.getIncludeInTax(), copy.getIncludeInTax()) &&
                condition.apply(criteria.getIncludeInShipping(), copy.getIncludeInShipping()) &&
                condition.apply(criteria.getIncludedInPrice(), copy.getIncludedInPrice()) &&
                condition.apply(criteria.getEntryDate(), copy.getEntryDate()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getQuoteItemId(), copy.getQuoteItemId()) &&
                condition.apply(criteria.getAdjustmentTypeId(), copy.getAdjustmentTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getTaxAuthorityId(), copy.getTaxAuthorityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
