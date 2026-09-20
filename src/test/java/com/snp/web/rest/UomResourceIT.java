package com.snp.web.rest;

import static com.snp.domain.UomAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Uom;
import com.snp.domain.UomType;
import com.snp.repository.UomRepository;
import com.snp.service.UomService;
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
 * Integration tests for the {@link UomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/uoms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UomRepository uomRepository;

    @Mock
    private UomRepository uomRepositoryMock;

    @Mock
    private UomService uomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUomMockMvc;

    private Uom uom;

    private Uom insertedUom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uom createEntity() {
        return new Uom()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .abbreviation(DEFAULT_ABBREVIATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uom createUpdatedEntity() {
        return new Uom()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .abbreviation(UPDATED_ABBREVIATION);
    }

    @BeforeEach
    void initTest() {
        uom = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUom != null) {
            uomRepository.delete(insertedUom);
            insertedUom = null;
        }
    }

    @Test
    @Transactional
    void createUom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Uom
        var returnedUom = om.readValue(
            restUomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uom)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Uom.class
        );

        // Validate the Uom in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUomUpdatableFieldsEquals(returnedUom, getPersistedUom(returnedUom));

        insertedUom = returnedUom;
    }

    @Test
    @Transactional
    void createUomWithExistingId() throws Exception {
        // Create the Uom with an existing ID
        uom.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uom)))
            .andExpect(status().isBadRequest());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uom.setName(null);

        // Create the Uom, which fails.

        restUomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uom)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUoms() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList
        restUomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(uomServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(uomServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(uomServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(uomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUom() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get the uom
        restUomMockMvc
            .perform(get(ENTITY_API_URL_ID, uom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION));
    }

    @Test
    @Transactional
    void getUomsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        Long id = uom.getId();

        defaultUomFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUomFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUomFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where name equals to
        defaultUomFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where name in
        defaultUomFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where name is not null
        defaultUomFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUomsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where name contains
        defaultUomFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where name does not contain
        defaultUomFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUomsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where description equals to
        defaultUomFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where description in
        defaultUomFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where description is not null
        defaultUomFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllUomsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where description contains
        defaultUomFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where description does not contain
        defaultUomFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo equals to
        defaultUomFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo in
        defaultUomFiltering("sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO, "sequenceNo.in=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo is not null
        defaultUomFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo is greater than or equal to
        defaultUomFiltering("sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO, "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo is less than or equal to
        defaultUomFiltering("sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO, "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo is less than
        defaultUomFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where sequenceNo is greater than
        defaultUomFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllUomsByAbbreviationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where abbreviation equals to
        defaultUomFiltering("abbreviation.equals=" + DEFAULT_ABBREVIATION, "abbreviation.equals=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllUomsByAbbreviationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where abbreviation in
        defaultUomFiltering(
            "abbreviation.in=" + DEFAULT_ABBREVIATION + "," + UPDATED_ABBREVIATION,
            "abbreviation.in=" + UPDATED_ABBREVIATION
        );
    }

    @Test
    @Transactional
    void getAllUomsByAbbreviationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where abbreviation is not null
        defaultUomFiltering("abbreviation.specified=true", "abbreviation.specified=false");
    }

    @Test
    @Transactional
    void getAllUomsByAbbreviationContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where abbreviation contains
        defaultUomFiltering("abbreviation.contains=" + DEFAULT_ABBREVIATION, "abbreviation.contains=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllUomsByAbbreviationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        // Get all the uomList where abbreviation does not contain
        defaultUomFiltering("abbreviation.doesNotContain=" + UPDATED_ABBREVIATION, "abbreviation.doesNotContain=" + DEFAULT_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllUomsByUomTypeIsEqualToSomething() throws Exception {
        UomType uomType;
        if (TestUtil.findAll(em, UomType.class).isEmpty()) {
            uomRepository.saveAndFlush(uom);
            uomType = UomTypeResourceIT.createEntity();
        } else {
            uomType = TestUtil.findAll(em, UomType.class).getFirst();
        }
        em.persist(uomType);
        em.flush();
        uom.setUomType(uomType);
        uomRepository.saveAndFlush(uom);
        Long uomTypeId = uomType.getId();
        // Get all the uomList where uomType equals to uomTypeId
        defaultUomShouldBeFound("uomTypeId.equals=" + uomTypeId);

        // Get all the uomList where uomType equals to (uomTypeId + 1)
        defaultUomShouldNotBeFound("uomTypeId.equals=" + (uomTypeId + 1));
    }

    private void defaultUomFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUomShouldBeFound(shouldBeFound);
        defaultUomShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUomShouldBeFound(String filter) throws Exception {
        restUomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));

        // Check, that the count call also returns 1
        restUomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUomShouldNotBeFound(String filter) throws Exception {
        restUomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUom() throws Exception {
        // Get the uom
        restUomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUom() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uom
        Uom updatedUom = uomRepository.findById(uom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUom are not directly saved in db
        em.detach(updatedUom);
        updatedUom.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO).abbreviation(UPDATED_ABBREVIATION);

        restUomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUom.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedUom))
            )
            .andExpect(status().isOk());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUomToMatchAllProperties(updatedUom);
    }

    @Test
    @Transactional
    void putNonExistingUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(put(ENTITY_API_URL_ID, uom.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uom)))
            .andExpect(status().isBadRequest());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUomWithPatch() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uom using partial update
        Uom partialUpdatedUom = new Uom();
        partialUpdatedUom.setId(uom.getId());

        partialUpdatedUom.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).sequenceNo(UPDATED_SEQUENCE_NO);

        restUomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUom))
            )
            .andExpect(status().isOk());

        // Validate the Uom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUom, uom), getPersistedUom(uom));
    }

    @Test
    @Transactional
    void fullUpdateUomWithPatch() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uom using partial update
        Uom partialUpdatedUom = new Uom();
        partialUpdatedUom.setId(uom.getId());

        partialUpdatedUom
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .abbreviation(UPDATED_ABBREVIATION);

        restUomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUom))
            )
            .andExpect(status().isOk());

        // Validate the Uom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUomUpdatableFieldsEquals(partialUpdatedUom, getPersistedUom(partialUpdatedUom));
    }

    @Test
    @Transactional
    void patchNonExistingUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(patch(ENTITY_API_URL_ID, uom.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uom)))
            .andExpect(status().isBadRequest());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUom() throws Exception {
        // Initialize the database
        insertedUom = uomRepository.saveAndFlush(uom);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uom
        restUomMockMvc.perform(delete(ENTITY_API_URL_ID, uom.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uomRepository.count();
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

    protected Uom getPersistedUom(Uom uom) {
        return uomRepository.findById(uom.getId()).orElseThrow();
    }

    protected void assertPersistedUomToMatchAllProperties(Uom expectedUom) {
        assertUomAllPropertiesEquals(expectedUom, getPersistedUom(expectedUom));
    }

    protected void assertPersistedUomToMatchUpdatableProperties(Uom expectedUom) {
        assertUomAllUpdatablePropertiesEquals(expectedUom, getPersistedUom(expectedUom));
    }
}
