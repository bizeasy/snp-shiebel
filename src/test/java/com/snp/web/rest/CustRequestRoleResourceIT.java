package com.snp.web.rest;

import static com.snp.domain.CustRequestRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.CustRequest;
import com.snp.domain.CustRequestItem;
import com.snp.domain.CustRequestRole;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.repository.CustRequestRoleRepository;
import com.snp.service.CustRequestRoleService;
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
 * Integration tests for the {@link CustRequestRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustRequestRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/cust-request-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustRequestRoleRepository custRequestRoleRepository;

    @Mock
    private CustRequestRoleRepository custRequestRoleRepositoryMock;

    @Mock
    private CustRequestRoleService custRequestRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustRequestRoleMockMvc;

    private CustRequestRole custRequestRole;

    private CustRequestRole insertedCustRequestRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestRole createEntity(EntityManager em) {
        CustRequestRole custRequestRole = new CustRequestRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        custRequestRole.setParty(party);
        return custRequestRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustRequestRole createUpdatedEntity(EntityManager em) {
        CustRequestRole updatedCustRequestRole = new CustRequestRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedCustRequestRole.setParty(party);
        return updatedCustRequestRole;
    }

    @BeforeEach
    void initTest() {
        custRequestRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustRequestRole != null) {
            custRequestRoleRepository.delete(insertedCustRequestRole);
            insertedCustRequestRole = null;
        }
    }

    @Test
    @Transactional
    void createCustRequestRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustRequestRole
        var returnedCustRequestRole = om.readValue(
            restCustRequestRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustRequestRole.class
        );

        // Validate the CustRequestRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustRequestRoleUpdatableFieldsEquals(returnedCustRequestRole, getPersistedCustRequestRole(returnedCustRequestRole));

        insertedCustRequestRole = returnedCustRequestRole;
    }

    @Test
    @Transactional
    void createCustRequestRoleWithExistingId() throws Exception {
        // Create the CustRequestRole with an existing ID
        custRequestRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustRequestRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestRole)))
            .andExpect(status().isBadRequest());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustRequestRoles() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        // Get all the custRequestRoleList
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(custRequestRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(custRequestRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustRequestRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(custRequestRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustRequestRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(custRequestRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustRequestRole() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        // Get the custRequestRole
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, custRequestRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custRequestRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getCustRequestRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        Long id = custRequestRole.getId();

        defaultCustRequestRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustRequestRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustRequestRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustRequestRolesByCustRequestIsEqualToSomething() throws Exception {
        CustRequest custRequest;
        if (TestUtil.findAll(em, CustRequest.class).isEmpty()) {
            custRequestRoleRepository.saveAndFlush(custRequestRole);
            custRequest = CustRequestResourceIT.createEntity(em);
        } else {
            custRequest = TestUtil.findAll(em, CustRequest.class).getFirst();
        }
        em.persist(custRequest);
        em.flush();
        custRequestRole.setCustRequest(custRequest);
        custRequestRoleRepository.saveAndFlush(custRequestRole);
        Long custRequestId = custRequest.getId();
        // Get all the custRequestRoleList where custRequest equals to custRequestId
        defaultCustRequestRoleShouldBeFound("custRequestId.equals=" + custRequestId);

        // Get all the custRequestRoleList where custRequest equals to (custRequestId + 1)
        defaultCustRequestRoleShouldNotBeFound("custRequestId.equals=" + (custRequestId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestRolesByCustRequestItemIsEqualToSomething() throws Exception {
        CustRequestItem custRequestItem;
        if (TestUtil.findAll(em, CustRequestItem.class).isEmpty()) {
            custRequestRoleRepository.saveAndFlush(custRequestRole);
            custRequestItem = CustRequestItemResourceIT.createEntity(em);
        } else {
            custRequestItem = TestUtil.findAll(em, CustRequestItem.class).getFirst();
        }
        em.persist(custRequestItem);
        em.flush();
        custRequestRole.setCustRequestItem(custRequestItem);
        custRequestRoleRepository.saveAndFlush(custRequestRole);
        Long custRequestItemId = custRequestItem.getId();
        // Get all the custRequestRoleList where custRequestItem equals to custRequestItemId
        defaultCustRequestRoleShouldBeFound("custRequestItemId.equals=" + custRequestItemId);

        // Get all the custRequestRoleList where custRequestItem equals to (custRequestItemId + 1)
        defaultCustRequestRoleShouldNotBeFound("custRequestItemId.equals=" + (custRequestItemId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            custRequestRoleRepository.saveAndFlush(custRequestRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        custRequestRole.setParty(party);
        custRequestRoleRepository.saveAndFlush(custRequestRole);
        Long partyId = party.getId();
        // Get all the custRequestRoleList where party equals to partyId
        defaultCustRequestRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the custRequestRoleList where party equals to (partyId + 1)
        defaultCustRequestRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllCustRequestRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            custRequestRoleRepository.saveAndFlush(custRequestRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        custRequestRole.setRoleType(roleType);
        custRequestRoleRepository.saveAndFlush(custRequestRole);
        Long roleTypeId = roleType.getId();
        // Get all the custRequestRoleList where roleType equals to roleTypeId
        defaultCustRequestRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the custRequestRoleList where roleType equals to (roleTypeId + 1)
        defaultCustRequestRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultCustRequestRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustRequestRoleShouldBeFound(shouldBeFound);
        defaultCustRequestRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustRequestRoleShouldBeFound(String filter) throws Exception {
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custRequestRole.getId().intValue())));

        // Check, that the count call also returns 1
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustRequestRoleShouldNotBeFound(String filter) throws Exception {
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustRequestRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustRequestRole() throws Exception {
        // Get the custRequestRole
        restCustRequestRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustRequestRole() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestRole
        CustRequestRole updatedCustRequestRole = custRequestRoleRepository.findById(custRequestRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustRequestRole are not directly saved in db
        em.detach(updatedCustRequestRole);

        restCustRequestRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustRequestRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustRequestRole))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustRequestRoleToMatchAllProperties(updatedCustRequestRole);
    }

    @Test
    @Transactional
    void putNonExistingCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, custRequestRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(custRequestRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(custRequestRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustRequestRoleWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestRole using partial update
        CustRequestRole partialUpdatedCustRequestRole = new CustRequestRole();
        partialUpdatedCustRequestRole.setId(custRequestRole.getId());

        restCustRequestRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestRole))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustRequestRole, custRequestRole),
            getPersistedCustRequestRole(custRequestRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustRequestRoleWithPatch() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the custRequestRole using partial update
        CustRequestRole partialUpdatedCustRequestRole = new CustRequestRole();
        partialUpdatedCustRequestRole.setId(custRequestRole.getId());

        restCustRequestRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustRequestRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustRequestRole))
            )
            .andExpect(status().isOk());

        // Validate the CustRequestRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustRequestRoleUpdatableFieldsEquals(
            partialUpdatedCustRequestRole,
            getPersistedCustRequestRole(partialUpdatedCustRequestRole)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, custRequestRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(custRequestRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustRequestRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        custRequestRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustRequestRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(custRequestRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustRequestRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustRequestRole() throws Exception {
        // Initialize the database
        insertedCustRequestRole = custRequestRoleRepository.saveAndFlush(custRequestRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the custRequestRole
        restCustRequestRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, custRequestRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return custRequestRoleRepository.count();
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

    protected CustRequestRole getPersistedCustRequestRole(CustRequestRole custRequestRole) {
        return custRequestRoleRepository.findById(custRequestRole.getId()).orElseThrow();
    }

    protected void assertPersistedCustRequestRoleToMatchAllProperties(CustRequestRole expectedCustRequestRole) {
        assertCustRequestRoleAllPropertiesEquals(expectedCustRequestRole, getPersistedCustRequestRole(expectedCustRequestRole));
    }

    protected void assertPersistedCustRequestRoleToMatchUpdatableProperties(CustRequestRole expectedCustRequestRole) {
        assertCustRequestRoleAllUpdatablePropertiesEquals(expectedCustRequestRole, getPersistedCustRequestRole(expectedCustRequestRole));
    }
}
