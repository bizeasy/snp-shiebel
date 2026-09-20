package com.snp.web.rest;

import static com.snp.domain.InventoryItemDelegateAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.InventoryItemDelegate;
import com.snp.domain.Invoice;
import com.snp.domain.InvoiceItem;
import com.snp.domain.ItemIssuance;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentItem;
import com.snp.domain.ShipmentReceipt;
import com.snp.repository.InventoryItemDelegateRepository;
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
 * Integration tests for the {@link InventoryItemDelegateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemDelegateResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final Instant DEFAULT_EFFECTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_QUANTITY_ON_HAND_DIFF = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_ON_HAND_DIFF = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_ON_HAND_DIFF = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AVAILABLE_TO_PROMISE_DIFF = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVAILABLE_TO_PROMISE_DIFF = new BigDecimal(2);
    private static final BigDecimal SMALLER_AVAILABLE_TO_PROMISE_DIFF = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCOUNTING_QUANTITY_DIFF = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNTING_QUANTITY_DIFF = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNTING_QUANTITY_DIFF = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_COST = new BigDecimal(1 - 1);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventory-item-delegates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemDelegateRepository inventoryItemDelegateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemDelegateMockMvc;

    private InventoryItemDelegate inventoryItemDelegate;

    private InventoryItemDelegate insertedInventoryItemDelegate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemDelegate createEntity(EntityManager em) {
        InventoryItemDelegate inventoryItemDelegate = new InventoryItemDelegate()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .quantityOnHandDiff(DEFAULT_QUANTITY_ON_HAND_DIFF)
            .availableToPromiseDiff(DEFAULT_AVAILABLE_TO_PROMISE_DIFF)
            .accountingQuantityDiff(DEFAULT_ACCOUNTING_QUANTITY_DIFF)
            .unitCost(DEFAULT_UNIT_COST)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        inventoryItemDelegate.setInventoryItem(inventoryItem);
        return inventoryItemDelegate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemDelegate createUpdatedEntity(EntityManager em) {
        InventoryItemDelegate updatedInventoryItemDelegate = new InventoryItemDelegate()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .quantityOnHandDiff(UPDATED_QUANTITY_ON_HAND_DIFF)
            .availableToPromiseDiff(UPDATED_AVAILABLE_TO_PROMISE_DIFF)
            .accountingQuantityDiff(UPDATED_ACCOUNTING_QUANTITY_DIFF)
            .unitCost(UPDATED_UNIT_COST)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createUpdatedEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        updatedInventoryItemDelegate.setInventoryItem(inventoryItem);
        return updatedInventoryItemDelegate;
    }

    @BeforeEach
    void initTest() {
        inventoryItemDelegate = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItemDelegate != null) {
            inventoryItemDelegateRepository.delete(insertedInventoryItemDelegate);
            insertedInventoryItemDelegate = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryItemDelegate
        var returnedInventoryItemDelegate = om.readValue(
            restInventoryItemDelegateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDelegate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItemDelegate.class
        );

        // Validate the InventoryItemDelegate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventoryItemDelegateUpdatableFieldsEquals(
            returnedInventoryItemDelegate,
            getPersistedInventoryItemDelegate(returnedInventoryItemDelegate)
        );

        insertedInventoryItemDelegate = returnedInventoryItemDelegate;
    }

    @Test
    @Transactional
    void createInventoryItemDelegateWithExistingId() throws Exception {
        // Create the InventoryItemDelegate with an existing ID
        inventoryItemDelegate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemDelegateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDelegate)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEffectiveDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventoryItemDelegate.setEffectiveDate(null);

        // Create the InventoryItemDelegate, which fails.

        restInventoryItemDelegateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDelegate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegates() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemDelegate.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantityOnHandDiff").value(hasItem(sameNumber(DEFAULT_QUANTITY_ON_HAND_DIFF))))
            .andExpect(jsonPath("$.[*].availableToPromiseDiff").value(hasItem(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_DIFF))))
            .andExpect(jsonPath("$.[*].accountingQuantityDiff").value(hasItem(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_DIFF))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getInventoryItemDelegate() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get the inventoryItemDelegate
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItemDelegate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItemDelegate.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.quantityOnHandDiff").value(sameNumber(DEFAULT_QUANTITY_ON_HAND_DIFF)))
            .andExpect(jsonPath("$.availableToPromiseDiff").value(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_DIFF)))
            .andExpect(jsonPath("$.accountingQuantityDiff").value(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_DIFF)))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getInventoryItemDelegatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        Long id = inventoryItemDelegate.getId();

        defaultInventoryItemDelegateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemDelegateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemDelegateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo equals to
        defaultInventoryItemDelegateFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo in
        defaultInventoryItemDelegateFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo is not null
        defaultInventoryItemDelegateFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo is greater than or equal to
        defaultInventoryItemDelegateFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo is less than or equal to
        defaultInventoryItemDelegateFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo is less than
        defaultInventoryItemDelegateFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where sequenceNo is greater than
        defaultInventoryItemDelegateFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByEffectiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where effectiveDate equals to
        defaultInventoryItemDelegateFiltering(
            "effectiveDate.equals=" + DEFAULT_EFFECTIVE_DATE,
            "effectiveDate.equals=" + UPDATED_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByEffectiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where effectiveDate in
        defaultInventoryItemDelegateFiltering(
            "effectiveDate.in=" + DEFAULT_EFFECTIVE_DATE + "," + UPDATED_EFFECTIVE_DATE,
            "effectiveDate.in=" + UPDATED_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByEffectiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where effectiveDate is not null
        defaultInventoryItemDelegateFiltering("effectiveDate.specified=true", "effectiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff equals to
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.equals=" + DEFAULT_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.equals=" + UPDATED_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff in
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.in=" + DEFAULT_QUANTITY_ON_HAND_DIFF + "," + UPDATED_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.in=" + UPDATED_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff is not null
        defaultInventoryItemDelegateFiltering("quantityOnHandDiff.specified=true", "quantityOnHandDiff.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff is greater than or equal to
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.greaterThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.greaterThanOrEqual=" + UPDATED_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff is less than or equal to
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.lessThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.lessThanOrEqual=" + SMALLER_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff is less than
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.lessThan=" + UPDATED_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.lessThan=" + DEFAULT_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByQuantityOnHandDiffIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where quantityOnHandDiff is greater than
        defaultInventoryItemDelegateFiltering(
            "quantityOnHandDiff.greaterThan=" + SMALLER_QUANTITY_ON_HAND_DIFF,
            "quantityOnHandDiff.greaterThan=" + DEFAULT_QUANTITY_ON_HAND_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff equals to
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.equals=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.equals=" + UPDATED_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff in
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.in=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF + "," + UPDATED_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.in=" + UPDATED_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff is not null
        defaultInventoryItemDelegateFiltering("availableToPromiseDiff.specified=true", "availableToPromiseDiff.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff is greater than or equal to
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.greaterThanOrEqual=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.greaterThanOrEqual=" + UPDATED_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff is less than or equal to
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.lessThanOrEqual=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.lessThanOrEqual=" + SMALLER_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff is less than
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.lessThan=" + UPDATED_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.lessThan=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAvailableToPromiseDiffIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where availableToPromiseDiff is greater than
        defaultInventoryItemDelegateFiltering(
            "availableToPromiseDiff.greaterThan=" + SMALLER_AVAILABLE_TO_PROMISE_DIFF,
            "availableToPromiseDiff.greaterThan=" + DEFAULT_AVAILABLE_TO_PROMISE_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff equals to
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.equals=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.equals=" + UPDATED_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff in
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.in=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF + "," + UPDATED_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.in=" + UPDATED_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff is not null
        defaultInventoryItemDelegateFiltering("accountingQuantityDiff.specified=true", "accountingQuantityDiff.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff is greater than or equal to
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.greaterThanOrEqual=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.greaterThanOrEqual=" + UPDATED_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff is less than or equal to
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.lessThanOrEqual=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.lessThanOrEqual=" + SMALLER_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff is less than
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.lessThan=" + UPDATED_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.lessThan=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByAccountingQuantityDiffIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where accountingQuantityDiff is greater than
        defaultInventoryItemDelegateFiltering(
            "accountingQuantityDiff.greaterThan=" + SMALLER_ACCOUNTING_QUANTITY_DIFF,
            "accountingQuantityDiff.greaterThan=" + DEFAULT_ACCOUNTING_QUANTITY_DIFF
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost equals to
        defaultInventoryItemDelegateFiltering("unitCost.equals=" + DEFAULT_UNIT_COST, "unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost in
        defaultInventoryItemDelegateFiltering(
            "unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST,
            "unitCost.in=" + UPDATED_UNIT_COST
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost is not null
        defaultInventoryItemDelegateFiltering("unitCost.specified=true", "unitCost.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost is greater than or equal to
        defaultInventoryItemDelegateFiltering(
            "unitCost.greaterThanOrEqual=" + DEFAULT_UNIT_COST,
            "unitCost.greaterThanOrEqual=" + UPDATED_UNIT_COST
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost is less than or equal to
        defaultInventoryItemDelegateFiltering(
            "unitCost.lessThanOrEqual=" + DEFAULT_UNIT_COST,
            "unitCost.lessThanOrEqual=" + SMALLER_UNIT_COST
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost is less than
        defaultInventoryItemDelegateFiltering("unitCost.lessThan=" + UPDATED_UNIT_COST, "unitCost.lessThan=" + DEFAULT_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByUnitCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where unitCost is greater than
        defaultInventoryItemDelegateFiltering("unitCost.greaterThan=" + SMALLER_UNIT_COST, "unitCost.greaterThan=" + DEFAULT_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where remarks equals to
        defaultInventoryItemDelegateFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where remarks in
        defaultInventoryItemDelegateFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where remarks is not null
        defaultInventoryItemDelegateFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where remarks contains
        defaultInventoryItemDelegateFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        // Get all the inventoryItemDelegateList where remarks does not contain
        defaultInventoryItemDelegateFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        inventoryItemDelegate.setInventoryItem(inventoryItem);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the inventoryItemDelegateList where inventoryItem equals to inventoryItemId
        defaultInventoryItemDelegateShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the inventoryItemDelegateList where inventoryItem equals to (inventoryItemId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        inventoryItemDelegate.setOrder(order);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long orderId = order.getId();
        // Get all the inventoryItemDelegateList where order equals to orderId
        defaultInventoryItemDelegateShouldBeFound("orderId.equals=" + orderId);

        // Get all the inventoryItemDelegateList where order equals to (orderId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        inventoryItemDelegate.setOrderItem(orderItem);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long orderItemId = orderItem.getId();
        // Get all the inventoryItemDelegateList where orderItem equals to orderItemId
        defaultInventoryItemDelegateShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the inventoryItemDelegateList where orderItem equals to (orderItemId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByInvoiceIsEqualToSomething() throws Exception {
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            invoice = InvoiceResourceIT.createEntity(em);
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(invoice);
        em.flush();
        inventoryItemDelegate.setInvoice(invoice);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long invoiceId = invoice.getId();
        // Get all the inventoryItemDelegateList where invoice equals to invoiceId
        defaultInventoryItemDelegateShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the inventoryItemDelegateList where invoice equals to (invoiceId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(invoiceItem);
        em.flush();
        inventoryItemDelegate.setInvoiceItem(invoiceItem);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long invoiceItemId = invoiceItem.getId();
        // Get all the inventoryItemDelegateList where invoiceItem equals to invoiceItemId
        defaultInventoryItemDelegateShouldBeFound("invoiceItemId.equals=" + invoiceItemId);

        // Get all the inventoryItemDelegateList where invoiceItem equals to (invoiceItemId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("invoiceItemId.equals=" + (invoiceItemId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        inventoryItemDelegate.setShipment(shipment);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long shipmentId = shipment.getId();
        // Get all the inventoryItemDelegateList where shipment equals to shipmentId
        defaultInventoryItemDelegateShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the inventoryItemDelegateList where shipment equals to (shipmentId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByShipmentItemIsEqualToSomething() throws Exception {
        ShipmentItem shipmentItem;
        if (TestUtil.findAll(em, ShipmentItem.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            shipmentItem = ShipmentItemResourceIT.createEntity(em);
        } else {
            shipmentItem = TestUtil.findAll(em, ShipmentItem.class).getFirst();
        }
        em.persist(shipmentItem);
        em.flush();
        inventoryItemDelegate.setShipmentItem(shipmentItem);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long shipmentItemId = shipmentItem.getId();
        // Get all the inventoryItemDelegateList where shipmentItem equals to shipmentItemId
        defaultInventoryItemDelegateShouldBeFound("shipmentItemId.equals=" + shipmentItemId);

        // Get all the inventoryItemDelegateList where shipmentItem equals to (shipmentItemId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("shipmentItemId.equals=" + (shipmentItemId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByItemIssuanceIsEqualToSomething() throws Exception {
        ItemIssuance itemIssuance;
        if (TestUtil.findAll(em, ItemIssuance.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            itemIssuance = ItemIssuanceResourceIT.createEntity(em);
        } else {
            itemIssuance = TestUtil.findAll(em, ItemIssuance.class).getFirst();
        }
        em.persist(itemIssuance);
        em.flush();
        inventoryItemDelegate.setItemIssuance(itemIssuance);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long itemIssuanceId = itemIssuance.getId();
        // Get all the inventoryItemDelegateList where itemIssuance equals to itemIssuanceId
        defaultInventoryItemDelegateShouldBeFound("itemIssuanceId.equals=" + itemIssuanceId);

        // Get all the inventoryItemDelegateList where itemIssuance equals to (itemIssuanceId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("itemIssuanceId.equals=" + (itemIssuanceId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemDelegatesByReceiptIsEqualToSomething() throws Exception {
        ShipmentReceipt receipt;
        if (TestUtil.findAll(em, ShipmentReceipt.class).isEmpty()) {
            inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
            receipt = ShipmentReceiptResourceIT.createEntity();
        } else {
            receipt = TestUtil.findAll(em, ShipmentReceipt.class).getFirst();
        }
        em.persist(receipt);
        em.flush();
        inventoryItemDelegate.setReceipt(receipt);
        inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);
        Long receiptId = receipt.getId();
        // Get all the inventoryItemDelegateList where receipt equals to receiptId
        defaultInventoryItemDelegateShouldBeFound("receiptId.equals=" + receiptId);

        // Get all the inventoryItemDelegateList where receipt equals to (receiptId + 1)
        defaultInventoryItemDelegateShouldNotBeFound("receiptId.equals=" + (receiptId + 1));
    }

    private void defaultInventoryItemDelegateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemDelegateShouldBeFound(shouldBeFound);
        defaultInventoryItemDelegateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemDelegateShouldBeFound(String filter) throws Exception {
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemDelegate.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantityOnHandDiff").value(hasItem(sameNumber(DEFAULT_QUANTITY_ON_HAND_DIFF))))
            .andExpect(jsonPath("$.[*].availableToPromiseDiff").value(hasItem(sameNumber(DEFAULT_AVAILABLE_TO_PROMISE_DIFF))))
            .andExpect(jsonPath("$.[*].accountingQuantityDiff").value(hasItem(sameNumber(DEFAULT_ACCOUNTING_QUANTITY_DIFF))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemDelegateShouldNotBeFound(String filter) throws Exception {
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemDelegateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItemDelegate() throws Exception {
        // Get the inventoryItemDelegate
        restInventoryItemDelegateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItemDelegate() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemDelegate
        InventoryItemDelegate updatedInventoryItemDelegate = inventoryItemDelegateRepository
            .findById(inventoryItemDelegate.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItemDelegate are not directly saved in db
        em.detach(updatedInventoryItemDelegate);
        updatedInventoryItemDelegate
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .quantityOnHandDiff(UPDATED_QUANTITY_ON_HAND_DIFF)
            .availableToPromiseDiff(UPDATED_AVAILABLE_TO_PROMISE_DIFF)
            .accountingQuantityDiff(UPDATED_ACCOUNTING_QUANTITY_DIFF)
            .unitCost(UPDATED_UNIT_COST)
            .remarks(UPDATED_REMARKS);

        restInventoryItemDelegateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventoryItemDelegate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventoryItemDelegate))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemDelegateToMatchAllProperties(updatedInventoryItemDelegate);
    }

    @Test
    @Transactional
    void putNonExistingInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemDelegate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDelegate))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDelegate))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDelegate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemDelegateWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemDelegate using partial update
        InventoryItemDelegate partialUpdatedInventoryItemDelegate = new InventoryItemDelegate();
        partialUpdatedInventoryItemDelegate.setId(inventoryItemDelegate.getId());

        partialUpdatedInventoryItemDelegate.availableToPromiseDiff(UPDATED_AVAILABLE_TO_PROMISE_DIFF).remarks(UPDATED_REMARKS);

        restInventoryItemDelegateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemDelegate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemDelegate))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemDelegate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemDelegateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItemDelegate, inventoryItemDelegate),
            getPersistedInventoryItemDelegate(inventoryItemDelegate)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemDelegateWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemDelegate using partial update
        InventoryItemDelegate partialUpdatedInventoryItemDelegate = new InventoryItemDelegate();
        partialUpdatedInventoryItemDelegate.setId(inventoryItemDelegate.getId());

        partialUpdatedInventoryItemDelegate
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .quantityOnHandDiff(UPDATED_QUANTITY_ON_HAND_DIFF)
            .availableToPromiseDiff(UPDATED_AVAILABLE_TO_PROMISE_DIFF)
            .accountingQuantityDiff(UPDATED_ACCOUNTING_QUANTITY_DIFF)
            .unitCost(UPDATED_UNIT_COST)
            .remarks(UPDATED_REMARKS);

        restInventoryItemDelegateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemDelegate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemDelegate))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemDelegate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemDelegateUpdatableFieldsEquals(
            partialUpdatedInventoryItemDelegate,
            getPersistedInventoryItemDelegate(partialUpdatedInventoryItemDelegate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItemDelegate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDelegate))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDelegate))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItemDelegate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemDelegate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemDelegateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItemDelegate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemDelegate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryItemDelegate() throws Exception {
        // Initialize the database
        insertedInventoryItemDelegate = inventoryItemDelegateRepository.saveAndFlush(inventoryItemDelegate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryItemDelegate
        restInventoryItemDelegateMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItemDelegate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryItemDelegateRepository.count();
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

    protected InventoryItemDelegate getPersistedInventoryItemDelegate(InventoryItemDelegate inventoryItemDelegate) {
        return inventoryItemDelegateRepository.findById(inventoryItemDelegate.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemDelegateToMatchAllProperties(InventoryItemDelegate expectedInventoryItemDelegate) {
        assertInventoryItemDelegateAllPropertiesEquals(
            expectedInventoryItemDelegate,
            getPersistedInventoryItemDelegate(expectedInventoryItemDelegate)
        );
    }

    protected void assertPersistedInventoryItemDelegateToMatchUpdatableProperties(InventoryItemDelegate expectedInventoryItemDelegate) {
        assertInventoryItemDelegateAllUpdatablePropertiesEquals(
            expectedInventoryItemDelegate,
            getPersistedInventoryItemDelegate(expectedInventoryItemDelegate)
        );
    }
}
