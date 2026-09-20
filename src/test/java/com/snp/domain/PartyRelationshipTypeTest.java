package com.snp.domain;

import static com.snp.domain.PartyRelationshipTypeTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyRelationshipTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyRelationshipType.class);
        PartyRelationshipType partyRelationshipType1 = getPartyRelationshipTypeSample1();
        PartyRelationshipType partyRelationshipType2 = new PartyRelationshipType();
        assertThat(partyRelationshipType1).isNotEqualTo(partyRelationshipType2);

        partyRelationshipType2.setId(partyRelationshipType1.getId());
        assertThat(partyRelationshipType1).isEqualTo(partyRelationshipType2);

        partyRelationshipType2 = getPartyRelationshipTypeSample2();
        assertThat(partyRelationshipType1).isNotEqualTo(partyRelationshipType2);
    }

    @Test
    void roleTypeFromTest() {
        PartyRelationshipType partyRelationshipType = getPartyRelationshipTypeRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        partyRelationshipType.setRoleTypeFrom(roleTypeBack);
        assertThat(partyRelationshipType.getRoleTypeFrom()).isEqualTo(roleTypeBack);

        partyRelationshipType.roleTypeFrom(null);
        assertThat(partyRelationshipType.getRoleTypeFrom()).isNull();
    }

    @Test
    void roleTypeToTest() {
        PartyRelationshipType partyRelationshipType = getPartyRelationshipTypeRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        partyRelationshipType.setRoleTypeTo(roleTypeBack);
        assertThat(partyRelationshipType.getRoleTypeTo()).isEqualTo(roleTypeBack);

        partyRelationshipType.roleTypeTo(null);
        assertThat(partyRelationshipType.getRoleTypeTo()).isNull();
    }
}
