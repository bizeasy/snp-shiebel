package com.snp.web.rest;

import static com.snp.domain.FacilityGroupMemberAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityGroup;
import com.snp.domain.FacilityGroupMember;
import com.snp.repository.FacilityGroupMemberRepository;
import com.snp.service.FacilityGroupMemberService;
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
 * Integration tests for the {@link FacilityGroupMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityGroupMemberResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/facility-group-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityGroupMemberRepository facilityGroupMemberRepository;

    @Mock
    private FacilityGroupMemberRepository facilityGroupMemberRepositoryMock;

    @Mock
    private FacilityGroupMemberService facilityGroupMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityGroupMemberMockMvc;

    private FacilityGroupMember facilityGroupMember;

    private FacilityGroupMember insertedFacilityGroupMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroupMember createEntity(EntityManager em) {
        FacilityGroupMember facilityGroupMember = new FacilityGroupMember()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        facilityGroupMember.setFacility(facility);
        // Add required entity
        FacilityGroup facilityGroup;
        if (TestUtil.findAll(em, FacilityGroup.class).isEmpty()) {
            facilityGroup = FacilityGroupResourceIT.createEntity();
            em.persist(facilityGroup);
            em.flush();
        } else {
            facilityGroup = TestUtil.findAll(em, FacilityGroup.class).getFirst();
        }
        facilityGroupMember.setFacilityGroup(facilityGroup);
        return facilityGroupMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityGroupMember createUpdatedEntity(EntityManager em) {
        FacilityGroupMember updatedFacilityGroupMember = new FacilityGroupMember()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedFacilityGroupMember.setFacility(facility);
        // Add required entity
        FacilityGroup facilityGroup;
        if (TestUtil.findAll(em, FacilityGroup.class).isEmpty()) {
            facilityGroup = FacilityGroupResourceIT.createUpdatedEntity();
            em.persist(facilityGroup);
            em.flush();
        } else {
            facilityGroup = TestUtil.findAll(em, FacilityGroup.class).getFirst();
        }
        updatedFacilityGroupMember.setFacilityGroup(facilityGroup);
        return updatedFacilityGroupMember;
    }

    @BeforeEach
    void initTest() {
        facilityGroupMember = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityGroupMember != null) {
            facilityGroupMemberRepository.delete(insertedFacilityGroupMember);
            insertedFacilityGroupMember = null;
        }
    }

    @Test
    @Transactional
    void createFacilityGroupMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityGroupMember
        var returnedFacilityGroupMember = om.readValue(
            restFacilityGroupMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupMember)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityGroupMember.class
        );

        // Validate the FacilityGroupMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityGroupMemberUpdatableFieldsEquals(
            returnedFacilityGroupMember,
            getPersistedFacilityGroupMember(returnedFacilityGroupMember)
        );

        insertedFacilityGroupMember = returnedFacilityGroupMember;
    }

    @Test
    @Transactional
    void createFacilityGroupMemberWithExistingId() throws Exception {
        // Create the FacilityGroupMember with an existing ID
        facilityGroupMember.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityGroupMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupMember)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityGroupMember.setFromDate(null);

        // Create the FacilityGroupMember, which fails.

        restFacilityGroupMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupMember)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembers() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityGroupMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityGroupMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityGroupMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityGroupMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityGroupMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityGroupMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityGroupMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityGroupMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityGroupMember() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get the facilityGroupMember
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityGroupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityGroupMember.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getFacilityGroupMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        Long id = facilityGroupMember.getId();

        defaultFacilityGroupMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityGroupMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityGroupMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where fromDate equals to
        defaultFacilityGroupMemberFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where fromDate in
        defaultFacilityGroupMemberFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where fromDate is not null
        defaultFacilityGroupMemberFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where thruDate equals to
        defaultFacilityGroupMemberFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where thruDate in
        defaultFacilityGroupMemberFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where thruDate is not null
        defaultFacilityGroupMemberFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo equals to
        defaultFacilityGroupMemberFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo in
        defaultFacilityGroupMemberFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo is not null
        defaultFacilityGroupMemberFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo is greater than or equal to
        defaultFacilityGroupMemberFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo is less than or equal to
        defaultFacilityGroupMemberFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo is less than
        defaultFacilityGroupMemberFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        // Get all the facilityGroupMemberList where sequenceNo is greater than
        defaultFacilityGroupMemberFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        facilityGroupMember.setFacility(facility);
        facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);
        Long facilityId = facility.getId();
        // Get all the facilityGroupMemberList where facility equals to facilityId
        defaultFacilityGroupMemberShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the facilityGroupMemberList where facility equals to (facilityId + 1)
        defaultFacilityGroupMemberShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityGroupMembersByFacilityGroupIsEqualToSomething() throws Exception {
        FacilityGroup facilityGroup;
        if (TestUtil.findAll(em, FacilityGroup.class).isEmpty()) {
            facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);
            facilityGroup = FacilityGroupResourceIT.createEntity();
        } else {
            facilityGroup = TestUtil.findAll(em, FacilityGroup.class).getFirst();
        }
        em.persist(facilityGroup);
        em.flush();
        facilityGroupMember.setFacilityGroup(facilityGroup);
        facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);
        Long facilityGroupId = facilityGroup.getId();
        // Get all the facilityGroupMemberList where facilityGroup equals to facilityGroupId
        defaultFacilityGroupMemberShouldBeFound("facilityGroupId.equals=" + facilityGroupId);

        // Get all the facilityGroupMemberList where facilityGroup equals to (facilityGroupId + 1)
        defaultFacilityGroupMemberShouldNotBeFound("facilityGroupId.equals=" + (facilityGroupId + 1));
    }

    private void defaultFacilityGroupMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityGroupMemberShouldBeFound(shouldBeFound);
        defaultFacilityGroupMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityGroupMemberShouldBeFound(String filter) throws Exception {
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityGroupMemberShouldNotBeFound(String filter) throws Exception {
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityGroupMember() throws Exception {
        // Get the facilityGroupMember
        restFacilityGroupMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityGroupMember() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupMember
        FacilityGroupMember updatedFacilityGroupMember = facilityGroupMemberRepository.findById(facilityGroupMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityGroupMember are not directly saved in db
        em.detach(updatedFacilityGroupMember);
        updatedFacilityGroupMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restFacilityGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityGroupMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityGroupMember))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityGroupMemberToMatchAllProperties(updatedFacilityGroupMember);
    }

    @Test
    @Transactional
    void putNonExistingFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityGroupMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroupMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityGroupMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityGroupMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityGroupMemberWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupMember using partial update
        FacilityGroupMember partialUpdatedFacilityGroupMember = new FacilityGroupMember();
        partialUpdatedFacilityGroupMember.setId(facilityGroupMember.getId());

        partialUpdatedFacilityGroupMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroupMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroupMember))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityGroupMember, facilityGroupMember),
            getPersistedFacilityGroupMember(facilityGroupMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityGroupMemberWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityGroupMember using partial update
        FacilityGroupMember partialUpdatedFacilityGroupMember = new FacilityGroupMember();
        partialUpdatedFacilityGroupMember.setId(facilityGroupMember.getId());

        partialUpdatedFacilityGroupMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restFacilityGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityGroupMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityGroupMember))
            )
            .andExpect(status().isOk());

        // Validate the FacilityGroupMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityGroupMemberUpdatableFieldsEquals(
            partialUpdatedFacilityGroupMember,
            getPersistedFacilityGroupMember(partialUpdatedFacilityGroupMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityGroupMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroupMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityGroupMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityGroupMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityGroupMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityGroupMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityGroupMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityGroupMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityGroupMember() throws Exception {
        // Initialize the database
        insertedFacilityGroupMember = facilityGroupMemberRepository.saveAndFlush(facilityGroupMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityGroupMember
        restFacilityGroupMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityGroupMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityGroupMemberRepository.count();
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

    protected FacilityGroupMember getPersistedFacilityGroupMember(FacilityGroupMember facilityGroupMember) {
        return facilityGroupMemberRepository.findById(facilityGroupMember.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityGroupMemberToMatchAllProperties(FacilityGroupMember expectedFacilityGroupMember) {
        assertFacilityGroupMemberAllPropertiesEquals(
            expectedFacilityGroupMember,
            getPersistedFacilityGroupMember(expectedFacilityGroupMember)
        );
    }

    protected void assertPersistedFacilityGroupMemberToMatchUpdatableProperties(FacilityGroupMember expectedFacilityGroupMember) {
        assertFacilityGroupMemberAllUpdatablePropertiesEquals(
            expectedFacilityGroupMember,
            getPersistedFacilityGroupMember(expectedFacilityGroupMember)
        );
    }
}
