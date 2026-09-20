package com.snp.web.rest;

import static com.snp.domain.ShipmentTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ShipmentType;
import com.snp.repository.ShipmentTypeRepository;
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
 * Integration tests for the {@link ShipmentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentTypeRepository shipmentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentTypeMockMvc;

    private ShipmentType shipmentType;

    private ShipmentType insertedShipmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentType createEntity() {
        return new ShipmentType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentType createUpdatedEntity() {
        return new ShipmentType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        shipmentType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentType != null) {
            shipmentTypeRepository.delete(insertedShipmentType);
            insertedShipmentType = null;
        }
    }

    @Test
    @Transactional
    void createShipmentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentType
        var returnedShipmentType = om.readValue(
            restShipmentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentType.class
        );

        // Validate the ShipmentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentTypeUpdatableFieldsEquals(returnedShipmentType, getPersistedShipmentType(returnedShipmentType));

        insertedShipmentType = returnedShipmentType;
    }

    @Test
    @Transactional
    void createShipmentTypeWithExistingId() throws Exception {
        // Create the ShipmentType with an existing ID
        shipmentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentType)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentType.setName(null);

        // Create the ShipmentType, which fails.

        restShipmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentTypes() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getShipmentType() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get the shipmentType
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getShipmentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        Long id = shipmentType.getId();

        defaultShipmentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where name equals to
        defaultShipmentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where name in
        defaultShipmentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where name is not null
        defaultShipmentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where name contains
        defaultShipmentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where name does not contain
        defaultShipmentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where description equals to
        defaultShipmentTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where description in
        defaultShipmentTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShipmentTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where description is not null
        defaultShipmentTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where description contains
        defaultShipmentTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShipmentTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList where description does not contain
        defaultShipmentTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultShipmentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentTypeShouldBeFound(shouldBeFound);
        defaultShipmentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentTypeShouldBeFound(String filter) throws Exception {
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentTypeShouldNotBeFound(String filter) throws Exception {
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentType() throws Exception {
        // Get the shipmentType
        restShipmentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentType() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentType
        ShipmentType updatedShipmentType = shipmentTypeRepository.findById(shipmentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentType are not directly saved in db
        em.detach(updatedShipmentType);
        updatedShipmentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentTypeToMatchAllProperties(updatedShipmentType);
    }

    @Test
    @Transactional
    void putNonExistingShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentType using partial update
        ShipmentType partialUpdatedShipmentType = new ShipmentType();
        partialUpdatedShipmentType.setId(shipmentType.getId());

        partialUpdatedShipmentType.description(UPDATED_DESCRIPTION);

        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentType, shipmentType),
            getPersistedShipmentType(shipmentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentType using partial update
        ShipmentType partialUpdatedShipmentType = new ShipmentType();
        partialUpdatedShipmentType.setId(shipmentType.getId());

        partialUpdatedShipmentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentTypeUpdatableFieldsEquals(partialUpdatedShipmentType, getPersistedShipmentType(partialUpdatedShipmentType));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentType() throws Exception {
        // Initialize the database
        insertedShipmentType = shipmentTypeRepository.saveAndFlush(shipmentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentType
        restShipmentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentTypeRepository.count();
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

    protected ShipmentType getPersistedShipmentType(ShipmentType shipmentType) {
        return shipmentTypeRepository.findById(shipmentType.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentTypeToMatchAllProperties(ShipmentType expectedShipmentType) {
        assertShipmentTypeAllPropertiesEquals(expectedShipmentType, getPersistedShipmentType(expectedShipmentType));
    }

    protected void assertPersistedShipmentTypeToMatchUpdatableProperties(ShipmentType expectedShipmentType) {
        assertShipmentTypeAllUpdatablePropertiesEquals(expectedShipmentType, getPersistedShipmentType(expectedShipmentType));
    }
}
