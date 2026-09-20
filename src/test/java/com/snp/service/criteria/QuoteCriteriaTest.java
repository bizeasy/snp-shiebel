package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteCriteriaTest {

    @Test
    void newQuoteCriteriaHasAllFiltersNullTest() {
        var quoteCriteria = new QuoteCriteria();
        assertThat(quoteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteCriteria = new QuoteCriteria();

        setAllFilters(quoteCriteria);

        assertThat(quoteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteCriteriaCopyCreatesNullFilterTest() {
        var quoteCriteria = new QuoteCriteria();
        var copy = quoteCriteria.copy();

        assertThat(quoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteCriteria)
        );
    }

    @Test
    void quoteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteCriteria = new QuoteCriteria();
        setAllFilters(quoteCriteria);

        var copy = quoteCriteria.copy();

        assertThat(quoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteCriteria = new QuoteCriteria();

        assertThat(quoteCriteria).hasToString("QuoteCriteria{}");
    }

    private static void setAllFilters(QuoteCriteria quoteCriteria) {
        quoteCriteria.id();
        quoteCriteria.name();
        quoteCriteria.offerNo();
        quoteCriteria.issueDate();
        quoteCriteria.validFromDate();
        quoteCriteria.validThruDate();
        quoteCriteria.description();
        quoteCriteria.offerValue();
        quoteCriteria.remarks();
        quoteCriteria.quoteTypeId();
        quoteCriteria.custRequestId();
        quoteCriteria.partyId();
        quoteCriteria.statusId();
        quoteCriteria.productStoreId();
        quoteCriteria.salesChannelId();
        quoteCriteria.currencyUomId();
        quoteCriteria.currencyConversionId();
        quoteCriteria.distinct();
    }

    private static Condition<QuoteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getOfferNo()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getValidFromDate()) &&
                condition.apply(criteria.getValidThruDate()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getOfferValue()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getQuoteTypeId()) &&
                condition.apply(criteria.getCustRequestId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getProductStoreId()) &&
                condition.apply(criteria.getSalesChannelId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getCurrencyConversionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteCriteria> copyFiltersAre(QuoteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getOfferNo(), copy.getOfferNo()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getValidFromDate(), copy.getValidFromDate()) &&
                condition.apply(criteria.getValidThruDate(), copy.getValidThruDate()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getOfferValue(), copy.getOfferValue()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getQuoteTypeId(), copy.getQuoteTypeId()) &&
                condition.apply(criteria.getCustRequestId(), copy.getCustRequestId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getProductStoreId(), copy.getProductStoreId()) &&
                condition.apply(criteria.getSalesChannelId(), copy.getSalesChannelId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getCurrencyConversionId(), copy.getCurrencyConversionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
