package com.snp.web.rest;

import static com.snp.domain.SupplierProductAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.SupplierProduct;
import com.snp.domain.Uom;
import com.snp.repository.SupplierProductRepository;
import com.snp.service.SupplierProductService;
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
 * Integration tests for the {@link SupplierProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierProductResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_SUPPLIER_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_PRODUCT_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MIN_ORDER_QTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MIN_ORDER_QTY = new BigDecimal(2);
    private static final BigDecimal SMALLER_MIN_ORDER_QTY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LAST_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LAST_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SHIPPING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIPPING_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SHIPPING_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CGST = new BigDecimal(1);
    private static final BigDecimal UPDATED_CGST = new BigDecimal(2);
    private static final BigDecimal SMALLER_CGST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_IGST = new BigDecimal(1);
    private static final BigDecimal UPDATED_IGST = new BigDecimal(2);
    private static final BigDecimal SMALLER_IGST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SGST = new BigDecimal(1);
    private static final BigDecimal UPDATED_SGST = new BigDecimal(2);
    private static final BigDecimal SMALLER_SGST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEAD_TIME_DAYS = 1;
    private static final Integer UPDATED_LEAD_TIME_DAYS = 2;
    private static final Integer SMALLER_LEAD_TIME_DAYS = 1 - 1;

    private static final BigDecimal DEFAULT_UNITS_INCLUDED = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNITS_INCLUDED = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNITS_INCLUDED = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_CAN_DROP_SHIP = false;
    private static final Boolean UPDATED_CAN_DROP_SHIP = true;

    private static final String ENTITY_API_URL = "/api/supplier-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierProductRepository supplierProductRepository;

    @Mock
    private SupplierProductRepository supplierProductRepositoryMock;

    @Mock
    private SupplierProductService supplierProductServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierProductMockMvc;

    private SupplierProduct supplierProduct;

    private SupplierProduct insertedSupplierProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierProduct createEntity(EntityManager em) {
        SupplierProduct supplierProduct = new SupplierProduct()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .supplierProductId(DEFAULT_SUPPLIER_PRODUCT_ID)
            .minOrderQty(DEFAULT_MIN_ORDER_QTY)
            .lastPrice(DEFAULT_LAST_PRICE)
            .shippingPrice(DEFAULT_SHIPPING_PRICE)
            .cgst(DEFAULT_CGST)
            .igst(DEFAULT_IGST)
            .sgst(DEFAULT_SGST)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .comments(DEFAULT_COMMENTS)
            .leadTimeDays(DEFAULT_LEAD_TIME_DAYS)
            .unitsIncluded(DEFAULT_UNITS_INCLUDED)
            .canDropShip(DEFAULT_CAN_DROP_SHIP);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        supplierProduct.setProduct(product);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        supplierProduct.setParty(party);
        return supplierProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierProduct createUpdatedEntity(EntityManager em) {
        SupplierProduct updatedSupplierProduct = new SupplierProduct()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .supplierProductId(UPDATED_SUPPLIER_PRODUCT_ID)
            .minOrderQty(UPDATED_MIN_ORDER_QTY)
            .lastPrice(UPDATED_LAST_PRICE)
            .shippingPrice(UPDATED_SHIPPING_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .comments(UPDATED_COMMENTS)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .unitsIncluded(UPDATED_UNITS_INCLUDED)
            .canDropShip(UPDATED_CAN_DROP_SHIP);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedSupplierProduct.setProduct(product);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedSupplierProduct.setParty(party);
        return updatedSupplierProduct;
    }

    @BeforeEach
    void initTest() {
        supplierProduct = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSupplierProduct != null) {
            supplierProductRepository.delete(insertedSupplierProduct);
            insertedSupplierProduct = null;
        }
    }

    @Test
    @Transactional
    void createSupplierProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierProduct
        var returnedSupplierProduct = om.readValue(
            restSupplierProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierProduct)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierProduct.class
        );

        // Validate the SupplierProduct in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierProductUpdatableFieldsEquals(returnedSupplierProduct, getPersistedSupplierProduct(returnedSupplierProduct));

        insertedSupplierProduct = returnedSupplierProduct;
    }

    @Test
    @Transactional
    void createSupplierProductWithExistingId() throws Exception {
        // Create the SupplierProduct with an existing ID
        supplierProduct.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierProduct)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierProduct.setFromDate(null);

        // Create the SupplierProduct, which fails.

        restSupplierProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierProduct)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierProducts() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].supplierProductId").value(hasItem(DEFAULT_SUPPLIER_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].minOrderQty").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_QTY))))
            .andExpect(jsonPath("$.[*].lastPrice").value(hasItem(sameNumber(DEFAULT_LAST_PRICE))))
            .andExpect(jsonPath("$.[*].shippingPrice").value(hasItem(sameNumber(DEFAULT_SHIPPING_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].leadTimeDays").value(hasItem(DEFAULT_LEAD_TIME_DAYS)))
            .andExpect(jsonPath("$.[*].unitsIncluded").value(hasItem(sameNumber(DEFAULT_UNITS_INCLUDED))))
            .andExpect(jsonPath("$.[*].canDropShip").value(hasItem(DEFAULT_CAN_DROP_SHIP)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierProductServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierProductServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierProductRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierProduct() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get the supplierProduct
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierProduct.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.supplierProductId").value(DEFAULT_SUPPLIER_PRODUCT_ID))
            .andExpect(jsonPath("$.minOrderQty").value(sameNumber(DEFAULT_MIN_ORDER_QTY)))
            .andExpect(jsonPath("$.lastPrice").value(sameNumber(DEFAULT_LAST_PRICE)))
            .andExpect(jsonPath("$.shippingPrice").value(sameNumber(DEFAULT_SHIPPING_PRICE)))
            .andExpect(jsonPath("$.cgst").value(sameNumber(DEFAULT_CGST)))
            .andExpect(jsonPath("$.igst").value(sameNumber(DEFAULT_IGST)))
            .andExpect(jsonPath("$.sgst").value(sameNumber(DEFAULT_SGST)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.leadTimeDays").value(DEFAULT_LEAD_TIME_DAYS))
            .andExpect(jsonPath("$.unitsIncluded").value(sameNumber(DEFAULT_UNITS_INCLUDED)))
            .andExpect(jsonPath("$.canDropShip").value(DEFAULT_CAN_DROP_SHIP));
    }

    @Test
    @Transactional
    void getSupplierProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        Long id = supplierProduct.getId();

        defaultSupplierProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where fromDate equals to
        defaultSupplierProductFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where fromDate in
        defaultSupplierProductFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where fromDate is not null
        defaultSupplierProductFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where thruDate equals to
        defaultSupplierProductFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where thruDate in
        defaultSupplierProductFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where thruDate is not null
        defaultSupplierProductFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySupplierProductIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where supplierProductId equals to
        defaultSupplierProductFiltering(
            "supplierProductId.equals=" + DEFAULT_SUPPLIER_PRODUCT_ID,
            "supplierProductId.equals=" + UPDATED_SUPPLIER_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySupplierProductIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where supplierProductId in
        defaultSupplierProductFiltering(
            "supplierProductId.in=" + DEFAULT_SUPPLIER_PRODUCT_ID + "," + UPDATED_SUPPLIER_PRODUCT_ID,
            "supplierProductId.in=" + UPDATED_SUPPLIER_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySupplierProductIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where supplierProductId is not null
        defaultSupplierProductFiltering("supplierProductId.specified=true", "supplierProductId.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySupplierProductIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where supplierProductId contains
        defaultSupplierProductFiltering(
            "supplierProductId.contains=" + DEFAULT_SUPPLIER_PRODUCT_ID,
            "supplierProductId.contains=" + UPDATED_SUPPLIER_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySupplierProductIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where supplierProductId does not contain
        defaultSupplierProductFiltering(
            "supplierProductId.doesNotContain=" + UPDATED_SUPPLIER_PRODUCT_ID,
            "supplierProductId.doesNotContain=" + DEFAULT_SUPPLIER_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty equals to
        defaultSupplierProductFiltering("minOrderQty.equals=" + DEFAULT_MIN_ORDER_QTY, "minOrderQty.equals=" + UPDATED_MIN_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty in
        defaultSupplierProductFiltering(
            "minOrderQty.in=" + DEFAULT_MIN_ORDER_QTY + "," + UPDATED_MIN_ORDER_QTY,
            "minOrderQty.in=" + UPDATED_MIN_ORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty is not null
        defaultSupplierProductFiltering("minOrderQty.specified=true", "minOrderQty.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty is greater than or equal to
        defaultSupplierProductFiltering(
            "minOrderQty.greaterThanOrEqual=" + DEFAULT_MIN_ORDER_QTY,
            "minOrderQty.greaterThanOrEqual=" + UPDATED_MIN_ORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty is less than or equal to
        defaultSupplierProductFiltering(
            "minOrderQty.lessThanOrEqual=" + DEFAULT_MIN_ORDER_QTY,
            "minOrderQty.lessThanOrEqual=" + SMALLER_MIN_ORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty is less than
        defaultSupplierProductFiltering("minOrderQty.lessThan=" + UPDATED_MIN_ORDER_QTY, "minOrderQty.lessThan=" + DEFAULT_MIN_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByMinOrderQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where minOrderQty is greater than
        defaultSupplierProductFiltering(
            "minOrderQty.greaterThan=" + SMALLER_MIN_ORDER_QTY,
            "minOrderQty.greaterThan=" + DEFAULT_MIN_ORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice equals to
        defaultSupplierProductFiltering("lastPrice.equals=" + DEFAULT_LAST_PRICE, "lastPrice.equals=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice in
        defaultSupplierProductFiltering(
            "lastPrice.in=" + DEFAULT_LAST_PRICE + "," + UPDATED_LAST_PRICE,
            "lastPrice.in=" + UPDATED_LAST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice is not null
        defaultSupplierProductFiltering("lastPrice.specified=true", "lastPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice is greater than or equal to
        defaultSupplierProductFiltering(
            "lastPrice.greaterThanOrEqual=" + DEFAULT_LAST_PRICE,
            "lastPrice.greaterThanOrEqual=" + UPDATED_LAST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice is less than or equal to
        defaultSupplierProductFiltering(
            "lastPrice.lessThanOrEqual=" + DEFAULT_LAST_PRICE,
            "lastPrice.lessThanOrEqual=" + SMALLER_LAST_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice is less than
        defaultSupplierProductFiltering("lastPrice.lessThan=" + UPDATED_LAST_PRICE, "lastPrice.lessThan=" + DEFAULT_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLastPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where lastPrice is greater than
        defaultSupplierProductFiltering("lastPrice.greaterThan=" + SMALLER_LAST_PRICE, "lastPrice.greaterThan=" + DEFAULT_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice equals to
        defaultSupplierProductFiltering("shippingPrice.equals=" + DEFAULT_SHIPPING_PRICE, "shippingPrice.equals=" + UPDATED_SHIPPING_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice in
        defaultSupplierProductFiltering(
            "shippingPrice.in=" + DEFAULT_SHIPPING_PRICE + "," + UPDATED_SHIPPING_PRICE,
            "shippingPrice.in=" + UPDATED_SHIPPING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice is not null
        defaultSupplierProductFiltering("shippingPrice.specified=true", "shippingPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice is greater than or equal to
        defaultSupplierProductFiltering(
            "shippingPrice.greaterThanOrEqual=" + DEFAULT_SHIPPING_PRICE,
            "shippingPrice.greaterThanOrEqual=" + UPDATED_SHIPPING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice is less than or equal to
        defaultSupplierProductFiltering(
            "shippingPrice.lessThanOrEqual=" + DEFAULT_SHIPPING_PRICE,
            "shippingPrice.lessThanOrEqual=" + SMALLER_SHIPPING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice is less than
        defaultSupplierProductFiltering(
            "shippingPrice.lessThan=" + UPDATED_SHIPPING_PRICE,
            "shippingPrice.lessThan=" + DEFAULT_SHIPPING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByShippingPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where shippingPrice is greater than
        defaultSupplierProductFiltering(
            "shippingPrice.greaterThan=" + SMALLER_SHIPPING_PRICE,
            "shippingPrice.greaterThan=" + DEFAULT_SHIPPING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst equals to
        defaultSupplierProductFiltering("cgst.equals=" + DEFAULT_CGST, "cgst.equals=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst in
        defaultSupplierProductFiltering("cgst.in=" + DEFAULT_CGST + "," + UPDATED_CGST, "cgst.in=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst is not null
        defaultSupplierProductFiltering("cgst.specified=true", "cgst.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst is greater than or equal to
        defaultSupplierProductFiltering("cgst.greaterThanOrEqual=" + DEFAULT_CGST, "cgst.greaterThanOrEqual=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst is less than or equal to
        defaultSupplierProductFiltering("cgst.lessThanOrEqual=" + DEFAULT_CGST, "cgst.lessThanOrEqual=" + SMALLER_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst is less than
        defaultSupplierProductFiltering("cgst.lessThan=" + UPDATED_CGST, "cgst.lessThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where cgst is greater than
        defaultSupplierProductFiltering("cgst.greaterThan=" + SMALLER_CGST, "cgst.greaterThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst equals to
        defaultSupplierProductFiltering("igst.equals=" + DEFAULT_IGST, "igst.equals=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst in
        defaultSupplierProductFiltering("igst.in=" + DEFAULT_IGST + "," + UPDATED_IGST, "igst.in=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst is not null
        defaultSupplierProductFiltering("igst.specified=true", "igst.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst is greater than or equal to
        defaultSupplierProductFiltering("igst.greaterThanOrEqual=" + DEFAULT_IGST, "igst.greaterThanOrEqual=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst is less than or equal to
        defaultSupplierProductFiltering("igst.lessThanOrEqual=" + DEFAULT_IGST, "igst.lessThanOrEqual=" + SMALLER_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst is less than
        defaultSupplierProductFiltering("igst.lessThan=" + UPDATED_IGST, "igst.lessThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByIgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where igst is greater than
        defaultSupplierProductFiltering("igst.greaterThan=" + SMALLER_IGST, "igst.greaterThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst equals to
        defaultSupplierProductFiltering("sgst.equals=" + DEFAULT_SGST, "sgst.equals=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst in
        defaultSupplierProductFiltering("sgst.in=" + DEFAULT_SGST + "," + UPDATED_SGST, "sgst.in=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst is not null
        defaultSupplierProductFiltering("sgst.specified=true", "sgst.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst is greater than or equal to
        defaultSupplierProductFiltering("sgst.greaterThanOrEqual=" + DEFAULT_SGST, "sgst.greaterThanOrEqual=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst is less than or equal to
        defaultSupplierProductFiltering("sgst.lessThanOrEqual=" + DEFAULT_SGST, "sgst.lessThanOrEqual=" + SMALLER_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst is less than
        defaultSupplierProductFiltering("sgst.lessThan=" + UPDATED_SGST, "sgst.lessThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsBySgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where sgst is greater than
        defaultSupplierProductFiltering("sgst.greaterThan=" + SMALLER_SGST, "sgst.greaterThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice equals to
        defaultSupplierProductFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice in
        defaultSupplierProductFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice is not null
        defaultSupplierProductFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice is greater than or equal to
        defaultSupplierProductFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice is less than or equal to
        defaultSupplierProductFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice is less than
        defaultSupplierProductFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where totalPrice is greater than
        defaultSupplierProductFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where comments equals to
        defaultSupplierProductFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where comments in
        defaultSupplierProductFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where comments is not null
        defaultSupplierProductFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where comments contains
        defaultSupplierProductFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where comments does not contain
        defaultSupplierProductFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays equals to
        defaultSupplierProductFiltering("leadTimeDays.equals=" + DEFAULT_LEAD_TIME_DAYS, "leadTimeDays.equals=" + UPDATED_LEAD_TIME_DAYS);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays in
        defaultSupplierProductFiltering(
            "leadTimeDays.in=" + DEFAULT_LEAD_TIME_DAYS + "," + UPDATED_LEAD_TIME_DAYS,
            "leadTimeDays.in=" + UPDATED_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays is not null
        defaultSupplierProductFiltering("leadTimeDays.specified=true", "leadTimeDays.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays is greater than or equal to
        defaultSupplierProductFiltering(
            "leadTimeDays.greaterThanOrEqual=" + DEFAULT_LEAD_TIME_DAYS,
            "leadTimeDays.greaterThanOrEqual=" + UPDATED_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays is less than or equal to
        defaultSupplierProductFiltering(
            "leadTimeDays.lessThanOrEqual=" + DEFAULT_LEAD_TIME_DAYS,
            "leadTimeDays.lessThanOrEqual=" + SMALLER_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays is less than
        defaultSupplierProductFiltering(
            "leadTimeDays.lessThan=" + UPDATED_LEAD_TIME_DAYS,
            "leadTimeDays.lessThan=" + DEFAULT_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByLeadTimeDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where leadTimeDays is greater than
        defaultSupplierProductFiltering(
            "leadTimeDays.greaterThan=" + SMALLER_LEAD_TIME_DAYS,
            "leadTimeDays.greaterThan=" + DEFAULT_LEAD_TIME_DAYS
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded equals to
        defaultSupplierProductFiltering("unitsIncluded.equals=" + DEFAULT_UNITS_INCLUDED, "unitsIncluded.equals=" + UPDATED_UNITS_INCLUDED);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded in
        defaultSupplierProductFiltering(
            "unitsIncluded.in=" + DEFAULT_UNITS_INCLUDED + "," + UPDATED_UNITS_INCLUDED,
            "unitsIncluded.in=" + UPDATED_UNITS_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded is not null
        defaultSupplierProductFiltering("unitsIncluded.specified=true", "unitsIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded is greater than or equal to
        defaultSupplierProductFiltering(
            "unitsIncluded.greaterThanOrEqual=" + DEFAULT_UNITS_INCLUDED,
            "unitsIncluded.greaterThanOrEqual=" + UPDATED_UNITS_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded is less than or equal to
        defaultSupplierProductFiltering(
            "unitsIncluded.lessThanOrEqual=" + DEFAULT_UNITS_INCLUDED,
            "unitsIncluded.lessThanOrEqual=" + SMALLER_UNITS_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded is less than
        defaultSupplierProductFiltering(
            "unitsIncluded.lessThan=" + UPDATED_UNITS_INCLUDED,
            "unitsIncluded.lessThan=" + DEFAULT_UNITS_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByUnitsIncludedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where unitsIncluded is greater than
        defaultSupplierProductFiltering(
            "unitsIncluded.greaterThan=" + SMALLER_UNITS_INCLUDED,
            "unitsIncluded.greaterThan=" + DEFAULT_UNITS_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCanDropShipIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where canDropShip equals to
        defaultSupplierProductFiltering("canDropShip.equals=" + DEFAULT_CAN_DROP_SHIP, "canDropShip.equals=" + UPDATED_CAN_DROP_SHIP);
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCanDropShipIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where canDropShip in
        defaultSupplierProductFiltering(
            "canDropShip.in=" + DEFAULT_CAN_DROP_SHIP + "," + UPDATED_CAN_DROP_SHIP,
            "canDropShip.in=" + UPDATED_CAN_DROP_SHIP
        );
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCanDropShipIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        // Get all the supplierProductList where canDropShip is not null
        defaultSupplierProductFiltering("canDropShip.specified=true", "canDropShip.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplierProductsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            supplierProductRepository.saveAndFlush(supplierProduct);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        supplierProduct.setProduct(product);
        supplierProductRepository.saveAndFlush(supplierProduct);
        Long productId = product.getId();
        // Get all the supplierProductList where product equals to productId
        defaultSupplierProductShouldBeFound("productId.equals=" + productId);

        // Get all the supplierProductList where product equals to (productId + 1)
        defaultSupplierProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierProductsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            supplierProductRepository.saveAndFlush(supplierProduct);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        supplierProduct.setParty(party);
        supplierProductRepository.saveAndFlush(supplierProduct);
        Long partyId = party.getId();
        // Get all the supplierProductList where party equals to partyId
        defaultSupplierProductShouldBeFound("partyId.equals=" + partyId);

        // Get all the supplierProductList where party equals to (partyId + 1)
        defaultSupplierProductShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierProductsByQuantityUomIsEqualToSomething() throws Exception {
        Uom quantityUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            supplierProductRepository.saveAndFlush(supplierProduct);
            quantityUom = UomResourceIT.createEntity();
        } else {
            quantityUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(quantityUom);
        em.flush();
        supplierProduct.setQuantityUom(quantityUom);
        supplierProductRepository.saveAndFlush(supplierProduct);
        Long quantityUomId = quantityUom.getId();
        // Get all the supplierProductList where quantityUom equals to quantityUomId
        defaultSupplierProductShouldBeFound("quantityUomId.equals=" + quantityUomId);

        // Get all the supplierProductList where quantityUom equals to (quantityUomId + 1)
        defaultSupplierProductShouldNotBeFound("quantityUomId.equals=" + (quantityUomId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierProductsByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            supplierProductRepository.saveAndFlush(supplierProduct);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        supplierProduct.setCurrencyUom(currencyUom);
        supplierProductRepository.saveAndFlush(supplierProduct);
        Long currencyUomId = currencyUom.getId();
        // Get all the supplierProductList where currencyUom equals to currencyUomId
        defaultSupplierProductShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the supplierProductList where currencyUom equals to (currencyUomId + 1)
        defaultSupplierProductShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultSupplierProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierProductShouldBeFound(shouldBeFound);
        defaultSupplierProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierProductShouldBeFound(String filter) throws Exception {
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].supplierProductId").value(hasItem(DEFAULT_SUPPLIER_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].minOrderQty").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_QTY))))
            .andExpect(jsonPath("$.[*].lastPrice").value(hasItem(sameNumber(DEFAULT_LAST_PRICE))))
            .andExpect(jsonPath("$.[*].shippingPrice").value(hasItem(sameNumber(DEFAULT_SHIPPING_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].leadTimeDays").value(hasItem(DEFAULT_LEAD_TIME_DAYS)))
            .andExpect(jsonPath("$.[*].unitsIncluded").value(hasItem(sameNumber(DEFAULT_UNITS_INCLUDED))))
            .andExpect(jsonPath("$.[*].canDropShip").value(hasItem(DEFAULT_CAN_DROP_SHIP)));

        // Check, that the count call also returns 1
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierProductShouldNotBeFound(String filter) throws Exception {
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierProduct() throws Exception {
        // Get the supplierProduct
        restSupplierProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierProduct() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierProduct
        SupplierProduct updatedSupplierProduct = supplierProductRepository.findById(supplierProduct.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierProduct are not directly saved in db
        em.detach(updatedSupplierProduct);
        updatedSupplierProduct
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .supplierProductId(UPDATED_SUPPLIER_PRODUCT_ID)
            .minOrderQty(UPDATED_MIN_ORDER_QTY)
            .lastPrice(UPDATED_LAST_PRICE)
            .shippingPrice(UPDATED_SHIPPING_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .comments(UPDATED_COMMENTS)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .unitsIncluded(UPDATED_UNITS_INCLUDED)
            .canDropShip(UPDATED_CAN_DROP_SHIP);

        restSupplierProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplierProduct))
            )
            .andExpect(status().isOk());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierProductToMatchAllProperties(updatedSupplierProduct);
    }

    @Test
    @Transactional
    void putNonExistingSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierProductWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierProduct using partial update
        SupplierProduct partialUpdatedSupplierProduct = new SupplierProduct();
        partialUpdatedSupplierProduct.setId(supplierProduct.getId());

        partialUpdatedSupplierProduct
            .thruDate(UPDATED_THRU_DATE)
            .lastPrice(UPDATED_LAST_PRICE)
            .shippingPrice(UPDATED_SHIPPING_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .comments(UPDATED_COMMENTS)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .canDropShip(UPDATED_CAN_DROP_SHIP);

        restSupplierProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierProduct))
            )
            .andExpect(status().isOk());

        // Validate the SupplierProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierProductUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierProduct, supplierProduct),
            getPersistedSupplierProduct(supplierProduct)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierProductWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierProduct using partial update
        SupplierProduct partialUpdatedSupplierProduct = new SupplierProduct();
        partialUpdatedSupplierProduct.setId(supplierProduct.getId());

        partialUpdatedSupplierProduct
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .supplierProductId(UPDATED_SUPPLIER_PRODUCT_ID)
            .minOrderQty(UPDATED_MIN_ORDER_QTY)
            .lastPrice(UPDATED_LAST_PRICE)
            .shippingPrice(UPDATED_SHIPPING_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .comments(UPDATED_COMMENTS)
            .leadTimeDays(UPDATED_LEAD_TIME_DAYS)
            .unitsIncluded(UPDATED_UNITS_INCLUDED)
            .canDropShip(UPDATED_CAN_DROP_SHIP);

        restSupplierProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierProduct))
            )
            .andExpect(status().isOk());

        // Validate the SupplierProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierProductUpdatableFieldsEquals(
            partialUpdatedSupplierProduct,
            getPersistedSupplierProduct(partialUpdatedSupplierProduct)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierProduct() throws Exception {
        // Initialize the database
        insertedSupplierProduct = supplierProductRepository.saveAndFlush(supplierProduct);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierProduct
        restSupplierProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierProductRepository.count();
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

    protected SupplierProduct getPersistedSupplierProduct(SupplierProduct supplierProduct) {
        return supplierProductRepository.findById(supplierProduct.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierProductToMatchAllProperties(SupplierProduct expectedSupplierProduct) {
        assertSupplierProductAllPropertiesEquals(expectedSupplierProduct, getPersistedSupplierProduct(expectedSupplierProduct));
    }

    protected void assertPersistedSupplierProductToMatchUpdatableProperties(SupplierProduct expectedSupplierProduct) {
        assertSupplierProductAllUpdatablePropertiesEquals(expectedSupplierProduct, getPersistedSupplierProduct(expectedSupplierProduct));
    }
}
