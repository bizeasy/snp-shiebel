package com.snp.web.rest;

import static com.snp.domain.TaxSlabAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.TaxSlab;
import com.snp.repository.TaxSlabRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TaxSlabResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxSlabResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RATE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/tax-slabs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxSlabRepository taxSlabRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxSlabMockMvc;

    private TaxSlab taxSlab;

    private TaxSlab insertedTaxSlab;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxSlab createEntity() {
        return new TaxSlab().name(DEFAULT_NAME).rate(DEFAULT_RATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxSlab createUpdatedEntity() {
        return new TaxSlab().name(UPDATED_NAME).rate(UPDATED_RATE);
    }

    @BeforeEach
    void initTest() {
        taxSlab = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTaxSlab != null) {
            taxSlabRepository.delete(insertedTaxSlab);
            insertedTaxSlab = null;
        }
    }

    @Test
    @Transactional
    void createTaxSlab() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaxSlab
        var returnedTaxSlab = om.readValue(
            restTaxSlabMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaxSlab.class
        );

        // Validate the TaxSlab in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaxSlabUpdatableFieldsEquals(returnedTaxSlab, getPersistedTaxSlab(returnedTaxSlab));

        insertedTaxSlab = returnedTaxSlab;
    }

    @Test
    @Transactional
    void createTaxSlabWithExistingId() throws Exception {
        // Create the TaxSlab with an existing ID
        taxSlab.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxSlabMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isBadRequest());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxSlab.setName(null);

        // Create the TaxSlab, which fails.

        restTaxSlabMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxSlab.setRate(null);

        // Create the TaxSlab, which fails.

        restTaxSlabMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxSlabs() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxSlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))));
    }

    @Test
    @Transactional
    void getTaxSlab() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get the taxSlab
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL_ID, taxSlab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxSlab.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rate").value(sameNumber(DEFAULT_RATE)));
    }

    @Test
    @Transactional
    void getTaxSlabsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        Long id = taxSlab.getId();

        defaultTaxSlabFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaxSlabFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaxSlabFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where name equals to
        defaultTaxSlabFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where name in
        defaultTaxSlabFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where name is not null
        defaultTaxSlabFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxSlabsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where name contains
        defaultTaxSlabFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where name does not contain
        defaultTaxSlabFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate equals to
        defaultTaxSlabFiltering("rate.equals=" + DEFAULT_RATE, "rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate in
        defaultTaxSlabFiltering("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE, "rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate is not null
        defaultTaxSlabFiltering("rate.specified=true", "rate.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate is greater than or equal to
        defaultTaxSlabFiltering("rate.greaterThanOrEqual=" + DEFAULT_RATE, "rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate is less than or equal to
        defaultTaxSlabFiltering("rate.lessThanOrEqual=" + DEFAULT_RATE, "rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate is less than
        defaultTaxSlabFiltering("rate.lessThan=" + UPDATED_RATE, "rate.lessThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllTaxSlabsByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        // Get all the taxSlabList where rate is greater than
        defaultTaxSlabFiltering("rate.greaterThan=" + SMALLER_RATE, "rate.greaterThan=" + DEFAULT_RATE);
    }

    private void defaultTaxSlabFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaxSlabShouldBeFound(shouldBeFound);
        defaultTaxSlabShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxSlabShouldBeFound(String filter) throws Exception {
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxSlab.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))));

        // Check, that the count call also returns 1
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxSlabShouldNotBeFound(String filter) throws Exception {
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxSlabMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxSlab() throws Exception {
        // Get the taxSlab
        restTaxSlabMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxSlab() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxSlab
        TaxSlab updatedTaxSlab = taxSlabRepository.findById(taxSlab.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxSlab are not directly saved in db
        em.detach(updatedTaxSlab);
        updatedTaxSlab.name(UPDATED_NAME).rate(UPDATED_RATE);

        restTaxSlabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxSlab.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTaxSlab))
            )
            .andExpect(status().isOk());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxSlabToMatchAllProperties(updatedTaxSlab);
    }

    @Test
    @Transactional
    void putNonExistingTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(put(ENTITY_API_URL_ID, taxSlab.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isBadRequest());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxSlab))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxSlabWithPatch() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxSlab using partial update
        TaxSlab partialUpdatedTaxSlab = new TaxSlab();
        partialUpdatedTaxSlab.setId(taxSlab.getId());

        restTaxSlabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxSlab.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxSlab))
            )
            .andExpect(status().isOk());

        // Validate the TaxSlab in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxSlabUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTaxSlab, taxSlab), getPersistedTaxSlab(taxSlab));
    }

    @Test
    @Transactional
    void fullUpdateTaxSlabWithPatch() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxSlab using partial update
        TaxSlab partialUpdatedTaxSlab = new TaxSlab();
        partialUpdatedTaxSlab.setId(taxSlab.getId());

        partialUpdatedTaxSlab.name(UPDATED_NAME).rate(UPDATED_RATE);

        restTaxSlabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxSlab.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxSlab))
            )
            .andExpect(status().isOk());

        // Validate the TaxSlab in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxSlabUpdatableFieldsEquals(partialUpdatedTaxSlab, getPersistedTaxSlab(partialUpdatedTaxSlab));
    }

    @Test
    @Transactional
    void patchNonExistingTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxSlab.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxSlab))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxSlab))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxSlab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxSlab.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxSlabMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxSlab)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxSlab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxSlab() throws Exception {
        // Initialize the database
        insertedTaxSlab = taxSlabRepository.saveAndFlush(taxSlab);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taxSlab
        restTaxSlabMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxSlab.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taxSlabRepository.count();
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

    protected TaxSlab getPersistedTaxSlab(TaxSlab taxSlab) {
        return taxSlabRepository.findById(taxSlab.getId()).orElseThrow();
    }

    protected void assertPersistedTaxSlabToMatchAllProperties(TaxSlab expectedTaxSlab) {
        assertTaxSlabAllPropertiesEquals(expectedTaxSlab, getPersistedTaxSlab(expectedTaxSlab));
    }

    protected void assertPersistedTaxSlabToMatchUpdatableProperties(TaxSlab expectedTaxSlab) {
        assertTaxSlabAllUpdatablePropertiesEquals(expectedTaxSlab, getPersistedTaxSlab(expectedTaxSlab));
    }
}
