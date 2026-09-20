package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityLocationCriteriaTest {

    @Test
    void newFacilityLocationCriteriaHasAllFiltersNullTest() {
        var facilityLocationCriteria = new FacilityLocationCriteria();
        assertThat(facilityLocationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityLocationCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityLocationCriteria = new FacilityLocationCriteria();

        setAllFilters(facilityLocationCriteria);

        assertThat(facilityLocationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityLocationCriteriaCopyCreatesNullFilterTest() {
        var facilityLocationCriteria = new FacilityLocationCriteria();
        var copy = facilityLocationCriteria.copy();

        assertThat(facilityLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityLocationCriteria)
        );
    }

    @Test
    void facilityLocationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityLocationCriteria = new FacilityLocationCriteria();
        setAllFilters(facilityLocationCriteria);

        var copy = facilityLocationCriteria.copy();

        assertThat(facilityLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityLocationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityLocationCriteria = new FacilityLocationCriteria();

        assertThat(facilityLocationCriteria).hasToString("FacilityLocationCriteria{}");
    }

    private static void setAllFilters(FacilityLocationCriteria facilityLocationCriteria) {
        facilityLocationCriteria.id();
        facilityLocationCriteria.locationSeqId();
        facilityLocationCriteria.areaId();
        facilityLocationCriteria.aisleId();
        facilityLocationCriteria.sectionId();
        facilityLocationCriteria.levelId();
        facilityLocationCriteria.positionId();
        facilityLocationCriteria.facilityId();
        facilityLocationCriteria.locationTypeId();
        facilityLocationCriteria.distinct();
    }

    private static Condition<FacilityLocationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLocationSeqId()) &&
                condition.apply(criteria.getAreaId()) &&
                condition.apply(criteria.getAisleId()) &&
                condition.apply(criteria.getSectionId()) &&
                condition.apply(criteria.getLevelId()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getLocationTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityLocationCriteria> copyFiltersAre(
        FacilityLocationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLocationSeqId(), copy.getLocationSeqId()) &&
                condition.apply(criteria.getAreaId(), copy.getAreaId()) &&
                condition.apply(criteria.getAisleId(), copy.getAisleId()) &&
                condition.apply(criteria.getSectionId(), copy.getSectionId()) &&
                condition.apply(criteria.getLevelId(), copy.getLevelId()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getLocationTypeId(), copy.getLocationTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
