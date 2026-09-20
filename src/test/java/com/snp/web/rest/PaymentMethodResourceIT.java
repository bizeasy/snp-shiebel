package com.snp.web.rest;

import static com.snp.domain.PaymentMethodAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PaymentMethod;
import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentMethodRepository;
import com.snp.service.PaymentMethodService;
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
 * Integration tests for the {@link PaymentMethodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentMethodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/payment-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepositoryMock;

    @Mock
    private PaymentMethodService paymentMethodServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMethodMockMvc;

    private PaymentMethod paymentMethod;

    private PaymentMethod insertedPaymentMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            paymentMethodType = PaymentMethodTypeResourceIT.createEntity();
            em.persist(paymentMethodType);
            em.flush();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        paymentMethod.setPaymentMethodType(paymentMethodType);
        return paymentMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createUpdatedEntity(EntityManager em) {
        PaymentMethod updatedPaymentMethod = new PaymentMethod()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE);
        // Add required entity
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            paymentMethodType = PaymentMethodTypeResourceIT.createUpdatedEntity();
            em.persist(paymentMethodType);
            em.flush();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        updatedPaymentMethod.setPaymentMethodType(paymentMethodType);
        return updatedPaymentMethod;
    }

    @BeforeEach
    void initTest() {
        paymentMethod = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentMethod != null) {
            paymentMethodRepository.delete(insertedPaymentMethod);
            insertedPaymentMethod = null;
        }
    }

    @Test
    @Transactional
    void createPaymentMethod() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentMethod
        var returnedPaymentMethod = om.readValue(
            restPaymentMethodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethod)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentMethod.class
        );

        // Validate the PaymentMethod in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentMethodUpdatableFieldsEquals(returnedPaymentMethod, getPersistedPaymentMethod(returnedPaymentMethod));

        insertedPaymentMethod = returnedPaymentMethod;
    }

    @Test
    @Transactional
    void createPaymentMethodWithExistingId() throws Exception {
        // Create the PaymentMethod with an existing ID
        paymentMethod.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMethod.setName(null);

        // Create the PaymentMethod, which fails.

        restPaymentMethodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethod)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentMethods() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentMethodsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentMethodServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentMethodServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentMethodsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentMethodServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentMethodRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPaymentMethod() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get the paymentMethod
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethod.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getPaymentMethodsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        Long id = paymentMethod.getId();

        defaultPaymentMethodFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentMethodFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentMethodFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name equals to
        defaultPaymentMethodFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name in
        defaultPaymentMethodFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name is not null
        defaultPaymentMethodFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name contains
        defaultPaymentMethodFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name does not contain
        defaultPaymentMethodFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description equals to
        defaultPaymentMethodFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description in
        defaultPaymentMethodFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description is not null
        defaultPaymentMethodFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description contains
        defaultPaymentMethodFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description does not contain
        defaultPaymentMethodFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where fromDate equals to
        defaultPaymentMethodFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where fromDate in
        defaultPaymentMethodFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where fromDate is not null
        defaultPaymentMethodFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where thruDate equals to
        defaultPaymentMethodFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where thruDate in
        defaultPaymentMethodFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where thruDate is not null
        defaultPaymentMethodFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByPaymentMethodTypeIsEqualToSomething() throws Exception {
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            paymentMethodRepository.saveAndFlush(paymentMethod);
            paymentMethodType = PaymentMethodTypeResourceIT.createEntity();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        em.persist(paymentMethodType);
        em.flush();
        paymentMethod.setPaymentMethodType(paymentMethodType);
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Long paymentMethodTypeId = paymentMethodType.getId();
        // Get all the paymentMethodList where paymentMethodType equals to paymentMethodTypeId
        defaultPaymentMethodShouldBeFound("paymentMethodTypeId.equals=" + paymentMethodTypeId);

        // Get all the paymentMethodList where paymentMethodType equals to (paymentMethodTypeId + 1)
        defaultPaymentMethodShouldNotBeFound("paymentMethodTypeId.equals=" + (paymentMethodTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            paymentMethodRepository.saveAndFlush(paymentMethod);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        paymentMethod.setParty(party);
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Long partyId = party.getId();
        // Get all the paymentMethodList where party equals to partyId
        defaultPaymentMethodShouldBeFound("partyId.equals=" + partyId);

        // Get all the paymentMethodList where party equals to (partyId + 1)
        defaultPaymentMethodShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    private void defaultPaymentMethodFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentMethodShouldBeFound(shouldBeFound);
        defaultPaymentMethodShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMethodShouldBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMethodShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentMethod() throws Exception {
        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentMethod() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethod
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentMethod are not directly saved in db
        em.detach(updatedPaymentMethod);
        updatedPaymentMethod.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentMethod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentMethod))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentMethodToMatchAllProperties(updatedPaymentMethod);
    }

    @Test
    @Transactional
    void putNonExistingPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMethod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMethod))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMethodUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentMethod, paymentMethod),
            getPersistedPaymentMethod(paymentMethod)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE);

        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMethod))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMethodUpdatableFieldsEquals(partialUpdatedPaymentMethod, getPersistedPaymentMethod(partialUpdatedPaymentMethod));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMethod))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentMethod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentMethod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentMethod() throws Exception {
        // Initialize the database
        insertedPaymentMethod = paymentMethodRepository.saveAndFlush(paymentMethod);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentMethod
        restPaymentMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentMethodRepository.count();
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

    protected PaymentMethod getPersistedPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.findById(paymentMethod.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentMethodToMatchAllProperties(PaymentMethod expectedPaymentMethod) {
        assertPaymentMethodAllPropertiesEquals(expectedPaymentMethod, getPersistedPaymentMethod(expectedPaymentMethod));
    }

    protected void assertPersistedPaymentMethodToMatchUpdatableProperties(PaymentMethod expectedPaymentMethod) {
        assertPaymentMethodAllUpdatablePropertiesEquals(expectedPaymentMethod, getPersistedPaymentMethod(expectedPaymentMethod));
    }
}
