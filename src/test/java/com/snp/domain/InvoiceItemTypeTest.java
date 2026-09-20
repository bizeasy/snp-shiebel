package com.snp.domain;

import static com.snp.domain.InvoiceItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItemType.class);
        InvoiceItemType invoiceItemType1 = getInvoiceItemTypeSample1();
        InvoiceItemType invoiceItemType2 = new InvoiceItemType();
        assertThat(invoiceItemType1).isNotEqualTo(invoiceItemType2);

        invoiceItemType2.setId(invoiceItemType1.getId());
        assertThat(invoiceItemType1).isEqualTo(invoiceItemType2);

        invoiceItemType2 = getInvoiceItemTypeSample2();
        assertThat(invoiceItemType1).isNotEqualTo(invoiceItemType2);
    }
}
