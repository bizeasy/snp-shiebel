package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PicklistRoleCriteriaTest {

    @Test
    void newPicklistRoleCriteriaHasAllFiltersNullTest() {
        var picklistRoleCriteria = new PicklistRoleCriteria();
        assertThat(picklistRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void picklistRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var picklistRoleCriteria = new PicklistRoleCriteria();

        setAllFilters(picklistRoleCriteria);

        assertThat(picklistRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void picklistRoleCriteriaCopyCreatesNullFilterTest() {
        var picklistRoleCriteria = new PicklistRoleCriteria();
        var copy = picklistRoleCriteria.copy();

        assertThat(picklistRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(picklistRoleCriteria)
        );
    }

    @Test
    void picklistRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var picklistRoleCriteria = new PicklistRoleCriteria();
        setAllFilters(picklistRoleCriteria);

        var copy = picklistRoleCriteria.copy();

        assertThat(picklistRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(picklistRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var picklistRoleCriteria = new PicklistRoleCriteria();

        assertThat(picklistRoleCriteria).hasToString("PicklistRoleCriteria{}");
    }

    private static void setAllFilters(PicklistRoleCriteria picklistRoleCriteria) {
        picklistRoleCriteria.id();
        picklistRoleCriteria.pickListId();
        picklistRoleCriteria.partyId();
        picklistRoleCriteria.roleTypeId();
        picklistRoleCriteria.distinct();
    }

    private static Condition<PicklistRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPickListId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PicklistRoleCriteria> copyFiltersAre(
        PicklistRoleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPickListId(), copy.getPickListId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
