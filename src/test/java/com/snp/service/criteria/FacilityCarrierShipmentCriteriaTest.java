package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityCarrierShipmentCriteriaTest {

    @Test
    void newFacilityCarrierShipmentCriteriaHasAllFiltersNullTest() {
        var facilityCarrierShipmentCriteria = new FacilityCarrierShipmentCriteria();
        assertThat(facilityCarrierShipmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityCarrierShipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityCarrierShipmentCriteria = new FacilityCarrierShipmentCriteria();

        setAllFilters(facilityCarrierShipmentCriteria);

        assertThat(facilityCarrierShipmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityCarrierShipmentCriteriaCopyCreatesNullFilterTest() {
        var facilityCarrierShipmentCriteria = new FacilityCarrierShipmentCriteria();
        var copy = facilityCarrierShipmentCriteria.copy();

        assertThat(facilityCarrierShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCarrierShipmentCriteria)
        );
    }

    @Test
    void facilityCarrierShipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityCarrierShipmentCriteria = new FacilityCarrierShipmentCriteria();
        setAllFilters(facilityCarrierShipmentCriteria);

        var copy = facilityCarrierShipmentCriteria.copy();

        assertThat(facilityCarrierShipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCarrierShipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityCarrierShipmentCriteria = new FacilityCarrierShipmentCriteria();

        assertThat(facilityCarrierShipmentCriteria).hasToString("FacilityCarrierShipmentCriteria{}");
    }

    private static void setAllFilters(FacilityCarrierShipmentCriteria facilityCarrierShipmentCriteria) {
        facilityCarrierShipmentCriteria.id();
        facilityCarrierShipmentCriteria.facilityId();
        facilityCarrierShipmentCriteria.partyId();
        facilityCarrierShipmentCriteria.roleTypeId();
        facilityCarrierShipmentCriteria.shipmentMethodTypeId();
        facilityCarrierShipmentCriteria.distinct();
    }

    private static Condition<FacilityCarrierShipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityCarrierShipmentCriteria> copyFiltersAre(
        FacilityCarrierShipmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getShipmentMethodTypeId(), copy.getShipmentMethodTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
