package com.snp.web.rest;

import static com.snp.domain.InventoryTransferAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityLocation;
import com.snp.domain.InventoryItem;
import com.snp.domain.InventoryTransfer;
import com.snp.domain.ItemIssuance;
import com.snp.domain.Status;
import com.snp.repository.InventoryTransferRepository;
import com.snp.service.InventoryTransferService;
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
 * Integration tests for the {@link InventoryTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventoryTransferResourceIT {

    private static final Instant DEFAULT_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventory-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryTransferRepository inventoryTransferRepository;

    @Mock
    private InventoryTransferRepository inventoryTransferRepositoryMock;

    @Mock
    private InventoryTransferService inventoryTransferServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryTransferMockMvc;

    private InventoryTransfer inventoryTransfer;

    private InventoryTransfer insertedInventoryTransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryTransfer createEntity(EntityManager em) {
        InventoryTransfer inventoryTransfer = new InventoryTransfer()
            .sentDate(DEFAULT_SENT_DATE)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        inventoryTransfer.setInventoryItem(inventoryItem);
        return inventoryTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryTransfer createUpdatedEntity(EntityManager em) {
        InventoryTransfer updatedInventoryTransfer = new InventoryTransfer()
            .sentDate(UPDATED_SENT_DATE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createUpdatedEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        updatedInventoryTransfer.setInventoryItem(inventoryItem);
        return updatedInventoryTransfer;
    }

    @BeforeEach
    void initTest() {
        inventoryTransfer = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryTransfer != null) {
            inventoryTransferRepository.delete(insertedInventoryTransfer);
            insertedInventoryTransfer = null;
        }
    }

    @Test
    @Transactional
    void createInventoryTransfer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryTransfer
        var returnedInventoryTransfer = om.readValue(
            restInventoryTransferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryTransfer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryTransfer.class
        );

        // Validate the InventoryTransfer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventoryTransferUpdatableFieldsEquals(returnedInventoryTransfer, getPersistedInventoryTransfer(returnedInventoryTransfer));

        insertedInventoryTransfer = returnedInventoryTransfer;
    }

    @Test
    @Transactional
    void createInventoryTransferWithExistingId() throws Exception {
        // Create the InventoryTransfer with an existing ID
        inventoryTransfer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventoryTransfers() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoryTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventoryTransferServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventoryTransferServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoryTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventoryTransferServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventoryTransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventoryTransfer() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get the inventoryTransfer
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryTransfer.getId().intValue()))
            .andExpect(jsonPath("$.sentDate").value(DEFAULT_SENT_DATE.toString()))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getInventoryTransfersByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        Long id = inventoryTransfer.getId();

        defaultInventoryTransferFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryTransferFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryTransferFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersBySentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where sentDate equals to
        defaultInventoryTransferFiltering("sentDate.equals=" + DEFAULT_SENT_DATE, "sentDate.equals=" + UPDATED_SENT_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersBySentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where sentDate in
        defaultInventoryTransferFiltering("sentDate.in=" + DEFAULT_SENT_DATE + "," + UPDATED_SENT_DATE, "sentDate.in=" + UPDATED_SENT_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersBySentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where sentDate is not null
        defaultInventoryTransferFiltering("sentDate.specified=true", "sentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where receivedDate equals to
        defaultInventoryTransferFiltering("receivedDate.equals=" + DEFAULT_RECEIVED_DATE, "receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where receivedDate in
        defaultInventoryTransferFiltering(
            "receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE,
            "receivedDate.in=" + UPDATED_RECEIVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where receivedDate is not null
        defaultInventoryTransferFiltering("receivedDate.specified=true", "receivedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where comments equals to
        defaultInventoryTransferFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where comments in
        defaultInventoryTransferFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where comments is not null
        defaultInventoryTransferFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where comments contains
        defaultInventoryTransferFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        // Get all the inventoryTransferList where comments does not contain
        defaultInventoryTransferFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        inventoryTransfer.setInventoryItem(inventoryItem);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the inventoryTransferList where inventoryItem equals to inventoryItemId
        defaultInventoryTransferShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the inventoryTransferList where inventoryItem equals to (inventoryItemId + 1)
        defaultInventoryTransferShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        inventoryTransfer.setStatus(status);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long statusId = status.getId();
        // Get all the inventoryTransferList where status equals to statusId
        defaultInventoryTransferShouldBeFound("statusId.equals=" + statusId);

        // Get all the inventoryTransferList where status equals to (statusId + 1)
        defaultInventoryTransferShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        inventoryTransfer.setFacility(facility);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long facilityId = facility.getId();
        // Get all the inventoryTransferList where facility equals to facilityId
        defaultInventoryTransferShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the inventoryTransferList where facility equals to (facilityId + 1)
        defaultInventoryTransferShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByFacilityLocationIsEqualToSomething() throws Exception {
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            facilityLocation = FacilityLocationResourceIT.createEntity(em);
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        em.persist(facilityLocation);
        em.flush();
        inventoryTransfer.setFacilityLocation(facilityLocation);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long facilityLocationId = facilityLocation.getId();
        // Get all the inventoryTransferList where facilityLocation equals to facilityLocationId
        defaultInventoryTransferShouldBeFound("facilityLocationId.equals=" + facilityLocationId);

        // Get all the inventoryTransferList where facilityLocation equals to (facilityLocationId + 1)
        defaultInventoryTransferShouldNotBeFound("facilityLocationId.equals=" + (facilityLocationId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByFacilityToIsEqualToSomething() throws Exception {
        Facility facilityTo;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            facilityTo = FacilityResourceIT.createEntity();
        } else {
            facilityTo = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facilityTo);
        em.flush();
        inventoryTransfer.setFacilityTo(facilityTo);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long facilityToId = facilityTo.getId();
        // Get all the inventoryTransferList where facilityTo equals to facilityToId
        defaultInventoryTransferShouldBeFound("facilityToId.equals=" + facilityToId);

        // Get all the inventoryTransferList where facilityTo equals to (facilityToId + 1)
        defaultInventoryTransferShouldNotBeFound("facilityToId.equals=" + (facilityToId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByFacilityLocationToIsEqualToSomething() throws Exception {
        FacilityLocation facilityLocationTo;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            facilityLocationTo = FacilityLocationResourceIT.createEntity(em);
        } else {
            facilityLocationTo = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        em.persist(facilityLocationTo);
        em.flush();
        inventoryTransfer.setFacilityLocationTo(facilityLocationTo);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long facilityLocationToId = facilityLocationTo.getId();
        // Get all the inventoryTransferList where facilityLocationTo equals to facilityLocationToId
        defaultInventoryTransferShouldBeFound("facilityLocationToId.equals=" + facilityLocationToId);

        // Get all the inventoryTransferList where facilityLocationTo equals to (facilityLocationToId + 1)
        defaultInventoryTransferShouldNotBeFound("facilityLocationToId.equals=" + (facilityLocationToId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryTransfersByIssuanceIsEqualToSomething() throws Exception {
        ItemIssuance issuance;
        if (TestUtil.findAll(em, ItemIssuance.class).isEmpty()) {
            inventoryTransferRepository.saveAndFlush(inventoryTransfer);
            issuance = ItemIssuanceResourceIT.createEntity(em);
        } else {
            issuance = TestUtil.findAll(em, ItemIssuance.class).getFirst();
        }
        em.persist(issuance);
        em.flush();
        inventoryTransfer.setIssuance(issuance);
        inventoryTransferRepository.saveAndFlush(inventoryTransfer);
        Long issuanceId = issuance.getId();
        // Get all the inventoryTransferList where issuance equals to issuanceId
        defaultInventoryTransferShouldBeFound("issuanceId.equals=" + issuanceId);

        // Get all the inventoryTransferList where issuance equals to (issuanceId + 1)
        defaultInventoryTransferShouldNotBeFound("issuanceId.equals=" + (issuanceId + 1));
    }

    private void defaultInventoryTransferFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryTransferShouldBeFound(shouldBeFound);
        defaultInventoryTransferShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryTransferShouldBeFound(String filter) throws Exception {
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryTransferShouldNotBeFound(String filter) throws Exception {
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryTransferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryTransfer() throws Exception {
        // Get the inventoryTransfer
        restInventoryTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryTransfer() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryTransfer
        InventoryTransfer updatedInventoryTransfer = inventoryTransferRepository.findById(inventoryTransfer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryTransfer are not directly saved in db
        em.detach(updatedInventoryTransfer);
        updatedInventoryTransfer.sentDate(UPDATED_SENT_DATE).receivedDate(UPDATED_RECEIVED_DATE).comments(UPDATED_COMMENTS);

        restInventoryTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventoryTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventoryTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryTransferToMatchAllProperties(updatedInventoryTransfer);
    }

    @Test
    @Transactional
    void putNonExistingInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryTransfer using partial update
        InventoryTransfer partialUpdatedInventoryTransfer = new InventoryTransfer();
        partialUpdatedInventoryTransfer.setId(inventoryTransfer.getId());

        partialUpdatedInventoryTransfer.sentDate(UPDATED_SENT_DATE).comments(UPDATED_COMMENTS);

        restInventoryTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryTransferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryTransfer, inventoryTransfer),
            getPersistedInventoryTransfer(inventoryTransfer)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryTransfer using partial update
        InventoryTransfer partialUpdatedInventoryTransfer = new InventoryTransfer();
        partialUpdatedInventoryTransfer.setId(inventoryTransfer.getId());

        partialUpdatedInventoryTransfer.sentDate(UPDATED_SENT_DATE).receivedDate(UPDATED_RECEIVED_DATE).comments(UPDATED_COMMENTS);

        restInventoryTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryTransferUpdatableFieldsEquals(
            partialUpdatedInventoryTransfer,
            getPersistedInventoryTransfer(partialUpdatedInventoryTransfer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryTransfer() throws Exception {
        // Initialize the database
        insertedInventoryTransfer = inventoryTransferRepository.saveAndFlush(inventoryTransfer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryTransfer
        restInventoryTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryTransferRepository.count();
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

    protected InventoryTransfer getPersistedInventoryTransfer(InventoryTransfer inventoryTransfer) {
        return inventoryTransferRepository.findById(inventoryTransfer.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryTransferToMatchAllProperties(InventoryTransfer expectedInventoryTransfer) {
        assertInventoryTransferAllPropertiesEquals(expectedInventoryTransfer, getPersistedInventoryTransfer(expectedInventoryTransfer));
    }

    protected void assertPersistedInventoryTransferToMatchUpdatableProperties(InventoryTransfer expectedInventoryTransfer) {
        assertInventoryTransferAllUpdatablePropertiesEquals(
            expectedInventoryTransfer,
            getPersistedInventoryTransfer(expectedInventoryTransfer)
        );
    }
}
