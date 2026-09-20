package com.snp.web.rest;

import static com.snp.domain.PartyRelationshipTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PartyRelationshipType;
import com.snp.domain.RoleType;
import com.snp.repository.PartyRelationshipTypeRepository;
import com.snp.service.PartyRelationshipTypeService;
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
 * Integration tests for the {@link PartyRelationshipTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyRelationshipTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-relationship-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyRelationshipTypeRepository partyRelationshipTypeRepository;

    @Mock
    private PartyRelationshipTypeRepository partyRelationshipTypeRepositoryMock;

    @Mock
    private PartyRelationshipTypeService partyRelationshipTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyRelationshipTypeMockMvc;

    private PartyRelationshipType partyRelationshipType;

    private PartyRelationshipType insertedPartyRelationshipType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationshipType createEntity() {
        return new PartyRelationshipType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationshipType createUpdatedEntity() {
        return new PartyRelationshipType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        partyRelationshipType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyRelationshipType != null) {
            partyRelationshipTypeRepository.delete(insertedPartyRelationshipType);
            insertedPartyRelationshipType = null;
        }
    }

    @Test
    @Transactional
    void createPartyRelationshipType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyRelationshipType
        var returnedPartyRelationshipType = om.readValue(
            restPartyRelationshipTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationshipType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyRelationshipType.class
        );

        // Validate the PartyRelationshipType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyRelationshipTypeUpdatableFieldsEquals(
            returnedPartyRelationshipType,
            getPersistedPartyRelationshipType(returnedPartyRelationshipType)
        );

        insertedPartyRelationshipType = returnedPartyRelationshipType;
    }

    @Test
    @Transactional
    void createPartyRelationshipTypeWithExistingId() throws Exception {
        // Create the PartyRelationshipType with an existing ID
        partyRelationshipType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyRelationshipTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationshipType)))
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyRelationshipType.setName(null);

        // Create the PartyRelationshipType, which fails.

        restPartyRelationshipTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationshipType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypes() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationshipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRelationshipTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyRelationshipTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartyRelationshipTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyRelationshipTypeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRelationshipTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyRelationshipTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartyRelationshipTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyRelationshipTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyRelationshipType() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get the partyRelationshipType
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, partyRelationshipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyRelationshipType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPartyRelationshipTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        Long id = partyRelationshipType.getId();

        defaultPartyRelationshipTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyRelationshipTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyRelationshipTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where name equals to
        defaultPartyRelationshipTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where name in
        defaultPartyRelationshipTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where name is not null
        defaultPartyRelationshipTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where name contains
        defaultPartyRelationshipTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where name does not contain
        defaultPartyRelationshipTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where description equals to
        defaultPartyRelationshipTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where description in
        defaultPartyRelationshipTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where description is not null
        defaultPartyRelationshipTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where description contains
        defaultPartyRelationshipTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        // Get all the partyRelationshipTypeList where description does not contain
        defaultPartyRelationshipTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByRoleTypeFromIsEqualToSomething() throws Exception {
        RoleType roleTypeFrom;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);
            roleTypeFrom = RoleTypeResourceIT.createEntity();
        } else {
            roleTypeFrom = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleTypeFrom);
        em.flush();
        partyRelationshipType.setRoleTypeFrom(roleTypeFrom);
        partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);
        Long roleTypeFromId = roleTypeFrom.getId();
        // Get all the partyRelationshipTypeList where roleTypeFrom equals to roleTypeFromId
        defaultPartyRelationshipTypeShouldBeFound("roleTypeFromId.equals=" + roleTypeFromId);

        // Get all the partyRelationshipTypeList where roleTypeFrom equals to (roleTypeFromId + 1)
        defaultPartyRelationshipTypeShouldNotBeFound("roleTypeFromId.equals=" + (roleTypeFromId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRelationshipTypesByRoleTypeToIsEqualToSomething() throws Exception {
        RoleType roleTypeTo;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);
            roleTypeTo = RoleTypeResourceIT.createEntity();
        } else {
            roleTypeTo = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleTypeTo);
        em.flush();
        partyRelationshipType.setRoleTypeTo(roleTypeTo);
        partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);
        Long roleTypeToId = roleTypeTo.getId();
        // Get all the partyRelationshipTypeList where roleTypeTo equals to roleTypeToId
        defaultPartyRelationshipTypeShouldBeFound("roleTypeToId.equals=" + roleTypeToId);

        // Get all the partyRelationshipTypeList where roleTypeTo equals to (roleTypeToId + 1)
        defaultPartyRelationshipTypeShouldNotBeFound("roleTypeToId.equals=" + (roleTypeToId + 1));
    }

    private void defaultPartyRelationshipTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyRelationshipTypeShouldBeFound(shouldBeFound);
        defaultPartyRelationshipTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyRelationshipTypeShouldBeFound(String filter) throws Exception {
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationshipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyRelationshipTypeShouldNotBeFound(String filter) throws Exception {
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyRelationshipTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyRelationshipType() throws Exception {
        // Get the partyRelationshipType
        restPartyRelationshipTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyRelationshipType() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationshipType
        PartyRelationshipType updatedPartyRelationshipType = partyRelationshipTypeRepository
            .findById(partyRelationshipType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPartyRelationshipType are not directly saved in db
        em.detach(updatedPartyRelationshipType);
        updatedPartyRelationshipType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyRelationshipTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyRelationshipType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyRelationshipType))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyRelationshipTypeToMatchAllProperties(updatedPartyRelationshipType);
    }

    @Test
    @Transactional
    void putNonExistingPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyRelationshipType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyRelationshipType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyRelationshipType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationshipType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyRelationshipTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationshipType using partial update
        PartyRelationshipType partialUpdatedPartyRelationshipType = new PartyRelationshipType();
        partialUpdatedPartyRelationshipType.setId(partyRelationshipType.getId());

        partialUpdatedPartyRelationshipType.name(UPDATED_NAME);

        restPartyRelationshipTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationshipType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRelationshipType))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationshipType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRelationshipTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyRelationshipType, partyRelationshipType),
            getPersistedPartyRelationshipType(partyRelationshipType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyRelationshipTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationshipType using partial update
        PartyRelationshipType partialUpdatedPartyRelationshipType = new PartyRelationshipType();
        partialUpdatedPartyRelationshipType.setId(partyRelationshipType.getId());

        partialUpdatedPartyRelationshipType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyRelationshipTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationshipType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRelationshipType))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationshipType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRelationshipTypeUpdatableFieldsEquals(
            partialUpdatedPartyRelationshipType,
            getPersistedPartyRelationshipType(partialUpdatedPartyRelationshipType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyRelationshipType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRelationshipType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRelationshipType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyRelationshipType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationshipType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyRelationshipType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationshipType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyRelationshipType() throws Exception {
        // Initialize the database
        insertedPartyRelationshipType = partyRelationshipTypeRepository.saveAndFlush(partyRelationshipType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyRelationshipType
        restPartyRelationshipTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyRelationshipType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyRelationshipTypeRepository.count();
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

    protected PartyRelationshipType getPersistedPartyRelationshipType(PartyRelationshipType partyRelationshipType) {
        return partyRelationshipTypeRepository.findById(partyRelationshipType.getId()).orElseThrow();
    }

    protected void assertPersistedPartyRelationshipTypeToMatchAllProperties(PartyRelationshipType expectedPartyRelationshipType) {
        assertPartyRelationshipTypeAllPropertiesEquals(
            expectedPartyRelationshipType,
            getPersistedPartyRelationshipType(expectedPartyRelationshipType)
        );
    }

    protected void assertPersistedPartyRelationshipTypeToMatchUpdatableProperties(PartyRelationshipType expectedPartyRelationshipType) {
        assertPartyRelationshipTypeAllUpdatablePropertiesEquals(
            expectedPartyRelationshipType,
            getPersistedPartyRelationshipType(expectedPartyRelationshipType)
        );
    }
}
