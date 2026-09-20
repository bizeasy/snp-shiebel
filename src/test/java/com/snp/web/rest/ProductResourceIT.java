package com.snp.web.rest;

import static com.snp.domain.ProductAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItemType;
import com.snp.domain.Product;
import com.snp.domain.Product;
import com.snp.domain.ProductCategory;
import com.snp.domain.ProductType;
import com.snp.domain.TaxSlab;
import com.snp.domain.Uom;
import com.snp.repository.ProductRepository;
import com.snp.service.ProductService;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INTERNAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VARIANT = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final Instant DEFAULT_INTRODUCTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INTRODUCTION_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_QUANTITY_INCLUDED = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_INCLUDED = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_INCLUDED = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_PIECES_INCLUDED = 1;
    private static final Integer UPDATED_PIECES_INCLUDED = 2;
    private static final Integer SMALLER_PIECES_INCLUDED = 1 - 1;

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WEIGHT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_HEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_HEIGHT = new BigDecimal(2);
    private static final BigDecimal SMALLER_HEIGHT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_WIDTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_WIDTH = new BigDecimal(2);
    private static final BigDecimal SMALLER_WIDTH = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEPTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPTH = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPTH = new BigDecimal(1 - 1);

    private static final String DEFAULT_SMALL_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SMALL_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIUM_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LARGE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_LARGE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_1 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_2 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_3 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_3 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_4 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_4 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_5 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_5 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL_6 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL_6 = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

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

    private static final BigDecimal DEFAULT_CGST_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CGST_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CGST_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_IGST_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_IGST_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_IGST_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SGST_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SGST_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SGST_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VIRTUAL = false;
    private static final Boolean UPDATED_IS_VIRTUAL = true;

    private static final Boolean DEFAULT_IS_VARIANT = false;
    private static final Boolean UPDATED_IS_VARIANT = true;

    private static final Boolean DEFAULT_REQUIRE_INVENTORY = false;
    private static final Boolean UPDATED_REQUIRE_INVENTORY = true;

    private static final Boolean DEFAULT_RETURNABLE = false;
    private static final Boolean UPDATED_RETURNABLE = true;

    private static final Boolean DEFAULT_IS_POPULAR = false;
    private static final Boolean UPDATED_IS_POPULAR = true;

    private static final Boolean DEFAULT_IS_BANNER = false;
    private static final Boolean UPDATED_IS_BANNER = true;

    private static final String DEFAULT_BANNER_URL = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        return new Product()
            .name(DEFAULT_NAME)
            .internalName(DEFAULT_INTERNAL_NAME)
            .brandName(DEFAULT_BRAND_NAME)
            .variant(DEFAULT_VARIANT)
            .sku(DEFAULT_SKU)
            .introductionDate(DEFAULT_INTRODUCTION_DATE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .quantityIncluded(DEFAULT_QUANTITY_INCLUDED)
            .piecesIncluded(DEFAULT_PIECES_INCLUDED)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH)
            .depth(DEFAULT_DEPTH)
            .smallImageUrl(DEFAULT_SMALL_IMAGE_URL)
            .mediumImageUrl(DEFAULT_MEDIUM_IMAGE_URL)
            .largeImageUrl(DEFAULT_LARGE_IMAGE_URL)
            .detailImageUrl(DEFAULT_DETAIL_IMAGE_URL)
            .originalImageUrl(DEFAULT_ORIGINAL_IMAGE_URL)
            .altImageUrl1(DEFAULT_ALT_IMAGE_URL_1)
            .altImageUrl2(DEFAULT_ALT_IMAGE_URL_2)
            .altImageUrl3(DEFAULT_ALT_IMAGE_URL_3)
            .altImageUrl4(DEFAULT_ALT_IMAGE_URL_4)
            .altImageUrl5(DEFAULT_ALT_IMAGE_URL_5)
            .altImageUrl6(DEFAULT_ALT_IMAGE_URL_6)
            .videoUrl(DEFAULT_VIDEO_URL)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .cgst(DEFAULT_CGST)
            .igst(DEFAULT_IGST)
            .sgst(DEFAULT_SGST)
            .cgstPercentage(DEFAULT_CGST_PERCENTAGE)
            .igstPercentage(DEFAULT_IGST_PERCENTAGE)
            .sgstPercentage(DEFAULT_SGST_PERCENTAGE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .description(DEFAULT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .info(DEFAULT_INFO)
            .isVirtual(DEFAULT_IS_VIRTUAL)
            .isVariant(DEFAULT_IS_VARIANT)
            .requireInventory(DEFAULT_REQUIRE_INVENTORY)
            .returnable(DEFAULT_RETURNABLE)
            .isPopular(DEFAULT_IS_POPULAR)
            .isBanner(DEFAULT_IS_BANNER)
            .bannerUrl(DEFAULT_BANNER_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .name(UPDATED_NAME)
            .internalName(UPDATED_INTERNAL_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .variant(UPDATED_VARIANT)
            .sku(UPDATED_SKU)
            .introductionDate(UPDATED_INTRODUCTION_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .quantityIncluded(UPDATED_QUANTITY_INCLUDED)
            .piecesIncluded(UPDATED_PIECES_INCLUDED)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .depth(UPDATED_DEPTH)
            .smallImageUrl(UPDATED_SMALL_IMAGE_URL)
            .mediumImageUrl(UPDATED_MEDIUM_IMAGE_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .detailImageUrl(UPDATED_DETAIL_IMAGE_URL)
            .originalImageUrl(UPDATED_ORIGINAL_IMAGE_URL)
            .altImageUrl1(UPDATED_ALT_IMAGE_URL_1)
            .altImageUrl2(UPDATED_ALT_IMAGE_URL_2)
            .altImageUrl3(UPDATED_ALT_IMAGE_URL_3)
            .altImageUrl4(UPDATED_ALT_IMAGE_URL_4)
            .altImageUrl5(UPDATED_ALT_IMAGE_URL_5)
            .altImageUrl6(UPDATED_ALT_IMAGE_URL_6)
            .videoUrl(UPDATED_VIDEO_URL)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .cgstPercentage(UPDATED_CGST_PERCENTAGE)
            .igstPercentage(UPDATED_IGST_PERCENTAGE)
            .sgstPercentage(UPDATED_SGST_PERCENTAGE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .info(UPDATED_INFO)
            .isVirtual(UPDATED_IS_VIRTUAL)
            .isVariant(UPDATED_IS_VARIANT)
            .requireInventory(UPDATED_REQUIRE_INVENTORY)
            .returnable(UPDATED_RETURNABLE)
            .isPopular(UPDATED_IS_POPULAR)
            .isBanner(UPDATED_IS_BANNER)
            .bannerUrl(UPDATED_BANNER_URL);
    }

    @BeforeEach
    void initTest() {
        product = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        var returnedProduct = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Product.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].internalName").value(hasItem(DEFAULT_INTERNAL_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].variant").value(hasItem(DEFAULT_VARIANT)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].introductionDate").value(hasItem(DEFAULT_INTRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantityIncluded").value(hasItem(sameNumber(DEFAULT_QUANTITY_INCLUDED))))
            .andExpect(jsonPath("$.[*].piecesIncluded").value(hasItem(DEFAULT_PIECES_INCLUDED)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(sameNumber(DEFAULT_HEIGHT))))
            .andExpect(jsonPath("$.[*].width").value(hasItem(sameNumber(DEFAULT_WIDTH))))
            .andExpect(jsonPath("$.[*].depth").value(hasItem(sameNumber(DEFAULT_DEPTH))))
            .andExpect(jsonPath("$.[*].smallImageUrl").value(hasItem(DEFAULT_SMALL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].mediumImageUrl").value(hasItem(DEFAULT_MEDIUM_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].largeImageUrl").value(hasItem(DEFAULT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].detailImageUrl").value(hasItem(DEFAULT_DETAIL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].originalImageUrl").value(hasItem(DEFAULT_ORIGINAL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altImageUrl1").value(hasItem(DEFAULT_ALT_IMAGE_URL_1)))
            .andExpect(jsonPath("$.[*].altImageUrl2").value(hasItem(DEFAULT_ALT_IMAGE_URL_2)))
            .andExpect(jsonPath("$.[*].altImageUrl3").value(hasItem(DEFAULT_ALT_IMAGE_URL_3)))
            .andExpect(jsonPath("$.[*].altImageUrl4").value(hasItem(DEFAULT_ALT_IMAGE_URL_4)))
            .andExpect(jsonPath("$.[*].altImageUrl5").value(hasItem(DEFAULT_ALT_IMAGE_URL_5)))
            .andExpect(jsonPath("$.[*].altImageUrl6").value(hasItem(DEFAULT_ALT_IMAGE_URL_6)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].cgstPercentage").value(hasItem(sameNumber(DEFAULT_CGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].igstPercentage").value(hasItem(sameNumber(DEFAULT_IGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].sgstPercentage").value(hasItem(sameNumber(DEFAULT_SGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].isVirtual").value(hasItem(DEFAULT_IS_VIRTUAL)))
            .andExpect(jsonPath("$.[*].isVariant").value(hasItem(DEFAULT_IS_VARIANT)))
            .andExpect(jsonPath("$.[*].requireInventory").value(hasItem(DEFAULT_REQUIRE_INVENTORY)))
            .andExpect(jsonPath("$.[*].returnable").value(hasItem(DEFAULT_RETURNABLE)))
            .andExpect(jsonPath("$.[*].isPopular").value(hasItem(DEFAULT_IS_POPULAR)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.internalName").value(DEFAULT_INTERNAL_NAME))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.variant").value(DEFAULT_VARIANT))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.introductionDate").value(DEFAULT_INTRODUCTION_DATE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.quantityIncluded").value(sameNumber(DEFAULT_QUANTITY_INCLUDED)))
            .andExpect(jsonPath("$.piecesIncluded").value(DEFAULT_PIECES_INCLUDED))
            .andExpect(jsonPath("$.weight").value(sameNumber(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.height").value(sameNumber(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.width").value(sameNumber(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.depth").value(sameNumber(DEFAULT_DEPTH)))
            .andExpect(jsonPath("$.smallImageUrl").value(DEFAULT_SMALL_IMAGE_URL))
            .andExpect(jsonPath("$.mediumImageUrl").value(DEFAULT_MEDIUM_IMAGE_URL))
            .andExpect(jsonPath("$.largeImageUrl").value(DEFAULT_LARGE_IMAGE_URL))
            .andExpect(jsonPath("$.detailImageUrl").value(DEFAULT_DETAIL_IMAGE_URL))
            .andExpect(jsonPath("$.originalImageUrl").value(DEFAULT_ORIGINAL_IMAGE_URL))
            .andExpect(jsonPath("$.altImageUrl1").value(DEFAULT_ALT_IMAGE_URL_1))
            .andExpect(jsonPath("$.altImageUrl2").value(DEFAULT_ALT_IMAGE_URL_2))
            .andExpect(jsonPath("$.altImageUrl3").value(DEFAULT_ALT_IMAGE_URL_3))
            .andExpect(jsonPath("$.altImageUrl4").value(DEFAULT_ALT_IMAGE_URL_4))
            .andExpect(jsonPath("$.altImageUrl5").value(DEFAULT_ALT_IMAGE_URL_5))
            .andExpect(jsonPath("$.altImageUrl6").value(DEFAULT_ALT_IMAGE_URL_6))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.cgst").value(sameNumber(DEFAULT_CGST)))
            .andExpect(jsonPath("$.igst").value(sameNumber(DEFAULT_IGST)))
            .andExpect(jsonPath("$.sgst").value(sameNumber(DEFAULT_SGST)))
            .andExpect(jsonPath("$.cgstPercentage").value(sameNumber(DEFAULT_CGST_PERCENTAGE)))
            .andExpect(jsonPath("$.igstPercentage").value(sameNumber(DEFAULT_IGST_PERCENTAGE)))
            .andExpect(jsonPath("$.sgstPercentage").value(sameNumber(DEFAULT_SGST_PERCENTAGE)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.isVirtual").value(DEFAULT_IS_VIRTUAL))
            .andExpect(jsonPath("$.isVariant").value(DEFAULT_IS_VARIANT))
            .andExpect(jsonPath("$.requireInventory").value(DEFAULT_REQUIRE_INVENTORY))
            .andExpect(jsonPath("$.returnable").value(DEFAULT_RETURNABLE))
            .andExpect(jsonPath("$.isPopular").value(DEFAULT_IS_POPULAR))
            .andExpect(jsonPath("$.isBanner").value(DEFAULT_IS_BANNER))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name equals to
        defaultProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name in
        defaultProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name contains
        defaultProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain
        defaultProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByInternalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where internalName equals to
        defaultProductFiltering("internalName.equals=" + DEFAULT_INTERNAL_NAME, "internalName.equals=" + UPDATED_INTERNAL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByInternalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where internalName in
        defaultProductFiltering(
            "internalName.in=" + DEFAULT_INTERNAL_NAME + "," + UPDATED_INTERNAL_NAME,
            "internalName.in=" + UPDATED_INTERNAL_NAME
        );
    }

    @Test
    @Transactional
    void getAllProductsByInternalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where internalName is not null
        defaultProductFiltering("internalName.specified=true", "internalName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByInternalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where internalName contains
        defaultProductFiltering("internalName.contains=" + DEFAULT_INTERNAL_NAME, "internalName.contains=" + UPDATED_INTERNAL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByInternalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where internalName does not contain
        defaultProductFiltering(
            "internalName.doesNotContain=" + UPDATED_INTERNAL_NAME,
            "internalName.doesNotContain=" + DEFAULT_INTERNAL_NAME
        );
    }

    @Test
    @Transactional
    void getAllProductsByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where brandName equals to
        defaultProductFiltering("brandName.equals=" + DEFAULT_BRAND_NAME, "brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where brandName in
        defaultProductFiltering("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME, "brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where brandName is not null
        defaultProductFiltering("brandName.specified=true", "brandName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where brandName contains
        defaultProductFiltering("brandName.contains=" + DEFAULT_BRAND_NAME, "brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where brandName does not contain
        defaultProductFiltering("brandName.doesNotContain=" + UPDATED_BRAND_NAME, "brandName.doesNotContain=" + DEFAULT_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where variant equals to
        defaultProductFiltering("variant.equals=" + DEFAULT_VARIANT, "variant.equals=" + UPDATED_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsByVariantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where variant in
        defaultProductFiltering("variant.in=" + DEFAULT_VARIANT + "," + UPDATED_VARIANT, "variant.in=" + UPDATED_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsByVariantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where variant is not null
        defaultProductFiltering("variant.specified=true", "variant.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByVariantContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where variant contains
        defaultProductFiltering("variant.contains=" + DEFAULT_VARIANT, "variant.contains=" + UPDATED_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsByVariantNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where variant does not contain
        defaultProductFiltering("variant.doesNotContain=" + UPDATED_VARIANT, "variant.doesNotContain=" + DEFAULT_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to
        defaultProductFiltering("sku.equals=" + DEFAULT_SKU, "sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku in
        defaultProductFiltering("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU, "sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductFiltering("sku.specified=true", "sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku contains
        defaultProductFiltering("sku.contains=" + DEFAULT_SKU, "sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain
        defaultProductFiltering("sku.doesNotContain=" + UPDATED_SKU, "sku.doesNotContain=" + DEFAULT_SKU);
    }

    @Test
    @Transactional
    void getAllProductsByIntroductionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where introductionDate equals to
        defaultProductFiltering(
            "introductionDate.equals=" + DEFAULT_INTRODUCTION_DATE,
            "introductionDate.equals=" + UPDATED_INTRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIntroductionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where introductionDate in
        defaultProductFiltering(
            "introductionDate.in=" + DEFAULT_INTRODUCTION_DATE + "," + UPDATED_INTRODUCTION_DATE,
            "introductionDate.in=" + UPDATED_INTRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIntroductionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where introductionDate is not null
        defaultProductFiltering("introductionDate.specified=true", "introductionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where releaseDate equals to
        defaultProductFiltering("releaseDate.equals=" + DEFAULT_RELEASE_DATE, "releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where releaseDate in
        defaultProductFiltering(
            "releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE,
            "releaseDate.in=" + UPDATED_RELEASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where releaseDate is not null
        defaultProductFiltering("releaseDate.specified=true", "releaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded equals to
        defaultProductFiltering(
            "quantityIncluded.equals=" + DEFAULT_QUANTITY_INCLUDED,
            "quantityIncluded.equals=" + UPDATED_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded in
        defaultProductFiltering(
            "quantityIncluded.in=" + DEFAULT_QUANTITY_INCLUDED + "," + UPDATED_QUANTITY_INCLUDED,
            "quantityIncluded.in=" + UPDATED_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded is not null
        defaultProductFiltering("quantityIncluded.specified=true", "quantityIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded is greater than or equal to
        defaultProductFiltering(
            "quantityIncluded.greaterThanOrEqual=" + DEFAULT_QUANTITY_INCLUDED,
            "quantityIncluded.greaterThanOrEqual=" + UPDATED_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded is less than or equal to
        defaultProductFiltering(
            "quantityIncluded.lessThanOrEqual=" + DEFAULT_QUANTITY_INCLUDED,
            "quantityIncluded.lessThanOrEqual=" + SMALLER_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded is less than
        defaultProductFiltering(
            "quantityIncluded.lessThan=" + UPDATED_QUANTITY_INCLUDED,
            "quantityIncluded.lessThan=" + DEFAULT_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIncludedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantityIncluded is greater than
        defaultProductFiltering(
            "quantityIncluded.greaterThan=" + SMALLER_QUANTITY_INCLUDED,
            "quantityIncluded.greaterThan=" + DEFAULT_QUANTITY_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded equals to
        defaultProductFiltering("piecesIncluded.equals=" + DEFAULT_PIECES_INCLUDED, "piecesIncluded.equals=" + UPDATED_PIECES_INCLUDED);
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded in
        defaultProductFiltering(
            "piecesIncluded.in=" + DEFAULT_PIECES_INCLUDED + "," + UPDATED_PIECES_INCLUDED,
            "piecesIncluded.in=" + UPDATED_PIECES_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded is not null
        defaultProductFiltering("piecesIncluded.specified=true", "piecesIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded is greater than or equal to
        defaultProductFiltering(
            "piecesIncluded.greaterThanOrEqual=" + DEFAULT_PIECES_INCLUDED,
            "piecesIncluded.greaterThanOrEqual=" + UPDATED_PIECES_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded is less than or equal to
        defaultProductFiltering(
            "piecesIncluded.lessThanOrEqual=" + DEFAULT_PIECES_INCLUDED,
            "piecesIncluded.lessThanOrEqual=" + SMALLER_PIECES_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded is less than
        defaultProductFiltering("piecesIncluded.lessThan=" + UPDATED_PIECES_INCLUDED, "piecesIncluded.lessThan=" + DEFAULT_PIECES_INCLUDED);
    }

    @Test
    @Transactional
    void getAllProductsByPiecesIncludedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where piecesIncluded is greater than
        defaultProductFiltering(
            "piecesIncluded.greaterThan=" + SMALLER_PIECES_INCLUDED,
            "piecesIncluded.greaterThan=" + DEFAULT_PIECES_INCLUDED
        );
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight equals to
        defaultProductFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight in
        defaultProductFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is not null
        defaultProductFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than or equal to
        defaultProductFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than or equal to
        defaultProductFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than
        defaultProductFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than
        defaultProductFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height equals to
        defaultProductFiltering("height.equals=" + DEFAULT_HEIGHT, "height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height in
        defaultProductFiltering("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT, "height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is not null
        defaultProductFiltering("height.specified=true", "height.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is greater than or equal to
        defaultProductFiltering("height.greaterThanOrEqual=" + DEFAULT_HEIGHT, "height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is less than or equal to
        defaultProductFiltering("height.lessThanOrEqual=" + DEFAULT_HEIGHT, "height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is less than
        defaultProductFiltering("height.lessThan=" + UPDATED_HEIGHT, "height.lessThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is greater than
        defaultProductFiltering("height.greaterThan=" + SMALLER_HEIGHT, "height.greaterThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width equals to
        defaultProductFiltering("width.equals=" + DEFAULT_WIDTH, "width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width in
        defaultProductFiltering("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH, "width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is not null
        defaultProductFiltering("width.specified=true", "width.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is greater than or equal to
        defaultProductFiltering("width.greaterThanOrEqual=" + DEFAULT_WIDTH, "width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is less than or equal to
        defaultProductFiltering("width.lessThanOrEqual=" + DEFAULT_WIDTH, "width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is less than
        defaultProductFiltering("width.lessThan=" + UPDATED_WIDTH, "width.lessThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is greater than
        defaultProductFiltering("width.greaterThan=" + SMALLER_WIDTH, "width.greaterThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth equals to
        defaultProductFiltering("depth.equals=" + DEFAULT_DEPTH, "depth.equals=" + UPDATED_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth in
        defaultProductFiltering("depth.in=" + DEFAULT_DEPTH + "," + UPDATED_DEPTH, "depth.in=" + UPDATED_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth is not null
        defaultProductFiltering("depth.specified=true", "depth.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth is greater than or equal to
        defaultProductFiltering("depth.greaterThanOrEqual=" + DEFAULT_DEPTH, "depth.greaterThanOrEqual=" + UPDATED_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth is less than or equal to
        defaultProductFiltering("depth.lessThanOrEqual=" + DEFAULT_DEPTH, "depth.lessThanOrEqual=" + SMALLER_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth is less than
        defaultProductFiltering("depth.lessThan=" + UPDATED_DEPTH, "depth.lessThan=" + DEFAULT_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsByDepthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where depth is greater than
        defaultProductFiltering("depth.greaterThan=" + SMALLER_DEPTH, "depth.greaterThan=" + DEFAULT_DEPTH);
    }

    @Test
    @Transactional
    void getAllProductsBySmallImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where smallImageUrl equals to
        defaultProductFiltering("smallImageUrl.equals=" + DEFAULT_SMALL_IMAGE_URL, "smallImageUrl.equals=" + UPDATED_SMALL_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsBySmallImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where smallImageUrl in
        defaultProductFiltering(
            "smallImageUrl.in=" + DEFAULT_SMALL_IMAGE_URL + "," + UPDATED_SMALL_IMAGE_URL,
            "smallImageUrl.in=" + UPDATED_SMALL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsBySmallImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where smallImageUrl is not null
        defaultProductFiltering("smallImageUrl.specified=true", "smallImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySmallImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where smallImageUrl contains
        defaultProductFiltering("smallImageUrl.contains=" + DEFAULT_SMALL_IMAGE_URL, "smallImageUrl.contains=" + UPDATED_SMALL_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsBySmallImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where smallImageUrl does not contain
        defaultProductFiltering(
            "smallImageUrl.doesNotContain=" + UPDATED_SMALL_IMAGE_URL,
            "smallImageUrl.doesNotContain=" + DEFAULT_SMALL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByMediumImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where mediumImageUrl equals to
        defaultProductFiltering("mediumImageUrl.equals=" + DEFAULT_MEDIUM_IMAGE_URL, "mediumImageUrl.equals=" + UPDATED_MEDIUM_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByMediumImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where mediumImageUrl in
        defaultProductFiltering(
            "mediumImageUrl.in=" + DEFAULT_MEDIUM_IMAGE_URL + "," + UPDATED_MEDIUM_IMAGE_URL,
            "mediumImageUrl.in=" + UPDATED_MEDIUM_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByMediumImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where mediumImageUrl is not null
        defaultProductFiltering("mediumImageUrl.specified=true", "mediumImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMediumImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where mediumImageUrl contains
        defaultProductFiltering(
            "mediumImageUrl.contains=" + DEFAULT_MEDIUM_IMAGE_URL,
            "mediumImageUrl.contains=" + UPDATED_MEDIUM_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByMediumImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where mediumImageUrl does not contain
        defaultProductFiltering(
            "mediumImageUrl.doesNotContain=" + UPDATED_MEDIUM_IMAGE_URL,
            "mediumImageUrl.doesNotContain=" + DEFAULT_MEDIUM_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByLargeImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where largeImageUrl equals to
        defaultProductFiltering("largeImageUrl.equals=" + DEFAULT_LARGE_IMAGE_URL, "largeImageUrl.equals=" + UPDATED_LARGE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByLargeImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where largeImageUrl in
        defaultProductFiltering(
            "largeImageUrl.in=" + DEFAULT_LARGE_IMAGE_URL + "," + UPDATED_LARGE_IMAGE_URL,
            "largeImageUrl.in=" + UPDATED_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByLargeImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where largeImageUrl is not null
        defaultProductFiltering("largeImageUrl.specified=true", "largeImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLargeImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where largeImageUrl contains
        defaultProductFiltering("largeImageUrl.contains=" + DEFAULT_LARGE_IMAGE_URL, "largeImageUrl.contains=" + UPDATED_LARGE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByLargeImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where largeImageUrl does not contain
        defaultProductFiltering(
            "largeImageUrl.doesNotContain=" + UPDATED_LARGE_IMAGE_URL,
            "largeImageUrl.doesNotContain=" + DEFAULT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByDetailImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where detailImageUrl equals to
        defaultProductFiltering("detailImageUrl.equals=" + DEFAULT_DETAIL_IMAGE_URL, "detailImageUrl.equals=" + UPDATED_DETAIL_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByDetailImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where detailImageUrl in
        defaultProductFiltering(
            "detailImageUrl.in=" + DEFAULT_DETAIL_IMAGE_URL + "," + UPDATED_DETAIL_IMAGE_URL,
            "detailImageUrl.in=" + UPDATED_DETAIL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByDetailImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where detailImageUrl is not null
        defaultProductFiltering("detailImageUrl.specified=true", "detailImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDetailImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where detailImageUrl contains
        defaultProductFiltering(
            "detailImageUrl.contains=" + DEFAULT_DETAIL_IMAGE_URL,
            "detailImageUrl.contains=" + UPDATED_DETAIL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByDetailImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where detailImageUrl does not contain
        defaultProductFiltering(
            "detailImageUrl.doesNotContain=" + UPDATED_DETAIL_IMAGE_URL,
            "detailImageUrl.doesNotContain=" + DEFAULT_DETAIL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalImageUrl equals to
        defaultProductFiltering(
            "originalImageUrl.equals=" + DEFAULT_ORIGINAL_IMAGE_URL,
            "originalImageUrl.equals=" + UPDATED_ORIGINAL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalImageUrl in
        defaultProductFiltering(
            "originalImageUrl.in=" + DEFAULT_ORIGINAL_IMAGE_URL + "," + UPDATED_ORIGINAL_IMAGE_URL,
            "originalImageUrl.in=" + UPDATED_ORIGINAL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalImageUrl is not null
        defaultProductFiltering("originalImageUrl.specified=true", "originalImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByOriginalImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalImageUrl contains
        defaultProductFiltering(
            "originalImageUrl.contains=" + DEFAULT_ORIGINAL_IMAGE_URL,
            "originalImageUrl.contains=" + UPDATED_ORIGINAL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalImageUrl does not contain
        defaultProductFiltering(
            "originalImageUrl.doesNotContain=" + UPDATED_ORIGINAL_IMAGE_URL,
            "originalImageUrl.doesNotContain=" + DEFAULT_ORIGINAL_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl1 equals to
        defaultProductFiltering("altImageUrl1.equals=" + DEFAULT_ALT_IMAGE_URL_1, "altImageUrl1.equals=" + UPDATED_ALT_IMAGE_URL_1);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl1 in
        defaultProductFiltering(
            "altImageUrl1.in=" + DEFAULT_ALT_IMAGE_URL_1 + "," + UPDATED_ALT_IMAGE_URL_1,
            "altImageUrl1.in=" + UPDATED_ALT_IMAGE_URL_1
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl1 is not null
        defaultProductFiltering("altImageUrl1.specified=true", "altImageUrl1.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl1ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl1 contains
        defaultProductFiltering("altImageUrl1.contains=" + DEFAULT_ALT_IMAGE_URL_1, "altImageUrl1.contains=" + UPDATED_ALT_IMAGE_URL_1);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl1 does not contain
        defaultProductFiltering(
            "altImageUrl1.doesNotContain=" + UPDATED_ALT_IMAGE_URL_1,
            "altImageUrl1.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_1
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl2 equals to
        defaultProductFiltering("altImageUrl2.equals=" + DEFAULT_ALT_IMAGE_URL_2, "altImageUrl2.equals=" + UPDATED_ALT_IMAGE_URL_2);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl2 in
        defaultProductFiltering(
            "altImageUrl2.in=" + DEFAULT_ALT_IMAGE_URL_2 + "," + UPDATED_ALT_IMAGE_URL_2,
            "altImageUrl2.in=" + UPDATED_ALT_IMAGE_URL_2
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl2 is not null
        defaultProductFiltering("altImageUrl2.specified=true", "altImageUrl2.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl2ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl2 contains
        defaultProductFiltering("altImageUrl2.contains=" + DEFAULT_ALT_IMAGE_URL_2, "altImageUrl2.contains=" + UPDATED_ALT_IMAGE_URL_2);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl2 does not contain
        defaultProductFiltering(
            "altImageUrl2.doesNotContain=" + UPDATED_ALT_IMAGE_URL_2,
            "altImageUrl2.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_2
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl3 equals to
        defaultProductFiltering("altImageUrl3.equals=" + DEFAULT_ALT_IMAGE_URL_3, "altImageUrl3.equals=" + UPDATED_ALT_IMAGE_URL_3);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl3 in
        defaultProductFiltering(
            "altImageUrl3.in=" + DEFAULT_ALT_IMAGE_URL_3 + "," + UPDATED_ALT_IMAGE_URL_3,
            "altImageUrl3.in=" + UPDATED_ALT_IMAGE_URL_3
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl3 is not null
        defaultProductFiltering("altImageUrl3.specified=true", "altImageUrl3.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl3ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl3 contains
        defaultProductFiltering("altImageUrl3.contains=" + DEFAULT_ALT_IMAGE_URL_3, "altImageUrl3.contains=" + UPDATED_ALT_IMAGE_URL_3);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl3 does not contain
        defaultProductFiltering(
            "altImageUrl3.doesNotContain=" + UPDATED_ALT_IMAGE_URL_3,
            "altImageUrl3.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_3
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl4IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl4 equals to
        defaultProductFiltering("altImageUrl4.equals=" + DEFAULT_ALT_IMAGE_URL_4, "altImageUrl4.equals=" + UPDATED_ALT_IMAGE_URL_4);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl4IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl4 in
        defaultProductFiltering(
            "altImageUrl4.in=" + DEFAULT_ALT_IMAGE_URL_4 + "," + UPDATED_ALT_IMAGE_URL_4,
            "altImageUrl4.in=" + UPDATED_ALT_IMAGE_URL_4
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl4IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl4 is not null
        defaultProductFiltering("altImageUrl4.specified=true", "altImageUrl4.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl4ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl4 contains
        defaultProductFiltering("altImageUrl4.contains=" + DEFAULT_ALT_IMAGE_URL_4, "altImageUrl4.contains=" + UPDATED_ALT_IMAGE_URL_4);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl4NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl4 does not contain
        defaultProductFiltering(
            "altImageUrl4.doesNotContain=" + UPDATED_ALT_IMAGE_URL_4,
            "altImageUrl4.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_4
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl5IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl5 equals to
        defaultProductFiltering("altImageUrl5.equals=" + DEFAULT_ALT_IMAGE_URL_5, "altImageUrl5.equals=" + UPDATED_ALT_IMAGE_URL_5);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl5IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl5 in
        defaultProductFiltering(
            "altImageUrl5.in=" + DEFAULT_ALT_IMAGE_URL_5 + "," + UPDATED_ALT_IMAGE_URL_5,
            "altImageUrl5.in=" + UPDATED_ALT_IMAGE_URL_5
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl5IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl5 is not null
        defaultProductFiltering("altImageUrl5.specified=true", "altImageUrl5.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl5ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl5 contains
        defaultProductFiltering("altImageUrl5.contains=" + DEFAULT_ALT_IMAGE_URL_5, "altImageUrl5.contains=" + UPDATED_ALT_IMAGE_URL_5);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl5NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl5 does not contain
        defaultProductFiltering(
            "altImageUrl5.doesNotContain=" + UPDATED_ALT_IMAGE_URL_5,
            "altImageUrl5.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_5
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl6IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl6 equals to
        defaultProductFiltering("altImageUrl6.equals=" + DEFAULT_ALT_IMAGE_URL_6, "altImageUrl6.equals=" + UPDATED_ALT_IMAGE_URL_6);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl6IsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl6 in
        defaultProductFiltering(
            "altImageUrl6.in=" + DEFAULT_ALT_IMAGE_URL_6 + "," + UPDATED_ALT_IMAGE_URL_6,
            "altImageUrl6.in=" + UPDATED_ALT_IMAGE_URL_6
        );
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl6IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl6 is not null
        defaultProductFiltering("altImageUrl6.specified=true", "altImageUrl6.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl6ContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl6 contains
        defaultProductFiltering("altImageUrl6.contains=" + DEFAULT_ALT_IMAGE_URL_6, "altImageUrl6.contains=" + UPDATED_ALT_IMAGE_URL_6);
    }

    @Test
    @Transactional
    void getAllProductsByAltImageUrl6NotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where altImageUrl6 does not contain
        defaultProductFiltering(
            "altImageUrl6.doesNotContain=" + UPDATED_ALT_IMAGE_URL_6,
            "altImageUrl6.doesNotContain=" + DEFAULT_ALT_IMAGE_URL_6
        );
    }

    @Test
    @Transactional
    void getAllProductsByVideoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where videoUrl equals to
        defaultProductFiltering("videoUrl.equals=" + DEFAULT_VIDEO_URL, "videoUrl.equals=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllProductsByVideoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where videoUrl in
        defaultProductFiltering("videoUrl.in=" + DEFAULT_VIDEO_URL + "," + UPDATED_VIDEO_URL, "videoUrl.in=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllProductsByVideoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where videoUrl is not null
        defaultProductFiltering("videoUrl.specified=true", "videoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByVideoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where videoUrl contains
        defaultProductFiltering("videoUrl.contains=" + DEFAULT_VIDEO_URL, "videoUrl.contains=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllProductsByVideoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where videoUrl does not contain
        defaultProductFiltering("videoUrl.doesNotContain=" + UPDATED_VIDEO_URL, "videoUrl.doesNotContain=" + DEFAULT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity equals to
        defaultProductFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity in
        defaultProductFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is not null
        defaultProductFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than or equal to
        defaultProductFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than or equal to
        defaultProductFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than
        defaultProductFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than
        defaultProductFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price equals to
        defaultProductFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price in
        defaultProductFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to
        defaultProductFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to
        defaultProductFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than
        defaultProductFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than
        defaultProductFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst equals to
        defaultProductFiltering("cgst.equals=" + DEFAULT_CGST, "cgst.equals=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst in
        defaultProductFiltering("cgst.in=" + DEFAULT_CGST + "," + UPDATED_CGST, "cgst.in=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst is not null
        defaultProductFiltering("cgst.specified=true", "cgst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst is greater than or equal to
        defaultProductFiltering("cgst.greaterThanOrEqual=" + DEFAULT_CGST, "cgst.greaterThanOrEqual=" + UPDATED_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst is less than or equal to
        defaultProductFiltering("cgst.lessThanOrEqual=" + DEFAULT_CGST, "cgst.lessThanOrEqual=" + SMALLER_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst is less than
        defaultProductFiltering("cgst.lessThan=" + UPDATED_CGST, "cgst.lessThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgst is greater than
        defaultProductFiltering("cgst.greaterThan=" + SMALLER_CGST, "cgst.greaterThan=" + DEFAULT_CGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst equals to
        defaultProductFiltering("igst.equals=" + DEFAULT_IGST, "igst.equals=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst in
        defaultProductFiltering("igst.in=" + DEFAULT_IGST + "," + UPDATED_IGST, "igst.in=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst is not null
        defaultProductFiltering("igst.specified=true", "igst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst is greater than or equal to
        defaultProductFiltering("igst.greaterThanOrEqual=" + DEFAULT_IGST, "igst.greaterThanOrEqual=" + UPDATED_IGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst is less than or equal to
        defaultProductFiltering("igst.lessThanOrEqual=" + DEFAULT_IGST, "igst.lessThanOrEqual=" + SMALLER_IGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst is less than
        defaultProductFiltering("igst.lessThan=" + UPDATED_IGST, "igst.lessThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllProductsByIgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igst is greater than
        defaultProductFiltering("igst.greaterThan=" + SMALLER_IGST, "igst.greaterThan=" + DEFAULT_IGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst equals to
        defaultProductFiltering("sgst.equals=" + DEFAULT_SGST, "sgst.equals=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst in
        defaultProductFiltering("sgst.in=" + DEFAULT_SGST + "," + UPDATED_SGST, "sgst.in=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst is not null
        defaultProductFiltering("sgst.specified=true", "sgst.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst is greater than or equal to
        defaultProductFiltering("sgst.greaterThanOrEqual=" + DEFAULT_SGST, "sgst.greaterThanOrEqual=" + UPDATED_SGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst is less than or equal to
        defaultProductFiltering("sgst.lessThanOrEqual=" + DEFAULT_SGST, "sgst.lessThanOrEqual=" + SMALLER_SGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst is less than
        defaultProductFiltering("sgst.lessThan=" + UPDATED_SGST, "sgst.lessThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllProductsBySgstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgst is greater than
        defaultProductFiltering("sgst.greaterThan=" + SMALLER_SGST, "sgst.greaterThan=" + DEFAULT_SGST);
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage equals to
        defaultProductFiltering("cgstPercentage.equals=" + DEFAULT_CGST_PERCENTAGE, "cgstPercentage.equals=" + UPDATED_CGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage in
        defaultProductFiltering(
            "cgstPercentage.in=" + DEFAULT_CGST_PERCENTAGE + "," + UPDATED_CGST_PERCENTAGE,
            "cgstPercentage.in=" + UPDATED_CGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage is not null
        defaultProductFiltering("cgstPercentage.specified=true", "cgstPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage is greater than or equal to
        defaultProductFiltering(
            "cgstPercentage.greaterThanOrEqual=" + DEFAULT_CGST_PERCENTAGE,
            "cgstPercentage.greaterThanOrEqual=" + UPDATED_CGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage is less than or equal to
        defaultProductFiltering(
            "cgstPercentage.lessThanOrEqual=" + DEFAULT_CGST_PERCENTAGE,
            "cgstPercentage.lessThanOrEqual=" + SMALLER_CGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage is less than
        defaultProductFiltering("cgstPercentage.lessThan=" + UPDATED_CGST_PERCENTAGE, "cgstPercentage.lessThan=" + DEFAULT_CGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsByCgstPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cgstPercentage is greater than
        defaultProductFiltering(
            "cgstPercentage.greaterThan=" + SMALLER_CGST_PERCENTAGE,
            "cgstPercentage.greaterThan=" + DEFAULT_CGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage equals to
        defaultProductFiltering("igstPercentage.equals=" + DEFAULT_IGST_PERCENTAGE, "igstPercentage.equals=" + UPDATED_IGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage in
        defaultProductFiltering(
            "igstPercentage.in=" + DEFAULT_IGST_PERCENTAGE + "," + UPDATED_IGST_PERCENTAGE,
            "igstPercentage.in=" + UPDATED_IGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage is not null
        defaultProductFiltering("igstPercentage.specified=true", "igstPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage is greater than or equal to
        defaultProductFiltering(
            "igstPercentage.greaterThanOrEqual=" + DEFAULT_IGST_PERCENTAGE,
            "igstPercentage.greaterThanOrEqual=" + UPDATED_IGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage is less than or equal to
        defaultProductFiltering(
            "igstPercentage.lessThanOrEqual=" + DEFAULT_IGST_PERCENTAGE,
            "igstPercentage.lessThanOrEqual=" + SMALLER_IGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage is less than
        defaultProductFiltering("igstPercentage.lessThan=" + UPDATED_IGST_PERCENTAGE, "igstPercentage.lessThan=" + DEFAULT_IGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsByIgstPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where igstPercentage is greater than
        defaultProductFiltering(
            "igstPercentage.greaterThan=" + SMALLER_IGST_PERCENTAGE,
            "igstPercentage.greaterThan=" + DEFAULT_IGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage equals to
        defaultProductFiltering("sgstPercentage.equals=" + DEFAULT_SGST_PERCENTAGE, "sgstPercentage.equals=" + UPDATED_SGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage in
        defaultProductFiltering(
            "sgstPercentage.in=" + DEFAULT_SGST_PERCENTAGE + "," + UPDATED_SGST_PERCENTAGE,
            "sgstPercentage.in=" + UPDATED_SGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage is not null
        defaultProductFiltering("sgstPercentage.specified=true", "sgstPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage is greater than or equal to
        defaultProductFiltering(
            "sgstPercentage.greaterThanOrEqual=" + DEFAULT_SGST_PERCENTAGE,
            "sgstPercentage.greaterThanOrEqual=" + UPDATED_SGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage is less than or equal to
        defaultProductFiltering(
            "sgstPercentage.lessThanOrEqual=" + DEFAULT_SGST_PERCENTAGE,
            "sgstPercentage.lessThanOrEqual=" + SMALLER_SGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage is less than
        defaultProductFiltering("sgstPercentage.lessThan=" + UPDATED_SGST_PERCENTAGE, "sgstPercentage.lessThan=" + DEFAULT_SGST_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllProductsBySgstPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sgstPercentage is greater than
        defaultProductFiltering(
            "sgstPercentage.greaterThan=" + SMALLER_SGST_PERCENTAGE,
            "sgstPercentage.greaterThan=" + DEFAULT_SGST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice equals to
        defaultProductFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice in
        defaultProductFiltering("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE, "totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice is not null
        defaultProductFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice is greater than or equal to
        defaultProductFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice is less than or equal to
        defaultProductFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice is less than
        defaultProductFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where totalPrice is greater than
        defaultProductFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description equals to
        defaultProductFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description in
        defaultProductFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description contains
        defaultProductFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain
        defaultProductFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByLongDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where longDescription equals to
        defaultProductFiltering("longDescription.equals=" + DEFAULT_LONG_DESCRIPTION, "longDescription.equals=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByLongDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where longDescription in
        defaultProductFiltering(
            "longDescription.in=" + DEFAULT_LONG_DESCRIPTION + "," + UPDATED_LONG_DESCRIPTION,
            "longDescription.in=" + UPDATED_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByLongDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where longDescription is not null
        defaultProductFiltering("longDescription.specified=true", "longDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLongDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where longDescription contains
        defaultProductFiltering(
            "longDescription.contains=" + DEFAULT_LONG_DESCRIPTION,
            "longDescription.contains=" + UPDATED_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByLongDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where longDescription does not contain
        defaultProductFiltering(
            "longDescription.doesNotContain=" + UPDATED_LONG_DESCRIPTION,
            "longDescription.doesNotContain=" + DEFAULT_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByIsVirtualIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVirtual equals to
        defaultProductFiltering("isVirtual.equals=" + DEFAULT_IS_VIRTUAL, "isVirtual.equals=" + UPDATED_IS_VIRTUAL);
    }

    @Test
    @Transactional
    void getAllProductsByIsVirtualIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVirtual in
        defaultProductFiltering("isVirtual.in=" + DEFAULT_IS_VIRTUAL + "," + UPDATED_IS_VIRTUAL, "isVirtual.in=" + UPDATED_IS_VIRTUAL);
    }

    @Test
    @Transactional
    void getAllProductsByIsVirtualIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVirtual is not null
        defaultProductFiltering("isVirtual.specified=true", "isVirtual.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIsVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVariant equals to
        defaultProductFiltering("isVariant.equals=" + DEFAULT_IS_VARIANT, "isVariant.equals=" + UPDATED_IS_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsByIsVariantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVariant in
        defaultProductFiltering("isVariant.in=" + DEFAULT_IS_VARIANT + "," + UPDATED_IS_VARIANT, "isVariant.in=" + UPDATED_IS_VARIANT);
    }

    @Test
    @Transactional
    void getAllProductsByIsVariantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isVariant is not null
        defaultProductFiltering("isVariant.specified=true", "isVariant.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRequireInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where requireInventory equals to
        defaultProductFiltering(
            "requireInventory.equals=" + DEFAULT_REQUIRE_INVENTORY,
            "requireInventory.equals=" + UPDATED_REQUIRE_INVENTORY
        );
    }

    @Test
    @Transactional
    void getAllProductsByRequireInventoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where requireInventory in
        defaultProductFiltering(
            "requireInventory.in=" + DEFAULT_REQUIRE_INVENTORY + "," + UPDATED_REQUIRE_INVENTORY,
            "requireInventory.in=" + UPDATED_REQUIRE_INVENTORY
        );
    }

    @Test
    @Transactional
    void getAllProductsByRequireInventoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where requireInventory is not null
        defaultProductFiltering("requireInventory.specified=true", "requireInventory.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByReturnableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where returnable equals to
        defaultProductFiltering("returnable.equals=" + DEFAULT_RETURNABLE, "returnable.equals=" + UPDATED_RETURNABLE);
    }

    @Test
    @Transactional
    void getAllProductsByReturnableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where returnable in
        defaultProductFiltering("returnable.in=" + DEFAULT_RETURNABLE + "," + UPDATED_RETURNABLE, "returnable.in=" + UPDATED_RETURNABLE);
    }

    @Test
    @Transactional
    void getAllProductsByReturnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where returnable is not null
        defaultProductFiltering("returnable.specified=true", "returnable.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIsPopularIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isPopular equals to
        defaultProductFiltering("isPopular.equals=" + DEFAULT_IS_POPULAR, "isPopular.equals=" + UPDATED_IS_POPULAR);
    }

    @Test
    @Transactional
    void getAllProductsByIsPopularIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isPopular in
        defaultProductFiltering("isPopular.in=" + DEFAULT_IS_POPULAR + "," + UPDATED_IS_POPULAR, "isPopular.in=" + UPDATED_IS_POPULAR);
    }

    @Test
    @Transactional
    void getAllProductsByIsPopularIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isPopular is not null
        defaultProductFiltering("isPopular.specified=true", "isPopular.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIsBannerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isBanner equals to
        defaultProductFiltering("isBanner.equals=" + DEFAULT_IS_BANNER, "isBanner.equals=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllProductsByIsBannerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isBanner in
        defaultProductFiltering("isBanner.in=" + DEFAULT_IS_BANNER + "," + UPDATED_IS_BANNER, "isBanner.in=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllProductsByIsBannerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isBanner is not null
        defaultProductFiltering("isBanner.specified=true", "isBanner.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBannerUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where bannerUrl equals to
        defaultProductFiltering("bannerUrl.equals=" + DEFAULT_BANNER_URL, "bannerUrl.equals=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductsByBannerUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where bannerUrl in
        defaultProductFiltering("bannerUrl.in=" + DEFAULT_BANNER_URL + "," + UPDATED_BANNER_URL, "bannerUrl.in=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductsByBannerUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where bannerUrl is not null
        defaultProductFiltering("bannerUrl.specified=true", "bannerUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBannerUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where bannerUrl contains
        defaultProductFiltering("bannerUrl.contains=" + DEFAULT_BANNER_URL, "bannerUrl.contains=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductsByBannerUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where bannerUrl does not contain
        defaultProductFiltering("bannerUrl.doesNotContain=" + UPDATED_BANNER_URL, "bannerUrl.doesNotContain=" + DEFAULT_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductsByProductTypeIsEqualToSomething() throws Exception {
        ProductType productType;
        if (TestUtil.findAll(em, ProductType.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            productType = ProductTypeResourceIT.createEntity();
        } else {
            productType = TestUtil.findAll(em, ProductType.class).getFirst();
        }
        em.persist(productType);
        em.flush();
        product.setProductType(productType);
        productRepository.saveAndFlush(product);
        Long productTypeId = productType.getId();
        // Get all the productList where productType equals to productTypeId
        defaultProductShouldBeFound("productTypeId.equals=" + productTypeId);

        // Get all the productList where productType equals to (productTypeId + 1)
        defaultProductShouldNotBeFound("productTypeId.equals=" + (productTypeId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByPrimaryProductCategoryIsEqualToSomething() throws Exception {
        ProductCategory primaryProductCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            primaryProductCategory = ProductCategoryResourceIT.createEntity();
        } else {
            primaryProductCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        em.persist(primaryProductCategory);
        em.flush();
        product.setPrimaryProductCategory(primaryProductCategory);
        productRepository.saveAndFlush(product);
        Long primaryProductCategoryId = primaryProductCategory.getId();
        // Get all the productList where primaryProductCategory equals to primaryProductCategoryId
        defaultProductShouldBeFound("primaryProductCategoryId.equals=" + primaryProductCategoryId);

        // Get all the productList where primaryProductCategory equals to (primaryProductCategoryId + 1)
        defaultProductShouldNotBeFound("primaryProductCategoryId.equals=" + (primaryProductCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByVirtualProductIsEqualToSomething() throws Exception {
        Product virtualProduct;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            virtualProduct = ProductResourceIT.createEntity();
        } else {
            virtualProduct = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(virtualProduct);
        em.flush();
        product.setVirtualProduct(virtualProduct);
        productRepository.saveAndFlush(product);
        Long virtualProductId = virtualProduct.getId();
        // Get all the productList where virtualProduct equals to virtualProductId
        defaultProductShouldBeFound("virtualProductId.equals=" + virtualProductId);

        // Get all the productList where virtualProduct equals to (virtualProductId + 1)
        defaultProductShouldNotBeFound("virtualProductId.equals=" + (virtualProductId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByInventoryItemTypeIsEqualToSomething() throws Exception {
        InventoryItemType inventoryItemType;
        if (TestUtil.findAll(em, InventoryItemType.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            inventoryItemType = InventoryItemTypeResourceIT.createEntity();
        } else {
            inventoryItemType = TestUtil.findAll(em, InventoryItemType.class).getFirst();
        }
        em.persist(inventoryItemType);
        em.flush();
        product.setInventoryItemType(inventoryItemType);
        productRepository.saveAndFlush(product);
        Long inventoryItemTypeId = inventoryItemType.getId();
        // Get all the productList where inventoryItemType equals to inventoryItemTypeId
        defaultProductShouldBeFound("inventoryItemTypeId.equals=" + inventoryItemTypeId);

        // Get all the productList where inventoryItemType equals to (inventoryItemTypeId + 1)
        defaultProductShouldNotBeFound("inventoryItemTypeId.equals=" + (inventoryItemTypeId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByTaxSlabIsEqualToSomething() throws Exception {
        TaxSlab taxSlab;
        if (TestUtil.findAll(em, TaxSlab.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            taxSlab = TaxSlabResourceIT.createEntity();
        } else {
            taxSlab = TestUtil.findAll(em, TaxSlab.class).getFirst();
        }
        em.persist(taxSlab);
        em.flush();
        product.setTaxSlab(taxSlab);
        productRepository.saveAndFlush(product);
        Long taxSlabId = taxSlab.getId();
        // Get all the productList where taxSlab equals to taxSlabId
        defaultProductShouldBeFound("taxSlabId.equals=" + taxSlabId);

        // Get all the productList where taxSlab equals to (taxSlabId + 1)
        defaultProductShouldNotBeFound("taxSlabId.equals=" + (taxSlabId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByUomIsEqualToSomething() throws Exception {
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            uom = UomResourceIT.createEntity();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(uom);
        em.flush();
        product.setUom(uom);
        productRepository.saveAndFlush(product);
        Long uomId = uom.getId();
        // Get all the productList where uom equals to uomId
        defaultProductShouldBeFound("uomId.equals=" + uomId);

        // Get all the productList where uom equals to (uomId + 1)
        defaultProductShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByQuantityUomIsEqualToSomething() throws Exception {
        Uom quantityUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            quantityUom = UomResourceIT.createEntity();
        } else {
            quantityUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(quantityUom);
        em.flush();
        product.setQuantityUom(quantityUom);
        productRepository.saveAndFlush(product);
        Long quantityUomId = quantityUom.getId();
        // Get all the productList where quantityUom equals to quantityUomId
        defaultProductShouldBeFound("quantityUomId.equals=" + quantityUomId);

        // Get all the productList where quantityUom equals to (quantityUomId + 1)
        defaultProductShouldNotBeFound("quantityUomId.equals=" + (quantityUomId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByWeightUomIsEqualToSomething() throws Exception {
        Uom weightUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            weightUom = UomResourceIT.createEntity();
        } else {
            weightUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(weightUom);
        em.flush();
        product.setWeightUom(weightUom);
        productRepository.saveAndFlush(product);
        Long weightUomId = weightUom.getId();
        // Get all the productList where weightUom equals to weightUomId
        defaultProductShouldBeFound("weightUomId.equals=" + weightUomId);

        // Get all the productList where weightUom equals to (weightUomId + 1)
        defaultProductShouldNotBeFound("weightUomId.equals=" + (weightUomId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySizeUomIsEqualToSomething() throws Exception {
        Uom sizeUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            sizeUom = UomResourceIT.createEntity();
        } else {
            sizeUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(sizeUom);
        em.flush();
        product.setSizeUom(sizeUom);
        productRepository.saveAndFlush(product);
        Long sizeUomId = sizeUom.getId();
        // Get all the productList where sizeUom equals to sizeUomId
        defaultProductShouldBeFound("sizeUomId.equals=" + sizeUomId);

        // Get all the productList where sizeUom equals to (sizeUomId + 1)
        defaultProductShouldNotBeFound("sizeUomId.equals=" + (sizeUomId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].internalName").value(hasItem(DEFAULT_INTERNAL_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].variant").value(hasItem(DEFAULT_VARIANT)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].introductionDate").value(hasItem(DEFAULT_INTRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantityIncluded").value(hasItem(sameNumber(DEFAULT_QUANTITY_INCLUDED))))
            .andExpect(jsonPath("$.[*].piecesIncluded").value(hasItem(DEFAULT_PIECES_INCLUDED)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(sameNumber(DEFAULT_HEIGHT))))
            .andExpect(jsonPath("$.[*].width").value(hasItem(sameNumber(DEFAULT_WIDTH))))
            .andExpect(jsonPath("$.[*].depth").value(hasItem(sameNumber(DEFAULT_DEPTH))))
            .andExpect(jsonPath("$.[*].smallImageUrl").value(hasItem(DEFAULT_SMALL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].mediumImageUrl").value(hasItem(DEFAULT_MEDIUM_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].largeImageUrl").value(hasItem(DEFAULT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].detailImageUrl").value(hasItem(DEFAULT_DETAIL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].originalImageUrl").value(hasItem(DEFAULT_ORIGINAL_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altImageUrl1").value(hasItem(DEFAULT_ALT_IMAGE_URL_1)))
            .andExpect(jsonPath("$.[*].altImageUrl2").value(hasItem(DEFAULT_ALT_IMAGE_URL_2)))
            .andExpect(jsonPath("$.[*].altImageUrl3").value(hasItem(DEFAULT_ALT_IMAGE_URL_3)))
            .andExpect(jsonPath("$.[*].altImageUrl4").value(hasItem(DEFAULT_ALT_IMAGE_URL_4)))
            .andExpect(jsonPath("$.[*].altImageUrl5").value(hasItem(DEFAULT_ALT_IMAGE_URL_5)))
            .andExpect(jsonPath("$.[*].altImageUrl6").value(hasItem(DEFAULT_ALT_IMAGE_URL_6)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(sameNumber(DEFAULT_CGST))))
            .andExpect(jsonPath("$.[*].igst").value(hasItem(sameNumber(DEFAULT_IGST))))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(sameNumber(DEFAULT_SGST))))
            .andExpect(jsonPath("$.[*].cgstPercentage").value(hasItem(sameNumber(DEFAULT_CGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].igstPercentage").value(hasItem(sameNumber(DEFAULT_IGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].sgstPercentage").value(hasItem(sameNumber(DEFAULT_SGST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].isVirtual").value(hasItem(DEFAULT_IS_VIRTUAL)))
            .andExpect(jsonPath("$.[*].isVariant").value(hasItem(DEFAULT_IS_VARIANT)))
            .andExpect(jsonPath("$.[*].requireInventory").value(hasItem(DEFAULT_REQUIRE_INVENTORY)))
            .andExpect(jsonPath("$.[*].returnable").value(hasItem(DEFAULT_RETURNABLE)))
            .andExpect(jsonPath("$.[*].isPopular").value(hasItem(DEFAULT_IS_POPULAR)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .internalName(UPDATED_INTERNAL_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .variant(UPDATED_VARIANT)
            .sku(UPDATED_SKU)
            .introductionDate(UPDATED_INTRODUCTION_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .quantityIncluded(UPDATED_QUANTITY_INCLUDED)
            .piecesIncluded(UPDATED_PIECES_INCLUDED)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .depth(UPDATED_DEPTH)
            .smallImageUrl(UPDATED_SMALL_IMAGE_URL)
            .mediumImageUrl(UPDATED_MEDIUM_IMAGE_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .detailImageUrl(UPDATED_DETAIL_IMAGE_URL)
            .originalImageUrl(UPDATED_ORIGINAL_IMAGE_URL)
            .altImageUrl1(UPDATED_ALT_IMAGE_URL_1)
            .altImageUrl2(UPDATED_ALT_IMAGE_URL_2)
            .altImageUrl3(UPDATED_ALT_IMAGE_URL_3)
            .altImageUrl4(UPDATED_ALT_IMAGE_URL_4)
            .altImageUrl5(UPDATED_ALT_IMAGE_URL_5)
            .altImageUrl6(UPDATED_ALT_IMAGE_URL_6)
            .videoUrl(UPDATED_VIDEO_URL)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .cgstPercentage(UPDATED_CGST_PERCENTAGE)
            .igstPercentage(UPDATED_IGST_PERCENTAGE)
            .sgstPercentage(UPDATED_SGST_PERCENTAGE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .info(UPDATED_INFO)
            .isVirtual(UPDATED_IS_VIRTUAL)
            .isVariant(UPDATED_IS_VARIANT)
            .requireInventory(UPDATED_REQUIRE_INVENTORY)
            .returnable(UPDATED_RETURNABLE)
            .isPopular(UPDATED_IS_POPULAR)
            .isBanner(UPDATED_IS_BANNER)
            .bannerUrl(UPDATED_BANNER_URL);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL_ID, product.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .variant(UPDATED_VARIANT)
            .sku(UPDATED_SKU)
            .quantityIncluded(UPDATED_QUANTITY_INCLUDED)
            .width(UPDATED_WIDTH)
            .depth(UPDATED_DEPTH)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .altImageUrl1(UPDATED_ALT_IMAGE_URL_1)
            .altImageUrl2(UPDATED_ALT_IMAGE_URL_2)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .cgstPercentage(UPDATED_CGST_PERCENTAGE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .description(UPDATED_DESCRIPTION)
            .isVirtual(UPDATED_IS_VIRTUAL)
            .isVariant(UPDATED_IS_VARIANT)
            .returnable(UPDATED_RETURNABLE)
            .isPopular(UPDATED_IS_POPULAR)
            .isBanner(UPDATED_IS_BANNER)
            .bannerUrl(UPDATED_BANNER_URL);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .internalName(UPDATED_INTERNAL_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .variant(UPDATED_VARIANT)
            .sku(UPDATED_SKU)
            .introductionDate(UPDATED_INTRODUCTION_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .quantityIncluded(UPDATED_QUANTITY_INCLUDED)
            .piecesIncluded(UPDATED_PIECES_INCLUDED)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .depth(UPDATED_DEPTH)
            .smallImageUrl(UPDATED_SMALL_IMAGE_URL)
            .mediumImageUrl(UPDATED_MEDIUM_IMAGE_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .detailImageUrl(UPDATED_DETAIL_IMAGE_URL)
            .originalImageUrl(UPDATED_ORIGINAL_IMAGE_URL)
            .altImageUrl1(UPDATED_ALT_IMAGE_URL_1)
            .altImageUrl2(UPDATED_ALT_IMAGE_URL_2)
            .altImageUrl3(UPDATED_ALT_IMAGE_URL_3)
            .altImageUrl4(UPDATED_ALT_IMAGE_URL_4)
            .altImageUrl5(UPDATED_ALT_IMAGE_URL_5)
            .altImageUrl6(UPDATED_ALT_IMAGE_URL_6)
            .videoUrl(UPDATED_VIDEO_URL)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .cgst(UPDATED_CGST)
            .igst(UPDATED_IGST)
            .sgst(UPDATED_SGST)
            .cgstPercentage(UPDATED_CGST_PERCENTAGE)
            .igstPercentage(UPDATED_IGST_PERCENTAGE)
            .sgstPercentage(UPDATED_SGST_PERCENTAGE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .info(UPDATED_INFO)
            .isVirtual(UPDATED_IS_VIRTUAL)
            .isVariant(UPDATED_IS_VARIANT)
            .requireInventory(UPDATED_REQUIRE_INVENTORY)
            .returnable(UPDATED_RETURNABLE)
            .isPopular(UPDATED_IS_POPULAR)
            .isBanner(UPDATED_IS_BANNER)
            .bannerUrl(UPDATED_BANNER_URL);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
