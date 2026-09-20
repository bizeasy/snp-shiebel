package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemTypeCriteriaTest {

    @Test
    void newInventoryItemTypeCriteriaHasAllFiltersNullTest() {
        var inventoryItemTypeCriteria = new InventoryItemTypeCriteria();
        assertThat(inventoryItemTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemTypeCriteria = new InventoryItemTypeCriteria();

        setAllFilters(inventoryItemTypeCriteria);

        assertThat(inventoryItemTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemTypeCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemTypeCriteria = new InventoryItemTypeCriteria();
        var copy = inventoryItemTypeCriteria.copy();

        assertThat(inventoryItemTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemTypeCriteria)
        );
    }

    @Test
    void inventoryItemTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemTypeCriteria = new InventoryItemTypeCriteria();
        setAllFilters(inventoryItemTypeCriteria);

        var copy = inventoryItemTypeCriteria.copy();

        assertThat(inventoryItemTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemTypeCriteria = new InventoryItemTypeCriteria();

        assertThat(inventoryItemTypeCriteria).hasToString("InventoryItemTypeCriteria{}");
    }

    private static void setAllFilters(InventoryItemTypeCriteria inventoryItemTypeCriteria) {
        inventoryItemTypeCriteria.id();
        inventoryItemTypeCriteria.name();
        inventoryItemTypeCriteria.description();
        inventoryItemTypeCriteria.distinct();
    }

    private static Condition<InventoryItemTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemTypeCriteria> copyFiltersAre(
        InventoryItemTypeCriteria copy,
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
