package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentCriteriaTest {

    @Test
    void newShipmentCriteriaHasAllFiltersNullTest() {
        var shipmentCriteria = new ShipmentCriteria();
        assertThat(shipmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentCriteria = new ShipmentCriteria();

        setAllFilters(shipmentCriteria);

        assertThat(shipmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentCriteriaCopyCreatesNullFilterTest() {
        var shipmentCriteria = new ShipmentCriteria();
        var copy = shipmentCriteria.copy();

        assertThat(shipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentCriteria)
        );
    }

    @Test
    void shipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentCriteria = new ShipmentCriteria();
        setAllFilters(shipmentCriteria);

        var copy = shipmentCriteria.copy();

        assertThat(shipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentCriteria = new ShipmentCriteria();

        assertThat(shipmentCriteria).hasToString("ShipmentCriteria{}");
    }

    private static void setAllFilters(ShipmentCriteria shipmentCriteria) {
        shipmentCriteria.id();
        shipmentCriteria.estimatedShipDate();
        shipmentCriteria.estimatedReadyDate();
        shipmentCriteria.estimatedArrivalDate();
        shipmentCriteria.estimatedShipCost();
        shipmentCriteria.shipmentTypeId();
        shipmentCriteria.shipmentMethodTypeId();
        shipmentCriteria.statusId();
        shipmentCriteria.orderId();
        shipmentCriteria.orderItemId();
        shipmentCriteria.originFacilityId();
        shipmentCriteria.destinationFacilityId();
        shipmentCriteria.fromPartyId();
        shipmentCriteria.toPartyId();
        shipmentCriteria.carrierPartyId();
        shipmentCriteria.contactMechId();
        shipmentCriteria.distinct();
    }

    private static Condition<ShipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEstimatedShipDate()) &&
                condition.apply(criteria.getEstimatedReadyDate()) &&
                condition.apply(criteria.getEstimatedArrivalDate()) &&
                condition.apply(criteria.getEstimatedShipCost()) &&
                condition.apply(criteria.getShipmentTypeId()) &&
                condition.apply(criteria.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getOriginFacilityId()) &&
                condition.apply(criteria.getDestinationFacilityId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId()) &&
                condition.apply(criteria.getCarrierPartyId()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentCriteria> copyFiltersAre(ShipmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEstimatedShipDate(), copy.getEstimatedShipDate()) &&
                condition.apply(criteria.getEstimatedReadyDate(), copy.getEstimatedReadyDate()) &&
                condition.apply(criteria.getEstimatedArrivalDate(), copy.getEstimatedArrivalDate()) &&
                condition.apply(criteria.getEstimatedShipCost(), copy.getEstimatedShipCost()) &&
                condition.apply(criteria.getShipmentTypeId(), copy.getShipmentTypeId()) &&
                condition.apply(criteria.getShipmentMethodTypeId(), copy.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getOriginFacilityId(), copy.getOriginFacilityId()) &&
                condition.apply(criteria.getDestinationFacilityId(), copy.getDestinationFacilityId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId(), copy.getToPartyId()) &&
                condition.apply(criteria.getCarrierPartyId(), copy.getCarrierPartyId()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
