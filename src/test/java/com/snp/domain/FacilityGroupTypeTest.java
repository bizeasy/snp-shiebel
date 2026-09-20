package com.snp.domain;

import static com.snp.domain.FacilityGroupTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityGroupTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityGroupType.class);
        FacilityGroupType facilityGroupType1 = getFacilityGroupTypeSample1();
        FacilityGroupType facilityGroupType2 = new FacilityGroupType();
        assertThat(facilityGroupType1).isNotEqualTo(facilityGroupType2);

        facilityGroupType2.setId(facilityGroupType1.getId());
        assertThat(facilityGroupType1).isEqualTo(facilityGroupType2);

        facilityGroupType2 = getFacilityGroupTypeSample2();
        assertThat(facilityGroupType1).isNotEqualTo(facilityGroupType2);
    }
}
