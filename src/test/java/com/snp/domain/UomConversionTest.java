package com.snp.domain;

import static com.snp.domain.UomConversionTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UomConversionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UomConversion.class);
        UomConversion uomConversion1 = getUomConversionSample1();
        UomConversion uomConversion2 = new UomConversion();
        assertThat(uomConversion1).isNotEqualTo(uomConversion2);

        uomConversion2.setId(uomConversion1.getId());
        assertThat(uomConversion1).isEqualTo(uomConversion2);

        uomConversion2 = getUomConversionSample2();
        assertThat(uomConversion1).isNotEqualTo(uomConversion2);
    }

    @Test
    void fromUomTest() {
        UomConversion uomConversion = getUomConversionRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        uomConversion.setFromUom(uomBack);
        assertThat(uomConversion.getFromUom()).isEqualTo(uomBack);

        uomConversion.fromUom(null);
        assertThat(uomConversion.getFromUom()).isNull();
    }

    @Test
    void toUomTest() {
        UomConversion uomConversion = getUomConversionRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        uomConversion.setToUom(uomBack);
        assertThat(uomConversion.getToUom()).isEqualTo(uomBack);

        uomConversion.toUom(null);
        assertThat(uomConversion.getToUom()).isNull();
    }
}
