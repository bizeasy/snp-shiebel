package com.snp.web.rest;

import static com.snp.domain.CatalogueCategoryAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Catalogue;
import com.snp.domain.CatalogueCategory;
import com.snp.domain.CatalogueCategoryType;
import com.snp.domain.ProductCategory;
import com.snp.repository.CatalogueCategoryRepository;
import com.snp.service.CatalogueCategoryService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CatalogueCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CatalogueCategoryResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/catalogue-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CatalogueCategoryRepository catalogueCategoryRepository;

    @Mock
    private CatalogueCategoryRepository catalogueCategoryRepositoryMock;

    @Mock
    private CatalogueCategoryService catalogueCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogueCategoryMockMvc;

    private CatalogueCategory catalogueCategory;

    private CatalogueCategory insertedCatalogueCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogueCategory createEntity(EntityManager em) {
        CatalogueCategory catalogueCategory = new CatalogueCategory().sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        Catalogue catalogue;
        if (TestUtil.findAll(em, Catalogue.class).isEmpty()) {
            catalogue = CatalogueResourceIT.createEntity();
            em.persist(catalogue);
            em.flush();
        } else {
            catalogue = TestUtil.findAll(em, Catalogue.class).getFirst();
        }
        catalogueCategory.setCatalogue(catalogue);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        catalogueCategory.setProductCategory(productCategory);
        return catalogueCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogueCategory createUpdatedEntity(EntityManager em) {
        CatalogueCategory updatedCatalogueCategory = new CatalogueCategory().sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        Catalogue catalogue;
        if (TestUtil.findAll(em, Catalogue.class).isEmpty()) {
            catalogue = CatalogueResourceIT.createUpdatedEntity();
            em.persist(catalogue);
            em.flush();
        } else {
            catalogue = TestUtil.findAll(em, Catalogue.class).getFirst();
        }
        updatedCatalogueCategory.setCatalogue(catalogue);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createUpdatedEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        updatedCatalogueCategory.setProductCategory(productCategory);
        return updatedCatalogueCategory;
    }

    @BeforeEach
    void initTest() {
        catalogueCategory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCatalogueCategory != null) {
            catalogueCategoryRepository.delete(insertedCatalogueCategory);
            insertedCatalogueCategory = null;
        }
    }

    @Test
    @Transactional
    void createCatalogueCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CatalogueCategory
        var returnedCatalogueCategory = om.readValue(
            restCatalogueCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CatalogueCategory.class
        );

        // Validate the CatalogueCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCatalogueCategoryUpdatableFieldsEquals(returnedCatalogueCategory, getPersistedCatalogueCategory(returnedCatalogueCategory));

        insertedCatalogueCategory = returnedCatalogueCategory;
    }

    @Test
    @Transactional
    void createCatalogueCategoryWithExistingId() throws Exception {
        // Create the CatalogueCategory with an existing ID
        catalogueCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogueCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategory)))
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCatalogueCategories() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogueCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCatalogueCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(catalogueCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCatalogueCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(catalogueCategoryServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCatalogueCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(catalogueCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCatalogueCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(catalogueCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCatalogueCategory() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get the catalogueCategory
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogueCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogueCategory.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getCatalogueCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        Long id = catalogueCategory.getId();

        defaultCatalogueCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCatalogueCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCatalogueCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo equals to
        defaultCatalogueCategoryFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo in
        defaultCatalogueCategoryFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo is not null
        defaultCatalogueCategoryFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo is greater than or equal to
        defaultCatalogueCategoryFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo is less than or equal to
        defaultCatalogueCategoryFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo is less than
        defaultCatalogueCategoryFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        // Get all the catalogueCategoryList where sequenceNo is greater than
        defaultCatalogueCategoryFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesByCatalogueIsEqualToSomething() throws Exception {
        Catalogue catalogue;
        if (TestUtil.findAll(em, Catalogue.class).isEmpty()) {
            catalogueCategoryRepository.saveAndFlush(catalogueCategory);
            catalogue = CatalogueResourceIT.createEntity();
        } else {
            catalogue = TestUtil.findAll(em, Catalogue.class).getFirst();
        }
        em.persist(catalogue);
        em.flush();
        catalogueCategory.setCatalogue(catalogue);
        catalogueCategoryRepository.saveAndFlush(catalogueCategory);
        Long catalogueId = catalogue.getId();
        // Get all the catalogueCategoryList where catalogue equals to catalogueId
        defaultCatalogueCategoryShouldBeFound("catalogueId.equals=" + catalogueId);

        // Get all the catalogueCategoryList where catalogue equals to (catalogueId + 1)
        defaultCatalogueCategoryShouldNotBeFound("catalogueId.equals=" + (catalogueId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesByProductCategoryIsEqualToSomething() throws Exception {
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            catalogueCategoryRepository.saveAndFlush(catalogueCategory);
            productCategory = ProductCategoryResourceIT.createEntity();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        em.persist(productCategory);
        em.flush();
        catalogueCategory.setProductCategory(productCategory);
        catalogueCategoryRepository.saveAndFlush(catalogueCategory);
        Long productCategoryId = productCategory.getId();
        // Get all the catalogueCategoryList where productCategory equals to productCategoryId
        defaultCatalogueCategoryShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the catalogueCategoryList where productCategory equals to (productCategoryId + 1)
        defaultCatalogueCategoryShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCatalogueCategoriesByCatalogueCategoryTypeIsEqualToSomething() throws Exception {
        CatalogueCategoryType catalogueCategoryType;
        if (TestUtil.findAll(em, CatalogueCategoryType.class).isEmpty()) {
            catalogueCategoryRepository.saveAndFlush(catalogueCategory);
            catalogueCategoryType = CatalogueCategoryTypeResourceIT.createEntity();
        } else {
            catalogueCategoryType = TestUtil.findAll(em, CatalogueCategoryType.class).getFirst();
        }
        em.persist(catalogueCategoryType);
        em.flush();
        catalogueCategory.setCatalogueCategoryType(catalogueCategoryType);
        catalogueCategoryRepository.saveAndFlush(catalogueCategory);
        Long catalogueCategoryTypeId = catalogueCategoryType.getId();
        // Get all the catalogueCategoryList where catalogueCategoryType equals to catalogueCategoryTypeId
        defaultCatalogueCategoryShouldBeFound("catalogueCategoryTypeId.equals=" + catalogueCategoryTypeId);

        // Get all the catalogueCategoryList where catalogueCategoryType equals to (catalogueCategoryTypeId + 1)
        defaultCatalogueCategoryShouldNotBeFound("catalogueCategoryTypeId.equals=" + (catalogueCategoryTypeId + 1));
    }

    private void defaultCatalogueCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCatalogueCategoryShouldBeFound(shouldBeFound);
        defaultCatalogueCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogueCategoryShouldBeFound(String filter) throws Exception {
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogueCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogueCategoryShouldNotBeFound(String filter) throws Exception {
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogueCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatalogueCategory() throws Exception {
        // Get the catalogueCategory
        restCatalogueCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalogueCategory() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategory
        CatalogueCategory updatedCatalogueCategory = catalogueCategoryRepository.findById(catalogueCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCatalogueCategory are not directly saved in db
        em.detach(updatedCatalogueCategory);
        updatedCatalogueCategory.sequenceNo(UPDATED_SEQUENCE_NO);

        restCatalogueCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogueCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCatalogueCategory))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCatalogueCategoryToMatchAllProperties(updatedCatalogueCategory);
    }

    @Test
    @Transactional
    void putNonExistingCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogueCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogueCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogueCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogueCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategory using partial update
        CatalogueCategory partialUpdatedCatalogueCategory = new CatalogueCategory();
        partialUpdatedCatalogueCategory.setId(catalogueCategory.getId());

        partialUpdatedCatalogueCategory.sequenceNo(UPDATED_SEQUENCE_NO);

        restCatalogueCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogueCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogueCategory))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCatalogueCategory, catalogueCategory),
            getPersistedCatalogueCategory(catalogueCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateCatalogueCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategory using partial update
        CatalogueCategory partialUpdatedCatalogueCategory = new CatalogueCategory();
        partialUpdatedCatalogueCategory.setId(catalogueCategory.getId());

        partialUpdatedCatalogueCategory.sequenceNo(UPDATED_SEQUENCE_NO);

        restCatalogueCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogueCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogueCategory))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueCategoryUpdatableFieldsEquals(
            partialUpdatedCatalogueCategory,
            getPersistedCatalogueCategory(partialUpdatedCatalogueCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogueCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogueCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogueCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogueCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(catalogueCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogueCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogueCategory() throws Exception {
        // Initialize the database
        insertedCatalogueCategory = catalogueCategoryRepository.saveAndFlush(catalogueCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the catalogueCategory
        restCatalogueCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogueCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return catalogueCategoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CatalogueCategory getPersistedCatalogueCategory(CatalogueCategory catalogueCategory) {
        return catalogueCategoryRepository.findById(catalogueCategory.getId()).orElseThrow();
    }

    protected void assertPersistedCatalogueCategoryToMatchAllProperties(CatalogueCategory expectedCatalogueCategory) {
        assertCatalogueCategoryAllPropertiesEquals(expectedCatalogueCategory, getPersistedCatalogueCategory(expectedCatalogueCategory));
    }

    protected void assertPersistedCatalogueCategoryToMatchUpdatableProperties(CatalogueCategory expectedCatalogueCategory) {
        assertCatalogueCategoryAllUpdatablePropertiesEquals(
            expectedCatalogueCategory,
            getPersistedCatalogueCategory(expectedCatalogueCategory)
        );
    }
}
