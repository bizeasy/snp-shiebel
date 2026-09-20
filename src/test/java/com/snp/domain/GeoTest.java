package com.snp.domain;

import static com.snp.domain.GeoTestSamples.*;
import static com.snp.domain.GeoTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Geo.class);
        Geo geo1 = getGeoSample1();
        Geo geo2 = new Geo();
        assertThat(geo1).isNotEqualTo(geo2);

        geo2.setId(geo1.getId());
        assertThat(geo1).isEqualTo(geo2);

        geo2 = getGeoSample2();
        assertThat(geo1).isNotEqualTo(geo2);
    }

    @Test
    void geoTypeTest() {
        Geo geo = getGeoRandomSampleGenerator();
        GeoType geoTypeBack = getGeoTypeRandomSampleGenerator();

        geo.setGeoType(geoTypeBack);
        assertThat(geo.getGeoType()).isEqualTo(geoTypeBack);

        geo.geoType(null);
        assertThat(geo.getGeoType()).isNull();
    }
}
