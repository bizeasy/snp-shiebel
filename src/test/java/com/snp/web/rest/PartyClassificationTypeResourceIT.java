package com.snp.web.rest;

import static com.snp.domain.PartyClassificationTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PartyClassificationType;
import com.snp.repository.PartyClassificationTypeRepository;
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
 * Integration tests for the {@link PartyClassificationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyClassificationTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-classification-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyClassificationTypeRepository partyClassificationTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyClassificationTypeMockMvc;

    private PartyClassificationType partyClassificationType;

    private PartyClassificationType insertedPartyClassificationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassificationType createEntity() {
        return new PartyClassificationType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassificationType createUpdatedEntity() {
        return new PartyClassificationType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        partyClassificationType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyClassificationType != null) {
            partyClassificationTypeRepository.delete(insertedPartyClassificationType);
            insertedPartyClassificationType = null;
        }
    }

    @Test
    @Transactional
    void createPartyClassificationType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyClassificationType
        var returnedPartyClassificationType = om.readValue(
            restPartyClassificationTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyClassificationType.class
        );

        // Validate the PartyClassificationType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyClassificationTypeUpdatableFieldsEquals(
            returnedPartyClassificationType,
            getPersistedPartyClassificationType(returnedPartyClassificationType)
        );

        insertedPartyClassificationType = returnedPartyClassificationType;
    }

    @Test
    @Transactional
    void createPartyClassificationTypeWithExistingId() throws Exception {
        // Create the PartyClassificationType with an existing ID
        partyClassificationType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyClassificationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationType)))
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyClassificationType.setName(null);

        // Create the PartyClassificationType, which fails.

        restPartyClassificationTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypes() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPartyClassificationType() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get the partyClassificationType
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, partyClassificationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyClassificationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPartyClassificationTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        Long id = partyClassificationType.getId();

        defaultPartyClassificationTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyClassificationTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyClassificationTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where name equals to
        defaultPartyClassificationTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where name in
        defaultPartyClassificationTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where name is not null
        defaultPartyClassificationTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where name contains
        defaultPartyClassificationTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where name does not contain
        defaultPartyClassificationTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where description equals to
        defaultPartyClassificationTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where description in
        defaultPartyClassificationTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where description is not null
        defaultPartyClassificationTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where description contains
        defaultPartyClassificationTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        // Get all the partyClassificationTypeList where description does not contain
        defaultPartyClassificationTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultPartyClassificationTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyClassificationTypeShouldBeFound(shouldBeFound);
        defaultPartyClassificationTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyClassificationTypeShouldBeFound(String filter) throws Exception {
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyClassificationTypeShouldNotBeFound(String filter) throws Exception {
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyClassificationType() throws Exception {
        // Get the partyClassificationType
        restPartyClassificationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyClassificationType() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationType
        PartyClassificationType updatedPartyClassificationType = partyClassificationTypeRepository
            .findById(partyClassificationType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPartyClassificationType are not directly saved in db
        em.detach(updatedPartyClassificationType);
        updatedPartyClassificationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyClassificationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyClassificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyClassificationTypeToMatchAllProperties(updatedPartyClassificationType);
    }

    @Test
    @Transactional
    void putNonExistingPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyClassificationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyClassificationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationType using partial update
        PartyClassificationType partialUpdatedPartyClassificationType = new PartyClassificationType();
        partialUpdatedPartyClassificationType.setId(partyClassificationType.getId());

        partialUpdatedPartyClassificationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyClassificationType, partyClassificationType),
            getPersistedPartyClassificationType(partyClassificationType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyClassificationTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationType using partial update
        PartyClassificationType partialUpdatedPartyClassificationType = new PartyClassificationType();
        partialUpdatedPartyClassificationType.setId(partyClassificationType.getId());

        partialUpdatedPartyClassificationType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassificationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationTypeUpdatableFieldsEquals(
            partialUpdatedPartyClassificationType,
            getPersistedPartyClassificationType(partialUpdatedPartyClassificationType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyClassificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassificationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyClassificationType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyClassificationType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassificationType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyClassificationType() throws Exception {
        // Initialize the database
        insertedPartyClassificationType = partyClassificationTypeRepository.saveAndFlush(partyClassificationType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyClassificationType
        restPartyClassificationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyClassificationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyClassificationTypeRepository.count();
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

    protected PartyClassificationType getPersistedPartyClassificationType(PartyClassificationType partyClassificationType) {
        return partyClassificationTypeRepository.findById(partyClassificationType.getId()).orElseThrow();
    }

    protected void assertPersistedPartyClassificationTypeToMatchAllProperties(PartyClassificationType expectedPartyClassificationType) {
        assertPartyClassificationTypeAllPropertiesEquals(
            expectedPartyClassificationType,
            getPersistedPartyClassificationType(expectedPartyClassificationType)
        );
    }

    protected void assertPersistedPartyClassificationTypeToMatchUpdatableProperties(
        PartyClassificationType expectedPartyClassificationType
    ) {
        assertPartyClassificationTypeAllUpdatablePropertiesEquals(
            expectedPartyClassificationType,
            getPersistedPartyClassificationType(expectedPartyClassificationType)
        );
    }
}
