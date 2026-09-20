package com.snp.web.rest;

import static com.snp.domain.FacilityCarrierShipmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityCarrierShipment;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.domain.ShipmentMethodType;
import com.snp.repository.FacilityCarrierShipmentRepository;
import com.snp.service.FacilityCarrierShipmentService;
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
 * Integration tests for the {@link FacilityCarrierShipmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityCarrierShipmentResourceIT {

    private static final String ENTITY_API_URL = "/api/facility-carrier-shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityCarrierShipmentRepository facilityCarrierShipmentRepository;

    @Mock
    private FacilityCarrierShipmentRepository facilityCarrierShipmentRepositoryMock;

    @Mock
    private FacilityCarrierShipmentService facilityCarrierShipmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityCarrierShipmentMockMvc;

    private FacilityCarrierShipment facilityCarrierShipment;

    private FacilityCarrierShipment insertedFacilityCarrierShipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityCarrierShipment createEntity(EntityManager em) {
        FacilityCarrierShipment facilityCarrierShipment = new FacilityCarrierShipment();
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        facilityCarrierShipment.setFacility(facility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        facilityCarrierShipment.setParty(party);
        return facilityCarrierShipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityCarrierShipment createUpdatedEntity(EntityManager em) {
        FacilityCarrierShipment updatedFacilityCarrierShipment = new FacilityCarrierShipment();
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedFacilityCarrierShipment.setFacility(facility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedFacilityCarrierShipment.setParty(party);
        return updatedFacilityCarrierShipment;
    }

    @BeforeEach
    void initTest() {
        facilityCarrierShipment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityCarrierShipment != null) {
            facilityCarrierShipmentRepository.delete(insertedFacilityCarrierShipment);
            insertedFacilityCarrierShipment = null;
        }
    }

    @Test
    @Transactional
    void createFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityCarrierShipment
        var returnedFacilityCarrierShipment = om.readValue(
            restFacilityCarrierShipmentMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityCarrierShipment))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityCarrierShipment.class
        );

        // Validate the FacilityCarrierShipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityCarrierShipmentUpdatableFieldsEquals(
            returnedFacilityCarrierShipment,
            getPersistedFacilityCarrierShipment(returnedFacilityCarrierShipment)
        );

        insertedFacilityCarrierShipment = returnedFacilityCarrierShipment;
    }

    @Test
    @Transactional
    void createFacilityCarrierShipmentWithExistingId() throws Exception {
        // Create the FacilityCarrierShipment with an existing ID
        facilityCarrierShipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityCarrierShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityCarrierShipment)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacilityCarrierShipments() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        // Get all the facilityCarrierShipmentList
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityCarrierShipment.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityCarrierShipmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityCarrierShipmentServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restFacilityCarrierShipmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityCarrierShipmentServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityCarrierShipmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityCarrierShipmentServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restFacilityCarrierShipmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityCarrierShipmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityCarrierShipment() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        // Get the facilityCarrierShipment
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityCarrierShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityCarrierShipment.getId().intValue()));
    }

    @Test
    @Transactional
    void getFacilityCarrierShipmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        Long id = facilityCarrierShipment.getId();

        defaultFacilityCarrierShipmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityCarrierShipmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityCarrierShipmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityCarrierShipmentsByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        facilityCarrierShipment.setFacility(facility);
        facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
        Long facilityId = facility.getId();
        // Get all the facilityCarrierShipmentList where facility equals to facilityId
        defaultFacilityCarrierShipmentShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the facilityCarrierShipmentList where facility equals to (facilityId + 1)
        defaultFacilityCarrierShipmentShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityCarrierShipmentsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        facilityCarrierShipment.setParty(party);
        facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
        Long partyId = party.getId();
        // Get all the facilityCarrierShipmentList where party equals to partyId
        defaultFacilityCarrierShipmentShouldBeFound("partyId.equals=" + partyId);

        // Get all the facilityCarrierShipmentList where party equals to (partyId + 1)
        defaultFacilityCarrierShipmentShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityCarrierShipmentsByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        facilityCarrierShipment.setRoleType(roleType);
        facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
        Long roleTypeId = roleType.getId();
        // Get all the facilityCarrierShipmentList where roleType equals to roleTypeId
        defaultFacilityCarrierShipmentShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the facilityCarrierShipmentList where roleType equals to (roleTypeId + 1)
        defaultFacilityCarrierShipmentShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityCarrierShipmentsByShipmentMethodTypeIsEqualToSomething() throws Exception {
        ShipmentMethodType shipmentMethodType;
        if (TestUtil.findAll(em, ShipmentMethodType.class).isEmpty()) {
            facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
            shipmentMethodType = ShipmentMethodTypeResourceIT.createEntity();
        } else {
            shipmentMethodType = TestUtil.findAll(em, ShipmentMethodType.class).getFirst();
        }
        em.persist(shipmentMethodType);
        em.flush();
        facilityCarrierShipment.setShipmentMethodType(shipmentMethodType);
        facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);
        Long shipmentMethodTypeId = shipmentMethodType.getId();
        // Get all the facilityCarrierShipmentList where shipmentMethodType equals to shipmentMethodTypeId
        defaultFacilityCarrierShipmentShouldBeFound("shipmentMethodTypeId.equals=" + shipmentMethodTypeId);

        // Get all the facilityCarrierShipmentList where shipmentMethodType equals to (shipmentMethodTypeId + 1)
        defaultFacilityCarrierShipmentShouldNotBeFound("shipmentMethodTypeId.equals=" + (shipmentMethodTypeId + 1));
    }

    private void defaultFacilityCarrierShipmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityCarrierShipmentShouldBeFound(shouldBeFound);
        defaultFacilityCarrierShipmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityCarrierShipmentShouldBeFound(String filter) throws Exception {
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityCarrierShipment.getId().intValue())));

        // Check, that the count call also returns 1
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityCarrierShipmentShouldNotBeFound(String filter) throws Exception {
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityCarrierShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityCarrierShipment() throws Exception {
        // Get the facilityCarrierShipment
        restFacilityCarrierShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityCarrierShipment() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityCarrierShipment
        FacilityCarrierShipment updatedFacilityCarrierShipment = facilityCarrierShipmentRepository
            .findById(facilityCarrierShipment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityCarrierShipment are not directly saved in db
        em.detach(updatedFacilityCarrierShipment);

        restFacilityCarrierShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityCarrierShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityCarrierShipment))
            )
            .andExpect(status().isOk());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityCarrierShipmentToMatchAllProperties(updatedFacilityCarrierShipment);
    }

    @Test
    @Transactional
    void putNonExistingFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityCarrierShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityCarrierShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityCarrierShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityCarrierShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityCarrierShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityCarrierShipment using partial update
        FacilityCarrierShipment partialUpdatedFacilityCarrierShipment = new FacilityCarrierShipment();
        partialUpdatedFacilityCarrierShipment.setId(facilityCarrierShipment.getId());

        restFacilityCarrierShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityCarrierShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityCarrierShipment))
            )
            .andExpect(status().isOk());

        // Validate the FacilityCarrierShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityCarrierShipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityCarrierShipment, facilityCarrierShipment),
            getPersistedFacilityCarrierShipment(facilityCarrierShipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityCarrierShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityCarrierShipment using partial update
        FacilityCarrierShipment partialUpdatedFacilityCarrierShipment = new FacilityCarrierShipment();
        partialUpdatedFacilityCarrierShipment.setId(facilityCarrierShipment.getId());

        restFacilityCarrierShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityCarrierShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityCarrierShipment))
            )
            .andExpect(status().isOk());

        // Validate the FacilityCarrierShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityCarrierShipmentUpdatableFieldsEquals(
            partialUpdatedFacilityCarrierShipment,
            getPersistedFacilityCarrierShipment(partialUpdatedFacilityCarrierShipment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityCarrierShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityCarrierShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityCarrierShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityCarrierShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityCarrierShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityCarrierShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityCarrierShipment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityCarrierShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityCarrierShipment() throws Exception {
        // Initialize the database
        insertedFacilityCarrierShipment = facilityCarrierShipmentRepository.saveAndFlush(facilityCarrierShipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityCarrierShipment
        restFacilityCarrierShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityCarrierShipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityCarrierShipmentRepository.count();
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

    protected FacilityCarrierShipment getPersistedFacilityCarrierShipment(FacilityCarrierShipment facilityCarrierShipment) {
        return facilityCarrierShipmentRepository.findById(facilityCarrierShipment.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityCarrierShipmentToMatchAllProperties(FacilityCarrierShipment expectedFacilityCarrierShipment) {
        assertFacilityCarrierShipmentAllPropertiesEquals(
            expectedFacilityCarrierShipment,
            getPersistedFacilityCarrierShipment(expectedFacilityCarrierShipment)
        );
    }

    protected void assertPersistedFacilityCarrierShipmentToMatchUpdatableProperties(
        FacilityCarrierShipment expectedFacilityCarrierShipment
    ) {
        assertFacilityCarrierShipmentAllUpdatablePropertiesEquals(
            expectedFacilityCarrierShipment,
            getPersistedFacilityCarrierShipment(expectedFacilityCarrierShipment)
        );
    }
}
