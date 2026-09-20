package com.snp.web.rest;

import static com.snp.domain.PicklistRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PickList;
import com.snp.domain.PicklistRole;
import com.snp.domain.RoleType;
import com.snp.repository.PicklistRoleRepository;
import com.snp.service.PicklistRoleService;
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
 * Integration tests for the {@link PicklistRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PicklistRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/picklist-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PicklistRoleRepository picklistRoleRepository;

    @Mock
    private PicklistRoleRepository picklistRoleRepositoryMock;

    @Mock
    private PicklistRoleService picklistRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPicklistRoleMockMvc;

    private PicklistRole picklistRole;

    private PicklistRole insertedPicklistRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PicklistRole createEntity(EntityManager em) {
        PicklistRole picklistRole = new PicklistRole();
        // Add required entity
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            pickList = PickListResourceIT.createEntity();
            em.persist(pickList);
            em.flush();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        picklistRole.setPickList(pickList);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        picklistRole.setParty(party);
        return picklistRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PicklistRole createUpdatedEntity(EntityManager em) {
        PicklistRole updatedPicklistRole = new PicklistRole();
        // Add required entity
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            pickList = PickListResourceIT.createUpdatedEntity();
            em.persist(pickList);
            em.flush();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        updatedPicklistRole.setPickList(pickList);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPicklistRole.setParty(party);
        return updatedPicklistRole;
    }

    @BeforeEach
    void initTest() {
        picklistRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPicklistRole != null) {
            picklistRoleRepository.delete(insertedPicklistRole);
            insertedPicklistRole = null;
        }
    }

    @Test
    @Transactional
    void createPicklistRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PicklistRole
        var returnedPicklistRole = om.readValue(
            restPicklistRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(picklistRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PicklistRole.class
        );

        // Validate the PicklistRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPicklistRoleUpdatableFieldsEquals(returnedPicklistRole, getPersistedPicklistRole(returnedPicklistRole));

        insertedPicklistRole = returnedPicklistRole;
    }

    @Test
    @Transactional
    void createPicklistRoleWithExistingId() throws Exception {
        // Create the PicklistRole with an existing ID
        picklistRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPicklistRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(picklistRole)))
            .andExpect(status().isBadRequest());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPicklistRoles() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        // Get all the picklistRoleList
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picklistRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPicklistRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(picklistRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPicklistRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(picklistRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPicklistRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(picklistRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPicklistRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(picklistRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPicklistRole() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        // Get the picklistRole
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, picklistRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(picklistRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getPicklistRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        Long id = picklistRole.getId();

        defaultPicklistRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPicklistRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPicklistRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPicklistRolesByPickListIsEqualToSomething() throws Exception {
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            picklistRoleRepository.saveAndFlush(picklistRole);
            pickList = PickListResourceIT.createEntity();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        em.persist(pickList);
        em.flush();
        picklistRole.setPickList(pickList);
        picklistRoleRepository.saveAndFlush(picklistRole);
        Long pickListId = pickList.getId();
        // Get all the picklistRoleList where pickList equals to pickListId
        defaultPicklistRoleShouldBeFound("pickListId.equals=" + pickListId);

        // Get all the picklistRoleList where pickList equals to (pickListId + 1)
        defaultPicklistRoleShouldNotBeFound("pickListId.equals=" + (pickListId + 1));
    }

    @Test
    @Transactional
    void getAllPicklistRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            picklistRoleRepository.saveAndFlush(picklistRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        picklistRole.setParty(party);
        picklistRoleRepository.saveAndFlush(picklistRole);
        Long partyId = party.getId();
        // Get all the picklistRoleList where party equals to partyId
        defaultPicklistRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the picklistRoleList where party equals to (partyId + 1)
        defaultPicklistRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllPicklistRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            picklistRoleRepository.saveAndFlush(picklistRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        picklistRole.setRoleType(roleType);
        picklistRoleRepository.saveAndFlush(picklistRole);
        Long roleTypeId = roleType.getId();
        // Get all the picklistRoleList where roleType equals to roleTypeId
        defaultPicklistRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the picklistRoleList where roleType equals to (roleTypeId + 1)
        defaultPicklistRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultPicklistRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPicklistRoleShouldBeFound(shouldBeFound);
        defaultPicklistRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPicklistRoleShouldBeFound(String filter) throws Exception {
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picklistRole.getId().intValue())));

        // Check, that the count call also returns 1
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPicklistRoleShouldNotBeFound(String filter) throws Exception {
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPicklistRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPicklistRole() throws Exception {
        // Get the picklistRole
        restPicklistRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPicklistRole() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the picklistRole
        PicklistRole updatedPicklistRole = picklistRoleRepository.findById(picklistRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPicklistRole are not directly saved in db
        em.detach(updatedPicklistRole);

        restPicklistRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPicklistRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPicklistRole))
            )
            .andExpect(status().isOk());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPicklistRoleToMatchAllProperties(updatedPicklistRole);
    }

    @Test
    @Transactional
    void putNonExistingPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, picklistRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(picklistRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(picklistRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(picklistRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePicklistRoleWithPatch() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the picklistRole using partial update
        PicklistRole partialUpdatedPicklistRole = new PicklistRole();
        partialUpdatedPicklistRole.setId(picklistRole.getId());

        restPicklistRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicklistRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPicklistRole))
            )
            .andExpect(status().isOk());

        // Validate the PicklistRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPicklistRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPicklistRole, picklistRole),
            getPersistedPicklistRole(picklistRole)
        );
    }

    @Test
    @Transactional
    void fullUpdatePicklistRoleWithPatch() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the picklistRole using partial update
        PicklistRole partialUpdatedPicklistRole = new PicklistRole();
        partialUpdatedPicklistRole.setId(picklistRole.getId());

        restPicklistRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicklistRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPicklistRole))
            )
            .andExpect(status().isOk());

        // Validate the PicklistRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPicklistRoleUpdatableFieldsEquals(partialUpdatedPicklistRole, getPersistedPicklistRole(partialUpdatedPicklistRole));
    }

    @Test
    @Transactional
    void patchNonExistingPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, picklistRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(picklistRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(picklistRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPicklistRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        picklistRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPicklistRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(picklistRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PicklistRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePicklistRole() throws Exception {
        // Initialize the database
        insertedPicklistRole = picklistRoleRepository.saveAndFlush(picklistRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the picklistRole
        restPicklistRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, picklistRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return picklistRoleRepository.count();
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

    protected PicklistRole getPersistedPicklistRole(PicklistRole picklistRole) {
        return picklistRoleRepository.findById(picklistRole.getId()).orElseThrow();
    }

    protected void assertPersistedPicklistRoleToMatchAllProperties(PicklistRole expectedPicklistRole) {
        assertPicklistRoleAllPropertiesEquals(expectedPicklistRole, getPersistedPicklistRole(expectedPicklistRole));
    }

    protected void assertPersistedPicklistRoleToMatchUpdatableProperties(PicklistRole expectedPicklistRole) {
        assertPicklistRoleAllUpdatablePropertiesEquals(expectedPicklistRole, getPersistedPicklistRole(expectedPicklistRole));
    }
}
