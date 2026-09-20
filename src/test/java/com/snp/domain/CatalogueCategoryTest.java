package com.snp.domain;

import static com.snp.domain.CatalogueCategoryTestSamples.*;
import static com.snp.domain.CatalogueCategoryTypeTestSamples.*;
import static com.snp.domain.CatalogueTestSamples.*;
import static com.snp.domain.ProductCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogueCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogueCategory.class);
        CatalogueCategory catalogueCategory1 = getCatalogueCategorySample1();
        CatalogueCategory catalogueCategory2 = new CatalogueCategory();
        assertThat(catalogueCategory1).isNotEqualTo(catalogueCategory2);

        catalogueCategory2.setId(catalogueCategory1.getId());
        assertThat(catalogueCategory1).isEqualTo(catalogueCategory2);

        catalogueCategory2 = getCatalogueCategorySample2();
        assertThat(catalogueCategory1).isNotEqualTo(catalogueCategory2);
    }

    @Test
    void catalogueTest() {
        CatalogueCategory catalogueCategory = getCatalogueCategoryRandomSampleGenerator();
        Catalogue catalogueBack = getCatalogueRandomSampleGenerator();

        catalogueCategory.setCatalogue(catalogueBack);
        assertThat(catalogueCategory.getCatalogue()).isEqualTo(catalogueBack);

        catalogueCategory.catalogue(null);
        assertThat(catalogueCategory.getCatalogue()).isNull();
    }

    @Test
    void productCategoryTest() {
        CatalogueCategory catalogueCategory = getCatalogueCategoryRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        catalogueCategory.setProductCategory(productCategoryBack);
        assertThat(catalogueCategory.getProductCategory()).isEqualTo(productCategoryBack);

        catalogueCategory.productCategory(null);
        assertThat(catalogueCategory.getProductCategory()).isNull();
    }

    @Test
    void catalogueCategoryTypeTest() {
        CatalogueCategory catalogueCategory = getCatalogueCategoryRandomSampleGenerator();
        CatalogueCategoryType catalogueCategoryTypeBack = getCatalogueCategoryTypeRandomSampleGenerator();

        catalogueCategory.setCatalogueCategoryType(catalogueCategoryTypeBack);
        assertThat(catalogueCategory.getCatalogueCategoryType()).isEqualTo(catalogueCategoryTypeBack);

        catalogueCategory.catalogueCategoryType(null);
        assertThat(catalogueCategory.getCatalogueCategoryType()).isNull();
    }
}
