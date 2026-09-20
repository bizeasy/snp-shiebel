package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductReviewCriteriaTest {

    @Test
    void newProductReviewCriteriaHasAllFiltersNullTest() {
        var productReviewCriteria = new ProductReviewCriteria();
        assertThat(productReviewCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productReviewCriteriaFluentMethodsCreatesFiltersTest() {
        var productReviewCriteria = new ProductReviewCriteria();

        setAllFilters(productReviewCriteria);

        assertThat(productReviewCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productReviewCriteriaCopyCreatesNullFilterTest() {
        var productReviewCriteria = new ProductReviewCriteria();
        var copy = productReviewCriteria.copy();

        assertThat(productReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productReviewCriteria)
        );
    }

    @Test
    void productReviewCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productReviewCriteria = new ProductReviewCriteria();
        setAllFilters(productReviewCriteria);

        var copy = productReviewCriteria.copy();

        assertThat(productReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productReviewCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productReviewCriteria = new ProductReviewCriteria();

        assertThat(productReviewCriteria).hasToString("ProductReviewCriteria{}");
    }

    private static void setAllFilters(ProductReviewCriteria productReviewCriteria) {
        productReviewCriteria.id();
        productReviewCriteria.rating();
        productReviewCriteria.message();
        productReviewCriteria.reviewDate();
        productReviewCriteria.productId();
        productReviewCriteria.partyId();
        productReviewCriteria.orderItemId();
        productReviewCriteria.distinct();
    }

    private static Condition<ProductReviewCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getReviewDate()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductReviewCriteria> copyFiltersAre(
        ProductReviewCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getReviewDate(), copy.getReviewDate()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
