package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LocationTypeCriteriaTest {

    @Test
    void newLocationTypeCriteriaHasAllFiltersNullTest() {
        var locationTypeCriteria = new LocationTypeCriteria();
        assertThat(locationTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void locationTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var locationTypeCriteria = new LocationTypeCriteria();

        setAllFilters(locationTypeCriteria);

        assertThat(locationTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void locationTypeCriteriaCopyCreatesNullFilterTest() {
        var locationTypeCriteria = new LocationTypeCriteria();
        var copy = locationTypeCriteria.copy();

        assertThat(locationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(locationTypeCriteria)
        );
    }

    @Test
    void locationTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var locationTypeCriteria = new LocationTypeCriteria();
        setAllFilters(locationTypeCriteria);

        var copy = locationTypeCriteria.copy();

        assertThat(locationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(locationTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var locationTypeCriteria = new LocationTypeCriteria();

        assertThat(locationTypeCriteria).hasToString("LocationTypeCriteria{}");
    }

    private static void setAllFilters(LocationTypeCriteria locationTypeCriteria) {
        locationTypeCriteria.id();
        locationTypeCriteria.name();
        locationTypeCriteria.description();
        locationTypeCriteria.distinct();
    }

    private static Condition<LocationTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LocationTypeCriteria> copyFiltersAre(
        LocationTypeCriteria copy,
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
