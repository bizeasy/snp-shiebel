package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCategoryMemberCriteriaTest {

    @Test
    void newProductCategoryMemberCriteriaHasAllFiltersNullTest() {
        var productCategoryMemberCriteria = new ProductCategoryMemberCriteria();
        assertThat(productCategoryMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCategoryMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var productCategoryMemberCriteria = new ProductCategoryMemberCriteria();

        setAllFilters(productCategoryMemberCriteria);

        assertThat(productCategoryMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCategoryMemberCriteriaCopyCreatesNullFilterTest() {
        var productCategoryMemberCriteria = new ProductCategoryMemberCriteria();
        var copy = productCategoryMemberCriteria.copy();

        assertThat(productCategoryMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryMemberCriteria)
        );
    }

    @Test
    void productCategoryMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCategoryMemberCriteria = new ProductCategoryMemberCriteria();
        setAllFilters(productCategoryMemberCriteria);

        var copy = productCategoryMemberCriteria.copy();

        assertThat(productCategoryMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCategoryMemberCriteria = new ProductCategoryMemberCriteria();

        assertThat(productCategoryMemberCriteria).hasToString("ProductCategoryMemberCriteria{}");
    }

    private static void setAllFilters(ProductCategoryMemberCriteria productCategoryMemberCriteria) {
        productCategoryMemberCriteria.id();
        productCategoryMemberCriteria.fromDate();
        productCategoryMemberCriteria.thruDate();
        productCategoryMemberCriteria.sequenceNo();
        productCategoryMemberCriteria.productId();
        productCategoryMemberCriteria.productCategoryId();
        productCategoryMemberCriteria.distinct();
    }

    private static Condition<ProductCategoryMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCategoryMemberCriteria> copyFiltersAre(
        ProductCategoryMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductCategoryId(), copy.getProductCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
