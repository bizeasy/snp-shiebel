package com.snp.web.rest;

import static com.snp.domain.TransactionCategoryAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.TransactionCategory;
import com.snp.repository.TransactionCategoryRepository;
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
 * Integration tests for the {@link TransactionCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionCategoryMockMvc;

    private TransactionCategory transactionCategory;

    private TransactionCategory insertedTransactionCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionCategory createEntity() {
        return new TransactionCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionCategory createUpdatedEntity() {
        return new TransactionCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        transactionCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransactionCategory != null) {
            transactionCategoryRepository.delete(insertedTransactionCategory);
            insertedTransactionCategory = null;
        }
    }

    @Test
    @Transactional
    void createTransactionCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionCategory
        var returnedTransactionCategory = om.readValue(
            restTransactionCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionCategory.class
        );

        // Validate the TransactionCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionCategoryUpdatableFieldsEquals(
            returnedTransactionCategory,
            getPersistedTransactionCategory(returnedTransactionCategory)
        );

        insertedTransactionCategory = returnedTransactionCategory;
    }

    @Test
    @Transactional
    void createTransactionCategoryWithExistingId() throws Exception {
        // Create the TransactionCategory with an existing ID
        transactionCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionCategory)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionCategory.setName(null);

        // Create the TransactionCategory, which fails.

        restTransactionCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionCategories() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTransactionCategory() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get the transactionCategory
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTransactionCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        Long id = transactionCategory.getId();

        defaultTransactionCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransactionCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransactionCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where name equals to
        defaultTransactionCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where name in
        defaultTransactionCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where name is not null
        defaultTransactionCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where name contains
        defaultTransactionCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where name does not contain
        defaultTransactionCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where description equals to
        defaultTransactionCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where description in
        defaultTransactionCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where description is not null
        defaultTransactionCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where description contains
        defaultTransactionCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        // Get all the transactionCategoryList where description does not contain
        defaultTransactionCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultTransactionCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransactionCategoryShouldBeFound(shouldBeFound);
        defaultTransactionCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionCategoryShouldBeFound(String filter) throws Exception {
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionCategoryShouldNotBeFound(String filter) throws Exception {
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionCategory() throws Exception {
        // Get the transactionCategory
        restTransactionCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionCategory() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionCategory
        TransactionCategory updatedTransactionCategory = transactionCategoryRepository.findById(transactionCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionCategory are not directly saved in db
        em.detach(updatedTransactionCategory);
        updatedTransactionCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTransactionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransactionCategory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionCategoryToMatchAllProperties(updatedTransactionCategory);
    }

    @Test
    @Transactional
    void putNonExistingTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionCategory using partial update
        TransactionCategory partialUpdatedTransactionCategory = new TransactionCategory();
        partialUpdatedTransactionCategory.setId(transactionCategory.getId());

        partialUpdatedTransactionCategory.name(UPDATED_NAME);

        restTransactionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionCategory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionCategory, transactionCategory),
            getPersistedTransactionCategory(transactionCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionCategory using partial update
        TransactionCategory partialUpdatedTransactionCategory = new TransactionCategory();
        partialUpdatedTransactionCategory.setId(transactionCategory.getId());

        partialUpdatedTransactionCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTransactionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionCategory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionCategoryUpdatableFieldsEquals(
            partialUpdatedTransactionCategory,
            getPersistedTransactionCategory(partialUpdatedTransactionCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionCategory() throws Exception {
        // Initialize the database
        insertedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionCategory
        restTransactionCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionCategoryRepository.count();
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

    protected TransactionCategory getPersistedTransactionCategory(TransactionCategory transactionCategory) {
        return transactionCategoryRepository.findById(transactionCategory.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionCategoryToMatchAllProperties(TransactionCategory expectedTransactionCategory) {
        assertTransactionCategoryAllPropertiesEquals(
            expectedTransactionCategory,
            getPersistedTransactionCategory(expectedTransactionCategory)
        );
    }

    protected void assertPersistedTransactionCategoryToMatchUpdatableProperties(TransactionCategory expectedTransactionCategory) {
        assertTransactionCategoryAllUpdatablePropertiesEquals(
            expectedTransactionCategory,
            getPersistedTransactionCategory(expectedTransactionCategory)
        );
    }
}
