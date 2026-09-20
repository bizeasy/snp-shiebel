package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PartyClassificationTypeCriteriaTest {

    @Test
    void newPartyClassificationTypeCriteriaHasAllFiltersNullTest() {
        var partyClassificationTypeCriteria = new PartyClassificationTypeCriteria();
        assertThat(partyClassificationTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void partyClassificationTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var partyClassificationTypeCriteria = new PartyClassificationTypeCriteria();

        setAllFilters(partyClassificationTypeCriteria);

        assertThat(partyClassificationTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void partyClassificationTypeCriteriaCopyCreatesNullFilterTest() {
        var partyClassificationTypeCriteria = new PartyClassificationTypeCriteria();
        var copy = partyClassificationTypeCriteria.copy();

        assertThat(partyClassificationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationTypeCriteria)
        );
    }

    @Test
    void partyClassificationTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var partyClassificationTypeCriteria = new PartyClassificationTypeCriteria();
        setAllFilters(partyClassificationTypeCriteria);

        var copy = partyClassificationTypeCriteria.copy();

        assertThat(partyClassificationTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(partyClassificationTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var partyClassificationTypeCriteria = new PartyClassificationTypeCriteria();

        assertThat(partyClassificationTypeCriteria).hasToString("PartyClassificationTypeCriteria{}");
    }

    private static void setAllFilters(PartyClassificationTypeCriteria partyClassificationTypeCriteria) {
        partyClassificationTypeCriteria.id();
        partyClassificationTypeCriteria.name();
        partyClassificationTypeCriteria.description();
        partyClassificationTypeCriteria.distinct();
    }

    private static Condition<PartyClassificationTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PartyClassificationTypeCriteria> copyFiltersAre(
        PartyClassificationTypeCriteria copy,
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
