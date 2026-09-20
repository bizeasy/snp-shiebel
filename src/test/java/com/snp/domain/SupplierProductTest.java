package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.SupplierProductTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierProduct.class);
        SupplierProduct supplierProduct1 = getSupplierProductSample1();
        SupplierProduct supplierProduct2 = new SupplierProduct();
        assertThat(supplierProduct1).isNotEqualTo(supplierProduct2);

        supplierProduct2.setId(supplierProduct1.getId());
        assertThat(supplierProduct1).isEqualTo(supplierProduct2);

        supplierProduct2 = getSupplierProductSample2();
        assertThat(supplierProduct1).isNotEqualTo(supplierProduct2);
    }

    @Test
    void productTest() {
        SupplierProduct supplierProduct = getSupplierProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        supplierProduct.setProduct(productBack);
        assertThat(supplierProduct.getProduct()).isEqualTo(productBack);

        supplierProduct.product(null);
        assertThat(supplierProduct.getProduct()).isNull();
    }

    @Test
    void partyTest() {
        SupplierProduct supplierProduct = getSupplierProductRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        supplierProduct.setParty(partyBack);
        assertThat(supplierProduct.getParty()).isEqualTo(partyBack);

        supplierProduct.party(null);
        assertThat(supplierProduct.getParty()).isNull();
    }

    @Test
    void quantityUomTest() {
        SupplierProduct supplierProduct = getSupplierProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        supplierProduct.setQuantityUom(uomBack);
        assertThat(supplierProduct.getQuantityUom()).isEqualTo(uomBack);

        supplierProduct.quantityUom(null);
        assertThat(supplierProduct.getQuantityUom()).isNull();
    }

    @Test
    void currencyUomTest() {
        SupplierProduct supplierProduct = getSupplierProductRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        supplierProduct.setCurrencyUom(uomBack);
        assertThat(supplierProduct.getCurrencyUom()).isEqualTo(uomBack);

        supplierProduct.currencyUom(null);
        assertThat(supplierProduct.getCurrencyUom()).isNull();
    }
}
