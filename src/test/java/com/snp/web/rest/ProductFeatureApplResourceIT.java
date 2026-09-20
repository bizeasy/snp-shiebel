package com.snp.web.rest;

import static com.snp.domain.ProductFeatureApplAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.ProductFeature;
import com.snp.domain.ProductFeatureAppl;
import com.snp.domain.ProductFeatureApplType;
import com.snp.repository.ProductFeatureApplRepository;
import com.snp.service.ProductFeatureApplService;
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
 * Integration tests for the {@link ProductFeatureApplResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureApplResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/product-feature-appls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureApplRepository productFeatureApplRepository;

    @Mock
    private ProductFeatureApplRepository productFeatureApplRepositoryMock;

    @Mock
    private ProductFeatureApplService productFeatureApplServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureApplMockMvc;

    private ProductFeatureAppl productFeatureAppl;

    private ProductFeatureAppl insertedProductFeatureAppl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureAppl createEntity(EntityManager em) {
        ProductFeatureAppl productFeatureAppl = new ProductFeatureAppl()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .amount(DEFAULT_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productFeatureAppl.setProduct(product);
        // Add required entity
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeature = ProductFeatureResourceIT.createEntity();
            em.persist(productFeature);
            em.flush();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        productFeatureAppl.setProductFeature(productFeature);
        return productFeatureAppl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureAppl createUpdatedEntity(EntityManager em) {
        ProductFeatureAppl updatedProductFeatureAppl = new ProductFeatureAppl()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .amount(UPDATED_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductFeatureAppl.setProduct(product);
        // Add required entity
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeature = ProductFeatureResourceIT.createUpdatedEntity();
            em.persist(productFeature);
            em.flush();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        updatedProductFeatureAppl.setProductFeature(productFeature);
        return updatedProductFeatureAppl;
    }

    @BeforeEach
    void initTest() {
        productFeatureAppl = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureAppl != null) {
            productFeatureApplRepository.delete(insertedProductFeatureAppl);
            insertedProductFeatureAppl = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureAppl() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureAppl
        var returnedProductFeatureAppl = om.readValue(
            restProductFeatureApplMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureAppl)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureAppl.class
        );

        // Validate the ProductFeatureAppl in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureApplUpdatableFieldsEquals(
            returnedProductFeatureAppl,
            getPersistedProductFeatureAppl(returnedProductFeatureAppl)
        );

        insertedProductFeatureAppl = returnedProductFeatureAppl;
    }

    @Test
    @Transactional
    void createProductFeatureApplWithExistingId() throws Exception {
        // Create the ProductFeatureAppl with an existing ID
        productFeatureAppl.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureAppl)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureAppl.setFromDate(null);

        // Create the ProductFeatureAppl, which fails.

        restProductFeatureApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureAppl)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureAppls() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureApplsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFeatureApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFeatureApplServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureApplsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFeatureApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFeatureApplRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFeatureAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get the productFeatureAppl
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureAppl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureAppl.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getProductFeatureApplsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        Long id = productFeatureAppl.getId();

        defaultProductFeatureApplFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureApplFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureApplFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where fromDate equals to
        defaultProductFeatureApplFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where fromDate in
        defaultProductFeatureApplFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where fromDate is not null
        defaultProductFeatureApplFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where thruDate equals to
        defaultProductFeatureApplFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where thruDate in
        defaultProductFeatureApplFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where thruDate is not null
        defaultProductFeatureApplFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo equals to
        defaultProductFeatureApplFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo in
        defaultProductFeatureApplFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo is not null
        defaultProductFeatureApplFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo is greater than or equal to
        defaultProductFeatureApplFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo is less than or equal to
        defaultProductFeatureApplFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo is less than
        defaultProductFeatureApplFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where sequenceNo is greater than
        defaultProductFeatureApplFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount equals to
        defaultProductFeatureApplFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount in
        defaultProductFeatureApplFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount is not null
        defaultProductFeatureApplFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount is greater than or equal to
        defaultProductFeatureApplFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount is less than or equal to
        defaultProductFeatureApplFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount is less than
        defaultProductFeatureApplFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        // Get all the productFeatureApplList where amount is greater than
        defaultProductFeatureApplFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productFeatureApplRepository.saveAndFlush(productFeatureAppl);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productFeatureAppl.setProduct(product);
        productFeatureApplRepository.saveAndFlush(productFeatureAppl);
        Long productId = product.getId();
        // Get all the productFeatureApplList where product equals to productId
        defaultProductFeatureApplShouldBeFound("productId.equals=" + productId);

        // Get all the productFeatureApplList where product equals to (productId + 1)
        defaultProductFeatureApplShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeatureApplRepository.saveAndFlush(productFeatureAppl);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        productFeatureAppl.setProductFeature(productFeature);
        productFeatureApplRepository.saveAndFlush(productFeatureAppl);
        Long productFeatureId = productFeature.getId();
        // Get all the productFeatureApplList where productFeature equals to productFeatureId
        defaultProductFeatureApplShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the productFeatureApplList where productFeature equals to (productFeatureId + 1)
        defaultProductFeatureApplShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    @Test
    @Transactional
    void getAllProductFeatureApplsByFeatureApplTypeIsEqualToSomething() throws Exception {
        ProductFeatureApplType featureApplType;
        if (TestUtil.findAll(em, ProductFeatureApplType.class).isEmpty()) {
            productFeatureApplRepository.saveAndFlush(productFeatureAppl);
            featureApplType = ProductFeatureApplTypeResourceIT.createEntity();
        } else {
            featureApplType = TestUtil.findAll(em, ProductFeatureApplType.class).getFirst();
        }
        em.persist(featureApplType);
        em.flush();
        productFeatureAppl.setFeatureApplType(featureApplType);
        productFeatureApplRepository.saveAndFlush(productFeatureAppl);
        Long featureApplTypeId = featureApplType.getId();
        // Get all the productFeatureApplList where featureApplType equals to featureApplTypeId
        defaultProductFeatureApplShouldBeFound("featureApplTypeId.equals=" + featureApplTypeId);

        // Get all the productFeatureApplList where featureApplType equals to (featureApplTypeId + 1)
        defaultProductFeatureApplShouldNotBeFound("featureApplTypeId.equals=" + (featureApplTypeId + 1));
    }

    private void defaultProductFeatureApplFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureApplShouldBeFound(shouldBeFound);
        defaultProductFeatureApplShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureApplShouldBeFound(String filter) throws Exception {
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));

        // Check, that the count call also returns 1
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureApplShouldNotBeFound(String filter) throws Exception {
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureAppl() throws Exception {
        // Get the productFeatureAppl
        restProductFeatureApplMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureAppl
        ProductFeatureAppl updatedProductFeatureAppl = productFeatureApplRepository.findById(productFeatureAppl.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureAppl are not directly saved in db
        em.detach(updatedProductFeatureAppl);
        updatedProductFeatureAppl
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .amount(UPDATED_AMOUNT);

        restProductFeatureApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureApplToMatchAllProperties(updatedProductFeatureAppl);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureAppl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureAppl using partial update
        ProductFeatureAppl partialUpdatedProductFeatureAppl = new ProductFeatureAppl();
        partialUpdatedProductFeatureAppl.setId(productFeatureAppl.getId());

        partialUpdatedProductFeatureAppl.sequenceNo(UPDATED_SEQUENCE_NO);

        restProductFeatureApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureApplUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureAppl, productFeatureAppl),
            getPersistedProductFeatureAppl(productFeatureAppl)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureAppl using partial update
        ProductFeatureAppl partialUpdatedProductFeatureAppl = new ProductFeatureAppl();
        partialUpdatedProductFeatureAppl.setId(productFeatureAppl.getId());

        partialUpdatedProductFeatureAppl
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .amount(UPDATED_AMOUNT);

        restProductFeatureApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureApplUpdatableFieldsEquals(
            partialUpdatedProductFeatureAppl,
            getPersistedProductFeatureAppl(partialUpdatedProductFeatureAppl)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureApplMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureAppl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureAppl = productFeatureApplRepository.saveAndFlush(productFeatureAppl);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureAppl
        restProductFeatureApplMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureAppl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureApplRepository.count();
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

    protected ProductFeatureAppl getPersistedProductFeatureAppl(ProductFeatureAppl productFeatureAppl) {
        return productFeatureApplRepository.findById(productFeatureAppl.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureApplToMatchAllProperties(ProductFeatureAppl expectedProductFeatureAppl) {
        assertProductFeatureApplAllPropertiesEquals(expectedProductFeatureAppl, getPersistedProductFeatureAppl(expectedProductFeatureAppl));
    }

    protected void assertPersistedProductFeatureApplToMatchUpdatableProperties(ProductFeatureAppl expectedProductFeatureAppl) {
        assertProductFeatureApplAllUpdatablePropertiesEquals(
            expectedProductFeatureAppl,
            getPersistedProductFeatureAppl(expectedProductFeatureAppl)
        );
    }
}
