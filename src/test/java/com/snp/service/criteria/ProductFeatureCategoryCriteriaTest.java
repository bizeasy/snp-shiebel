package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureCategoryCriteriaTest {

    @Test
    void newProductFeatureCategoryCriteriaHasAllFiltersNullTest() {
        var productFeatureCategoryCriteria = new ProductFeatureCategoryCriteria();
        assertThat(productFeatureCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureCategoryCriteria = new ProductFeatureCategoryCriteria();

        setAllFilters(productFeatureCategoryCriteria);

        assertThat(productFeatureCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureCategoryCriteriaCopyCreatesNullFilterTest() {
        var productFeatureCategoryCriteria = new ProductFeatureCategoryCriteria();
        var copy = productFeatureCategoryCriteria.copy();

        assertThat(productFeatureCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCategoryCriteria)
        );
    }

    @Test
    void productFeatureCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureCategoryCriteria = new ProductFeatureCategoryCriteria();
        setAllFilters(productFeatureCategoryCriteria);

        var copy = productFeatureCategoryCriteria.copy();

        assertThat(productFeatureCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureCategoryCriteria = new ProductFeatureCategoryCriteria();

        assertThat(productFeatureCategoryCriteria).hasToString("ProductFeatureCategoryCriteria{}");
    }

    private static void setAllFilters(ProductFeatureCategoryCriteria productFeatureCategoryCriteria) {
        productFeatureCategoryCriteria.id();
        productFeatureCategoryCriteria.name();
        productFeatureCategoryCriteria.description();
        productFeatureCategoryCriteria.distinct();
    }

    private static Condition<ProductFeatureCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureCategoryCriteria> copyFiltersAre(
        ProductFeatureCategoryCriteria copy,
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
