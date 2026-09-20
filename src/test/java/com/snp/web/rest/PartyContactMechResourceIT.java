package com.snp.web.rest;

import static com.snp.domain.PartyContactMechAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.ContactMechPurpose;
import com.snp.domain.Party;
import com.snp.domain.PartyContactMech;
import com.snp.repository.PartyContactMechRepository;
import com.snp.service.PartyContactMechService;
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
 * Integration tests for the {@link PartyContactMechResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyContactMechResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/party-contact-meches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyContactMechRepository partyContactMechRepository;

    @Mock
    private PartyContactMechRepository partyContactMechRepositoryMock;

    @Mock
    private PartyContactMechService partyContactMechServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyContactMechMockMvc;

    private PartyContactMech partyContactMech;

    private PartyContactMech insertedPartyContactMech;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyContactMech createEntity(EntityManager em) {
        PartyContactMech partyContactMech = new PartyContactMech().fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        partyContactMech.setParty(party);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        partyContactMech.setContactMech(contactMech);
        return partyContactMech;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyContactMech createUpdatedEntity(EntityManager em) {
        PartyContactMech updatedPartyContactMech = new PartyContactMech().fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPartyContactMech.setParty(party);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createUpdatedEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        updatedPartyContactMech.setContactMech(contactMech);
        return updatedPartyContactMech;
    }

    @BeforeEach
    void initTest() {
        partyContactMech = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyContactMech != null) {
            partyContactMechRepository.delete(insertedPartyContactMech);
            insertedPartyContactMech = null;
        }
    }

    @Test
    @Transactional
    void createPartyContactMech() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyContactMech
        var returnedPartyContactMech = om.readValue(
            restPartyContactMechMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyContactMech)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyContactMech.class
        );

        // Validate the PartyContactMech in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyContactMechUpdatableFieldsEquals(returnedPartyContactMech, getPersistedPartyContactMech(returnedPartyContactMech));

        insertedPartyContactMech = returnedPartyContactMech;
    }

    @Test
    @Transactional
    void createPartyContactMechWithExistingId() throws Exception {
        // Create the PartyContactMech with an existing ID
        partyContactMech.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyContactMech)))
            .andExpect(status().isBadRequest());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyContactMech.setFromDate(null);

        // Create the PartyContactMech, which fails.

        restPartyContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyContactMech)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyContactMeches() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyContactMechesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyContactMechServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyContactMechesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyContactMechRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyContactMech() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get the partyContactMech
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL_ID, partyContactMech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyContactMech.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getPartyContactMechesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        Long id = partyContactMech.getId();

        defaultPartyContactMechFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyContactMechFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyContactMechFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where fromDate equals to
        defaultPartyContactMechFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where fromDate in
        defaultPartyContactMechFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where fromDate is not null
        defaultPartyContactMechFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where thruDate equals to
        defaultPartyContactMechFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where thruDate in
        defaultPartyContactMechFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        // Get all the partyContactMechList where thruDate is not null
        defaultPartyContactMechFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyContactMechRepository.saveAndFlush(partyContactMech);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        partyContactMech.setParty(party);
        partyContactMechRepository.saveAndFlush(partyContactMech);
        Long partyId = party.getId();
        // Get all the partyContactMechList where party equals to partyId
        defaultPartyContactMechShouldBeFound("partyId.equals=" + partyId);

        // Get all the partyContactMechList where party equals to (partyId + 1)
        defaultPartyContactMechShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            partyContactMechRepository.saveAndFlush(partyContactMech);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        partyContactMech.setContactMech(contactMech);
        partyContactMechRepository.saveAndFlush(partyContactMech);
        Long contactMechId = contactMech.getId();
        // Get all the partyContactMechList where contactMech equals to contactMechId
        defaultPartyContactMechShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the partyContactMechList where contactMech equals to (contactMechId + 1)
        defaultPartyContactMechShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    @Test
    @Transactional
    void getAllPartyContactMechesByContactMechPurposeIsEqualToSomething() throws Exception {
        ContactMechPurpose contactMechPurpose;
        if (TestUtil.findAll(em, ContactMechPurpose.class).isEmpty()) {
            partyContactMechRepository.saveAndFlush(partyContactMech);
            contactMechPurpose = ContactMechPurposeResourceIT.createEntity();
        } else {
            contactMechPurpose = TestUtil.findAll(em, ContactMechPurpose.class).getFirst();
        }
        em.persist(contactMechPurpose);
        em.flush();
        partyContactMech.setContactMechPurpose(contactMechPurpose);
        partyContactMechRepository.saveAndFlush(partyContactMech);
        Long contactMechPurposeId = contactMechPurpose.getId();
        // Get all the partyContactMechList where contactMechPurpose equals to contactMechPurposeId
        defaultPartyContactMechShouldBeFound("contactMechPurposeId.equals=" + contactMechPurposeId);

        // Get all the partyContactMechList where contactMechPurpose equals to (contactMechPurposeId + 1)
        defaultPartyContactMechShouldNotBeFound("contactMechPurposeId.equals=" + (contactMechPurposeId + 1));
    }

    private void defaultPartyContactMechFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyContactMechShouldBeFound(shouldBeFound);
        defaultPartyContactMechShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyContactMechShouldBeFound(String filter) throws Exception {
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyContactMechShouldNotBeFound(String filter) throws Exception {
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyContactMech() throws Exception {
        // Get the partyContactMech
        restPartyContactMechMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyContactMech() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyContactMech
        PartyContactMech updatedPartyContactMech = partyContactMechRepository.findById(partyContactMech.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartyContactMech are not directly saved in db
        em.detach(updatedPartyContactMech);
        updatedPartyContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPartyContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyContactMech))
            )
            .andExpect(status().isOk());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyContactMechToMatchAllProperties(updatedPartyContactMech);
    }

    @Test
    @Transactional
    void putNonExistingPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyContactMech using partial update
        PartyContactMech partialUpdatedPartyContactMech = new PartyContactMech();
        partialUpdatedPartyContactMech.setId(partyContactMech.getId());

        restPartyContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyContactMech))
            )
            .andExpect(status().isOk());

        // Validate the PartyContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyContactMechUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyContactMech, partyContactMech),
            getPersistedPartyContactMech(partyContactMech)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyContactMech using partial update
        PartyContactMech partialUpdatedPartyContactMech = new PartyContactMech();
        partialUpdatedPartyContactMech.setId(partyContactMech.getId());

        partialUpdatedPartyContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPartyContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyContactMech))
            )
            .andExpect(status().isOk());

        // Validate the PartyContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyContactMechUpdatableFieldsEquals(
            partialUpdatedPartyContactMech,
            getPersistedPartyContactMech(partialUpdatedPartyContactMech)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyContactMechMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyContactMech() throws Exception {
        // Initialize the database
        insertedPartyContactMech = partyContactMechRepository.saveAndFlush(partyContactMech);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyContactMech
        restPartyContactMechMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyContactMech.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyContactMechRepository.count();
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

    protected PartyContactMech getPersistedPartyContactMech(PartyContactMech partyContactMech) {
        return partyContactMechRepository.findById(partyContactMech.getId()).orElseThrow();
    }

    protected void assertPersistedPartyContactMechToMatchAllProperties(PartyContactMech expectedPartyContactMech) {
        assertPartyContactMechAllPropertiesEquals(expectedPartyContactMech, getPersistedPartyContactMech(expectedPartyContactMech));
    }

    protected void assertPersistedPartyContactMechToMatchUpdatableProperties(PartyContactMech expectedPartyContactMech) {
        assertPartyContactMechAllUpdatablePropertiesEquals(
            expectedPartyContactMech,
            getPersistedPartyContactMech(expectedPartyContactMech)
        );
    }
}
