package com.snp.domain;

import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.InvoiceTypeTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void invoiceTypeTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        InvoiceType invoiceTypeBack = getInvoiceTypeRandomSampleGenerator();

        invoice.setInvoiceType(invoiceTypeBack);
        assertThat(invoice.getInvoiceType()).isEqualTo(invoiceTypeBack);

        invoice.invoiceType(null);
        assertThat(invoice.getInvoiceType()).isNull();
    }

    @Test
    void orderTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        invoice.setOrder(orderBack);
        assertThat(invoice.getOrder()).isEqualTo(orderBack);

        invoice.order(null);
        assertThat(invoice.getOrder()).isNull();
    }

    @Test
    void fromPartyTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        invoice.setFromParty(partyBack);
        assertThat(invoice.getFromParty()).isEqualTo(partyBack);

        invoice.fromParty(null);
        assertThat(invoice.getFromParty()).isNull();
    }

    @Test
    void toPartyTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        invoice.setToParty(partyBack);
        assertThat(invoice.getToParty()).isEqualTo(partyBack);

        invoice.toParty(null);
        assertThat(invoice.getToParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        invoice.setRoleType(roleTypeBack);
        assertThat(invoice.getRoleType()).isEqualTo(roleTypeBack);

        invoice.roleType(null);
        assertThat(invoice.getRoleType()).isNull();
    }

    @Test
    void statusTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        invoice.setStatus(statusBack);
        assertThat(invoice.getStatus()).isEqualTo(statusBack);

        invoice.status(null);
        assertThat(invoice.getStatus()).isNull();
    }

    @Test
    void contactMechTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        invoice.setContactMech(contactMechBack);
        assertThat(invoice.getContactMech()).isEqualTo(contactMechBack);

        invoice.contactMech(null);
        assertThat(invoice.getContactMech()).isNull();
    }

    @Test
    void currencyUomTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        invoice.setCurrencyUom(uomBack);
        assertThat(invoice.getCurrencyUom()).isEqualTo(uomBack);

        invoice.currencyUom(null);
        assertThat(invoice.getCurrencyUom()).isNull();
    }
}
