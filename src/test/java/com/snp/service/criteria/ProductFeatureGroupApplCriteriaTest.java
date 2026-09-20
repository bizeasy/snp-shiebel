package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureGroupApplCriteriaTest {

    @Test
    void newProductFeatureGroupApplCriteriaHasAllFiltersNullTest() {
        var productFeatureGroupApplCriteria = new ProductFeatureGroupApplCriteria();
        assertThat(productFeatureGroupApplCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureGroupApplCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureGroupApplCriteria = new ProductFeatureGroupApplCriteria();

        setAllFilters(productFeatureGroupApplCriteria);

        assertThat(productFeatureGroupApplCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureGroupApplCriteriaCopyCreatesNullFilterTest() {
        var productFeatureGroupApplCriteria = new ProductFeatureGroupApplCriteria();
        var copy = productFeatureGroupApplCriteria.copy();

        assertThat(productFeatureGroupApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureGroupApplCriteria)
        );
    }

    @Test
    void productFeatureGroupApplCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureGroupApplCriteria = new ProductFeatureGroupApplCriteria();
        setAllFilters(productFeatureGroupApplCriteria);

        var copy = productFeatureGroupApplCriteria.copy();

        assertThat(productFeatureGroupApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureGroupApplCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureGroupApplCriteria = new ProductFeatureGroupApplCriteria();

        assertThat(productFeatureGroupApplCriteria).hasToString("ProductFeatureGroupApplCriteria{}");
    }

    private static void setAllFilters(ProductFeatureGroupApplCriteria productFeatureGroupApplCriteria) {
        productFeatureGroupApplCriteria.id();
        productFeatureGroupApplCriteria.fromDate();
        productFeatureGroupApplCriteria.thruDate();
        productFeatureGroupApplCriteria.sequenceNo();
        productFeatureGroupApplCriteria.productFeatureGroupId();
        productFeatureGroupApplCriteria.productFeatureId();
        productFeatureGroupApplCriteria.distinct();
    }

    private static Condition<ProductFeatureGroupApplCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getProductFeatureGroupId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureGroupApplCriteria> copyFiltersAre(
        ProductFeatureGroupApplCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getProductFeatureGroupId(), copy.getProductFeatureGroupId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
