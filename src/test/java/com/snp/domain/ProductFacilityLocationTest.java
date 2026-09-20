package com.snp.domain;

import static com.snp.domain.FacilityLocationTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.ProductFacilityLocationTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFacilityLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFacilityLocation.class);
        ProductFacilityLocation productFacilityLocation1 = getProductFacilityLocationSample1();
        ProductFacilityLocation productFacilityLocation2 = new ProductFacilityLocation();
        assertThat(productFacilityLocation1).isNotEqualTo(productFacilityLocation2);

        productFacilityLocation2.setId(productFacilityLocation1.getId());
        assertThat(productFacilityLocation1).isEqualTo(productFacilityLocation2);

        productFacilityLocation2 = getProductFacilityLocationSample2();
        assertThat(productFacilityLocation1).isNotEqualTo(productFacilityLocation2);
    }

    @Test
    void productTest() {
        ProductFacilityLocation productFacilityLocation = getProductFacilityLocationRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productFacilityLocation.setProduct(productBack);
        assertThat(productFacilityLocation.getProduct()).isEqualTo(productBack);

        productFacilityLocation.product(null);
        assertThat(productFacilityLocation.getProduct()).isNull();
    }

    @Test
    void facilityTest() {
        ProductFacilityLocation productFacilityLocation = getProductFacilityLocationRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        productFacilityLocation.setFacility(facilityBack);
        assertThat(productFacilityLocation.getFacility()).isEqualTo(facilityBack);

        productFacilityLocation.facility(null);
        assertThat(productFacilityLocation.getFacility()).isNull();
    }

    @Test
    void facilityLocationTest() {
        ProductFacilityLocation productFacilityLocation = getProductFacilityLocationRandomSampleGenerator();
        FacilityLocation facilityLocationBack = getFacilityLocationRandomSampleGenerator();

        productFacilityLocation.setFacilityLocation(facilityLocationBack);
        assertThat(productFacilityLocation.getFacilityLocation()).isEqualTo(facilityLocationBack);

        productFacilityLocation.facilityLocation(null);
        assertThat(productFacilityLocation.getFacilityLocation()).isNull();
    }
}
