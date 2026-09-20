package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StatusValidChangeCriteriaTest {

    @Test
    void newStatusValidChangeCriteriaHasAllFiltersNullTest() {
        var statusValidChangeCriteria = new StatusValidChangeCriteria();
        assertThat(statusValidChangeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void statusValidChangeCriteriaFluentMethodsCreatesFiltersTest() {
        var statusValidChangeCriteria = new StatusValidChangeCriteria();

        setAllFilters(statusValidChangeCriteria);

        assertThat(statusValidChangeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void statusValidChangeCriteriaCopyCreatesNullFilterTest() {
        var statusValidChangeCriteria = new StatusValidChangeCriteria();
        var copy = statusValidChangeCriteria.copy();

        assertThat(statusValidChangeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(statusValidChangeCriteria)
        );
    }

    @Test
    void statusValidChangeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var statusValidChangeCriteria = new StatusValidChangeCriteria();
        setAllFilters(statusValidChangeCriteria);

        var copy = statusValidChangeCriteria.copy();

        assertThat(statusValidChangeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(statusValidChangeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var statusValidChangeCriteria = new StatusValidChangeCriteria();

        assertThat(statusValidChangeCriteria).hasToString("StatusValidChangeCriteria{}");
    }

    private static void setAllFilters(StatusValidChangeCriteria statusValidChangeCriteria) {
        statusValidChangeCriteria.id();
        statusValidChangeCriteria.transitionName();
        statusValidChangeCriteria.statusId();
        statusValidChangeCriteria.statusToId();
        statusValidChangeCriteria.distinct();
    }

    private static Condition<StatusValidChangeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTransitionName()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getStatusToId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StatusValidChangeCriteria> copyFiltersAre(
        StatusValidChangeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTransitionName(), copy.getTransitionName()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getStatusToId(), copy.getStatusToId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
