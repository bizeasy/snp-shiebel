package com.snp.web.rest;

import static com.snp.domain.GeoAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Geo;
import com.snp.domain.GeoType;
import com.snp.repository.GeoRepository;
import com.snp.service.GeoService;
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
 * Integration tests for the {@link GeoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GeoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/geos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoRepository geoRepository;

    @Mock
    private GeoRepository geoRepositoryMock;

    @Mock
    private GeoService geoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoMockMvc;

    private Geo geo;

    private Geo insertedGeo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Geo createEntity() {
        return new Geo().name(DEFAULT_NAME).code(DEFAULT_CODE).abbreviation(DEFAULT_ABBREVIATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Geo createUpdatedEntity() {
        return new Geo().name(UPDATED_NAME).code(UPDATED_CODE).abbreviation(UPDATED_ABBREVIATION);
    }

    @BeforeEach
    void initTest() {
        geo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGeo != null) {
            geoRepository.delete(insertedGeo);
            insertedGeo = null;
        }
    }

    @Test
    @Transactional
    void createGeo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Geo
        var returnedGeo = om.readValue(
            restGeoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Geo.class
        );

        // Validate the Geo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGeoUpdatableFieldsEquals(returnedGeo, getPersistedGeo(returnedGeo));

        insertedGeo = returnedGeo;
    }

    @Test
    @Transactional
    void createGeoWithExistingId() throws Exception {
        // Create the Geo with an existing ID
        geo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
            .andExpect(status().isBadRequest());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geo.setName(null);

        // Create the Geo, which fails.

        restGeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geo.setCode(null);

        // Create the Geo, which fails.

        restGeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeos() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList
        restGeoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeosWithEagerRelationshipsIsEnabled() throws Exception {
        when(geoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGeoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(geoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(geoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGeoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(geoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGeo() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get the geo
        restGeoMockMvc
            .perform(get(ENTITY_API_URL_ID, geo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION));
    }

    @Test
    @Transactional
    void getGeosByIdFiltering() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        Long id = geo.getId();

        defaultGeoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGeoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGeoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where name equals to
        defaultGeoFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where name in
        defaultGeoFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where name is not null
        defaultGeoFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGeosByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where name contains
        defaultGeoFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGeosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where name does not contain
        defaultGeoFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllGeosByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where code equals to
        defaultGeoFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGeosByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where code in
        defaultGeoFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGeosByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where code is not null
        defaultGeoFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllGeosByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where code contains
        defaultGeoFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGeosByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where code does not contain
        defaultGeoFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllGeosByAbbreviationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where abbreviation equals to
        defaultGeoFiltering("abbreviation.equals=" + DEFAULT_ABBREVIATION, "abbreviation.equals=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllGeosByAbbreviationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where abbreviation in
        defaultGeoFiltering(
            "abbreviation.in=" + DEFAULT_ABBREVIATION + "," + UPDATED_ABBREVIATION,
            "abbreviation.in=" + UPDATED_ABBREVIATION
        );
    }

    @Test
    @Transactional
    void getAllGeosByAbbreviationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where abbreviation is not null
        defaultGeoFiltering("abbreviation.specified=true", "abbreviation.specified=false");
    }

    @Test
    @Transactional
    void getAllGeosByAbbreviationContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where abbreviation contains
        defaultGeoFiltering("abbreviation.contains=" + DEFAULT_ABBREVIATION, "abbreviation.contains=" + UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllGeosByAbbreviationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        // Get all the geoList where abbreviation does not contain
        defaultGeoFiltering("abbreviation.doesNotContain=" + UPDATED_ABBREVIATION, "abbreviation.doesNotContain=" + DEFAULT_ABBREVIATION);
    }

    @Test
    @Transactional
    void getAllGeosByGeoTypeIsEqualToSomething() throws Exception {
        GeoType geoType;
        if (TestUtil.findAll(em, GeoType.class).isEmpty()) {
            geoRepository.saveAndFlush(geo);
            geoType = GeoTypeResourceIT.createEntity();
        } else {
            geoType = TestUtil.findAll(em, GeoType.class).getFirst();
        }
        em.persist(geoType);
        em.flush();
        geo.setGeoType(geoType);
        geoRepository.saveAndFlush(geo);
        Long geoTypeId = geoType.getId();
        // Get all the geoList where geoType equals to geoTypeId
        defaultGeoShouldBeFound("geoTypeId.equals=" + geoTypeId);

        // Get all the geoList where geoType equals to (geoTypeId + 1)
        defaultGeoShouldNotBeFound("geoTypeId.equals=" + (geoTypeId + 1));
    }

    private void defaultGeoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGeoShouldBeFound(shouldBeFound);
        defaultGeoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoShouldBeFound(String filter) throws Exception {
        restGeoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));

        // Check, that the count call also returns 1
        restGeoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeoShouldNotBeFound(String filter) throws Exception {
        restGeoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeo() throws Exception {
        // Get the geo
        restGeoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeo() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geo
        Geo updatedGeo = geoRepository.findById(geo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeo are not directly saved in db
        em.detach(updatedGeo);
        updatedGeo.name(UPDATED_NAME).code(UPDATED_CODE).abbreviation(UPDATED_ABBREVIATION);

        restGeoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedGeo))
            )
            .andExpect(status().isOk());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoToMatchAllProperties(updatedGeo);
    }

    @Test
    @Transactional
    void putNonExistingGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(put(ENTITY_API_URL_ID, geo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
            .andExpect(status().isBadRequest());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoWithPatch() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geo using partial update
        Geo partialUpdatedGeo = new Geo();
        partialUpdatedGeo.setId(geo.getId());

        partialUpdatedGeo.name(UPDATED_NAME);

        restGeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeo))
            )
            .andExpect(status().isOk());

        // Validate the Geo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGeo, geo), getPersistedGeo(geo));
    }

    @Test
    @Transactional
    void fullUpdateGeoWithPatch() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geo using partial update
        Geo partialUpdatedGeo = new Geo();
        partialUpdatedGeo.setId(geo.getId());

        partialUpdatedGeo.name(UPDATED_NAME).code(UPDATED_CODE).abbreviation(UPDATED_ABBREVIATION);

        restGeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeo))
            )
            .andExpect(status().isOk());

        // Validate the Geo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoUpdatableFieldsEquals(partialUpdatedGeo, getPersistedGeo(partialUpdatedGeo));
    }

    @Test
    @Transactional
    void patchNonExistingGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(patch(ENTITY_API_URL_ID, geo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geo)))
            .andExpect(status().isBadRequest());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Geo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeo() throws Exception {
        // Initialize the database
        insertedGeo = geoRepository.saveAndFlush(geo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geo
        restGeoMockMvc.perform(delete(ENTITY_API_URL_ID, geo.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoRepository.count();
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

    protected Geo getPersistedGeo(Geo geo) {
        return geoRepository.findById(geo.getId()).orElseThrow();
    }

    protected void assertPersistedGeoToMatchAllProperties(Geo expectedGeo) {
        assertGeoAllPropertiesEquals(expectedGeo, getPersistedGeo(expectedGeo));
    }

    protected void assertPersistedGeoToMatchUpdatableProperties(Geo expectedGeo) {
        assertGeoAllUpdatablePropertiesEquals(expectedGeo, getPersistedGeo(expectedGeo));
    }
}
