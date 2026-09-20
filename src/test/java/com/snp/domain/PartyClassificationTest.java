package com.snp.domain;

import static com.snp.domain.PartyClassificationGroupTestSamples.*;
import static com.snp.domain.PartyClassificationTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyClassificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyClassification.class);
        PartyClassification partyClassification1 = getPartyClassificationSample1();
        PartyClassification partyClassification2 = new PartyClassification();
        assertThat(partyClassification1).isNotEqualTo(partyClassification2);

        partyClassification2.setId(partyClassification1.getId());
        assertThat(partyClassification1).isEqualTo(partyClassification2);

        partyClassification2 = getPartyClassificationSample2();
        assertThat(partyClassification1).isNotEqualTo(partyClassification2);
    }

    @Test
    void partyTest() {
        PartyClassification partyClassification = getPartyClassificationRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        partyClassification.setParty(partyBack);
        assertThat(partyClassification.getParty()).isEqualTo(partyBack);

        partyClassification.party(null);
        assertThat(partyClassification.getParty()).isNull();
    }

    @Test
    void classificationGroupTest() {
        PartyClassification partyClassification = getPartyClassificationRandomSampleGenerator();
        PartyClassificationGroup partyClassificationGroupBack = getPartyClassificationGroupRandomSampleGenerator();

        partyClassification.setClassificationGroup(partyClassificationGroupBack);
        assertThat(partyClassification.getClassificationGroup()).isEqualTo(partyClassificationGroupBack);

        partyClassification.classificationGroup(null);
        assertThat(partyClassification.getClassificationGroup()).isNull();
    }
}
