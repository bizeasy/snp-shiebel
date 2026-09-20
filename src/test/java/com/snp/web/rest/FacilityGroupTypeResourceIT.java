package com.snp.web.rest;

import static com.snp.domain.FacilityGroupTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.FacilityGroupType;
import com.snp.repository.FacilityGroupTypeRepository;
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
 * Integration tests for the {@link FacilityGroupTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacilityGroupTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facility-group-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityGroupTypeRepository facilityGroupTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityGroupTypeMockMvc;

    private FacilityGroupType facilityGroupType;

    private FacilityGroupType insertedFacilityGroupType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroupType createEntity() {
        return new FacilityGroupType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroupType createUpdatedEntity() {
        return new FacilityGroupType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        facilityGroupType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityGroupType != null) {
            facilityGroupTypeRepository.delete(insertedFacilityGroupType);
            insertedFacilityGroupType = null;
        }
    }

    @Test
    @Transactional
    void createFacilityGroupType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityGroupType
        var returnedFacilityGroupType = om.readValue(
            restFacilityGroupTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityGroupType.class
        );

        // Validate the FacilityGroupType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityGroupTypeUpdatableFieldsEquals(returnedFacilityGroupType, getPersistedFacilityGroupType(returnedFacilityGroupType));

        insertedFacilityGroupType = returnedFacilityGroupType;
    }

    @Test
    @Transactional
    void createFacilityGroupTypeWithExistingId() throws Exception {
        // Create the FacilityGroupType with an existing ID
        facilityGroupType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityGroupTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupType)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityGroupType.setName(null);

        // Create the FacilityGroupType, which fails.

        restFacilityGroupTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypes() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroupType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFacilityGroupType() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get the facilityGroupType
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityGroupType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityGroupType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFacilityGroupTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        Long id = facilityGroupType.getId();

        defaultFacilityGroupTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityGroupTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityGroupTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where name equals to
        defaultFacilityGroupTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where name in
        defaultFacilityGroupTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where name is not null
        defaultFacilityGroupTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where name contains
        defaultFacilityGroupTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where name does not contain
        defaultFacilityGroupTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where description equals to
        defaultFacilityGroupTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where description in
        defaultFacilityGroupTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where description is not null
        defaultFacilityGroupTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where description contains
        defaultFacilityGroupTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityGroupTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        // Get all the facilityGroupTypeList where description does not contain
        defaultFacilityGroupTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultFacilityGroupTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityGroupTypeShouldBeFound(shouldBeFound);
        defaultFacilityGroupTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityGroupTypeShouldBeFound(String filter) throws Exception {
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroupType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityGroupTypeShouldNotBeFound(String filter) throws Exception {
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityGroupTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityGroupType() throws Exception {
        // Get the facilityGroupType
        restFacilityGroupTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityGroupType() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupType
        FacilityGroupType updatedFacilityGroupType = facilityGroupTypeRepository.findById(facilityGroupType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityGroupType are not directly saved in db
        em.detach(updatedFacilityGroupType);
        updatedFacilityGroupType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityGroupTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityGroupType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityGroupType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityGroupTypeToMatchAllProperties(updatedFacilityGroupType);
    }

    @Test
    @Transactional
    void putNonExistingFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityGroupType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroupType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroupType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityGroupTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupType using partial update
        FacilityGroupType partialUpdatedFacilityGroupType = new FacilityGroupType();
        partialUpdatedFacilityGroupType.setId(facilityGroupType.getId());

        partialUpdatedFacilityGroupType.name(UPDATED_NAME);

        restFacilityGroupTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroupType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroupType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityGroupType, facilityGroupType),
            getPersistedFacilityGroupType(facilityGroupType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityGroupTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupType using partial update
        FacilityGroupType partialUpdatedFacilityGroupType = new FacilityGroupType();
        partialUpdatedFacilityGroupType.setId(facilityGroupType.getId());

        partialUpdatedFacilityGroupType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityGroupTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroupType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroupType))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupTypeUpdatableFieldsEquals(
            partialUpdatedFacilityGroupType,
            getPersistedFacilityGroupType(partialUpdatedFacilityGroupType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityGroupType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroupType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroupType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityGroupType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityGroupType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroupType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityGroupType() throws Exception {
        // Initialize the database
        insertedFacilityGroupType = facilityGroupTypeRepository.saveAndFlush(facilityGroupType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityGroupType
        restFacilityGroupTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityGroupType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityGroupTypeRepository.count();
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

    protected FacilityGroupType getPersistedFacilityGroupType(FacilityGroupType facilityGroupType) {
        return facilityGroupTypeRepository.findById(facilityGroupType.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityGroupTypeToMatchAllProperties(FacilityGroupType expectedFacilityGroupType) {
        assertFacilityGroupTypeAllPropertiesEquals(expectedFacilityGroupType, getPersistedFacilityGroupType(expectedFacilityGroupType));
    }

    protected void assertPersistedFacilityGroupTypeToMatchUpdatableProperties(FacilityGroupType expectedFacilityGroupType) {
        assertFacilityGroupTypeAllUpdatablePropertiesEquals(
            expectedFacilityGroupType,
            getPersistedFacilityGroupType(expectedFacilityGroupType)
        );
    }
}
