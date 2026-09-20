package com.snp.domain;

import static com.snp.domain.PartyClassificationTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyClassificationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyClassificationType.class);
        PartyClassificationType partyClassificationType1 = getPartyClassificationTypeSample1();
        PartyClassificationType partyClassificationType2 = new PartyClassificationType();
        assertThat(partyClassificationType1).isNotEqualTo(partyClassificationType2);

        partyClassificationType2.setId(partyClassificationType1.getId());
        assertThat(partyClassificationType1).isEqualTo(partyClassificationType2);

        partyClassificationType2 = getPartyClassificationTypeSample2();
        assertThat(partyClassificationType1).isNotEqualTo(partyClassificationType2);
    }
}
