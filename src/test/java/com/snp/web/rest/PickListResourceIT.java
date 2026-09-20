package com.snp.web.rest;

import static com.snp.domain.PickListAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.Party;
import com.snp.domain.PickList;
import com.snp.domain.Status;
import com.snp.repository.PickListRepository;
import com.snp.service.PickListService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PickListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PickListResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_PICK_LIST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PICK_LIST_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/pick-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PickListRepository pickListRepository;

    @Mock
    private PickListRepository pickListRepositoryMock;

    @Mock
    private PickListService pickListServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPickListMockMvc;

    private PickList pickList;

    private PickList insertedPickList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickList createEntity() {
        return new PickList().description(DEFAULT_DESCRIPTION).pickListDate(DEFAULT_PICK_LIST_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickList createUpdatedEntity() {
        return new PickList().description(UPDATED_DESCRIPTION).pickListDate(UPDATED_PICK_LIST_DATE);
    }

    @BeforeEach
    void initTest() {
        pickList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPickList != null) {
            pickListRepository.delete(insertedPickList);
            insertedPickList = null;
        }
    }

    @Test
    @Transactional
    void createPickList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PickList
        var returnedPickList = om.readValue(
            restPickListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PickList.class
        );

        // Validate the PickList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPickListUpdatableFieldsEquals(returnedPickList, getPersistedPickList(returnedPickList));

        insertedPickList = returnedPickList;
    }

    @Test
    @Transactional
    void createPickListWithExistingId() throws Exception {
        // Create the PickList with an existing ID
        pickList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPickListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickList)))
            .andExpect(status().isBadRequest());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPickListDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pickList.setPickListDate(null);

        // Create the PickList, which fails.

        restPickListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickList)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPickLists() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList
        restPickListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickList.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pickListDate").value(hasItem(DEFAULT_PICK_LIST_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPickListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pickListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPickListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pickListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPickListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pickListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPickListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pickListRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPickList() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get the pickList
        restPickListMockMvc
            .perform(get(ENTITY_API_URL_ID, pickList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pickList.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pickListDate").value(DEFAULT_PICK_LIST_DATE.toString()));
    }

    @Test
    @Transactional
    void getPickListsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        Long id = pickList.getId();

        defaultPickListFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPickListFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPickListFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPickListsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where description equals to
        defaultPickListFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPickListsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where description in
        defaultPickListFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPickListsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where description is not null
        defaultPickListFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPickListsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where description contains
        defaultPickListFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPickListsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where description does not contain
        defaultPickListFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPickListsByPickListDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where pickListDate equals to
        defaultPickListFiltering("pickListDate.equals=" + DEFAULT_PICK_LIST_DATE, "pickListDate.equals=" + UPDATED_PICK_LIST_DATE);
    }

    @Test
    @Transactional
    void getAllPickListsByPickListDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where pickListDate in
        defaultPickListFiltering(
            "pickListDate.in=" + DEFAULT_PICK_LIST_DATE + "," + UPDATED_PICK_LIST_DATE,
            "pickListDate.in=" + UPDATED_PICK_LIST_DATE
        );
    }

    @Test
    @Transactional
    void getAllPickListsByPickListDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        // Get all the pickListList where pickListDate is not null
        defaultPickListFiltering("pickListDate.specified=true", "pickListDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPickListsByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            pickListRepository.saveAndFlush(pickList);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        pickList.setFacility(facility);
        pickListRepository.saveAndFlush(pickList);
        Long facilityId = facility.getId();
        // Get all the pickListList where facility equals to facilityId
        defaultPickListShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the pickListList where facility equals to (facilityId + 1)
        defaultPickListShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllPickListsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            pickListRepository.saveAndFlush(pickList);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        pickList.setStatus(status);
        pickListRepository.saveAndFlush(pickList);
        Long statusId = status.getId();
        // Get all the pickListList where status equals to statusId
        defaultPickListShouldBeFound("statusId.equals=" + statusId);

        // Get all the pickListList where status equals to (statusId + 1)
        defaultPickListShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllPickListsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            pickListRepository.saveAndFlush(pickList);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        pickList.setParty(party);
        pickListRepository.saveAndFlush(pickList);
        Long partyId = party.getId();
        // Get all the pickListList where party equals to partyId
        defaultPickListShouldBeFound("partyId.equals=" + partyId);

        // Get all the pickListList where party equals to (partyId + 1)
        defaultPickListShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    private void defaultPickListFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPickListShouldBeFound(shouldBeFound);
        defaultPickListShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPickListShouldBeFound(String filter) throws Exception {
        restPickListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickList.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pickListDate").value(hasItem(DEFAULT_PICK_LIST_DATE.toString())));

        // Check, that the count call also returns 1
        restPickListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPickListShouldNotBeFound(String filter) throws Exception {
        restPickListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPickListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPickList() throws Exception {
        // Get the pickList
        restPickListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPickList() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickList
        PickList updatedPickList = pickListRepository.findById(pickList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPickList are not directly saved in db
        em.detach(updatedPickList);
        updatedPickList.description(UPDATED_DESCRIPTION).pickListDate(UPDATED_PICK_LIST_DATE);

        restPickListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPickList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPickList))
            )
            .andExpect(status().isOk());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPickListToMatchAllProperties(updatedPickList);
    }

    @Test
    @Transactional
    void putNonExistingPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pickList.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pickList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePickListWithPatch() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickList using partial update
        PickList partialUpdatedPickList = new PickList();
        partialUpdatedPickList.setId(pickList.getId());

        partialUpdatedPickList.pickListDate(UPDATED_PICK_LIST_DATE);

        restPickListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPickList))
            )
            .andExpect(status().isOk());

        // Validate the PickList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPickListUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPickList, pickList), getPersistedPickList(pickList));
    }

    @Test
    @Transactional
    void fullUpdatePickListWithPatch() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickList using partial update
        PickList partialUpdatedPickList = new PickList();
        partialUpdatedPickList.setId(pickList.getId());

        partialUpdatedPickList.description(UPDATED_DESCRIPTION).pickListDate(UPDATED_PICK_LIST_DATE);

        restPickListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPickList))
            )
            .andExpect(status().isOk());

        // Validate the PickList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPickListUpdatableFieldsEquals(partialUpdatedPickList, getPersistedPickList(partialUpdatedPickList));
    }

    @Test
    @Transactional
    void patchNonExistingPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pickList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pickList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pickList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPickList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pickList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePickList() throws Exception {
        // Initialize the database
        insertedPickList = pickListRepository.saveAndFlush(pickList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pickList
        restPickListMockMvc
            .perform(delete(ENTITY_API_URL_ID, pickList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pickListRepository.count();
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

    protected PickList getPersistedPickList(PickList pickList) {
        return pickListRepository.findById(pickList.getId()).orElseThrow();
    }

    protected void assertPersistedPickListToMatchAllProperties(PickList expectedPickList) {
        assertPickListAllPropertiesEquals(expectedPickList, getPersistedPickList(expectedPickList));
    }

    protected void assertPersistedPickListToMatchUpdatableProperties(PickList expectedPickList) {
        assertPickListAllUpdatablePropertiesEquals(expectedPickList, getPersistedPickList(expectedPickList));
    }
}
