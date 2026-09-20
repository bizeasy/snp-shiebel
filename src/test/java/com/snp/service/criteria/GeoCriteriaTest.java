package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GeoCriteriaTest {

    @Test
    void newGeoCriteriaHasAllFiltersNullTest() {
        var geoCriteria = new GeoCriteria();
        assertThat(geoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void geoCriteriaFluentMethodsCreatesFiltersTest() {
        var geoCriteria = new GeoCriteria();

        setAllFilters(geoCriteria);

        assertThat(geoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void geoCriteriaCopyCreatesNullFilterTest() {
        var geoCriteria = new GeoCriteria();
        var copy = geoCriteria.copy();

        assertThat(geoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(geoCriteria)
        );
    }

    @Test
    void geoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var geoCriteria = new GeoCriteria();
        setAllFilters(geoCriteria);

        var copy = geoCriteria.copy();

        assertThat(geoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(geoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var geoCriteria = new GeoCriteria();

        assertThat(geoCriteria).hasToString("GeoCriteria{}");
    }

    private static void setAllFilters(GeoCriteria geoCriteria) {
        geoCriteria.id();
        geoCriteria.name();
        geoCriteria.code();
        geoCriteria.abbreviation();
        geoCriteria.geoTypeId();
        geoCriteria.distinct();
    }

    private static Condition<GeoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getAbbreviation()) &&
                condition.apply(criteria.getGeoTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GeoCriteria> copyFiltersAre(GeoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getAbbreviation(), copy.getAbbreviation()) &&
                condition.apply(criteria.getGeoTypeId(), copy.getGeoTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
