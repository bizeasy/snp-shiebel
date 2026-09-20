package com.snp.web.rest;

import static com.snp.domain.LocationTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.LocationType;
import com.snp.repository.LocationTypeRepository;
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
 * Integration tests for the {@link LocationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationTypeMockMvc;

    private LocationType locationType;

    private LocationType insertedLocationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationType createEntity() {
        return new LocationType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationType createUpdatedEntity() {
        return new LocationType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        locationType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLocationType != null) {
            locationTypeRepository.delete(insertedLocationType);
            insertedLocationType = null;
        }
    }

    @Test
    @Transactional
    void createLocationType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LocationType
        var returnedLocationType = om.readValue(
            restLocationTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LocationType.class
        );

        // Validate the LocationType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocationTypeUpdatableFieldsEquals(returnedLocationType, getPersistedLocationType(returnedLocationType));

        insertedLocationType = returnedLocationType;
    }

    @Test
    @Transactional
    void createLocationTypeWithExistingId() throws Exception {
        // Create the LocationType with an existing ID
        locationType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationType)))
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        locationType.setName(null);

        // Create the LocationType, which fails.

        restLocationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocationTypes() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getLocationType() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get the locationType
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, locationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLocationTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        Long id = locationType.getId();

        defaultLocationTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLocationTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLocationTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where name equals to
        defaultLocationTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where name in
        defaultLocationTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where name is not null
        defaultLocationTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where name contains
        defaultLocationTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where name does not contain
        defaultLocationTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where description equals to
        defaultLocationTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLocationTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where description in
        defaultLocationTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLocationTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where description is not null
        defaultLocationTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where description contains
        defaultLocationTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLocationTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList where description does not contain
        defaultLocationTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultLocationTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLocationTypeShouldBeFound(shouldBeFound);
        defaultLocationTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationTypeShouldBeFound(String filter) throws Exception {
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationTypeShouldNotBeFound(String filter) throws Exception {
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocationType() throws Exception {
        // Get the locationType
        restLocationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocationType() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationType
        LocationType updatedLocationType = locationTypeRepository.findById(locationType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocationType are not directly saved in db
        em.detach(updatedLocationType);
        updatedLocationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocationType))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationTypeToMatchAllProperties(updatedLocationType);
    }

    @Test
    @Transactional
    void putNonExistingLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationType using partial update
        LocationType partialUpdatedLocationType = new LocationType();
        partialUpdatedLocationType.setId(locationType.getId());

        partialUpdatedLocationType.name(UPDATED_NAME);

        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocationType))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocationType, locationType),
            getPersistedLocationType(locationType)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationType using partial update
        LocationType partialUpdatedLocationType = new LocationType();
        partialUpdatedLocationType.setId(locationType.getId());

        partialUpdatedLocationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocationType))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationTypeUpdatableFieldsEquals(partialUpdatedLocationType, getPersistedLocationType(partialUpdatedLocationType));
    }

    @Test
    @Transactional
    void patchNonExistingLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(locationType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationType() throws Exception {
        // Initialize the database
        insertedLocationType = locationTypeRepository.saveAndFlush(locationType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the locationType
        restLocationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationTypeRepository.count();
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

    protected LocationType getPersistedLocationType(LocationType locationType) {
        return locationTypeRepository.findById(locationType.getId()).orElseThrow();
    }

    protected void assertPersistedLocationTypeToMatchAllProperties(LocationType expectedLocationType) {
        assertLocationTypeAllPropertiesEquals(expectedLocationType, getPersistedLocationType(expectedLocationType));
    }

    protected void assertPersistedLocationTypeToMatchUpdatableProperties(LocationType expectedLocationType) {
        assertLocationTypeAllUpdatablePropertiesEquals(expectedLocationType, getPersistedLocationType(expectedLocationType));
    }
}
