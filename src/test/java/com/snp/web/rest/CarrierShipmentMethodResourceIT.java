package com.snp.web.rest;

import static com.snp.domain.CarrierShipmentMethodAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CarrierShipmentMethod;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.domain.ShipmentMethodType;
import com.snp.repository.CarrierShipmentMethodRepository;
import com.snp.service.CarrierShipmentMethodService;
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
 * Integration tests for the {@link CarrierShipmentMethodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarrierShipmentMethodResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_CARRIER_SERVICE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARRIER_SERVICE_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/carrier-shipment-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarrierShipmentMethodRepository carrierShipmentMethodRepository;

    @Mock
    private CarrierShipmentMethodRepository carrierShipmentMethodRepositoryMock;

    @Mock
    private CarrierShipmentMethodService carrierShipmentMethodServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarrierShipmentMethodMockMvc;

    private CarrierShipmentMethod carrierShipmentMethod;

    private CarrierShipmentMethod insertedCarrierShipmentMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarrierShipmentMethod createEntity(EntityManager em) {
        CarrierShipmentMethod carrierShipmentMethod = new CarrierShipmentMethod()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .carrierServiceCode(DEFAULT_CARRIER_SERVICE_CODE);
        // Add required entity
        ShipmentMethodType shipmentMethodType;
        if (TestUtil.findAll(em, ShipmentMethodType.class).isEmpty()) {
            shipmentMethodType = ShipmentMethodTypeResourceIT.createEntity();
            em.persist(shipmentMethodType);
            em.flush();
        } else {
            shipmentMethodType = TestUtil.findAll(em, ShipmentMethodType.class).getFirst();
        }
        carrierShipmentMethod.setShipmentMethodType(shipmentMethodType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        carrierShipmentMethod.setParty(party);
        return carrierShipmentMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarrierShipmentMethod createUpdatedEntity(EntityManager em) {
        CarrierShipmentMethod updatedCarrierShipmentMethod = new CarrierShipmentMethod()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .carrierServiceCode(UPDATED_CARRIER_SERVICE_CODE);
        // Add required entity
        ShipmentMethodType shipmentMethodType;
        if (TestUtil.findAll(em, ShipmentMethodType.class).isEmpty()) {
            shipmentMethodType = ShipmentMethodTypeResourceIT.createUpdatedEntity();
            em.persist(shipmentMethodType);
            em.flush();
        } else {
            shipmentMethodType = TestUtil.findAll(em, ShipmentMethodType.class).getFirst();
        }
        updatedCarrierShipmentMethod.setShipmentMethodType(shipmentMethodType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedCarrierShipmentMethod.setParty(party);
        return updatedCarrierShipmentMethod;
    }

    @BeforeEach
    void initTest() {
        carrierShipmentMethod = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCarrierShipmentMethod != null) {
            carrierShipmentMethodRepository.delete(insertedCarrierShipmentMethod);
            insertedCarrierShipmentMethod = null;
        }
    }

    @Test
    @Transactional
    void createCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarrierShipmentMethod
        var returnedCarrierShipmentMethod = om.readValue(
            restCarrierShipmentMethodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carrierShipmentMethod)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarrierShipmentMethod.class
        );

        // Validate the CarrierShipmentMethod in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarrierShipmentMethodUpdatableFieldsEquals(
            returnedCarrierShipmentMethod,
            getPersistedCarrierShipmentMethod(returnedCarrierShipmentMethod)
        );

        insertedCarrierShipmentMethod = returnedCarrierShipmentMethod;
    }

    @Test
    @Transactional
    void createCarrierShipmentMethodWithExistingId() throws Exception {
        // Create the CarrierShipmentMethod with an existing ID
        carrierShipmentMethod.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarrierShipmentMethodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carrierShipmentMethod)))
            .andExpect(status().isBadRequest());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethods() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrierShipmentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].carrierServiceCode").value(hasItem(DEFAULT_CARRIER_SERVICE_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarrierShipmentMethodsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carrierShipmentMethodServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarrierShipmentMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carrierShipmentMethodServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarrierShipmentMethodsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carrierShipmentMethodServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarrierShipmentMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carrierShipmentMethodRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarrierShipmentMethod() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get the carrierShipmentMethod
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, carrierShipmentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carrierShipmentMethod.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.carrierServiceCode").value(DEFAULT_CARRIER_SERVICE_CODE));
    }

    @Test
    @Transactional
    void getCarrierShipmentMethodsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        Long id = carrierShipmentMethod.getId();

        defaultCarrierShipmentMethodFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCarrierShipmentMethodFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCarrierShipmentMethodFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo equals to
        defaultCarrierShipmentMethodFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo in
        defaultCarrierShipmentMethodFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo is not null
        defaultCarrierShipmentMethodFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo is greater than or equal to
        defaultCarrierShipmentMethodFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo is less than or equal to
        defaultCarrierShipmentMethodFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo is less than
        defaultCarrierShipmentMethodFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where sequenceNo is greater than
        defaultCarrierShipmentMethodFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByCarrierServiceCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where carrierServiceCode equals to
        defaultCarrierShipmentMethodFiltering(
            "carrierServiceCode.equals=" + DEFAULT_CARRIER_SERVICE_CODE,
            "carrierServiceCode.equals=" + UPDATED_CARRIER_SERVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByCarrierServiceCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where carrierServiceCode in
        defaultCarrierShipmentMethodFiltering(
            "carrierServiceCode.in=" + DEFAULT_CARRIER_SERVICE_CODE + "," + UPDATED_CARRIER_SERVICE_CODE,
            "carrierServiceCode.in=" + UPDATED_CARRIER_SERVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByCarrierServiceCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where carrierServiceCode is not null
        defaultCarrierShipmentMethodFiltering("carrierServiceCode.specified=true", "carrierServiceCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByCarrierServiceCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where carrierServiceCode contains
        defaultCarrierShipmentMethodFiltering(
            "carrierServiceCode.contains=" + DEFAULT_CARRIER_SERVICE_CODE,
            "carrierServiceCode.contains=" + UPDATED_CARRIER_SERVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByCarrierServiceCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        // Get all the carrierShipmentMethodList where carrierServiceCode does not contain
        defaultCarrierShipmentMethodFiltering(
            "carrierServiceCode.doesNotContain=" + UPDATED_CARRIER_SERVICE_CODE,
            "carrierServiceCode.doesNotContain=" + DEFAULT_CARRIER_SERVICE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByShipmentMethodTypeIsEqualToSomething() throws Exception {
        ShipmentMethodType shipmentMethodType;
        if (TestUtil.findAll(em, ShipmentMethodType.class).isEmpty()) {
            carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
            shipmentMethodType = ShipmentMethodTypeResourceIT.createEntity();
        } else {
            shipmentMethodType = TestUtil.findAll(em, ShipmentMethodType.class).getFirst();
        }
        em.persist(shipmentMethodType);
        em.flush();
        carrierShipmentMethod.setShipmentMethodType(shipmentMethodType);
        carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
        Long shipmentMethodTypeId = shipmentMethodType.getId();
        // Get all the carrierShipmentMethodList where shipmentMethodType equals to shipmentMethodTypeId
        defaultCarrierShipmentMethodShouldBeFound("shipmentMethodTypeId.equals=" + shipmentMethodTypeId);

        // Get all the carrierShipmentMethodList where shipmentMethodType equals to (shipmentMethodTypeId + 1)
        defaultCarrierShipmentMethodShouldNotBeFound("shipmentMethodTypeId.equals=" + (shipmentMethodTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        carrierShipmentMethod.setParty(party);
        carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
        Long partyId = party.getId();
        // Get all the carrierShipmentMethodList where party equals to partyId
        defaultCarrierShipmentMethodShouldBeFound("partyId.equals=" + partyId);

        // Get all the carrierShipmentMethodList where party equals to (partyId + 1)
        defaultCarrierShipmentMethodShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllCarrierShipmentMethodsByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        carrierShipmentMethod.setRoleType(roleType);
        carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);
        Long roleTypeId = roleType.getId();
        // Get all the carrierShipmentMethodList where roleType equals to roleTypeId
        defaultCarrierShipmentMethodShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the carrierShipmentMethodList where roleType equals to (roleTypeId + 1)
        defaultCarrierShipmentMethodShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultCarrierShipmentMethodFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCarrierShipmentMethodShouldBeFound(shouldBeFound);
        defaultCarrierShipmentMethodShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarrierShipmentMethodShouldBeFound(String filter) throws Exception {
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrierShipmentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].carrierServiceCode").value(hasItem(DEFAULT_CARRIER_SERVICE_CODE)));

        // Check, that the count call also returns 1
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarrierShipmentMethodShouldNotBeFound(String filter) throws Exception {
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarrierShipmentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarrierShipmentMethod() throws Exception {
        // Get the carrierShipmentMethod
        restCarrierShipmentMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarrierShipmentMethod() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrierShipmentMethod
        CarrierShipmentMethod updatedCarrierShipmentMethod = carrierShipmentMethodRepository
            .findById(carrierShipmentMethod.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCarrierShipmentMethod are not directly saved in db
        em.detach(updatedCarrierShipmentMethod);
        updatedCarrierShipmentMethod.sequenceNo(UPDATED_SEQUENCE_NO).carrierServiceCode(UPDATED_CARRIER_SERVICE_CODE);

        restCarrierShipmentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarrierShipmentMethod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCarrierShipmentMethod))
            )
            .andExpect(status().isOk());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarrierShipmentMethodToMatchAllProperties(updatedCarrierShipmentMethod);
    }

    @Test
    @Transactional
    void putNonExistingCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carrierShipmentMethod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carrierShipmentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carrierShipmentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carrierShipmentMethod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarrierShipmentMethodWithPatch() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrierShipmentMethod using partial update
        CarrierShipmentMethod partialUpdatedCarrierShipmentMethod = new CarrierShipmentMethod();
        partialUpdatedCarrierShipmentMethod.setId(carrierShipmentMethod.getId());

        partialUpdatedCarrierShipmentMethod.sequenceNo(UPDATED_SEQUENCE_NO);

        restCarrierShipmentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrierShipmentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarrierShipmentMethod))
            )
            .andExpect(status().isOk());

        // Validate the CarrierShipmentMethod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarrierShipmentMethodUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCarrierShipmentMethod, carrierShipmentMethod),
            getPersistedCarrierShipmentMethod(carrierShipmentMethod)
        );
    }

    @Test
    @Transactional
    void fullUpdateCarrierShipmentMethodWithPatch() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrierShipmentMethod using partial update
        CarrierShipmentMethod partialUpdatedCarrierShipmentMethod = new CarrierShipmentMethod();
        partialUpdatedCarrierShipmentMethod.setId(carrierShipmentMethod.getId());

        partialUpdatedCarrierShipmentMethod.sequenceNo(UPDATED_SEQUENCE_NO).carrierServiceCode(UPDATED_CARRIER_SERVICE_CODE);

        restCarrierShipmentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrierShipmentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarrierShipmentMethod))
            )
            .andExpect(status().isOk());

        // Validate the CarrierShipmentMethod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarrierShipmentMethodUpdatableFieldsEquals(
            partialUpdatedCarrierShipmentMethod,
            getPersistedCarrierShipmentMethod(partialUpdatedCarrierShipmentMethod)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carrierShipmentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carrierShipmentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carrierShipmentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarrierShipmentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrierShipmentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarrierShipmentMethodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carrierShipmentMethod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarrierShipmentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarrierShipmentMethod() throws Exception {
        // Initialize the database
        insertedCarrierShipmentMethod = carrierShipmentMethodRepository.saveAndFlush(carrierShipmentMethod);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carrierShipmentMethod
        restCarrierShipmentMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, carrierShipmentMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carrierShipmentMethodRepository.count();
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

    protected CarrierShipmentMethod getPersistedCarrierShipmentMethod(CarrierShipmentMethod carrierShipmentMethod) {
        return carrierShipmentMethodRepository.findById(carrierShipmentMethod.getId()).orElseThrow();
    }

    protected void assertPersistedCarrierShipmentMethodToMatchAllProperties(CarrierShipmentMethod expectedCarrierShipmentMethod) {
        assertCarrierShipmentMethodAllPropertiesEquals(
            expectedCarrierShipmentMethod,
            getPersistedCarrierShipmentMethod(expectedCarrierShipmentMethod)
        );
    }

    protected void assertPersistedCarrierShipmentMethodToMatchUpdatableProperties(CarrierShipmentMethod expectedCarrierShipmentMethod) {
        assertCarrierShipmentMethodAllUpdatablePropertiesEquals(
            expectedCarrierShipmentMethod,
            getPersistedCarrierShipmentMethod(expectedCarrierShipmentMethod)
        );
    }
}
