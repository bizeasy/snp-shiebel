package com.snp.web.rest;

import static com.snp.domain.ReturnHeaderTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ReturnHeaderType;
import com.snp.repository.ReturnHeaderTypeRepository;
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
 * Integration tests for the {@link ReturnHeaderTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReturnHeaderTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/return-header-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReturnHeaderTypeRepository returnHeaderTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnHeaderTypeMockMvc;

    private ReturnHeaderType returnHeaderType;

    private ReturnHeaderType insertedReturnHeaderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnHeaderType createEntity() {
        return new ReturnHeaderType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnHeaderType createUpdatedEntity() {
        return new ReturnHeaderType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        returnHeaderType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReturnHeaderType != null) {
            returnHeaderTypeRepository.delete(insertedReturnHeaderType);
            insertedReturnHeaderType = null;
        }
    }

    @Test
    @Transactional
    void createReturnHeaderType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReturnHeaderType
        var returnedReturnHeaderType = om.readValue(
            restReturnHeaderTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeaderType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReturnHeaderType.class
        );

        // Validate the ReturnHeaderType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReturnHeaderTypeUpdatableFieldsEquals(returnedReturnHeaderType, getPersistedReturnHeaderType(returnedReturnHeaderType));

        insertedReturnHeaderType = returnedReturnHeaderType;
    }

    @Test
    @Transactional
    void createReturnHeaderTypeWithExistingId() throws Exception {
        // Create the ReturnHeaderType with an existing ID
        returnHeaderType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnHeaderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeaderType)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        returnHeaderType.setName(null);

        // Create the ReturnHeaderType, which fails.

        restReturnHeaderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeaderType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypes() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnHeaderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getReturnHeaderType() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get the returnHeaderType
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, returnHeaderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnHeaderType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getReturnHeaderTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        Long id = returnHeaderType.getId();

        defaultReturnHeaderTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReturnHeaderTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReturnHeaderTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where name equals to
        defaultReturnHeaderTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where name in
        defaultReturnHeaderTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where name is not null
        defaultReturnHeaderTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where name contains
        defaultReturnHeaderTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where name does not contain
        defaultReturnHeaderTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where description equals to
        defaultReturnHeaderTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where description in
        defaultReturnHeaderTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where description is not null
        defaultReturnHeaderTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where description contains
        defaultReturnHeaderTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReturnHeaderTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        // Get all the returnHeaderTypeList where description does not contain
        defaultReturnHeaderTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultReturnHeaderTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReturnHeaderTypeShouldBeFound(shouldBeFound);
        defaultReturnHeaderTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReturnHeaderTypeShouldBeFound(String filter) throws Exception {
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnHeaderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReturnHeaderTypeShouldNotBeFound(String filter) throws Exception {
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnHeaderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReturnHeaderType() throws Exception {
        // Get the returnHeaderType
        restReturnHeaderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReturnHeaderType() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeaderType
        ReturnHeaderType updatedReturnHeaderType = returnHeaderTypeRepository.findById(returnHeaderType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReturnHeaderType are not directly saved in db
        em.detach(updatedReturnHeaderType);
        updatedReturnHeaderType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restReturnHeaderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnHeaderType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReturnHeaderType))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReturnHeaderTypeToMatchAllProperties(updatedReturnHeaderType);
    }

    @Test
    @Transactional
    void putNonExistingReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnHeaderType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnHeaderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnHeaderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeaderType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnHeaderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeaderType using partial update
        ReturnHeaderType partialUpdatedReturnHeaderType = new ReturnHeaderType();
        partialUpdatedReturnHeaderType.setId(returnHeaderType.getId());

        partialUpdatedReturnHeaderType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restReturnHeaderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnHeaderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnHeaderType))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeaderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnHeaderTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReturnHeaderType, returnHeaderType),
            getPersistedReturnHeaderType(returnHeaderType)
        );
    }

    @Test
    @Transactional
    void fullUpdateReturnHeaderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeaderType using partial update
        ReturnHeaderType partialUpdatedReturnHeaderType = new ReturnHeaderType();
        partialUpdatedReturnHeaderType.setId(returnHeaderType.getId());

        partialUpdatedReturnHeaderType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restReturnHeaderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnHeaderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnHeaderType))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeaderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnHeaderTypeUpdatableFieldsEquals(
            partialUpdatedReturnHeaderType,
            getPersistedReturnHeaderType(partialUpdatedReturnHeaderType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnHeaderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnHeaderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnHeaderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnHeaderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeaderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(returnHeaderType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnHeaderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnHeaderType() throws Exception {
        // Initialize the database
        insertedReturnHeaderType = returnHeaderTypeRepository.saveAndFlush(returnHeaderType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the returnHeaderType
        restReturnHeaderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnHeaderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return returnHeaderTypeRepository.count();
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

    protected ReturnHeaderType getPersistedReturnHeaderType(ReturnHeaderType returnHeaderType) {
        return returnHeaderTypeRepository.findById(returnHeaderType.getId()).orElseThrow();
    }

    protected void assertPersistedReturnHeaderTypeToMatchAllProperties(ReturnHeaderType expectedReturnHeaderType) {
        assertReturnHeaderTypeAllPropertiesEquals(expectedReturnHeaderType, getPersistedReturnHeaderType(expectedReturnHeaderType));
    }

    protected void assertPersistedReturnHeaderTypeToMatchUpdatableProperties(ReturnHeaderType expectedReturnHeaderType) {
        assertReturnHeaderTypeAllUpdatablePropertiesEquals(
            expectedReturnHeaderType,
            getPersistedReturnHeaderType(expectedReturnHeaderType)
        );
    }
}
