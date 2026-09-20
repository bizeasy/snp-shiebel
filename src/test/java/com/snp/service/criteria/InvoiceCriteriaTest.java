package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceCriteriaTest {

    @Test
    void newInvoiceCriteriaHasAllFiltersNullTest() {
        var invoiceCriteria = new InvoiceCriteria();
        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceCriteria = new InvoiceCriteria();

        setAllFilters(invoiceCriteria);

        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceCriteriaCopyCreatesNullFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void invoiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        setAllFilters(invoiceCriteria);

        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceCriteria = new InvoiceCriteria();

        assertThat(invoiceCriteria).hasToString("InvoiceCriteria{}");
    }

    private static void setAllFilters(InvoiceCriteria invoiceCriteria) {
        invoiceCriteria.id();
        invoiceCriteria.invoiceDate();
        invoiceCriteria.dueDate();
        invoiceCriteria.paidDate();
        invoiceCriteria.referenceNumber();
        invoiceCriteria.invoiceMessage();
        invoiceCriteria.externalId();
        invoiceCriteria.entryDate();
        invoiceCriteria.grandTotal();
        invoiceCriteria.projectName();
        invoiceCriteria.remarks();
        invoiceCriteria.invoiceTypeId();
        invoiceCriteria.orderId();
        invoiceCriteria.fromPartyId();
        invoiceCriteria.toPartyId();
        invoiceCriteria.roleTypeId();
        invoiceCriteria.statusId();
        invoiceCriteria.contactMechId();
        invoiceCriteria.currencyUomId();
        invoiceCriteria.distinct();
    }

    private static Condition<InvoiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getPaidDate()) &&
                condition.apply(criteria.getReferenceNumber()) &&
                condition.apply(criteria.getInvoiceMessage()) &&
                condition.apply(criteria.getExternalId()) &&
                condition.apply(criteria.getEntryDate()) &&
                condition.apply(criteria.getGrandTotal()) &&
                condition.apply(criteria.getProjectName()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getInvoiceTypeId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getContactMechId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceCriteria> copyFiltersAre(InvoiceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceDate(), copy.getInvoiceDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getPaidDate(), copy.getPaidDate()) &&
                condition.apply(criteria.getReferenceNumber(), copy.getReferenceNumber()) &&
                condition.apply(criteria.getInvoiceMessage(), copy.getInvoiceMessage()) &&
                condition.apply(criteria.getExternalId(), copy.getExternalId()) &&
                condition.apply(criteria.getEntryDate(), copy.getEntryDate()) &&
                condition.apply(criteria.getGrandTotal(), copy.getGrandTotal()) &&
                condition.apply(criteria.getProjectName(), copy.getProjectName()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getInvoiceTypeId(), copy.getInvoiceTypeId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId(), copy.getToPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getContactMechId(), copy.getContactMechId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
