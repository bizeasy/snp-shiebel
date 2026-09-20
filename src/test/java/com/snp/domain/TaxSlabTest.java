package com.snp.domain;

import static com.snp.domain.TaxSlabTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxSlabTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxSlab.class);
        TaxSlab taxSlab1 = getTaxSlabSample1();
        TaxSlab taxSlab2 = new TaxSlab();
        assertThat(taxSlab1).isNotEqualTo(taxSlab2);

        taxSlab2.setId(taxSlab1.getId());
        assertThat(taxSlab1).isEqualTo(taxSlab2);

        taxSlab2 = getTaxSlabSample2();
        assertThat(taxSlab1).isNotEqualTo(taxSlab2);
    }
}
