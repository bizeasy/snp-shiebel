package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentReceiptCriteriaTest {

    @Test
    void newShipmentReceiptCriteriaHasAllFiltersNullTest() {
        var shipmentReceiptCriteria = new ShipmentReceiptCriteria();
        assertThat(shipmentReceiptCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentReceiptCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentReceiptCriteria = new ShipmentReceiptCriteria();

        setAllFilters(shipmentReceiptCriteria);

        assertThat(shipmentReceiptCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentReceiptCriteriaCopyCreatesNullFilterTest() {
        var shipmentReceiptCriteria = new ShipmentReceiptCriteria();
        var copy = shipmentReceiptCriteria.copy();

        assertThat(shipmentReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentReceiptCriteria)
        );
    }

    @Test
    void shipmentReceiptCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentReceiptCriteria = new ShipmentReceiptCriteria();
        setAllFilters(shipmentReceiptCriteria);

        var copy = shipmentReceiptCriteria.copy();

        assertThat(shipmentReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentReceiptCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentReceiptCriteria = new ShipmentReceiptCriteria();

        assertThat(shipmentReceiptCriteria).hasToString("ShipmentReceiptCriteria{}");
    }

    private static void setAllFilters(ShipmentReceiptCriteria shipmentReceiptCriteria) {
        shipmentReceiptCriteria.id();
        shipmentReceiptCriteria.sequenceNo();
        shipmentReceiptCriteria.quantity();
        shipmentReceiptCriteria.quantityAccepted();
        shipmentReceiptCriteria.quantityRejected();
        shipmentReceiptCriteria.receivedDate();
        shipmentReceiptCriteria.message();
        shipmentReceiptCriteria.shipmentId();
        shipmentReceiptCriteria.shipmentItemId();
        shipmentReceiptCriteria.orderId();
        shipmentReceiptCriteria.orderItemId();
        shipmentReceiptCriteria.inventoryItemId();
        shipmentReceiptCriteria.productId();
        shipmentReceiptCriteria.receivedById();
        shipmentReceiptCriteria.distinct();
    }

    private static Condition<ShipmentReceiptCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getQuantityAccepted()) &&
                condition.apply(criteria.getQuantityRejected()) &&
                condition.apply(criteria.getReceivedDate()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getReceivedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentReceiptCriteria> copyFiltersAre(
        ShipmentReceiptCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getQuantityAccepted(), copy.getQuantityAccepted()) &&
                condition.apply(criteria.getQuantityRejected(), copy.getQuantityRejected()) &&
                condition.apply(criteria.getReceivedDate(), copy.getReceivedDate()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId(), copy.getShipmentItemId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getReceivedById(), copy.getReceivedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
