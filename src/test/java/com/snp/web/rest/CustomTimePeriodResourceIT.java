package com.snp.web.rest;

import static com.snp.domain.CustomTimePeriodAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustomTimePeriod;
import com.snp.repository.CustomTimePeriodRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link CustomTimePeriodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomTimePeriodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_THRU_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_THRU_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_THRU_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/custom-time-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomTimePeriodRepository customTimePeriodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomTimePeriodMockMvc;

    private CustomTimePeriod customTimePeriod;

    private CustomTimePeriod insertedCustomTimePeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomTimePeriod createEntity() {
        return new CustomTimePeriod().name(DEFAULT_NAME).fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomTimePeriod createUpdatedEntity() {
        return new CustomTimePeriod().name(UPDATED_NAME).fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
    }

    @BeforeEach
    void initTest() {
        customTimePeriod = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomTimePeriod != null) {
            customTimePeriodRepository.delete(insertedCustomTimePeriod);
            insertedCustomTimePeriod = null;
        }
    }

    @Test
    @Transactional
    void createCustomTimePeriod() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomTimePeriod
        var returnedCustomTimePeriod = om.readValue(
            restCustomTimePeriodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customTimePeriod)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomTimePeriod.class
        );

        // Validate the CustomTimePeriod in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomTimePeriodUpdatableFieldsEquals(returnedCustomTimePeriod, getPersistedCustomTimePeriod(returnedCustomTimePeriod));

        insertedCustomTimePeriod = returnedCustomTimePeriod;
    }

    @Test
    @Transactional
    void createCustomTimePeriodWithExistingId() throws Exception {
        // Create the CustomTimePeriod with an existing ID
        customTimePeriod.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomTimePeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customTimePeriod)))
            .andExpect(status().isBadRequest());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriods() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customTimePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @Test
    @Transactional
    void getCustomTimePeriod() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get the customTimePeriod
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, customTimePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customTimePeriod.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getCustomTimePeriodsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        Long id = customTimePeriod.getId();

        defaultCustomTimePeriodFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomTimePeriodFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomTimePeriodFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where name equals to
        defaultCustomTimePeriodFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where name in
        defaultCustomTimePeriodFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where name is not null
        defaultCustomTimePeriodFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where name contains
        defaultCustomTimePeriodFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where name does not contain
        defaultCustomTimePeriodFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate equals to
        defaultCustomTimePeriodFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate in
        defaultCustomTimePeriodFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate is not null
        defaultCustomTimePeriodFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate is greater than or equal to
        defaultCustomTimePeriodFiltering(
            "fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE,
            "fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate is less than or equal to
        defaultCustomTimePeriodFiltering("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate is less than
        defaultCustomTimePeriodFiltering("fromDate.lessThan=" + UPDATED_FROM_DATE, "fromDate.lessThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where fromDate is greater than
        defaultCustomTimePeriodFiltering("fromDate.greaterThan=" + SMALLER_FROM_DATE, "fromDate.greaterThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate equals to
        defaultCustomTimePeriodFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate in
        defaultCustomTimePeriodFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate is not null
        defaultCustomTimePeriodFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate is greater than or equal to
        defaultCustomTimePeriodFiltering(
            "thruDate.greaterThanOrEqual=" + DEFAULT_THRU_DATE,
            "thruDate.greaterThanOrEqual=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate is less than or equal to
        defaultCustomTimePeriodFiltering("thruDate.lessThanOrEqual=" + DEFAULT_THRU_DATE, "thruDate.lessThanOrEqual=" + SMALLER_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate is less than
        defaultCustomTimePeriodFiltering("thruDate.lessThan=" + UPDATED_THRU_DATE, "thruDate.lessThan=" + DEFAULT_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllCustomTimePeriodsByThruDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        // Get all the customTimePeriodList where thruDate is greater than
        defaultCustomTimePeriodFiltering("thruDate.greaterThan=" + SMALLER_THRU_DATE, "thruDate.greaterThan=" + DEFAULT_THRU_DATE);
    }

    private void defaultCustomTimePeriodFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomTimePeriodShouldBeFound(shouldBeFound);
        defaultCustomTimePeriodShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomTimePeriodShouldBeFound(String filter) throws Exception {
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customTimePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomTimePeriodShouldNotBeFound(String filter) throws Exception {
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomTimePeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomTimePeriod() throws Exception {
        // Get the customTimePeriod
        restCustomTimePeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomTimePeriod() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customTimePeriod
        CustomTimePeriod updatedCustomTimePeriod = customTimePeriodRepository.findById(customTimePeriod.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomTimePeriod are not directly saved in db
        em.detach(updatedCustomTimePeriod);
        updatedCustomTimePeriod.name(UPDATED_NAME).fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restCustomTimePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomTimePeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomTimePeriod))
            )
            .andExpect(status().isOk());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomTimePeriodToMatchAllProperties(updatedCustomTimePeriod);
    }

    @Test
    @Transactional
    void putNonExistingCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customTimePeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customTimePeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customTimePeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customTimePeriod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomTimePeriodWithPatch() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customTimePeriod using partial update
        CustomTimePeriod partialUpdatedCustomTimePeriod = new CustomTimePeriod();
        partialUpdatedCustomTimePeriod.setId(customTimePeriod.getId());

        partialUpdatedCustomTimePeriod.name(UPDATED_NAME).fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restCustomTimePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomTimePeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomTimePeriod))
            )
            .andExpect(status().isOk());

        // Validate the CustomTimePeriod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomTimePeriodUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomTimePeriod, customTimePeriod),
            getPersistedCustomTimePeriod(customTimePeriod)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomTimePeriodWithPatch() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customTimePeriod using partial update
        CustomTimePeriod partialUpdatedCustomTimePeriod = new CustomTimePeriod();
        partialUpdatedCustomTimePeriod.setId(customTimePeriod.getId());

        partialUpdatedCustomTimePeriod.name(UPDATED_NAME).fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restCustomTimePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomTimePeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomTimePeriod))
            )
            .andExpect(status().isOk());

        // Validate the CustomTimePeriod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomTimePeriodUpdatableFieldsEquals(
            partialUpdatedCustomTimePeriod,
            getPersistedCustomTimePeriod(partialUpdatedCustomTimePeriod)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customTimePeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customTimePeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customTimePeriod))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomTimePeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customTimePeriod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomTimePeriodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customTimePeriod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomTimePeriod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomTimePeriod() throws Exception {
        // Initialize the database
        insertedCustomTimePeriod = customTimePeriodRepository.saveAndFlush(customTimePeriod);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customTimePeriod
        restCustomTimePeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, customTimePeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customTimePeriodRepository.count();
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

    protected CustomTimePeriod getPersistedCustomTimePeriod(CustomTimePeriod customTimePeriod) {
        return customTimePeriodRepository.findById(customTimePeriod.getId()).orElseThrow();
    }

    protected void assertPersistedCustomTimePeriodToMatchAllProperties(CustomTimePeriod expectedCustomTimePeriod) {
        assertCustomTimePeriodAllPropertiesEquals(expectedCustomTimePeriod, getPersistedCustomTimePeriod(expectedCustomTimePeriod));
    }

    protected void assertPersistedCustomTimePeriodToMatchUpdatableProperties(CustomTimePeriod expectedCustomTimePeriod) {
        assertCustomTimePeriodAllUpdatablePropertiesEquals(
            expectedCustomTimePeriod,
            getPersistedCustomTimePeriod(expectedCustomTimePeriod)
        );
    }
}
