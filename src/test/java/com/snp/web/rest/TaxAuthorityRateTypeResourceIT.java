package com.snp.web.rest;

import static com.snp.domain.TaxAuthorityRateTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.TaxAuthority;
import com.snp.domain.TaxAuthorityRateType;
import com.snp.domain.TaxSlab;
import com.snp.repository.TaxAuthorityRateTypeRepository;
import com.snp.service.TaxAuthorityRateTypeService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TaxAuthorityRateTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxAuthorityRateTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tax-authority-rate-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepository;

    @Mock
    private TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepositoryMock;

    @Mock
    private TaxAuthorityRateTypeService taxAuthorityRateTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxAuthorityRateTypeMockMvc;

    private TaxAuthorityRateType taxAuthorityRateType;

    private TaxAuthorityRateType insertedTaxAuthorityRateType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAuthorityRateType createEntity(EntityManager em) {
        TaxAuthorityRateType taxAuthorityRateType = new TaxAuthorityRateType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        TaxAuthority taxAuthority;
        if (TestUtil.findAll(em, TaxAuthority.class).isEmpty()) {
            taxAuthority = TaxAuthorityResourceIT.createEntity();
            em.persist(taxAuthority);
            em.flush();
        } else {
            taxAuthority = TestUtil.findAll(em, TaxAuthority.class).getFirst();
        }
        taxAuthorityRateType.setTaxAuthority(taxAuthority);
        // Add required entity
        TaxSlab taxSlab;
        if (TestUtil.findAll(em, TaxSlab.class).isEmpty()) {
            taxSlab = TaxSlabResourceIT.createEntity();
            em.persist(taxSlab);
            em.flush();
        } else {
            taxSlab = TestUtil.findAll(em, TaxSlab.class).getFirst();
        }
        taxAuthorityRateType.setTaxSlab(taxSlab);
        return taxAuthorityRateType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAuthorityRateType createUpdatedEntity(EntityManager em) {
        TaxAuthorityRateType updatedTaxAuthorityRateType = new TaxAuthorityRateType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        TaxAuthority taxAuthority;
        if (TestUtil.findAll(em, TaxAuthority.class).isEmpty()) {
            taxAuthority = TaxAuthorityResourceIT.createUpdatedEntity();
            em.persist(taxAuthority);
            em.flush();
        } else {
            taxAuthority = TestUtil.findAll(em, TaxAuthority.class).getFirst();
        }
        updatedTaxAuthorityRateType.setTaxAuthority(taxAuthority);
        // Add required entity
        TaxSlab taxSlab;
        if (TestUtil.findAll(em, TaxSlab.class).isEmpty()) {
            taxSlab = TaxSlabResourceIT.createUpdatedEntity();
            em.persist(taxSlab);
            em.flush();
        } else {
            taxSlab = TestUtil.findAll(em, TaxSlab.class).getFirst();
        }
        updatedTaxAuthorityRateType.setTaxSlab(taxSlab);
        return updatedTaxAuthorityRateType;
    }

    @BeforeEach
    void initTest() {
        taxAuthorityRateType = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTaxAuthorityRateType != null) {
            taxAuthorityRateTypeRepository.delete(insertedTaxAuthorityRateType);
            insertedTaxAuthorityRateType = null;
        }
    }

    @Test
    @Transactional
    void createTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaxAuthorityRateType
        var returnedTaxAuthorityRateType = om.readValue(
            restTaxAuthorityRateTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthorityRateType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaxAuthorityRateType.class
        );

        // Validate the TaxAuthorityRateType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaxAuthorityRateTypeUpdatableFieldsEquals(
            returnedTaxAuthorityRateType,
            getPersistedTaxAuthorityRateType(returnedTaxAuthorityRateType)
        );

        insertedTaxAuthorityRateType = returnedTaxAuthorityRateType;
    }

    @Test
    @Transactional
    void createTaxAuthorityRateTypeWithExistingId() throws Exception {
        // Create the TaxAuthorityRateType with an existing ID
        taxAuthorityRateType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxAuthorityRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthorityRateType)))
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxAuthorityRateType.setName(null);

        // Create the TaxAuthorityRateType, which fails.

        restTaxAuthorityRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthorityRateType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypes() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAuthorityRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAuthorityRateTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxAuthorityRateTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTaxAuthorityRateTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxAuthorityRateTypeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAuthorityRateTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxAuthorityRateTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTaxAuthorityRateTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(taxAuthorityRateTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTaxAuthorityRateType() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get the taxAuthorityRateType
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, taxAuthorityRateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxAuthorityRateType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTaxAuthorityRateTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        Long id = taxAuthorityRateType.getId();

        defaultTaxAuthorityRateTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaxAuthorityRateTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaxAuthorityRateTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where name equals to
        defaultTaxAuthorityRateTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where name in
        defaultTaxAuthorityRateTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where name is not null
        defaultTaxAuthorityRateTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where name contains
        defaultTaxAuthorityRateTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where name does not contain
        defaultTaxAuthorityRateTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where description equals to
        defaultTaxAuthorityRateTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where description in
        defaultTaxAuthorityRateTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where description is not null
        defaultTaxAuthorityRateTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where description contains
        defaultTaxAuthorityRateTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        // Get all the taxAuthorityRateTypeList where description does not contain
        defaultTaxAuthorityRateTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByTaxAuthorityIsEqualToSomething() throws Exception {
        TaxAuthority taxAuthority;
        if (TestUtil.findAll(em, TaxAuthority.class).isEmpty()) {
            taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);
            taxAuthority = TaxAuthorityResourceIT.createEntity();
        } else {
            taxAuthority = TestUtil.findAll(em, TaxAuthority.class).getFirst();
        }
        em.persist(taxAuthority);
        em.flush();
        taxAuthorityRateType.setTaxAuthority(taxAuthority);
        taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);
        Long taxAuthorityId = taxAuthority.getId();
        // Get all the taxAuthorityRateTypeList where taxAuthority equals to taxAuthorityId
        defaultTaxAuthorityRateTypeShouldBeFound("taxAuthorityId.equals=" + taxAuthorityId);

        // Get all the taxAuthorityRateTypeList where taxAuthority equals to (taxAuthorityId + 1)
        defaultTaxAuthorityRateTypeShouldNotBeFound("taxAuthorityId.equals=" + (taxAuthorityId + 1));
    }

    @Test
    @Transactional
    void getAllTaxAuthorityRateTypesByTaxSlabIsEqualToSomething() throws Exception {
        TaxSlab taxSlab;
        if (TestUtil.findAll(em, TaxSlab.class).isEmpty()) {
            taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);
            taxSlab = TaxSlabResourceIT.createEntity();
        } else {
            taxSlab = TestUtil.findAll(em, TaxSlab.class).getFirst();
        }
        em.persist(taxSlab);
        em.flush();
        taxAuthorityRateType.setTaxSlab(taxSlab);
        taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);
        Long taxSlabId = taxSlab.getId();
        // Get all the taxAuthorityRateTypeList where taxSlab equals to taxSlabId
        defaultTaxAuthorityRateTypeShouldBeFound("taxSlabId.equals=" + taxSlabId);

        // Get all the taxAuthorityRateTypeList where taxSlab equals to (taxSlabId + 1)
        defaultTaxAuthorityRateTypeShouldNotBeFound("taxSlabId.equals=" + (taxSlabId + 1));
    }

    private void defaultTaxAuthorityRateTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaxAuthorityRateTypeShouldBeFound(shouldBeFound);
        defaultTaxAuthorityRateTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxAuthorityRateTypeShouldBeFound(String filter) throws Exception {
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAuthorityRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxAuthorityRateTypeShouldNotBeFound(String filter) throws Exception {
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxAuthorityRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxAuthorityRateType() throws Exception {
        // Get the taxAuthorityRateType
        restTaxAuthorityRateTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxAuthorityRateType() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthorityRateType
        TaxAuthorityRateType updatedTaxAuthorityRateType = taxAuthorityRateTypeRepository
            .findById(taxAuthorityRateType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTaxAuthorityRateType are not directly saved in db
        em.detach(updatedTaxAuthorityRateType);
        updatedTaxAuthorityRateType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTaxAuthorityRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxAuthorityRateType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTaxAuthorityRateType))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxAuthorityRateTypeToMatchAllProperties(updatedTaxAuthorityRateType);
    }

    @Test
    @Transactional
    void putNonExistingTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAuthorityRateType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxAuthorityRateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxAuthorityRateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthorityRateType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxAuthorityRateTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthorityRateType using partial update
        TaxAuthorityRateType partialUpdatedTaxAuthorityRateType = new TaxAuthorityRateType();
        partialUpdatedTaxAuthorityRateType.setId(taxAuthorityRateType.getId());

        partialUpdatedTaxAuthorityRateType.name(UPDATED_NAME);

        restTaxAuthorityRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAuthorityRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxAuthorityRateType))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthorityRateType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxAuthorityRateTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaxAuthorityRateType, taxAuthorityRateType),
            getPersistedTaxAuthorityRateType(taxAuthorityRateType)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaxAuthorityRateTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthorityRateType using partial update
        TaxAuthorityRateType partialUpdatedTaxAuthorityRateType = new TaxAuthorityRateType();
        partialUpdatedTaxAuthorityRateType.setId(taxAuthorityRateType.getId());

        partialUpdatedTaxAuthorityRateType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTaxAuthorityRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAuthorityRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxAuthorityRateType))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthorityRateType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxAuthorityRateTypeUpdatableFieldsEquals(
            partialUpdatedTaxAuthorityRateType,
            getPersistedTaxAuthorityRateType(partialUpdatedTaxAuthorityRateType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxAuthorityRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxAuthorityRateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxAuthorityRateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxAuthorityRateType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthorityRateType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityRateTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxAuthorityRateType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAuthorityRateType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxAuthorityRateType() throws Exception {
        // Initialize the database
        insertedTaxAuthorityRateType = taxAuthorityRateTypeRepository.saveAndFlush(taxAuthorityRateType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taxAuthorityRateType
        restTaxAuthorityRateTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxAuthorityRateType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taxAuthorityRateTypeRepository.count();
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

    protected TaxAuthorityRateType getPersistedTaxAuthorityRateType(TaxAuthorityRateType taxAuthorityRateType) {
        return taxAuthorityRateTypeRepository.findById(taxAuthorityRateType.getId()).orElseThrow();
    }

    protected void assertPersistedTaxAuthorityRateTypeToMatchAllProperties(TaxAuthorityRateType expectedTaxAuthorityRateType) {
        assertTaxAuthorityRateTypeAllPropertiesEquals(
            expectedTaxAuthorityRateType,
            getPersistedTaxAuthorityRateType(expectedTaxAuthorityRateType)
        );
    }

    protected void assertPersistedTaxAuthorityRateTypeToMatchUpdatableProperties(TaxAuthorityRateType expectedTaxAuthorityRateType) {
        assertTaxAuthorityRateTypeAllUpdatablePropertiesEquals(
            expectedTaxAuthorityRateType,
            getPersistedTaxAuthorityRateType(expectedTaxAuthorityRateType)
        );
    }
}
