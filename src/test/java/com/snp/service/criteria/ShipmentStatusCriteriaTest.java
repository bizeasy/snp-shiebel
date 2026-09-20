package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentStatusCriteriaTest {

    @Test
    void newShipmentStatusCriteriaHasAllFiltersNullTest() {
        var shipmentStatusCriteria = new ShipmentStatusCriteria();
        assertThat(shipmentStatusCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentStatusCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentStatusCriteria = new ShipmentStatusCriteria();

        setAllFilters(shipmentStatusCriteria);

        assertThat(shipmentStatusCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentStatusCriteriaCopyCreatesNullFilterTest() {
        var shipmentStatusCriteria = new ShipmentStatusCriteria();
        var copy = shipmentStatusCriteria.copy();

        assertThat(shipmentStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentStatusCriteria)
        );
    }

    @Test
    void shipmentStatusCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentStatusCriteria = new ShipmentStatusCriteria();
        setAllFilters(shipmentStatusCriteria);

        var copy = shipmentStatusCriteria.copy();

        assertThat(shipmentStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentStatusCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentStatusCriteria = new ShipmentStatusCriteria();

        assertThat(shipmentStatusCriteria).hasToString("ShipmentStatusCriteria{}");
    }

    private static void setAllFilters(ShipmentStatusCriteria shipmentStatusCriteria) {
        shipmentStatusCriteria.id();
        shipmentStatusCriteria.statusDateTime();
        shipmentStatusCriteria.sequenceNo();
        shipmentStatusCriteria.shipmentId();
        shipmentStatusCriteria.statusId();
        shipmentStatusCriteria.distinct();
    }

    private static Condition<ShipmentStatusCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatusDateTime()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentStatusCriteria> copyFiltersAre(
        ShipmentStatusCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatusDateTime(), copy.getStatusDateTime()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
