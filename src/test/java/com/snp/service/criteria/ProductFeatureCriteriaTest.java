package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureCriteriaTest {

    @Test
    void newProductFeatureCriteriaHasAllFiltersNullTest() {
        var productFeatureCriteria = new ProductFeatureCriteria();
        assertThat(productFeatureCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureCriteria = new ProductFeatureCriteria();

        setAllFilters(productFeatureCriteria);

        assertThat(productFeatureCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureCriteriaCopyCreatesNullFilterTest() {
        var productFeatureCriteria = new ProductFeatureCriteria();
        var copy = productFeatureCriteria.copy();

        assertThat(productFeatureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCriteria)
        );
    }

    @Test
    void productFeatureCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureCriteria = new ProductFeatureCriteria();
        setAllFilters(productFeatureCriteria);

        var copy = productFeatureCriteria.copy();

        assertThat(productFeatureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureCriteria = new ProductFeatureCriteria();

        assertThat(productFeatureCriteria).hasToString("ProductFeatureCriteria{}");
    }

    private static void setAllFilters(ProductFeatureCriteria productFeatureCriteria) {
        productFeatureCriteria.id();
        productFeatureCriteria.name();
        productFeatureCriteria.description();
        productFeatureCriteria.amount();
        productFeatureCriteria.sequenceNo();
        productFeatureCriteria.abbrev();
        productFeatureCriteria.productFeatureTypeId();
        productFeatureCriteria.productFeatureCategoryId();
        productFeatureCriteria.distinct();
    }

    private static Condition<ProductFeatureCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getAbbrev()) &&
                condition.apply(criteria.getProductFeatureTypeId()) &&
                condition.apply(criteria.getProductFeatureCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureCriteria> copyFiltersAre(
        ProductFeatureCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getAbbrev(), copy.getAbbrev()) &&
                condition.apply(criteria.getProductFeatureTypeId(), copy.getProductFeatureTypeId()) &&
                condition.apply(criteria.getProductFeatureCategoryId(), copy.getProductFeatureCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
