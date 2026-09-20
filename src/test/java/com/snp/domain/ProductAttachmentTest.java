package com.snp.domain;

import static com.snp.domain.AttachmentTestSamples.*;
import static com.snp.domain.ProductAttachmentTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAttachment.class);
        ProductAttachment productAttachment1 = getProductAttachmentSample1();
        ProductAttachment productAttachment2 = new ProductAttachment();
        assertThat(productAttachment1).isNotEqualTo(productAttachment2);

        productAttachment2.setId(productAttachment1.getId());
        assertThat(productAttachment1).isEqualTo(productAttachment2);

        productAttachment2 = getProductAttachmentSample2();
        assertThat(productAttachment1).isNotEqualTo(productAttachment2);
    }

    @Test
    void productTest() {
        ProductAttachment productAttachment = getProductAttachmentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productAttachment.setProduct(productBack);
        assertThat(productAttachment.getProduct()).isEqualTo(productBack);

        productAttachment.product(null);
        assertThat(productAttachment.getProduct()).isNull();
    }

    @Test
    void attachmentTest() {
        ProductAttachment productAttachment = getProductAttachmentRandomSampleGenerator();
        Attachment attachmentBack = getAttachmentRandomSampleGenerator();

        productAttachment.setAttachment(attachmentBack);
        assertThat(productAttachment.getAttachment()).isEqualTo(attachmentBack);

        productAttachment.attachment(null);
        assertThat(productAttachment.getAttachment()).isNull();
    }
}
