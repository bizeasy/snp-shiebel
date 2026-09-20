package com.snp.domain;

import static com.snp.domain.InvoiceItemTypeMapTestSamples.*;
import static com.snp.domain.InvoiceItemTypeTestSamples.*;
import static com.snp.domain.InvoiceTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemTypeMapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItemTypeMap.class);
        InvoiceItemTypeMap invoiceItemTypeMap1 = getInvoiceItemTypeMapSample1();
        InvoiceItemTypeMap invoiceItemTypeMap2 = new InvoiceItemTypeMap();
        assertThat(invoiceItemTypeMap1).isNotEqualTo(invoiceItemTypeMap2);

        invoiceItemTypeMap2.setId(invoiceItemTypeMap1.getId());
        assertThat(invoiceItemTypeMap1).isEqualTo(invoiceItemTypeMap2);

        invoiceItemTypeMap2 = getInvoiceItemTypeMapSample2();
        assertThat(invoiceItemTypeMap1).isNotEqualTo(invoiceItemTypeMap2);
    }

    @Test
    void invoiceTypeTest() {
        InvoiceItemTypeMap invoiceItemTypeMap = getInvoiceItemTypeMapRandomSampleGenerator();
        InvoiceType invoiceTypeBack = getInvoiceTypeRandomSampleGenerator();

        invoiceItemTypeMap.setInvoiceType(invoiceTypeBack);
        assertThat(invoiceItemTypeMap.getInvoiceType()).isEqualTo(invoiceTypeBack);

        invoiceItemTypeMap.invoiceType(null);
        assertThat(invoiceItemTypeMap.getInvoiceType()).isNull();
    }

    @Test
    void invoiceItemTypeTest() {
        InvoiceItemTypeMap invoiceItemTypeMap = getInvoiceItemTypeMapRandomSampleGenerator();
        InvoiceItemType invoiceItemTypeBack = getInvoiceItemTypeRandomSampleGenerator();

        invoiceItemTypeMap.setInvoiceItemType(invoiceItemTypeBack);
        assertThat(invoiceItemTypeMap.getInvoiceItemType()).isEqualTo(invoiceItemTypeBack);

        invoiceItemTypeMap.invoiceItemType(null);
        assertThat(invoiceItemTypeMap.getInvoiceItemType()).isNull();
    }
}
