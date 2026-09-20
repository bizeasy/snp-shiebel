package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureGroupCriteriaTest {

    @Test
    void newProductFeatureGroupCriteriaHasAllFiltersNullTest() {
        var productFeatureGroupCriteria = new ProductFeatureGroupCriteria();
        assertThat(productFeatureGroupCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureGroupCriteria = new ProductFeatureGroupCriteria();

        setAllFilters(productFeatureGroupCriteria);

        assertThat(productFeatureGroupCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureGroupCriteriaCopyCreatesNullFilterTest() {
        var productFeatureGroupCriteria = new ProductFeatureGroupCriteria();
        var copy = productFeatureGroupCriteria.copy();

        assertThat(productFeatureGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureGroupCriteria)
        );
    }

    @Test
    void productFeatureGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureGroupCriteria = new ProductFeatureGroupCriteria();
        setAllFilters(productFeatureGroupCriteria);

        var copy = productFeatureGroupCriteria.copy();

        assertThat(productFeatureGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureGroupCriteria = new ProductFeatureGroupCriteria();

        assertThat(productFeatureGroupCriteria).hasToString("ProductFeatureGroupCriteria{}");
    }

    private static void setAllFilters(ProductFeatureGroupCriteria productFeatureGroupCriteria) {
        productFeatureGroupCriteria.id();
        productFeatureGroupCriteria.name();
        productFeatureGroupCriteria.description();
        productFeatureGroupCriteria.distinct();
    }

    private static Condition<ProductFeatureGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureGroupCriteria> copyFiltersAre(
        ProductFeatureGroupCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
