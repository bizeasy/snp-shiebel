package com.snp.web.rest;

import static com.snp.domain.ProductFeatureApplTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeatureApplType;
import com.snp.repository.ProductFeatureApplTypeRepository;
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
 * Integration tests for the {@link ProductFeatureApplTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureApplTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-feature-appl-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureApplTypeRepository productFeatureApplTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureApplTypeMockMvc;

    private ProductFeatureApplType productFeatureApplType;

    private ProductFeatureApplType insertedProductFeatureApplType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureApplType createEntity() {
        return new ProductFeatureApplType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureApplType createUpdatedEntity() {
        return new ProductFeatureApplType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productFeatureApplType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureApplType != null) {
            productFeatureApplTypeRepository.delete(insertedProductFeatureApplType);
            insertedProductFeatureApplType = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureApplType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureApplType
        var returnedProductFeatureApplType = om.readValue(
            restProductFeatureApplTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureApplType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureApplType.class
        );

        // Validate the ProductFeatureApplType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureApplTypeUpdatableFieldsEquals(
            returnedProductFeatureApplType,
            getPersistedProductFeatureApplType(returnedProductFeatureApplType)
        );

        insertedProductFeatureApplType = returnedProductFeatureApplType;
    }

    @Test
    @Transactional
    void createProductFeatureApplTypeWithExistingId() throws Exception {
        // Create the ProductFeatureApplType with an existing ID
        productFeatureApplType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureApplTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureApplType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureApplType.setName(null);

        // Create the ProductFeatureApplType, which fails.

        restProductFeatureApplTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureApplType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypes() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureApplType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductFeatureApplType() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get the productFeatureApplType
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureApplType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureApplType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductFeatureApplTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        Long id = productFeatureApplType.getId();

        defaultProductFeatureApplTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureApplTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureApplTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where name equals to
        defaultProductFeatureApplTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where name in
        defaultProductFeatureApplTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where name is not null
        defaultProductFeatureApplTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where name contains
        defaultProductFeatureApplTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where name does not contain
        defaultProductFeatureApplTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where description equals to
        defaultProductFeatureApplTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where description in
        defaultProductFeatureApplTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where description is not null
        defaultProductFeatureApplTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where description contains
        defaultProductFeatureApplTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        // Get all the productFeatureApplTypeList where description does not contain
        defaultProductFeatureApplTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductFeatureApplTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureApplTypeShouldBeFound(shouldBeFound);
        defaultProductFeatureApplTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureApplTypeShouldBeFound(String filter) throws Exception {
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureApplType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureApplTypeShouldNotBeFound(String filter) throws Exception {
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureApplTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureApplType() throws Exception {
        // Get the productFeatureApplType
        restProductFeatureApplTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureApplType() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureApplType
        ProductFeatureApplType updatedProductFeatureApplType = productFeatureApplTypeRepository
            .findById(productFeatureApplType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureApplType are not directly saved in db
        em.detach(updatedProductFeatureApplType);
        updatedProductFeatureApplType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureApplTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureApplType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureApplType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureApplTypeToMatchAllProperties(updatedProductFeatureApplType);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureApplType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureApplType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureApplType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureApplType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureApplTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureApplType using partial update
        ProductFeatureApplType partialUpdatedProductFeatureApplType = new ProductFeatureApplType();
        partialUpdatedProductFeatureApplType.setId(productFeatureApplType.getId());

        restProductFeatureApplTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureApplType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureApplType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureApplType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureApplTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureApplType, productFeatureApplType),
            getPersistedProductFeatureApplType(productFeatureApplType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureApplTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureApplType using partial update
        ProductFeatureApplType partialUpdatedProductFeatureApplType = new ProductFeatureApplType();
        partialUpdatedProductFeatureApplType.setId(productFeatureApplType.getId());

        partialUpdatedProductFeatureApplType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureApplTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureApplType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureApplType))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureApplType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureApplTypeUpdatableFieldsEquals(
            partialUpdatedProductFeatureApplType,
            getPersistedProductFeatureApplType(partialUpdatedProductFeatureApplType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureApplType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureApplType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureApplType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureApplType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureApplType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureApplType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureApplType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureApplType() throws Exception {
        // Initialize the database
        insertedProductFeatureApplType = productFeatureApplTypeRepository.saveAndFlush(productFeatureApplType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureApplType
        restProductFeatureApplTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureApplType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureApplTypeRepository.count();
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

    protected ProductFeatureApplType getPersistedProductFeatureApplType(ProductFeatureApplType productFeatureApplType) {
        return productFeatureApplTypeRepository.findById(productFeatureApplType.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureApplTypeToMatchAllProperties(ProductFeatureApplType expectedProductFeatureApplType) {
        assertProductFeatureApplTypeAllPropertiesEquals(
            expectedProductFeatureApplType,
            getPersistedProductFeatureApplType(expectedProductFeatureApplType)
        );
    }

    protected void assertPersistedProductFeatureApplTypeToMatchUpdatableProperties(ProductFeatureApplType expectedProductFeatureApplType) {
        assertProductFeatureApplTypeAllUpdatablePropertiesEquals(
            expectedProductFeatureApplType,
            getPersistedProductFeatureApplType(expectedProductFeatureApplType)
        );
    }
}
