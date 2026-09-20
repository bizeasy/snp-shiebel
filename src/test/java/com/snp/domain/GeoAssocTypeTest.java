package com.snp.domain;

import static com.snp.domain.GeoAssocTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoAssocTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoAssocType.class);
        GeoAssocType geoAssocType1 = getGeoAssocTypeSample1();
        GeoAssocType geoAssocType2 = new GeoAssocType();
        assertThat(geoAssocType1).isNotEqualTo(geoAssocType2);

        geoAssocType2.setId(geoAssocType1.getId());
        assertThat(geoAssocType1).isEqualTo(geoAssocType2);

        geoAssocType2 = getGeoAssocTypeSample2();
        assertThat(geoAssocType1).isNotEqualTo(geoAssocType2);
    }
}
