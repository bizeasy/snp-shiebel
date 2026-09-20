package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GeoTypeCriteriaTest {

    @Test
    void newGeoTypeCriteriaHasAllFiltersNullTest() {
        var geoTypeCriteria = new GeoTypeCriteria();
        assertThat(geoTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void geoTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var geoTypeCriteria = new GeoTypeCriteria();

        setAllFilters(geoTypeCriteria);

        assertThat(geoTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void geoTypeCriteriaCopyCreatesNullFilterTest() {
        var geoTypeCriteria = new GeoTypeCriteria();
        var copy = geoTypeCriteria.copy();

        assertThat(geoTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(geoTypeCriteria)
        );
    }

    @Test
    void geoTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var geoTypeCriteria = new GeoTypeCriteria();
        setAllFilters(geoTypeCriteria);

        var copy = geoTypeCriteria.copy();

        assertThat(geoTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(geoTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var geoTypeCriteria = new GeoTypeCriteria();

        assertThat(geoTypeCriteria).hasToString("GeoTypeCriteria{}");
    }

    private static void setAllFilters(GeoTypeCriteria geoTypeCriteria) {
        geoTypeCriteria.id();
        geoTypeCriteria.name();
        geoTypeCriteria.distinct();
    }

    private static Condition<GeoTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GeoTypeCriteria> copyFiltersAre(GeoTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
