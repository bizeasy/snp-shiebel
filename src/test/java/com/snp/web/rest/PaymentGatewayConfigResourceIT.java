package com.snp.web.rest;

import static com.snp.domain.PaymentGatewayConfigAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PaymentGatewayConfig;
import com.snp.repository.PaymentGatewayConfigRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PaymentGatewayConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentGatewayConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTH_URL = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REFUND_URL = "AAAAAAAAAA";
    private static final String UPDATED_REFUND_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-gateway-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentGatewayConfigRepository paymentGatewayConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentGatewayConfigMockMvc;

    private PaymentGatewayConfig paymentGatewayConfig;

    private PaymentGatewayConfig insertedPaymentGatewayConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGatewayConfig createEntity() {
        return new PaymentGatewayConfig()
            .name(DEFAULT_NAME)
            .authUrl(DEFAULT_AUTH_URL)
            .releaseUrl(DEFAULT_RELEASE_URL)
            .refundUrl(DEFAULT_REFUND_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGatewayConfig createUpdatedEntity() {
        return new PaymentGatewayConfig()
            .name(UPDATED_NAME)
            .authUrl(UPDATED_AUTH_URL)
            .releaseUrl(UPDATED_RELEASE_URL)
            .refundUrl(UPDATED_REFUND_URL);
    }

    @BeforeEach
    void initTest() {
        paymentGatewayConfig = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentGatewayConfig != null) {
            paymentGatewayConfigRepository.delete(insertedPaymentGatewayConfig);
            insertedPaymentGatewayConfig = null;
        }
    }

    @Test
    @Transactional
    void createPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentGatewayConfig
        var returnedPaymentGatewayConfig = om.readValue(
            restPaymentGatewayConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayConfig)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentGatewayConfig.class
        );

        // Validate the PaymentGatewayConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentGatewayConfigUpdatableFieldsEquals(
            returnedPaymentGatewayConfig,
            getPersistedPaymentGatewayConfig(returnedPaymentGatewayConfig)
        );

        insertedPaymentGatewayConfig = returnedPaymentGatewayConfig;
    }

    @Test
    @Transactional
    void createPaymentGatewayConfigWithExistingId() throws Exception {
        // Create the PaymentGatewayConfig with an existing ID
        paymentGatewayConfig.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentGatewayConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentGatewayConfig.setName(null);

        // Create the PaymentGatewayConfig, which fails.

        restPaymentGatewayConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayConfig)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigs() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGatewayConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].authUrl").value(hasItem(DEFAULT_AUTH_URL)))
            .andExpect(jsonPath("$.[*].releaseUrl").value(hasItem(DEFAULT_RELEASE_URL)))
            .andExpect(jsonPath("$.[*].refundUrl").value(hasItem(DEFAULT_REFUND_URL)));
    }

    @Test
    @Transactional
    void getPaymentGatewayConfig() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get the paymentGatewayConfig
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentGatewayConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentGatewayConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.authUrl").value(DEFAULT_AUTH_URL))
            .andExpect(jsonPath("$.releaseUrl").value(DEFAULT_RELEASE_URL))
            .andExpect(jsonPath("$.refundUrl").value(DEFAULT_REFUND_URL));
    }

    @Test
    @Transactional
    void getPaymentGatewayConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        Long id = paymentGatewayConfig.getId();

        defaultPaymentGatewayConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentGatewayConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentGatewayConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where name equals to
        defaultPaymentGatewayConfigFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where name in
        defaultPaymentGatewayConfigFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where name is not null
        defaultPaymentGatewayConfigFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where name contains
        defaultPaymentGatewayConfigFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where name does not contain
        defaultPaymentGatewayConfigFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByAuthUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where authUrl equals to
        defaultPaymentGatewayConfigFiltering("authUrl.equals=" + DEFAULT_AUTH_URL, "authUrl.equals=" + UPDATED_AUTH_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByAuthUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where authUrl in
        defaultPaymentGatewayConfigFiltering("authUrl.in=" + DEFAULT_AUTH_URL + "," + UPDATED_AUTH_URL, "authUrl.in=" + UPDATED_AUTH_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByAuthUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where authUrl is not null
        defaultPaymentGatewayConfigFiltering("authUrl.specified=true", "authUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByAuthUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where authUrl contains
        defaultPaymentGatewayConfigFiltering("authUrl.contains=" + DEFAULT_AUTH_URL, "authUrl.contains=" + UPDATED_AUTH_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByAuthUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where authUrl does not contain
        defaultPaymentGatewayConfigFiltering("authUrl.doesNotContain=" + UPDATED_AUTH_URL, "authUrl.doesNotContain=" + DEFAULT_AUTH_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByReleaseUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where releaseUrl equals to
        defaultPaymentGatewayConfigFiltering("releaseUrl.equals=" + DEFAULT_RELEASE_URL, "releaseUrl.equals=" + UPDATED_RELEASE_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByReleaseUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where releaseUrl in
        defaultPaymentGatewayConfigFiltering(
            "releaseUrl.in=" + DEFAULT_RELEASE_URL + "," + UPDATED_RELEASE_URL,
            "releaseUrl.in=" + UPDATED_RELEASE_URL
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByReleaseUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where releaseUrl is not null
        defaultPaymentGatewayConfigFiltering("releaseUrl.specified=true", "releaseUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByReleaseUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where releaseUrl contains
        defaultPaymentGatewayConfigFiltering("releaseUrl.contains=" + DEFAULT_RELEASE_URL, "releaseUrl.contains=" + UPDATED_RELEASE_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByReleaseUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where releaseUrl does not contain
        defaultPaymentGatewayConfigFiltering(
            "releaseUrl.doesNotContain=" + UPDATED_RELEASE_URL,
            "releaseUrl.doesNotContain=" + DEFAULT_RELEASE_URL
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByRefundUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where refundUrl equals to
        defaultPaymentGatewayConfigFiltering("refundUrl.equals=" + DEFAULT_REFUND_URL, "refundUrl.equals=" + UPDATED_REFUND_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByRefundUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where refundUrl in
        defaultPaymentGatewayConfigFiltering(
            "refundUrl.in=" + DEFAULT_REFUND_URL + "," + UPDATED_REFUND_URL,
            "refundUrl.in=" + UPDATED_REFUND_URL
        );
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByRefundUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where refundUrl is not null
        defaultPaymentGatewayConfigFiltering("refundUrl.specified=true", "refundUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByRefundUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where refundUrl contains
        defaultPaymentGatewayConfigFiltering("refundUrl.contains=" + DEFAULT_REFUND_URL, "refundUrl.contains=" + UPDATED_REFUND_URL);
    }

    @Test
    @Transactional
    void getAllPaymentGatewayConfigsByRefundUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        // Get all the paymentGatewayConfigList where refundUrl does not contain
        defaultPaymentGatewayConfigFiltering(
            "refundUrl.doesNotContain=" + UPDATED_REFUND_URL,
            "refundUrl.doesNotContain=" + DEFAULT_REFUND_URL
        );
    }

    private void defaultPaymentGatewayConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentGatewayConfigShouldBeFound(shouldBeFound);
        defaultPaymentGatewayConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentGatewayConfigShouldBeFound(String filter) throws Exception {
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGatewayConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].authUrl").value(hasItem(DEFAULT_AUTH_URL)))
            .andExpect(jsonPath("$.[*].releaseUrl").value(hasItem(DEFAULT_RELEASE_URL)))
            .andExpect(jsonPath("$.[*].refundUrl").value(hasItem(DEFAULT_REFUND_URL)));

        // Check, that the count call also returns 1
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentGatewayConfigShouldNotBeFound(String filter) throws Exception {
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentGatewayConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentGatewayConfig() throws Exception {
        // Get the paymentGatewayConfig
        restPaymentGatewayConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentGatewayConfig() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayConfig
        PaymentGatewayConfig updatedPaymentGatewayConfig = paymentGatewayConfigRepository
            .findById(paymentGatewayConfig.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentGatewayConfig are not directly saved in db
        em.detach(updatedPaymentGatewayConfig);
        updatedPaymentGatewayConfig
            .name(UPDATED_NAME)
            .authUrl(UPDATED_AUTH_URL)
            .releaseUrl(UPDATED_RELEASE_URL)
            .refundUrl(UPDATED_REFUND_URL);

        restPaymentGatewayConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentGatewayConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentGatewayConfig))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentGatewayConfigToMatchAllProperties(updatedPaymentGatewayConfig);
    }

    @Test
    @Transactional
    void putNonExistingPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentGatewayConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGatewayConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentGatewayConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentGatewayConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentGatewayConfigWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayConfig using partial update
        PaymentGatewayConfig partialUpdatedPaymentGatewayConfig = new PaymentGatewayConfig();
        partialUpdatedPaymentGatewayConfig.setId(paymentGatewayConfig.getId());

        partialUpdatedPaymentGatewayConfig.refundUrl(UPDATED_REFUND_URL);

        restPaymentGatewayConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGatewayConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGatewayConfig))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGatewayConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentGatewayConfig, paymentGatewayConfig),
            getPersistedPaymentGatewayConfig(paymentGatewayConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentGatewayConfigWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentGatewayConfig using partial update
        PaymentGatewayConfig partialUpdatedPaymentGatewayConfig = new PaymentGatewayConfig();
        partialUpdatedPaymentGatewayConfig.setId(paymentGatewayConfig.getId());

        partialUpdatedPaymentGatewayConfig
            .name(UPDATED_NAME)
            .authUrl(UPDATED_AUTH_URL)
            .releaseUrl(UPDATED_RELEASE_URL)
            .refundUrl(UPDATED_REFUND_URL);

        restPaymentGatewayConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGatewayConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentGatewayConfig))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGatewayConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentGatewayConfigUpdatableFieldsEquals(
            partialUpdatedPaymentGatewayConfig,
            getPersistedPaymentGatewayConfig(partialUpdatedPaymentGatewayConfig)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentGatewayConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGatewayConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentGatewayConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentGatewayConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentGatewayConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentGatewayConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGatewayConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentGatewayConfig() throws Exception {
        // Initialize the database
        insertedPaymentGatewayConfig = paymentGatewayConfigRepository.saveAndFlush(paymentGatewayConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentGatewayConfig
        restPaymentGatewayConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentGatewayConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentGatewayConfigRepository.count();
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

    protected PaymentGatewayConfig getPersistedPaymentGatewayConfig(PaymentGatewayConfig paymentGatewayConfig) {
        return paymentGatewayConfigRepository.findById(paymentGatewayConfig.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentGatewayConfigToMatchAllProperties(PaymentGatewayConfig expectedPaymentGatewayConfig) {
        assertPaymentGatewayConfigAllPropertiesEquals(
            expectedPaymentGatewayConfig,
            getPersistedPaymentGatewayConfig(expectedPaymentGatewayConfig)
        );
    }

    protected void assertPersistedPaymentGatewayConfigToMatchUpdatableProperties(PaymentGatewayConfig expectedPaymentGatewayConfig) {
        assertPaymentGatewayConfigAllUpdatablePropertiesEquals(
            expectedPaymentGatewayConfig,
            getPersistedPaymentGatewayConfig(expectedPaymentGatewayConfig)
        );
    }
}
