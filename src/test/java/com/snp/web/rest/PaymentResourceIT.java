package com.snp.web.rest;

import static com.snp.domain.PaymentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.Payment;
import com.snp.domain.PaymentGatewayResponse;
import com.snp.domain.PaymentMethod;
import com.snp.domain.PaymentMethodType;
import com.snp.domain.PaymentType;
import com.snp.domain.RoleType;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.PaymentRepository;
import com.snp.service.PaymentService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_PAYMENT_REF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REF_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_EFFECTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACTUAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTUAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACTUAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MIHPAY_ID = "AAAAAAAAAA";
    private static final String UPDATED_MIHPAY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TXN_ID = "AAAAAAAAAA";
    private static final String UPDATED_TXN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Mock
    private PaymentService paymentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    private Payment insertedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity() {
        return new Payment()
            .paymentRefNumber(DEFAULT_PAYMENT_REF_NUMBER)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .amount(DEFAULT_AMOUNT)
            .actualAmount(DEFAULT_ACTUAL_AMOUNT)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .mihpayId(DEFAULT_MIHPAY_ID)
            .txnId(DEFAULT_TXN_ID)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .productInfo(DEFAULT_PRODUCT_INFO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity() {
        return new Payment()
            .paymentRefNumber(UPDATED_PAYMENT_REF_NUMBER)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .actualAmount(UPDATED_ACTUAL_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .mihpayId(UPDATED_MIHPAY_ID)
            .txnId(UPDATED_TXN_ID)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .productInfo(UPDATED_PRODUCT_INFO);
    }

    @BeforeEach
    void initTest() {
        payment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPayment != null) {
            paymentRepository.delete(insertedPayment);
            insertedPayment = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        var returnedPayment = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Payment.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));

        insertedPayment = returnedPayment;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentRefNumber").value(hasItem(DEFAULT_PAYMENT_REF_NUMBER)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].actualAmount").value(hasItem(sameNumber(DEFAULT_ACTUAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].mihpayId").value(hasItem(DEFAULT_MIHPAY_ID)))
            .andExpect(jsonPath("$.[*].txnId").value(hasItem(DEFAULT_TXN_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].productInfo").value(hasItem(DEFAULT_PRODUCT_INFO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentRefNumber").value(DEFAULT_PAYMENT_REF_NUMBER))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.actualAmount").value(sameNumber(DEFAULT_ACTUAL_AMOUNT)))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS))
            .andExpect(jsonPath("$.mihpayId").value(DEFAULT_MIHPAY_ID))
            .andExpect(jsonPath("$.txnId").value(DEFAULT_TXN_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.productInfo").value(DEFAULT_PRODUCT_INFO));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentRefNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentRefNumber equals to
        defaultPaymentFiltering(
            "paymentRefNumber.equals=" + DEFAULT_PAYMENT_REF_NUMBER,
            "paymentRefNumber.equals=" + UPDATED_PAYMENT_REF_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentRefNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentRefNumber in
        defaultPaymentFiltering(
            "paymentRefNumber.in=" + DEFAULT_PAYMENT_REF_NUMBER + "," + UPDATED_PAYMENT_REF_NUMBER,
            "paymentRefNumber.in=" + UPDATED_PAYMENT_REF_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentRefNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentRefNumber is not null
        defaultPaymentFiltering("paymentRefNumber.specified=true", "paymentRefNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentRefNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentRefNumber contains
        defaultPaymentFiltering(
            "paymentRefNumber.contains=" + DEFAULT_PAYMENT_REF_NUMBER,
            "paymentRefNumber.contains=" + UPDATED_PAYMENT_REF_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentRefNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentRefNumber does not contain
        defaultPaymentFiltering(
            "paymentRefNumber.doesNotContain=" + UPDATED_PAYMENT_REF_NUMBER,
            "paymentRefNumber.doesNotContain=" + DEFAULT_PAYMENT_REF_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByEffectiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where effectiveDate equals to
        defaultPaymentFiltering("effectiveDate.equals=" + DEFAULT_EFFECTIVE_DATE, "effectiveDate.equals=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByEffectiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where effectiveDate in
        defaultPaymentFiltering(
            "effectiveDate.in=" + DEFAULT_EFFECTIVE_DATE + "," + UPDATED_EFFECTIVE_DATE,
            "effectiveDate.in=" + UPDATED_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByEffectiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where effectiveDate is not null
        defaultPaymentFiltering("effectiveDate.specified=true", "effectiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate equals to
        defaultPaymentFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate in
        defaultPaymentFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is not null
        defaultPaymentFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to
        defaultPaymentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in
        defaultPaymentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to
        defaultPaymentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to
        defaultPaymentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than
        defaultPaymentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than
        defaultPaymentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount equals to
        defaultPaymentFiltering("actualAmount.equals=" + DEFAULT_ACTUAL_AMOUNT, "actualAmount.equals=" + UPDATED_ACTUAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount in
        defaultPaymentFiltering(
            "actualAmount.in=" + DEFAULT_ACTUAL_AMOUNT + "," + UPDATED_ACTUAL_AMOUNT,
            "actualAmount.in=" + UPDATED_ACTUAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount is not null
        defaultPaymentFiltering("actualAmount.specified=true", "actualAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount is greater than or equal to
        defaultPaymentFiltering(
            "actualAmount.greaterThanOrEqual=" + DEFAULT_ACTUAL_AMOUNT,
            "actualAmount.greaterThanOrEqual=" + UPDATED_ACTUAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount is less than or equal to
        defaultPaymentFiltering(
            "actualAmount.lessThanOrEqual=" + DEFAULT_ACTUAL_AMOUNT,
            "actualAmount.lessThanOrEqual=" + SMALLER_ACTUAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount is less than
        defaultPaymentFiltering("actualAmount.lessThan=" + UPDATED_ACTUAL_AMOUNT, "actualAmount.lessThan=" + DEFAULT_ACTUAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByActualAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where actualAmount is greater than
        defaultPaymentFiltering("actualAmount.greaterThan=" + SMALLER_ACTUAL_AMOUNT, "actualAmount.greaterThan=" + DEFAULT_ACTUAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus equals to
        defaultPaymentFiltering("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS, "paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus in
        defaultPaymentFiltering(
            "paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS,
            "paymentStatus.in=" + UPDATED_PAYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus is not null
        defaultPaymentFiltering("paymentStatus.specified=true", "paymentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus contains
        defaultPaymentFiltering("paymentStatus.contains=" + DEFAULT_PAYMENT_STATUS, "paymentStatus.contains=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus does not contain
        defaultPaymentFiltering(
            "paymentStatus.doesNotContain=" + UPDATED_PAYMENT_STATUS,
            "paymentStatus.doesNotContain=" + DEFAULT_PAYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByMihpayIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where mihpayId equals to
        defaultPaymentFiltering("mihpayId.equals=" + DEFAULT_MIHPAY_ID, "mihpayId.equals=" + UPDATED_MIHPAY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByMihpayIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where mihpayId in
        defaultPaymentFiltering("mihpayId.in=" + DEFAULT_MIHPAY_ID + "," + UPDATED_MIHPAY_ID, "mihpayId.in=" + UPDATED_MIHPAY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByMihpayIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where mihpayId is not null
        defaultPaymentFiltering("mihpayId.specified=true", "mihpayId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByMihpayIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where mihpayId contains
        defaultPaymentFiltering("mihpayId.contains=" + DEFAULT_MIHPAY_ID, "mihpayId.contains=" + UPDATED_MIHPAY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByMihpayIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where mihpayId does not contain
        defaultPaymentFiltering("mihpayId.doesNotContain=" + UPDATED_MIHPAY_ID, "mihpayId.doesNotContain=" + DEFAULT_MIHPAY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTxnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where txnId equals to
        defaultPaymentFiltering("txnId.equals=" + DEFAULT_TXN_ID, "txnId.equals=" + UPDATED_TXN_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTxnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where txnId in
        defaultPaymentFiltering("txnId.in=" + DEFAULT_TXN_ID + "," + UPDATED_TXN_ID, "txnId.in=" + UPDATED_TXN_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTxnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where txnId is not null
        defaultPaymentFiltering("txnId.specified=true", "txnId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTxnIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where txnId contains
        defaultPaymentFiltering("txnId.contains=" + DEFAULT_TXN_ID, "txnId.contains=" + UPDATED_TXN_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTxnIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where txnId does not contain
        defaultPaymentFiltering("txnId.doesNotContain=" + UPDATED_TXN_ID, "txnId.doesNotContain=" + DEFAULT_TXN_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where email equals to
        defaultPaymentFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPaymentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where email in
        defaultPaymentFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPaymentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where email is not null
        defaultPaymentFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where email contains
        defaultPaymentFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPaymentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where email does not contain
        defaultPaymentFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phone equals to
        defaultPaymentFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phone in
        defaultPaymentFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phone is not null
        defaultPaymentFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phone contains
        defaultPaymentFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phone does not contain
        defaultPaymentFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllPaymentsByProductInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where productInfo equals to
        defaultPaymentFiltering("productInfo.equals=" + DEFAULT_PRODUCT_INFO, "productInfo.equals=" + UPDATED_PRODUCT_INFO);
    }

    @Test
    @Transactional
    void getAllPaymentsByProductInfoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where productInfo in
        defaultPaymentFiltering(
            "productInfo.in=" + DEFAULT_PRODUCT_INFO + "," + UPDATED_PRODUCT_INFO,
            "productInfo.in=" + UPDATED_PRODUCT_INFO
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByProductInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where productInfo is not null
        defaultPaymentFiltering("productInfo.specified=true", "productInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByProductInfoContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where productInfo contains
        defaultPaymentFiltering("productInfo.contains=" + DEFAULT_PRODUCT_INFO, "productInfo.contains=" + UPDATED_PRODUCT_INFO);
    }

    @Test
    @Transactional
    void getAllPaymentsByProductInfoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where productInfo does not contain
        defaultPaymentFiltering("productInfo.doesNotContain=" + UPDATED_PRODUCT_INFO, "productInfo.doesNotContain=" + DEFAULT_PRODUCT_INFO);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentTypeIsEqualToSomething() throws Exception {
        PaymentType paymentType;
        if (TestUtil.findAll(em, PaymentType.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            paymentType = PaymentTypeResourceIT.createEntity();
        } else {
            paymentType = TestUtil.findAll(em, PaymentType.class).getFirst();
        }
        em.persist(paymentType);
        em.flush();
        payment.setPaymentType(paymentType);
        paymentRepository.saveAndFlush(payment);
        Long paymentTypeId = paymentType.getId();
        // Get all the paymentList where paymentType equals to paymentTypeId
        defaultPaymentShouldBeFound("paymentTypeId.equals=" + paymentTypeId);

        // Get all the paymentList where paymentType equals to (paymentTypeId + 1)
        defaultPaymentShouldNotBeFound("paymentTypeId.equals=" + (paymentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodTypeIsEqualToSomething() throws Exception {
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            paymentMethodType = PaymentMethodTypeResourceIT.createEntity();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        em.persist(paymentMethodType);
        em.flush();
        payment.setPaymentMethodType(paymentMethodType);
        paymentRepository.saveAndFlush(payment);
        Long paymentMethodTypeId = paymentMethodType.getId();
        // Get all the paymentList where paymentMethodType equals to paymentMethodTypeId
        defaultPaymentShouldBeFound("paymentMethodTypeId.equals=" + paymentMethodTypeId);

        // Get all the paymentList where paymentMethodType equals to (paymentMethodTypeId + 1)
        defaultPaymentShouldNotBeFound("paymentMethodTypeId.equals=" + (paymentMethodTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            paymentMethod = PaymentMethodResourceIT.createEntity(em);
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).getFirst();
        }
        em.persist(paymentMethod);
        em.flush();
        payment.setPaymentMethod(paymentMethod);
        paymentRepository.saveAndFlush(payment);
        Long paymentMethodId = paymentMethod.getId();
        // Get all the paymentList where paymentMethod equals to paymentMethodId
        defaultPaymentShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the paymentList where paymentMethod equals to (paymentMethodId + 1)
        defaultPaymentShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentGatewayResponseIsEqualToSomething() throws Exception {
        PaymentGatewayResponse paymentGatewayResponse;
        if (TestUtil.findAll(em, PaymentGatewayResponse.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            paymentGatewayResponse = PaymentGatewayResponseResourceIT.createEntity();
        } else {
            paymentGatewayResponse = TestUtil.findAll(em, PaymentGatewayResponse.class).getFirst();
        }
        em.persist(paymentGatewayResponse);
        em.flush();
        payment.setPaymentGatewayResponse(paymentGatewayResponse);
        paymentRepository.saveAndFlush(payment);
        Long paymentGatewayResponseId = paymentGatewayResponse.getId();
        // Get all the paymentList where paymentGatewayResponse equals to paymentGatewayResponseId
        defaultPaymentShouldBeFound("paymentGatewayResponseId.equals=" + paymentGatewayResponseId);

        // Get all the paymentList where paymentGatewayResponse equals to (paymentGatewayResponseId + 1)
        defaultPaymentShouldNotBeFound("paymentGatewayResponseId.equals=" + (paymentGatewayResponseId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        payment.setStatus(status);
        paymentRepository.saveAndFlush(payment);
        Long statusId = status.getId();
        // Get all the paymentList where status equals to statusId
        defaultPaymentShouldBeFound("statusId.equals=" + statusId);

        // Get all the paymentList where status equals to (statusId + 1)
        defaultPaymentShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        payment.setFromParty(fromParty);
        paymentRepository.saveAndFlush(payment);
        Long fromPartyId = fromParty.getId();
        // Get all the paymentList where fromParty equals to fromPartyId
        defaultPaymentShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the paymentList where fromParty equals to (fromPartyId + 1)
        defaultPaymentShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByToPartyIsEqualToSomething() throws Exception {
        Party toParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            toParty = PartyResourceIT.createEntity();
        } else {
            toParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(toParty);
        em.flush();
        payment.setToParty(toParty);
        paymentRepository.saveAndFlush(payment);
        Long toPartyId = toParty.getId();
        // Get all the paymentList where toParty equals to toPartyId
        defaultPaymentShouldBeFound("toPartyId.equals=" + toPartyId);

        // Get all the paymentList where toParty equals to (toPartyId + 1)
        defaultPaymentShouldNotBeFound("toPartyId.equals=" + (toPartyId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        payment.setRoleType(roleType);
        paymentRepository.saveAndFlush(payment);
        Long roleTypeId = roleType.getId();
        // Get all the paymentList where roleType equals to roleTypeId
        defaultPaymentShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the paymentList where roleType equals to (roleTypeId + 1)
        defaultPaymentShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        payment.setCurrencyUom(currencyUom);
        paymentRepository.saveAndFlush(payment);
        Long currencyUomId = currencyUom.getId();
        // Get all the paymentList where currencyUom equals to currencyUomId
        defaultPaymentShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the paymentList where currencyUom equals to (currencyUomId + 1)
        defaultPaymentShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentShouldBeFound(shouldBeFound);
        defaultPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentRefNumber").value(hasItem(DEFAULT_PAYMENT_REF_NUMBER)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].actualAmount").value(hasItem(sameNumber(DEFAULT_ACTUAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].mihpayId").value(hasItem(DEFAULT_MIHPAY_ID)))
            .andExpect(jsonPath("$.[*].txnId").value(hasItem(DEFAULT_TXN_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].productInfo").value(hasItem(DEFAULT_PRODUCT_INFO)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentRefNumber(UPDATED_PAYMENT_REF_NUMBER)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .actualAmount(UPDATED_ACTUAL_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .mihpayId(UPDATED_MIHPAY_ID)
            .txnId(UPDATED_TXN_ID)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .productInfo(UPDATED_PRODUCT_INFO);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL_ID, payment.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.amount(UPDATED_AMOUNT).paymentStatus(UPDATED_PAYMENT_STATUS).phone(UPDATED_PHONE);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paymentRefNumber(UPDATED_PAYMENT_REF_NUMBER)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT)
            .actualAmount(UPDATED_ACTUAL_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .mihpayId(UPDATED_MIHPAY_ID)
            .txnId(UPDATED_TXN_ID)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .productInfo(UPDATED_PRODUCT_INFO);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payment.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
