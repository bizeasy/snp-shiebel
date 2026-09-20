package com.snp.web.rest;

import static com.snp.domain.CustRequestTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequestType;
import com.snp.domain.CustRequestType;
import com.snp.domain.Party;
import com.snp.repository.CustRequestTypeRepository;
import com.snp.service.CustRequestTypeService;
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
 * Integration tests for the {@link CustRequestTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cust-request-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestTypeRepository custRequestTypeRepository;

    @Mock
    private CustRequestTypeRepository custRequestTypeRepositoryMock;

    @Mock
    private CustRequestTypeService custRequestTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestTypeMockMvc;

    private CustRequestType custRequestType;

    private CustRequestType insertedCustRequestType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestType createEntity() {
        return new CustRequestType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestType createUpdatedEntity() {
        return new CustRequestType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        custRequestType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestType != null) {
            custRequestTypeRepository.delete(insertedCustRequestType);
            insertedCustRequestType = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestType
        var returnedCustRequestType = om.readValue(
            restCustRequestTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestType.class
        );

        // Validate the CustRequestType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestTypeUpdatableFieldsEquals(returnedCustRequestType, getPersistedCustRequestType(returnedCustRequestType));

        insertedCustRequestType = returnedCustRequestType;
    }

    @Test
    @Transactional
    void createCustRequestTypeWithExistingId() throws Exception {
        // Create the CustRequestType with an existing ID
        custRequestType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestType)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        custRequestType.setName(null);

        // Create the CustRequestType, which fails.

        restCustRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustRequestTypes() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCustRequestTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestTypeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCustRequestTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestType() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get the custRequestType
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCustRequestTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        Long id = custRequestType.getId();

        defaultCustRequestTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where name equals to
        defaultCustRequestTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where name in
        defaultCustRequestTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where name is not null
        defaultCustRequestTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where name contains
        defaultCustRequestTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where name does not contain
        defaultCustRequestTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where description equals to
        defaultCustRequestTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where description in
        defaultCustRequestTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where description is not null
        defaultCustRequestTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where description contains
        defaultCustRequestTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        // Get all the custRequestTypeList where description does not contain
        defaultCustRequestTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByParentIsEqualToSomething() throws Exception {
        CustRequestType parent;
        if (TestUtil.findAll(em, CustRequestType.class).isEmpty()) {
            custRequestTypeRepository.saveAndFlush(custRequestType);
            parent = CustRequestTypeResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, CustRequestType.class).getFirst();
        }
        em.persist(parent);
        em.flush();
        custRequestType.setParent(parent);
        custRequestTypeRepository.saveAndFlush(custRequestType);
        Long parentId = parent.getId();
        // Get all the custRequestTypeList where parent equals to parentId
        defaultCustRequestTypeShouldBeFound("parentId.equals=" + parentId);

        // Get all the custRequestTypeList where parent equals to (parentId + 1)
        defaultCustRequestTypeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestTypesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            custRequestTypeRepository.saveAndFlush(custRequestType);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        custRequestType.setParty(party);
        custRequestTypeRepository.saveAndFlush(custRequestType);
        Long partyId = party.getId();
        // Get all the custRequestTypeList where party equals to partyId
        defaultCustRequestTypeShouldBeFound("partyId.equals=" + partyId);

        // Get all the custRequestTypeList where party equals to (partyId + 1)
        defaultCustRequestTypeShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    private void defaultCustRequestTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestTypeShouldBeFound(shouldBeFound);
        defaultCustRequestTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestTypeShouldBeFound(String filter) throws Exception {
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestTypeShouldNotBeFound(String filter) throws Exception {
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestType() throws Exception {
        // Get the custRequestType
        restCustRequestTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestType() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestType
        CustRequestType updatedCustRequestType = custRequestTypeRepository.findById(custRequestType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestType are not directly saved in db
        em.detach(updatedCustRequestType);
        updatedCustRequestType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestType))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestTypeToMatchAllProperties(updatedCustRequestType);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestType using partial update
        CustRequestType partialUpdatedCustRequestType = new CustRequestType();
        partialUpdatedCustRequestType.setId(custRequestType.getId());

        partialUpdatedCustRequestType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestType))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestType, custRequestType),
            getPersistedCustRequestType(custRequestType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestType using partial update
        CustRequestType partialUpdatedCustRequestType = new CustRequestType();
        partialUpdatedCustRequestType.setId(custRequestType.getId());

        partialUpdatedCustRequestType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestType))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestTypeUpdatableFieldsEquals(
            partialUpdatedCustRequestType,
            getPersistedCustRequestType(partialUpdatedCustRequestType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestType() throws Exception {
        // Initialize the database
        insertedCustRequestType = custRequestTypeRepository.saveAndFlush(custRequestType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestType
        restCustRequestTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestTypeRepository.count();
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

    protected CustRequestType getPersistedCustRequestType(CustRequestType custRequestType) {
        return custRequestTypeRepository.findById(custRequestType.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestTypeToMatchAllProperties(CustRequestType expectedCustRequestType) {
        assertCustRequestTypeAllPropertiesEquals(expectedCustRequestType, getPersistedCustRequestType(expectedCustRequestType));
    }

    protected void assertPersistedCustRequestTypeToMatchUpdatableProperties(CustRequestType expectedCustRequestType) {
        assertCustRequestTypeAllUpdatablePropertiesEquals(expectedCustRequestType, getPersistedCustRequestType(expectedCustRequestType));
    }
}
