package com.snp.domain;

import static com.snp.domain.InventoryItemTypeTestSamples.*;
import static com.snp.domain.ProductCategoryTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ProductTypeTestSamples.*;
import static com.snp.domain.TaxSlabTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void productTypeTest() {
        Product product = getProductRandomSampleGenerator();
        ProductType productTypeBack = getProductTypeRandomSampleGenerator();

        product.setProductType(productTypeBack);
        assertThat(product.getProductType()).isEqualTo(productTypeBack);

        product.productType(null);
        assertThat(product.getProductType()).isNull();
    }

    @Test
    void primaryProductCategoryTest() {
        Product product = getProductRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        product.setPrimaryProductCategory(productCategoryBack);
        assertThat(product.getPrimaryProductCategory()).isEqualTo(productCategoryBack);

        product.primaryProductCategory(null);
        assertThat(product.getPrimaryProductCategory()).isNull();
    }

    @Test
    void virtualProductTest() {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.setVirtualProduct(productBack);
        assertThat(product.getVirtualProduct()).isEqualTo(productBack);

        product.virtualProduct(null);
        assertThat(product.getVirtualProduct()).isNull();
    }

    @Test
    void inventoryItemTypeTest() {
        Product product = getProductRandomSampleGenerator();
        InventoryItemType inventoryItemTypeBack = getInventoryItemTypeRandomSampleGenerator();

        product.setInventoryItemType(inventoryItemTypeBack);
        assertThat(product.getInventoryItemType()).isEqualTo(inventoryItemTypeBack);

        product.inventoryItemType(null);
        assertThat(product.getInventoryItemType()).isNull();
    }

    @Test
    void taxSlabTest() {
        Product product = getProductRandomSampleGenerator();
        TaxSlab taxSlabBack = getTaxSlabRandomSampleGenerator();

        product.setTaxSlab(taxSlabBack);
        assertThat(product.getTaxSlab()).isEqualTo(taxSlabBack);

        product.taxSlab(null);
        assertThat(product.getTaxSlab()).isNull();
    }

    @Test
    void uomTest() {
        Product product = getProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        product.setUom(uomBack);
        assertThat(product.getUom()).isEqualTo(uomBack);

        product.uom(null);
        assertThat(product.getUom()).isNull();
    }

    @Test
    void quantityUomTest() {
        Product product = getProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        product.setQuantityUom(uomBack);
        assertThat(product.getQuantityUom()).isEqualTo(uomBack);

        product.quantityUom(null);
        assertThat(product.getQuantityUom()).isNull();
    }

    @Test
    void weightUomTest() {
        Product product = getProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        product.setWeightUom(uomBack);
        assertThat(product.getWeightUom()).isEqualTo(uomBack);

        product.weightUom(null);
        assertThat(product.getWeightUom()).isNull();
    }

    @Test
    void sizeUomTest() {
        Product product = getProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        product.setSizeUom(uomBack);
        assertThat(product.getSizeUom()).isEqualTo(uomBack);

        product.sizeUom(null);
        assertThat(product.getSizeUom()).isNull();
    }
}
