package com.snp.web.rest;

import static com.snp.domain.PartyIdentificationAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PartyIdentification;
import com.snp.domain.PartyIdentificationType;
import com.snp.repository.PartyIdentificationRepository;
import com.snp.service.PartyIdentificationService;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PartyIdentificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyIdentificationResourceIT {

    private static final String DEFAULT_ID_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ID_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-identifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyIdentificationRepository partyIdentificationRepository;

    @Mock
    private PartyIdentificationRepository partyIdentificationRepositoryMock;

    @Mock
    private PartyIdentificationService partyIdentificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyIdentificationMockMvc;

    private PartyIdentification partyIdentification;

    private PartyIdentification insertedPartyIdentification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyIdentification createEntity(EntityManager em) {
        PartyIdentification partyIdentification = new PartyIdentification().idValue(DEFAULT_ID_VALUE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        partyIdentification.setParty(party);
        // Add required entity
        PartyIdentificationType partyIdentificationType;
        if (TestUtil.findAll(em, PartyIdentificationType.class).isEmpty()) {
            partyIdentificationType = PartyIdentificationTypeResourceIT.createEntity();
            em.persist(partyIdentificationType);
            em.flush();
        } else {
            partyIdentificationType = TestUtil.findAll(em, PartyIdentificationType.class).getFirst();
        }
        partyIdentification.setType(partyIdentificationType);
        return partyIdentification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyIdentification createUpdatedEntity(EntityManager em) {
        PartyIdentification updatedPartyIdentification = new PartyIdentification().idValue(UPDATED_ID_VALUE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPartyIdentification.setParty(party);
        // Add required entity
        PartyIdentificationType partyIdentificationType;
        if (TestUtil.findAll(em, PartyIdentificationType.class).isEmpty()) {
            partyIdentificationType = PartyIdentificationTypeResourceIT.createUpdatedEntity();
            em.persist(partyIdentificationType);
            em.flush();
        } else {
            partyIdentificationType = TestUtil.findAll(em, PartyIdentificationType.class).getFirst();
        }
        updatedPartyIdentification.setType(partyIdentificationType);
        return updatedPartyIdentification;
    }

    @BeforeEach
    void initTest() {
        partyIdentification = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyIdentification != null) {
            partyIdentificationRepository.delete(insertedPartyIdentification);
            insertedPartyIdentification = null;
        }
    }

    @Test
    @Transactional
    void createPartyIdentification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyIdentification
        var returnedPartyIdentification = om.readValue(
            restPartyIdentificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentification)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyIdentification.class
        );

        // Validate the PartyIdentification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyIdentificationUpdatableFieldsEquals(
            returnedPartyIdentification,
            getPersistedPartyIdentification(returnedPartyIdentification)
        );

        insertedPartyIdentification = returnedPartyIdentification;
    }

    @Test
    @Transactional
    void createPartyIdentificationWithExistingId() throws Exception {
        // Create the PartyIdentification with an existing ID
        partyIdentification.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyIdentificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentification)))
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partyIdentification.setIdValue(null);

        // Create the PartyIdentification, which fails.

        restPartyIdentificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentification)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyIdentifications() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyIdentification.getId().intValue())))
            .andExpect(jsonPath("$.[*].idValue").value(hasItem(DEFAULT_ID_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyIdentificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyIdentificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyIdentificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyIdentificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyIdentificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyIdentificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyIdentificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyIdentificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyIdentification() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get the partyIdentification
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL_ID, partyIdentification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyIdentification.getId().intValue()))
            .andExpect(jsonPath("$.idValue").value(DEFAULT_ID_VALUE));
    }

    @Test
    @Transactional
    void getPartyIdentificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        Long id = partyIdentification.getId();

        defaultPartyIdentificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyIdentificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyIdentificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByIdValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList where idValue equals to
        defaultPartyIdentificationFiltering("idValue.equals=" + DEFAULT_ID_VALUE, "idValue.equals=" + UPDATED_ID_VALUE);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByIdValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList where idValue in
        defaultPartyIdentificationFiltering("idValue.in=" + DEFAULT_ID_VALUE + "," + UPDATED_ID_VALUE, "idValue.in=" + UPDATED_ID_VALUE);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByIdValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList where idValue is not null
        defaultPartyIdentificationFiltering("idValue.specified=true", "idValue.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByIdValueContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList where idValue contains
        defaultPartyIdentificationFiltering("idValue.contains=" + DEFAULT_ID_VALUE, "idValue.contains=" + UPDATED_ID_VALUE);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByIdValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        // Get all the partyIdentificationList where idValue does not contain
        defaultPartyIdentificationFiltering("idValue.doesNotContain=" + UPDATED_ID_VALUE, "idValue.doesNotContain=" + DEFAULT_ID_VALUE);
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyIdentificationRepository.saveAndFlush(partyIdentification);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        partyIdentification.setParty(party);
        partyIdentificationRepository.saveAndFlush(partyIdentification);
        Long partyId = party.getId();
        // Get all the partyIdentificationList where party equals to partyId
        defaultPartyIdentificationShouldBeFound("partyId.equals=" + partyId);

        // Get all the partyIdentificationList where party equals to (partyId + 1)
        defaultPartyIdentificationShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllPartyIdentificationsByTypeIsEqualToSomething() throws Exception {
        PartyIdentificationType type;
        if (TestUtil.findAll(em, PartyIdentificationType.class).isEmpty()) {
            partyIdentificationRepository.saveAndFlush(partyIdentification);
            type = PartyIdentificationTypeResourceIT.createEntity();
        } else {
            type = TestUtil.findAll(em, PartyIdentificationType.class).getFirst();
        }
        em.persist(type);
        em.flush();
        partyIdentification.setType(type);
        partyIdentificationRepository.saveAndFlush(partyIdentification);
        Long typeId = type.getId();
        // Get all the partyIdentificationList where type equals to typeId
        defaultPartyIdentificationShouldBeFound("typeId.equals=" + typeId);

        // Get all the partyIdentificationList where type equals to (typeId + 1)
        defaultPartyIdentificationShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    private void defaultPartyIdentificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyIdentificationShouldBeFound(shouldBeFound);
        defaultPartyIdentificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyIdentificationShouldBeFound(String filter) throws Exception {
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyIdentification.getId().intValue())))
            .andExpect(jsonPath("$.[*].idValue").value(hasItem(DEFAULT_ID_VALUE)));

        // Check, that the count call also returns 1
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyIdentificationShouldNotBeFound(String filter) throws Exception {
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyIdentificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyIdentification() throws Exception {
        // Get the partyIdentification
        restPartyIdentificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyIdentification() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentification
        PartyIdentification updatedPartyIdentification = partyIdentificationRepository.findById(partyIdentification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartyIdentification are not directly saved in db
        em.detach(updatedPartyIdentification);
        updatedPartyIdentification.idValue(UPDATED_ID_VALUE);

        restPartyIdentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyIdentification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyIdentification))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyIdentificationToMatchAllProperties(updatedPartyIdentification);
    }

    @Test
    @Transactional
    void putNonExistingPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyIdentification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyIdentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyIdentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyIdentification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyIdentificationWithPatch() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentification using partial update
        PartyIdentification partialUpdatedPartyIdentification = new PartyIdentification();
        partialUpdatedPartyIdentification.setId(partyIdentification.getId());

        restPartyIdentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyIdentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyIdentification))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyIdentificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyIdentification, partyIdentification),
            getPersistedPartyIdentification(partyIdentification)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyIdentificationWithPatch() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyIdentification using partial update
        PartyIdentification partialUpdatedPartyIdentification = new PartyIdentification();
        partialUpdatedPartyIdentification.setId(partyIdentification.getId());

        partialUpdatedPartyIdentification.idValue(UPDATED_ID_VALUE);

        restPartyIdentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyIdentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyIdentification))
            )
            .andExpect(status().isOk());

        // Validate the PartyIdentification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyIdentificationUpdatableFieldsEquals(
            partialUpdatedPartyIdentification,
            getPersistedPartyIdentification(partialUpdatedPartyIdentification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyIdentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyIdentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyIdentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyIdentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyIdentification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdentificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyIdentification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyIdentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyIdentification() throws Exception {
        // Initialize the database
        insertedPartyIdentification = partyIdentificationRepository.saveAndFlush(partyIdentification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyIdentification
        restPartyIdentificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyIdentification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyIdentificationRepository.count();
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

    protected PartyIdentification getPersistedPartyIdentification(PartyIdentification partyIdentification) {
        return partyIdentificationRepository.findById(partyIdentification.getId()).orElseThrow();
    }

    protected void assertPersistedPartyIdentificationToMatchAllProperties(PartyIdentification expectedPartyIdentification) {
        assertPartyIdentificationAllPropertiesEquals(
            expectedPartyIdentification,
            getPersistedPartyIdentification(expectedPartyIdentification)
        );
    }

    protected void assertPersistedPartyIdentificationToMatchUpdatableProperties(PartyIdentification expectedPartyIdentification) {
        assertPartyIdentificationAllUpdatablePropertiesEquals(
            expectedPartyIdentification,
            getPersistedPartyIdentification(expectedPartyIdentification)
        );
    }
}
