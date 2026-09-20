package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShoppingListCriteriaTest {

    @Test
    void newShoppingListCriteriaHasAllFiltersNullTest() {
        var shoppingListCriteria = new ShoppingListCriteria();
        assertThat(shoppingListCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shoppingListCriteriaFluentMethodsCreatesFiltersTest() {
        var shoppingListCriteria = new ShoppingListCriteria();

        setAllFilters(shoppingListCriteria);

        assertThat(shoppingListCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shoppingListCriteriaCopyCreatesNullFilterTest() {
        var shoppingListCriteria = new ShoppingListCriteria();
        var copy = shoppingListCriteria.copy();

        assertThat(shoppingListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListCriteria)
        );
    }

    @Test
    void shoppingListCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shoppingListCriteria = new ShoppingListCriteria();
        setAllFilters(shoppingListCriteria);

        var copy = shoppingListCriteria.copy();

        assertThat(shoppingListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shoppingListCriteria = new ShoppingListCriteria();

        assertThat(shoppingListCriteria).hasToString("ShoppingListCriteria{}");
    }

    private static void setAllFilters(ShoppingListCriteria shoppingListCriteria) {
        shoppingListCriteria.id();
        shoppingListCriteria.name();
        shoppingListCriteria.isActive();
        shoppingListCriteria.shoppingListTypeId();
        shoppingListCriteria.partyId();
        shoppingListCriteria.statusId();
        shoppingListCriteria.distinct();
    }

    private static Condition<ShoppingListCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getShoppingListTypeId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShoppingListCriteria> copyFiltersAre(
        ShoppingListCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getShoppingListTypeId(), copy.getShoppingListTypeId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
