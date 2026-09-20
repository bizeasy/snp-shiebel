package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyRelationshipTypeCriteriaTest {

    @Test
    void newPartyRelationshipTypeCriteriaHasAllFiltersNullTest() {
        var partyRelationshipTypeCriteria = new PartyRelationshipTypeCriteria();
        assertThat(partyRelationshipTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyRelationshipTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var partyRelationshipTypeCriteria = new PartyRelationshipTypeCriteria();

        setAllFilters(partyRelationshipTypeCriteria);

        assertThat(partyRelationshipTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyRelationshipTypeCriteriaCopyCreatesNullFilterTest() {
        var partyRelationshipTypeCriteria = new PartyRelationshipTypeCriteria();
        var copy = partyRelationshipTypeCriteria.copy();

        assertThat(partyRelationshipTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRelationshipTypeCriteria)
        );
    }

    @Test
    void partyRelationshipTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyRelationshipTypeCriteria = new PartyRelationshipTypeCriteria();
        setAllFilters(partyRelationshipTypeCriteria);

        var copy = partyRelationshipTypeCriteria.copy();

        assertThat(partyRelationshipTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRelationshipTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyRelationshipTypeCriteria = new PartyRelationshipTypeCriteria();

        assertThat(partyRelationshipTypeCriteria).hasToString("PartyRelationshipTypeCriteria{}");
    }

    private static void setAllFilters(PartyRelationshipTypeCriteria partyRelationshipTypeCriteria) {
        partyRelationshipTypeCriteria.id();
        partyRelationshipTypeCriteria.name();
        partyRelationshipTypeCriteria.description();
        partyRelationshipTypeCriteria.roleTypeFromId();
        partyRelationshipTypeCriteria.roleTypeToId();
        partyRelationshipTypeCriteria.distinct();
    }

    private static Condition<PartyRelationshipTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getRoleTypeFromId()) &&
                condition.apply(criteria.getRoleTypeToId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyRelationshipTypeCriteria> copyFiltersAre(
        PartyRelationshipTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getRoleTypeFromId(), copy.getRoleTypeFromId()) &&
                condition.apply(criteria.getRoleTypeToId(), copy.getRoleTypeToId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
