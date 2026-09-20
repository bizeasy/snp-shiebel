package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.CashBookAttachmentTestSamples.*;
import static com.snp.domain.CashBookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashBookAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBookAttachment.class);
        CashBookAttachment cashBookAttachment1 = getCashBookAttachmentSample1();
        CashBookAttachment cashBookAttachment2 = new CashBookAttachment();
        assertThat(cashBookAttachment1).isNotEqualTo(cashBookAttachment2);

        cashBookAttachment2.setId(cashBookAttachment1.getId());
        assertThat(cashBookAttachment1).isEqualTo(cashBookAttachment2);

        cashBookAttachment2 = getCashBookAttachmentSample2();
        assertThat(cashBookAttachment1).isNotEqualTo(cashBookAttachment2);
    }

    @Test
    void cashBookTest() {
        CashBookAttachment cashBookAttachment = getCashBookAttachmentRandomSampleGenerator();
        CashBook cashBookBack = getCashBookRandomSampleGenerator();

        cashBookAttachment.setCashBook(cashBookBack);
        assertThat(cashBookAttachment.getCashBook()).isEqualTo(cashBookBack);

        cashBookAttachment.cashBook(null);
        assertThat(cashBookAttachment.getCashBook()).isNull();
    }

    @Test
    void attachmentTest() {
        CashBookAttachment cashBookAttachment = getCashBookAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        cashBookAttachment.setAttachment(attachmentBack);
        assertThat(cashBookAttachment.getAttachment()).isEqualTo(attachmentBack);

        cashBookAttachment.attachment(null);
        assertThat(cashBookAttachment.getAttachment()).isNull();
    }
}
