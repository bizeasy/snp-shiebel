package com.snp.web.rest;

import static com.snp.domain.CustRequestItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequest;
import com.snp.domain.CustRequestItem;
import com.snp.domain.Product;
import com.snp.domain.ShoppingListItem;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.CustRequestItemRepository;
import com.snp.service.CustRequestItemService;
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
 * Integration tests for the {@link CustRequestItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Instant DEFAULT_REQUIRED_BY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUIRED_BY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SELECTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELECTED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SELECTED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAX_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STORY = "AAAAAAAAAA";
    private static final String UPDATED_STORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cust-request-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestItemRepository custRequestItemRepository;

    @Mock
    private CustRequestItemRepository custRequestItemRepositoryMock;

    @Mock
    private CustRequestItemService custRequestItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestItemMockMvc;

    private CustRequestItem custRequestItem;

    private CustRequestItem insertedCustRequestItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestItem createEntity(EntityManager em) {
        CustRequestItem custRequestItem = new CustRequestItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .priority(DEFAULT_PRIORITY)
            .requiredByDate(DEFAULT_REQUIRED_BY_DATE)
            .quantity(DEFAULT_QUANTITY)
            .selectedAmount(DEFAULT_SELECTED_AMOUNT)
            .maxAmount(DEFAULT_MAX_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .story(DEFAULT_STORY);
        // Add required entity
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequest = CustRequestResourceIT.createEntity(em);
            em.persist(custRequest);
            em.flush();
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        custRequestItem.setCustRequest(custRequest);
        return custRequestItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestItem createUpdatedEntity(EntityManager em) {
        CustRequestItem updatedCustRequestItem = new CustRequestItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .priority(UPDATED_PRIORITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);
        // Add required entity
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequest = CustRequestResourceIT.createUpdatedEntity(em);
            em.persist(custRequest);
            em.flush();
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        updatedCustRequestItem.setCustRequest(custRequest);
        return updatedCustRequestItem;
    }

    @BeforeEach
    void initTest() {
        custRequestItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestItem != null) {
            custRequestItemRepository.delete(insertedCustRequestItem);
            insertedCustRequestItem = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestItem
        var returnedCustRequestItem = om.readValue(
            restCustRequestItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestItem.class
        );

        // Validate the CustRequestItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestItemUpdatableFieldsEquals(returnedCustRequestItem, getPersistedCustRequestItem(returnedCustRequestItem));

        insertedCustRequestItem = returnedCustRequestItem;
    }

    @Test
    @Transactional
    void createCustRequestItemWithExistingId() throws Exception {
        // Create the CustRequestItem with an existing ID
        custRequestItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItem)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        custRequestItem.setQuantity(null);

        // Create the CustRequestItem, which fails.

        restCustRequestItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustRequestItems() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].requiredByDate").value(hasItem(DEFAULT_REQUIRED_BY_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(sameNumber(DEFAULT_MAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestItem() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get the custRequestItem
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.requiredByDate").value(DEFAULT_REQUIRED_BY_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.selectedAmount").value(sameNumber(DEFAULT_SELECTED_AMOUNT)))
            .andExpect(jsonPath("$.maxAmount").value(sameNumber(DEFAULT_MAX_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.story").value(DEFAULT_STORY));
    }

    @Test
    @Transactional
    void getCustRequestItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        Long id = custRequestItem.getId();

        defaultCustRequestItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo equals to
        defaultCustRequestItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo in
        defaultCustRequestItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo is not null
        defaultCustRequestItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo is greater than or equal to
        defaultCustRequestItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo is less than or equal to
        defaultCustRequestItemFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo is less than
        defaultCustRequestItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where sequenceNo is greater than
        defaultCustRequestItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority equals to
        defaultCustRequestItemFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority in
        defaultCustRequestItemFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority is not null
        defaultCustRequestItemFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority is greater than or equal to
        defaultCustRequestItemFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority is less than or equal to
        defaultCustRequestItemFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority is less than
        defaultCustRequestItemFiltering("priority.lessThan=" + (DEFAULT_PRIORITY + 1), "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where priority is greater than
        defaultCustRequestItemFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByRequiredByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where requiredByDate equals to
        defaultCustRequestItemFiltering(
            "requiredByDate.equals=" + DEFAULT_REQUIRED_BY_DATE,
            "requiredByDate.equals=" + UPDATED_REQUIRED_BY_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByRequiredByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where requiredByDate in
        defaultCustRequestItemFiltering(
            "requiredByDate.in=" + DEFAULT_REQUIRED_BY_DATE + "," + UPDATED_REQUIRED_BY_DATE,
            "requiredByDate.in=" + UPDATED_REQUIRED_BY_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByRequiredByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where requiredByDate is not null
        defaultCustRequestItemFiltering("requiredByDate.specified=true", "requiredByDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity equals to
        defaultCustRequestItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity in
        defaultCustRequestItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity is not null
        defaultCustRequestItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity is greater than or equal to
        defaultCustRequestItemFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity is less than or equal to
        defaultCustRequestItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity is less than
        defaultCustRequestItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where quantity is greater than
        defaultCustRequestItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount equals to
        defaultCustRequestItemFiltering(
            "selectedAmount.equals=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.equals=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount in
        defaultCustRequestItemFiltering(
            "selectedAmount.in=" + DEFAULT_SELECTED_AMOUNT + "," + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.in=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount is not null
        defaultCustRequestItemFiltering("selectedAmount.specified=true", "selectedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount is greater than or equal to
        defaultCustRequestItemFiltering(
            "selectedAmount.greaterThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.greaterThanOrEqual=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount is less than or equal to
        defaultCustRequestItemFiltering(
            "selectedAmount.lessThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.lessThanOrEqual=" + SMALLER_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount is less than
        defaultCustRequestItemFiltering(
            "selectedAmount.lessThan=" + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.lessThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsBySelectedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where selectedAmount is greater than
        defaultCustRequestItemFiltering(
            "selectedAmount.greaterThan=" + SMALLER_SELECTED_AMOUNT,
            "selectedAmount.greaterThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount equals to
        defaultCustRequestItemFiltering("maxAmount.equals=" + DEFAULT_MAX_AMOUNT, "maxAmount.equals=" + UPDATED_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount in
        defaultCustRequestItemFiltering(
            "maxAmount.in=" + DEFAULT_MAX_AMOUNT + "," + UPDATED_MAX_AMOUNT,
            "maxAmount.in=" + UPDATED_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount is not null
        defaultCustRequestItemFiltering("maxAmount.specified=true", "maxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount is greater than or equal to
        defaultCustRequestItemFiltering(
            "maxAmount.greaterThanOrEqual=" + DEFAULT_MAX_AMOUNT,
            "maxAmount.greaterThanOrEqual=" + UPDATED_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount is less than or equal to
        defaultCustRequestItemFiltering(
            "maxAmount.lessThanOrEqual=" + DEFAULT_MAX_AMOUNT,
            "maxAmount.lessThanOrEqual=" + SMALLER_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount is less than
        defaultCustRequestItemFiltering("maxAmount.lessThan=" + UPDATED_MAX_AMOUNT, "maxAmount.lessThan=" + DEFAULT_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByMaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where maxAmount is greater than
        defaultCustRequestItemFiltering("maxAmount.greaterThan=" + SMALLER_MAX_AMOUNT, "maxAmount.greaterThan=" + DEFAULT_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where description equals to
        defaultCustRequestItemFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where description in
        defaultCustRequestItemFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where description is not null
        defaultCustRequestItemFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where description contains
        defaultCustRequestItemFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where description does not contain
        defaultCustRequestItemFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where story equals to
        defaultCustRequestItemFiltering("story.equals=" + DEFAULT_STORY, "story.equals=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where story in
        defaultCustRequestItemFiltering("story.in=" + DEFAULT_STORY + "," + UPDATED_STORY, "story.in=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where story is not null
        defaultCustRequestItemFiltering("story.specified=true", "story.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStoryContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where story contains
        defaultCustRequestItemFiltering("story.contains=" + DEFAULT_STORY, "story.contains=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        // Get all the custRequestItemList where story does not contain
        defaultCustRequestItemFiltering("story.doesNotContain=" + UPDATED_STORY, "story.doesNotContain=" + DEFAULT_STORY);
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByCustRequestIsEqualToSomething() throws Exception {
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequestItemRepository.saveAndFlush(custRequestItem);
            custRequest = CustRequestResourceIT.createEntity(em);
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        em.persist(custRequest);
        em.flush();
        custRequestItem.setCustRequest(custRequest);
        custRequestItemRepository.saveAndFlush(custRequestItem);
        Long custRequestId = custRequest.getId();
        // Get all the custRequestItemList where custRequest equals to custRequestId
        defaultCustRequestItemShouldBeFound("custRequestId.equals=" + custRequestId);

        // Get all the custRequestItemList where custRequest equals to (custRequestId + 1)
        defaultCustRequestItemShouldNotBeFound("custRequestId.equals=" + (custRequestId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            custRequestItemRepository.saveAndFlush(custRequestItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        custRequestItem.setStatus(status);
        custRequestItemRepository.saveAndFlush(custRequestItem);
        Long statusId = status.getId();
        // Get all the custRequestItemList where status equals to statusId
        defaultCustRequestItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the custRequestItemList where status equals to (statusId + 1)
        defaultCustRequestItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            custRequestItemRepository.saveAndFlush(custRequestItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        custRequestItem.setProduct(product);
        custRequestItemRepository.saveAndFlush(custRequestItem);
        Long productId = product.getId();
        // Get all the custRequestItemList where product equals to productId
        defaultCustRequestItemShouldBeFound("productId.equals=" + productId);

        // Get all the custRequestItemList where product equals to (productId + 1)
        defaultCustRequestItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            custRequestItemRepository.saveAndFlush(custRequestItem);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        custRequestItem.setUom(uom);
        custRequestItemRepository.saveAndFlush(custRequestItem);
        Long uomId = uom.getId();
        // Get all the custRequestItemList where uom equals to uomId
        defaultCustRequestItemShouldBeFound("uomId.equals=" + uomId);

        // Get all the custRequestItemList where uom equals to (uomId + 1)
        defaultCustRequestItemShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestItemsByShoppingListItemIsEqualToSomething() throws Exception {
        ShoppingListItem shoppingListItem;
        if (TestUtil.findAll(em, ShoppingListItem.class).isEmpty()) {
            custRequestItemRepository.saveAndFlush(custRequestItem);
            shoppingListItem = ShoppingListItemResourceIT.createEntity(em);
        } else {
            shoppingListItem = TestUtil.findAll(em, ShoppingListItem.class).getFirst();
        }
        em.persist(shoppingListItem);
        em.flush();
        custRequestItem.setShoppingListItem(shoppingListItem);
        custRequestItemRepository.saveAndFlush(custRequestItem);
        Long shoppingListItemId = shoppingListItem.getId();
        // Get all the custRequestItemList where shoppingListItem equals to shoppingListItemId
        defaultCustRequestItemShouldBeFound("shoppingListItemId.equals=" + shoppingListItemId);

        // Get all the custRequestItemList where shoppingListItem equals to (shoppingListItemId + 1)
        defaultCustRequestItemShouldNotBeFound("shoppingListItemId.equals=" + (shoppingListItemId + 1));
    }

    private void defaultCustRequestItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestItemShouldBeFound(shouldBeFound);
        defaultCustRequestItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestItemShouldBeFound(String filter) throws Exception {
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].requiredByDate").value(hasItem(DEFAULT_REQUIRED_BY_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(sameNumber(DEFAULT_MAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY)));

        // Check, that the count call also returns 1
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestItemShouldNotBeFound(String filter) throws Exception {
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestItem() throws Exception {
        // Get the custRequestItem
        restCustRequestItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestItem() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItem
        CustRequestItem updatedCustRequestItem = custRequestItemRepository.findById(custRequestItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestItem are not directly saved in db
        em.detach(updatedCustRequestItem);
        updatedCustRequestItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .priority(UPDATED_PRIORITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);

        restCustRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestItemToMatchAllProperties(updatedCustRequestItem);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestItemWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItem using partial update
        CustRequestItem partialUpdatedCustRequestItem = new CustRequestItem();
        partialUpdatedCustRequestItem.setId(custRequestItem.getId());

        partialUpdatedCustRequestItem
            .priority(UPDATED_PRIORITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION);

        restCustRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestItem, custRequestItem),
            getPersistedCustRequestItem(custRequestItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestItemWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestItem using partial update
        CustRequestItem partialUpdatedCustRequestItem = new CustRequestItem();
        partialUpdatedCustRequestItem.setId(custRequestItem.getId());

        partialUpdatedCustRequestItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .priority(UPDATED_PRIORITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);

        restCustRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestItem))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestItemUpdatableFieldsEquals(
            partialUpdatedCustRequestItem,
            getPersistedCustRequestItem(partialUpdatedCustRequestItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestItem() throws Exception {
        // Initialize the database
        insertedCustRequestItem = custRequestItemRepository.saveAndFlush(custRequestItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestItem
        restCustRequestItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestItemRepository.count();
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

    protected CustRequestItem getPersistedCustRequestItem(CustRequestItem custRequestItem) {
        return custRequestItemRepository.findById(custRequestItem.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestItemToMatchAllProperties(CustRequestItem expectedCustRequestItem) {
        assertCustRequestItemAllPropertiesEquals(expectedCustRequestItem, getPersistedCustRequestItem(expectedCustRequestItem));
    }

    protected void assertPersistedCustRequestItemToMatchUpdatableProperties(CustRequestItem expectedCustRequestItem) {
        assertCustRequestItemAllUpdatablePropertiesEquals(expectedCustRequestItem, getPersistedCustRequestItem(expectedCustRequestItem));
    }
}
