package com.snp.web.rest;

import static com.snp.domain.ProductAssocTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductAssocType;
import com.snp.repository.ProductAssocTypeRepository;
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
 * Integration tests for the {@link ProductAssocTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductAssocTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-assoc-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAssocTypeRepository productAssocTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAssocTypeMockMvc;

    private ProductAssocType productAssocType;

    private ProductAssocType insertedProductAssocType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAssocType createEntity() {
        return new ProductAssocType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAssocType createUpdatedEntity() {
        return new ProductAssocType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productAssocType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductAssocType != null) {
            productAssocTypeRepository.delete(insertedProductAssocType);
            insertedProductAssocType = null;
        }
    }

    @Test
    @Transactional
    void createProductAssocType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductAssocType
        var returnedProductAssocType = om.readValue(
            restProductAssocTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssocType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAssocType.class
        );

        // Validate the ProductAssocType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAssocTypeUpdatableFieldsEquals(returnedProductAssocType, getPersistedProductAssocType(returnedProductAssocType));

        insertedProductAssocType = returnedProductAssocType;
    }

    @Test
    @Transactional
    void createProductAssocTypeWithExistingId() throws Exception {
        // Create the ProductAssocType with an existing ID
        productAssocType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAssocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssocType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productAssocType.setName(null);

        // Create the ProductAssocType, which fails.

        restProductAssocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssocType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductAssocTypes() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAssocType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductAssocType() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get the productAssocType
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productAssocType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAssocType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductAssocTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        Long id = productAssocType.getId();

        defaultProductAssocTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductAssocTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductAssocTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where name equals to
        defaultProductAssocTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where name in
        defaultProductAssocTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where name is not null
        defaultProductAssocTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where name contains
        defaultProductAssocTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where name does not contain
        defaultProductAssocTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where description equals to
        defaultProductAssocTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where description in
        defaultProductAssocTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where description is not null
        defaultProductAssocTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where description contains
        defaultProductAssocTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductAssocTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        // Get all the productAssocTypeList where description does not contain
        defaultProductAssocTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductAssocTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductAssocTypeShouldBeFound(shouldBeFound);
        defaultProductAssocTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductAssocTypeShouldBeFound(String filter) throws Exception {
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAssocType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductAssocTypeShouldNotBeFound(String filter) throws Exception {
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductAssocType() throws Exception {
        // Get the productAssocType
        restProductAssocTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAssocType() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssocType
        ProductAssocType updatedProductAssocType = productAssocTypeRepository.findById(productAssocType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAssocType are not directly saved in db
        em.detach(updatedProductAssocType);
        updatedProductAssocType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAssocType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAssocType))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAssocTypeToMatchAllProperties(updatedProductAssocType);
    }

    @Test
    @Transactional
    void putNonExistingProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAssocType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssocType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductAssocTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssocType using partial update
        ProductAssocType partialUpdatedProductAssocType = new ProductAssocType();
        partialUpdatedProductAssocType.setId(productAssocType.getId());

        partialUpdatedProductAssocType.name(UPDATED_NAME);

        restProductAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAssocType))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssocType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAssocTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAssocType, productAssocType),
            getPersistedProductAssocType(productAssocType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAssocTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssocType using partial update
        ProductAssocType partialUpdatedProductAssocType = new ProductAssocType();
        partialUpdatedProductAssocType.setId(productAssocType.getId());

        partialUpdatedProductAssocType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAssocType))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssocType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAssocTypeUpdatableFieldsEquals(
            partialUpdatedProductAssocType,
            getPersistedProductAssocType(partialUpdatedProductAssocType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAssocType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductAssocType() throws Exception {
        // Initialize the database
        insertedProductAssocType = productAssocTypeRepository.saveAndFlush(productAssocType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productAssocType
        restProductAssocTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAssocType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productAssocTypeRepository.count();
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

    protected ProductAssocType getPersistedProductAssocType(ProductAssocType productAssocType) {
        return productAssocTypeRepository.findById(productAssocType.getId()).orElseThrow();
    }

    protected void assertPersistedProductAssocTypeToMatchAllProperties(ProductAssocType expectedProductAssocType) {
        assertProductAssocTypeAllPropertiesEquals(expectedProductAssocType, getPersistedProductAssocType(expectedProductAssocType));
    }

    protected void assertPersistedProductAssocTypeToMatchUpdatableProperties(ProductAssocType expectedProductAssocType) {
        assertProductAssocTypeAllUpdatablePropertiesEquals(
            expectedProductAssocType,
            getPersistedProductAssocType(expectedProductAssocType)
        );
    }
}
