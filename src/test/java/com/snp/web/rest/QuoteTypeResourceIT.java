package com.snp.web.rest;

import static com.snp.domain.QuoteTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.QuoteType;
import com.snp.repository.QuoteTypeRepository;
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
 * Integration tests for the {@link QuoteTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quote-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteTypeRepository quoteTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteTypeMockMvc;

    private QuoteType quoteType;

    private QuoteType insertedQuoteType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteType createEntity() {
        return new QuoteType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteType createUpdatedEntity() {
        return new QuoteType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        quoteType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteType != null) {
            quoteTypeRepository.delete(insertedQuoteType);
            insertedQuoteType = null;
        }
    }

    @Test
    @Transactional
    void createQuoteType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteType
        var returnedQuoteType = om.readValue(
            restQuoteTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteType.class
        );

        // Validate the QuoteType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteTypeUpdatableFieldsEquals(returnedQuoteType, getPersistedQuoteType(returnedQuoteType));

        insertedQuoteType = returnedQuoteType;
    }

    @Test
    @Transactional
    void createQuoteTypeWithExistingId() throws Exception {
        // Create the QuoteType with an existing ID
        quoteType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteType)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quoteType.setName(null);

        // Create the QuoteType, which fails.

        restQuoteTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuoteTypes() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getQuoteType() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get the quoteType
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getQuoteTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        Long id = quoteType.getId();

        defaultQuoteTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where name equals to
        defaultQuoteTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where name in
        defaultQuoteTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where name is not null
        defaultQuoteTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where name contains
        defaultQuoteTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where name does not contain
        defaultQuoteTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where description equals to
        defaultQuoteTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where description in
        defaultQuoteTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuoteTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where description is not null
        defaultQuoteTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where description contains
        defaultQuoteTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuoteTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        // Get all the quoteTypeList where description does not contain
        defaultQuoteTypeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultQuoteTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteTypeShouldBeFound(shouldBeFound);
        defaultQuoteTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteTypeShouldBeFound(String filter) throws Exception {
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteTypeShouldNotBeFound(String filter) throws Exception {
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuoteType() throws Exception {
        // Get the quoteType
        restQuoteTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteType() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteType
        QuoteType updatedQuoteType = quoteTypeRepository.findById(quoteType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteType are not directly saved in db
        em.detach(updatedQuoteType);
        updatedQuoteType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restQuoteTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteType))
            )
            .andExpect(status().isOk());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteTypeToMatchAllProperties(updatedQuoteType);
    }

    @Test
    @Transactional
    void putNonExistingQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteType using partial update
        QuoteType partialUpdatedQuoteType = new QuoteType();
        partialUpdatedQuoteType.setId(quoteType.getId());

        partialUpdatedQuoteType.name(UPDATED_NAME);

        restQuoteTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteType))
            )
            .andExpect(status().isOk());

        // Validate the QuoteType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteType, quoteType),
            getPersistedQuoteType(quoteType)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteTypeWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteType using partial update
        QuoteType partialUpdatedQuoteType = new QuoteType();
        partialUpdatedQuoteType.setId(quoteType.getId());

        partialUpdatedQuoteType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restQuoteTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteType))
            )
            .andExpect(status().isOk());

        // Validate the QuoteType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteTypeUpdatableFieldsEquals(partialUpdatedQuoteType, getPersistedQuoteType(partialUpdatedQuoteType));
    }

    @Test
    @Transactional
    void patchNonExistingQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteType() throws Exception {
        // Initialize the database
        insertedQuoteType = quoteTypeRepository.saveAndFlush(quoteType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteType
        restQuoteTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteTypeRepository.count();
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

    protected QuoteType getPersistedQuoteType(QuoteType quoteType) {
        return quoteTypeRepository.findById(quoteType.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteTypeToMatchAllProperties(QuoteType expectedQuoteType) {
        assertQuoteTypeAllPropertiesEquals(expectedQuoteType, getPersistedQuoteType(expectedQuoteType));
    }

    protected void assertPersistedQuoteTypeToMatchUpdatableProperties(QuoteType expectedQuoteType) {
        assertQuoteTypeAllUpdatablePropertiesEquals(expectedQuoteType, getPersistedQuoteType(expectedQuoteType));
    }
}
