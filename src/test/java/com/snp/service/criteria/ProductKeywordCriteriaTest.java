package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductKeywordCriteriaTest {

    @Test
    void newProductKeywordCriteriaHasAllFiltersNullTest() {
        var productKeywordCriteria = new ProductKeywordCriteria();
        assertThat(productKeywordCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productKeywordCriteriaFluentMethodsCreatesFiltersTest() {
        var productKeywordCriteria = new ProductKeywordCriteria();

        setAllFilters(productKeywordCriteria);

        assertThat(productKeywordCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productKeywordCriteriaCopyCreatesNullFilterTest() {
        var productKeywordCriteria = new ProductKeywordCriteria();
        var copy = productKeywordCriteria.copy();

        assertThat(productKeywordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productKeywordCriteria)
        );
    }

    @Test
    void productKeywordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productKeywordCriteria = new ProductKeywordCriteria();
        setAllFilters(productKeywordCriteria);

        var copy = productKeywordCriteria.copy();

        assertThat(productKeywordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productKeywordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productKeywordCriteria = new ProductKeywordCriteria();

        assertThat(productKeywordCriteria).hasToString("ProductKeywordCriteria{}");
    }

    private static void setAllFilters(ProductKeywordCriteria productKeywordCriteria) {
        productKeywordCriteria.id();
        productKeywordCriteria.keyword();
        productKeywordCriteria.relevancyWeight();
        productKeywordCriteria.productId();
        productKeywordCriteria.keywordTypeId();
        productKeywordCriteria.distinct();
    }

    private static Condition<ProductKeywordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKeyword()) &&
                condition.apply(criteria.getRelevancyWeight()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getKeywordTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductKeywordCriteria> copyFiltersAre(
        ProductKeywordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKeyword(), copy.getKeyword()) &&
                condition.apply(criteria.getRelevancyWeight(), copy.getRelevancyWeight()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getKeywordTypeId(), copy.getKeywordTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
