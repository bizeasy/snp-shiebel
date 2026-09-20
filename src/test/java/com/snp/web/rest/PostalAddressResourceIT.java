package com.snp.web.rest;

import static com.snp.domain.PostalAddressAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.Geo;
import com.snp.domain.GeoPoint;
import com.snp.domain.PostalAddress;
import com.snp.repository.PostalAddressRepository;
import com.snp.service.PostalAddressService;
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
 * Integration tests for the {@link PostalAddressResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PostalAddressResourceIT {

    private static final String DEFAULT_TO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TO_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String DEFAULT_CUSTOM_ADDRESS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ADDRESS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_STR = "AAAAAAAAAA";
    private static final String UPDATED_STATE_STR = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_STR = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_STR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INDIAN_ADDRESS = false;
    private static final Boolean UPDATED_IS_INDIAN_ADDRESS = true;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTIONS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/postal-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Mock
    private PostalAddressRepository postalAddressRepositoryMock;

    @Mock
    private PostalAddressService postalAddressServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostalAddressMockMvc;

    private PostalAddress postalAddress;

    private PostalAddress insertedPostalAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddress createEntity() {
        return new PostalAddress()
            .toName(DEFAULT_TO_NAME)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .landmark(DEFAULT_LANDMARK)
            .postalCode(DEFAULT_POSTAL_CODE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .customAddressType(DEFAULT_CUSTOM_ADDRESS_TYPE)
            .stateStr(DEFAULT_STATE_STR)
            .countryStr(DEFAULT_COUNTRY_STR)
            .isIndianAddress(DEFAULT_IS_INDIAN_ADDRESS)
            .note(DEFAULT_NOTE)
            .directions(DEFAULT_DIRECTIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddress createUpdatedEntity() {
        return new PostalAddress()
            .toName(UPDATED_TO_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .landmark(UPDATED_LANDMARK)
            .postalCode(UPDATED_POSTAL_CODE)
            .isDefault(UPDATED_IS_DEFAULT)
            .customAddressType(UPDATED_CUSTOM_ADDRESS_TYPE)
            .stateStr(UPDATED_STATE_STR)
            .countryStr(UPDATED_COUNTRY_STR)
            .isIndianAddress(UPDATED_IS_INDIAN_ADDRESS)
            .note(UPDATED_NOTE)
            .directions(UPDATED_DIRECTIONS);
    }

    @BeforeEach
    void initTest() {
        postalAddress = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPostalAddress != null) {
            postalAddressRepository.delete(insertedPostalAddress);
            insertedPostalAddress = null;
        }
    }

    @Test
    @Transactional
    void createPostalAddress() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostalAddress
        var returnedPostalAddress = om.readValue(
            restPostalAddressMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postalAddress)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostalAddress.class
        );

        // Validate the PostalAddress in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostalAddressUpdatableFieldsEquals(returnedPostalAddress, getPersistedPostalAddress(returnedPostalAddress));

        insertedPostalAddress = returnedPostalAddress;
    }

    @Test
    @Transactional
    void createPostalAddressWithExistingId() throws Exception {
        // Create the PostalAddress with an existing ID
        postalAddress.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postalAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPostalAddresses() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].toName").value(hasItem(DEFAULT_TO_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].customAddressType").value(hasItem(DEFAULT_CUSTOM_ADDRESS_TYPE)))
            .andExpect(jsonPath("$.[*].stateStr").value(hasItem(DEFAULT_STATE_STR)))
            .andExpect(jsonPath("$.[*].countryStr").value(hasItem(DEFAULT_COUNTRY_STR)))
            .andExpect(jsonPath("$.[*].isIndianAddress").value(hasItem(DEFAULT_IS_INDIAN_ADDRESS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].directions").value(hasItem(DEFAULT_DIRECTIONS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostalAddressesWithEagerRelationshipsIsEnabled() throws Exception {
        when(postalAddressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostalAddressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(postalAddressServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostalAddressesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(postalAddressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostalAddressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(postalAddressRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPostalAddress() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get the postalAddress
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, postalAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postalAddress.getId().intValue()))
            .andExpect(jsonPath("$.toName").value(DEFAULT_TO_NAME))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.customAddressType").value(DEFAULT_CUSTOM_ADDRESS_TYPE))
            .andExpect(jsonPath("$.stateStr").value(DEFAULT_STATE_STR))
            .andExpect(jsonPath("$.countryStr").value(DEFAULT_COUNTRY_STR))
            .andExpect(jsonPath("$.isIndianAddress").value(DEFAULT_IS_INDIAN_ADDRESS))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.directions").value(DEFAULT_DIRECTIONS));
    }

    @Test
    @Transactional
    void getPostalAddressesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        Long id = postalAddress.getId();

        defaultPostalAddressFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPostalAddressFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPostalAddressFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByToNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where toName equals to
        defaultPostalAddressFiltering("toName.equals=" + DEFAULT_TO_NAME, "toName.equals=" + UPDATED_TO_NAME);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByToNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where toName in
        defaultPostalAddressFiltering("toName.in=" + DEFAULT_TO_NAME + "," + UPDATED_TO_NAME, "toName.in=" + UPDATED_TO_NAME);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByToNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where toName is not null
        defaultPostalAddressFiltering("toName.specified=true", "toName.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByToNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where toName contains
        defaultPostalAddressFiltering("toName.contains=" + DEFAULT_TO_NAME, "toName.contains=" + UPDATED_TO_NAME);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByToNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where toName does not contain
        defaultPostalAddressFiltering("toName.doesNotContain=" + UPDATED_TO_NAME, "toName.doesNotContain=" + DEFAULT_TO_NAME);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address1 equals to
        defaultPostalAddressFiltering("address1.equals=" + DEFAULT_ADDRESS_1, "address1.equals=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address1 in
        defaultPostalAddressFiltering("address1.in=" + DEFAULT_ADDRESS_1 + "," + UPDATED_ADDRESS_1, "address1.in=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address1 is not null
        defaultPostalAddressFiltering("address1.specified=true", "address1.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress1ContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address1 contains
        defaultPostalAddressFiltering("address1.contains=" + DEFAULT_ADDRESS_1, "address1.contains=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address1 does not contain
        defaultPostalAddressFiltering("address1.doesNotContain=" + UPDATED_ADDRESS_1, "address1.doesNotContain=" + DEFAULT_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address2 equals to
        defaultPostalAddressFiltering("address2.equals=" + DEFAULT_ADDRESS_2, "address2.equals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address2 in
        defaultPostalAddressFiltering("address2.in=" + DEFAULT_ADDRESS_2 + "," + UPDATED_ADDRESS_2, "address2.in=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address2 is not null
        defaultPostalAddressFiltering("address2.specified=true", "address2.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress2ContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address2 contains
        defaultPostalAddressFiltering("address2.contains=" + DEFAULT_ADDRESS_2, "address2.contains=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByAddress2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where address2 does not contain
        defaultPostalAddressFiltering("address2.doesNotContain=" + UPDATED_ADDRESS_2, "address2.doesNotContain=" + DEFAULT_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where city equals to
        defaultPostalAddressFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where city in
        defaultPostalAddressFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where city is not null
        defaultPostalAddressFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where city contains
        defaultPostalAddressFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where city does not contain
        defaultPostalAddressFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByLandmarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where landmark equals to
        defaultPostalAddressFiltering("landmark.equals=" + DEFAULT_LANDMARK, "landmark.equals=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByLandmarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where landmark in
        defaultPostalAddressFiltering("landmark.in=" + DEFAULT_LANDMARK + "," + UPDATED_LANDMARK, "landmark.in=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByLandmarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where landmark is not null
        defaultPostalAddressFiltering("landmark.specified=true", "landmark.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByLandmarkContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where landmark contains
        defaultPostalAddressFiltering("landmark.contains=" + DEFAULT_LANDMARK, "landmark.contains=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByLandmarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where landmark does not contain
        defaultPostalAddressFiltering("landmark.doesNotContain=" + UPDATED_LANDMARK, "landmark.doesNotContain=" + DEFAULT_LANDMARK);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where postalCode equals to
        defaultPostalAddressFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where postalCode in
        defaultPostalAddressFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where postalCode is not null
        defaultPostalAddressFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where postalCode contains
        defaultPostalAddressFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where postalCode does not contain
        defaultPostalAddressFiltering(
            "postalCode.doesNotContain=" + UPDATED_POSTAL_CODE,
            "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isDefault equals to
        defaultPostalAddressFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isDefault in
        defaultPostalAddressFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isDefault is not null
        defaultPostalAddressFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCustomAddressTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where customAddressType equals to
        defaultPostalAddressFiltering(
            "customAddressType.equals=" + DEFAULT_CUSTOM_ADDRESS_TYPE,
            "customAddressType.equals=" + UPDATED_CUSTOM_ADDRESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCustomAddressTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where customAddressType in
        defaultPostalAddressFiltering(
            "customAddressType.in=" + DEFAULT_CUSTOM_ADDRESS_TYPE + "," + UPDATED_CUSTOM_ADDRESS_TYPE,
            "customAddressType.in=" + UPDATED_CUSTOM_ADDRESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCustomAddressTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where customAddressType is not null
        defaultPostalAddressFiltering("customAddressType.specified=true", "customAddressType.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCustomAddressTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where customAddressType contains
        defaultPostalAddressFiltering(
            "customAddressType.contains=" + DEFAULT_CUSTOM_ADDRESS_TYPE,
            "customAddressType.contains=" + UPDATED_CUSTOM_ADDRESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCustomAddressTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where customAddressType does not contain
        defaultPostalAddressFiltering(
            "customAddressType.doesNotContain=" + UPDATED_CUSTOM_ADDRESS_TYPE,
            "customAddressType.doesNotContain=" + DEFAULT_CUSTOM_ADDRESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateStrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where stateStr equals to
        defaultPostalAddressFiltering("stateStr.equals=" + DEFAULT_STATE_STR, "stateStr.equals=" + UPDATED_STATE_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateStrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where stateStr in
        defaultPostalAddressFiltering("stateStr.in=" + DEFAULT_STATE_STR + "," + UPDATED_STATE_STR, "stateStr.in=" + UPDATED_STATE_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateStrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where stateStr is not null
        defaultPostalAddressFiltering("stateStr.specified=true", "stateStr.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateStrContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where stateStr contains
        defaultPostalAddressFiltering("stateStr.contains=" + DEFAULT_STATE_STR, "stateStr.contains=" + UPDATED_STATE_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateStrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where stateStr does not contain
        defaultPostalAddressFiltering("stateStr.doesNotContain=" + UPDATED_STATE_STR, "stateStr.doesNotContain=" + DEFAULT_STATE_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryStrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where countryStr equals to
        defaultPostalAddressFiltering("countryStr.equals=" + DEFAULT_COUNTRY_STR, "countryStr.equals=" + UPDATED_COUNTRY_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryStrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where countryStr in
        defaultPostalAddressFiltering(
            "countryStr.in=" + DEFAULT_COUNTRY_STR + "," + UPDATED_COUNTRY_STR,
            "countryStr.in=" + UPDATED_COUNTRY_STR
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryStrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where countryStr is not null
        defaultPostalAddressFiltering("countryStr.specified=true", "countryStr.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryStrContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where countryStr contains
        defaultPostalAddressFiltering("countryStr.contains=" + DEFAULT_COUNTRY_STR, "countryStr.contains=" + UPDATED_COUNTRY_STR);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryStrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where countryStr does not contain
        defaultPostalAddressFiltering(
            "countryStr.doesNotContain=" + UPDATED_COUNTRY_STR,
            "countryStr.doesNotContain=" + DEFAULT_COUNTRY_STR
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsIndianAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isIndianAddress equals to
        defaultPostalAddressFiltering(
            "isIndianAddress.equals=" + DEFAULT_IS_INDIAN_ADDRESS,
            "isIndianAddress.equals=" + UPDATED_IS_INDIAN_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsIndianAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isIndianAddress in
        defaultPostalAddressFiltering(
            "isIndianAddress.in=" + DEFAULT_IS_INDIAN_ADDRESS + "," + UPDATED_IS_INDIAN_ADDRESS,
            "isIndianAddress.in=" + UPDATED_IS_INDIAN_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByIsIndianAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where isIndianAddress is not null
        defaultPostalAddressFiltering("isIndianAddress.specified=true", "isIndianAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where note equals to
        defaultPostalAddressFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where note in
        defaultPostalAddressFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where note is not null
        defaultPostalAddressFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where note contains
        defaultPostalAddressFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where note does not contain
        defaultPostalAddressFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByDirectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where directions equals to
        defaultPostalAddressFiltering("directions.equals=" + DEFAULT_DIRECTIONS, "directions.equals=" + UPDATED_DIRECTIONS);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByDirectionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where directions in
        defaultPostalAddressFiltering(
            "directions.in=" + DEFAULT_DIRECTIONS + "," + UPDATED_DIRECTIONS,
            "directions.in=" + UPDATED_DIRECTIONS
        );
    }

    @Test
    @Transactional
    void getAllPostalAddressesByDirectionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where directions is not null
        defaultPostalAddressFiltering("directions.specified=true", "directions.specified=false");
    }

    @Test
    @Transactional
    void getAllPostalAddressesByDirectionsContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where directions contains
        defaultPostalAddressFiltering("directions.contains=" + DEFAULT_DIRECTIONS, "directions.contains=" + UPDATED_DIRECTIONS);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByDirectionsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList where directions does not contain
        defaultPostalAddressFiltering("directions.doesNotContain=" + UPDATED_DIRECTIONS, "directions.doesNotContain=" + DEFAULT_DIRECTIONS);
    }

    @Test
    @Transactional
    void getAllPostalAddressesByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            postalAddressRepository.saveAndFlush(postalAddress);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        postalAddress.setContactMech(contactMech);
        postalAddressRepository.saveAndFlush(postalAddress);
        Long contactMechId = contactMech.getId();
        // Get all the postalAddressList where contactMech equals to contactMechId
        defaultPostalAddressShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the postalAddressList where contactMech equals to (contactMechId + 1)
        defaultPostalAddressShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    @Test
    @Transactional
    void getAllPostalAddressesByStateIsEqualToSomething() throws Exception {
        Geo state;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            postalAddressRepository.saveAndFlush(postalAddress);
            state = GeoResourceIT.createEntity();
        } else {
            state = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(state);
        em.flush();
        postalAddress.setState(state);
        postalAddressRepository.saveAndFlush(postalAddress);
        Long stateId = state.getId();
        // Get all the postalAddressList where state equals to stateId
        defaultPostalAddressShouldBeFound("stateId.equals=" + stateId);

        // Get all the postalAddressList where state equals to (stateId + 1)
        defaultPostalAddressShouldNotBeFound("stateId.equals=" + (stateId + 1));
    }

    @Test
    @Transactional
    void getAllPostalAddressesByPincodeIsEqualToSomething() throws Exception {
        Geo pincode;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            postalAddressRepository.saveAndFlush(postalAddress);
            pincode = GeoResourceIT.createEntity();
        } else {
            pincode = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(pincode);
        em.flush();
        postalAddress.setPincode(pincode);
        postalAddressRepository.saveAndFlush(postalAddress);
        Long pincodeId = pincode.getId();
        // Get all the postalAddressList where pincode equals to pincodeId
        defaultPostalAddressShouldBeFound("pincodeId.equals=" + pincodeId);

        // Get all the postalAddressList where pincode equals to (pincodeId + 1)
        defaultPostalAddressShouldNotBeFound("pincodeId.equals=" + (pincodeId + 1));
    }

    @Test
    @Transactional
    void getAllPostalAddressesByCountryIsEqualToSomething() throws Exception {
        Geo country;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            postalAddressRepository.saveAndFlush(postalAddress);
            country = GeoResourceIT.createEntity();
        } else {
            country = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(country);
        em.flush();
        postalAddress.setCountry(country);
        postalAddressRepository.saveAndFlush(postalAddress);
        Long countryId = country.getId();
        // Get all the postalAddressList where country equals to countryId
        defaultPostalAddressShouldBeFound("countryId.equals=" + countryId);

        // Get all the postalAddressList where country equals to (countryId + 1)
        defaultPostalAddressShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllPostalAddressesByGeoPointIsEqualToSomething() throws Exception {
        GeoPoint geoPoint;
        if (TestUtil.findAll(em, GeoPoint.class).isEmpty()) {
            postalAddressRepository.saveAndFlush(postalAddress);
            geoPoint = GeoPointResourceIT.createEntity();
        } else {
            geoPoint = TestUtil.findAll(em, GeoPoint.class).getFirst();
        }
        em.persist(geoPoint);
        em.flush();
        postalAddress.setGeoPoint(geoPoint);
        postalAddressRepository.saveAndFlush(postalAddress);
        Long geoPointId = geoPoint.getId();
        // Get all the postalAddressList where geoPoint equals to geoPointId
        defaultPostalAddressShouldBeFound("geoPointId.equals=" + geoPointId);

        // Get all the postalAddressList where geoPoint equals to (geoPointId + 1)
        defaultPostalAddressShouldNotBeFound("geoPointId.equals=" + (geoPointId + 1));
    }

    private void defaultPostalAddressFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPostalAddressShouldBeFound(shouldBeFound);
        defaultPostalAddressShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostalAddressShouldBeFound(String filter) throws Exception {
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].toName").value(hasItem(DEFAULT_TO_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].customAddressType").value(hasItem(DEFAULT_CUSTOM_ADDRESS_TYPE)))
            .andExpect(jsonPath("$.[*].stateStr").value(hasItem(DEFAULT_STATE_STR)))
            .andExpect(jsonPath("$.[*].countryStr").value(hasItem(DEFAULT_COUNTRY_STR)))
            .andExpect(jsonPath("$.[*].isIndianAddress").value(hasItem(DEFAULT_IS_INDIAN_ADDRESS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].directions").value(hasItem(DEFAULT_DIRECTIONS)));

        // Check, that the count call also returns 1
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostalAddressShouldNotBeFound(String filter) throws Exception {
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostalAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPostalAddress() throws Exception {
        // Get the postalAddress
        restPostalAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostalAddress() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postalAddress
        PostalAddress updatedPostalAddress = postalAddressRepository.findById(postalAddress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostalAddress are not directly saved in db
        em.detach(updatedPostalAddress);
        updatedPostalAddress
            .toName(UPDATED_TO_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .landmark(UPDATED_LANDMARK)
            .postalCode(UPDATED_POSTAL_CODE)
            .isDefault(UPDATED_IS_DEFAULT)
            .customAddressType(UPDATED_CUSTOM_ADDRESS_TYPE)
            .stateStr(UPDATED_STATE_STR)
            .countryStr(UPDATED_COUNTRY_STR)
            .isIndianAddress(UPDATED_IS_INDIAN_ADDRESS)
            .note(UPDATED_NOTE)
            .directions(UPDATED_DIRECTIONS);

        restPostalAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostalAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPostalAddress))
            )
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostalAddressToMatchAllProperties(updatedPostalAddress);
    }

    @Test
    @Transactional
    void putNonExistingPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postalAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postalAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postalAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postalAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostalAddressWithPatch() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postalAddress using partial update
        PostalAddress partialUpdatedPostalAddress = new PostalAddress();
        partialUpdatedPostalAddress.setId(postalAddress.getId());

        partialUpdatedPostalAddress
            .landmark(UPDATED_LANDMARK)
            .postalCode(UPDATED_POSTAL_CODE)
            .isDefault(UPDATED_IS_DEFAULT)
            .stateStr(UPDATED_STATE_STR)
            .countryStr(UPDATED_COUNTRY_STR)
            .note(UPDATED_NOTE);

        restPostalAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostalAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostalAddress))
            )
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostalAddressUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostalAddress, postalAddress),
            getPersistedPostalAddress(postalAddress)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostalAddressWithPatch() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postalAddress using partial update
        PostalAddress partialUpdatedPostalAddress = new PostalAddress();
        partialUpdatedPostalAddress.setId(postalAddress.getId());

        partialUpdatedPostalAddress
            .toName(UPDATED_TO_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .landmark(UPDATED_LANDMARK)
            .postalCode(UPDATED_POSTAL_CODE)
            .isDefault(UPDATED_IS_DEFAULT)
            .customAddressType(UPDATED_CUSTOM_ADDRESS_TYPE)
            .stateStr(UPDATED_STATE_STR)
            .countryStr(UPDATED_COUNTRY_STR)
            .isIndianAddress(UPDATED_IS_INDIAN_ADDRESS)
            .note(UPDATED_NOTE)
            .directions(UPDATED_DIRECTIONS);

        restPostalAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostalAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostalAddress))
            )
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostalAddressUpdatableFieldsEquals(partialUpdatedPostalAddress, getPersistedPostalAddress(partialUpdatedPostalAddress));
    }

    @Test
    @Transactional
    void patchNonExistingPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postalAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postalAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postalAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostalAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postalAddress.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postalAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostalAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostalAddress() throws Exception {
        // Initialize the database
        insertedPostalAddress = postalAddressRepository.saveAndFlush(postalAddress);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postalAddress
        restPostalAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, postalAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postalAddressRepository.count();
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

    protected PostalAddress getPersistedPostalAddress(PostalAddress postalAddress) {
        return postalAddressRepository.findById(postalAddress.getId()).orElseThrow();
    }

    protected void assertPersistedPostalAddressToMatchAllProperties(PostalAddress expectedPostalAddress) {
        assertPostalAddressAllPropertiesEquals(expectedPostalAddress, getPersistedPostalAddress(expectedPostalAddress));
    }

    protected void assertPersistedPostalAddressToMatchUpdatableProperties(PostalAddress expectedPostalAddress) {
        assertPostalAddressAllUpdatablePropertiesEquals(expectedPostalAddress, getPersistedPostalAddress(expectedPostalAddress));
    }
}
