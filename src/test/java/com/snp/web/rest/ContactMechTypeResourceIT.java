package com.snp.web.rest;

import static com.snp.domain.ContactMechTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMechType;
import com.snp.repository.ContactMechTypeRepository;
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
 * Integration tests for the {@link ContactMechTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactMechTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-mech-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactMechTypeRepository contactMechTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMechTypeMockMvc;

    private ContactMechType contactMechType;

    private ContactMechType insertedContactMechType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMechType createEntity() {
        return new ContactMechType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMechType createUpdatedEntity() {
        return new ContactMechType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        contactMechType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContactMechType != null) {
            contactMechTypeRepository.delete(insertedContactMechType);
            insertedContactMechType = null;
        }
    }

    @Test
    @Transactional
    void createContactMechType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactMechType
        var returnedContactMechType = om.readValue(
            restContactMechTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactMechType.class
        );

        // Validate the ContactMechType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContactMechTypeUpdatableFieldsEquals(returnedContactMechType, getPersistedContactMechType(returnedContactMechType));

        insertedContactMechType = returnedContactMechType;
    }

    @Test
    @Transactional
    void createContactMechTypeWithExistingId() throws Exception {
        // Create the ContactMechType with an existing ID
        contactMechType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMechTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechType)))
            .andExpect(status().isBadRequest());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactMechType.setName(null);

        // Create the ContactMechType, which fails.

        restContactMechTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactMechTypes() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMechType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getContactMechType() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get the contactMechType
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, contactMechType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactMechType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getContactMechTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        Long id = contactMechType.getId();

        defaultContactMechTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContactMechTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContactMechTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where name equals to
        defaultContactMechTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where name in
        defaultContactMechTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where name is not null
        defaultContactMechTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllContactMechTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where name contains
        defaultContactMechTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where name does not contain
        defaultContactMechTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where description equals to
        defaultContactMechTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where description in
        defaultContactMechTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllContactMechTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where description is not null
        defaultContactMechTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllContactMechTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where description contains
        defaultContactMechTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContactMechTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        // Get all the contactMechTypeList where description does not contain
        defaultContactMechTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultContactMechTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContactMechTypeShouldBeFound(shouldBeFound);
        defaultContactMechTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactMechTypeShouldBeFound(String filter) throws Exception {
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMechType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactMechTypeShouldNotBeFound(String filter) throws Exception {
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMechTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactMechType() throws Exception {
        // Get the contactMechType
        restContactMechTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactMechType() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechType
        ContactMechType updatedContactMechType = contactMechTypeRepository.findById(contactMechType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactMechType are not directly saved in db
        em.detach(updatedContactMechType);
        updatedContactMechType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContactMechTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactMechType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContactMechType))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactMechTypeToMatchAllProperties(updatedContactMechType);
    }

    @Test
    @Transactional
    void putNonExistingContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactMechType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMechType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMechType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMechType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactMechTypeWithPatch() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechType using partial update
        ContactMechType partialUpdatedContactMechType = new ContactMechType();
        partialUpdatedContactMechType.setId(contactMechType.getId());

        restContactMechTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMechType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMechType))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactMechType, contactMechType),
            getPersistedContactMechType(contactMechType)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactMechTypeWithPatch() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMechType using partial update
        ContactMechType partialUpdatedContactMechType = new ContactMechType();
        partialUpdatedContactMechType.setId(contactMechType.getId());

        partialUpdatedContactMechType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContactMechTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMechType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMechType))
            )
            .andExpect(status().isOk());

        // Validate the ContactMechType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechTypeUpdatableFieldsEquals(
            partialUpdatedContactMechType,
            getPersistedContactMechType(partialUpdatedContactMechType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactMechType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMechType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMechType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactMechType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMechType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactMechType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMechType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactMechType() throws Exception {
        // Initialize the database
        insertedContactMechType = contactMechTypeRepository.saveAndFlush(contactMechType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactMechType
        restContactMechTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactMechType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactMechTypeRepository.count();
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

    protected ContactMechType getPersistedContactMechType(ContactMechType contactMechType) {
        return contactMechTypeRepository.findById(contactMechType.getId()).orElseThrow();
    }

    protected void assertPersistedContactMechTypeToMatchAllProperties(ContactMechType expectedContactMechType) {
        assertContactMechTypeAllPropertiesEquals(expectedContactMechType, getPersistedContactMechType(expectedContactMechType));
    }

    protected void assertPersistedContactMechTypeToMatchUpdatableProperties(ContactMechType expectedContactMechType) {
        assertContactMechTypeAllUpdatablePropertiesEquals(expectedContactMechType, getPersistedContactMechType(expectedContactMechType));
    }
}
