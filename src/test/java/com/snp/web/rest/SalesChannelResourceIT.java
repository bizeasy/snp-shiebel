package com.snp.web.rest;

import static com.snp.domain.SalesChannelAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.SalesChannel;
import com.snp.repository.SalesChannelRepository;
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
 * Integration tests for the {@link SalesChannelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalesChannelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sales-channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesChannelMockMvc;

    private SalesChannel salesChannel;

    private SalesChannel insertedSalesChannel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesChannel createEntity() {
        return new SalesChannel().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).sequenceNo(DEFAULT_SEQUENCE_NO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesChannel createUpdatedEntity() {
        return new SalesChannel().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);
    }

    @BeforeEach
    void initTest() {
        salesChannel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSalesChannel != null) {
            salesChannelRepository.delete(insertedSalesChannel);
            insertedSalesChannel = null;
        }
    }

    @Test
    @Transactional
    void createSalesChannel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SalesChannel
        var returnedSalesChannel = om.readValue(
            restSalesChannelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salesChannel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SalesChannel.class
        );

        // Validate the SalesChannel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSalesChannelUpdatableFieldsEquals(returnedSalesChannel, getPersistedSalesChannel(returnedSalesChannel));

        insertedSalesChannel = returnedSalesChannel;
    }

    @Test
    @Transactional
    void createSalesChannelWithExistingId() throws Exception {
        // Create the SalesChannel with an existing ID
        salesChannel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salesChannel)))
            .andExpect(status().isBadRequest());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salesChannel.setName(null);

        // Create the SalesChannel, which fails.

        restSalesChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salesChannel)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalesChannels() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @Test
    @Transactional
    void getSalesChannel() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get the salesChannel
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, salesChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesChannel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getSalesChannelsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        Long id = salesChannel.getId();

        defaultSalesChannelFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSalesChannelFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSalesChannelFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where name equals to
        defaultSalesChannelFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where name in
        defaultSalesChannelFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where name is not null
        defaultSalesChannelFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSalesChannelsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where name contains
        defaultSalesChannelFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where name does not contain
        defaultSalesChannelFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where description equals to
        defaultSalesChannelFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where description in
        defaultSalesChannelFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSalesChannelsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where description is not null
        defaultSalesChannelFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllSalesChannelsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where description contains
        defaultSalesChannelFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSalesChannelsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where description does not contain
        defaultSalesChannelFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo equals to
        defaultSalesChannelFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo in
        defaultSalesChannelFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo is not null
        defaultSalesChannelFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo is greater than or equal to
        defaultSalesChannelFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo is less than or equal to
        defaultSalesChannelFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo is less than
        defaultSalesChannelFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllSalesChannelsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        // Get all the salesChannelList where sequenceNo is greater than
        defaultSalesChannelFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    private void defaultSalesChannelFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSalesChannelShouldBeFound(shouldBeFound);
        defaultSalesChannelShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSalesChannelShouldBeFound(String filter) throws Exception {
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSalesChannelShouldNotBeFound(String filter) throws Exception {
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSalesChannelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSalesChannel() throws Exception {
        // Get the salesChannel
        restSalesChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalesChannel() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salesChannel
        SalesChannel updatedSalesChannel = salesChannelRepository.findById(salesChannel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalesChannel are not directly saved in db
        em.detach(updatedSalesChannel);
        updatedSalesChannel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restSalesChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalesChannel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSalesChannel))
            )
            .andExpect(status().isOk());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSalesChannelToMatchAllProperties(updatedSalesChannel);
    }

    @Test
    @Transactional
    void putNonExistingSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesChannel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salesChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salesChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salesChannel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalesChannelWithPatch() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salesChannel using partial update
        SalesChannel partialUpdatedSalesChannel = new SalesChannel();
        partialUpdatedSalesChannel.setId(salesChannel.getId());

        restSalesChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalesChannel))
            )
            .andExpect(status().isOk());

        // Validate the SalesChannel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalesChannelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSalesChannel, salesChannel),
            getPersistedSalesChannel(salesChannel)
        );
    }

    @Test
    @Transactional
    void fullUpdateSalesChannelWithPatch() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salesChannel using partial update
        SalesChannel partialUpdatedSalesChannel = new SalesChannel();
        partialUpdatedSalesChannel.setId(salesChannel.getId());

        partialUpdatedSalesChannel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restSalesChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalesChannel))
            )
            .andExpect(status().isOk());

        // Validate the SalesChannel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalesChannelUpdatableFieldsEquals(partialUpdatedSalesChannel, getPersistedSalesChannel(partialUpdatedSalesChannel));
    }

    @Test
    @Transactional
    void patchNonExistingSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salesChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salesChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salesChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalesChannel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salesChannel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesChannelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(salesChannel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesChannel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalesChannel() throws Exception {
        // Initialize the database
        insertedSalesChannel = salesChannelRepository.saveAndFlush(salesChannel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the salesChannel
        restSalesChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, salesChannel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return salesChannelRepository.count();
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

    protected SalesChannel getPersistedSalesChannel(SalesChannel salesChannel) {
        return salesChannelRepository.findById(salesChannel.getId()).orElseThrow();
    }

    protected void assertPersistedSalesChannelToMatchAllProperties(SalesChannel expectedSalesChannel) {
        assertSalesChannelAllPropertiesEquals(expectedSalesChannel, getPersistedSalesChannel(expectedSalesChannel));
    }

    protected void assertPersistedSalesChannelToMatchUpdatableProperties(SalesChannel expectedSalesChannel) {
        assertSalesChannelAllUpdatablePropertiesEquals(expectedSalesChannel, getPersistedSalesChannel(expectedSalesChannel));
    }
}
