package com.snp.domain;

import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.ProductFacilityTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFacilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFacility.class);
        ProductFacility productFacility1 = getProductFacilitySample1();
        ProductFacility productFacility2 = new ProductFacility();
        assertThat(productFacility1).isNotEqualTo(productFacility2);

        productFacility2.setId(productFacility1.getId());
        assertThat(productFacility1).isEqualTo(productFacility2);

        productFacility2 = getProductFacilitySample2();
        assertThat(productFacility1).isNotEqualTo(productFacility2);
    }

    @Test
    void productTest() {
        ProductFacility productFacility = getProductFacilityRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productFacility.setProduct(productBack);
        assertThat(productFacility.getProduct()).isEqualTo(productBack);

        productFacility.product(null);
        assertThat(productFacility.getProduct()).isNull();
    }

    @Test
    void facilityTest() {
        ProductFacility productFacility = getProductFacilityRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        productFacility.setFacility(facilityBack);
        assertThat(productFacility.getFacility()).isEqualTo(facilityBack);

        productFacility.facility(null);
        assertThat(productFacility.getFacility()).isNull();
    }
}
