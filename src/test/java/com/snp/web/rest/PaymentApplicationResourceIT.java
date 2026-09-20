package com.snp.web.rest;

import static com.snp.domain.PaymentApplicationAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Invoice;
import com.snp.domain.InvoiceItem;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.Payment;
import com.snp.domain.PaymentApplication;
import com.snp.repository.PaymentApplicationRepository;
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
 * Integration tests for the {@link PaymentApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentApplicationResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT_APPLIED = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_APPLIED = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT_APPLIED = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/payment-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentApplicationRepository paymentApplicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentApplicationMockMvc;

    private PaymentApplication paymentApplication;

    private PaymentApplication insertedPaymentApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentApplication createEntity(EntityManager em) {
        PaymentApplication paymentApplication = new PaymentApplication().amountApplied(DEFAULT_AMOUNT_APPLIED);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity();
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).getFirst();
        }
        paymentApplication.setPayment(payment);
        return paymentApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentApplication createUpdatedEntity(EntityManager em) {
        PaymentApplication updatedPaymentApplication = new PaymentApplication().amountApplied(UPDATED_AMOUNT_APPLIED);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createUpdatedEntity();
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).getFirst();
        }
        updatedPaymentApplication.setPayment(payment);
        return updatedPaymentApplication;
    }

    @BeforeEach
    void initTest() {
        paymentApplication = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentApplication != null) {
            paymentApplicationRepository.delete(insertedPaymentApplication);
            insertedPaymentApplication = null;
        }
    }

    @Test
    @Transactional
    void createPaymentApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentApplication
        var returnedPaymentApplication = om.readValue(
            restPaymentApplicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentApplication)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentApplication.class
        );

        // Validate the PaymentApplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentApplicationUpdatableFieldsEquals(
            returnedPaymentApplication,
            getPersistedPaymentApplication(returnedPaymentApplication)
        );

        insertedPaymentApplication = returnedPaymentApplication;
    }

    @Test
    @Transactional
    void createPaymentApplicationWithExistingId() throws Exception {
        // Create the PaymentApplication with an existing ID
        paymentApplication.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentApplication)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountAppliedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentApplication.setAmountApplied(null);

        // Create the PaymentApplication, which fails.

        restPaymentApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentApplication)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentApplications() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountApplied").value(hasItem(sameNumber(DEFAULT_AMOUNT_APPLIED))));
    }

    @Test
    @Transactional
    void getPaymentApplication() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get the paymentApplication
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentApplication.getId().intValue()))
            .andExpect(jsonPath("$.amountApplied").value(sameNumber(DEFAULT_AMOUNT_APPLIED)));
    }

    @Test
    @Transactional
    void getPaymentApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        Long id = paymentApplication.getId();

        defaultPaymentApplicationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentApplicationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentApplicationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied equals to
        defaultPaymentApplicationFiltering(
            "amountApplied.equals=" + DEFAULT_AMOUNT_APPLIED,
            "amountApplied.equals=" + UPDATED_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied in
        defaultPaymentApplicationFiltering(
            "amountApplied.in=" + DEFAULT_AMOUNT_APPLIED + "," + UPDATED_AMOUNT_APPLIED,
            "amountApplied.in=" + UPDATED_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied is not null
        defaultPaymentApplicationFiltering("amountApplied.specified=true", "amountApplied.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied is greater than or equal to
        defaultPaymentApplicationFiltering(
            "amountApplied.greaterThanOrEqual=" + DEFAULT_AMOUNT_APPLIED,
            "amountApplied.greaterThanOrEqual=" + UPDATED_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied is less than or equal to
        defaultPaymentApplicationFiltering(
            "amountApplied.lessThanOrEqual=" + DEFAULT_AMOUNT_APPLIED,
            "amountApplied.lessThanOrEqual=" + SMALLER_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied is less than
        defaultPaymentApplicationFiltering(
            "amountApplied.lessThan=" + UPDATED_AMOUNT_APPLIED,
            "amountApplied.lessThan=" + DEFAULT_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByAmountAppliedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        // Get all the paymentApplicationList where amountApplied is greater than
        defaultPaymentApplicationFiltering(
            "amountApplied.greaterThan=" + SMALLER_AMOUNT_APPLIED,
            "amountApplied.greaterThan=" + DEFAULT_AMOUNT_APPLIED
        );
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByPaymentIsEqualToSomething() throws Exception {
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            payment = PaymentResourceIT.createEntity();
        } else {
            payment = TestUtil.findAll(em, Payment.class).getFirst();
        }
        em.persist(payment);
        em.flush();
        paymentApplication.setPayment(payment);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long paymentId = payment.getId();
        // Get all the paymentApplicationList where payment equals to paymentId
        defaultPaymentApplicationShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the paymentApplicationList where payment equals to (paymentId + 1)
        defaultPaymentApplicationShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByToPaymentIsEqualToSomething() throws Exception {
        Payment toPayment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            toPayment = PaymentResourceIT.createEntity();
        } else {
            toPayment = TestUtil.findAll(em, Payment.class).getFirst();
        }
        em.persist(toPayment);
        em.flush();
        paymentApplication.setToPayment(toPayment);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long toPaymentId = toPayment.getId();
        // Get all the paymentApplicationList where toPayment equals to toPaymentId
        defaultPaymentApplicationShouldBeFound("toPaymentId.equals=" + toPaymentId);

        // Get all the paymentApplicationList where toPayment equals to (toPaymentId + 1)
        defaultPaymentApplicationShouldNotBeFound("toPaymentId.equals=" + (toPaymentId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByInvoiceIsEqualToSomething() throws Exception {
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            invoice = InvoiceResourceIT.createEntity(em);
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(invoice);
        em.flush();
        paymentApplication.setInvoice(invoice);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long invoiceId = invoice.getId();
        // Get all the paymentApplicationList where invoice equals to invoiceId
        defaultPaymentApplicationShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the paymentApplicationList where invoice equals to (invoiceId + 1)
        defaultPaymentApplicationShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(invoiceItem);
        em.flush();
        paymentApplication.setInvoiceItem(invoiceItem);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long invoiceItemId = invoiceItem.getId();
        // Get all the paymentApplicationList where invoiceItem equals to invoiceItemId
        defaultPaymentApplicationShouldBeFound("invoiceItemId.equals=" + invoiceItemId);

        // Get all the paymentApplicationList where invoiceItem equals to (invoiceItemId + 1)
        defaultPaymentApplicationShouldNotBeFound("invoiceItemId.equals=" + (invoiceItemId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        paymentApplication.setOrder(order);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long orderId = order.getId();
        // Get all the paymentApplicationList where order equals to orderId
        defaultPaymentApplicationShouldBeFound("orderId.equals=" + orderId);

        // Get all the paymentApplicationList where order equals to (orderId + 1)
        defaultPaymentApplicationShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentApplicationsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            paymentApplicationRepository.saveAndFlush(paymentApplication);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        paymentApplication.setOrderItem(orderItem);
        paymentApplicationRepository.saveAndFlush(paymentApplication);
        Long orderItemId = orderItem.getId();
        // Get all the paymentApplicationList where orderItem equals to orderItemId
        defaultPaymentApplicationShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the paymentApplicationList where orderItem equals to (orderItemId + 1)
        defaultPaymentApplicationShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    private void defaultPaymentApplicationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentApplicationShouldBeFound(shouldBeFound);
        defaultPaymentApplicationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentApplicationShouldBeFound(String filter) throws Exception {
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountApplied").value(hasItem(sameNumber(DEFAULT_AMOUNT_APPLIED))));

        // Check, that the count call also returns 1
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentApplicationShouldNotBeFound(String filter) throws Exception {
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentApplication() throws Exception {
        // Get the paymentApplication
        restPaymentApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentApplication() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentApplication
        PaymentApplication updatedPaymentApplication = paymentApplicationRepository.findById(paymentApplication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentApplication are not directly saved in db
        em.detach(updatedPaymentApplication);
        updatedPaymentApplication.amountApplied(UPDATED_AMOUNT_APPLIED);

        restPaymentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentApplication))
            )
            .andExpect(status().isOk());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentApplicationToMatchAllProperties(updatedPaymentApplication);
    }

    @Test
    @Transactional
    void putNonExistingPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentApplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentApplication using partial update
        PaymentApplication partialUpdatedPaymentApplication = new PaymentApplication();
        partialUpdatedPaymentApplication.setId(paymentApplication.getId());

        partialUpdatedPaymentApplication.amountApplied(UPDATED_AMOUNT_APPLIED);

        restPaymentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentApplication))
            )
            .andExpect(status().isOk());

        // Validate the PaymentApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentApplication, paymentApplication),
            getPersistedPaymentApplication(paymentApplication)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentApplication using partial update
        PaymentApplication partialUpdatedPaymentApplication = new PaymentApplication();
        partialUpdatedPaymentApplication.setId(paymentApplication.getId());

        partialUpdatedPaymentApplication.amountApplied(UPDATED_AMOUNT_APPLIED);

        restPaymentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentApplication))
            )
            .andExpect(status().isOk());

        // Validate the PaymentApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentApplicationUpdatableFieldsEquals(
            partialUpdatedPaymentApplication,
            getPersistedPaymentApplication(partialUpdatedPaymentApplication)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentApplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentApplicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentApplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentApplication() throws Exception {
        // Initialize the database
        insertedPaymentApplication = paymentApplicationRepository.saveAndFlush(paymentApplication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentApplication
        restPaymentApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentApplicationRepository.count();
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

    protected PaymentApplication getPersistedPaymentApplication(PaymentApplication paymentApplication) {
        return paymentApplicationRepository.findById(paymentApplication.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentApplicationToMatchAllProperties(PaymentApplication expectedPaymentApplication) {
        assertPaymentApplicationAllPropertiesEquals(expectedPaymentApplication, getPersistedPaymentApplication(expectedPaymentApplication));
    }

    protected void assertPersistedPaymentApplicationToMatchUpdatableProperties(PaymentApplication expectedPaymentApplication) {
        assertPaymentApplicationAllUpdatablePropertiesEquals(
            expectedPaymentApplication,
            getPersistedPaymentApplication(expectedPaymentApplication)
        );
    }
}
