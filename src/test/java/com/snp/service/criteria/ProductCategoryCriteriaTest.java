package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCategoryCriteriaTest {

    @Test
    void newProductCategoryCriteriaHasAllFiltersNullTest() {
        var productCategoryCriteria = new ProductCategoryCriteria();
        assertThat(productCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var productCategoryCriteria = new ProductCategoryCriteria();

        setAllFilters(productCategoryCriteria);

        assertThat(productCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCategoryCriteriaCopyCreatesNullFilterTest() {
        var productCategoryCriteria = new ProductCategoryCriteria();
        var copy = productCategoryCriteria.copy();

        assertThat(productCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryCriteria)
        );
    }

    @Test
    void productCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCategoryCriteria = new ProductCategoryCriteria();
        setAllFilters(productCategoryCriteria);

        var copy = productCategoryCriteria.copy();

        assertThat(productCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCategoryCriteria = new ProductCategoryCriteria();

        assertThat(productCategoryCriteria).hasToString("ProductCategoryCriteria{}");
    }

    private static void setAllFilters(ProductCategoryCriteria productCategoryCriteria) {
        productCategoryCriteria.id();
        productCategoryCriteria.name();
        productCategoryCriteria.description();
        productCategoryCriteria.longDescription();
        productCategoryCriteria.attribute();
        productCategoryCriteria.sequenceNo();
        productCategoryCriteria.imageUrl();
        productCategoryCriteria.altImageUrl();
        productCategoryCriteria.bannerUrl();
        productCategoryCriteria.largeImageUrl();
        productCategoryCriteria.altLargeImageUrl();
        productCategoryCriteria.isBanner();
        productCategoryCriteria.productCategoryTypeId();
        productCategoryCriteria.parentId();
        productCategoryCriteria.distinct();
    }

    private static Condition<ProductCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getLongDescription()) &&
                condition.apply(criteria.getAttribute()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getAltImageUrl()) &&
                condition.apply(criteria.getBannerUrl()) &&
                condition.apply(criteria.getLargeImageUrl()) &&
                condition.apply(criteria.getAltLargeImageUrl()) &&
                condition.apply(criteria.getIsBanner()) &&
                condition.apply(criteria.getProductCategoryTypeId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCategoryCriteria> copyFiltersAre(
        ProductCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getLongDescription(), copy.getLongDescription()) &&
                condition.apply(criteria.getAttribute(), copy.getAttribute()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getAltImageUrl(), copy.getAltImageUrl()) &&
                condition.apply(criteria.getBannerUrl(), copy.getBannerUrl()) &&
                condition.apply(criteria.getLargeImageUrl(), copy.getLargeImageUrl()) &&
                condition.apply(criteria.getAltLargeImageUrl(), copy.getAltLargeImageUrl()) &&
                condition.apply(criteria.getIsBanner(), copy.getIsBanner()) &&
                condition.apply(criteria.getProductCategoryTypeId(), copy.getProductCategoryTypeId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
