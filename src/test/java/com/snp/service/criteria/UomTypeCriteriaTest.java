package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UomTypeCriteriaTest {

    @Test
    void newUomTypeCriteriaHasAllFiltersNullTest() {
        var uomTypeCriteria = new UomTypeCriteria();
        assertThat(uomTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void uomTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var uomTypeCriteria = new UomTypeCriteria();

        setAllFilters(uomTypeCriteria);

        assertThat(uomTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void uomTypeCriteriaCopyCreatesNullFilterTest() {
        var uomTypeCriteria = new UomTypeCriteria();
        var copy = uomTypeCriteria.copy();

        assertThat(uomTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(uomTypeCriteria)
        );
    }

    @Test
    void uomTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var uomTypeCriteria = new UomTypeCriteria();
        setAllFilters(uomTypeCriteria);

        var copy = uomTypeCriteria.copy();

        assertThat(uomTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(uomTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var uomTypeCriteria = new UomTypeCriteria();

        assertThat(uomTypeCriteria).hasToString("UomTypeCriteria{}");
    }

    private static void setAllFilters(UomTypeCriteria uomTypeCriteria) {
        uomTypeCriteria.id();
        uomTypeCriteria.name();
        uomTypeCriteria.description();
        uomTypeCriteria.distinct();
    }

    private static Condition<UomTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UomTypeCriteria> copyFiltersAre(UomTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
