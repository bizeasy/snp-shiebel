package com.snp.web.rest;

import static com.snp.domain.QuoteAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.Quote;
import com.snp.domain.QuoteAttachment;
import com.snp.repository.QuoteAttachmentRepository;
import com.snp.service.QuoteAttachmentService;
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
 * Integration tests for the {@link QuoteAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuoteAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quote-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteAttachmentRepository quoteAttachmentRepository;

    @Mock
    private QuoteAttachmentRepository quoteAttachmentRepositoryMock;

    @Mock
    private QuoteAttachmentService quoteAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteAttachmentMockMvc;

    private QuoteAttachment quoteAttachment;

    private QuoteAttachment insertedQuoteAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteAttachment createEntity(EntityManager em) {
        QuoteAttachment quoteAttachment = new QuoteAttachment().name(DEFAULT_NAME);
        // Add required entity
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quote = QuoteResourceIT.createEntity(em);
            em.persist(quote);
            em.flush();
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        quoteAttachment.setQuote(quote);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        quoteAttachment.setAttachment(attachment);
        return quoteAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteAttachment createUpdatedEntity(EntityManager em) {
        QuoteAttachment updatedQuoteAttachment = new QuoteAttachment().name(UPDATED_NAME);
        // Add required entity
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quote = QuoteResourceIT.createUpdatedEntity(em);
            em.persist(quote);
            em.flush();
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        updatedQuoteAttachment.setQuote(quote);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedQuoteAttachment.setAttachment(attachment);
        return updatedQuoteAttachment;
    }

    @BeforeEach
    void initTest() {
        quoteAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteAttachment != null) {
            quoteAttachmentRepository.delete(insertedQuoteAttachment);
            insertedQuoteAttachment = null;
        }
    }

    @Test
    @Transactional
    void createQuoteAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteAttachment
        var returnedQuoteAttachment = om.readValue(
            restQuoteAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteAttachment.class
        );

        // Validate the QuoteAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteAttachmentUpdatableFieldsEquals(returnedQuoteAttachment, getPersistedQuoteAttachment(returnedQuoteAttachment));

        insertedQuoteAttachment = returnedQuoteAttachment;
    }

    @Test
    @Transactional
    void createQuoteAttachmentWithExistingId() throws Exception {
        // Create the QuoteAttachment with an existing ID
        quoteAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuoteAttachments() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quoteAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quoteAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quoteAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quoteAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuoteAttachment() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get the quoteAttachment
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getQuoteAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        Long id = quoteAttachment.getId();

        defaultQuoteAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList where name equals to
        defaultQuoteAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList where name in
        defaultQuoteAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList where name is not null
        defaultQuoteAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList where name contains
        defaultQuoteAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        // Get all the quoteAttachmentList where name does not contain
        defaultQuoteAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByQuoteIsEqualToSomething() throws Exception {
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quoteAttachmentRepository.saveAndFlush(quoteAttachment);
            quote = QuoteResourceIT.createEntity(em);
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        em.persist(quote);
        em.flush();
        quoteAttachment.setQuote(quote);
        quoteAttachmentRepository.saveAndFlush(quoteAttachment);
        Long quoteId = quote.getId();
        // Get all the quoteAttachmentList where quote equals to quoteId
        defaultQuoteAttachmentShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the quoteAttachmentList where quote equals to (quoteId + 1)
        defaultQuoteAttachmentShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            quoteAttachmentRepository.saveAndFlush(quoteAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        quoteAttachment.setAttachment(attachment);
        quoteAttachmentRepository.saveAndFlush(quoteAttachment);
        Long attachmentId = attachment.getId();
        // Get all the quoteAttachmentList where attachment equals to attachmentId
        defaultQuoteAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the quoteAttachmentList where attachment equals to (attachmentId + 1)
        defaultQuoteAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultQuoteAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteAttachmentShouldBeFound(shouldBeFound);
        defaultQuoteAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteAttachmentShouldBeFound(String filter) throws Exception {
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteAttachmentShouldNotBeFound(String filter) throws Exception {
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuoteAttachment() throws Exception {
        // Get the quoteAttachment
        restQuoteAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteAttachment() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAttachment
        QuoteAttachment updatedQuoteAttachment = quoteAttachmentRepository.findById(quoteAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteAttachment are not directly saved in db
        em.detach(updatedQuoteAttachment);
        updatedQuoteAttachment.name(UPDATED_NAME);

        restQuoteAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteAttachment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteAttachmentToMatchAllProperties(updatedQuoteAttachment);
    }

    @Test
    @Transactional
    void putNonExistingQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAttachment using partial update
        QuoteAttachment partialUpdatedQuoteAttachment = new QuoteAttachment();
        partialUpdatedQuoteAttachment.setId(quoteAttachment.getId());

        partialUpdatedQuoteAttachment.name(UPDATED_NAME);

        restQuoteAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteAttachment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteAttachment, quoteAttachment),
            getPersistedQuoteAttachment(quoteAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAttachment using partial update
        QuoteAttachment partialUpdatedQuoteAttachment = new QuoteAttachment();
        partialUpdatedQuoteAttachment.setId(quoteAttachment.getId());

        partialUpdatedQuoteAttachment.name(UPDATED_NAME);

        restQuoteAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteAttachment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteAttachmentUpdatableFieldsEquals(
            partialUpdatedQuoteAttachment,
            getPersistedQuoteAttachment(partialUpdatedQuoteAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteAttachment() throws Exception {
        // Initialize the database
        insertedQuoteAttachment = quoteAttachmentRepository.saveAndFlush(quoteAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteAttachment
        restQuoteAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteAttachmentRepository.count();
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

    protected QuoteAttachment getPersistedQuoteAttachment(QuoteAttachment quoteAttachment) {
        return quoteAttachmentRepository.findById(quoteAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteAttachmentToMatchAllProperties(QuoteAttachment expectedQuoteAttachment) {
        assertQuoteAttachmentAllPropertiesEquals(expectedQuoteAttachment, getPersistedQuoteAttachment(expectedQuoteAttachment));
    }

    protected void assertPersistedQuoteAttachmentToMatchUpdatableProperties(QuoteAttachment expectedQuoteAttachment) {
        assertQuoteAttachmentAllUpdatablePropertiesEquals(expectedQuoteAttachment, getPersistedQuoteAttachment(expectedQuoteAttachment));
    }
}
