package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GeoAssocCriteriaTest {

    @Test
    void newGeoAssocCriteriaHasAllFiltersNullTest() {
        var geoAssocCriteria = new GeoAssocCriteria();
        assertThat(geoAssocCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void geoAssocCriteriaFluentMethodsCreatesFiltersTest() {
        var geoAssocCriteria = new GeoAssocCriteria();

        setAllFilters(geoAssocCriteria);

        assertThat(geoAssocCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void geoAssocCriteriaCopyCreatesNullFilterTest() {
        var geoAssocCriteria = new GeoAssocCriteria();
        var copy = geoAssocCriteria.copy();

        assertThat(geoAssocCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(geoAssocCriteria)
        );
    }

    @Test
    void geoAssocCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var geoAssocCriteria = new GeoAssocCriteria();
        setAllFilters(geoAssocCriteria);

        var copy = geoAssocCriteria.copy();

        assertThat(geoAssocCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(geoAssocCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var geoAssocCriteria = new GeoAssocCriteria();

        assertThat(geoAssocCriteria).hasToString("GeoAssocCriteria{}");
    }

    private static void setAllFilters(GeoAssocCriteria geoAssocCriteria) {
        geoAssocCriteria.id();
        geoAssocCriteria.geoId();
        geoAssocCriteria.geoToId();
        geoAssocCriteria.geoAssocTypeId();
        geoAssocCriteria.distinct();
    }

    private static Condition<GeoAssocCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getGeoId()) &&
                condition.apply(criteria.getGeoToId()) &&
                condition.apply(criteria.getGeoAssocTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GeoAssocCriteria> copyFiltersAre(GeoAssocCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getGeoId(), copy.getGeoId()) &&
                condition.apply(criteria.getGeoToId(), copy.getGeoToId()) &&
                condition.apply(criteria.getGeoAssocTypeId(), copy.getGeoAssocTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
