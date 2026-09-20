package com.snp.web.rest;

import static com.snp.domain.ProductAssocAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.ProductAssoc;
import com.snp.domain.ProductAssocType;
import com.snp.repository.ProductAssocRepository;
import com.snp.service.ProductAssocService;
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
 * Integration tests for the {@link ProductAssocResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductAssocResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/product-assocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAssocRepository productAssocRepository;

    @Mock
    private ProductAssocRepository productAssocRepositoryMock;

    @Mock
    private ProductAssocService productAssocServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAssocMockMvc;

    private ProductAssoc productAssoc;

    private ProductAssoc insertedProductAssoc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAssoc createEntity(EntityManager em) {
        ProductAssoc productAssoc = new ProductAssoc()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productAssoc.setProduct(product);
        // Add required entity
        productAssoc.setProductTo(product);
        return productAssoc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAssoc createUpdatedEntity(EntityManager em) {
        ProductAssoc updatedProductAssoc = new ProductAssoc()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductAssoc.setProduct(product);
        // Add required entity
        updatedProductAssoc.setProductTo(product);
        return updatedProductAssoc;
    }

    @BeforeEach
    void initTest() {
        productAssoc = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductAssoc != null) {
            productAssocRepository.delete(insertedProductAssoc);
            insertedProductAssoc = null;
        }
    }

    @Test
    @Transactional
    void createProductAssoc() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductAssoc
        var returnedProductAssoc = om.readValue(
            restProductAssocMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssoc)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAssoc.class
        );

        // Validate the ProductAssoc in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAssocUpdatableFieldsEquals(returnedProductAssoc, getPersistedProductAssoc(returnedProductAssoc));

        insertedProductAssoc = returnedProductAssoc;
    }

    @Test
    @Transactional
    void createProductAssocWithExistingId() throws Exception {
        // Create the ProductAssoc with an existing ID
        productAssoc.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAssocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssoc)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productAssoc.setFromDate(null);

        // Create the ProductAssoc, which fails.

        restProductAssocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssoc)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductAssocs() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAssoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAssocsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productAssocServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAssocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productAssocServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAssocsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productAssocServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAssocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productAssocRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductAssoc() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get the productAssoc
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL_ID, productAssoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAssoc.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getProductAssocsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        Long id = productAssoc.getId();

        defaultProductAssocFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductAssocFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductAssocFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductAssocsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where fromDate equals to
        defaultProductAssocFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductAssocsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where fromDate in
        defaultProductAssocFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductAssocsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where fromDate is not null
        defaultProductAssocFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where thruDate equals to
        defaultProductAssocFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductAssocsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where thruDate in
        defaultProductAssocFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductAssocsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where thruDate is not null
        defaultProductAssocFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo equals to
        defaultProductAssocFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo in
        defaultProductAssocFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo is not null
        defaultProductAssocFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo is greater than or equal to
        defaultProductAssocFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo is less than or equal to
        defaultProductAssocFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo is less than
        defaultProductAssocFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductAssocsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where sequenceNo is greater than
        defaultProductAssocFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity equals to
        defaultProductAssocFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity in
        defaultProductAssocFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity is not null
        defaultProductAssocFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity is greater than or equal to
        defaultProductAssocFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity is less than or equal to
        defaultProductAssocFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity is less than
        defaultProductAssocFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        // Get all the productAssocList where quantity is greater than
        defaultProductAssocFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductAssocsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productAssocRepository.saveAndFlush(productAssoc);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productAssoc.setProduct(product);
        productAssocRepository.saveAndFlush(productAssoc);
        Long productId = product.getId();
        // Get all the productAssocList where product equals to productId
        defaultProductAssocShouldBeFound("productId.equals=" + productId);

        // Get all the productAssocList where product equals to (productId + 1)
        defaultProductAssocShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductAssocsByProductToIsEqualToSomething() throws Exception {
        Product productTo;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productAssocRepository.saveAndFlush(productAssoc);
            productTo = ProductResourceIT.createEntity();
        } else {
            productTo = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(productTo);
        em.flush();
        productAssoc.setProductTo(productTo);
        productAssocRepository.saveAndFlush(productAssoc);
        Long productToId = productTo.getId();
        // Get all the productAssocList where productTo equals to productToId
        defaultProductAssocShouldBeFound("productToId.equals=" + productToId);

        // Get all the productAssocList where productTo equals to (productToId + 1)
        defaultProductAssocShouldNotBeFound("productToId.equals=" + (productToId + 1));
    }

    @Test
    @Transactional
    void getAllProductAssocsByProductAssocTypeIsEqualToSomething() throws Exception {
        ProductAssocType productAssocType;
        if (TestUtil.findAll(em, ProductAssocType.class).isEmpty()) {
            productAssocRepository.saveAndFlush(productAssoc);
            productAssocType = ProductAssocTypeResourceIT.createEntity();
        } else {
            productAssocType = TestUtil.findAll(em, ProductAssocType.class).getFirst();
        }
        em.persist(productAssocType);
        em.flush();
        productAssoc.setProductAssocType(productAssocType);
        productAssocRepository.saveAndFlush(productAssoc);
        Long productAssocTypeId = productAssocType.getId();
        // Get all the productAssocList where productAssocType equals to productAssocTypeId
        defaultProductAssocShouldBeFound("productAssocTypeId.equals=" + productAssocTypeId);

        // Get all the productAssocList where productAssocType equals to (productAssocTypeId + 1)
        defaultProductAssocShouldNotBeFound("productAssocTypeId.equals=" + (productAssocTypeId + 1));
    }

    private void defaultProductAssocFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductAssocShouldBeFound(shouldBeFound);
        defaultProductAssocShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductAssocShouldBeFound(String filter) throws Exception {
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAssoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));

        // Check, that the count call also returns 1
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductAssocShouldNotBeFound(String filter) throws Exception {
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductAssocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductAssoc() throws Exception {
        // Get the productAssoc
        restProductAssocMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAssoc() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssoc
        ProductAssoc updatedProductAssoc = productAssocRepository.findById(productAssoc.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAssoc are not directly saved in db
        em.detach(updatedProductAssoc);
        updatedProductAssoc
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY);

        restProductAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAssoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAssoc))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAssocToMatchAllProperties(updatedProductAssoc);
    }

    @Test
    @Transactional
    void putNonExistingProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAssoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAssoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductAssocWithPatch() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssoc using partial update
        ProductAssoc partialUpdatedProductAssoc = new ProductAssoc();
        partialUpdatedProductAssoc.setId(productAssoc.getId());

        partialUpdatedProductAssoc.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restProductAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAssoc))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAssocUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAssoc, productAssoc),
            getPersistedProductAssoc(productAssoc)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAssocWithPatch() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAssoc using partial update
        ProductAssoc partialUpdatedProductAssoc = new ProductAssoc();
        partialUpdatedProductAssoc.setId(productAssoc.getId());

        partialUpdatedProductAssoc
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY);

        restProductAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAssoc))
            )
            .andExpect(status().isOk());

        // Validate the ProductAssoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAssocUpdatableFieldsEquals(partialUpdatedProductAssoc, getPersistedProductAssoc(partialUpdatedProductAssoc));
    }

    @Test
    @Transactional
    void patchNonExistingProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssocMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAssoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductAssoc() throws Exception {
        // Initialize the database
        insertedProductAssoc = productAssocRepository.saveAndFlush(productAssoc);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productAssoc
        restProductAssocMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAssoc.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productAssocRepository.count();
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

    protected ProductAssoc getPersistedProductAssoc(ProductAssoc productAssoc) {
        return productAssocRepository.findById(productAssoc.getId()).orElseThrow();
    }

    protected void assertPersistedProductAssocToMatchAllProperties(ProductAssoc expectedProductAssoc) {
        assertProductAssocAllPropertiesEquals(expectedProductAssoc, getPersistedProductAssoc(expectedProductAssoc));
    }

    protected void assertPersistedProductAssocToMatchUpdatableProperties(ProductAssoc expectedProductAssoc) {
        assertProductAssocAllUpdatablePropertiesEquals(expectedProductAssoc, getPersistedProductAssoc(expectedProductAssoc));
    }
}
