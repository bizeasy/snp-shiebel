package com.snp.web.rest;

import static com.snp.domain.ProductFacilityLocationAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityLocation;
import com.snp.domain.Product;
import com.snp.domain.ProductFacilityLocation;
import com.snp.repository.ProductFacilityLocationRepository;
import com.snp.service.ProductFacilityLocationService;
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
 * Integration tests for the {@link ProductFacilityLocationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFacilityLocationResourceIT {

    private static final BigDecimal DEFAULT_MINIMUM_STOCK = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_STOCK = new BigDecimal(2);
    private static final BigDecimal SMALLER_MINIMUM_STOCK = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MOVE_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MOVE_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_MOVE_QUANTITY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/product-facility-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFacilityLocationRepository productFacilityLocationRepository;

    @Mock
    private ProductFacilityLocationRepository productFacilityLocationRepositoryMock;

    @Mock
    private ProductFacilityLocationService productFacilityLocationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFacilityLocationMockMvc;

    private ProductFacilityLocation productFacilityLocation;

    private ProductFacilityLocation insertedProductFacilityLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacilityLocation createEntity(EntityManager em) {
        ProductFacilityLocation productFacilityLocation = new ProductFacilityLocation()
            .minimumStock(DEFAULT_MINIMUM_STOCK)
            .moveQuantity(DEFAULT_MOVE_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productFacilityLocation.setProduct(product);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        productFacilityLocation.setFacility(facility);
        // Add required entity
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            facilityLocation = FacilityLocationResourceIT.createEntity(em);
            em.persist(facilityLocation);
            em.flush();
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        productFacilityLocation.setFacilityLocation(facilityLocation);
        return productFacilityLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacilityLocation createUpdatedEntity(EntityManager em) {
        ProductFacilityLocation updatedProductFacilityLocation = new ProductFacilityLocation()
            .minimumStock(UPDATED_MINIMUM_STOCK)
            .moveQuantity(UPDATED_MOVE_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductFacilityLocation.setProduct(product);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedProductFacilityLocation.setFacility(facility);
        // Add required entity
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            facilityLocation = FacilityLocationResourceIT.createUpdatedEntity(em);
            em.persist(facilityLocation);
            em.flush();
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        updatedProductFacilityLocation.setFacilityLocation(facilityLocation);
        return updatedProductFacilityLocation;
    }

    @BeforeEach
    void initTest() {
        productFacilityLocation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFacilityLocation != null) {
            productFacilityLocationRepository.delete(insertedProductFacilityLocation);
            insertedProductFacilityLocation = null;
        }
    }

    @Test
    @Transactional
    void createProductFacilityLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFacilityLocation
        var returnedProductFacilityLocation = om.readValue(
            restProductFacilityLocationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityLocation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFacilityLocation.class
        );

        // Validate the ProductFacilityLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFacilityLocationUpdatableFieldsEquals(
            returnedProductFacilityLocation,
            getPersistedProductFacilityLocation(returnedProductFacilityLocation)
        );

        insertedProductFacilityLocation = returnedProductFacilityLocation;
    }

    @Test
    @Transactional
    void createProductFacilityLocationWithExistingId() throws Exception {
        // Create the ProductFacilityLocation with an existing ID
        productFacilityLocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFacilityLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityLocation)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductFacilityLocations() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacilityLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumStock").value(hasItem(sameNumber(DEFAULT_MINIMUM_STOCK))))
            .andExpect(jsonPath("$.[*].moveQuantity").value(hasItem(sameNumber(DEFAULT_MOVE_QUANTITY))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilityLocationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFacilityLocationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFacilityLocationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilityLocationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFacilityLocationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFacilityLocationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFacilityLocation() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get the productFacilityLocation
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productFacilityLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFacilityLocation.getId().intValue()))
            .andExpect(jsonPath("$.minimumStock").value(sameNumber(DEFAULT_MINIMUM_STOCK)))
            .andExpect(jsonPath("$.moveQuantity").value(sameNumber(DEFAULT_MOVE_QUANTITY)));
    }

    @Test
    @Transactional
    void getProductFacilityLocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        Long id = productFacilityLocation.getId();

        defaultProductFacilityLocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFacilityLocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFacilityLocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock equals to
        defaultProductFacilityLocationFiltering(
            "minimumStock.equals=" + DEFAULT_MINIMUM_STOCK,
            "minimumStock.equals=" + UPDATED_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock in
        defaultProductFacilityLocationFiltering(
            "minimumStock.in=" + DEFAULT_MINIMUM_STOCK + "," + UPDATED_MINIMUM_STOCK,
            "minimumStock.in=" + UPDATED_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock is not null
        defaultProductFacilityLocationFiltering("minimumStock.specified=true", "minimumStock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock is greater than or equal to
        defaultProductFacilityLocationFiltering(
            "minimumStock.greaterThanOrEqual=" + DEFAULT_MINIMUM_STOCK,
            "minimumStock.greaterThanOrEqual=" + UPDATED_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock is less than or equal to
        defaultProductFacilityLocationFiltering(
            "minimumStock.lessThanOrEqual=" + DEFAULT_MINIMUM_STOCK,
            "minimumStock.lessThanOrEqual=" + SMALLER_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock is less than
        defaultProductFacilityLocationFiltering(
            "minimumStock.lessThan=" + UPDATED_MINIMUM_STOCK,
            "minimumStock.lessThan=" + DEFAULT_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMinimumStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where minimumStock is greater than
        defaultProductFacilityLocationFiltering(
            "minimumStock.greaterThan=" + SMALLER_MINIMUM_STOCK,
            "minimumStock.greaterThan=" + DEFAULT_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity equals to
        defaultProductFacilityLocationFiltering(
            "moveQuantity.equals=" + DEFAULT_MOVE_QUANTITY,
            "moveQuantity.equals=" + UPDATED_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity in
        defaultProductFacilityLocationFiltering(
            "moveQuantity.in=" + DEFAULT_MOVE_QUANTITY + "," + UPDATED_MOVE_QUANTITY,
            "moveQuantity.in=" + UPDATED_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity is not null
        defaultProductFacilityLocationFiltering("moveQuantity.specified=true", "moveQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity is greater than or equal to
        defaultProductFacilityLocationFiltering(
            "moveQuantity.greaterThanOrEqual=" + DEFAULT_MOVE_QUANTITY,
            "moveQuantity.greaterThanOrEqual=" + UPDATED_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity is less than or equal to
        defaultProductFacilityLocationFiltering(
            "moveQuantity.lessThanOrEqual=" + DEFAULT_MOVE_QUANTITY,
            "moveQuantity.lessThanOrEqual=" + SMALLER_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity is less than
        defaultProductFacilityLocationFiltering(
            "moveQuantity.lessThan=" + UPDATED_MOVE_QUANTITY,
            "moveQuantity.lessThan=" + DEFAULT_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByMoveQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        // Get all the productFacilityLocationList where moveQuantity is greater than
        defaultProductFacilityLocationFiltering(
            "moveQuantity.greaterThan=" + SMALLER_MOVE_QUANTITY,
            "moveQuantity.greaterThan=" + DEFAULT_MOVE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productFacilityLocation.setProduct(product);
        productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
        Long productId = product.getId();
        // Get all the productFacilityLocationList where product equals to productId
        defaultProductFacilityLocationShouldBeFound("productId.equals=" + productId);

        // Get all the productFacilityLocationList where product equals to (productId + 1)
        defaultProductFacilityLocationShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        productFacilityLocation.setFacility(facility);
        productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
        Long facilityId = facility.getId();
        // Get all the productFacilityLocationList where facility equals to facilityId
        defaultProductFacilityLocationShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the productFacilityLocationList where facility equals to (facilityId + 1)
        defaultProductFacilityLocationShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllProductFacilityLocationsByFacilityLocationIsEqualToSomething() throws Exception {
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
            facilityLocation = FacilityLocationResourceIT.createEntity(em);
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        em.persist(facilityLocation);
        em.flush();
        productFacilityLocation.setFacilityLocation(facilityLocation);
        productFacilityLocationRepository.saveAndFlush(productFacilityLocation);
        Long facilityLocationId = facilityLocation.getId();
        // Get all the productFacilityLocationList where facilityLocation equals to facilityLocationId
        defaultProductFacilityLocationShouldBeFound("facilityLocationId.equals=" + facilityLocationId);

        // Get all the productFacilityLocationList where facilityLocation equals to (facilityLocationId + 1)
        defaultProductFacilityLocationShouldNotBeFound("facilityLocationId.equals=" + (facilityLocationId + 1));
    }

    private void defaultProductFacilityLocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFacilityLocationShouldBeFound(shouldBeFound);
        defaultProductFacilityLocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFacilityLocationShouldBeFound(String filter) throws Exception {
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacilityLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumStock").value(hasItem(sameNumber(DEFAULT_MINIMUM_STOCK))))
            .andExpect(jsonPath("$.[*].moveQuantity").value(hasItem(sameNumber(DEFAULT_MOVE_QUANTITY))));

        // Check, that the count call also returns 1
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFacilityLocationShouldNotBeFound(String filter) throws Exception {
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFacilityLocation() throws Exception {
        // Get the productFacilityLocation
        restProductFacilityLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFacilityLocation() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityLocation
        ProductFacilityLocation updatedProductFacilityLocation = productFacilityLocationRepository
            .findById(productFacilityLocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductFacilityLocation are not directly saved in db
        em.detach(updatedProductFacilityLocation);
        updatedProductFacilityLocation.minimumStock(UPDATED_MINIMUM_STOCK).moveQuantity(UPDATED_MOVE_QUANTITY);

        restProductFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFacilityLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFacilityLocationToMatchAllProperties(updatedProductFacilityLocation);
    }

    @Test
    @Transactional
    void putNonExistingProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFacilityLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFacilityLocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityLocation using partial update
        ProductFacilityLocation partialUpdatedProductFacilityLocation = new ProductFacilityLocation();
        partialUpdatedProductFacilityLocation.setId(productFacilityLocation.getId());

        partialUpdatedProductFacilityLocation.minimumStock(UPDATED_MINIMUM_STOCK);

        restProductFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFacilityLocation, productFacilityLocation),
            getPersistedProductFacilityLocation(productFacilityLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFacilityLocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityLocation using partial update
        ProductFacilityLocation partialUpdatedProductFacilityLocation = new ProductFacilityLocation();
        partialUpdatedProductFacilityLocation.setId(productFacilityLocation.getId());

        partialUpdatedProductFacilityLocation.minimumStock(UPDATED_MINIMUM_STOCK).moveQuantity(UPDATED_MOVE_QUANTITY);

        restProductFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityLocationUpdatableFieldsEquals(
            partialUpdatedProductFacilityLocation,
            getPersistedProductFacilityLocation(partialUpdatedProductFacilityLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFacilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFacilityLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFacilityLocation() throws Exception {
        // Initialize the database
        insertedProductFacilityLocation = productFacilityLocationRepository.saveAndFlush(productFacilityLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFacilityLocation
        restProductFacilityLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFacilityLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFacilityLocationRepository.count();
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

    protected ProductFacilityLocation getPersistedProductFacilityLocation(ProductFacilityLocation productFacilityLocation) {
        return productFacilityLocationRepository.findById(productFacilityLocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductFacilityLocationToMatchAllProperties(ProductFacilityLocation expectedProductFacilityLocation) {
        assertProductFacilityLocationAllPropertiesEquals(
            expectedProductFacilityLocation,
            getPersistedProductFacilityLocation(expectedProductFacilityLocation)
        );
    }

    protected void assertPersistedProductFacilityLocationToMatchUpdatableProperties(
        ProductFacilityLocation expectedProductFacilityLocation
    ) {
        assertProductFacilityLocationAllUpdatablePropertiesEquals(
            expectedProductFacilityLocation,
            getPersistedProductFacilityLocation(expectedProductFacilityLocation)
        );
    }
}
