package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFacilityCriteriaTest {

    @Test
    void newProductFacilityCriteriaHasAllFiltersNullTest() {
        var productFacilityCriteria = new ProductFacilityCriteria();
        assertThat(productFacilityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFacilityCriteriaFluentMethodsCreatesFiltersTest() {
        var productFacilityCriteria = new ProductFacilityCriteria();

        setAllFilters(productFacilityCriteria);

        assertThat(productFacilityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFacilityCriteriaCopyCreatesNullFilterTest() {
        var productFacilityCriteria = new ProductFacilityCriteria();
        var copy = productFacilityCriteria.copy();

        assertThat(productFacilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityCriteria)
        );
    }

    @Test
    void productFacilityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFacilityCriteria = new ProductFacilityCriteria();
        setAllFilters(productFacilityCriteria);

        var copy = productFacilityCriteria.copy();

        assertThat(productFacilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFacilityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFacilityCriteria = new ProductFacilityCriteria();

        assertThat(productFacilityCriteria).hasToString("ProductFacilityCriteria{}");
    }

    private static void setAllFilters(ProductFacilityCriteria productFacilityCriteria) {
        productFacilityCriteria.id();
        productFacilityCriteria.minimumStock();
        productFacilityCriteria.reorderQty();
        productFacilityCriteria.daysToShip();
        productFacilityCriteria.lastInventoryCount();
        productFacilityCriteria.productId();
        productFacilityCriteria.facilityId();
        productFacilityCriteria.distinct();
    }

    private static Condition<ProductFacilityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMinimumStock()) &&
                condition.apply(criteria.getReorderQty()) &&
                condition.apply(criteria.getDaysToShip()) &&
                condition.apply(criteria.getLastInventoryCount()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFacilityCriteria> copyFiltersAre(
        ProductFacilityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMinimumStock(), copy.getMinimumStock()) &&
                condition.apply(criteria.getReorderQty(), copy.getReorderQty()) &&
                condition.apply(criteria.getDaysToShip(), copy.getDaysToShip()) &&
                condition.apply(criteria.getLastInventoryCount(), copy.getLastInventoryCount()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
