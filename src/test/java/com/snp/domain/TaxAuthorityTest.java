package com.snp.domain;

import static com.snp.domain.GeoTestSamples.*;
import static com.snp.domain.TaxAuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAuthority.class);
        TaxAuthority taxAuthority1 = getTaxAuthoritySample1();
        TaxAuthority taxAuthority2 = new TaxAuthority();
        assertThat(taxAuthority1).isNotEqualTo(taxAuthority2);

        taxAuthority2.setId(taxAuthority1.getId());
        assertThat(taxAuthority1).isEqualTo(taxAuthority2);

        taxAuthority2 = getTaxAuthoritySample2();
        assertThat(taxAuthority1).isNotEqualTo(taxAuthority2);
    }

    @Test
    void geoTest() {
        TaxAuthority taxAuthority = getTaxAuthorityRandomSampleGenerator();
        Geo geoBack = getGeoRandomSampleGenerator();

        taxAuthority.setGeo(geoBack);
        assertThat(taxAuthority.getGeo()).isEqualTo(geoBack);

        taxAuthority.geo(null);
        assertThat(taxAuthority.getGeo()).isNull();
    }
}
