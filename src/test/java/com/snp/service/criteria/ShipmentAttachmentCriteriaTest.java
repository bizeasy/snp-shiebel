package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentAttachmentCriteriaTest {

    @Test
    void newShipmentAttachmentCriteriaHasAllFiltersNullTest() {
        var shipmentAttachmentCriteria = new ShipmentAttachmentCriteria();
        assertThat(shipmentAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentAttachmentCriteria = new ShipmentAttachmentCriteria();

        setAllFilters(shipmentAttachmentCriteria);

        assertThat(shipmentAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentAttachmentCriteriaCopyCreatesNullFilterTest() {
        var shipmentAttachmentCriteria = new ShipmentAttachmentCriteria();
        var copy = shipmentAttachmentCriteria.copy();

        assertThat(shipmentAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentAttachmentCriteria)
        );
    }

    @Test
    void shipmentAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentAttachmentCriteria = new ShipmentAttachmentCriteria();
        setAllFilters(shipmentAttachmentCriteria);

        var copy = shipmentAttachmentCriteria.copy();

        assertThat(shipmentAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentAttachmentCriteria = new ShipmentAttachmentCriteria();

        assertThat(shipmentAttachmentCriteria).hasToString("ShipmentAttachmentCriteria{}");
    }

    private static void setAllFilters(ShipmentAttachmentCriteria shipmentAttachmentCriteria) {
        shipmentAttachmentCriteria.id();
        shipmentAttachmentCriteria.name();
        shipmentAttachmentCriteria.shipmentId();
        shipmentAttachmentCriteria.attachmentId();
        shipmentAttachmentCriteria.distinct();
    }

    private static Condition<ShipmentAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentAttachmentCriteria> copyFiltersAre(
        ShipmentAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
