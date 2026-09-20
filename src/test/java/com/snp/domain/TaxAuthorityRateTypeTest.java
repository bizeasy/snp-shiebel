package com.snp.domain;

import static com.snp.domain.TaxAuthorityRateTypeTestSamples.*;
import static com.snp.domain.TaxAuthorityTestSamples.*;
import static com.snp.domain.TaxSlabTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAuthorityRateTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAuthorityRateType.class);
        TaxAuthorityRateType taxAuthorityRateType1 = getTaxAuthorityRateTypeSample1();
        TaxAuthorityRateType taxAuthorityRateType2 = new TaxAuthorityRateType();
        assertThat(taxAuthorityRateType1).isNotEqualTo(taxAuthorityRateType2);

        taxAuthorityRateType2.setId(taxAuthorityRateType1.getId());
        assertThat(taxAuthorityRateType1).isEqualTo(taxAuthorityRateType2);

        taxAuthorityRateType2 = getTaxAuthorityRateTypeSample2();
        assertThat(taxAuthorityRateType1).isNotEqualTo(taxAuthorityRateType2);
    }

    @Test
    void taxAuthorityTest() {
        TaxAuthorityRateType taxAuthorityRateType = getTaxAuthorityRateTypeRandomSampleGenerator();
        TaxAuthority taxAuthorityBack = getTaxAuthorityRandomSampleGenerator();

        taxAuthorityRateType.setTaxAuthority(taxAuthorityBack);
        assertThat(taxAuthorityRateType.getTaxAuthority()).isEqualTo(taxAuthorityBack);

        taxAuthorityRateType.taxAuthority(null);
        assertThat(taxAuthorityRateType.getTaxAuthority()).isNull();
    }

    @Test
    void taxSlabTest() {
        TaxAuthorityRateType taxAuthorityRateType = getTaxAuthorityRateTypeRandomSampleGenerator();
        TaxSlab taxSlabBack = getTaxSlabRandomSampleGenerator();

        taxAuthorityRateType.setTaxSlab(taxSlabBack);
        assertThat(taxAuthorityRateType.getTaxSlab()).isEqualTo(taxSlabBack);

        taxAuthorityRateType.taxSlab(null);
        assertThat(taxAuthorityRateType.getTaxSlab()).isNull();
    }
}
