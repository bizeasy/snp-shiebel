package com.snp.web.rest;

import static com.snp.domain.KeywordTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.KeywordType;
import com.snp.repository.KeywordTypeRepository;
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
 * Integration tests for the {@link KeywordTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KeywordTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/keyword-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KeywordTypeRepository keywordTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeywordTypeMockMvc;

    private KeywordType keywordType;

    private KeywordType insertedKeywordType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeywordType createEntity() {
        return new KeywordType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeywordType createUpdatedEntity() {
        return new KeywordType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        keywordType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKeywordType != null) {
            keywordTypeRepository.delete(insertedKeywordType);
            insertedKeywordType = null;
        }
    }

    @Test
    @Transactional
    void createKeywordType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KeywordType
        var returnedKeywordType = om.readValue(
            restKeywordTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keywordType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KeywordType.class
        );

        // Validate the KeywordType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKeywordTypeUpdatableFieldsEquals(returnedKeywordType, getPersistedKeywordType(returnedKeywordType));

        insertedKeywordType = returnedKeywordType;
    }

    @Test
    @Transactional
    void createKeywordTypeWithExistingId() throws Exception {
        // Create the KeywordType with an existing ID
        keywordType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeywordTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keywordType)))
            .andExpect(status().isBadRequest());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keywordType.setName(null);

        // Create the KeywordType, which fails.

        restKeywordTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keywordType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKeywordTypes() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keywordType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getKeywordType() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get the keywordType
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, keywordType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keywordType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getKeywordTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        Long id = keywordType.getId();

        defaultKeywordTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultKeywordTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultKeywordTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKeywordTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList where name equals to
        defaultKeywordTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKeywordTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList where name in
        defaultKeywordTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKeywordTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList where name is not null
        defaultKeywordTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllKeywordTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList where name contains
        defaultKeywordTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKeywordTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        // Get all the keywordTypeList where name does not contain
        defaultKeywordTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultKeywordTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultKeywordTypeShouldBeFound(shouldBeFound);
        defaultKeywordTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKeywordTypeShouldBeFound(String filter) throws Exception {
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keywordType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKeywordTypeShouldNotBeFound(String filter) throws Exception {
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKeywordTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKeywordType() throws Exception {
        // Get the keywordType
        restKeywordTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKeywordType() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keywordType
        KeywordType updatedKeywordType = keywordTypeRepository.findById(keywordType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKeywordType are not directly saved in db
        em.detach(updatedKeywordType);
        updatedKeywordType.name(UPDATED_NAME);

        restKeywordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKeywordType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKeywordType))
            )
            .andExpect(status().isOk());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKeywordTypeToMatchAllProperties(updatedKeywordType);
    }

    @Test
    @Transactional
    void putNonExistingKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keywordType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keywordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keywordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keywordType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKeywordTypeWithPatch() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keywordType using partial update
        KeywordType partialUpdatedKeywordType = new KeywordType();
        partialUpdatedKeywordType.setId(keywordType.getId());

        partialUpdatedKeywordType.name(UPDATED_NAME);

        restKeywordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeywordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeywordType))
            )
            .andExpect(status().isOk());

        // Validate the KeywordType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeywordTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKeywordType, keywordType),
            getPersistedKeywordType(keywordType)
        );
    }

    @Test
    @Transactional
    void fullUpdateKeywordTypeWithPatch() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keywordType using partial update
        KeywordType partialUpdatedKeywordType = new KeywordType();
        partialUpdatedKeywordType.setId(keywordType.getId());

        partialUpdatedKeywordType.name(UPDATED_NAME);

        restKeywordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeywordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeywordType))
            )
            .andExpect(status().isOk());

        // Validate the KeywordType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeywordTypeUpdatableFieldsEquals(partialUpdatedKeywordType, getPersistedKeywordType(partialUpdatedKeywordType));
    }

    @Test
    @Transactional
    void patchNonExistingKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, keywordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keywordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keywordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKeywordType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keywordType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeywordTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(keywordType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeywordType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKeywordType() throws Exception {
        // Initialize the database
        insertedKeywordType = keywordTypeRepository.saveAndFlush(keywordType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the keywordType
        restKeywordTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, keywordType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return keywordTypeRepository.count();
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

    protected KeywordType getPersistedKeywordType(KeywordType keywordType) {
        return keywordTypeRepository.findById(keywordType.getId()).orElseThrow();
    }

    protected void assertPersistedKeywordTypeToMatchAllProperties(KeywordType expectedKeywordType) {
        assertKeywordTypeAllPropertiesEquals(expectedKeywordType, getPersistedKeywordType(expectedKeywordType));
    }

    protected void assertPersistedKeywordTypeToMatchUpdatableProperties(KeywordType expectedKeywordType) {
        assertKeywordTypeAllUpdatablePropertiesEquals(expectedKeywordType, getPersistedKeywordType(expectedKeywordType));
    }
}
