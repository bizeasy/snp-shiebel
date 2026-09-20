package com.snp.domain;

import static com.snp.domain.FacilityTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PickListTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PickListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PickList.class);
        PickList pickList1 = getPickListSample1();
        PickList pickList2 = new PickList();
        assertThat(pickList1).isNotEqualTo(pickList2);

        pickList2.setId(pickList1.getId());
        assertThat(pickList1).isEqualTo(pickList2);

        pickList2 = getPickListSample2();
        assertThat(pickList1).isNotEqualTo(pickList2);
    }

    @Test
    void facilityTest() {
        PickList pickList = getPickListRandomSampleGenerator();
        Facility facilityBack = getFacilityRandomSampleGenerator();

        pickList.setFacility(facilityBack);
        assertThat(pickList.getFacility()).isEqualTo(facilityBack);

        pickList.facility(null);
        assertThat(pickList.getFacility()).isNull();
    }

    @Test
    void statusTest() {
        PickList pickList = getPickListRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        pickList.setStatus(statusBack);
        assertThat(pickList.getStatus()).isEqualTo(statusBack);

        pickList.status(null);
        assertThat(pickList.getStatus()).isNull();
    }

    @Test
    void partyTest() {
        PickList pickList = getPickListRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        pickList.setParty(partyBack);
        assertThat(pickList.getParty()).isEqualTo(partyBack);

        pickList.party(null);
        assertThat(pickList.getParty()).isNull();
    }
}
