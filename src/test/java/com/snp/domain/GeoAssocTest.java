package com.snp.domain;

import static com.snp.domain.GeoAssocTestSamples.*;
import static com.snp.domain.GeoAssocTypeTestSamples.*;
import static com.snp.domain.GeoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoAssocTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoAssoc.class);
        GeoAssoc geoAssoc1 = getGeoAssocSample1();
        GeoAssoc geoAssoc2 = new GeoAssoc();
        assertThat(geoAssoc1).isNotEqualTo(geoAssoc2);

        geoAssoc2.setId(geoAssoc1.getId());
        assertThat(geoAssoc1).isEqualTo(geoAssoc2);

        geoAssoc2 = getGeoAssocSample2();
        assertThat(geoAssoc1).isNotEqualTo(geoAssoc2);
    }

    @Test
    void geoTest() {
        GeoAssoc geoAssoc = getGeoAssocRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        geoAssoc.setGeo(geoBack);
        assertThat(geoAssoc.getGeo()).isEqualTo(geoBack);

        geoAssoc.geo(null);
        assertThat(geoAssoc.getGeo()).isNull();
    }

    @Test
    void geoToTest() {
        GeoAssoc geoAssoc = getGeoAssocRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        geoAssoc.setGeoTo(geoBack);
        assertThat(geoAssoc.getGeoTo()).isEqualTo(geoBack);

        geoAssoc.geoTo(null);
        assertThat(geoAssoc.getGeoTo()).isNull();
    }

    @Test
    void geoAssocTypeTest() {
        GeoAssoc geoAssoc = getGeoAssocRandomSampleGenerator();
        GeoAssocType geoAssocTypeBack = getGeoAssocTypeRandomSampleGenerator();

        geoAssoc.setGeoAssocType(geoAssocTypeBack);
        assertThat(geoAssoc.getGeoAssocType()).isEqualTo(geoAssocTypeBack);

        geoAssoc.geoAssocType(null);
        assertThat(geoAssoc.getGeoAssocType()).isNull();
    }
}
