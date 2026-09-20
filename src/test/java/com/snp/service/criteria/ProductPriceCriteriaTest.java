package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductPriceCriteriaTest {

    @Test
    void newProductPriceCriteriaHasAllFiltersNullTest() {
        var productPriceCriteria = new ProductPriceCriteria();
        assertThat(productPriceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productPriceCriteriaFluentMethodsCreatesFiltersTest() {
        var productPriceCriteria = new ProductPriceCriteria();

        setAllFilters(productPriceCriteria);

        assertThat(productPriceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productPriceCriteriaCopyCreatesNullFilterTest() {
        var productPriceCriteria = new ProductPriceCriteria();
        var copy = productPriceCriteria.copy();

        assertThat(productPriceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productPriceCriteria)
        );
    }

    @Test
    void productPriceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productPriceCriteria = new ProductPriceCriteria();
        setAllFilters(productPriceCriteria);

        var copy = productPriceCriteria.copy();

        assertThat(productPriceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productPriceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productPriceCriteria = new ProductPriceCriteria();

        assertThat(productPriceCriteria).hasToString("ProductPriceCriteria{}");
    }

    private static void setAllFilters(ProductPriceCriteria productPriceCriteria) {
        productPriceCriteria.id();
        productPriceCriteria.fromDate();
        productPriceCriteria.thruDate();
        productPriceCriteria.price();
        productPriceCriteria.cgst();
        productPriceCriteria.igst();
        productPriceCriteria.sgst();
        productPriceCriteria.totalPrice();
        productPriceCriteria.productId();
        productPriceCriteria.productPriceTypeId();
        productPriceCriteria.productPricePurposeId();
        productPriceCriteria.currencyUomId();
        productPriceCriteria.distinct();
    }

    private static Condition<ProductPriceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getCgst()) &&
                condition.apply(criteria.getIgst()) &&
                condition.apply(criteria.getSgst()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getProductPriceTypeId()) &&
                condition.apply(criteria.getProductPricePurposeId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductPriceCriteria> copyFiltersAre(
        ProductPriceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getCgst(), copy.getCgst()) &&
                condition.apply(criteria.getIgst(), copy.getIgst()) &&
                condition.apply(criteria.getSgst(), copy.getSgst()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getProductPriceTypeId(), copy.getProductPriceTypeId()) &&
                condition.apply(criteria.getProductPricePurposeId(), copy.getProductPricePurposeId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
