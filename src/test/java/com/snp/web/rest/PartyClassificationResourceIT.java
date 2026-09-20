package com.snp.web.rest;

import static com.snp.domain.PartyClassificationAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PartyClassification;
import com.snp.domain.PartyClassificationGroup;
import com.snp.repository.PartyClassificationRepository;
import com.snp.service.PartyClassificationService;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PartyClassificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyClassificationResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/party-classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyClassificationRepository partyClassificationRepository;

    @Mock
    private PartyClassificationRepository partyClassificationRepositoryMock;

    @Mock
    private PartyClassificationService partyClassificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyClassificationMockMvc;

    private PartyClassification partyClassification;

    private PartyClassification insertedPartyClassification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassification createEntity(EntityManager em) {
        PartyClassification partyClassification = new PartyClassification().fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        partyClassification.setParty(party);
        // Add required entity
        PartyClassificationGroup partyClassificationGroup;
        if (TestUtil.findAll(em, PartyClassificationGroup.class).isEmpty()) {
            partyClassificationGroup = PartyClassificationGroupResourceIT.createEntity();
            em.persist(partyClassificationGroup);
            em.flush();
        } else {
            partyClassificationGroup = TestUtil.findAll(em, PartyClassificationGroup.class).getFirst();
        }
        partyClassification.setClassificationGroup(partyClassificationGroup);
        return partyClassification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyClassification createUpdatedEntity(EntityManager em) {
        PartyClassification updatedPartyClassification = new PartyClassification().fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPartyClassification.setParty(party);
        // Add required entity
        PartyClassificationGroup partyClassificationGroup;
        if (TestUtil.findAll(em, PartyClassificationGroup.class).isEmpty()) {
            partyClassificationGroup = PartyClassificationGroupResourceIT.createUpdatedEntity();
            em.persist(partyClassificationGroup);
            em.flush();
        } else {
            partyClassificationGroup = TestUtil.findAll(em, PartyClassificationGroup.class).getFirst();
        }
        updatedPartyClassification.setClassificationGroup(partyClassificationGroup);
        return updatedPartyClassification;
    }

    @BeforeEach
    void initTest() {
        partyClassification = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyClassification != null) {
            partyClassificationRepository.delete(insertedPartyClassification);
            insertedPartyClassification = null;
        }
    }

    @Test
    @Transactional
    void createPartyClassification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyClassification
        var returnedPartyClassification = om.readValue(
            restPartyClassificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassification)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyClassification.class
        );

        // Validate the PartyClassification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyClassificationUpdatableFieldsEquals(
            returnedPartyClassification,
            getPersistedPartyClassification(returnedPartyClassification)
        );

        insertedPartyClassification = returnedPartyClassification;
    }

    @Test
    @Transactional
    void createPartyClassificationWithExistingId() throws Exception {
        // Create the PartyClassification with an existing ID
        partyClassification.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyClassificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassification)))
            .andExpect(status().isBadRequest());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyClassification.setFromDate(null);

        // Create the PartyClassification, which fails.

        restPartyClassificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassification)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyClassifications() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyClassificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyClassificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyClassificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyClassificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyClassificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyClassificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyClassificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyClassificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyClassification() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get the partyClassification
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL_ID, partyClassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyClassification.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getPartyClassificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        Long id = partyClassification.getId();

        defaultPartyClassificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyClassificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyClassificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where fromDate equals to
        defaultPartyClassificationFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where fromDate in
        defaultPartyClassificationFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where fromDate is not null
        defaultPartyClassificationFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where thruDate equals to
        defaultPartyClassificationFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where thruDate in
        defaultPartyClassificationFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        // Get all the partyClassificationList where thruDate is not null
        defaultPartyClassificationFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyClassificationRepository.saveAndFlush(partyClassification);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        partyClassification.setParty(party);
        partyClassificationRepository.saveAndFlush(partyClassification);
        Long partyId = party.getId();
        // Get all the partyClassificationList where party equals to partyId
        defaultPartyClassificationShouldBeFound("partyId.equals=" + partyId);

        // Get all the partyClassificationList where party equals to (partyId + 1)
        defaultPartyClassificationShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllPartyClassificationsByClassificationGroupIsEqualToSomething() throws Exception {
        PartyClassificationGroup classificationGroup;
        if (TestUtil.findAll(em, PartyClassificationGroup.class).isEmpty()) {
            partyClassificationRepository.saveAndFlush(partyClassification);
            classificationGroup = PartyClassificationGroupResourceIT.createEntity();
        } else {
            classificationGroup = TestUtil.findAll(em, PartyClassificationGroup.class).getFirst();
        }
        em.persist(classificationGroup);
        em.flush();
        partyClassification.setClassificationGroup(classificationGroup);
        partyClassificationRepository.saveAndFlush(partyClassification);
        Long classificationGroupId = classificationGroup.getId();
        // Get all the partyClassificationList where classificationGroup equals to classificationGroupId
        defaultPartyClassificationShouldBeFound("classificationGroupId.equals=" + classificationGroupId);

        // Get all the partyClassificationList where classificationGroup equals to (classificationGroupId + 1)
        defaultPartyClassificationShouldNotBeFound("classificationGroupId.equals=" + (classificationGroupId + 1));
    }

    private void defaultPartyClassificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyClassificationShouldBeFound(shouldBeFound);
        defaultPartyClassificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyClassificationShouldBeFound(String filter) throws Exception {
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyClassificationShouldNotBeFound(String filter) throws Exception {
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyClassification() throws Exception {
        // Get the partyClassification
        restPartyClassificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyClassification() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassification
        PartyClassification updatedPartyClassification = partyClassificationRepository.findById(partyClassification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartyClassification are not directly saved in db
        em.detach(updatedPartyClassification);
        updatedPartyClassification.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPartyClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyClassification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyClassification))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyClassificationToMatchAllProperties(updatedPartyClassification);
    }

    @Test
    @Transactional
    void putNonExistingPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyClassification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyClassification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyClassificationWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassification using partial update
        PartyClassification partialUpdatedPartyClassification = new PartyClassification();
        partialUpdatedPartyClassification.setId(partyClassification.getId());

        partialUpdatedPartyClassification.thruDate(UPDATED_THRU_DATE);

        restPartyClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassification))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyClassification, partyClassification),
            getPersistedPartyClassification(partyClassification)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyClassificationWithPatch() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyClassification using partial update
        PartyClassification partialUpdatedPartyClassification = new PartyClassification();
        partialUpdatedPartyClassification.setId(partyClassification.getId());

        partialUpdatedPartyClassification.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restPartyClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyClassification))
            )
            .andExpect(status().isOk());

        // Validate the PartyClassification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyClassificationUpdatableFieldsEquals(
            partialUpdatedPartyClassification,
            getPersistedPartyClassification(partialUpdatedPartyClassification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyClassification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyClassification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyClassificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyClassification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyClassification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyClassification() throws Exception {
        // Initialize the database
        insertedPartyClassification = partyClassificationRepository.saveAndFlush(partyClassification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyClassification
        restPartyClassificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyClassification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyClassificationRepository.count();
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

    protected PartyClassification getPersistedPartyClassification(PartyClassification partyClassification) {
        return partyClassificationRepository.findById(partyClassification.getId()).orElseThrow();
    }

    protected void assertPersistedPartyClassificationToMatchAllProperties(PartyClassification expectedPartyClassification) {
        assertPartyClassificationAllPropertiesEquals(
            expectedPartyClassification,
            getPersistedPartyClassification(expectedPartyClassification)
        );
    }

    protected void assertPersistedPartyClassificationToMatchUpdatableProperties(PartyClassification expectedPartyClassification) {
        assertPartyClassificationAllUpdatablePropertiesEquals(
            expectedPartyClassification,
            getPersistedPartyClassification(expectedPartyClassification)
        );
    }
}
