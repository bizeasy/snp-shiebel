package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PickListCriteriaTest {

    @Test
    void newPickListCriteriaHasAllFiltersNullTest() {
        var pickListCriteria = new PickListCriteria();
        assertThat(pickListCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pickListCriteriaFluentMethodsCreatesFiltersTest() {
        var pickListCriteria = new PickListCriteria();

        setAllFilters(pickListCriteria);

        assertThat(pickListCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pickListCriteriaCopyCreatesNullFilterTest() {
        var pickListCriteria = new PickListCriteria();
        var copy = pickListCriteria.copy();

        assertThat(pickListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pickListCriteria)
        );
    }

    @Test
    void pickListCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pickListCriteria = new PickListCriteria();
        setAllFilters(pickListCriteria);

        var copy = pickListCriteria.copy();

        assertThat(pickListCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pickListCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pickListCriteria = new PickListCriteria();

        assertThat(pickListCriteria).hasToString("PickListCriteria{}");
    }

    private static void setAllFilters(PickListCriteria pickListCriteria) {
        pickListCriteria.id();
        pickListCriteria.description();
        pickListCriteria.pickListDate();
        pickListCriteria.facilityId();
        pickListCriteria.statusId();
        pickListCriteria.partyId();
        pickListCriteria.distinct();
    }

    private static Condition<PickListCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPickListDate()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PickListCriteria> copyFiltersAre(PickListCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPickListDate(), copy.getPickListDate()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
