package com.snp.web.rest;

import static com.snp.domain.FacilityGroupAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.FacilityGroup;
import com.snp.domain.FacilityGroup;
import com.snp.domain.FacilityGroupType;
import com.snp.repository.FacilityGroupRepository;
import com.snp.service.FacilityGroupService;
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
 * Integration tests for the {@link FacilityGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facility-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityGroupRepository facilityGroupRepository;

    @Mock
    private FacilityGroupRepository facilityGroupRepositoryMock;

    @Mock
    private FacilityGroupService facilityGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityGroupMockMvc;

    private FacilityGroup facilityGroup;

    private FacilityGroup insertedFacilityGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroup createEntity() {
        return new FacilityGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroup createUpdatedEntity() {
        return new FacilityGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        facilityGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityGroup != null) {
            facilityGroupRepository.delete(insertedFacilityGroup);
            insertedFacilityGroup = null;
        }
    }

    @Test
    @Transactional
    void createFacilityGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityGroup
        var returnedFacilityGroup = om.readValue(
            restFacilityGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityGroup.class
        );

        // Validate the FacilityGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityGroupUpdatableFieldsEquals(returnedFacilityGroup, getPersistedFacilityGroup(returnedFacilityGroup));

        insertedFacilityGroup = returnedFacilityGroup;
    }

    @Test
    @Transactional
    void createFacilityGroupWithExistingId() throws Exception {
        // Create the FacilityGroup with an existing ID
        facilityGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroup)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityGroup.setName(null);

        // Create the FacilityGroup, which fails.

        restFacilityGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityGroups() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityGroupServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restFacilityGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityGroupServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityGroupServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restFacilityGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityGroup() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get the facilityGroup
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFacilityGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        Long id = facilityGroup.getId();

        defaultFacilityGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where name equals to
        defaultFacilityGroupFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where name in
        defaultFacilityGroupFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where name is not null
        defaultFacilityGroupFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where name contains
        defaultFacilityGroupFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where name does not contain
        defaultFacilityGroupFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where description equals to
        defaultFacilityGroupFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where description in
        defaultFacilityGroupFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where description is not null
        defaultFacilityGroupFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where description contains
        defaultFacilityGroupFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        // Get all the facilityGroupList where description does not contain
        defaultFacilityGroupFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByFacilityGroupTypeIsEqualToSomething() throws Exception {
        FacilityGroupType facilityGroupType;
        if (TestUtil.findAll(em, FacilityGroupType.class).isEmpty()) {
            facilityGroupRepository.saveAndFlush(facilityGroup);
            facilityGroupType = FacilityGroupTypeResourceIT.createEntity();
        } else {
            facilityGroupType = TestUtil.findAll(em, FacilityGroupType.class).getFirst();
        }
        em.persist(facilityGroupType);
        em.flush();
        facilityGroup.setFacilityGroupType(facilityGroupType);
        facilityGroupRepository.saveAndFlush(facilityGroup);
        Long facilityGroupTypeId = facilityGroupType.getId();
        // Get all the facilityGroupList where facilityGroupType equals to facilityGroupTypeId
        defaultFacilityGroupShouldBeFound("facilityGroupTypeId.equals=" + facilityGroupTypeId);

        // Get all the facilityGroupList where facilityGroupType equals to (facilityGroupTypeId + 1)
        defaultFacilityGroupShouldNotBeFound("facilityGroupTypeId.equals=" + (facilityGroupTypeId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityGroupsByParentFacilityGroupIsEqualToSomething() throws Exception {
        FacilityGroup parentFacilityGroup;
        if (TestUtil.findAll(em, FacilityGroup.class).isEmpty()) {
            facilityGroupRepository.saveAndFlush(facilityGroup);
            parentFacilityGroup = FacilityGroupResourceIT.createEntity();
        } else {
            parentFacilityGroup = TestUtil.findAll(em, FacilityGroup.class).getFirst();
        }
        em.persist(parentFacilityGroup);
        em.flush();
        facilityGroup.setParentFacilityGroup(parentFacilityGroup);
        facilityGroupRepository.saveAndFlush(facilityGroup);
        Long parentFacilityGroupId = parentFacilityGroup.getId();
        // Get all the facilityGroupList where parentFacilityGroup equals to parentFacilityGroupId
        defaultFacilityGroupShouldBeFound("parentFacilityGroupId.equals=" + parentFacilityGroupId);

        // Get all the facilityGroupList where parentFacilityGroup equals to (parentFacilityGroupId + 1)
        defaultFacilityGroupShouldNotBeFound("parentFacilityGroupId.equals=" + (parentFacilityGroupId + 1));
    }

    private void defaultFacilityGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityGroupShouldBeFound(shouldBeFound);
        defaultFacilityGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityGroupShouldBeFound(String filter) throws Exception {
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityGroupShouldNotBeFound(String filter) throws Exception {
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityGroup() throws Exception {
        // Get the facilityGroup
        restFacilityGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityGroup() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroup
        FacilityGroup updatedFacilityGroup = facilityGroupRepository.findById(facilityGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityGroup are not directly saved in db
        em.detach(updatedFacilityGroup);
        updatedFacilityGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityGroup))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityGroupToMatchAllProperties(updatedFacilityGroup);
    }

    @Test
    @Transactional
    void putNonExistingFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityGroupWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroup using partial update
        FacilityGroup partialUpdatedFacilityGroup = new FacilityGroup();
        partialUpdatedFacilityGroup.setId(facilityGroup.getId());

        partialUpdatedFacilityGroup.description(UPDATED_DESCRIPTION);

        restFacilityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroup))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityGroup, facilityGroup),
            getPersistedFacilityGroup(facilityGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityGroupWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroup using partial update
        FacilityGroup partialUpdatedFacilityGroup = new FacilityGroup();
        partialUpdatedFacilityGroup.setId(facilityGroup.getId());

        partialUpdatedFacilityGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroup))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupUpdatableFieldsEquals(partialUpdatedFacilityGroup, getPersistedFacilityGroup(partialUpdatedFacilityGroup));
    }

    @Test
    @Transactional
    void patchNonExistingFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityGroup() throws Exception {
        // Initialize the database
        insertedFacilityGroup = facilityGroupRepository.saveAndFlush(facilityGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityGroup
        restFacilityGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityGroupRepository.count();
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

    protected FacilityGroup getPersistedFacilityGroup(FacilityGroup facilityGroup) {
        return facilityGroupRepository.findById(facilityGroup.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityGroupToMatchAllProperties(FacilityGroup expectedFacilityGroup) {
        assertFacilityGroupAllPropertiesEquals(expectedFacilityGroup, getPersistedFacilityGroup(expectedFacilityGroup));
    }

    protected void assertPersistedFacilityGroupToMatchUpdatableProperties(FacilityGroup expectedFacilityGroup) {
        assertFacilityGroupAllUpdatablePropertiesEquals(expectedFacilityGroup, getPersistedFacilityGroup(expectedFacilityGroup));
    }
}
