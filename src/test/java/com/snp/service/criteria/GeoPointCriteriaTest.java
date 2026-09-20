package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GeoPointCriteriaTest {

    @Test
    void newGeoPointCriteriaHasAllFiltersNullTest() {
        var geoPointCriteria = new GeoPointCriteria();
        assertThat(geoPointCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void geoPointCriteriaFluentMethodsCreatesFiltersTest() {
        var geoPointCriteria = new GeoPointCriteria();

        setAllFilters(geoPointCriteria);

        assertThat(geoPointCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void geoPointCriteriaCopyCreatesNullFilterTest() {
        var geoPointCriteria = new GeoPointCriteria();
        var copy = geoPointCriteria.copy();

        assertThat(geoPointCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(geoPointCriteria)
        );
    }

    @Test
    void geoPointCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var geoPointCriteria = new GeoPointCriteria();
        setAllFilters(geoPointCriteria);

        var copy = geoPointCriteria.copy();

        assertThat(geoPointCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(geoPointCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var geoPointCriteria = new GeoPointCriteria();

        assertThat(geoPointCriteria).hasToString("GeoPointCriteria{}");
    }

    private static void setAllFilters(GeoPointCriteria geoPointCriteria) {
        geoPointCriteria.id();
        geoPointCriteria.latitude();
        geoPointCriteria.longitude();
        geoPointCriteria.distinct();
    }

    private static Condition<GeoPointCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GeoPointCriteria> copyFiltersAre(GeoPointCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
