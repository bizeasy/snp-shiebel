package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductAssocCriteriaTest {

    @Test
    void newProductAssocCriteriaHasAllFiltersNullTest() {
        var productAssocCriteria = new ProductAssocCriteria();
        assertThat(productAssocCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productAssocCriteriaFluentMethodsCreatesFiltersTest() {
        var productAssocCriteria = new ProductAssocCriteria();

        setAllFilters(productAssocCriteria);

        assertThat(productAssocCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productAssocCriteriaCopyCreatesNullFilterTest() {
        var productAssocCriteria = new ProductAssocCriteria();
        var copy = productAssocCriteria.copy();

        assertThat(productAssocCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productAssocCriteria)
        );
    }

    @Test
    void productAssocCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productAssocCriteria = new ProductAssocCriteria();
        setAllFilters(productAssocCriteria);

        var copy = productAssocCriteria.copy();

        assertThat(productAssocCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productAssocCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productAssocCriteria = new ProductAssocCriteria();

        assertThat(productAssocCriteria).hasToString("ProductAssocCriteria{}");
    }

    private static void setAllFilters(ProductAssocCriteria productAssocCriteria) {
        productAssocCriteria.id();
        productAssocCriteria.fromDate();
        productAssocCriteria.thruDate();
        productAssocCriteria.sequenceNo();
        productAssocCriteria.quantity();
        productAssocCriteria.productId();
        productAssocCriteria.productToId();
        productAssocCriteria.productAssocTypeId();
        productAssocCriteria.distinct();
    }

    private static Condition<ProductAssocCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductToId()) &&
                condition.apply(criteria.getProductAssocTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductAssocCriteria> copyFiltersAre(
        ProductAssocCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductToId(), copy.getProductToId()) &&
                condition.apply(criteria.getProductAssocTypeId(), copy.getProductAssocTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
