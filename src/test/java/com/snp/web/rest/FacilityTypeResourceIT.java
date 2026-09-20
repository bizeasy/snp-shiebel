package com.snp.web.rest;

import static com.snp.domain.FacilityTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.FacilityType;
import com.snp.repository.FacilityTypeRepository;
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
 * Integration tests for the {@link FacilityTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacilityTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facility-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityTypeRepository facilityTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityTypeMockMvc;

    private FacilityType facilityType;

    private FacilityType insertedFacilityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityType createEntity() {
        return new FacilityType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityType createUpdatedEntity() {
        return new FacilityType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        facilityType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityType != null) {
            facilityTypeRepository.delete(insertedFacilityType);
            insertedFacilityType = null;
        }
    }

    @Test
    @Transactional
    void createFacilityType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityType
        var returnedFacilityType = om.readValue(
            restFacilityTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityType.class
        );

        // Validate the FacilityType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityTypeUpdatableFieldsEquals(returnedFacilityType, getPersistedFacilityType(returnedFacilityType));

        insertedFacilityType = returnedFacilityType;
    }

    @Test
    @Transactional
    void createFacilityTypeWithExistingId() throws Exception {
        // Create the FacilityType with an existing ID
        facilityType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityType)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityType.setName(null);

        // Create the FacilityType, which fails.

        restFacilityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityTypes() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFacilityType() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get the facilityType
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFacilityTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        Long id = facilityType.getId();

        defaultFacilityTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where name equals to
        defaultFacilityTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where name in
        defaultFacilityTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where name is not null
        defaultFacilityTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where name contains
        defaultFacilityTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where name does not contain
        defaultFacilityTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where description equals to
        defaultFacilityTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where description in
        defaultFacilityTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where description is not null
        defaultFacilityTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where description contains
        defaultFacilityTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        // Get all the facilityTypeList where description does not contain
        defaultFacilityTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultFacilityTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityTypeShouldBeFound(shouldBeFound);
        defaultFacilityTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityTypeShouldBeFound(String filter) throws Exception {
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityTypeShouldNotBeFound(String filter) throws Exception {
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityType() throws Exception {
        // Get the facilityType
        restFacilityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityType() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityType
        FacilityType updatedFacilityType = facilityTypeRepository.findById(facilityType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityType are not directly saved in db
        em.detach(updatedFacilityType);
        updatedFacilityType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityTypeToMatchAllProperties(updatedFacilityType);
    }

    @Test
    @Transactional
    void putNonExistingFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityType using partial update
        FacilityType partialUpdatedFacilityType = new FacilityType();
        partialUpdatedFacilityType.setId(facilityType.getId());

        partialUpdatedFacilityType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityType, facilityType),
            getPersistedFacilityType(facilityType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityType using partial update
        FacilityType partialUpdatedFacilityType = new FacilityType();
        partialUpdatedFacilityType.setId(facilityType.getId());

        partialUpdatedFacilityType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityTypeUpdatableFieldsEquals(partialUpdatedFacilityType, getPersistedFacilityType(partialUpdatedFacilityType));
    }

    @Test
    @Transactional
    void patchNonExistingFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityType() throws Exception {
        // Initialize the database
        insertedFacilityType = facilityTypeRepository.saveAndFlush(facilityType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityType
        restFacilityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityTypeRepository.count();
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

    protected FacilityType getPersistedFacilityType(FacilityType facilityType) {
        return facilityTypeRepository.findById(facilityType.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityTypeToMatchAllProperties(FacilityType expectedFacilityType) {
        assertFacilityTypeAllPropertiesEquals(expectedFacilityType, getPersistedFacilityType(expectedFacilityType));
    }

    protected void assertPersistedFacilityTypeToMatchUpdatableProperties(FacilityType expectedFacilityType) {
        assertFacilityTypeAllUpdatablePropertiesEquals(expectedFacilityType, getPersistedFacilityType(expectedFacilityType));
    }
}
