package com.snp.domain;

import static com.snp.domain.InvoiceTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceType.class);
        InvoiceType invoiceType1 = getInvoiceTypeSample1();
        InvoiceType invoiceType2 = new InvoiceType();
        assertThat(invoiceType1).isNotEqualTo(invoiceType2);

        invoiceType2.setId(invoiceType1.getId());
        assertThat(invoiceType1).isEqualTo(invoiceType2);

        invoiceType2 = getInvoiceTypeSample2();
        assertThat(invoiceType1).isNotEqualTo(invoiceType2);
    }
}
