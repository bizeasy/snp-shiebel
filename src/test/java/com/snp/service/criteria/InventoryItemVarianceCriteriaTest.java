package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemVarianceCriteriaTest {

    @Test
    void newInventoryItemVarianceCriteriaHasAllFiltersNullTest() {
        var inventoryItemVarianceCriteria = new InventoryItemVarianceCriteria();
        assertThat(inventoryItemVarianceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemVarianceCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemVarianceCriteria = new InventoryItemVarianceCriteria();

        setAllFilters(inventoryItemVarianceCriteria);

        assertThat(inventoryItemVarianceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemVarianceCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemVarianceCriteria = new InventoryItemVarianceCriteria();
        var copy = inventoryItemVarianceCriteria.copy();

        assertThat(inventoryItemVarianceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemVarianceCriteria)
        );
    }

    @Test
    void inventoryItemVarianceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemVarianceCriteria = new InventoryItemVarianceCriteria();
        setAllFilters(inventoryItemVarianceCriteria);

        var copy = inventoryItemVarianceCriteria.copy();

        assertThat(inventoryItemVarianceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemVarianceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemVarianceCriteria = new InventoryItemVarianceCriteria();

        assertThat(inventoryItemVarianceCriteria).hasToString("InventoryItemVarianceCriteria{}");
    }

    private static void setAllFilters(InventoryItemVarianceCriteria inventoryItemVarianceCriteria) {
        inventoryItemVarianceCriteria.id();
        inventoryItemVarianceCriteria.varianceReason();
        inventoryItemVarianceCriteria.atpVar();
        inventoryItemVarianceCriteria.qohVar();
        inventoryItemVarianceCriteria.comments();
        inventoryItemVarianceCriteria.inventoryItemId();
        inventoryItemVarianceCriteria.distinct();
    }

    private static Condition<InventoryItemVarianceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVarianceReason()) &&
                condition.apply(criteria.getAtpVar()) &&
                condition.apply(criteria.getQohVar()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemVarianceCriteria> copyFiltersAre(
        InventoryItemVarianceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVarianceReason(), copy.getVarianceReason()) &&
                condition.apply(criteria.getAtpVar(), copy.getAtpVar()) &&
                condition.apply(criteria.getQohVar(), copy.getQohVar()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
