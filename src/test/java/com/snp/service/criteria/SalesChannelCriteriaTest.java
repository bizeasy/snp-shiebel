package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SalesChannelCriteriaTest {

    @Test
    void newSalesChannelCriteriaHasAllFiltersNullTest() {
        var salesChannelCriteria = new SalesChannelCriteria();
        assertThat(salesChannelCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void salesChannelCriteriaFluentMethodsCreatesFiltersTest() {
        var salesChannelCriteria = new SalesChannelCriteria();

        setAllFilters(salesChannelCriteria);

        assertThat(salesChannelCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void salesChannelCriteriaCopyCreatesNullFilterTest() {
        var salesChannelCriteria = new SalesChannelCriteria();
        var copy = salesChannelCriteria.copy();

        assertThat(salesChannelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(salesChannelCriteria)
        );
    }

    @Test
    void salesChannelCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var salesChannelCriteria = new SalesChannelCriteria();
        setAllFilters(salesChannelCriteria);

        var copy = salesChannelCriteria.copy();

        assertThat(salesChannelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(salesChannelCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var salesChannelCriteria = new SalesChannelCriteria();

        assertThat(salesChannelCriteria).hasToString("SalesChannelCriteria{}");
    }

    private static void setAllFilters(SalesChannelCriteria salesChannelCriteria) {
        salesChannelCriteria.id();
        salesChannelCriteria.name();
        salesChannelCriteria.description();
        salesChannelCriteria.sequenceNo();
        salesChannelCriteria.distinct();
    }

    private static Condition<SalesChannelCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SalesChannelCriteria> copyFiltersAre(
        SalesChannelCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
