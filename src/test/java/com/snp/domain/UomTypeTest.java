package com.snp.domain;

import static com.snp.domain.UomTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UomTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UomType.class);
        UomType uomType1 = getUomTypeSample1();
        UomType uomType2 = new UomType();
        assertThat(uomType1).isNotEqualTo(uomType2);

        uomType2.setId(uomType1.getId());
        assertThat(uomType1).isEqualTo(uomType2);

        uomType2 = getUomTypeSample2();
        assertThat(uomType1).isNotEqualTo(uomType2);
    }
}
