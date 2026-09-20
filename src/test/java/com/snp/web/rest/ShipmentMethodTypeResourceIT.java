package com.snp.web.rest;

import static com.snp.domain.ShipmentMethodTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ShipmentMethodType;
import com.snp.repository.ShipmentMethodTypeRepository;
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
 * Integration tests for the {@link ShipmentMethodTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentMethodTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/shipment-method-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentMethodTypeRepository shipmentMethodTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentMethodTypeMockMvc;

    private ShipmentMethodType shipmentMethodType;

    private ShipmentMethodType insertedShipmentMethodType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentMethodType createEntity() {
        return new ShipmentMethodType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).sequenceNo(DEFAULT_SEQUENCE_NO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentMethodType createUpdatedEntity() {
        return new ShipmentMethodType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);
    }

    @BeforeEach
    void initTest() {
        shipmentMethodType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentMethodType != null) {
            shipmentMethodTypeRepository.delete(insertedShipmentMethodType);
            insertedShipmentMethodType = null;
        }
    }

    @Test
    @Transactional
    void createShipmentMethodType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentMethodType
        var returnedShipmentMethodType = om.readValue(
            restShipmentMethodTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMethodType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentMethodType.class
        );

        // Validate the ShipmentMethodType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentMethodTypeUpdatableFieldsEquals(
            returnedShipmentMethodType,
            getPersistedShipmentMethodType(returnedShipmentMethodType)
        );

        insertedShipmentMethodType = returnedShipmentMethodType;
    }

    @Test
    @Transactional
    void createShipmentMethodTypeWithExistingId() throws Exception {
        // Create the ShipmentMethodType with an existing ID
        shipmentMethodType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMethodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMethodType)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentMethodType.setName(null);

        // Create the ShipmentMethodType, which fails.

        restShipmentMethodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMethodType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypes() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMethodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @Test
    @Transactional
    void getShipmentMethodType() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get the shipmentMethodType
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentMethodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentMethodType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getShipmentMethodTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        Long id = shipmentMethodType.getId();

        defaultShipmentMethodTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentMethodTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentMethodTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where name equals to
        defaultShipmentMethodTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where name in
        defaultShipmentMethodTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where name is not null
        defaultShipmentMethodTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where name contains
        defaultShipmentMethodTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where name does not contain
        defaultShipmentMethodTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where description equals to
        defaultShipmentMethodTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where description in
        defaultShipmentMethodTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where description is not null
        defaultShipmentMethodTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where description contains
        defaultShipmentMethodTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where description does not contain
        defaultShipmentMethodTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo equals to
        defaultShipmentMethodTypeFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo in
        defaultShipmentMethodTypeFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo is not null
        defaultShipmentMethodTypeFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo is greater than or equal to
        defaultShipmentMethodTypeFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo is less than or equal to
        defaultShipmentMethodTypeFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo is less than
        defaultShipmentMethodTypeFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentMethodTypesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        // Get all the shipmentMethodTypeList where sequenceNo is greater than
        defaultShipmentMethodTypeFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    private void defaultShipmentMethodTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentMethodTypeShouldBeFound(shouldBeFound);
        defaultShipmentMethodTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentMethodTypeShouldBeFound(String filter) throws Exception {
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentMethodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentMethodTypeShouldNotBeFound(String filter) throws Exception {
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentMethodType() throws Exception {
        // Get the shipmentMethodType
        restShipmentMethodTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentMethodType() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMethodType
        ShipmentMethodType updatedShipmentMethodType = shipmentMethodTypeRepository.findById(shipmentMethodType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentMethodType are not directly saved in db
        em.detach(updatedShipmentMethodType);
        updatedShipmentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restShipmentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentMethodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentMethodTypeToMatchAllProperties(updatedShipmentMethodType);
    }

    @Test
    @Transactional
    void putNonExistingShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentMethodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentMethodType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentMethodTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMethodType using partial update
        ShipmentMethodType partialUpdatedShipmentMethodType = new ShipmentMethodType();
        partialUpdatedShipmentMethodType.setId(shipmentMethodType.getId());

        partialUpdatedShipmentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restShipmentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMethodType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMethodTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentMethodType, shipmentMethodType),
            getPersistedShipmentMethodType(shipmentMethodType)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentMethodTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentMethodType using partial update
        ShipmentMethodType partialUpdatedShipmentMethodType = new ShipmentMethodType();
        partialUpdatedShipmentMethodType.setId(shipmentMethodType.getId());

        partialUpdatedShipmentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restShipmentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentMethodType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentMethodTypeUpdatableFieldsEquals(
            partialUpdatedShipmentMethodType,
            getPersistedShipmentMethodType(partialUpdatedShipmentMethodType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMethodTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentMethodType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentMethodType() throws Exception {
        // Initialize the database
        insertedShipmentMethodType = shipmentMethodTypeRepository.saveAndFlush(shipmentMethodType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentMethodType
        restShipmentMethodTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentMethodType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentMethodTypeRepository.count();
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

    protected ShipmentMethodType getPersistedShipmentMethodType(ShipmentMethodType shipmentMethodType) {
        return shipmentMethodTypeRepository.findById(shipmentMethodType.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentMethodTypeToMatchAllProperties(ShipmentMethodType expectedShipmentMethodType) {
        assertShipmentMethodTypeAllPropertiesEquals(expectedShipmentMethodType, getPersistedShipmentMethodType(expectedShipmentMethodType));
    }

    protected void assertPersistedShipmentMethodTypeToMatchUpdatableProperties(ShipmentMethodType expectedShipmentMethodType) {
        assertShipmentMethodTypeAllUpdatablePropertiesEquals(
            expectedShipmentMethodType,
            getPersistedShipmentMethodType(expectedShipmentMethodType)
        );
    }
}
