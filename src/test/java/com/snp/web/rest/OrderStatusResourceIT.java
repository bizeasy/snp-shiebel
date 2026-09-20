package com.snp.web.rest;

import static com.snp.domain.OrderStatusAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Order;
import com.snp.domain.OrderStatus;
import com.snp.domain.Status;
import com.snp.repository.OrderStatusRepository;
import com.snp.service.OrderStatusService;
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
 * Integration tests for the {@link OrderStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderStatusResourceIT {

    private static final Instant DEFAULT_STATUS_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_DATE_TIME = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/order-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Mock
    private OrderStatusRepository orderStatusRepositoryMock;

    @Mock
    private OrderStatusService orderStatusServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderStatusMockMvc;

    private OrderStatus orderStatus;

    private OrderStatus insertedOrderStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatus createEntity(EntityManager em) {
        OrderStatus orderStatus = new OrderStatus().statusDateTime(DEFAULT_STATUS_DATE_TIME).sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        orderStatus.setOrder(order);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        orderStatus.setStatus(status);
        return orderStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatus createUpdatedEntity(EntityManager em) {
        OrderStatus updatedOrderStatus = new OrderStatus().statusDateTime(UPDATED_STATUS_DATE_TIME).sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        updatedOrderStatus.setOrder(order);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity();
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        updatedOrderStatus.setStatus(status);
        return updatedOrderStatus;
    }

    @BeforeEach
    void initTest() {
        orderStatus = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderStatus != null) {
            orderStatusRepository.delete(insertedOrderStatus);
            insertedOrderStatus = null;
        }
    }

    @Test
    @Transactional
    void createOrderStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderStatus
        var returnedOrderStatus = om.readValue(
            restOrderStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderStatus)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderStatus.class
        );

        // Validate the OrderStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderStatusUpdatableFieldsEquals(returnedOrderStatus, getPersistedOrderStatus(returnedOrderStatus));

        insertedOrderStatus = returnedOrderStatus;
    }

    @Test
    @Transactional
    void createOrderStatusWithExistingId() throws Exception {
        // Create the OrderStatus with an existing ID
        orderStatus.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderStatus)))
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderStatus.setStatusDateTime(null);

        // Create the OrderStatus, which fails.

        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderStatus)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderStatuses() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusDateTime").value(hasItem(DEFAULT_STATUS_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderStatusRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderStatus() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get the orderStatus
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, orderStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusDateTime").value(DEFAULT_STATUS_DATE_TIME.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getOrderStatusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        Long id = orderStatus.getId();

        defaultOrderStatusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderStatusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderStatusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByStatusDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where statusDateTime equals to
        defaultOrderStatusFiltering(
            "statusDateTime.equals=" + DEFAULT_STATUS_DATE_TIME,
            "statusDateTime.equals=" + UPDATED_STATUS_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOrderStatusesByStatusDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where statusDateTime in
        defaultOrderStatusFiltering(
            "statusDateTime.in=" + DEFAULT_STATUS_DATE_TIME + "," + UPDATED_STATUS_DATE_TIME,
            "statusDateTime.in=" + UPDATED_STATUS_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOrderStatusesByStatusDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where statusDateTime is not null
        defaultOrderStatusFiltering("statusDateTime.specified=true", "statusDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo equals to
        defaultOrderStatusFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo in
        defaultOrderStatusFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo is not null
        defaultOrderStatusFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo is greater than or equal to
        defaultOrderStatusFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo is less than or equal to
        defaultOrderStatusFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo is less than
        defaultOrderStatusFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderStatusesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where sequenceNo is greater than
        defaultOrderStatusFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderStatusRepository.saveAndFlush(orderStatus);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderStatus.setOrder(order);
        orderStatusRepository.saveAndFlush(orderStatus);
        Long orderId = order.getId();
        // Get all the orderStatusList where order equals to orderId
        defaultOrderStatusShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderStatusList where order equals to (orderId + 1)
        defaultOrderStatusShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderStatusesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            orderStatusRepository.saveAndFlush(orderStatus);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        orderStatus.setStatus(status);
        orderStatusRepository.saveAndFlush(orderStatus);
        Long statusId = status.getId();
        // Get all the orderStatusList where status equals to statusId
        defaultOrderStatusShouldBeFound("statusId.equals=" + statusId);

        // Get all the orderStatusList where status equals to (statusId + 1)
        defaultOrderStatusShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultOrderStatusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderStatusShouldBeFound(shouldBeFound);
        defaultOrderStatusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderStatusShouldBeFound(String filter) throws Exception {
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusDateTime").value(hasItem(DEFAULT_STATUS_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderStatusShouldNotBeFound(String filter) throws Exception {
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderStatus() throws Exception {
        // Get the orderStatus
        restOrderStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderStatus() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderStatus
        OrderStatus updatedOrderStatus = orderStatusRepository.findById(orderStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderStatus are not directly saved in db
        em.detach(updatedOrderStatus);
        updatedOrderStatus.statusDateTime(UPDATED_STATUS_DATE_TIME).sequenceNo(UPDATED_SEQUENCE_NO);

        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderStatusToMatchAllProperties(updatedOrderStatus);
    }

    @Test
    @Transactional
    void putNonExistingOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderStatusWithPatch() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderStatus using partial update
        OrderStatus partialUpdatedOrderStatus = new OrderStatus();
        partialUpdatedOrderStatus.setId(orderStatus.getId());

        partialUpdatedOrderStatus.statusDateTime(UPDATED_STATUS_DATE_TIME);

        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderStatus, orderStatus),
            getPersistedOrderStatus(orderStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderStatusWithPatch() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderStatus using partial update
        OrderStatus partialUpdatedOrderStatus = new OrderStatus();
        partialUpdatedOrderStatus.setId(orderStatus.getId());

        partialUpdatedOrderStatus.statusDateTime(UPDATED_STATUS_DATE_TIME).sequenceNo(UPDATED_SEQUENCE_NO);

        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderStatusUpdatableFieldsEquals(partialUpdatedOrderStatus, getPersistedOrderStatus(partialUpdatedOrderStatus));
    }

    @Test
    @Transactional
    void patchNonExistingOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderStatus() throws Exception {
        // Initialize the database
        insertedOrderStatus = orderStatusRepository.saveAndFlush(orderStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderStatus
        restOrderStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderStatusRepository.count();
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

    protected OrderStatus getPersistedOrderStatus(OrderStatus orderStatus) {
        return orderStatusRepository.findById(orderStatus.getId()).orElseThrow();
    }

    protected void assertPersistedOrderStatusToMatchAllProperties(OrderStatus expectedOrderStatus) {
        assertOrderStatusAllPropertiesEquals(expectedOrderStatus, getPersistedOrderStatus(expectedOrderStatus));
    }

    protected void assertPersistedOrderStatusToMatchUpdatableProperties(OrderStatus expectedOrderStatus) {
        assertOrderStatusAllUpdatablePropertiesEquals(expectedOrderStatus, getPersistedOrderStatus(expectedOrderStatus));
    }
}
