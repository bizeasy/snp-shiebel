package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReturnHeaderTypeCriteriaTest {

    @Test
    void newReturnHeaderTypeCriteriaHasAllFiltersNullTest() {
        var returnHeaderTypeCriteria = new ReturnHeaderTypeCriteria();
        assertThat(returnHeaderTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void returnHeaderTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var returnHeaderTypeCriteria = new ReturnHeaderTypeCriteria();

        setAllFilters(returnHeaderTypeCriteria);

        assertThat(returnHeaderTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void returnHeaderTypeCriteriaCopyCreatesNullFilterTest() {
        var returnHeaderTypeCriteria = new ReturnHeaderTypeCriteria();
        var copy = returnHeaderTypeCriteria.copy();

        assertThat(returnHeaderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(returnHeaderTypeCriteria)
        );
    }

    @Test
    void returnHeaderTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var returnHeaderTypeCriteria = new ReturnHeaderTypeCriteria();
        setAllFilters(returnHeaderTypeCriteria);

        var copy = returnHeaderTypeCriteria.copy();

        assertThat(returnHeaderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(returnHeaderTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var returnHeaderTypeCriteria = new ReturnHeaderTypeCriteria();

        assertThat(returnHeaderTypeCriteria).hasToString("ReturnHeaderTypeCriteria{}");
    }

    private static void setAllFilters(ReturnHeaderTypeCriteria returnHeaderTypeCriteria) {
        returnHeaderTypeCriteria.id();
        returnHeaderTypeCriteria.name();
        returnHeaderTypeCriteria.description();
        returnHeaderTypeCriteria.distinct();
    }

    private static Condition<ReturnHeaderTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReturnHeaderTypeCriteria> copyFiltersAre(
        ReturnHeaderTypeCriteria copy,
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
