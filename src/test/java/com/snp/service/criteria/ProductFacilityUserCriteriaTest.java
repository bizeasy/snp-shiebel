package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFacilityUserCriteriaTest {

    @Test
    void newProductFacilityUserCriteriaHasAllFiltersNullTest() {
        var productFacilityUserCriteria = new ProductFacilityUserCriteria();
        assertThat(productFacilityUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFacilityUserCriteriaFluentMethodsCreatesFiltersTest() {
        var productFacilityUserCriteria = new ProductFacilityUserCriteria();

        setAllFilters(productFacilityUserCriteria);

        assertThat(productFacilityUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFacilityUserCriteriaCopyCreatesNullFilterTest() {
        var productFacilityUserCriteria = new ProductFacilityUserCriteria();
        var copy = productFacilityUserCriteria.copy();

        assertThat(productFacilityUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityUserCriteria)
        );
    }

    @Test
    void productFacilityUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFacilityUserCriteria = new ProductFacilityUserCriteria();
        setAllFilters(productFacilityUserCriteria);

        var copy = productFacilityUserCriteria.copy();

        assertThat(productFacilityUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFacilityUserCriteria = new ProductFacilityUserCriteria();

        assertThat(productFacilityUserCriteria).hasToString("ProductFacilityUserCriteria{}");
    }

    private static void setAllFilters(ProductFacilityUserCriteria productFacilityUserCriteria) {
        productFacilityUserCriteria.id();
        productFacilityUserCriteria.fromDate();
        productFacilityUserCriteria.thruDate();
        productFacilityUserCriteria.costPerDay();
        productFacilityUserCriteria.totalCost();
        productFacilityUserCriteria.discount();
        productFacilityUserCriteria.productFacilityId();
        productFacilityUserCriteria.partyId();
        productFacilityUserCriteria.statusId();
        productFacilityUserCriteria.distinct();
    }

    private static Condition<ProductFacilityUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getCostPerDay()) &&
                condition.apply(criteria.getTotalCost()) &&
                condition.apply(criteria.getDiscount()) &&
                condition.apply(criteria.getProductFacilityId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFacilityUserCriteria> copyFiltersAre(
        ProductFacilityUserCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getCostPerDay(), copy.getCostPerDay()) &&
                condition.apply(criteria.getTotalCost(), copy.getTotalCost()) &&
                condition.apply(criteria.getDiscount(), copy.getDiscount()) &&
                condition.apply(criteria.getProductFacilityId(), copy.getProductFacilityId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
