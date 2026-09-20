package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReturnHeaderCriteriaTest {

    @Test
    void newReturnHeaderCriteriaHasAllFiltersNullTest() {
        var returnHeaderCriteria = new ReturnHeaderCriteria();
        assertThat(returnHeaderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void returnHeaderCriteriaFluentMethodsCreatesFiltersTest() {
        var returnHeaderCriteria = new ReturnHeaderCriteria();

        setAllFilters(returnHeaderCriteria);

        assertThat(returnHeaderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void returnHeaderCriteriaCopyCreatesNullFilterTest() {
        var returnHeaderCriteria = new ReturnHeaderCriteria();
        var copy = returnHeaderCriteria.copy();

        assertThat(returnHeaderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(returnHeaderCriteria)
        );
    }

    @Test
    void returnHeaderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var returnHeaderCriteria = new ReturnHeaderCriteria();
        setAllFilters(returnHeaderCriteria);

        var copy = returnHeaderCriteria.copy();

        assertThat(returnHeaderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(returnHeaderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var returnHeaderCriteria = new ReturnHeaderCriteria();

        assertThat(returnHeaderCriteria).hasToString("ReturnHeaderCriteria{}");
    }

    private static void setAllFilters(ReturnHeaderCriteria returnHeaderCriteria) {
        returnHeaderCriteria.id();
        returnHeaderCriteria.needsInventoryReceive();
        returnHeaderCriteria.supplierRmaId();
        returnHeaderCriteria.returnTypeId();
        returnHeaderCriteria.statusId();
        returnHeaderCriteria.fromPartyId();
        returnHeaderCriteria.toPartyId();
        returnHeaderCriteria.paymentMethodId();
        returnHeaderCriteria.originContactMechId();
        returnHeaderCriteria.destinationFacilityId();
        returnHeaderCriteria.currencyUomId();
        returnHeaderCriteria.distinct();
    }

    private static Condition<ReturnHeaderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNeedsInventoryReceive()) &&
                condition.apply(criteria.getSupplierRmaId()) &&
                condition.apply(criteria.getReturnTypeId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId()) &&
                condition.apply(criteria.getPaymentMethodId()) &&
                condition.apply(criteria.getOriginContactMechId()) &&
                condition.apply(criteria.getDestinationFacilityId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReturnHeaderCriteria> copyFiltersAre(
        ReturnHeaderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNeedsInventoryReceive(), copy.getNeedsInventoryReceive()) &&
                condition.apply(criteria.getSupplierRmaId(), copy.getSupplierRmaId()) &&
                condition.apply(criteria.getReturnTypeId(), copy.getReturnTypeId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId(), copy.getToPartyId()) &&
                condition.apply(criteria.getPaymentMethodId(), copy.getPaymentMethodId()) &&
                condition.apply(criteria.getOriginContactMechId(), copy.getOriginContactMechId()) &&
                condition.apply(criteria.getDestinationFacilityId(), copy.getDestinationFacilityId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
