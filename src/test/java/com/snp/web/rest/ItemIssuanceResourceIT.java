package com.snp.web.rest;

import static com.snp.domain.ItemIssuanceAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.ItemIssuance;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.Party;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentItem;
import com.snp.repository.ItemIssuanceRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link ItemIssuanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemIssuanceResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_ISSUED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUED_BY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CANCEL_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_CANCEL_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_CANCEL_QUANTITY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/item-issuances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemIssuanceRepository itemIssuanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemIssuanceMockMvc;

    private ItemIssuance itemIssuance;

    private ItemIssuance insertedItemIssuance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemIssuance createEntity(EntityManager em) {
        ItemIssuance itemIssuance = new ItemIssuance()
            .message(DEFAULT_MESSAGE)
            .issuedDate(DEFAULT_ISSUED_DATE)
            .issuedBy(DEFAULT_ISSUED_BY)
            .quantity(DEFAULT_QUANTITY)
            .cancelQuantity(DEFAULT_CANCEL_QUANTITY);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        itemIssuance.setInventoryItem(inventoryItem);
        return itemIssuance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemIssuance createUpdatedEntity(EntityManager em) {
        ItemIssuance updatedItemIssuance = new ItemIssuance()
            .message(UPDATED_MESSAGE)
            .issuedDate(UPDATED_ISSUED_DATE)
            .issuedBy(UPDATED_ISSUED_BY)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createUpdatedEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        updatedItemIssuance.setInventoryItem(inventoryItem);
        return updatedItemIssuance;
    }

    @BeforeEach
    void initTest() {
        itemIssuance = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedItemIssuance != null) {
            itemIssuanceRepository.delete(insertedItemIssuance);
            insertedItemIssuance = null;
        }
    }

    @Test
    @Transactional
    void createItemIssuance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ItemIssuance
        var returnedItemIssuance = om.readValue(
            restItemIssuanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemIssuance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemIssuance.class
        );

        // Validate the ItemIssuance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertItemIssuanceUpdatableFieldsEquals(returnedItemIssuance, getPersistedItemIssuance(returnedItemIssuance));

        insertedItemIssuance = returnedItemIssuance;
    }

    @Test
    @Transactional
    void createItemIssuanceWithExistingId() throws Exception {
        // Create the ItemIssuance with an existing ID
        itemIssuance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemIssuanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemIssuance)))
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itemIssuance.setQuantity(null);

        // Create the ItemIssuance, which fails.

        restItemIssuanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemIssuance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemIssuances() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemIssuance.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuedBy").value(hasItem(DEFAULT_ISSUED_BY)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].cancelQuantity").value(hasItem(sameNumber(DEFAULT_CANCEL_QUANTITY))));
    }

    @Test
    @Transactional
    void getItemIssuance() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get the itemIssuance
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL_ID, itemIssuance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemIssuance.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.issuedDate").value(DEFAULT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.issuedBy").value(DEFAULT_ISSUED_BY))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.cancelQuantity").value(sameNumber(DEFAULT_CANCEL_QUANTITY)));
    }

    @Test
    @Transactional
    void getItemIssuancesByIdFiltering() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        Long id = itemIssuance.getId();

        defaultItemIssuanceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultItemIssuanceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultItemIssuanceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where message equals to
        defaultItemIssuanceFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where message in
        defaultItemIssuanceFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where message is not null
        defaultItemIssuanceFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllItemIssuancesByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where message contains
        defaultItemIssuanceFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where message does not contain
        defaultItemIssuanceFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedDate equals to
        defaultItemIssuanceFiltering("issuedDate.equals=" + DEFAULT_ISSUED_DATE, "issuedDate.equals=" + UPDATED_ISSUED_DATE);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedDate in
        defaultItemIssuanceFiltering(
            "issuedDate.in=" + DEFAULT_ISSUED_DATE + "," + UPDATED_ISSUED_DATE,
            "issuedDate.in=" + UPDATED_ISSUED_DATE
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedDate is not null
        defaultItemIssuanceFiltering("issuedDate.specified=true", "issuedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedBy equals to
        defaultItemIssuanceFiltering("issuedBy.equals=" + DEFAULT_ISSUED_BY, "issuedBy.equals=" + UPDATED_ISSUED_BY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedBy in
        defaultItemIssuanceFiltering("issuedBy.in=" + DEFAULT_ISSUED_BY + "," + UPDATED_ISSUED_BY, "issuedBy.in=" + UPDATED_ISSUED_BY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedBy is not null
        defaultItemIssuanceFiltering("issuedBy.specified=true", "issuedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByContainsSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedBy contains
        defaultItemIssuanceFiltering("issuedBy.contains=" + DEFAULT_ISSUED_BY, "issuedBy.contains=" + UPDATED_ISSUED_BY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where issuedBy does not contain
        defaultItemIssuanceFiltering("issuedBy.doesNotContain=" + UPDATED_ISSUED_BY, "issuedBy.doesNotContain=" + DEFAULT_ISSUED_BY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity equals to
        defaultItemIssuanceFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity in
        defaultItemIssuanceFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity is not null
        defaultItemIssuanceFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity is greater than or equal to
        defaultItemIssuanceFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity is less than or equal to
        defaultItemIssuanceFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity is less than
        defaultItemIssuanceFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where quantity is greater than
        defaultItemIssuanceFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity equals to
        defaultItemIssuanceFiltering(
            "cancelQuantity.equals=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.equals=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity in
        defaultItemIssuanceFiltering(
            "cancelQuantity.in=" + DEFAULT_CANCEL_QUANTITY + "," + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.in=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity is not null
        defaultItemIssuanceFiltering("cancelQuantity.specified=true", "cancelQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity is greater than or equal to
        defaultItemIssuanceFiltering(
            "cancelQuantity.greaterThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.greaterThanOrEqual=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity is less than or equal to
        defaultItemIssuanceFiltering(
            "cancelQuantity.lessThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.lessThanOrEqual=" + SMALLER_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity is less than
        defaultItemIssuanceFiltering(
            "cancelQuantity.lessThan=" + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.lessThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByCancelQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList where cancelQuantity is greater than
        defaultItemIssuanceFiltering(
            "cancelQuantity.greaterThan=" + SMALLER_CANCEL_QUANTITY,
            "cancelQuantity.greaterThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllItemIssuancesByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        itemIssuance.setInventoryItem(inventoryItem);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the itemIssuanceList where inventoryItem equals to inventoryItemId
        defaultItemIssuanceShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the itemIssuanceList where inventoryItem equals to (inventoryItemId + 1)
        defaultItemIssuanceShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllItemIssuancesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        itemIssuance.setOrder(order);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long orderId = order.getId();
        // Get all the itemIssuanceList where order equals to orderId
        defaultItemIssuanceShouldBeFound("orderId.equals=" + orderId);

        // Get all the itemIssuanceList where order equals to (orderId + 1)
        defaultItemIssuanceShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllItemIssuancesByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        itemIssuance.setOrderItem(orderItem);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long orderItemId = orderItem.getId();
        // Get all the itemIssuanceList where orderItem equals to orderItemId
        defaultItemIssuanceShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the itemIssuanceList where orderItem equals to (orderItemId + 1)
        defaultItemIssuanceShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllItemIssuancesByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        itemIssuance.setShipment(shipment);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long shipmentId = shipment.getId();
        // Get all the itemIssuanceList where shipment equals to shipmentId
        defaultItemIssuanceShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the itemIssuanceList where shipment equals to (shipmentId + 1)
        defaultItemIssuanceShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllItemIssuancesByShipmentItemIsEqualToSomething() throws Exception {
        ShipmentItem shipmentItem;
        if (TestUtil.findAll(em, ShipmentItem.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            shipmentItem = ShipmentItemResourceIT.createEntity(em);
        } else {
            shipmentItem = TestUtil.findAll(em, ShipmentItem.class).getFirst();
        }
        em.persist(shipmentItem);
        em.flush();
        itemIssuance.setShipmentItem(shipmentItem);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long shipmentItemId = shipmentItem.getId();
        // Get all the itemIssuanceList where shipmentItem equals to shipmentItemId
        defaultItemIssuanceShouldBeFound("shipmentItemId.equals=" + shipmentItemId);

        // Get all the itemIssuanceList where shipmentItem equals to (shipmentItemId + 1)
        defaultItemIssuanceShouldNotBeFound("shipmentItemId.equals=" + (shipmentItemId + 1));
    }

    @Test
    @Transactional
    void getAllItemIssuancesByIssuedByPartyIsEqualToSomething() throws Exception {
        Party issuedByParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            itemIssuanceRepository.saveAndFlush(itemIssuance);
            issuedByParty = PartyResourceIT.createEntity();
        } else {
            issuedByParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(issuedByParty);
        em.flush();
        itemIssuance.setIssuedByParty(issuedByParty);
        itemIssuanceRepository.saveAndFlush(itemIssuance);
        Long issuedByPartyId = issuedByParty.getId();
        // Get all the itemIssuanceList where issuedByParty equals to issuedByPartyId
        defaultItemIssuanceShouldBeFound("issuedByPartyId.equals=" + issuedByPartyId);

        // Get all the itemIssuanceList where issuedByParty equals to (issuedByPartyId + 1)
        defaultItemIssuanceShouldNotBeFound("issuedByPartyId.equals=" + (issuedByPartyId + 1));
    }

    private void defaultItemIssuanceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultItemIssuanceShouldBeFound(shouldBeFound);
        defaultItemIssuanceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemIssuanceShouldBeFound(String filter) throws Exception {
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemIssuance.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuedBy").value(hasItem(DEFAULT_ISSUED_BY)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].cancelQuantity").value(hasItem(sameNumber(DEFAULT_CANCEL_QUANTITY))));

        // Check, that the count call also returns 1
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemIssuanceShouldNotBeFound(String filter) throws Exception {
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemIssuanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItemIssuance() throws Exception {
        // Get the itemIssuance
        restItemIssuanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemIssuance() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemIssuance
        ItemIssuance updatedItemIssuance = itemIssuanceRepository.findById(itemIssuance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItemIssuance are not directly saved in db
        em.detach(updatedItemIssuance);
        updatedItemIssuance
            .message(UPDATED_MESSAGE)
            .issuedDate(UPDATED_ISSUED_DATE)
            .issuedBy(UPDATED_ISSUED_BY)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY);

        restItemIssuanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemIssuance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedItemIssuance))
            )
            .andExpect(status().isOk());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemIssuanceToMatchAllProperties(updatedItemIssuance);
    }

    @Test
    @Transactional
    void putNonExistingItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemIssuance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemIssuance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemIssuance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemIssuance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemIssuanceWithPatch() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemIssuance using partial update
        ItemIssuance partialUpdatedItemIssuance = new ItemIssuance();
        partialUpdatedItemIssuance.setId(itemIssuance.getId());

        partialUpdatedItemIssuance
            .message(UPDATED_MESSAGE)
            .issuedDate(UPDATED_ISSUED_DATE)
            .issuedBy(UPDATED_ISSUED_BY)
            .quantity(UPDATED_QUANTITY);

        restItemIssuanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemIssuance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemIssuance))
            )
            .andExpect(status().isOk());

        // Validate the ItemIssuance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemIssuanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedItemIssuance, itemIssuance),
            getPersistedItemIssuance(itemIssuance)
        );
    }

    @Test
    @Transactional
    void fullUpdateItemIssuanceWithPatch() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemIssuance using partial update
        ItemIssuance partialUpdatedItemIssuance = new ItemIssuance();
        partialUpdatedItemIssuance.setId(itemIssuance.getId());

        partialUpdatedItemIssuance
            .message(UPDATED_MESSAGE)
            .issuedDate(UPDATED_ISSUED_DATE)
            .issuedBy(UPDATED_ISSUED_BY)
            .quantity(UPDATED_QUANTITY)
            .cancelQuantity(UPDATED_CANCEL_QUANTITY);

        restItemIssuanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemIssuance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemIssuance))
            )
            .andExpect(status().isOk());

        // Validate the ItemIssuance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemIssuanceUpdatableFieldsEquals(partialUpdatedItemIssuance, getPersistedItemIssuance(partialUpdatedItemIssuance));
    }

    @Test
    @Transactional
    void patchNonExistingItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemIssuance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemIssuance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemIssuance))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemIssuance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemIssuance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemIssuance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemIssuance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemIssuance() throws Exception {
        // Initialize the database
        insertedItemIssuance = itemIssuanceRepository.saveAndFlush(itemIssuance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the itemIssuance
        restItemIssuanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemIssuance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itemIssuanceRepository.count();
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

    protected ItemIssuance getPersistedItemIssuance(ItemIssuance itemIssuance) {
        return itemIssuanceRepository.findById(itemIssuance.getId()).orElseThrow();
    }

    protected void assertPersistedItemIssuanceToMatchAllProperties(ItemIssuance expectedItemIssuance) {
        assertItemIssuanceAllPropertiesEquals(expectedItemIssuance, getPersistedItemIssuance(expectedItemIssuance));
    }

    protected void assertPersistedItemIssuanceToMatchUpdatableProperties(ItemIssuance expectedItemIssuance) {
        assertItemIssuanceAllUpdatablePropertiesEquals(expectedItemIssuance, getPersistedItemIssuance(expectedItemIssuance));
    }
}
