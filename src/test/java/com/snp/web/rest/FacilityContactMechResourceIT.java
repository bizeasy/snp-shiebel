package com.snp.web.rest;

import static com.snp.domain.FacilityContactMechAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.ContactMechPurpose;
import com.snp.domain.Facility;
import com.snp.domain.FacilityContactMech;
import com.snp.repository.FacilityContactMechRepository;
import com.snp.service.FacilityContactMechService;
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
 * Integration tests for the {@link FacilityContactMechResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityContactMechResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/facility-contact-meches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityContactMechRepository facilityContactMechRepository;

    @Mock
    private FacilityContactMechRepository facilityContactMechRepositoryMock;

    @Mock
    private FacilityContactMechService facilityContactMechServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityContactMechMockMvc;

    private FacilityContactMech facilityContactMech;

    private FacilityContactMech insertedFacilityContactMech;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityContactMech createEntity(EntityManager em) {
        FacilityContactMech facilityContactMech = new FacilityContactMech().fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        facilityContactMech.setFacility(facility);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        facilityContactMech.setContactMech(contactMech);
        return facilityContactMech;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityContactMech createUpdatedEntity(EntityManager em) {
        FacilityContactMech updatedFacilityContactMech = new FacilityContactMech().fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedFacilityContactMech.setFacility(facility);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createUpdatedEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        updatedFacilityContactMech.setContactMech(contactMech);
        return updatedFacilityContactMech;
    }

    @BeforeEach
    void initTest() {
        facilityContactMech = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityContactMech != null) {
            facilityContactMechRepository.delete(insertedFacilityContactMech);
            insertedFacilityContactMech = null;
        }
    }

    @Test
    @Transactional
    void createFacilityContactMech() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityContactMech
        var returnedFacilityContactMech = om.readValue(
            restFacilityContactMechMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityContactMech)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityContactMech.class
        );

        // Validate the FacilityContactMech in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityContactMechUpdatableFieldsEquals(
            returnedFacilityContactMech,
            getPersistedFacilityContactMech(returnedFacilityContactMech)
        );

        insertedFacilityContactMech = returnedFacilityContactMech;
    }

    @Test
    @Transactional
    void createFacilityContactMechWithExistingId() throws Exception {
        // Create the FacilityContactMech with an existing ID
        facilityContactMech.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityContactMech)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityContactMech.setFromDate(null);

        // Create the FacilityContactMech, which fails.

        restFacilityContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityContactMech)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityContactMeches() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityContactMechesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityContactMechServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityContactMechesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityContactMechRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityContactMech() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get the facilityContactMech
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityContactMech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityContactMech.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getFacilityContactMechesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        Long id = facilityContactMech.getId();

        defaultFacilityContactMechFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityContactMechFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityContactMechFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where fromDate equals to
        defaultFacilityContactMechFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where fromDate in
        defaultFacilityContactMechFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where fromDate is not null
        defaultFacilityContactMechFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where thruDate equals to
        defaultFacilityContactMechFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where thruDate in
        defaultFacilityContactMechFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        // Get all the facilityContactMechList where thruDate is not null
        defaultFacilityContactMechFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityContactMechRepository.saveAndFlush(facilityContactMech);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        facilityContactMech.setFacility(facility);
        facilityContactMechRepository.saveAndFlush(facilityContactMech);
        Long facilityId = facility.getId();
        // Get all the facilityContactMechList where facility equals to facilityId
        defaultFacilityContactMechShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the facilityContactMechList where facility equals to (facilityId + 1)
        defaultFacilityContactMechShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            facilityContactMechRepository.saveAndFlush(facilityContactMech);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        facilityContactMech.setContactMech(contactMech);
        facilityContactMechRepository.saveAndFlush(facilityContactMech);
        Long contactMechId = contactMech.getId();
        // Get all the facilityContactMechList where contactMech equals to contactMechId
        defaultFacilityContactMechShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the facilityContactMechList where contactMech equals to (contactMechId + 1)
        defaultFacilityContactMechShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityContactMechesByContactMechPurposeIsEqualToSomething() throws Exception {
        ContactMechPurpose contactMechPurpose;
        if (TestUtil.findAll(em, ContactMechPurpose.class).isEmpty()) {
            facilityContactMechRepository.saveAndFlush(facilityContactMech);
            contactMechPurpose = ContactMechPurposeResourceIT.createEntity();
        } else {
            contactMechPurpose = TestUtil.findAll(em, ContactMechPurpose.class).getFirst();
        }
        em.persist(contactMechPurpose);
        em.flush();
        facilityContactMech.setContactMechPurpose(contactMechPurpose);
        facilityContactMechRepository.saveAndFlush(facilityContactMech);
        Long contactMechPurposeId = contactMechPurpose.getId();
        // Get all the facilityContactMechList where contactMechPurpose equals to contactMechPurposeId
        defaultFacilityContactMechShouldBeFound("contactMechPurposeId.equals=" + contactMechPurposeId);

        // Get all the facilityContactMechList where contactMechPurpose equals to (contactMechPurposeId + 1)
        defaultFacilityContactMechShouldNotBeFound("contactMechPurposeId.equals=" + (contactMechPurposeId + 1));
    }

    private void defaultFacilityContactMechFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityContactMechShouldBeFound(shouldBeFound);
        defaultFacilityContactMechShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityContactMechShouldBeFound(String filter) throws Exception {
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityContactMechShouldNotBeFound(String filter) throws Exception {
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityContactMech() throws Exception {
        // Get the facilityContactMech
        restFacilityContactMechMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityContactMech() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityContactMech
        FacilityContactMech updatedFacilityContactMech = facilityContactMechRepository.findById(facilityContactMech.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityContactMech are not directly saved in db
        em.detach(updatedFacilityContactMech);
        updatedFacilityContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityContactMech))
            )
            .andExpect(status().isOk());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityContactMechToMatchAllProperties(updatedFacilityContactMech);
    }

    @Test
    @Transactional
    void putNonExistingFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityContactMech using partial update
        FacilityContactMech partialUpdatedFacilityContactMech = new FacilityContactMech();
        partialUpdatedFacilityContactMech.setId(facilityContactMech.getId());

        partialUpdatedFacilityContactMech.fromDate(UPDATED_FROM_DATE);

        restFacilityContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityContactMech))
            )
            .andExpect(status().isOk());

        // Validate the FacilityContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityContactMechUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityContactMech, facilityContactMech),
            getPersistedFacilityContactMech(facilityContactMech)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityContactMech using partial update
        FacilityContactMech partialUpdatedFacilityContactMech = new FacilityContactMech();
        partialUpdatedFacilityContactMech.setId(facilityContactMech.getId());

        partialUpdatedFacilityContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restFacilityContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityContactMech))
            )
            .andExpect(status().isOk());

        // Validate the FacilityContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityContactMechUpdatableFieldsEquals(
            partialUpdatedFacilityContactMech,
            getPersistedFacilityContactMech(partialUpdatedFacilityContactMech)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityContactMechMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityContactMech() throws Exception {
        // Initialize the database
        insertedFacilityContactMech = facilityContactMechRepository.saveAndFlush(facilityContactMech);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityContactMech
        restFacilityContactMechMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityContactMech.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityContactMechRepository.count();
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

    protected FacilityContactMech getPersistedFacilityContactMech(FacilityContactMech facilityContactMech) {
        return facilityContactMechRepository.findById(facilityContactMech.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityContactMechToMatchAllProperties(FacilityContactMech expectedFacilityContactMech) {
        assertFacilityContactMechAllPropertiesEquals(
            expectedFacilityContactMech,
            getPersistedFacilityContactMech(expectedFacilityContactMech)
        );
    }

    protected void assertPersistedFacilityContactMechToMatchUpdatableProperties(FacilityContactMech expectedFacilityContactMech) {
        assertFacilityContactMechAllUpdatablePropertiesEquals(
            expectedFacilityContactMech,
            getPersistedFacilityContactMech(expectedFacilityContactMech)
        );
    }
}
