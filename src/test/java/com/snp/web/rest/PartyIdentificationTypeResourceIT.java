package com.snp.web.rest;

import static com.snp.domain.PartyIdentificationTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PartyIdentificationType;
import com.snp.repository.PartyIdentificationTypeRepository;
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
 * Integration tests for the {@link PartyIdentificationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyIdentificationTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-identification-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyIdentificationTypeRepository partyIdentificationTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyIdentificationTypeMockMvc;

    private PartyIdentificationType partyIdentificationType;

    private PartyIdentificationType insertedPartyIdentificationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyIdentificationType createEntity() {
        return new PartyIdentificationType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyIdentificationType createUpdatedEntity() {
        return new PartyIdentificationType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        partyIdentificationType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyIdentificationType != null) {
            partyIdentificationTypeRepository.delete(insertedPartyIdentificationType);
            insertedPartyIdentificationType = null;
        }
    }

    @Test
    @Transactional
    void createPartyIdentificationType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyIdentificationType
        var returnedPartyIdentificationType = om.readValue(
            restPartyIdentificationTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentificationType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyIdentificationType.class
        );

        // Validate the PartyIdentificationType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyIdentificationTypeUpdatableFieldsEquals(
            returnedPartyIdentificationType,
            getPersistedPartyIdentificationType(returnedPartyIdentificationType)
        );

        insertedPartyIdentificationType = returnedPartyIdentificationType;
    }

    @Test
    @Transactional
    void createPartyIdentificationTypeWithExistingId() throws Exception {
        // Create the PartyIdentificationType with an existing ID
        partyIdentificationType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyIdentificationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentificationType)))
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyIdentificationType.setName(null);

        // Create the PartyIdentificationType, which fails.

        restPartyIdentificationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentificationType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypes() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyIdentificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPartyIdentificationType() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get the partyIdentificationType
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, partyIdentificationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyIdentificationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPartyIdentificationTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        Long id = partyIdentificationType.getId();

        defaultPartyIdentificationTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyIdentificationTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyIdentificationTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where name equals to
        defaultPartyIdentificationTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where name in
        defaultPartyIdentificationTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where name is not null
        defaultPartyIdentificationTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where name contains
        defaultPartyIdentificationTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where name does not contain
        defaultPartyIdentificationTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where description equals to
        defaultPartyIdentificationTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where description in
        defaultPartyIdentificationTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where description is not null
        defaultPartyIdentificationTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where description contains
        defaultPartyIdentificationTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyIdentificationTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        // Get all the partyIdentificationTypeList where description does not contain
        defaultPartyIdentificationTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultPartyIdentificationTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyIdentificationTypeShouldBeFound(shouldBeFound);
        defaultPartyIdentificationTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyIdentificationTypeShouldBeFound(String filter) throws Exception {
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyIdentificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyIdentificationTypeShouldNotBeFound(String filter) throws Exception {
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyIdentificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyIdentificationType() throws Exception {
        // Get the partyIdentificationType
        restPartyIdentificationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyIdentificationType() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentificationType
        PartyIdentificationType updatedPartyIdentificationType = partyIdentificationTypeRepository
            .findById(partyIdentificationType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPartyIdentificationType are not directly saved in db
        em.detach(updatedPartyIdentificationType);
        updatedPartyIdentificationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyIdentificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyIdentificationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyIdentificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyIdentificationTypeToMatchAllProperties(updatedPartyIdentificationType);
    }

    @Test
    @Transactional
    void putNonExistingPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyIdentificationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyIdentificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyIdentificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentificationType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyIdentificationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentificationType using partial update
        PartyIdentificationType partialUpdatedPartyIdentificationType = new PartyIdentificationType();
        partialUpdatedPartyIdentificationType.setId(partyIdentificationType.getId());

        restPartyIdentificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyIdentificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyIdentificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentificationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyIdentificationTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyIdentificationType, partyIdentificationType),
            getPersistedPartyIdentificationType(partyIdentificationType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyIdentificationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentificationType using partial update
        PartyIdentificationType partialUpdatedPartyIdentificationType = new PartyIdentificationType();
        partialUpdatedPartyIdentificationType.setId(partyIdentificationType.getId());

        partialUpdatedPartyIdentificationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyIdentificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyIdentificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyIdentificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentificationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyIdentificationTypeUpdatableFieldsEquals(
            partialUpdatedPartyIdentificationType,
            getPersistedPartyIdentificationType(partialUpdatedPartyIdentificationType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyIdentificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyIdentificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyIdentificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyIdentificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyIdentificationType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyIdentificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyIdentificationType() throws Exception {
        // Initialize the database
        insertedPartyIdentificationType = partyIdentificationTypeRepository.saveAndFlush(partyIdentificationType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyIdentificationType
        restPartyIdentificationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyIdentificationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyIdentificationTypeRepository.count();
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

    protected PartyIdentificationType getPersistedPartyIdentificationType(PartyIdentificationType partyIdentificationType) {
        return partyIdentificationTypeRepository.findById(partyIdentificationType.getId()).orElseThrow();
    }

    protected void assertPersistedPartyIdentificationTypeToMatchAllProperties(PartyIdentificationType expectedPartyIdentificationType) {
        assertPartyIdentificationTypeAllPropertiesEquals(
            expectedPartyIdentificationType,
            getPersistedPartyIdentificationType(expectedPartyIdentificationType)
        );
    }

    protected void assertPersistedPartyIdentificationTypeToMatchUpdatableProperties(
        PartyIdentificationType expectedPartyIdentificationType
    ) {
        assertPartyIdentificationTypeAllUpdatablePropertiesEquals(
            expectedPartyIdentificationType,
            getPersistedPartyIdentificationType(expectedPartyIdentificationType)
        );
    }
}
