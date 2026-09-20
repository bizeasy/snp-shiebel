package com.snp.web.rest;

import static com.snp.domain.ProductFeatureCategoryAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeatureCategory;
import com.snp.repository.ProductFeatureCategoryRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ProductFeatureCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-feature-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureCategoryRepository productFeatureCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureCategoryMockMvc;

    private ProductFeatureCategory productFeatureCategory;

    private ProductFeatureCategory insertedProductFeatureCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureCategory createEntity() {
        return new ProductFeatureCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureCategory createUpdatedEntity() {
        return new ProductFeatureCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productFeatureCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureCategory != null) {
            productFeatureCategoryRepository.delete(insertedProductFeatureCategory);
            insertedProductFeatureCategory = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureCategory
        var returnedProductFeatureCategory = om.readValue(
            restProductFeatureCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureCategory.class
        );

        // Validate the ProductFeatureCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureCategoryUpdatableFieldsEquals(
            returnedProductFeatureCategory,
            getPersistedProductFeatureCategory(returnedProductFeatureCategory)
        );

        insertedProductFeatureCategory = returnedProductFeatureCategory;
    }

    @Test
    @Transactional
    void createProductFeatureCategoryWithExistingId() throws Exception {
        // Create the ProductFeatureCategory with an existing ID
        productFeatureCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureCategory.setName(null);

        // Create the ProductFeatureCategory, which fails.

        restProductFeatureCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategories() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductFeatureCategory() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get the productFeatureCategory
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductFeatureCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        Long id = productFeatureCategory.getId();

        defaultProductFeatureCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where name equals to
        defaultProductFeatureCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where name in
        defaultProductFeatureCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where name is not null
        defaultProductFeatureCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where name contains
        defaultProductFeatureCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where name does not contain
        defaultProductFeatureCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where description equals to
        defaultProductFeatureCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where description in
        defaultProductFeatureCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where description is not null
        defaultProductFeatureCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where description contains
        defaultProductFeatureCategoryFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        // Get all the productFeatureCategoryList where description does not contain
        defaultProductFeatureCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductFeatureCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureCategoryShouldBeFound(shouldBeFound);
        defaultProductFeatureCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureCategoryShouldBeFound(String filter) throws Exception {
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureCategoryShouldNotBeFound(String filter) throws Exception {
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureCategory() throws Exception {
        // Get the productFeatureCategory
        restProductFeatureCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureCategory() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategory
        ProductFeatureCategory updatedProductFeatureCategory = productFeatureCategoryRepository
            .findById(productFeatureCategory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureCategory are not directly saved in db
        em.detach(updatedProductFeatureCategory);
        updatedProductFeatureCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureCategoryToMatchAllProperties(updatedProductFeatureCategory);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategory using partial update
        ProductFeatureCategory partialUpdatedProductFeatureCategory = new ProductFeatureCategory();
        partialUpdatedProductFeatureCategory.setId(productFeatureCategory.getId());

        restProductFeatureCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureCategory, productFeatureCategory),
            getPersistedProductFeatureCategory(productFeatureCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategory using partial update
        ProductFeatureCategory partialUpdatedProductFeatureCategory = new ProductFeatureCategory();
        partialUpdatedProductFeatureCategory.setId(productFeatureCategory.getId());

        partialUpdatedProductFeatureCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureCategoryUpdatableFieldsEquals(
            partialUpdatedProductFeatureCategory,
            getPersistedProductFeatureCategory(partialUpdatedProductFeatureCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureCategory() throws Exception {
        // Initialize the database
        insertedProductFeatureCategory = productFeatureCategoryRepository.saveAndFlush(productFeatureCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureCategory
        restProductFeatureCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureCategoryRepository.count();
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

    protected ProductFeatureCategory getPersistedProductFeatureCategory(ProductFeatureCategory productFeatureCategory) {
        return productFeatureCategoryRepository.findById(productFeatureCategory.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureCategoryToMatchAllProperties(ProductFeatureCategory expectedProductFeatureCategory) {
        assertProductFeatureCategoryAllPropertiesEquals(
            expectedProductFeatureCategory,
            getPersistedProductFeatureCategory(expectedProductFeatureCategory)
        );
    }

    protected void assertPersistedProductFeatureCategoryToMatchUpdatableProperties(ProductFeatureCategory expectedProductFeatureCategory) {
        assertProductFeatureCategoryAllUpdatablePropertiesEquals(
            expectedProductFeatureCategory,
            getPersistedProductFeatureCategory(expectedProductFeatureCategory)
        );
    }
}
