package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestRoleCriteriaTest {

    @Test
    void newCustRequestRoleCriteriaHasAllFiltersNullTest() {
        var custRequestRoleCriteria = new CustRequestRoleCriteria();
        assertThat(custRequestRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestRoleCriteria = new CustRequestRoleCriteria();

        setAllFilters(custRequestRoleCriteria);

        assertThat(custRequestRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestRoleCriteriaCopyCreatesNullFilterTest() {
        var custRequestRoleCriteria = new CustRequestRoleCriteria();
        var copy = custRequestRoleCriteria.copy();

        assertThat(custRequestRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestRoleCriteria)
        );
    }

    @Test
    void custRequestRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestRoleCriteria = new CustRequestRoleCriteria();
        setAllFilters(custRequestRoleCriteria);

        var copy = custRequestRoleCriteria.copy();

        assertThat(custRequestRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestRoleCriteria = new CustRequestRoleCriteria();

        assertThat(custRequestRoleCriteria).hasToString("CustRequestRoleCriteria{}");
    }

    private static void setAllFilters(CustRequestRoleCriteria custRequestRoleCriteria) {
        custRequestRoleCriteria.id();
        custRequestRoleCriteria.custRequestId();
        custRequestRoleCriteria.custRequestItemId();
        custRequestRoleCriteria.partyId();
        custRequestRoleCriteria.roleTypeId();
        custRequestRoleCriteria.distinct();
    }

    private static Condition<CustRequestRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCustRequestId()) &&
                condition.apply(criteria.getCustRequestItemId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestRoleCriteria> copyFiltersAre(
        CustRequestRoleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCustRequestId(), copy.getCustRequestId()) &&
                condition.apply(criteria.getCustRequestItemId(), copy.getCustRequestItemId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
