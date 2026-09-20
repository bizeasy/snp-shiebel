package com.snp.web.rest;

import static com.snp.domain.ProductCategoryAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductCategory;
import com.snp.domain.ProductCategory;
import com.snp.domain.ProductCategoryType;
import com.snp.repository.ProductCategoryRepository;
import com.snp.service.ProductCategoryService;
import jakarta.persistence.EntityManager;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ProductCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BANNER_URL = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LARGE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_LARGE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_LARGE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ALT_LARGE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BANNER = false;
    private static final Boolean UPDATED_IS_BANNER = true;

    private static final String ENTITY_API_URL = "/api/product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepositoryMock;

    @Mock
    private ProductCategoryService productCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCategoryMockMvc;

    private ProductCategory productCategory;

    private ProductCategory insertedProductCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity() {
        return new ProductCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .attribute(DEFAULT_ATTRIBUTE)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .imageUrl(DEFAULT_IMAGE_URL)
            .altImageUrl(DEFAULT_ALT_IMAGE_URL)
            .bannerUrl(DEFAULT_BANNER_URL)
            .largeImageUrl(DEFAULT_LARGE_IMAGE_URL)
            .altLargeImageUrl(DEFAULT_ALT_LARGE_IMAGE_URL)
            .info(DEFAULT_INFO)
            .isBanner(DEFAULT_IS_BANNER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createUpdatedEntity() {
        return new ProductCategory()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .attribute(UPDATED_ATTRIBUTE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imageUrl(UPDATED_IMAGE_URL)
            .altImageUrl(UPDATED_ALT_IMAGE_URL)
            .bannerUrl(UPDATED_BANNER_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .altLargeImageUrl(UPDATED_ALT_LARGE_IMAGE_URL)
            .info(UPDATED_INFO)
            .isBanner(UPDATED_IS_BANNER);
    }

    @BeforeEach
    void initTest() {
        productCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductCategory != null) {
            productCategoryRepository.delete(insertedProductCategory);
            insertedProductCategory = null;
        }
    }

    @Test
    @Transactional
    void createProductCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductCategory
        var returnedProductCategory = om.readValue(
            restProductCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductCategory.class
        );

        // Validate the ProductCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductCategoryUpdatableFieldsEquals(returnedProductCategory, getPersistedProductCategory(returnedProductCategory));

        insertedProductCategory = returnedProductCategory;
    }

    @Test
    @Transactional
    void createProductCategoryWithExistingId() throws Exception {
        // Create the ProductCategory with an existing ID
        productCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productCategory.setName(null);

        // Create the ProductCategory, which fails.

        restProductCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductCategories() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altImageUrl").value(hasItem(DEFAULT_ALT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].largeImageUrl").value(hasItem(DEFAULT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altLargeImageUrl").value(hasItem(DEFAULT_ALT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restProductCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productCategoryServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restProductCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductCategory() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get the productCategory
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.altImageUrl").value(DEFAULT_ALT_IMAGE_URL))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL))
            .andExpect(jsonPath("$.largeImageUrl").value(DEFAULT_LARGE_IMAGE_URL))
            .andExpect(jsonPath("$.altLargeImageUrl").value(DEFAULT_ALT_LARGE_IMAGE_URL))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.isBanner").value(DEFAULT_IS_BANNER));
    }

    @Test
    @Transactional
    void getProductCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        Long id = productCategory.getId();

        defaultProductCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where name equals to
        defaultProductCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where name in
        defaultProductCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where name is not null
        defaultProductCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where name contains
        defaultProductCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where name does not contain
        defaultProductCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description equals to
        defaultProductCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description in
        defaultProductCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description is not null
        defaultProductCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description contains
        defaultProductCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description does not contain
        defaultProductCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLongDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where longDescription equals to
        defaultProductCategoryFiltering(
            "longDescription.equals=" + DEFAULT_LONG_DESCRIPTION,
            "longDescription.equals=" + UPDATED_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLongDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where longDescription in
        defaultProductCategoryFiltering(
            "longDescription.in=" + DEFAULT_LONG_DESCRIPTION + "," + UPDATED_LONG_DESCRIPTION,
            "longDescription.in=" + UPDATED_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLongDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where longDescription is not null
        defaultProductCategoryFiltering("longDescription.specified=true", "longDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLongDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where longDescription contains
        defaultProductCategoryFiltering(
            "longDescription.contains=" + DEFAULT_LONG_DESCRIPTION,
            "longDescription.contains=" + UPDATED_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLongDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where longDescription does not contain
        defaultProductCategoryFiltering(
            "longDescription.doesNotContain=" + UPDATED_LONG_DESCRIPTION,
            "longDescription.doesNotContain=" + DEFAULT_LONG_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where attribute equals to
        defaultProductCategoryFiltering("attribute.equals=" + DEFAULT_ATTRIBUTE, "attribute.equals=" + UPDATED_ATTRIBUTE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where attribute in
        defaultProductCategoryFiltering("attribute.in=" + DEFAULT_ATTRIBUTE + "," + UPDATED_ATTRIBUTE, "attribute.in=" + UPDATED_ATTRIBUTE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where attribute is not null
        defaultProductCategoryFiltering("attribute.specified=true", "attribute.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAttributeContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where attribute contains
        defaultProductCategoryFiltering("attribute.contains=" + DEFAULT_ATTRIBUTE, "attribute.contains=" + UPDATED_ATTRIBUTE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAttributeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where attribute does not contain
        defaultProductCategoryFiltering("attribute.doesNotContain=" + UPDATED_ATTRIBUTE, "attribute.doesNotContain=" + DEFAULT_ATTRIBUTE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo equals to
        defaultProductCategoryFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo in
        defaultProductCategoryFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo is not null
        defaultProductCategoryFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo is greater than or equal to
        defaultProductCategoryFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo is less than or equal to
        defaultProductCategoryFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo is less than
        defaultProductCategoryFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductCategoriesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where sequenceNo is greater than
        defaultProductCategoryFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageUrl equals to
        defaultProductCategoryFiltering("imageUrl.equals=" + DEFAULT_IMAGE_URL, "imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageUrl in
        defaultProductCategoryFiltering("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageUrl is not null
        defaultProductCategoryFiltering("imageUrl.specified=true", "imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageUrl contains
        defaultProductCategoryFiltering("imageUrl.contains=" + DEFAULT_IMAGE_URL, "imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageUrl does not contain
        defaultProductCategoryFiltering("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL, "imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altImageUrl equals to
        defaultProductCategoryFiltering("altImageUrl.equals=" + DEFAULT_ALT_IMAGE_URL, "altImageUrl.equals=" + UPDATED_ALT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altImageUrl in
        defaultProductCategoryFiltering(
            "altImageUrl.in=" + DEFAULT_ALT_IMAGE_URL + "," + UPDATED_ALT_IMAGE_URL,
            "altImageUrl.in=" + UPDATED_ALT_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altImageUrl is not null
        defaultProductCategoryFiltering("altImageUrl.specified=true", "altImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altImageUrl contains
        defaultProductCategoryFiltering("altImageUrl.contains=" + DEFAULT_ALT_IMAGE_URL, "altImageUrl.contains=" + UPDATED_ALT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altImageUrl does not contain
        defaultProductCategoryFiltering(
            "altImageUrl.doesNotContain=" + UPDATED_ALT_IMAGE_URL,
            "altImageUrl.doesNotContain=" + DEFAULT_ALT_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByBannerUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where bannerUrl equals to
        defaultProductCategoryFiltering("bannerUrl.equals=" + DEFAULT_BANNER_URL, "bannerUrl.equals=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByBannerUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where bannerUrl in
        defaultProductCategoryFiltering(
            "bannerUrl.in=" + DEFAULT_BANNER_URL + "," + UPDATED_BANNER_URL,
            "bannerUrl.in=" + UPDATED_BANNER_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByBannerUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where bannerUrl is not null
        defaultProductCategoryFiltering("bannerUrl.specified=true", "bannerUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByBannerUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where bannerUrl contains
        defaultProductCategoryFiltering("bannerUrl.contains=" + DEFAULT_BANNER_URL, "bannerUrl.contains=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByBannerUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where bannerUrl does not contain
        defaultProductCategoryFiltering("bannerUrl.doesNotContain=" + UPDATED_BANNER_URL, "bannerUrl.doesNotContain=" + DEFAULT_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLargeImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where largeImageUrl equals to
        defaultProductCategoryFiltering(
            "largeImageUrl.equals=" + DEFAULT_LARGE_IMAGE_URL,
            "largeImageUrl.equals=" + UPDATED_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLargeImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where largeImageUrl in
        defaultProductCategoryFiltering(
            "largeImageUrl.in=" + DEFAULT_LARGE_IMAGE_URL + "," + UPDATED_LARGE_IMAGE_URL,
            "largeImageUrl.in=" + UPDATED_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLargeImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where largeImageUrl is not null
        defaultProductCategoryFiltering("largeImageUrl.specified=true", "largeImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLargeImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where largeImageUrl contains
        defaultProductCategoryFiltering(
            "largeImageUrl.contains=" + DEFAULT_LARGE_IMAGE_URL,
            "largeImageUrl.contains=" + UPDATED_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByLargeImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where largeImageUrl does not contain
        defaultProductCategoryFiltering(
            "largeImageUrl.doesNotContain=" + UPDATED_LARGE_IMAGE_URL,
            "largeImageUrl.doesNotContain=" + DEFAULT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltLargeImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altLargeImageUrl equals to
        defaultProductCategoryFiltering(
            "altLargeImageUrl.equals=" + DEFAULT_ALT_LARGE_IMAGE_URL,
            "altLargeImageUrl.equals=" + UPDATED_ALT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltLargeImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altLargeImageUrl in
        defaultProductCategoryFiltering(
            "altLargeImageUrl.in=" + DEFAULT_ALT_LARGE_IMAGE_URL + "," + UPDATED_ALT_LARGE_IMAGE_URL,
            "altLargeImageUrl.in=" + UPDATED_ALT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltLargeImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altLargeImageUrl is not null
        defaultProductCategoryFiltering("altLargeImageUrl.specified=true", "altLargeImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltLargeImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altLargeImageUrl contains
        defaultProductCategoryFiltering(
            "altLargeImageUrl.contains=" + DEFAULT_ALT_LARGE_IMAGE_URL,
            "altLargeImageUrl.contains=" + UPDATED_ALT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByAltLargeImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where altLargeImageUrl does not contain
        defaultProductCategoryFiltering(
            "altLargeImageUrl.doesNotContain=" + UPDATED_ALT_LARGE_IMAGE_URL,
            "altLargeImageUrl.doesNotContain=" + DEFAULT_ALT_LARGE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllProductCategoriesByIsBannerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where isBanner equals to
        defaultProductCategoryFiltering("isBanner.equals=" + DEFAULT_IS_BANNER, "isBanner.equals=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByIsBannerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where isBanner in
        defaultProductCategoryFiltering("isBanner.in=" + DEFAULT_IS_BANNER + "," + UPDATED_IS_BANNER, "isBanner.in=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByIsBannerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where isBanner is not null
        defaultProductCategoryFiltering("isBanner.specified=true", "isBanner.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByProductCategoryTypeIsEqualToSomething() throws Exception {
        ProductCategoryType productCategoryType;
        if (TestUtil.findAll(em, ProductCategoryType.class).isEmpty()) {
            productCategoryRepository.saveAndFlush(productCategory);
            productCategoryType = ProductCategoryTypeResourceIT.createEntity();
        } else {
            productCategoryType = TestUtil.findAll(em, ProductCategoryType.class).getFirst();
        }
        em.persist(productCategoryType);
        em.flush();
        productCategory.setProductCategoryType(productCategoryType);
        productCategoryRepository.saveAndFlush(productCategory);
        Long productCategoryTypeId = productCategoryType.getId();
        // Get all the productCategoryList where productCategoryType equals to productCategoryTypeId
        defaultProductCategoryShouldBeFound("productCategoryTypeId.equals=" + productCategoryTypeId);

        // Get all the productCategoryList where productCategoryType equals to (productCategoryTypeId + 1)
        defaultProductCategoryShouldNotBeFound("productCategoryTypeId.equals=" + (productCategoryTypeId + 1));
    }

    @Test
    @Transactional
    void getAllProductCategoriesByParentIsEqualToSomething() throws Exception {
        ProductCategory parent;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategoryRepository.saveAndFlush(productCategory);
            parent = ProductCategoryResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        em.persist(parent);
        em.flush();
        productCategory.setParent(parent);
        productCategoryRepository.saveAndFlush(productCategory);
        Long parentId = parent.getId();
        // Get all the productCategoryList where parent equals to parentId
        defaultProductCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the productCategoryList where parent equals to (parentId + 1)
        defaultProductCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultProductCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductCategoryShouldBeFound(shouldBeFound);
        defaultProductCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductCategoryShouldBeFound(String filter) throws Exception {
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altImageUrl").value(hasItem(DEFAULT_ALT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].largeImageUrl").value(hasItem(DEFAULT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].altLargeImageUrl").value(hasItem(DEFAULT_ALT_LARGE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)));

        // Check, that the count call also returns 1
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductCategoryShouldNotBeFound(String filter) throws Exception {
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductCategory() throws Exception {
        // Get the productCategory
        restProductCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductCategory() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findById(productCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductCategory are not directly saved in db
        em.detach(updatedProductCategory);
        updatedProductCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .attribute(UPDATED_ATTRIBUTE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imageUrl(UPDATED_IMAGE_URL)
            .altImageUrl(UPDATED_ALT_IMAGE_URL)
            .bannerUrl(UPDATED_BANNER_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .altLargeImageUrl(UPDATED_ALT_LARGE_IMAGE_URL)
            .info(UPDATED_INFO)
            .isBanner(UPDATED_IS_BANNER);

        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductCategoryToMatchAllProperties(updatedProductCategory);
    }

    @Test
    @Transactional
    void putNonExistingProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imageUrl(UPDATED_IMAGE_URL)
            .altImageUrl(UPDATED_ALT_IMAGE_URL)
            .bannerUrl(UPDATED_BANNER_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .altLargeImageUrl(UPDATED_ALT_LARGE_IMAGE_URL)
            .isBanner(UPDATED_IS_BANNER);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductCategory, productCategory),
            getPersistedProductCategory(productCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .attribute(UPDATED_ATTRIBUTE)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imageUrl(UPDATED_IMAGE_URL)
            .altImageUrl(UPDATED_ALT_IMAGE_URL)
            .bannerUrl(UPDATED_BANNER_URL)
            .largeImageUrl(UPDATED_LARGE_IMAGE_URL)
            .altLargeImageUrl(UPDATED_ALT_LARGE_IMAGE_URL)
            .info(UPDATED_INFO)
            .isBanner(UPDATED_IS_BANNER);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryUpdatableFieldsEquals(
            partialUpdatedProductCategory,
            getPersistedProductCategory(partialUpdatedProductCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCategory() throws Exception {
        // Initialize the database
        insertedProductCategory = productCategoryRepository.saveAndFlush(productCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productCategory
        restProductCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productCategoryRepository.count();
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

    protected ProductCategory getPersistedProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.findById(productCategory.getId()).orElseThrow();
    }

    protected void assertPersistedProductCategoryToMatchAllProperties(ProductCategory expectedProductCategory) {
        assertProductCategoryAllPropertiesEquals(expectedProductCategory, getPersistedProductCategory(expectedProductCategory));
    }

    protected void assertPersistedProductCategoryToMatchUpdatableProperties(ProductCategory expectedProductCategory) {
        assertProductCategoryAllUpdatablePropertiesEquals(expectedProductCategory, getPersistedProductCategory(expectedProductCategory));
    }
}
