package com.snp.web.rest;

import static com.snp.domain.ShoppingListTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ShoppingListType;
import com.snp.repository.ShoppingListTypeRepository;
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
 * Integration tests for the {@link ShoppingListTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShoppingListTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shopping-list-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingListTypeRepository shoppingListTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingListTypeMockMvc;

    private ShoppingListType shoppingListType;

    private ShoppingListType insertedShoppingListType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingListType createEntity() {
        return new ShoppingListType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingListType createUpdatedEntity() {
        return new ShoppingListType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        shoppingListType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShoppingListType != null) {
            shoppingListTypeRepository.delete(insertedShoppingListType);
            insertedShoppingListType = null;
        }
    }

    @Test
    @Transactional
    void createShoppingListType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingListType
        var returnedShoppingListType = om.readValue(
            restShoppingListTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingListType.class
        );

        // Validate the ShoppingListType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShoppingListTypeUpdatableFieldsEquals(returnedShoppingListType, getPersistedShoppingListType(returnedShoppingListType));

        insertedShoppingListType = returnedShoppingListType;
    }

    @Test
    @Transactional
    void createShoppingListTypeWithExistingId() throws Exception {
        // Create the ShoppingListType with an existing ID
        shoppingListType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingListTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListType)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingListType.setName(null);

        // Create the ShoppingListType, which fails.

        restShoppingListTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingListTypes() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingListType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getShoppingListType() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get the shoppingListType
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingListType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingListType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getShoppingListTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        Long id = shoppingListType.getId();

        defaultShoppingListTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShoppingListTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShoppingListTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where name equals to
        defaultShoppingListTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where name in
        defaultShoppingListTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where name is not null
        defaultShoppingListTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where name contains
        defaultShoppingListTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where name does not contain
        defaultShoppingListTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where description equals to
        defaultShoppingListTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where description in
        defaultShoppingListTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where description is not null
        defaultShoppingListTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where description contains
        defaultShoppingListTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShoppingListTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        // Get all the shoppingListTypeList where description does not contain
        defaultShoppingListTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultShoppingListTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShoppingListTypeShouldBeFound(shouldBeFound);
        defaultShoppingListTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShoppingListTypeShouldBeFound(String filter) throws Exception {
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingListType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShoppingListTypeShouldNotBeFound(String filter) throws Exception {
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShoppingListTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShoppingListType() throws Exception {
        // Get the shoppingListType
        restShoppingListTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingListType() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListType
        ShoppingListType updatedShoppingListType = shoppingListTypeRepository.findById(shoppingListType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingListType are not directly saved in db
        em.detach(updatedShoppingListType);
        updatedShoppingListType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restShoppingListTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShoppingListType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShoppingListType))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingListTypeToMatchAllProperties(updatedShoppingListType);
    }

    @Test
    @Transactional
    void putNonExistingShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingListType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingListType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingListType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingListTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListType using partial update
        ShoppingListType partialUpdatedShoppingListType = new ShoppingListType();
        partialUpdatedShoppingListType.setId(shoppingListType.getId());

        partialUpdatedShoppingListType.description(UPDATED_DESCRIPTION);

        restShoppingListTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingListType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingListType))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingListType, shoppingListType),
            getPersistedShoppingListType(shoppingListType)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingListTypeWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListType using partial update
        ShoppingListType partialUpdatedShoppingListType = new ShoppingListType();
        partialUpdatedShoppingListType.setId(shoppingListType.getId());

        partialUpdatedShoppingListType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restShoppingListTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingListType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingListType))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListTypeUpdatableFieldsEquals(
            partialUpdatedShoppingListType,
            getPersistedShoppingListType(partialUpdatedShoppingListType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingListType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingListType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingListType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingListType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingListType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingListType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingListType() throws Exception {
        // Initialize the database
        insertedShoppingListType = shoppingListTypeRepository.saveAndFlush(shoppingListType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingListType
        restShoppingListTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingListType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingListTypeRepository.count();
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

    protected ShoppingListType getPersistedShoppingListType(ShoppingListType shoppingListType) {
        return shoppingListTypeRepository.findById(shoppingListType.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingListTypeToMatchAllProperties(ShoppingListType expectedShoppingListType) {
        assertShoppingListTypeAllPropertiesEquals(expectedShoppingListType, getPersistedShoppingListType(expectedShoppingListType));
    }

    protected void assertPersistedShoppingListTypeToMatchUpdatableProperties(ShoppingListType expectedShoppingListType) {
        assertShoppingListTypeAllUpdatablePropertiesEquals(
            expectedShoppingListType,
            getPersistedShoppingListType(expectedShoppingListType)
        );
    }
}
