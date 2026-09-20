package com.snp.web.rest;

import static com.snp.domain.PaymentTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.PaymentType;
import com.snp.repository.PaymentTypeRepository;
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
 * Integration tests for the {@link PaymentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentTypeMockMvc;

    private PaymentType paymentType;

    private PaymentType insertedPaymentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentType createEntity() {
        return new PaymentType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentType createUpdatedEntity() {
        return new PaymentType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        paymentType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentType != null) {
            paymentTypeRepository.delete(insertedPaymentType);
            insertedPaymentType = null;
        }
    }

    @Test
    @Transactional
    void createPaymentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentType
        var returnedPaymentType = om.readValue(
            restPaymentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentType.class
        );

        // Validate the PaymentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentTypeUpdatableFieldsEquals(returnedPaymentType, getPersistedPaymentType(returnedPaymentType));

        insertedPaymentType = returnedPaymentType;
    }

    @Test
    @Transactional
    void createPaymentTypeWithExistingId() throws Exception {
        // Create the PaymentType with an existing ID
        paymentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentType)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentType.setName(null);

        // Create the PaymentType, which fails.

        restPaymentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentTypes() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPaymentType() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get the paymentType
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPaymentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        Long id = paymentType.getId();

        defaultPaymentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where name equals to
        defaultPaymentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where name in
        defaultPaymentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where name is not null
        defaultPaymentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where name contains
        defaultPaymentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where name does not contain
        defaultPaymentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where description equals to
        defaultPaymentTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where description in
        defaultPaymentTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPaymentTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where description is not null
        defaultPaymentTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where description contains
        defaultPaymentTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        // Get all the paymentTypeList where description does not contain
        defaultPaymentTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultPaymentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentTypeShouldBeFound(shouldBeFound);
        defaultPaymentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentTypeShouldBeFound(String filter) throws Exception {
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentTypeShouldNotBeFound(String filter) throws Exception {
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentType() throws Exception {
        // Get the paymentType
        restPaymentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentType() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentType
        PaymentType updatedPaymentType = paymentTypeRepository.findById(paymentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentType are not directly saved in db
        em.detach(updatedPaymentType);
        updatedPaymentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPaymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentTypeToMatchAllProperties(updatedPaymentType);
    }

    @Test
    @Transactional
    void putNonExistingPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentType using partial update
        PaymentType partialUpdatedPaymentType = new PaymentType();
        partialUpdatedPaymentType.setId(paymentType.getId());

        partialUpdatedPaymentType.description(UPDATED_DESCRIPTION);

        restPaymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentType, paymentType),
            getPersistedPaymentType(paymentType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentType using partial update
        PaymentType partialUpdatedPaymentType = new PaymentType();
        partialUpdatedPaymentType.setId(paymentType.getId());

        partialUpdatedPaymentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPaymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentType))
            )
            .andExpect(status().isOk());

        // Validate the PaymentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentTypeUpdatableFieldsEquals(partialUpdatedPaymentType, getPersistedPaymentType(partialUpdatedPaymentType));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentType() throws Exception {
        // Initialize the database
        insertedPaymentType = paymentTypeRepository.saveAndFlush(paymentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentType
        restPaymentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentTypeRepository.count();
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

    protected PaymentType getPersistedPaymentType(PaymentType paymentType) {
        return paymentTypeRepository.findById(paymentType.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentTypeToMatchAllProperties(PaymentType expectedPaymentType) {
        assertPaymentTypeAllPropertiesEquals(expectedPaymentType, getPersistedPaymentType(expectedPaymentType));
    }

    protected void assertPersistedPaymentTypeToMatchUpdatableProperties(PaymentType expectedPaymentType) {
        assertPaymentTypeAllUpdatablePropertiesEquals(expectedPaymentType, getPersistedPaymentType(expectedPaymentType));
    }
}
