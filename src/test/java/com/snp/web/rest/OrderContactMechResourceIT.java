package com.snp.web.rest;

import static com.snp.domain.OrderContactMechAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.ContactMechPurpose;
import com.snp.domain.Order;
import com.snp.domain.OrderContactMech;
import com.snp.repository.OrderContactMechRepository;
import com.snp.service.OrderContactMechService;
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
 * Integration tests for the {@link OrderContactMechResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderContactMechResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/order-contact-meches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderContactMechRepository orderContactMechRepository;

    @Mock
    private OrderContactMechRepository orderContactMechRepositoryMock;

    @Mock
    private OrderContactMechService orderContactMechServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderContactMechMockMvc;

    private OrderContactMech orderContactMech;

    private OrderContactMech insertedOrderContactMech;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderContactMech createEntity(EntityManager em) {
        OrderContactMech orderContactMech = new OrderContactMech().fromDate(DEFAULT_FROM_DATE).thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        orderContactMech.setOrder(order);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        orderContactMech.setContactMech(contactMech);
        return orderContactMech;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderContactMech createUpdatedEntity(EntityManager em) {
        OrderContactMech updatedOrderContactMech = new OrderContactMech().fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        updatedOrderContactMech.setOrder(order);
        // Add required entity
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            contactMech = ContactMechResourceIT.createUpdatedEntity();
            em.persist(contactMech);
            em.flush();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        updatedOrderContactMech.setContactMech(contactMech);
        return updatedOrderContactMech;
    }

    @BeforeEach
    void initTest() {
        orderContactMech = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderContactMech != null) {
            orderContactMechRepository.delete(insertedOrderContactMech);
            insertedOrderContactMech = null;
        }
    }

    @Test
    @Transactional
    void createOrderContactMech() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderContactMech
        var returnedOrderContactMech = om.readValue(
            restOrderContactMechMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderContactMech)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderContactMech.class
        );

        // Validate the OrderContactMech in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderContactMechUpdatableFieldsEquals(returnedOrderContactMech, getPersistedOrderContactMech(returnedOrderContactMech));

        insertedOrderContactMech = returnedOrderContactMech;
    }

    @Test
    @Transactional
    void createOrderContactMechWithExistingId() throws Exception {
        // Create the OrderContactMech with an existing ID
        orderContactMech.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderContactMech)))
            .andExpect(status().isBadRequest());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderContactMech.setFromDate(null);

        // Create the OrderContactMech, which fails.

        restOrderContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderContactMech)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderContactMeches() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderContactMechesWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderContactMechServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderContactMechesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderContactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderContactMechRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderContactMech() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get the orderContactMech
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL_ID, orderContactMech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderContactMech.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getOrderContactMechesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        Long id = orderContactMech.getId();

        defaultOrderContactMechFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderContactMechFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderContactMechFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where fromDate equals to
        defaultOrderContactMechFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where fromDate in
        defaultOrderContactMechFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where fromDate is not null
        defaultOrderContactMechFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where thruDate equals to
        defaultOrderContactMechFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where thruDate in
        defaultOrderContactMechFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        // Get all the orderContactMechList where thruDate is not null
        defaultOrderContactMechFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderContactMechRepository.saveAndFlush(orderContactMech);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderContactMech.setOrder(order);
        orderContactMechRepository.saveAndFlush(orderContactMech);
        Long orderId = order.getId();
        // Get all the orderContactMechList where order equals to orderId
        defaultOrderContactMechShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderContactMechList where order equals to (orderId + 1)
        defaultOrderContactMechShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            orderContactMechRepository.saveAndFlush(orderContactMech);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        orderContactMech.setContactMech(contactMech);
        orderContactMechRepository.saveAndFlush(orderContactMech);
        Long contactMechId = contactMech.getId();
        // Get all the orderContactMechList where contactMech equals to contactMechId
        defaultOrderContactMechShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the orderContactMechList where contactMech equals to (contactMechId + 1)
        defaultOrderContactMechShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    @Test
    @Transactional
    void getAllOrderContactMechesByContactMechPurposeIsEqualToSomething() throws Exception {
        ContactMechPurpose contactMechPurpose;
        if (TestUtil.findAll(em, ContactMechPurpose.class).isEmpty()) {
            orderContactMechRepository.saveAndFlush(orderContactMech);
            contactMechPurpose = ContactMechPurposeResourceIT.createEntity();
        } else {
            contactMechPurpose = TestUtil.findAll(em, ContactMechPurpose.class).getFirst();
        }
        em.persist(contactMechPurpose);
        em.flush();
        orderContactMech.setContactMechPurpose(contactMechPurpose);
        orderContactMechRepository.saveAndFlush(orderContactMech);
        Long contactMechPurposeId = contactMechPurpose.getId();
        // Get all the orderContactMechList where contactMechPurpose equals to contactMechPurposeId
        defaultOrderContactMechShouldBeFound("contactMechPurposeId.equals=" + contactMechPurposeId);

        // Get all the orderContactMechList where contactMechPurpose equals to (contactMechPurposeId + 1)
        defaultOrderContactMechShouldNotBeFound("contactMechPurposeId.equals=" + (contactMechPurposeId + 1));
    }

    private void defaultOrderContactMechFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderContactMechShouldBeFound(shouldBeFound);
        defaultOrderContactMechShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderContactMechShouldBeFound(String filter) throws Exception {
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderContactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderContactMechShouldNotBeFound(String filter) throws Exception {
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderContactMech() throws Exception {
        // Get the orderContactMech
        restOrderContactMechMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderContactMech() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderContactMech
        OrderContactMech updatedOrderContactMech = orderContactMechRepository.findById(orderContactMech.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderContactMech are not directly saved in db
        em.detach(updatedOrderContactMech);
        updatedOrderContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restOrderContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderContactMech))
            )
            .andExpect(status().isOk());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderContactMechToMatchAllProperties(updatedOrderContactMech);
    }

    @Test
    @Transactional
    void putNonExistingOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderContactMech using partial update
        OrderContactMech partialUpdatedOrderContactMech = new OrderContactMech();
        partialUpdatedOrderContactMech.setId(orderContactMech.getId());

        partialUpdatedOrderContactMech.thruDate(UPDATED_THRU_DATE);

        restOrderContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderContactMech))
            )
            .andExpect(status().isOk());

        // Validate the OrderContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderContactMechUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderContactMech, orderContactMech),
            getPersistedOrderContactMech(orderContactMech)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderContactMech using partial update
        OrderContactMech partialUpdatedOrderContactMech = new OrderContactMech();
        partialUpdatedOrderContactMech.setId(orderContactMech.getId());

        partialUpdatedOrderContactMech.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restOrderContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderContactMech))
            )
            .andExpect(status().isOk());

        // Validate the OrderContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderContactMechUpdatableFieldsEquals(
            partialUpdatedOrderContactMech,
            getPersistedOrderContactMech(partialUpdatedOrderContactMech)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderContactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderContactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContactMechMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderContactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderContactMech() throws Exception {
        // Initialize the database
        insertedOrderContactMech = orderContactMechRepository.saveAndFlush(orderContactMech);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderContactMech
        restOrderContactMechMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderContactMech.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderContactMechRepository.count();
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

    protected OrderContactMech getPersistedOrderContactMech(OrderContactMech orderContactMech) {
        return orderContactMechRepository.findById(orderContactMech.getId()).orElseThrow();
    }

    protected void assertPersistedOrderContactMechToMatchAllProperties(OrderContactMech expectedOrderContactMech) {
        assertOrderContactMechAllPropertiesEquals(expectedOrderContactMech, getPersistedOrderContactMech(expectedOrderContactMech));
    }

    protected void assertPersistedOrderContactMechToMatchUpdatableProperties(OrderContactMech expectedOrderContactMech) {
        assertOrderContactMechAllUpdatablePropertiesEquals(
            expectedOrderContactMech,
            getPersistedOrderContactMech(expectedOrderContactMech)
        );
    }
}
