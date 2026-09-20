package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteItemCriteriaTest {

    @Test
    void newQuoteItemCriteriaHasAllFiltersNullTest() {
        var quoteItemCriteria = new QuoteItemCriteria();
        assertThat(quoteItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteItemCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteItemCriteria = new QuoteItemCriteria();

        setAllFilters(quoteItemCriteria);

        assertThat(quoteItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteItemCriteriaCopyCreatesNullFilterTest() {
        var quoteItemCriteria = new QuoteItemCriteria();
        var copy = quoteItemCriteria.copy();

        assertThat(quoteItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteItemCriteria)
        );
    }

    @Test
    void quoteItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteItemCriteria = new QuoteItemCriteria();
        setAllFilters(quoteItemCriteria);

        var copy = quoteItemCriteria.copy();

        assertThat(quoteItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteItemCriteria = new QuoteItemCriteria();

        assertThat(quoteItemCriteria).hasToString("QuoteItemCriteria{}");
    }

    private static void setAllFilters(QuoteItemCriteria quoteItemCriteria) {
        quoteItemCriteria.id();
        quoteItemCriteria.sequenceNo();
        quoteItemCriteria.quantity();
        quoteItemCriteria.selectedAmount();
        quoteItemCriteria.quoteUnitPrice();
        quoteItemCriteria.totalAmount();
        quoteItemCriteria.estimatedDeliveryDate();
        quoteItemCriteria.leadTimeDays();
        quoteItemCriteria.comments();
        quoteItemCriteria.quoteId();
        quoteItemCriteria.productId();
        quoteItemCriteria.productFeatureId();
        quoteItemCriteria.uomId();
        quoteItemCriteria.statusId();
        quoteItemCriteria.custRequestId();
        quoteItemCriteria.custRequestItemId();
        quoteItemCriteria.distinct();
    }

    private static Condition<QuoteItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getSelectedAmount()) &&
                condition.apply(criteria.getQuoteUnitPrice()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getLeadTimeDays()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getCustRequestId()) &&
                condition.apply(criteria.getCustRequestItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteItemCriteria> copyFiltersAre(QuoteItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getSelectedAmount(), copy.getSelectedAmount()) &&
                condition.apply(criteria.getQuoteUnitPrice(), copy.getQuoteUnitPrice()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getEstimatedDeliveryDate(), copy.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getLeadTimeDays(), copy.getLeadTimeDays()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getCustRequestId(), copy.getCustRequestId()) &&
                condition.apply(criteria.getCustRequestItemId(), copy.getCustRequestItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
