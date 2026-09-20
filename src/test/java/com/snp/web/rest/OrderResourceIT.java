package com.snp.web.rest;

import static com.snp.domain.OrderAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustomTimePeriod;
import com.snp.domain.Department;
import com.snp.domain.Facility;
import com.snp.domain.Order;
import com.snp.domain.OrderType;
import com.snp.domain.Party;
import com.snp.domain.ProductStore;
import com.snp.domain.Quote;
import com.snp.domain.SalesChannel;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.domain.UomConversion;
import com.snp.repository.OrderRepository;
import com.snp.service.OrderService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_REF_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_REF_NO = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Boolean DEFAULT_IS_RUSH_ORDER = false;
    private static final Boolean UPDATED_IS_RUSH_ORDER = true;

    private static final Boolean DEFAULT_NEEDS_INVENTORY_ISSUANCE = false;
    private static final Boolean UPDATED_NEEDS_INVENTORY_ISSUANCE = true;

    private static final BigDecimal DEFAULT_REMAINING_SUB_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_REMAINING_SUB_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_REMAINING_SUB_TOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GRAND_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_GRAND_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_GRAND_TOTAL = new BigDecimal(1 - 1);

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;
    private static final Integer SMALLER_VERSION = 1 - 1;

    private static final String DEFAULT_VERSION_REF = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_REF = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TERMS = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_TERMS = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR_1 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR_2 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR_3 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_3 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private OrderService orderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    private Order insertedOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .name(DEFAULT_NAME)
            .orderRefNo(DEFAULT_ORDER_REF_NO)
            .externalId(DEFAULT_EXTERNAL_ID)
            .orderDate(DEFAULT_ORDER_DATE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .priority(DEFAULT_PRIORITY)
            .isRushOrder(DEFAULT_IS_RUSH_ORDER)
            .needsInventoryIssuance(DEFAULT_NEEDS_INVENTORY_ISSUANCE)
            .remainingSubTotal(DEFAULT_REMAINING_SUB_TOTAL)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .projectName(DEFAULT_PROJECT_NAME)
            .version(DEFAULT_VERSION)
            .versionRef(DEFAULT_VERSION_REF)
            .description(DEFAULT_DESCRIPTION)
            .remarks(DEFAULT_REMARKS)
            .paymentTerms(DEFAULT_PAYMENT_TERMS)
            .deliveryTerms(DEFAULT_DELIVERY_TERMS)
            .info(DEFAULT_INFO)
            .attr1(DEFAULT_ATTR_1)
            .attr2(DEFAULT_ATTR_2)
            .attr3(DEFAULT_ATTR_3);
        // Add required entity
        OrderType orderType;
        if (TestUtil.findAll(em, OrderType.class).isEmpty()) {
            orderType = OrderTypeResourceIT.createEntity();
            em.persist(orderType);
            em.flush();
        } else {
            orderType = TestUtil.findAll(em, OrderType.class).getFirst();
        }
        order.setOrderType(orderType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        order.setParty(party);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order updatedOrder = new Order()
            .name(UPDATED_NAME)
            .orderRefNo(UPDATED_ORDER_REF_NO)
            .externalId(UPDATED_EXTERNAL_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .priority(UPDATED_PRIORITY)
            .isRushOrder(UPDATED_IS_RUSH_ORDER)
            .needsInventoryIssuance(UPDATED_NEEDS_INVENTORY_ISSUANCE)
            .remainingSubTotal(UPDATED_REMAINING_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .version(UPDATED_VERSION)
            .versionRef(UPDATED_VERSION_REF)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);
        // Add required entity
        OrderType orderType;
        if (TestUtil.findAll(em, OrderType.class).isEmpty()) {
            orderType = OrderTypeResourceIT.createUpdatedEntity();
            em.persist(orderType);
            em.flush();
        } else {
            orderType = TestUtil.findAll(em, OrderType.class).getFirst();
        }
        updatedOrder.setOrderType(orderType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedOrder.setParty(party);
        return updatedOrder;
    }

    @BeforeEach
    void initTest() {
        order = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrder != null) {
            orderRepository.delete(insertedOrder);
            insertedOrder = null;
        }
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Order
        var returnedOrder = om.readValue(
            restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Order.class
        );

        // Validate the Order in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderUpdatableFieldsEquals(returnedOrder, getPersistedOrder(returnedOrder));

        insertedOrder = returnedOrder;
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        order.setOrderDate(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].orderRefNo").value(hasItem(DEFAULT_ORDER_REF_NO)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isRushOrder").value(hasItem(DEFAULT_IS_RUSH_ORDER)))
            .andExpect(jsonPath("$.[*].needsInventoryIssuance").value(hasItem(DEFAULT_NEEDS_INVENTORY_ISSUANCE)))
            .andExpect(jsonPath("$.[*].remainingSubTotal").value(hasItem(sameNumber(DEFAULT_REMAINING_SUB_TOTAL))))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(sameNumber(DEFAULT_GRAND_TOTAL))))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].versionRef").value(hasItem(DEFAULT_VERSION_REF)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS)))
            .andExpect(jsonPath("$.[*].deliveryTerms").value(hasItem(DEFAULT_DELIVERY_TERMS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].attr1").value(hasItem(DEFAULT_ATTR_1)))
            .andExpect(jsonPath("$.[*].attr2").value(hasItem(DEFAULT_ATTR_2)))
            .andExpect(jsonPath("$.[*].attr3").value(hasItem(DEFAULT_ATTR_3)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.orderRefNo").value(DEFAULT_ORDER_REF_NO))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.isRushOrder").value(DEFAULT_IS_RUSH_ORDER))
            .andExpect(jsonPath("$.needsInventoryIssuance").value(DEFAULT_NEEDS_INVENTORY_ISSUANCE))
            .andExpect(jsonPath("$.remainingSubTotal").value(sameNumber(DEFAULT_REMAINING_SUB_TOTAL)))
            .andExpect(jsonPath("$.grandTotal").value(sameNumber(DEFAULT_GRAND_TOTAL)))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.versionRef").value(DEFAULT_VERSION_REF))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.paymentTerms").value(DEFAULT_PAYMENT_TERMS))
            .andExpect(jsonPath("$.deliveryTerms").value(DEFAULT_DELIVERY_TERMS))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.attr1").value(DEFAULT_ATTR_1))
            .andExpect(jsonPath("$.attr2").value(DEFAULT_ATTR_2))
            .andExpect(jsonPath("$.attr3").value(DEFAULT_ATTR_3));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where name equals to
        defaultOrderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where name in
        defaultOrderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where name is not null
        defaultOrderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where name contains
        defaultOrderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where name does not contain
        defaultOrderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderRefNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderRefNo equals to
        defaultOrderFiltering("orderRefNo.equals=" + DEFAULT_ORDER_REF_NO, "orderRefNo.equals=" + UPDATED_ORDER_REF_NO);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderRefNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderRefNo in
        defaultOrderFiltering(
            "orderRefNo.in=" + DEFAULT_ORDER_REF_NO + "," + UPDATED_ORDER_REF_NO,
            "orderRefNo.in=" + UPDATED_ORDER_REF_NO
        );
    }

    @Test
    @Transactional
    void getAllOrdersByOrderRefNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderRefNo is not null
        defaultOrderFiltering("orderRefNo.specified=true", "orderRefNo.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderRefNoContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderRefNo contains
        defaultOrderFiltering("orderRefNo.contains=" + DEFAULT_ORDER_REF_NO, "orderRefNo.contains=" + UPDATED_ORDER_REF_NO);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderRefNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderRefNo does not contain
        defaultOrderFiltering("orderRefNo.doesNotContain=" + UPDATED_ORDER_REF_NO, "orderRefNo.doesNotContain=" + DEFAULT_ORDER_REF_NO);
    }

    @Test
    @Transactional
    void getAllOrdersByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where externalId equals to
        defaultOrderFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where externalId in
        defaultOrderFiltering("externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID, "externalId.in=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where externalId is not null
        defaultOrderFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where externalId contains
        defaultOrderFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where externalId does not contain
        defaultOrderFiltering("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID, "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate equals to
        defaultOrderFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate in
        defaultOrderFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderDate is not null
        defaultOrderFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where entryDate equals to
        defaultOrderFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where entryDate in
        defaultOrderFiltering("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE, "entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where entryDate is not null
        defaultOrderFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate equals to
        defaultOrderFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate in
        defaultOrderFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate is not null
        defaultOrderFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate is greater than or equal to
        defaultOrderFiltering(
            "deliveryDate.greaterThanOrEqual=" + DEFAULT_DELIVERY_DATE,
            "deliveryDate.greaterThanOrEqual=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate is less than or equal to
        defaultOrderFiltering(
            "deliveryDate.lessThanOrEqual=" + DEFAULT_DELIVERY_DATE,
            "deliveryDate.lessThanOrEqual=" + SMALLER_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate is less than
        defaultOrderFiltering("deliveryDate.lessThan=" + UPDATED_DELIVERY_DATE, "deliveryDate.lessThan=" + DEFAULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDate is greater than
        defaultOrderFiltering("deliveryDate.greaterThan=" + SMALLER_DELIVERY_DATE, "deliveryDate.greaterThan=" + DEFAULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority equals to
        defaultOrderFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority in
        defaultOrderFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority is not null
        defaultOrderFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority is greater than or equal to
        defaultOrderFiltering("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY, "priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority is less than or equal to
        defaultOrderFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority is less than
        defaultOrderFiltering("priority.lessThan=" + (DEFAULT_PRIORITY + 1), "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOrdersByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where priority is greater than
        defaultOrderFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOrdersByIsRushOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where isRushOrder equals to
        defaultOrderFiltering("isRushOrder.equals=" + DEFAULT_IS_RUSH_ORDER, "isRushOrder.equals=" + UPDATED_IS_RUSH_ORDER);
    }

    @Test
    @Transactional
    void getAllOrdersByIsRushOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where isRushOrder in
        defaultOrderFiltering(
            "isRushOrder.in=" + DEFAULT_IS_RUSH_ORDER + "," + UPDATED_IS_RUSH_ORDER,
            "isRushOrder.in=" + UPDATED_IS_RUSH_ORDER
        );
    }

    @Test
    @Transactional
    void getAllOrdersByIsRushOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where isRushOrder is not null
        defaultOrderFiltering("isRushOrder.specified=true", "isRushOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByNeedsInventoryIssuanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where needsInventoryIssuance equals to
        defaultOrderFiltering(
            "needsInventoryIssuance.equals=" + DEFAULT_NEEDS_INVENTORY_ISSUANCE,
            "needsInventoryIssuance.equals=" + UPDATED_NEEDS_INVENTORY_ISSUANCE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByNeedsInventoryIssuanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where needsInventoryIssuance in
        defaultOrderFiltering(
            "needsInventoryIssuance.in=" + DEFAULT_NEEDS_INVENTORY_ISSUANCE + "," + UPDATED_NEEDS_INVENTORY_ISSUANCE,
            "needsInventoryIssuance.in=" + UPDATED_NEEDS_INVENTORY_ISSUANCE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByNeedsInventoryIssuanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where needsInventoryIssuance is not null
        defaultOrderFiltering("needsInventoryIssuance.specified=true", "needsInventoryIssuance.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal equals to
        defaultOrderFiltering(
            "remainingSubTotal.equals=" + DEFAULT_REMAINING_SUB_TOTAL,
            "remainingSubTotal.equals=" + UPDATED_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal in
        defaultOrderFiltering(
            "remainingSubTotal.in=" + DEFAULT_REMAINING_SUB_TOTAL + "," + UPDATED_REMAINING_SUB_TOTAL,
            "remainingSubTotal.in=" + UPDATED_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal is not null
        defaultOrderFiltering("remainingSubTotal.specified=true", "remainingSubTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal is greater than or equal to
        defaultOrderFiltering(
            "remainingSubTotal.greaterThanOrEqual=" + DEFAULT_REMAINING_SUB_TOTAL,
            "remainingSubTotal.greaterThanOrEqual=" + UPDATED_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal is less than or equal to
        defaultOrderFiltering(
            "remainingSubTotal.lessThanOrEqual=" + DEFAULT_REMAINING_SUB_TOTAL,
            "remainingSubTotal.lessThanOrEqual=" + SMALLER_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal is less than
        defaultOrderFiltering(
            "remainingSubTotal.lessThan=" + UPDATED_REMAINING_SUB_TOTAL,
            "remainingSubTotal.lessThan=" + DEFAULT_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRemainingSubTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remainingSubTotal is greater than
        defaultOrderFiltering(
            "remainingSubTotal.greaterThan=" + SMALLER_REMAINING_SUB_TOTAL,
            "remainingSubTotal.greaterThan=" + DEFAULT_REMAINING_SUB_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal equals to
        defaultOrderFiltering("grandTotal.equals=" + DEFAULT_GRAND_TOTAL, "grandTotal.equals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal in
        defaultOrderFiltering("grandTotal.in=" + DEFAULT_GRAND_TOTAL + "," + UPDATED_GRAND_TOTAL, "grandTotal.in=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is not null
        defaultOrderFiltering("grandTotal.specified=true", "grandTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is greater than or equal to
        defaultOrderFiltering(
            "grandTotal.greaterThanOrEqual=" + DEFAULT_GRAND_TOTAL,
            "grandTotal.greaterThanOrEqual=" + UPDATED_GRAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is less than or equal to
        defaultOrderFiltering("grandTotal.lessThanOrEqual=" + DEFAULT_GRAND_TOTAL, "grandTotal.lessThanOrEqual=" + SMALLER_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is less than
        defaultOrderFiltering("grandTotal.lessThan=" + UPDATED_GRAND_TOTAL, "grandTotal.lessThan=" + DEFAULT_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByGrandTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where grandTotal is greater than
        defaultOrderFiltering("grandTotal.greaterThan=" + SMALLER_GRAND_TOTAL, "grandTotal.greaterThan=" + DEFAULT_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllOrdersByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where projectName equals to
        defaultOrderFiltering("projectName.equals=" + DEFAULT_PROJECT_NAME, "projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where projectName in
        defaultOrderFiltering(
            "projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME,
            "projectName.in=" + UPDATED_PROJECT_NAME
        );
    }

    @Test
    @Transactional
    void getAllOrdersByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where projectName is not null
        defaultOrderFiltering("projectName.specified=true", "projectName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where projectName contains
        defaultOrderFiltering("projectName.contains=" + DEFAULT_PROJECT_NAME, "projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where projectName does not contain
        defaultOrderFiltering("projectName.doesNotContain=" + UPDATED_PROJECT_NAME, "projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version equals to
        defaultOrderFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version in
        defaultOrderFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version is not null
        defaultOrderFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version is greater than or equal to
        defaultOrderFiltering("version.greaterThanOrEqual=" + DEFAULT_VERSION, "version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version is less than or equal to
        defaultOrderFiltering("version.lessThanOrEqual=" + DEFAULT_VERSION, "version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version is less than
        defaultOrderFiltering("version.lessThan=" + UPDATED_VERSION, "version.lessThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where version is greater than
        defaultOrderFiltering("version.greaterThan=" + SMALLER_VERSION, "version.greaterThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionRefIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where versionRef equals to
        defaultOrderFiltering("versionRef.equals=" + DEFAULT_VERSION_REF, "versionRef.equals=" + UPDATED_VERSION_REF);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionRefIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where versionRef in
        defaultOrderFiltering("versionRef.in=" + DEFAULT_VERSION_REF + "," + UPDATED_VERSION_REF, "versionRef.in=" + UPDATED_VERSION_REF);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where versionRef is not null
        defaultOrderFiltering("versionRef.specified=true", "versionRef.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByVersionRefContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where versionRef contains
        defaultOrderFiltering("versionRef.contains=" + DEFAULT_VERSION_REF, "versionRef.contains=" + UPDATED_VERSION_REF);
    }

    @Test
    @Transactional
    void getAllOrdersByVersionRefNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where versionRef does not contain
        defaultOrderFiltering("versionRef.doesNotContain=" + UPDATED_VERSION_REF, "versionRef.doesNotContain=" + DEFAULT_VERSION_REF);
    }

    @Test
    @Transactional
    void getAllOrdersByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remarks equals to
        defaultOrderFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllOrdersByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remarks in
        defaultOrderFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllOrdersByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remarks is not null
        defaultOrderFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remarks contains
        defaultOrderFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllOrdersByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where remarks does not contain
        defaultOrderFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr1 equals to
        defaultOrderFiltering("attr1.equals=" + DEFAULT_ATTR_1, "attr1.equals=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr1 in
        defaultOrderFiltering("attr1.in=" + DEFAULT_ATTR_1 + "," + UPDATED_ATTR_1, "attr1.in=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr1 is not null
        defaultOrderFiltering("attr1.specified=true", "attr1.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByAttr1ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr1 contains
        defaultOrderFiltering("attr1.contains=" + DEFAULT_ATTR_1, "attr1.contains=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr1 does not contain
        defaultOrderFiltering("attr1.doesNotContain=" + UPDATED_ATTR_1, "attr1.doesNotContain=" + DEFAULT_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr2 equals to
        defaultOrderFiltering("attr2.equals=" + DEFAULT_ATTR_2, "attr2.equals=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr2 in
        defaultOrderFiltering("attr2.in=" + DEFAULT_ATTR_2 + "," + UPDATED_ATTR_2, "attr2.in=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr2 is not null
        defaultOrderFiltering("attr2.specified=true", "attr2.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByAttr2ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr2 contains
        defaultOrderFiltering("attr2.contains=" + DEFAULT_ATTR_2, "attr2.contains=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr2 does not contain
        defaultOrderFiltering("attr2.doesNotContain=" + UPDATED_ATTR_2, "attr2.doesNotContain=" + DEFAULT_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr3 equals to
        defaultOrderFiltering("attr3.equals=" + DEFAULT_ATTR_3, "attr3.equals=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr3 in
        defaultOrderFiltering("attr3.in=" + DEFAULT_ATTR_3 + "," + UPDATED_ATTR_3, "attr3.in=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr3 is not null
        defaultOrderFiltering("attr3.specified=true", "attr3.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByAttr3ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr3 contains
        defaultOrderFiltering("attr3.contains=" + DEFAULT_ATTR_3, "attr3.contains=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrdersByAttr3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where attr3 does not contain
        defaultOrderFiltering("attr3.doesNotContain=" + UPDATED_ATTR_3, "attr3.doesNotContain=" + DEFAULT_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsEqualToSomething() throws Exception {
        OrderType orderType;
        if (TestUtil.findAll(em, OrderType.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            orderType = OrderTypeResourceIT.createEntity();
        } else {
            orderType = TestUtil.findAll(em, OrderType.class).getFirst();
        }
        em.persist(orderType);
        em.flush();
        order.setOrderType(orderType);
        orderRepository.saveAndFlush(order);
        Long orderTypeId = orderType.getId();
        // Get all the orderList where orderType equals to orderTypeId
        defaultOrderShouldBeFound("orderTypeId.equals=" + orderTypeId);

        // Get all the orderList where orderType equals to (orderTypeId + 1)
        defaultOrderShouldNotBeFound("orderTypeId.equals=" + (orderTypeId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersBySalesChannelIsEqualToSomething() throws Exception {
        SalesChannel salesChannel;
        if (TestUtil.findAll(em, SalesChannel.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            salesChannel = SalesChannelResourceIT.createEntity();
        } else {
            salesChannel = TestUtil.findAll(em, SalesChannel.class).getFirst();
        }
        em.persist(salesChannel);
        em.flush();
        order.setSalesChannel(salesChannel);
        orderRepository.saveAndFlush(order);
        Long salesChannelId = salesChannel.getId();
        // Get all the orderList where salesChannel equals to salesChannelId
        defaultOrderShouldBeFound("salesChannelId.equals=" + salesChannelId);

        // Get all the orderList where salesChannel equals to (salesChannelId + 1)
        defaultOrderShouldNotBeFound("salesChannelId.equals=" + (salesChannelId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        order.setParty(party);
        orderRepository.saveAndFlush(order);
        Long partyId = party.getId();
        // Get all the orderList where party equals to partyId
        defaultOrderShouldBeFound("partyId.equals=" + partyId);

        // Get all the orderList where party equals to (partyId + 1)
        defaultOrderShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersBySupplierIsEqualToSomething() throws Exception {
        Party supplier;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            supplier = PartyResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(supplier);
        em.flush();
        order.setSupplier(supplier);
        orderRepository.saveAndFlush(order);
        Long supplierId = supplier.getId();
        // Get all the orderList where supplier equals to supplierId
        defaultOrderShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the orderList where supplier equals to (supplierId + 1)
        defaultOrderShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        order.setStatus(status);
        orderRepository.saveAndFlush(order);
        Long statusId = status.getId();
        // Get all the orderList where status equals to statusId
        defaultOrderShouldBeFound("statusId.equals=" + statusId);

        // Get all the orderList where status equals to (statusId + 1)
        defaultOrderShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        order.setCurrencyUom(currencyUom);
        orderRepository.saveAndFlush(order);
        Long currencyUomId = currencyUom.getId();
        // Get all the orderList where currencyUom equals to currencyUomId
        defaultOrderShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the orderList where currencyUom equals to (currencyUomId + 1)
        defaultOrderShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByCurrencyConversionIsEqualToSomething() throws Exception {
        UomConversion currencyConversion;
        if (TestUtil.findAll(em, UomConversion.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            currencyConversion = UomConversionResourceIT.createEntity(em);
        } else {
            currencyConversion = TestUtil.findAll(em, UomConversion.class).getFirst();
        }
        em.persist(currencyConversion);
        em.flush();
        order.setCurrencyConversion(currencyConversion);
        orderRepository.saveAndFlush(order);
        Long currencyConversionId = currencyConversion.getId();
        // Get all the orderList where currencyConversion equals to currencyConversionId
        defaultOrderShouldBeFound("currencyConversionId.equals=" + currencyConversionId);

        // Get all the orderList where currencyConversion equals to (currencyConversionId + 1)
        defaultOrderShouldNotBeFound("currencyConversionId.equals=" + (currencyConversionId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByQuoteIsEqualToSomething() throws Exception {
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            quote = QuoteResourceIT.createEntity(em);
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        em.persist(quote);
        em.flush();
        order.setQuote(quote);
        orderRepository.saveAndFlush(order);
        Long quoteId = quote.getId();
        // Get all the orderList where quote equals to quoteId
        defaultOrderShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the orderList where quote equals to (quoteId + 1)
        defaultOrderShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        order.setFacility(facility);
        orderRepository.saveAndFlush(order);
        Long facilityId = facility.getId();
        // Get all the orderList where facility equals to facilityId
        defaultOrderShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the orderList where facility equals to (facilityId + 1)
        defaultOrderShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByProductStoreIsEqualToSomething() throws Exception {
        ProductStore productStore;
        if (TestUtil.findAll(em, ProductStore.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            productStore = ProductStoreResourceIT.createEntity();
        } else {
            productStore = TestUtil.findAll(em, ProductStore.class).getFirst();
        }
        em.persist(productStore);
        em.flush();
        order.setProductStore(productStore);
        orderRepository.saveAndFlush(order);
        Long productStoreId = productStore.getId();
        // Get all the orderList where productStore equals to productStoreId
        defaultOrderShouldBeFound("productStoreId.equals=" + productStoreId);

        // Get all the orderList where productStore equals to (productStoreId + 1)
        defaultOrderShouldNotBeFound("productStoreId.equals=" + (productStoreId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            department = DepartmentResourceIT.createEntity();
        } else {
            department = TestUtil.findAll(em, Department.class).getFirst();
        }
        em.persist(department);
        em.flush();
        order.setDepartment(department);
        orderRepository.saveAndFlush(order);
        Long departmentId = department.getId();
        // Get all the orderList where department equals to departmentId
        defaultOrderShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the orderList where department equals to (departmentId + 1)
        defaultOrderShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByCustomTimePeriodIsEqualToSomething() throws Exception {
        CustomTimePeriod customTimePeriod;
        if (TestUtil.findAll(em, CustomTimePeriod.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            customTimePeriod = CustomTimePeriodResourceIT.createEntity();
        } else {
            customTimePeriod = TestUtil.findAll(em, CustomTimePeriod.class).getFirst();
        }
        em.persist(customTimePeriod);
        em.flush();
        order.setCustomTimePeriod(customTimePeriod);
        orderRepository.saveAndFlush(order);
        Long customTimePeriodId = customTimePeriod.getId();
        // Get all the orderList where customTimePeriod equals to customTimePeriodId
        defaultOrderShouldBeFound("customTimePeriodId.equals=" + customTimePeriodId);

        // Get all the orderList where customTimePeriod equals to (customTimePeriodId + 1)
        defaultOrderShouldNotBeFound("customTimePeriodId.equals=" + (customTimePeriodId + 1));
    }

    private void defaultOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderShouldBeFound(shouldBeFound);
        defaultOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].orderRefNo").value(hasItem(DEFAULT_ORDER_REF_NO)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isRushOrder").value(hasItem(DEFAULT_IS_RUSH_ORDER)))
            .andExpect(jsonPath("$.[*].needsInventoryIssuance").value(hasItem(DEFAULT_NEEDS_INVENTORY_ISSUANCE)))
            .andExpect(jsonPath("$.[*].remainingSubTotal").value(hasItem(sameNumber(DEFAULT_REMAINING_SUB_TOTAL))))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(sameNumber(DEFAULT_GRAND_TOTAL))))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].versionRef").value(hasItem(DEFAULT_VERSION_REF)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS)))
            .andExpect(jsonPath("$.[*].deliveryTerms").value(hasItem(DEFAULT_DELIVERY_TERMS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].attr1").value(hasItem(DEFAULT_ATTR_1)))
            .andExpect(jsonPath("$.[*].attr2").value(hasItem(DEFAULT_ATTR_2)))
            .andExpect(jsonPath("$.[*].attr3").value(hasItem(DEFAULT_ATTR_3)));

        // Check, that the count call also returns 1
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .name(UPDATED_NAME)
            .orderRefNo(UPDATED_ORDER_REF_NO)
            .externalId(UPDATED_EXTERNAL_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .priority(UPDATED_PRIORITY)
            .isRushOrder(UPDATED_IS_RUSH_ORDER)
            .needsInventoryIssuance(UPDATED_NEEDS_INVENTORY_ISSUANCE)
            .remainingSubTotal(UPDATED_REMAINING_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .version(UPDATED_VERSION)
            .versionRef(UPDATED_VERSION_REF)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderToMatchAllProperties(updatedOrder);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL_ID, order.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .name(UPDATED_NAME)
            .orderRefNo(UPDATED_ORDER_REF_NO)
            .externalId(UPDATED_EXTERNAL_ID)
            .isRushOrder(UPDATED_IS_RUSH_ORDER)
            .needsInventoryIssuance(UPDATED_NEEDS_INVENTORY_ISSUANCE)
            .remainingSubTotal(UPDATED_REMAINING_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .version(UPDATED_VERSION)
            .versionRef(UPDATED_VERSION_REF)
            .description(UPDATED_DESCRIPTION)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO)
            .attr1(UPDATED_ATTR_1)
            .attr3(UPDATED_ATTR_3);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrder, order), getPersistedOrder(order));
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .name(UPDATED_NAME)
            .orderRefNo(UPDATED_ORDER_REF_NO)
            .externalId(UPDATED_EXTERNAL_ID)
            .orderDate(UPDATED_ORDER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .priority(UPDATED_PRIORITY)
            .isRushOrder(UPDATED_IS_RUSH_ORDER)
            .needsInventoryIssuance(UPDATED_NEEDS_INVENTORY_ISSUANCE)
            .remainingSubTotal(UPDATED_REMAINING_SUB_TOTAL)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .version(UPDATED_VERSION)
            .versionRef(UPDATED_VERSION_REF)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUpdatableFieldsEquals(partialUpdatedOrder, getPersistedOrder(partialUpdatedOrder));
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, order.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderRepository.count();
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

    protected Order getPersistedOrder(Order order) {
        return orderRepository.findById(order.getId()).orElseThrow();
    }

    protected void assertPersistedOrderToMatchAllProperties(Order expectedOrder) {
        assertOrderAllPropertiesEquals(expectedOrder, getPersistedOrder(expectedOrder));
    }

    protected void assertPersistedOrderToMatchUpdatableProperties(Order expectedOrder) {
        assertOrderAllUpdatablePropertiesEquals(expectedOrder, getPersistedOrder(expectedOrder));
    }
}
