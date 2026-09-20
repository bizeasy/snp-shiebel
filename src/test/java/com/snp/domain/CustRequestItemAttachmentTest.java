package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.CustRequestItemAttachmentTestSamples.*;
import static com.snp.domain.CustRequestItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestItemAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestItemAttachment.class);
        CustRequestItemAttachment custRequestItemAttachment1 = getCustRequestItemAttachmentSample1();
        CustRequestItemAttachment custRequestItemAttachment2 = new CustRequestItemAttachment();
        assertThat(custRequestItemAttachment1).isNotEqualTo(custRequestItemAttachment2);

        custRequestItemAttachment2.setId(custRequestItemAttachment1.getId());
        assertThat(custRequestItemAttachment1).isEqualTo(custRequestItemAttachment2);

        custRequestItemAttachment2 = getCustRequestItemAttachmentSample2();
        assertThat(custRequestItemAttachment1).isNotEqualTo(custRequestItemAttachment2);
    }

    @Test
    void custRequestItemTest() {
        CustRequestItemAttachment custRequestItemAttachment = getCustRequestItemAttachmentRandomSampleGenerator();
        CustRequestItem custRequestItemBack = getCustRequestItemRandomSampleGenerator();

        custRequestItemAttachment.setCustRequestItem(custRequestItemBack);
        assertThat(custRequestItemAttachment.getCustRequestItem()).isEqualTo(custRequestItemBack);

        custRequestItemAttachment.custRequestItem(null);
        assertThat(custRequestItemAttachment.getCustRequestItem()).isNull();
    }

    @Test
    void attachmentTest() {
        CustRequestItemAttachment custRequestItemAttachment = getCustRequestItemAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        custRequestItemAttachment.setAttachment(attachmentBack);
        assertThat(custRequestItemAttachment.getAttachment()).isEqualTo(attachmentBack);

        custRequestItemAttachment.attachment(null);
        assertThat(custRequestItemAttachment.getAttachment()).isNull();
    }
}
