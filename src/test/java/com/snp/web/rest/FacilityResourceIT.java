package com.snp.web.rest;

import static com.snp.domain.FacilityAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.Facility;
import com.snp.domain.FacilityGroup;
import com.snp.domain.FacilityType;
import com.snp.domain.GeoPoint;
import com.snp.domain.InventoryItemType;
import com.snp.domain.Party;
import com.snp.domain.ProductStore;
import com.snp.domain.Uom;
import com.snp.repository.FacilityRepository;
import com.snp.service.FacilityService;
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
 * Integration tests for the {@link FacilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FACILITY_SIZE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FACILITY_SIZE = new BigDecimal(2);
    private static final BigDecimal SMALLER_FACILITY_SIZE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/facilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityRepository facilityRepository;

    @Mock
    private FacilityRepository facilityRepositoryMock;

    @Mock
    private FacilityService facilityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityMockMvc;

    private Facility facility;

    private Facility insertedFacility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facility createEntity() {
        return new Facility().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).facilitySize(DEFAULT_FACILITY_SIZE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facility createUpdatedEntity() {
        return new Facility().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).facilitySize(UPDATED_FACILITY_SIZE);
    }

    @BeforeEach
    void initTest() {
        facility = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFacility != null) {
            facilityRepository.delete(insertedFacility);
            insertedFacility = null;
        }
    }

    @Test
    @Transactional
    void createFacility() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Facility
        var returnedFacility = om.readValue(
            restFacilityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facility)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Facility.class
        );

        // Validate the Facility in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityUpdatableFieldsEquals(returnedFacility, getPersistedFacility(returnedFacility));

        insertedFacility = returnedFacility;
    }

    @Test
    @Transactional
    void createFacilityWithExistingId() throws Exception {
        // Create the Facility with an existing ID
        facility.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facility)))
            .andExpect(status().isBadRequest());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facility.setName(null);

        // Create the Facility, which fails.

        restFacilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facility)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilities() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facility.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].facilitySize").value(hasItem(sameNumber(DEFAULT_FACILITY_SIZE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacility() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get the facility
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL_ID, facility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facility.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.facilitySize").value(sameNumber(DEFAULT_FACILITY_SIZE)));
    }

    @Test
    @Transactional
    void getFacilitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        Long id = facility.getId();

        defaultFacilityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where name equals to
        defaultFacilityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where name in
        defaultFacilityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where name is not null
        defaultFacilityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where name contains
        defaultFacilityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacilitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where name does not contain
        defaultFacilityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFacilitiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where description equals to
        defaultFacilityFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilitiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where description in
        defaultFacilityFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilitiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where description is not null
        defaultFacilityFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilitiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where description contains
        defaultFacilityFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilitiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where description does not contain
        defaultFacilityFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize equals to
        defaultFacilityFiltering("facilitySize.equals=" + DEFAULT_FACILITY_SIZE, "facilitySize.equals=" + UPDATED_FACILITY_SIZE);
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize in
        defaultFacilityFiltering(
            "facilitySize.in=" + DEFAULT_FACILITY_SIZE + "," + UPDATED_FACILITY_SIZE,
            "facilitySize.in=" + UPDATED_FACILITY_SIZE
        );
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize is not null
        defaultFacilityFiltering("facilitySize.specified=true", "facilitySize.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize is greater than or equal to
        defaultFacilityFiltering(
            "facilitySize.greaterThanOrEqual=" + DEFAULT_FACILITY_SIZE,
            "facilitySize.greaterThanOrEqual=" + UPDATED_FACILITY_SIZE
        );
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize is less than or equal to
        defaultFacilityFiltering(
            "facilitySize.lessThanOrEqual=" + DEFAULT_FACILITY_SIZE,
            "facilitySize.lessThanOrEqual=" + SMALLER_FACILITY_SIZE
        );
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize is less than
        defaultFacilityFiltering("facilitySize.lessThan=" + UPDATED_FACILITY_SIZE, "facilitySize.lessThan=" + DEFAULT_FACILITY_SIZE);
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilitySizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        // Get all the facilityList where facilitySize is greater than
        defaultFacilityFiltering("facilitySize.greaterThan=" + SMALLER_FACILITY_SIZE, "facilitySize.greaterThan=" + DEFAULT_FACILITY_SIZE);
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilityTypeIsEqualToSomething() throws Exception {
        FacilityType facilityType;
        if (TestUtil.findAll(em, FacilityType.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            facilityType = FacilityTypeResourceIT.createEntity();
        } else {
            facilityType = TestUtil.findAll(em, FacilityType.class).getFirst();
        }
        em.persist(facilityType);
        em.flush();
        facility.setFacilityType(facilityType);
        facilityRepository.saveAndFlush(facility);
        Long facilityTypeId = facilityType.getId();
        // Get all the facilityList where facilityType equals to facilityTypeId
        defaultFacilityShouldBeFound("facilityTypeId.equals=" + facilityTypeId);

        // Get all the facilityList where facilityType equals to (facilityTypeId + 1)
        defaultFacilityShouldNotBeFound("facilityTypeId.equals=" + (facilityTypeId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByParentFacilityIsEqualToSomething() throws Exception {
        Facility parentFacility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            parentFacility = FacilityResourceIT.createEntity();
        } else {
            parentFacility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(parentFacility);
        em.flush();
        facility.setParentFacility(parentFacility);
        facilityRepository.saveAndFlush(facility);
        Long parentFacilityId = parentFacility.getId();
        // Get all the facilityList where parentFacility equals to parentFacilityId
        defaultFacilityShouldBeFound("parentFacilityId.equals=" + parentFacilityId);

        // Get all the facilityList where parentFacility equals to (parentFacilityId + 1)
        defaultFacilityShouldNotBeFound("parentFacilityId.equals=" + (parentFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByOwnerPartyIsEqualToSomething() throws Exception {
        Party ownerParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            ownerParty = PartyResourceIT.createEntity();
        } else {
            ownerParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(ownerParty);
        em.flush();
        facility.setOwnerParty(ownerParty);
        facilityRepository.saveAndFlush(facility);
        Long ownerPartyId = ownerParty.getId();
        // Get all the facilityList where ownerParty equals to ownerPartyId
        defaultFacilityShouldBeFound("ownerPartyId.equals=" + ownerPartyId);

        // Get all the facilityList where ownerParty equals to (ownerPartyId + 1)
        defaultFacilityShouldNotBeFound("ownerPartyId.equals=" + (ownerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByFacilityGroupIsEqualToSomething() throws Exception {
        FacilityGroup facilityGroup;
        if (TestUtil.findAll(em, FacilityGroup.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            facilityGroup = FacilityGroupResourceIT.createEntity();
        } else {
            facilityGroup = TestUtil.findAll(em, FacilityGroup.class).getFirst();
        }
        em.persist(facilityGroup);
        em.flush();
        facility.setFacilityGroup(facilityGroup);
        facilityRepository.saveAndFlush(facility);
        Long facilityGroupId = facilityGroup.getId();
        // Get all the facilityList where facilityGroup equals to facilityGroupId
        defaultFacilityShouldBeFound("facilityGroupId.equals=" + facilityGroupId);

        // Get all the facilityList where facilityGroup equals to (facilityGroupId + 1)
        defaultFacilityShouldNotBeFound("facilityGroupId.equals=" + (facilityGroupId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesBySizeUomIsEqualToSomething() throws Exception {
        Uom sizeUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            sizeUom = UomResourceIT.createEntity();
        } else {
            sizeUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(sizeUom);
        em.flush();
        facility.setSizeUom(sizeUom);
        facilityRepository.saveAndFlush(facility);
        Long sizeUomId = sizeUom.getId();
        // Get all the facilityList where sizeUom equals to sizeUomId
        defaultFacilityShouldBeFound("sizeUomId.equals=" + sizeUomId);

        // Get all the facilityList where sizeUom equals to (sizeUomId + 1)
        defaultFacilityShouldNotBeFound("sizeUomId.equals=" + (sizeUomId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByGeoPointIsEqualToSomething() throws Exception {
        GeoPoint geoPoint;
        if (TestUtil.findAll(em, GeoPoint.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            geoPoint = GeoPointResourceIT.createEntity();
        } else {
            geoPoint = TestUtil.findAll(em, GeoPoint.class).getFirst();
        }
        em.persist(geoPoint);
        em.flush();
        facility.setGeoPoint(geoPoint);
        facilityRepository.saveAndFlush(facility);
        Long geoPointId = geoPoint.getId();
        // Get all the facilityList where geoPoint equals to geoPointId
        defaultFacilityShouldBeFound("geoPointId.equals=" + geoPointId);

        // Get all the facilityList where geoPoint equals to (geoPointId + 1)
        defaultFacilityShouldNotBeFound("geoPointId.equals=" + (geoPointId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByInventoryItemTypeIsEqualToSomething() throws Exception {
        InventoryItemType inventoryItemType;
        if (TestUtil.findAll(em, InventoryItemType.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            inventoryItemType = InventoryItemTypeResourceIT.createEntity();
        } else {
            inventoryItemType = TestUtil.findAll(em, InventoryItemType.class).getFirst();
        }
        em.persist(inventoryItemType);
        em.flush();
        facility.setInventoryItemType(inventoryItemType);
        facilityRepository.saveAndFlush(facility);
        Long inventoryItemTypeId = inventoryItemType.getId();
        // Get all the facilityList where inventoryItemType equals to inventoryItemTypeId
        defaultFacilityShouldBeFound("inventoryItemTypeId.equals=" + inventoryItemTypeId);

        // Get all the facilityList where inventoryItemType equals to (inventoryItemTypeId + 1)
        defaultFacilityShouldNotBeFound("inventoryItemTypeId.equals=" + (inventoryItemTypeId + 1));
    }

    @Test
    @Transactional
    void getAllFacilitiesByProductStoreIsEqualToSomething() throws Exception {
        ProductStore productStore;
        if (TestUtil.findAll(em, ProductStore.class).isEmpty()) {
            facilityRepository.saveAndFlush(facility);
            productStore = ProductStoreResourceIT.createEntity();
        } else {
            productStore = TestUtil.findAll(em, ProductStore.class).getFirst();
        }
        em.persist(productStore);
        em.flush();
        facility.setProductStore(productStore);
        facilityRepository.saveAndFlush(facility);
        Long productStoreId = productStore.getId();
        // Get all the facilityList where productStore equals to productStoreId
        defaultFacilityShouldBeFound("productStoreId.equals=" + productStoreId);

        // Get all the facilityList where productStore equals to (productStoreId + 1)
        defaultFacilityShouldNotBeFound("productStoreId.equals=" + (productStoreId + 1));
    }

    private void defaultFacilityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityShouldBeFound(shouldBeFound);
        defaultFacilityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityShouldBeFound(String filter) throws Exception {
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facility.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].facilitySize").value(hasItem(sameNumber(DEFAULT_FACILITY_SIZE))));

        // Check, that the count call also returns 1
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityShouldNotBeFound(String filter) throws Exception {
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacility() throws Exception {
        // Get the facility
        restFacilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacility() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facility
        Facility updatedFacility = facilityRepository.findById(facility.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacility are not directly saved in db
        em.detach(updatedFacility);
        updatedFacility.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).facilitySize(UPDATED_FACILITY_SIZE);

        restFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacility.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacility))
            )
            .andExpect(status().isOk());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityToMatchAllProperties(updatedFacility);
    }

    @Test
    @Transactional
    void putNonExistingFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facility.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facility))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facility))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facility)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityWithPatch() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facility using partial update
        Facility partialUpdatedFacility = new Facility();
        partialUpdatedFacility.setId(facility.getId());

        restFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacility))
            )
            .andExpect(status().isOk());

        // Validate the Facility in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFacility, facility), getPersistedFacility(facility));
    }

    @Test
    @Transactional
    void fullUpdateFacilityWithPatch() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facility using partial update
        Facility partialUpdatedFacility = new Facility();
        partialUpdatedFacility.setId(facility.getId());

        partialUpdatedFacility.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).facilitySize(UPDATED_FACILITY_SIZE);

        restFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacility))
            )
            .andExpect(status().isOk());

        // Validate the Facility in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityUpdatableFieldsEquals(partialUpdatedFacility, getPersistedFacility(partialUpdatedFacility));
    }

    @Test
    @Transactional
    void patchNonExistingFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facility.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facility))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facility))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacility() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facility.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facility)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facility in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacility() throws Exception {
        // Initialize the database
        insertedFacility = facilityRepository.saveAndFlush(facility);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facility
        restFacilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, facility.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityRepository.count();
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

    protected Facility getPersistedFacility(Facility facility) {
        return facilityRepository.findById(facility.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityToMatchAllProperties(Facility expectedFacility) {
        assertFacilityAllPropertiesEquals(expectedFacility, getPersistedFacility(expectedFacility));
    }

    protected void assertPersistedFacilityToMatchUpdatableProperties(Facility expectedFacility) {
        assertFacilityAllUpdatablePropertiesEquals(expectedFacility, getPersistedFacility(expectedFacility));
    }
}
