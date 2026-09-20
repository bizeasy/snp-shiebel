package com.snp.domain;

import static com.snp.domain.FacilityLocationTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.LocationTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityLocation.class);
        FacilityLocation facilityLocation1 = getFacilityLocationSample1();
        FacilityLocation facilityLocation2 = new FacilityLocation();
        assertThat(facilityLocation1).isNotEqualTo(facilityLocation2);

        facilityLocation2.setId(facilityLocation1.getId());
        assertThat(facilityLocation1).isEqualTo(facilityLocation2);

        facilityLocation2 = getFacilityLocationSample2();
        assertThat(facilityLocation1).isNotEqualTo(facilityLocation2);
    }

    @Test
    void facilityTest() {
        FacilityLocation facilityLocation = getFacilityLocationRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facilityLocation.setFacility(facilityBack);
        assertThat(facilityLocation.getFacility()).isEqualTo(facilityBack);

        facilityLocation.facility(null);
        assertThat(facilityLocation.getFacility()).isNull();
    }

    @Test
    void locationTypeTest() {
        FacilityLocation facilityLocation = getFacilityLocationRandomSampleGenerator();
        LocationType locationTypeBack = getLocationTypeRandomSampleGenerator();

        facilityLocation.setLocationType(locationTypeBack);
        assertThat(facilityLocation.getLocationType()).isEqualTo(locationTypeBack);

        facilityLocation.locationType(null);
        assertThat(facilityLocation.getLocationType()).isNull();
    }
}
