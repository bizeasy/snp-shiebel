package com.snp.web.rest;

import static com.snp.domain.OrderItemAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.OrderItem;
import com.snp.domain.OrderItemAttachment;
import com.snp.repository.OrderItemAttachmentRepository;
import com.snp.service.OrderItemAttachmentService;
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
 * Integration tests for the {@link OrderItemAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderItemAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-item-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderItemAttachmentRepository orderItemAttachmentRepository;

    @Mock
    private OrderItemAttachmentRepository orderItemAttachmentRepositoryMock;

    @Mock
    private OrderItemAttachmentService orderItemAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemAttachmentMockMvc;

    private OrderItemAttachment orderItemAttachment;

    private OrderItemAttachment insertedOrderItemAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemAttachment createEntity(EntityManager em) {
        OrderItemAttachment orderItemAttachment = new OrderItemAttachment().name(DEFAULT_NAME);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        orderItemAttachment.setOrderItem(orderItem);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        orderItemAttachment.setAttachment(attachment);
        return orderItemAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemAttachment createUpdatedEntity(EntityManager em) {
        OrderItemAttachment updatedOrderItemAttachment = new OrderItemAttachment().name(UPDATED_NAME);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createUpdatedEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        updatedOrderItemAttachment.setOrderItem(orderItem);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedOrderItemAttachment.setAttachment(attachment);
        return updatedOrderItemAttachment;
    }

    @BeforeEach
    void initTest() {
        orderItemAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderItemAttachment != null) {
            orderItemAttachmentRepository.delete(insertedOrderItemAttachment);
            insertedOrderItemAttachment = null;
        }
    }

    @Test
    @Transactional
    void createOrderItemAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderItemAttachment
        var returnedOrderItemAttachment = om.readValue(
            restOrderItemAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderItemAttachment.class
        );

        // Validate the OrderItemAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderItemAttachmentUpdatableFieldsEquals(
            returnedOrderItemAttachment,
            getPersistedOrderItemAttachment(returnedOrderItemAttachment)
        );

        insertedOrderItemAttachment = returnedOrderItemAttachment;
    }

    @Test
    @Transactional
    void createOrderItemAttachmentWithExistingId() throws Exception {
        // Create the OrderItemAttachment with an existing ID
        orderItemAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachments() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderItemAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderItemAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderItemAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderItemAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderItemAttachment() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get the orderItemAttachment
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getOrderItemAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        Long id = orderItemAttachment.getId();

        defaultOrderItemAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderItemAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderItemAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList where name equals to
        defaultOrderItemAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList where name in
        defaultOrderItemAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList where name is not null
        defaultOrderItemAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList where name contains
        defaultOrderItemAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        // Get all the orderItemAttachmentList where name does not contain
        defaultOrderItemAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        orderItemAttachment.setOrderItem(orderItem);
        orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);
        Long orderItemId = orderItem.getId();
        // Get all the orderItemAttachmentList where orderItem equals to orderItemId
        defaultOrderItemAttachmentShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderItemAttachmentList where orderItem equals to (orderItemId + 1)
        defaultOrderItemAttachmentShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        orderItemAttachment.setAttachment(attachment);
        orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);
        Long attachmentId = attachment.getId();
        // Get all the orderItemAttachmentList where attachment equals to attachmentId
        defaultOrderItemAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the orderItemAttachmentList where attachment equals to (attachmentId + 1)
        defaultOrderItemAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultOrderItemAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderItemAttachmentShouldBeFound(shouldBeFound);
        defaultOrderItemAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemAttachmentShouldBeFound(String filter) throws Exception {
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemAttachmentShouldNotBeFound(String filter) throws Exception {
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemAttachment() throws Exception {
        // Get the orderItemAttachment
        restOrderItemAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItemAttachment() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemAttachment
        OrderItemAttachment updatedOrderItemAttachment = orderItemAttachmentRepository.findById(orderItemAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItemAttachment are not directly saved in db
        em.detach(updatedOrderItemAttachment);
        updatedOrderItemAttachment.name(UPDATED_NAME);

        restOrderItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderItemAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderItemAttachmentToMatchAllProperties(updatedOrderItemAttachment);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemAttachment using partial update
        OrderItemAttachment partialUpdatedOrderItemAttachment = new OrderItemAttachment();
        partialUpdatedOrderItemAttachment.setId(orderItemAttachment.getId());

        partialUpdatedOrderItemAttachment.name(UPDATED_NAME);

        restOrderItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderItemAttachment, orderItemAttachment),
            getPersistedOrderItemAttachment(orderItemAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderItemAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemAttachment using partial update
        OrderItemAttachment partialUpdatedOrderItemAttachment = new OrderItemAttachment();
        partialUpdatedOrderItemAttachment.setId(orderItemAttachment.getId());

        partialUpdatedOrderItemAttachment.name(UPDATED_NAME);

        restOrderItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemAttachmentUpdatableFieldsEquals(
            partialUpdatedOrderItemAttachment,
            getPersistedOrderItemAttachment(partialUpdatedOrderItemAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderItemAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItemAttachment() throws Exception {
        // Initialize the database
        insertedOrderItemAttachment = orderItemAttachmentRepository.saveAndFlush(orderItemAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderItemAttachment
        restOrderItemAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderItemAttachmentRepository.count();
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

    protected OrderItemAttachment getPersistedOrderItemAttachment(OrderItemAttachment orderItemAttachment) {
        return orderItemAttachmentRepository.findById(orderItemAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedOrderItemAttachmentToMatchAllProperties(OrderItemAttachment expectedOrderItemAttachment) {
        assertOrderItemAttachmentAllPropertiesEquals(
            expectedOrderItemAttachment,
            getPersistedOrderItemAttachment(expectedOrderItemAttachment)
        );
    }

    protected void assertPersistedOrderItemAttachmentToMatchUpdatableProperties(OrderItemAttachment expectedOrderItemAttachment) {
        assertOrderItemAttachmentAllUpdatablePropertiesEquals(
            expectedOrderItemAttachment,
            getPersistedOrderItemAttachment(expectedOrderItemAttachment)
        );
    }
}
