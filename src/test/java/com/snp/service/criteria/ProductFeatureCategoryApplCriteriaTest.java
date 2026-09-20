package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureCategoryApplCriteriaTest {

    @Test
    void newProductFeatureCategoryApplCriteriaHasAllFiltersNullTest() {
        var productFeatureCategoryApplCriteria = new ProductFeatureCategoryApplCriteria();
        assertThat(productFeatureCategoryApplCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureCategoryApplCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureCategoryApplCriteria = new ProductFeatureCategoryApplCriteria();

        setAllFilters(productFeatureCategoryApplCriteria);

        assertThat(productFeatureCategoryApplCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureCategoryApplCriteriaCopyCreatesNullFilterTest() {
        var productFeatureCategoryApplCriteria = new ProductFeatureCategoryApplCriteria();
        var copy = productFeatureCategoryApplCriteria.copy();

        assertThat(productFeatureCategoryApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCategoryApplCriteria)
        );
    }

    @Test
    void productFeatureCategoryApplCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureCategoryApplCriteria = new ProductFeatureCategoryApplCriteria();
        setAllFilters(productFeatureCategoryApplCriteria);

        var copy = productFeatureCategoryApplCriteria.copy();

        assertThat(productFeatureCategoryApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCategoryApplCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureCategoryApplCriteria = new ProductFeatureCategoryApplCriteria();

        assertThat(productFeatureCategoryApplCriteria).hasToString("ProductFeatureCategoryApplCriteria{}");
    }

    private static void setAllFilters(ProductFeatureCategoryApplCriteria productFeatureCategoryApplCriteria) {
        productFeatureCategoryApplCriteria.id();
        productFeatureCategoryApplCriteria.fromDate();
        productFeatureCategoryApplCriteria.thruDate();
        productFeatureCategoryApplCriteria.productCategoryId();
        productFeatureCategoryApplCriteria.productFeatureCategoryId();
        productFeatureCategoryApplCriteria.distinct();
    }

    private static Condition<ProductFeatureCategoryApplCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getProductCategoryId()) &&
                condition.apply(criteria.getProductFeatureCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureCategoryApplCriteria> copyFiltersAre(
        ProductFeatureCategoryApplCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getProductCategoryId(), copy.getProductCategoryId()) &&
                condition.apply(criteria.getProductFeatureCategoryId(), copy.getProductFeatureCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
