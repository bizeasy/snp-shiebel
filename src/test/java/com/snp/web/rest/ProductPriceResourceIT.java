package com.snp.web.rest;

import static com.snp.domain.ProductPriceAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.ProductPrice;
import com.snp.domain.ProductPricePurpose;
import com.snp.domain.ProductPriceType;
import com.snp.domain.Uom;
import com.snp.repository.ProductPriceRepository;
import com.snp.service.ProductPriceService;
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
 * Integration tests for the {@link ProductPriceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductPriceResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

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

    private static final String ENTITY_API_URL = "/api/product-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Mock
    private ProductPriceRepository productPriceRepositoryMock;

    @Mock
    private ProductPriceService productPriceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPriceMockMvc;

    private ProductPrice productPrice;

    private ProductPrice insertedProductPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createEntity(EntityManager em) {
        ProductPrice productPrice = new ProductPrice()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .price(DEFAULT_PRICE)
            .cgst(DEFAULT_CGST)
            .igst(DEFAULT_IGST)
            .sgst(DEFAULT_SGST)
            .totalPrice(DEFAULT_TOTAL_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productPrice.setProduct(product);
        return productPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createUpdatedEntity(EntityManager em) {
        ProductPrice updatedProductPrice = new ProductPrice()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductPrice.setProduct(product);
        return updatedProductPrice;
    }

    @BeforeEach
    void initTest() {
        productPrice = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductPrice != null) {
            productPriceRepository.delete(insertedProductPrice);
            insertedProductPrice = null;
        }
    }

    @Test
    @Transactional
    void createProductPrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductPrice
        var returnedProductPrice = om.readValue(
            restProductPriceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPrice)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductPrice.class
        );

        // Validate the ProductPrice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductPriceUpdatableFieldsEquals(returnedProductPrice, getPersistedProductPrice(returnedProductPrice));

        insertedProductPrice = returnedProductPrice;
    }

    @Test
    @Transactional
    void createProductPriceWithExistingId() throws Exception {
        // Create the ProductPrice with an existing ID
        productPrice.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPrice)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productPrice.setFromDate(null);

        // Create the ProductPrice, which fails.

        restProductPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPrice)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productPrice.setPrice(null);

        // Create the ProductPrice, which fails.

        restProductPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPrice)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductPrices() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductPricesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductPriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productPriceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductPricesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductPriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productPriceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductPrice() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get the productPrice
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, productPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPrice.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.cgst").value(sameNumber(DEFAULT_CGST)))
            .andExpect(jsonPath("$.igst").value(sameNumber(DEFAULT_IGST)))
            .andExpect(jsonPath("$.sgst").value(sameNumber(DEFAULT_SGST)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    void getProductPricesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        Long id = productPrice.getId();

        defaultProductPriceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductPriceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductPriceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductPricesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where fromDate equals to
        defaultProductPriceFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductPricesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where fromDate in
        defaultProductPriceFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductPricesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where fromDate is not null
        defaultProductPriceFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where thruDate equals to
        defaultProductPriceFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductPricesByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where thruDate in
        defaultProductPriceFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductPricesByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where thruDate is not null
        defaultProductPriceFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price equals to
        defaultProductPriceFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price in
        defaultProductPriceFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price is not null
        defaultProductPriceFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price is greater than or equal to
        defaultProductPriceFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price is less than or equal to
        defaultProductPriceFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price is less than
        defaultProductPriceFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where price is greater than
        defaultProductPriceFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst equals to
        defaultProductPriceFiltering("cgst.equals=" + DEFAULT_CGST, "cgst.equals=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst in
        defaultProductPriceFiltering("cgst.in=" + DEFAULT_CGST + "," + UPDATED_CGST, "cgst.in=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst is not null
        defaultProductPriceFiltering("cgst.specified=true", "cgst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst is greater than or equal to
        defaultProductPriceFiltering("cgst.greaterThanOrEqual=" + DEFAULT_CGST, "cgst.greaterThanOrEqual=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst is less than or equal to
        defaultProductPriceFiltering("cgst.lessThanOrEqual=" + DEFAULT_CGST, "cgst.lessThanOrEqual=" + SMALLER_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst is less than
        defaultProductPriceFiltering("cgst.lessThan=" + UPDATED_CGST, "cgst.lessThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByCgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where cgst is greater than
        defaultProductPriceFiltering("cgst.greaterThan=" + SMALLER_CGST, "cgst.greaterThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst equals to
        defaultProductPriceFiltering("igst.equals=" + DEFAULT_IGST, "igst.equals=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst in
        defaultProductPriceFiltering("igst.in=" + DEFAULT_IGST + "," + UPDATED_IGST, "igst.in=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst is not null
        defaultProductPriceFiltering("igst.specified=true", "igst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst is greater than or equal to
        defaultProductPriceFiltering("igst.greaterThanOrEqual=" + DEFAULT_IGST, "igst.greaterThanOrEqual=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst is less than or equal to
        defaultProductPriceFiltering("igst.lessThanOrEqual=" + DEFAULT_IGST, "igst.lessThanOrEqual=" + SMALLER_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst is less than
        defaultProductPriceFiltering("igst.lessThan=" + UPDATED_IGST, "igst.lessThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByIgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where igst is greater than
        defaultProductPriceFiltering("igst.greaterThan=" + SMALLER_IGST, "igst.greaterThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst equals to
        defaultProductPriceFiltering("sgst.equals=" + DEFAULT_SGST, "sgst.equals=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst in
        defaultProductPriceFiltering("sgst.in=" + DEFAULT_SGST + "," + UPDATED_SGST, "sgst.in=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst is not null
        defaultProductPriceFiltering("sgst.specified=true", "sgst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst is greater than or equal to
        defaultProductPriceFiltering("sgst.greaterThanOrEqual=" + DEFAULT_SGST, "sgst.greaterThanOrEqual=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst is less than or equal to
        defaultProductPriceFiltering("sgst.lessThanOrEqual=" + DEFAULT_SGST, "sgst.lessThanOrEqual=" + SMALLER_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst is less than
        defaultProductPriceFiltering("sgst.lessThan=" + UPDATED_SGST, "sgst.lessThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesBySgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where sgst is greater than
        defaultProductPriceFiltering("sgst.greaterThan=" + SMALLER_SGST, "sgst.greaterThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice equals to
        defaultProductPriceFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice in
        defaultProductPriceFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice is not null
        defaultProductPriceFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice is greater than or equal to
        defaultProductPriceFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice is less than or equal to
        defaultProductPriceFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice is less than
        defaultProductPriceFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList where totalPrice is greater than
        defaultProductPriceFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductPricesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productPriceRepository.saveAndFlush(productPrice);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productPrice.setProduct(product);
        productPriceRepository.saveAndFlush(productPrice);
        Long productId = product.getId();
        // Get all the productPriceList where product equals to productId
        defaultProductPriceShouldBeFound("productId.equals=" + productId);

        // Get all the productPriceList where product equals to (productId + 1)
        defaultProductPriceShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductPricesByProductPriceTypeIsEqualToSomething() throws Exception {
        ProductPriceType productPriceType;
        if (TestUtil.findAll(em, ProductPriceType.class).isEmpty()) {
            productPriceRepository.saveAndFlush(productPrice);
            productPriceType = ProductPriceTypeResourceIT.createEntity();
        } else {
            productPriceType = TestUtil.findAll(em, ProductPriceType.class).getFirst();
        }
        em.persist(productPriceType);
        em.flush();
        productPrice.setProductPriceType(productPriceType);
        productPriceRepository.saveAndFlush(productPrice);
        Long productPriceTypeId = productPriceType.getId();
        // Get all the productPriceList where productPriceType equals to productPriceTypeId
        defaultProductPriceShouldBeFound("productPriceTypeId.equals=" + productPriceTypeId);

        // Get all the productPriceList where productPriceType equals to (productPriceTypeId + 1)
        defaultProductPriceShouldNotBeFound("productPriceTypeId.equals=" + (productPriceTypeId + 1));
    }

    @Test
    @Transactional
    void getAllProductPricesByProductPricePurposeIsEqualToSomething() throws Exception {
        ProductPricePurpose productPricePurpose;
        if (TestUtil.findAll(em, ProductPricePurpose.class).isEmpty()) {
            productPriceRepository.saveAndFlush(productPrice);
            productPricePurpose = ProductPricePurposeResourceIT.createEntity();
        } else {
            productPricePurpose = TestUtil.findAll(em, ProductPricePurpose.class).getFirst();
        }
        em.persist(productPricePurpose);
        em.flush();
        productPrice.setProductPricePurpose(productPricePurpose);
        productPriceRepository.saveAndFlush(productPrice);
        Long productPricePurposeId = productPricePurpose.getId();
        // Get all the productPriceList where productPricePurpose equals to productPricePurposeId
        defaultProductPriceShouldBeFound("productPricePurposeId.equals=" + productPricePurposeId);

        // Get all the productPriceList where productPricePurpose equals to (productPricePurposeId + 1)
        defaultProductPriceShouldNotBeFound("productPricePurposeId.equals=" + (productPricePurposeId + 1));
    }

    @Test
    @Transactional
    void getAllProductPricesByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            productPriceRepository.saveAndFlush(productPrice);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        productPrice.setCurrencyUom(currencyUom);
        productPriceRepository.saveAndFlush(productPrice);
        Long currencyUomId = currencyUom.getId();
        // Get all the productPriceList where currencyUom equals to currencyUomId
        defaultProductPriceShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the productPriceList where currencyUom equals to (currencyUomId + 1)
        defaultProductPriceShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultProductPriceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductPriceShouldBeFound(shouldBeFound);
        defaultProductPriceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductPriceShouldBeFound(String filter) throws Exception {
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));

        // Check, that the count call also returns 1
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductPriceShouldNotBeFound(String filter) throws Exception {
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductPrice() throws Exception {
        // Get the productPrice
        restProductPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPrice() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPrice
        ProductPrice updatedProductPrice = productPriceRepository.findById(productPrice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductPrice are not directly saved in db
        em.detach(updatedProductPrice);
        updatedProductPrice
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductPriceToMatchAllProperties(updatedProductPrice);
    }

    @Test
    @Transactional
    void putNonExistingProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice.fromDate(UPDATED_FROM_DATE).cgst(UPDATED_CGST).igst(UPDATED_IGST);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPriceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductPrice, productPrice),
            getPersistedProductPrice(productPrice)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPriceUpdatableFieldsEquals(partialUpdatedProductPrice, getPersistedProductPrice(partialUpdatedProductPrice));
    }

    @Test
    @Transactional
    void patchNonExistingProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPrice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPrice() throws Exception {
        // Initialize the database
        insertedProductPrice = productPriceRepository.saveAndFlush(productPrice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productPrice
        restProductPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productPriceRepository.count();
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

    protected ProductPrice getPersistedProductPrice(ProductPrice productPrice) {
        return productPriceRepository.findById(productPrice.getId()).orElseThrow();
    }

    protected void assertPersistedProductPriceToMatchAllProperties(ProductPrice expectedProductPrice) {
        assertProductPriceAllPropertiesEquals(expectedProductPrice, getPersistedProductPrice(expectedProductPrice));
    }

    protected void assertPersistedProductPriceToMatchUpdatableProperties(ProductPrice expectedProductPrice) {
        assertProductPriceAllUpdatablePropertiesEquals(expectedProductPrice, getPersistedProductPrice(expectedProductPrice));
    }
}
