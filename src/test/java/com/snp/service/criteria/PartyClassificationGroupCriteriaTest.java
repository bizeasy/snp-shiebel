package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyClassificationGroupCriteriaTest {

    @Test
    void newPartyClassificationGroupCriteriaHasAllFiltersNullTest() {
        var partyClassificationGroupCriteria = new PartyClassificationGroupCriteria();
        assertThat(partyClassificationGroupCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyClassificationGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var partyClassificationGroupCriteria = new PartyClassificationGroupCriteria();

        setAllFilters(partyClassificationGroupCriteria);

        assertThat(partyClassificationGroupCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyClassificationGroupCriteriaCopyCreatesNullFilterTest() {
        var partyClassificationGroupCriteria = new PartyClassificationGroupCriteria();
        var copy = partyClassificationGroupCriteria.copy();

        assertThat(partyClassificationGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationGroupCriteria)
        );
    }

    @Test
    void partyClassificationGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyClassificationGroupCriteria = new PartyClassificationGroupCriteria();
        setAllFilters(partyClassificationGroupCriteria);

        var copy = partyClassificationGroupCriteria.copy();

        assertThat(partyClassificationGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyClassificationGroupCriteria = new PartyClassificationGroupCriteria();

        assertThat(partyClassificationGroupCriteria).hasToString("PartyClassificationGroupCriteria{}");
    }

    private static void setAllFilters(PartyClassificationGroupCriteria partyClassificationGroupCriteria) {
        partyClassificationGroupCriteria.id();
        partyClassificationGroupCriteria.name();
        partyClassificationGroupCriteria.description();
        partyClassificationGroupCriteria.classificationTypeId();
        partyClassificationGroupCriteria.distinct();
    }

    private static Condition<PartyClassificationGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getClassificationTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyClassificationGroupCriteria> copyFiltersAre(
        PartyClassificationGroupCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getClassificationTypeId(), copy.getClassificationTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
