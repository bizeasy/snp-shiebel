package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceItemTypeMapCriteriaTest {

    @Test
    void newInvoiceItemTypeMapCriteriaHasAllFiltersNullTest() {
        var invoiceItemTypeMapCriteria = new InvoiceItemTypeMapCriteria();
        assertThat(invoiceItemTypeMapCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceItemTypeMapCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceItemTypeMapCriteria = new InvoiceItemTypeMapCriteria();

        setAllFilters(invoiceItemTypeMapCriteria);

        assertThat(invoiceItemTypeMapCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceItemTypeMapCriteriaCopyCreatesNullFilterTest() {
        var invoiceItemTypeMapCriteria = new InvoiceItemTypeMapCriteria();
        var copy = invoiceItemTypeMapCriteria.copy();

        assertThat(invoiceItemTypeMapCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceItemTypeMapCriteria)
        );
    }

    @Test
    void invoiceItemTypeMapCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceItemTypeMapCriteria = new InvoiceItemTypeMapCriteria();
        setAllFilters(invoiceItemTypeMapCriteria);

        var copy = invoiceItemTypeMapCriteria.copy();

        assertThat(invoiceItemTypeMapCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceItemTypeMapCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceItemTypeMapCriteria = new InvoiceItemTypeMapCriteria();

        assertThat(invoiceItemTypeMapCriteria).hasToString("InvoiceItemTypeMapCriteria{}");
    }

    private static void setAllFilters(InvoiceItemTypeMapCriteria invoiceItemTypeMapCriteria) {
        invoiceItemTypeMapCriteria.id();
        invoiceItemTypeMapCriteria.invoiceItemMapKey();
        invoiceItemTypeMapCriteria.invoiceTypeId();
        invoiceItemTypeMapCriteria.invoiceItemTypeId();
        invoiceItemTypeMapCriteria.distinct();
    }

    private static Condition<InvoiceItemTypeMapCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceItemMapKey()) &&
                condition.apply(criteria.getInvoiceTypeId()) &&
                condition.apply(criteria.getInvoiceItemTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceItemTypeMapCriteria> copyFiltersAre(
        InvoiceItemTypeMapCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceItemMapKey(), copy.getInvoiceItemMapKey()) &&
                condition.apply(criteria.getInvoiceTypeId(), copy.getInvoiceTypeId()) &&
                condition.apply(criteria.getInvoiceItemTypeId(), copy.getInvoiceItemTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
