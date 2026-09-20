package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyRoleCriteriaTest {

    @Test
    void newPartyRoleCriteriaHasAllFiltersNullTest() {
        var partyRoleCriteria = new PartyRoleCriteria();
        assertThat(partyRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var partyRoleCriteria = new PartyRoleCriteria();

        setAllFilters(partyRoleCriteria);

        assertThat(partyRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyRoleCriteriaCopyCreatesNullFilterTest() {
        var partyRoleCriteria = new PartyRoleCriteria();
        var copy = partyRoleCriteria.copy();

        assertThat(partyRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRoleCriteria)
        );
    }

    @Test
    void partyRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyRoleCriteria = new PartyRoleCriteria();
        setAllFilters(partyRoleCriteria);

        var copy = partyRoleCriteria.copy();

        assertThat(partyRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyRoleCriteria = new PartyRoleCriteria();

        assertThat(partyRoleCriteria).hasToString("PartyRoleCriteria{}");
    }

    private static void setAllFilters(PartyRoleCriteria partyRoleCriteria) {
        partyRoleCriteria.id();
        partyRoleCriteria.partyId();
        partyRoleCriteria.roleTypeId();
        partyRoleCriteria.distinct();
    }

    private static Condition<PartyRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyRoleCriteria> copyFiltersAre(PartyRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
