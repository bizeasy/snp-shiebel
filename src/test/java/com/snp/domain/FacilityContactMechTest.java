package com.snp.domain;

import static com.snp.domain.ContactMechPurposeTestSamples.*;
import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.FacilityContactMechTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityContactMechTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityContactMech.class);
        FacilityContactMech facilityContactMech1 = getFacilityContactMechSample1();
        FacilityContactMech facilityContactMech2 = new FacilityContactMech();
        assertThat(facilityContactMech1).isNotEqualTo(facilityContactMech2);

        facilityContactMech2.setId(facilityContactMech1.getId());
        assertThat(facilityContactMech1).isEqualTo(facilityContactMech2);

        facilityContactMech2 = getFacilityContactMechSample2();
        assertThat(facilityContactMech1).isNotEqualTo(facilityContactMech2);
    }

    @Test
    void facilityTest() {
        FacilityContactMech facilityContactMech = getFacilityContactMechRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facilityContactMech.setFacility(facilityBack);
        assertThat(facilityContactMech.getFacility()).isEqualTo(facilityBack);

        facilityContactMech.facility(null);
        assertThat(facilityContactMech.getFacility()).isNull();
    }

    @Test
    void contactMechTest() {
        FacilityContactMech facilityContactMech = getFacilityContactMechRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        facilityContactMech.setContactMech(contactMechBack);
        assertThat(facilityContactMech.getContactMech()).isEqualTo(contactMechBack);

        facilityContactMech.contactMech(null);
        assertThat(facilityContactMech.getContactMech()).isNull();
    }

    @Test
    void contactMechPurposeTest() {
        FacilityContactMech facilityContactMech = getFacilityContactMechRandomSampleGenerator();
        ContactMechPurpose contactMechPurposeBack = getContactMechPurposeRandomSampleGenerator();

        facilityContactMech.setContactMechPurpose(contactMechPurposeBack);
        assertThat(facilityContactMech.getContactMechPurpose()).isEqualTo(contactMechPurposeBack);

        facilityContactMech.contactMechPurpose(null);
        assertThat(facilityContactMech.getContactMechPurpose()).isNull();
    }
}
