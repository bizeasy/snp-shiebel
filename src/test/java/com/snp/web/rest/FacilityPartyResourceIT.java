package com.snp.web.rest;

import static com.snp.domain.FacilityPartyAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityParty;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.repository.FacilityPartyRepository;
import com.snp.service.FacilityPartyService;
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
 * Integration tests for the {@link FacilityPartyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityPartyResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/facility-parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityPartyRepository facilityPartyRepository;

    @Mock
    private FacilityPartyRepository facilityPartyRepositoryMock;

    @Mock
    private FacilityPartyService facilityPartyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityPartyMockMvc;

    private FacilityParty facilityParty;

    private FacilityParty insertedFacilityParty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityParty createEntity(EntityManager em) {
        FacilityParty facilityParty = new FacilityParty().fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        facilityParty.setFacility(facility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        facilityParty.setParty(party);
        return facilityParty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityParty createUpdatedEntity(EntityManager em) {
        FacilityParty updatedFacilityParty = new FacilityParty().fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedFacilityParty.setFacility(facility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedFacilityParty.setParty(party);
        return updatedFacilityParty;
    }

    @BeforeEach
    void initTest() {
        facilityParty = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityParty != null) {
            facilityPartyRepository.delete(insertedFacilityParty);
            insertedFacilityParty = null;
        }
    }

    @Test
    @Transactional
    void createFacilityParty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityParty
        var returnedFacilityParty = om.readValue(
            restFacilityPartyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityParty)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityParty.class
        );

        // Validate the FacilityParty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityPartyUpdatableFieldsEquals(returnedFacilityParty, getPersistedFacilityParty(returnedFacilityParty));

        insertedFacilityParty = returnedFacilityParty;
    }

    @Test
    @Transactional
    void createFacilityPartyWithExistingId() throws Exception {
        // Create the FacilityParty with an existing ID
        facilityParty.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityParty)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityParty.setFromDate(null);

        // Create the FacilityParty, which fails.

        restFacilityPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityParty)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityParties() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityParty.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityPartiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityPartyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityPartyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityPartyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityPartiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityPartyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityPartyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityPartyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityParty() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get the facilityParty
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityParty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityParty.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getFacilityPartiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        Long id = facilityParty.getId();

        defaultFacilityPartyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityPartyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityPartyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where fromDate equals to
        defaultFacilityPartyFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where fromDate in
        defaultFacilityPartyFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where fromDate is not null
        defaultFacilityPartyFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where thruDate equals to
        defaultFacilityPartyFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where thruDate in
        defaultFacilityPartyFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        // Get all the facilityPartyList where thruDate is not null
        defaultFacilityPartyFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityPartyRepository.saveAndFlush(facilityParty);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        facilityParty.setFacility(facility);
        facilityPartyRepository.saveAndFlush(facilityParty);
        Long facilityId = facility.getId();
        // Get all the facilityPartyList where facility equals to facilityId
        defaultFacilityPartyShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the facilityPartyList where facility equals to (facilityId + 1)
        defaultFacilityPartyShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            facilityPartyRepository.saveAndFlush(facilityParty);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        facilityParty.setParty(party);
        facilityPartyRepository.saveAndFlush(facilityParty);
        Long partyId = party.getId();
        // Get all the facilityPartyList where party equals to partyId
        defaultFacilityPartyShouldBeFound("partyId.equals=" + partyId);

        // Get all the facilityPartyList where party equals to (partyId + 1)
        defaultFacilityPartyShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityPartiesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            facilityPartyRepository.saveAndFlush(facilityParty);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        facilityParty.setRoleType(roleType);
        facilityPartyRepository.saveAndFlush(facilityParty);
        Long roleTypeId = roleType.getId();
        // Get all the facilityPartyList where roleType equals to roleTypeId
        defaultFacilityPartyShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the facilityPartyList where roleType equals to (roleTypeId + 1)
        defaultFacilityPartyShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultFacilityPartyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityPartyShouldBeFound(shouldBeFound);
        defaultFacilityPartyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityPartyShouldBeFound(String filter) throws Exception {
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityParty.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityPartyShouldNotBeFound(String filter) throws Exception {
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityParty() throws Exception {
        // Get the facilityParty
        restFacilityPartyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityParty() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityParty
        FacilityParty updatedFacilityParty = facilityPartyRepository.findById(facilityParty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityParty are not directly saved in db
        em.detach(updatedFacilityParty);
        updatedFacilityParty.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityParty))
            )
            .andExpect(status().isOk());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityPartyToMatchAllProperties(updatedFacilityParty);
    }

    @Test
    @Transactional
    void putNonExistingFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityParty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityPartyWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityParty using partial update
        FacilityParty partialUpdatedFacilityParty = new FacilityParty();
        partialUpdatedFacilityParty.setId(facilityParty.getId());

        partialUpdatedFacilityParty.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityParty))
            )
            .andExpect(status().isOk());

        // Validate the FacilityParty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityPartyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityParty, facilityParty),
            getPersistedFacilityParty(facilityParty)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityPartyWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityParty using partial update
        FacilityParty partialUpdatedFacilityParty = new FacilityParty();
        partialUpdatedFacilityParty.setId(facilityParty.getId());

        partialUpdatedFacilityParty.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityParty))
            )
            .andExpect(status().isOk());

        // Validate the FacilityParty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityPartyUpdatableFieldsEquals(partialUpdatedFacilityParty, getPersistedFacilityParty(partialUpdatedFacilityParty));
    }

    @Test
    @Transactional
    void patchNonExistingFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityParty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityPartyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityParty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityParty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityParty() throws Exception {
        // Initialize the database
        insertedFacilityParty = facilityPartyRepository.saveAndFlush(facilityParty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityParty
        restFacilityPartyMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityParty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityPartyRepository.count();
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

    protected FacilityParty getPersistedFacilityParty(FacilityParty facilityParty) {
        return facilityPartyRepository.findById(facilityParty.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityPartyToMatchAllProperties(FacilityParty expectedFacilityParty) {
        assertFacilityPartyAllPropertiesEquals(expectedFacilityParty, getPersistedFacilityParty(expectedFacilityParty));
    }

    protected void assertPersistedFacilityPartyToMatchUpdatableProperties(FacilityParty expectedFacilityParty) {
        assertFacilityPartyAllUpdatablePropertiesEquals(expectedFacilityParty, getPersistedFacilityParty(expectedFacilityParty));
    }
}
