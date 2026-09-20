package com.snp.domain;

import static com.snp.domain.ContactMechPurposeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactMechPurposeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechPurpose.class);
        ContactMechPurpose contactMechPurpose1 = getContactMechPurposeSample1();
        ContactMechPurpose contactMechPurpose2 = new ContactMechPurpose();
        assertThat(contactMechPurpose1).isNotEqualTo(contactMechPurpose2);

        contactMechPurpose2.setId(contactMechPurpose1.getId());
        assertThat(contactMechPurpose1).isEqualTo(contactMechPurpose2);

        contactMechPurpose2 = getContactMechPurposeSample2();
        assertThat(contactMechPurpose1).isNotEqualTo(contactMechPurpose2);
    }
}
