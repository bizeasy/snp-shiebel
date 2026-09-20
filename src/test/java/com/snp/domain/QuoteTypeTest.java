package com.snp.domain;

import static com.snp.domain.QuoteTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteType.class);
        QuoteType quoteType1 = getQuoteTypeSample1();
        QuoteType quoteType2 = new QuoteType();
        assertThat(quoteType1).isNotEqualTo(quoteType2);

        quoteType2.setId(quoteType1.getId());
        assertThat(quoteType1).isEqualTo(quoteType2);

        quoteType2 = getQuoteTypeSample2();
        assertThat(quoteType1).isNotEqualTo(quoteType2);
    }
}
