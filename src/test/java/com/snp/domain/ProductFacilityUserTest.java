package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductFacilityTestSamples.*;
import static com.snp.domain.ProductFacilityUserTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFacilityUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFacilityUser.class);
        ProductFacilityUser productFacilityUser1 = getProductFacilityUserSample1();
        ProductFacilityUser productFacilityUser2 = new ProductFacilityUser();
        assertThat(productFacilityUser1).isNotEqualTo(productFacilityUser2);

        productFacilityUser2.setId(productFacilityUser1.getId());
        assertThat(productFacilityUser1).isEqualTo(productFacilityUser2);

        productFacilityUser2 = getProductFacilityUserSample2();
        assertThat(productFacilityUser1).isNotEqualTo(productFacilityUser2);
    }

    @Test
    void productFacilityTest() {
        ProductFacilityUser productFacilityUser = getProductFacilityUserRandomSampleGenerator();
        ProductFacility productFacilityBack = getProductFacilityRandomSampleGenerator();

        productFacilityUser.setProductFacility(productFacilityBack);
        assertThat(productFacilityUser.getProductFacility()).isEqualTo(productFacilityBack);

        productFacilityUser.productFacility(null);
        assertThat(productFacilityUser.getProductFacility()).isNull();
    }

    @Test
    void partyTest() {
        ProductFacilityUser productFacilityUser = getProductFacilityUserRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        productFacilityUser.setParty(partyBack);
        assertThat(productFacilityUser.getParty()).isEqualTo(partyBack);

        productFacilityUser.party(null);
        assertThat(productFacilityUser.getParty()).isNull();
    }

    @Test
    void statusTest() {
        ProductFacilityUser productFacilityUser = getProductFacilityUserRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        productFacilityUser.setStatus(statusBack);
        assertThat(productFacilityUser.getStatus()).isEqualTo(statusBack);

        productFacilityUser.status(null);
        assertThat(productFacilityUser.getStatus()).isNull();
    }
}
