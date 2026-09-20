package com.snp.web.rest;

import static com.snp.domain.InvoiceItemTypeMapAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InvoiceItemType;
import com.snp.domain.InvoiceItemTypeMap;
import com.snp.domain.InvoiceType;
import com.snp.repository.InvoiceItemTypeMapRepository;
import com.snp.service.InvoiceItemTypeMapService;
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
 * Integration tests for the {@link InvoiceItemTypeMapResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemTypeMapResourceIT {

    private static final String DEFAULT_INVOICE_ITEM_MAP_KEY = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ITEM_MAP_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-item-type-maps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceItemTypeMapRepository invoiceItemTypeMapRepository;

    @Mock
    private InvoiceItemTypeMapRepository invoiceItemTypeMapRepositoryMock;

    @Mock
    private InvoiceItemTypeMapService invoiceItemTypeMapServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemTypeMapMockMvc;

    private InvoiceItemTypeMap invoiceItemTypeMap;

    private InvoiceItemTypeMap insertedInvoiceItemTypeMap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItemTypeMap createEntity(EntityManager em) {
        InvoiceItemTypeMap invoiceItemTypeMap = new InvoiceItemTypeMap().invoiceItemMapKey(DEFAULT_INVOICE_ITEM_MAP_KEY);
        // Add required entity
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceType = InvoiceTypeResourceIT.createEntity();
            em.persist(invoiceType);
            em.flush();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        invoiceItemTypeMap.setInvoiceType(invoiceType);
        // Add required entity
        InvoiceItemType invoiceItemType;
        if (TestUtil.findAll(em, InvoiceItemType.class).isEmpty()) {
            invoiceItemType = InvoiceItemTypeResourceIT.createEntity();
            em.persist(invoiceItemType);
            em.flush();
        } else {
            invoiceItemType = TestUtil.findAll(em, InvoiceItemType.class).getFirst();
        }
        invoiceItemTypeMap.setInvoiceItemType(invoiceItemType);
        return invoiceItemTypeMap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItemTypeMap createUpdatedEntity(EntityManager em) {
        InvoiceItemTypeMap updatedInvoiceItemTypeMap = new InvoiceItemTypeMap().invoiceItemMapKey(UPDATED_INVOICE_ITEM_MAP_KEY);
        // Add required entity
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceType = InvoiceTypeResourceIT.createUpdatedEntity();
            em.persist(invoiceType);
            em.flush();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        updatedInvoiceItemTypeMap.setInvoiceType(invoiceType);
        // Add required entity
        InvoiceItemType invoiceItemType;
        if (TestUtil.findAll(em, InvoiceItemType.class).isEmpty()) {
            invoiceItemType = InvoiceItemTypeResourceIT.createUpdatedEntity();
            em.persist(invoiceItemType);
            em.flush();
        } else {
            invoiceItemType = TestUtil.findAll(em, InvoiceItemType.class).getFirst();
        }
        updatedInvoiceItemTypeMap.setInvoiceItemType(invoiceItemType);
        return updatedInvoiceItemTypeMap;
    }

    @BeforeEach
    void initTest() {
        invoiceItemTypeMap = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceItemTypeMap != null) {
            invoiceItemTypeMapRepository.delete(insertedInvoiceItemTypeMap);
            insertedInvoiceItemTypeMap = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceItemTypeMap
        var returnedInvoiceItemTypeMap = om.readValue(
            restInvoiceItemTypeMapMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemTypeMap)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceItemTypeMap.class
        );

        // Validate the InvoiceItemTypeMap in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceItemTypeMapUpdatableFieldsEquals(
            returnedInvoiceItemTypeMap,
            getPersistedInvoiceItemTypeMap(returnedInvoiceItemTypeMap)
        );

        insertedInvoiceItemTypeMap = returnedInvoiceItemTypeMap;
    }

    @Test
    @Transactional
    void createInvoiceItemTypeMapWithExistingId() throws Exception {
        // Create the InvoiceItemTypeMap with an existing ID
        invoiceItemTypeMap.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemTypeMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemTypeMap)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceItemMapKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceItemTypeMap.setInvoiceItemMapKey(null);

        // Create the InvoiceItemTypeMap, which fails.

        restInvoiceItemTypeMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemTypeMap)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMaps() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItemTypeMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceItemMapKey").value(hasItem(DEFAULT_INVOICE_ITEM_MAP_KEY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceItemTypeMapsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceItemTypeMapServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restInvoiceItemTypeMapMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceItemTypeMapServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoiceItemTypeMapsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceItemTypeMapServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restInvoiceItemTypeMapMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceItemTypeMapRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoiceItemTypeMap() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get the invoiceItemTypeMap
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItemTypeMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItemTypeMap.getId().intValue()))
            .andExpect(jsonPath("$.invoiceItemMapKey").value(DEFAULT_INVOICE_ITEM_MAP_KEY));
    }

    @Test
    @Transactional
    void getInvoiceItemTypeMapsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        Long id = invoiceItemTypeMap.getId();

        defaultInvoiceItemTypeMapFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInvoiceItemTypeMapFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInvoiceItemTypeMapFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemMapKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList where invoiceItemMapKey equals to
        defaultInvoiceItemTypeMapFiltering(
            "invoiceItemMapKey.equals=" + DEFAULT_INVOICE_ITEM_MAP_KEY,
            "invoiceItemMapKey.equals=" + UPDATED_INVOICE_ITEM_MAP_KEY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemMapKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList where invoiceItemMapKey in
        defaultInvoiceItemTypeMapFiltering(
            "invoiceItemMapKey.in=" + DEFAULT_INVOICE_ITEM_MAP_KEY + "," + UPDATED_INVOICE_ITEM_MAP_KEY,
            "invoiceItemMapKey.in=" + UPDATED_INVOICE_ITEM_MAP_KEY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemMapKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList where invoiceItemMapKey is not null
        defaultInvoiceItemTypeMapFiltering("invoiceItemMapKey.specified=true", "invoiceItemMapKey.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemMapKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList where invoiceItemMapKey contains
        defaultInvoiceItemTypeMapFiltering(
            "invoiceItemMapKey.contains=" + DEFAULT_INVOICE_ITEM_MAP_KEY,
            "invoiceItemMapKey.contains=" + UPDATED_INVOICE_ITEM_MAP_KEY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemMapKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        // Get all the invoiceItemTypeMapList where invoiceItemMapKey does not contain
        defaultInvoiceItemTypeMapFiltering(
            "invoiceItemMapKey.doesNotContain=" + UPDATED_INVOICE_ITEM_MAP_KEY,
            "invoiceItemMapKey.doesNotContain=" + DEFAULT_INVOICE_ITEM_MAP_KEY
        );
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceTypeIsEqualToSomething() throws Exception {
        InvoiceType invoiceType;
        if (TestUtil.findAll(em, InvoiceType.class).isEmpty()) {
            invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);
            invoiceType = InvoiceTypeResourceIT.createEntity();
        } else {
            invoiceType = TestUtil.findAll(em, InvoiceType.class).getFirst();
        }
        em.persist(invoiceType);
        em.flush();
        invoiceItemTypeMap.setInvoiceType(invoiceType);
        invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);
        Long invoiceTypeId = invoiceType.getId();
        // Get all the invoiceItemTypeMapList where invoiceType equals to invoiceTypeId
        defaultInvoiceItemTypeMapShouldBeFound("invoiceTypeId.equals=" + invoiceTypeId);

        // Get all the invoiceItemTypeMapList where invoiceType equals to (invoiceTypeId + 1)
        defaultInvoiceItemTypeMapShouldNotBeFound("invoiceTypeId.equals=" + (invoiceTypeId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemTypeMapsByInvoiceItemTypeIsEqualToSomething() throws Exception {
        InvoiceItemType invoiceItemType;
        if (TestUtil.findAll(em, InvoiceItemType.class).isEmpty()) {
            invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);
            invoiceItemType = InvoiceItemTypeResourceIT.createEntity();
        } else {
            invoiceItemType = TestUtil.findAll(em, InvoiceItemType.class).getFirst();
        }
        em.persist(invoiceItemType);
        em.flush();
        invoiceItemTypeMap.setInvoiceItemType(invoiceItemType);
        invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);
        Long invoiceItemTypeId = invoiceItemType.getId();
        // Get all the invoiceItemTypeMapList where invoiceItemType equals to invoiceItemTypeId
        defaultInvoiceItemTypeMapShouldBeFound("invoiceItemTypeId.equals=" + invoiceItemTypeId);

        // Get all the invoiceItemTypeMapList where invoiceItemType equals to (invoiceItemTypeId + 1)
        defaultInvoiceItemTypeMapShouldNotBeFound("invoiceItemTypeId.equals=" + (invoiceItemTypeId + 1));
    }

    private void defaultInvoiceItemTypeMapFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInvoiceItemTypeMapShouldBeFound(shouldBeFound);
        defaultInvoiceItemTypeMapShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceItemTypeMapShouldBeFound(String filter) throws Exception {
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItemTypeMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceItemMapKey").value(hasItem(DEFAULT_INVOICE_ITEM_MAP_KEY)));

        // Check, that the count call also returns 1
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceItemTypeMapShouldNotBeFound(String filter) throws Exception {
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceItemTypeMapMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItemTypeMap() throws Exception {
        // Get the invoiceItemTypeMap
        restInvoiceItemTypeMapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItemTypeMap() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemTypeMap
        InvoiceItemTypeMap updatedInvoiceItemTypeMap = invoiceItemTypeMapRepository.findById(invoiceItemTypeMap.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItemTypeMap are not directly saved in db
        em.detach(updatedInvoiceItemTypeMap);
        updatedInvoiceItemTypeMap.invoiceItemMapKey(UPDATED_INVOICE_ITEM_MAP_KEY);

        restInvoiceItemTypeMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceItemTypeMap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceItemTypeMap))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceItemTypeMapToMatchAllProperties(updatedInvoiceItemTypeMap);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemTypeMap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemTypeMap))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceItemTypeMap))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceItemTypeMap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemTypeMapWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemTypeMap using partial update
        InvoiceItemTypeMap partialUpdatedInvoiceItemTypeMap = new InvoiceItemTypeMap();
        partialUpdatedInvoiceItemTypeMap.setId(invoiceItemTypeMap.getId());

        partialUpdatedInvoiceItemTypeMap.invoiceItemMapKey(UPDATED_INVOICE_ITEM_MAP_KEY);

        restInvoiceItemTypeMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItemTypeMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItemTypeMap))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemTypeMap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemTypeMapUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceItemTypeMap, invoiceItemTypeMap),
            getPersistedInvoiceItemTypeMap(invoiceItemTypeMap)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemTypeMapWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceItemTypeMap using partial update
        InvoiceItemTypeMap partialUpdatedInvoiceItemTypeMap = new InvoiceItemTypeMap();
        partialUpdatedInvoiceItemTypeMap.setId(invoiceItemTypeMap.getId());

        partialUpdatedInvoiceItemTypeMap.invoiceItemMapKey(UPDATED_INVOICE_ITEM_MAP_KEY);

        restInvoiceItemTypeMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItemTypeMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceItemTypeMap))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItemTypeMap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceItemTypeMapUpdatableFieldsEquals(
            partialUpdatedInvoiceItemTypeMap,
            getPersistedInvoiceItemTypeMap(partialUpdatedInvoiceItemTypeMap)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItemTypeMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemTypeMap))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceItemTypeMap))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItemTypeMap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceItemTypeMap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemTypeMapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceItemTypeMap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItemTypeMap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItemTypeMap() throws Exception {
        // Initialize the database
        insertedInvoiceItemTypeMap = invoiceItemTypeMapRepository.saveAndFlush(invoiceItemTypeMap);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceItemTypeMap
        restInvoiceItemTypeMapMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItemTypeMap.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceItemTypeMapRepository.count();
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

    protected InvoiceItemTypeMap getPersistedInvoiceItemTypeMap(InvoiceItemTypeMap invoiceItemTypeMap) {
        return invoiceItemTypeMapRepository.findById(invoiceItemTypeMap.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceItemTypeMapToMatchAllProperties(InvoiceItemTypeMap expectedInvoiceItemTypeMap) {
        assertInvoiceItemTypeMapAllPropertiesEquals(expectedInvoiceItemTypeMap, getPersistedInvoiceItemTypeMap(expectedInvoiceItemTypeMap));
    }

    protected void assertPersistedInvoiceItemTypeMapToMatchUpdatableProperties(InvoiceItemTypeMap expectedInvoiceItemTypeMap) {
        assertInvoiceItemTypeMapAllUpdatablePropertiesEquals(
            expectedInvoiceItemTypeMap,
            getPersistedInvoiceItemTypeMap(expectedInvoiceItemTypeMap)
        );
    }
}
