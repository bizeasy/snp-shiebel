package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityGroupTypeCriteriaTest {

    @Test
    void newFacilityGroupTypeCriteriaHasAllFiltersNullTest() {
        var facilityGroupTypeCriteria = new FacilityGroupTypeCriteria();
        assertThat(facilityGroupTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityGroupTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityGroupTypeCriteria = new FacilityGroupTypeCriteria();

        setAllFilters(facilityGroupTypeCriteria);

        assertThat(facilityGroupTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityGroupTypeCriteriaCopyCreatesNullFilterTest() {
        var facilityGroupTypeCriteria = new FacilityGroupTypeCriteria();
        var copy = facilityGroupTypeCriteria.copy();

        assertThat(facilityGroupTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityGroupTypeCriteria)
        );
    }

    @Test
    void facilityGroupTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityGroupTypeCriteria = new FacilityGroupTypeCriteria();
        setAllFilters(facilityGroupTypeCriteria);

        var copy = facilityGroupTypeCriteria.copy();

        assertThat(facilityGroupTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityGroupTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityGroupTypeCriteria = new FacilityGroupTypeCriteria();

        assertThat(facilityGroupTypeCriteria).hasToString("FacilityGroupTypeCriteria{}");
    }

    private static void setAllFilters(FacilityGroupTypeCriteria facilityGroupTypeCriteria) {
        facilityGroupTypeCriteria.id();
        facilityGroupTypeCriteria.name();
        facilityGroupTypeCriteria.description();
        facilityGroupTypeCriteria.distinct();
    }

    private static Condition<FacilityGroupTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityGroupTypeCriteria> copyFiltersAre(
        FacilityGroupTypeCriteria copy,
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
