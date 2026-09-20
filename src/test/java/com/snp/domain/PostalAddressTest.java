package com.snp.domain;

import static com.snp.domain.ContactMechTestSamples.*;
import static com.snp.domain.GeoPointTestSamples.*;
import static com.snp.domain.GeoTestSamples.*;
import static com.snp.domain.PostalAddressTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostalAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddress.class);
        PostalAddress postalAddress1 = getPostalAddressSample1();
        PostalAddress postalAddress2 = new PostalAddress();
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);

        postalAddress2.setId(postalAddress1.getId());
        assertThat(postalAddress1).isEqualTo(postalAddress2);

        postalAddress2 = getPostalAddressSample2();
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
    }

    @Test
    void contactMechTest() {
        PostalAddress postalAddress = getPostalAddressRandomSampleGenerator();
        ContactMech contactMechBack = getContactMechRandomSampleGenerator();

        postalAddress.setContactMech(contactMechBack);
        assertThat(postalAddress.getContactMech()).isEqualTo(contactMechBack);

        postalAddress.contactMech(null);
        assertThat(postalAddress.getContactMech()).isNull();
    }

    @Test
    void stateTest() {
        PostalAddress postalAddress = getPostalAddressRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        postalAddress.setState(geoBack);
        assertThat(postalAddress.getState()).isEqualTo(geoBack);

        postalAddress.state(null);
        assertThat(postalAddress.getState()).isNull();
    }

    @Test
    void pincodeTest() {
        PostalAddress postalAddress = getPostalAddressRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        postalAddress.setPincode(geoBack);
        assertThat(postalAddress.getPincode()).isEqualTo(geoBack);

        postalAddress.pincode(null);
        assertThat(postalAddress.getPincode()).isNull();
    }

    @Test
    void countryTest() {
        PostalAddress postalAddress = getPostalAddressRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        postalAddress.setCountry(geoBack);
        assertThat(postalAddress.getCountry()).isEqualTo(geoBack);

        postalAddress.country(null);
        assertThat(postalAddress.getCountry()).isNull();
    }

    @Test
    void geoPointTest() {
        PostalAddress postalAddress = getPostalAddressRandomSampleGenerator();
        GeoPoint geoPointBack = getGeoPointRandomSampleGenerator();

        postalAddress.setGeoPoint(geoPointBack);
        assertThat(postalAddress.getGeoPoint()).isEqualTo(geoPointBack);

        postalAddress.geoPoint(null);
        assertThat(postalAddress.getGeoPoint()).isNull();
    }
}
