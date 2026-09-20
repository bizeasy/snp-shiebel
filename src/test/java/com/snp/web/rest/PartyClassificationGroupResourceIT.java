package com.snp.web.rest;

import static com.snp.domain.PartyClassificationGroupAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PartyClassificationGroup;
import com.snp.domain.PartyClassificationType;
import com.snp.repository.PartyClassificationGroupRepository;
import com.snp.service.PartyClassificationGroupService;
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
 * Integration tests for the {@link PartyClassificationGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyClassificationGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-classification-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyClassificationGroupRepository partyClassificationGroupRepository;

    @Mock
    private PartyClassificationGroupRepository partyClassificationGroupRepositoryMock;

    @Mock
    private PartyClassificationGroupService partyClassificationGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyClassificationGroupMockMvc;

    private PartyClassificationGroup partyClassificationGroup;

    private PartyClassificationGroup insertedPartyClassificationGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassificationGroup createEntity() {
        return new PartyClassificationGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassificationGroup createUpdatedEntity() {
        return new PartyClassificationGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        partyClassificationGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyClassificationGroup != null) {
            partyClassificationGroupRepository.delete(insertedPartyClassificationGroup);
            insertedPartyClassificationGroup = null;
        }
    }

    @Test
    @Transactional
    void createPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyClassificationGroup
        var returnedPartyClassificationGroup = om.readValue(
            restPartyClassificationGroupMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationGroup))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyClassificationGroup.class
        );

        // Validate the PartyClassificationGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyClassificationGroupUpdatableFieldsEquals(
            returnedPartyClassificationGroup,
            getPersistedPartyClassificationGroup(returnedPartyClassificationGroup)
        );

        insertedPartyClassificationGroup = returnedPartyClassificationGroup;
    }

    @Test
    @Transactional
    void createPartyClassificationGroupWithExistingId() throws Exception {
        // Create the PartyClassificationGroup with an existing ID
        partyClassificationGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyClassificationGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyClassificationGroup.setName(null);

        // Create the PartyClassificationGroup, which fails.

        restPartyClassificationGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationGroup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroups() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassificationGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyClassificationGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyClassificationGroupServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartyClassificationGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyClassificationGroupServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyClassificationGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyClassificationGroupServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartyClassificationGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyClassificationGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyClassificationGroup() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get the partyClassificationGroup
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, partyClassificationGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyClassificationGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPartyClassificationGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        Long id = partyClassificationGroup.getId();

        defaultPartyClassificationGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyClassificationGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyClassificationGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where name equals to
        defaultPartyClassificationGroupFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where name in
        defaultPartyClassificationGroupFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where name is not null
        defaultPartyClassificationGroupFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where name contains
        defaultPartyClassificationGroupFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where name does not contain
        defaultPartyClassificationGroupFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where description equals to
        defaultPartyClassificationGroupFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where description in
        defaultPartyClassificationGroupFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where description is not null
        defaultPartyClassificationGroupFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where description contains
        defaultPartyClassificationGroupFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        // Get all the partyClassificationGroupList where description does not contain
        defaultPartyClassificationGroupFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationGroupsByClassificationTypeIsEqualToSomething() throws Exception {
        PartyClassificationType classificationType;
        if (TestUtil.findAll(em, PartyClassificationType.class).isEmpty()) {
            partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);
            classificationType = PartyClassificationTypeResourceIT.createEntity();
        } else {
            classificationType = TestUtil.findAll(em, PartyClassificationType.class).getFirst();
        }
        em.persist(classificationType);
        em.flush();
        partyClassificationGroup.setClassificationType(classificationType);
        partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);
        Long classificationTypeId = classificationType.getId();
        // Get all the partyClassificationGroupList where classificationType equals to classificationTypeId
        defaultPartyClassificationGroupShouldBeFound("classificationTypeId.equals=" + classificationTypeId);

        // Get all the partyClassificationGroupList where classificationType equals to (classificationTypeId + 1)
        defaultPartyClassificationGroupShouldNotBeFound("classificationTypeId.equals=" + (classificationTypeId + 1));
    }

    private void defaultPartyClassificationGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyClassificationGroupShouldBeFound(shouldBeFound);
        defaultPartyClassificationGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyClassificationGroupShouldBeFound(String filter) throws Exception {
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassificationGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyClassificationGroupShouldNotBeFound(String filter) throws Exception {
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyClassificationGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyClassificationGroup() throws Exception {
        // Get the partyClassificationGroup
        restPartyClassificationGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyClassificationGroup() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationGroup
        PartyClassificationGroup updatedPartyClassificationGroup = partyClassificationGroupRepository
            .findById(partyClassificationGroup.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPartyClassificationGroup are not directly saved in db
        em.detach(updatedPartyClassificationGroup);
        updatedPartyClassificationGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyClassificationGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyClassificationGroup))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyClassificationGroupToMatchAllProperties(updatedPartyClassificationGroup);
    }

    @Test
    @Transactional
    void putNonExistingPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyClassificationGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassificationGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassificationGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassificationGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyClassificationGroupWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationGroup using partial update
        PartyClassificationGroup partialUpdatedPartyClassificationGroup = new PartyClassificationGroup();
        partialUpdatedPartyClassificationGroup.setId(partyClassificationGroup.getId());

        partialUpdatedPartyClassificationGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassificationGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassificationGroup))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyClassificationGroup, partyClassificationGroup),
            getPersistedPartyClassificationGroup(partyClassificationGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyClassificationGroupWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassificationGroup using partial update
        PartyClassificationGroup partialUpdatedPartyClassificationGroup = new PartyClassificationGroup();
        partialUpdatedPartyClassificationGroup.setId(partyClassificationGroup.getId());

        partialUpdatedPartyClassificationGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPartyClassificationGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassificationGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassificationGroup))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassificationGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationGroupUpdatableFieldsEquals(
            partialUpdatedPartyClassificationGroup,
            getPersistedPartyClassificationGroup(partialUpdatedPartyClassificationGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyClassificationGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassificationGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassificationGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyClassificationGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassificationGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyClassificationGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassificationGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyClassificationGroup() throws Exception {
        // Initialize the database
        insertedPartyClassificationGroup = partyClassificationGroupRepository.saveAndFlush(partyClassificationGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyClassificationGroup
        restPartyClassificationGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyClassificationGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyClassificationGroupRepository.count();
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

    protected PartyClassificationGroup getPersistedPartyClassificationGroup(PartyClassificationGroup partyClassificationGroup) {
        return partyClassificationGroupRepository.findById(partyClassificationGroup.getId()).orElseThrow();
    }

    protected void assertPersistedPartyClassificationGroupToMatchAllProperties(PartyClassificationGroup expectedPartyClassificationGroup) {
        assertPartyClassificationGroupAllPropertiesEquals(
            expectedPartyClassificationGroup,
            getPersistedPartyClassificationGroup(expectedPartyClassificationGroup)
        );
    }

    protected void assertPersistedPartyClassificationGroupToMatchUpdatableProperties(
        PartyClassificationGroup expectedPartyClassificationGroup
    ) {
        assertPartyClassificationGroupAllUpdatablePropertiesEquals(
            expectedPartyClassificationGroup,
            getPersistedPartyClassificationGroup(expectedPartyClassificationGroup)
        );
    }
}
