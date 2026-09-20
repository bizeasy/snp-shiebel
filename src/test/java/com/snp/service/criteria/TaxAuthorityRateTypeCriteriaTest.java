package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TaxAuthorityRateTypeCriteriaTest {

    @Test
    void newTaxAuthorityRateTypeCriteriaHasAllFiltersNullTest() {
        var taxAuthorityRateTypeCriteria = new TaxAuthorityRateTypeCriteria();
        assertThat(taxAuthorityRateTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void taxAuthorityRateTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var taxAuthorityRateTypeCriteria = new TaxAuthorityRateTypeCriteria();

        setAllFilters(taxAuthorityRateTypeCriteria);

        assertThat(taxAuthorityRateTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void taxAuthorityRateTypeCriteriaCopyCreatesNullFilterTest() {
        var taxAuthorityRateTypeCriteria = new TaxAuthorityRateTypeCriteria();
        var copy = taxAuthorityRateTypeCriteria.copy();

        assertThat(taxAuthorityRateTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(taxAuthorityRateTypeCriteria)
        );
    }

    @Test
    void taxAuthorityRateTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var taxAuthorityRateTypeCriteria = new TaxAuthorityRateTypeCriteria();
        setAllFilters(taxAuthorityRateTypeCriteria);

        var copy = taxAuthorityRateTypeCriteria.copy();

        assertThat(taxAuthorityRateTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(taxAuthorityRateTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var taxAuthorityRateTypeCriteria = new TaxAuthorityRateTypeCriteria();

        assertThat(taxAuthorityRateTypeCriteria).hasToString("TaxAuthorityRateTypeCriteria{}");
    }

    private static void setAllFilters(TaxAuthorityRateTypeCriteria taxAuthorityRateTypeCriteria) {
        taxAuthorityRateTypeCriteria.id();
        taxAuthorityRateTypeCriteria.name();
        taxAuthorityRateTypeCriteria.description();
        taxAuthorityRateTypeCriteria.taxAuthorityId();
        taxAuthorityRateTypeCriteria.taxSlabId();
        taxAuthorityRateTypeCriteria.distinct();
    }

    private static Condition<TaxAuthorityRateTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTaxAuthorityId()) &&
                condition.apply(criteria.getTaxSlabId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TaxAuthorityRateTypeCriteria> copyFiltersAre(
        TaxAuthorityRateTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTaxAuthorityId(), copy.getTaxAuthorityId()) &&
                condition.apply(criteria.getTaxSlabId(), copy.getTaxSlabId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
