package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityPartyCriteriaTest {

    @Test
    void newFacilityPartyCriteriaHasAllFiltersNullTest() {
        var facilityPartyCriteria = new FacilityPartyCriteria();
        assertThat(facilityPartyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityPartyCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityPartyCriteria = new FacilityPartyCriteria();

        setAllFilters(facilityPartyCriteria);

        assertThat(facilityPartyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityPartyCriteriaCopyCreatesNullFilterTest() {
        var facilityPartyCriteria = new FacilityPartyCriteria();
        var copy = facilityPartyCriteria.copy();

        assertThat(facilityPartyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityPartyCriteria)
        );
    }

    @Test
    void facilityPartyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityPartyCriteria = new FacilityPartyCriteria();
        setAllFilters(facilityPartyCriteria);

        var copy = facilityPartyCriteria.copy();

        assertThat(facilityPartyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityPartyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityPartyCriteria = new FacilityPartyCriteria();

        assertThat(facilityPartyCriteria).hasToString("FacilityPartyCriteria{}");
    }

    private static void setAllFilters(FacilityPartyCriteria facilityPartyCriteria) {
        facilityPartyCriteria.id();
        facilityPartyCriteria.fromDate();
        facilityPartyCriteria.thruDate();
        facilityPartyCriteria.facilityId();
        facilityPartyCriteria.partyId();
        facilityPartyCriteria.roleTypeId();
        facilityPartyCriteria.distinct();
    }

    private static Condition<FacilityPartyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityPartyCriteria> copyFiltersAre(
        FacilityPartyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
