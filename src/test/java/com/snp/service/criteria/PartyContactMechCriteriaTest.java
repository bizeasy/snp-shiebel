package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyContactMechCriteriaTest {

    @Test
    void newPartyContactMechCriteriaHasAllFiltersNullTest() {
        var partyContactMechCriteria = new PartyContactMechCriteria();
        assertThat(partyContactMechCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyContactMechCriteriaFluentMethodsCreatesFiltersTest() {
        var partyContactMechCriteria = new PartyContactMechCriteria();

        setAllFilters(partyContactMechCriteria);

        assertThat(partyContactMechCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyContactMechCriteriaCopyCreatesNullFilterTest() {
        var partyContactMechCriteria = new PartyContactMechCriteria();
        var copy = partyContactMechCriteria.copy();

        assertThat(partyContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyContactMechCriteria)
        );
    }

    @Test
    void partyContactMechCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyContactMechCriteria = new PartyContactMechCriteria();
        setAllFilters(partyContactMechCriteria);

        var copy = partyContactMechCriteria.copy();

        assertThat(partyContactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyContactMechCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyContactMechCriteria = new PartyContactMechCriteria();

        assertThat(partyContactMechCriteria).hasToString("PartyContactMechCriteria{}");
    }

    private static void setAllFilters(PartyContactMechCriteria partyContactMechCriteria) {
        partyContactMechCriteria.id();
        partyContactMechCriteria.fromDate();
        partyContactMechCriteria.thruDate();
        partyContactMechCriteria.partyId();
        partyContactMechCriteria.contactMechId();
        partyContactMechCriteria.contactMechPurposeId();
        partyContactMechCriteria.distinct();
    }

    private static Condition<PartyContactMechCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyContactMechCriteria> copyFiltersAre(
        PartyContactMechCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getContactMechPurposeId(), copy.getContactMechPurposeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
