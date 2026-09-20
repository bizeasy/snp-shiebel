package com.snp.web.rest;

import static com.snp.domain.InvoiceItemTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InvoiceItemType;
import com.snp.repository.InvoiceItemTypeRepository;
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
 * Integration tests for the {@link InvoiceItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceItemTypeRepository invoiceItemTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemTypeMockMvc;

    private InvoiceItemType invoiceItemType;

    private InvoiceItemType insertedInvoiceItemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItemType createEntity() {
        return new InvoiceItemType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItemType createUpdatedEntity() {
        return new InvoiceItemType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        invoiceItemType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceItemType != null) {
            invoiceItemTypeRepository.delete(insertedInvoiceItemType);
            insertedInvoiceItemType = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceItemType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceItemType
        var returnedInvoiceItemType = om.readValue(
            restInvoiceItemTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceItemType.class
        );

        // Validate the InvoiceItemType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceItemTypeUpdatableFieldsEquals(returnedInvoiceItemType, getPersistedInvoiceItemType(returnedInvoiceItemType));

        insertedInvoiceItemType = returnedInvoiceItemType;
    }

    @Test
    @Transactional
    void createInvoiceItemTypeWithExistingId() throws Exception {
        // Create the InvoiceItemType with an existing ID
        invoiceItemType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemType)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItemType.setName(null);

        // Create the InvoiceItemType, which fails.

        restInvoiceItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypes() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getInvoiceItemType() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get the invoiceItemType
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItemType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInvoiceItemTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        Long id = invoiceItemType.getId();

        defaultInvoiceItemTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceItemTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceItemTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where name equals to
        defaultInvoiceItemTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where name in
        defaultInvoiceItemTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where name is not null
        defaultInvoiceItemTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where name contains
        defaultInvoiceItemTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where name does not contain
        defaultInvoiceItemTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where description equals to
        defaultInvoiceItemTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where description in
        defaultInvoiceItemTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where description is not null
        defaultInvoiceItemTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where description contains
        defaultInvoiceItemTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        // Get all the invoiceItemTypeList where description does not contain
        defaultInvoiceItemTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultInvoiceItemTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceItemTypeShouldBeFound(shouldBeFound);
        defaultInvoiceItemTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceItemTypeShouldBeFound(String filter) throws Exception {
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceItemTypeShouldNotBeFound(String filter) throws Exception {
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItemType() throws Exception {
        // Get the invoiceItemType
        restInvoiceItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItemType() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemType
        InvoiceItemType updatedInvoiceItemType = invoiceItemTypeRepository.findById(invoiceItemType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItemType are not directly saved in db
        em.detach(updatedInvoiceItemType);
        updatedInvoiceItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceItemType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceItemTypeToMatchAllProperties(updatedInvoiceItemType);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemType using partial update
        InvoiceItemType partialUpdatedInvoiceItemType = new InvoiceItemType();
        partialUpdatedInvoiceItemType.setId(invoiceItemType.getId());

        partialUpdatedInvoiceItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItemType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceItemType, invoiceItemType),
            getPersistedInvoiceItemType(invoiceItemType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemType using partial update
        InvoiceItemType partialUpdatedInvoiceItemType = new InvoiceItemType();
        partialUpdatedInvoiceItemType.setId(invoiceItemType.getId());

        partialUpdatedInvoiceItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restInvoiceItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItemType))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemTypeUpdatableFieldsEquals(
            partialUpdatedInvoiceItemType,
            getPersistedInvoiceItemType(partialUpdatedInvoiceItemType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItemType() throws Exception {
        // Initialize the database
        insertedInvoiceItemType = invoiceItemTypeRepository.saveAndFlush(invoiceItemType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceItemType
        restInvoiceItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItemType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceItemTypeRepository.count();
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

    protected InvoiceItemType getPersistedInvoiceItemType(InvoiceItemType invoiceItemType) {
        return invoiceItemTypeRepository.findById(invoiceItemType.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceItemTypeToMatchAllProperties(InvoiceItemType expectedInvoiceItemType) {
        assertInvoiceItemTypeAllPropertiesEquals(expectedInvoiceItemType, getPersistedInvoiceItemType(expectedInvoiceItemType));
    }

    protected void assertPersistedInvoiceItemTypeToMatchUpdatableProperties(InvoiceItemType expectedInvoiceItemType) {
        assertInvoiceItemTypeAllUpdatablePropertiesEquals(expectedInvoiceItemType, getPersistedInvoiceItemType(expectedInvoiceItemType));
    }
}
