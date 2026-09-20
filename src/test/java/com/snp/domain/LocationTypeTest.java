package com.snp.domain;

import static com.snp.domain.LocationTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationType.class);
        LocationType locationType1 = getLocationTypeSample1();
        LocationType locationType2 = new LocationType();
        assertThat(locationType1).isNotEqualTo(locationType2);

        locationType2.setId(locationType1.getId());
        assertThat(locationType1).isEqualTo(locationType2);

        locationType2 = getLocationTypeSample2();
        assertThat(locationType1).isNotEqualTo(locationType2);
    }
}
