package com.snp.domain;

import static com.snp.domain.CustRequestTypeTestSamples.*;
import static com.snp.domain.CustRequestTypeTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestType.class);
        CustRequestType custRequestType1 = getCustRequestTypeSample1();
        CustRequestType custRequestType2 = new CustRequestType();
        assertThat(custRequestType1).isNotEqualTo(custRequestType2);

        custRequestType2.setId(custRequestType1.getId());
        assertThat(custRequestType1).isEqualTo(custRequestType2);

        custRequestType2 = getCustRequestTypeSample2();
        assertThat(custRequestType1).isNotEqualTo(custRequestType2);
    }

    @Test
    void parentTest() {
        CustRequestType custRequestType = getCustRequestTypeRandomSampleGenerator();
        CustRequestType custRequestTypeBack = getCustRequestTypeRandomSampleGenerator();

        custRequestType.setParent(custRequestTypeBack);
        assertThat(custRequestType.getParent()).isEqualTo(custRequestTypeBack);

        custRequestType.parent(null);
        assertThat(custRequestType.getParent()).isNull();
    }

    @Test
    void partyTest() {
        CustRequestType custRequestType = getCustRequestTypeRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        custRequestType.setParty(partyBack);
        assertThat(custRequestType.getParty()).isEqualTo(partyBack);

        custRequestType.party(null);
        assertThat(custRequestType.getParty()).isNull();
    }
}
