package com.snp.domain;

import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.InvoiceItemTestSamples.*;
import static com.snp.domain.InvoiceItemTypeTestSamples.*;
import static com.snp.domain.InvoiceTestSamples.*;
import static com.snp.domain.ProductFacilityTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.TaxAuthorityRateTypeTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItem.class);
        InvoiceItem invoiceItem1 = getInvoiceItemSample1();
        InvoiceItem invoiceItem2 = new InvoiceItem();
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);

        invoiceItem2.setId(invoiceItem1.getId());
        assertThat(invoiceItem1).isEqualTo(invoiceItem2);

        invoiceItem2 = getInvoiceItemSample2();
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
    }

    @Test
    void invoiceTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        invoiceItem.setInvoice(invoiceBack);
        assertThat(invoiceItem.getInvoice()).isEqualTo(invoiceBack);

        invoiceItem.invoice(null);
        assertThat(invoiceItem.getInvoice()).isNull();
    }

    @Test
    void invoiceItemTypeTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        InvoiceItemType invoiceItemTypeBack = getInvoiceItemTypeRandomSampleGenerator();

        invoiceItem.setInvoiceItemType(invoiceItemTypeBack);
        assertThat(invoiceItem.getInvoiceItemType()).isEqualTo(invoiceItemTypeBack);

        invoiceItem.invoiceItemType(null);
        assertThat(invoiceItem.getInvoiceItemType()).isNull();
    }

    @Test
    void productTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        invoiceItem.setProduct(productBack);
        assertThat(invoiceItem.getProduct()).isEqualTo(productBack);

        invoiceItem.product(null);
        assertThat(invoiceItem.getProduct()).isNull();
    }

    @Test
    void uomTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        invoiceItem.setUom(uomBack);
        assertThat(invoiceItem.getUom()).isEqualTo(uomBack);

        invoiceItem.uom(null);
        assertThat(invoiceItem.getUom()).isNull();
    }

    @Test
    void productFeatureTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        invoiceItem.setProductFeature(productFeatureBack);
        assertThat(invoiceItem.getProductFeature()).isEqualTo(productFeatureBack);

        invoiceItem.productFeature(null);
        assertThat(invoiceItem.getProductFeature()).isNull();
    }

    @Test
    void productFacilityTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        ProductFacility productFacilityBack = getProductFacilityRandomSampleGenerator();

        invoiceItem.setProductFacility(productFacilityBack);
        assertThat(invoiceItem.getProductFacility()).isEqualTo(productFacilityBack);

        invoiceItem.productFacility(null);
        assertThat(invoiceItem.getProductFacility()).isNull();
    }

    @Test
    void fromInventoryItemTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        invoiceItem.setFromInventoryItem(inventoryItemBack);
        assertThat(invoiceItem.getFromInventoryItem()).isEqualTo(inventoryItemBack);

        invoiceItem.fromInventoryItem(null);
        assertThat(invoiceItem.getFromInventoryItem()).isNull();
    }

    @Test
    void taxAuthRateTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        TaxAuthorityRateType taxAuthorityRateTypeBack = getTaxAuthorityRateTypeRandomSampleGenerator();

        invoiceItem.setTaxAuthRate(taxAuthorityRateTypeBack);
        assertThat(invoiceItem.getTaxAuthRate()).isEqualTo(taxAuthorityRateTypeBack);

        invoiceItem.taxAuthRate(null);
        assertThat(invoiceItem.getTaxAuthRate()).isNull();
    }

    @Test
    void parentInvoiceTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        invoiceItem.setParentInvoice(invoiceBack);
        assertThat(invoiceItem.getParentInvoice()).isEqualTo(invoiceBack);

        invoiceItem.parentInvoice(null);
        assertThat(invoiceItem.getParentInvoice()).isNull();
    }

    @Test
    void parentInvoiceItemTest() {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        invoiceItem.setParentInvoiceItem(invoiceItemBack);
        assertThat(invoiceItem.getParentInvoiceItem()).isEqualTo(invoiceItemBack);

        invoiceItem.parentInvoiceItem(null);
        assertThat(invoiceItem.getParentInvoiceItem()).isNull();
    }
}
