package com.snp.domain;

import static com.snp.domain.ContactMechPurposeTestSamples.*;
import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.PartyContactMechTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyContactMechTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyContactMech.class);
        PartyContactMech partyContactMech1 = getPartyContactMechSample1();
        PartyContactMech partyContactMech2 = new PartyContactMech();
        assertThat(partyContactMech1).isNotEqualTo(partyContactMech2);

        partyContactMech2.setId(partyContactMech1.getId());
        assertThat(partyContactMech1).isEqualTo(partyContactMech2);

        partyContactMech2 = getPartyContactMechSample2();
        assertThat(partyContactMech1).isNotEqualTo(partyContactMech2);
    }

    @Test
    void partyTest() {
        PartyContactMech partyContactMech = getPartyContactMechRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyContactMech.setParty(partyBack);
        assertThat(partyContactMech.getParty()).isEqualTo(partyBack);

        partyContactMech.party(null);
        assertThat(partyContactMech.getParty()).isNull();
    }

    @Test
    void contactMechTest() {
        PartyContactMech partyContactMech = getPartyContactMechRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        partyContactMech.setContactMech(contactMechBack);
        assertThat(partyContactMech.getContactMech()).isEqualTo(contactMechBack);

        partyContactMech.contactMech(null);
        assertThat(partyContactMech.getContactMech()).isNull();
    }

    @Test
    void contactMechPurposeTest() {
        PartyContactMech partyContactMech = getPartyContactMechRandomSampleGenerator();
        ContactMechPurpose contactMechPurposeBack = getContactMechPurposeRandomSampleGenerator();

        partyContactMech.setContactMechPurpose(contactMechPurposeBack);
        assertThat(partyContactMech.getContactMechPurpose()).isEqualTo(contactMechPurposeBack);

        partyContactMech.contactMechPurpose(null);
        assertThat(partyContactMech.getContactMechPurpose()).isNull();
    }
}
