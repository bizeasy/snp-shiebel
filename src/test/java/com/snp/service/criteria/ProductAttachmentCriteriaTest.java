package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductAttachmentCriteriaTest {

    @Test
    void newProductAttachmentCriteriaHasAllFiltersNullTest() {
        var productAttachmentCriteria = new ProductAttachmentCriteria();
        assertThat(productAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var productAttachmentCriteria = new ProductAttachmentCriteria();

        setAllFilters(productAttachmentCriteria);

        assertThat(productAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productAttachmentCriteriaCopyCreatesNullFilterTest() {
        var productAttachmentCriteria = new ProductAttachmentCriteria();
        var copy = productAttachmentCriteria.copy();

        assertThat(productAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productAttachmentCriteria)
        );
    }

    @Test
    void productAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productAttachmentCriteria = new ProductAttachmentCriteria();
        setAllFilters(productAttachmentCriteria);

        var copy = productAttachmentCriteria.copy();

        assertThat(productAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productAttachmentCriteria = new ProductAttachmentCriteria();

        assertThat(productAttachmentCriteria).hasToString("ProductAttachmentCriteria{}");
    }

    private static void setAllFilters(ProductAttachmentCriteria productAttachmentCriteria) {
        productAttachmentCriteria.id();
        productAttachmentCriteria.name();
        productAttachmentCriteria.productId();
        productAttachmentCriteria.attachmentId();
        productAttachmentCriteria.distinct();
    }

    private static Condition<ProductAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductAttachmentCriteria> copyFiltersAre(
        ProductAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
