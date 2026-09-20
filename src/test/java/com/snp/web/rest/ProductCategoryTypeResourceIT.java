package com.snp.web.rest;

import static com.snp.domain.ProductCategoryTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductCategoryType;
import com.snp.repository.ProductCategoryTypeRepository;
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
 * Integration tests for the {@link ProductCategoryTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductCategoryTypeRepository productCategoryTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCategoryTypeMockMvc;

    private ProductCategoryType productCategoryType;

    private ProductCategoryType insertedProductCategoryType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategoryType createEntity() {
        return new ProductCategoryType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategoryType createUpdatedEntity() {
        return new ProductCategoryType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productCategoryType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductCategoryType != null) {
            productCategoryTypeRepository.delete(insertedProductCategoryType);
            insertedProductCategoryType = null;
        }
    }

    @Test
    @Transactional
    void createProductCategoryType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductCategoryType
        var returnedProductCategoryType = om.readValue(
            restProductCategoryTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductCategoryType.class
        );

        // Validate the ProductCategoryType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductCategoryTypeUpdatableFieldsEquals(
            returnedProductCategoryType,
            getPersistedProductCategoryType(returnedProductCategoryType)
        );

        insertedProductCategoryType = returnedProductCategoryType;
    }

    @Test
    @Transactional
    void createProductCategoryTypeWithExistingId() throws Exception {
        // Create the ProductCategoryType with an existing ID
        productCategoryType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productCategoryType.setName(null);

        // Create the ProductCategoryType, which fails.

        restProductCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypes() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductCategoryType() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get the productCategoryType
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCategoryType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductCategoryTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        Long id = productCategoryType.getId();

        defaultProductCategoryTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductCategoryTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductCategoryTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where name equals to
        defaultProductCategoryTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where name in
        defaultProductCategoryTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where name is not null
        defaultProductCategoryTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where name contains
        defaultProductCategoryTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where name does not contain
        defaultProductCategoryTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where description equals to
        defaultProductCategoryTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where description in
        defaultProductCategoryTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where description is not null
        defaultProductCategoryTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where description contains
        defaultProductCategoryTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductCategoryTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        // Get all the productCategoryTypeList where description does not contain
        defaultProductCategoryTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductCategoryTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductCategoryTypeShouldBeFound(shouldBeFound);
        defaultProductCategoryTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductCategoryTypeShouldBeFound(String filter) throws Exception {
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductCategoryTypeShouldNotBeFound(String filter) throws Exception {
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductCategoryType() throws Exception {
        // Get the productCategoryType
        restProductCategoryTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductCategoryType() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryType
        ProductCategoryType updatedProductCategoryType = productCategoryTypeRepository.findById(productCategoryType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductCategoryType are not directly saved in db
        em.detach(updatedProductCategoryType);
        updatedProductCategoryType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCategoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductCategoryTypeToMatchAllProperties(updatedProductCategoryType);
    }

    @Test
    @Transactional
    void putNonExistingProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCategoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryType using partial update
        ProductCategoryType partialUpdatedProductCategoryType = new ProductCategoryType();
        partialUpdatedProductCategoryType.setId(productCategoryType.getId());

        partialUpdatedProductCategoryType.description(UPDATED_DESCRIPTION);

        restProductCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductCategoryType, productCategoryType),
            getPersistedProductCategoryType(productCategoryType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryType using partial update
        ProductCategoryType partialUpdatedProductCategoryType = new ProductCategoryType();
        partialUpdatedProductCategoryType.setId(productCategoryType.getId());

        partialUpdatedProductCategoryType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryTypeUpdatableFieldsEquals(
            partialUpdatedProductCategoryType,
            getPersistedProductCategoryType(partialUpdatedProductCategoryType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productCategoryType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCategoryType() throws Exception {
        // Initialize the database
        insertedProductCategoryType = productCategoryTypeRepository.saveAndFlush(productCategoryType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productCategoryType
        restProductCategoryTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCategoryType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productCategoryTypeRepository.count();
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

    protected ProductCategoryType getPersistedProductCategoryType(ProductCategoryType productCategoryType) {
        return productCategoryTypeRepository.findById(productCategoryType.getId()).orElseThrow();
    }

    protected void assertPersistedProductCategoryTypeToMatchAllProperties(ProductCategoryType expectedProductCategoryType) {
        assertProductCategoryTypeAllPropertiesEquals(
            expectedProductCategoryType,
            getPersistedProductCategoryType(expectedProductCategoryType)
        );
    }

    protected void assertPersistedProductCategoryTypeToMatchUpdatableProperties(ProductCategoryType expectedProductCategoryType) {
        assertProductCategoryTypeAllUpdatablePropertiesEquals(
            expectedProductCategoryType,
            getPersistedProductCategoryType(expectedProductCategoryType)
        );
    }
}
