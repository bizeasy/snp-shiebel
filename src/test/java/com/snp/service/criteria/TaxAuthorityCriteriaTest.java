package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TaxAuthorityCriteriaTest {

    @Test
    void newTaxAuthorityCriteriaHasAllFiltersNullTest() {
        var taxAuthorityCriteria = new TaxAuthorityCriteria();
        assertThat(taxAuthorityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void taxAuthorityCriteriaFluentMethodsCreatesFiltersTest() {
        var taxAuthorityCriteria = new TaxAuthorityCriteria();

        setAllFilters(taxAuthorityCriteria);

        assertThat(taxAuthorityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void taxAuthorityCriteriaCopyCreatesNullFilterTest() {
        var taxAuthorityCriteria = new TaxAuthorityCriteria();
        var copy = taxAuthorityCriteria.copy();

        assertThat(taxAuthorityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(taxAuthorityCriteria)
        );
    }

    @Test
    void taxAuthorityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var taxAuthorityCriteria = new TaxAuthorityCriteria();
        setAllFilters(taxAuthorityCriteria);

        var copy = taxAuthorityCriteria.copy();

        assertThat(taxAuthorityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(taxAuthorityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var taxAuthorityCriteria = new TaxAuthorityCriteria();

        assertThat(taxAuthorityCriteria).hasToString("TaxAuthorityCriteria{}");
    }

    private static void setAllFilters(TaxAuthorityCriteria taxAuthorityCriteria) {
        taxAuthorityCriteria.id();
        taxAuthorityCriteria.name();
        taxAuthorityCriteria.geoId();
        taxAuthorityCriteria.distinct();
    }

    private static Condition<TaxAuthorityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getGeoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TaxAuthorityCriteria> copyFiltersAre(
        TaxAuthorityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getGeoId(), copy.getGeoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
