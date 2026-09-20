package com.snp.domain;

import static com.snp.domain.PartyRoleTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyRole.class);
        PartyRole partyRole1 = getPartyRoleSample1();
        PartyRole partyRole2 = new PartyRole();
        assertThat(partyRole1).isNotEqualTo(partyRole2);

        partyRole2.setId(partyRole1.getId());
        assertThat(partyRole1).isEqualTo(partyRole2);

        partyRole2 = getPartyRoleSample2();
        assertThat(partyRole1).isNotEqualTo(partyRole2);
    }

    @Test
    void partyTest() {
        PartyRole partyRole = getPartyRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyRole.setParty(partyBack);
        assertThat(partyRole.getParty()).isEqualTo(partyBack);

        partyRole.party(null);
        assertThat(partyRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        PartyRole partyRole = getPartyRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        partyRole.setRoleType(roleTypeBack);
        assertThat(partyRole.getRoleType()).isEqualTo(roleTypeBack);

        partyRole.roleType(null);
        assertThat(partyRole.getRoleType()).isNull();
    }
}
