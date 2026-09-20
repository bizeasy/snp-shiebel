package com.snp.domain;

import static com.snp.domain.CatalogueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogue.class);
        Catalogue catalogue1 = getCatalogueSample1();
        Catalogue catalogue2 = new Catalogue();
        assertThat(catalogue1).isNotEqualTo(catalogue2);

        catalogue2.setId(catalogue1.getId());
        assertThat(catalogue1).isEqualTo(catalogue2);

        catalogue2 = getCatalogueSample2();
        assertThat(catalogue1).isNotEqualTo(catalogue2);
    }
}
