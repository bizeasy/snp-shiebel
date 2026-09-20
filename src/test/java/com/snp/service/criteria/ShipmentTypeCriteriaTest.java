package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentTypeCriteriaTest {

    @Test
    void newShipmentTypeCriteriaHasAllFiltersNullTest() {
        var shipmentTypeCriteria = new ShipmentTypeCriteria();
        assertThat(shipmentTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shipmentTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentTypeCriteria = new ShipmentTypeCriteria();

        setAllFilters(shipmentTypeCriteria);

        assertThat(shipmentTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shipmentTypeCriteriaCopyCreatesNullFilterTest() {
        var shipmentTypeCriteria = new ShipmentTypeCriteria();
        var copy = shipmentTypeCriteria.copy();

        assertThat(shipmentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentTypeCriteria)
        );
    }

    @Test
    void shipmentTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentTypeCriteria = new ShipmentTypeCriteria();
        setAllFilters(shipmentTypeCriteria);

        var copy = shipmentTypeCriteria.copy();

        assertThat(shipmentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shipmentTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentTypeCriteria = new ShipmentTypeCriteria();

        assertThat(shipmentTypeCriteria).hasToString("ShipmentTypeCriteria{}");
    }

    private static void setAllFilters(ShipmentTypeCriteria shipmentTypeCriteria) {
        shipmentTypeCriteria.id();
        shipmentTypeCriteria.name();
        shipmentTypeCriteria.description();
        shipmentTypeCriteria.distinct();
    }

    private static Condition<ShipmentTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentTypeCriteria> copyFiltersAre(
        ShipmentTypeCriteria copy,
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
