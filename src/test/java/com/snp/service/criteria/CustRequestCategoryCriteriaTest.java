package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestCategoryCriteriaTest {

    @Test
    void newCustRequestCategoryCriteriaHasAllFiltersNullTest() {
        var custRequestCategoryCriteria = new CustRequestCategoryCriteria();
        assertThat(custRequestCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestCategoryCriteria = new CustRequestCategoryCriteria();

        setAllFilters(custRequestCategoryCriteria);

        assertThat(custRequestCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestCategoryCriteriaCopyCreatesNullFilterTest() {
        var custRequestCategoryCriteria = new CustRequestCategoryCriteria();
        var copy = custRequestCategoryCriteria.copy();

        assertThat(custRequestCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestCategoryCriteria)
        );
    }

    @Test
    void custRequestCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestCategoryCriteria = new CustRequestCategoryCriteria();
        setAllFilters(custRequestCategoryCriteria);

        var copy = custRequestCategoryCriteria.copy();

        assertThat(custRequestCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestCategoryCriteria = new CustRequestCategoryCriteria();

        assertThat(custRequestCategoryCriteria).hasToString("CustRequestCategoryCriteria{}");
    }

    private static void setAllFilters(CustRequestCategoryCriteria custRequestCategoryCriteria) {
        custRequestCategoryCriteria.id();
        custRequestCategoryCriteria.name();
        custRequestCategoryCriteria.description();
        custRequestCategoryCriteria.custRequestTypeId();
        custRequestCategoryCriteria.distinct();
    }

    private static Condition<CustRequestCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCustRequestTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestCategoryCriteria> copyFiltersAre(
        CustRequestCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCustRequestTypeId(), copy.getCustRequestTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
