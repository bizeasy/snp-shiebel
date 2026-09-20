package com.snp.web.rest;

import static com.snp.domain.ProductFacilityAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.Product;
import com.snp.domain.ProductFacility;
import com.snp.repository.ProductFacilityRepository;
import com.snp.service.ProductFacilityService;
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
 * Integration tests for the {@link ProductFacilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFacilityResourceIT {

    private static final BigDecimal DEFAULT_MINIMUM_STOCK = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_STOCK = new BigDecimal(2);
    private static final BigDecimal SMALLER_MINIMUM_STOCK = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_REORDER_QTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_REORDER_QTY = new BigDecimal(2);
    private static final BigDecimal SMALLER_REORDER_QTY = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_DAYS_TO_SHIP = 1;
    private static final Integer UPDATED_DAYS_TO_SHIP = 2;
    private static final Integer SMALLER_DAYS_TO_SHIP = 1 - 1;

    private static final BigDecimal DEFAULT_LAST_INVENTORY_COUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_INVENTORY_COUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LAST_INVENTORY_COUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/product-facilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFacilityRepository productFacilityRepository;

    @Mock
    private ProductFacilityRepository productFacilityRepositoryMock;

    @Mock
    private ProductFacilityService productFacilityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFacilityMockMvc;

    private ProductFacility productFacility;

    private ProductFacility insertedProductFacility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacility createEntity(EntityManager em) {
        ProductFacility productFacility = new ProductFacility()
            .minimumStock(DEFAULT_MINIMUM_STOCK)
            .reorderQty(DEFAULT_REORDER_QTY)
            .daysToShip(DEFAULT_DAYS_TO_SHIP)
            .lastInventoryCount(DEFAULT_LAST_INVENTORY_COUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productFacility.setProduct(product);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        productFacility.setFacility(facility);
        return productFacility;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacility createUpdatedEntity(EntityManager em) {
        ProductFacility updatedProductFacility = new ProductFacility()
            .minimumStock(UPDATED_MINIMUM_STOCK)
            .reorderQty(UPDATED_REORDER_QTY)
            .daysToShip(UPDATED_DAYS_TO_SHIP)
            .lastInventoryCount(UPDATED_LAST_INVENTORY_COUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductFacility.setProduct(product);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedProductFacility.setFacility(facility);
        return updatedProductFacility;
    }

    @BeforeEach
    void initTest() {
        productFacility = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFacility != null) {
            productFacilityRepository.delete(insertedProductFacility);
            insertedProductFacility = null;
        }
    }

    @Test
    @Transactional
    void createProductFacility() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFacility
        var returnedProductFacility = om.readValue(
            restProductFacilityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacility)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFacility.class
        );

        // Validate the ProductFacility in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFacilityUpdatableFieldsEquals(returnedProductFacility, getPersistedProductFacility(returnedProductFacility));

        insertedProductFacility = returnedProductFacility;
    }

    @Test
    @Transactional
    void createProductFacilityWithExistingId() throws Exception {
        // Create the ProductFacility with an existing ID
        productFacility.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFacilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacility)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductFacilities() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumStock").value(hasItem(sameNumber(DEFAULT_MINIMUM_STOCK))))
            .andExpect(jsonPath("$.[*].reorderQty").value(hasItem(sameNumber(DEFAULT_REORDER_QTY))))
            .andExpect(jsonPath("$.[*].daysToShip").value(hasItem(DEFAULT_DAYS_TO_SHIP)))
            .andExpect(jsonPath("$.[*].lastInventoryCount").value(hasItem(sameNumber(DEFAULT_LAST_INVENTORY_COUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFacilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFacilityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFacilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFacilityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFacility() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get the productFacility
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL_ID, productFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFacility.getId().intValue()))
            .andExpect(jsonPath("$.minimumStock").value(sameNumber(DEFAULT_MINIMUM_STOCK)))
            .andExpect(jsonPath("$.reorderQty").value(sameNumber(DEFAULT_REORDER_QTY)))
            .andExpect(jsonPath("$.daysToShip").value(DEFAULT_DAYS_TO_SHIP))
            .andExpect(jsonPath("$.lastInventoryCount").value(sameNumber(DEFAULT_LAST_INVENTORY_COUNT)));
    }

    @Test
    @Transactional
    void getProductFacilitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        Long id = productFacility.getId();

        defaultProductFacilityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFacilityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFacilityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock equals to
        defaultProductFacilityFiltering("minimumStock.equals=" + DEFAULT_MINIMUM_STOCK, "minimumStock.equals=" + UPDATED_MINIMUM_STOCK);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock in
        defaultProductFacilityFiltering(
            "minimumStock.in=" + DEFAULT_MINIMUM_STOCK + "," + UPDATED_MINIMUM_STOCK,
            "minimumStock.in=" + UPDATED_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock is not null
        defaultProductFacilityFiltering("minimumStock.specified=true", "minimumStock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock is greater than or equal to
        defaultProductFacilityFiltering(
            "minimumStock.greaterThanOrEqual=" + DEFAULT_MINIMUM_STOCK,
            "minimumStock.greaterThanOrEqual=" + UPDATED_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock is less than or equal to
        defaultProductFacilityFiltering(
            "minimumStock.lessThanOrEqual=" + DEFAULT_MINIMUM_STOCK,
            "minimumStock.lessThanOrEqual=" + SMALLER_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock is less than
        defaultProductFacilityFiltering("minimumStock.lessThan=" + UPDATED_MINIMUM_STOCK, "minimumStock.lessThan=" + DEFAULT_MINIMUM_STOCK);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByMinimumStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where minimumStock is greater than
        defaultProductFacilityFiltering(
            "minimumStock.greaterThan=" + SMALLER_MINIMUM_STOCK,
            "minimumStock.greaterThan=" + DEFAULT_MINIMUM_STOCK
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty equals to
        defaultProductFacilityFiltering("reorderQty.equals=" + DEFAULT_REORDER_QTY, "reorderQty.equals=" + UPDATED_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty in
        defaultProductFacilityFiltering(
            "reorderQty.in=" + DEFAULT_REORDER_QTY + "," + UPDATED_REORDER_QTY,
            "reorderQty.in=" + UPDATED_REORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty is not null
        defaultProductFacilityFiltering("reorderQty.specified=true", "reorderQty.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty is greater than or equal to
        defaultProductFacilityFiltering(
            "reorderQty.greaterThanOrEqual=" + DEFAULT_REORDER_QTY,
            "reorderQty.greaterThanOrEqual=" + UPDATED_REORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty is less than or equal to
        defaultProductFacilityFiltering(
            "reorderQty.lessThanOrEqual=" + DEFAULT_REORDER_QTY,
            "reorderQty.lessThanOrEqual=" + SMALLER_REORDER_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty is less than
        defaultProductFacilityFiltering("reorderQty.lessThan=" + UPDATED_REORDER_QTY, "reorderQty.lessThan=" + DEFAULT_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByReorderQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where reorderQty is greater than
        defaultProductFacilityFiltering("reorderQty.greaterThan=" + SMALLER_REORDER_QTY, "reorderQty.greaterThan=" + DEFAULT_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip equals to
        defaultProductFacilityFiltering("daysToShip.equals=" + DEFAULT_DAYS_TO_SHIP, "daysToShip.equals=" + UPDATED_DAYS_TO_SHIP);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip in
        defaultProductFacilityFiltering(
            "daysToShip.in=" + DEFAULT_DAYS_TO_SHIP + "," + UPDATED_DAYS_TO_SHIP,
            "daysToShip.in=" + UPDATED_DAYS_TO_SHIP
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip is not null
        defaultProductFacilityFiltering("daysToShip.specified=true", "daysToShip.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip is greater than or equal to
        defaultProductFacilityFiltering(
            "daysToShip.greaterThanOrEqual=" + DEFAULT_DAYS_TO_SHIP,
            "daysToShip.greaterThanOrEqual=" + UPDATED_DAYS_TO_SHIP
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip is less than or equal to
        defaultProductFacilityFiltering(
            "daysToShip.lessThanOrEqual=" + DEFAULT_DAYS_TO_SHIP,
            "daysToShip.lessThanOrEqual=" + SMALLER_DAYS_TO_SHIP
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip is less than
        defaultProductFacilityFiltering("daysToShip.lessThan=" + UPDATED_DAYS_TO_SHIP, "daysToShip.lessThan=" + DEFAULT_DAYS_TO_SHIP);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByDaysToShipIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where daysToShip is greater than
        defaultProductFacilityFiltering("daysToShip.greaterThan=" + SMALLER_DAYS_TO_SHIP, "daysToShip.greaterThan=" + DEFAULT_DAYS_TO_SHIP);
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount equals to
        defaultProductFacilityFiltering(
            "lastInventoryCount.equals=" + DEFAULT_LAST_INVENTORY_COUNT,
            "lastInventoryCount.equals=" + UPDATED_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount in
        defaultProductFacilityFiltering(
            "lastInventoryCount.in=" + DEFAULT_LAST_INVENTORY_COUNT + "," + UPDATED_LAST_INVENTORY_COUNT,
            "lastInventoryCount.in=" + UPDATED_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount is not null
        defaultProductFacilityFiltering("lastInventoryCount.specified=true", "lastInventoryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount is greater than or equal to
        defaultProductFacilityFiltering(
            "lastInventoryCount.greaterThanOrEqual=" + DEFAULT_LAST_INVENTORY_COUNT,
            "lastInventoryCount.greaterThanOrEqual=" + UPDATED_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount is less than or equal to
        defaultProductFacilityFiltering(
            "lastInventoryCount.lessThanOrEqual=" + DEFAULT_LAST_INVENTORY_COUNT,
            "lastInventoryCount.lessThanOrEqual=" + SMALLER_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount is less than
        defaultProductFacilityFiltering(
            "lastInventoryCount.lessThan=" + UPDATED_LAST_INVENTORY_COUNT,
            "lastInventoryCount.lessThan=" + DEFAULT_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByLastInventoryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        // Get all the productFacilityList where lastInventoryCount is greater than
        defaultProductFacilityFiltering(
            "lastInventoryCount.greaterThan=" + SMALLER_LAST_INVENTORY_COUNT,
            "lastInventoryCount.greaterThan=" + DEFAULT_LAST_INVENTORY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productFacilityRepository.saveAndFlush(productFacility);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productFacility.setProduct(product);
        productFacilityRepository.saveAndFlush(productFacility);
        Long productId = product.getId();
        // Get all the productFacilityList where product equals to productId
        defaultProductFacilityShouldBeFound("productId.equals=" + productId);

        // Get all the productFacilityList where product equals to (productId + 1)
        defaultProductFacilityShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductFacilitiesByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            productFacilityRepository.saveAndFlush(productFacility);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        productFacility.setFacility(facility);
        productFacilityRepository.saveAndFlush(productFacility);
        Long facilityId = facility.getId();
        // Get all the productFacilityList where facility equals to facilityId
        defaultProductFacilityShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the productFacilityList where facility equals to (facilityId + 1)
        defaultProductFacilityShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    private void defaultProductFacilityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFacilityShouldBeFound(shouldBeFound);
        defaultProductFacilityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFacilityShouldBeFound(String filter) throws Exception {
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumStock").value(hasItem(sameNumber(DEFAULT_MINIMUM_STOCK))))
            .andExpect(jsonPath("$.[*].reorderQty").value(hasItem(sameNumber(DEFAULT_REORDER_QTY))))
            .andExpect(jsonPath("$.[*].daysToShip").value(hasItem(DEFAULT_DAYS_TO_SHIP)))
            .andExpect(jsonPath("$.[*].lastInventoryCount").value(hasItem(sameNumber(DEFAULT_LAST_INVENTORY_COUNT))));

        // Check, that the count call also returns 1
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFacilityShouldNotBeFound(String filter) throws Exception {
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFacility() throws Exception {
        // Get the productFacility
        restProductFacilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFacility() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacility
        ProductFacility updatedProductFacility = productFacilityRepository.findById(productFacility.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFacility are not directly saved in db
        em.detach(updatedProductFacility);
        updatedProductFacility
            .minimumStock(UPDATED_MINIMUM_STOCK)
            .reorderQty(UPDATED_REORDER_QTY)
            .daysToShip(UPDATED_DAYS_TO_SHIP)
            .lastInventoryCount(UPDATED_LAST_INVENTORY_COUNT);

        restProductFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFacility.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFacility))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFacilityToMatchAllProperties(updatedProductFacility);
    }

    @Test
    @Transactional
    void putNonExistingProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFacility.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacility))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacility))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacility)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFacilityWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacility using partial update
        ProductFacility partialUpdatedProductFacility = new ProductFacility();
        partialUpdatedProductFacility.setId(productFacility.getId());

        partialUpdatedProductFacility.daysToShip(UPDATED_DAYS_TO_SHIP).lastInventoryCount(UPDATED_LAST_INVENTORY_COUNT);

        restProductFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacility))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacility in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFacility, productFacility),
            getPersistedProductFacility(productFacility)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFacilityWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacility using partial update
        ProductFacility partialUpdatedProductFacility = new ProductFacility();
        partialUpdatedProductFacility.setId(productFacility.getId());

        partialUpdatedProductFacility
            .minimumStock(UPDATED_MINIMUM_STOCK)
            .reorderQty(UPDATED_REORDER_QTY)
            .daysToShip(UPDATED_DAYS_TO_SHIP)
            .lastInventoryCount(UPDATED_LAST_INVENTORY_COUNT);

        restProductFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacility))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacility in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityUpdatableFieldsEquals(
            partialUpdatedProductFacility,
            getPersistedProductFacility(partialUpdatedProductFacility)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacility))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacility))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFacility)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFacility() throws Exception {
        // Initialize the database
        insertedProductFacility = productFacilityRepository.saveAndFlush(productFacility);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFacility
        restProductFacilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFacility.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFacilityRepository.count();
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

    protected ProductFacility getPersistedProductFacility(ProductFacility productFacility) {
        return productFacilityRepository.findById(productFacility.getId()).orElseThrow();
    }

    protected void assertPersistedProductFacilityToMatchAllProperties(ProductFacility expectedProductFacility) {
        assertProductFacilityAllPropertiesEquals(expectedProductFacility, getPersistedProductFacility(expectedProductFacility));
    }

    protected void assertPersistedProductFacilityToMatchUpdatableProperties(ProductFacility expectedProductFacility) {
        assertProductFacilityAllUpdatablePropertiesEquals(expectedProductFacility, getPersistedProductFacility(expectedProductFacility));
    }
}
