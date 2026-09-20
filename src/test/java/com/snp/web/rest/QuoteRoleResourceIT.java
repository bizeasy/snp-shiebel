package com.snp.web.rest;

import static com.snp.domain.QuoteRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.Quote;
import com.snp.domain.QuoteItem;
import com.snp.domain.QuoteRole;
import com.snp.domain.RoleType;
import com.snp.repository.QuoteRoleRepository;
import com.snp.service.QuoteRoleService;
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
 * Integration tests for the {@link QuoteRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuoteRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/quote-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteRoleRepository quoteRoleRepository;

    @Mock
    private QuoteRoleRepository quoteRoleRepositoryMock;

    @Mock
    private QuoteRoleService quoteRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteRoleMockMvc;

    private QuoteRole quoteRole;

    private QuoteRole insertedQuoteRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteRole createEntity(EntityManager em) {
        QuoteRole quoteRole = new QuoteRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        quoteRole.setParty(party);
        return quoteRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteRole createUpdatedEntity(EntityManager em) {
        QuoteRole updatedQuoteRole = new QuoteRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedQuoteRole.setParty(party);
        return updatedQuoteRole;
    }

    @BeforeEach
    void initTest() {
        quoteRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteRole != null) {
            quoteRoleRepository.delete(insertedQuoteRole);
            insertedQuoteRole = null;
        }
    }

    @Test
    @Transactional
    void createQuoteRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteRole
        var returnedQuoteRole = om.readValue(
            restQuoteRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteRole.class
        );

        // Validate the QuoteRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteRoleUpdatableFieldsEquals(returnedQuoteRole, getPersistedQuoteRole(returnedQuoteRole));

        insertedQuoteRole = returnedQuoteRole;
    }

    @Test
    @Transactional
    void createQuoteRoleWithExistingId() throws Exception {
        // Create the QuoteRole with an existing ID
        quoteRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteRole)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuoteRoles() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        // Get all the quoteRoleList
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(quoteRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quoteRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuoteRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quoteRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuoteRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quoteRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuoteRole() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        // Get the quoteRole
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getQuoteRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        Long id = quoteRole.getId();

        defaultQuoteRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuoteRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuoteRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuoteRolesByQuoteIsEqualToSomething() throws Exception {
        Quote quote;
        if (TestUtil.findAll(em, Quote.class).isEmpty()) {
            quoteRoleRepository.saveAndFlush(quoteRole);
            quote = QuoteResourceIT.createEntity(em);
        } else {
            quote = TestUtil.findAll(em, Quote.class).getFirst();
        }
        em.persist(quote);
        em.flush();
        quoteRole.setQuote(quote);
        quoteRoleRepository.saveAndFlush(quoteRole);
        Long quoteId = quote.getId();
        // Get all the quoteRoleList where quote equals to quoteId
        defaultQuoteRoleShouldBeFound("quoteId.equals=" + quoteId);

        // Get all the quoteRoleList where quote equals to (quoteId + 1)
        defaultQuoteRoleShouldNotBeFound("quoteId.equals=" + (quoteId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteRolesByQuoteItemIsEqualToSomething() throws Exception {
        QuoteItem quoteItem;
        if (TestUtil.findAll(em, QuoteItem.class).isEmpty()) {
            quoteRoleRepository.saveAndFlush(quoteRole);
            quoteItem = QuoteItemResourceIT.createEntity(em);
        } else {
            quoteItem = TestUtil.findAll(em, QuoteItem.class).getFirst();
        }
        em.persist(quoteItem);
        em.flush();
        quoteRole.setQuoteItem(quoteItem);
        quoteRoleRepository.saveAndFlush(quoteRole);
        Long quoteItemId = quoteItem.getId();
        // Get all the quoteRoleList where quoteItem equals to quoteItemId
        defaultQuoteRoleShouldBeFound("quoteItemId.equals=" + quoteItemId);

        // Get all the quoteRoleList where quoteItem equals to (quoteItemId + 1)
        defaultQuoteRoleShouldNotBeFound("quoteItemId.equals=" + (quoteItemId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            quoteRoleRepository.saveAndFlush(quoteRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        quoteRole.setParty(party);
        quoteRoleRepository.saveAndFlush(quoteRole);
        Long partyId = party.getId();
        // Get all the quoteRoleList where party equals to partyId
        defaultQuoteRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the quoteRoleList where party equals to (partyId + 1)
        defaultQuoteRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllQuoteRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            quoteRoleRepository.saveAndFlush(quoteRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        quoteRole.setRoleType(roleType);
        quoteRoleRepository.saveAndFlush(quoteRole);
        Long roleTypeId = roleType.getId();
        // Get all the quoteRoleList where roleType equals to roleTypeId
        defaultQuoteRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the quoteRoleList where roleType equals to (roleTypeId + 1)
        defaultQuoteRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultQuoteRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteRoleShouldBeFound(shouldBeFound);
        defaultQuoteRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteRoleShouldBeFound(String filter) throws Exception {
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteRole.getId().intValue())));

        // Check, that the count call also returns 1
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteRoleShouldNotBeFound(String filter) throws Exception {
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuoteRole() throws Exception {
        // Get the quoteRole
        restQuoteRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteRole() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteRole
        QuoteRole updatedQuoteRole = quoteRoleRepository.findById(quoteRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteRole are not directly saved in db
        em.detach(updatedQuoteRole);

        restQuoteRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteRole))
            )
            .andExpect(status().isOk());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteRoleToMatchAllProperties(updatedQuoteRole);
    }

    @Test
    @Transactional
    void putNonExistingQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteRole.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteRoleWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteRole using partial update
        QuoteRole partialUpdatedQuoteRole = new QuoteRole();
        partialUpdatedQuoteRole.setId(quoteRole.getId());

        restQuoteRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteRole))
            )
            .andExpect(status().isOk());

        // Validate the QuoteRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteRole, quoteRole),
            getPersistedQuoteRole(quoteRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteRoleWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteRole using partial update
        QuoteRole partialUpdatedQuoteRole = new QuoteRole();
        partialUpdatedQuoteRole.setId(quoteRole.getId());

        restQuoteRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteRole))
            )
            .andExpect(status().isOk());

        // Validate the QuoteRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteRoleUpdatableFieldsEquals(partialUpdatedQuoteRole, getPersistedQuoteRole(partialUpdatedQuoteRole));
    }

    @Test
    @Transactional
    void patchNonExistingQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteRole() throws Exception {
        // Initialize the database
        insertedQuoteRole = quoteRoleRepository.saveAndFlush(quoteRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteRole
        restQuoteRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteRoleRepository.count();
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

    protected QuoteRole getPersistedQuoteRole(QuoteRole quoteRole) {
        return quoteRoleRepository.findById(quoteRole.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteRoleToMatchAllProperties(QuoteRole expectedQuoteRole) {
        assertQuoteRoleAllPropertiesEquals(expectedQuoteRole, getPersistedQuoteRole(expectedQuoteRole));
    }

    protected void assertPersistedQuoteRoleToMatchUpdatableProperties(QuoteRole expectedQuoteRole) {
        assertQuoteRoleAllUpdatablePropertiesEquals(expectedQuoteRole, getPersistedQuoteRole(expectedQuoteRole));
    }
}
