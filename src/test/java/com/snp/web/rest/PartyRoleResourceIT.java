package com.snp.web.rest;

import static com.snp.domain.PartyRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.PartyRole;
import com.snp.domain.RoleType;
import com.snp.repository.PartyRoleRepository;
import com.snp.service.PartyRoleService;
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
 * Integration tests for the {@link PartyRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/party-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartyRoleRepository partyRoleRepository;

    @Mock
    private PartyRoleRepository partyRoleRepositoryMock;

    @Mock
    private PartyRoleService partyRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyRoleMockMvc;

    private PartyRole partyRole;

    private PartyRole insertedPartyRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRole createEntity(EntityManager em) {
        PartyRole partyRole = new PartyRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        partyRole.setParty(party);
        // Add required entity
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            roleType = RoleTypeResourceIT.createEntity();
            em.persist(roleType);
            em.flush();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        partyRole.setRoleType(roleType);
        return partyRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRole createUpdatedEntity(EntityManager em) {
        PartyRole updatedPartyRole = new PartyRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedPartyRole.setParty(party);
        // Add required entity
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            roleType = RoleTypeResourceIT.createUpdatedEntity();
            em.persist(roleType);
            em.flush();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        updatedPartyRole.setRoleType(roleType);
        return updatedPartyRole;
    }

    @BeforeEach
    void initTest() {
        partyRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartyRole != null) {
            partyRoleRepository.delete(insertedPartyRole);
            insertedPartyRole = null;
        }
    }

    @Test
    @Transactional
    void createPartyRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartyRole
        var returnedPartyRole = om.readValue(
            restPartyRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartyRole.class
        );

        // Validate the PartyRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartyRoleUpdatableFieldsEquals(returnedPartyRole, getPersistedPartyRole(returnedPartyRole));

        insertedPartyRole = returnedPartyRole;
    }

    @Test
    @Transactional
    void createPartyRoleWithExistingId() throws Exception {
        // Create the PartyRole with an existing ID
        partyRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRole)))
            .andExpect(status().isBadRequest());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartyRoles() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        // Get all the partyRoleList
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partyRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partyRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartyRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partyRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartyRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partyRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartyRole() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        // Get the partyRole
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, partyRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getPartyRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        Long id = partyRole.getId();

        defaultPartyRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartyRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartyRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            partyRoleRepository.saveAndFlush(partyRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        partyRole.setParty(party);
        partyRoleRepository.saveAndFlush(partyRole);
        Long partyId = party.getId();
        // Get all the partyRoleList where party equals to partyId
        defaultPartyRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the partyRoleList where party equals to (partyId + 1)
        defaultPartyRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllPartyRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            partyRoleRepository.saveAndFlush(partyRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        partyRole.setRoleType(roleType);
        partyRoleRepository.saveAndFlush(partyRole);
        Long roleTypeId = roleType.getId();
        // Get all the partyRoleList where roleType equals to roleTypeId
        defaultPartyRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the partyRoleList where roleType equals to (roleTypeId + 1)
        defaultPartyRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultPartyRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartyRoleShouldBeFound(shouldBeFound);
        defaultPartyRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyRoleShouldBeFound(String filter) throws Exception {
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRole.getId().intValue())));

        // Check, that the count call also returns 1
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyRoleShouldNotBeFound(String filter) throws Exception {
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyRole() throws Exception {
        // Get the partyRole
        restPartyRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartyRole() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRole
        PartyRole updatedPartyRole = partyRoleRepository.findById(partyRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartyRole are not directly saved in db
        em.detach(updatedPartyRole);

        restPartyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartyRole))
            )
            .andExpect(status().isOk());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartyRoleToMatchAllProperties(updatedPartyRole);
    }

    @Test
    @Transactional
    void putNonExistingPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyRole.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partyRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyRoleWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRole using partial update
        PartyRole partialUpdatedPartyRole = new PartyRole();
        partialUpdatedPartyRole.setId(partyRole.getId());

        restPartyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRole))
            )
            .andExpect(status().isOk());

        // Validate the PartyRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartyRole, partyRole),
            getPersistedPartyRole(partyRole)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartyRoleWithPatch() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partyRole using partial update
        PartyRole partialUpdatedPartyRole = new PartyRole();
        partialUpdatedPartyRole.setId(partyRole.getId());

        restPartyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartyRole))
            )
            .andExpect(status().isOk());

        // Validate the PartyRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartyRoleUpdatableFieldsEquals(partialUpdatedPartyRole, getPersistedPartyRole(partialUpdatedPartyRole));
    }

    @Test
    @Transactional
    void patchNonExistingPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partyRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partyRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partyRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyRole() throws Exception {
        // Initialize the database
        insertedPartyRole = partyRoleRepository.saveAndFlush(partyRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partyRole
        restPartyRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partyRoleRepository.count();
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

    protected PartyRole getPersistedPartyRole(PartyRole partyRole) {
        return partyRoleRepository.findById(partyRole.getId()).orElseThrow();
    }

    protected void assertPersistedPartyRoleToMatchAllProperties(PartyRole expectedPartyRole) {
        assertPartyRoleAllPropertiesEquals(expectedPartyRole, getPersistedPartyRole(expectedPartyRole));
    }

    protected void assertPersistedPartyRoleToMatchUpdatableProperties(PartyRole expectedPartyRole) {
        assertPartyRoleAllUpdatablePropertiesEquals(expectedPartyRole, getPersistedPartyRole(expectedPartyRole));
    }
}
