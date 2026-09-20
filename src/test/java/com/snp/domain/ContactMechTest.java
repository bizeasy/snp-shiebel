package com.snp.domain;

import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.ContactMechTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactMechTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMech.class);
        ContactMech contactMech1 = getContactMechSample1();
        ContactMech contactMech2 = new ContactMech();
        assertThat(contactMech1).isNotEqualTo(contactMech2);

        contactMech2.setId(contactMech1.getId());
        assertThat(contactMech1).isEqualTo(contactMech2);

        contactMech2 = getContactMechSample2();
        assertThat(contactMech1).isNotEqualTo(contactMech2);
    }

    @Test
    void contactMechTypeTest() {
        ContactMech contactMech = getContactMechRandomSampleGenerator();
        ContactMechType contactMechTypeBack = getContactMechTypeRandomSampleGenerator();

        contactMech.setContactMechType(contactMechTypeBack);
        assertThat(contactMech.getContactMechType()).isEqualTo(contactMechTypeBack);

        contactMech.contactMechType(null);
        assertThat(contactMech.getContactMechType()).isNull();
    }
}
