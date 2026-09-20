package com.snp.web.rest;

import static com.snp.domain.PartyAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PartyType;
import com.snp.domain.PostalAddress;
import com.snp.domain.Status;
import com.snp.domain.User;
import com.snp.domain.enumeration.Gender;
import com.snp.repository.PartyRepository;
import com.snp.repository.UserRepository;
import com.snp.service.PartyService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link PartyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyResourceIT {

    private static final Boolean DEFAULT_IS_ORGANISATION = false;
    private static final Boolean UPDATED_IS_ORGANISATION = true;

    private static final String DEFAULT_ORGANISATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SALUTATION = "AAAAAAAAAA";
    private static final String UPDATED_SALUTATION = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.parse("2023-12-14");
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PRIMARY_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_TEMPORARY_PASSWORD = false;
    private static final Boolean UPDATED_IS_TEMPORARY_PASSWORD = true;

    private static final String DEFAULT_LOGO_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Mock
    private PartyService partyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyMockMvc;

    private Party party;

    private Party insertedParty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createEntity() {
        return new Party()
            .isOrganisation(DEFAULT_IS_ORGANISATION)
            .organisationName(DEFAULT_ORGANISATION_NAME)
            .organisationShortName(DEFAULT_ORGANISATION_SHORT_NAME)
            .salutation(DEFAULT_SALUTATION)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .primaryPhone(DEFAULT_PRIMARY_PHONE)
            .primaryEmail(DEFAULT_PRIMARY_EMAIL)
            .isTemporaryPassword(DEFAULT_IS_TEMPORARY_PASSWORD)
            .logoImageUrl(DEFAULT_LOGO_IMAGE_URL)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .notes(DEFAULT_NOTES)
            .postalAddress(DEFAULT_POSTAL_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createUpdatedEntity() {
        return new Party()
            .isOrganisation(UPDATED_IS_ORGANISATION)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .organisationShortName(UPDATED_ORGANISATION_SHORT_NAME)
            .salutation(UPDATED_SALUTATION)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .primaryPhone(UPDATED_PRIMARY_PHONE)
            .primaryEmail(UPDATED_PRIMARY_EMAIL)
            .isTemporaryPassword(UPDATED_IS_TEMPORARY_PASSWORD)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .notes(UPDATED_NOTES)
            .postalAddress(UPDATED_POSTAL_ADDRESS);
    }

    @BeforeEach
    void initTest() {
        party = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedParty != null) {
            partyRepository.delete(insertedParty);
            insertedParty = null;
        }
    }

    @Test
    @Transactional
    void createParty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Party
        var returnedParty = om.readValue(
            restPartyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(party)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Party.class
        );

        // Validate the Party in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyUpdatableFieldsEquals(returnedParty, getPersistedParty(returnedParty));

        insertedParty = returnedParty;
    }

    @Test
    @Transactional
    void createPartyWithExistingId() throws Exception {
        // Create the Party with an existing ID
        party.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(party)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParties() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(party.getId().intValue())))
            .andExpect(jsonPath("$.[*].isOrganisation").value(hasItem(DEFAULT_IS_ORGANISATION)))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME)))
            .andExpect(jsonPath("$.[*].organisationShortName").value(hasItem(DEFAULT_ORGANISATION_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].salutation").value(hasItem(DEFAULT_SALUTATION)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].primaryPhone").value(hasItem(DEFAULT_PRIMARY_PHONE)))
            .andExpect(jsonPath("$.[*].primaryEmail").value(hasItem(DEFAULT_PRIMARY_EMAIL)))
            .andExpect(jsonPath("$.[*].isTemporaryPassword").value(hasItem(DEFAULT_IS_TEMPORARY_PASSWORD)))
            .andExpect(jsonPath("$.[*].logoImageUrl").value(hasItem(DEFAULT_LOGO_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getParty() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get the party
        restPartyMockMvc
            .perform(get(ENTITY_API_URL_ID, party.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(party.getId().intValue()))
            .andExpect(jsonPath("$.isOrganisation").value(DEFAULT_IS_ORGANISATION))
            .andExpect(jsonPath("$.organisationName").value(DEFAULT_ORGANISATION_NAME))
            .andExpect(jsonPath("$.organisationShortName").value(DEFAULT_ORGANISATION_SHORT_NAME))
            .andExpect(jsonPath("$.salutation").value(DEFAULT_SALUTATION))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.primaryPhone").value(DEFAULT_PRIMARY_PHONE))
            .andExpect(jsonPath("$.primaryEmail").value(DEFAULT_PRIMARY_EMAIL))
            .andExpect(jsonPath("$.isTemporaryPassword").value(DEFAULT_IS_TEMPORARY_PASSWORD))
            .andExpect(jsonPath("$.logoImageUrl").value(DEFAULT_LOGO_IMAGE_URL))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.postalAddress").value(DEFAULT_POSTAL_ADDRESS));
    }

    @Test
    @Transactional
    void getPartiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        Long id = party.getId();

        defaultPartyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartiesByIsOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isOrganisation equals to
        defaultPartyFiltering("isOrganisation.equals=" + DEFAULT_IS_ORGANISATION, "isOrganisation.equals=" + UPDATED_IS_ORGANISATION);
    }

    @Test
    @Transactional
    void getAllPartiesByIsOrganisationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isOrganisation in
        defaultPartyFiltering(
            "isOrganisation.in=" + DEFAULT_IS_ORGANISATION + "," + UPDATED_IS_ORGANISATION,
            "isOrganisation.in=" + UPDATED_IS_ORGANISATION
        );
    }

    @Test
    @Transactional
    void getAllPartiesByIsOrganisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isOrganisation is not null
        defaultPartyFiltering("isOrganisation.specified=true", "isOrganisation.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationName equals to
        defaultPartyFiltering(
            "organisationName.equals=" + DEFAULT_ORGANISATION_NAME,
            "organisationName.equals=" + UPDATED_ORGANISATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationName in
        defaultPartyFiltering(
            "organisationName.in=" + DEFAULT_ORGANISATION_NAME + "," + UPDATED_ORGANISATION_NAME,
            "organisationName.in=" + UPDATED_ORGANISATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationName is not null
        defaultPartyFiltering("organisationName.specified=true", "organisationName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationNameContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationName contains
        defaultPartyFiltering(
            "organisationName.contains=" + DEFAULT_ORGANISATION_NAME,
            "organisationName.contains=" + UPDATED_ORGANISATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationName does not contain
        defaultPartyFiltering(
            "organisationName.doesNotContain=" + UPDATED_ORGANISATION_NAME,
            "organisationName.doesNotContain=" + DEFAULT_ORGANISATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationShortName equals to
        defaultPartyFiltering(
            "organisationShortName.equals=" + DEFAULT_ORGANISATION_SHORT_NAME,
            "organisationShortName.equals=" + UPDATED_ORGANISATION_SHORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationShortName in
        defaultPartyFiltering(
            "organisationShortName.in=" + DEFAULT_ORGANISATION_SHORT_NAME + "," + UPDATED_ORGANISATION_SHORT_NAME,
            "organisationShortName.in=" + UPDATED_ORGANISATION_SHORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationShortName is not null
        defaultPartyFiltering("organisationShortName.specified=true", "organisationShortName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationShortNameContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationShortName contains
        defaultPartyFiltering(
            "organisationShortName.contains=" + DEFAULT_ORGANISATION_SHORT_NAME,
            "organisationShortName.contains=" + UPDATED_ORGANISATION_SHORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesByOrganisationShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where organisationShortName does not contain
        defaultPartyFiltering(
            "organisationShortName.doesNotContain=" + UPDATED_ORGANISATION_SHORT_NAME,
            "organisationShortName.doesNotContain=" + DEFAULT_ORGANISATION_SHORT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPartiesBySalutationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where salutation equals to
        defaultPartyFiltering("salutation.equals=" + DEFAULT_SALUTATION, "salutation.equals=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllPartiesBySalutationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where salutation in
        defaultPartyFiltering("salutation.in=" + DEFAULT_SALUTATION + "," + UPDATED_SALUTATION, "salutation.in=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllPartiesBySalutationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where salutation is not null
        defaultPartyFiltering("salutation.specified=true", "salutation.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesBySalutationContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where salutation contains
        defaultPartyFiltering("salutation.contains=" + DEFAULT_SALUTATION, "salutation.contains=" + UPDATED_SALUTATION);
    }

    @Test
    @Transactional
    void getAllPartiesBySalutationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where salutation does not contain
        defaultPartyFiltering("salutation.doesNotContain=" + UPDATED_SALUTATION, "salutation.doesNotContain=" + DEFAULT_SALUTATION);
    }

    @Test
    @Transactional
    void getAllPartiesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where firstName equals to
        defaultPartyFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where firstName in
        defaultPartyFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where firstName is not null
        defaultPartyFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where firstName contains
        defaultPartyFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where firstName does not contain
        defaultPartyFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where middleName equals to
        defaultPartyFiltering("middleName.equals=" + DEFAULT_MIDDLE_NAME, "middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where middleName in
        defaultPartyFiltering("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME, "middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where middleName is not null
        defaultPartyFiltering("middleName.specified=true", "middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where middleName contains
        defaultPartyFiltering("middleName.contains=" + DEFAULT_MIDDLE_NAME, "middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where middleName does not contain
        defaultPartyFiltering("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME, "middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where lastName equals to
        defaultPartyFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where lastName in
        defaultPartyFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where lastName is not null
        defaultPartyFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where lastName contains
        defaultPartyFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where lastName does not contain
        defaultPartyFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPartiesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where gender equals to
        defaultPartyFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPartiesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where gender in
        defaultPartyFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPartiesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where gender is not null
        defaultPartyFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate equals to
        defaultPartyFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate in
        defaultPartyFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate is not null
        defaultPartyFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate is greater than or equal to
        defaultPartyFiltering("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate is less than or equal to
        defaultPartyFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate is less than
        defaultPartyFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where birthDate is greater than
        defaultPartyFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryPhone equals to
        defaultPartyFiltering("primaryPhone.equals=" + DEFAULT_PRIMARY_PHONE, "primaryPhone.equals=" + UPDATED_PRIMARY_PHONE);
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryPhone in
        defaultPartyFiltering(
            "primaryPhone.in=" + DEFAULT_PRIMARY_PHONE + "," + UPDATED_PRIMARY_PHONE,
            "primaryPhone.in=" + UPDATED_PRIMARY_PHONE
        );
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryPhone is not null
        defaultPartyFiltering("primaryPhone.specified=true", "primaryPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryPhone contains
        defaultPartyFiltering("primaryPhone.contains=" + DEFAULT_PRIMARY_PHONE, "primaryPhone.contains=" + UPDATED_PRIMARY_PHONE);
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryPhone does not contain
        defaultPartyFiltering(
            "primaryPhone.doesNotContain=" + UPDATED_PRIMARY_PHONE,
            "primaryPhone.doesNotContain=" + DEFAULT_PRIMARY_PHONE
        );
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryEmail equals to
        defaultPartyFiltering("primaryEmail.equals=" + DEFAULT_PRIMARY_EMAIL, "primaryEmail.equals=" + UPDATED_PRIMARY_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryEmail in
        defaultPartyFiltering(
            "primaryEmail.in=" + DEFAULT_PRIMARY_EMAIL + "," + UPDATED_PRIMARY_EMAIL,
            "primaryEmail.in=" + UPDATED_PRIMARY_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryEmail is not null
        defaultPartyFiltering("primaryEmail.specified=true", "primaryEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryEmail contains
        defaultPartyFiltering("primaryEmail.contains=" + DEFAULT_PRIMARY_EMAIL, "primaryEmail.contains=" + UPDATED_PRIMARY_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartiesByPrimaryEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where primaryEmail does not contain
        defaultPartyFiltering(
            "primaryEmail.doesNotContain=" + UPDATED_PRIMARY_EMAIL,
            "primaryEmail.doesNotContain=" + DEFAULT_PRIMARY_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByIsTemporaryPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isTemporaryPassword equals to
        defaultPartyFiltering(
            "isTemporaryPassword.equals=" + DEFAULT_IS_TEMPORARY_PASSWORD,
            "isTemporaryPassword.equals=" + UPDATED_IS_TEMPORARY_PASSWORD
        );
    }

    @Test
    @Transactional
    void getAllPartiesByIsTemporaryPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isTemporaryPassword in
        defaultPartyFiltering(
            "isTemporaryPassword.in=" + DEFAULT_IS_TEMPORARY_PASSWORD + "," + UPDATED_IS_TEMPORARY_PASSWORD,
            "isTemporaryPassword.in=" + UPDATED_IS_TEMPORARY_PASSWORD
        );
    }

    @Test
    @Transactional
    void getAllPartiesByIsTemporaryPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where isTemporaryPassword is not null
        defaultPartyFiltering("isTemporaryPassword.specified=true", "isTemporaryPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByLogoImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where logoImageUrl equals to
        defaultPartyFiltering("logoImageUrl.equals=" + DEFAULT_LOGO_IMAGE_URL, "logoImageUrl.equals=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPartiesByLogoImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where logoImageUrl in
        defaultPartyFiltering(
            "logoImageUrl.in=" + DEFAULT_LOGO_IMAGE_URL + "," + UPDATED_LOGO_IMAGE_URL,
            "logoImageUrl.in=" + UPDATED_LOGO_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByLogoImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where logoImageUrl is not null
        defaultPartyFiltering("logoImageUrl.specified=true", "logoImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByLogoImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where logoImageUrl contains
        defaultPartyFiltering("logoImageUrl.contains=" + DEFAULT_LOGO_IMAGE_URL, "logoImageUrl.contains=" + UPDATED_LOGO_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPartiesByLogoImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where logoImageUrl does not contain
        defaultPartyFiltering(
            "logoImageUrl.doesNotContain=" + UPDATED_LOGO_IMAGE_URL,
            "logoImageUrl.doesNotContain=" + DEFAULT_LOGO_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where profileImageUrl equals to
        defaultPartyFiltering("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL, "profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPartiesByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where profileImageUrl in
        defaultPartyFiltering(
            "profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL,
            "profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where profileImageUrl is not null
        defaultPartyFiltering("profileImageUrl.specified=true", "profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where profileImageUrl contains
        defaultPartyFiltering(
            "profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL,
            "profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        // Get all the partyList where profileImageUrl does not contain
        defaultPartyFiltering(
            "profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL,
            "profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL
        );
    }

    @Test
    @Transactional
    void getAllPartiesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            partyRepository.saveAndFlush(party);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).getFirst();
        }
        em.persist(user);
        em.flush();
        party.setUser(user);
        partyRepository.saveAndFlush(party);
        Long userId = user.getId();
        // Get all the partyList where user equals to userId
        defaultPartyShouldBeFound("userId.equals=" + userId);

        // Get all the partyList where user equals to (userId + 1)
        defaultPartyShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeIsEqualToSomething() throws Exception {
        PartyType partyType;
        if (TestUtil.findAll(em, PartyType.class).isEmpty()) {
            partyRepository.saveAndFlush(party);
            partyType = PartyTypeResourceIT.createEntity();
        } else {
            partyType = TestUtil.findAll(em, PartyType.class).getFirst();
        }
        em.persist(partyType);
        em.flush();
        party.setPartyType(partyType);
        partyRepository.saveAndFlush(party);
        Long partyTypeId = partyType.getId();
        // Get all the partyList where partyType equals to partyTypeId
        defaultPartyShouldBeFound("partyTypeId.equals=" + partyTypeId);

        // Get all the partyList where partyType equals to (partyTypeId + 1)
        defaultPartyShouldNotBeFound("partyTypeId.equals=" + (partyTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            partyRepository.saveAndFlush(party);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        party.setStatus(status);
        partyRepository.saveAndFlush(party);
        Long statusId = status.getId();
        // Get all the partyList where status equals to statusId
        defaultPartyShouldBeFound("statusId.equals=" + statusId);

        // Get all the partyList where status equals to (statusId + 1)
        defaultPartyShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByAddressIsEqualToSomething() throws Exception {
        PostalAddress address;
        if (TestUtil.findAll(em, PostalAddress.class).isEmpty()) {
            partyRepository.saveAndFlush(party);
            address = PostalAddressResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, PostalAddress.class).getFirst();
        }
        em.persist(address);
        em.flush();
        party.setAddress(address);
        partyRepository.saveAndFlush(party);
        Long addressId = address.getId();
        // Get all the partyList where address equals to addressId
        defaultPartyShouldBeFound("addressId.equals=" + addressId);

        // Get all the partyList where address equals to (addressId + 1)
        defaultPartyShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    private void defaultPartyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyShouldBeFound(shouldBeFound);
        defaultPartyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyShouldBeFound(String filter) throws Exception {
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(party.getId().intValue())))
            .andExpect(jsonPath("$.[*].isOrganisation").value(hasItem(DEFAULT_IS_ORGANISATION)))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME)))
            .andExpect(jsonPath("$.[*].organisationShortName").value(hasItem(DEFAULT_ORGANISATION_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].salutation").value(hasItem(DEFAULT_SALUTATION)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].primaryPhone").value(hasItem(DEFAULT_PRIMARY_PHONE)))
            .andExpect(jsonPath("$.[*].primaryEmail").value(hasItem(DEFAULT_PRIMARY_EMAIL)))
            .andExpect(jsonPath("$.[*].isTemporaryPassword").value(hasItem(DEFAULT_IS_TEMPORARY_PASSWORD)))
            .andExpect(jsonPath("$.[*].logoImageUrl").value(hasItem(DEFAULT_LOGO_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)));

        // Check, that the count call also returns 1
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyShouldNotBeFound(String filter) throws Exception {
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParty() throws Exception {
        // Get the party
        restPartyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParty() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the party
        Party updatedParty = partyRepository.findById(party.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParty are not directly saved in db
        em.detach(updatedParty);
        updatedParty
            .isOrganisation(UPDATED_IS_ORGANISATION)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .organisationShortName(UPDATED_ORGANISATION_SHORT_NAME)
            .salutation(UPDATED_SALUTATION)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .primaryPhone(UPDATED_PRIMARY_PHONE)
            .primaryEmail(UPDATED_PRIMARY_EMAIL)
            .isTemporaryPassword(UPDATED_IS_TEMPORARY_PASSWORD)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .notes(UPDATED_NOTES)
            .postalAddress(UPDATED_POSTAL_ADDRESS);

        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyToMatchAllProperties(updatedParty);
    }

    @Test
    @Transactional
    void putNonExistingParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(put(ENTITY_API_URL_ID, party.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(party)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(party)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        partialUpdatedParty
            .organisationShortName(UPDATED_ORGANISATION_SHORT_NAME)
            .salutation(UPDATED_SALUTATION)
            .firstName(UPDATED_FIRST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .primaryPhone(UPDATED_PRIMARY_PHONE)
            .primaryEmail(UPDATED_PRIMARY_EMAIL)
            .postalAddress(UPDATED_POSTAL_ADDRESS);

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedParty, party), getPersistedParty(party));
    }

    @Test
    @Transactional
    void fullUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        partialUpdatedParty
            .isOrganisation(UPDATED_IS_ORGANISATION)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .organisationShortName(UPDATED_ORGANISATION_SHORT_NAME)
            .salutation(UPDATED_SALUTATION)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .primaryPhone(UPDATED_PRIMARY_PHONE)
            .primaryEmail(UPDATED_PRIMARY_EMAIL)
            .isTemporaryPassword(UPDATED_IS_TEMPORARY_PASSWORD)
            .logoImageUrl(UPDATED_LOGO_IMAGE_URL)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .notes(UPDATED_NOTES)
            .postalAddress(UPDATED_POSTAL_ADDRESS);

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyUpdatableFieldsEquals(partialUpdatedParty, getPersistedParty(partialUpdatedParty));
    }

    @Test
    @Transactional
    void patchNonExistingParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, party.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        party.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(party)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParty() throws Exception {
        // Initialize the database
        insertedParty = partyRepository.saveAndFlush(party);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the party
        restPartyMockMvc
            .perform(delete(ENTITY_API_URL_ID, party.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyRepository.count();
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

    protected Party getPersistedParty(Party party) {
        return partyRepository.findById(party.getId()).orElseThrow();
    }

    protected void assertPersistedPartyToMatchAllProperties(Party expectedParty) {
        assertPartyAllPropertiesEquals(expectedParty, getPersistedParty(expectedParty));
    }

    protected void assertPersistedPartyToMatchUpdatableProperties(Party expectedParty) {
        assertPartyAllUpdatablePropertiesEquals(expectedParty, getPersistedParty(expectedParty));
    }
}
