package com.snp.domain;

import static com.snp.domain.OtpAuthTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpAuthTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtpAuth.class);
        OtpAuth otpAuth1 = getOtpAuthSample1();
        OtpAuth otpAuth2 = new OtpAuth();
        assertThat(otpAuth1).isNotEqualTo(otpAuth2);

        otpAuth2.setId(otpAuth1.getId());
        assertThat(otpAuth1).isEqualTo(otpAuth2);

        otpAuth2 = getOtpAuthSample2();
        assertThat(otpAuth1).isNotEqualTo(otpAuth2);
    }

    @Test
    void partyTest() {
        OtpAuth otpAuth = getOtpAuthRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        otpAuth.setParty(partyBack);
        assertThat(otpAuth.getParty()).isEqualTo(partyBack);

        otpAuth.party(null);
        assertThat(otpAuth.getParty()).isNull();
    }
}
