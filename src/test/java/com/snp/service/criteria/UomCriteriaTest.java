package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UomCriteriaTest {

    @Test
    void newUomCriteriaHasAllFiltersNullTest() {
        var uomCriteria = new UomCriteria();
        assertThat(uomCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void uomCriteriaFluentMethodsCreatesFiltersTest() {
        var uomCriteria = new UomCriteria();

        setAllFilters(uomCriteria);

        assertThat(uomCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void uomCriteriaCopyCreatesNullFilterTest() {
        var uomCriteria = new UomCriteria();
        var copy = uomCriteria.copy();

        assertThat(uomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(uomCriteria)
        );
    }

    @Test
    void uomCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var uomCriteria = new UomCriteria();
        setAllFilters(uomCriteria);

        var copy = uomCriteria.copy();

        assertThat(uomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(uomCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var uomCriteria = new UomCriteria();

        assertThat(uomCriteria).hasToString("UomCriteria{}");
    }

    private static void setAllFilters(UomCriteria uomCriteria) {
        uomCriteria.id();
        uomCriteria.name();
        uomCriteria.description();
        uomCriteria.sequenceNo();
        uomCriteria.abbreviation();
        uomCriteria.uomTypeId();
        uomCriteria.distinct();
    }

    private static Condition<UomCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getAbbreviation()) &&
                condition.apply(criteria.getUomTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UomCriteria> copyFiltersAre(UomCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getAbbreviation(), copy.getAbbreviation()) &&
                condition.apply(criteria.getUomTypeId(), copy.getUomTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
