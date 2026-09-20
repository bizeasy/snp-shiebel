package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyIdentificationTypeCriteriaTest {

    @Test
    void newPartyIdentificationTypeCriteriaHasAllFiltersNullTest() {
        var partyIdentificationTypeCriteria = new PartyIdentificationTypeCriteria();
        assertThat(partyIdentificationTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyIdentificationTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var partyIdentificationTypeCriteria = new PartyIdentificationTypeCriteria();

        setAllFilters(partyIdentificationTypeCriteria);

        assertThat(partyIdentificationTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyIdentificationTypeCriteriaCopyCreatesNullFilterTest() {
        var partyIdentificationTypeCriteria = new PartyIdentificationTypeCriteria();
        var copy = partyIdentificationTypeCriteria.copy();

        assertThat(partyIdentificationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyIdentificationTypeCriteria)
        );
    }

    @Test
    void partyIdentificationTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyIdentificationTypeCriteria = new PartyIdentificationTypeCriteria();
        setAllFilters(partyIdentificationTypeCriteria);

        var copy = partyIdentificationTypeCriteria.copy();

        assertThat(partyIdentificationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyIdentificationTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyIdentificationTypeCriteria = new PartyIdentificationTypeCriteria();

        assertThat(partyIdentificationTypeCriteria).hasToString("PartyIdentificationTypeCriteria{}");
    }

    private static void setAllFilters(PartyIdentificationTypeCriteria partyIdentificationTypeCriteria) {
        partyIdentificationTypeCriteria.id();
        partyIdentificationTypeCriteria.name();
        partyIdentificationTypeCriteria.description();
        partyIdentificationTypeCriteria.distinct();
    }

    private static Condition<PartyIdentificationTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyIdentificationTypeCriteria> copyFiltersAre(
        PartyIdentificationTypeCriteria copy,
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
