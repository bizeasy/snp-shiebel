package com.snp.web.rest;

import static com.snp.domain.ShoppingListAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.ShoppingList;
import com.snp.domain.ShoppingListType;
import com.snp.domain.Status;
import com.snp.repository.ShoppingListRepository;
import com.snp.service.ShoppingListService;
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
 * Integration tests for the {@link ShoppingListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShoppingListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/shopping-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ShoppingListRepository shoppingListRepositoryMock;

    @Mock
    private ShoppingListService shoppingListServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingListMockMvc;

    private ShoppingList shoppingList;

    private ShoppingList insertedShoppingList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingList createEntity(EntityManager em) {
        ShoppingList shoppingList = new ShoppingList().name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        shoppingList.setParty(party);
        return shoppingList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingList createUpdatedEntity(EntityManager em) {
        ShoppingList updatedShoppingList = new ShoppingList().name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedShoppingList.setParty(party);
        return updatedShoppingList;
    }

    @BeforeEach
    void initTest() {
        shoppingList = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShoppingList != null) {
            shoppingListRepository.delete(insertedShoppingList);
            insertedShoppingList = null;
        }
    }

    @Test
    @Transactional
    void createShoppingList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingList
        var returnedShoppingList = om.readValue(
            restShoppingListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingList.class
        );

        // Validate the ShoppingList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShoppingListUpdatableFieldsEquals(returnedShoppingList, getPersistedShoppingList(returnedShoppingList));

        insertedShoppingList = returnedShoppingList;
    }

    @Test
    @Transactional
    void createShoppingListWithExistingId() throws Exception {
        // Create the ShoppingList with an existing ID
        shoppingList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingList)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingList.setName(null);

        // Create the ShoppingList, which fails.

        restShoppingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingList)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingLists() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shoppingListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shoppingListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shoppingListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shoppingListRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShoppingList() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get the shoppingList
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getShoppingListsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        Long id = shoppingList.getId();

        defaultShoppingListFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShoppingListFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShoppingListFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShoppingListsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where name equals to
        defaultShoppingListFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where name in
        defaultShoppingListFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where name is not null
        defaultShoppingListFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where name contains
        defaultShoppingListFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where name does not contain
        defaultShoppingListFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllShoppingListsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where isActive equals to
        defaultShoppingListFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllShoppingListsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where isActive in
        defaultShoppingListFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllShoppingListsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList where isActive is not null
        defaultShoppingListFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListsByShoppingListTypeIsEqualToSomething() throws Exception {
        ShoppingListType shoppingListType;
        if (TestUtil.findAll(em, ShoppingListType.class).isEmpty()) {
            shoppingListRepository.saveAndFlush(shoppingList);
            shoppingListType = ShoppingListTypeResourceIT.createEntity();
        } else {
            shoppingListType = TestUtil.findAll(em, ShoppingListType.class).getFirst();
        }
        em.persist(shoppingListType);
        em.flush();
        shoppingList.setShoppingListType(shoppingListType);
        shoppingListRepository.saveAndFlush(shoppingList);
        Long shoppingListTypeId = shoppingListType.getId();
        // Get all the shoppingListList where shoppingListType equals to shoppingListTypeId
        defaultShoppingListShouldBeFound("shoppingListTypeId.equals=" + shoppingListTypeId);

        // Get all the shoppingListList where shoppingListType equals to (shoppingListTypeId + 1)
        defaultShoppingListShouldNotBeFound("shoppingListTypeId.equals=" + (shoppingListTypeId + 1));
    }

    @Test
    @Transactional
    void getAllShoppingListsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            shoppingListRepository.saveAndFlush(shoppingList);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        shoppingList.setParty(party);
        shoppingListRepository.saveAndFlush(shoppingList);
        Long partyId = party.getId();
        // Get all the shoppingListList where party equals to partyId
        defaultShoppingListShouldBeFound("partyId.equals=" + partyId);

        // Get all the shoppingListList where party equals to (partyId + 1)
        defaultShoppingListShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllShoppingListsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            shoppingListRepository.saveAndFlush(shoppingList);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        shoppingList.setStatus(status);
        shoppingListRepository.saveAndFlush(shoppingList);
        Long statusId = status.getId();
        // Get all the shoppingListList where status equals to statusId
        defaultShoppingListShouldBeFound("statusId.equals=" + statusId);

        // Get all the shoppingListList where status equals to (statusId + 1)
        defaultShoppingListShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultShoppingListFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShoppingListShouldBeFound(shouldBeFound);
        defaultShoppingListShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShoppingListShouldBeFound(String filter) throws Exception {
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShoppingListShouldNotBeFound(String filter) throws Exception {
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShoppingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShoppingList() throws Exception {
        // Get the shoppingList
        restShoppingListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingList() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingList
        ShoppingList updatedShoppingList = shoppingListRepository.findById(shoppingList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingList are not directly saved in db
        em.detach(updatedShoppingList);
        updatedShoppingList.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restShoppingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShoppingList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShoppingList))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingListToMatchAllProperties(updatedShoppingList);
    }

    @Test
    @Transactional
    void putNonExistingShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingListWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingList using partial update
        ShoppingList partialUpdatedShoppingList = new ShoppingList();
        partialUpdatedShoppingList.setId(shoppingList.getId());

        partialUpdatedShoppingList.name(UPDATED_NAME);

        restShoppingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingList))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingList, shoppingList),
            getPersistedShoppingList(shoppingList)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingListWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingList using partial update
        ShoppingList partialUpdatedShoppingList = new ShoppingList();
        partialUpdatedShoppingList.setId(shoppingList.getId());

        partialUpdatedShoppingList.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restShoppingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingList))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListUpdatableFieldsEquals(partialUpdatedShoppingList, getPersistedShoppingList(partialUpdatedShoppingList));
    }

    @Test
    @Transactional
    void patchNonExistingShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingList() throws Exception {
        // Initialize the database
        insertedShoppingList = shoppingListRepository.saveAndFlush(shoppingList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingList
        restShoppingListMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingListRepository.count();
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

    protected ShoppingList getPersistedShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.findById(shoppingList.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingListToMatchAllProperties(ShoppingList expectedShoppingList) {
        assertShoppingListAllPropertiesEquals(expectedShoppingList, getPersistedShoppingList(expectedShoppingList));
    }

    protected void assertPersistedShoppingListToMatchUpdatableProperties(ShoppingList expectedShoppingList) {
        assertShoppingListAllUpdatablePropertiesEquals(expectedShoppingList, getPersistedShoppingList(expectedShoppingList));
    }
}
