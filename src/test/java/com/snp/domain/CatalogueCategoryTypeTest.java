package com.snp.domain;

import static com.snp.domain.CatalogueCategoryTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogueCategoryTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogueCategoryType.class);
        CatalogueCategoryType catalogueCategoryType1 = getCatalogueCategoryTypeSample1();
        CatalogueCategoryType catalogueCategoryType2 = new CatalogueCategoryType();
        assertThat(catalogueCategoryType1).isNotEqualTo(catalogueCategoryType2);

        catalogueCategoryType2.setId(catalogueCategoryType1.getId());
        assertThat(catalogueCategoryType1).isEqualTo(catalogueCategoryType2);

        catalogueCategoryType2 = getCatalogueCategoryTypeSample2();
        assertThat(catalogueCategoryType1).isNotEqualTo(catalogueCategoryType2);
    }
}
