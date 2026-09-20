package com.snp.web.rest;

import static com.snp.domain.CustRequestItemAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.CustRequestItem;
import com.snp.domain.CustRequestItemAttachment;
import com.snp.repository.CustRequestItemAttachmentRepository;
import com.snp.service.CustRequestItemAttachmentService;
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
 * Integration tests for the {@link CustRequestItemAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestItemAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cust-request-item-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestItemAttachmentRepository custRequestItemAttachmentRepository;

    @Mock
    private CustRequestItemAttachmentRepository custRequestItemAttachmentRepositoryMock;

    @Mock
    private CustRequestItemAttachmentService custRequestItemAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestItemAttachmentMockMvc;

    private CustRequestItemAttachment custRequestItemAttachment;

    private CustRequestItemAttachment insertedCustRequestItemAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestItemAttachment createEntity(EntityManager em) {
        CustRequestItemAttachment custRequestItemAttachment = new CustRequestItemAttachment().name(DEFAULT_NAME);
        // Add required entity
        CustRequestItem custRequestItem;
        if (TestUtil.findAll(em, CustRequestItem.class).isEmpty()) {
            custRequestItem = CustRequestItemResourceIT.createEntity(em);
            em.persist(custRequestItem);
            em.flush();
        } else {
            custRequestItem = TestUtil.findAll(em, CustRequestItem.class).getFirst();
        }
        custRequestItemAttachment.setCustRequestItem(custRequestItem);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        custRequestItemAttachment.setAttachment(attachment);
        return custRequestItemAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestItemAttachment createUpdatedEntity(EntityManager em) {
        CustRequestItemAttachment updatedCustRequestItemAttachment = new CustRequestItemAttachment().name(UPDATED_NAME);
        // Add required entity
        CustRequestItem custRequestItem;
        if (TestUtil.findAll(em, CustRequestItem.class).isEmpty()) {
            custRequestItem = CustRequestItemResourceIT.createUpdatedEntity(em);
            em.persist(custRequestItem);
            em.flush();
        } else {
            custRequestItem = TestUtil.findAll(em, CustRequestItem.class).getFirst();
        }
        updatedCustRequestItemAttachment.setCustRequestItem(custRequestItem);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedCustRequestItemAttachment.setAttachment(attachment);
        return updatedCustRequestItemAttachment;
    }

    @BeforeEach
    void initTest() {
        custRequestItemAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestItemAttachment != null) {
            custRequestItemAttachmentRepository.delete(insertedCustRequestItemAttachment);
            insertedCustRequestItemAttachment = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestItemAttachment
        var returnedCustRequestItemAttachment = om.readValue(
            restCustRequestItemAttachmentMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItemAttachment))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestItemAttachment.class
        );

        // Validate the CustRequestItemAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestItemAttachmentUpdatableFieldsEquals(
            returnedCustRequestItemAttachment,
            getPersistedCustRequestItemAttachment(returnedCustRequestItemAttachment)
        );

        insertedCustRequestItemAttachment = returnedCustRequestItemAttachment;
    }

    @Test
    @Transactional
    void createCustRequestItemAttachmentWithExistingId() throws Exception {
        // Create the CustRequestItemAttachment with an existing ID
        custRequestItemAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestItemAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItemAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachments() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestItemAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestItemAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestItemAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestItemAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestItemAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestItemAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestItemAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestItemAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestItemAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestItemAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get the custRequestItemAttachment
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestItemAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestItemAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCustRequestItemAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        Long id = custRequestItemAttachment.getId();

        defaultCustRequestItemAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestItemAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestItemAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList where name equals to
        defaultCustRequestItemAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList where name in
        defaultCustRequestItemAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList where name is not null
        defaultCustRequestItemAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList where name contains
        defaultCustRequestItemAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        // Get all the custRequestItemAttachmentList where name does not contain
        defaultCustRequestItemAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByCustRequestItemIsEqualToSomething() throws Exception {
        CustRequestItem custRequestItem;
        if (TestUtil.findAll(em, CustRequestItem.class).isEmpty()) {
            custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);
            custRequestItem = CustRequestItemResourceIT.createEntity(em);
        } else {
            custRequestItem = TestUtil.findAll(em, CustRequestItem.class).getFirst();
        }
        em.persist(custRequestItem);
        em.flush();
        custRequestItemAttachment.setCustRequestItem(custRequestItem);
        custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);
        Long custRequestItemId = custRequestItem.getId();
        // Get all the custRequestItemAttachmentList where custRequestItem equals to custRequestItemId
        defaultCustRequestItemAttachmentShouldBeFound("custRequestItemId.equals=" + custRequestItemId);

        // Get all the custRequestItemAttachmentList where custRequestItem equals to (custRequestItemId + 1)
        defaultCustRequestItemAttachmentShouldNotBeFound("custRequestItemId.equals=" + (custRequestItemId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestItemAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        custRequestItemAttachment.setAttachment(attachment);
        custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);
        Long attachmentId = attachment.getId();
        // Get all the custRequestItemAttachmentList where attachment equals to attachmentId
        defaultCustRequestItemAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the custRequestItemAttachmentList where attachment equals to (attachmentId + 1)
        defaultCustRequestItemAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultCustRequestItemAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestItemAttachmentShouldBeFound(shouldBeFound);
        defaultCustRequestItemAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestItemAttachmentShouldBeFound(String filter) throws Exception {
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestItemAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestItemAttachmentShouldNotBeFound(String filter) throws Exception {
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestItemAttachment() throws Exception {
        // Get the custRequestItemAttachment
        restCustRequestItemAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestItemAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItemAttachment
        CustRequestItemAttachment updatedCustRequestItemAttachment = custRequestItemAttachmentRepository
            .findById(custRequestItemAttachment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestItemAttachment are not directly saved in db
        em.detach(updatedCustRequestItemAttachment);
        updatedCustRequestItemAttachment.name(UPDATED_NAME);

        restCustRequestItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestItemAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestItemAttachmentToMatchAllProperties(updatedCustRequestItemAttachment);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestItemAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItemAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestItemAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItemAttachment using partial update
        CustRequestItemAttachment partialUpdatedCustRequestItemAttachment = new CustRequestItemAttachment();
        partialUpdatedCustRequestItemAttachment.setId(custRequestItemAttachment.getId());

        restCustRequestItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItemAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestItemAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestItemAttachment, custRequestItemAttachment),
            getPersistedCustRequestItemAttachment(custRequestItemAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestItemAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItemAttachment using partial update
        CustRequestItemAttachment partialUpdatedCustRequestItemAttachment = new CustRequestItemAttachment();
        partialUpdatedCustRequestItemAttachment.setId(custRequestItemAttachment.getId());

        partialUpdatedCustRequestItemAttachment.name(UPDATED_NAME);

        restCustRequestItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItemAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestItemAttachmentUpdatableFieldsEquals(
            partialUpdatedCustRequestItemAttachment,
            getPersistedCustRequestItemAttachment(partialUpdatedCustRequestItemAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestItemAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestItemAttachment() throws Exception {
        // Initialize the database
        insertedCustRequestItemAttachment = custRequestItemAttachmentRepository.saveAndFlush(custRequestItemAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestItemAttachment
        restCustRequestItemAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestItemAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestItemAttachmentRepository.count();
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

    protected CustRequestItemAttachment getPersistedCustRequestItemAttachment(CustRequestItemAttachment custRequestItemAttachment) {
        return custRequestItemAttachmentRepository.findById(custRequestItemAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestItemAttachmentToMatchAllProperties(
        CustRequestItemAttachment expectedCustRequestItemAttachment
    ) {
        assertCustRequestItemAttachmentAllPropertiesEquals(
            expectedCustRequestItemAttachment,
            getPersistedCustRequestItemAttachment(expectedCustRequestItemAttachment)
        );
    }

    protected void assertPersistedCustRequestItemAttachmentToMatchUpdatableProperties(
        CustRequestItemAttachment expectedCustRequestItemAttachment
    ) {
        assertCustRequestItemAttachmentAllUpdatablePropertiesEquals(
            expectedCustRequestItemAttachment,
            getPersistedCustRequestItemAttachment(expectedCustRequestItemAttachment)
        );
    }
}
