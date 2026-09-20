package com.snp.domain;

import static com.snp.domain.CashBookTestSamples.*;
import static com.snp.domain.DepartmentTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PaymentMethodTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.TransactionCategoryTestSamples.*;
import static com.snp.domain.TransactionTypeTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashBookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBook.class);
        CashBook cashBook1 = getCashBookSample1();
        CashBook cashBook2 = new CashBook();
        assertThat(cashBook1).isNotEqualTo(cashBook2);

        cashBook2.setId(cashBook1.getId());
        assertThat(cashBook1).isEqualTo(cashBook2);

        cashBook2 = getCashBookSample2();
        assertThat(cashBook1).isNotEqualTo(cashBook2);
    }

    @Test
    void transactionTypeTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        TransactionType transactionTypeBack = getTransactionTypeRandomSampleGenerator();

        cashBook.setTransactionType(transactionTypeBack);
        assertThat(cashBook.getTransactionType()).isEqualTo(transactionTypeBack);

        cashBook.transactionType(null);
        assertThat(cashBook.getTransactionType()).isNull();
    }

    @Test
    void transactionCategoryTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        TransactionCategory transactionCategoryBack = getTransactionCategoryRandomSampleGenerator();

        cashBook.setTransactionCategory(transactionCategoryBack);
        assertThat(cashBook.getTransactionCategory()).isEqualTo(transactionCategoryBack);

        cashBook.transactionCategory(null);
        assertThat(cashBook.getTransactionCategory()).isNull();
    }

    @Test
    void paymentMethodTypeTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        PaymentMethodType paymentMethodTypeBack = getPaymentMethodTypeRandomSampleGenerator();

        cashBook.setPaymentMethodType(paymentMethodTypeBack);
        assertThat(cashBook.getPaymentMethodType()).isEqualTo(paymentMethodTypeBack);

        cashBook.paymentMethodType(null);
        assertThat(cashBook.getPaymentMethodType()).isNull();
    }

    @Test
    void departmentTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        cashBook.setDepartment(departmentBack);
        assertThat(cashBook.getDepartment()).isEqualTo(departmentBack);

        cashBook.department(null);
        assertThat(cashBook.getDepartment()).isNull();
    }

    @Test
    void currencyUomTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        cashBook.setCurrencyUom(uomBack);
        assertThat(cashBook.getCurrencyUom()).isEqualTo(uomBack);

        cashBook.currencyUom(null);
        assertThat(cashBook.getCurrencyUom()).isNull();
    }

    @Test
    void statusTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        cashBook.setStatus(statusBack);
        assertThat(cashBook.getStatus()).isEqualTo(statusBack);

        cashBook.status(null);
        assertThat(cashBook.getStatus()).isNull();
    }

    @Test
    void orderTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        cashBook.setOrder(orderBack);
        assertThat(cashBook.getOrder()).isEqualTo(orderBack);

        cashBook.order(null);
        assertThat(cashBook.getOrder()).isNull();
    }

    @Test
    void invoiceTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        cashBook.setInvoice(invoiceBack);
        assertThat(cashBook.getInvoice()).isEqualTo(invoiceBack);

        cashBook.invoice(null);
        assertThat(cashBook.getInvoice()).isNull();
    }

    @Test
    void fromPartyTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        cashBook.setFromParty(partyBack);
        assertThat(cashBook.getFromParty()).isEqualTo(partyBack);

        cashBook.fromParty(null);
        assertThat(cashBook.getFromParty()).isNull();
    }

    @Test
    void toPartyTest() {
        CashBook cashBook = getCashBookRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        cashBook.setToParty(partyBack);
        assertThat(cashBook.getToParty()).isEqualTo(partyBack);

        cashBook.toParty(null);
        assertThat(cashBook.getToParty()).isNull();
    }
}
