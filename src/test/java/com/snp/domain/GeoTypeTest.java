package com.snp.domain;

import static com.snp.domain.GeoTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoType.class);
        GeoType geoType1 = getGeoTypeSample1();
        GeoType geoType2 = new GeoType();
        assertThat(geoType1).isNotEqualTo(geoType2);

        geoType2.setId(geoType1.getId());
        assertThat(geoType1).isEqualTo(geoType2);

        geoType2 = getGeoTypeSample2();
        assertThat(geoType1).isNotEqualTo(geoType2);
    }
}
