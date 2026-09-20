package com.snp.web.rest;

import static com.snp.domain.CustRequestCategoryAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequestCategory;
import com.snp.domain.CustRequestType;
import com.snp.repository.CustRequestCategoryRepository;
import com.snp.service.CustRequestCategoryService;
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
 * Integration tests for the {@link CustRequestCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cust-request-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestCategoryRepository custRequestCategoryRepository;

    @Mock
    private CustRequestCategoryRepository custRequestCategoryRepositoryMock;

    @Mock
    private CustRequestCategoryService custRequestCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestCategoryMockMvc;

    private CustRequestCategory custRequestCategory;

    private CustRequestCategory insertedCustRequestCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestCategory createEntity() {
        return new CustRequestCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestCategory createUpdatedEntity() {
        return new CustRequestCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        custRequestCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestCategory != null) {
            custRequestCategoryRepository.delete(insertedCustRequestCategory);
            insertedCustRequestCategory = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestCategory
        var returnedCustRequestCategory = om.readValue(
            restCustRequestCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestCategory.class
        );

        // Validate the CustRequestCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestCategoryUpdatableFieldsEquals(
            returnedCustRequestCategory,
            getPersistedCustRequestCategory(returnedCustRequestCategory)
        );

        insertedCustRequestCategory = returnedCustRequestCategory;
    }

    @Test
    @Transactional
    void createCustRequestCategoryWithExistingId() throws Exception {
        // Create the CustRequestCategory with an existing ID
        custRequestCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestCategory)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        custRequestCategory.setName(null);

        // Create the CustRequestCategory, which fails.

        restCustRequestCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustRequestCategories() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCustRequestCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestCategoryServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestCategoryServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCustRequestCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestCategory() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get the custRequestCategory
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCustRequestCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        Long id = custRequestCategory.getId();

        defaultCustRequestCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where name equals to
        defaultCustRequestCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where name in
        defaultCustRequestCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where name is not null
        defaultCustRequestCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where name contains
        defaultCustRequestCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where name does not contain
        defaultCustRequestCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where description equals to
        defaultCustRequestCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where description in
        defaultCustRequestCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where description is not null
        defaultCustRequestCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where description contains
        defaultCustRequestCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        // Get all the custRequestCategoryList where description does not contain
        defaultCustRequestCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestCategoriesByCustRequestTypeIsEqualToSomething() throws Exception {
        CustRequestType custRequestType;
        if (TestUtil.findAll(em, CustRequestType.class).isEmpty()) {
            custRequestCategoryRepository.saveAndFlush(custRequestCategory);
            custRequestType = CustRequestTypeResourceIT.createEntity();
        } else {
            custRequestType = TestUtil.findAll(em, CustRequestType.class).getFirst();
        }
        em.persist(custRequestType);
        em.flush();
        custRequestCategory.setCustRequestType(custRequestType);
        custRequestCategoryRepository.saveAndFlush(custRequestCategory);
        Long custRequestTypeId = custRequestType.getId();
        // Get all the custRequestCategoryList where custRequestType equals to custRequestTypeId
        defaultCustRequestCategoryShouldBeFound("custRequestTypeId.equals=" + custRequestTypeId);

        // Get all the custRequestCategoryList where custRequestType equals to (custRequestTypeId + 1)
        defaultCustRequestCategoryShouldNotBeFound("custRequestTypeId.equals=" + (custRequestTypeId + 1));
    }

    private void defaultCustRequestCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestCategoryShouldBeFound(shouldBeFound);
        defaultCustRequestCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestCategoryShouldBeFound(String filter) throws Exception {
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestCategoryShouldNotBeFound(String filter) throws Exception {
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestCategory() throws Exception {
        // Get the custRequestCategory
        restCustRequestCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestCategory() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestCategory
        CustRequestCategory updatedCustRequestCategory = custRequestCategoryRepository.findById(custRequestCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestCategory are not directly saved in db
        em.detach(updatedCustRequestCategory);
        updatedCustRequestCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustRequestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestCategory))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestCategoryToMatchAllProperties(updatedCustRequestCategory);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestCategory using partial update
        CustRequestCategory partialUpdatedCustRequestCategory = new CustRequestCategory();
        partialUpdatedCustRequestCategory.setId(custRequestCategory.getId());

        partialUpdatedCustRequestCategory.name(UPDATED_NAME);

        restCustRequestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestCategory))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestCategory, custRequestCategory),
            getPersistedCustRequestCategory(custRequestCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestCategory using partial update
        CustRequestCategory partialUpdatedCustRequestCategory = new CustRequestCategory();
        partialUpdatedCustRequestCategory.setId(custRequestCategory.getId());

        partialUpdatedCustRequestCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustRequestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestCategory))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestCategoryUpdatableFieldsEquals(
            partialUpdatedCustRequestCategory,
            getPersistedCustRequestCategory(partialUpdatedCustRequestCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestCategory() throws Exception {
        // Initialize the database
        insertedCustRequestCategory = custRequestCategoryRepository.saveAndFlush(custRequestCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestCategory
        restCustRequestCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestCategoryRepository.count();
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

    protected CustRequestCategory getPersistedCustRequestCategory(CustRequestCategory custRequestCategory) {
        return custRequestCategoryRepository.findById(custRequestCategory.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestCategoryToMatchAllProperties(CustRequestCategory expectedCustRequestCategory) {
        assertCustRequestCategoryAllPropertiesEquals(
            expectedCustRequestCategory,
            getPersistedCustRequestCategory(expectedCustRequestCategory)
        );
    }

    protected void assertPersistedCustRequestCategoryToMatchUpdatableProperties(CustRequestCategory expectedCustRequestCategory) {
        assertCustRequestCategoryAllUpdatablePropertiesEquals(
            expectedCustRequestCategory,
            getPersistedCustRequestCategory(expectedCustRequestCategory)
        );
    }
}
