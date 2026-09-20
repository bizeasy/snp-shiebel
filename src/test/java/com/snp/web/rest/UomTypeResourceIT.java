package com.snp.web.rest;

import static com.snp.domain.UomTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.UomType;
import com.snp.repository.UomTypeRepository;
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
 * Integration tests for the {@link UomTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UomTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/uom-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UomTypeRepository uomTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUomTypeMockMvc;

    private UomType uomType;

    private UomType insertedUomType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UomType createEntity() {
        return new UomType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UomType createUpdatedEntity() {
        return new UomType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        uomType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUomType != null) {
            uomTypeRepository.delete(insertedUomType);
            insertedUomType = null;
        }
    }

    @Test
    @Transactional
    void createUomType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UomType
        var returnedUomType = om.readValue(
            restUomTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UomType.class
        );

        // Validate the UomType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUomTypeUpdatableFieldsEquals(returnedUomType, getPersistedUomType(returnedUomType));

        insertedUomType = returnedUomType;
    }

    @Test
    @Transactional
    void createUomTypeWithExistingId() throws Exception {
        // Create the UomType with an existing ID
        uomType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomType)))
            .andExpect(status().isBadRequest());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uomType.setName(null);

        // Create the UomType, which fails.

        restUomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUomTypes() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uomType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUomType() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get the uomType
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, uomType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uomType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getUomTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        Long id = uomType.getId();

        defaultUomTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUomTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUomTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUomTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where name equals to
        defaultUomTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where name in
        defaultUomTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where name is not null
        defaultUomTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUomTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where name contains
        defaultUomTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where name does not contain
        defaultUomTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUomTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where description equals to
        defaultUomTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where description in
        defaultUomTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllUomTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where description is not null
        defaultUomTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllUomTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where description contains
        defaultUomTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        // Get all the uomTypeList where description does not contain
        defaultUomTypeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultUomTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUomTypeShouldBeFound(shouldBeFound);
        defaultUomTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUomTypeShouldBeFound(String filter) throws Exception {
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uomType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUomTypeShouldNotBeFound(String filter) throws Exception {
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUomTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUomType() throws Exception {
        // Get the uomType
        restUomTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUomType() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomType
        UomType updatedUomType = uomTypeRepository.findById(uomType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUomType are not directly saved in db
        em.detach(updatedUomType);
        updatedUomType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUomType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUomType))
            )
            .andExpect(status().isOk());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUomTypeToMatchAllProperties(updatedUomType);
    }

    @Test
    @Transactional
    void putNonExistingUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(put(ENTITY_API_URL_ID, uomType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomType)))
            .andExpect(status().isBadRequest());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUomTypeWithPatch() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomType using partial update
        UomType partialUpdatedUomType = new UomType();
        partialUpdatedUomType.setId(uomType.getId());

        partialUpdatedUomType.description(UPDATED_DESCRIPTION);

        restUomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUomType))
            )
            .andExpect(status().isOk());

        // Validate the UomType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUomType, uomType), getPersistedUomType(uomType));
    }

    @Test
    @Transactional
    void fullUpdateUomTypeWithPatch() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomType using partial update
        UomType partialUpdatedUomType = new UomType();
        partialUpdatedUomType.setId(uomType.getId());

        partialUpdatedUomType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUomType))
            )
            .andExpect(status().isOk());

        // Validate the UomType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomTypeUpdatableFieldsEquals(partialUpdatedUomType, getPersistedUomType(partialUpdatedUomType));
    }

    @Test
    @Transactional
    void patchNonExistingUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uomType.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUomType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uomType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UomType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUomType() throws Exception {
        // Initialize the database
        insertedUomType = uomTypeRepository.saveAndFlush(uomType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uomType
        restUomTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, uomType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uomTypeRepository.count();
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

    protected UomType getPersistedUomType(UomType uomType) {
        return uomTypeRepository.findById(uomType.getId()).orElseThrow();
    }

    protected void assertPersistedUomTypeToMatchAllProperties(UomType expectedUomType) {
        assertUomTypeAllPropertiesEquals(expectedUomType, getPersistedUomType(expectedUomType));
    }

    protected void assertPersistedUomTypeToMatchUpdatableProperties(UomType expectedUomType) {
        assertUomTypeAllUpdatablePropertiesEquals(expectedUomType, getPersistedUomType(expectedUomType));
    }
}
