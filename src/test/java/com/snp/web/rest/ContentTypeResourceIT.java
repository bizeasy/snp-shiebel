package com.snp.web.rest;

import static com.snp.domain.ContentTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContentType;
import com.snp.repository.ContentTypeRepository;
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
 * Integration tests for the {@link ContentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/content-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentTypeMockMvc;

    private ContentType contentType;

    private ContentType insertedContentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createEntity() {
        return new ContentType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentType createUpdatedEntity() {
        return new ContentType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        contentType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContentType != null) {
            contentTypeRepository.delete(insertedContentType);
            insertedContentType = null;
        }
    }

    @Test
    @Transactional
    void createContentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContentType
        var returnedContentType = om.readValue(
            restContentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContentType.class
        );

        // Validate the ContentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContentTypeUpdatableFieldsEquals(returnedContentType, getPersistedContentType(returnedContentType));

        insertedContentType = returnedContentType;
    }

    @Test
    @Transactional
    void createContentTypeWithExistingId() throws Exception {
        // Create the ContentType with an existing ID
        contentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentType)))
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contentType.setName(null);

        // Create the ContentType, which fails.

        restContentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentTypes() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getContentType() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get the contentType
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, contentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getContentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        Long id = contentType.getId();

        defaultContentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList where name equals to
        defaultContentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList where name in
        defaultContentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList where name is not null
        defaultContentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllContentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList where name contains
        defaultContentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        // Get all the contentTypeList where name does not contain
        defaultContentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultContentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContentTypeShouldBeFound(shouldBeFound);
        defaultContentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContentTypeShouldBeFound(String filter) throws Exception {
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContentTypeShouldNotBeFound(String filter) throws Exception {
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContentType() throws Exception {
        // Get the contentType
        restContentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentType() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentType
        ContentType updatedContentType = contentTypeRepository.findById(contentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContentType are not directly saved in db
        em.detach(updatedContentType);
        updatedContentType.name(UPDATED_NAME);

        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContentType))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContentTypeToMatchAllProperties(updatedContentType);
    }

    @Test
    @Transactional
    void putNonExistingContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentType using partial update
        ContentType partialUpdatedContentType = new ContentType();
        partialUpdatedContentType.setId(contentType.getId());

        partialUpdatedContentType.name(UPDATED_NAME);

        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContentType))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContentType, contentType),
            getPersistedContentType(contentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateContentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentType using partial update
        ContentType partialUpdatedContentType = new ContentType();
        partialUpdatedContentType.setId(contentType.getId());

        partialUpdatedContentType.name(UPDATED_NAME);

        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContentType))
            )
            .andExpect(status().isOk());

        // Validate the ContentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentTypeUpdatableFieldsEquals(partialUpdatedContentType, getPersistedContentType(partialUpdatedContentType));
    }

    @Test
    @Transactional
    void patchNonExistingContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentType() throws Exception {
        // Initialize the database
        insertedContentType = contentTypeRepository.saveAndFlush(contentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contentType
        restContentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contentTypeRepository.count();
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

    protected ContentType getPersistedContentType(ContentType contentType) {
        return contentTypeRepository.findById(contentType.getId()).orElseThrow();
    }

    protected void assertPersistedContentTypeToMatchAllProperties(ContentType expectedContentType) {
        assertContentTypeAllPropertiesEquals(expectedContentType, getPersistedContentType(expectedContentType));
    }

    protected void assertPersistedContentTypeToMatchUpdatableProperties(ContentType expectedContentType) {
        assertContentTypeAllUpdatablePropertiesEquals(expectedContentType, getPersistedContentType(expectedContentType));
    }
}
