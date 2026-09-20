package com.snp.web.rest;

import static com.snp.domain.PaymentGatewayResponseAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PaymentGatewayConfig;
import com.snp.domain.PaymentGatewayResponse;
import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentGatewayResponseRepository;
import com.snp.service.PaymentGatewayResponseService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentGatewayResponseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentGatewayResponseResourceIT {

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_GATEWAY_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-gateway-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentGatewayResponseRepository paymentGatewayResponseRepository;

    @Mock
    private PaymentGatewayResponseRepository paymentGatewayResponseRepositoryMock;

    @Mock
    private PaymentGatewayResponseService paymentGatewayResponseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentGatewayResponseMockMvc;

    private PaymentGatewayResponse paymentGatewayResponse;

    private PaymentGatewayResponse insertedPaymentGatewayResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGatewayResponse createEntity() {
        return new PaymentGatewayResponse()
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .amount(DEFAULT_AMOUNT)
            .gatewayMessage(DEFAULT_GATEWAY_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGatewayResponse createUpdatedEntity() {
        return new PaymentGatewayResponse()
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .amount(UPDATED_AMOUNT)
            .gatewayMessage(UPDATED_GATEWAY_MESSAGE);
    }

    @BeforeEach
    void initTest() {
        paymentGatewayResponse = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentGatewayResponse != null) {
            paymentGatewayResponseRepository.delete(insertedPaymentGatewayResponse);
            insertedPaymentGatewayResponse = null;
        }
    }

    @Test
    @Transactional
    void createPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentGatewayResponse
        var returnedPaymentGatewayResponse = om.readValue(
            restPaymentGatewayResponseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayResponse)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentGatewayResponse.class
        );

        // Validate the PaymentGatewayResponse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentGatewayResponseUpdatableFieldsEquals(
            returnedPaymentGatewayResponse,
            getPersistedPaymentGatewayResponse(returnedPaymentGatewayResponse)
        );

        insertedPaymentGatewayResponse = returnedPaymentGatewayResponse;
    }

    @Test
    @Transactional
    void createPaymentGatewayResponseWithExistingId() throws Exception {
        // Create the PaymentGatewayResponse with an existing ID
        paymentGatewayResponse.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentGatewayResponseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayResponse)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponses() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGatewayResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].gatewayMessage").value(hasItem(DEFAULT_GATEWAY_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentGatewayResponsesWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentGatewayResponseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentGatewayResponseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentGatewayResponseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentGatewayResponsesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentGatewayResponseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentGatewayResponseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentGatewayResponseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPaymentGatewayResponse() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get the paymentGatewayResponse
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentGatewayResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentGatewayResponse.getId().intValue()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.gatewayMessage").value(DEFAULT_GATEWAY_MESSAGE));
    }

    @Test
    @Transactional
    void getPaymentGatewayResponsesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        Long id = paymentGatewayResponse.getId();

        defaultPaymentGatewayResponseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentGatewayResponseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentGatewayResponseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where referenceNumber equals to
        defaultPaymentGatewayResponseFiltering(
            "referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where referenceNumber in
        defaultPaymentGatewayResponseFiltering(
            "referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.in=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where referenceNumber is not null
        defaultPaymentGatewayResponseFiltering("referenceNumber.specified=true", "referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where referenceNumber contains
        defaultPaymentGatewayResponseFiltering(
            "referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where referenceNumber does not contain
        defaultPaymentGatewayResponseFiltering(
            "referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount equals to
        defaultPaymentGatewayResponseFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount in
        defaultPaymentGatewayResponseFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount is not null
        defaultPaymentGatewayResponseFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount is greater than or equal to
        defaultPaymentGatewayResponseFiltering(
            "amount.greaterThanOrEqual=" + DEFAULT_AMOUNT,
            "amount.greaterThanOrEqual=" + UPDATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount is less than or equal to
        defaultPaymentGatewayResponseFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount is less than
        defaultPaymentGatewayResponseFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where amount is greater than
        defaultPaymentGatewayResponseFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByGatewayMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where gatewayMessage equals to
        defaultPaymentGatewayResponseFiltering(
            "gatewayMessage.equals=" + DEFAULT_GATEWAY_MESSAGE,
            "gatewayMessage.equals=" + UPDATED_GATEWAY_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByGatewayMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where gatewayMessage in
        defaultPaymentGatewayResponseFiltering(
            "gatewayMessage.in=" + DEFAULT_GATEWAY_MESSAGE + "," + UPDATED_GATEWAY_MESSAGE,
            "gatewayMessage.in=" + UPDATED_GATEWAY_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByGatewayMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where gatewayMessage is not null
        defaultPaymentGatewayResponseFiltering("gatewayMessage.specified=true", "gatewayMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByGatewayMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where gatewayMessage contains
        defaultPaymentGatewayResponseFiltering(
            "gatewayMessage.contains=" + DEFAULT_GATEWAY_MESSAGE,
            "gatewayMessage.contains=" + UPDATED_GATEWAY_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByGatewayMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        // Get all the paymentGatewayResponseList where gatewayMessage does not contain
        defaultPaymentGatewayResponseFiltering(
            "gatewayMessage.doesNotContain=" + UPDATED_GATEWAY_MESSAGE,
            "gatewayMessage.doesNotContain=" + DEFAULT_GATEWAY_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByPaymentGatewayConfigIsEqualToSomething() throws Exception {
        PaymentGatewayConfig paymentGatewayConfig;
        if (TestUtil.findAll(em, PaymentGatewayConfig.class).isEmpty()) {
            paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);
            paymentGatewayConfig = PaymentGatewayConfigResourceIT.createEntity();
        } else {
            paymentGatewayConfig = TestUtil.findAll(em, PaymentGatewayConfig.class).getFirst();
        }
        em.persist(paymentGatewayConfig);
        em.flush();
        paymentGatewayResponse.setPaymentGatewayConfig(paymentGatewayConfig);
        paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);
        Long paymentGatewayConfigId = paymentGatewayConfig.getId();
        // Get all the paymentGatewayResponseList where paymentGatewayConfig equals to paymentGatewayConfigId
        defaultPaymentGatewayResponseShouldBeFound("paymentGatewayConfigId.equals=" + paymentGatewayConfigId);

        // Get all the paymentGatewayResponseList where paymentGatewayConfig equals to (paymentGatewayConfigId + 1)
        defaultPaymentGatewayResponseShouldNotBeFound("paymentGatewayConfigId.equals=" + (paymentGatewayConfigId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentGatewayResponsesByPaymentMethodTypeIsEqualToSomething() throws Exception {
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);
            paymentMethodType = PaymentMethodTypeResourceIT.createEntity();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        em.persist(paymentMethodType);
        em.flush();
        paymentGatewayResponse.setPaymentMethodType(paymentMethodType);
        paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);
        Long paymentMethodTypeId = paymentMethodType.getId();
        // Get all the paymentGatewayResponseList where paymentMethodType equals to paymentMethodTypeId
        defaultPaymentGatewayResponseShouldBeFound("paymentMethodTypeId.equals=" + paymentMethodTypeId);

        // Get all the paymentGatewayResponseList where paymentMethodType equals to (paymentMethodTypeId + 1)
        defaultPaymentGatewayResponseShouldNotBeFound("paymentMethodTypeId.equals=" + (paymentMethodTypeId + 1));
    }

    private void defaultPaymentGatewayResponseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentGatewayResponseShouldBeFound(shouldBeFound);
        defaultPaymentGatewayResponseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentGatewayResponseShouldBeFound(String filter) throws Exception {
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGatewayResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].gatewayMessage").value(hasItem(DEFAULT_GATEWAY_MESSAGE)));

        // Check, that the count call also returns 1
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentGatewayResponseShouldNotBeFound(String filter) throws Exception {
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentGatewayResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentGatewayResponse() throws Exception {
        // Get the paymentGatewayResponse
        restPaymentGatewayResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentGatewayResponse() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayResponse
        PaymentGatewayResponse updatedPaymentGatewayResponse = paymentGatewayResponseRepository
            .findById(paymentGatewayResponse.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentGatewayResponse are not directly saved in db
        em.detach(updatedPaymentGatewayResponse);
        updatedPaymentGatewayResponse
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .amount(UPDATED_AMOUNT)
            .gatewayMessage(UPDATED_GATEWAY_MESSAGE);

        restPaymentGatewayResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentGatewayResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentGatewayResponse))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentGatewayResponseToMatchAllProperties(updatedPaymentGatewayResponse);
    }

    @Test
    @Transactional
    void putNonExistingPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentGatewayResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGatewayResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGatewayResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayResponse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentGatewayResponseWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayResponse using partial update
        PaymentGatewayResponse partialUpdatedPaymentGatewayResponse = new PaymentGatewayResponse();
        partialUpdatedPaymentGatewayResponse.setId(paymentGatewayResponse.getId());

        partialUpdatedPaymentGatewayResponse.referenceNumber(UPDATED_REFERENCE_NUMBER).amount(UPDATED_AMOUNT);

        restPaymentGatewayResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGatewayResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGatewayResponse))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayResponse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGatewayResponseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentGatewayResponse, paymentGatewayResponse),
            getPersistedPaymentGatewayResponse(paymentGatewayResponse)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentGatewayResponseWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayResponse using partial update
        PaymentGatewayResponse partialUpdatedPaymentGatewayResponse = new PaymentGatewayResponse();
        partialUpdatedPaymentGatewayResponse.setId(paymentGatewayResponse.getId());

        partialUpdatedPaymentGatewayResponse
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .amount(UPDATED_AMOUNT)
            .gatewayMessage(UPDATED_GATEWAY_MESSAGE);

        restPaymentGatewayResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGatewayResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGatewayResponse))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayResponse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGatewayResponseUpdatableFieldsEquals(
            partialUpdatedPaymentGatewayResponse,
            getPersistedPaymentGatewayResponse(partialUpdatedPaymentGatewayResponse)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentGatewayResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGatewayResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGatewayResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentGatewayResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayResponse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayResponseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentGatewayResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGatewayResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentGatewayResponse() throws Exception {
        // Initialize the database
        insertedPaymentGatewayResponse = paymentGatewayResponseRepository.saveAndFlush(paymentGatewayResponse);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentGatewayResponse
        restPaymentGatewayResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentGatewayResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentGatewayResponseRepository.count();
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

    protected PaymentGatewayResponse getPersistedPaymentGatewayResponse(PaymentGatewayResponse paymentGatewayResponse) {
        return paymentGatewayResponseRepository.findById(paymentGatewayResponse.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentGatewayResponseToMatchAllProperties(PaymentGatewayResponse expectedPaymentGatewayResponse) {
        assertPaymentGatewayResponseAllPropertiesEquals(
            expectedPaymentGatewayResponse,
            getPersistedPaymentGatewayResponse(expectedPaymentGatewayResponse)
        );
    }

    protected void assertPersistedPaymentGatewayResponseToMatchUpdatableProperties(PaymentGatewayResponse expectedPaymentGatewayResponse) {
        assertPaymentGatewayResponseAllUpdatablePropertiesEquals(
            expectedPaymentGatewayResponse,
            getPersistedPaymentGatewayResponse(expectedPaymentGatewayResponse)
        );
    }
}
