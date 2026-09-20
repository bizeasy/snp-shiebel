package com.snp.web.rest;

import static com.snp.domain.ProductPriceTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductPriceType;
import com.snp.repository.ProductPriceTypeRepository;
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
 * Integration tests for the {@link ProductPriceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductPriceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-price-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductPriceTypeRepository productPriceTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPriceTypeMockMvc;

    private ProductPriceType productPriceType;

    private ProductPriceType insertedProductPriceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPriceType createEntity() {
        return new ProductPriceType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPriceType createUpdatedEntity() {
        return new ProductPriceType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productPriceType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductPriceType != null) {
            productPriceTypeRepository.delete(insertedProductPriceType);
            insertedProductPriceType = null;
        }
    }

    @Test
    @Transactional
    void createProductPriceType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductPriceType
        var returnedProductPriceType = om.readValue(
            restProductPriceTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPriceType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductPriceType.class
        );

        // Validate the ProductPriceType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductPriceTypeUpdatableFieldsEquals(returnedProductPriceType, getPersistedProductPriceType(returnedProductPriceType));

        insertedProductPriceType = returnedProductPriceType;
    }

    @Test
    @Transactional
    void createProductPriceTypeWithExistingId() throws Exception {
        // Create the ProductPriceType with an existing ID
        productPriceType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPriceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPriceType)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productPriceType.setName(null);

        // Create the ProductPriceType, which fails.

        restProductPriceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPriceType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductPriceTypes() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPriceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductPriceType() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get the productPriceType
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productPriceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPriceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductPriceTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        Long id = productPriceType.getId();

        defaultProductPriceTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductPriceTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductPriceTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where name equals to
        defaultProductPriceTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where name in
        defaultProductPriceTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where name is not null
        defaultProductPriceTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where name contains
        defaultProductPriceTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where name does not contain
        defaultProductPriceTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where description equals to
        defaultProductPriceTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where description in
        defaultProductPriceTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where description is not null
        defaultProductPriceTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where description contains
        defaultProductPriceTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductPriceTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        // Get all the productPriceTypeList where description does not contain
        defaultProductPriceTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductPriceTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductPriceTypeShouldBeFound(shouldBeFound);
        defaultProductPriceTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductPriceTypeShouldBeFound(String filter) throws Exception {
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPriceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductPriceTypeShouldNotBeFound(String filter) throws Exception {
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductPriceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductPriceType() throws Exception {
        // Get the productPriceType
        restProductPriceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPriceType() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPriceType
        ProductPriceType updatedProductPriceType = productPriceTypeRepository.findById(productPriceType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductPriceType are not directly saved in db
        em.detach(updatedProductPriceType);
        updatedProductPriceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductPriceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductPriceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductPriceType))
            )
            .andExpect(status().isOk());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductPriceTypeToMatchAllProperties(updatedProductPriceType);
    }

    @Test
    @Transactional
    void putNonExistingProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPriceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPriceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPriceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPriceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPriceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPriceType using partial update
        ProductPriceType partialUpdatedProductPriceType = new ProductPriceType();
        partialUpdatedProductPriceType.setId(productPriceType.getId());

        partialUpdatedProductPriceType.description(UPDATED_DESCRIPTION);

        restProductPriceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPriceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPriceType))
            )
            .andExpect(status().isOk());

        // Validate the ProductPriceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPriceTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductPriceType, productPriceType),
            getPersistedProductPriceType(productPriceType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductPriceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPriceType using partial update
        ProductPriceType partialUpdatedProductPriceType = new ProductPriceType();
        partialUpdatedProductPriceType.setId(productPriceType.getId());

        partialUpdatedProductPriceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductPriceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPriceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPriceType))
            )
            .andExpect(status().isOk());

        // Validate the ProductPriceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPriceTypeUpdatableFieldsEquals(
            partialUpdatedProductPriceType,
            getPersistedProductPriceType(partialUpdatedProductPriceType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPriceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPriceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPriceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPriceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPriceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productPriceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPriceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPriceType() throws Exception {
        // Initialize the database
        insertedProductPriceType = productPriceTypeRepository.saveAndFlush(productPriceType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productPriceType
        restProductPriceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPriceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productPriceTypeRepository.count();
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

    protected ProductPriceType getPersistedProductPriceType(ProductPriceType productPriceType) {
        return productPriceTypeRepository.findById(productPriceType.getId()).orElseThrow();
    }

    protected void assertPersistedProductPriceTypeToMatchAllProperties(ProductPriceType expectedProductPriceType) {
        assertProductPriceTypeAllPropertiesEquals(expectedProductPriceType, getPersistedProductPriceType(expectedProductPriceType));
    }

    protected void assertPersistedProductPriceTypeToMatchUpdatableProperties(ProductPriceType expectedProductPriceType) {
        assertProductPriceTypeAllUpdatablePropertiesEquals(
            expectedProductPriceType,
            getPersistedProductPriceType(expectedProductPriceType)
        );
    }
}
