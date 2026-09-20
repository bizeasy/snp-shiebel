package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentItemCriteriaTest {

    @Test
    void newShipmentItemCriteriaHasAllFiltersNullTest() {
        var shipmentItemCriteria = new ShipmentItemCriteria();
        assertThat(shipmentItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentItemCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentItemCriteria = new ShipmentItemCriteria();

        setAllFilters(shipmentItemCriteria);

        assertThat(shipmentItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentItemCriteriaCopyCreatesNullFilterTest() {
        var shipmentItemCriteria = new ShipmentItemCriteria();
        var copy = shipmentItemCriteria.copy();

        assertThat(shipmentItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentItemCriteria)
        );
    }

    @Test
    void shipmentItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentItemCriteria = new ShipmentItemCriteria();
        setAllFilters(shipmentItemCriteria);

        var copy = shipmentItemCriteria.copy();

        assertThat(shipmentItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentItemCriteria = new ShipmentItemCriteria();

        assertThat(shipmentItemCriteria).hasToString("ShipmentItemCriteria{}");
    }

    private static void setAllFilters(ShipmentItemCriteria shipmentItemCriteria) {
        shipmentItemCriteria.id();
        shipmentItemCriteria.sequenceNo();
        shipmentItemCriteria.quantity();
        shipmentItemCriteria.message();
        shipmentItemCriteria.shipmentId();
        shipmentItemCriteria.productId();
        shipmentItemCriteria.distinct();
    }

    private static Condition<ShipmentItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentItemCriteria> copyFiltersAre(
        ShipmentItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
