package com.snp.web.rest;

import static com.snp.domain.PartyRelationshipAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PartyRelationship;
import com.snp.domain.PartyRelationshipType;
import com.snp.domain.RoleType;
import com.snp.repository.PartyRelationshipRepository;
import com.snp.service.PartyRelationshipService;
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
 * Integration tests for the {@link PartyRelationshipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyRelationshipResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_RELATIONSHIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyRelationshipRepository partyRelationshipRepository;

    @Mock
    private PartyRelationshipRepository partyRelationshipRepositoryMock;

    @Mock
    private PartyRelationshipService partyRelationshipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyRelationshipMockMvc;

    private PartyRelationship partyRelationship;

    private PartyRelationship insertedPartyRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationship createEntity(EntityManager em) {
        PartyRelationship partyRelationship = new PartyRelationship()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .relationshipName(DEFAULT_RELATIONSHIP_NAME);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        partyRelationship.setPartyFrom(party);
        // Add required entity
        partyRelationship.setPartyTo(party);
        return partyRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationship createUpdatedEntity(EntityManager em) {
        PartyRelationship updatedPartyRelationship = new PartyRelationship()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .relationshipName(UPDATED_RELATIONSHIP_NAME);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPartyRelationship.setPartyFrom(party);
        // Add required entity
        updatedPartyRelationship.setPartyTo(party);
        return updatedPartyRelationship;
    }

    @BeforeEach
    void initTest() {
        partyRelationship = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyRelationship != null) {
            partyRelationshipRepository.delete(insertedPartyRelationship);
            insertedPartyRelationship = null;
        }
    }

    @Test
    @Transactional
    void createPartyRelationship() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyRelationship
        var returnedPartyRelationship = om.readValue(
            restPartyRelationshipMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationship)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyRelationship.class
        );

        // Validate the PartyRelationship in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyRelationshipUpdatableFieldsEquals(returnedPartyRelationship, getPersistedPartyRelationship(returnedPartyRelationship));

        insertedPartyRelationship = returnedPartyRelationship;
    }

    @Test
    @Transactional
    void createPartyRelationshipWithExistingId() throws Exception {
        // Create the PartyRelationship with an existing ID
        partyRelationship.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationship)))
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyRelationship.setFromDate(null);

        // Create the PartyRelationship, which fails.

        restPartyRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationship)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyRelationships() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].relationshipName").value(hasItem(DEFAULT_RELATIONSHIP_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRelationshipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyRelationshipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyRelationshipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyRelationshipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRelationshipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyRelationshipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyRelationshipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyRelationshipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyRelationship() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get the partyRelationship
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, partyRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyRelationship.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.relationshipName").value(DEFAULT_RELATIONSHIP_NAME));
    }

    @Test
    @Transactional
    void getPartyRelationshipsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        Long id = partyRelationship.getId();

        defaultPartyRelationshipFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyRelationshipFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyRelationshipFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where fromDate equals to
        defaultPartyRelationshipFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where fromDate in
        defaultPartyRelationshipFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where fromDate is not null
        defaultPartyRelationshipFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where thruDate equals to
        defaultPartyRelationshipFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where thruDate in
        defaultPartyRelationshipFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where thruDate is not null
        defaultPartyRelationshipFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where relationshipName equals to
        defaultPartyRelationshipFiltering(
            "relationshipName.equals=" + DEFAULT_RELATIONSHIP_NAME,
            "relationshipName.equals=" + UPDATED_RELATIONSHIP_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where relationshipName in
        defaultPartyRelationshipFiltering(
            "relationshipName.in=" + DEFAULT_RELATIONSHIP_NAME + "," + UPDATED_RELATIONSHIP_NAME,
            "relationshipName.in=" + UPDATED_RELATIONSHIP_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where relationshipName is not null
        defaultPartyRelationshipFiltering("relationshipName.specified=true", "relationshipName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where relationshipName contains
        defaultPartyRelationshipFiltering(
            "relationshipName.contains=" + DEFAULT_RELATIONSHIP_NAME,
            "relationshipName.contains=" + UPDATED_RELATIONSHIP_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        // Get all the partyRelationshipList where relationshipName does not contain
        defaultPartyRelationshipFiltering(
            "relationshipName.doesNotContain=" + UPDATED_RELATIONSHIP_NAME,
            "relationshipName.doesNotContain=" + DEFAULT_RELATIONSHIP_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByPartyFromIsEqualToSomething() throws Exception {
        Party partyFrom;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyRelationshipRepository.saveAndFlush(partyRelationship);
            partyFrom = PartyResourceIT.createEntity();
        } else {
            partyFrom = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(partyFrom);
        em.flush();
        partyRelationship.setPartyFrom(partyFrom);
        partyRelationshipRepository.saveAndFlush(partyRelationship);
        Long partyFromId = partyFrom.getId();
        // Get all the partyRelationshipList where partyFrom equals to partyFromId
        defaultPartyRelationshipShouldBeFound("partyFromId.equals=" + partyFromId);

        // Get all the partyRelationshipList where partyFrom equals to (partyFromId + 1)
        defaultPartyRelationshipShouldNotBeFound("partyFromId.equals=" + (partyFromId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByPartyToIsEqualToSomething() throws Exception {
        Party partyTo;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyRelationshipRepository.saveAndFlush(partyRelationship);
            partyTo = PartyResourceIT.createEntity();
        } else {
            partyTo = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(partyTo);
        em.flush();
        partyRelationship.setPartyTo(partyTo);
        partyRelationshipRepository.saveAndFlush(partyRelationship);
        Long partyToId = partyTo.getId();
        // Get all the partyRelationshipList where partyTo equals to partyToId
        defaultPartyRelationshipShouldBeFound("partyToId.equals=" + partyToId);

        // Get all the partyRelationshipList where partyTo equals to (partyToId + 1)
        defaultPartyRelationshipShouldNotBeFound("partyToId.equals=" + (partyToId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRoleTypeFromIsEqualToSomething() throws Exception {
        RoleType roleTypeFrom;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            partyRelationshipRepository.saveAndFlush(partyRelationship);
            roleTypeFrom = RoleTypeResourceIT.createEntity();
        } else {
            roleTypeFrom = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleTypeFrom);
        em.flush();
        partyRelationship.setRoleTypeFrom(roleTypeFrom);
        partyRelationshipRepository.saveAndFlush(partyRelationship);
        Long roleTypeFromId = roleTypeFrom.getId();
        // Get all the partyRelationshipList where roleTypeFrom equals to roleTypeFromId
        defaultPartyRelationshipShouldBeFound("roleTypeFromId.equals=" + roleTypeFromId);

        // Get all the partyRelationshipList where roleTypeFrom equals to (roleTypeFromId + 1)
        defaultPartyRelationshipShouldNotBeFound("roleTypeFromId.equals=" + (roleTypeFromId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRoleTypeToIsEqualToSomething() throws Exception {
        RoleType roleTypeTo;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            partyRelationshipRepository.saveAndFlush(partyRelationship);
            roleTypeTo = RoleTypeResourceIT.createEntity();
        } else {
            roleTypeTo = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleTypeTo);
        em.flush();
        partyRelationship.setRoleTypeTo(roleTypeTo);
        partyRelationshipRepository.saveAndFlush(partyRelationship);
        Long roleTypeToId = roleTypeTo.getId();
        // Get all the partyRelationshipList where roleTypeTo equals to roleTypeToId
        defaultPartyRelationshipShouldBeFound("roleTypeToId.equals=" + roleTypeToId);

        // Get all the partyRelationshipList where roleTypeTo equals to (roleTypeToId + 1)
        defaultPartyRelationshipShouldNotBeFound("roleTypeToId.equals=" + (roleTypeToId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRelationshipsByRelationshipTypeIsEqualToSomething() throws Exception {
        PartyRelationshipType relationshipType;
        if (TestUtil.findAll(em, PartyRelationshipType.class).isEmpty()) {
            partyRelationshipRepository.saveAndFlush(partyRelationship);
            relationshipType = PartyRelationshipTypeResourceIT.createEntity();
        } else {
            relationshipType = TestUtil.findAll(em, PartyRelationshipType.class).getFirst();
        }
        em.persist(relationshipType);
        em.flush();
        partyRelationship.setRelationshipType(relationshipType);
        partyRelationshipRepository.saveAndFlush(partyRelationship);
        Long relationshipTypeId = relationshipType.getId();
        // Get all the partyRelationshipList where relationshipType equals to relationshipTypeId
        defaultPartyRelationshipShouldBeFound("relationshipTypeId.equals=" + relationshipTypeId);

        // Get all the partyRelationshipList where relationshipType equals to (relationshipTypeId + 1)
        defaultPartyRelationshipShouldNotBeFound("relationshipTypeId.equals=" + (relationshipTypeId + 1));
    }

    private void defaultPartyRelationshipFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyRelationshipShouldBeFound(shouldBeFound);
        defaultPartyRelationshipShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyRelationshipShouldBeFound(String filter) throws Exception {
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].relationshipName").value(hasItem(DEFAULT_RELATIONSHIP_NAME)));

        // Check, that the count call also returns 1
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyRelationshipShouldNotBeFound(String filter) throws Exception {
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyRelationship() throws Exception {
        // Get the partyRelationship
        restPartyRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyRelationship() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationship
        PartyRelationship updatedPartyRelationship = partyRelationshipRepository.findById(partyRelationship.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartyRelationship are not directly saved in db
        em.detach(updatedPartyRelationship);
        updatedPartyRelationship.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).relationshipName(UPDATED_RELATIONSHIP_NAME);

        restPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyRelationship))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyRelationshipToMatchAllProperties(updatedPartyRelationship);
    }

    @Test
    @Transactional
    void putNonExistingPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRelationship)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyRelationshipWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationship using partial update
        PartyRelationship partialUpdatedPartyRelationship = new PartyRelationship();
        partialUpdatedPartyRelationship.setId(partyRelationship.getId());

        partialUpdatedPartyRelationship.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRelationship))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationship in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRelationshipUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyRelationship, partyRelationship),
            getPersistedPartyRelationship(partyRelationship)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyRelationshipWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRelationship using partial update
        PartyRelationship partialUpdatedPartyRelationship = new PartyRelationship();
        partialUpdatedPartyRelationship.setId(partyRelationship.getId());

        partialUpdatedPartyRelationship.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).relationshipName(UPDATED_RELATIONSHIP_NAME);

        restPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRelationship))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationship in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRelationshipUpdatableFieldsEquals(
            partialUpdatedPartyRelationship,
            getPersistedPartyRelationship(partialUpdatedPartyRelationship)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyRelationship() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRelationship.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationshipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyRelationship)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationship in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyRelationship() throws Exception {
        // Initialize the database
        insertedPartyRelationship = partyRelationshipRepository.saveAndFlush(partyRelationship);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyRelationship
        restPartyRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyRelationshipRepository.count();
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

    protected PartyRelationship getPersistedPartyRelationship(PartyRelationship partyRelationship) {
        return partyRelationshipRepository.findById(partyRelationship.getId()).orElseThrow();
    }

    protected void assertPersistedPartyRelationshipToMatchAllProperties(PartyRelationship expectedPartyRelationship) {
        assertPartyRelationshipAllPropertiesEquals(expectedPartyRelationship, getPersistedPartyRelationship(expectedPartyRelationship));
    }

    protected void assertPersistedPartyRelationshipToMatchUpdatableProperties(PartyRelationship expectedPartyRelationship) {
        assertPartyRelationshipAllUpdatablePropertiesEquals(
            expectedPartyRelationship,
            getPersistedPartyRelationship(expectedPartyRelationship)
        );
    }
}
