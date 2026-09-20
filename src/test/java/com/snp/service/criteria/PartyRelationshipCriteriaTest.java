package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyRelationshipCriteriaTest {

    @Test
    void newPartyRelationshipCriteriaHasAllFiltersNullTest() {
        var partyRelationshipCriteria = new PartyRelationshipCriteria();
        assertThat(partyRelationshipCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyRelationshipCriteriaFluentMethodsCreatesFiltersTest() {
        var partyRelationshipCriteria = new PartyRelationshipCriteria();

        setAllFilters(partyRelationshipCriteria);

        assertThat(partyRelationshipCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyRelationshipCriteriaCopyCreatesNullFilterTest() {
        var partyRelationshipCriteria = new PartyRelationshipCriteria();
        var copy = partyRelationshipCriteria.copy();

        assertThat(partyRelationshipCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRelationshipCriteria)
        );
    }

    @Test
    void partyRelationshipCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyRelationshipCriteria = new PartyRelationshipCriteria();
        setAllFilters(partyRelationshipCriteria);

        var copy = partyRelationshipCriteria.copy();

        assertThat(partyRelationshipCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRelationshipCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyRelationshipCriteria = new PartyRelationshipCriteria();

        assertThat(partyRelationshipCriteria).hasToString("PartyRelationshipCriteria{}");
    }

    private static void setAllFilters(PartyRelationshipCriteria partyRelationshipCriteria) {
        partyRelationshipCriteria.id();
        partyRelationshipCriteria.fromDate();
        partyRelationshipCriteria.thruDate();
        partyRelationshipCriteria.relationshipName();
        partyRelationshipCriteria.partyFromId();
        partyRelationshipCriteria.partyToId();
        partyRelationshipCriteria.roleTypeFromId();
        partyRelationshipCriteria.roleTypeToId();
        partyRelationshipCriteria.relationshipTypeId();
        partyRelationshipCriteria.distinct();
    }

    private static Condition<PartyRelationshipCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getRelationshipName()) &&
                condition.apply(criteria.getPartyFromId()) &&
                condition.apply(criteria.getPartyToId()) &&
                condition.apply(criteria.getRoleTypeFromId()) &&
                condition.apply(criteria.getRoleTypeToId()) &&
                condition.apply(criteria.getRelationshipTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyRelationshipCriteria> copyFiltersAre(
        PartyRelationshipCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getRelationshipName(), copy.getRelationshipName()) &&
                condition.apply(criteria.getPartyFromId(), copy.getPartyFromId()) &&
                condition.apply(criteria.getPartyToId(), copy.getPartyToId()) &&
                condition.apply(criteria.getRoleTypeFromId(), copy.getRoleTypeFromId()) &&
                condition.apply(criteria.getRoleTypeToId(), copy.getRoleTypeToId()) &&
                condition.apply(criteria.getRelationshipTypeId(), copy.getRelationshipTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
