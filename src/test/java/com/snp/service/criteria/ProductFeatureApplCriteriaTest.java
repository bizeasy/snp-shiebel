package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductFeatureApplCriteriaTest {

    @Test
    void newProductFeatureApplCriteriaHasAllFiltersNullTest() {
        var productFeatureApplCriteria = new ProductFeatureApplCriteria();
        assertThat(productFeatureApplCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productFeatureApplCriteriaFluentMethodsCreatesFiltersTest() {
        var productFeatureApplCriteria = new ProductFeatureApplCriteria();

        setAllFilters(productFeatureApplCriteria);

        assertThat(productFeatureApplCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productFeatureApplCriteriaCopyCreatesNullFilterTest() {
        var productFeatureApplCriteria = new ProductFeatureApplCriteria();
        var copy = productFeatureApplCriteria.copy();

        assertThat(productFeatureApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureApplCriteria)
        );
    }

    @Test
    void productFeatureApplCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productFeatureApplCriteria = new ProductFeatureApplCriteria();
        setAllFilters(productFeatureApplCriteria);

        var copy = productFeatureApplCriteria.copy();

        assertThat(productFeatureApplCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productFeatureApplCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productFeatureApplCriteria = new ProductFeatureApplCriteria();

        assertThat(productFeatureApplCriteria).hasToString("ProductFeatureApplCriteria{}");
    }

    private static void setAllFilters(ProductFeatureApplCriteria productFeatureApplCriteria) {
        productFeatureApplCriteria.id();
        productFeatureApplCriteria.fromDate();
        productFeatureApplCriteria.thruDate();
        productFeatureApplCriteria.sequenceNo();
        productFeatureApplCriteria.amount();
        productFeatureApplCriteria.productId();
        productFeatureApplCriteria.productFeatureId();
        productFeatureApplCriteria.featureApplTypeId();
        productFeatureApplCriteria.distinct();
    }

    private static Condition<ProductFeatureApplCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getFeatureApplTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductFeatureApplCriteria> copyFiltersAre(
        ProductFeatureApplCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getFeatureApplTypeId(), copy.getFeatureApplTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
