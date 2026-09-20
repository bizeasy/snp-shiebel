package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityGroupMemberCriteriaTest {

    @Test
    void newFacilityGroupMemberCriteriaHasAllFiltersNullTest() {
        var facilityGroupMemberCriteria = new FacilityGroupMemberCriteria();
        assertThat(facilityGroupMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityGroupMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityGroupMemberCriteria = new FacilityGroupMemberCriteria();

        setAllFilters(facilityGroupMemberCriteria);

        assertThat(facilityGroupMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityGroupMemberCriteriaCopyCreatesNullFilterTest() {
        var facilityGroupMemberCriteria = new FacilityGroupMemberCriteria();
        var copy = facilityGroupMemberCriteria.copy();

        assertThat(facilityGroupMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityGroupMemberCriteria)
        );
    }

    @Test
    void facilityGroupMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityGroupMemberCriteria = new FacilityGroupMemberCriteria();
        setAllFilters(facilityGroupMemberCriteria);

        var copy = facilityGroupMemberCriteria.copy();

        assertThat(facilityGroupMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityGroupMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityGroupMemberCriteria = new FacilityGroupMemberCriteria();

        assertThat(facilityGroupMemberCriteria).hasToString("FacilityGroupMemberCriteria{}");
    }

    private static void setAllFilters(FacilityGroupMemberCriteria facilityGroupMemberCriteria) {
        facilityGroupMemberCriteria.id();
        facilityGroupMemberCriteria.fromDate();
        facilityGroupMemberCriteria.thruDate();
        facilityGroupMemberCriteria.sequenceNo();
        facilityGroupMemberCriteria.facilityId();
        facilityGroupMemberCriteria.facilityGroupId();
        facilityGroupMemberCriteria.distinct();
    }

    private static Condition<FacilityGroupMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getFacilityGroupId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityGroupMemberCriteria> copyFiltersAre(
        FacilityGroupMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getFacilityGroupId(), copy.getFacilityGroupId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
