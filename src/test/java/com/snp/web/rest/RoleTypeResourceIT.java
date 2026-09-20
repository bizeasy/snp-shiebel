package com.snp.web.rest;

import static com.snp.domain.RoleTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.RoleType;
import com.snp.repository.RoleTypeRepository;
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
 * Integration tests for the {@link RoleTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/role-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoleTypeRepository roleTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleTypeMockMvc;

    private RoleType roleType;

    private RoleType insertedRoleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleType createEntity() {
        return new RoleType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleType createUpdatedEntity() {
        return new RoleType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        roleType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRoleType != null) {
            roleTypeRepository.delete(insertedRoleType);
            insertedRoleType = null;
        }
    }

    @Test
    @Transactional
    void createRoleType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoleType
        var returnedRoleType = om.readValue(
            restRoleTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoleType.class
        );

        // Validate the RoleType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRoleTypeUpdatableFieldsEquals(returnedRoleType, getPersistedRoleType(returnedRoleType));

        insertedRoleType = returnedRoleType;
    }

    @Test
    @Transactional
    void createRoleTypeWithExistingId() throws Exception {
        // Create the RoleType with an existing ID
        roleType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleType)))
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roleType.setName(null);

        // Create the RoleType, which fails.

        restRoleTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoleTypes() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRoleType() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get the roleType
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, roleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getRoleTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        Long id = roleType.getId();

        defaultRoleTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoleTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoleTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoleTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where name equals to
        defaultRoleTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoleTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where name in
        defaultRoleTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoleTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where name is not null
        defaultRoleTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRoleTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where name contains
        defaultRoleTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoleTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where name does not contain
        defaultRoleTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRoleTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where description equals to
        defaultRoleTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoleTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where description in
        defaultRoleTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllRoleTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where description is not null
        defaultRoleTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRoleTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where description contains
        defaultRoleTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoleTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList where description does not contain
        defaultRoleTypeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultRoleTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoleTypeShouldBeFound(shouldBeFound);
        defaultRoleTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoleTypeShouldBeFound(String filter) throws Exception {
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoleTypeShouldNotBeFound(String filter) throws Exception {
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoleType() throws Exception {
        // Get the roleType
        restRoleTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoleType() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleType
        RoleType updatedRoleType = roleTypeRepository.findById(roleType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoleType are not directly saved in db
        em.detach(updatedRoleType);
        updatedRoleType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoleType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRoleType))
            )
            .andExpect(status().isOk());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleTypeToMatchAllProperties(updatedRoleType);
    }

    @Test
    @Transactional
    void putNonExistingRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleTypeWithPatch() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleType using partial update
        RoleType partialUpdatedRoleType = new RoleType();
        partialUpdatedRoleType.setId(roleType.getId());

        partialUpdatedRoleType.description(UPDATED_DESCRIPTION);

        restRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleType))
            )
            .andExpect(status().isOk());

        // Validate the RoleType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoleType, roleType), getPersistedRoleType(roleType));
    }

    @Test
    @Transactional
    void fullUpdateRoleTypeWithPatch() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleType using partial update
        RoleType partialUpdatedRoleType = new RoleType();
        partialUpdatedRoleType.setId(roleType.getId());

        partialUpdatedRoleType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleType))
            )
            .andExpect(status().isOk());

        // Validate the RoleType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleTypeUpdatableFieldsEquals(partialUpdatedRoleType, getPersistedRoleType(partialUpdatedRoleType));
    }

    @Test
    @Transactional
    void patchNonExistingRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleType() throws Exception {
        // Initialize the database
        insertedRoleType = roleTypeRepository.saveAndFlush(roleType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roleType
        restRoleTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roleTypeRepository.count();
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

    protected RoleType getPersistedRoleType(RoleType roleType) {
        return roleTypeRepository.findById(roleType.getId()).orElseThrow();
    }

    protected void assertPersistedRoleTypeToMatchAllProperties(RoleType expectedRoleType) {
        assertRoleTypeAllPropertiesEquals(expectedRoleType, getPersistedRoleType(expectedRoleType));
    }

    protected void assertPersistedRoleTypeToMatchUpdatableProperties(RoleType expectedRoleType) {
        assertRoleTypeAllUpdatablePropertiesEquals(expectedRoleType, getPersistedRoleType(expectedRoleType));
    }
}
