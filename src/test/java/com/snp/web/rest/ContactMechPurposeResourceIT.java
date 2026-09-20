package com.snp.web.rest;

import static com.snp.domain.ContactMechPurposeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMechPurpose;
import com.snp.repository.ContactMechPurposeRepository;
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
 * Integration tests for the {@link ContactMechPurposeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactMechPurposeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-mech-purposes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactMechPurposeRepository contactMechPurposeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMechPurposeMockMvc;

    private ContactMechPurpose contactMechPurpose;

    private ContactMechPurpose insertedContactMechPurpose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMechPurpose createEntity() {
        return new ContactMechPurpose().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMechPurpose createUpdatedEntity() {
        return new ContactMechPurpose().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        contactMechPurpose = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContactMechPurpose != null) {
            contactMechPurposeRepository.delete(insertedContactMechPurpose);
            insertedContactMechPurpose = null;
        }
    }

    @Test
    @Transactional
    void createContactMechPurpose() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactMechPurpose
        var returnedContactMechPurpose = om.readValue(
            restContactMechPurposeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechPurpose)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactMechPurpose.class
        );

        // Validate the ContactMechPurpose in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContactMechPurposeUpdatableFieldsEquals(
            returnedContactMechPurpose,
            getPersistedContactMechPurpose(returnedContactMechPurpose)
        );

        insertedContactMechPurpose = returnedContactMechPurpose;
    }

    @Test
    @Transactional
    void createContactMechPurposeWithExistingId() throws Exception {
        // Create the ContactMechPurpose with an existing ID
        contactMechPurpose.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMechPurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechPurpose)))
            .andExpect(status().isBadRequest());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactMechPurpose.setName(null);

        // Create the ContactMechPurpose, which fails.

        restContactMechPurposeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechPurpose)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactMechPurposes() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMechPurpose.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getContactMechPurpose() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get the contactMechPurpose
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL_ID, contactMechPurpose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactMechPurpose.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getContactMechPurposesByIdFiltering() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        Long id = contactMechPurpose.getId();

        defaultContactMechPurposeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContactMechPurposeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContactMechPurposeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where name equals to
        defaultContactMechPurposeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where name in
        defaultContactMechPurposeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where name is not null
        defaultContactMechPurposeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where name contains
        defaultContactMechPurposeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where name does not contain
        defaultContactMechPurposeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where description equals to
        defaultContactMechPurposeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where description in
        defaultContactMechPurposeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where description is not null
        defaultContactMechPurposeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where description contains
        defaultContactMechPurposeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactMechPurposesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        // Get all the contactMechPurposeList where description does not contain
        defaultContactMechPurposeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultContactMechPurposeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContactMechPurposeShouldBeFound(shouldBeFound);
        defaultContactMechPurposeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactMechPurposeShouldBeFound(String filter) throws Exception {
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMechPurpose.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactMechPurposeShouldNotBeFound(String filter) throws Exception {
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMechPurposeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactMechPurpose() throws Exception {
        // Get the contactMechPurpose
        restContactMechPurposeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactMechPurpose() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechPurpose
        ContactMechPurpose updatedContactMechPurpose = contactMechPurposeRepository.findById(contactMechPurpose.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactMechPurpose are not directly saved in db
        em.detach(updatedContactMechPurpose);
        updatedContactMechPurpose.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContactMechPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactMechPurpose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContactMechPurpose))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactMechPurposeToMatchAllProperties(updatedContactMechPurpose);
    }

    @Test
    @Transactional
    void putNonExistingContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactMechPurpose.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMechPurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMechPurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechPurpose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactMechPurposeWithPatch() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechPurpose using partial update
        ContactMechPurpose partialUpdatedContactMechPurpose = new ContactMechPurpose();
        partialUpdatedContactMechPurpose.setId(contactMechPurpose.getId());

        partialUpdatedContactMechPurpose.name(UPDATED_NAME);

        restContactMechPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMechPurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMechPurpose))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechPurpose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechPurposeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactMechPurpose, contactMechPurpose),
            getPersistedContactMechPurpose(contactMechPurpose)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactMechPurposeWithPatch() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechPurpose using partial update
        ContactMechPurpose partialUpdatedContactMechPurpose = new ContactMechPurpose();
        partialUpdatedContactMechPurpose.setId(contactMechPurpose.getId());

        partialUpdatedContactMechPurpose.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContactMechPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMechPurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMechPurpose))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechPurpose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechPurposeUpdatableFieldsEquals(
            partialUpdatedContactMechPurpose,
            getPersistedContactMechPurpose(partialUpdatedContactMechPurpose)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactMechPurpose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMechPurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMechPurpose))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactMechPurpose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechPurpose.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechPurposeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactMechPurpose)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMechPurpose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactMechPurpose() throws Exception {
        // Initialize the database
        insertedContactMechPurpose = contactMechPurposeRepository.saveAndFlush(contactMechPurpose);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactMechPurpose
        restContactMechPurposeMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactMechPurpose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactMechPurposeRepository.count();
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

    protected ContactMechPurpose getPersistedContactMechPurpose(ContactMechPurpose contactMechPurpose) {
        return contactMechPurposeRepository.findById(contactMechPurpose.getId()).orElseThrow();
    }

    protected void assertPersistedContactMechPurposeToMatchAllProperties(ContactMechPurpose expectedContactMechPurpose) {
        assertContactMechPurposeAllPropertiesEquals(expectedContactMechPurpose, getPersistedContactMechPurpose(expectedContactMechPurpose));
    }

    protected void assertPersistedContactMechPurposeToMatchUpdatableProperties(ContactMechPurpose expectedContactMechPurpose) {
        assertContactMechPurposeAllUpdatablePropertiesEquals(
            expectedContactMechPurpose,
            getPersistedContactMechPurpose(expectedContactMechPurpose)
        );
    }
}
