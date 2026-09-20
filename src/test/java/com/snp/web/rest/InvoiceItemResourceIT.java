package com.snp.web.rest;

import static com.snp.domain.InvoiceItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.Invoice;
import com.snp.domain.InvoiceItem;
import com.snp.domain.InvoiceItem;
import com.snp.domain.InvoiceItemType;
import com.snp.domain.Product;
import com.snp.domain.ProductFacility;
import com.snp.domain.ProductFeature;
import com.snp.domain.TaxAuthorityRateType;
import com.snp.domain.Uom;
import com.snp.repository.InvoiceItemRepository;
import com.snp.service.InvoiceItemService;
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
 * Integration tests for the {@link InvoiceItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

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

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemRepository invoiceItemRepositoryMock;

    @Mock
    private InvoiceItemService invoiceItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    private InvoiceItem insertedInvoiceItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .amount(DEFAULT_AMOUNT)
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
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        invoiceItem.setInvoice(invoice);
        return invoiceItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createUpdatedEntity(EntityManager em) {
        InvoiceItem updatedInvoiceItem = new InvoiceItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
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
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createUpdatedEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        updatedInvoiceItem.setInvoice(invoice);
        return updatedInvoiceItem;
    }

    @BeforeEach
    void initTest() {
        invoiceItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceItem != null) {
            invoiceItemRepository.delete(insertedInvoiceItem);
            insertedInvoiceItem = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceItem
        var returnedInvoiceItem = om.readValue(
            restInvoiceItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceItem.class
        );

        // Validate the InvoiceItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceItemUpdatableFieldsEquals(returnedInvoiceItem, getPersistedInvoiceItem(returnedInvoiceItem));

        insertedInvoiceItem = returnedInvoiceItem;
    }

    @Test
    @Transactional
    void createInvoiceItemWithExistingId() throws Exception {
        // Create the InvoiceItem with an existing ID
        invoiceItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItem.setQuantity(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
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
    void getAllInvoiceItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
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
    void getInvoiceItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        Long id = invoiceItem.getId();

        defaultInvoiceItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo equals to
        defaultInvoiceItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo in
        defaultInvoiceItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo is not null
        defaultInvoiceItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo is greater than or equal to
        defaultInvoiceItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo is less than or equal to
        defaultInvoiceItemFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo is less than
        defaultInvoiceItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where sequenceNo is greater than
        defaultInvoiceItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity equals to
        defaultInvoiceItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity in
        defaultInvoiceItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity is not null
        defaultInvoiceItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity is greater than or equal to
        defaultInvoiceItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity is less than or equal to
        defaultInvoiceItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity is less than
        defaultInvoiceItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where quantity is greater than
        defaultInvoiceItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount equals to
        defaultInvoiceItemFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount in
        defaultInvoiceItemFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount is not null
        defaultInvoiceItemFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount is greater than or equal to
        defaultInvoiceItemFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount is less than or equal to
        defaultInvoiceItemFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount is less than
        defaultInvoiceItemFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where amount is greater than
        defaultInvoiceItemFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity equals to
        defaultInvoiceItemFiltering("cancelQuantity.equals=" + DEFAULT_CANCEL_QUANTITY, "cancelQuantity.equals=" + UPDATED_CANCEL_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity in
        defaultInvoiceItemFiltering(
            "cancelQuantity.in=" + DEFAULT_CANCEL_QUANTITY + "," + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.in=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity is not null
        defaultInvoiceItemFiltering("cancelQuantity.specified=true", "cancelQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity is greater than or equal to
        defaultInvoiceItemFiltering(
            "cancelQuantity.greaterThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.greaterThanOrEqual=" + UPDATED_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity is less than or equal to
        defaultInvoiceItemFiltering(
            "cancelQuantity.lessThanOrEqual=" + DEFAULT_CANCEL_QUANTITY,
            "cancelQuantity.lessThanOrEqual=" + SMALLER_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity is less than
        defaultInvoiceItemFiltering(
            "cancelQuantity.lessThan=" + UPDATED_CANCEL_QUANTITY,
            "cancelQuantity.lessThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByCancelQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where cancelQuantity is greater than
        defaultInvoiceItemFiltering(
            "cancelQuantity.greaterThan=" + SMALLER_CANCEL_QUANTITY,
            "cancelQuantity.greaterThan=" + DEFAULT_CANCEL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount equals to
        defaultInvoiceItemFiltering("selectedAmount.equals=" + DEFAULT_SELECTED_AMOUNT, "selectedAmount.equals=" + UPDATED_SELECTED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount in
        defaultInvoiceItemFiltering(
            "selectedAmount.in=" + DEFAULT_SELECTED_AMOUNT + "," + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.in=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount is not null
        defaultInvoiceItemFiltering("selectedAmount.specified=true", "selectedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount is greater than or equal to
        defaultInvoiceItemFiltering(
            "selectedAmount.greaterThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.greaterThanOrEqual=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount is less than or equal to
        defaultInvoiceItemFiltering(
            "selectedAmount.lessThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.lessThanOrEqual=" + SMALLER_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount is less than
        defaultInvoiceItemFiltering(
            "selectedAmount.lessThan=" + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.lessThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsBySelectedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where selectedAmount is greater than
        defaultInvoiceItemFiltering(
            "selectedAmount.greaterThan=" + SMALLER_SELECTED_AMOUNT,
            "selectedAmount.greaterThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice equals to
        defaultInvoiceItemFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice in
        defaultInvoiceItemFiltering("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE, "unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice is not null
        defaultInvoiceItemFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice is greater than or equal to
        defaultInvoiceItemFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice is less than or equal to
        defaultInvoiceItemFiltering("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE, "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice is less than
        defaultInvoiceItemFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitPrice is greater than
        defaultInvoiceItemFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice equals to
        defaultInvoiceItemFiltering("unitListPrice.equals=" + DEFAULT_UNIT_LIST_PRICE, "unitListPrice.equals=" + UPDATED_UNIT_LIST_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice in
        defaultInvoiceItemFiltering(
            "unitListPrice.in=" + DEFAULT_UNIT_LIST_PRICE + "," + UPDATED_UNIT_LIST_PRICE,
            "unitListPrice.in=" + UPDATED_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice is not null
        defaultInvoiceItemFiltering("unitListPrice.specified=true", "unitListPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice is greater than or equal to
        defaultInvoiceItemFiltering(
            "unitListPrice.greaterThanOrEqual=" + DEFAULT_UNIT_LIST_PRICE,
            "unitListPrice.greaterThanOrEqual=" + UPDATED_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice is less than or equal to
        defaultInvoiceItemFiltering(
            "unitListPrice.lessThanOrEqual=" + DEFAULT_UNIT_LIST_PRICE,
            "unitListPrice.lessThanOrEqual=" + SMALLER_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice is less than
        defaultInvoiceItemFiltering(
            "unitListPrice.lessThan=" + UPDATED_UNIT_LIST_PRICE,
            "unitListPrice.lessThan=" + DEFAULT_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUnitListPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where unitListPrice is greater than
        defaultInvoiceItemFiltering(
            "unitListPrice.greaterThan=" + SMALLER_UNIT_LIST_PRICE,
            "unitListPrice.greaterThan=" + DEFAULT_UNIT_LIST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByIsModifiedPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where isModifiedPrice equals to
        defaultInvoiceItemFiltering(
            "isModifiedPrice.equals=" + DEFAULT_IS_MODIFIED_PRICE,
            "isModifiedPrice.equals=" + UPDATED_IS_MODIFIED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByIsModifiedPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where isModifiedPrice in
        defaultInvoiceItemFiltering(
            "isModifiedPrice.in=" + DEFAULT_IS_MODIFIED_PRICE + "," + UPDATED_IS_MODIFIED_PRICE,
            "isModifiedPrice.in=" + UPDATED_IS_MODIFIED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByIsModifiedPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where isModifiedPrice is not null
        defaultInvoiceItemFiltering("isModifiedPrice.specified=true", "isModifiedPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedShipDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedShipDate equals to
        defaultInvoiceItemFiltering(
            "estimatedShipDate.equals=" + DEFAULT_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.equals=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedShipDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedShipDate in
        defaultInvoiceItemFiltering(
            "estimatedShipDate.in=" + DEFAULT_ESTIMATED_SHIP_DATE + "," + UPDATED_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.in=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedShipDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedShipDate is not null
        defaultInvoiceItemFiltering("estimatedShipDate.specified=true", "estimatedShipDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedDeliveryDate equals to
        defaultInvoiceItemFiltering(
            "estimatedDeliveryDate.equals=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.equals=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedDeliveryDate in
        defaultInvoiceItemFiltering(
            "estimatedDeliveryDate.in=" + DEFAULT_ESTIMATED_DELIVERY_DATE + "," + UPDATED_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.in=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEstimatedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where estimatedDeliveryDate is not null
        defaultInvoiceItemFiltering("estimatedDeliveryDate.specified=true", "estimatedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipBeforeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipBeforeDate equals to
        defaultInvoiceItemFiltering(
            "shipBeforeDate.equals=" + DEFAULT_SHIP_BEFORE_DATE,
            "shipBeforeDate.equals=" + UPDATED_SHIP_BEFORE_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipBeforeDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipBeforeDate in
        defaultInvoiceItemFiltering(
            "shipBeforeDate.in=" + DEFAULT_SHIP_BEFORE_DATE + "," + UPDATED_SHIP_BEFORE_DATE,
            "shipBeforeDate.in=" + UPDATED_SHIP_BEFORE_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipBeforeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipBeforeDate is not null
        defaultInvoiceItemFiltering("shipBeforeDate.specified=true", "shipBeforeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipAfterDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipAfterDate equals to
        defaultInvoiceItemFiltering("shipAfterDate.equals=" + DEFAULT_SHIP_AFTER_DATE, "shipAfterDate.equals=" + UPDATED_SHIP_AFTER_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipAfterDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipAfterDate in
        defaultInvoiceItemFiltering(
            "shipAfterDate.in=" + DEFAULT_SHIP_AFTER_DATE + "," + UPDATED_SHIP_AFTER_DATE,
            "shipAfterDate.in=" + UPDATED_SHIP_AFTER_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByShipAfterDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where shipAfterDate is not null
        defaultInvoiceItemFiltering("shipAfterDate.specified=true", "shipAfterDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where entryDate equals to
        defaultInvoiceItemFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where entryDate in
        defaultInvoiceItemFiltering("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE, "entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where entryDate is not null
        defaultInvoiceItemFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr1 equals to
        defaultInvoiceItemFiltering("attr1.equals=" + DEFAULT_ATTR_1, "attr1.equals=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr1 in
        defaultInvoiceItemFiltering("attr1.in=" + DEFAULT_ATTR_1 + "," + UPDATED_ATTR_1, "attr1.in=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr1 is not null
        defaultInvoiceItemFiltering("attr1.specified=true", "attr1.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr1ContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr1 contains
        defaultInvoiceItemFiltering("attr1.contains=" + DEFAULT_ATTR_1, "attr1.contains=" + UPDATED_ATTR_1);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr1 does not contain
        defaultInvoiceItemFiltering("attr1.doesNotContain=" + UPDATED_ATTR_1, "attr1.doesNotContain=" + DEFAULT_ATTR_1);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr2 equals to
        defaultInvoiceItemFiltering("attr2.equals=" + DEFAULT_ATTR_2, "attr2.equals=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr2 in
        defaultInvoiceItemFiltering("attr2.in=" + DEFAULT_ATTR_2 + "," + UPDATED_ATTR_2, "attr2.in=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr2 is not null
        defaultInvoiceItemFiltering("attr2.specified=true", "attr2.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr2ContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr2 contains
        defaultInvoiceItemFiltering("attr2.contains=" + DEFAULT_ATTR_2, "attr2.contains=" + UPDATED_ATTR_2);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr2 does not contain
        defaultInvoiceItemFiltering("attr2.doesNotContain=" + UPDATED_ATTR_2, "attr2.doesNotContain=" + DEFAULT_ATTR_2);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr3 equals to
        defaultInvoiceItemFiltering("attr3.equals=" + DEFAULT_ATTR_3, "attr3.equals=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr3 in
        defaultInvoiceItemFiltering("attr3.in=" + DEFAULT_ATTR_3 + "," + UPDATED_ATTR_3, "attr3.in=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr3 is not null
        defaultInvoiceItemFiltering("attr3.specified=true", "attr3.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr3ContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr3 contains
        defaultInvoiceItemFiltering("attr3.contains=" + DEFAULT_ATTR_3, "attr3.contains=" + UPDATED_ATTR_3);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByAttr3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where attr3 does not contain
        defaultInvoiceItemFiltering("attr3.doesNotContain=" + UPDATED_ATTR_3, "attr3.doesNotContain=" + DEFAULT_ATTR_3);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByInvoiceIsEqualToSomething() throws Exception {
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            invoice = InvoiceResourceIT.createEntity(em);
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(invoice);
        em.flush();
        invoiceItem.setInvoice(invoice);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long invoiceId = invoice.getId();
        // Get all the invoiceItemList where invoice equals to invoiceId
        defaultInvoiceItemShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the invoiceItemList where invoice equals to (invoiceId + 1)
        defaultInvoiceItemShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByInvoiceItemTypeIsEqualToSomething() throws Exception {
        InvoiceItemType invoiceItemType;
        if (TestUtil.findAll(em, InvoiceItemType.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            invoiceItemType = InvoiceItemTypeResourceIT.createEntity();
        } else {
            invoiceItemType = TestUtil.findAll(em, InvoiceItemType.class).getFirst();
        }
        em.persist(invoiceItemType);
        em.flush();
        invoiceItem.setInvoiceItemType(invoiceItemType);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long invoiceItemTypeId = invoiceItemType.getId();
        // Get all the invoiceItemList where invoiceItemType equals to invoiceItemTypeId
        defaultInvoiceItemShouldBeFound("invoiceItemTypeId.equals=" + invoiceItemTypeId);

        // Get all the invoiceItemList where invoiceItemType equals to (invoiceItemTypeId + 1)
        defaultInvoiceItemShouldNotBeFound("invoiceItemTypeId.equals=" + (invoiceItemTypeId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        invoiceItem.setProduct(product);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long productId = product.getId();
        // Get all the invoiceItemList where product equals to productId
        defaultInvoiceItemShouldBeFound("productId.equals=" + productId);

        // Get all the invoiceItemList where product equals to (productId + 1)
        defaultInvoiceItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        invoiceItem.setUom(uom);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long uomId = uom.getId();
        // Get all the invoiceItemList where uom equals to uomId
        defaultInvoiceItemShouldBeFound("uomId.equals=" + uomId);

        // Get all the invoiceItemList where uom equals to (uomId + 1)
        defaultInvoiceItemShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        invoiceItem.setProductFeature(productFeature);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long productFeatureId = productFeature.getId();
        // Get all the invoiceItemList where productFeature equals to productFeatureId
        defaultInvoiceItemShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the invoiceItemList where productFeature equals to (productFeatureId + 1)
        defaultInvoiceItemShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByProductFacilityIsEqualToSomething() throws Exception {
        ProductFacility productFacility;
        if (TestUtil.findAll(em, ProductFacility.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            productFacility = ProductFacilityResourceIT.createEntity(em);
        } else {
            productFacility = TestUtil.findAll(em, ProductFacility.class).getFirst();
        }
        em.persist(productFacility);
        em.flush();
        invoiceItem.setProductFacility(productFacility);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long productFacilityId = productFacility.getId();
        // Get all the invoiceItemList where productFacility equals to productFacilityId
        defaultInvoiceItemShouldBeFound("productFacilityId.equals=" + productFacilityId);

        // Get all the invoiceItemList where productFacility equals to (productFacilityId + 1)
        defaultInvoiceItemShouldNotBeFound("productFacilityId.equals=" + (productFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByFromInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem fromInventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            fromInventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            fromInventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(fromInventoryItem);
        em.flush();
        invoiceItem.setFromInventoryItem(fromInventoryItem);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long fromInventoryItemId = fromInventoryItem.getId();
        // Get all the invoiceItemList where fromInventoryItem equals to fromInventoryItemId
        defaultInvoiceItemShouldBeFound("fromInventoryItemId.equals=" + fromInventoryItemId);

        // Get all the invoiceItemList where fromInventoryItem equals to (fromInventoryItemId + 1)
        defaultInvoiceItemShouldNotBeFound("fromInventoryItemId.equals=" + (fromInventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByTaxAuthRateIsEqualToSomething() throws Exception {
        TaxAuthorityRateType taxAuthRate;
        if (TestUtil.findAll(em, TaxAuthorityRateType.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            taxAuthRate = TaxAuthorityRateTypeResourceIT.createEntity(em);
        } else {
            taxAuthRate = TestUtil.findAll(em, TaxAuthorityRateType.class).getFirst();
        }
        em.persist(taxAuthRate);
        em.flush();
        invoiceItem.setTaxAuthRate(taxAuthRate);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long taxAuthRateId = taxAuthRate.getId();
        // Get all the invoiceItemList where taxAuthRate equals to taxAuthRateId
        defaultInvoiceItemShouldBeFound("taxAuthRateId.equals=" + taxAuthRateId);

        // Get all the invoiceItemList where taxAuthRate equals to (taxAuthRateId + 1)
        defaultInvoiceItemShouldNotBeFound("taxAuthRateId.equals=" + (taxAuthRateId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByParentInvoiceIsEqualToSomething() throws Exception {
        Invoice parentInvoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            parentInvoice = InvoiceResourceIT.createEntity(em);
        } else {
            parentInvoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(parentInvoice);
        em.flush();
        invoiceItem.setParentInvoice(parentInvoice);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long parentInvoiceId = parentInvoice.getId();
        // Get all the invoiceItemList where parentInvoice equals to parentInvoiceId
        defaultInvoiceItemShouldBeFound("parentInvoiceId.equals=" + parentInvoiceId);

        // Get all the invoiceItemList where parentInvoice equals to (parentInvoiceId + 1)
        defaultInvoiceItemShouldNotBeFound("parentInvoiceId.equals=" + (parentInvoiceId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByParentInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem parentInvoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            invoiceItemRepository.saveAndFlush(invoiceItem);
            parentInvoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            parentInvoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(parentInvoiceItem);
        em.flush();
        invoiceItem.setParentInvoiceItem(parentInvoiceItem);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long parentInvoiceItemId = parentInvoiceItem.getId();
        // Get all the invoiceItemList where parentInvoiceItem equals to parentInvoiceItemId
        defaultInvoiceItemShouldBeFound("parentInvoiceItemId.equals=" + parentInvoiceItemId);

        // Get all the invoiceItemList where parentInvoiceItem equals to (parentInvoiceItemId + 1)
        defaultInvoiceItemShouldNotBeFound("parentInvoiceItemId.equals=" + (parentInvoiceItemId + 1));
    }

    private void defaultInvoiceItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceItemShouldBeFound(shouldBeFound);
        defaultInvoiceItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceItemShouldBeFound(String filter) throws Exception {
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
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
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceItemShouldNotBeFound(String filter) throws Exception {
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItem are not directly saved in db
        em.detach(updatedInvoiceItem);
        updatedInvoiceItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
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

        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceItemToMatchAllProperties(updatedInvoiceItem);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .amount(UPDATED_AMOUNT)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitListPrice(UPDATED_UNIT_LIST_PRICE)
            .isModifiedPrice(UPDATED_IS_MODIFIED_PRICE)
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .shipBeforeDate(UPDATED_SHIP_BEFORE_DATE)
            .shipAfterDate(UPDATED_SHIP_AFTER_DATE)
            .attr1(UPDATED_ATTR_1)
            .attr2(UPDATED_ATTR_2);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceItem, invoiceItem),
            getPersistedInvoiceItem(invoiceItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
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

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemUpdatableFieldsEquals(partialUpdatedInvoiceItem, getPersistedInvoiceItem(partialUpdatedInvoiceItem));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItem() throws Exception {
        // Initialize the database
        insertedInvoiceItem = invoiceItemRepository.saveAndFlush(invoiceItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceItem
        restInvoiceItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceItemRepository.count();
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

    protected InvoiceItem getPersistedInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepository.findById(invoiceItem.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceItemToMatchAllProperties(InvoiceItem expectedInvoiceItem) {
        assertInvoiceItemAllPropertiesEquals(expectedInvoiceItem, getPersistedInvoiceItem(expectedInvoiceItem));
    }

    protected void assertPersistedInvoiceItemToMatchUpdatableProperties(InvoiceItem expectedInvoiceItem) {
        assertInvoiceItemAllUpdatablePropertiesEquals(expectedInvoiceItem, getPersistedInvoiceItem(expectedInvoiceItem));
    }
}
