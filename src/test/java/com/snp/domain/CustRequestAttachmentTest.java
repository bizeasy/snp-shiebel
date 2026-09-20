package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.CustRequestAttachmentTestSamples.*;
import static com.snp.domain.CustRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestAttachment.class);
        CustRequestAttachment custRequestAttachment1 = getCustRequestAttachmentSample1();
        CustRequestAttachment custRequestAttachment2 = new CustRequestAttachment();
        assertThat(custRequestAttachment1).isNotEqualTo(custRequestAttachment2);

        custRequestAttachment2.setId(custRequestAttachment1.getId());
        assertThat(custRequestAttachment1).isEqualTo(custRequestAttachment2);

        custRequestAttachment2 = getCustRequestAttachmentSample2();
        assertThat(custRequestAttachment1).isNotEqualTo(custRequestAttachment2);
    }

    @Test
    void custRequestTest() {
        CustRequestAttachment custRequestAttachment = getCustRequestAttachmentRandomSampleGenerator();
        CustRequest custRequestBack = getCustRequestRandomSampleGenerator();

        custRequestAttachment.setCustRequest(custRequestBack);
        assertThat(custRequestAttachment.getCustRequest()).isEqualTo(custRequestBack);

        custRequestAttachment.custRequest(null);
        assertThat(custRequestAttachment.getCustRequest()).isNull();
    }

    @Test
    void attachmentTest() {
        CustRequestAttachment custRequestAttachment = getCustRequestAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        custRequestAttachment.setAttachment(attachmentBack);
        assertThat(custRequestAttachment.getAttachment()).isEqualTo(attachmentBack);

        custRequestAttachment.attachment(null);
        assertThat(custRequestAttachment.getAttachment()).isNull();
    }
}
