package com.snp.domain;

import static com.snp.domain.PartyClassificationGroupTestSamples.*;
import static com.snp.domain.PartyClassificationTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyClassificationGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyClassificationGroup.class);
        PartyClassificationGroup partyClassificationGroup1 = getPartyClassificationGroupSample1();
        PartyClassificationGroup partyClassificationGroup2 = new PartyClassificationGroup();
        assertThat(partyClassificationGroup1).isNotEqualTo(partyClassificationGroup2);

        partyClassificationGroup2.setId(partyClassificationGroup1.getId());
        assertThat(partyClassificationGroup1).isEqualTo(partyClassificationGroup2);

        partyClassificationGroup2 = getPartyClassificationGroupSample2();
        assertThat(partyClassificationGroup1).isNotEqualTo(partyClassificationGroup2);
    }

    @Test
    void classificationTypeTest() {
        PartyClassificationGroup partyClassificationGroup = getPartyClassificationGroupRandomSampleGenerator();
        PartyClassificationType partyClassificationTypeBack = getPartyClassificationTypeRandomSampleGenerator();

        partyClassificationGroup.setClassificationType(partyClassificationTypeBack);
        assertThat(partyClassificationGroup.getClassificationType()).isEqualTo(partyClassificationTypeBack);

        partyClassificationGroup.classificationType(null);
        assertThat(partyClassificationGroup.getClassificationType()).isNull();
    }
}
