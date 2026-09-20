package com.snp.web.rest;

import static com.snp.domain.CustRequestAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequest;
import com.snp.domain.CustRequestCategory;
import com.snp.domain.CustRequestType;
import com.snp.domain.Party;
import com.snp.domain.ProductStore;
import com.snp.domain.SalesChannel;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.CustRequestRepository;
import com.snp.service.CustRequestService;
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
 * Integration tests for the {@link CustRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Instant DEFAULT_CUST_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CUST_REQUEST_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_RESPONSE_REQUIRED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONSE_REQUIRED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_OPEN_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPEN_DATE_TIME = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_CLOSED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_DATE_TIME = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_INTERNAL_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BILLED = false;
    private static final Boolean UPDATED_BILLED = true;

    private static final Integer DEFAULT_CUST_ESTIMATED_DURATION_DAYS = 1;
    private static final Integer UPDATED_CUST_ESTIMATED_DURATION_DAYS = 2;
    private static final Integer SMALLER_CUST_ESTIMATED_DURATION_DAYS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cust-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestRepository custRequestRepository;

    @Mock
    private CustRequestRepository custRequestRepositoryMock;

    @Mock
    private CustRequestService custRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestMockMvc;

    private CustRequest custRequest;

    private CustRequest insertedCustRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequest createEntity(EntityManager em) {
        CustRequest custRequest = new CustRequest()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .custRequestDate(DEFAULT_CUST_REQUEST_DATE)
            .responseRequiredDate(DEFAULT_RESPONSE_REQUIRED_DATE)
            .openDateTime(DEFAULT_OPEN_DATE_TIME)
            .closedDateTime(DEFAULT_CLOSED_DATE_TIME)
            .internalComment(DEFAULT_INTERNAL_COMMENT)
            .billed(DEFAULT_BILLED)
            .custEstimatedDurationDays(DEFAULT_CUST_ESTIMATED_DURATION_DAYS);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        custRequest.setFromParty(party);
        return custRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequest createUpdatedEntity(EntityManager em) {
        CustRequest updatedCustRequest = new CustRequest()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .custRequestDate(UPDATED_CUST_REQUEST_DATE)
            .responseRequiredDate(UPDATED_RESPONSE_REQUIRED_DATE)
            .openDateTime(UPDATED_OPEN_DATE_TIME)
            .closedDateTime(UPDATED_CLOSED_DATE_TIME)
            .internalComment(UPDATED_INTERNAL_COMMENT)
            .billed(UPDATED_BILLED)
            .custEstimatedDurationDays(UPDATED_CUST_ESTIMATED_DURATION_DAYS);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedCustRequest.setFromParty(party);
        return updatedCustRequest;
    }

    @BeforeEach
    void initTest() {
        custRequest = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequest != null) {
            custRequestRepository.delete(insertedCustRequest);
            insertedCustRequest = null;
        }
    }

    @Test
    @Transactional
    void createCustRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequest
        var returnedCustRequest = om.readValue(
            restCustRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequest.class
        );

        // Validate the CustRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestUpdatableFieldsEquals(returnedCustRequest, getPersistedCustRequest(returnedCustRequest));

        insertedCustRequest = returnedCustRequest;
    }

    @Test
    @Transactional
    void createCustRequestWithExistingId() throws Exception {
        // Create the CustRequest with an existing ID
        custRequest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequest)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        custRequest.setName(null);

        // Create the CustRequest, which fails.

        restCustRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequest)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustRequestDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        custRequest.setCustRequestDate(null);

        // Create the CustRequest, which fails.

        restCustRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequest)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustRequests() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].custRequestDate").value(hasItem(DEFAULT_CUST_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseRequiredDate").value(hasItem(DEFAULT_RESPONSE_REQUIRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].openDateTime").value(hasItem(DEFAULT_OPEN_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].closedDateTime").value(hasItem(DEFAULT_CLOSED_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].internalComment").value(hasItem(DEFAULT_INTERNAL_COMMENT)))
            .andExpect(jsonPath("$.[*].billed").value(hasItem(DEFAULT_BILLED)))
            .andExpect(jsonPath("$.[*].custEstimatedDurationDays").value(hasItem(DEFAULT_CUST_ESTIMATED_DURATION_DAYS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequest() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get the custRequest
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.custRequestDate").value(DEFAULT_CUST_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.responseRequiredDate").value(DEFAULT_RESPONSE_REQUIRED_DATE.toString()))
            .andExpect(jsonPath("$.openDateTime").value(DEFAULT_OPEN_DATE_TIME.toString()))
            .andExpect(jsonPath("$.closedDateTime").value(DEFAULT_CLOSED_DATE_TIME.toString()))
            .andExpect(jsonPath("$.internalComment").value(DEFAULT_INTERNAL_COMMENT))
            .andExpect(jsonPath("$.billed").value(DEFAULT_BILLED))
            .andExpect(jsonPath("$.custEstimatedDurationDays").value(DEFAULT_CUST_ESTIMATED_DURATION_DAYS));
    }

    @Test
    @Transactional
    void getCustRequestsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        Long id = custRequest.getId();

        defaultCustRequestFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where name equals to
        defaultCustRequestFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where name in
        defaultCustRequestFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where name is not null
        defaultCustRequestFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where name contains
        defaultCustRequestFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where name does not contain
        defaultCustRequestFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where description equals to
        defaultCustRequestFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where description in
        defaultCustRequestFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where description is not null
        defaultCustRequestFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where description contains
        defaultCustRequestFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where description does not contain
        defaultCustRequestFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority equals to
        defaultCustRequestFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority in
        defaultCustRequestFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority is not null
        defaultCustRequestFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority is greater than or equal to
        defaultCustRequestFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority is less than or equal to
        defaultCustRequestFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority is less than
        defaultCustRequestFiltering("priority.lessThan=" + (DEFAULT_PRIORITY + 1), "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where priority is greater than
        defaultCustRequestFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustRequestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custRequestDate equals to
        defaultCustRequestFiltering(
            "custRequestDate.equals=" + DEFAULT_CUST_REQUEST_DATE,
            "custRequestDate.equals=" + UPDATED_CUST_REQUEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustRequestDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custRequestDate in
        defaultCustRequestFiltering(
            "custRequestDate.in=" + DEFAULT_CUST_REQUEST_DATE + "," + UPDATED_CUST_REQUEST_DATE,
            "custRequestDate.in=" + UPDATED_CUST_REQUEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustRequestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custRequestDate is not null
        defaultCustRequestFiltering("custRequestDate.specified=true", "custRequestDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByResponseRequiredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where responseRequiredDate equals to
        defaultCustRequestFiltering(
            "responseRequiredDate.equals=" + DEFAULT_RESPONSE_REQUIRED_DATE,
            "responseRequiredDate.equals=" + UPDATED_RESPONSE_REQUIRED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByResponseRequiredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where responseRequiredDate in
        defaultCustRequestFiltering(
            "responseRequiredDate.in=" + DEFAULT_RESPONSE_REQUIRED_DATE + "," + UPDATED_RESPONSE_REQUIRED_DATE,
            "responseRequiredDate.in=" + UPDATED_RESPONSE_REQUIRED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByResponseRequiredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where responseRequiredDate is not null
        defaultCustRequestFiltering("responseRequiredDate.specified=true", "responseRequiredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByOpenDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where openDateTime equals to
        defaultCustRequestFiltering("openDateTime.equals=" + DEFAULT_OPEN_DATE_TIME, "openDateTime.equals=" + UPDATED_OPEN_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllCustRequestsByOpenDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where openDateTime in
        defaultCustRequestFiltering(
            "openDateTime.in=" + DEFAULT_OPEN_DATE_TIME + "," + UPDATED_OPEN_DATE_TIME,
            "openDateTime.in=" + UPDATED_OPEN_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByOpenDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where openDateTime is not null
        defaultCustRequestFiltering("openDateTime.specified=true", "openDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByClosedDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where closedDateTime equals to
        defaultCustRequestFiltering(
            "closedDateTime.equals=" + DEFAULT_CLOSED_DATE_TIME,
            "closedDateTime.equals=" + UPDATED_CLOSED_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByClosedDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where closedDateTime in
        defaultCustRequestFiltering(
            "closedDateTime.in=" + DEFAULT_CLOSED_DATE_TIME + "," + UPDATED_CLOSED_DATE_TIME,
            "closedDateTime.in=" + UPDATED_CLOSED_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByClosedDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where closedDateTime is not null
        defaultCustRequestFiltering("closedDateTime.specified=true", "closedDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByInternalCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where internalComment equals to
        defaultCustRequestFiltering(
            "internalComment.equals=" + DEFAULT_INTERNAL_COMMENT,
            "internalComment.equals=" + UPDATED_INTERNAL_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByInternalCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where internalComment in
        defaultCustRequestFiltering(
            "internalComment.in=" + DEFAULT_INTERNAL_COMMENT + "," + UPDATED_INTERNAL_COMMENT,
            "internalComment.in=" + UPDATED_INTERNAL_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByInternalCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where internalComment is not null
        defaultCustRequestFiltering("internalComment.specified=true", "internalComment.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByInternalCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where internalComment contains
        defaultCustRequestFiltering(
            "internalComment.contains=" + DEFAULT_INTERNAL_COMMENT,
            "internalComment.contains=" + UPDATED_INTERNAL_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByInternalCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where internalComment does not contain
        defaultCustRequestFiltering(
            "internalComment.doesNotContain=" + UPDATED_INTERNAL_COMMENT,
            "internalComment.doesNotContain=" + DEFAULT_INTERNAL_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByBilledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where billed equals to
        defaultCustRequestFiltering("billed.equals=" + DEFAULT_BILLED, "billed.equals=" + UPDATED_BILLED);
    }

    @Test
    @Transactional
    void getAllCustRequestsByBilledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where billed in
        defaultCustRequestFiltering("billed.in=" + DEFAULT_BILLED + "," + UPDATED_BILLED, "billed.in=" + UPDATED_BILLED);
    }

    @Test
    @Transactional
    void getAllCustRequestsByBilledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where billed is not null
        defaultCustRequestFiltering("billed.specified=true", "billed.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays equals to
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.equals=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.equals=" + UPDATED_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays in
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.in=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS + "," + UPDATED_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.in=" + UPDATED_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays is not null
        defaultCustRequestFiltering("custEstimatedDurationDays.specified=true", "custEstimatedDurationDays.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays is greater than or equal to
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.greaterThanOrEqual=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.greaterThanOrEqual=" + UPDATED_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays is less than or equal to
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.lessThanOrEqual=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.lessThanOrEqual=" + SMALLER_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays is less than
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.lessThan=" + UPDATED_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.lessThan=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustEstimatedDurationDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        // Get all the custRequestList where custEstimatedDurationDays is greater than
        defaultCustRequestFiltering(
            "custEstimatedDurationDays.greaterThan=" + SMALLER_CUST_ESTIMATED_DURATION_DAYS,
            "custEstimatedDurationDays.greaterThan=" + DEFAULT_CUST_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustRequestTypeIsEqualToSomething() throws Exception {
        CustRequestType custRequestType;
        if (TestUtil.findAll(em, CustRequestType.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            custRequestType = CustRequestTypeResourceIT.createEntity();
        } else {
            custRequestType = TestUtil.findAll(em, CustRequestType.class).getFirst();
        }
        em.persist(custRequestType);
        em.flush();
        custRequest.setCustRequestType(custRequestType);
        custRequestRepository.saveAndFlush(custRequest);
        Long custRequestTypeId = custRequestType.getId();
        // Get all the custRequestList where custRequestType equals to custRequestTypeId
        defaultCustRequestShouldBeFound("custRequestTypeId.equals=" + custRequestTypeId);

        // Get all the custRequestList where custRequestType equals to (custRequestTypeId + 1)
        defaultCustRequestShouldNotBeFound("custRequestTypeId.equals=" + (custRequestTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsByCustRequestCategoryIsEqualToSomething() throws Exception {
        CustRequestCategory custRequestCategory;
        if (TestUtil.findAll(em, CustRequestCategory.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            custRequestCategory = CustRequestCategoryResourceIT.createEntity();
        } else {
            custRequestCategory = TestUtil.findAll(em, CustRequestCategory.class).getFirst();
        }
        em.persist(custRequestCategory);
        em.flush();
        custRequest.setCustRequestCategory(custRequestCategory);
        custRequestRepository.saveAndFlush(custRequest);
        Long custRequestCategoryId = custRequestCategory.getId();
        // Get all the custRequestList where custRequestCategory equals to custRequestCategoryId
        defaultCustRequestShouldBeFound("custRequestCategoryId.equals=" + custRequestCategoryId);

        // Get all the custRequestList where custRequestCategory equals to (custRequestCategoryId + 1)
        defaultCustRequestShouldNotBeFound("custRequestCategoryId.equals=" + (custRequestCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        custRequest.setStatus(status);
        custRequestRepository.saveAndFlush(custRequest);
        Long statusId = status.getId();
        // Get all the custRequestList where status equals to statusId
        defaultCustRequestShouldBeFound("statusId.equals=" + statusId);

        // Get all the custRequestList where status equals to (statusId + 1)
        defaultCustRequestShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        custRequest.setFromParty(fromParty);
        custRequestRepository.saveAndFlush(custRequest);
        Long fromPartyId = fromParty.getId();
        // Get all the custRequestList where fromParty equals to fromPartyId
        defaultCustRequestShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the custRequestList where fromParty equals to (fromPartyId + 1)
        defaultCustRequestShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsByProductStoreIsEqualToSomething() throws Exception {
        ProductStore productStore;
        if (TestUtil.findAll(em, ProductStore.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            productStore = ProductStoreResourceIT.createEntity();
        } else {
            productStore = TestUtil.findAll(em, ProductStore.class).getFirst();
        }
        em.persist(productStore);
        em.flush();
        custRequest.setProductStore(productStore);
        custRequestRepository.saveAndFlush(custRequest);
        Long productStoreId = productStore.getId();
        // Get all the custRequestList where productStore equals to productStoreId
        defaultCustRequestShouldBeFound("productStoreId.equals=" + productStoreId);

        // Get all the custRequestList where productStore equals to (productStoreId + 1)
        defaultCustRequestShouldNotBeFound("productStoreId.equals=" + (productStoreId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsBySalesChannelIsEqualToSomething() throws Exception {
        SalesChannel salesChannel;
        if (TestUtil.findAll(em, SalesChannel.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            salesChannel = SalesChannelResourceIT.createEntity();
        } else {
            salesChannel = TestUtil.findAll(em, SalesChannel.class).getFirst();
        }
        em.persist(salesChannel);
        em.flush();
        custRequest.setSalesChannel(salesChannel);
        custRequestRepository.saveAndFlush(custRequest);
        Long salesChannelId = salesChannel.getId();
        // Get all the custRequestList where salesChannel equals to salesChannelId
        defaultCustRequestShouldBeFound("salesChannelId.equals=" + salesChannelId);

        // Get all the custRequestList where salesChannel equals to (salesChannelId + 1)
        defaultCustRequestShouldNotBeFound("salesChannelId.equals=" + (salesChannelId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestsByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            custRequestRepository.saveAndFlush(custRequest);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        custRequest.setCurrencyUom(currencyUom);
        custRequestRepository.saveAndFlush(custRequest);
        Long currencyUomId = currencyUom.getId();
        // Get all the custRequestList where currencyUom equals to currencyUomId
        defaultCustRequestShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the custRequestList where currencyUom equals to (currencyUomId + 1)
        defaultCustRequestShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultCustRequestFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestShouldBeFound(shouldBeFound);
        defaultCustRequestShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestShouldBeFound(String filter) throws Exception {
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].custRequestDate").value(hasItem(DEFAULT_CUST_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseRequiredDate").value(hasItem(DEFAULT_RESPONSE_REQUIRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].openDateTime").value(hasItem(DEFAULT_OPEN_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].closedDateTime").value(hasItem(DEFAULT_CLOSED_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].internalComment").value(hasItem(DEFAULT_INTERNAL_COMMENT)))
            .andExpect(jsonPath("$.[*].billed").value(hasItem(DEFAULT_BILLED)))
            .andExpect(jsonPath("$.[*].custEstimatedDurationDays").value(hasItem(DEFAULT_CUST_ESTIMATED_DURATION_DAYS)));

        // Check, that the count call also returns 1
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestShouldNotBeFound(String filter) throws Exception {
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequest() throws Exception {
        // Get the custRequest
        restCustRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequest() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequest
        CustRequest updatedCustRequest = custRequestRepository.findById(custRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequest are not directly saved in db
        em.detach(updatedCustRequest);
        updatedCustRequest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .custRequestDate(UPDATED_CUST_REQUEST_DATE)
            .responseRequiredDate(UPDATED_RESPONSE_REQUIRED_DATE)
            .openDateTime(UPDATED_OPEN_DATE_TIME)
            .closedDateTime(UPDATED_CLOSED_DATE_TIME)
            .internalComment(UPDATED_INTERNAL_COMMENT)
            .billed(UPDATED_BILLED)
            .custEstimatedDurationDays(UPDATED_CUST_ESTIMATED_DURATION_DAYS);

        restCustRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequest))
            )
            .andExpect(status().isOk());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestToMatchAllProperties(updatedCustRequest);
    }

    @Test
    @Transactional
    void putNonExistingCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequest using partial update
        CustRequest partialUpdatedCustRequest = new CustRequest();
        partialUpdatedCustRequest.setId(custRequest.getId());

        partialUpdatedCustRequest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .openDateTime(UPDATED_OPEN_DATE_TIME)
            .billed(UPDATED_BILLED)
            .custEstimatedDurationDays(UPDATED_CUST_ESTIMATED_DURATION_DAYS);

        restCustRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequest))
            )
            .andExpect(status().isOk());

        // Validate the CustRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequest, custRequest),
            getPersistedCustRequest(custRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequest using partial update
        CustRequest partialUpdatedCustRequest = new CustRequest();
        partialUpdatedCustRequest.setId(custRequest.getId());

        partialUpdatedCustRequest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .custRequestDate(UPDATED_CUST_REQUEST_DATE)
            .responseRequiredDate(UPDATED_RESPONSE_REQUIRED_DATE)
            .openDateTime(UPDATED_OPEN_DATE_TIME)
            .closedDateTime(UPDATED_CLOSED_DATE_TIME)
            .internalComment(UPDATED_INTERNAL_COMMENT)
            .billed(UPDATED_BILLED)
            .custEstimatedDurationDays(UPDATED_CUST_ESTIMATED_DURATION_DAYS);

        restCustRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequest))
            )
            .andExpect(status().isOk());

        // Validate the CustRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestUpdatableFieldsEquals(partialUpdatedCustRequest, getPersistedCustRequest(partialUpdatedCustRequest));
    }

    @Test
    @Transactional
    void patchNonExistingCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequest() throws Exception {
        // Initialize the database
        insertedCustRequest = custRequestRepository.saveAndFlush(custRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequest
        restCustRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestRepository.count();
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

    protected CustRequest getPersistedCustRequest(CustRequest custRequest) {
        return custRequestRepository.findById(custRequest.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestToMatchAllProperties(CustRequest expectedCustRequest) {
        assertCustRequestAllPropertiesEquals(expectedCustRequest, getPersistedCustRequest(expectedCustRequest));
    }

    protected void assertPersistedCustRequestToMatchUpdatableProperties(CustRequest expectedCustRequest) {
        assertCustRequestAllUpdatablePropertiesEquals(expectedCustRequest, getPersistedCustRequest(expectedCustRequest));
    }
}
