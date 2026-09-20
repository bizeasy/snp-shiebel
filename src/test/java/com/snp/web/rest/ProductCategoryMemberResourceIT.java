package com.snp.web.rest;

import static com.snp.domain.ProductCategoryMemberAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.ProductCategory;
import com.snp.domain.ProductCategoryMember;
import com.snp.repository.ProductCategoryMemberRepository;
import com.snp.service.ProductCategoryMemberService;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ProductCategoryMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryMemberResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-category-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductCategoryMemberRepository productCategoryMemberRepository;

    @Mock
    private ProductCategoryMemberRepository productCategoryMemberRepositoryMock;

    @Mock
    private ProductCategoryMemberService productCategoryMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCategoryMemberMockMvc;

    private ProductCategoryMember productCategoryMember;

    private ProductCategoryMember insertedProductCategoryMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategoryMember createEntity(EntityManager em) {
        ProductCategoryMember productCategoryMember = new ProductCategoryMember()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productCategoryMember.setProduct(product);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        productCategoryMember.setProductCategory(productCategory);
        return productCategoryMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategoryMember createUpdatedEntity(EntityManager em) {
        ProductCategoryMember updatedProductCategoryMember = new ProductCategoryMember()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductCategoryMember.setProduct(product);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createUpdatedEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        updatedProductCategoryMember.setProductCategory(productCategory);
        return updatedProductCategoryMember;
    }

    @BeforeEach
    void initTest() {
        productCategoryMember = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductCategoryMember != null) {
            productCategoryMemberRepository.delete(insertedProductCategoryMember);
            insertedProductCategoryMember = null;
        }
    }

    @Test
    @Transactional
    void createProductCategoryMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductCategoryMember
        var returnedProductCategoryMember = om.readValue(
            restProductCategoryMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryMember)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductCategoryMember.class
        );

        // Validate the ProductCategoryMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductCategoryMemberUpdatableFieldsEquals(
            returnedProductCategoryMember,
            getPersistedProductCategoryMember(returnedProductCategoryMember)
        );

        insertedProductCategoryMember = returnedProductCategoryMember;
    }

    @Test
    @Transactional
    void createProductCategoryMemberWithExistingId() throws Exception {
        // Create the ProductCategoryMember with an existing ID
        productCategoryMember.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryMember)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productCategoryMember.setFromDate(null);

        // Create the ProductCategoryMember, which fails.

        restProductCategoryMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryMember)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembers() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategoryMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoryMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(productCategoryMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductCategoryMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productCategoryMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoryMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productCategoryMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductCategoryMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productCategoryMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductCategoryMember() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get the productCategoryMember
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, productCategoryMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCategoryMember.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getProductCategoryMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        Long id = productCategoryMember.getId();

        defaultProductCategoryMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductCategoryMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductCategoryMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where fromDate equals to
        defaultProductCategoryMemberFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where fromDate in
        defaultProductCategoryMemberFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where fromDate is not null
        defaultProductCategoryMemberFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where thruDate equals to
        defaultProductCategoryMemberFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where thruDate in
        defaultProductCategoryMemberFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where thruDate is not null
        defaultProductCategoryMemberFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo equals to
        defaultProductCategoryMemberFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo in
        defaultProductCategoryMemberFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo is not null
        defaultProductCategoryMemberFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo is greater than or equal to
        defaultProductCategoryMemberFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo is less than or equal to
        defaultProductCategoryMemberFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo is less than
        defaultProductCategoryMemberFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        // Get all the productCategoryMemberList where sequenceNo is greater than
        defaultProductCategoryMemberFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productCategoryMemberRepository.saveAndFlush(productCategoryMember);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productCategoryMember.setProduct(product);
        productCategoryMemberRepository.saveAndFlush(productCategoryMember);
        Long productId = product.getId();
        // Get all the productCategoryMemberList where product equals to productId
        defaultProductCategoryMemberShouldBeFound("productId.equals=" + productId);

        // Get all the productCategoryMemberList where product equals to (productId + 1)
        defaultProductCategoryMemberShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductCategoryMembersByProductCategoryIsEqualToSomething() throws Exception {
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategoryMemberRepository.saveAndFlush(productCategoryMember);
            productCategory = ProductCategoryResourceIT.createEntity();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        em.persist(productCategory);
        em.flush();
        productCategoryMember.setProductCategory(productCategory);
        productCategoryMemberRepository.saveAndFlush(productCategoryMember);
        Long productCategoryId = productCategory.getId();
        // Get all the productCategoryMemberList where productCategory equals to productCategoryId
        defaultProductCategoryMemberShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the productCategoryMemberList where productCategory equals to (productCategoryId + 1)
        defaultProductCategoryMemberShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    private void defaultProductCategoryMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductCategoryMemberShouldBeFound(shouldBeFound);
        defaultProductCategoryMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductCategoryMemberShouldBeFound(String filter) throws Exception {
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategoryMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductCategoryMemberShouldNotBeFound(String filter) throws Exception {
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductCategoryMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductCategoryMember() throws Exception {
        // Get the productCategoryMember
        restProductCategoryMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductCategoryMember() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryMember
        ProductCategoryMember updatedProductCategoryMember = productCategoryMemberRepository
            .findById(productCategoryMember.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductCategoryMember are not directly saved in db
        em.detach(updatedProductCategoryMember);
        updatedProductCategoryMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restProductCategoryMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCategoryMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductCategoryMember))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductCategoryMemberToMatchAllProperties(updatedProductCategoryMember);
    }

    @Test
    @Transactional
    void putNonExistingProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCategoryMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategoryMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productCategoryMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productCategoryMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCategoryMemberWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryMember using partial update
        ProductCategoryMember partialUpdatedProductCategoryMember = new ProductCategoryMember();
        partialUpdatedProductCategoryMember.setId(productCategoryMember.getId());

        partialUpdatedProductCategoryMember.thruDate(UPDATED_THRU_DATE);

        restProductCategoryMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategoryMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategoryMember))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductCategoryMember, productCategoryMember),
            getPersistedProductCategoryMember(productCategoryMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductCategoryMemberWithPatch() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productCategoryMember using partial update
        ProductCategoryMember partialUpdatedProductCategoryMember = new ProductCategoryMember();
        partialUpdatedProductCategoryMember.setId(productCategoryMember.getId());

        partialUpdatedProductCategoryMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restProductCategoryMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategoryMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductCategoryMember))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategoryMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductCategoryMemberUpdatableFieldsEquals(
            partialUpdatedProductCategoryMember,
            getPersistedProductCategoryMember(partialUpdatedProductCategoryMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCategoryMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategoryMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productCategoryMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCategoryMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productCategoryMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productCategoryMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategoryMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCategoryMember() throws Exception {
        // Initialize the database
        insertedProductCategoryMember = productCategoryMemberRepository.saveAndFlush(productCategoryMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productCategoryMember
        restProductCategoryMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCategoryMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productCategoryMemberRepository.count();
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

    protected ProductCategoryMember getPersistedProductCategoryMember(ProductCategoryMember productCategoryMember) {
        return productCategoryMemberRepository.findById(productCategoryMember.getId()).orElseThrow();
    }

    protected void assertPersistedProductCategoryMemberToMatchAllProperties(ProductCategoryMember expectedProductCategoryMember) {
        assertProductCategoryMemberAllPropertiesEquals(
            expectedProductCategoryMember,
            getPersistedProductCategoryMember(expectedProductCategoryMember)
        );
    }

    protected void assertPersistedProductCategoryMemberToMatchUpdatableProperties(ProductCategoryMember expectedProductCategoryMember) {
        assertProductCategoryMemberAllUpdatablePropertiesEquals(
            expectedProductCategoryMember,
            getPersistedProductCategoryMember(expectedProductCategoryMember)
        );
    }
}
