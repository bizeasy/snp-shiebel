package com.snp.web.rest;

import static com.snp.domain.GeoAssocTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.GeoAssocType;
import com.snp.repository.GeoAssocTypeRepository;
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
 * Integration tests for the {@link GeoAssocTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeoAssocTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/geo-assoc-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoAssocTypeRepository geoAssocTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoAssocTypeMockMvc;

    private GeoAssocType geoAssocType;

    private GeoAssocType insertedGeoAssocType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoAssocType createEntity() {
        return new GeoAssocType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoAssocType createUpdatedEntity() {
        return new GeoAssocType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        geoAssocType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGeoAssocType != null) {
            geoAssocTypeRepository.delete(insertedGeoAssocType);
            insertedGeoAssocType = null;
        }
    }

    @Test
    @Transactional
    void createGeoAssocType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GeoAssocType
        var returnedGeoAssocType = om.readValue(
            restGeoAssocTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssocType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GeoAssocType.class
        );

        // Validate the GeoAssocType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGeoAssocTypeUpdatableFieldsEquals(returnedGeoAssocType, getPersistedGeoAssocType(returnedGeoAssocType));

        insertedGeoAssocType = returnedGeoAssocType;
    }

    @Test
    @Transactional
    void createGeoAssocTypeWithExistingId() throws Exception {
        // Create the GeoAssocType with an existing ID
        geoAssocType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoAssocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssocType)))
            .andExpect(status().isBadRequest());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geoAssocType.setName(null);

        // Create the GeoAssocType, which fails.

        restGeoAssocTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssocType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeoAssocTypes() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoAssocType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getGeoAssocType() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get the geoAssocType
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, geoAssocType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geoAssocType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getGeoAssocTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        Long id = geoAssocType.getId();

        defaultGeoAssocTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGeoAssocTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGeoAssocTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeoAssocTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList where name equals to
        defaultGeoAssocTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoAssocTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList where name in
        defaultGeoAssocTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoAssocTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList where name is not null
        defaultGeoAssocTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGeoAssocTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList where name contains
        defaultGeoAssocTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeoAssocTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        // Get all the geoAssocTypeList where name does not contain
        defaultGeoAssocTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultGeoAssocTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGeoAssocTypeShouldBeFound(shouldBeFound);
        defaultGeoAssocTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoAssocTypeShouldBeFound(String filter) throws Exception {
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoAssocType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeoAssocTypeShouldNotBeFound(String filter) throws Exception {
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeoAssocTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeoAssocType() throws Exception {
        // Get the geoAssocType
        restGeoAssocTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeoAssocType() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssocType
        GeoAssocType updatedGeoAssocType = geoAssocTypeRepository.findById(geoAssocType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeoAssocType are not directly saved in db
        em.detach(updatedGeoAssocType);
        updatedGeoAssocType.name(UPDATED_NAME);

        restGeoAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeoAssocType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGeoAssocType))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoAssocTypeToMatchAllProperties(updatedGeoAssocType);
    }

    @Test
    @Transactional
    void putNonExistingGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, geoAssocType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssocType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoAssocTypeWithPatch() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssocType using partial update
        GeoAssocType partialUpdatedGeoAssocType = new GeoAssocType();
        partialUpdatedGeoAssocType.setId(geoAssocType.getId());

        partialUpdatedGeoAssocType.name(UPDATED_NAME);

        restGeoAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoAssocType))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssocType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoAssocTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGeoAssocType, geoAssocType),
            getPersistedGeoAssocType(geoAssocType)
        );
    }

    @Test
    @Transactional
    void fullUpdateGeoAssocTypeWithPatch() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssocType using partial update
        GeoAssocType partialUpdatedGeoAssocType = new GeoAssocType();
        partialUpdatedGeoAssocType.setId(geoAssocType.getId());

        partialUpdatedGeoAssocType.name(UPDATED_NAME);

        restGeoAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoAssocType))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssocType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoAssocTypeUpdatableFieldsEquals(partialUpdatedGeoAssocType, getPersistedGeoAssocType(partialUpdatedGeoAssocType));
    }

    @Test
    @Transactional
    void patchNonExistingGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, geoAssocType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoAssocType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeoAssocType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssocType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoAssocType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoAssocType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeoAssocType() throws Exception {
        // Initialize the database
        insertedGeoAssocType = geoAssocTypeRepository.saveAndFlush(geoAssocType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geoAssocType
        restGeoAssocTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, geoAssocType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoAssocTypeRepository.count();
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

    protected GeoAssocType getPersistedGeoAssocType(GeoAssocType geoAssocType) {
        return geoAssocTypeRepository.findById(geoAssocType.getId()).orElseThrow();
    }

    protected void assertPersistedGeoAssocTypeToMatchAllProperties(GeoAssocType expectedGeoAssocType) {
        assertGeoAssocTypeAllPropertiesEquals(expectedGeoAssocType, getPersistedGeoAssocType(expectedGeoAssocType));
    }

    protected void assertPersistedGeoAssocTypeToMatchUpdatableProperties(GeoAssocType expectedGeoAssocType) {
        assertGeoAssocTypeAllUpdatablePropertiesEquals(expectedGeoAssocType, getPersistedGeoAssocType(expectedGeoAssocType));
    }
}
