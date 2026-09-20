package com.snp.domain;

import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.QuoteItemTestSamples.*;
import static com.snp.domain.QuoteRoleTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static com.snp.domain.RoleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteRole.class);
        QuoteRole quoteRole1 = getQuoteRoleSample1();
        QuoteRole quoteRole2 = new QuoteRole();
        assertThat(quoteRole1).isNotEqualTo(quoteRole2);

        quoteRole2.setId(quoteRole1.getId());
        assertThat(quoteRole1).isEqualTo(quoteRole2);

        quoteRole2 = getQuoteRoleSample2();
        assertThat(quoteRole1).isNotEqualTo(quoteRole2);
    }

    @Test
    void quoteTest() {
        QuoteRole quoteRole = getQuoteRoleRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        quoteRole.setQuote(quoteBack);
        assertThat(quoteRole.getQuote()).isEqualTo(quoteBack);

        quoteRole.quote(null);
        assertThat(quoteRole.getQuote()).isNull();
    }

    @Test
    void quoteItemTest() {
        QuoteRole quoteRole = getQuoteRoleRandomSampleGenerator();
        QuoteItem quoteItemBack = getQuoteItemRandomSampleGenerator();

        quoteRole.setQuoteItem(quoteItemBack);
        assertThat(quoteRole.getQuoteItem()).isEqualTo(quoteItemBack);

        quoteRole.quoteItem(null);
        assertThat(quoteRole.getQuoteItem()).isNull();
    }

    @Test
    void partyTest() {
        QuoteRole quoteRole = getQuoteRoleRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        quoteRole.setParty(partyBack);
        assertThat(quoteRole.getParty()).isEqualTo(partyBack);

        quoteRole.party(null);
        assertThat(quoteRole.getParty()).isNull();
    }

    @Test
    void roleTypeTest() {
        QuoteRole quoteRole = getQuoteRoleRandomSampleGenerator();
        RoleType roleTypeBack = getRoleTypeRandomSampleGenerator();

        quoteRole.setRoleType(roleTypeBack);
        assertThat(quoteRole.getRoleType()).isEqualTo(roleTypeBack);

        quoteRole.roleType(null);
        assertThat(quoteRole.getRoleType()).isNull();
    }
}
