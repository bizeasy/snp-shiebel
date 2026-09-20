package com.snp.domain;

import static com.snp.domain.PartyRelationshipTestSamples.*;
import static com.snp.domain.PartyRelationshipTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyRelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyRelationship.class);
        PartyRelationship partyRelationship1 = getPartyRelationshipSample1();
        PartyRelationship partyRelationship2 = new PartyRelationship();
        assertThat(partyRelationship1).isNotEqualTo(partyRelationship2);

        partyRelationship2.setId(partyRelationship1.getId());
        assertThat(partyRelationship1).isEqualTo(partyRelationship2);

        partyRelationship2 = getPartyRelationshipSample2();
        assertThat(partyRelationship1).isNotEqualTo(partyRelationship2);
    }

    @Test
    void partyFromTest() {
        PartyRelationship partyRelationship = getPartyRelationshipRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyRelationship.setPartyFrom(partyBack);
        assertThat(partyRelationship.getPartyFrom()).isEqualTo(partyBack);

        partyRelationship.partyFrom(null);
        assertThat(partyRelationship.getPartyFrom()).isNull();
    }

    @Test
    void partyToTest() {
        PartyRelationship partyRelationship = getPartyRelationshipRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyRelationship.setPartyTo(partyBack);
        assertThat(partyRelationship.getPartyTo()).isEqualTo(partyBack);

        partyRelationship.partyTo(null);
        assertThat(partyRelationship.getPartyTo()).isNull();
    }

    @Test
    void roleTypeFromTest() {
        PartyRelationship partyRelationship = getPartyRelationshipRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        partyRelationship.setRoleTypeFrom(roleTypeBack);
        assertThat(partyRelationship.getRoleTypeFrom()).isEqualTo(roleTypeBack);

        partyRelationship.roleTypeFrom(null);
        assertThat(partyRelationship.getRoleTypeFrom()).isNull();
    }

    @Test
    void roleTypeToTest() {
        PartyRelationship partyRelationship = getPartyRelationshipRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        partyRelationship.setRoleTypeTo(roleTypeBack);
        assertThat(partyRelationship.getRoleTypeTo()).isEqualTo(roleTypeBack);

        partyRelationship.roleTypeTo(null);
        assertThat(partyRelationship.getRoleTypeTo()).isNull();
    }

    @Test
    void relationshipTypeTest() {
        PartyRelationship partyRelationship = getPartyRelationshipRandomSampleGenerator();
        PartyRelationshipType partyRelationshipTypeBack = getPartyRelationshipTypeRandomSampleGenerator();

        partyRelationship.setRelationshipType(partyRelationshipTypeBack);
        assertThat(partyRelationship.getRelationshipType()).isEqualTo(partyRelationshipTypeBack);

        partyRelationship.relationshipType(null);
        assertThat(partyRelationship.getRelationshipType()).isNull();
    }
}
