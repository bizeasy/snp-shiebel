package com.snp.web.rest;

import static com.snp.domain.ProductTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductType;
import com.snp.repository.ProductTypeRepository;
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
 * Integration tests for the {@link ProductTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTypeMockMvc;

    private ProductType productType;

    private ProductType insertedProductType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createEntity() {
        return new ProductType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createUpdatedEntity() {
        return new ProductType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductType != null) {
            productTypeRepository.delete(insertedProductType);
            insertedProductType = null;
        }
    }

    @Test
    @Transactional
    void createProductType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductType
        var returnedProductType = om.readValue(
            restProductTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductType.class
        );

        // Validate the ProductType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductTypeUpdatableFieldsEquals(returnedProductType, getPersistedProductType(returnedProductType));

        insertedProductType = returnedProductType;
    }

    @Test
    @Transactional
    void createProductTypeWithExistingId() throws Exception {
        // Create the ProductType with an existing ID
        productType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productType.setName(null);

        // Create the ProductType, which fails.

        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductTypes() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        Long id = productType.getId();

        defaultProductTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name equals to
        defaultProductTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name in
        defaultProductTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name is not null
        defaultProductTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name contains
        defaultProductTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name does not contain
        defaultProductTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where description equals to
        defaultProductTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where description in
        defaultProductTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where description is not null
        defaultProductTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where description contains
        defaultProductTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where description does not contain
        defaultProductTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductTypeShouldBeFound(shouldBeFound);
        defaultProductTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductTypeShouldBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductTypeShouldNotBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductType() throws Exception {
        // Get the productType
        restProductTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType
        ProductType updatedProductType = productTypeRepository.findById(productType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductType are not directly saved in db
        em.detach(updatedProductType);
        updatedProductType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductTypeToMatchAllProperties(updatedProductType);
    }

    @Test
    @Transactional
    void putNonExistingProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.description(UPDATED_DESCRIPTION);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductType, productType),
            getPersistedProductType(productType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductTypeUpdatableFieldsEquals(partialUpdatedProductType, getPersistedProductType(partialUpdatedProductType));
    }

    @Test
    @Transactional
    void patchNonExistingProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productType
        restProductTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productTypeRepository.count();
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

    protected ProductType getPersistedProductType(ProductType productType) {
        return productTypeRepository.findById(productType.getId()).orElseThrow();
    }

    protected void assertPersistedProductTypeToMatchAllProperties(ProductType expectedProductType) {
        assertProductTypeAllPropertiesEquals(expectedProductType, getPersistedProductType(expectedProductType));
    }

    protected void assertPersistedProductTypeToMatchUpdatableProperties(ProductType expectedProductType) {
        assertProductTypeAllUpdatablePropertiesEquals(expectedProductType, getPersistedProductType(expectedProductType));
    }
}
