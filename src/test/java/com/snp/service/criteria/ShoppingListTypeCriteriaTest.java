package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShoppingListTypeCriteriaTest {

    @Test
    void newShoppingListTypeCriteriaHasAllFiltersNullTest() {
        var shoppingListTypeCriteria = new ShoppingListTypeCriteria();
        assertThat(shoppingListTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shoppingListTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var shoppingListTypeCriteria = new ShoppingListTypeCriteria();

        setAllFilters(shoppingListTypeCriteria);

        assertThat(shoppingListTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shoppingListTypeCriteriaCopyCreatesNullFilterTest() {
        var shoppingListTypeCriteria = new ShoppingListTypeCriteria();
        var copy = shoppingListTypeCriteria.copy();

        assertThat(shoppingListTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListTypeCriteria)
        );
    }

    @Test
    void shoppingListTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shoppingListTypeCriteria = new ShoppingListTypeCriteria();
        setAllFilters(shoppingListTypeCriteria);

        var copy = shoppingListTypeCriteria.copy();

        assertThat(shoppingListTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shoppingListTypeCriteria = new ShoppingListTypeCriteria();

        assertThat(shoppingListTypeCriteria).hasToString("ShoppingListTypeCriteria{}");
    }

    private static void setAllFilters(ShoppingListTypeCriteria shoppingListTypeCriteria) {
        shoppingListTypeCriteria.id();
        shoppingListTypeCriteria.name();
        shoppingListTypeCriteria.description();
        shoppingListTypeCriteria.distinct();
    }

    private static Condition<ShoppingListTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShoppingListTypeCriteria> copyFiltersAre(
        ShoppingListTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
