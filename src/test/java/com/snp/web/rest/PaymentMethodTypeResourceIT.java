package com.snp.web.rest;

import static com.snp.domain.PaymentMethodTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentMethodTypeRepository;
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
 * Integration tests for the {@link PaymentMethodTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentMethodTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-method-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentMethodTypeRepository paymentMethodTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMethodTypeMockMvc;

    private PaymentMethodType paymentMethodType;

    private PaymentMethodType insertedPaymentMethodType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethodType createEntity() {
        return new PaymentMethodType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethodType createUpdatedEntity() {
        return new PaymentMethodType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        paymentMethodType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentMethodType != null) {
            paymentMethodTypeRepository.delete(insertedPaymentMethodType);
            insertedPaymentMethodType = null;
        }
    }

    @Test
    @Transactional
    void createPaymentMethodType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentMethodType
        var returnedPaymentMethodType = om.readValue(
            restPaymentMethodTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethodType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentMethodType.class
        );

        // Validate the PaymentMethodType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentMethodTypeUpdatableFieldsEquals(returnedPaymentMethodType, getPersistedPaymentMethodType(returnedPaymentMethodType));

        insertedPaymentMethodType = returnedPaymentMethodType;
    }

    @Test
    @Transactional
    void createPaymentMethodTypeWithExistingId() throws Exception {
        // Create the PaymentMethodType with an existing ID
        paymentMethodType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethodType)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentMethodType.setName(null);

        // Create the PaymentMethodType, which fails.

        restPaymentMethodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethodType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypes() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPaymentMethodType() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get the paymentMethodType
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentMethodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethodType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPaymentMethodTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        Long id = paymentMethodType.getId();

        defaultPaymentMethodTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentMethodTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentMethodTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where name equals to
        defaultPaymentMethodTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where name in
        defaultPaymentMethodTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where name is not null
        defaultPaymentMethodTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where name contains
        defaultPaymentMethodTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where name does not contain
        defaultPaymentMethodTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where description equals to
        defaultPaymentMethodTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where description in
        defaultPaymentMethodTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where description is not null
        defaultPaymentMethodTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where description contains
        defaultPaymentMethodTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        // Get all the paymentMethodTypeList where description does not contain
        defaultPaymentMethodTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultPaymentMethodTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentMethodTypeShouldBeFound(shouldBeFound);
        defaultPaymentMethodTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMethodTypeShouldBeFound(String filter) throws Exception {
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMethodTypeShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentMethodType() throws Exception {
        // Get the paymentMethodType
        restPaymentMethodTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentMethodType() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethodType
        PaymentMethodType updatedPaymentMethodType = paymentMethodTypeRepository.findById(paymentMethodType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentMethodType are not directly saved in db
        em.detach(updatedPaymentMethodType);
        updatedPaymentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPaymentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentMethodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentMethodTypeToMatchAllProperties(updatedPaymentMethodType);
    }

    @Test
    @Transactional
    void putNonExistingPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMethodType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentMethodType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentMethodTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethodType using partial update
        PaymentMethodType partialUpdatedPaymentMethodType = new PaymentMethodType();
        partialUpdatedPaymentMethodType.setId(paymentMethodType.getId());

        partialUpdatedPaymentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPaymentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethodType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMethodTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentMethodType, paymentMethodType),
            getPersistedPaymentMethodType(paymentMethodType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentMethodTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentMethodType using partial update
        PaymentMethodType partialUpdatedPaymentMethodType = new PaymentMethodType();
        partialUpdatedPaymentMethodType.setId(paymentMethodType.getId());

        partialUpdatedPaymentMethodType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPaymentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentMethodType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethodType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentMethodTypeUpdatableFieldsEquals(
            partialUpdatedPaymentMethodType,
            getPersistedPaymentMethodType(partialUpdatedPaymentMethodType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentMethodType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentMethodType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentMethodType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentMethodType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentMethodType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethodType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentMethodType() throws Exception {
        // Initialize the database
        insertedPaymentMethodType = paymentMethodTypeRepository.saveAndFlush(paymentMethodType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentMethodType
        restPaymentMethodTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentMethodType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentMethodTypeRepository.count();
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

    protected PaymentMethodType getPersistedPaymentMethodType(PaymentMethodType paymentMethodType) {
        return paymentMethodTypeRepository.findById(paymentMethodType.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentMethodTypeToMatchAllProperties(PaymentMethodType expectedPaymentMethodType) {
        assertPaymentMethodTypeAllPropertiesEquals(expectedPaymentMethodType, getPersistedPaymentMethodType(expectedPaymentMethodType));
    }

    protected void assertPersistedPaymentMethodTypeToMatchUpdatableProperties(PaymentMethodType expectedPaymentMethodType) {
        assertPaymentMethodTypeAllUpdatablePropertiesEquals(
            expectedPaymentMethodType,
            getPersistedPaymentMethodType(expectedPaymentMethodType)
        );
    }
}
