package com.snp.web.rest;

import static com.snp.domain.ProductFeatureGroupAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeatureGroup;
import com.snp.repository.ProductFeatureGroupRepository;
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
 * Integration tests for the {@link ProductFeatureGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-feature-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureGroupRepository productFeatureGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureGroupMockMvc;

    private ProductFeatureGroup productFeatureGroup;

    private ProductFeatureGroup insertedProductFeatureGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureGroup createEntity() {
        return new ProductFeatureGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureGroup createUpdatedEntity() {
        return new ProductFeatureGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        productFeatureGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureGroup != null) {
            productFeatureGroupRepository.delete(insertedProductFeatureGroup);
            insertedProductFeatureGroup = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureGroup
        var returnedProductFeatureGroup = om.readValue(
            restProductFeatureGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureGroup.class
        );

        // Validate the ProductFeatureGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureGroupUpdatableFieldsEquals(
            returnedProductFeatureGroup,
            getPersistedProductFeatureGroup(returnedProductFeatureGroup)
        );

        insertedProductFeatureGroup = returnedProductFeatureGroup;
    }

    @Test
    @Transactional
    void createProductFeatureGroupWithExistingId() throws Exception {
        // Create the ProductFeatureGroup with an existing ID
        productFeatureGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureGroup.setName(null);

        // Create the ProductFeatureGroup, which fails.

        restProductFeatureGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroups() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductFeatureGroup() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get the productFeatureGroup
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductFeatureGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        Long id = productFeatureGroup.getId();

        defaultProductFeatureGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where name equals to
        defaultProductFeatureGroupFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where name in
        defaultProductFeatureGroupFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where name is not null
        defaultProductFeatureGroupFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where name contains
        defaultProductFeatureGroupFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where name does not contain
        defaultProductFeatureGroupFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where description equals to
        defaultProductFeatureGroupFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where description in
        defaultProductFeatureGroupFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where description is not null
        defaultProductFeatureGroupFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where description contains
        defaultProductFeatureGroupFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        // Get all the productFeatureGroupList where description does not contain
        defaultProductFeatureGroupFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultProductFeatureGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureGroupShouldBeFound(shouldBeFound);
        defaultProductFeatureGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureGroupShouldBeFound(String filter) throws Exception {
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureGroupShouldNotBeFound(String filter) throws Exception {
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureGroup() throws Exception {
        // Get the productFeatureGroup
        restProductFeatureGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureGroup() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroup
        ProductFeatureGroup updatedProductFeatureGroup = productFeatureGroupRepository.findById(productFeatureGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureGroup are not directly saved in db
        em.detach(updatedProductFeatureGroup);
        updatedProductFeatureGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureGroupToMatchAllProperties(updatedProductFeatureGroup);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureGroupWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroup using partial update
        ProductFeatureGroup partialUpdatedProductFeatureGroup = new ProductFeatureGroup();
        partialUpdatedProductFeatureGroup.setId(productFeatureGroup.getId());

        partialUpdatedProductFeatureGroup.description(UPDATED_DESCRIPTION);

        restProductFeatureGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureGroup, productFeatureGroup),
            getPersistedProductFeatureGroup(productFeatureGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureGroupWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroup using partial update
        ProductFeatureGroup partialUpdatedProductFeatureGroup = new ProductFeatureGroup();
        partialUpdatedProductFeatureGroup.setId(productFeatureGroup.getId());

        partialUpdatedProductFeatureGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductFeatureGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureGroupUpdatableFieldsEquals(
            partialUpdatedProductFeatureGroup,
            getPersistedProductFeatureGroup(partialUpdatedProductFeatureGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureGroup() throws Exception {
        // Initialize the database
        insertedProductFeatureGroup = productFeatureGroupRepository.saveAndFlush(productFeatureGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureGroup
        restProductFeatureGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureGroupRepository.count();
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

    protected ProductFeatureGroup getPersistedProductFeatureGroup(ProductFeatureGroup productFeatureGroup) {
        return productFeatureGroupRepository.findById(productFeatureGroup.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureGroupToMatchAllProperties(ProductFeatureGroup expectedProductFeatureGroup) {
        assertProductFeatureGroupAllPropertiesEquals(
            expectedProductFeatureGroup,
            getPersistedProductFeatureGroup(expectedProductFeatureGroup)
        );
    }

    protected void assertPersistedProductFeatureGroupToMatchUpdatableProperties(ProductFeatureGroup expectedProductFeatureGroup) {
        assertProductFeatureGroupAllUpdatablePropertiesEquals(
            expectedProductFeatureGroup,
            getPersistedProductFeatureGroup(expectedProductFeatureGroup)
        );
    }
}
