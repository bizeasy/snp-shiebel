package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomTimePeriodCriteriaTest {

    @Test
    void newCustomTimePeriodCriteriaHasAllFiltersNullTest() {
        var customTimePeriodCriteria = new CustomTimePeriodCriteria();
        assertThat(customTimePeriodCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void customTimePeriodCriteriaFluentMethodsCreatesFiltersTest() {
        var customTimePeriodCriteria = new CustomTimePeriodCriteria();

        setAllFilters(customTimePeriodCriteria);

        assertThat(customTimePeriodCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void customTimePeriodCriteriaCopyCreatesNullFilterTest() {
        var customTimePeriodCriteria = new CustomTimePeriodCriteria();
        var copy = customTimePeriodCriteria.copy();

        assertThat(customTimePeriodCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(customTimePeriodCriteria)
        );
    }

    @Test
    void customTimePeriodCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customTimePeriodCriteria = new CustomTimePeriodCriteria();
        setAllFilters(customTimePeriodCriteria);

        var copy = customTimePeriodCriteria.copy();

        assertThat(customTimePeriodCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(customTimePeriodCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customTimePeriodCriteria = new CustomTimePeriodCriteria();

        assertThat(customTimePeriodCriteria).hasToString("CustomTimePeriodCriteria{}");
    }

    private static void setAllFilters(CustomTimePeriodCriteria customTimePeriodCriteria) {
        customTimePeriodCriteria.id();
        customTimePeriodCriteria.name();
        customTimePeriodCriteria.fromDate();
        customTimePeriodCriteria.thruDate();
        customTimePeriodCriteria.distinct();
    }

    private static Condition<CustomTimePeriodCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustomTimePeriodCriteria> copyFiltersAre(
        CustomTimePeriodCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
