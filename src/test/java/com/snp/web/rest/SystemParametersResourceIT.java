package com.snp.web.rest;

import static com.snp.domain.SystemParametersAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.SystemParameters;
import com.snp.repository.SystemParametersRepository;
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
 * Integration tests for the {@link SystemParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemParametersResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ENVIRONMENT = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemParametersRepository systemParametersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemParametersMockMvc;

    private SystemParameters systemParameters;

    private SystemParameters insertedSystemParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameters createEntity() {
        return new SystemParameters()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .environment(DEFAULT_ENVIRONMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameters createUpdatedEntity() {
        return new SystemParameters()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .environment(UPDATED_ENVIRONMENT);
    }

    @BeforeEach
    void initTest() {
        systemParameters = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemParameters != null) {
            systemParametersRepository.delete(insertedSystemParameters);
            insertedSystemParameters = null;
        }
    }

    @Test
    @Transactional
    void createSystemParameters() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemParameters
        var returnedSystemParameters = om.readValue(
            restSystemParametersMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemParameters)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemParameters.class
        );

        // Validate the SystemParameters in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSystemParametersUpdatableFieldsEquals(returnedSystemParameters, getPersistedSystemParameters(returnedSystemParameters));

        insertedSystemParameters = returnedSystemParameters;
    }

    @Test
    @Transactional
    void createSystemParametersWithExistingId() throws Exception {
        // Create the SystemParameters with an existing ID
        systemParameters.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemParametersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemParameters)))
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemParameters.setKey(null);

        // Create the SystemParameters, which fails.

        restSystemParametersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemParameters)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemParameterses() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT)));
    }

    @Test
    @Transactional
    void getSystemParameters() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get the systemParameters
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, systemParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemParameters.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.environment").value(DEFAULT_ENVIRONMENT));
    }

    @Test
    @Transactional
    void getSystemParametersesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        Long id = systemParameters.getId();

        defaultSystemParametersFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSystemParametersFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSystemParametersFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where key equals to
        defaultSystemParametersFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where key in
        defaultSystemParametersFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where key is not null
        defaultSystemParametersFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemParametersesByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where key contains
        defaultSystemParametersFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where key does not contain
        defaultSystemParametersFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where value equals to
        defaultSystemParametersFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where value in
        defaultSystemParametersFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where value is not null
        defaultSystemParametersFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemParametersesByValueContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where value contains
        defaultSystemParametersFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where value does not contain
        defaultSystemParametersFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where description equals to
        defaultSystemParametersFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where description in
        defaultSystemParametersFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSystemParametersesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where description is not null
        defaultSystemParametersFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemParametersesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where description contains
        defaultSystemParametersFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where description does not contain
        defaultSystemParametersFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSystemParametersesByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where environment equals to
        defaultSystemParametersFiltering("environment.equals=" + DEFAULT_ENVIRONMENT, "environment.equals=" + UPDATED_ENVIRONMENT);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByEnvironmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where environment in
        defaultSystemParametersFiltering(
            "environment.in=" + DEFAULT_ENVIRONMENT + "," + UPDATED_ENVIRONMENT,
            "environment.in=" + UPDATED_ENVIRONMENT
        );
    }

    @Test
    @Transactional
    void getAllSystemParametersesByEnvironmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where environment is not null
        defaultSystemParametersFiltering("environment.specified=true", "environment.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemParametersesByEnvironmentContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where environment contains
        defaultSystemParametersFiltering("environment.contains=" + DEFAULT_ENVIRONMENT, "environment.contains=" + UPDATED_ENVIRONMENT);
    }

    @Test
    @Transactional
    void getAllSystemParametersesByEnvironmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList where environment does not contain
        defaultSystemParametersFiltering(
            "environment.doesNotContain=" + UPDATED_ENVIRONMENT,
            "environment.doesNotContain=" + DEFAULT_ENVIRONMENT
        );
    }

    private void defaultSystemParametersFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSystemParametersShouldBeFound(shouldBeFound);
        defaultSystemParametersShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemParametersShouldBeFound(String filter) throws Exception {
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].environment").value(hasItem(DEFAULT_ENVIRONMENT)));

        // Check, that the count call also returns 1
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemParametersShouldNotBeFound(String filter) throws Exception {
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemParameters() throws Exception {
        // Get the systemParameters
        restSystemParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemParameters() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemParameters
        SystemParameters updatedSystemParameters = systemParametersRepository.findById(systemParameters.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemParameters are not directly saved in db
        em.detach(updatedSystemParameters);
        updatedSystemParameters.key(UPDATED_KEY).value(UPDATED_VALUE).description(UPDATED_DESCRIPTION).environment(UPDATED_ENVIRONMENT);

        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSystemParameters))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemParametersToMatchAllProperties(updatedSystemParameters);
    }

    @Test
    @Transactional
    void putNonExistingSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemParameters)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemParametersWithPatch() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemParameters using partial update
        SystemParameters partialUpdatedSystemParameters = new SystemParameters();
        partialUpdatedSystemParameters.setId(systemParameters.getId());

        partialUpdatedSystemParameters.key(UPDATED_KEY).description(UPDATED_DESCRIPTION).environment(UPDATED_ENVIRONMENT);

        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemParameters))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemParametersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemParameters, systemParameters),
            getPersistedSystemParameters(systemParameters)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemParametersWithPatch() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemParameters using partial update
        SystemParameters partialUpdatedSystemParameters = new SystemParameters();
        partialUpdatedSystemParameters.setId(systemParameters.getId());

        partialUpdatedSystemParameters
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .environment(UPDATED_ENVIRONMENT);

        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemParameters))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemParametersUpdatableFieldsEquals(
            partialUpdatedSystemParameters,
            getPersistedSystemParameters(partialUpdatedSystemParameters)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemParameters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemParameters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemParameters)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemParameters() throws Exception {
        // Initialize the database
        insertedSystemParameters = systemParametersRepository.saveAndFlush(systemParameters);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemParameters
        restSystemParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemParametersRepository.count();
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

    protected SystemParameters getPersistedSystemParameters(SystemParameters systemParameters) {
        return systemParametersRepository.findById(systemParameters.getId()).orElseThrow();
    }

    protected void assertPersistedSystemParametersToMatchAllProperties(SystemParameters expectedSystemParameters) {
        assertSystemParametersAllPropertiesEquals(expectedSystemParameters, getPersistedSystemParameters(expectedSystemParameters));
    }

    protected void assertPersistedSystemParametersToMatchUpdatableProperties(SystemParameters expectedSystemParameters) {
        assertSystemParametersAllUpdatablePropertiesEquals(
            expectedSystemParameters,
            getPersistedSystemParameters(expectedSystemParameters)
        );
    }
}
