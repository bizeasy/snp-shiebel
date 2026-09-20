package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PickListItemCriteriaTest {

    @Test
    void newPickListItemCriteriaHasAllFiltersNullTest() {
        var pickListItemCriteria = new PickListItemCriteria();
        assertThat(pickListItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pickListItemCriteriaFluentMethodsCreatesFiltersTest() {
        var pickListItemCriteria = new PickListItemCriteria();

        setAllFilters(pickListItemCriteria);

        assertThat(pickListItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pickListItemCriteriaCopyCreatesNullFilterTest() {
        var pickListItemCriteria = new PickListItemCriteria();
        var copy = pickListItemCriteria.copy();

        assertThat(pickListItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pickListItemCriteria)
        );
    }

    @Test
    void pickListItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pickListItemCriteria = new PickListItemCriteria();
        setAllFilters(pickListItemCriteria);

        var copy = pickListItemCriteria.copy();

        assertThat(pickListItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pickListItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pickListItemCriteria = new PickListItemCriteria();

        assertThat(pickListItemCriteria).hasToString("PickListItemCriteria{}");
    }

    private static void setAllFilters(PickListItemCriteria pickListItemCriteria) {
        pickListItemCriteria.id();
        pickListItemCriteria.quantity();
        pickListItemCriteria.pickListId();
        pickListItemCriteria.orderId();
        pickListItemCriteria.orderItemId();
        pickListItemCriteria.inventoryItemId();
        pickListItemCriteria.productId();
        pickListItemCriteria.facilityLocationId();
        pickListItemCriteria.statusId();
        pickListItemCriteria.distinct();
    }

    private static Condition<PickListItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getPickListId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getFacilityLocationId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PickListItemCriteria> copyFiltersAre(
        PickListItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getPickListId(), copy.getPickListId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getFacilityLocationId(), copy.getFacilityLocationId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
