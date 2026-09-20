package com.snp.web.rest;

import static com.snp.domain.ProductFeatureTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeatureType;
import com.snp.repository.ProductFeatureTypeRepository;
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
 * Integration tests for the {@link ProductFeatureTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-feature-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureTypeRepository productFeatureTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureTypeMockMvc;

    private ProductFeatureType productFeatureType;

    private ProductFeatureType insertedProductFeatureType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureType createEntity() {
        return new ProductFeatureType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureType createUpdatedEntity() {
        return new ProductFeatureType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productFeatureType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureType != null) {
            productFeatureTypeRepository.delete(insertedProductFeatureType);
            insertedProductFeatureType = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureType
        var returnedProductFeatureType = om.readValue(
            restProductFeatureTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureType.class
        );

        // Validate the ProductFeatureType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureTypeUpdatableFieldsEquals(
            returnedProductFeatureType,
            getPersistedProductFeatureType(returnedProductFeatureType)
        );

        insertedProductFeatureType = returnedProductFeatureType;
    }

    @Test
    @Transactional
    void createProductFeatureTypeWithExistingId() throws Exception {
        // Create the ProductFeatureType with an existing ID
        productFeatureType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureType.setName(null);

        // Create the ProductFeatureType, which fails.

        restProductFeatureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypes() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductFeatureType() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get the productFeatureType
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductFeatureTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        Long id = productFeatureType.getId();

        defaultProductFeatureTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where name equals to
        defaultProductFeatureTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where name in
        defaultProductFeatureTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where name is not null
        defaultProductFeatureTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where name contains
        defaultProductFeatureTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where name does not contain
        defaultProductFeatureTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where description equals to
        defaultProductFeatureTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where description in
        defaultProductFeatureTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where description is not null
        defaultProductFeatureTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where description contains
        defaultProductFeatureTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        // Get all the productFeatureTypeList where description does not contain
        defaultProductFeatureTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductFeatureTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureTypeShouldBeFound(shouldBeFound);
        defaultProductFeatureTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureTypeShouldBeFound(String filter) throws Exception {
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureTypeShouldNotBeFound(String filter) throws Exception {
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureType() throws Exception {
        // Get the productFeatureType
        restProductFeatureTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureType() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureType
        ProductFeatureType updatedProductFeatureType = productFeatureTypeRepository.findById(productFeatureType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureType are not directly saved in db
        em.detach(updatedProductFeatureType);
        updatedProductFeatureType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureTypeToMatchAllProperties(updatedProductFeatureType);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureType using partial update
        ProductFeatureType partialUpdatedProductFeatureType = new ProductFeatureType();
        partialUpdatedProductFeatureType.setId(productFeatureType.getId());

        partialUpdatedProductFeatureType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureType, productFeatureType),
            getPersistedProductFeatureType(productFeatureType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureType using partial update
        ProductFeatureType partialUpdatedProductFeatureType = new ProductFeatureType();
        partialUpdatedProductFeatureType.setId(productFeatureType.getId());

        partialUpdatedProductFeatureType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureTypeUpdatableFieldsEquals(
            partialUpdatedProductFeatureType,
            getPersistedProductFeatureType(partialUpdatedProductFeatureType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureType() throws Exception {
        // Initialize the database
        insertedProductFeatureType = productFeatureTypeRepository.saveAndFlush(productFeatureType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureType
        restProductFeatureTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureTypeRepository.count();
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

    protected ProductFeatureType getPersistedProductFeatureType(ProductFeatureType productFeatureType) {
        return productFeatureTypeRepository.findById(productFeatureType.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureTypeToMatchAllProperties(ProductFeatureType expectedProductFeatureType) {
        assertProductFeatureTypeAllPropertiesEquals(expectedProductFeatureType, getPersistedProductFeatureType(expectedProductFeatureType));
    }

    protected void assertPersistedProductFeatureTypeToMatchUpdatableProperties(ProductFeatureType expectedProductFeatureType) {
        assertProductFeatureTypeAllUpdatablePropertiesEquals(
            expectedProductFeatureType,
            getPersistedProductFeatureType(expectedProductFeatureType)
        );
    }
}
