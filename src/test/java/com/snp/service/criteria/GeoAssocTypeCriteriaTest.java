package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GeoAssocTypeCriteriaTest {

    @Test
    void newGeoAssocTypeCriteriaHasAllFiltersNullTest() {
        var geoAssocTypeCriteria = new GeoAssocTypeCriteria();
        assertThat(geoAssocTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void geoAssocTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var geoAssocTypeCriteria = new GeoAssocTypeCriteria();

        setAllFilters(geoAssocTypeCriteria);

        assertThat(geoAssocTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void geoAssocTypeCriteriaCopyCreatesNullFilterTest() {
        var geoAssocTypeCriteria = new GeoAssocTypeCriteria();
        var copy = geoAssocTypeCriteria.copy();

        assertThat(geoAssocTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(geoAssocTypeCriteria)
        );
    }

    @Test
    void geoAssocTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var geoAssocTypeCriteria = new GeoAssocTypeCriteria();
        setAllFilters(geoAssocTypeCriteria);

        var copy = geoAssocTypeCriteria.copy();

        assertThat(geoAssocTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(geoAssocTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var geoAssocTypeCriteria = new GeoAssocTypeCriteria();

        assertThat(geoAssocTypeCriteria).hasToString("GeoAssocTypeCriteria{}");
    }

    private static void setAllFilters(GeoAssocTypeCriteria geoAssocTypeCriteria) {
        geoAssocTypeCriteria.id();
        geoAssocTypeCriteria.name();
        geoAssocTypeCriteria.distinct();
    }

    private static Condition<GeoAssocTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GeoAssocTypeCriteria> copyFiltersAre(
        GeoAssocTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
