package com.snp.web.rest;

import static com.snp.domain.ProductFacilityUserAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.ProductFacility;
import com.snp.domain.ProductFacilityUser;
import com.snp.domain.Status;
import com.snp.repository.ProductFacilityUserRepository;
import com.snp.service.ProductFacilityUserService;
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
 * Integration tests for the {@link ProductFacilityUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductFacilityUserResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_COST_PER_DAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST_PER_DAY = new BigDecimal(2);
    private static final BigDecimal SMALLER_COST_PER_DAY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/product-facility-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductFacilityUserRepository productFacilityUserRepository;

    @Mock
    private ProductFacilityUserRepository productFacilityUserRepositoryMock;

    @Mock
    private ProductFacilityUserService productFacilityUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFacilityUserMockMvc;

    private ProductFacilityUser productFacilityUser;

    private ProductFacilityUser insertedProductFacilityUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacilityUser createEntity(EntityManager em) {
        ProductFacilityUser productFacilityUser = new ProductFacilityUser()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .costPerDay(DEFAULT_COST_PER_DAY)
            .totalCost(DEFAULT_TOTAL_COST)
            .discount(DEFAULT_DISCOUNT);
        // Add required entity
        ProductFacility productFacility;
        if (TestUtil.findAll(em, ProductFacility.class).isEmpty()) {
            productFacility = ProductFacilityResourceIT.createEntity(em);
            em.persist(productFacility);
            em.flush();
        } else {
            productFacility = TestUtil.findAll(em, ProductFacility.class).getFirst();
        }
        productFacilityUser.setProductFacility(productFacility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        productFacilityUser.setParty(party);
        return productFacilityUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFacilityUser createUpdatedEntity(EntityManager em) {
        ProductFacilityUser updatedProductFacilityUser = new ProductFacilityUser()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .costPerDay(UPDATED_COST_PER_DAY)
            .totalCost(UPDATED_TOTAL_COST)
            .discount(UPDATED_DISCOUNT);
        // Add required entity
        ProductFacility productFacility;
        if (TestUtil.findAll(em, ProductFacility.class).isEmpty()) {
            productFacility = ProductFacilityResourceIT.createUpdatedEntity(em);
            em.persist(productFacility);
            em.flush();
        } else {
            productFacility = TestUtil.findAll(em, ProductFacility.class).getFirst();
        }
        updatedProductFacilityUser.setProductFacility(productFacility);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedProductFacilityUser.setParty(party);
        return updatedProductFacilityUser;
    }

    @BeforeEach
    void initTest() {
        productFacilityUser = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductFacilityUser != null) {
            productFacilityUserRepository.delete(insertedProductFacilityUser);
            insertedProductFacilityUser = null;
        }
    }

    @Test
    @Transactional
    void createProductFacilityUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductFacilityUser
        var returnedProductFacilityUser = om.readValue(
            restProductFacilityUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductFacilityUser.class
        );

        // Validate the ProductFacilityUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductFacilityUserUpdatableFieldsEquals(
            returnedProductFacilityUser,
            getPersistedProductFacilityUser(returnedProductFacilityUser)
        );

        insertedProductFacilityUser = returnedProductFacilityUser;
    }

    @Test
    @Transactional
    void createProductFacilityUserWithExistingId() throws Exception {
        // Create the ProductFacilityUser with an existing ID
        productFacilityUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFacilityUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityUser)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productFacilityUser.setFromDate(null);

        // Create the ProductFacilityUser, which fails.

        restProductFacilityUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityUser)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsers() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacilityUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].costPerDay").value(hasItem(sameNumber(DEFAULT_COST_PER_DAY))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(sameNumber(DEFAULT_DISCOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilityUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(productFacilityUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productFacilityUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductFacilityUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productFacilityUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductFacilityUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productFacilityUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductFacilityUser() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get the productFacilityUser
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL_ID, productFacilityUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFacilityUser.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.costPerDay").value(sameNumber(DEFAULT_COST_PER_DAY)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.discount").value(sameNumber(DEFAULT_DISCOUNT)));
    }

    @Test
    @Transactional
    void getProductFacilityUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        Long id = productFacilityUser.getId();

        defaultProductFacilityUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFacilityUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFacilityUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where fromDate equals to
        defaultProductFacilityUserFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where fromDate in
        defaultProductFacilityUserFiltering(
            "fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE,
            "fromDate.in=" + UPDATED_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where fromDate is not null
        defaultProductFacilityUserFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where thruDate equals to
        defaultProductFacilityUserFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where thruDate in
        defaultProductFacilityUserFiltering(
            "thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE,
            "thruDate.in=" + UPDATED_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where thruDate is not null
        defaultProductFacilityUserFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay equals to
        defaultProductFacilityUserFiltering("costPerDay.equals=" + DEFAULT_COST_PER_DAY, "costPerDay.equals=" + UPDATED_COST_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay in
        defaultProductFacilityUserFiltering(
            "costPerDay.in=" + DEFAULT_COST_PER_DAY + "," + UPDATED_COST_PER_DAY,
            "costPerDay.in=" + UPDATED_COST_PER_DAY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay is not null
        defaultProductFacilityUserFiltering("costPerDay.specified=true", "costPerDay.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay is greater than or equal to
        defaultProductFacilityUserFiltering(
            "costPerDay.greaterThanOrEqual=" + DEFAULT_COST_PER_DAY,
            "costPerDay.greaterThanOrEqual=" + UPDATED_COST_PER_DAY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay is less than or equal to
        defaultProductFacilityUserFiltering(
            "costPerDay.lessThanOrEqual=" + DEFAULT_COST_PER_DAY,
            "costPerDay.lessThanOrEqual=" + SMALLER_COST_PER_DAY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay is less than
        defaultProductFacilityUserFiltering("costPerDay.lessThan=" + UPDATED_COST_PER_DAY, "costPerDay.lessThan=" + DEFAULT_COST_PER_DAY);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByCostPerDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where costPerDay is greater than
        defaultProductFacilityUserFiltering(
            "costPerDay.greaterThan=" + SMALLER_COST_PER_DAY,
            "costPerDay.greaterThan=" + DEFAULT_COST_PER_DAY
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost equals to
        defaultProductFacilityUserFiltering("totalCost.equals=" + DEFAULT_TOTAL_COST, "totalCost.equals=" + UPDATED_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost in
        defaultProductFacilityUserFiltering(
            "totalCost.in=" + DEFAULT_TOTAL_COST + "," + UPDATED_TOTAL_COST,
            "totalCost.in=" + UPDATED_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost is not null
        defaultProductFacilityUserFiltering("totalCost.specified=true", "totalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost is greater than or equal to
        defaultProductFacilityUserFiltering(
            "totalCost.greaterThanOrEqual=" + DEFAULT_TOTAL_COST,
            "totalCost.greaterThanOrEqual=" + UPDATED_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost is less than or equal to
        defaultProductFacilityUserFiltering(
            "totalCost.lessThanOrEqual=" + DEFAULT_TOTAL_COST,
            "totalCost.lessThanOrEqual=" + SMALLER_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost is less than
        defaultProductFacilityUserFiltering("totalCost.lessThan=" + UPDATED_TOTAL_COST, "totalCost.lessThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByTotalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where totalCost is greater than
        defaultProductFacilityUserFiltering("totalCost.greaterThan=" + SMALLER_TOTAL_COST, "totalCost.greaterThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount equals to
        defaultProductFacilityUserFiltering("discount.equals=" + DEFAULT_DISCOUNT, "discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount in
        defaultProductFacilityUserFiltering("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT, "discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount is not null
        defaultProductFacilityUserFiltering("discount.specified=true", "discount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount is greater than or equal to
        defaultProductFacilityUserFiltering(
            "discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT,
            "discount.greaterThanOrEqual=" + UPDATED_DISCOUNT
        );
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount is less than or equal to
        defaultProductFacilityUserFiltering("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT, "discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount is less than
        defaultProductFacilityUserFiltering("discount.lessThan=" + UPDATED_DISCOUNT, "discount.lessThan=" + DEFAULT_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        // Get all the productFacilityUserList where discount is greater than
        defaultProductFacilityUserFiltering("discount.greaterThan=" + SMALLER_DISCOUNT, "discount.greaterThan=" + DEFAULT_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByProductFacilityIsEqualToSomething() throws Exception {
        ProductFacility productFacility;
        if (TestUtil.findAll(em, ProductFacility.class).isEmpty()) {
            productFacilityUserRepository.saveAndFlush(productFacilityUser);
            productFacility = ProductFacilityResourceIT.createEntity(em);
        } else {
            productFacility = TestUtil.findAll(em, ProductFacility.class).getFirst();
        }
        em.persist(productFacility);
        em.flush();
        productFacilityUser.setProductFacility(productFacility);
        productFacilityUserRepository.saveAndFlush(productFacilityUser);
        Long productFacilityId = productFacility.getId();
        // Get all the productFacilityUserList where productFacility equals to productFacilityId
        defaultProductFacilityUserShouldBeFound("productFacilityId.equals=" + productFacilityId);

        // Get all the productFacilityUserList where productFacility equals to (productFacilityId + 1)
        defaultProductFacilityUserShouldNotBeFound("productFacilityId.equals=" + (productFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            productFacilityUserRepository.saveAndFlush(productFacilityUser);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        productFacilityUser.setParty(party);
        productFacilityUserRepository.saveAndFlush(productFacilityUser);
        Long partyId = party.getId();
        // Get all the productFacilityUserList where party equals to partyId
        defaultProductFacilityUserShouldBeFound("partyId.equals=" + partyId);

        // Get all the productFacilityUserList where party equals to (partyId + 1)
        defaultProductFacilityUserShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllProductFacilityUsersByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            productFacilityUserRepository.saveAndFlush(productFacilityUser);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        productFacilityUser.setStatus(status);
        productFacilityUserRepository.saveAndFlush(productFacilityUser);
        Long statusId = status.getId();
        // Get all the productFacilityUserList where status equals to statusId
        defaultProductFacilityUserShouldBeFound("statusId.equals=" + statusId);

        // Get all the productFacilityUserList where status equals to (statusId + 1)
        defaultProductFacilityUserShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultProductFacilityUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductFacilityUserShouldBeFound(shouldBeFound);
        defaultProductFacilityUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductFacilityUserShouldBeFound(String filter) throws Exception {
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFacilityUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].costPerDay").value(hasItem(sameNumber(DEFAULT_COST_PER_DAY))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(sameNumber(DEFAULT_DISCOUNT))));

        // Check, that the count call also returns 1
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductFacilityUserShouldNotBeFound(String filter) throws Exception {
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductFacilityUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductFacilityUser() throws Exception {
        // Get the productFacilityUser
        restProductFacilityUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductFacilityUser() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityUser
        ProductFacilityUser updatedProductFacilityUser = productFacilityUserRepository.findById(productFacilityUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductFacilityUser are not directly saved in db
        em.detach(updatedProductFacilityUser);
        updatedProductFacilityUser
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .costPerDay(UPDATED_COST_PER_DAY)
            .totalCost(UPDATED_TOTAL_COST)
            .discount(UPDATED_DISCOUNT);

        restProductFacilityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFacilityUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductFacilityUser))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductFacilityUserToMatchAllProperties(updatedProductFacilityUser);
    }

    @Test
    @Transactional
    void putNonExistingProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFacilityUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacilityUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productFacilityUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productFacilityUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFacilityUserWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityUser using partial update
        ProductFacilityUser partialUpdatedProductFacilityUser = new ProductFacilityUser();
        partialUpdatedProductFacilityUser.setId(productFacilityUser.getId());

        partialUpdatedProductFacilityUser.totalCost(UPDATED_TOTAL_COST);

        restProductFacilityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacilityUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacilityUser))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductFacilityUser, productFacilityUser),
            getPersistedProductFacilityUser(productFacilityUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductFacilityUserWithPatch() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productFacilityUser using partial update
        ProductFacilityUser partialUpdatedProductFacilityUser = new ProductFacilityUser();
        partialUpdatedProductFacilityUser.setId(productFacilityUser.getId());

        partialUpdatedProductFacilityUser
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .costPerDay(UPDATED_COST_PER_DAY)
            .totalCost(UPDATED_TOTAL_COST)
            .discount(UPDATED_DISCOUNT);

        restProductFacilityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFacilityUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductFacilityUser))
            )
            .andExpect(status().isOk());

        // Validate the ProductFacilityUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductFacilityUserUpdatableFieldsEquals(
            partialUpdatedProductFacilityUser,
            getPersistedProductFacilityUser(partialUpdatedProductFacilityUser)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFacilityUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacilityUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productFacilityUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFacilityUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productFacilityUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFacilityUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productFacilityUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFacilityUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFacilityUser() throws Exception {
        // Initialize the database
        insertedProductFacilityUser = productFacilityUserRepository.saveAndFlush(productFacilityUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productFacilityUser
        restProductFacilityUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFacilityUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productFacilityUserRepository.count();
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

    protected ProductFacilityUser getPersistedProductFacilityUser(ProductFacilityUser productFacilityUser) {
        return productFacilityUserRepository.findById(productFacilityUser.getId()).orElseThrow();
    }

    protected void assertPersistedProductFacilityUserToMatchAllProperties(ProductFacilityUser expectedProductFacilityUser) {
        assertProductFacilityUserAllPropertiesEquals(
            expectedProductFacilityUser,
            getPersistedProductFacilityUser(expectedProductFacilityUser)
        );
    }

    protected void assertPersistedProductFacilityUserToMatchUpdatableProperties(ProductFacilityUser expectedProductFacilityUser) {
        assertProductFacilityUserAllUpdatablePropertiesEquals(
            expectedProductFacilityUser,
            getPersistedProductFacilityUser(expectedProductFacilityUser)
        );
    }
}
