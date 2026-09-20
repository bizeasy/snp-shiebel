package com.snp.web.rest;

import static com.snp.domain.FacilityLocationAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Facility;
import com.snp.domain.FacilityLocation;
import com.snp.domain.LocationType;
import com.snp.repository.FacilityLocationRepository;
import com.snp.service.FacilityLocationService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link FacilityLocationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityLocationResourceIT {

    private static final String DEFAULT_LOCATION_SEQ_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_SEQ_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_ID = "AAAAAAAAAA";
    private static final String UPDATED_AREA_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AISLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_AISLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION_ID = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facility-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityLocationRepository facilityLocationRepository;

    @Mock
    private FacilityLocationRepository facilityLocationRepositoryMock;

    @Mock
    private FacilityLocationService facilityLocationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityLocationMockMvc;

    private FacilityLocation facilityLocation;

    private FacilityLocation insertedFacilityLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityLocation createEntity(EntityManager em) {
        FacilityLocation facilityLocation = new FacilityLocation()
            .locationSeqId(DEFAULT_LOCATION_SEQ_ID)
            .areaId(DEFAULT_AREA_ID)
            .aisleId(DEFAULT_AISLE_ID)
            .sectionId(DEFAULT_SECTION_ID)
            .levelId(DEFAULT_LEVEL_ID)
            .positionId(DEFAULT_POSITION_ID);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        facilityLocation.setFacility(facility);
        return facilityLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityLocation createUpdatedEntity(EntityManager em) {
        FacilityLocation updatedFacilityLocation = new FacilityLocation()
            .locationSeqId(UPDATED_LOCATION_SEQ_ID)
            .areaId(UPDATED_AREA_ID)
            .aisleId(UPDATED_AISLE_ID)
            .sectionId(UPDATED_SECTION_ID)
            .levelId(UPDATED_LEVEL_ID)
            .positionId(UPDATED_POSITION_ID);
        // Add required entity
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facility = FacilityResourceIT.createUpdatedEntity();
            em.persist(facility);
            em.flush();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        updatedFacilityLocation.setFacility(facility);
        return updatedFacilityLocation;
    }

    @BeforeEach
    void initTest() {
        facilityLocation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityLocation != null) {
            facilityLocationRepository.delete(insertedFacilityLocation);
            insertedFacilityLocation = null;
        }
    }

    @Test
    @Transactional
    void createFacilityLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityLocation
        var returnedFacilityLocation = om.readValue(
            restFacilityLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityLocation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityLocation.class
        );

        // Validate the FacilityLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFacilityLocationUpdatableFieldsEquals(returnedFacilityLocation, getPersistedFacilityLocation(returnedFacilityLocation));

        insertedFacilityLocation = returnedFacilityLocation;
    }

    @Test
    @Transactional
    void createFacilityLocationWithExistingId() throws Exception {
        // Create the FacilityLocation with an existing ID
        facilityLocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityLocation)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocationSeqIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityLocation.setLocationSeqId(null);

        // Create the FacilityLocation, which fails.

        restFacilityLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityLocation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityLocations() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationSeqId").value(hasItem(DEFAULT_LOCATION_SEQ_ID)))
            .andExpect(jsonPath("$.[*].areaId").value(hasItem(DEFAULT_AREA_ID)))
            .andExpect(jsonPath("$.[*].aisleId").value(hasItem(DEFAULT_AISLE_ID)))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID)))
            .andExpect(jsonPath("$.[*].levelId").value(hasItem(DEFAULT_LEVEL_ID)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityLocationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityLocationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityLocationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityLocationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityLocationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityLocationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityLocation() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get the facilityLocation
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityLocation.getId().intValue()))
            .andExpect(jsonPath("$.locationSeqId").value(DEFAULT_LOCATION_SEQ_ID))
            .andExpect(jsonPath("$.areaId").value(DEFAULT_AREA_ID))
            .andExpect(jsonPath("$.aisleId").value(DEFAULT_AISLE_ID))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID))
            .andExpect(jsonPath("$.levelId").value(DEFAULT_LEVEL_ID))
            .andExpect(jsonPath("$.positionId").value(DEFAULT_POSITION_ID));
    }

    @Test
    @Transactional
    void getFacilityLocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        Long id = facilityLocation.getId();

        defaultFacilityLocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityLocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityLocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationSeqIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where locationSeqId equals to
        defaultFacilityLocationFiltering(
            "locationSeqId.equals=" + DEFAULT_LOCATION_SEQ_ID,
            "locationSeqId.equals=" + UPDATED_LOCATION_SEQ_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationSeqIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where locationSeqId in
        defaultFacilityLocationFiltering(
            "locationSeqId.in=" + DEFAULT_LOCATION_SEQ_ID + "," + UPDATED_LOCATION_SEQ_ID,
            "locationSeqId.in=" + UPDATED_LOCATION_SEQ_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationSeqIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where locationSeqId is not null
        defaultFacilityLocationFiltering("locationSeqId.specified=true", "locationSeqId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationSeqIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where locationSeqId contains
        defaultFacilityLocationFiltering(
            "locationSeqId.contains=" + DEFAULT_LOCATION_SEQ_ID,
            "locationSeqId.contains=" + UPDATED_LOCATION_SEQ_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationSeqIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where locationSeqId does not contain
        defaultFacilityLocationFiltering(
            "locationSeqId.doesNotContain=" + UPDATED_LOCATION_SEQ_ID,
            "locationSeqId.doesNotContain=" + DEFAULT_LOCATION_SEQ_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAreaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where areaId equals to
        defaultFacilityLocationFiltering("areaId.equals=" + DEFAULT_AREA_ID, "areaId.equals=" + UPDATED_AREA_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAreaIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where areaId in
        defaultFacilityLocationFiltering("areaId.in=" + DEFAULT_AREA_ID + "," + UPDATED_AREA_ID, "areaId.in=" + UPDATED_AREA_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAreaIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where areaId is not null
        defaultFacilityLocationFiltering("areaId.specified=true", "areaId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAreaIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where areaId contains
        defaultFacilityLocationFiltering("areaId.contains=" + DEFAULT_AREA_ID, "areaId.contains=" + UPDATED_AREA_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAreaIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where areaId does not contain
        defaultFacilityLocationFiltering("areaId.doesNotContain=" + UPDATED_AREA_ID, "areaId.doesNotContain=" + DEFAULT_AREA_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAisleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where aisleId equals to
        defaultFacilityLocationFiltering("aisleId.equals=" + DEFAULT_AISLE_ID, "aisleId.equals=" + UPDATED_AISLE_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAisleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where aisleId in
        defaultFacilityLocationFiltering("aisleId.in=" + DEFAULT_AISLE_ID + "," + UPDATED_AISLE_ID, "aisleId.in=" + UPDATED_AISLE_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAisleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where aisleId is not null
        defaultFacilityLocationFiltering("aisleId.specified=true", "aisleId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAisleIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where aisleId contains
        defaultFacilityLocationFiltering("aisleId.contains=" + DEFAULT_AISLE_ID, "aisleId.contains=" + UPDATED_AISLE_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByAisleIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where aisleId does not contain
        defaultFacilityLocationFiltering("aisleId.doesNotContain=" + UPDATED_AISLE_ID, "aisleId.doesNotContain=" + DEFAULT_AISLE_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsBySectionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where sectionId equals to
        defaultFacilityLocationFiltering("sectionId.equals=" + DEFAULT_SECTION_ID, "sectionId.equals=" + UPDATED_SECTION_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsBySectionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where sectionId in
        defaultFacilityLocationFiltering(
            "sectionId.in=" + DEFAULT_SECTION_ID + "," + UPDATED_SECTION_ID,
            "sectionId.in=" + UPDATED_SECTION_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsBySectionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where sectionId is not null
        defaultFacilityLocationFiltering("sectionId.specified=true", "sectionId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsBySectionIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where sectionId contains
        defaultFacilityLocationFiltering("sectionId.contains=" + DEFAULT_SECTION_ID, "sectionId.contains=" + UPDATED_SECTION_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsBySectionIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where sectionId does not contain
        defaultFacilityLocationFiltering(
            "sectionId.doesNotContain=" + UPDATED_SECTION_ID,
            "sectionId.doesNotContain=" + DEFAULT_SECTION_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLevelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where levelId equals to
        defaultFacilityLocationFiltering("levelId.equals=" + DEFAULT_LEVEL_ID, "levelId.equals=" + UPDATED_LEVEL_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLevelIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where levelId in
        defaultFacilityLocationFiltering("levelId.in=" + DEFAULT_LEVEL_ID + "," + UPDATED_LEVEL_ID, "levelId.in=" + UPDATED_LEVEL_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLevelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where levelId is not null
        defaultFacilityLocationFiltering("levelId.specified=true", "levelId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLevelIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where levelId contains
        defaultFacilityLocationFiltering("levelId.contains=" + DEFAULT_LEVEL_ID, "levelId.contains=" + UPDATED_LEVEL_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLevelIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where levelId does not contain
        defaultFacilityLocationFiltering("levelId.doesNotContain=" + UPDATED_LEVEL_ID, "levelId.doesNotContain=" + DEFAULT_LEVEL_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByPositionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where positionId equals to
        defaultFacilityLocationFiltering("positionId.equals=" + DEFAULT_POSITION_ID, "positionId.equals=" + UPDATED_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByPositionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where positionId in
        defaultFacilityLocationFiltering(
            "positionId.in=" + DEFAULT_POSITION_ID + "," + UPDATED_POSITION_ID,
            "positionId.in=" + UPDATED_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByPositionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where positionId is not null
        defaultFacilityLocationFiltering("positionId.specified=true", "positionId.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByPositionIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where positionId contains
        defaultFacilityLocationFiltering("positionId.contains=" + DEFAULT_POSITION_ID, "positionId.contains=" + UPDATED_POSITION_ID);
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByPositionIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        // Get all the facilityLocationList where positionId does not contain
        defaultFacilityLocationFiltering(
            "positionId.doesNotContain=" + UPDATED_POSITION_ID,
            "positionId.doesNotContain=" + DEFAULT_POSITION_ID
        );
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByFacilityIsEqualToSomething() throws Exception {
        Facility facility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            facilityLocationRepository.saveAndFlush(facilityLocation);
            facility = FacilityResourceIT.createEntity();
        } else {
            facility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(facility);
        em.flush();
        facilityLocation.setFacility(facility);
        facilityLocationRepository.saveAndFlush(facilityLocation);
        Long facilityId = facility.getId();
        // Get all the facilityLocationList where facility equals to facilityId
        defaultFacilityLocationShouldBeFound("facilityId.equals=" + facilityId);

        // Get all the facilityLocationList where facility equals to (facilityId + 1)
        defaultFacilityLocationShouldNotBeFound("facilityId.equals=" + (facilityId + 1));
    }

    @Test
    @Transactional
    void getAllFacilityLocationsByLocationTypeIsEqualToSomething() throws Exception {
        LocationType locationType;
        if (TestUtil.findAll(em, LocationType.class).isEmpty()) {
            facilityLocationRepository.saveAndFlush(facilityLocation);
            locationType = LocationTypeResourceIT.createEntity();
        } else {
            locationType = TestUtil.findAll(em, LocationType.class).getFirst();
        }
        em.persist(locationType);
        em.flush();
        facilityLocation.setLocationType(locationType);
        facilityLocationRepository.saveAndFlush(facilityLocation);
        Long locationTypeId = locationType.getId();
        // Get all the facilityLocationList where locationType equals to locationTypeId
        defaultFacilityLocationShouldBeFound("locationTypeId.equals=" + locationTypeId);

        // Get all the facilityLocationList where locationType equals to (locationTypeId + 1)
        defaultFacilityLocationShouldNotBeFound("locationTypeId.equals=" + (locationTypeId + 1));
    }

    private void defaultFacilityLocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityLocationShouldBeFound(shouldBeFound);
        defaultFacilityLocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityLocationShouldBeFound(String filter) throws Exception {
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationSeqId").value(hasItem(DEFAULT_LOCATION_SEQ_ID)))
            .andExpect(jsonPath("$.[*].areaId").value(hasItem(DEFAULT_AREA_ID)))
            .andExpect(jsonPath("$.[*].aisleId").value(hasItem(DEFAULT_AISLE_ID)))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID)))
            .andExpect(jsonPath("$.[*].levelId").value(hasItem(DEFAULT_LEVEL_ID)))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID)));

        // Check, that the count call also returns 1
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityLocationShouldNotBeFound(String filter) throws Exception {
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityLocation() throws Exception {
        // Get the facilityLocation
        restFacilityLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityLocation() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityLocation
        FacilityLocation updatedFacilityLocation = facilityLocationRepository.findById(facilityLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityLocation are not directly saved in db
        em.detach(updatedFacilityLocation);
        updatedFacilityLocation
            .locationSeqId(UPDATED_LOCATION_SEQ_ID)
            .areaId(UPDATED_AREA_ID)
            .aisleId(UPDATED_AISLE_ID)
            .sectionId(UPDATED_SECTION_ID)
            .levelId(UPDATED_LEVEL_ID)
            .positionId(UPDATED_POSITION_ID);

        restFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityLocationToMatchAllProperties(updatedFacilityLocation);
    }

    @Test
    @Transactional
    void putNonExistingFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityLocationWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityLocation using partial update
        FacilityLocation partialUpdatedFacilityLocation = new FacilityLocation();
        partialUpdatedFacilityLocation.setId(facilityLocation.getId());

        partialUpdatedFacilityLocation.sectionId(UPDATED_SECTION_ID).levelId(UPDATED_LEVEL_ID).positionId(UPDATED_POSITION_ID);

        restFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the FacilityLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityLocation, facilityLocation),
            getPersistedFacilityLocation(facilityLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityLocationWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityLocation using partial update
        FacilityLocation partialUpdatedFacilityLocation = new FacilityLocation();
        partialUpdatedFacilityLocation.setId(facilityLocation.getId());

        partialUpdatedFacilityLocation
            .locationSeqId(UPDATED_LOCATION_SEQ_ID)
            .areaId(UPDATED_AREA_ID)
            .aisleId(UPDATED_AISLE_ID)
            .sectionId(UPDATED_SECTION_ID)
            .levelId(UPDATED_LEVEL_ID)
            .positionId(UPDATED_POSITION_ID);

        restFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityLocation))
            )
            .andExpect(status().isOk());

        // Validate the FacilityLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityLocationUpdatableFieldsEquals(
            partialUpdatedFacilityLocation,
            getPersistedFacilityLocation(partialUpdatedFacilityLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityLocation() throws Exception {
        // Initialize the database
        insertedFacilityLocation = facilityLocationRepository.saveAndFlush(facilityLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityLocation
        restFacilityLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityLocationRepository.count();
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

    protected FacilityLocation getPersistedFacilityLocation(FacilityLocation facilityLocation) {
        return facilityLocationRepository.findById(facilityLocation.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityLocationToMatchAllProperties(FacilityLocation expectedFacilityLocation) {
        assertFacilityLocationAllPropertiesEquals(expectedFacilityLocation, getPersistedFacilityLocation(expectedFacilityLocation));
    }

    protected void assertPersistedFacilityLocationToMatchUpdatableProperties(FacilityLocation expectedFacilityLocation) {
        assertFacilityLocationAllUpdatablePropertiesEquals(
            expectedFacilityLocation,
            getPersistedFacilityLocation(expectedFacilityLocation)
        );
    }
}
