package com.snp.web.rest;

import static com.snp.domain.ProductPricePurposeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductPricePurpose;
import com.snp.repository.ProductPricePurposeRepository;
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
 * Integration tests for the {@link ProductPricePurposeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductPricePurposeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-price-purposes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductPricePurposeRepository productPricePurposeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPricePurposeMockMvc;

    private ProductPricePurpose productPricePurpose;

    private ProductPricePurpose insertedProductPricePurpose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPricePurpose createEntity() {
        return new ProductPricePurpose().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPricePurpose createUpdatedEntity() {
        return new ProductPricePurpose().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productPricePurpose = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductPricePurpose != null) {
            productPricePurposeRepository.delete(insertedProductPricePurpose);
            insertedProductPricePurpose = null;
        }
    }

    @Test
    @Transactional
    void createProductPricePurpose() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductPricePurpose
        var returnedProductPricePurpose = om.readValue(
            restProductPricePurposeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPricePurpose)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductPricePurpose.class
        );

        // Validate the ProductPricePurpose in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductPricePurposeUpdatableFieldsEquals(
            returnedProductPricePurpose,
            getPersistedProductPricePurpose(returnedProductPricePurpose)
        );

        insertedProductPricePurpose = returnedProductPricePurpose;
    }

    @Test
    @Transactional
    void createProductPricePurposeWithExistingId() throws Exception {
        // Create the ProductPricePurpose with an existing ID
        productPricePurpose.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPricePurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPricePurpose)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productPricePurpose.setName(null);

        // Create the ProductPricePurpose, which fails.

        restProductPricePurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPricePurpose)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductPricePurposes() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPricePurpose.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductPricePurpose() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get the productPricePurpose
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL_ID, productPricePurpose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPricePurpose.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductPricePurposesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        Long id = productPricePurpose.getId();

        defaultProductPricePurposeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductPricePurposeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductPricePurposeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where name equals to
        defaultProductPricePurposeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where name in
        defaultProductPricePurposeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where name is not null
        defaultProductPricePurposeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where name contains
        defaultProductPricePurposeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where name does not contain
        defaultProductPricePurposeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where description equals to
        defaultProductPricePurposeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where description in
        defaultProductPricePurposeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where description is not null
        defaultProductPricePurposeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where description contains
        defaultProductPricePurposeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductPricePurposesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        // Get all the productPricePurposeList where description does not contain
        defaultProductPricePurposeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductPricePurposeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductPricePurposeShouldBeFound(shouldBeFound);
        defaultProductPricePurposeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductPricePurposeShouldBeFound(String filter) throws Exception {
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPricePurpose.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductPricePurposeShouldNotBeFound(String filter) throws Exception {
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductPricePurposeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductPricePurpose() throws Exception {
        // Get the productPricePurpose
        restProductPricePurposeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPricePurpose() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPricePurpose
        ProductPricePurpose updatedProductPricePurpose = productPricePurposeRepository.findById(productPricePurpose.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductPricePurpose are not directly saved in db
        em.detach(updatedProductPricePurpose);
        updatedProductPricePurpose.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductPricePurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductPricePurpose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductPricePurpose))
            )
            .andExpect(status().isOk());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductPricePurposeToMatchAllProperties(updatedProductPricePurpose);
    }

    @Test
    @Transactional
    void putNonExistingProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPricePurpose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPricePurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPricePurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPricePurpose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPricePurposeWithPatch() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPricePurpose using partial update
        ProductPricePurpose partialUpdatedProductPricePurpose = new ProductPricePurpose();
        partialUpdatedProductPricePurpose.setId(productPricePurpose.getId());

        partialUpdatedProductPricePurpose.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductPricePurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPricePurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPricePurpose))
            )
            .andExpect(status().isOk());

        // Validate the ProductPricePurpose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPricePurposeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductPricePurpose, productPricePurpose),
            getPersistedProductPricePurpose(productPricePurpose)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductPricePurposeWithPatch() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPricePurpose using partial update
        ProductPricePurpose partialUpdatedProductPricePurpose = new ProductPricePurpose();
        partialUpdatedProductPricePurpose.setId(productPricePurpose.getId());

        partialUpdatedProductPricePurpose.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductPricePurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPricePurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPricePurpose))
            )
            .andExpect(status().isOk());

        // Validate the ProductPricePurpose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPricePurposeUpdatableFieldsEquals(
            partialUpdatedProductPricePurpose,
            getPersistedProductPricePurpose(partialUpdatedProductPricePurpose)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPricePurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPricePurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPricePurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPricePurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPricePurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPricePurposeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productPricePurpose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPricePurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPricePurpose() throws Exception {
        // Initialize the database
        insertedProductPricePurpose = productPricePurposeRepository.saveAndFlush(productPricePurpose);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productPricePurpose
        restProductPricePurposeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPricePurpose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productPricePurposeRepository.count();
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

    protected ProductPricePurpose getPersistedProductPricePurpose(ProductPricePurpose productPricePurpose) {
        return productPricePurposeRepository.findById(productPricePurpose.getId()).orElseThrow();
    }

    protected void assertPersistedProductPricePurposeToMatchAllProperties(ProductPricePurpose expectedProductPricePurpose) {
        assertProductPricePurposeAllPropertiesEquals(
            expectedProductPricePurpose,
            getPersistedProductPricePurpose(expectedProductPricePurpose)
        );
    }

    protected void assertPersistedProductPricePurposeToMatchUpdatableProperties(ProductPricePurpose expectedProductPricePurpose) {
        assertProductPricePurposeAllUpdatablePropertiesEquals(
            expectedProductPricePurpose,
            getPersistedProductPricePurpose(expectedProductPricePurpose)
        );
    }
}
