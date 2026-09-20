package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShoppingListItemCriteriaTest {

    @Test
    void newShoppingListItemCriteriaHasAllFiltersNullTest() {
        var shoppingListItemCriteria = new ShoppingListItemCriteria();
        assertThat(shoppingListItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shoppingListItemCriteriaFluentMethodsCreatesFiltersTest() {
        var shoppingListItemCriteria = new ShoppingListItemCriteria();

        setAllFilters(shoppingListItemCriteria);

        assertThat(shoppingListItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shoppingListItemCriteriaCopyCreatesNullFilterTest() {
        var shoppingListItemCriteria = new ShoppingListItemCriteria();
        var copy = shoppingListItemCriteria.copy();

        assertThat(shoppingListItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListItemCriteria)
        );
    }

    @Test
    void shoppingListItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shoppingListItemCriteria = new ShoppingListItemCriteria();
        setAllFilters(shoppingListItemCriteria);

        var copy = shoppingListItemCriteria.copy();

        assertThat(shoppingListItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shoppingListItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shoppingListItemCriteria = new ShoppingListItemCriteria();

        assertThat(shoppingListItemCriteria).hasToString("ShoppingListItemCriteria{}");
    }

    private static void setAllFilters(ShoppingListItemCriteria shoppingListItemCriteria) {
        shoppingListItemCriteria.id();
        shoppingListItemCriteria.sequenceNo();
        shoppingListItemCriteria.quantity();
        shoppingListItemCriteria.requiredByDate();
        shoppingListItemCriteria.selectedAmount();
        shoppingListItemCriteria.maxAmount();
        shoppingListItemCriteria.description();
        shoppingListItemCriteria.story();
        shoppingListItemCriteria.shoppingListId();
        shoppingListItemCriteria.productId();
        shoppingListItemCriteria.statusId();
        shoppingListItemCriteria.distinct();
    }

    private static Condition<ShoppingListItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getRequiredByDate()) &&
                condition.apply(criteria.getSelectedAmount()) &&
                condition.apply(criteria.getMaxAmount()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStory()) &&
                condition.apply(criteria.getShoppingListId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShoppingListItemCriteria> copyFiltersAre(
        ShoppingListItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getRequiredByDate(), copy.getRequiredByDate()) &&
                condition.apply(criteria.getSelectedAmount(), copy.getSelectedAmount()) &&
                condition.apply(criteria.getMaxAmount(), copy.getMaxAmount()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStory(), copy.getStory()) &&
                condition.apply(criteria.getShoppingListId(), copy.getShoppingListId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
