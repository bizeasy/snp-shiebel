package com.snp.web.rest;

import static com.snp.domain.OrderAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.Order;
import com.snp.domain.OrderAttachment;
import com.snp.repository.OrderAttachmentRepository;
import com.snp.service.OrderAttachmentService;
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
 * Integration tests for the {@link OrderAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderAttachmentRepository orderAttachmentRepository;

    @Mock
    private OrderAttachmentRepository orderAttachmentRepositoryMock;

    @Mock
    private OrderAttachmentService orderAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderAttachmentMockMvc;

    private OrderAttachment orderAttachment;

    private OrderAttachment insertedOrderAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAttachment createEntity(EntityManager em) {
        OrderAttachment orderAttachment = new OrderAttachment().name(DEFAULT_NAME);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        orderAttachment.setOrder(order);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        orderAttachment.setAttachment(attachment);
        return orderAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAttachment createUpdatedEntity(EntityManager em) {
        OrderAttachment updatedOrderAttachment = new OrderAttachment().name(UPDATED_NAME);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        updatedOrderAttachment.setOrder(order);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedOrderAttachment.setAttachment(attachment);
        return updatedOrderAttachment;
    }

    @BeforeEach
    void initTest() {
        orderAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderAttachment != null) {
            orderAttachmentRepository.delete(insertedOrderAttachment);
            insertedOrderAttachment = null;
        }
    }

    @Test
    @Transactional
    void createOrderAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderAttachment
        var returnedOrderAttachment = om.readValue(
            restOrderAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderAttachment.class
        );

        // Validate the OrderAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderAttachmentUpdatableFieldsEquals(returnedOrderAttachment, getPersistedOrderAttachment(returnedOrderAttachment));

        insertedOrderAttachment = returnedOrderAttachment;
    }

    @Test
    @Transactional
    void createOrderAttachmentWithExistingId() throws Exception {
        // Create the OrderAttachment with an existing ID
        orderAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderAttachments() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderAttachment() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get the orderAttachment
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, orderAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getOrderAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        Long id = orderAttachment.getId();

        defaultOrderAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList where name equals to
        defaultOrderAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList where name in
        defaultOrderAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList where name is not null
        defaultOrderAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList where name contains
        defaultOrderAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        // Get all the orderAttachmentList where name does not contain
        defaultOrderAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderAttachmentRepository.saveAndFlush(orderAttachment);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderAttachment.setOrder(order);
        orderAttachmentRepository.saveAndFlush(orderAttachment);
        Long orderId = order.getId();
        // Get all the orderAttachmentList where order equals to orderId
        defaultOrderAttachmentShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderAttachmentList where order equals to (orderId + 1)
        defaultOrderAttachmentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            orderAttachmentRepository.saveAndFlush(orderAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        orderAttachment.setAttachment(attachment);
        orderAttachmentRepository.saveAndFlush(orderAttachment);
        Long attachmentId = attachment.getId();
        // Get all the orderAttachmentList where attachment equals to attachmentId
        defaultOrderAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the orderAttachmentList where attachment equals to (attachmentId + 1)
        defaultOrderAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultOrderAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderAttachmentShouldBeFound(shouldBeFound);
        defaultOrderAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderAttachmentShouldBeFound(String filter) throws Exception {
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderAttachmentShouldNotBeFound(String filter) throws Exception {
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderAttachment() throws Exception {
        // Get the orderAttachment
        restOrderAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderAttachment() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAttachment
        OrderAttachment updatedOrderAttachment = orderAttachmentRepository.findById(orderAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderAttachment are not directly saved in db
        em.detach(updatedOrderAttachment);
        updatedOrderAttachment.name(UPDATED_NAME);

        restOrderAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderAttachmentToMatchAllProperties(updatedOrderAttachment);
    }

    @Test
    @Transactional
    void putNonExistingOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAttachment using partial update
        OrderAttachment partialUpdatedOrderAttachment = new OrderAttachment();
        partialUpdatedOrderAttachment.setId(orderAttachment.getId());

        partialUpdatedOrderAttachment.name(UPDATED_NAME);

        restOrderAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderAttachment, orderAttachment),
            getPersistedOrderAttachment(orderAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAttachment using partial update
        OrderAttachment partialUpdatedOrderAttachment = new OrderAttachment();
        partialUpdatedOrderAttachment.setId(orderAttachment.getId());

        partialUpdatedOrderAttachment.name(UPDATED_NAME);

        restOrderAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAttachment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAttachmentUpdatableFieldsEquals(
            partialUpdatedOrderAttachment,
            getPersistedOrderAttachment(partialUpdatedOrderAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderAttachment() throws Exception {
        // Initialize the database
        insertedOrderAttachment = orderAttachmentRepository.saveAndFlush(orderAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderAttachment
        restOrderAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderAttachmentRepository.count();
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

    protected OrderAttachment getPersistedOrderAttachment(OrderAttachment orderAttachment) {
        return orderAttachmentRepository.findById(orderAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedOrderAttachmentToMatchAllProperties(OrderAttachment expectedOrderAttachment) {
        assertOrderAttachmentAllPropertiesEquals(expectedOrderAttachment, getPersistedOrderAttachment(expectedOrderAttachment));
    }

    protected void assertPersistedOrderAttachmentToMatchUpdatableProperties(OrderAttachment expectedOrderAttachment) {
        assertOrderAttachmentAllUpdatablePropertiesEquals(expectedOrderAttachment, getPersistedOrderAttachment(expectedOrderAttachment));
    }
}
