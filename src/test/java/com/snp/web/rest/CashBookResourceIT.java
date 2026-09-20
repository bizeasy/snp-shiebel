package com.snp.web.rest;

import static com.snp.domain.CashBookAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CashBook;
import com.snp.domain.Department;
import com.snp.domain.Invoice;
import com.snp.domain.Order;
import com.snp.domain.Party;
import com.snp.domain.PaymentMethodType;
import com.snp.domain.Status;
import com.snp.domain.TransactionCategory;
import com.snp.domain.TransactionType;
import com.snp.domain.Uom;
import com.snp.domain.enumeration.RecurrenceFrequency;
import com.snp.repository.CashBookRepository;
import com.snp.service.CashBookService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CashBookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CashBookResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_NO = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_INVOICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT = "BBBBBBBBBB";

    private static final String DEFAULT_USAGE_UNITS = "AAAAAAAAAA";
    private static final String UPDATED_USAGE_UNITS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECURRING_TRANSACTION = false;
    private static final Boolean UPDATED_RECURRING_TRANSACTION = true;

    private static final RecurrenceFrequency DEFAULT_FREQUENCY = RecurrenceFrequency.NONE;
    private static final RecurrenceFrequency UPDATED_FREQUENCY = RecurrenceFrequency.DAILY;

    private static final String ENTITY_API_URL = "/api/cash-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CashBookRepository cashBookRepository;

    @Mock
    private CashBookRepository cashBookRepositoryMock;

    @Mock
    private CashBookService cashBookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashBookMockMvc;

    private CashBook cashBook;

    private CashBook insertedCashBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .vendorCustomerName(DEFAULT_VENDOR_CUSTOMER_NAME)
            .invoiceNo(DEFAULT_INVOICE_NO)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .purpose(DEFAULT_PURPOSE)
            .remarks(DEFAULT_REMARKS)
            .project(DEFAULT_PROJECT)
            .usageUnits(DEFAULT_USAGE_UNITS)
            .recurringTransaction(DEFAULT_RECURRING_TRANSACTION)
            .frequency(DEFAULT_FREQUENCY);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createEntity();
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).getFirst();
        }
        cashBook.setTransactionType(transactionType);
        return cashBook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createUpdatedEntity(EntityManager em) {
        CashBook updatedCashBook = new CashBook()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .vendorCustomerName(UPDATED_VENDOR_CUSTOMER_NAME)
            .invoiceNo(UPDATED_INVOICE_NO)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .project(UPDATED_PROJECT)
            .usageUnits(UPDATED_USAGE_UNITS)
            .recurringTransaction(UPDATED_RECURRING_TRANSACTION)
            .frequency(UPDATED_FREQUENCY);
        // Add required entity
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            transactionType = TransactionTypeResourceIT.createUpdatedEntity();
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).getFirst();
        }
        updatedCashBook.setTransactionType(transactionType);
        return updatedCashBook;
    }

    @BeforeEach
    void initTest() {
        cashBook = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCashBook != null) {
            cashBookRepository.delete(insertedCashBook);
            insertedCashBook = null;
        }
    }

    @Test
    @Transactional
    void createCashBook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CashBook
        var returnedCashBook = om.readValue(
            restCashBookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CashBook.class
        );

        // Validate the CashBook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCashBookUpdatableFieldsEquals(returnedCashBook, getPersistedCashBook(returnedCashBook));

        insertedCashBook = returnedCashBook;
    }

    @Test
    @Transactional
    void createCashBookWithExistingId() throws Exception {
        // Create the CashBook with an existing ID
        cashBook.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cashBook.setTransactionDate(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cashBook.setAmount(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCashBooks() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vendorCustomerName").value(hasItem(DEFAULT_VENDOR_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].invoiceNo").value(hasItem(DEFAULT_INVOICE_NO)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT)))
            .andExpect(jsonPath("$.[*].usageUnits").value(hasItem(DEFAULT_USAGE_UNITS)))
            .andExpect(jsonPath("$.[*].recurringTransaction").value(hasItem(DEFAULT_RECURRING_TRANSACTION)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCashBooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(cashBookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCashBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cashBookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCashBooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cashBookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCashBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cashBookRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCashBook() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get the cashBook
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL_ID, cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashBook.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vendorCustomerName").value(DEFAULT_VENDOR_CUSTOMER_NAME))
            .andExpect(jsonPath("$.invoiceNo").value(DEFAULT_INVOICE_NO))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.project").value(DEFAULT_PROJECT))
            .andExpect(jsonPath("$.usageUnits").value(DEFAULT_USAGE_UNITS))
            .andExpect(jsonPath("$.recurringTransaction").value(DEFAULT_RECURRING_TRANSACTION))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    @Transactional
    void getCashBooksByIdFiltering() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        Long id = cashBook.getId();

        defaultCashBookFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCashBookFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCashBookFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate equals to
        defaultCashBookFiltering(
            "transactionDate.equals=" + DEFAULT_TRANSACTION_DATE,
            "transactionDate.equals=" + UPDATED_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate in
        defaultCashBookFiltering(
            "transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE,
            "transactionDate.in=" + UPDATED_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is not null
        defaultCashBookFiltering("transactionDate.specified=true", "transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than or equal to
        defaultCashBookFiltering(
            "transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE,
            "transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than or equal to
        defaultCashBookFiltering(
            "transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE,
            "transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than
        defaultCashBookFiltering(
            "transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE,
            "transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than
        defaultCashBookFiltering(
            "transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE,
            "transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate equals to
        defaultCashBookFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate in
        defaultCashBookFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate is not null
        defaultCashBookFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate is greater than or equal to
        defaultCashBookFiltering("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE, "dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate is less than or equal to
        defaultCashBookFiltering("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE, "dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate is less than
        defaultCashBookFiltering("dueDate.lessThan=" + UPDATED_DUE_DATE, "dueDate.lessThan=" + DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where dueDate is greater than
        defaultCashBookFiltering("dueDate.greaterThan=" + SMALLER_DUE_DATE, "dueDate.greaterThan=" + DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount equals to
        defaultCashBookFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount in
        defaultCashBookFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount is not null
        defaultCashBookFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount is greater than or equal to
        defaultCashBookFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount is less than or equal to
        defaultCashBookFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount is less than
        defaultCashBookFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where amount is greater than
        defaultCashBookFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCashBooksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where description equals to
        defaultCashBookFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where description in
        defaultCashBookFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where description is not null
        defaultCashBookFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where description contains
        defaultCashBookFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where description does not contain
        defaultCashBookFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCashBooksByVendorCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where vendorCustomerName equals to
        defaultCashBookFiltering(
            "vendorCustomerName.equals=" + DEFAULT_VENDOR_CUSTOMER_NAME,
            "vendorCustomerName.equals=" + UPDATED_VENDOR_CUSTOMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByVendorCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where vendorCustomerName in
        defaultCashBookFiltering(
            "vendorCustomerName.in=" + DEFAULT_VENDOR_CUSTOMER_NAME + "," + UPDATED_VENDOR_CUSTOMER_NAME,
            "vendorCustomerName.in=" + UPDATED_VENDOR_CUSTOMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByVendorCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where vendorCustomerName is not null
        defaultCashBookFiltering("vendorCustomerName.specified=true", "vendorCustomerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByVendorCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where vendorCustomerName contains
        defaultCashBookFiltering(
            "vendorCustomerName.contains=" + DEFAULT_VENDOR_CUSTOMER_NAME,
            "vendorCustomerName.contains=" + UPDATED_VENDOR_CUSTOMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByVendorCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where vendorCustomerName does not contain
        defaultCashBookFiltering(
            "vendorCustomerName.doesNotContain=" + UPDATED_VENDOR_CUSTOMER_NAME,
            "vendorCustomerName.doesNotContain=" + DEFAULT_VENDOR_CUSTOMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceNo equals to
        defaultCashBookFiltering("invoiceNo.equals=" + DEFAULT_INVOICE_NO, "invoiceNo.equals=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceNo in
        defaultCashBookFiltering("invoiceNo.in=" + DEFAULT_INVOICE_NO + "," + UPDATED_INVOICE_NO, "invoiceNo.in=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceNo is not null
        defaultCashBookFiltering("invoiceNo.specified=true", "invoiceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceNoContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceNo contains
        defaultCashBookFiltering("invoiceNo.contains=" + DEFAULT_INVOICE_NO, "invoiceNo.contains=" + UPDATED_INVOICE_NO);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceNo does not contain
        defaultCashBookFiltering("invoiceNo.doesNotContain=" + UPDATED_INVOICE_NO, "invoiceNo.doesNotContain=" + DEFAULT_INVOICE_NO);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate equals to
        defaultCashBookFiltering("invoiceDate.equals=" + DEFAULT_INVOICE_DATE, "invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate in
        defaultCashBookFiltering(
            "invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE,
            "invoiceDate.in=" + UPDATED_INVOICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate is not null
        defaultCashBookFiltering("invoiceDate.specified=true", "invoiceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate is greater than or equal to
        defaultCashBookFiltering(
            "invoiceDate.greaterThanOrEqual=" + DEFAULT_INVOICE_DATE,
            "invoiceDate.greaterThanOrEqual=" + UPDATED_INVOICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate is less than or equal to
        defaultCashBookFiltering(
            "invoiceDate.lessThanOrEqual=" + DEFAULT_INVOICE_DATE,
            "invoiceDate.lessThanOrEqual=" + SMALLER_INVOICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate is less than
        defaultCashBookFiltering("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE, "invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where invoiceDate is greater than
        defaultCashBookFiltering("invoiceDate.greaterThan=" + SMALLER_INVOICE_DATE, "invoiceDate.greaterThan=" + DEFAULT_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllCashBooksByPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where purpose equals to
        defaultCashBookFiltering("purpose.equals=" + DEFAULT_PURPOSE, "purpose.equals=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllCashBooksByPurposeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where purpose in
        defaultCashBookFiltering("purpose.in=" + DEFAULT_PURPOSE + "," + UPDATED_PURPOSE, "purpose.in=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllCashBooksByPurposeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where purpose is not null
        defaultCashBookFiltering("purpose.specified=true", "purpose.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByPurposeContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where purpose contains
        defaultCashBookFiltering("purpose.contains=" + DEFAULT_PURPOSE, "purpose.contains=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllCashBooksByPurposeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where purpose does not contain
        defaultCashBookFiltering("purpose.doesNotContain=" + UPDATED_PURPOSE, "purpose.doesNotContain=" + DEFAULT_PURPOSE);
    }

    @Test
    @Transactional
    void getAllCashBooksByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where remarks equals to
        defaultCashBookFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCashBooksByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where remarks in
        defaultCashBookFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCashBooksByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where remarks is not null
        defaultCashBookFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where remarks contains
        defaultCashBookFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllCashBooksByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where remarks does not contain
        defaultCashBookFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllCashBooksByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where project equals to
        defaultCashBookFiltering("project.equals=" + DEFAULT_PROJECT, "project.equals=" + UPDATED_PROJECT);
    }

    @Test
    @Transactional
    void getAllCashBooksByProjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where project in
        defaultCashBookFiltering("project.in=" + DEFAULT_PROJECT + "," + UPDATED_PROJECT, "project.in=" + UPDATED_PROJECT);
    }

    @Test
    @Transactional
    void getAllCashBooksByProjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where project is not null
        defaultCashBookFiltering("project.specified=true", "project.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByProjectContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where project contains
        defaultCashBookFiltering("project.contains=" + DEFAULT_PROJECT, "project.contains=" + UPDATED_PROJECT);
    }

    @Test
    @Transactional
    void getAllCashBooksByProjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where project does not contain
        defaultCashBookFiltering("project.doesNotContain=" + UPDATED_PROJECT, "project.doesNotContain=" + DEFAULT_PROJECT);
    }

    @Test
    @Transactional
    void getAllCashBooksByUsageUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where usageUnits equals to
        defaultCashBookFiltering("usageUnits.equals=" + DEFAULT_USAGE_UNITS, "usageUnits.equals=" + UPDATED_USAGE_UNITS);
    }

    @Test
    @Transactional
    void getAllCashBooksByUsageUnitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where usageUnits in
        defaultCashBookFiltering(
            "usageUnits.in=" + DEFAULT_USAGE_UNITS + "," + UPDATED_USAGE_UNITS,
            "usageUnits.in=" + UPDATED_USAGE_UNITS
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByUsageUnitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where usageUnits is not null
        defaultCashBookFiltering("usageUnits.specified=true", "usageUnits.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByUsageUnitsContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where usageUnits contains
        defaultCashBookFiltering("usageUnits.contains=" + DEFAULT_USAGE_UNITS, "usageUnits.contains=" + UPDATED_USAGE_UNITS);
    }

    @Test
    @Transactional
    void getAllCashBooksByUsageUnitsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where usageUnits does not contain
        defaultCashBookFiltering("usageUnits.doesNotContain=" + UPDATED_USAGE_UNITS, "usageUnits.doesNotContain=" + DEFAULT_USAGE_UNITS);
    }

    @Test
    @Transactional
    void getAllCashBooksByRecurringTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where recurringTransaction equals to
        defaultCashBookFiltering(
            "recurringTransaction.equals=" + DEFAULT_RECURRING_TRANSACTION,
            "recurringTransaction.equals=" + UPDATED_RECURRING_TRANSACTION
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByRecurringTransactionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where recurringTransaction in
        defaultCashBookFiltering(
            "recurringTransaction.in=" + DEFAULT_RECURRING_TRANSACTION + "," + UPDATED_RECURRING_TRANSACTION,
            "recurringTransaction.in=" + UPDATED_RECURRING_TRANSACTION
        );
    }

    @Test
    @Transactional
    void getAllCashBooksByRecurringTransactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where recurringTransaction is not null
        defaultCashBookFiltering("recurringTransaction.specified=true", "recurringTransaction.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where frequency equals to
        defaultCashBookFiltering("frequency.equals=" + DEFAULT_FREQUENCY, "frequency.equals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllCashBooksByFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where frequency in
        defaultCashBookFiltering("frequency.in=" + DEFAULT_FREQUENCY + "," + UPDATED_FREQUENCY, "frequency.in=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllCashBooksByFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where frequency is not null
        defaultCashBookFiltering("frequency.specified=true", "frequency.specified=false");
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionTypeIsEqualToSomething() throws Exception {
        TransactionType transactionType;
        if (TestUtil.findAll(em, TransactionType.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            transactionType = TransactionTypeResourceIT.createEntity();
        } else {
            transactionType = TestUtil.findAll(em, TransactionType.class).getFirst();
        }
        em.persist(transactionType);
        em.flush();
        cashBook.setTransactionType(transactionType);
        cashBookRepository.saveAndFlush(cashBook);
        Long transactionTypeId = transactionType.getId();
        // Get all the cashBookList where transactionType equals to transactionTypeId
        defaultCashBookShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashBookList where transactionType equals to (transactionTypeId + 1)
        defaultCashBookShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByTransactionCategoryIsEqualToSomething() throws Exception {
        TransactionCategory transactionCategory;
        if (TestUtil.findAll(em, TransactionCategory.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            transactionCategory = TransactionCategoryResourceIT.createEntity();
        } else {
            transactionCategory = TestUtil.findAll(em, TransactionCategory.class).getFirst();
        }
        em.persist(transactionCategory);
        em.flush();
        cashBook.setTransactionCategory(transactionCategory);
        cashBookRepository.saveAndFlush(cashBook);
        Long transactionCategoryId = transactionCategory.getId();
        // Get all the cashBookList where transactionCategory equals to transactionCategoryId
        defaultCashBookShouldBeFound("transactionCategoryId.equals=" + transactionCategoryId);

        // Get all the cashBookList where transactionCategory equals to (transactionCategoryId + 1)
        defaultCashBookShouldNotBeFound("transactionCategoryId.equals=" + (transactionCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByPaymentMethodTypeIsEqualToSomething() throws Exception {
        PaymentMethodType paymentMethodType;
        if (TestUtil.findAll(em, PaymentMethodType.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            paymentMethodType = PaymentMethodTypeResourceIT.createEntity();
        } else {
            paymentMethodType = TestUtil.findAll(em, PaymentMethodType.class).getFirst();
        }
        em.persist(paymentMethodType);
        em.flush();
        cashBook.setPaymentMethodType(paymentMethodType);
        cashBookRepository.saveAndFlush(cashBook);
        Long paymentMethodTypeId = paymentMethodType.getId();
        // Get all the cashBookList where paymentMethodType equals to paymentMethodTypeId
        defaultCashBookShouldBeFound("paymentMethodTypeId.equals=" + paymentMethodTypeId);

        // Get all the cashBookList where paymentMethodType equals to (paymentMethodTypeId + 1)
        defaultCashBookShouldNotBeFound("paymentMethodTypeId.equals=" + (paymentMethodTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            department = DepartmentResourceIT.createEntity();
        } else {
            department = TestUtil.findAll(em, Department.class).getFirst();
        }
        em.persist(department);
        em.flush();
        cashBook.setDepartment(department);
        cashBookRepository.saveAndFlush(cashBook);
        Long departmentId = department.getId();
        // Get all the cashBookList where department equals to departmentId
        defaultCashBookShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the cashBookList where department equals to (departmentId + 1)
        defaultCashBookShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        cashBook.setCurrencyUom(currencyUom);
        cashBookRepository.saveAndFlush(cashBook);
        Long currencyUomId = currencyUom.getId();
        // Get all the cashBookList where currencyUom equals to currencyUomId
        defaultCashBookShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the cashBookList where currencyUom equals to (currencyUomId + 1)
        defaultCashBookShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        cashBook.setStatus(status);
        cashBookRepository.saveAndFlush(cashBook);
        Long statusId = status.getId();
        // Get all the cashBookList where status equals to statusId
        defaultCashBookShouldBeFound("statusId.equals=" + statusId);

        // Get all the cashBookList where status equals to (statusId + 1)
        defaultCashBookShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        cashBook.setOrder(order);
        cashBookRepository.saveAndFlush(cashBook);
        Long orderId = order.getId();
        // Get all the cashBookList where order equals to orderId
        defaultCashBookShouldBeFound("orderId.equals=" + orderId);

        // Get all the cashBookList where order equals to (orderId + 1)
        defaultCashBookShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByInvoiceIsEqualToSomething() throws Exception {
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            invoice = InvoiceResourceIT.createEntity(em);
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(invoice);
        em.flush();
        cashBook.setInvoice(invoice);
        cashBookRepository.saveAndFlush(cashBook);
        Long invoiceId = invoice.getId();
        // Get all the cashBookList where invoice equals to invoiceId
        defaultCashBookShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the cashBookList where invoice equals to (invoiceId + 1)
        defaultCashBookShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        cashBook.setFromParty(fromParty);
        cashBookRepository.saveAndFlush(cashBook);
        Long fromPartyId = fromParty.getId();
        // Get all the cashBookList where fromParty equals to fromPartyId
        defaultCashBookShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the cashBookList where fromParty equals to (fromPartyId + 1)
        defaultCashBookShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllCashBooksByToPartyIsEqualToSomething() throws Exception {
        Party toParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            cashBookRepository.saveAndFlush(cashBook);
            toParty = PartyResourceIT.createEntity();
        } else {
            toParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(toParty);
        em.flush();
        cashBook.setToParty(toParty);
        cashBookRepository.saveAndFlush(cashBook);
        Long toPartyId = toParty.getId();
        // Get all the cashBookList where toParty equals to toPartyId
        defaultCashBookShouldBeFound("toPartyId.equals=" + toPartyId);

        // Get all the cashBookList where toParty equals to (toPartyId + 1)
        defaultCashBookShouldNotBeFound("toPartyId.equals=" + (toPartyId + 1));
    }

    private void defaultCashBookFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCashBookShouldBeFound(shouldBeFound);
        defaultCashBookShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookShouldBeFound(String filter) throws Exception {
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vendorCustomerName").value(hasItem(DEFAULT_VENDOR_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].invoiceNo").value(hasItem(DEFAULT_INVOICE_NO)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT)))
            .andExpect(jsonPath("$.[*].usageUnits").value(hasItem(DEFAULT_USAGE_UNITS)))
            .andExpect(jsonPath("$.[*].recurringTransaction").value(hasItem(DEFAULT_RECURRING_TRANSACTION)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));

        // Check, that the count call also returns 1
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookShouldNotBeFound(String filter) throws Exception {
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCashBook() throws Exception {
        // Get the cashBook
        restCashBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCashBook() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBook
        CashBook updatedCashBook = cashBookRepository.findById(cashBook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCashBook are not directly saved in db
        em.detach(updatedCashBook);
        updatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .vendorCustomerName(UPDATED_VENDOR_CUSTOMER_NAME)
            .invoiceNo(UPDATED_INVOICE_NO)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .project(UPDATED_PROJECT)
            .usageUnits(UPDATED_USAGE_UNITS)
            .recurringTransaction(UPDATED_RECURRING_TRANSACTION)
            .frequency(UPDATED_FREQUENCY);

        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCashBookToMatchAllProperties(updatedCashBook);
    }

    @Test
    @Transactional
    void putNonExistingCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashBook.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashBookWithPatch() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBook using partial update
        CashBook partialUpdatedCashBook = new CashBook();
        partialUpdatedCashBook.setId(cashBook.getId());

        partialUpdatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .vendorCustomerName(UPDATED_VENDOR_CUSTOMER_NAME)
            .invoiceNo(UPDATED_INVOICE_NO)
            .purpose(UPDATED_PURPOSE)
            .usageUnits(UPDATED_USAGE_UNITS)
            .recurringTransaction(UPDATED_RECURRING_TRANSACTION);

        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashBookUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCashBook, cashBook), getPersistedCashBook(cashBook));
    }

    @Test
    @Transactional
    void fullUpdateCashBookWithPatch() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashBook using partial update
        CashBook partialUpdatedCashBook = new CashBook();
        partialUpdatedCashBook.setId(cashBook.getId());

        partialUpdatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .vendorCustomerName(UPDATED_VENDOR_CUSTOMER_NAME)
            .invoiceNo(UPDATED_INVOICE_NO)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .project(UPDATED_PROJECT)
            .usageUnits(UPDATED_USAGE_UNITS)
            .recurringTransaction(UPDATED_RECURRING_TRANSACTION)
            .frequency(UPDATED_FREQUENCY);

        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashBook))
            )
            .andExpect(status().isOk());

        // Validate the CashBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashBookUpdatableFieldsEquals(partialUpdatedCashBook, getPersistedCashBook(partialUpdatedCashBook));
    }

    @Test
    @Transactional
    void patchNonExistingCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashBook.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cashBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashBook() throws Exception {
        // Initialize the database
        insertedCashBook = cashBookRepository.saveAndFlush(cashBook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cashBook
        restCashBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cashBookRepository.count();
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

    protected CashBook getPersistedCashBook(CashBook cashBook) {
        return cashBookRepository.findById(cashBook.getId()).orElseThrow();
    }

    protected void assertPersistedCashBookToMatchAllProperties(CashBook expectedCashBook) {
        assertCashBookAllPropertiesEquals(expectedCashBook, getPersistedCashBook(expectedCashBook));
    }

    protected void assertPersistedCashBookToMatchUpdatableProperties(CashBook expectedCashBook) {
        assertCashBookAllUpdatablePropertiesEquals(expectedCashBook, getPersistedCashBook(expectedCashBook));
    }
}
