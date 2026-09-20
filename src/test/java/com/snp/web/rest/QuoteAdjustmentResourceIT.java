package com.snp.web.rest;

import static com.snp.domain.QuoteAdjustmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderAdjustmentType;
import com.snp.domain.Product;
import com.snp.domain.ProductFeature;
import com.snp.domain.Quote;
import com.snp.domain.QuoteAdjustment;
import com.snp.domain.QuoteItem;
import com.snp.domain.TaxAuthority;
import com.snp.repository.QuoteAdjustmentRepository;
import com.snp.service.QuoteAdjustmentService;
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
 * Integration tests for the {@link QuoteAdjustmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuoteAdjustmentResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SOURCE_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SOURCE_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SOURCE_PERCENTAGE = new BigDecimal(1 - 1);

    private static final String DEFAULT_SOURCE_REF_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_REF_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXEMPT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXEMPT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXEMPT_AMOUNT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_INCLUDE_IN_TAX = false;
    private static final Boolean UPDATED_INCLUDE_IN_TAX = true;

    private static final Boolean DEFAULT_INCLUDE_IN_SHIPPING = false;
    private static final Boolean UPDATED_INCLUDE_IN_SHIPPING = true;

    private static final Boolean DEFAULT_INCLUDED_IN_PRICE = false;
    private static final Boolean UPDATED_INCLUDED_IN_PRICE = true;

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/quote-adjustments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteAdjustmentRepository quoteAdjustmentRepository;

    @Mock
    private QuoteAdjustmentRepository quoteAdjustmentRepositoryMock;

    @Mock
    private QuoteAdjustmentService quoteAdjustmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteAdjustmentMockMvc;

    private QuoteAdjustment quoteAdjustment;

    private QuoteAdjustment insertedQuoteAdjustment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteAdjustment createEntity() {
        return new QuoteAdjustment()
            .description(DEFAULT_DESCRIPTION)
            .comments(DEFAULT_COMMENTS)
            .amount(DEFAULT_AMOUNT)
            .sourcePercentage(DEFAULT_SOURCE_PERCENTAGE)
            .sourceRefId(DEFAULT_SOURCE_REF_ID)
            .exemptAmount(DEFAULT_EXEMPT_AMOUNT)
            .includeInTax(DEFAULT_INCLUDE_IN_TAX)
            .includeInShipping(DEFAULT_INCLUDE_IN_SHIPPING)
            .includedInPrice(DEFAULT_INCLUDED_IN_PRICE)
            .entryDate(DEFAULT_ENTRY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteAdjustment createUpdatedEntity() {
        return new QuoteAdjustment()
            .description(UPDATED_DESCRIPTION)
            .comments(UPDATED_COMMENTS)
            .amount(UPDATED_AMOUNT)
            .sourcePercentage(UPDATED_SOURCE_PERCENTAGE)
            .sourceRefId(UPDATED_SOURCE_REF_ID)
            .exemptAmount(UPDATED_EXEMPT_AMOUNT)
            .includeInTax(UPDATED_INCLUDE_IN_TAX)
            .includeInShipping(UPDATED_INCLUDE_IN_SHIPPING)
            .includedInPrice(UPDATED_INCLUDED_IN_PRICE)
            .entryDate(UPDATED_ENTRY_DATE);
    }

    @BeforeEach
    void initTest() {
        quoteAdjustment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteAdjustment != null) {
            quoteAdjustmentRepository.delete(insertedQuoteAdjustment);
            insertedQuoteAdjustment = null;
        }
    }

    @Test
    @Transactional
    void createQuoteAdjustment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteAdjustment
        var returnedQuoteAdjustment = om.readValue(
            restQuoteAdjustmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAdjustment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteAdjustment.class
        );

        // Validate the QuoteAdjustment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteAdjustmentUpdatableFieldsEquals(returnedQuoteAdjustment, getPersistedQuoteAdjustment(returnedQuoteAdjustment));

        insertedQuoteAdjustment = returnedQuoteAdjustment;
    }

    @Test
    @Transactional
    void createQuoteAdjustmentWithExistingId() throws Exception {
        // Create the QuoteAdjustment with an existing ID
        quoteAdjustment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteAdjustmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAdjustment)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustments() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].sourcePercentage").value(hasItem(sameNumber(DEFAULT_SOURCE_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].sourceRefId").value(hasItem(DEFAULT_SOURCE_REF_ID)))
            .andExpect(jsonPath("$.[*].exemptAmount").value(hasItem(sameNumber(DEFAULT_EXEMPT_AMOUNT))))
            .andExpect(jsonPath("$.[*].includeInTax").value(hasItem(DEFAULT_INCLUDE_IN_TAX)))
            .andExpect(jsonPath("$.[*].includeInShipping").value(hasItem(DEFAULT_INCLUDE_IN_SHIPPING)))
            .andExpect(jsonPath("$.[*].includedInPrice").value(hasItem(DEFAULT_INCLUDED_IN_PRICE)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteAdjustmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quoteAdjustmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteAdjustmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quoteAdjustmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteAdjustmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quoteAdjustmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteAdjustmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quoteAdjustmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuoteAdjustment() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get the quoteAdjustment
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteAdjustment.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.sourcePercentage").value(sameNumber(DEFAULT_SOURCE_PERCENTAGE)))
            .andExpect(jsonPath("$.sourceRefId").value(DEFAULT_SOURCE_REF_ID))
            .andExpect(jsonPath("$.exemptAmount").value(sameNumber(DEFAULT_EXEMPT_AMOUNT)))
            .andExpect(jsonPath("$.includeInTax").value(DEFAULT_INCLUDE_IN_TAX))
            .andExpect(jsonPath("$.includeInShipping").value(DEFAULT_INCLUDE_IN_SHIPPING))
            .andExpect(jsonPath("$.includedInPrice").value(DEFAULT_INCLUDED_IN_PRICE))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getQuoteAdjustmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        Long id = quoteAdjustment.getId();

        defaultQuoteAdjustmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteAdjustmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteAdjustmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where description equals to
        defaultQuoteAdjustmentFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where description in
        defaultQuoteAdjustmentFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where description is not null
        defaultQuoteAdjustmentFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where description contains
        defaultQuoteAdjustmentFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where description does not contain
        defaultQuoteAdjustmentFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where comments equals to
        defaultQuoteAdjustmentFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where comments in
        defaultQuoteAdjustmentFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where comments is not null
        defaultQuoteAdjustmentFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where comments contains
        defaultQuoteAdjustmentFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where comments does not contain
        defaultQuoteAdjustmentFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount equals to
        defaultQuoteAdjustmentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount in
        defaultQuoteAdjustmentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount is not null
        defaultQuoteAdjustmentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount is greater than or equal to
        defaultQuoteAdjustmentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount is less than or equal to
        defaultQuoteAdjustmentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount is less than
        defaultQuoteAdjustmentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where amount is greater than
        defaultQuoteAdjustmentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage equals to
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.equals=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.equals=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage in
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.in=" + DEFAULT_SOURCE_PERCENTAGE + "," + UPDATED_SOURCE_PERCENTAGE,
            "sourcePercentage.in=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage is not null
        defaultQuoteAdjustmentFiltering("sourcePercentage.specified=true", "sourcePercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage is greater than or equal to
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.greaterThanOrEqual=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.greaterThanOrEqual=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage is less than or equal to
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.lessThanOrEqual=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.lessThanOrEqual=" + SMALLER_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage is less than
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.lessThan=" + UPDATED_SOURCE_PERCENTAGE,
            "sourcePercentage.lessThan=" + DEFAULT_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourcePercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourcePercentage is greater than
        defaultQuoteAdjustmentFiltering(
            "sourcePercentage.greaterThan=" + SMALLER_SOURCE_PERCENTAGE,
            "sourcePercentage.greaterThan=" + DEFAULT_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourceRefIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourceRefId equals to
        defaultQuoteAdjustmentFiltering("sourceRefId.equals=" + DEFAULT_SOURCE_REF_ID, "sourceRefId.equals=" + UPDATED_SOURCE_REF_ID);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourceRefIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourceRefId in
        defaultQuoteAdjustmentFiltering(
            "sourceRefId.in=" + DEFAULT_SOURCE_REF_ID + "," + UPDATED_SOURCE_REF_ID,
            "sourceRefId.in=" + UPDATED_SOURCE_REF_ID
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourceRefIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourceRefId is not null
        defaultQuoteAdjustmentFiltering("sourceRefId.specified=true", "sourceRefId.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourceRefIdContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourceRefId contains
        defaultQuoteAdjustmentFiltering("sourceRefId.contains=" + DEFAULT_SOURCE_REF_ID, "sourceRefId.contains=" + UPDATED_SOURCE_REF_ID);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsBySourceRefIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where sourceRefId does not contain
        defaultQuoteAdjustmentFiltering(
            "sourceRefId.doesNotContain=" + UPDATED_SOURCE_REF_ID,
            "sourceRefId.doesNotContain=" + DEFAULT_SOURCE_REF_ID
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount equals to
        defaultQuoteAdjustmentFiltering("exemptAmount.equals=" + DEFAULT_EXEMPT_AMOUNT, "exemptAmount.equals=" + UPDATED_EXEMPT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount in
        defaultQuoteAdjustmentFiltering(
            "exemptAmount.in=" + DEFAULT_EXEMPT_AMOUNT + "," + UPDATED_EXEMPT_AMOUNT,
            "exemptAmount.in=" + UPDATED_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount is not null
        defaultQuoteAdjustmentFiltering("exemptAmount.specified=true", "exemptAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount is greater than or equal to
        defaultQuoteAdjustmentFiltering(
            "exemptAmount.greaterThanOrEqual=" + DEFAULT_EXEMPT_AMOUNT,
            "exemptAmount.greaterThanOrEqual=" + UPDATED_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount is less than or equal to
        defaultQuoteAdjustmentFiltering(
            "exemptAmount.lessThanOrEqual=" + DEFAULT_EXEMPT_AMOUNT,
            "exemptAmount.lessThanOrEqual=" + SMALLER_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount is less than
        defaultQuoteAdjustmentFiltering("exemptAmount.lessThan=" + UPDATED_EXEMPT_AMOUNT, "exemptAmount.lessThan=" + DEFAULT_EXEMPT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByExemptAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where exemptAmount is greater than
        defaultQuoteAdjustmentFiltering(
            "exemptAmount.greaterThan=" + SMALLER_EXEMPT_AMOUNT,
            "exemptAmount.greaterThan=" + DEFAULT_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInTax equals to
        defaultQuoteAdjustmentFiltering("includeInTax.equals=" + DEFAULT_INCLUDE_IN_TAX, "includeInTax.equals=" + UPDATED_INCLUDE_IN_TAX);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInTax in
        defaultQuoteAdjustmentFiltering(
            "includeInTax.in=" + DEFAULT_INCLUDE_IN_TAX + "," + UPDATED_INCLUDE_IN_TAX,
            "includeInTax.in=" + UPDATED_INCLUDE_IN_TAX
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInTax is not null
        defaultQuoteAdjustmentFiltering("includeInTax.specified=true", "includeInTax.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInShippingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInShipping equals to
        defaultQuoteAdjustmentFiltering(
            "includeInShipping.equals=" + DEFAULT_INCLUDE_IN_SHIPPING,
            "includeInShipping.equals=" + UPDATED_INCLUDE_IN_SHIPPING
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInShippingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInShipping in
        defaultQuoteAdjustmentFiltering(
            "includeInShipping.in=" + DEFAULT_INCLUDE_IN_SHIPPING + "," + UPDATED_INCLUDE_IN_SHIPPING,
            "includeInShipping.in=" + UPDATED_INCLUDE_IN_SHIPPING
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludeInShippingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includeInShipping is not null
        defaultQuoteAdjustmentFiltering("includeInShipping.specified=true", "includeInShipping.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludedInPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includedInPrice equals to
        defaultQuoteAdjustmentFiltering(
            "includedInPrice.equals=" + DEFAULT_INCLUDED_IN_PRICE,
            "includedInPrice.equals=" + UPDATED_INCLUDED_IN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludedInPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includedInPrice in
        defaultQuoteAdjustmentFiltering(
            "includedInPrice.in=" + DEFAULT_INCLUDED_IN_PRICE + "," + UPDATED_INCLUDED_IN_PRICE,
            "includedInPrice.in=" + UPDATED_INCLUDED_IN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByIncludedInPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where includedInPrice is not null
        defaultQuoteAdjustmentFiltering("includedInPrice.specified=true", "includedInPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where entryDate equals to
        defaultQuoteAdjustmentFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where entryDate in
        defaultQuoteAdjustmentFiltering(
            "entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE,
            "entryDate.in=" + UPDATED_ENTRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        // Get all the quoteAdjustmentList where entryDate is not null
        defaultQuoteAdjustmentFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByQuoteIsEqualToSomething() throws Exception {
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            quote = QuoteResourceIT.createEntity(em);
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        em.persist(quote);
        em.flush();
        quoteAdjustment.setQuote(quote);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long quoteId = quote.getId();
        // Get all the quoteAdjustmentList where quote equals to quoteId
        defaultQuoteAdjustmentShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the quoteAdjustmentList where quote equals to (quoteId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByQuoteItemIsEqualToSomething() throws Exception {
        QuoteItem quoteItem;
        if (TestUtil.findAll(em, QuoteItem.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            quoteItem = QuoteItemResourceIT.createEntity(em);
        } else {
            quoteItem = TestUtil.findAll(em, QuoteItem.class).getFirst();
        }
        em.persist(quoteItem);
        em.flush();
        quoteAdjustment.setQuoteItem(quoteItem);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long quoteItemId = quoteItem.getId();
        // Get all the quoteAdjustmentList where quoteItem equals to quoteItemId
        defaultQuoteAdjustmentShouldBeFound("quoteItemId.equals=" + quoteItemId);

        // Get all the quoteAdjustmentList where quoteItem equals to (quoteItemId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("quoteItemId.equals=" + (quoteItemId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByAdjustmentTypeIsEqualToSomething() throws Exception {
        OrderAdjustmentType adjustmentType;
        if (TestUtil.findAll(em, OrderAdjustmentType.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            adjustmentType = OrderAdjustmentTypeResourceIT.createEntity();
        } else {
            adjustmentType = TestUtil.findAll(em, OrderAdjustmentType.class).getFirst();
        }
        em.persist(adjustmentType);
        em.flush();
        quoteAdjustment.setAdjustmentType(adjustmentType);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long adjustmentTypeId = adjustmentType.getId();
        // Get all the quoteAdjustmentList where adjustmentType equals to adjustmentTypeId
        defaultQuoteAdjustmentShouldBeFound("adjustmentTypeId.equals=" + adjustmentTypeId);

        // Get all the quoteAdjustmentList where adjustmentType equals to (adjustmentTypeId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("adjustmentTypeId.equals=" + (adjustmentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        quoteAdjustment.setProduct(product);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long productId = product.getId();
        // Get all the quoteAdjustmentList where product equals to productId
        defaultQuoteAdjustmentShouldBeFound("productId.equals=" + productId);

        // Get all the quoteAdjustmentList where product equals to (productId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        quoteAdjustment.setProductFeature(productFeature);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long productFeatureId = productFeature.getId();
        // Get all the quoteAdjustmentList where productFeature equals to productFeatureId
        defaultQuoteAdjustmentShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the quoteAdjustmentList where productFeature equals to (productFeatureId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteAdjustmentsByTaxAuthorityIsEqualToSomething() throws Exception {
        TaxAuthority taxAuthority;
        if (TestUtil.findAll(em, TaxAuthority.class).isEmpty()) {
            quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
            taxAuthority = TaxAuthorityResourceIT.createEntity();
        } else {
            taxAuthority = TestUtil.findAll(em, TaxAuthority.class).getFirst();
        }
        em.persist(taxAuthority);
        em.flush();
        quoteAdjustment.setTaxAuthority(taxAuthority);
        quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);
        Long taxAuthorityId = taxAuthority.getId();
        // Get all the quoteAdjustmentList where taxAuthority equals to taxAuthorityId
        defaultQuoteAdjustmentShouldBeFound("taxAuthorityId.equals=" + taxAuthorityId);

        // Get all the quoteAdjustmentList where taxAuthority equals to (taxAuthorityId + 1)
        defaultQuoteAdjustmentShouldNotBeFound("taxAuthorityId.equals=" + (taxAuthorityId + 1));
    }

    private void defaultQuoteAdjustmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteAdjustmentShouldBeFound(shouldBeFound);
        defaultQuoteAdjustmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteAdjustmentShouldBeFound(String filter) throws Exception {
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].sourcePercentage").value(hasItem(sameNumber(DEFAULT_SOURCE_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].sourceRefId").value(hasItem(DEFAULT_SOURCE_REF_ID)))
            .andExpect(jsonPath("$.[*].exemptAmount").value(hasItem(sameNumber(DEFAULT_EXEMPT_AMOUNT))))
            .andExpect(jsonPath("$.[*].includeInTax").value(hasItem(DEFAULT_INCLUDE_IN_TAX)))
            .andExpect(jsonPath("$.[*].includeInShipping").value(hasItem(DEFAULT_INCLUDE_IN_SHIPPING)))
            .andExpect(jsonPath("$.[*].includedInPrice").value(hasItem(DEFAULT_INCLUDED_IN_PRICE)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())));

        // Check, that the count call also returns 1
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteAdjustmentShouldNotBeFound(String filter) throws Exception {
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuoteAdjustment() throws Exception {
        // Get the quoteAdjustment
        restQuoteAdjustmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteAdjustment() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAdjustment
        QuoteAdjustment updatedQuoteAdjustment = quoteAdjustmentRepository.findById(quoteAdjustment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteAdjustment are not directly saved in db
        em.detach(updatedQuoteAdjustment);
        updatedQuoteAdjustment
            .description(UPDATED_DESCRIPTION)
            .comments(UPDATED_COMMENTS)
            .amount(UPDATED_AMOUNT)
            .sourcePercentage(UPDATED_SOURCE_PERCENTAGE)
            .sourceRefId(UPDATED_SOURCE_REF_ID)
            .exemptAmount(UPDATED_EXEMPT_AMOUNT)
            .includeInTax(UPDATED_INCLUDE_IN_TAX)
            .includeInShipping(UPDATED_INCLUDE_IN_SHIPPING)
            .includedInPrice(UPDATED_INCLUDED_IN_PRICE)
            .entryDate(UPDATED_ENTRY_DATE);

        restQuoteAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteAdjustment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteAdjustmentToMatchAllProperties(updatedQuoteAdjustment);
    }

    @Test
    @Transactional
    void putNonExistingQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteAdjustment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteAdjustment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteAdjustmentWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAdjustment using partial update
        QuoteAdjustment partialUpdatedQuoteAdjustment = new QuoteAdjustment();
        partialUpdatedQuoteAdjustment.setId(quoteAdjustment.getId());

        partialUpdatedQuoteAdjustment
            .sourceRefId(UPDATED_SOURCE_REF_ID)
            .exemptAmount(UPDATED_EXEMPT_AMOUNT)
            .includedInPrice(UPDATED_INCLUDED_IN_PRICE)
            .entryDate(UPDATED_ENTRY_DATE);

        restQuoteAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAdjustment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteAdjustmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteAdjustment, quoteAdjustment),
            getPersistedQuoteAdjustment(quoteAdjustment)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteAdjustmentWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteAdjustment using partial update
        QuoteAdjustment partialUpdatedQuoteAdjustment = new QuoteAdjustment();
        partialUpdatedQuoteAdjustment.setId(quoteAdjustment.getId());

        partialUpdatedQuoteAdjustment
            .description(UPDATED_DESCRIPTION)
            .comments(UPDATED_COMMENTS)
            .amount(UPDATED_AMOUNT)
            .sourcePercentage(UPDATED_SOURCE_PERCENTAGE)
            .sourceRefId(UPDATED_SOURCE_REF_ID)
            .exemptAmount(UPDATED_EXEMPT_AMOUNT)
            .includeInTax(UPDATED_INCLUDE_IN_TAX)
            .includeInShipping(UPDATED_INCLUDE_IN_SHIPPING)
            .includedInPrice(UPDATED_INCLUDED_IN_PRICE)
            .entryDate(UPDATED_ENTRY_DATE);

        restQuoteAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the QuoteAdjustment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteAdjustmentUpdatableFieldsEquals(
            partialUpdatedQuoteAdjustment,
            getPersistedQuoteAdjustment(partialUpdatedQuoteAdjustment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteAdjustmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteAdjustment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteAdjustment() throws Exception {
        // Initialize the database
        insertedQuoteAdjustment = quoteAdjustmentRepository.saveAndFlush(quoteAdjustment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteAdjustment
        restQuoteAdjustmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteAdjustment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteAdjustmentRepository.count();
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

    protected QuoteAdjustment getPersistedQuoteAdjustment(QuoteAdjustment quoteAdjustment) {
        return quoteAdjustmentRepository.findById(quoteAdjustment.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteAdjustmentToMatchAllProperties(QuoteAdjustment expectedQuoteAdjustment) {
        assertQuoteAdjustmentAllPropertiesEquals(expectedQuoteAdjustment, getPersistedQuoteAdjustment(expectedQuoteAdjustment));
    }

    protected void assertPersistedQuoteAdjustmentToMatchUpdatableProperties(QuoteAdjustment expectedQuoteAdjustment) {
        assertQuoteAdjustmentAllUpdatablePropertiesEquals(expectedQuoteAdjustment, getPersistedQuoteAdjustment(expectedQuoteAdjustment));
    }
}
