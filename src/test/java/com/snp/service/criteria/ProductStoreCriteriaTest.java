package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductStoreCriteriaTest {

    @Test
    void newProductStoreCriteriaHasAllFiltersNullTest() {
        var productStoreCriteria = new ProductStoreCriteria();
        assertThat(productStoreCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productStoreCriteriaFluentMethodsCreatesFiltersTest() {
        var productStoreCriteria = new ProductStoreCriteria();

        setAllFilters(productStoreCriteria);

        assertThat(productStoreCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productStoreCriteriaCopyCreatesNullFilterTest() {
        var productStoreCriteria = new ProductStoreCriteria();
        var copy = productStoreCriteria.copy();

        assertThat(productStoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productStoreCriteria)
        );
    }

    @Test
    void productStoreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productStoreCriteria = new ProductStoreCriteria();
        setAllFilters(productStoreCriteria);

        var copy = productStoreCriteria.copy();

        assertThat(productStoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productStoreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productStoreCriteria = new ProductStoreCriteria();

        assertThat(productStoreCriteria).hasToString("ProductStoreCriteria{}");
    }

    private static void setAllFilters(ProductStoreCriteria productStoreCriteria) {
        productStoreCriteria.id();
        productStoreCriteria.name();
        productStoreCriteria.title();
        productStoreCriteria.ownerId();
        productStoreCriteria.adminId();
        productStoreCriteria.distinct();
    }

    private static Condition<ProductStoreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getAdminId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductStoreCriteria> copyFiltersAre(
        ProductStoreCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getAdminId(), copy.getAdminId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
