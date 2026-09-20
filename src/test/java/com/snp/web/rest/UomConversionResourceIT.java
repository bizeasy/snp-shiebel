package com.snp.web.rest;

import static com.snp.domain.UomConversionAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Uom;
import com.snp.domain.UomConversion;
import com.snp.repository.UomConversionRepository;
import com.snp.service.UomConversionService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link UomConversionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UomConversionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CONVERSION_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONVERSION_FACTOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_CONVERSION_FACTOR = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/uom-conversions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UomConversionRepository uomConversionRepository;

    @Mock
    private UomConversionRepository uomConversionRepositoryMock;

    @Mock
    private UomConversionService uomConversionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUomConversionMockMvc;

    private UomConversion uomConversion;

    private UomConversion insertedUomConversion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UomConversion createEntity(EntityManager em) {
        UomConversion uomConversion = new UomConversion()
            .name(DEFAULT_NAME)
            .conversionFactor(DEFAULT_CONVERSION_FACTOR)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE);
        // Add required entity
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            uom = UomResourceIT.createEntity();
            em.persist(uom);
            em.flush();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        uomConversion.setFromUom(uom);
        // Add required entity
        uomConversion.setToUom(uom);
        return uomConversion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UomConversion createUpdatedEntity(EntityManager em) {
        UomConversion updatedUomConversion = new UomConversion()
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);
        // Add required entity
        Uom uom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            uom = UomResourceIT.createUpdatedEntity();
            em.persist(uom);
            em.flush();
        } else {
            uom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        updatedUomConversion.setFromUom(uom);
        // Add required entity
        updatedUomConversion.setToUom(uom);
        return updatedUomConversion;
    }

    @BeforeEach
    void initTest() {
        uomConversion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUomConversion != null) {
            uomConversionRepository.delete(insertedUomConversion);
            insertedUomConversion = null;
        }
    }

    @Test
    @Transactional
    void createUomConversion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UomConversion
        var returnedUomConversion = om.readValue(
            restUomConversionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomConversion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UomConversion.class
        );

        // Validate the UomConversion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUomConversionUpdatableFieldsEquals(returnedUomConversion, getPersistedUomConversion(returnedUomConversion));

        insertedUomConversion = returnedUomConversion;
    }

    @Test
    @Transactional
    void createUomConversionWithExistingId() throws Exception {
        // Create the UomConversion with an existing ID
        uomConversion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUomConversionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomConversion)))
            .andExpect(status().isBadRequest());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uomConversion.setName(null);

        // Create the UomConversion, which fails.

        restUomConversionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomConversion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConversionFactorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uomConversion.setConversionFactor(null);

        // Create the UomConversion, which fails.

        restUomConversionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomConversion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUomConversions() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uomConversion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].conversionFactor").value(hasItem(sameNumber(DEFAULT_CONVERSION_FACTOR))))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUomConversionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(uomConversionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUomConversionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(uomConversionServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUomConversionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(uomConversionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUomConversionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(uomConversionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUomConversion() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get the uomConversion
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL_ID, uomConversion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uomConversion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.conversionFactor").value(sameNumber(DEFAULT_CONVERSION_FACTOR)))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    void getUomConversionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        Long id = uomConversion.getId();

        defaultUomConversionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUomConversionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUomConversionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUomConversionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where name equals to
        defaultUomConversionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomConversionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where name in
        defaultUomConversionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomConversionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where name is not null
        defaultUomConversionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUomConversionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where name contains
        defaultUomConversionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomConversionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where name does not contain
        defaultUomConversionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor equals to
        defaultUomConversionFiltering(
            "conversionFactor.equals=" + DEFAULT_CONVERSION_FACTOR,
            "conversionFactor.equals=" + UPDATED_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor in
        defaultUomConversionFiltering(
            "conversionFactor.in=" + DEFAULT_CONVERSION_FACTOR + "," + UPDATED_CONVERSION_FACTOR,
            "conversionFactor.in=" + UPDATED_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor is not null
        defaultUomConversionFiltering("conversionFactor.specified=true", "conversionFactor.specified=false");
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor is greater than or equal to
        defaultUomConversionFiltering(
            "conversionFactor.greaterThanOrEqual=" + DEFAULT_CONVERSION_FACTOR,
            "conversionFactor.greaterThanOrEqual=" + UPDATED_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor is less than or equal to
        defaultUomConversionFiltering(
            "conversionFactor.lessThanOrEqual=" + DEFAULT_CONVERSION_FACTOR,
            "conversionFactor.lessThanOrEqual=" + SMALLER_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor is less than
        defaultUomConversionFiltering(
            "conversionFactor.lessThan=" + UPDATED_CONVERSION_FACTOR,
            "conversionFactor.lessThan=" + DEFAULT_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByConversionFactorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where conversionFactor is greater than
        defaultUomConversionFiltering(
            "conversionFactor.greaterThan=" + SMALLER_CONVERSION_FACTOR,
            "conversionFactor.greaterThan=" + DEFAULT_CONVERSION_FACTOR
        );
    }

    @Test
    @Transactional
    void getAllUomConversionsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where fromDate equals to
        defaultUomConversionFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllUomConversionsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where fromDate in
        defaultUomConversionFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllUomConversionsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where fromDate is not null
        defaultUomConversionFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUomConversionsByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where toDate equals to
        defaultUomConversionFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllUomConversionsByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where toDate in
        defaultUomConversionFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllUomConversionsByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        // Get all the uomConversionList where toDate is not null
        defaultUomConversionFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUomConversionsByFromUomIsEqualToSomething() throws Exception {
        Uom fromUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            uomConversionRepository.saveAndFlush(uomConversion);
            fromUom = UomResourceIT.createEntity();
        } else {
            fromUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(fromUom);
        em.flush();
        uomConversion.setFromUom(fromUom);
        uomConversionRepository.saveAndFlush(uomConversion);
        Long fromUomId = fromUom.getId();
        // Get all the uomConversionList where fromUom equals to fromUomId
        defaultUomConversionShouldBeFound("fromUomId.equals=" + fromUomId);

        // Get all the uomConversionList where fromUom equals to (fromUomId + 1)
        defaultUomConversionShouldNotBeFound("fromUomId.equals=" + (fromUomId + 1));
    }

    @Test
    @Transactional
    void getAllUomConversionsByToUomIsEqualToSomething() throws Exception {
        Uom toUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            uomConversionRepository.saveAndFlush(uomConversion);
            toUom = UomResourceIT.createEntity();
        } else {
            toUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(toUom);
        em.flush();
        uomConversion.setToUom(toUom);
        uomConversionRepository.saveAndFlush(uomConversion);
        Long toUomId = toUom.getId();
        // Get all the uomConversionList where toUom equals to toUomId
        defaultUomConversionShouldBeFound("toUomId.equals=" + toUomId);

        // Get all the uomConversionList where toUom equals to (toUomId + 1)
        defaultUomConversionShouldNotBeFound("toUomId.equals=" + (toUomId + 1));
    }

    private void defaultUomConversionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUomConversionShouldBeFound(shouldBeFound);
        defaultUomConversionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUomConversionShouldBeFound(String filter) throws Exception {
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uomConversion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].conversionFactor").value(hasItem(sameNumber(DEFAULT_CONVERSION_FACTOR))))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));

        // Check, that the count call also returns 1
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUomConversionShouldNotBeFound(String filter) throws Exception {
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUomConversionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUomConversion() throws Exception {
        // Get the uomConversion
        restUomConversionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUomConversion() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomConversion
        UomConversion updatedUomConversion = uomConversionRepository.findById(uomConversion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUomConversion are not directly saved in db
        em.detach(updatedUomConversion);
        updatedUomConversion
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);

        restUomConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUomConversion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUomConversion))
            )
            .andExpect(status().isOk());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUomConversionToMatchAllProperties(updatedUomConversion);
    }

    @Test
    @Transactional
    void putNonExistingUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uomConversion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uomConversion))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uomConversion))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uomConversion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUomConversionWithPatch() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomConversion using partial update
        UomConversion partialUpdatedUomConversion = new UomConversion();
        partialUpdatedUomConversion.setId(uomConversion.getId());

        partialUpdatedUomConversion.conversionFactor(UPDATED_CONVERSION_FACTOR);

        restUomConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUomConversion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUomConversion))
            )
            .andExpect(status().isOk());

        // Validate the UomConversion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomConversionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUomConversion, uomConversion),
            getPersistedUomConversion(uomConversion)
        );
    }

    @Test
    @Transactional
    void fullUpdateUomConversionWithPatch() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uomConversion using partial update
        UomConversion partialUpdatedUomConversion = new UomConversion();
        partialUpdatedUomConversion.setId(uomConversion.getId());

        partialUpdatedUomConversion
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE);

        restUomConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUomConversion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUomConversion))
            )
            .andExpect(status().isOk());

        // Validate the UomConversion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomConversionUpdatableFieldsEquals(partialUpdatedUomConversion, getPersistedUomConversion(partialUpdatedUomConversion));
    }

    @Test
    @Transactional
    void patchNonExistingUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uomConversion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uomConversion))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uomConversion))
            )
            .andExpect(status().isBadRequest());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUomConversion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uomConversion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomConversionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uomConversion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UomConversion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUomConversion() throws Exception {
        // Initialize the database
        insertedUomConversion = uomConversionRepository.saveAndFlush(uomConversion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uomConversion
        restUomConversionMockMvc
            .perform(delete(ENTITY_API_URL_ID, uomConversion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uomConversionRepository.count();
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

    protected UomConversion getPersistedUomConversion(UomConversion uomConversion) {
        return uomConversionRepository.findById(uomConversion.getId()).orElseThrow();
    }

    protected void assertPersistedUomConversionToMatchAllProperties(UomConversion expectedUomConversion) {
        assertUomConversionAllPropertiesEquals(expectedUomConversion, getPersistedUomConversion(expectedUomConversion));
    }

    protected void assertPersistedUomConversionToMatchUpdatableProperties(UomConversion expectedUomConversion) {
        assertUomConversionAllUpdatablePropertiesEquals(expectedUomConversion, getPersistedUomConversion(expectedUomConversion));
    }
}
