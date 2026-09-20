package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CarrierShipmentMethodCriteriaTest {

    @Test
    void newCarrierShipmentMethodCriteriaHasAllFiltersNullTest() {
        var carrierShipmentMethodCriteria = new CarrierShipmentMethodCriteria();
        assertThat(carrierShipmentMethodCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void carrierShipmentMethodCriteriaFluentMethodsCreatesFiltersTest() {
        var carrierShipmentMethodCriteria = new CarrierShipmentMethodCriteria();

        setAllFilters(carrierShipmentMethodCriteria);

        assertThat(carrierShipmentMethodCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void carrierShipmentMethodCriteriaCopyCreatesNullFilterTest() {
        var carrierShipmentMethodCriteria = new CarrierShipmentMethodCriteria();
        var copy = carrierShipmentMethodCriteria.copy();

        assertThat(carrierShipmentMethodCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(carrierShipmentMethodCriteria)
        );
    }

    @Test
    void carrierShipmentMethodCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var carrierShipmentMethodCriteria = new CarrierShipmentMethodCriteria();
        setAllFilters(carrierShipmentMethodCriteria);

        var copy = carrierShipmentMethodCriteria.copy();

        assertThat(carrierShipmentMethodCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(carrierShipmentMethodCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var carrierShipmentMethodCriteria = new CarrierShipmentMethodCriteria();

        assertThat(carrierShipmentMethodCriteria).hasToString("CarrierShipmentMethodCriteria{}");
    }

    private static void setAllFilters(CarrierShipmentMethodCriteria carrierShipmentMethodCriteria) {
        carrierShipmentMethodCriteria.id();
        carrierShipmentMethodCriteria.sequenceNo();
        carrierShipmentMethodCriteria.carrierServiceCode();
        carrierShipmentMethodCriteria.shipmentMethodTypeId();
        carrierShipmentMethodCriteria.partyId();
        carrierShipmentMethodCriteria.roleTypeId();
        carrierShipmentMethodCriteria.distinct();
    }

    private static Condition<CarrierShipmentMethodCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getCarrierServiceCode()) &&
                condition.apply(criteria.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CarrierShipmentMethodCriteria> copyFiltersAre(
        CarrierShipmentMethodCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getCarrierServiceCode(), copy.getCarrierServiceCode()) &&
                condition.apply(criteria.getShipmentMethodTypeId(), copy.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
