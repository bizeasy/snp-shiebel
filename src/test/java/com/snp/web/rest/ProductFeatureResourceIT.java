package com.snp.web.rest;

import static com.snp.domain.ProductFeatureAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeature;
import com.snp.domain.ProductFeatureCategory;
import com.snp.domain.ProductFeatureType;
import com.snp.repository.ProductFeatureRepository;
import com.snp.service.ProductFeatureService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProductFeatureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_ABBREV = "AAAAAAAAAA";
    private static final String UPDATED_ABBREV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-features";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureRepository productFeatureRepository;

    @Mock
    private ProductFeatureRepository productFeatureRepositoryMock;

    @Mock
    private ProductFeatureService productFeatureServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureMockMvc;

    private ProductFeature productFeature;

    private ProductFeature insertedProductFeature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeature createEntity() {
        return new ProductFeature()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .abbrev(DEFAULT_ABBREV);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeature createUpdatedEntity() {
        return new ProductFeature()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .abbrev(UPDATED_ABBREV);
    }

    @BeforeEach
    void initTest() {
        productFeature = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeature != null) {
            productFeatureRepository.delete(insertedProductFeature);
            insertedProductFeature = null;
        }
    }

    @Test
    @Transactional
    void createProductFeature() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeature
        var returnedProductFeature = om.readValue(
            restProductFeatureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeature)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeature.class
        );

        // Validate the ProductFeature in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureUpdatableFieldsEquals(returnedProductFeature, getPersistedProductFeature(returnedProductFeature));

        insertedProductFeature = returnedProductFeature;
    }

    @Test
    @Transactional
    void createProductFeatureWithExistingId() throws Exception {
        // Create the ProductFeature with an existing ID
        productFeature.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeature)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeature.setName(null);

        // Create the ProductFeature, which fails.

        restProductFeatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeature)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatures() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeature.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].abbrev").value(hasItem(DEFAULT_ABBREV)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeaturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFeatureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFeatureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeaturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFeatureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFeatureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFeature() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get the productFeature
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeature.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.abbrev").value(DEFAULT_ABBREV));
    }

    @Test
    @Transactional
    void getProductFeaturesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        Long id = productFeature.getId();

        defaultProductFeatureFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where name equals to
        defaultProductFeatureFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where name in
        defaultProductFeatureFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where name is not null
        defaultProductFeatureFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeaturesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where name contains
        defaultProductFeatureFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where name does not contain
        defaultProductFeatureFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where description equals to
        defaultProductFeatureFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where description in
        defaultProductFeatureFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeaturesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where description is not null
        defaultProductFeatureFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeaturesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where description contains
        defaultProductFeatureFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where description does not contain
        defaultProductFeatureFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount equals to
        defaultProductFeatureFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount in
        defaultProductFeatureFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount is not null
        defaultProductFeatureFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount is greater than or equal to
        defaultProductFeatureFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount is less than or equal to
        defaultProductFeatureFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount is less than
        defaultProductFeatureFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where amount is greater than
        defaultProductFeatureFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo equals to
        defaultProductFeatureFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo in
        defaultProductFeatureFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo is not null
        defaultProductFeatureFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo is greater than or equal to
        defaultProductFeatureFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo is less than or equal to
        defaultProductFeatureFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo is less than
        defaultProductFeatureFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeaturesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where sequenceNo is greater than
        defaultProductFeatureFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAbbrevIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where abbrev equals to
        defaultProductFeatureFiltering("abbrev.equals=" + DEFAULT_ABBREV, "abbrev.equals=" + UPDATED_ABBREV);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAbbrevIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where abbrev in
        defaultProductFeatureFiltering("abbrev.in=" + DEFAULT_ABBREV + "," + UPDATED_ABBREV, "abbrev.in=" + UPDATED_ABBREV);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAbbrevIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where abbrev is not null
        defaultProductFeatureFiltering("abbrev.specified=true", "abbrev.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAbbrevContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where abbrev contains
        defaultProductFeatureFiltering("abbrev.contains=" + DEFAULT_ABBREV, "abbrev.contains=" + UPDATED_ABBREV);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByAbbrevNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        // Get all the productFeatureList where abbrev does not contain
        defaultProductFeatureFiltering("abbrev.doesNotContain=" + UPDATED_ABBREV, "abbrev.doesNotContain=" + DEFAULT_ABBREV);
    }

    @Test
    @Transactional
    void getAllProductFeaturesByProductFeatureTypeIsEqualToSomething() throws Exception {
        ProductFeatureType productFeatureType;
        if (TestUtil.findAll(em, ProductFeatureType.class).isEmpty()) {
            productFeatureRepository.saveAndFlush(productFeature);
            productFeatureType = ProductFeatureTypeResourceIT.createEntity();
        } else {
            productFeatureType = TestUtil.findAll(em, ProductFeatureType.class).getFirst();
        }
        em.persist(productFeatureType);
        em.flush();
        productFeature.setProductFeatureType(productFeatureType);
        productFeatureRepository.saveAndFlush(productFeature);
        Long productFeatureTypeId = productFeatureType.getId();
        // Get all the productFeatureList where productFeatureType equals to productFeatureTypeId
        defaultProductFeatureShouldBeFound("productFeatureTypeId.equals=" + productFeatureTypeId);

        // Get all the productFeatureList where productFeatureType equals to (productFeatureTypeId + 1)
        defaultProductFeatureShouldNotBeFound("productFeatureTypeId.equals=" + (productFeatureTypeId + 1));
    }

    @Test
    @Transactional
    void getAllProductFeaturesByProductFeatureCategoryIsEqualToSomething() throws Exception {
        ProductFeatureCategory productFeatureCategory;
        if (TestUtil.findAll(em, ProductFeatureCategory.class).isEmpty()) {
            productFeatureRepository.saveAndFlush(productFeature);
            productFeatureCategory = ProductFeatureCategoryResourceIT.createEntity();
        } else {
            productFeatureCategory = TestUtil.findAll(em, ProductFeatureCategory.class).getFirst();
        }
        em.persist(productFeatureCategory);
        em.flush();
        productFeature.setProductFeatureCategory(productFeatureCategory);
        productFeatureRepository.saveAndFlush(productFeature);
        Long productFeatureCategoryId = productFeatureCategory.getId();
        // Get all the productFeatureList where productFeatureCategory equals to productFeatureCategoryId
        defaultProductFeatureShouldBeFound("productFeatureCategoryId.equals=" + productFeatureCategoryId);

        // Get all the productFeatureList where productFeatureCategory equals to (productFeatureCategoryId + 1)
        defaultProductFeatureShouldNotBeFound("productFeatureCategoryId.equals=" + (productFeatureCategoryId + 1));
    }

    private void defaultProductFeatureFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureShouldBeFound(shouldBeFound);
        defaultProductFeatureShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureShouldBeFound(String filter) throws Exception {
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeature.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].abbrev").value(hasItem(DEFAULT_ABBREV)));

        // Check, that the count call also returns 1
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureShouldNotBeFound(String filter) throws Exception {
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeature() throws Exception {
        // Get the productFeature
        restProductFeatureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeature() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeature
        ProductFeature updatedProductFeature = productFeatureRepository.findById(productFeature.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeature are not directly saved in db
        em.detach(updatedProductFeature);
        updatedProductFeature
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .abbrev(UPDATED_ABBREV);

        restProductFeatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeature.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeature))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureToMatchAllProperties(updatedProductFeature);
    }

    @Test
    @Transactional
    void putNonExistingProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeature.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeature))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeature))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeature)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeature using partial update
        ProductFeature partialUpdatedProductFeature = new ProductFeature();
        partialUpdatedProductFeature.setId(productFeature.getId());

        partialUpdatedProductFeature.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restProductFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeature.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeature))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeature, productFeature),
            getPersistedProductFeature(productFeature)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeature using partial update
        ProductFeature partialUpdatedProductFeature = new ProductFeature();
        partialUpdatedProductFeature.setId(productFeature.getId());

        partialUpdatedProductFeature
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .abbrev(UPDATED_ABBREV);

        restProductFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeature.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeature))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureUpdatableFieldsEquals(partialUpdatedProductFeature, getPersistedProductFeature(partialUpdatedProductFeature));
    }

    @Test
    @Transactional
    void patchNonExistingProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeature.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeature))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeature))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeature)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeature() throws Exception {
        // Initialize the database
        insertedProductFeature = productFeatureRepository.saveAndFlush(productFeature);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeature
        restProductFeatureMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeature.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureRepository.count();
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

    protected ProductFeature getPersistedProductFeature(ProductFeature productFeature) {
        return productFeatureRepository.findById(productFeature.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureToMatchAllProperties(ProductFeature expectedProductFeature) {
        assertProductFeatureAllPropertiesEquals(expectedProductFeature, getPersistedProductFeature(expectedProductFeature));
    }

    protected void assertPersistedProductFeatureToMatchUpdatableProperties(ProductFeature expectedProductFeature) {
        assertProductFeatureAllUpdatablePropertiesEquals(expectedProductFeature, getPersistedProductFeature(expectedProductFeature));
    }
}
