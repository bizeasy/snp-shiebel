package com.snp.web.rest;

import static com.snp.domain.ProductFeatureGroupApplAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ProductFeature;
import com.snp.domain.ProductFeatureGroup;
import com.snp.domain.ProductFeatureGroupAppl;
import com.snp.repository.ProductFeatureGroupApplRepository;
import com.snp.service.ProductFeatureGroupApplService;
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
 * Integration tests for the {@link ProductFeatureGroupApplResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFeatureGroupApplResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-feature-group-appls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFeatureGroupApplRepository productFeatureGroupApplRepository;

    @Mock
    private ProductFeatureGroupApplRepository productFeatureGroupApplRepositoryMock;

    @Mock
    private ProductFeatureGroupApplService productFeatureGroupApplServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeatureGroupApplMockMvc;

    private ProductFeatureGroupAppl productFeatureGroupAppl;

    private ProductFeatureGroupAppl insertedProductFeatureGroupAppl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureGroupAppl createEntity(EntityManager em) {
        ProductFeatureGroupAppl productFeatureGroupAppl = new ProductFeatureGroupAppl()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .sequenceNo(DEFAULT_SEQUENCE_NO);
        // Add required entity
        ProductFeatureGroup productFeatureGroup;
        if (TestUtil.findAll(em, ProductFeatureGroup.class).isEmpty()) {
            productFeatureGroup = ProductFeatureGroupResourceIT.createEntity();
            em.persist(productFeatureGroup);
            em.flush();
        } else {
            productFeatureGroup = TestUtil.findAll(em, ProductFeatureGroup.class).getFirst();
        }
        productFeatureGroupAppl.setProductFeatureGroup(productFeatureGroup);
        // Add required entity
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeature = ProductFeatureResourceIT.createEntity();
            em.persist(productFeature);
            em.flush();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        productFeatureGroupAppl.setProductFeature(productFeature);
        return productFeatureGroupAppl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeatureGroupAppl createUpdatedEntity(EntityManager em) {
        ProductFeatureGroupAppl updatedProductFeatureGroupAppl = new ProductFeatureGroupAppl()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .sequenceNo(UPDATED_SEQUENCE_NO);
        // Add required entity
        ProductFeatureGroup productFeatureGroup;
        if (TestUtil.findAll(em, ProductFeatureGroup.class).isEmpty()) {
            productFeatureGroup = ProductFeatureGroupResourceIT.createUpdatedEntity();
            em.persist(productFeatureGroup);
            em.flush();
        } else {
            productFeatureGroup = TestUtil.findAll(em, ProductFeatureGroup.class).getFirst();
        }
        updatedProductFeatureGroupAppl.setProductFeatureGroup(productFeatureGroup);
        // Add required entity
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeature = ProductFeatureResourceIT.createUpdatedEntity();
            em.persist(productFeature);
            em.flush();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        updatedProductFeatureGroupAppl.setProductFeature(productFeature);
        return updatedProductFeatureGroupAppl;
    }

    @BeforeEach
    void initTest() {
        productFeatureGroupAppl = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFeatureGroupAppl != null) {
            productFeatureGroupApplRepository.delete(insertedProductFeatureGroupAppl);
            insertedProductFeatureGroupAppl = null;
        }
    }

    @Test
    @Transactional
    void createProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFeatureGroupAppl
        var returnedProductFeatureGroupAppl = om.readValue(
            restProductFeatureGroupApplMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroupAppl))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFeatureGroupAppl.class
        );

        // Validate the ProductFeatureGroupAppl in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFeatureGroupApplUpdatableFieldsEquals(
            returnedProductFeatureGroupAppl,
            getPersistedProductFeatureGroupAppl(returnedProductFeatureGroupAppl)
        );

        insertedProductFeatureGroupAppl = returnedProductFeatureGroupAppl;
    }

    @Test
    @Transactional
    void createProductFeatureGroupApplWithExistingId() throws Exception {
        // Create the ProductFeatureGroupAppl with an existing ID
        productFeatureGroupAppl.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeatureGroupApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroupAppl)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFeatureGroupAppl.setFromDate(null);

        // Create the ProductFeatureGroupAppl, which fails.

        restProductFeatureGroupApplMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroupAppl)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupAppls() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureGroupAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureGroupApplsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFeatureGroupApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureGroupApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFeatureGroupApplServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFeatureGroupApplsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFeatureGroupApplServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFeatureGroupApplMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFeatureGroupApplRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFeatureGroupAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get the productFeatureGroupAppl
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeatureGroupAppl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeatureGroupAppl.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO));
    }

    @Test
    @Transactional
    void getProductFeatureGroupApplsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        Long id = productFeatureGroupAppl.getId();

        defaultProductFeatureGroupApplFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFeatureGroupApplFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFeatureGroupApplFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where fromDate equals to
        defaultProductFeatureGroupApplFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where fromDate in
        defaultProductFeatureGroupApplFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where fromDate is not null
        defaultProductFeatureGroupApplFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where thruDate equals to
        defaultProductFeatureGroupApplFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where thruDate in
        defaultProductFeatureGroupApplFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where thruDate is not null
        defaultProductFeatureGroupApplFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo equals to
        defaultProductFeatureGroupApplFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo in
        defaultProductFeatureGroupApplFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo is not null
        defaultProductFeatureGroupApplFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo is greater than or equal to
        defaultProductFeatureGroupApplFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo is less than or equal to
        defaultProductFeatureGroupApplFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo is less than
        defaultProductFeatureGroupApplFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        // Get all the productFeatureGroupApplList where sequenceNo is greater than
        defaultProductFeatureGroupApplFiltering(
            "sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO,
            "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByProductFeatureGroupIsEqualToSomething() throws Exception {
        ProductFeatureGroup productFeatureGroup;
        if (TestUtil.findAll(em, ProductFeatureGroup.class).isEmpty()) {
            productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);
            productFeatureGroup = ProductFeatureGroupResourceIT.createEntity();
        } else {
            productFeatureGroup = TestUtil.findAll(em, ProductFeatureGroup.class).getFirst();
        }
        em.persist(productFeatureGroup);
        em.flush();
        productFeatureGroupAppl.setProductFeatureGroup(productFeatureGroup);
        productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);
        Long productFeatureGroupId = productFeatureGroup.getId();
        // Get all the productFeatureGroupApplList where productFeatureGroup equals to productFeatureGroupId
        defaultProductFeatureGroupApplShouldBeFound("productFeatureGroupId.equals=" + productFeatureGroupId);

        // Get all the productFeatureGroupApplList where productFeatureGroup equals to (productFeatureGroupId + 1)
        defaultProductFeatureGroupApplShouldNotBeFound("productFeatureGroupId.equals=" + (productFeatureGroupId + 1));
    }

    @Test
    @Transactional
    void getAllProductFeatureGroupApplsByProductFeatureIsEqualToSomething() throws Exception {
        ProductFeature productFeature;
        if (TestUtil.findAll(em, ProductFeature.class).isEmpty()) {
            productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);
            productFeature = ProductFeatureResourceIT.createEntity();
        } else {
            productFeature = TestUtil.findAll(em, ProductFeature.class).getFirst();
        }
        em.persist(productFeature);
        em.flush();
        productFeatureGroupAppl.setProductFeature(productFeature);
        productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);
        Long productFeatureId = productFeature.getId();
        // Get all the productFeatureGroupApplList where productFeature equals to productFeatureId
        defaultProductFeatureGroupApplShouldBeFound("productFeatureId.equals=" + productFeatureId);

        // Get all the productFeatureGroupApplList where productFeature equals to (productFeatureId + 1)
        defaultProductFeatureGroupApplShouldNotBeFound("productFeatureId.equals=" + (productFeatureId + 1));
    }

    private void defaultProductFeatureGroupApplFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFeatureGroupApplShouldBeFound(shouldBeFound);
        defaultProductFeatureGroupApplShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFeatureGroupApplShouldBeFound(String filter) throws Exception {
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeatureGroupAppl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)));

        // Check, that the count call also returns 1
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFeatureGroupApplShouldNotBeFound(String filter) throws Exception {
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFeatureGroupApplMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFeatureGroupAppl() throws Exception {
        // Get the productFeatureGroupAppl
        restProductFeatureGroupApplMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFeatureGroupAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroupAppl
        ProductFeatureGroupAppl updatedProductFeatureGroupAppl = productFeatureGroupApplRepository
            .findById(productFeatureGroupAppl.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductFeatureGroupAppl are not directly saved in db
        em.detach(updatedProductFeatureGroupAppl);
        updatedProductFeatureGroupAppl.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restProductFeatureGroupApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeatureGroupAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFeatureGroupAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFeatureGroupApplToMatchAllProperties(updatedProductFeatureGroupAppl);
    }

    @Test
    @Transactional
    void putNonExistingProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeatureGroupAppl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureGroupAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFeatureGroupAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFeatureGroupAppl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeatureGroupApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroupAppl using partial update
        ProductFeatureGroupAppl partialUpdatedProductFeatureGroupAppl = new ProductFeatureGroupAppl();
        partialUpdatedProductFeatureGroupAppl.setId(productFeatureGroupAppl.getId());

        partialUpdatedProductFeatureGroupAppl.sequenceNo(UPDATED_SEQUENCE_NO);

        restProductFeatureGroupApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureGroupAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureGroupAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroupAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureGroupApplUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFeatureGroupAppl, productFeatureGroupAppl),
            getPersistedProductFeatureGroupAppl(productFeatureGroupAppl)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFeatureGroupApplWithPatch() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFeatureGroupAppl using partial update
        ProductFeatureGroupAppl partialUpdatedProductFeatureGroupAppl = new ProductFeatureGroupAppl();
        partialUpdatedProductFeatureGroupAppl.setId(productFeatureGroupAppl.getId());

        partialUpdatedProductFeatureGroupAppl.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).sequenceNo(UPDATED_SEQUENCE_NO);

        restProductFeatureGroupApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeatureGroupAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFeatureGroupAppl))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeatureGroupAppl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFeatureGroupApplUpdatableFieldsEquals(
            partialUpdatedProductFeatureGroupAppl,
            getPersistedProductFeatureGroupAppl(partialUpdatedProductFeatureGroupAppl)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeatureGroupAppl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureGroupAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFeatureGroupAppl))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeatureGroupAppl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFeatureGroupAppl.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeatureGroupApplMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFeatureGroupAppl))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeatureGroupAppl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeatureGroupAppl() throws Exception {
        // Initialize the database
        insertedProductFeatureGroupAppl = productFeatureGroupApplRepository.saveAndFlush(productFeatureGroupAppl);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFeatureGroupAppl
        restProductFeatureGroupApplMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeatureGroupAppl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFeatureGroupApplRepository.count();
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

    protected ProductFeatureGroupAppl getPersistedProductFeatureGroupAppl(ProductFeatureGroupAppl productFeatureGroupAppl) {
        return productFeatureGroupApplRepository.findById(productFeatureGroupAppl.getId()).orElseThrow();
    }

    protected void assertPersistedProductFeatureGroupApplToMatchAllProperties(ProductFeatureGroupAppl expectedProductFeatureGroupAppl) {
        assertProductFeatureGroupApplAllPropertiesEquals(
            expectedProductFeatureGroupAppl,
            getPersistedProductFeatureGroupAppl(expectedProductFeatureGroupAppl)
        );
    }

    protected void assertPersistedProductFeatureGroupApplToMatchUpdatableProperties(
        ProductFeatureGroupAppl expectedProductFeatureGroupAppl
    ) {
        assertProductFeatureGroupApplAllUpdatablePropertiesEquals(
            expectedProductFeatureGroupAppl,
            getPersistedProductFeatureGroupAppl(expectedProductFeatureGroupAppl)
        );
    }
}
