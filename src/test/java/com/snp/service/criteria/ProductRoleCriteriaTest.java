package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductRoleCriteriaTest {

    @Test
    void newProductRoleCriteriaHasAllFiltersNullTest() {
        var productRoleCriteria = new ProductRoleCriteria();
        assertThat(productRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var productRoleCriteria = new ProductRoleCriteria();

        setAllFilters(productRoleCriteria);

        assertThat(productRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productRoleCriteriaCopyCreatesNullFilterTest() {
        var productRoleCriteria = new ProductRoleCriteria();
        var copy = productRoleCriteria.copy();

        assertThat(productRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productRoleCriteria)
        );
    }

    @Test
    void productRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productRoleCriteria = new ProductRoleCriteria();
        setAllFilters(productRoleCriteria);

        var copy = productRoleCriteria.copy();

        assertThat(productRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productRoleCriteria = new ProductRoleCriteria();

        assertThat(productRoleCriteria).hasToString("ProductRoleCriteria{}");
    }

    private static void setAllFilters(ProductRoleCriteria productRoleCriteria) {
        productRoleCriteria.id();
        productRoleCriteria.productId();
        productRoleCriteria.partyId();
        productRoleCriteria.roleTypeId();
        productRoleCriteria.distinct();
    }

    private static Condition<ProductRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductRoleCriteria> copyFiltersAre(ProductRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
