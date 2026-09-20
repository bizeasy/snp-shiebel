package com.snp.web.rest;

import static com.snp.domain.ShipmentStatusAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentStatus;
import com.snp.domain.Status;
import com.snp.repository.ShipmentStatusRepository;
import com.snp.service.ShipmentStatusService;
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
 * Integration tests for the {@link ShipmentStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipmentStatusResourceIT {

    private static final Instant DEFAULT_STATUS_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_DATE_TIME = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/shipment-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentStatusRepository shipmentStatusRepository;

    @Mock
    private ShipmentStatusRepository shipmentStatusRepositoryMock;

    @Mock
    private ShipmentStatusService shipmentStatusServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentStatusMockMvc;

    private ShipmentStatus shipmentStatus;

    private ShipmentStatus insertedShipmentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentStatus createEntity(EntityManager em) {
        ShipmentStatus shipmentStatus = new ShipmentStatus().statusDateTime(DEFAULT_STATUS_DATE_TIME).sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        shipmentStatus.setShipment(shipment);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentStatus createUpdatedEntity(EntityManager em) {
        ShipmentStatus updatedShipmentStatus = new ShipmentStatus()
            .statusDateTime(UPDATED_STATUS_DATE_TIME)
            .sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createUpdatedEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        updatedShipmentStatus.setShipment(shipment);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        updatedShipmentStatus.setStatus(status);
        return updatedShipmentStatus;
    }

    @BeforeEach
    void initTest() {
        shipmentStatus = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentStatus != null) {
            shipmentStatusRepository.delete(insertedShipmentStatus);
            insertedShipmentStatus = null;
        }
    }

    @Test
    @Transactional
    void createShipmentStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentStatus
        var returnedShipmentStatus = om.readValue(
            restShipmentStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentStatus)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentStatus.class
        );

        // Validate the ShipmentStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentStatusUpdatableFieldsEquals(returnedShipmentStatus, getPersistedShipmentStatus(returnedShipmentStatus));

        insertedShipmentStatus = returnedShipmentStatus;
    }

    @Test
    @Transactional
    void createShipmentStatusWithExistingId() throws Exception {
        // Create the ShipmentStatus with an existing ID
        shipmentStatus.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentStatus)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentStatus.setStatusDateTime(null);

        // Create the ShipmentStatus, which fails.

        restShipmentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentStatus)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentStatuses() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusDateTime").value(hasItem(DEFAULT_STATUS_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipmentStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipmentStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipmentStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipmentStatusRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShipmentStatus() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get the shipmentStatus
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusDateTime").value(DEFAULT_STATUS_DATE_TIME.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getShipmentStatusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        Long id = shipmentStatus.getId();

        defaultShipmentStatusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentStatusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentStatusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentStatusesByStatusDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where statusDateTime equals to
        defaultShipmentStatusFiltering(
            "statusDateTime.equals=" + DEFAULT_STATUS_DATE_TIME,
            "statusDateTime.equals=" + UPDATED_STATUS_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllShipmentStatusesByStatusDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where statusDateTime in
        defaultShipmentStatusFiltering(
            "statusDateTime.in=" + DEFAULT_STATUS_DATE_TIME + "," + UPDATED_STATUS_DATE_TIME,
            "statusDateTime.in=" + UPDATED_STATUS_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllShipmentStatusesByStatusDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where statusDateTime is not null
        defaultShipmentStatusFiltering("statusDateTime.specified=true", "statusDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo equals to
        defaultShipmentStatusFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo in
        defaultShipmentStatusFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo is not null
        defaultShipmentStatusFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo is greater than or equal to
        defaultShipmentStatusFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo is less than or equal to
        defaultShipmentStatusFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo is less than
        defaultShipmentStatusFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentStatusesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        // Get all the shipmentStatusList where sequenceNo is greater than
        defaultShipmentStatusFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentStatusesByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipmentStatusRepository.saveAndFlush(shipmentStatus);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        shipmentStatus.setShipment(shipment);
        shipmentStatusRepository.saveAndFlush(shipmentStatus);
        Long shipmentId = shipment.getId();
        // Get all the shipmentStatusList where shipment equals to shipmentId
        defaultShipmentStatusShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the shipmentStatusList where shipment equals to (shipmentId + 1)
        defaultShipmentStatusShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentStatusesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            shipmentStatusRepository.saveAndFlush(shipmentStatus);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        shipmentStatus.setStatus(status);
        shipmentStatusRepository.saveAndFlush(shipmentStatus);
        Long statusId = status.getId();
        // Get all the shipmentStatusList where status equals to statusId
        defaultShipmentStatusShouldBeFound("statusId.equals=" + statusId);

        // Get all the shipmentStatusList where status equals to (statusId + 1)
        defaultShipmentStatusShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultShipmentStatusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentStatusShouldBeFound(shouldBeFound);
        defaultShipmentStatusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentStatusShouldBeFound(String filter) throws Exception {
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusDateTime").value(hasItem(DEFAULT_STATUS_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentStatusShouldNotBeFound(String filter) throws Exception {
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentStatus() throws Exception {
        // Get the shipmentStatus
        restShipmentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentStatus() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentStatus
        ShipmentStatus updatedShipmentStatus = shipmentStatusRepository.findById(shipmentStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentStatus are not directly saved in db
        em.detach(updatedShipmentStatus);
        updatedShipmentStatus.statusDateTime(UPDATED_STATUS_DATE_TIME).sequenceNo(UPDATED_SEQUENCE_NO);

        restShipmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentStatus))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentStatusToMatchAllProperties(updatedShipmentStatus);
    }

    @Test
    @Transactional
    void putNonExistingShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentStatus using partial update
        ShipmentStatus partialUpdatedShipmentStatus = new ShipmentStatus();
        partialUpdatedShipmentStatus.setId(shipmentStatus.getId());

        partialUpdatedShipmentStatus.sequenceNo(UPDATED_SEQUENCE_NO);

        restShipmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentStatus))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentStatus, shipmentStatus),
            getPersistedShipmentStatus(shipmentStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentStatus using partial update
        ShipmentStatus partialUpdatedShipmentStatus = new ShipmentStatus();
        partialUpdatedShipmentStatus.setId(shipmentStatus.getId());

        partialUpdatedShipmentStatus.statusDateTime(UPDATED_STATUS_DATE_TIME).sequenceNo(UPDATED_SEQUENCE_NO);

        restShipmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentStatus))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentStatusUpdatableFieldsEquals(partialUpdatedShipmentStatus, getPersistedShipmentStatus(partialUpdatedShipmentStatus));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentStatus() throws Exception {
        // Initialize the database
        insertedShipmentStatus = shipmentStatusRepository.saveAndFlush(shipmentStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentStatus
        restShipmentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentStatusRepository.count();
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

    protected ShipmentStatus getPersistedShipmentStatus(ShipmentStatus shipmentStatus) {
        return shipmentStatusRepository.findById(shipmentStatus.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentStatusToMatchAllProperties(ShipmentStatus expectedShipmentStatus) {
        assertShipmentStatusAllPropertiesEquals(expectedShipmentStatus, getPersistedShipmentStatus(expectedShipmentStatus));
    }

    protected void assertPersistedShipmentStatusToMatchUpdatableProperties(ShipmentStatus expectedShipmentStatus) {
        assertShipmentStatusAllUpdatablePropertiesEquals(expectedShipmentStatus, getPersistedShipmentStatus(expectedShipmentStatus));
    }
}
