package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestTypeCriteriaTest {

    @Test
    void newCustRequestTypeCriteriaHasAllFiltersNullTest() {
        var custRequestTypeCriteria = new CustRequestTypeCriteria();
        assertThat(custRequestTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestTypeCriteria = new CustRequestTypeCriteria();

        setAllFilters(custRequestTypeCriteria);

        assertThat(custRequestTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestTypeCriteriaCopyCreatesNullFilterTest() {
        var custRequestTypeCriteria = new CustRequestTypeCriteria();
        var copy = custRequestTypeCriteria.copy();

        assertThat(custRequestTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestTypeCriteria)
        );
    }

    @Test
    void custRequestTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestTypeCriteria = new CustRequestTypeCriteria();
        setAllFilters(custRequestTypeCriteria);

        var copy = custRequestTypeCriteria.copy();

        assertThat(custRequestTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestTypeCriteria = new CustRequestTypeCriteria();

        assertThat(custRequestTypeCriteria).hasToString("CustRequestTypeCriteria{}");
    }

    private static void setAllFilters(CustRequestTypeCriteria custRequestTypeCriteria) {
        custRequestTypeCriteria.id();
        custRequestTypeCriteria.name();
        custRequestTypeCriteria.description();
        custRequestTypeCriteria.parentId();
        custRequestTypeCriteria.partyId();
        custRequestTypeCriteria.distinct();
    }

    private static Condition<CustRequestTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestTypeCriteria> copyFiltersAre(
        CustRequestTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
