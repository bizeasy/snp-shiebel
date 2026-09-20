package com.snp.web.rest;

import static com.snp.domain.ProductFeatureCategoryApplAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductCategory;
import com.snp.domain.ProductFeatureCategory;
import com.snp.domain.ProductFeatureCategoryAppl;
import com.snp.repository.ProductFeatureCategoryApplRepository;
import com.snp.service.ProductFeatureCategoryApplService;
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
 * Integration tests for the {@link ProductFeatureCategoryApplResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureCategoryApplResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/product-feature-category-appls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository;

    @Mock
    private ProductFeatureCategoryApplRepository productFeatureCategoryApplRepositoryMock;

    @Mock
    private ProductFeatureCategoryApplService productFeatureCategoryApplServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureCategoryApplMockMvc;

    private ProductFeatureCategoryAppl productFeatureCategoryAppl;

    private ProductFeatureCategoryAppl insertedProductFeatureCategoryAppl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureCategoryAppl createEntity(EntityManager em) {
        ProductFeatureCategoryAppl productFeatureCategoryAppl = new ProductFeatureCategoryAppl()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        productFeatureCategoryAppl.setProductCategory(productCategory);
        // Add required entity
        ProductFeatureCategory productFeatureCategory;
        if (TestUtil.findAll(em, ProductFeatureCategory.class).isEmpty()) {
            productFeatureCategory = ProductFeatureCategoryResourceIT.createEntity();
            em.persist(productFeatureCategory);
            em.flush();
        } else {
            productFeatureCategory = TestUtil.findAll(em, ProductFeatureCategory.class).getFirst();
        }
        productFeatureCategoryAppl.setProductFeatureCategory(productFeatureCategory);
        return productFeatureCategoryAppl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureCategoryAppl createUpdatedEntity(EntityManager em) {
        ProductFeatureCategoryAppl updatedProductFeatureCategoryAppl = new ProductFeatureCategoryAppl()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createUpdatedEntity();
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        updatedProductFeatureCategoryAppl.setProductCategory(productCategory);
        // Add required entity
        ProductFeatureCategory productFeatureCategory;
        if (TestUtil.findAll(em, ProductFeatureCategory.class).isEmpty()) {
            productFeatureCategory = ProductFeatureCategoryResourceIT.createUpdatedEntity();
            em.persist(productFeatureCategory);
            em.flush();
        } else {
            productFeatureCategory = TestUtil.findAll(em, ProductFeatureCategory.class).getFirst();
        }
        updatedProductFeatureCategoryAppl.setProductFeatureCategory(productFeatureCategory);
        return updatedProductFeatureCategoryAppl;
    }

    @BeforeEach
    void initTest() {
        productFeatureCategoryAppl = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureCategoryAppl != null) {
            productFeatureCategoryApplRepository.delete(insertedProductFeatureCategoryAppl);
            insertedProductFeatureCategoryAppl = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureCategoryAppl
        var returnedProductFeatureCategoryAppl = om.readValue(
            restProductFeatureCategoryApplMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategoryAppl))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureCategoryAppl.class
        );

        // Validate the ProductFeatureCategoryAppl in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureCategoryApplUpdatableFieldsEquals(
            returnedProductFeatureCategoryAppl,
            getPersistedProductFeatureCategoryAppl(returnedProductFeatureCategoryAppl)
        );

        insertedProductFeatureCategoryAppl = returnedProductFeatureCategoryAppl;
    }

    @Test
    @Transactional
    void createProductFeatureCategoryApplWithExistingId() throws Exception {
        // Create the ProductFeatureCategoryAppl with an existing ID
        productFeatureCategoryAppl.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureCategoryApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategoryAppl)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureCategoryAppl.setFromDate(null);

        // Create the ProductFeatureCategoryAppl, which fails.

        restProductFeatureCategoryApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategoryAppl)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryAppls() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureCategoryAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureCategoryApplsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFeatureCategoryApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureCategoryApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFeatureCategoryApplServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureCategoryApplsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFeatureCategoryApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureCategoryApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFeatureCategoryApplRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFeatureCategoryAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get the productFeatureCategoryAppl
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureCategoryAppl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureCategoryAppl.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductFeatureCategoryApplsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        Long id = productFeatureCategoryAppl.getId();

        defaultProductFeatureCategoryApplFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureCategoryApplFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureCategoryApplFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where fromDate equals to
        defaultProductFeatureCategoryApplFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where fromDate in
        defaultProductFeatureCategoryApplFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where fromDate is not null
        defaultProductFeatureCategoryApplFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where thruDate equals to
        defaultProductFeatureCategoryApplFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where thruDate in
        defaultProductFeatureCategoryApplFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        // Get all the productFeatureCategoryApplList where thruDate is not null
        defaultProductFeatureCategoryApplFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByProductCategoryIsEqualToSomething() throws Exception {
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);
            productCategory = ProductCategoryResourceIT.createEntity();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).getFirst();
        }
        em.persist(productCategory);
        em.flush();
        productFeatureCategoryAppl.setProductCategory(productCategory);
        productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);
        Long productCategoryId = productCategory.getId();
        // Get all the productFeatureCategoryApplList where productCategory equals to productCategoryId
        defaultProductFeatureCategoryApplShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the productFeatureCategoryApplList where productCategory equals to (productCategoryId + 1)
        defaultProductFeatureCategoryApplShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductFeatureCategoryApplsByProductFeatureCategoryIsEqualToSomething() throws Exception {
        ProductFeatureCategory productFeatureCategory;
        if (TestUtil.findAll(em, ProductFeatureCategory.class).isEmpty()) {
            productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);
            productFeatureCategory = ProductFeatureCategoryResourceIT.createEntity();
        } else {
            productFeatureCategory = TestUtil.findAll(em, ProductFeatureCategory.class).getFirst();
        }
        em.persist(productFeatureCategory);
        em.flush();
        productFeatureCategoryAppl.setProductFeatureCategory(productFeatureCategory);
        productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);
        Long productFeatureCategoryId = productFeatureCategory.getId();
        // Get all the productFeatureCategoryApplList where productFeatureCategory equals to productFeatureCategoryId
        defaultProductFeatureCategoryApplShouldBeFound("productFeatureCategoryId.equals=" + productFeatureCategoryId);

        // Get all the productFeatureCategoryApplList where productFeatureCategory equals to (productFeatureCategoryId + 1)
        defaultProductFeatureCategoryApplShouldNotBeFound("productFeatureCategoryId.equals=" + (productFeatureCategoryId + 1));
    }

    private void defaultProductFeatureCategoryApplFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureCategoryApplShouldBeFound(shouldBeFound);
        defaultProductFeatureCategoryApplShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureCategoryApplShouldBeFound(String filter) throws Exception {
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureCategoryAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())));

        // Check, that the count call also returns 1
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureCategoryApplShouldNotBeFound(String filter) throws Exception {
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureCategoryApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureCategoryAppl() throws Exception {
        // Get the productFeatureCategoryAppl
        restProductFeatureCategoryApplMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureCategoryAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategoryAppl
        ProductFeatureCategoryAppl updatedProductFeatureCategoryAppl = productFeatureCategoryApplRepository
            .findById(productFeatureCategoryAppl.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureCategoryAppl are not directly saved in db
        em.detach(updatedProductFeatureCategoryAppl);
        updatedProductFeatureCategoryAppl.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restProductFeatureCategoryApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureCategoryAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureCategoryAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureCategoryApplToMatchAllProperties(updatedProductFeatureCategoryAppl);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureCategoryAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureCategoryAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureCategoryAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureCategoryAppl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureCategoryApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategoryAppl using partial update
        ProductFeatureCategoryAppl partialUpdatedProductFeatureCategoryAppl = new ProductFeatureCategoryAppl();
        partialUpdatedProductFeatureCategoryAppl.setId(productFeatureCategoryAppl.getId());

        restProductFeatureCategoryApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureCategoryAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureCategoryAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategoryAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureCategoryApplUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureCategoryAppl, productFeatureCategoryAppl),
            getPersistedProductFeatureCategoryAppl(productFeatureCategoryAppl)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureCategoryApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureCategoryAppl using partial update
        ProductFeatureCategoryAppl partialUpdatedProductFeatureCategoryAppl = new ProductFeatureCategoryAppl();
        partialUpdatedProductFeatureCategoryAppl.setId(productFeatureCategoryAppl.getId());

        partialUpdatedProductFeatureCategoryAppl.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE);

        restProductFeatureCategoryApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureCategoryAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureCategoryAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureCategoryAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureCategoryApplUpdatableFieldsEquals(
            partialUpdatedProductFeatureCategoryAppl,
            getPersistedProductFeatureCategoryAppl(partialUpdatedProductFeatureCategoryAppl)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureCategoryAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureCategoryAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureCategoryAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureCategoryAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureCategoryAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureCategoryApplMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureCategoryAppl))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureCategoryAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureCategoryAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureCategoryAppl = productFeatureCategoryApplRepository.saveAndFlush(productFeatureCategoryAppl);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureCategoryAppl
        restProductFeatureCategoryApplMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureCategoryAppl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureCategoryApplRepository.count();
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

    protected ProductFeatureCategoryAppl getPersistedProductFeatureCategoryAppl(ProductFeatureCategoryAppl productFeatureCategoryAppl) {
        return productFeatureCategoryApplRepository.findById(productFeatureCategoryAppl.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureCategoryApplToMatchAllProperties(
        ProductFeatureCategoryAppl expectedProductFeatureCategoryAppl
    ) {
        assertProductFeatureCategoryApplAllPropertiesEquals(
            expectedProductFeatureCategoryAppl,
            getPersistedProductFeatureCategoryAppl(expectedProductFeatureCategoryAppl)
        );
    }

    protected void assertPersistedProductFeatureCategoryApplToMatchUpdatableProperties(
        ProductFeatureCategoryAppl expectedProductFeatureCategoryAppl
    ) {
        assertProductFeatureCategoryApplAllUpdatablePropertiesEquals(
            expectedProductFeatureCategoryAppl,
            getPersistedProductFeatureCategoryAppl(expectedProductFeatureCategoryAppl)
        );
    }
}
