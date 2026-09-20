package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ItemIssuanceCriteriaTest {

    @Test
    void newItemIssuanceCriteriaHasAllFiltersNullTest() {
        var itemIssuanceCriteria = new ItemIssuanceCriteria();
        assertThat(itemIssuanceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void itemIssuanceCriteriaFluentMethodsCreatesFiltersTest() {
        var itemIssuanceCriteria = new ItemIssuanceCriteria();

        setAllFilters(itemIssuanceCriteria);

        assertThat(itemIssuanceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void itemIssuanceCriteriaCopyCreatesNullFilterTest() {
        var itemIssuanceCriteria = new ItemIssuanceCriteria();
        var copy = itemIssuanceCriteria.copy();

        assertThat(itemIssuanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(itemIssuanceCriteria)
        );
    }

    @Test
    void itemIssuanceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var itemIssuanceCriteria = new ItemIssuanceCriteria();
        setAllFilters(itemIssuanceCriteria);

        var copy = itemIssuanceCriteria.copy();

        assertThat(itemIssuanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(itemIssuanceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var itemIssuanceCriteria = new ItemIssuanceCriteria();

        assertThat(itemIssuanceCriteria).hasToString("ItemIssuanceCriteria{}");
    }

    private static void setAllFilters(ItemIssuanceCriteria itemIssuanceCriteria) {
        itemIssuanceCriteria.id();
        itemIssuanceCriteria.message();
        itemIssuanceCriteria.issuedDate();
        itemIssuanceCriteria.issuedBy();
        itemIssuanceCriteria.quantity();
        itemIssuanceCriteria.cancelQuantity();
        itemIssuanceCriteria.inventoryItemId();
        itemIssuanceCriteria.orderId();
        itemIssuanceCriteria.orderItemId();
        itemIssuanceCriteria.shipmentId();
        itemIssuanceCriteria.shipmentItemId();
        itemIssuanceCriteria.issuedByPartyId();
        itemIssuanceCriteria.distinct();
    }

    private static Condition<ItemIssuanceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getIssuedDate()) &&
                condition.apply(criteria.getIssuedBy()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getCancelQuantity()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId()) &&
                condition.apply(criteria.getIssuedByPartyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ItemIssuanceCriteria> copyFiltersAre(
        ItemIssuanceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getIssuedDate(), copy.getIssuedDate()) &&
                condition.apply(criteria.getIssuedBy(), copy.getIssuedBy()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getCancelQuantity(), copy.getCancelQuantity()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId(), copy.getShipmentItemId()) &&
                condition.apply(criteria.getIssuedByPartyId(), copy.getIssuedByPartyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
