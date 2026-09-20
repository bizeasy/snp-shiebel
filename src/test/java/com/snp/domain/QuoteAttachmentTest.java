package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.QuoteAttachmentTestSamples.*;
import static com.snp.domain.QuoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteAttachment.class);
        QuoteAttachment quoteAttachment1 = getQuoteAttachmentSample1();
        QuoteAttachment quoteAttachment2 = new QuoteAttachment();
        assertThat(quoteAttachment1).isNotEqualTo(quoteAttachment2);

        quoteAttachment2.setId(quoteAttachment1.getId());
        assertThat(quoteAttachment1).isEqualTo(quoteAttachment2);

        quoteAttachment2 = getQuoteAttachmentSample2();
        assertThat(quoteAttachment1).isNotEqualTo(quoteAttachment2);
    }

    @Test
    void quoteTest() {
        QuoteAttachment quoteAttachment = getQuoteAttachmentRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        quoteAttachment.setQuote(quoteBack);
        assertThat(quoteAttachment.getQuote()).isEqualTo(quoteBack);

        quoteAttachment.quote(null);
        assertThat(quoteAttachment.getQuote()).isNull();
    }

    @Test
    void attachmentTest() {
        QuoteAttachment quoteAttachment = getQuoteAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        quoteAttachment.setAttachment(attachmentBack);
        assertThat(quoteAttachment.getAttachment()).isEqualTo(attachmentBack);

        quoteAttachment.attachment(null);
        assertThat(quoteAttachment.getAttachment()).isNull();
    }
}
