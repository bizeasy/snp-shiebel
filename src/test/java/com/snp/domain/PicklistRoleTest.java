package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.PickListTestSamples.*;
import static com.snp.domain.PicklistRoleTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PicklistRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PicklistRole.class);
        PicklistRole picklistRole1 = getPicklistRoleSample1();
        PicklistRole picklistRole2 = new PicklistRole();
        assertThat(picklistRole1).isNotEqualTo(picklistRole2);

        picklistRole2.setId(picklistRole1.getId());
        assertThat(picklistRole1).isEqualTo(picklistRole2);

        picklistRole2 = getPicklistRoleSample2();
        assertThat(picklistRole1).isNotEqualTo(picklistRole2);
    }

    @Test
    void pickListTest() {
        PicklistRole picklistRole = getPicklistRoleRandomSampleGenerator();
        PickList pickListBack = getPickListRandomSampleGenerator();

        picklistRole.setPickList(pickListBack);
        assertThat(picklistRole.getPickList()).isEqualTo(pickListBack);

        picklistRole.pickList(null);
        assertThat(picklistRole.getPickList()).isNull();
    }

    @Test
    void partyTest() {
        PicklistRole picklistRole = getPicklistRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        picklistRole.setParty(partyBack);
        assertThat(picklistRole.getParty()).isEqualTo(partyBack);

        picklistRole.party(null);
        assertThat(picklistRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        PicklistRole picklistRole = getPicklistRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        picklistRole.setRoleType(roleTypeBack);
        assertThat(picklistRole.getRoleType()).isEqualTo(roleTypeBack);

        picklistRole.roleType(null);
        assertThat(picklistRole.getRoleType()).isNull();
    }
}
