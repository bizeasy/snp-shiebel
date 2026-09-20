package com.snp.web.rest;

import static com.snp.domain.CashBookAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.CashBook;
import com.snp.domain.CashBookAttachment;
import com.snp.repository.CashBookAttachmentRepository;
import com.snp.service.CashBookAttachmentService;
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
 * Integration tests for the {@link CashBookAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CashBookAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cash-book-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CashBookAttachmentRepository cashBookAttachmentRepository;

    @Mock
    private CashBookAttachmentRepository cashBookAttachmentRepositoryMock;

    @Mock
    private CashBookAttachmentService cashBookAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashBookAttachmentMockMvc;

    private CashBookAttachment cashBookAttachment;

    private CashBookAttachment insertedCashBookAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookAttachment createEntity(EntityManager em) {
        CashBookAttachment cashBookAttachment = new CashBookAttachment().name(DEFAULT_NAME);
        // Add required entity
        CashBook cashBook;
        if (TestUtil.findAll(em, CashBook.class).isEmpty()) {
            cashBook = CashBookResourceIT.createEntity(em);
            em.persist(cashBook);
            em.flush();
        } else {
            cashBook = TestUtil.findAll(em, CashBook.class).getFirst();
        }
        cashBookAttachment.setCashBook(cashBook);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        cashBookAttachment.setAttachment(attachment);
        return cashBookAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookAttachment createUpdatedEntity(EntityManager em) {
        CashBookAttachment updatedCashBookAttachment = new CashBookAttachment().name(UPDATED_NAME);
        // Add required entity
        CashBook cashBook;
        if (TestUtil.findAll(em, CashBook.class).isEmpty()) {
            cashBook = CashBookResourceIT.createUpdatedEntity(em);
            em.persist(cashBook);
            em.flush();
        } else {
            cashBook = TestUtil.findAll(em, CashBook.class).getFirst();
        }
        updatedCashBookAttachment.setCashBook(cashBook);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedCashBookAttachment.setAttachment(attachment);
        return updatedCashBookAttachment;
    }

    @BeforeEach
    void initTest() {
        cashBookAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCashBookAttachment != null) {
            cashBookAttachmentRepository.delete(insertedCashBookAttachment);
            insertedCashBookAttachment = null;
        }
    }

    @Test
    @Transactional
    void createCashBookAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CashBookAttachment
        var returnedCashBookAttachment = om.readValue(
            restCashBookAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBookAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CashBookAttachment.class
        );

        // Validate the CashBookAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCashBookAttachmentUpdatableFieldsEquals(
            returnedCashBookAttachment,
            getPersistedCashBookAttachment(returnedCashBookAttachment)
        );

        insertedCashBookAttachment = returnedCashBookAttachment;
    }

    @Test
    @Transactional
    void createCashBookAttachmentWithExistingId() throws Exception {
        // Create the CashBookAttachment with an existing ID
        cashBookAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBookAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCashBookAttachments() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCashBookAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cashBookAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCashBookAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cashBookAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCashBookAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cashBookAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCashBookAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cashBookAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCashBookAttachment() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get the cashBookAttachment
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, cashBookAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashBookAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCashBookAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        Long id = cashBookAttachment.getId();

        defaultCashBookAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCashBookAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCashBookAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList where name equals to
        defaultCashBookAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList where name in
        defaultCashBookAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList where name is not null
        defaultCashBookAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList where name contains
        defaultCashBookAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        // Get all the cashBookAttachmentList where name does not contain
        defaultCashBookAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByCashBookIsEqualToSomething() throws Exception {
        CashBook cashBook;
        if (TestUtil.findAll(em, CashBook.class).isEmpty()) {
            cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);
            cashBook = CashBookResourceIT.createEntity(em);
        } else {
            cashBook = TestUtil.findAll(em, CashBook.class).getFirst();
        }
        em.persist(cashBook);
        em.flush();
        cashBookAttachment.setCashBook(cashBook);
        cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);
        Long cashBookId = cashBook.getId();
        // Get all the cashBookAttachmentList where cashBook equals to cashBookId
        defaultCashBookAttachmentShouldBeFound("cashBookId.equals=" + cashBookId);

        // Get all the cashBookAttachmentList where cashBook equals to (cashBookId + 1)
        defaultCashBookAttachmentShouldNotBeFound("cashBookId.equals=" + (cashBookId + 1));
    }

    @Test
    @Transactional
    void getAllCashBookAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        cashBookAttachment.setAttachment(attachment);
        cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);
        Long attachmentId = attachment.getId();
        // Get all the cashBookAttachmentList where attachment equals to attachmentId
        defaultCashBookAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the cashBookAttachmentList where attachment equals to (attachmentId + 1)
        defaultCashBookAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultCashBookAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCashBookAttachmentShouldBeFound(shouldBeFound);
        defaultCashBookAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookAttachmentShouldBeFound(String filter) throws Exception {
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookAttachmentShouldNotBeFound(String filter) throws Exception {
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCashBookAttachment() throws Exception {
        // Get the cashBookAttachment
        restCashBookAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCashBookAttachment() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBookAttachment
        CashBookAttachment updatedCashBookAttachment = cashBookAttachmentRepository.findById(cashBookAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCashBookAttachment are not directly saved in db
        em.detach(updatedCashBookAttachment);
        updatedCashBookAttachment.name(UPDATED_NAME);

        restCashBookAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashBookAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCashBookAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCashBookAttachmentToMatchAllProperties(updatedCashBookAttachment);
    }

    @Test
    @Transactional
    void putNonExistingCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashBookAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cashBookAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cashBookAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBookAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashBookAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBookAttachment using partial update
        CashBookAttachment partialUpdatedCashBookAttachment = new CashBookAttachment();
        partialUpdatedCashBookAttachment.setId(cashBookAttachment.getId());

        restCashBookAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBookAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashBookAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CashBookAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashBookAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCashBookAttachment, cashBookAttachment),
            getPersistedCashBookAttachment(cashBookAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCashBookAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBookAttachment using partial update
        CashBookAttachment partialUpdatedCashBookAttachment = new CashBookAttachment();
        partialUpdatedCashBookAttachment.setId(cashBookAttachment.getId());

        partialUpdatedCashBookAttachment.name(UPDATED_NAME);

        restCashBookAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBookAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashBookAttachment))
            )
            .andExpect(status().isOk());

        // Validate the CashBookAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashBookAttachmentUpdatableFieldsEquals(
            partialUpdatedCashBookAttachment,
            getPersistedCashBookAttachment(partialUpdatedCashBookAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashBookAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashBookAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashBookAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashBookAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBookAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cashBookAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBookAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashBookAttachment() throws Exception {
        // Initialize the database
        insertedCashBookAttachment = cashBookAttachmentRepository.saveAndFlush(cashBookAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cashBookAttachment
        restCashBookAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashBookAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cashBookAttachmentRepository.count();
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

    protected CashBookAttachment getPersistedCashBookAttachment(CashBookAttachment cashBookAttachment) {
        return cashBookAttachmentRepository.findById(cashBookAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedCashBookAttachmentToMatchAllProperties(CashBookAttachment expectedCashBookAttachment) {
        assertCashBookAttachmentAllPropertiesEquals(expectedCashBookAttachment, getPersistedCashBookAttachment(expectedCashBookAttachment));
    }

    protected void assertPersistedCashBookAttachmentToMatchUpdatableProperties(CashBookAttachment expectedCashBookAttachment) {
        assertCashBookAttachmentAllUpdatablePropertiesEquals(
            expectedCashBookAttachment,
            getPersistedCashBookAttachment(expectedCashBookAttachment)
        );
    }
}
