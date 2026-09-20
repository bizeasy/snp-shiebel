package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityContactMechCriteriaTest {

    @Test
    void newFacilityContactMechCriteriaHasAllFiltersNullTest() {
        var facilityContactMechCriteria = new FacilityContactMechCriteria();
        assertThat(facilityContactMechCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityContactMechCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityContactMechCriteria = new FacilityContactMechCriteria();

        setAllFilters(facilityContactMechCriteria);

        assertThat(facilityContactMechCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityContactMechCriteriaCopyCreatesNullFilterTest() {
        var facilityContactMechCriteria = new FacilityContactMechCriteria();
        var copy = facilityContactMechCriteria.copy();

        assertThat(facilityContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityContactMechCriteria)
        );
    }

    @Test
    void facilityContactMechCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityContactMechCriteria = new FacilityContactMechCriteria();
        setAllFilters(facilityContactMechCriteria);

        var copy = facilityContactMechCriteria.copy();

        assertThat(facilityContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityContactMechCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityContactMechCriteria = new FacilityContactMechCriteria();

        assertThat(facilityContactMechCriteria).hasToString("FacilityContactMechCriteria{}");
    }

    private static void setAllFilters(FacilityContactMechCriteria facilityContactMechCriteria) {
        facilityContactMechCriteria.id();
        facilityContactMechCriteria.fromDate();
        facilityContactMechCriteria.thruDate();
        facilityContactMechCriteria.facilityId();
        facilityContactMechCriteria.contactMechId();
        facilityContactMechCriteria.contactMechPurposeId();
        facilityContactMechCriteria.distinct();
    }

    private static Condition<FacilityContactMechCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityContactMechCriteria> copyFiltersAre(
        FacilityContactMechCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId(), copy.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
