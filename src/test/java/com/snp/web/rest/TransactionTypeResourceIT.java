package com.snp.web.rest;

import static com.snp.domain.TransactionTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.TransactionCategory;
import com.snp.domain.TransactionType;
import com.snp.repository.TransactionTypeRepository;
import com.snp.service.TransactionTypeService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Mock
    private TransactionTypeRepository transactionTypeRepositoryMock;

    @Mock
    private TransactionTypeService transactionTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    private TransactionType insertedTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity() {
        return new TransactionType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity() {
        return new TransactionType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        transactionType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransactionType != null) {
            transactionTypeRepository.delete(insertedTransactionType);
            insertedTransactionType = null;
        }
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionType
        var returnedTransactionType = om.readValue(
            restTransactionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionType.class
        );

        // Validate the TransactionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionTypeUpdatableFieldsEquals(returnedTransactionType, getPersistedTransactionType(returnedTransactionType));

        insertedTransactionType = returnedTransactionType;
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionType.setName(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionTypeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(transactionTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        Long id = transactionType.getId();

        defaultTransactionTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransactionTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransactionTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name equals to
        defaultTransactionTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name in
        defaultTransactionTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name is not null
        defaultTransactionTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name contains
        defaultTransactionTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name does not contain
        defaultTransactionTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description equals to
        defaultTransactionTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description in
        defaultTransactionTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description is not null
        defaultTransactionTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description contains
        defaultTransactionTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where description does not contain
        defaultTransactionTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCategoryIsEqualToSomething() throws Exception {
        TransactionCategory category;
        if (TestUtil.findAll(em, TransactionCategory.class).isEmpty()) {
            transactionTypeRepository.saveAndFlush(transactionType);
            category = TransactionCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, TransactionCategory.class).getFirst();
        }
        em.persist(category);
        em.flush();
        transactionType.setCategory(category);
        transactionTypeRepository.saveAndFlush(transactionType);
        Long categoryId = category.getId();
        // Get all the transactionTypeList where category equals to categoryId
        defaultTransactionTypeShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the transactionTypeList where category equals to (categoryId + 1)
        defaultTransactionTypeShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultTransactionTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransactionTypeShouldBeFound(shouldBeFound);
        defaultTransactionTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionTypeShouldBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionTypeToMatchAllProperties(updatedTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.description(UPDATED_DESCRIPTION);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionType, transactionType),
            getPersistedTransactionType(transactionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            partialUpdatedTransactionType,
            getPersistedTransactionType(partialUpdatedTransactionType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionTypeRepository.count();
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

    protected TransactionType getPersistedTransactionType(TransactionType transactionType) {
        return transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionTypeToMatchAllProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllPropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }

    protected void assertPersistedTransactionTypeToMatchUpdatableProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllUpdatablePropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }
}
