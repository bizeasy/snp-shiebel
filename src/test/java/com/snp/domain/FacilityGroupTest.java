package com.snp.domain;

import static com.snp.domain.FacilityGroupTestSamples.*;
import static com.snp.domain.FacilityGroupTestSamples.*;
import static com.snp.domain.FacilityGroupTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityGroup.class);
        FacilityGroup facilityGroup1 = getFacilityGroupSample1();
        FacilityGroup facilityGroup2 = new FacilityGroup();
        assertThat(facilityGroup1).isNotEqualTo(facilityGroup2);

        facilityGroup2.setId(facilityGroup1.getId());
        assertThat(facilityGroup1).isEqualTo(facilityGroup2);

        facilityGroup2 = getFacilityGroupSample2();
        assertThat(facilityGroup1).isNotEqualTo(facilityGroup2);
    }

    @Test
    void facilityGroupTypeTest() {
        FacilityGroup facilityGroup = getFacilityGroupRandomSampleGenerator();
        FacilityGroupType facilityGroupTypeBack = getFacilityGroupTypeRandomSampleGenerator();

        facilityGroup.setFacilityGroupType(facilityGroupTypeBack);
        assertThat(facilityGroup.getFacilityGroupType()).isEqualTo(facilityGroupTypeBack);

        facilityGroup.facilityGroupType(null);
        assertThat(facilityGroup.getFacilityGroupType()).isNull();
    }

    @Test
    void parentFacilityGroupTest() {
        FacilityGroup facilityGroup = getFacilityGroupRandomSampleGenerator();
        FacilityGroup facilityGroupBack = getFacilityGroupRandomSampleGenerator();

        facilityGroup.setParentFacilityGroup(facilityGroupBack);
        assertThat(facilityGroup.getParentFacilityGroup()).isEqualTo(facilityGroupBack);

        facilityGroup.parentFacilityGroup(null);
        assertThat(facilityGroup.getParentFacilityGroup()).isNull();
    }
}
