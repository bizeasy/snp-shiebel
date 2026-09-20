package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceTypeCriteriaTest {

    @Test
    void newInvoiceTypeCriteriaHasAllFiltersNullTest() {
        var invoiceTypeCriteria = new InvoiceTypeCriteria();
        assertThat(invoiceTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceTypeCriteria = new InvoiceTypeCriteria();

        setAllFilters(invoiceTypeCriteria);

        assertThat(invoiceTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceTypeCriteriaCopyCreatesNullFilterTest() {
        var invoiceTypeCriteria = new InvoiceTypeCriteria();
        var copy = invoiceTypeCriteria.copy();

        assertThat(invoiceTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceTypeCriteria)
        );
    }

    @Test
    void invoiceTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceTypeCriteria = new InvoiceTypeCriteria();
        setAllFilters(invoiceTypeCriteria);

        var copy = invoiceTypeCriteria.copy();

        assertThat(invoiceTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceTypeCriteria = new InvoiceTypeCriteria();

        assertThat(invoiceTypeCriteria).hasToString("InvoiceTypeCriteria{}");
    }

    private static void setAllFilters(InvoiceTypeCriteria invoiceTypeCriteria) {
        invoiceTypeCriteria.id();
        invoiceTypeCriteria.name();
        invoiceTypeCriteria.description();
        invoiceTypeCriteria.distinct();
    }

    private static Condition<InvoiceTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceTypeCriteria> copyFiltersAre(InvoiceTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
