package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFacilityLocationCriteriaTest {

    @Test
    void newProductFacilityLocationCriteriaHasAllFiltersNullTest() {
        var productFacilityLocationCriteria = new ProductFacilityLocationCriteria();
        assertThat(productFacilityLocationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFacilityLocationCriteriaFluentMethodsCreatesFiltersTest() {
        var productFacilityLocationCriteria = new ProductFacilityLocationCriteria();

        setAllFilters(productFacilityLocationCriteria);

        assertThat(productFacilityLocationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFacilityLocationCriteriaCopyCreatesNullFilterTest() {
        var productFacilityLocationCriteria = new ProductFacilityLocationCriteria();
        var copy = productFacilityLocationCriteria.copy();

        assertThat(productFacilityLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityLocationCriteria)
        );
    }

    @Test
    void productFacilityLocationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFacilityLocationCriteria = new ProductFacilityLocationCriteria();
        setAllFilters(productFacilityLocationCriteria);

        var copy = productFacilityLocationCriteria.copy();

        assertThat(productFacilityLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityLocationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFacilityLocationCriteria = new ProductFacilityLocationCriteria();

        assertThat(productFacilityLocationCriteria).hasToString("ProductFacilityLocationCriteria{}");
    }

    private static void setAllFilters(ProductFacilityLocationCriteria productFacilityLocationCriteria) {
        productFacilityLocationCriteria.id();
        productFacilityLocationCriteria.minimumStock();
        productFacilityLocationCriteria.moveQuantity();
        productFacilityLocationCriteria.productId();
        productFacilityLocationCriteria.facilityId();
        productFacilityLocationCriteria.facilityLocationId();
        productFacilityLocationCriteria.distinct();
    }

    private static Condition<ProductFacilityLocationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMinimumStock()) &&
                condition.apply(criteria.getMoveQuantity()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFacilityLocationCriteria> copyFiltersAre(
        ProductFacilityLocationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMinimumStock(), copy.getMinimumStock()) &&
                condition.apply(criteria.getMoveQuantity(), copy.getMoveQuantity()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId(), copy.getFacilityLocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
