package com.snp.domain;

import static com.snp.domain.PartyIdentificationTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyIdentificationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyIdentificationType.class);
        PartyIdentificationType partyIdentificationType1 = getPartyIdentificationTypeSample1();
        PartyIdentificationType partyIdentificationType2 = new PartyIdentificationType();
        assertThat(partyIdentificationType1).isNotEqualTo(partyIdentificationType2);

        partyIdentificationType2.setId(partyIdentificationType1.getId());
        assertThat(partyIdentificationType1).isEqualTo(partyIdentificationType2);

        partyIdentificationType2 = getPartyIdentificationTypeSample2();
        assertThat(partyIdentificationType1).isNotEqualTo(partyIdentificationType2);
    }
}
