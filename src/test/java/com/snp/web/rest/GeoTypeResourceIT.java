package com.snp.web.rest;

import static com.snp.domain.GeoTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.GeoType;
import com.snp.repository.GeoTypeRepository;
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
 * Integration tests for the {@link GeoTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeoTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/geo-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoTypeRepository geoTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoTypeMockMvc;

    private GeoType geoType;

    private GeoType insertedGeoType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoType createEntity() {
        return new GeoType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoType createUpdatedEntity() {
        return new GeoType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        geoType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGeoType != null) {
            geoTypeRepository.delete(insertedGeoType);
            insertedGeoType = null;
        }
    }

    @Test
    @Transactional
    void createGeoType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GeoType
        var returnedGeoType = om.readValue(
            restGeoTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GeoType.class
        );

        // Validate the GeoType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGeoTypeUpdatableFieldsEquals(returnedGeoType, getPersistedGeoType(returnedGeoType));

        insertedGeoType = returnedGeoType;
    }

    @Test
    @Transactional
    void createGeoTypeWithExistingId() throws Exception {
        // Create the GeoType with an existing ID
        geoType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoType)))
            .andExpect(status().isBadRequest());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geoType.setName(null);

        // Create the GeoType, which fails.

        restGeoTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeoTypes() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getGeoType() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get the geoType
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, geoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geoType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getGeoTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        Long id = geoType.getId();

        defaultGeoTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGeoTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGeoTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeoTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList where name equals to
        defaultGeoTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList where name in
        defaultGeoTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList where name is not null
        defaultGeoTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGeoTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList where name contains
        defaultGeoTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        // Get all the geoTypeList where name does not contain
        defaultGeoTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultGeoTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGeoTypeShouldBeFound(shouldBeFound);
        defaultGeoTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoTypeShouldBeFound(String filter) throws Exception {
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeoTypeShouldNotBeFound(String filter) throws Exception {
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeoTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeoType() throws Exception {
        // Get the geoType
        restGeoTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeoType() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoType
        GeoType updatedGeoType = geoTypeRepository.findById(geoType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeoType are not directly saved in db
        em.detach(updatedGeoType);
        updatedGeoType.name(UPDATED_NAME);

        restGeoTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeoType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGeoType))
            )
            .andExpect(status().isOk());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoTypeToMatchAllProperties(updatedGeoType);
    }

    @Test
    @Transactional
    void putNonExistingGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(put(ENTITY_API_URL_ID, geoType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoType)))
            .andExpect(status().isBadRequest());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoTypeWithPatch() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoType using partial update
        GeoType partialUpdatedGeoType = new GeoType();
        partialUpdatedGeoType.setId(geoType.getId());

        restGeoTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoType))
            )
            .andExpect(status().isOk());

        // Validate the GeoType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGeoType, geoType), getPersistedGeoType(geoType));
    }

    @Test
    @Transactional
    void fullUpdateGeoTypeWithPatch() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoType using partial update
        GeoType partialUpdatedGeoType = new GeoType();
        partialUpdatedGeoType.setId(geoType.getId());

        partialUpdatedGeoType.name(UPDATED_NAME);

        restGeoTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoType))
            )
            .andExpect(status().isOk());

        // Validate the GeoType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoTypeUpdatableFieldsEquals(partialUpdatedGeoType, getPersistedGeoType(partialUpdatedGeoType));
    }

    @Test
    @Transactional
    void patchNonExistingGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, geoType.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeoType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeoType() throws Exception {
        // Initialize the database
        insertedGeoType = geoTypeRepository.saveAndFlush(geoType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geoType
        restGeoTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, geoType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoTypeRepository.count();
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

    protected GeoType getPersistedGeoType(GeoType geoType) {
        return geoTypeRepository.findById(geoType.getId()).orElseThrow();
    }

    protected void assertPersistedGeoTypeToMatchAllProperties(GeoType expectedGeoType) {
        assertGeoTypeAllPropertiesEquals(expectedGeoType, getPersistedGeoType(expectedGeoType));
    }

    protected void assertPersistedGeoTypeToMatchUpdatableProperties(GeoType expectedGeoType) {
        assertGeoTypeAllUpdatablePropertiesEquals(expectedGeoType, getPersistedGeoType(expectedGeoType));
    }
}
