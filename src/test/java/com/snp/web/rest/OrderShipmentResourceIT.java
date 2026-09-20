package com.snp.web.rest;

import static com.snp.domain.OrderShipmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.OrderShipment;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentItem;
import com.snp.repository.OrderShipmentRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link OrderShipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderShipmentResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/order-shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderShipmentRepository orderShipmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderShipmentMockMvc;

    private OrderShipment orderShipment;

    private OrderShipment insertedOrderShipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderShipment createEntity(EntityManager em) {
        OrderShipment orderShipment = new OrderShipment().quantity(DEFAULT_QUANTITY);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        orderShipment.setOrder(order);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        orderShipment.setOrderItem(orderItem);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        orderShipment.setShipment(shipment);
        return orderShipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderShipment createUpdatedEntity(EntityManager em) {
        OrderShipment updatedOrderShipment = new OrderShipment().quantity(UPDATED_QUANTITY);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        updatedOrderShipment.setOrder(order);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createUpdatedEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        updatedOrderShipment.setOrderItem(orderItem);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createUpdatedEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        updatedOrderShipment.setShipment(shipment);
        return updatedOrderShipment;
    }

    @BeforeEach
    void initTest() {
        orderShipment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderShipment != null) {
            orderShipmentRepository.delete(insertedOrderShipment);
            insertedOrderShipment = null;
        }
    }

    @Test
    @Transactional
    void createOrderShipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderShipment
        var returnedOrderShipment = om.readValue(
            restOrderShipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderShipment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderShipment.class
        );

        // Validate the OrderShipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderShipmentUpdatableFieldsEquals(returnedOrderShipment, getPersistedOrderShipment(returnedOrderShipment));

        insertedOrderShipment = returnedOrderShipment;
    }

    @Test
    @Transactional
    void createOrderShipmentWithExistingId() throws Exception {
        // Create the OrderShipment with an existing ID
        orderShipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderShipment)))
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderShipment.setQuantity(null);

        // Create the OrderShipment, which fails.

        restOrderShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderShipment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderShipments() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));
    }

    @Test
    @Transactional
    void getOrderShipment() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get the orderShipment
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, orderShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderShipment.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getOrderShipmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        Long id = orderShipment.getId();

        defaultOrderShipmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderShipmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderShipmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity equals to
        defaultOrderShipmentFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity in
        defaultOrderShipmentFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity is not null
        defaultOrderShipmentFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity is greater than or equal to
        defaultOrderShipmentFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity is less than or equal to
        defaultOrderShipmentFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity is less than
        defaultOrderShipmentFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList where quantity is greater than
        defaultOrderShipmentFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderShipmentRepository.saveAndFlush(orderShipment);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderShipment.setOrder(order);
        orderShipmentRepository.saveAndFlush(orderShipment);
        Long orderId = order.getId();
        // Get all the orderShipmentList where order equals to orderId
        defaultOrderShipmentShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderShipmentList where order equals to (orderId + 1)
        defaultOrderShipmentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderShipmentRepository.saveAndFlush(orderShipment);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        orderShipment.setOrderItem(orderItem);
        orderShipmentRepository.saveAndFlush(orderShipment);
        Long orderItemId = orderItem.getId();
        // Get all the orderShipmentList where orderItem equals to orderItemId
        defaultOrderShipmentShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderShipmentList where orderItem equals to (orderItemId + 1)
        defaultOrderShipmentShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            orderShipmentRepository.saveAndFlush(orderShipment);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        orderShipment.setShipment(shipment);
        orderShipmentRepository.saveAndFlush(orderShipment);
        Long shipmentId = shipment.getId();
        // Get all the orderShipmentList where shipment equals to shipmentId
        defaultOrderShipmentShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the orderShipmentList where shipment equals to (shipmentId + 1)
        defaultOrderShipmentShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderShipmentsByShipmentItemIsEqualToSomething() throws Exception {
        ShipmentItem shipmentItem;
        if (TestUtil.findAll(em, ShipmentItem.class).isEmpty()) {
            orderShipmentRepository.saveAndFlush(orderShipment);
            shipmentItem = ShipmentItemResourceIT.createEntity(em);
        } else {
            shipmentItem = TestUtil.findAll(em, ShipmentItem.class).getFirst();
        }
        em.persist(shipmentItem);
        em.flush();
        orderShipment.setShipmentItem(shipmentItem);
        orderShipmentRepository.saveAndFlush(orderShipment);
        Long shipmentItemId = shipmentItem.getId();
        // Get all the orderShipmentList where shipmentItem equals to shipmentItemId
        defaultOrderShipmentShouldBeFound("shipmentItemId.equals=" + shipmentItemId);

        // Get all the orderShipmentList where shipmentItem equals to (shipmentItemId + 1)
        defaultOrderShipmentShouldNotBeFound("shipmentItemId.equals=" + (shipmentItemId + 1));
    }

    private void defaultOrderShipmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderShipmentShouldBeFound(shouldBeFound);
        defaultOrderShipmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShipmentShouldBeFound(String filter) throws Exception {
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));

        // Check, that the count call also returns 1
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShipmentShouldNotBeFound(String filter) throws Exception {
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderShipment() throws Exception {
        // Get the orderShipment
        restOrderShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderShipment() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderShipment
        OrderShipment updatedOrderShipment = orderShipmentRepository.findById(orderShipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderShipment are not directly saved in db
        em.detach(updatedOrderShipment);
        updatedOrderShipment.quantity(UPDATED_QUANTITY);

        restOrderShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderShipment))
            )
            .andExpect(status().isOk());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderShipmentToMatchAllProperties(updatedOrderShipment);
    }

    @Test
    @Transactional
    void putNonExistingOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderShipment using partial update
        OrderShipment partialUpdatedOrderShipment = new OrderShipment();
        partialUpdatedOrderShipment.setId(orderShipment.getId());

        restOrderShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderShipment))
            )
            .andExpect(status().isOk());

        // Validate the OrderShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderShipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderShipment, orderShipment),
            getPersistedOrderShipment(orderShipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderShipment using partial update
        OrderShipment partialUpdatedOrderShipment = new OrderShipment();
        partialUpdatedOrderShipment.setId(orderShipment.getId());

        partialUpdatedOrderShipment.quantity(UPDATED_QUANTITY);

        restOrderShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderShipment))
            )
            .andExpect(status().isOk());

        // Validate the OrderShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderShipmentUpdatableFieldsEquals(partialUpdatedOrderShipment, getPersistedOrderShipment(partialUpdatedOrderShipment));
    }

    @Test
    @Transactional
    void patchNonExistingOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderShipment() throws Exception {
        // Initialize the database
        insertedOrderShipment = orderShipmentRepository.saveAndFlush(orderShipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderShipment
        restOrderShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderShipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderShipmentRepository.count();
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

    protected OrderShipment getPersistedOrderShipment(OrderShipment orderShipment) {
        return orderShipmentRepository.findById(orderShipment.getId()).orElseThrow();
    }

    protected void assertPersistedOrderShipmentToMatchAllProperties(OrderShipment expectedOrderShipment) {
        assertOrderShipmentAllPropertiesEquals(expectedOrderShipment, getPersistedOrderShipment(expectedOrderShipment));
    }

    protected void assertPersistedOrderShipmentToMatchUpdatableProperties(OrderShipment expectedOrderShipment) {
        assertOrderShipmentAllUpdatablePropertiesEquals(expectedOrderShipment, getPersistedOrderShipment(expectedOrderShipment));
    }
}
