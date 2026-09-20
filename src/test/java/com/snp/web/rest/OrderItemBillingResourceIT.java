package com.snp.web.rest;

import static com.snp.domain.OrderItemBillingAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InvoiceItem;
import com.snp.domain.ItemIssuance;
import com.snp.domain.OrderItem;
import com.snp.domain.OrderItemBilling;
import com.snp.domain.ShipmentReceipt;
import com.snp.repository.OrderItemBillingRepository;
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
 * Integration tests for the {@link OrderItemBillingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemBillingResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/order-item-billings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderItemBillingRepository orderItemBillingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemBillingMockMvc;

    private OrderItemBilling orderItemBilling;

    private OrderItemBilling insertedOrderItemBilling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemBilling createEntity(EntityManager em) {
        OrderItemBilling orderItemBilling = new OrderItemBilling().quantity(DEFAULT_QUANTITY).amount(DEFAULT_AMOUNT);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        orderItemBilling.setOrderItem(orderItem);
        // Add required entity
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
            em.persist(invoiceItem);
            em.flush();
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        orderItemBilling.setInvoiceItem(invoiceItem);
        return orderItemBilling;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemBilling createUpdatedEntity(EntityManager em) {
        OrderItemBilling updatedOrderItemBilling = new OrderItemBilling().quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);
        // Add required entity
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItem = OrderItemResourceIT.createUpdatedEntity(em);
            em.persist(orderItem);
            em.flush();
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        updatedOrderItemBilling.setOrderItem(orderItem);
        // Add required entity
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            invoiceItem = InvoiceItemResourceIT.createUpdatedEntity(em);
            em.persist(invoiceItem);
            em.flush();
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        updatedOrderItemBilling.setInvoiceItem(invoiceItem);
        return updatedOrderItemBilling;
    }

    @BeforeEach
    void initTest() {
        orderItemBilling = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderItemBilling != null) {
            orderItemBillingRepository.delete(insertedOrderItemBilling);
            insertedOrderItemBilling = null;
        }
    }

    @Test
    @Transactional
    void createOrderItemBilling() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderItemBilling
        var returnedOrderItemBilling = om.readValue(
            restOrderItemBillingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemBilling)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderItemBilling.class
        );

        // Validate the OrderItemBilling in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderItemBillingUpdatableFieldsEquals(returnedOrderItemBilling, getPersistedOrderItemBilling(returnedOrderItemBilling));

        insertedOrderItemBilling = returnedOrderItemBilling;
    }

    @Test
    @Transactional
    void createOrderItemBillingWithExistingId() throws Exception {
        // Create the OrderItemBilling with an existing ID
        orderItemBilling.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemBillingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemBilling)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderItemBilling.setQuantity(null);

        // Create the OrderItemBilling, which fails.

        restOrderItemBillingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemBilling)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItemBillings() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemBilling.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getOrderItemBilling() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get the orderItemBilling
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemBilling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemBilling.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getOrderItemBillingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        Long id = orderItemBilling.getId();

        defaultOrderItemBillingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderItemBillingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderItemBillingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity equals to
        defaultOrderItemBillingFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity in
        defaultOrderItemBillingFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity is not null
        defaultOrderItemBillingFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity is greater than or equal to
        defaultOrderItemBillingFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity is less than or equal to
        defaultOrderItemBillingFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity is less than
        defaultOrderItemBillingFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where quantity is greater than
        defaultOrderItemBillingFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount equals to
        defaultOrderItemBillingFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount in
        defaultOrderItemBillingFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount is not null
        defaultOrderItemBillingFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount is greater than or equal to
        defaultOrderItemBillingFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount is less than or equal to
        defaultOrderItemBillingFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount is less than
        defaultOrderItemBillingFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        // Get all the orderItemBillingList where amount is greater than
        defaultOrderItemBillingFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderItemBillingRepository.saveAndFlush(orderItemBilling);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        orderItemBilling.setOrderItem(orderItem);
        orderItemBillingRepository.saveAndFlush(orderItemBilling);
        Long orderItemId = orderItem.getId();
        // Get all the orderItemBillingList where orderItem equals to orderItemId
        defaultOrderItemBillingShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderItemBillingList where orderItem equals to (orderItemId + 1)
        defaultOrderItemBillingShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            orderItemBillingRepository.saveAndFlush(orderItemBilling);
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(invoiceItem);
        em.flush();
        orderItemBilling.setInvoiceItem(invoiceItem);
        orderItemBillingRepository.saveAndFlush(orderItemBilling);
        Long invoiceItemId = invoiceItem.getId();
        // Get all the orderItemBillingList where invoiceItem equals to invoiceItemId
        defaultOrderItemBillingShouldBeFound("invoiceItemId.equals=" + invoiceItemId);

        // Get all the orderItemBillingList where invoiceItem equals to (invoiceItemId + 1)
        defaultOrderItemBillingShouldNotBeFound("invoiceItemId.equals=" + (invoiceItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByShipmentReceiptIsEqualToSomething() throws Exception {
        ShipmentReceipt shipmentReceipt;
        if (TestUtil.findAll(em, ShipmentReceipt.class).isEmpty()) {
            orderItemBillingRepository.saveAndFlush(orderItemBilling);
            shipmentReceipt = ShipmentReceiptResourceIT.createEntity();
        } else {
            shipmentReceipt = TestUtil.findAll(em, ShipmentReceipt.class).getFirst();
        }
        em.persist(shipmentReceipt);
        em.flush();
        orderItemBilling.setShipmentReceipt(shipmentReceipt);
        orderItemBillingRepository.saveAndFlush(orderItemBilling);
        Long shipmentReceiptId = shipmentReceipt.getId();
        // Get all the orderItemBillingList where shipmentReceipt equals to shipmentReceiptId
        defaultOrderItemBillingShouldBeFound("shipmentReceiptId.equals=" + shipmentReceiptId);

        // Get all the orderItemBillingList where shipmentReceipt equals to (shipmentReceiptId + 1)
        defaultOrderItemBillingShouldNotBeFound("shipmentReceiptId.equals=" + (shipmentReceiptId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemBillingsByItemIssuanceIsEqualToSomething() throws Exception {
        ItemIssuance itemIssuance;
        if (TestUtil.findAll(em, ItemIssuance.class).isEmpty()) {
            orderItemBillingRepository.saveAndFlush(orderItemBilling);
            itemIssuance = ItemIssuanceResourceIT.createEntity(em);
        } else {
            itemIssuance = TestUtil.findAll(em, ItemIssuance.class).getFirst();
        }
        em.persist(itemIssuance);
        em.flush();
        orderItemBilling.setItemIssuance(itemIssuance);
        orderItemBillingRepository.saveAndFlush(orderItemBilling);
        Long itemIssuanceId = itemIssuance.getId();
        // Get all the orderItemBillingList where itemIssuance equals to itemIssuanceId
        defaultOrderItemBillingShouldBeFound("itemIssuanceId.equals=" + itemIssuanceId);

        // Get all the orderItemBillingList where itemIssuance equals to (itemIssuanceId + 1)
        defaultOrderItemBillingShouldNotBeFound("itemIssuanceId.equals=" + (itemIssuanceId + 1));
    }

    private void defaultOrderItemBillingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderItemBillingShouldBeFound(shouldBeFound);
        defaultOrderItemBillingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemBillingShouldBeFound(String filter) throws Exception {
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemBilling.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemBillingShouldNotBeFound(String filter) throws Exception {
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemBilling() throws Exception {
        // Get the orderItemBilling
        restOrderItemBillingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItemBilling() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemBilling
        OrderItemBilling updatedOrderItemBilling = orderItemBillingRepository.findById(orderItemBilling.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItemBilling are not directly saved in db
        em.detach(updatedOrderItemBilling);
        updatedOrderItemBilling.quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);

        restOrderItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderItemBilling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderItemBillingToMatchAllProperties(updatedOrderItemBilling);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemBilling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemBilling)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemBillingWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemBilling using partial update
        OrderItemBilling partialUpdatedOrderItemBilling = new OrderItemBilling();
        partialUpdatedOrderItemBilling.setId(orderItemBilling.getId());

        restOrderItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemBilling in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemBillingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderItemBilling, orderItemBilling),
            getPersistedOrderItemBilling(orderItemBilling)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderItemBillingWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemBilling using partial update
        OrderItemBilling partialUpdatedOrderItemBilling = new OrderItemBilling();
        partialUpdatedOrderItemBilling.setId(orderItemBilling.getId());

        partialUpdatedOrderItemBilling.quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);

        restOrderItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemBilling in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemBillingUpdatableFieldsEquals(
            partialUpdatedOrderItemBilling,
            getPersistedOrderItemBilling(partialUpdatedOrderItemBilling)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemBillingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderItemBilling)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItemBilling() throws Exception {
        // Initialize the database
        insertedOrderItemBilling = orderItemBillingRepository.saveAndFlush(orderItemBilling);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderItemBilling
        restOrderItemBillingMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemBilling.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderItemBillingRepository.count();
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

    protected OrderItemBilling getPersistedOrderItemBilling(OrderItemBilling orderItemBilling) {
        return orderItemBillingRepository.findById(orderItemBilling.getId()).orElseThrow();
    }

    protected void assertPersistedOrderItemBillingToMatchAllProperties(OrderItemBilling expectedOrderItemBilling) {
        assertOrderItemBillingAllPropertiesEquals(expectedOrderItemBilling, getPersistedOrderItemBilling(expectedOrderItemBilling));
    }

    protected void assertPersistedOrderItemBillingToMatchUpdatableProperties(OrderItemBilling expectedOrderItemBilling) {
        assertOrderItemBillingAllUpdatablePropertiesEquals(
            expectedOrderItemBilling,
            getPersistedOrderItemBilling(expectedOrderItemBilling)
        );
    }
}
