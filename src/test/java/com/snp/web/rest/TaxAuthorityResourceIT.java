package com.snp.web.rest;

import static com.snp.domain.TaxAuthorityAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Geo;
import com.snp.domain.TaxAuthority;
import com.snp.repository.TaxAuthorityRepository;
import com.snp.service.TaxAuthorityService;
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
 * Integration tests for the {@link TaxAuthorityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxAuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tax-authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxAuthorityRepository taxAuthorityRepository;

    @Mock
    private TaxAuthorityRepository taxAuthorityRepositoryMock;

    @Mock
    private TaxAuthorityService taxAuthorityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxAuthorityMockMvc;

    private TaxAuthority taxAuthority;

    private TaxAuthority insertedTaxAuthority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAuthority createEntity() {
        return new TaxAuthority().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAuthority createUpdatedEntity() {
        return new TaxAuthority().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        taxAuthority = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTaxAuthority != null) {
            taxAuthorityRepository.delete(insertedTaxAuthority);
            insertedTaxAuthority = null;
        }
    }

    @Test
    @Transactional
    void createTaxAuthority() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaxAuthority
        var returnedTaxAuthority = om.readValue(
            restTaxAuthorityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthority)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaxAuthority.class
        );

        // Validate the TaxAuthority in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaxAuthorityUpdatableFieldsEquals(returnedTaxAuthority, getPersistedTaxAuthority(returnedTaxAuthority));

        insertedTaxAuthority = returnedTaxAuthority;
    }

    @Test
    @Transactional
    void createTaxAuthorityWithExistingId() throws Exception {
        // Create the TaxAuthority with an existing ID
        taxAuthority.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxAuthorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthority)))
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxAuthority.setName(null);

        // Create the TaxAuthority, which fails.

        restTaxAuthorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthority)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxAuthorities() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAuthoritiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxAuthorityServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTaxAuthorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxAuthorityServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAuthoritiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxAuthorityServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTaxAuthorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(taxAuthorityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTaxAuthority() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get the taxAuthority
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL_ID, taxAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxAuthority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTaxAuthoritiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        Long id = taxAuthority.getId();

        defaultTaxAuthorityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaxAuthorityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaxAuthorityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList where name equals to
        defaultTaxAuthorityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList where name in
        defaultTaxAuthorityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList where name is not null
        defaultTaxAuthorityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList where name contains
        defaultTaxAuthorityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        // Get all the taxAuthorityList where name does not contain
        defaultTaxAuthorityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTaxAuthoritiesByGeoIsEqualToSomething() throws Exception {
        Geo geo;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            taxAuthorityRepository.saveAndFlush(taxAuthority);
            geo = GeoResourceIT.createEntity();
        } else {
            geo = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(geo);
        em.flush();
        taxAuthority.setGeo(geo);
        taxAuthorityRepository.saveAndFlush(taxAuthority);
        Long geoId = geo.getId();
        // Get all the taxAuthorityList where geo equals to geoId
        defaultTaxAuthorityShouldBeFound("geoId.equals=" + geoId);

        // Get all the taxAuthorityList where geo equals to (geoId + 1)
        defaultTaxAuthorityShouldNotBeFound("geoId.equals=" + (geoId + 1));
    }

    private void defaultTaxAuthorityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaxAuthorityShouldBeFound(shouldBeFound);
        defaultTaxAuthorityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxAuthorityShouldBeFound(String filter) throws Exception {
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxAuthorityShouldNotBeFound(String filter) throws Exception {
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxAuthority() throws Exception {
        // Get the taxAuthority
        restTaxAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxAuthority() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthority
        TaxAuthority updatedTaxAuthority = taxAuthorityRepository.findById(taxAuthority.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxAuthority are not directly saved in db
        em.detach(updatedTaxAuthority);
        updatedTaxAuthority.name(UPDATED_NAME);

        restTaxAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxAuthority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTaxAuthority))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxAuthorityToMatchAllProperties(updatedTaxAuthority);
    }

    @Test
    @Transactional
    void putNonExistingTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAuthority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxAuthority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxAuthority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxAuthority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxAuthorityWithPatch() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthority using partial update
        TaxAuthority partialUpdatedTaxAuthority = new TaxAuthority();
        partialUpdatedTaxAuthority.setId(taxAuthority.getId());

        partialUpdatedTaxAuthority.name(UPDATED_NAME);

        restTaxAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxAuthority))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxAuthorityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaxAuthority, taxAuthority),
            getPersistedTaxAuthority(taxAuthority)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaxAuthorityWithPatch() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxAuthority using partial update
        TaxAuthority partialUpdatedTaxAuthority = new TaxAuthority();
        partialUpdatedTaxAuthority.setId(taxAuthority.getId());

        partialUpdatedTaxAuthority.name(UPDATED_NAME);

        restTaxAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxAuthority))
            )
            .andExpect(status().isOk());

        // Validate the TaxAuthority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxAuthorityUpdatableFieldsEquals(partialUpdatedTaxAuthority, getPersistedTaxAuthority(partialUpdatedTaxAuthority));
    }

    @Test
    @Transactional
    void patchNonExistingTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxAuthority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxAuthority))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxAuthority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAuthorityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxAuthority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAuthority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxAuthority() throws Exception {
        // Initialize the database
        insertedTaxAuthority = taxAuthorityRepository.saveAndFlush(taxAuthority);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taxAuthority
        restTaxAuthorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxAuthority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taxAuthorityRepository.count();
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

    protected TaxAuthority getPersistedTaxAuthority(TaxAuthority taxAuthority) {
        return taxAuthorityRepository.findById(taxAuthority.getId()).orElseThrow();
    }

    protected void assertPersistedTaxAuthorityToMatchAllProperties(TaxAuthority expectedTaxAuthority) {
        assertTaxAuthorityAllPropertiesEquals(expectedTaxAuthority, getPersistedTaxAuthority(expectedTaxAuthority));
    }

    protected void assertPersistedTaxAuthorityToMatchUpdatableProperties(TaxAuthority expectedTaxAuthority) {
        assertTaxAuthorityAllUpdatablePropertiesEquals(expectedTaxAuthority, getPersistedTaxAuthority(expectedTaxAuthority));
    }
}
