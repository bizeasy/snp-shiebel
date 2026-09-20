package com.snp.domain;

import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.InvoiceRoleTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceRole.class);
        InvoiceRole invoiceRole1 = getInvoiceRoleSample1();
        InvoiceRole invoiceRole2 = new InvoiceRole();
        assertThat(invoiceRole1).isNotEqualTo(invoiceRole2);

        invoiceRole2.setId(invoiceRole1.getId());
        assertThat(invoiceRole1).isEqualTo(invoiceRole2);

        invoiceRole2 = getInvoiceRoleSample2();
        assertThat(invoiceRole1).isNotEqualTo(invoiceRole2);
    }

    @Test
    void invoiceTest() {
        InvoiceRole invoiceRole = getInvoiceRoleRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        invoiceRole.setInvoice(invoiceBack);
        assertThat(invoiceRole.getInvoice()).isEqualTo(invoiceBack);

        invoiceRole.invoice(null);
        assertThat(invoiceRole.getInvoice()).isNull();
    }

    @Test
    void invoiceItemTest() {
        InvoiceRole invoiceRole = getInvoiceRoleRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        invoiceRole.setInvoiceItem(invoiceItemBack);
        assertThat(invoiceRole.getInvoiceItem()).isEqualTo(invoiceItemBack);

        invoiceRole.invoiceItem(null);
        assertThat(invoiceRole.getInvoiceItem()).isNull();
    }

    @Test
    void partyTest() {
        InvoiceRole invoiceRole = getInvoiceRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        invoiceRole.setParty(partyBack);
        assertThat(invoiceRole.getParty()).isEqualTo(partyBack);

        invoiceRole.party(null);
        assertThat(invoiceRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        InvoiceRole invoiceRole = getInvoiceRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        invoiceRole.setRoleType(roleTypeBack);
        assertThat(invoiceRole.getRoleType()).isEqualTo(roleTypeBack);

        invoiceRole.roleType(null);
        assertThat(invoiceRole.getRoleType()).isNull();
    }
}
