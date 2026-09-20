package com.snp.domain;

import static com.snp.domain.ContactMechTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactMechTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechType.class);
        ContactMechType contactMechType1 = getContactMechTypeSample1();
        ContactMechType contactMechType2 = new ContactMechType();
        assertThat(contactMechType1).isNotEqualTo(contactMechType2);

        contactMechType2.setId(contactMechType1.getId());
        assertThat(contactMechType1).isEqualTo(contactMechType2);

        contactMechType2 = getContactMechTypeSample2();
        assertThat(contactMechType1).isNotEqualTo(contactMechType2);
    }
}
