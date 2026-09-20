package com.snp.domain;

import static com.snp.domain.FacilityGroupMemberTestSamples.*;
import static com.snp.domain.FacilityGroupTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityGroupMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityGroupMember.class);
        FacilityGroupMember facilityGroupMember1 = getFacilityGroupMemberSample1();
        FacilityGroupMember facilityGroupMember2 = new FacilityGroupMember();
        assertThat(facilityGroupMember1).isNotEqualTo(facilityGroupMember2);

        facilityGroupMember2.setId(facilityGroupMember1.getId());
        assertThat(facilityGroupMember1).isEqualTo(facilityGroupMember2);

        facilityGroupMember2 = getFacilityGroupMemberSample2();
        assertThat(facilityGroupMember1).isNotEqualTo(facilityGroupMember2);
    }

    @Test
    void facilityTest() {
        FacilityGroupMember facilityGroupMember = getFacilityGroupMemberRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facilityGroupMember.setFacility(facilityBack);
        assertThat(facilityGroupMember.getFacility()).isEqualTo(facilityBack);

        facilityGroupMember.facility(null);
        assertThat(facilityGroupMember.getFacility()).isNull();
    }

    @Test
    void facilityGroupTest() {
        FacilityGroupMember facilityGroupMember = getFacilityGroupMemberRandomSampleGenerator();
        FacilityGroup facilityGroupBack = getFacilityGroupRandomSampleGenerator();

        facilityGroupMember.setFacilityGroup(facilityGroupBack);
        assertThat(facilityGroupMember.getFacilityGroup()).isEqualTo(facilityGroupBack);

        facilityGroupMember.facilityGroup(null);
        assertThat(facilityGroupMember.getFacilityGroup()).isNull();
    }
}
