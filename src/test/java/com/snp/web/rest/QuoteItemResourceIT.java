package com.snp.web.rest;

import static com.snp.domain.QuoteItemAsserts.*;
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
import com.snp.domain.ProductFeature;
import com.snp.domain.Quote;
import com.snp.domain.QuoteItem;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.QuoteItemRepository;
import com.snp.service.QuoteItemService;
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
 * Integration tests for the {@link QuoteItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuoteItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SELECTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELECTED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SELECTED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QUOTE_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUOTE_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUOTE_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_ESTIMATED_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_DELIVERY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_LEAD_TIME_DAYS = 1;
    private static final Integer UPDATED_LEAD_TIME_DAYS = 2;
    private static final Integer SMALLER_LEAD_TIME_DAYS = 1 - 1;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quote-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteItemRepository quoteItemRepository;

    @Mock
    private QuoteItemRepository quoteItemRepositoryMock;

    @Mock
    private QuoteItemService quoteItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteItemMockMvc;

    private QuoteItem quoteItem;

    private QuoteItem insertedQuoteItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteItem createEntity(EntityManager em) {
        QuoteItem quoteItem = new QuoteItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .selectedAmount(DEFAULT_SELECTED_AMOUNT)
            .quoteUnitPrice(DEFAULT_QUOTE_UNIT_PRICE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .estimatedDeliveryDate(DEFAULT_ESTIMATED_DELIVERY_DATE)
            .leadTimeDays(DEFAULT_LEAD_TIME_DAYS)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quote = QuoteResourceIT.createEntity(em);
            em.persist(quote);
            em.flush();
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        quoteItem.setQuote(quote);
        return quoteItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteItem createUpdatedEntity(EntityManager em) {
        QuoteItem updatedQuoteItem = new QuoteItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .quoteUnitPrice(UPDATED_QUOTE_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quote = QuoteResourceIT.createUpdatedEntity(em);
            em.persist(quote);
            em.flush();
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        updatedQuoteItem.setQuote(quote);
        return updatedQuoteItem;
    }

    @BeforeEach
    void initTest() {
        quoteItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteItem != null) {
            quoteItemRepository.delete(insertedQuoteItem);
            insertedQuoteItem = null;
        }
    }

    @Test
    @Transactional
    void createQuoteItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteItem
        var returnedQuoteItem = om.readValue(
            restQuoteItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteItem.class
        );

        // Validate the QuoteItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteItemUpdatableFieldsEquals(returnedQuoteItem, getPersistedQuoteItem(returnedQuoteItem));

        insertedQuoteItem = returnedQuoteItem;
    }

    @Test
    @Transactional
    void createQuoteItemWithExistingId() throws Exception {
        // Create the QuoteItem with an existing ID
        quoteItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteItem)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quoteItem.setQuantity(null);

        // Create the QuoteItem, which fails.

        restQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuoteItems() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].quoteUnitPrice").value(hasItem(sameNumber(DEFAULT_QUOTE_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].leadTimeDays").value(hasItem(DEFAULT_LEAD_TIME_DAYS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quoteItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quoteItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quoteItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quoteItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuoteItem() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get the quoteItem
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.selectedAmount").value(sameNumber(DEFAULT_SELECTED_AMOUNT)))
            .andExpect(jsonPath("$.quoteUnitPrice").value(sameNumber(DEFAULT_QUOTE_UNIT_PRICE)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.estimatedDeliveryDate").value(DEFAULT_ESTIMATED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.leadTimeDays").value(DEFAULT_LEAD_TIME_DAYS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getQuoteItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        Long id = quoteItem.getId();

        defaultQuoteItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo equals to
        defaultQuoteItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo in
        defaultQuoteItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo is not null
        defaultQuoteItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo is greater than or equal to
        defaultQuoteItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo is less than or equal to
        defaultQuoteItemFiltering("sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO, "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo is less than
        defaultQuoteItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where sequenceNo is greater than
        defaultQuoteItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity equals to
        defaultQuoteItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity in
        defaultQuoteItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity is not null
        defaultQuoteItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity is greater than or equal to
        defaultQuoteItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity is less than or equal to
        defaultQuoteItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity is less than
        defaultQuoteItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quantity is greater than
        defaultQuoteItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount equals to
        defaultQuoteItemFiltering("selectedAmount.equals=" + DEFAULT_SELECTED_AMOUNT, "selectedAmount.equals=" + UPDATED_SELECTED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount in
        defaultQuoteItemFiltering(
            "selectedAmount.in=" + DEFAULT_SELECTED_AMOUNT + "," + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.in=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount is not null
        defaultQuoteItemFiltering("selectedAmount.specified=true", "selectedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount is greater than or equal to
        defaultQuoteItemFiltering(
            "selectedAmount.greaterThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.greaterThanOrEqual=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount is less than or equal to
        defaultQuoteItemFiltering(
            "selectedAmount.lessThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.lessThanOrEqual=" + SMALLER_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount is less than
        defaultQuoteItemFiltering(
            "selectedAmount.lessThan=" + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.lessThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsBySelectedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where selectedAmount is greater than
        defaultQuoteItemFiltering(
            "selectedAmount.greaterThan=" + SMALLER_SELECTED_AMOUNT,
            "selectedAmount.greaterThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice equals to
        defaultQuoteItemFiltering("quoteUnitPrice.equals=" + DEFAULT_QUOTE_UNIT_PRICE, "quoteUnitPrice.equals=" + UPDATED_QUOTE_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice in
        defaultQuoteItemFiltering(
            "quoteUnitPrice.in=" + DEFAULT_QUOTE_UNIT_PRICE + "," + UPDATED_QUOTE_UNIT_PRICE,
            "quoteUnitPrice.in=" + UPDATED_QUOTE_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice is not null
        defaultQuoteItemFiltering("quoteUnitPrice.specified=true", "quoteUnitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice is greater than or equal to
        defaultQuoteItemFiltering(
            "quoteUnitPrice.greaterThanOrEqual=" + DEFAULT_QUOTE_UNIT_PRICE,
            "quoteUnitPrice.greaterThanOrEqual=" + UPDATED_QUOTE_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice is less than or equal to
        defaultQuoteItemFiltering(
            "quoteUnitPrice.lessThanOrEqual=" + DEFAULT_QUOTE_UNIT_PRICE,
            "quoteUnitPrice.lessThanOrEqual=" + SMALLER_QUOTE_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice is less than
        defaultQuoteItemFiltering(
            "quoteUnitPrice.lessThan=" + UPDATED_QUOTE_UNIT_PRICE,
            "quoteUnitPrice.lessThan=" + DEFAULT_QUOTE_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where quoteUnitPrice is greater than
        defaultQuoteItemFiltering(
            "quoteUnitPrice.greaterThan=" + SMALLER_QUOTE_UNIT_PRICE,
            "quoteUnitPrice.greaterThan=" + DEFAULT_QUOTE_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount equals to
        defaultQuoteItemFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount in
        defaultQuoteItemFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount is not null
        defaultQuoteItemFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount is greater than or equal to
        defaultQuoteItemFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount is less than or equal to
        defaultQuoteItemFiltering(
            "totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount is less than
        defaultQuoteItemFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where totalAmount is greater than
        defaultQuoteItemFiltering("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT, "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByEstimatedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where estimatedDeliveryDate equals to
        defaultQuoteItemFiltering(
            "estimatedDeliveryDate.equals=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.equals=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByEstimatedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where estimatedDeliveryDate in
        defaultQuoteItemFiltering(
            "estimatedDeliveryDate.in=" + DEFAULT_ESTIMATED_DELIVERY_DATE + "," + UPDATED_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.in=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByEstimatedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where estimatedDeliveryDate is not null
        defaultQuoteItemFiltering("estimatedDeliveryDate.specified=true", "estimatedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays equals to
        defaultQuoteItemFiltering("leadTimeDays.equals=" + DEFAULT_LEAD_TIME_DAYS, "leadTimeDays.equals=" + UPDATED_LEAD_TIME_DAYS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays in
        defaultQuoteItemFiltering(
            "leadTimeDays.in=" + DEFAULT_LEAD_TIME_DAYS + "," + UPDATED_LEAD_TIME_DAYS,
            "leadTimeDays.in=" + UPDATED_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays is not null
        defaultQuoteItemFiltering("leadTimeDays.specified=true", "leadTimeDays.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays is greater than or equal to
        defaultQuoteItemFiltering(
            "leadTimeDays.greaterThanOrEqual=" + DEFAULT_LEAD_TIME_DAYS,
            "leadTimeDays.greaterThanOrEqual=" + UPDATED_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays is less than or equal to
        defaultQuoteItemFiltering(
            "leadTimeDays.lessThanOrEqual=" + DEFAULT_LEAD_TIME_DAYS,
            "leadTimeDays.lessThanOrEqual=" + SMALLER_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays is less than
        defaultQuoteItemFiltering("leadTimeDays.lessThan=" + UPDATED_LEAD_TIME_DAYS, "leadTimeDays.lessThan=" + DEFAULT_LEAD_TIME_DAYS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByLeadTimeDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where leadTimeDays is greater than
        defaultQuoteItemFiltering(
            "leadTimeDays.greaterThan=" + SMALLER_LEAD_TIME_DAYS,
            "leadTimeDays.greaterThan=" + DEFAULT_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where comments equals to
        defaultQuoteItemFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where comments in
        defaultQuoteItemFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where comments is not null
        defaultQuoteItemFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where comments contains
        defaultQuoteItemFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        // Get all the quoteItemList where comments does not contain
        defaultQuoteItemFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteItemsByQuoteIsEqualToSomething() throws Exception {
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            quote = QuoteResourceIT.createEntity(em);
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        em.persist(quote);
        em.flush();
        quoteItem.setQuote(quote);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long quoteId = quote.getId();
        // Get all the quoteItemList where quote equals to quoteId
        defaultQuoteItemShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the quoteItemList where quote equals to (quoteId + 1)
        defaultQuoteItemShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        quoteItem.setProduct(product);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long productId = product.getId();
        // Get all the quoteItemList where product equals to productId
        defaultQuoteItemShouldBeFound("productId.equals=" + productId);

        // Get all the quoteItemList where product equals to (productId + 1)
        defaultQuoteItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        quoteItem.setProductFeature(productFeature);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long productFeatureId = productFeature.getId();
        // Get all the quoteItemList where productFeature equals to productFeatureId
        defaultQuoteItemShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the quoteItemList where productFeature equals to (productFeatureId + 1)
        defaultQuoteItemShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        quoteItem.setUom(uom);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long uomId = uom.getId();
        // Get all the quoteItemList where uom equals to uomId
        defaultQuoteItemShouldBeFound("uomId.equals=" + uomId);

        // Get all the quoteItemList where uom equals to (uomId + 1)
        defaultQuoteItemShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        quoteItem.setStatus(status);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long statusId = status.getId();
        // Get all the quoteItemList where status equals to statusId
        defaultQuoteItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the quoteItemList where status equals to (statusId + 1)
        defaultQuoteItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCustRequestIsEqualToSomething() throws Exception {
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            custRequest = CustRequestResourceIT.createEntity(em);
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        em.persist(custRequest);
        em.flush();
        quoteItem.setCustRequest(custRequest);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long custRequestId = custRequest.getId();
        // Get all the quoteItemList where custRequest equals to custRequestId
        defaultQuoteItemShouldBeFound("custRequestId.equals=" + custRequestId);

        // Get all the quoteItemList where custRequest equals to (custRequestId + 1)
        defaultQuoteItemShouldNotBeFound("custRequestId.equals=" + (custRequestId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteItemsByCustRequestItemIsEqualToSomething() throws Exception {
        CustRequestItem custRequestItem;
        if (TestUtil.findAll(em, CustRequestItem.class).isEmpty()) {
            quoteItemRepository.saveAndFlush(quoteItem);
            custRequestItem = CustRequestItemResourceIT.createEntity(em);
        } else {
            custRequestItem = TestUtil.findAll(em, CustRequestItem.class).getFirst();
        }
        em.persist(custRequestItem);
        em.flush();
        quoteItem.setCustRequestItem(custRequestItem);
        quoteItemRepository.saveAndFlush(quoteItem);
        Long custRequestItemId = custRequestItem.getId();
        // Get all the quoteItemList where custRequestItem equals to custRequestItemId
        defaultQuoteItemShouldBeFound("custRequestItemId.equals=" + custRequestItemId);

        // Get all the quoteItemList where custRequestItem equals to (custRequestItemId + 1)
        defaultQuoteItemShouldNotBeFound("custRequestItemId.equals=" + (custRequestItemId + 1));
    }

    private void defaultQuoteItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteItemShouldBeFound(shouldBeFound);
        defaultQuoteItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteItemShouldBeFound(String filter) throws Exception {
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].quoteUnitPrice").value(hasItem(sameNumber(DEFAULT_QUOTE_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].leadTimeDays").value(hasItem(DEFAULT_LEAD_TIME_DAYS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteItemShouldNotBeFound(String filter) throws Exception {
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuoteItem() throws Exception {
        // Get the quoteItem
        restQuoteItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteItem() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteItem
        QuoteItem updatedQuoteItem = quoteItemRepository.findById(quoteItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteItem are not directly saved in db
        em.detach(updatedQuoteItem);
        updatedQuoteItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .quoteUnitPrice(UPDATED_QUOTE_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .comments(UPDATED_COMMENTS);

        restQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteItem))
            )
            .andExpect(status().isOk());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteItemToMatchAllProperties(updatedQuoteItem);
    }

    @Test
    @Transactional
    void putNonExistingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteItemWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteItem using partial update
        QuoteItem partialUpdatedQuoteItem = new QuoteItem();
        partialUpdatedQuoteItem.setId(quoteItem.getId());

        partialUpdatedQuoteItem.sequenceNo(UPDATED_SEQUENCE_NO).selectedAmount(UPDATED_SELECTED_AMOUNT);

        restQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteItem))
            )
            .andExpect(status().isOk());

        // Validate the QuoteItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteItem, quoteItem),
            getPersistedQuoteItem(quoteItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteItemWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteItem using partial update
        QuoteItem partialUpdatedQuoteItem = new QuoteItem();
        partialUpdatedQuoteItem.setId(quoteItem.getId());

        partialUpdatedQuoteItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .quoteUnitPrice(UPDATED_QUOTE_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .comments(UPDATED_COMMENTS);

        restQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteItem))
            )
            .andExpect(status().isOk());

        // Validate the QuoteItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteItemUpdatableFieldsEquals(partialUpdatedQuoteItem, getPersistedQuoteItem(partialUpdatedQuoteItem));
    }

    @Test
    @Transactional
    void patchNonExistingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteItem() throws Exception {
        // Initialize the database
        insertedQuoteItem = quoteItemRepository.saveAndFlush(quoteItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteItem
        restQuoteItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteItemRepository.count();
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

    protected QuoteItem getPersistedQuoteItem(QuoteItem quoteItem) {
        return quoteItemRepository.findById(quoteItem.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteItemToMatchAllProperties(QuoteItem expectedQuoteItem) {
        assertQuoteItemAllPropertiesEquals(expectedQuoteItem, getPersistedQuoteItem(expectedQuoteItem));
    }

    protected void assertPersistedQuoteItemToMatchUpdatableProperties(QuoteItem expectedQuoteItem) {
        assertQuoteItemAllUpdatablePropertiesEquals(expectedQuoteItem, getPersistedQuoteItem(expectedQuoteItem));
    }
}
