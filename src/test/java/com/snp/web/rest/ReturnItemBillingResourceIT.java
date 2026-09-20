package com.snp.web.rest;

import static com.snp.domain.ReturnItemBillingAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InvoiceItem;
import com.snp.domain.OrderItem;
import com.snp.domain.ReturnItem;
import com.snp.domain.ReturnItemBilling;
import com.snp.domain.ShipmentReceipt;
import com.snp.repository.ReturnItemBillingRepository;
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
 * Integration tests for the {@link ReturnItemBillingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReturnItemBillingResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/return-item-billings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReturnItemBillingRepository returnItemBillingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnItemBillingMockMvc;

    private ReturnItemBilling returnItemBilling;

    private ReturnItemBilling insertedReturnItemBilling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnItemBilling createEntity(EntityManager em) {
        ReturnItemBilling returnItemBilling = new ReturnItemBilling().quantity(DEFAULT_QUANTITY).amount(DEFAULT_AMOUNT);
        // Add required entity
        ReturnItem returnItem;
        if (TestUtil.findAll(em, ReturnItem.class).isEmpty()) {
            returnItem = ReturnItemResourceIT.createEntity(em);
            em.persist(returnItem);
            em.flush();
        } else {
            returnItem = TestUtil.findAll(em, ReturnItem.class).getFirst();
        }
        returnItemBilling.setReturnItem(returnItem);
        return returnItemBilling;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnItemBilling createUpdatedEntity(EntityManager em) {
        ReturnItemBilling updatedReturnItemBilling = new ReturnItemBilling().quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);
        // Add required entity
        ReturnItem returnItem;
        if (TestUtil.findAll(em, ReturnItem.class).isEmpty()) {
            returnItem = ReturnItemResourceIT.createUpdatedEntity(em);
            em.persist(returnItem);
            em.flush();
        } else {
            returnItem = TestUtil.findAll(em, ReturnItem.class).getFirst();
        }
        updatedReturnItemBilling.setReturnItem(returnItem);
        return updatedReturnItemBilling;
    }

    @BeforeEach
    void initTest() {
        returnItemBilling = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReturnItemBilling != null) {
            returnItemBillingRepository.delete(insertedReturnItemBilling);
            insertedReturnItemBilling = null;
        }
    }

    @Test
    @Transactional
    void createReturnItemBilling() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReturnItemBilling
        var returnedReturnItemBilling = om.readValue(
            restReturnItemBillingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItemBilling)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReturnItemBilling.class
        );

        // Validate the ReturnItemBilling in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReturnItemBillingUpdatableFieldsEquals(returnedReturnItemBilling, getPersistedReturnItemBilling(returnedReturnItemBilling));

        insertedReturnItemBilling = returnedReturnItemBilling;
    }

    @Test
    @Transactional
    void createReturnItemBillingWithExistingId() throws Exception {
        // Create the ReturnItemBilling with an existing ID
        returnItemBilling.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnItemBillingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItemBilling)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        returnItemBilling.setQuantity(null);

        // Create the ReturnItemBilling, which fails.

        restReturnItemBillingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItemBilling)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReturnItemBillings() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItemBilling.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getReturnItemBilling() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get the returnItemBilling
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL_ID, returnItemBilling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnItemBilling.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getReturnItemBillingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        Long id = returnItemBilling.getId();

        defaultReturnItemBillingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReturnItemBillingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReturnItemBillingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity equals to
        defaultReturnItemBillingFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity in
        defaultReturnItemBillingFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity is not null
        defaultReturnItemBillingFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity is greater than or equal to
        defaultReturnItemBillingFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity is less than or equal to
        defaultReturnItemBillingFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity is less than
        defaultReturnItemBillingFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where quantity is greater than
        defaultReturnItemBillingFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount equals to
        defaultReturnItemBillingFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount in
        defaultReturnItemBillingFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount is not null
        defaultReturnItemBillingFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount is greater than or equal to
        defaultReturnItemBillingFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount is less than or equal to
        defaultReturnItemBillingFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount is less than
        defaultReturnItemBillingFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        // Get all the returnItemBillingList where amount is greater than
        defaultReturnItemBillingFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByReturnItemIsEqualToSomething() throws Exception {
        ReturnItem returnItem;
        if (TestUtil.findAll(em, ReturnItem.class).isEmpty()) {
            returnItemBillingRepository.saveAndFlush(returnItemBilling);
            returnItem = ReturnItemResourceIT.createEntity(em);
        } else {
            returnItem = TestUtil.findAll(em, ReturnItem.class).getFirst();
        }
        em.persist(returnItem);
        em.flush();
        returnItemBilling.setReturnItem(returnItem);
        returnItemBillingRepository.saveAndFlush(returnItemBilling);
        Long returnItemId = returnItem.getId();
        // Get all the returnItemBillingList where returnItem equals to returnItemId
        defaultReturnItemBillingShouldBeFound("returnItemId.equals=" + returnItemId);

        // Get all the returnItemBillingList where returnItem equals to (returnItemId + 1)
        defaultReturnItemBillingShouldNotBeFound("returnItemId.equals=" + (returnItemId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            returnItemBillingRepository.saveAndFlush(returnItemBilling);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        returnItemBilling.setOrderItem(orderItem);
        returnItemBillingRepository.saveAndFlush(returnItemBilling);
        Long orderItemId = orderItem.getId();
        // Get all the returnItemBillingList where orderItem equals to orderItemId
        defaultReturnItemBillingShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the returnItemBillingList where orderItem equals to (orderItemId + 1)
        defaultReturnItemBillingShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            returnItemBillingRepository.saveAndFlush(returnItemBilling);
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(invoiceItem);
        em.flush();
        returnItemBilling.setInvoiceItem(invoiceItem);
        returnItemBillingRepository.saveAndFlush(returnItemBilling);
        Long invoiceItemId = invoiceItem.getId();
        // Get all the returnItemBillingList where invoiceItem equals to invoiceItemId
        defaultReturnItemBillingShouldBeFound("invoiceItemId.equals=" + invoiceItemId);

        // Get all the returnItemBillingList where invoiceItem equals to (invoiceItemId + 1)
        defaultReturnItemBillingShouldNotBeFound("invoiceItemId.equals=" + (invoiceItemId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemBillingsByShipmentReceiptIsEqualToSomething() throws Exception {
        ShipmentReceipt shipmentReceipt;
        if (TestUtil.findAll(em, ShipmentReceipt.class).isEmpty()) {
            returnItemBillingRepository.saveAndFlush(returnItemBilling);
            shipmentReceipt = ShipmentReceiptResourceIT.createEntity();
        } else {
            shipmentReceipt = TestUtil.findAll(em, ShipmentReceipt.class).getFirst();
        }
        em.persist(shipmentReceipt);
        em.flush();
        returnItemBilling.setShipmentReceipt(shipmentReceipt);
        returnItemBillingRepository.saveAndFlush(returnItemBilling);
        Long shipmentReceiptId = shipmentReceipt.getId();
        // Get all the returnItemBillingList where shipmentReceipt equals to shipmentReceiptId
        defaultReturnItemBillingShouldBeFound("shipmentReceiptId.equals=" + shipmentReceiptId);

        // Get all the returnItemBillingList where shipmentReceipt equals to (shipmentReceiptId + 1)
        defaultReturnItemBillingShouldNotBeFound("shipmentReceiptId.equals=" + (shipmentReceiptId + 1));
    }

    private void defaultReturnItemBillingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReturnItemBillingShouldBeFound(shouldBeFound);
        defaultReturnItemBillingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReturnItemBillingShouldBeFound(String filter) throws Exception {
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItemBilling.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReturnItemBillingShouldNotBeFound(String filter) throws Exception {
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnItemBillingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReturnItemBilling() throws Exception {
        // Get the returnItemBilling
        restReturnItemBillingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReturnItemBilling() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItemBilling
        ReturnItemBilling updatedReturnItemBilling = returnItemBillingRepository.findById(returnItemBilling.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReturnItemBilling are not directly saved in db
        em.detach(updatedReturnItemBilling);
        updatedReturnItemBilling.quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);

        restReturnItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnItemBilling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReturnItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReturnItemBillingToMatchAllProperties(updatedReturnItemBilling);
    }

    @Test
    @Transactional
    void putNonExistingReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnItemBilling.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItemBilling)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnItemBillingWithPatch() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItemBilling using partial update
        ReturnItemBilling partialUpdatedReturnItemBilling = new ReturnItemBilling();
        partialUpdatedReturnItemBilling.setId(returnItemBilling.getId());

        partialUpdatedReturnItemBilling.amount(UPDATED_AMOUNT);

        restReturnItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItemBilling in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnItemBillingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReturnItemBilling, returnItemBilling),
            getPersistedReturnItemBilling(returnItemBilling)
        );
    }

    @Test
    @Transactional
    void fullUpdateReturnItemBillingWithPatch() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItemBilling using partial update
        ReturnItemBilling partialUpdatedReturnItemBilling = new ReturnItemBilling();
        partialUpdatedReturnItemBilling.setId(returnItemBilling.getId());

        partialUpdatedReturnItemBilling.quantity(UPDATED_QUANTITY).amount(UPDATED_AMOUNT);

        restReturnItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnItemBilling))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItemBilling in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnItemBillingUpdatableFieldsEquals(
            partialUpdatedReturnItemBilling,
            getPersistedReturnItemBilling(partialUpdatedReturnItemBilling)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnItemBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnItemBilling))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnItemBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItemBilling.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemBillingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(returnItemBilling)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnItemBilling in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnItemBilling() throws Exception {
        // Initialize the database
        insertedReturnItemBilling = returnItemBillingRepository.saveAndFlush(returnItemBilling);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the returnItemBilling
        restReturnItemBillingMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnItemBilling.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return returnItemBillingRepository.count();
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

    protected ReturnItemBilling getPersistedReturnItemBilling(ReturnItemBilling returnItemBilling) {
        return returnItemBillingRepository.findById(returnItemBilling.getId()).orElseThrow();
    }

    protected void assertPersistedReturnItemBillingToMatchAllProperties(ReturnItemBilling expectedReturnItemBilling) {
        assertReturnItemBillingAllPropertiesEquals(expectedReturnItemBilling, getPersistedReturnItemBilling(expectedReturnItemBilling));
    }

    protected void assertPersistedReturnItemBillingToMatchUpdatableProperties(ReturnItemBilling expectedReturnItemBilling) {
        assertReturnItemBillingAllUpdatablePropertiesEquals(
            expectedReturnItemBilling,
            getPersistedReturnItemBilling(expectedReturnItemBilling)
        );
    }
}
