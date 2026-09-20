package com.snp.web.rest;

import static com.snp.domain.InvoiceAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.Invoice;
import com.snp.domain.InvoiceType;
import com.snp.domain.Order;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.InvoiceRepository;
import com.snp.service.InvoiceService;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final Instant DEFAULT_INVOICE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INVOICE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_PAID_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAID_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_GRAND_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_GRAND_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_GRAND_TOTAL = new BigDecimal(1 - 1);

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Mock
    private InvoiceService invoiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    private Invoice insertedInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .paidDate(DEFAULT_PAID_DATE)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .invoiceMessage(DEFAULT_INVOICE_MESSAGE)
            .externalId(DEFAULT_EXTERNAL_ID)
            .entryDate(DEFAULT_ENTRY_DATE)
            .grandTotal(DEFAULT_GRAND_TOTAL)
            .projectName(DEFAULT_PROJECT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .remarks(DEFAULT_REMARKS)
            .paymentTerms(DEFAULT_PAYMENT_TERMS)
            .deliveryTerms(DEFAULT_DELIVERY_TERMS)
            .info(DEFAULT_INFO);
        // Add required entity
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceType = InvoiceTypeResourceIT.createEntity();
            em.persist(invoiceType);
            em.flush();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        invoice.setInvoiceType(invoiceType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        invoice.setFromParty(party);
        // Add required entity
        invoice.setToParty(party);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice updatedInvoice = new Invoice()
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .paidDate(UPDATED_PAID_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .invoiceMessage(UPDATED_INVOICE_MESSAGE)
            .externalId(UPDATED_EXTERNAL_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO);
        // Add required entity
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceType = InvoiceTypeResourceIT.createUpdatedEntity();
            em.persist(invoiceType);
            em.flush();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        updatedInvoice.setInvoiceType(invoiceType);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedInvoice.setFromParty(party);
        // Add required entity
        updatedInvoice.setToParty(party);
        return updatedInvoice;
    }

    @BeforeEach
    void initTest() {
        invoice = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoice != null) {
            invoiceRepository.delete(insertedInvoice);
            insertedInvoice = null;
        }
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Invoice
        var returnedInvoice = om.readValue(
            restInvoiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoice)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Invoice.class
        );

        // Validate the Invoice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceUpdatableFieldsEquals(returnedInvoice, getPersistedInvoice(returnedInvoice));

        insertedInvoice = returnedInvoice;
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoice.setInvoiceDate(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoice)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceMessage").value(hasItem(DEFAULT_INVOICE_MESSAGE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(sameNumber(DEFAULT_GRAND_TOTAL))))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS)))
            .andExpect(jsonPath("$.[*].deliveryTerms").value(hasItem(DEFAULT_DELIVERY_TERMS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE.toString()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.invoiceMessage").value(DEFAULT_INVOICE_MESSAGE))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.grandTotal").value(sameNumber(DEFAULT_GRAND_TOTAL)))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.paymentTerms").value(DEFAULT_PAYMENT_TERMS))
            .andExpect(jsonPath("$.deliveryTerms").value(DEFAULT_DELIVERY_TERMS))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO));
    }

    @Test
    @Transactional
    void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate equals to
        defaultInvoiceFiltering("invoiceDate.equals=" + DEFAULT_INVOICE_DATE, "invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate in
        defaultInvoiceFiltering(
            "invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE,
            "invoiceDate.in=" + UPDATED_INVOICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is not null
        defaultInvoiceFiltering("invoiceDate.specified=true", "invoiceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueDate equals to
        defaultInvoiceFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueDate in
        defaultInvoiceFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dueDate is not null
        defaultInvoiceFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate equals to
        defaultInvoiceFiltering("paidDate.equals=" + DEFAULT_PAID_DATE, "paidDate.equals=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate in
        defaultInvoiceFiltering("paidDate.in=" + DEFAULT_PAID_DATE + "," + UPDATED_PAID_DATE, "paidDate.in=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidDate is not null
        defaultInvoiceFiltering("paidDate.specified=true", "paidDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where referenceNumber equals to
        defaultInvoiceFiltering("referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER, "referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where referenceNumber in
        defaultInvoiceFiltering(
            "referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.in=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where referenceNumber is not null
        defaultInvoiceFiltering("referenceNumber.specified=true", "referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where referenceNumber contains
        defaultInvoiceFiltering(
            "referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER,
            "referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where referenceNumber does not contain
        defaultInvoiceFiltering(
            "referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER,
            "referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceMessage equals to
        defaultInvoiceFiltering("invoiceMessage.equals=" + DEFAULT_INVOICE_MESSAGE, "invoiceMessage.equals=" + UPDATED_INVOICE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceMessage in
        defaultInvoiceFiltering(
            "invoiceMessage.in=" + DEFAULT_INVOICE_MESSAGE + "," + UPDATED_INVOICE_MESSAGE,
            "invoiceMessage.in=" + UPDATED_INVOICE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceMessage is not null
        defaultInvoiceFiltering("invoiceMessage.specified=true", "invoiceMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceMessage contains
        defaultInvoiceFiltering("invoiceMessage.contains=" + DEFAULT_INVOICE_MESSAGE, "invoiceMessage.contains=" + UPDATED_INVOICE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceMessage does not contain
        defaultInvoiceFiltering(
            "invoiceMessage.doesNotContain=" + UPDATED_INVOICE_MESSAGE,
            "invoiceMessage.doesNotContain=" + DEFAULT_INVOICE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where externalId equals to
        defaultInvoiceFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where externalId in
        defaultInvoiceFiltering("externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID, "externalId.in=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where externalId is not null
        defaultInvoiceFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where externalId contains
        defaultInvoiceFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where externalId does not contain
        defaultInvoiceFiltering("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID, "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllInvoicesByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where entryDate equals to
        defaultInvoiceFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where entryDate in
        defaultInvoiceFiltering("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE, "entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where entryDate is not null
        defaultInvoiceFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal equals to
        defaultInvoiceFiltering("grandTotal.equals=" + DEFAULT_GRAND_TOTAL, "grandTotal.equals=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal in
        defaultInvoiceFiltering("grandTotal.in=" + DEFAULT_GRAND_TOTAL + "," + UPDATED_GRAND_TOTAL, "grandTotal.in=" + UPDATED_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal is not null
        defaultInvoiceFiltering("grandTotal.specified=true", "grandTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal is greater than or equal to
        defaultInvoiceFiltering(
            "grandTotal.greaterThanOrEqual=" + DEFAULT_GRAND_TOTAL,
            "grandTotal.greaterThanOrEqual=" + UPDATED_GRAND_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal is less than or equal to
        defaultInvoiceFiltering("grandTotal.lessThanOrEqual=" + DEFAULT_GRAND_TOTAL, "grandTotal.lessThanOrEqual=" + SMALLER_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal is less than
        defaultInvoiceFiltering("grandTotal.lessThan=" + UPDATED_GRAND_TOTAL, "grandTotal.lessThan=" + DEFAULT_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByGrandTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where grandTotal is greater than
        defaultInvoiceFiltering("grandTotal.greaterThan=" + SMALLER_GRAND_TOTAL, "grandTotal.greaterThan=" + DEFAULT_GRAND_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where projectName equals to
        defaultInvoiceFiltering("projectName.equals=" + DEFAULT_PROJECT_NAME, "projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where projectName in
        defaultInvoiceFiltering(
            "projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME,
            "projectName.in=" + UPDATED_PROJECT_NAME
        );
    }

    @Test
    @Transactional
    void getAllInvoicesByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where projectName is not null
        defaultInvoiceFiltering("projectName.specified=true", "projectName.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where projectName contains
        defaultInvoiceFiltering("projectName.contains=" + DEFAULT_PROJECT_NAME, "projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where projectName does not contain
        defaultInvoiceFiltering("projectName.doesNotContain=" + UPDATED_PROJECT_NAME, "projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remarks equals to
        defaultInvoiceFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remarks in
        defaultInvoiceFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remarks is not null
        defaultInvoiceFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remarks contains
        defaultInvoiceFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllInvoicesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where remarks does not contain
        defaultInvoiceFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceTypeIsEqualToSomething() throws Exception {
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            invoiceType = InvoiceTypeResourceIT.createEntity();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        em.persist(invoiceType);
        em.flush();
        invoice.setInvoiceType(invoiceType);
        invoiceRepository.saveAndFlush(invoice);
        Long invoiceTypeId = invoiceType.getId();
        // Get all the invoiceList where invoiceType equals to invoiceTypeId
        defaultInvoiceShouldBeFound("invoiceTypeId.equals=" + invoiceTypeId);

        // Get all the invoiceList where invoiceType equals to (invoiceTypeId + 1)
        defaultInvoiceShouldNotBeFound("invoiceTypeId.equals=" + (invoiceTypeId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        invoice.setOrder(order);
        invoiceRepository.saveAndFlush(invoice);
        Long orderId = order.getId();
        // Get all the invoiceList where order equals to orderId
        defaultInvoiceShouldBeFound("orderId.equals=" + orderId);

        // Get all the invoiceList where order equals to (orderId + 1)
        defaultInvoiceShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        invoice.setFromParty(fromParty);
        invoiceRepository.saveAndFlush(invoice);
        Long fromPartyId = fromParty.getId();
        // Get all the invoiceList where fromParty equals to fromPartyId
        defaultInvoiceShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the invoiceList where fromParty equals to (fromPartyId + 1)
        defaultInvoiceShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByToPartyIsEqualToSomething() throws Exception {
        Party toParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            toParty = PartyResourceIT.createEntity();
        } else {
            toParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(toParty);
        em.flush();
        invoice.setToParty(toParty);
        invoiceRepository.saveAndFlush(invoice);
        Long toPartyId = toParty.getId();
        // Get all the invoiceList where toParty equals to toPartyId
        defaultInvoiceShouldBeFound("toPartyId.equals=" + toPartyId);

        // Get all the invoiceList where toParty equals to (toPartyId + 1)
        defaultInvoiceShouldNotBeFound("toPartyId.equals=" + (toPartyId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        invoice.setRoleType(roleType);
        invoiceRepository.saveAndFlush(invoice);
        Long roleTypeId = roleType.getId();
        // Get all the invoiceList where roleType equals to roleTypeId
        defaultInvoiceShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the invoiceList where roleType equals to (roleTypeId + 1)
        defaultInvoiceShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        invoice.setStatus(status);
        invoiceRepository.saveAndFlush(invoice);
        Long statusId = status.getId();
        // Get all the invoiceList where status equals to statusId
        defaultInvoiceShouldBeFound("statusId.equals=" + statusId);

        // Get all the invoiceList where status equals to (statusId + 1)
        defaultInvoiceShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        invoice.setContactMech(contactMech);
        invoiceRepository.saveAndFlush(invoice);
        Long contactMechId = contactMech.getId();
        // Get all the invoiceList where contactMech equals to contactMechId
        defaultInvoiceShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the invoiceList where contactMech equals to (contactMechId + 1)
        defaultInvoiceShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            invoiceRepository.saveAndFlush(invoice);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        invoice.setCurrencyUom(currencyUom);
        invoiceRepository.saveAndFlush(invoice);
        Long currencyUomId = currencyUom.getId();
        // Get all the invoiceList where currencyUom equals to currencyUomId
        defaultInvoiceShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the invoiceList where currencyUom equals to (currencyUomId + 1)
        defaultInvoiceShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultInvoiceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceShouldBeFound(shouldBeFound);
        defaultInvoiceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceMessage").value(hasItem(DEFAULT_INVOICE_MESSAGE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].grandTotal").value(hasItem(sameNumber(DEFAULT_GRAND_TOTAL))))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS)))
            .andExpect(jsonPath("$.[*].deliveryTerms").value(hasItem(DEFAULT_DELIVERY_TERMS)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)));

        // Check, that the count call also returns 1
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoice() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .paidDate(UPDATED_PAID_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .invoiceMessage(UPDATED_INVOICE_MESSAGE)
            .externalId(UPDATED_EXTERNAL_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceToMatchAllProperties(updatedInvoice);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL_ID, invoice.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .invoiceMessage(UPDATED_INVOICE_MESSAGE)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedInvoice, invoice), getPersistedInvoice(invoice));
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .invoiceDate(UPDATED_INVOICE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .paidDate(UPDATED_PAID_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .invoiceMessage(UPDATED_INVOICE_MESSAGE)
            .externalId(UPDATED_EXTERNAL_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .grandTotal(UPDATED_GRAND_TOTAL)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .deliveryTerms(UPDATED_DELIVERY_TERMS)
            .info(UPDATED_INFO);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceUpdatableFieldsEquals(partialUpdatedInvoice, getPersistedInvoice(partialUpdatedInvoice));
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        insertedInvoice = invoiceRepository.saveAndFlush(invoice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceRepository.count();
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

    protected Invoice getPersistedInvoice(Invoice invoice) {
        return invoiceRepository.findById(invoice.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceToMatchAllProperties(Invoice expectedInvoice) {
        assertInvoiceAllPropertiesEquals(expectedInvoice, getPersistedInvoice(expectedInvoice));
    }

    protected void assertPersistedInvoiceToMatchUpdatableProperties(Invoice expectedInvoice) {
        assertInvoiceAllUpdatablePropertiesEquals(expectedInvoice, getPersistedInvoice(expectedInvoice));
    }
}
