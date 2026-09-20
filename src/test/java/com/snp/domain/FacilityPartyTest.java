package com.snp.domain;

import static com.snp.domain.FacilityPartyTestSamples.*;
import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityPartyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityParty.class);
        FacilityParty facilityParty1 = getFacilityPartySample1();
        FacilityParty facilityParty2 = new FacilityParty();
        assertThat(facilityParty1).isNotEqualTo(facilityParty2);

        facilityParty2.setId(facilityParty1.getId());
        assertThat(facilityParty1).isEqualTo(facilityParty2);

        facilityParty2 = getFacilityPartySample2();
        assertThat(facilityParty1).isNotEqualTo(facilityParty2);
    }

    @Test
    void facilityTest() {
        FacilityParty facilityParty = getFacilityPartyRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        facilityParty.setFacility(facilityBack);
        assertThat(facilityParty.getFacility()).isEqualTo(facilityBack);

        facilityParty.facility(null);
        assertThat(facilityParty.getFacility()).isNull();
    }

    @Test
    void partyTest() {
        FacilityParty facilityParty = getFacilityPartyRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        facilityParty.setParty(partyBack);
        assertThat(facilityParty.getParty()).isEqualTo(partyBack);

        facilityParty.party(null);
        assertThat(facilityParty.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        FacilityParty facilityParty = getFacilityPartyRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        facilityParty.setRoleType(roleTypeBack);
        assertThat(facilityParty.getRoleType()).isEqualTo(roleTypeBack);

        facilityParty.roleType(null);
        assertThat(facilityParty.getRoleType()).isNull();
    }
}
