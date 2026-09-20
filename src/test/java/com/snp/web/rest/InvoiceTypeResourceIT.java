package com.snp.web.rest;

import static com.snp.domain.InvoiceTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InvoiceType;
import com.snp.repository.InvoiceTypeRepository;
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
 * Integration tests for the {@link InvoiceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceTypeRepository invoiceTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceTypeMockMvc;

    private InvoiceType invoiceType;

    private InvoiceType insertedInvoiceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceType createEntity() {
        return new InvoiceType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceType createUpdatedEntity() {
        return new InvoiceType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        invoiceType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceType != null) {
            invoiceTypeRepository.delete(insertedInvoiceType);
            insertedInvoiceType = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceType
        var returnedInvoiceType = om.readValue(
            restInvoiceTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceType.class
        );

        // Validate the InvoiceType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceTypeUpdatableFieldsEquals(returnedInvoiceType, getPersistedInvoiceType(returnedInvoiceType));

        insertedInvoiceType = returnedInvoiceType;
    }

    @Test
    @Transactional
    void createInvoiceTypeWithExistingId() throws Exception {
        // Create the InvoiceType with an existing ID
        invoiceType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceType)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceType.setName(null);

        // Create the InvoiceType, which fails.

        restInvoiceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceTypes() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getInvoiceType() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get the invoiceType
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInvoiceTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        Long id = invoiceType.getId();

        defaultInvoiceTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where name equals to
        defaultInvoiceTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where name in
        defaultInvoiceTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where name is not null
        defaultInvoiceTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where name contains
        defaultInvoiceTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where name does not contain
        defaultInvoiceTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where description equals to
        defaultInvoiceTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where description in
        defaultInvoiceTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where description is not null
        defaultInvoiceTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where description contains
        defaultInvoiceTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoiceTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        // Get all the invoiceTypeList where description does not contain
        defaultInvoiceTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultInvoiceTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceTypeShouldBeFound(shouldBeFound);
        defaultInvoiceTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceTypeShouldBeFound(String filter) throws Exception {
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceTypeShouldNotBeFound(String filter) throws Exception {
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceType() throws Exception {
        // Get the invoiceType
        restInvoiceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceType() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceType
        InvoiceType updatedInvoiceType = invoiceTypeRepository.findById(invoiceType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceType are not directly saved in db
        em.detach(updatedInvoiceType);
        updatedInvoiceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceTypeToMatchAllProperties(updatedInvoiceType);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceType using partial update
        InvoiceType partialUpdatedInvoiceType = new InvoiceType();
        partialUpdatedInvoiceType.setId(invoiceType.getId());

        partialUpdatedInvoiceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceType, invoiceType),
            getPersistedInvoiceType(invoiceType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceType using partial update
        InvoiceType partialUpdatedInvoiceType = new InvoiceType();
        partialUpdatedInvoiceType.setId(invoiceType.getId());

        partialUpdatedInvoiceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceTypeUpdatableFieldsEquals(partialUpdatedInvoiceType, getPersistedInvoiceType(partialUpdatedInvoiceType));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceType() throws Exception {
        // Initialize the database
        insertedInvoiceType = invoiceTypeRepository.saveAndFlush(invoiceType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceType
        restInvoiceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceTypeRepository.count();
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

    protected InvoiceType getPersistedInvoiceType(InvoiceType invoiceType) {
        return invoiceTypeRepository.findById(invoiceType.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceTypeToMatchAllProperties(InvoiceType expectedInvoiceType) {
        assertInvoiceTypeAllPropertiesEquals(expectedInvoiceType, getPersistedInvoiceType(expectedInvoiceType));
    }

    protected void assertPersistedInvoiceTypeToMatchUpdatableProperties(InvoiceType expectedInvoiceType) {
        assertInvoiceTypeAllUpdatablePropertiesEquals(expectedInvoiceType, getPersistedInvoiceType(expectedInvoiceType));
    }
}
