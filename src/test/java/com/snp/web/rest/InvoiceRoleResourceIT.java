package com.snp.web.rest;

import static com.snp.domain.InvoiceRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Invoice;
import com.snp.domain.InvoiceItem;
import com.snp.domain.InvoiceRole;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.repository.InvoiceRoleRepository;
import com.snp.service.InvoiceRoleService;
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
 * Integration tests for the {@link InvoiceRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/invoice-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceRoleRepository invoiceRoleRepository;

    @Mock
    private InvoiceRoleRepository invoiceRoleRepositoryMock;

    @Mock
    private InvoiceRoleService invoiceRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceRoleMockMvc;

    private InvoiceRole invoiceRole;

    private InvoiceRole insertedInvoiceRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceRole createEntity(EntityManager em) {
        InvoiceRole invoiceRole = new InvoiceRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        invoiceRole.setParty(party);
        return invoiceRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceRole createUpdatedEntity(EntityManager em) {
        InvoiceRole updatedInvoiceRole = new InvoiceRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedInvoiceRole.setParty(party);
        return updatedInvoiceRole;
    }

    @BeforeEach
    void initTest() {
        invoiceRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceRole != null) {
            invoiceRoleRepository.delete(insertedInvoiceRole);
            insertedInvoiceRole = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceRole
        var returnedInvoiceRole = om.readValue(
            restInvoiceRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceRole.class
        );

        // Validate the InvoiceRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceRoleUpdatableFieldsEquals(returnedInvoiceRole, getPersistedInvoiceRole(returnedInvoiceRole));

        insertedInvoiceRole = returnedInvoiceRole;
    }

    @Test
    @Transactional
    void createInvoiceRoleWithExistingId() throws Exception {
        // Create the InvoiceRole with an existing ID
        invoiceRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceRole)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceRoles() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        // Get all the invoiceRoleList
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoiceRole() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        // Get the invoiceRole
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getInvoiceRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        Long id = invoiceRole.getId();

        defaultInvoiceRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceRolesByInvoiceIsEqualToSomething() throws Exception {
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoiceRoleRepository.saveAndFlush(invoiceRole);
            invoice = InvoiceResourceIT.createEntity(em);
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).getFirst();
        }
        em.persist(invoice);
        em.flush();
        invoiceRole.setInvoice(invoice);
        invoiceRoleRepository.saveAndFlush(invoiceRole);
        Long invoiceId = invoice.getId();
        // Get all the invoiceRoleList where invoice equals to invoiceId
        defaultInvoiceRoleShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the invoiceRoleList where invoice equals to (invoiceId + 1)
        defaultInvoiceRoleShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceRolesByInvoiceItemIsEqualToSomething() throws Exception {
        InvoiceItem invoiceItem;
        if (TestUtil.findAll(em, InvoiceItem.class).isEmpty()) {
            invoiceRoleRepository.saveAndFlush(invoiceRole);
            invoiceItem = InvoiceItemResourceIT.createEntity(em);
        } else {
            invoiceItem = TestUtil.findAll(em, InvoiceItem.class).getFirst();
        }
        em.persist(invoiceItem);
        em.flush();
        invoiceRole.setInvoiceItem(invoiceItem);
        invoiceRoleRepository.saveAndFlush(invoiceRole);
        Long invoiceItemId = invoiceItem.getId();
        // Get all the invoiceRoleList where invoiceItem equals to invoiceItemId
        defaultInvoiceRoleShouldBeFound("invoiceItemId.equals=" + invoiceItemId);

        // Get all the invoiceRoleList where invoiceItem equals to (invoiceItemId + 1)
        defaultInvoiceRoleShouldNotBeFound("invoiceItemId.equals=" + (invoiceItemId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            invoiceRoleRepository.saveAndFlush(invoiceRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        invoiceRole.setParty(party);
        invoiceRoleRepository.saveAndFlush(invoiceRole);
        Long partyId = party.getId();
        // Get all the invoiceRoleList where party equals to partyId
        defaultInvoiceRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the invoiceRoleList where party equals to (partyId + 1)
        defaultInvoiceRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            invoiceRoleRepository.saveAndFlush(invoiceRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        invoiceRole.setRoleType(roleType);
        invoiceRoleRepository.saveAndFlush(invoiceRole);
        Long roleTypeId = roleType.getId();
        // Get all the invoiceRoleList where roleType equals to roleTypeId
        defaultInvoiceRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the invoiceRoleList where roleType equals to (roleTypeId + 1)
        defaultInvoiceRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultInvoiceRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceRoleShouldBeFound(shouldBeFound);
        defaultInvoiceRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceRoleShouldBeFound(String filter) throws Exception {
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceRole.getId().intValue())));

        // Check, that the count call also returns 1
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceRoleShouldNotBeFound(String filter) throws Exception {
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceRole() throws Exception {
        // Get the invoiceRole
        restInvoiceRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceRole() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceRole
        InvoiceRole updatedInvoiceRole = invoiceRoleRepository.findById(invoiceRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceRole are not directly saved in db
        em.detach(updatedInvoiceRole);

        restInvoiceRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceRole))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceRoleToMatchAllProperties(updatedInvoiceRole);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceRoleWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceRole using partial update
        InvoiceRole partialUpdatedInvoiceRole = new InvoiceRole();
        partialUpdatedInvoiceRole.setId(invoiceRole.getId());

        restInvoiceRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceRole))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceRole, invoiceRole),
            getPersistedInvoiceRole(invoiceRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceRoleWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceRole using partial update
        InvoiceRole partialUpdatedInvoiceRole = new InvoiceRole();
        partialUpdatedInvoiceRole.setId(invoiceRole.getId());

        restInvoiceRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceRole))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceRoleUpdatableFieldsEquals(partialUpdatedInvoiceRole, getPersistedInvoiceRole(partialUpdatedInvoiceRole));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceRole() throws Exception {
        // Initialize the database
        insertedInvoiceRole = invoiceRoleRepository.saveAndFlush(invoiceRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceRole
        restInvoiceRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceRoleRepository.count();
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

    protected InvoiceRole getPersistedInvoiceRole(InvoiceRole invoiceRole) {
        return invoiceRoleRepository.findById(invoiceRole.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceRoleToMatchAllProperties(InvoiceRole expectedInvoiceRole) {
        assertInvoiceRoleAllPropertiesEquals(expectedInvoiceRole, getPersistedInvoiceRole(expectedInvoiceRole));
    }

    protected void assertPersistedInvoiceRoleToMatchUpdatableProperties(InvoiceRole expectedInvoiceRole) {
        assertInvoiceRoleAllUpdatablePropertiesEquals(expectedInvoiceRole, getPersistedInvoiceRole(expectedInvoiceRole));
    }
}
