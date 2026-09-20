package com.snp.domain;

import static com.snp.domain.UomTestSamples.*;
import static com.snp.domain.UomTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uom.class);
        Uom uom1 = getUomSample1();
        Uom uom2 = new Uom();
        assertThat(uom1).isNotEqualTo(uom2);

        uom2.setId(uom1.getId());
        assertThat(uom1).isEqualTo(uom2);

        uom2 = getUomSample2();
        assertThat(uom1).isNotEqualTo(uom2);
    }

    @Test
    void uomTypeTest() {
        Uom uom = getUomRandomSampleGenerator();
        UomType uomTypeBack = getUomTypeRandomSampleGenerator();

        uom.setUomType(uomTypeBack);
        assertThat(uom.getUomType()).isEqualTo(uomTypeBack);

        uom.uomType(null);
        assertThat(uom.getUomType()).isNull();
    }
}
