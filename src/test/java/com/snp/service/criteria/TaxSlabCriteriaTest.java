package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TaxSlabCriteriaTest {

    @Test
    void newTaxSlabCriteriaHasAllFiltersNullTest() {
        var taxSlabCriteria = new TaxSlabCriteria();
        assertThat(taxSlabCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void taxSlabCriteriaFluentMethodsCreatesFiltersTest() {
        var taxSlabCriteria = new TaxSlabCriteria();

        setAllFilters(taxSlabCriteria);

        assertThat(taxSlabCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void taxSlabCriteriaCopyCreatesNullFilterTest() {
        var taxSlabCriteria = new TaxSlabCriteria();
        var copy = taxSlabCriteria.copy();

        assertThat(taxSlabCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(taxSlabCriteria)
        );
    }

    @Test
    void taxSlabCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var taxSlabCriteria = new TaxSlabCriteria();
        setAllFilters(taxSlabCriteria);

        var copy = taxSlabCriteria.copy();

        assertThat(taxSlabCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(taxSlabCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var taxSlabCriteria = new TaxSlabCriteria();

        assertThat(taxSlabCriteria).hasToString("TaxSlabCriteria{}");
    }

    private static void setAllFilters(TaxSlabCriteria taxSlabCriteria) {
        taxSlabCriteria.id();
        taxSlabCriteria.name();
        taxSlabCriteria.rate();
        taxSlabCriteria.distinct();
    }

    private static Condition<TaxSlabCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getRate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TaxSlabCriteria> copyFiltersAre(TaxSlabCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getRate(), copy.getRate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
