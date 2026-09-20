package com.snp.domain;

import static com.snp.domain.PartyIdentificationTestSamples.*;
import static com.snp.domain.PartyIdentificationTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyIdentificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyIdentification.class);
        PartyIdentification partyIdentification1 = getPartyIdentificationSample1();
        PartyIdentification partyIdentification2 = new PartyIdentification();
        assertThat(partyIdentification1).isNotEqualTo(partyIdentification2);

        partyIdentification2.setId(partyIdentification1.getId());
        assertThat(partyIdentification1).isEqualTo(partyIdentification2);

        partyIdentification2 = getPartyIdentificationSample2();
        assertThat(partyIdentification1).isNotEqualTo(partyIdentification2);
    }

    @Test
    void partyTest() {
        PartyIdentification partyIdentification = getPartyIdentificationRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyIdentification.setParty(partyBack);
        assertThat(partyIdentification.getParty()).isEqualTo(partyBack);

        partyIdentification.party(null);
        assertThat(partyIdentification.getParty()).isNull();
    }

    @Test
    void typeTest() {
        PartyIdentification partyIdentification = getPartyIdentificationRandomSampleGenerator();
        PartyIdentificationType partyIdentificationTypeBack = getPartyIdentificationTypeRandomSampleGenerator();

        partyIdentification.setType(partyIdentificationTypeBack);
        assertThat(partyIdentification.getType()).isEqualTo(partyIdentificationTypeBack);

        partyIdentification.type(null);
        assertThat(partyIdentification.getType()).isNull();
    }
}
