package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductAssocTypeCriteriaTest {

    @Test
    void newProductAssocTypeCriteriaHasAllFiltersNullTest() {
        var productAssocTypeCriteria = new ProductAssocTypeCriteria();
        assertThat(productAssocTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productAssocTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var productAssocTypeCriteria = new ProductAssocTypeCriteria();

        setAllFilters(productAssocTypeCriteria);

        assertThat(productAssocTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productAssocTypeCriteriaCopyCreatesNullFilterTest() {
        var productAssocTypeCriteria = new ProductAssocTypeCriteria();
        var copy = productAssocTypeCriteria.copy();

        assertThat(productAssocTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productAssocTypeCriteria)
        );
    }

    @Test
    void productAssocTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productAssocTypeCriteria = new ProductAssocTypeCriteria();
        setAllFilters(productAssocTypeCriteria);

        var copy = productAssocTypeCriteria.copy();

        assertThat(productAssocTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productAssocTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productAssocTypeCriteria = new ProductAssocTypeCriteria();

        assertThat(productAssocTypeCriteria).hasToString("ProductAssocTypeCriteria{}");
    }

    private static void setAllFilters(ProductAssocTypeCriteria productAssocTypeCriteria) {
        productAssocTypeCriteria.id();
        productAssocTypeCriteria.name();
        productAssocTypeCriteria.description();
        productAssocTypeCriteria.distinct();
    }

    private static Condition<ProductAssocTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductAssocTypeCriteria> copyFiltersAre(
        ProductAssocTypeCriteria copy,
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
