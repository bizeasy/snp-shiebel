package com.snp.web.rest;

import static com.snp.domain.OrderAdjustmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Order;
import com.snp.domain.OrderAdjustment;
import com.snp.domain.OrderAdjustmentType;
import com.snp.domain.OrderItem;
import com.snp.domain.Product;
import com.snp.domain.ProductFeature;
import com.snp.domain.TaxAuthority;
import com.snp.repository.OrderAdjustmentRepository;
import com.snp.service.OrderAdjustmentService;
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
 * Integration tests for the {@link OrderAdjustmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderAdjustmentResourceIT {

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

    private static final String ENTITY_API_URL = "/api/order-adjustments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderAdjustmentRepository orderAdjustmentRepository;

    @Mock
    private OrderAdjustmentRepository orderAdjustmentRepositoryMock;

    @Mock
    private OrderAdjustmentService orderAdjustmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderAdjustmentMockMvc;

    private OrderAdjustment orderAdjustment;

    private OrderAdjustment insertedOrderAdjustment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAdjustment createEntity() {
        return new OrderAdjustment()
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
    public static OrderAdjustment createUpdatedEntity() {
        return new OrderAdjustment()
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
        orderAdjustment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderAdjustment != null) {
            orderAdjustmentRepository.delete(insertedOrderAdjustment);
            insertedOrderAdjustment = null;
        }
    }

    @Test
    @Transactional
    void createOrderAdjustment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderAdjustment
        var returnedOrderAdjustment = om.readValue(
            restOrderAdjustmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderAdjustment.class
        );

        // Validate the OrderAdjustment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderAdjustmentUpdatableFieldsEquals(returnedOrderAdjustment, getPersistedOrderAdjustment(returnedOrderAdjustment));

        insertedOrderAdjustment = returnedOrderAdjustment;
    }

    @Test
    @Transactional
    void createOrderAdjustmentWithExistingId() throws Exception {
        // Create the OrderAdjustment with an existing ID
        orderAdjustment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderAdjustmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustment)))
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderAdjustments() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAdjustment.getId().intValue())))
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
    void getAllOrderAdjustmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderAdjustmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAdjustmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderAdjustmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAdjustmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderAdjustmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderAdjustmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderAdjustmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderAdjustment() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get the orderAdjustment
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL_ID, orderAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderAdjustment.getId().intValue()))
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
    void getOrderAdjustmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        Long id = orderAdjustment.getId();

        defaultOrderAdjustmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderAdjustmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderAdjustmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where description equals to
        defaultOrderAdjustmentFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where description in
        defaultOrderAdjustmentFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where description is not null
        defaultOrderAdjustmentFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where description contains
        defaultOrderAdjustmentFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where description does not contain
        defaultOrderAdjustmentFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where comments equals to
        defaultOrderAdjustmentFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where comments in
        defaultOrderAdjustmentFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where comments is not null
        defaultOrderAdjustmentFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where comments contains
        defaultOrderAdjustmentFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where comments does not contain
        defaultOrderAdjustmentFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount equals to
        defaultOrderAdjustmentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount in
        defaultOrderAdjustmentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount is not null
        defaultOrderAdjustmentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount is greater than or equal to
        defaultOrderAdjustmentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount is less than or equal to
        defaultOrderAdjustmentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount is less than
        defaultOrderAdjustmentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where amount is greater than
        defaultOrderAdjustmentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage equals to
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.equals=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.equals=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage in
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.in=" + DEFAULT_SOURCE_PERCENTAGE + "," + UPDATED_SOURCE_PERCENTAGE,
            "sourcePercentage.in=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage is not null
        defaultOrderAdjustmentFiltering("sourcePercentage.specified=true", "sourcePercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage is greater than or equal to
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.greaterThanOrEqual=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.greaterThanOrEqual=" + UPDATED_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage is less than or equal to
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.lessThanOrEqual=" + DEFAULT_SOURCE_PERCENTAGE,
            "sourcePercentage.lessThanOrEqual=" + SMALLER_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage is less than
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.lessThan=" + UPDATED_SOURCE_PERCENTAGE,
            "sourcePercentage.lessThan=" + DEFAULT_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourcePercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourcePercentage is greater than
        defaultOrderAdjustmentFiltering(
            "sourcePercentage.greaterThan=" + SMALLER_SOURCE_PERCENTAGE,
            "sourcePercentage.greaterThan=" + DEFAULT_SOURCE_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourceRefIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourceRefId equals to
        defaultOrderAdjustmentFiltering("sourceRefId.equals=" + DEFAULT_SOURCE_REF_ID, "sourceRefId.equals=" + UPDATED_SOURCE_REF_ID);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourceRefIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourceRefId in
        defaultOrderAdjustmentFiltering(
            "sourceRefId.in=" + DEFAULT_SOURCE_REF_ID + "," + UPDATED_SOURCE_REF_ID,
            "sourceRefId.in=" + UPDATED_SOURCE_REF_ID
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourceRefIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourceRefId is not null
        defaultOrderAdjustmentFiltering("sourceRefId.specified=true", "sourceRefId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourceRefIdContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourceRefId contains
        defaultOrderAdjustmentFiltering("sourceRefId.contains=" + DEFAULT_SOURCE_REF_ID, "sourceRefId.contains=" + UPDATED_SOURCE_REF_ID);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsBySourceRefIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where sourceRefId does not contain
        defaultOrderAdjustmentFiltering(
            "sourceRefId.doesNotContain=" + UPDATED_SOURCE_REF_ID,
            "sourceRefId.doesNotContain=" + DEFAULT_SOURCE_REF_ID
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount equals to
        defaultOrderAdjustmentFiltering("exemptAmount.equals=" + DEFAULT_EXEMPT_AMOUNT, "exemptAmount.equals=" + UPDATED_EXEMPT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount in
        defaultOrderAdjustmentFiltering(
            "exemptAmount.in=" + DEFAULT_EXEMPT_AMOUNT + "," + UPDATED_EXEMPT_AMOUNT,
            "exemptAmount.in=" + UPDATED_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount is not null
        defaultOrderAdjustmentFiltering("exemptAmount.specified=true", "exemptAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount is greater than or equal to
        defaultOrderAdjustmentFiltering(
            "exemptAmount.greaterThanOrEqual=" + DEFAULT_EXEMPT_AMOUNT,
            "exemptAmount.greaterThanOrEqual=" + UPDATED_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount is less than or equal to
        defaultOrderAdjustmentFiltering(
            "exemptAmount.lessThanOrEqual=" + DEFAULT_EXEMPT_AMOUNT,
            "exemptAmount.lessThanOrEqual=" + SMALLER_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount is less than
        defaultOrderAdjustmentFiltering("exemptAmount.lessThan=" + UPDATED_EXEMPT_AMOUNT, "exemptAmount.lessThan=" + DEFAULT_EXEMPT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByExemptAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where exemptAmount is greater than
        defaultOrderAdjustmentFiltering(
            "exemptAmount.greaterThan=" + SMALLER_EXEMPT_AMOUNT,
            "exemptAmount.greaterThan=" + DEFAULT_EXEMPT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInTax equals to
        defaultOrderAdjustmentFiltering("includeInTax.equals=" + DEFAULT_INCLUDE_IN_TAX, "includeInTax.equals=" + UPDATED_INCLUDE_IN_TAX);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInTax in
        defaultOrderAdjustmentFiltering(
            "includeInTax.in=" + DEFAULT_INCLUDE_IN_TAX + "," + UPDATED_INCLUDE_IN_TAX,
            "includeInTax.in=" + UPDATED_INCLUDE_IN_TAX
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInTax is not null
        defaultOrderAdjustmentFiltering("includeInTax.specified=true", "includeInTax.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInShippingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInShipping equals to
        defaultOrderAdjustmentFiltering(
            "includeInShipping.equals=" + DEFAULT_INCLUDE_IN_SHIPPING,
            "includeInShipping.equals=" + UPDATED_INCLUDE_IN_SHIPPING
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInShippingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInShipping in
        defaultOrderAdjustmentFiltering(
            "includeInShipping.in=" + DEFAULT_INCLUDE_IN_SHIPPING + "," + UPDATED_INCLUDE_IN_SHIPPING,
            "includeInShipping.in=" + UPDATED_INCLUDE_IN_SHIPPING
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludeInShippingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includeInShipping is not null
        defaultOrderAdjustmentFiltering("includeInShipping.specified=true", "includeInShipping.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludedInPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includedInPrice equals to
        defaultOrderAdjustmentFiltering(
            "includedInPrice.equals=" + DEFAULT_INCLUDED_IN_PRICE,
            "includedInPrice.equals=" + UPDATED_INCLUDED_IN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludedInPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includedInPrice in
        defaultOrderAdjustmentFiltering(
            "includedInPrice.in=" + DEFAULT_INCLUDED_IN_PRICE + "," + UPDATED_INCLUDED_IN_PRICE,
            "includedInPrice.in=" + UPDATED_INCLUDED_IN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByIncludedInPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where includedInPrice is not null
        defaultOrderAdjustmentFiltering("includedInPrice.specified=true", "includedInPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where entryDate equals to
        defaultOrderAdjustmentFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where entryDate in
        defaultOrderAdjustmentFiltering(
            "entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE,
            "entryDate.in=" + UPDATED_ENTRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        // Get all the orderAdjustmentList where entryDate is not null
        defaultOrderAdjustmentFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderAdjustment.setOrder(order);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long orderId = order.getId();
        // Get all the orderAdjustmentList where order equals to orderId
        defaultOrderAdjustmentShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderAdjustmentList where order equals to (orderId + 1)
        defaultOrderAdjustmentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        orderAdjustment.setOrderItem(orderItem);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long orderItemId = orderItem.getId();
        // Get all the orderAdjustmentList where orderItem equals to orderItemId
        defaultOrderAdjustmentShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderAdjustmentList where orderItem equals to (orderItemId + 1)
        defaultOrderAdjustmentShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByAdjustmentTypeIsEqualToSomething() throws Exception {
        OrderAdjustmentType adjustmentType;
        if (TestUtil.findAll(em, OrderAdjustmentType.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            adjustmentType = OrderAdjustmentTypeResourceIT.createEntity();
        } else {
            adjustmentType = TestUtil.findAll(em, OrderAdjustmentType.class).getFirst();
        }
        em.persist(adjustmentType);
        em.flush();
        orderAdjustment.setAdjustmentType(adjustmentType);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long adjustmentTypeId = adjustmentType.getId();
        // Get all the orderAdjustmentList where adjustmentType equals to adjustmentTypeId
        defaultOrderAdjustmentShouldBeFound("adjustmentTypeId.equals=" + adjustmentTypeId);

        // Get all the orderAdjustmentList where adjustmentType equals to (adjustmentTypeId + 1)
        defaultOrderAdjustmentShouldNotBeFound("adjustmentTypeId.equals=" + (adjustmentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        orderAdjustment.setProduct(product);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long productId = product.getId();
        // Get all the orderAdjustmentList where product equals to productId
        defaultOrderAdjustmentShouldBeFound("productId.equals=" + productId);

        // Get all the orderAdjustmentList where product equals to (productId + 1)
        defaultOrderAdjustmentShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        orderAdjustment.setProductFeature(productFeature);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long productFeatureId = productFeature.getId();
        // Get all the orderAdjustmentList where productFeature equals to productFeatureId
        defaultOrderAdjustmentShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the orderAdjustmentList where productFeature equals to (productFeatureId + 1)
        defaultOrderAdjustmentShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentsByTaxAuthorityIsEqualToSomething() throws Exception {
        TaxAuthority taxAuthority;
        if (TestUtil.findAll(em, TaxAuthority.class).isEmpty()) {
            orderAdjustmentRepository.saveAndFlush(orderAdjustment);
            taxAuthority = TaxAuthorityResourceIT.createEntity();
        } else {
            taxAuthority = TestUtil.findAll(em, TaxAuthority.class).getFirst();
        }
        em.persist(taxAuthority);
        em.flush();
        orderAdjustment.setTaxAuthority(taxAuthority);
        orderAdjustmentRepository.saveAndFlush(orderAdjustment);
        Long taxAuthorityId = taxAuthority.getId();
        // Get all the orderAdjustmentList where taxAuthority equals to taxAuthorityId
        defaultOrderAdjustmentShouldBeFound("taxAuthorityId.equals=" + taxAuthorityId);

        // Get all the orderAdjustmentList where taxAuthority equals to (taxAuthorityId + 1)
        defaultOrderAdjustmentShouldNotBeFound("taxAuthorityId.equals=" + (taxAuthorityId + 1));
    }

    private void defaultOrderAdjustmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderAdjustmentShouldBeFound(shouldBeFound);
        defaultOrderAdjustmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderAdjustmentShouldBeFound(String filter) throws Exception {
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAdjustment.getId().intValue())))
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
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderAdjustmentShouldNotBeFound(String filter) throws Exception {
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderAdjustment() throws Exception {
        // Get the orderAdjustment
        restOrderAdjustmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderAdjustment() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustment
        OrderAdjustment updatedOrderAdjustment = orderAdjustmentRepository.findById(orderAdjustment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderAdjustment are not directly saved in db
        em.detach(updatedOrderAdjustment);
        updatedOrderAdjustment
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

        restOrderAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderAdjustment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderAdjustmentToMatchAllProperties(updatedOrderAdjustment);
    }

    @Test
    @Transactional
    void putNonExistingOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderAdjustment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderAdjustmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustment using partial update
        OrderAdjustment partialUpdatedOrderAdjustment = new OrderAdjustment();
        partialUpdatedOrderAdjustment.setId(orderAdjustment.getId());

        partialUpdatedOrderAdjustment
            .description(UPDATED_DESCRIPTION)
            .comments(UPDATED_COMMENTS)
            .sourcePercentage(UPDATED_SOURCE_PERCENTAGE)
            .exemptAmount(UPDATED_EXEMPT_AMOUNT)
            .includedInPrice(UPDATED_INCLUDED_IN_PRICE);

        restOrderAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAdjustmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderAdjustment, orderAdjustment),
            getPersistedOrderAdjustment(orderAdjustment)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderAdjustmentWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustment using partial update
        OrderAdjustment partialUpdatedOrderAdjustment = new OrderAdjustment();
        partialUpdatedOrderAdjustment.setId(orderAdjustment.getId());

        partialUpdatedOrderAdjustment
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

        restOrderAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAdjustmentUpdatableFieldsEquals(
            partialUpdatedOrderAdjustment,
            getPersistedOrderAdjustment(partialUpdatedOrderAdjustment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAdjustment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderAdjustment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderAdjustment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAdjustment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderAdjustment() throws Exception {
        // Initialize the database
        insertedOrderAdjustment = orderAdjustmentRepository.saveAndFlush(orderAdjustment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderAdjustment
        restOrderAdjustmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderAdjustment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderAdjustmentRepository.count();
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

    protected OrderAdjustment getPersistedOrderAdjustment(OrderAdjustment orderAdjustment) {
        return orderAdjustmentRepository.findById(orderAdjustment.getId()).orElseThrow();
    }

    protected void assertPersistedOrderAdjustmentToMatchAllProperties(OrderAdjustment expectedOrderAdjustment) {
        assertOrderAdjustmentAllPropertiesEquals(expectedOrderAdjustment, getPersistedOrderAdjustment(expectedOrderAdjustment));
    }

    protected void assertPersistedOrderAdjustmentToMatchUpdatableProperties(OrderAdjustment expectedOrderAdjustment) {
        assertOrderAdjustmentAllUpdatablePropertiesEquals(expectedOrderAdjustment, getPersistedOrderAdjustment(expectedOrderAdjustment));
    }
}
