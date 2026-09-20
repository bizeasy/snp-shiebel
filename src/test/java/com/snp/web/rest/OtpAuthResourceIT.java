package com.snp.web.rest;

import static com.snp.domain.OtpAuthAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OtpAuth;
import com.snp.domain.Party;
import com.snp.repository.OtpAuthRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link OtpAuthResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtpAuthResourceIT {

    private static final String DEFAULT_OTP = "AAAAAAAAAA";
    private static final String UPDATED_OTP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OTP_SENT = false;
    private static final Boolean UPDATED_OTP_SENT = true;

    private static final Boolean DEFAULT_VERIFICATION_SUCCESS = false;
    private static final Boolean UPDATED_VERIFICATION_SUCCESS = true;

    private static final Boolean DEFAULT_OTP_EXPIRED = false;
    private static final Boolean UPDATED_OTP_EXPIRED = true;

    private static final Instant DEFAULT_OTP_SENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OTP_SENT_TIME = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SENT_COUNTER = 1;
    private static final Integer UPDATED_SENT_COUNTER = 2;
    private static final Integer SMALLER_SENT_COUNTER = 1 - 1;

    private static final Integer DEFAULT_FAIL_COUNTER = 1;
    private static final Integer UPDATED_FAIL_COUNTER = 2;
    private static final Integer SMALLER_FAIL_COUNTER = 1 - 1;

    private static final Integer DEFAULT_OTP_RESET_COUNTER = 1;
    private static final Integer UPDATED_OTP_RESET_COUNTER = 2;
    private static final Integer SMALLER_OTP_RESET_COUNTER = 1 - 1;

    private static final Integer DEFAULT_MAX_RESEND = 1;
    private static final Integer UPDATED_MAX_RESEND = 2;
    private static final Integer SMALLER_MAX_RESEND = 1 - 1;

    private static final Integer DEFAULT_MAX_RESET = 1;
    private static final Integer UPDATED_MAX_RESET = 2;
    private static final Integer SMALLER_MAX_RESET = 1 - 1;

    private static final Integer DEFAULT_MAX_FAILURES = 1;
    private static final Integer UPDATED_MAX_FAILURES = 2;
    private static final Integer SMALLER_MAX_FAILURES = 1 - 1;

    private static final Integer DEFAULT_OTP_ACTIVE_TIME = 1;
    private static final Integer UPDATED_OTP_ACTIVE_TIME = 2;
    private static final Integer SMALLER_OTP_ACTIVE_TIME = 1 - 1;

    private static final String ENTITY_API_URL = "/api/otp-auths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OtpAuthRepository otpAuthRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtpAuthMockMvc;

    private OtpAuth otpAuth;

    private OtpAuth insertedOtpAuth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpAuth createEntity() {
        return new OtpAuth()
            .otp(DEFAULT_OTP)
            .otpSent(DEFAULT_OTP_SENT)
            .verificationSuccess(DEFAULT_VERIFICATION_SUCCESS)
            .otpExpired(DEFAULT_OTP_EXPIRED)
            .otpSentTime(DEFAULT_OTP_SENT_TIME)
            .sentCounter(DEFAULT_SENT_COUNTER)
            .failCounter(DEFAULT_FAIL_COUNTER)
            .otpResetCounter(DEFAULT_OTP_RESET_COUNTER)
            .maxResend(DEFAULT_MAX_RESEND)
            .maxReset(DEFAULT_MAX_RESET)
            .maxFailures(DEFAULT_MAX_FAILURES)
            .otpActiveTime(DEFAULT_OTP_ACTIVE_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpAuth createUpdatedEntity() {
        return new OtpAuth()
            .otp(UPDATED_OTP)
            .otpSent(UPDATED_OTP_SENT)
            .verificationSuccess(UPDATED_VERIFICATION_SUCCESS)
            .otpExpired(UPDATED_OTP_EXPIRED)
            .otpSentTime(UPDATED_OTP_SENT_TIME)
            .sentCounter(UPDATED_SENT_COUNTER)
            .failCounter(UPDATED_FAIL_COUNTER)
            .otpResetCounter(UPDATED_OTP_RESET_COUNTER)
            .maxResend(UPDATED_MAX_RESEND)
            .maxReset(UPDATED_MAX_RESET)
            .maxFailures(UPDATED_MAX_FAILURES)
            .otpActiveTime(UPDATED_OTP_ACTIVE_TIME);
    }

    @BeforeEach
    void initTest() {
        otpAuth = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOtpAuth != null) {
            otpAuthRepository.delete(insertedOtpAuth);
            insertedOtpAuth = null;
        }
    }

    @Test
    @Transactional
    void createOtpAuth() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OtpAuth
        var returnedOtpAuth = om.readValue(
            restOtpAuthMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpAuth)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OtpAuth.class
        );

        // Validate the OtpAuth in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOtpAuthUpdatableFieldsEquals(returnedOtpAuth, getPersistedOtpAuth(returnedOtpAuth));

        insertedOtpAuth = returnedOtpAuth;
    }

    @Test
    @Transactional
    void createOtpAuthWithExistingId() throws Exception {
        // Create the OtpAuth with an existing ID
        otpAuth.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtpAuthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpAuth)))
            .andExpect(status().isBadRequest());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOtpAuths() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otpAuth.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].otpSent").value(hasItem(DEFAULT_OTP_SENT)))
            .andExpect(jsonPath("$.[*].verificationSuccess").value(hasItem(DEFAULT_VERIFICATION_SUCCESS)))
            .andExpect(jsonPath("$.[*].otpExpired").value(hasItem(DEFAULT_OTP_EXPIRED)))
            .andExpect(jsonPath("$.[*].otpSentTime").value(hasItem(DEFAULT_OTP_SENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].sentCounter").value(hasItem(DEFAULT_SENT_COUNTER)))
            .andExpect(jsonPath("$.[*].failCounter").value(hasItem(DEFAULT_FAIL_COUNTER)))
            .andExpect(jsonPath("$.[*].otpResetCounter").value(hasItem(DEFAULT_OTP_RESET_COUNTER)))
            .andExpect(jsonPath("$.[*].maxResend").value(hasItem(DEFAULT_MAX_RESEND)))
            .andExpect(jsonPath("$.[*].maxReset").value(hasItem(DEFAULT_MAX_RESET)))
            .andExpect(jsonPath("$.[*].maxFailures").value(hasItem(DEFAULT_MAX_FAILURES)))
            .andExpect(jsonPath("$.[*].otpActiveTime").value(hasItem(DEFAULT_OTP_ACTIVE_TIME)));
    }

    @Test
    @Transactional
    void getOtpAuth() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get the otpAuth
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL_ID, otpAuth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otpAuth.getId().intValue()))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.otpSent").value(DEFAULT_OTP_SENT))
            .andExpect(jsonPath("$.verificationSuccess").value(DEFAULT_VERIFICATION_SUCCESS))
            .andExpect(jsonPath("$.otpExpired").value(DEFAULT_OTP_EXPIRED))
            .andExpect(jsonPath("$.otpSentTime").value(DEFAULT_OTP_SENT_TIME.toString()))
            .andExpect(jsonPath("$.sentCounter").value(DEFAULT_SENT_COUNTER))
            .andExpect(jsonPath("$.failCounter").value(DEFAULT_FAIL_COUNTER))
            .andExpect(jsonPath("$.otpResetCounter").value(DEFAULT_OTP_RESET_COUNTER))
            .andExpect(jsonPath("$.maxResend").value(DEFAULT_MAX_RESEND))
            .andExpect(jsonPath("$.maxReset").value(DEFAULT_MAX_RESET))
            .andExpect(jsonPath("$.maxFailures").value(DEFAULT_MAX_FAILURES))
            .andExpect(jsonPath("$.otpActiveTime").value(DEFAULT_OTP_ACTIVE_TIME));
    }

    @Test
    @Transactional
    void getOtpAuthsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        Long id = otpAuth.getId();

        defaultOtpAuthFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOtpAuthFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOtpAuthFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otp equals to
        defaultOtpAuthFiltering("otp.equals=" + DEFAULT_OTP, "otp.equals=" + UPDATED_OTP);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otp in
        defaultOtpAuthFiltering("otp.in=" + DEFAULT_OTP + "," + UPDATED_OTP, "otp.in=" + UPDATED_OTP);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otp is not null
        defaultOtpAuthFiltering("otp.specified=true", "otp.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpContainsSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otp contains
        defaultOtpAuthFiltering("otp.contains=" + DEFAULT_OTP, "otp.contains=" + UPDATED_OTP);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otp does not contain
        defaultOtpAuthFiltering("otp.doesNotContain=" + UPDATED_OTP, "otp.doesNotContain=" + DEFAULT_OTP);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSent equals to
        defaultOtpAuthFiltering("otpSent.equals=" + DEFAULT_OTP_SENT, "otpSent.equals=" + UPDATED_OTP_SENT);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSent in
        defaultOtpAuthFiltering("otpSent.in=" + DEFAULT_OTP_SENT + "," + UPDATED_OTP_SENT, "otpSent.in=" + UPDATED_OTP_SENT);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSent is not null
        defaultOtpAuthFiltering("otpSent.specified=true", "otpSent.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByVerificationSuccessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where verificationSuccess equals to
        defaultOtpAuthFiltering(
            "verificationSuccess.equals=" + DEFAULT_VERIFICATION_SUCCESS,
            "verificationSuccess.equals=" + UPDATED_VERIFICATION_SUCCESS
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByVerificationSuccessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where verificationSuccess in
        defaultOtpAuthFiltering(
            "verificationSuccess.in=" + DEFAULT_VERIFICATION_SUCCESS + "," + UPDATED_VERIFICATION_SUCCESS,
            "verificationSuccess.in=" + UPDATED_VERIFICATION_SUCCESS
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByVerificationSuccessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where verificationSuccess is not null
        defaultOtpAuthFiltering("verificationSuccess.specified=true", "verificationSuccess.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpExpiredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpExpired equals to
        defaultOtpAuthFiltering("otpExpired.equals=" + DEFAULT_OTP_EXPIRED, "otpExpired.equals=" + UPDATED_OTP_EXPIRED);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpExpiredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpExpired in
        defaultOtpAuthFiltering("otpExpired.in=" + DEFAULT_OTP_EXPIRED + "," + UPDATED_OTP_EXPIRED, "otpExpired.in=" + UPDATED_OTP_EXPIRED);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpExpiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpExpired is not null
        defaultOtpAuthFiltering("otpExpired.specified=true", "otpExpired.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSentTime equals to
        defaultOtpAuthFiltering("otpSentTime.equals=" + DEFAULT_OTP_SENT_TIME, "otpSentTime.equals=" + UPDATED_OTP_SENT_TIME);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSentTime in
        defaultOtpAuthFiltering(
            "otpSentTime.in=" + DEFAULT_OTP_SENT_TIME + "," + UPDATED_OTP_SENT_TIME,
            "otpSentTime.in=" + UPDATED_OTP_SENT_TIME
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpSentTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpSentTime is not null
        defaultOtpAuthFiltering("otpSentTime.specified=true", "otpSentTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter equals to
        defaultOtpAuthFiltering("sentCounter.equals=" + DEFAULT_SENT_COUNTER, "sentCounter.equals=" + UPDATED_SENT_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter in
        defaultOtpAuthFiltering(
            "sentCounter.in=" + DEFAULT_SENT_COUNTER + "," + UPDATED_SENT_COUNTER,
            "sentCounter.in=" + UPDATED_SENT_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter is not null
        defaultOtpAuthFiltering("sentCounter.specified=true", "sentCounter.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter is greater than or equal to
        defaultOtpAuthFiltering(
            "sentCounter.greaterThanOrEqual=" + DEFAULT_SENT_COUNTER,
            "sentCounter.greaterThanOrEqual=" + UPDATED_SENT_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter is less than or equal to
        defaultOtpAuthFiltering(
            "sentCounter.lessThanOrEqual=" + DEFAULT_SENT_COUNTER,
            "sentCounter.lessThanOrEqual=" + SMALLER_SENT_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter is less than
        defaultOtpAuthFiltering("sentCounter.lessThan=" + UPDATED_SENT_COUNTER, "sentCounter.lessThan=" + DEFAULT_SENT_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsBySentCounterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where sentCounter is greater than
        defaultOtpAuthFiltering("sentCounter.greaterThan=" + SMALLER_SENT_COUNTER, "sentCounter.greaterThan=" + DEFAULT_SENT_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter equals to
        defaultOtpAuthFiltering("failCounter.equals=" + DEFAULT_FAIL_COUNTER, "failCounter.equals=" + UPDATED_FAIL_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter in
        defaultOtpAuthFiltering(
            "failCounter.in=" + DEFAULT_FAIL_COUNTER + "," + UPDATED_FAIL_COUNTER,
            "failCounter.in=" + UPDATED_FAIL_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter is not null
        defaultOtpAuthFiltering("failCounter.specified=true", "failCounter.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter is greater than or equal to
        defaultOtpAuthFiltering(
            "failCounter.greaterThanOrEqual=" + DEFAULT_FAIL_COUNTER,
            "failCounter.greaterThanOrEqual=" + UPDATED_FAIL_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter is less than or equal to
        defaultOtpAuthFiltering(
            "failCounter.lessThanOrEqual=" + DEFAULT_FAIL_COUNTER,
            "failCounter.lessThanOrEqual=" + SMALLER_FAIL_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter is less than
        defaultOtpAuthFiltering("failCounter.lessThan=" + UPDATED_FAIL_COUNTER, "failCounter.lessThan=" + DEFAULT_FAIL_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByFailCounterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where failCounter is greater than
        defaultOtpAuthFiltering("failCounter.greaterThan=" + SMALLER_FAIL_COUNTER, "failCounter.greaterThan=" + DEFAULT_FAIL_COUNTER);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter equals to
        defaultOtpAuthFiltering(
            "otpResetCounter.equals=" + DEFAULT_OTP_RESET_COUNTER,
            "otpResetCounter.equals=" + UPDATED_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter in
        defaultOtpAuthFiltering(
            "otpResetCounter.in=" + DEFAULT_OTP_RESET_COUNTER + "," + UPDATED_OTP_RESET_COUNTER,
            "otpResetCounter.in=" + UPDATED_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter is not null
        defaultOtpAuthFiltering("otpResetCounter.specified=true", "otpResetCounter.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter is greater than or equal to
        defaultOtpAuthFiltering(
            "otpResetCounter.greaterThanOrEqual=" + DEFAULT_OTP_RESET_COUNTER,
            "otpResetCounter.greaterThanOrEqual=" + UPDATED_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter is less than or equal to
        defaultOtpAuthFiltering(
            "otpResetCounter.lessThanOrEqual=" + DEFAULT_OTP_RESET_COUNTER,
            "otpResetCounter.lessThanOrEqual=" + SMALLER_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter is less than
        defaultOtpAuthFiltering(
            "otpResetCounter.lessThan=" + UPDATED_OTP_RESET_COUNTER,
            "otpResetCounter.lessThan=" + DEFAULT_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpResetCounterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpResetCounter is greater than
        defaultOtpAuthFiltering(
            "otpResetCounter.greaterThan=" + SMALLER_OTP_RESET_COUNTER,
            "otpResetCounter.greaterThan=" + DEFAULT_OTP_RESET_COUNTER
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend equals to
        defaultOtpAuthFiltering("maxResend.equals=" + DEFAULT_MAX_RESEND, "maxResend.equals=" + UPDATED_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend in
        defaultOtpAuthFiltering("maxResend.in=" + DEFAULT_MAX_RESEND + "," + UPDATED_MAX_RESEND, "maxResend.in=" + UPDATED_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend is not null
        defaultOtpAuthFiltering("maxResend.specified=true", "maxResend.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend is greater than or equal to
        defaultOtpAuthFiltering("maxResend.greaterThanOrEqual=" + DEFAULT_MAX_RESEND, "maxResend.greaterThanOrEqual=" + UPDATED_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend is less than or equal to
        defaultOtpAuthFiltering("maxResend.lessThanOrEqual=" + DEFAULT_MAX_RESEND, "maxResend.lessThanOrEqual=" + SMALLER_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend is less than
        defaultOtpAuthFiltering("maxResend.lessThan=" + UPDATED_MAX_RESEND, "maxResend.lessThan=" + DEFAULT_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxResend is greater than
        defaultOtpAuthFiltering("maxResend.greaterThan=" + SMALLER_MAX_RESEND, "maxResend.greaterThan=" + DEFAULT_MAX_RESEND);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset equals to
        defaultOtpAuthFiltering("maxReset.equals=" + DEFAULT_MAX_RESET, "maxReset.equals=" + UPDATED_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset in
        defaultOtpAuthFiltering("maxReset.in=" + DEFAULT_MAX_RESET + "," + UPDATED_MAX_RESET, "maxReset.in=" + UPDATED_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset is not null
        defaultOtpAuthFiltering("maxReset.specified=true", "maxReset.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset is greater than or equal to
        defaultOtpAuthFiltering("maxReset.greaterThanOrEqual=" + DEFAULT_MAX_RESET, "maxReset.greaterThanOrEqual=" + UPDATED_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset is less than or equal to
        defaultOtpAuthFiltering("maxReset.lessThanOrEqual=" + DEFAULT_MAX_RESET, "maxReset.lessThanOrEqual=" + SMALLER_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset is less than
        defaultOtpAuthFiltering("maxReset.lessThan=" + UPDATED_MAX_RESET, "maxReset.lessThan=" + DEFAULT_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxResetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxReset is greater than
        defaultOtpAuthFiltering("maxReset.greaterThan=" + SMALLER_MAX_RESET, "maxReset.greaterThan=" + DEFAULT_MAX_RESET);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures equals to
        defaultOtpAuthFiltering("maxFailures.equals=" + DEFAULT_MAX_FAILURES, "maxFailures.equals=" + UPDATED_MAX_FAILURES);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures in
        defaultOtpAuthFiltering(
            "maxFailures.in=" + DEFAULT_MAX_FAILURES + "," + UPDATED_MAX_FAILURES,
            "maxFailures.in=" + UPDATED_MAX_FAILURES
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures is not null
        defaultOtpAuthFiltering("maxFailures.specified=true", "maxFailures.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures is greater than or equal to
        defaultOtpAuthFiltering(
            "maxFailures.greaterThanOrEqual=" + DEFAULT_MAX_FAILURES,
            "maxFailures.greaterThanOrEqual=" + UPDATED_MAX_FAILURES
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures is less than or equal to
        defaultOtpAuthFiltering(
            "maxFailures.lessThanOrEqual=" + DEFAULT_MAX_FAILURES,
            "maxFailures.lessThanOrEqual=" + SMALLER_MAX_FAILURES
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures is less than
        defaultOtpAuthFiltering("maxFailures.lessThan=" + UPDATED_MAX_FAILURES, "maxFailures.lessThan=" + DEFAULT_MAX_FAILURES);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByMaxFailuresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where maxFailures is greater than
        defaultOtpAuthFiltering("maxFailures.greaterThan=" + SMALLER_MAX_FAILURES, "maxFailures.greaterThan=" + DEFAULT_MAX_FAILURES);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime equals to
        defaultOtpAuthFiltering("otpActiveTime.equals=" + DEFAULT_OTP_ACTIVE_TIME, "otpActiveTime.equals=" + UPDATED_OTP_ACTIVE_TIME);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime in
        defaultOtpAuthFiltering(
            "otpActiveTime.in=" + DEFAULT_OTP_ACTIVE_TIME + "," + UPDATED_OTP_ACTIVE_TIME,
            "otpActiveTime.in=" + UPDATED_OTP_ACTIVE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime is not null
        defaultOtpAuthFiltering("otpActiveTime.specified=true", "otpActiveTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime is greater than or equal to
        defaultOtpAuthFiltering(
            "otpActiveTime.greaterThanOrEqual=" + DEFAULT_OTP_ACTIVE_TIME,
            "otpActiveTime.greaterThanOrEqual=" + UPDATED_OTP_ACTIVE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime is less than or equal to
        defaultOtpAuthFiltering(
            "otpActiveTime.lessThanOrEqual=" + DEFAULT_OTP_ACTIVE_TIME,
            "otpActiveTime.lessThanOrEqual=" + SMALLER_OTP_ACTIVE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime is less than
        defaultOtpAuthFiltering("otpActiveTime.lessThan=" + UPDATED_OTP_ACTIVE_TIME, "otpActiveTime.lessThan=" + DEFAULT_OTP_ACTIVE_TIME);
    }

    @Test
    @Transactional
    void getAllOtpAuthsByOtpActiveTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        // Get all the otpAuthList where otpActiveTime is greater than
        defaultOtpAuthFiltering(
            "otpActiveTime.greaterThan=" + SMALLER_OTP_ACTIVE_TIME,
            "otpActiveTime.greaterThan=" + DEFAULT_OTP_ACTIVE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOtpAuthsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            otpAuthRepository.saveAndFlush(otpAuth);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        otpAuth.setParty(party);
        otpAuthRepository.saveAndFlush(otpAuth);
        Long partyId = party.getId();
        // Get all the otpAuthList where party equals to partyId
        defaultOtpAuthShouldBeFound("partyId.equals=" + partyId);

        // Get all the otpAuthList where party equals to (partyId + 1)
        defaultOtpAuthShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    private void defaultOtpAuthFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOtpAuthShouldBeFound(shouldBeFound);
        defaultOtpAuthShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOtpAuthShouldBeFound(String filter) throws Exception {
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otpAuth.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].otpSent").value(hasItem(DEFAULT_OTP_SENT)))
            .andExpect(jsonPath("$.[*].verificationSuccess").value(hasItem(DEFAULT_VERIFICATION_SUCCESS)))
            .andExpect(jsonPath("$.[*].otpExpired").value(hasItem(DEFAULT_OTP_EXPIRED)))
            .andExpect(jsonPath("$.[*].otpSentTime").value(hasItem(DEFAULT_OTP_SENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].sentCounter").value(hasItem(DEFAULT_SENT_COUNTER)))
            .andExpect(jsonPath("$.[*].failCounter").value(hasItem(DEFAULT_FAIL_COUNTER)))
            .andExpect(jsonPath("$.[*].otpResetCounter").value(hasItem(DEFAULT_OTP_RESET_COUNTER)))
            .andExpect(jsonPath("$.[*].maxResend").value(hasItem(DEFAULT_MAX_RESEND)))
            .andExpect(jsonPath("$.[*].maxReset").value(hasItem(DEFAULT_MAX_RESET)))
            .andExpect(jsonPath("$.[*].maxFailures").value(hasItem(DEFAULT_MAX_FAILURES)))
            .andExpect(jsonPath("$.[*].otpActiveTime").value(hasItem(DEFAULT_OTP_ACTIVE_TIME)));

        // Check, that the count call also returns 1
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOtpAuthShouldNotBeFound(String filter) throws Exception {
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOtpAuthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOtpAuth() throws Exception {
        // Get the otpAuth
        restOtpAuthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOtpAuth() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpAuth
        OtpAuth updatedOtpAuth = otpAuthRepository.findById(otpAuth.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOtpAuth are not directly saved in db
        em.detach(updatedOtpAuth);
        updatedOtpAuth
            .otp(UPDATED_OTP)
            .otpSent(UPDATED_OTP_SENT)
            .verificationSuccess(UPDATED_VERIFICATION_SUCCESS)
            .otpExpired(UPDATED_OTP_EXPIRED)
            .otpSentTime(UPDATED_OTP_SENT_TIME)
            .sentCounter(UPDATED_SENT_COUNTER)
            .failCounter(UPDATED_FAIL_COUNTER)
            .otpResetCounter(UPDATED_OTP_RESET_COUNTER)
            .maxResend(UPDATED_MAX_RESEND)
            .maxReset(UPDATED_MAX_RESET)
            .maxFailures(UPDATED_MAX_FAILURES)
            .otpActiveTime(UPDATED_OTP_ACTIVE_TIME);

        restOtpAuthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOtpAuth.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOtpAuth))
            )
            .andExpect(status().isOk());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOtpAuthToMatchAllProperties(updatedOtpAuth);
    }

    @Test
    @Transactional
    void putNonExistingOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(put(ENTITY_API_URL_ID, otpAuth.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpAuth)))
            .andExpect(status().isBadRequest());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otpAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpAuth)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtpAuthWithPatch() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpAuth using partial update
        OtpAuth partialUpdatedOtpAuth = new OtpAuth();
        partialUpdatedOtpAuth.setId(otpAuth.getId());

        partialUpdatedOtpAuth
            .otpExpired(UPDATED_OTP_EXPIRED)
            .otpSentTime(UPDATED_OTP_SENT_TIME)
            .failCounter(UPDATED_FAIL_COUNTER)
            .otpResetCounter(UPDATED_OTP_RESET_COUNTER)
            .otpActiveTime(UPDATED_OTP_ACTIVE_TIME);

        restOtpAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpAuth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtpAuth))
            )
            .andExpect(status().isOk());

        // Validate the OtpAuth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtpAuthUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOtpAuth, otpAuth), getPersistedOtpAuth(otpAuth));
    }

    @Test
    @Transactional
    void fullUpdateOtpAuthWithPatch() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpAuth using partial update
        OtpAuth partialUpdatedOtpAuth = new OtpAuth();
        partialUpdatedOtpAuth.setId(otpAuth.getId());

        partialUpdatedOtpAuth
            .otp(UPDATED_OTP)
            .otpSent(UPDATED_OTP_SENT)
            .verificationSuccess(UPDATED_VERIFICATION_SUCCESS)
            .otpExpired(UPDATED_OTP_EXPIRED)
            .otpSentTime(UPDATED_OTP_SENT_TIME)
            .sentCounter(UPDATED_SENT_COUNTER)
            .failCounter(UPDATED_FAIL_COUNTER)
            .otpResetCounter(UPDATED_OTP_RESET_COUNTER)
            .maxResend(UPDATED_MAX_RESEND)
            .maxReset(UPDATED_MAX_RESET)
            .maxFailures(UPDATED_MAX_FAILURES)
            .otpActiveTime(UPDATED_OTP_ACTIVE_TIME);

        restOtpAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpAuth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtpAuth))
            )
            .andExpect(status().isOk());

        // Validate the OtpAuth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtpAuthUpdatableFieldsEquals(partialUpdatedOtpAuth, getPersistedOtpAuth(partialUpdatedOtpAuth));
    }

    @Test
    @Transactional
    void patchNonExistingOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otpAuth.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(otpAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otpAuth))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtpAuth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpAuth.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpAuthMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(otpAuth)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpAuth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtpAuth() throws Exception {
        // Initialize the database
        insertedOtpAuth = otpAuthRepository.saveAndFlush(otpAuth);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the otpAuth
        restOtpAuthMockMvc
            .perform(delete(ENTITY_API_URL_ID, otpAuth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return otpAuthRepository.count();
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

    protected OtpAuth getPersistedOtpAuth(OtpAuth otpAuth) {
        return otpAuthRepository.findById(otpAuth.getId()).orElseThrow();
    }

    protected void assertPersistedOtpAuthToMatchAllProperties(OtpAuth expectedOtpAuth) {
        assertOtpAuthAllPropertiesEquals(expectedOtpAuth, getPersistedOtpAuth(expectedOtpAuth));
    }

    protected void assertPersistedOtpAuthToMatchUpdatableProperties(OtpAuth expectedOtpAuth) {
        assertOtpAuthAllUpdatablePropertiesEquals(expectedOtpAuth, getPersistedOtpAuth(expectedOtpAuth));
    }
}
