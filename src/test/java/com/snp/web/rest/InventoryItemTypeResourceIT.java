package com.snp.web.rest;

import static com.snp.domain.InventoryItemTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItemType;
import com.snp.repository.InventoryItemTypeRepository;
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
 * Integration tests for the {@link InventoryItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventory-item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemTypeRepository inventoryItemTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemTypeMockMvc;

    private InventoryItemType inventoryItemType;

    private InventoryItemType insertedInventoryItemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemType createEntity() {
        return new InventoryItemType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemType createUpdatedEntity() {
        return new InventoryItemType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        inventoryItemType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItemType != null) {
            inventoryItemTypeRepository.delete(insertedInventoryItemType);
            insertedInventoryItemType = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItemType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryItemType
        var returnedInventoryItemType = om.readValue(
            restInventoryItemTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItemType.class
        );

        // Validate the InventoryItemType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventoryItemTypeUpdatableFieldsEquals(returnedInventoryItemType, getPersistedInventoryItemType(returnedInventoryItemType));

        insertedInventoryItemType = returnedInventoryItemType;
    }

    @Test
    @Transactional
    void createInventoryItemTypeWithExistingId() throws Exception {
        // Create the InventoryItemType with an existing ID
        inventoryItemType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemType)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventoryItemType.setName(null);

        // Create the InventoryItemType, which fails.

        restInventoryItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypes() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getInventoryItemType() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get the inventoryItemType
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItemType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInventoryItemTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        Long id = inventoryItemType.getId();

        defaultInventoryItemTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where name equals to
        defaultInventoryItemTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where name in
        defaultInventoryItemTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where name is not null
        defaultInventoryItemTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where name contains
        defaultInventoryItemTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where name does not contain
        defaultInventoryItemTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where description equals to
        defaultInventoryItemTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where description in
        defaultInventoryItemTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where description is not null
        defaultInventoryItemTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where description contains
        defaultInventoryItemTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInventoryItemTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        // Get all the inventoryItemTypeList where description does not contain
        defaultInventoryItemTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultInventoryItemTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemTypeShouldBeFound(shouldBeFound);
        defaultInventoryItemTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemTypeShouldBeFound(String filter) throws Exception {
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemTypeShouldNotBeFound(String filter) throws Exception {
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItemType() throws Exception {
        // Get the inventoryItemType
        restInventoryItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItemType() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemType
        InventoryItemType updatedInventoryItemType = inventoryItemTypeRepository.findById(inventoryItemType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItemType are not directly saved in db
        em.detach(updatedInventoryItemType);
        updatedInventoryItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInventoryItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventoryItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventoryItemType))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemTypeToMatchAllProperties(updatedInventoryItemType);
    }

    @Test
    @Transactional
    void putNonExistingInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemType using partial update
        InventoryItemType partialUpdatedInventoryItemType = new InventoryItemType();
        partialUpdatedInventoryItemType.setId(inventoryItemType.getId());

        restInventoryItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemType))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItemType, inventoryItemType),
            getPersistedInventoryItemType(inventoryItemType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemType using partial update
        InventoryItemType partialUpdatedInventoryItemType = new InventoryItemType();
        partialUpdatedInventoryItemType.setId(inventoryItemType.getId());

        partialUpdatedInventoryItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInventoryItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemType))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemTypeUpdatableFieldsEquals(
            partialUpdatedInventoryItemType,
            getPersistedInventoryItemType(partialUpdatedInventoryItemType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryItemType() throws Exception {
        // Initialize the database
        insertedInventoryItemType = inventoryItemTypeRepository.saveAndFlush(inventoryItemType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryItemType
        restInventoryItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItemType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryItemTypeRepository.count();
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

    protected InventoryItemType getPersistedInventoryItemType(InventoryItemType inventoryItemType) {
        return inventoryItemTypeRepository.findById(inventoryItemType.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemTypeToMatchAllProperties(InventoryItemType expectedInventoryItemType) {
        assertInventoryItemTypeAllPropertiesEquals(expectedInventoryItemType, getPersistedInventoryItemType(expectedInventoryItemType));
    }

    protected void assertPersistedInventoryItemTypeToMatchUpdatableProperties(InventoryItemType expectedInventoryItemType) {
        assertInventoryItemTypeAllUpdatablePropertiesEquals(
            expectedInventoryItemType,
            getPersistedInventoryItemType(expectedInventoryItemType)
        );
    }
}
