package com.snp.web.rest;

import static com.snp.domain.StatusValidChangeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Status;
import com.snp.domain.StatusValidChange;
import com.snp.repository.StatusValidChangeRepository;
import com.snp.service.StatusValidChangeService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link StatusValidChangeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatusValidChangeResourceIT {

    private static final String DEFAULT_TRANSITION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRANSITION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-valid-changes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatusValidChangeRepository statusValidChangeRepository;

    @Mock
    private StatusValidChangeRepository statusValidChangeRepositoryMock;

    @Mock
    private StatusValidChangeService statusValidChangeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusValidChangeMockMvc;

    private StatusValidChange statusValidChange;

    private StatusValidChange insertedStatusValidChange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusValidChange createEntity(EntityManager em) {
        StatusValidChange statusValidChange = new StatusValidChange().transitionName(DEFAULT_TRANSITION_NAME);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        statusValidChange.setStatus(status);
        // Add required entity
        statusValidChange.setStatusTo(status);
        return statusValidChange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusValidChange createUpdatedEntity(EntityManager em) {
        StatusValidChange updatedStatusValidChange = new StatusValidChange().transitionName(UPDATED_TRANSITION_NAME);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        updatedStatusValidChange.setStatus(status);
        // Add required entity
        updatedStatusValidChange.setStatusTo(status);
        return updatedStatusValidChange;
    }

    @BeforeEach
    void initTest() {
        statusValidChange = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedStatusValidChange != null) {
            statusValidChangeRepository.delete(insertedStatusValidChange);
            insertedStatusValidChange = null;
        }
    }

    @Test
    @Transactional
    void createStatusValidChange() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StatusValidChange
        var returnedStatusValidChange = om.readValue(
            restStatusValidChangeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusValidChange)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StatusValidChange.class
        );

        // Validate the StatusValidChange in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStatusValidChangeUpdatableFieldsEquals(returnedStatusValidChange, getPersistedStatusValidChange(returnedStatusValidChange));

        insertedStatusValidChange = returnedStatusValidChange;
    }

    @Test
    @Transactional
    void createStatusValidChangeWithExistingId() throws Exception {
        // Create the StatusValidChange with an existing ID
        statusValidChange.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusValidChangeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusValidChange)))
            .andExpect(status().isBadRequest());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStatusValidChanges() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusValidChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].transitionName").value(hasItem(DEFAULT_TRANSITION_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusValidChangesWithEagerRelationshipsIsEnabled() throws Exception {
        when(statusValidChangeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restStatusValidChangeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statusValidChangeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusValidChangesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statusValidChangeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restStatusValidChangeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(statusValidChangeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStatusValidChange() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get the statusValidChange
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL_ID, statusValidChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusValidChange.getId().intValue()))
            .andExpect(jsonPath("$.transitionName").value(DEFAULT_TRANSITION_NAME));
    }

    @Test
    @Transactional
    void getStatusValidChangesByIdFiltering() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        Long id = statusValidChange.getId();

        defaultStatusValidChangeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStatusValidChangeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStatusValidChangeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByTransitionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList where transitionName equals to
        defaultStatusValidChangeFiltering(
            "transitionName.equals=" + DEFAULT_TRANSITION_NAME,
            "transitionName.equals=" + UPDATED_TRANSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByTransitionNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList where transitionName in
        defaultStatusValidChangeFiltering(
            "transitionName.in=" + DEFAULT_TRANSITION_NAME + "," + UPDATED_TRANSITION_NAME,
            "transitionName.in=" + UPDATED_TRANSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByTransitionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList where transitionName is not null
        defaultStatusValidChangeFiltering("transitionName.specified=true", "transitionName.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByTransitionNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList where transitionName contains
        defaultStatusValidChangeFiltering(
            "transitionName.contains=" + DEFAULT_TRANSITION_NAME,
            "transitionName.contains=" + UPDATED_TRANSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByTransitionNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        // Get all the statusValidChangeList where transitionName does not contain
        defaultStatusValidChangeFiltering(
            "transitionName.doesNotContain=" + UPDATED_TRANSITION_NAME,
            "transitionName.doesNotContain=" + DEFAULT_TRANSITION_NAME
        );
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            statusValidChangeRepository.saveAndFlush(statusValidChange);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        statusValidChange.setStatus(status);
        statusValidChangeRepository.saveAndFlush(statusValidChange);
        Long statusId = status.getId();
        // Get all the statusValidChangeList where status equals to statusId
        defaultStatusValidChangeShouldBeFound("statusId.equals=" + statusId);

        // Get all the statusValidChangeList where status equals to (statusId + 1)
        defaultStatusValidChangeShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllStatusValidChangesByStatusToIsEqualToSomething() throws Exception {
        Status statusTo;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            statusValidChangeRepository.saveAndFlush(statusValidChange);
            statusTo = StatusResourceIT.createEntity();
        } else {
            statusTo = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(statusTo);
        em.flush();
        statusValidChange.setStatusTo(statusTo);
        statusValidChangeRepository.saveAndFlush(statusValidChange);
        Long statusToId = statusTo.getId();
        // Get all the statusValidChangeList where statusTo equals to statusToId
        defaultStatusValidChangeShouldBeFound("statusToId.equals=" + statusToId);

        // Get all the statusValidChangeList where statusTo equals to (statusToId + 1)
        defaultStatusValidChangeShouldNotBeFound("statusToId.equals=" + (statusToId + 1));
    }

    private void defaultStatusValidChangeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStatusValidChangeShouldBeFound(shouldBeFound);
        defaultStatusValidChangeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStatusValidChangeShouldBeFound(String filter) throws Exception {
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusValidChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].transitionName").value(hasItem(DEFAULT_TRANSITION_NAME)));

        // Check, that the count call also returns 1
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStatusValidChangeShouldNotBeFound(String filter) throws Exception {
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStatusValidChangeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStatusValidChange() throws Exception {
        // Get the statusValidChange
        restStatusValidChangeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatusValidChange() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusValidChange
        StatusValidChange updatedStatusValidChange = statusValidChangeRepository.findById(statusValidChange.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatusValidChange are not directly saved in db
        em.detach(updatedStatusValidChange);
        updatedStatusValidChange.transitionName(UPDATED_TRANSITION_NAME);

        restStatusValidChangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStatusValidChange.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStatusValidChange))
            )
            .andExpect(status().isOk());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatusValidChangeToMatchAllProperties(updatedStatusValidChange);
    }

    @Test
    @Transactional
    void putNonExistingStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusValidChange.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusValidChange))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusValidChange))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusValidChange)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusValidChangeWithPatch() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusValidChange using partial update
        StatusValidChange partialUpdatedStatusValidChange = new StatusValidChange();
        partialUpdatedStatusValidChange.setId(statusValidChange.getId());

        restStatusValidChangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusValidChange.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusValidChange))
            )
            .andExpect(status().isOk());

        // Validate the StatusValidChange in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusValidChangeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStatusValidChange, statusValidChange),
            getPersistedStatusValidChange(statusValidChange)
        );
    }

    @Test
    @Transactional
    void fullUpdateStatusValidChangeWithPatch() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusValidChange using partial update
        StatusValidChange partialUpdatedStatusValidChange = new StatusValidChange();
        partialUpdatedStatusValidChange.setId(statusValidChange.getId());

        partialUpdatedStatusValidChange.transitionName(UPDATED_TRANSITION_NAME);

        restStatusValidChangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusValidChange.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusValidChange))
            )
            .andExpect(status().isOk());

        // Validate the StatusValidChange in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusValidChangeUpdatableFieldsEquals(
            partialUpdatedStatusValidChange,
            getPersistedStatusValidChange(partialUpdatedStatusValidChange)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusValidChange.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusValidChange))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusValidChange))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatusValidChange() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusValidChange.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusValidChangeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(statusValidChange)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusValidChange in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatusValidChange() throws Exception {
        // Initialize the database
        insertedStatusValidChange = statusValidChangeRepository.saveAndFlush(statusValidChange);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the statusValidChange
        restStatusValidChangeMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusValidChange.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statusValidChangeRepository.count();
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

    protected StatusValidChange getPersistedStatusValidChange(StatusValidChange statusValidChange) {
        return statusValidChangeRepository.findById(statusValidChange.getId()).orElseThrow();
    }

    protected void assertPersistedStatusValidChangeToMatchAllProperties(StatusValidChange expectedStatusValidChange) {
        assertStatusValidChangeAllPropertiesEquals(expectedStatusValidChange, getPersistedStatusValidChange(expectedStatusValidChange));
    }

    protected void assertPersistedStatusValidChangeToMatchUpdatableProperties(StatusValidChange expectedStatusValidChange) {
        assertStatusValidChangeAllUpdatablePropertiesEquals(
            expectedStatusValidChange,
            getPersistedStatusValidChange(expectedStatusValidChange)
        );
    }
}
