package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceRoleCriteriaTest {

    @Test
    void newInvoiceRoleCriteriaHasAllFiltersNullTest() {
        var invoiceRoleCriteria = new InvoiceRoleCriteria();
        assertThat(invoiceRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceRoleCriteria = new InvoiceRoleCriteria();

        setAllFilters(invoiceRoleCriteria);

        assertThat(invoiceRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceRoleCriteriaCopyCreatesNullFilterTest() {
        var invoiceRoleCriteria = new InvoiceRoleCriteria();
        var copy = invoiceRoleCriteria.copy();

        assertThat(invoiceRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceRoleCriteria)
        );
    }

    @Test
    void invoiceRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceRoleCriteria = new InvoiceRoleCriteria();
        setAllFilters(invoiceRoleCriteria);

        var copy = invoiceRoleCriteria.copy();

        assertThat(invoiceRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceRoleCriteria = new InvoiceRoleCriteria();

        assertThat(invoiceRoleCriteria).hasToString("InvoiceRoleCriteria{}");
    }

    private static void setAllFilters(InvoiceRoleCriteria invoiceRoleCriteria) {
        invoiceRoleCriteria.id();
        invoiceRoleCriteria.invoiceId();
        invoiceRoleCriteria.invoiceItemId();
        invoiceRoleCriteria.partyId();
        invoiceRoleCriteria.roleTypeId();
        invoiceRoleCriteria.distinct();
    }

    private static Condition<InvoiceRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceRoleCriteria> copyFiltersAre(InvoiceRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId(), copy.getInvoiceItemId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
