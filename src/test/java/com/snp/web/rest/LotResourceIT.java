package com.snp.web.rest;

import static com.snp.domain.LotAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Lot;
import com.snp.repository.LotRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LotResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_RETEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETEST_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/lots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotMockMvc;

    private Lot lot;

    private Lot insertedLot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lot createEntity() {
        return new Lot()
            .creationDate(DEFAULT_CREATION_DATE)
            .quantity(DEFAULT_QUANTITY)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .retestDate(DEFAULT_RETEST_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lot createUpdatedEntity() {
        return new Lot()
            .creationDate(UPDATED_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .retestDate(UPDATED_RETEST_DATE);
    }

    @BeforeEach
    void initTest() {
        lot = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLot != null) {
            lotRepository.delete(insertedLot);
            insertedLot = null;
        }
    }

    @Test
    @Transactional
    void createLot() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lot
        var returnedLot = om.readValue(
            restLotMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lot)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Lot.class
        );

        // Validate the Lot in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLotUpdatableFieldsEquals(returnedLot, getPersistedLot(returnedLot));

        insertedLot = returnedLot;
    }

    @Test
    @Transactional
    void createLotWithExistingId() throws Exception {
        // Create the Lot with an existing ID
        lot.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lot)))
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLots() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].retestDate").value(hasItem(DEFAULT_RETEST_DATE.toString())));
    }

    @Test
    @Transactional
    void getLot() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get the lot
        restLotMockMvc
            .perform(get(ENTITY_API_URL_ID, lot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lot.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.retestDate").value(DEFAULT_RETEST_DATE.toString()));
    }

    @Test
    @Transactional
    void getLotsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        Long id = lot.getId();

        defaultLotFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLotFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLotFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLotsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where creationDate equals to
        defaultLotFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllLotsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where creationDate in
        defaultLotFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLotsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where creationDate is not null
        defaultLotFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity equals to
        defaultLotFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity in
        defaultLotFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity is not null
        defaultLotFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity is greater than or equal to
        defaultLotFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity is less than or equal to
        defaultLotFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity is less than
        defaultLotFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where quantity is greater than
        defaultLotFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllLotsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where expirationDate equals to
        defaultLotFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllLotsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where expirationDate in
        defaultLotFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLotsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where expirationDate is not null
        defaultLotFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLotsByRetestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where retestDate equals to
        defaultLotFiltering("retestDate.equals=" + DEFAULT_RETEST_DATE, "retestDate.equals=" + UPDATED_RETEST_DATE);
    }

    @Test
    @Transactional
    void getAllLotsByRetestDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where retestDate in
        defaultLotFiltering("retestDate.in=" + DEFAULT_RETEST_DATE + "," + UPDATED_RETEST_DATE, "retestDate.in=" + UPDATED_RETEST_DATE);
    }

    @Test
    @Transactional
    void getAllLotsByRetestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        // Get all the lotList where retestDate is not null
        defaultLotFiltering("retestDate.specified=true", "retestDate.specified=false");
    }

    private void defaultLotFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLotShouldBeFound(shouldBeFound);
        defaultLotShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLotShouldBeFound(String filter) throws Exception {
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].retestDate").value(hasItem(DEFAULT_RETEST_DATE.toString())));

        // Check, that the count call also returns 1
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLotShouldNotBeFound(String filter) throws Exception {
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLot() throws Exception {
        // Get the lot
        restLotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLot() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lot
        Lot updatedLot = lotRepository.findById(lot.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLot are not directly saved in db
        em.detach(updatedLot);
        updatedLot
            .creationDate(UPDATED_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .retestDate(UPDATED_RETEST_DATE);

        restLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLot.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedLot))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLotToMatchAllProperties(updatedLot);
    }

    @Test
    @Transactional
    void putNonExistingLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(put(ENTITY_API_URL_ID, lot.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lot)))
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLotWithPatch() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lot using partial update
        Lot partialUpdatedLot = new Lot();
        partialUpdatedLot.setId(lot.getId());

        partialUpdatedLot
            .creationDate(UPDATED_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .retestDate(UPDATED_RETEST_DATE);

        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLot))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLotUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLot, lot), getPersistedLot(lot));
    }

    @Test
    @Transactional
    void fullUpdateLotWithPatch() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lot using partial update
        Lot partialUpdatedLot = new Lot();
        partialUpdatedLot.setId(lot.getId());

        partialUpdatedLot
            .creationDate(UPDATED_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .retestDate(UPDATED_RETEST_DATE);

        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLot))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLotUpdatableFieldsEquals(partialUpdatedLot, getPersistedLot(partialUpdatedLot));
    }

    @Test
    @Transactional
    void patchNonExistingLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(patch(ENTITY_API_URL_ID, lot.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lot)))
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLot() throws Exception {
        // Initialize the database
        insertedLot = lotRepository.saveAndFlush(lot);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lot
        restLotMockMvc.perform(delete(ENTITY_API_URL_ID, lot.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lotRepository.count();
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

    protected Lot getPersistedLot(Lot lot) {
        return lotRepository.findById(lot.getId()).orElseThrow();
    }

    protected void assertPersistedLotToMatchAllProperties(Lot expectedLot) {
        assertLotAllPropertiesEquals(expectedLot, getPersistedLot(expectedLot));
    }

    protected void assertPersistedLotToMatchUpdatableProperties(Lot expectedLot) {
        assertLotAllUpdatablePropertiesEquals(expectedLot, getPersistedLot(expectedLot));
    }
}
