package com.snp.web.rest;

import static com.snp.domain.OrderItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.OrderItemType;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.ProductFacility;
import com.snp.domain.ProductFeature;
import com.snp.domain.QuoteItem;
import com.snp.domain.ShoppingListItem;
import com.snp.domain.Status;
import com.snp.domain.SupplierProduct;
import com.snp.domain.TaxAuthorityRateType;
import com.snp.domain.Uom;
import com.snp.repository.OrderItemRepository;
import com.snp.service.OrderItemService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CANCEL_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_CANCEL_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_CANCEL_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SELECTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELECTED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SELECTED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_LIST_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_LIST_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_LIST_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_MODIFIED_PRICE = false;
    private static final Boolean UPDATED_IS_MODIFIED_PRICE = true;

    private static final Instant DEFAULT_ESTIMATED_SHIP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_SHIP_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_ESTIMATED_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_DELIVERY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_SHIP_BEFORE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIP_BEFORE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_SHIP_AFTER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIP_AFTER_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_ATTR_1 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR_2 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR_3 = "AAAAAAAAAA";
    private static final String UPDATED_ATTR_3 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemRepository orderItemRepositoryMock;

    @Mock
    private OrderItemService orderItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    private OrderItem insertedOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .cancelQuantity(DEFAULT_CANCEL_QUANTITY)
            .selectedAmount(DEFAULT_SELECTED_AMOUNT)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .unitListPrice(DEFAULT_UNIT_LIST_PRICE)
            .isModifiedPrice(DEFAULT_IS_MODIFIED_PRICE)
            .estimatedShipDate(DEFAULT_ESTIMATED_SHIP_DATE)
            .estimatedDeliveryDate(DEFAULT_ESTIMATED_DELIVERY_DATE)
            .shipBeforeDate(DEFAULT_SHIP_BEFORE_DATE)
            .shipAfterDate(DEFAULT_SHIP_AFTER_DATE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .attr1(DEFAULT_ATTR_1)
            .attr2(DEFAULT_ATTR_2)
            .attr3(DEFAULT_ATTR_3);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        orderItem.setOrder(order);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem updatedOrderItem = new OrderItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitListPrice(UPDATED_UNIT_LIST_PRICE)
            .isModifiedPrice(UPDATED_IS_MODIFIED_PRICE)
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .shipBeforeDate(UPDATED_SHIP_BEFORE_DATE)
            .shipAfterDate(UPDATED_SHIP_AFTER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        updatedOrderItem.setOrder(order);
        return updatedOrderItem;
    }

    @BeforeEach
    void initTest() {
        orderItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderItem != null) {
            orderItemRepository.delete(insertedOrderItem);
            insertedOrderItem = null;
        }
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderItem
        var returnedOrderItem = om.readValue(
            restOrderItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderItem.class
        );

        // Validate the OrderItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderItemUpdatableFieldsEquals(returnedOrderItem, getPersistedOrderItem(returnedOrderItem));

        insertedOrderItem = returnedOrderItem;
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderItem.setQuantity(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].cancelQuantity").value(hasItem(sameNumber(DEFAULT_CANCEL_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].unitListPrice").value(hasItem(sameNumber(DEFAULT_UNIT_LIST_PRICE))))
            .andExpect(jsonPath("$.[*].isModifiedPrice").value(hasItem(DEFAULT_IS_MODIFIED_PRICE)))
            .andExpect(jsonPath("$.[*].estimatedShipDate").value(hasItem(DEFAULT_ESTIMATED_SHIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].shipBeforeDate").value(hasItem(DEFAULT_SHIP_BEFORE_DATE.toString())))
            .andExpect(jsonPath("$.[*].shipAfterDate").value(hasItem(DEFAULT_SHIP_AFTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].attr1").value(hasItem(DEFAULT_ATTR_1)))
            .andExpect(jsonPath("$.[*].attr2").value(hasItem(DEFAULT_ATTR_2)))
            .andExpect(jsonPath("$.[*].attr3").value(hasItem(DEFAULT_ATTR_3)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderItem() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.cancelQuantity").value(sameNumber(DEFAULT_CANCEL_QUANTITY)))
            .andExpect(jsonPath("$.selectedAmount").value(sameNumber(DEFAULT_SELECTED_AMOUNT)))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.unitListPrice").value(sameNumber(DEFAULT_UNIT_LIST_PRICE)))
            .andExpect(jsonPath("$.isModifiedPrice").value(DEFAULT_IS_MODIFIED_PRICE))
            .andExpect(jsonPath("$.estimatedShipDate").value(DEFAULT_ESTIMATED_SHIP_DATE.toString()))
            .andExpect(jsonPath("$.estimatedDeliveryDate").value(DEFAULT_ESTIMATED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.shipBeforeDate").value(DEFAULT_SHIP_BEFORE_DATE.toString()))
            .andExpect(jsonPath("$.shipAfterDate").value(DEFAULT_SHIP_AFTER_DATE.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.attr1").value(DEFAULT_ATTR_1))
            .andExpect(jsonPath("$.attr2").value(DEFAULT_ATTR_2))
            .andExpect(jsonPath("$.attr3").value(DEFAULT_ATTR_3));
    }

    @Test
    @Transactional
    void getOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        Long id = orderItem.getId();

        defaultOrderItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo equals to
        defaultOrderItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo in
        defaultOrderItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo is not null
        defaultOrderItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo is greater than or equal to
        defaultOrderItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo is less than or equal to
        defaultOrderItemFiltering("sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO, "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo is less than
        defaultOrderItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where sequenceNo is greater than
        defaultOrderItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity equals to
        defaultOrderItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity in
        defaultOrderItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is not null
        defaultOrderItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is greater than or equal to
        defaultOrderItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is less than or equal to
        defaultOrderItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is less than
        defaultOrderItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where quantity is greater than
        defaultOrderItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity equals to
        defaultOrderItemFiltering("cancelQuantity.equals=" + DEFAULT_CANCEL_QUANTITY, "cancelQuantity.equals=" + UPDATED_CANCEL_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity in
        defaultOrderItemFiltering(
            "cancelQuantity.in=" + DEFAULT_CANCEL_QUANTITY + "," + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.in=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity is not null
        defaultOrderItemFiltering("cancelQuantity.specified=true", "cancelQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity is greater than or equal to
        defaultOrderItemFiltering(
            "cancelQuantity.greaterThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.greaterThanOrEqual=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity is less than or equal to
        defaultOrderItemFiltering(
            "cancelQuantity.lessThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.lessThanOrEqual=" + SMALLER_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity is less than
        defaultOrderItemFiltering(
            "cancelQuantity.lessThan=" + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.lessThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelQuantity is greater than
        defaultOrderItemFiltering(
            "cancelQuantity.greaterThan=" + SMALLER_CANCEL_QUANTITY,
            "cancelQuantity.greaterThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount equals to
        defaultOrderItemFiltering("selectedAmount.equals=" + DEFAULT_SELECTED_AMOUNT, "selectedAmount.equals=" + UPDATED_SELECTED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount in
        defaultOrderItemFiltering(
            "selectedAmount.in=" + DEFAULT_SELECTED_AMOUNT + "," + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.in=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount is not null
        defaultOrderItemFiltering("selectedAmount.specified=true", "selectedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount is greater than or equal to
        defaultOrderItemFiltering(
            "selectedAmount.greaterThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.greaterThanOrEqual=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount is less than or equal to
        defaultOrderItemFiltering(
            "selectedAmount.lessThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.lessThanOrEqual=" + SMALLER_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount is less than
        defaultOrderItemFiltering(
            "selectedAmount.lessThan=" + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.lessThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsBySelectedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where selectedAmount is greater than
        defaultOrderItemFiltering(
            "selectedAmount.greaterThan=" + SMALLER_SELECTED_AMOUNT,
            "selectedAmount.greaterThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice equals to
        defaultOrderItemFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice in
        defaultOrderItemFiltering("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE, "unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice is not null
        defaultOrderItemFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice is greater than or equal to
        defaultOrderItemFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice is less than or equal to
        defaultOrderItemFiltering("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE, "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice is less than
        defaultOrderItemFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitPrice is greater than
        defaultOrderItemFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice equals to
        defaultOrderItemFiltering("unitListPrice.equals=" + DEFAULT_UNIT_LIST_PRICE, "unitListPrice.equals=" + UPDATED_UNIT_LIST_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice in
        defaultOrderItemFiltering(
            "unitListPrice.in=" + DEFAULT_UNIT_LIST_PRICE + "," + UPDATED_UNIT_LIST_PRICE,
            "unitListPrice.in=" + UPDATED_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice is not null
        defaultOrderItemFiltering("unitListPrice.specified=true", "unitListPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice is greater than or equal to
        defaultOrderItemFiltering(
            "unitListPrice.greaterThanOrEqual=" + DEFAULT_UNIT_LIST_PRICE,
            "unitListPrice.greaterThanOrEqual=" + UPDATED_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice is less than or equal to
        defaultOrderItemFiltering(
            "unitListPrice.lessThanOrEqual=" + DEFAULT_UNIT_LIST_PRICE,
            "unitListPrice.lessThanOrEqual=" + SMALLER_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice is less than
        defaultOrderItemFiltering("unitListPrice.lessThan=" + UPDATED_UNIT_LIST_PRICE, "unitListPrice.lessThan=" + DEFAULT_UNIT_LIST_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUnitListPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where unitListPrice is greater than
        defaultOrderItemFiltering(
            "unitListPrice.greaterThan=" + SMALLER_UNIT_LIST_PRICE,
            "unitListPrice.greaterThan=" + DEFAULT_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByIsModifiedPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where isModifiedPrice equals to
        defaultOrderItemFiltering(
            "isModifiedPrice.equals=" + DEFAULT_IS_MODIFIED_PRICE,
            "isModifiedPrice.equals=" + UPDATED_IS_MODIFIED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByIsModifiedPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where isModifiedPrice in
        defaultOrderItemFiltering(
            "isModifiedPrice.in=" + DEFAULT_IS_MODIFIED_PRICE + "," + UPDATED_IS_MODIFIED_PRICE,
            "isModifiedPrice.in=" + UPDATED_IS_MODIFIED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByIsModifiedPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where isModifiedPrice is not null
        defaultOrderItemFiltering("isModifiedPrice.specified=true", "isModifiedPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedShipDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedShipDate equals to
        defaultOrderItemFiltering(
            "estimatedShipDate.equals=" + DEFAULT_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.equals=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedShipDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedShipDate in
        defaultOrderItemFiltering(
            "estimatedShipDate.in=" + DEFAULT_ESTIMATED_SHIP_DATE + "," + UPDATED_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.in=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedShipDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedShipDate is not null
        defaultOrderItemFiltering("estimatedShipDate.specified=true", "estimatedShipDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedDeliveryDate equals to
        defaultOrderItemFiltering(
            "estimatedDeliveryDate.equals=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.equals=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedDeliveryDate in
        defaultOrderItemFiltering(
            "estimatedDeliveryDate.in=" + DEFAULT_ESTIMATED_DELIVERY_DATE + "," + UPDATED_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.in=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByEstimatedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where estimatedDeliveryDate is not null
        defaultOrderItemFiltering("estimatedDeliveryDate.specified=true", "estimatedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipBeforeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipBeforeDate equals to
        defaultOrderItemFiltering("shipBeforeDate.equals=" + DEFAULT_SHIP_BEFORE_DATE, "shipBeforeDate.equals=" + UPDATED_SHIP_BEFORE_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipBeforeDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipBeforeDate in
        defaultOrderItemFiltering(
            "shipBeforeDate.in=" + DEFAULT_SHIP_BEFORE_DATE + "," + UPDATED_SHIP_BEFORE_DATE,
            "shipBeforeDate.in=" + UPDATED_SHIP_BEFORE_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipBeforeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipBeforeDate is not null
        defaultOrderItemFiltering("shipBeforeDate.specified=true", "shipBeforeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipAfterDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipAfterDate equals to
        defaultOrderItemFiltering("shipAfterDate.equals=" + DEFAULT_SHIP_AFTER_DATE, "shipAfterDate.equals=" + UPDATED_SHIP_AFTER_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipAfterDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipAfterDate in
        defaultOrderItemFiltering(
            "shipAfterDate.in=" + DEFAULT_SHIP_AFTER_DATE + "," + UPDATED_SHIP_AFTER_DATE,
            "shipAfterDate.in=" + UPDATED_SHIP_AFTER_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderItemsByShipAfterDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where shipAfterDate is not null
        defaultOrderItemFiltering("shipAfterDate.specified=true", "shipAfterDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where entryDate equals to
        defaultOrderItemFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where entryDate in
        defaultOrderItemFiltering("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE, "entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where entryDate is not null
        defaultOrderItemFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr1 equals to
        defaultOrderItemFiltering("attr1.equals=" + DEFAULT_ATTR_1, "attr1.equals=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr1 in
        defaultOrderItemFiltering("attr1.in=" + DEFAULT_ATTR_1 + "," + UPDATED_ATTR_1, "attr1.in=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr1 is not null
        defaultOrderItemFiltering("attr1.specified=true", "attr1.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr1ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr1 contains
        defaultOrderItemFiltering("attr1.contains=" + DEFAULT_ATTR_1, "attr1.contains=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr1 does not contain
        defaultOrderItemFiltering("attr1.doesNotContain=" + UPDATED_ATTR_1, "attr1.doesNotContain=" + DEFAULT_ATTR_1);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr2 equals to
        defaultOrderItemFiltering("attr2.equals=" + DEFAULT_ATTR_2, "attr2.equals=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr2 in
        defaultOrderItemFiltering("attr2.in=" + DEFAULT_ATTR_2 + "," + UPDATED_ATTR_2, "attr2.in=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr2 is not null
        defaultOrderItemFiltering("attr2.specified=true", "attr2.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr2ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr2 contains
        defaultOrderItemFiltering("attr2.contains=" + DEFAULT_ATTR_2, "attr2.contains=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr2 does not contain
        defaultOrderItemFiltering("attr2.doesNotContain=" + UPDATED_ATTR_2, "attr2.doesNotContain=" + DEFAULT_ATTR_2);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr3 equals to
        defaultOrderItemFiltering("attr3.equals=" + DEFAULT_ATTR_3, "attr3.equals=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr3 in
        defaultOrderItemFiltering("attr3.in=" + DEFAULT_ATTR_3 + "," + UPDATED_ATTR_3, "attr3.in=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr3 is not null
        defaultOrderItemFiltering("attr3.specified=true", "attr3.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr3ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr3 contains
        defaultOrderItemFiltering("attr3.contains=" + DEFAULT_ATTR_3, "attr3.contains=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAttr3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where attr3 does not contain
        defaultOrderItemFiltering("attr3.doesNotContain=" + UPDATED_ATTR_3, "attr3.doesNotContain=" + DEFAULT_ATTR_3);
    }

    @Test
    @Transactional
    void getAllOrderItemsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderItem.setOrder(order);
        orderItemRepository.saveAndFlush(orderItem);
        Long orderId = order.getId();
        // Get all the orderItemList where order equals to orderId
        defaultOrderItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderItemList where order equals to (orderId + 1)
        defaultOrderItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByOrderItemTypeIsEqualToSomething() throws Exception {
        OrderItemType orderItemType;
        if (TestUtil.findAll(em, OrderItemType.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            orderItemType = OrderItemTypeResourceIT.createEntity();
        } else {
            orderItemType = TestUtil.findAll(em, OrderItemType.class).getFirst();
        }
        em.persist(orderItemType);
        em.flush();
        orderItem.setOrderItemType(orderItemType);
        orderItemRepository.saveAndFlush(orderItem);
        Long orderItemTypeId = orderItemType.getId();
        // Get all the orderItemList where orderItemType equals to orderItemTypeId
        defaultOrderItemShouldBeFound("orderItemTypeId.equals=" + orderItemTypeId);

        // Get all the orderItemList where orderItemType equals to (orderItemTypeId + 1)
        defaultOrderItemShouldNotBeFound("orderItemTypeId.equals=" + (orderItemTypeId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        orderItem.setProduct(product);
        orderItemRepository.saveAndFlush(orderItem);
        Long productId = product.getId();
        // Get all the orderItemList where product equals to productId
        defaultOrderItemShouldBeFound("productId.equals=" + productId);

        // Get all the orderItemList where product equals to (productId + 1)
        defaultOrderItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        orderItem.setUom(uom);
        orderItemRepository.saveAndFlush(orderItem);
        Long uomId = uom.getId();
        // Get all the orderItemList where uom equals to uomId
        defaultOrderItemShouldBeFound("uomId.equals=" + uomId);

        // Get all the orderItemList where uom equals to (uomId + 1)
        defaultOrderItemShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsBySupplierProductIsEqualToSomething() throws Exception {
        SupplierProduct supplierProduct;
        if (TestUtil.findAll(em, SupplierProduct.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            supplierProduct = SupplierProductResourceIT.createEntity(em);
        } else {
            supplierProduct = TestUtil.findAll(em, SupplierProduct.class).getFirst();
        }
        em.persist(supplierProduct);
        em.flush();
        orderItem.setSupplierProduct(supplierProduct);
        orderItemRepository.saveAndFlush(orderItem);
        Long supplierProductId = supplierProduct.getId();
        // Get all the orderItemList where supplierProduct equals to supplierProductId
        defaultOrderItemShouldBeFound("supplierProductId.equals=" + supplierProductId);

        // Get all the orderItemList where supplierProduct equals to (supplierProductId + 1)
        defaultOrderItemShouldNotBeFound("supplierProductId.equals=" + (supplierProductId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsBySupplierIsEqualToSomething() throws Exception {
        Party supplier;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            supplier = PartyResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(supplier);
        em.flush();
        orderItem.setSupplier(supplier);
        orderItemRepository.saveAndFlush(orderItem);
        Long supplierId = supplier.getId();
        // Get all the orderItemList where supplier equals to supplierId
        defaultOrderItemShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the orderItemList where supplier equals to (supplierId + 1)
        defaultOrderItemShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        orderItem.setProductFeature(productFeature);
        orderItemRepository.saveAndFlush(orderItem);
        Long productFeatureId = productFeature.getId();
        // Get all the orderItemList where productFeature equals to productFeatureId
        defaultOrderItemShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the orderItemList where productFeature equals to (productFeatureId + 1)
        defaultOrderItemShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByProductFacilityIsEqualToSomething() throws Exception {
        ProductFacility productFacility;
        if (TestUtil.findAll(em, ProductFacility.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            productFacility = ProductFacilityResourceIT.createEntity(em);
        } else {
            productFacility = TestUtil.findAll(em, ProductFacility.class).getFirst();
        }
        em.persist(productFacility);
        em.flush();
        orderItem.setProductFacility(productFacility);
        orderItemRepository.saveAndFlush(orderItem);
        Long productFacilityId = productFacility.getId();
        // Get all the orderItemList where productFacility equals to productFacilityId
        defaultOrderItemShouldBeFound("productFacilityId.equals=" + productFacilityId);

        // Get all the orderItemList where productFacility equals to (productFacilityId + 1)
        defaultOrderItemShouldNotBeFound("productFacilityId.equals=" + (productFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByFromInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem fromInventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            fromInventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            fromInventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(fromInventoryItem);
        em.flush();
        orderItem.setFromInventoryItem(fromInventoryItem);
        orderItemRepository.saveAndFlush(orderItem);
        Long fromInventoryItemId = fromInventoryItem.getId();
        // Get all the orderItemList where fromInventoryItem equals to fromInventoryItemId
        defaultOrderItemShouldBeFound("fromInventoryItemId.equals=" + fromInventoryItemId);

        // Get all the orderItemList where fromInventoryItem equals to (fromInventoryItemId + 1)
        defaultOrderItemShouldNotBeFound("fromInventoryItemId.equals=" + (fromInventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByShoppingListItemIsEqualToSomething() throws Exception {
        ShoppingListItem shoppingListItem;
        if (TestUtil.findAll(em, ShoppingListItem.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            shoppingListItem = ShoppingListItemResourceIT.createEntity(em);
        } else {
            shoppingListItem = TestUtil.findAll(em, ShoppingListItem.class).getFirst();
        }
        em.persist(shoppingListItem);
        em.flush();
        orderItem.setShoppingListItem(shoppingListItem);
        orderItemRepository.saveAndFlush(orderItem);
        Long shoppingListItemId = shoppingListItem.getId();
        // Get all the orderItemList where shoppingListItem equals to shoppingListItemId
        defaultOrderItemShouldBeFound("shoppingListItemId.equals=" + shoppingListItemId);

        // Get all the orderItemList where shoppingListItem equals to (shoppingListItemId + 1)
        defaultOrderItemShouldNotBeFound("shoppingListItemId.equals=" + (shoppingListItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByQuoteItemIsEqualToSomething() throws Exception {
        QuoteItem quoteItem;
        if (TestUtil.findAll(em, QuoteItem.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            quoteItem = QuoteItemResourceIT.createEntity(em);
        } else {
            quoteItem = TestUtil.findAll(em, QuoteItem.class).getFirst();
        }
        em.persist(quoteItem);
        em.flush();
        orderItem.setQuoteItem(quoteItem);
        orderItemRepository.saveAndFlush(orderItem);
        Long quoteItemId = quoteItem.getId();
        // Get all the orderItemList where quoteItem equals to quoteItemId
        defaultOrderItemShouldBeFound("quoteItemId.equals=" + quoteItemId);

        // Get all the orderItemList where quoteItem equals to (quoteItemId + 1)
        defaultOrderItemShouldNotBeFound("quoteItemId.equals=" + (quoteItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        orderItem.setStatus(status);
        orderItemRepository.saveAndFlush(orderItem);
        Long statusId = status.getId();
        // Get all the orderItemList where status equals to statusId
        defaultOrderItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the orderItemList where status equals to (statusId + 1)
        defaultOrderItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxAuthRateIsEqualToSomething() throws Exception {
        TaxAuthorityRateType taxAuthRate;
        if (TestUtil.findAll(em, TaxAuthorityRateType.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            taxAuthRate = TaxAuthorityRateTypeResourceIT.createEntity(em);
        } else {
            taxAuthRate = TestUtil.findAll(em, TaxAuthorityRateType.class).getFirst();
        }
        em.persist(taxAuthRate);
        em.flush();
        orderItem.setTaxAuthRate(taxAuthRate);
        orderItemRepository.saveAndFlush(orderItem);
        Long taxAuthRateId = taxAuthRate.getId();
        // Get all the orderItemList where taxAuthRate equals to taxAuthRateId
        defaultOrderItemShouldBeFound("taxAuthRateId.equals=" + taxAuthRateId);

        // Get all the orderItemList where taxAuthRate equals to (taxAuthRateId + 1)
        defaultOrderItemShouldNotBeFound("taxAuthRateId.equals=" + (taxAuthRateId + 1));
    }

    private void defaultOrderItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderItemShouldBeFound(shouldBeFound);
        defaultOrderItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemShouldBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].cancelQuantity").value(hasItem(sameNumber(DEFAULT_CANCEL_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].unitListPrice").value(hasItem(sameNumber(DEFAULT_UNIT_LIST_PRICE))))
            .andExpect(jsonPath("$.[*].isModifiedPrice").value(hasItem(DEFAULT_IS_MODIFIED_PRICE)))
            .andExpect(jsonPath("$.[*].estimatedShipDate").value(hasItem(DEFAULT_ESTIMATED_SHIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].shipBeforeDate").value(hasItem(DEFAULT_SHIP_BEFORE_DATE.toString())))
            .andExpect(jsonPath("$.[*].shipAfterDate").value(hasItem(DEFAULT_SHIP_AFTER_DATE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].attr1").value(hasItem(DEFAULT_ATTR_1)))
            .andExpect(jsonPath("$.[*].attr2").value(hasItem(DEFAULT_ATTR_2)))
            .andExpect(jsonPath("$.[*].attr3").value(hasItem(DEFAULT_ATTR_3)));

        // Check, that the count call also returns 1
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemShouldNotBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItem() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitListPrice(UPDATED_UNIT_LIST_PRICE)
            .isModifiedPrice(UPDATED_IS_MODIFIED_PRICE)
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .shipBeforeDate(UPDATED_SHIP_BEFORE_DATE)
            .shipAfterDate(UPDATED_SHIP_AFTER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderItemToMatchAllProperties(updatedOrderItem);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .shipBeforeDate(UPDATED_SHIP_BEFORE_DATE)
            .shipAfterDate(UPDATED_SHIP_AFTER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderItem, orderItem),
            getPersistedOrderItem(orderItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitListPrice(UPDATED_UNIT_LIST_PRICE)
            .isModifiedPrice(UPDATED_IS_MODIFIED_PRICE)
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .shipBeforeDate(UPDATED_SHIP_BEFORE_DATE)
            .shipAfterDate(UPDATED_SHIP_AFTER_DATE)
            .entryDate(UPDATED_ENTRY_DATE)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2)
            .attr3(UPDATED_ATTR_3);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemUpdatableFieldsEquals(partialUpdatedOrderItem, getPersistedOrderItem(partialUpdatedOrderItem));
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        insertedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderItemRepository.count();
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

    protected OrderItem getPersistedOrderItem(OrderItem orderItem) {
        return orderItemRepository.findById(orderItem.getId()).orElseThrow();
    }

    protected void assertPersistedOrderItemToMatchAllProperties(OrderItem expectedOrderItem) {
        assertOrderItemAllPropertiesEquals(expectedOrderItem, getPersistedOrderItem(expectedOrderItem));
    }

    protected void assertPersistedOrderItemToMatchUpdatableProperties(OrderItem expectedOrderItem) {
        assertOrderItemAllUpdatablePropertiesEquals(expectedOrderItem, getPersistedOrderItem(expectedOrderItem));
    }
}
