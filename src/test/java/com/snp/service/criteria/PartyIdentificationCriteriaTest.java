package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyIdentificationCriteriaTest {

    @Test
    void newPartyIdentificationCriteriaHasAllFiltersNullTest() {
        var partyIdentificationCriteria = new PartyIdentificationCriteria();
        assertThat(partyIdentificationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyIdentificationCriteriaFluentMethodsCreatesFiltersTest() {
        var partyIdentificationCriteria = new PartyIdentificationCriteria();

        setAllFilters(partyIdentificationCriteria);

        assertThat(partyIdentificationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyIdentificationCriteriaCopyCreatesNullFilterTest() {
        var partyIdentificationCriteria = new PartyIdentificationCriteria();
        var copy = partyIdentificationCriteria.copy();

        assertThat(partyIdentificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyIdentificationCriteria)
        );
    }

    @Test
    void partyIdentificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyIdentificationCriteria = new PartyIdentificationCriteria();
        setAllFilters(partyIdentificationCriteria);

        var copy = partyIdentificationCriteria.copy();

        assertThat(partyIdentificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyIdentificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyIdentificationCriteria = new PartyIdentificationCriteria();

        assertThat(partyIdentificationCriteria).hasToString("PartyIdentificationCriteria{}");
    }

    private static void setAllFilters(PartyIdentificationCriteria partyIdentificationCriteria) {
        partyIdentificationCriteria.id();
        partyIdentificationCriteria.idValue();
        partyIdentificationCriteria.partyId();
        partyIdentificationCriteria.typeId();
        partyIdentificationCriteria.distinct();
    }

    private static Condition<PartyIdentificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdValue()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyIdentificationCriteria> copyFiltersAre(
        PartyIdentificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdValue(), copy.getIdValue()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getTypeId(), copy.getTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
