package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CashBookCriteriaTest {

    @Test
    void newCashBookCriteriaHasAllFiltersNullTest() {
        var cashBookCriteria = new CashBookCriteria();
        assertThat(cashBookCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cashBookCriteriaFluentMethodsCreatesFiltersTest() {
        var cashBookCriteria = new CashBookCriteria();

        setAllFilters(cashBookCriteria);

        assertThat(cashBookCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cashBookCriteriaCopyCreatesNullFilterTest() {
        var cashBookCriteria = new CashBookCriteria();
        var copy = cashBookCriteria.copy();

        assertThat(cashBookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cashBookCriteria)
        );
    }

    @Test
    void cashBookCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cashBookCriteria = new CashBookCriteria();
        setAllFilters(cashBookCriteria);

        var copy = cashBookCriteria.copy();

        assertThat(cashBookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cashBookCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cashBookCriteria = new CashBookCriteria();

        assertThat(cashBookCriteria).hasToString("CashBookCriteria{}");
    }

    private static void setAllFilters(CashBookCriteria cashBookCriteria) {
        cashBookCriteria.id();
        cashBookCriteria.transactionDate();
        cashBookCriteria.dueDate();
        cashBookCriteria.amount();
        cashBookCriteria.description();
        cashBookCriteria.vendorCustomerName();
        cashBookCriteria.invoiceNo();
        cashBookCriteria.invoiceDate();
        cashBookCriteria.purpose();
        cashBookCriteria.remarks();
        cashBookCriteria.project();
        cashBookCriteria.usageUnits();
        cashBookCriteria.recurringTransaction();
        cashBookCriteria.frequency();
        cashBookCriteria.transactionTypeId();
        cashBookCriteria.transactionCategoryId();
        cashBookCriteria.paymentMethodTypeId();
        cashBookCriteria.departmentId();
        cashBookCriteria.currencyUomId();
        cashBookCriteria.statusId();
        cashBookCriteria.orderId();
        cashBookCriteria.invoiceId();
        cashBookCriteria.fromPartyId();
        cashBookCriteria.toPartyId();
        cashBookCriteria.distinct();
    }

    private static Condition<CashBookCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTransactionDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getVendorCustomerName()) &&
                condition.apply(criteria.getInvoiceNo()) &&
                condition.apply(criteria.getInvoiceDate()) &&
                condition.apply(criteria.getPurpose()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getProject()) &&
                condition.apply(criteria.getUsageUnits()) &&
                condition.apply(criteria.getRecurringTransaction()) &&
                condition.apply(criteria.getFrequency()) &&
                condition.apply(criteria.getTransactionTypeId()) &&
                condition.apply(criteria.getTransactionCategoryId()) &&
                condition.apply(criteria.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CashBookCriteria> copyFiltersAre(CashBookCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTransactionDate(), copy.getTransactionDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getVendorCustomerName(), copy.getVendorCustomerName()) &&
                condition.apply(criteria.getInvoiceNo(), copy.getInvoiceNo()) &&
                condition.apply(criteria.getInvoiceDate(), copy.getInvoiceDate()) &&
                condition.apply(criteria.getPurpose(), copy.getPurpose()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getProject(), copy.getProject()) &&
                condition.apply(criteria.getUsageUnits(), copy.getUsageUnits()) &&
                condition.apply(criteria.getRecurringTransaction(), copy.getRecurringTransaction()) &&
                condition.apply(criteria.getFrequency(), copy.getFrequency()) &&
                condition.apply(criteria.getTransactionTypeId(), copy.getTransactionTypeId()) &&
                condition.apply(criteria.getTransactionCategoryId(), copy.getTransactionCategoryId()) &&
                condition.apply(criteria.getPaymentMethodTypeId(), copy.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId(), copy.getToPartyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
