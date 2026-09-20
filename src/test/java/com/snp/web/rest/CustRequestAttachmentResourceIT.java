package com.snp.web.rest;

import static com.snp.domain.CustRequestAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.CustRequest;
import com.snp.domain.CustRequestAttachment;
import com.snp.repository.CustRequestAttachmentRepository;
import com.snp.service.CustRequestAttachmentService;
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
 * Integration tests for the {@link CustRequestAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cust-request-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestAttachmentRepository custRequestAttachmentRepository;

    @Mock
    private CustRequestAttachmentRepository custRequestAttachmentRepositoryMock;

    @Mock
    private CustRequestAttachmentService custRequestAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestAttachmentMockMvc;

    private CustRequestAttachment custRequestAttachment;

    private CustRequestAttachment insertedCustRequestAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestAttachment createEntity(EntityManager em) {
        CustRequestAttachment custRequestAttachment = new CustRequestAttachment().name(DEFAULT_NAME);
        // Add required entity
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequest = CustRequestResourceIT.createEntity(em);
            em.persist(custRequest);
            em.flush();
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        custRequestAttachment.setCustRequest(custRequest);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        custRequestAttachment.setAttachment(attachment);
        return custRequestAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestAttachment createUpdatedEntity(EntityManager em) {
        CustRequestAttachment updatedCustRequestAttachment = new CustRequestAttachment().name(UPDATED_NAME);
        // Add required entity
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequest = CustRequestResourceIT.createUpdatedEntity(em);
            em.persist(custRequest);
            em.flush();
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        updatedCustRequestAttachment.setCustRequest(custRequest);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedCustRequestAttachment.setAttachment(attachment);
        return updatedCustRequestAttachment;
    }

    @BeforeEach
    void initTest() {
        custRequestAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestAttachment != null) {
            custRequestAttachmentRepository.delete(insertedCustRequestAttachment);
            insertedCustRequestAttachment = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestAttachment
        var returnedCustRequestAttachment = om.readValue(
            restCustRequestAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestAttachment.class
        );

        // Validate the CustRequestAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestAttachmentUpdatableFieldsEquals(
            returnedCustRequestAttachment,
            getPersistedCustRequestAttachment(returnedCustRequestAttachment)
        );

        insertedCustRequestAttachment = returnedCustRequestAttachment;
    }

    @Test
    @Transactional
    void createCustRequestAttachmentWithExistingId() throws Exception {
        // Create the CustRequestAttachment with an existing ID
        custRequestAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachments() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get the custRequestAttachment
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCustRequestAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        Long id = custRequestAttachment.getId();

        defaultCustRequestAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList where name equals to
        defaultCustRequestAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList where name in
        defaultCustRequestAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList where name is not null
        defaultCustRequestAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList where name contains
        defaultCustRequestAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        // Get all the custRequestAttachmentList where name does not contain
        defaultCustRequestAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByCustRequestIsEqualToSomething() throws Exception {
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);
            custRequest = CustRequestResourceIT.createEntity(em);
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        em.persist(custRequest);
        em.flush();
        custRequestAttachment.setCustRequest(custRequest);
        custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);
        Long custRequestId = custRequest.getId();
        // Get all the custRequestAttachmentList where custRequest equals to custRequestId
        defaultCustRequestAttachmentShouldBeFound("custRequestId.equals=" + custRequestId);

        // Get all the custRequestAttachmentList where custRequest equals to (custRequestId + 1)
        defaultCustRequestAttachmentShouldNotBeFound("custRequestId.equals=" + (custRequestId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        custRequestAttachment.setAttachment(attachment);
        custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);
        Long attachmentId = attachment.getId();
        // Get all the custRequestAttachmentList where attachment equals to attachmentId
        defaultCustRequestAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the custRequestAttachmentList where attachment equals to (attachmentId + 1)
        defaultCustRequestAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultCustRequestAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestAttachmentShouldBeFound(shouldBeFound);
        defaultCustRequestAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestAttachmentShouldBeFound(String filter) throws Exception {
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestAttachmentShouldNotBeFound(String filter) throws Exception {
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestAttachment() throws Exception {
        // Get the custRequestAttachment
        restCustRequestAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestAttachment
        CustRequestAttachment updatedCustRequestAttachment = custRequestAttachmentRepository
            .findById(custRequestAttachment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestAttachment are not directly saved in db
        em.detach(updatedCustRequestAttachment);
        updatedCustRequestAttachment.name(UPDATED_NAME);

        restCustRequestAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestAttachmentToMatchAllProperties(updatedCustRequestAttachment);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestAttachment using partial update
        CustRequestAttachment partialUpdatedCustRequestAttachment = new CustRequestAttachment();
        partialUpdatedCustRequestAttachment.setId(custRequestAttachment.getId());

        partialUpdatedCustRequestAttachment.name(UPDATED_NAME);

        restCustRequestAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestAttachment, custRequestAttachment),
            getPersistedCustRequestAttachment(custRequestAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestAttachment using partial update
        CustRequestAttachment partialUpdatedCustRequestAttachment = new CustRequestAttachment();
        partialUpdatedCustRequestAttachment.setId(custRequestAttachment.getId());

        partialUpdatedCustRequestAttachment.name(UPDATED_NAME);

        restCustRequestAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestAttachmentUpdatableFieldsEquals(
            partialUpdatedCustRequestAttachment,
            getPersistedCustRequestAttachment(partialUpdatedCustRequestAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestAttachment = custRequestAttachmentRepository.saveAndFlush(custRequestAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestAttachment
        restCustRequestAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestAttachmentRepository.count();
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

    protected CustRequestAttachment getPersistedCustRequestAttachment(CustRequestAttachment custRequestAttachment) {
        return custRequestAttachmentRepository.findById(custRequestAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestAttachmentToMatchAllProperties(CustRequestAttachment expectedCustRequestAttachment) {
        assertCustRequestAttachmentAllPropertiesEquals(
            expectedCustRequestAttachment,
            getPersistedCustRequestAttachment(expectedCustRequestAttachment)
        );
    }

    protected void assertPersistedCustRequestAttachmentToMatchUpdatableProperties(CustRequestAttachment expectedCustRequestAttachment) {
        assertCustRequestAttachmentAllUpdatablePropertiesEquals(
            expectedCustRequestAttachment,
            getPersistedCustRequestAttachment(expectedCustRequestAttachment)
        );
    }
}
