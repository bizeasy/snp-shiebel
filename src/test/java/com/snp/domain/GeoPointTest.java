package com.snp.domain;

import static com.snp.domain.GeoPointTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoPoint.class);
        GeoPoint geoPoint1 = getGeoPointSample1();
        GeoPoint geoPoint2 = new GeoPoint();
        assertThat(geoPoint1).isNotEqualTo(geoPoint2);

        geoPoint2.setId(geoPoint1.getId());
        assertThat(geoPoint1).isEqualTo(geoPoint2);

        geoPoint2 = getGeoPointSample2();
        assertThat(geoPoint1).isNotEqualTo(geoPoint2);
    }
}
