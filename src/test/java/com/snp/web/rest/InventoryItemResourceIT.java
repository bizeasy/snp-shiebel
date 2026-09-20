package com.snp.web.rest;

import static com.snp.domain.InventoryItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityLocation;
import com.snp.domain.InventoryItem;
import com.snp.domain.InventoryItemType;
import com.snp.domain.Lot;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.InventoryItemRepository;
import com.snp.service.InventoryItemService;
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
 * Integration tests for the {@link InventoryItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemResourceIT {

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_MANUFACTURED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MANUFACTURED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_RETEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETEST_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_CONTAINER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTAINER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NO = "BBBBBBBBBB";

    private static final String DEFAULT_MFG_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_MFG_BATCH_NO = "BBBBBBBBBB";

    private static final String DEFAULT_LOT_NO_STR = "AAAAAAAAAA";
    private static final String UPDATED_LOT_NO_STR = "BBBBBBBBBB";

    private static final String DEFAULT_BIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BIN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY_ON_HAND_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_ON_HAND_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_ON_HAND_TOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AVAILABLE_TO_PROMISE_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVAILABLE_TO_PROMISE_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_AVAILABLE_TO_PROMISE_TOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCOUNTING_QUANTITY_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNTING_QUANTITY_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNTING_QUANTITY_TOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OLD_QUANTITY_ON_HAND = new BigDecimal(1);
    private static final BigDecimal UPDATED_OLD_QUANTITY_ON_HAND = new BigDecimal(2);
    private static final BigDecimal SMALLER_OLD_QUANTITY_ON_HAND = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OLD_AVAILABLE_TO_PROMISE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OLD_AVAILABLE_TO_PROMISE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OLD_AVAILABLE_TO_PROMISE = new BigDecimal(1 - 1);

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SOFT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_SOFT_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTIVATION_VALID_THRU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVATION_VALID_THRU = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_COST = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/inventory-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Mock
    private InventoryItemRepository inventoryItemRepositoryMock;

    @Mock
    private InventoryItemService inventoryItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemMockMvc;

    private InventoryItem inventoryItem;

    private InventoryItem insertedInventoryItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createEntity(EntityManager em) {
        InventoryItem inventoryItem = new InventoryItem()
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .manufacturedDate(DEFAULT_MANUFACTURED_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .retestDate(DEFAULT_RETEST_DATE)
            .containerId(DEFAULT_CONTAINER_ID)
            .batchNo(DEFAULT_BATCH_NO)
            .mfgBatchNo(DEFAULT_MFG_BATCH_NO)
            .lotNoStr(DEFAULT_LOT_NO_STR)
            .binNumber(DEFAULT_BIN_NUMBER)
            .comments(DEFAULT_COMMENTS)
            .quantityOnHandTotal(DEFAULT_QUANTITY_ON_HAND_TOTAL)
            .availableToPromiseTotal(DEFAULT_AVAILABLE_TO_PROMISE_TOTAL)
            .accountingQuantityTotal(DEFAULT_ACCOUNTING_QUANTITY_TOTAL)
            .oldQuantityOnHand(DEFAULT_OLD_QUANTITY_ON_HAND)
            .oldAvailableToPromise(DEFAULT_OLD_AVAILABLE_TO_PROMISE)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .softIdentifier(DEFAULT_SOFT_IDENTIFIER)
            .activationNumber(DEFAULT_ACTIVATION_NUMBER)
            .activationValidThru(DEFAULT_ACTIVATION_VALID_THRU)
            .unitCost(DEFAULT_UNIT_COST);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        inventoryItem.setProduct(product);
        return inventoryItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createUpdatedEntity(EntityManager em) {
        InventoryItem updatedInventoryItem = new InventoryItem()
            .receivedDate(UPDATED_RECEIVED_DATE)
            .manufacturedDate(UPDATED_MANUFACTURED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .retestDate(UPDATED_RETEST_DATE)
            .containerId(UPDATED_CONTAINER_ID)
            .batchNo(UPDATED_BATCH_NO)
            .mfgBatchNo(UPDATED_MFG_BATCH_NO)
            .lotNoStr(UPDATED_LOT_NO_STR)
            .binNumber(UPDATED_BIN_NUMBER)
            .comments(UPDATED_COMMENTS)
            .quantityOnHandTotal(UPDATED_QUANTITY_ON_HAND_TOTAL)
            .availableToPromiseTotal(UPDATED_AVAILABLE_TO_PROMISE_TOTAL)
            .accountingQuantityTotal(UPDATED_ACCOUNTING_QUANTITY_TOTAL)
            .oldQuantityOnHand(UPDATED_OLD_QUANTITY_ON_HAND)
            .oldAvailableToPromise(UPDATED_OLD_AVAILABLE_TO_PROMISE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .softIdentifier(UPDATED_SOFT_IDENTIFIER)
            .activationNumber(UPDATED_ACTIVATION_NUMBER)
            .activationValidThru(UPDATED_ACTIVATION_VALID_THRU)
            .unitCost(UPDATED_UNIT_COST);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedInventoryItem.setProduct(product);
        return updatedInventoryItem;
    }

    @BeforeEach
    void initTest() {
        inventoryItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItem != null) {
            inventoryItemRepository.delete(insertedInventoryItem);
            insertedInventoryItem = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryItem
        var returnedInventoryItem = om.readValue(
            restInventoryItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItem.class
        );

        // Validate the InventoryItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventoryItemUpdatableFieldsEquals(returnedInventoryItem, getPersistedInventoryItem(returnedInventoryItem));

        insertedInventoryItem = returnedInventoryItem;
    }

    @Test
    @Transactional
    void createInventoryItemWithExistingId() throws Exception {
        // Create the InventoryItem with an existing ID
        inventoryItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItem)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventoryItems() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].manufacturedDate").value(hasItem(DEFAULT_MANUFACTURED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].retestDate").value(hasItem(DEFAULT_RETEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].containerId").value(hasItem(DEFAULT_CONTAINER_ID)))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO)))
            .andExpect(jsonPath("$.[*].mfgBatchNo").value(hasItem(DEFAULT_MFG_BATCH_NO)))
            .andExpect(jsonPath("$.[*].lotNoStr").value(hasItem(DEFAULT_LOT_NO_STR)))
            .andExpect(jsonPath("$.[*].binNumber").value(hasItem(DEFAULT_BIN_NUMBER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].quantityOnHandTotal").value(hasItem(sameNumber(DEFAULT_QUANTITY_ON_HAND_TOTAL))))
            .andExpect(jsonPath("$.[*].availableToPromiseTotal").value(hasItem(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_TOTAL))))
            .andExpect(jsonPath("$.[*].accountingQuantityTotal").value(hasItem(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_TOTAL))))
            .andExpect(jsonPath("$.[*].oldQuantityOnHand").value(hasItem(sameNumber(DEFAULT_OLD_QUANTITY_ON_HAND))))
            .andExpect(jsonPath("$.[*].oldAvailableToPromise").value(hasItem(sameNumber(DEFAULT_OLD_AVAILABLE_TO_PROMISE))))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].softIdentifier").value(hasItem(DEFAULT_SOFT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].activationNumber").value(hasItem(DEFAULT_ACTIVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].activationValidThru").value(hasItem(DEFAULT_ACTIVATION_VALID_THRU.toString())))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoryItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventoryItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventoryItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoryItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventoryItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventoryItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get the inventoryItem
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItem.getId().intValue()))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.manufacturedDate").value(DEFAULT_MANUFACTURED_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.retestDate").value(DEFAULT_RETEST_DATE.toString()))
            .andExpect(jsonPath("$.containerId").value(DEFAULT_CONTAINER_ID))
            .andExpect(jsonPath("$.batchNo").value(DEFAULT_BATCH_NO))
            .andExpect(jsonPath("$.mfgBatchNo").value(DEFAULT_MFG_BATCH_NO))
            .andExpect(jsonPath("$.lotNoStr").value(DEFAULT_LOT_NO_STR))
            .andExpect(jsonPath("$.binNumber").value(DEFAULT_BIN_NUMBER))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.quantityOnHandTotal").value(sameNumber(DEFAULT_QUANTITY_ON_HAND_TOTAL)))
            .andExpect(jsonPath("$.availableToPromiseTotal").value(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_TOTAL)))
            .andExpect(jsonPath("$.accountingQuantityTotal").value(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_TOTAL)))
            .andExpect(jsonPath("$.oldQuantityOnHand").value(sameNumber(DEFAULT_OLD_QUANTITY_ON_HAND)))
            .andExpect(jsonPath("$.oldAvailableToPromise").value(sameNumber(DEFAULT_OLD_AVAILABLE_TO_PROMISE)))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.softIdentifier").value(DEFAULT_SOFT_IDENTIFIER))
            .andExpect(jsonPath("$.activationNumber").value(DEFAULT_ACTIVATION_NUMBER))
            .andExpect(jsonPath("$.activationValidThru").value(DEFAULT_ACTIVATION_VALID_THRU.toString()))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)));
    }

    @Test
    @Transactional
    void getInventoryItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        Long id = inventoryItem.getId();

        defaultInventoryItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where receivedDate equals to
        defaultInventoryItemFiltering("receivedDate.equals=" + DEFAULT_RECEIVED_DATE, "receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where receivedDate in
        defaultInventoryItemFiltering(
            "receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE,
            "receivedDate.in=" + UPDATED_RECEIVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where receivedDate is not null
        defaultInventoryItemFiltering("receivedDate.specified=true", "receivedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByManufacturedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where manufacturedDate equals to
        defaultInventoryItemFiltering(
            "manufacturedDate.equals=" + DEFAULT_MANUFACTURED_DATE,
            "manufacturedDate.equals=" + UPDATED_MANUFACTURED_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByManufacturedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where manufacturedDate in
        defaultInventoryItemFiltering(
            "manufacturedDate.in=" + DEFAULT_MANUFACTURED_DATE + "," + UPDATED_MANUFACTURED_DATE,
            "manufacturedDate.in=" + UPDATED_MANUFACTURED_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByManufacturedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where manufacturedDate is not null
        defaultInventoryItemFiltering("manufacturedDate.specified=true", "manufacturedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where expiryDate equals to
        defaultInventoryItemFiltering("expiryDate.equals=" + DEFAULT_EXPIRY_DATE, "expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where expiryDate in
        defaultInventoryItemFiltering(
            "expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE,
            "expiryDate.in=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where expiryDate is not null
        defaultInventoryItemFiltering("expiryDate.specified=true", "expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRetestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where retestDate equals to
        defaultInventoryItemFiltering("retestDate.equals=" + DEFAULT_RETEST_DATE, "retestDate.equals=" + UPDATED_RETEST_DATE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRetestDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where retestDate in
        defaultInventoryItemFiltering(
            "retestDate.in=" + DEFAULT_RETEST_DATE + "," + UPDATED_RETEST_DATE,
            "retestDate.in=" + UPDATED_RETEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRetestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where retestDate is not null
        defaultInventoryItemFiltering("retestDate.specified=true", "retestDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByContainerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where containerId equals to
        defaultInventoryItemFiltering("containerId.equals=" + DEFAULT_CONTAINER_ID, "containerId.equals=" + UPDATED_CONTAINER_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByContainerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where containerId in
        defaultInventoryItemFiltering(
            "containerId.in=" + DEFAULT_CONTAINER_ID + "," + UPDATED_CONTAINER_ID,
            "containerId.in=" + UPDATED_CONTAINER_ID
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByContainerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where containerId is not null
        defaultInventoryItemFiltering("containerId.specified=true", "containerId.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByContainerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where containerId contains
        defaultInventoryItemFiltering("containerId.contains=" + DEFAULT_CONTAINER_ID, "containerId.contains=" + UPDATED_CONTAINER_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByContainerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where containerId does not contain
        defaultInventoryItemFiltering(
            "containerId.doesNotContain=" + UPDATED_CONTAINER_ID,
            "containerId.doesNotContain=" + DEFAULT_CONTAINER_ID
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBatchNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where batchNo equals to
        defaultInventoryItemFiltering("batchNo.equals=" + DEFAULT_BATCH_NO, "batchNo.equals=" + UPDATED_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBatchNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where batchNo in
        defaultInventoryItemFiltering("batchNo.in=" + DEFAULT_BATCH_NO + "," + UPDATED_BATCH_NO, "batchNo.in=" + UPDATED_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBatchNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where batchNo is not null
        defaultInventoryItemFiltering("batchNo.specified=true", "batchNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBatchNoContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where batchNo contains
        defaultInventoryItemFiltering("batchNo.contains=" + DEFAULT_BATCH_NO, "batchNo.contains=" + UPDATED_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBatchNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where batchNo does not contain
        defaultInventoryItemFiltering("batchNo.doesNotContain=" + UPDATED_BATCH_NO, "batchNo.doesNotContain=" + DEFAULT_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByMfgBatchNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where mfgBatchNo equals to
        defaultInventoryItemFiltering("mfgBatchNo.equals=" + DEFAULT_MFG_BATCH_NO, "mfgBatchNo.equals=" + UPDATED_MFG_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByMfgBatchNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where mfgBatchNo in
        defaultInventoryItemFiltering(
            "mfgBatchNo.in=" + DEFAULT_MFG_BATCH_NO + "," + UPDATED_MFG_BATCH_NO,
            "mfgBatchNo.in=" + UPDATED_MFG_BATCH_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByMfgBatchNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where mfgBatchNo is not null
        defaultInventoryItemFiltering("mfgBatchNo.specified=true", "mfgBatchNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByMfgBatchNoContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where mfgBatchNo contains
        defaultInventoryItemFiltering("mfgBatchNo.contains=" + DEFAULT_MFG_BATCH_NO, "mfgBatchNo.contains=" + UPDATED_MFG_BATCH_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByMfgBatchNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where mfgBatchNo does not contain
        defaultInventoryItemFiltering(
            "mfgBatchNo.doesNotContain=" + UPDATED_MFG_BATCH_NO,
            "mfgBatchNo.doesNotContain=" + DEFAULT_MFG_BATCH_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotNoStrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lotNoStr equals to
        defaultInventoryItemFiltering("lotNoStr.equals=" + DEFAULT_LOT_NO_STR, "lotNoStr.equals=" + UPDATED_LOT_NO_STR);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotNoStrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lotNoStr in
        defaultInventoryItemFiltering("lotNoStr.in=" + DEFAULT_LOT_NO_STR + "," + UPDATED_LOT_NO_STR, "lotNoStr.in=" + UPDATED_LOT_NO_STR);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotNoStrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lotNoStr is not null
        defaultInventoryItemFiltering("lotNoStr.specified=true", "lotNoStr.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotNoStrContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lotNoStr contains
        defaultInventoryItemFiltering("lotNoStr.contains=" + DEFAULT_LOT_NO_STR, "lotNoStr.contains=" + UPDATED_LOT_NO_STR);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotNoStrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lotNoStr does not contain
        defaultInventoryItemFiltering("lotNoStr.doesNotContain=" + UPDATED_LOT_NO_STR, "lotNoStr.doesNotContain=" + DEFAULT_LOT_NO_STR);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBinNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where binNumber equals to
        defaultInventoryItemFiltering("binNumber.equals=" + DEFAULT_BIN_NUMBER, "binNumber.equals=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBinNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where binNumber in
        defaultInventoryItemFiltering(
            "binNumber.in=" + DEFAULT_BIN_NUMBER + "," + UPDATED_BIN_NUMBER,
            "binNumber.in=" + UPDATED_BIN_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBinNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where binNumber is not null
        defaultInventoryItemFiltering("binNumber.specified=true", "binNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBinNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where binNumber contains
        defaultInventoryItemFiltering("binNumber.contains=" + DEFAULT_BIN_NUMBER, "binNumber.contains=" + UPDATED_BIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBinNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where binNumber does not contain
        defaultInventoryItemFiltering("binNumber.doesNotContain=" + UPDATED_BIN_NUMBER, "binNumber.doesNotContain=" + DEFAULT_BIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where comments equals to
        defaultInventoryItemFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where comments in
        defaultInventoryItemFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where comments is not null
        defaultInventoryItemFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where comments contains
        defaultInventoryItemFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where comments does not contain
        defaultInventoryItemFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal equals to
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.equals=" + DEFAULT_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.equals=" + UPDATED_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal in
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.in=" + DEFAULT_QUANTITY_ON_HAND_TOTAL + "," + UPDATED_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.in=" + UPDATED_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal is not null
        defaultInventoryItemFiltering("quantityOnHandTotal.specified=true", "quantityOnHandTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal is greater than or equal to
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.greaterThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.greaterThanOrEqual=" + UPDATED_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal is less than or equal to
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.lessThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.lessThanOrEqual=" + SMALLER_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal is less than
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.lessThan=" + UPDATED_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.lessThan=" + DEFAULT_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityOnHandTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantityOnHandTotal is greater than
        defaultInventoryItemFiltering(
            "quantityOnHandTotal.greaterThan=" + SMALLER_QUANTITY_ON_HAND_TOTAL,
            "quantityOnHandTotal.greaterThan=" + DEFAULT_QUANTITY_ON_HAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal equals to
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.equals=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.equals=" + UPDATED_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal in
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.in=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL + "," + UPDATED_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.in=" + UPDATED_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal is not null
        defaultInventoryItemFiltering("availableToPromiseTotal.specified=true", "availableToPromiseTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal is greater than or equal to
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.greaterThanOrEqual=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.greaterThanOrEqual=" + UPDATED_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal is less than or equal to
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.lessThanOrEqual=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.lessThanOrEqual=" + SMALLER_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal is less than
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.lessThan=" + UPDATED_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.lessThan=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAvailableToPromiseTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where availableToPromiseTotal is greater than
        defaultInventoryItemFiltering(
            "availableToPromiseTotal.greaterThan=" + SMALLER_AVAILABLE_TO_PROMISE_TOTAL,
            "availableToPromiseTotal.greaterThan=" + DEFAULT_AVAILABLE_TO_PROMISE_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal equals to
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.equals=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.equals=" + UPDATED_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal in
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.in=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL + "," + UPDATED_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.in=" + UPDATED_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal is not null
        defaultInventoryItemFiltering("accountingQuantityTotal.specified=true", "accountingQuantityTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal is greater than or equal to
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.greaterThanOrEqual=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.greaterThanOrEqual=" + UPDATED_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal is less than or equal to
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.lessThanOrEqual=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.lessThanOrEqual=" + SMALLER_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal is less than
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.lessThan=" + UPDATED_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.lessThan=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByAccountingQuantityTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where accountingQuantityTotal is greater than
        defaultInventoryItemFiltering(
            "accountingQuantityTotal.greaterThan=" + SMALLER_ACCOUNTING_QUANTITY_TOTAL,
            "accountingQuantityTotal.greaterThan=" + DEFAULT_ACCOUNTING_QUANTITY_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand equals to
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.equals=" + DEFAULT_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.equals=" + UPDATED_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand in
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.in=" + DEFAULT_OLD_QUANTITY_ON_HAND + "," + UPDATED_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.in=" + UPDATED_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand is not null
        defaultInventoryItemFiltering("oldQuantityOnHand.specified=true", "oldQuantityOnHand.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand is greater than or equal to
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.greaterThanOrEqual=" + DEFAULT_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.greaterThanOrEqual=" + UPDATED_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand is less than or equal to
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.lessThanOrEqual=" + DEFAULT_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.lessThanOrEqual=" + SMALLER_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand is less than
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.lessThan=" + UPDATED_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.lessThan=" + DEFAULT_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldQuantityOnHandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldQuantityOnHand is greater than
        defaultInventoryItemFiltering(
            "oldQuantityOnHand.greaterThan=" + SMALLER_OLD_QUANTITY_ON_HAND,
            "oldQuantityOnHand.greaterThan=" + DEFAULT_OLD_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise equals to
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.equals=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.equals=" + UPDATED_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise in
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.in=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE + "," + UPDATED_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.in=" + UPDATED_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise is not null
        defaultInventoryItemFiltering("oldAvailableToPromise.specified=true", "oldAvailableToPromise.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise is greater than or equal to
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.greaterThanOrEqual=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.greaterThanOrEqual=" + UPDATED_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise is less than or equal to
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.lessThanOrEqual=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.lessThanOrEqual=" + SMALLER_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise is less than
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.lessThan=" + UPDATED_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.lessThan=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOldAvailableToPromiseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where oldAvailableToPromise is greater than
        defaultInventoryItemFiltering(
            "oldAvailableToPromise.greaterThan=" + SMALLER_OLD_AVAILABLE_TO_PROMISE,
            "oldAvailableToPromise.greaterThan=" + DEFAULT_OLD_AVAILABLE_TO_PROMISE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where serialNumber equals to
        defaultInventoryItemFiltering("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER, "serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where serialNumber in
        defaultInventoryItemFiltering(
            "serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER,
            "serialNumber.in=" + UPDATED_SERIAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where serialNumber is not null
        defaultInventoryItemFiltering("serialNumber.specified=true", "serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where serialNumber contains
        defaultInventoryItemFiltering("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER, "serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where serialNumber does not contain
        defaultInventoryItemFiltering(
            "serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER,
            "serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySoftIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where softIdentifier equals to
        defaultInventoryItemFiltering(
            "softIdentifier.equals=" + DEFAULT_SOFT_IDENTIFIER,
            "softIdentifier.equals=" + UPDATED_SOFT_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySoftIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where softIdentifier in
        defaultInventoryItemFiltering(
            "softIdentifier.in=" + DEFAULT_SOFT_IDENTIFIER + "," + UPDATED_SOFT_IDENTIFIER,
            "softIdentifier.in=" + UPDATED_SOFT_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySoftIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where softIdentifier is not null
        defaultInventoryItemFiltering("softIdentifier.specified=true", "softIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySoftIdentifierContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where softIdentifier contains
        defaultInventoryItemFiltering(
            "softIdentifier.contains=" + DEFAULT_SOFT_IDENTIFIER,
            "softIdentifier.contains=" + UPDATED_SOFT_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySoftIdentifierNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where softIdentifier does not contain
        defaultInventoryItemFiltering(
            "softIdentifier.doesNotContain=" + UPDATED_SOFT_IDENTIFIER,
            "softIdentifier.doesNotContain=" + DEFAULT_SOFT_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationNumber equals to
        defaultInventoryItemFiltering(
            "activationNumber.equals=" + DEFAULT_ACTIVATION_NUMBER,
            "activationNumber.equals=" + UPDATED_ACTIVATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationNumber in
        defaultInventoryItemFiltering(
            "activationNumber.in=" + DEFAULT_ACTIVATION_NUMBER + "," + UPDATED_ACTIVATION_NUMBER,
            "activationNumber.in=" + UPDATED_ACTIVATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationNumber is not null
        defaultInventoryItemFiltering("activationNumber.specified=true", "activationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationNumber contains
        defaultInventoryItemFiltering(
            "activationNumber.contains=" + DEFAULT_ACTIVATION_NUMBER,
            "activationNumber.contains=" + UPDATED_ACTIVATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationNumber does not contain
        defaultInventoryItemFiltering(
            "activationNumber.doesNotContain=" + UPDATED_ACTIVATION_NUMBER,
            "activationNumber.doesNotContain=" + DEFAULT_ACTIVATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationValidThruIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationValidThru equals to
        defaultInventoryItemFiltering(
            "activationValidThru.equals=" + DEFAULT_ACTIVATION_VALID_THRU,
            "activationValidThru.equals=" + UPDATED_ACTIVATION_VALID_THRU
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationValidThruIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationValidThru in
        defaultInventoryItemFiltering(
            "activationValidThru.in=" + DEFAULT_ACTIVATION_VALID_THRU + "," + UPDATED_ACTIVATION_VALID_THRU,
            "activationValidThru.in=" + UPDATED_ACTIVATION_VALID_THRU
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByActivationValidThruIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where activationValidThru is not null
        defaultInventoryItemFiltering("activationValidThru.specified=true", "activationValidThru.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost equals to
        defaultInventoryItemFiltering("unitCost.equals=" + DEFAULT_UNIT_COST, "unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost in
        defaultInventoryItemFiltering("unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST, "unitCost.in=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost is not null
        defaultInventoryItemFiltering("unitCost.specified=true", "unitCost.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost is greater than or equal to
        defaultInventoryItemFiltering(
            "unitCost.greaterThanOrEqual=" + DEFAULT_UNIT_COST,
            "unitCost.greaterThanOrEqual=" + UPDATED_UNIT_COST
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost is less than or equal to
        defaultInventoryItemFiltering("unitCost.lessThanOrEqual=" + DEFAULT_UNIT_COST, "unitCost.lessThanOrEqual=" + SMALLER_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost is less than
        defaultInventoryItemFiltering("unitCost.lessThan=" + UPDATED_UNIT_COST, "unitCost.lessThan=" + DEFAULT_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitCost is greater than
        defaultInventoryItemFiltering("unitCost.greaterThan=" + SMALLER_UNIT_COST, "unitCost.greaterThan=" + DEFAULT_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemTypeIsEqualToSomething() throws Exception {
        InventoryItemType inventoryItemType;
        if (TestUtil.findAll(em, InventoryItemType.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            inventoryItemType = InventoryItemTypeResourceIT.createEntity();
        } else {
            inventoryItemType = TestUtil.findAll(em, InventoryItemType.class).getFirst();
        }
        em.persist(inventoryItemType);
        em.flush();
        inventoryItem.setInventoryItemType(inventoryItemType);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long inventoryItemTypeId = inventoryItemType.getId();
        // Get all the inventoryItemList where inventoryItemType equals to inventoryItemTypeId
        defaultInventoryItemShouldBeFound("inventoryItemTypeId.equals=" + inventoryItemTypeId);

        // Get all the inventoryItemList where inventoryItemType equals to (inventoryItemTypeId + 1)
        defaultInventoryItemShouldNotBeFound("inventoryItemTypeId.equals=" + (inventoryItemTypeId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        inventoryItem.setProduct(product);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long productId = product.getId();
        // Get all the inventoryItemList where product equals to productId
        defaultInventoryItemShouldBeFound("productId.equals=" + productId);

        // Get all the inventoryItemList where product equals to (productId + 1)
        defaultInventoryItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsBySupplierIsEqualToSomething() throws Exception {
        Party supplier;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            supplier = PartyResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(supplier);
        em.flush();
        inventoryItem.setSupplier(supplier);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long supplierId = supplier.getId();
        // Get all the inventoryItemList where supplier equals to supplierId
        defaultInventoryItemShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the inventoryItemList where supplier equals to (supplierId + 1)
        defaultInventoryItemShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByOwnerPartyIsEqualToSomething() throws Exception {
        Party ownerParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            ownerParty = PartyResourceIT.createEntity();
        } else {
            ownerParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(ownerParty);
        em.flush();
        inventoryItem.setOwnerParty(ownerParty);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long ownerPartyId = ownerParty.getId();
        // Get all the inventoryItemList where ownerParty equals to ownerPartyId
        defaultInventoryItemShouldBeFound("ownerPartyId.equals=" + ownerPartyId);

        // Get all the inventoryItemList where ownerParty equals to (ownerPartyId + 1)
        defaultInventoryItemShouldNotBeFound("ownerPartyId.equals=" + (ownerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        inventoryItem.setStatus(status);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long statusId = status.getId();
        // Get all the inventoryItemList where status equals to statusId
        defaultInventoryItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the inventoryItemList where status equals to (statusId + 1)
        defaultInventoryItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        inventoryItem.setFacility(facility);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long facilityId = facility.getId();
        // Get all the inventoryItemList where facility equals to facilityId
        defaultInventoryItemShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the inventoryItemList where facility equals to (facilityId + 1)
        defaultInventoryItemShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByFacilityLocationIsEqualToSomething() throws Exception {
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            facilityLocation = FacilityLocationResourceIT.createEntity(em);
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        em.persist(facilityLocation);
        em.flush();
        inventoryItem.setFacilityLocation(facilityLocation);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long facilityLocationId = facilityLocation.getId();
        // Get all the inventoryItemList where facilityLocation equals to facilityLocationId
        defaultInventoryItemShouldBeFound("facilityLocationId.equals=" + facilityLocationId);

        // Get all the inventoryItemList where facilityLocation equals to (facilityLocationId + 1)
        defaultInventoryItemShouldNotBeFound("facilityLocationId.equals=" + (facilityLocationId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        inventoryItem.setUom(uom);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long uomId = uom.getId();
        // Get all the inventoryItemList where uom equals to uomId
        defaultInventoryItemShouldBeFound("uomId.equals=" + uomId);

        // Get all the inventoryItemList where uom equals to (uomId + 1)
        defaultInventoryItemShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        inventoryItem.setCurrencyUom(currencyUom);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long currencyUomId = currencyUom.getId();
        // Get all the inventoryItemList where currencyUom equals to currencyUomId
        defaultInventoryItemShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the inventoryItemList where currencyUom equals to (currencyUomId + 1)
        defaultInventoryItemShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLotIsEqualToSomething() throws Exception {
        Lot lot;
        if (TestUtil.findAll(em, Lot.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            lot = LotResourceIT.createEntity();
        } else {
            lot = TestUtil.findAll(em, Lot.class).getFirst();
        }
        em.persist(lot);
        em.flush();
        inventoryItem.setLot(lot);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long lotId = lot.getId();
        // Get all the inventoryItemList where lot equals to lotId
        defaultInventoryItemShouldBeFound("lotId.equals=" + lotId);

        // Get all the inventoryItemList where lot equals to (lotId + 1)
        defaultInventoryItemShouldNotBeFound("lotId.equals=" + (lotId + 1));
    }

    private void defaultInventoryItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemShouldBeFound(shouldBeFound);
        defaultInventoryItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemShouldBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].manufacturedDate").value(hasItem(DEFAULT_MANUFACTURED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].retestDate").value(hasItem(DEFAULT_RETEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].containerId").value(hasItem(DEFAULT_CONTAINER_ID)))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO)))
            .andExpect(jsonPath("$.[*].mfgBatchNo").value(hasItem(DEFAULT_MFG_BATCH_NO)))
            .andExpect(jsonPath("$.[*].lotNoStr").value(hasItem(DEFAULT_LOT_NO_STR)))
            .andExpect(jsonPath("$.[*].binNumber").value(hasItem(DEFAULT_BIN_NUMBER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].quantityOnHandTotal").value(hasItem(sameNumber(DEFAULT_QUANTITY_ON_HAND_TOTAL))))
            .andExpect(jsonPath("$.[*].availableToPromiseTotal").value(hasItem(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_TOTAL))))
            .andExpect(jsonPath("$.[*].accountingQuantityTotal").value(hasItem(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_TOTAL))))
            .andExpect(jsonPath("$.[*].oldQuantityOnHand").value(hasItem(sameNumber(DEFAULT_OLD_QUANTITY_ON_HAND))))
            .andExpect(jsonPath("$.[*].oldAvailableToPromise").value(hasItem(sameNumber(DEFAULT_OLD_AVAILABLE_TO_PROMISE))))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].softIdentifier").value(hasItem(DEFAULT_SOFT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].activationNumber").value(hasItem(DEFAULT_ACTIVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].activationValidThru").value(hasItem(DEFAULT_ACTIVATION_VALID_THRU.toString())))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))));

        // Check, that the count call also returns 1
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemShouldNotBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItem() throws Exception {
        // Get the inventoryItem
        restInventoryItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem
        InventoryItem updatedInventoryItem = inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItem are not directly saved in db
        em.detach(updatedInventoryItem);
        updatedInventoryItem
            .receivedDate(UPDATED_RECEIVED_DATE)
            .manufacturedDate(UPDATED_MANUFACTURED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .retestDate(UPDATED_RETEST_DATE)
            .containerId(UPDATED_CONTAINER_ID)
            .batchNo(UPDATED_BATCH_NO)
            .mfgBatchNo(UPDATED_MFG_BATCH_NO)
            .lotNoStr(UPDATED_LOT_NO_STR)
            .binNumber(UPDATED_BIN_NUMBER)
            .comments(UPDATED_COMMENTS)
            .quantityOnHandTotal(UPDATED_QUANTITY_ON_HAND_TOTAL)
            .availableToPromiseTotal(UPDATED_AVAILABLE_TO_PROMISE_TOTAL)
            .accountingQuantityTotal(UPDATED_ACCOUNTING_QUANTITY_TOTAL)
            .oldQuantityOnHand(UPDATED_OLD_QUANTITY_ON_HAND)
            .oldAvailableToPromise(UPDATED_OLD_AVAILABLE_TO_PROMISE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .softIdentifier(UPDATED_SOFT_IDENTIFIER)
            .activationNumber(UPDATED_ACTIVATION_NUMBER)
            .activationValidThru(UPDATED_ACTIVATION_VALID_THRU)
            .unitCost(UPDATED_UNIT_COST);

        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventoryItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemToMatchAllProperties(updatedInventoryItem);
    }

    @Test
    @Transactional
    void putNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem
            .receivedDate(UPDATED_RECEIVED_DATE)
            .manufacturedDate(UPDATED_MANUFACTURED_DATE)
            .retestDate(UPDATED_RETEST_DATE)
            .lotNoStr(UPDATED_LOT_NO_STR)
            .comments(UPDATED_COMMENTS)
            .quantityOnHandTotal(UPDATED_QUANTITY_ON_HAND_TOTAL)
            .oldQuantityOnHand(UPDATED_OLD_QUANTITY_ON_HAND)
            .oldAvailableToPromise(UPDATED_OLD_AVAILABLE_TO_PROMISE)
            .softIdentifier(UPDATED_SOFT_IDENTIFIER);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItem, inventoryItem),
            getPersistedInventoryItem(inventoryItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem
            .receivedDate(UPDATED_RECEIVED_DATE)
            .manufacturedDate(UPDATED_MANUFACTURED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .retestDate(UPDATED_RETEST_DATE)
            .containerId(UPDATED_CONTAINER_ID)
            .batchNo(UPDATED_BATCH_NO)
            .mfgBatchNo(UPDATED_MFG_BATCH_NO)
            .lotNoStr(UPDATED_LOT_NO_STR)
            .binNumber(UPDATED_BIN_NUMBER)
            .comments(UPDATED_COMMENTS)
            .quantityOnHandTotal(UPDATED_QUANTITY_ON_HAND_TOTAL)
            .availableToPromiseTotal(UPDATED_AVAILABLE_TO_PROMISE_TOTAL)
            .accountingQuantityTotal(UPDATED_ACCOUNTING_QUANTITY_TOTAL)
            .oldQuantityOnHand(UPDATED_OLD_QUANTITY_ON_HAND)
            .oldAvailableToPromise(UPDATED_OLD_AVAILABLE_TO_PROMISE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .softIdentifier(UPDATED_SOFT_IDENTIFIER)
            .activationNumber(UPDATED_ACTIVATION_NUMBER)
            .activationValidThru(UPDATED_ACTIVATION_VALID_THRU)
            .unitCost(UPDATED_UNIT_COST);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(partialUpdatedInventoryItem, getPersistedInventoryItem(partialUpdatedInventoryItem));
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryItem
        restInventoryItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryItemRepository.count();
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

    protected InventoryItem getPersistedInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemToMatchAllProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllPropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }

    protected void assertPersistedInventoryItemToMatchUpdatableProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllUpdatablePropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }
}
