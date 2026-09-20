package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SystemParametersCriteriaTest {

    @Test
    void newSystemParametersCriteriaHasAllFiltersNullTest() {
        var systemParametersCriteria = new SystemParametersCriteria();
        assertThat(systemParametersCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void systemParametersCriteriaFluentMethodsCreatesFiltersTest() {
        var systemParametersCriteria = new SystemParametersCriteria();

        setAllFilters(systemParametersCriteria);

        assertThat(systemParametersCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void systemParametersCriteriaCopyCreatesNullFilterTest() {
        var systemParametersCriteria = new SystemParametersCriteria();
        var copy = systemParametersCriteria.copy();

        assertThat(systemParametersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(systemParametersCriteria)
        );
    }

    @Test
    void systemParametersCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var systemParametersCriteria = new SystemParametersCriteria();
        setAllFilters(systemParametersCriteria);

        var copy = systemParametersCriteria.copy();

        assertThat(systemParametersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(systemParametersCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var systemParametersCriteria = new SystemParametersCriteria();

        assertThat(systemParametersCriteria).hasToString("SystemParametersCriteria{}");
    }

    private static void setAllFilters(SystemParametersCriteria systemParametersCriteria) {
        systemParametersCriteria.id();
        systemParametersCriteria.key();
        systemParametersCriteria.value();
        systemParametersCriteria.description();
        systemParametersCriteria.environment();
        systemParametersCriteria.distinct();
    }

    private static Condition<SystemParametersCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getEnvironment()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SystemParametersCriteria> copyFiltersAre(
        SystemParametersCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getEnvironment(), copy.getEnvironment()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
