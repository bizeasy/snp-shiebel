package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestCriteriaTest {

    @Test
    void newCustRequestCriteriaHasAllFiltersNullTest() {
        var custRequestCriteria = new CustRequestCriteria();
        assertThat(custRequestCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestCriteria = new CustRequestCriteria();

        setAllFilters(custRequestCriteria);

        assertThat(custRequestCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestCriteriaCopyCreatesNullFilterTest() {
        var custRequestCriteria = new CustRequestCriteria();
        var copy = custRequestCriteria.copy();

        assertThat(custRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestCriteria)
        );
    }

    @Test
    void custRequestCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestCriteria = new CustRequestCriteria();
        setAllFilters(custRequestCriteria);

        var copy = custRequestCriteria.copy();

        assertThat(custRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestCriteria = new CustRequestCriteria();

        assertThat(custRequestCriteria).hasToString("CustRequestCriteria{}");
    }

    private static void setAllFilters(CustRequestCriteria custRequestCriteria) {
        custRequestCriteria.id();
        custRequestCriteria.name();
        custRequestCriteria.description();
        custRequestCriteria.priority();
        custRequestCriteria.custRequestDate();
        custRequestCriteria.responseRequiredDate();
        custRequestCriteria.openDateTime();
        custRequestCriteria.closedDateTime();
        custRequestCriteria.internalComment();
        custRequestCriteria.billed();
        custRequestCriteria.custEstimatedDurationDays();
        custRequestCriteria.custRequestTypeId();
        custRequestCriteria.custRequestCategoryId();
        custRequestCriteria.statusId();
        custRequestCriteria.fromPartyId();
        custRequestCriteria.productStoreId();
        custRequestCriteria.salesChannelId();
        custRequestCriteria.currencyUomId();
        custRequestCriteria.distinct();
    }

    private static Condition<CustRequestCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getCustRequestDate()) &&
                condition.apply(criteria.getResponseRequiredDate()) &&
                condition.apply(criteria.getOpenDateTime()) &&
                condition.apply(criteria.getClosedDateTime()) &&
                condition.apply(criteria.getInternalComment()) &&
                condition.apply(criteria.getBilled()) &&
                condition.apply(criteria.getCustEstimatedDurationDays()) &&
                condition.apply(criteria.getCustRequestTypeId()) &&
                condition.apply(criteria.getCustRequestCategoryId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getProductStoreId()) &&
                condition.apply(criteria.getSalesChannelId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestCriteria> copyFiltersAre(CustRequestCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getCustRequestDate(), copy.getCustRequestDate()) &&
                condition.apply(criteria.getResponseRequiredDate(), copy.getResponseRequiredDate()) &&
                condition.apply(criteria.getOpenDateTime(), copy.getOpenDateTime()) &&
                condition.apply(criteria.getClosedDateTime(), copy.getClosedDateTime()) &&
                condition.apply(criteria.getInternalComment(), copy.getInternalComment()) &&
                condition.apply(criteria.getBilled(), copy.getBilled()) &&
                condition.apply(criteria.getCustEstimatedDurationDays(), copy.getCustEstimatedDurationDays()) &&
                condition.apply(criteria.getCustRequestTypeId(), copy.getCustRequestTypeId()) &&
                condition.apply(criteria.getCustRequestCategoryId(), copy.getCustRequestCategoryId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getProductStoreId(), copy.getProductStoreId()) &&
                condition.apply(criteria.getSalesChannelId(), copy.getSalesChannelId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
