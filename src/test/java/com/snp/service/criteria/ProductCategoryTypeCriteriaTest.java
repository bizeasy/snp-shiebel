package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCategoryTypeCriteriaTest {

    @Test
    void newProductCategoryTypeCriteriaHasAllFiltersNullTest() {
        var productCategoryTypeCriteria = new ProductCategoryTypeCriteria();
        assertThat(productCategoryTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCategoryTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var productCategoryTypeCriteria = new ProductCategoryTypeCriteria();

        setAllFilters(productCategoryTypeCriteria);

        assertThat(productCategoryTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCategoryTypeCriteriaCopyCreatesNullFilterTest() {
        var productCategoryTypeCriteria = new ProductCategoryTypeCriteria();
        var copy = productCategoryTypeCriteria.copy();

        assertThat(productCategoryTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryTypeCriteria)
        );
    }

    @Test
    void productCategoryTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCategoryTypeCriteria = new ProductCategoryTypeCriteria();
        setAllFilters(productCategoryTypeCriteria);

        var copy = productCategoryTypeCriteria.copy();

        assertThat(productCategoryTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCategoryTypeCriteria = new ProductCategoryTypeCriteria();

        assertThat(productCategoryTypeCriteria).hasToString("ProductCategoryTypeCriteria{}");
    }

    private static void setAllFilters(ProductCategoryTypeCriteria productCategoryTypeCriteria) {
        productCategoryTypeCriteria.id();
        productCategoryTypeCriteria.name();
        productCategoryTypeCriteria.description();
        productCategoryTypeCriteria.distinct();
    }

    private static Condition<ProductCategoryTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCategoryTypeCriteria> copyFiltersAre(
        ProductCategoryTypeCriteria copy,
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
