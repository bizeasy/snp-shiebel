package com.snp.web.rest;

import static com.snp.domain.CatalogueCategoryTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CatalogueCategoryType;
import com.snp.repository.CatalogueCategoryTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CatalogueCategoryTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogueCategoryTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalogue-category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CatalogueCategoryTypeRepository catalogueCategoryTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogueCategoryTypeMockMvc;

    private CatalogueCategoryType catalogueCategoryType;

    private CatalogueCategoryType insertedCatalogueCategoryType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogueCategoryType createEntity() {
        return new CatalogueCategoryType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogueCategoryType createUpdatedEntity() {
        return new CatalogueCategoryType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        catalogueCategoryType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCatalogueCategoryType != null) {
            catalogueCategoryTypeRepository.delete(insertedCatalogueCategoryType);
            insertedCatalogueCategoryType = null;
        }
    }

    @Test
    @Transactional
    void createCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CatalogueCategoryType
        var returnedCatalogueCategoryType = om.readValue(
            restCatalogueCategoryTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategoryType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CatalogueCategoryType.class
        );

        // Validate the CatalogueCategoryType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCatalogueCategoryTypeUpdatableFieldsEquals(
            returnedCatalogueCategoryType,
            getPersistedCatalogueCategoryType(returnedCatalogueCategoryType)
        );

        insertedCatalogueCategoryType = returnedCatalogueCategoryType;
    }

    @Test
    @Transactional
    void createCatalogueCategoryTypeWithExistingId() throws Exception {
        // Create the CatalogueCategoryType with an existing ID
        catalogueCategoryType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogueCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategoryType)))
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        catalogueCategoryType.setName(null);

        // Create the CatalogueCategoryType, which fails.

        restCatalogueCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategoryType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypes() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogueCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCatalogueCategoryType() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get the catalogueCategoryType
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogueCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogueCategoryType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCatalogueCategoryTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        Long id = catalogueCategoryType.getId();

        defaultCatalogueCategoryTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCatalogueCategoryTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCatalogueCategoryTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where name equals to
        defaultCatalogueCategoryTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where name in
        defaultCatalogueCategoryTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where name is not null
        defaultCatalogueCategoryTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where name contains
        defaultCatalogueCategoryTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where name does not contain
        defaultCatalogueCategoryTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where description equals to
        defaultCatalogueCategoryTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where description in
        defaultCatalogueCategoryTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where description is not null
        defaultCatalogueCategoryTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where description contains
        defaultCatalogueCategoryTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCatalogueCategoryTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        // Get all the catalogueCategoryTypeList where description does not contain
        defaultCatalogueCategoryTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultCatalogueCategoryTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCatalogueCategoryTypeShouldBeFound(shouldBeFound);
        defaultCatalogueCategoryTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogueCategoryTypeShouldBeFound(String filter) throws Exception {
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogueCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogueCategoryTypeShouldNotBeFound(String filter) throws Exception {
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogueCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatalogueCategoryType() throws Exception {
        // Get the catalogueCategoryType
        restCatalogueCategoryTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalogueCategoryType() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategoryType
        CatalogueCategoryType updatedCatalogueCategoryType = catalogueCategoryTypeRepository
            .findById(catalogueCategoryType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCatalogueCategoryType are not directly saved in db
        em.detach(updatedCatalogueCategoryType);
        updatedCatalogueCategoryType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCatalogueCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogueCategoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCatalogueCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCatalogueCategoryTypeToMatchAllProperties(updatedCatalogueCategoryType);
    }

    @Test
    @Transactional
    void putNonExistingCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogueCategoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogueCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogueCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogueCategoryType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogueCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategoryType using partial update
        CatalogueCategoryType partialUpdatedCatalogueCategoryType = new CatalogueCategoryType();
        partialUpdatedCatalogueCategoryType.setId(catalogueCategoryType.getId());

        partialUpdatedCatalogueCategoryType.name(UPDATED_NAME);

        restCatalogueCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogueCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogueCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategoryType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueCategoryTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCatalogueCategoryType, catalogueCategoryType),
            getPersistedCatalogueCategoryType(catalogueCategoryType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCatalogueCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogueCategoryType using partial update
        CatalogueCategoryType partialUpdatedCatalogueCategoryType = new CatalogueCategoryType();
        partialUpdatedCatalogueCategoryType.setId(catalogueCategoryType.getId());

        partialUpdatedCatalogueCategoryType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCatalogueCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogueCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogueCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CatalogueCategoryType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueCategoryTypeUpdatableFieldsEquals(
            partialUpdatedCatalogueCategoryType,
            getPersistedCatalogueCategoryType(partialUpdatedCatalogueCategoryType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogueCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogueCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogueCategoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogueCategoryType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogueCategoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueCategoryTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(catalogueCategoryType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogueCategoryType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogueCategoryType() throws Exception {
        // Initialize the database
        insertedCatalogueCategoryType = catalogueCategoryTypeRepository.saveAndFlush(catalogueCategoryType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the catalogueCategoryType
        restCatalogueCategoryTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogueCategoryType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return catalogueCategoryTypeRepository.count();
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

    protected CatalogueCategoryType getPersistedCatalogueCategoryType(CatalogueCategoryType catalogueCategoryType) {
        return catalogueCategoryTypeRepository.findById(catalogueCategoryType.getId()).orElseThrow();
    }

    protected void assertPersistedCatalogueCategoryTypeToMatchAllProperties(CatalogueCategoryType expectedCatalogueCategoryType) {
        assertCatalogueCategoryTypeAllPropertiesEquals(
            expectedCatalogueCategoryType,
            getPersistedCatalogueCategoryType(expectedCatalogueCategoryType)
        );
    }

    protected void assertPersistedCatalogueCategoryTypeToMatchUpdatableProperties(CatalogueCategoryType expectedCatalogueCategoryType) {
        assertCatalogueCategoryTypeAllUpdatablePropertiesEquals(
            expectedCatalogueCategoryType,
            getPersistedCatalogueCategoryType(expectedCatalogueCategoryType)
        );
    }
}
