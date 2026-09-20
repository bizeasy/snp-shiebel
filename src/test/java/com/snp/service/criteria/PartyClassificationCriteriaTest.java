package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyClassificationCriteriaTest {

    @Test
    void newPartyClassificationCriteriaHasAllFiltersNullTest() {
        var partyClassificationCriteria = new PartyClassificationCriteria();
        assertThat(partyClassificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyClassificationCriteriaFluentMethodsCreatesFiltersTest() {
        var partyClassificationCriteria = new PartyClassificationCriteria();

        setAllFilters(partyClassificationCriteria);

        assertThat(partyClassificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyClassificationCriteriaCopyCreatesNullFilterTest() {
        var partyClassificationCriteria = new PartyClassificationCriteria();
        var copy = partyClassificationCriteria.copy();

        assertThat(partyClassificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationCriteria)
        );
    }

    @Test
    void partyClassificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyClassificationCriteria = new PartyClassificationCriteria();
        setAllFilters(partyClassificationCriteria);

        var copy = partyClassificationCriteria.copy();

        assertThat(partyClassificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyClassificationCriteria = new PartyClassificationCriteria();

        assertThat(partyClassificationCriteria).hasToString("PartyClassificationCriteria{}");
    }

    private static void setAllFilters(PartyClassificationCriteria partyClassificationCriteria) {
        partyClassificationCriteria.id();
        partyClassificationCriteria.fromDate();
        partyClassificationCriteria.thruDate();
        partyClassificationCriteria.partyId();
        partyClassificationCriteria.classificationGroupId();
        partyClassificationCriteria.distinct();
    }

    private static Condition<PartyClassificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getClassificationGroupId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyClassificationCriteria> copyFiltersAre(
        PartyClassificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getClassificationGroupId(), copy.getClassificationGroupId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
