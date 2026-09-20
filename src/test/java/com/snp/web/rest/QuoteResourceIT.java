package com.snp.web.rest;

import static com.snp.domain.QuoteAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequest;
import com.snp.domain.Party;
import com.snp.domain.ProductStore;
import com.snp.domain.Quote;
import com.snp.domain.QuoteType;
import com.snp.domain.SalesChannel;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.domain.UomConversion;
import com.snp.repository.QuoteRepository;
import com.snp.service.QuoteService;
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
 * Integration tests for the {@link QuoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuoteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFER_NO = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_NO = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_VALID_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_VALID_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_OFFER_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OFFER_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OFFER_VALUE = new BigDecimal(1 - 1);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteRepository quoteRepositoryMock;

    @Mock
    private QuoteService quoteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteMockMvc;

    private Quote quote;

    private Quote insertedQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity(EntityManager em) {
        Quote quote = new Quote()
            .name(DEFAULT_NAME)
            .offerNo(DEFAULT_OFFER_NO)
            .issueDate(DEFAULT_ISSUE_DATE)
            .validFromDate(DEFAULT_VALID_FROM_DATE)
            .validThruDate(DEFAULT_VALID_THRU_DATE)
            .description(DEFAULT_DESCRIPTION)
            .offerValue(DEFAULT_OFFER_VALUE)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        quote.setParty(party);
        return quote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createUpdatedEntity(EntityManager em) {
        Quote updatedQuote = new Quote()
            .name(UPDATED_NAME)
            .offerNo(UPDATED_OFFER_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .validFromDate(UPDATED_VALID_FROM_DATE)
            .validThruDate(UPDATED_VALID_THRU_DATE)
            .description(UPDATED_DESCRIPTION)
            .offerValue(UPDATED_OFFER_VALUE)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedQuote.setParty(party);
        return updatedQuote;
    }

    @BeforeEach
    void initTest() {
        quote = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuote != null) {
            quoteRepository.delete(insertedQuote);
            insertedQuote = null;
        }
    }

    @Test
    @Transactional
    void createQuote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quote
        var returnedQuote = om.readValue(
            restQuoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Quote.class
        );

        // Validate the Quote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteUpdatableFieldsEquals(returnedQuote, getPersistedQuote(returnedQuote));

        insertedQuote = returnedQuote;
    }

    @Test
    @Transactional
    void createQuoteWithExistingId() throws Exception {
        // Create the Quote with an existing ID
        quote.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quote.setIssueDate(null);

        // Create the Quote, which fails.

        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotes() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].offerNo").value(hasItem(DEFAULT_OFFER_NO)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validFromDate").value(hasItem(DEFAULT_VALID_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].validThruDate").value(hasItem(DEFAULT_VALID_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].offerValue").value(hasItem(sameNumber(DEFAULT_OFFER_VALUE))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(quoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quoteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL_ID, quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.offerNo").value(DEFAULT_OFFER_NO))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.validFromDate").value(DEFAULT_VALID_FROM_DATE.toString()))
            .andExpect(jsonPath("$.validThruDate").value(DEFAULT_VALID_THRU_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.offerValue").value(sameNumber(DEFAULT_OFFER_VALUE)))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getQuotesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        Long id = quote.getId();

        defaultQuoteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuotesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where name equals to
        defaultQuoteFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuotesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where name in
        defaultQuoteFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuotesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where name is not null
        defaultQuoteFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where name contains
        defaultQuoteFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuotesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where name does not contain
        defaultQuoteFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerNo equals to
        defaultQuoteFiltering("offerNo.equals=" + DEFAULT_OFFER_NO, "offerNo.equals=" + UPDATED_OFFER_NO);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerNo in
        defaultQuoteFiltering("offerNo.in=" + DEFAULT_OFFER_NO + "," + UPDATED_OFFER_NO, "offerNo.in=" + UPDATED_OFFER_NO);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerNo is not null
        defaultQuoteFiltering("offerNo.specified=true", "offerNo.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByOfferNoContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerNo contains
        defaultQuoteFiltering("offerNo.contains=" + DEFAULT_OFFER_NO, "offerNo.contains=" + UPDATED_OFFER_NO);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerNo does not contain
        defaultQuoteFiltering("offerNo.doesNotContain=" + UPDATED_OFFER_NO, "offerNo.doesNotContain=" + DEFAULT_OFFER_NO);
    }

    @Test
    @Transactional
    void getAllQuotesByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where issueDate equals to
        defaultQuoteFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where issueDate in
        defaultQuoteFiltering("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE, "issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where issueDate is not null
        defaultQuoteFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByValidFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validFromDate equals to
        defaultQuoteFiltering("validFromDate.equals=" + DEFAULT_VALID_FROM_DATE, "validFromDate.equals=" + UPDATED_VALID_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByValidFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validFromDate in
        defaultQuoteFiltering(
            "validFromDate.in=" + DEFAULT_VALID_FROM_DATE + "," + UPDATED_VALID_FROM_DATE,
            "validFromDate.in=" + UPDATED_VALID_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuotesByValidFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validFromDate is not null
        defaultQuoteFiltering("validFromDate.specified=true", "validFromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByValidThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validThruDate equals to
        defaultQuoteFiltering("validThruDate.equals=" + DEFAULT_VALID_THRU_DATE, "validThruDate.equals=" + UPDATED_VALID_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByValidThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validThruDate in
        defaultQuoteFiltering(
            "validThruDate.in=" + DEFAULT_VALID_THRU_DATE + "," + UPDATED_VALID_THRU_DATE,
            "validThruDate.in=" + UPDATED_VALID_THRU_DATE
        );
    }

    @Test
    @Transactional
    void getAllQuotesByValidThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where validThruDate is not null
        defaultQuoteFiltering("validThruDate.specified=true", "validThruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where description equals to
        defaultQuoteFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuotesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where description in
        defaultQuoteFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuotesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where description is not null
        defaultQuoteFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where description contains
        defaultQuoteFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuotesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where description does not contain
        defaultQuoteFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue equals to
        defaultQuoteFiltering("offerValue.equals=" + DEFAULT_OFFER_VALUE, "offerValue.equals=" + UPDATED_OFFER_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue in
        defaultQuoteFiltering("offerValue.in=" + DEFAULT_OFFER_VALUE + "," + UPDATED_OFFER_VALUE, "offerValue.in=" + UPDATED_OFFER_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue is not null
        defaultQuoteFiltering("offerValue.specified=true", "offerValue.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue is greater than or equal to
        defaultQuoteFiltering(
            "offerValue.greaterThanOrEqual=" + DEFAULT_OFFER_VALUE,
            "offerValue.greaterThanOrEqual=" + UPDATED_OFFER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue is less than or equal to
        defaultQuoteFiltering("offerValue.lessThanOrEqual=" + DEFAULT_OFFER_VALUE, "offerValue.lessThanOrEqual=" + SMALLER_OFFER_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue is less than
        defaultQuoteFiltering("offerValue.lessThan=" + UPDATED_OFFER_VALUE, "offerValue.lessThan=" + DEFAULT_OFFER_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotesByOfferValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where offerValue is greater than
        defaultQuoteFiltering("offerValue.greaterThan=" + SMALLER_OFFER_VALUE, "offerValue.greaterThan=" + DEFAULT_OFFER_VALUE);
    }

    @Test
    @Transactional
    void getAllQuotesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where remarks equals to
        defaultQuoteFiltering("remarks.equals=" + DEFAULT_REMARKS, "remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllQuotesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where remarks in
        defaultQuoteFiltering("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS, "remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllQuotesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where remarks is not null
        defaultQuoteFiltering("remarks.specified=true", "remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where remarks contains
        defaultQuoteFiltering("remarks.contains=" + DEFAULT_REMARKS, "remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllQuotesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where remarks does not contain
        defaultQuoteFiltering("remarks.doesNotContain=" + UPDATED_REMARKS, "remarks.doesNotContain=" + DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void getAllQuotesByQuoteTypeIsEqualToSomething() throws Exception {
        QuoteType quoteType;
        if (TestUtil.findAll(em, QuoteType.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            quoteType = QuoteTypeResourceIT.createEntity();
        } else {
            quoteType = TestUtil.findAll(em, QuoteType.class).getFirst();
        }
        em.persist(quoteType);
        em.flush();
        quote.setQuoteType(quoteType);
        quoteRepository.saveAndFlush(quote);
        Long quoteTypeId = quoteType.getId();
        // Get all the quoteList where quoteType equals to quoteTypeId
        defaultQuoteShouldBeFound("quoteTypeId.equals=" + quoteTypeId);

        // Get all the quoteList where quoteType equals to (quoteTypeId + 1)
        defaultQuoteShouldNotBeFound("quoteTypeId.equals=" + (quoteTypeId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByCustRequestIsEqualToSomething() throws Exception {
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            custRequest = CustRequestResourceIT.createEntity(em);
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        em.persist(custRequest);
        em.flush();
        quote.setCustRequest(custRequest);
        quoteRepository.saveAndFlush(quote);
        Long custRequestId = custRequest.getId();
        // Get all the quoteList where custRequest equals to custRequestId
        defaultQuoteShouldBeFound("custRequestId.equals=" + custRequestId);

        // Get all the quoteList where custRequest equals to (custRequestId + 1)
        defaultQuoteShouldNotBeFound("custRequestId.equals=" + (custRequestId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        quote.setParty(party);
        quoteRepository.saveAndFlush(quote);
        Long partyId = party.getId();
        // Get all the quoteList where party equals to partyId
        defaultQuoteShouldBeFound("partyId.equals=" + partyId);

        // Get all the quoteList where party equals to (partyId + 1)
        defaultQuoteShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        quote.setStatus(status);
        quoteRepository.saveAndFlush(quote);
        Long statusId = status.getId();
        // Get all the quoteList where status equals to statusId
        defaultQuoteShouldBeFound("statusId.equals=" + statusId);

        // Get all the quoteList where status equals to (statusId + 1)
        defaultQuoteShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByProductStoreIsEqualToSomething() throws Exception {
        ProductStore productStore;
        if (TestUtil.findAll(em, ProductStore.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            productStore = ProductStoreResourceIT.createEntity();
        } else {
            productStore = TestUtil.findAll(em, ProductStore.class).getFirst();
        }
        em.persist(productStore);
        em.flush();
        quote.setProductStore(productStore);
        quoteRepository.saveAndFlush(quote);
        Long productStoreId = productStore.getId();
        // Get all the quoteList where productStore equals to productStoreId
        defaultQuoteShouldBeFound("productStoreId.equals=" + productStoreId);

        // Get all the quoteList where productStore equals to (productStoreId + 1)
        defaultQuoteShouldNotBeFound("productStoreId.equals=" + (productStoreId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesBySalesChannelIsEqualToSomething() throws Exception {
        SalesChannel salesChannel;
        if (TestUtil.findAll(em, SalesChannel.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            salesChannel = SalesChannelResourceIT.createEntity();
        } else {
            salesChannel = TestUtil.findAll(em, SalesChannel.class).getFirst();
        }
        em.persist(salesChannel);
        em.flush();
        quote.setSalesChannel(salesChannel);
        quoteRepository.saveAndFlush(quote);
        Long salesChannelId = salesChannel.getId();
        // Get all the quoteList where salesChannel equals to salesChannelId
        defaultQuoteShouldBeFound("salesChannelId.equals=" + salesChannelId);

        // Get all the quoteList where salesChannel equals to (salesChannelId + 1)
        defaultQuoteShouldNotBeFound("salesChannelId.equals=" + (salesChannelId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        quote.setCurrencyUom(currencyUom);
        quoteRepository.saveAndFlush(quote);
        Long currencyUomId = currencyUom.getId();
        // Get all the quoteList where currencyUom equals to currencyUomId
        defaultQuoteShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the quoteList where currencyUom equals to (currencyUomId + 1)
        defaultQuoteShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    @Test
    @Transactional
    void getAllQuotesByCurrencyConversionIsEqualToSomething() throws Exception {
        UomConversion currencyConversion;
        if (TestUtil.findAll(em, UomConversion.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            currencyConversion = UomConversionResourceIT.createEntity(em);
        } else {
            currencyConversion = TestUtil.findAll(em, UomConversion.class).getFirst();
        }
        em.persist(currencyConversion);
        em.flush();
        quote.setCurrencyConversion(currencyConversion);
        quoteRepository.saveAndFlush(quote);
        Long currencyConversionId = currencyConversion.getId();
        // Get all the quoteList where currencyConversion equals to currencyConversionId
        defaultQuoteShouldBeFound("currencyConversionId.equals=" + currencyConversionId);

        // Get all the quoteList where currencyConversion equals to (currencyConversionId + 1)
        defaultQuoteShouldNotBeFound("currencyConversionId.equals=" + (currencyConversionId + 1));
    }

    private void defaultQuoteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteShouldBeFound(shouldBeFound);
        defaultQuoteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteShouldBeFound(String filter) throws Exception {
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].offerNo").value(hasItem(DEFAULT_OFFER_NO)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].validFromDate").value(hasItem(DEFAULT_VALID_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].validThruDate").value(hasItem(DEFAULT_VALID_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].offerValue").value(hasItem(sameNumber(DEFAULT_OFFER_VALUE))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));

        // Check, that the count call also returns 1
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteShouldNotBeFound(String filter) throws Exception {
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote
        Quote updatedQuote = quoteRepository.findById(quote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuote are not directly saved in db
        em.detach(updatedQuote);
        updatedQuote
            .name(UPDATED_NAME)
            .offerNo(UPDATED_OFFER_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .validFromDate(UPDATED_VALID_FROM_DATE)
            .validThruDate(UPDATED_VALID_THRU_DATE)
            .description(UPDATED_DESCRIPTION)
            .offerValue(UPDATED_OFFER_VALUE)
            .remarks(UPDATED_REMARKS);

        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteToMatchAllProperties(updatedQuote);
    }

    @Test
    @Transactional
    void putNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(put(ENTITY_API_URL_ID, quote.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote.name(UPDATED_NAME).issueDate(UPDATED_ISSUE_DATE).description(UPDATED_DESCRIPTION);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuote, quote), getPersistedQuote(quote));
    }

    @Test
    @Transactional
    void fullUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote
            .name(UPDATED_NAME)
            .offerNo(UPDATED_OFFER_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .validFromDate(UPDATED_VALID_FROM_DATE)
            .validThruDate(UPDATED_VALID_THRU_DATE)
            .description(UPDATED_DESCRIPTION)
            .offerValue(UPDATED_OFFER_VALUE)
            .remarks(UPDATED_REMARKS);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(partialUpdatedQuote, getPersistedQuote(partialUpdatedQuote));
    }

    @Test
    @Transactional
    void patchNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quote.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quote
        restQuoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, quote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteRepository.count();
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

    protected Quote getPersistedQuote(Quote quote) {
        return quoteRepository.findById(quote.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteToMatchAllProperties(Quote expectedQuote) {
        assertQuoteAllPropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }

    protected void assertPersistedQuoteToMatchUpdatableProperties(Quote expectedQuote) {
        assertQuoteAllUpdatablePropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }
}
