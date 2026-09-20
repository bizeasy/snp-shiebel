package com.snp.web.rest;

import static com.snp.domain.ProductStoreAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.ProductStore;
import com.snp.repository.ProductStoreRepository;
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
 * Integration tests for the {@link ProductStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductStoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductStoreRepository productStoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductStoreMockMvc;

    private ProductStore productStore;

    private ProductStore insertedProductStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStore createEntity() {
        return new ProductStore().name(DEFAULT_NAME).title(DEFAULT_TITLE).postalAddress(DEFAULT_POSTAL_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStore createUpdatedEntity() {
        return new ProductStore().name(UPDATED_NAME).title(UPDATED_TITLE).postalAddress(UPDATED_POSTAL_ADDRESS);
    }

    @BeforeEach
    void initTest() {
        productStore = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductStore != null) {
            productStoreRepository.delete(insertedProductStore);
            insertedProductStore = null;
        }
    }

    @Test
    @Transactional
    void createProductStore() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductStore
        var returnedProductStore = om.readValue(
            restProductStoreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStore)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductStore.class
        );

        // Validate the ProductStore in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductStoreUpdatableFieldsEquals(returnedProductStore, getPersistedProductStore(returnedProductStore));

        insertedProductStore = returnedProductStore;
    }

    @Test
    @Transactional
    void createProductStoreWithExistingId() throws Exception {
        // Create the ProductStore with an existing ID
        productStore.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStore)))
            .andExpect(status().isBadRequest());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productStore.setName(null);

        // Create the ProductStore, which fails.

        restProductStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStore)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductStores() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)));
    }

    @Test
    @Transactional
    void getProductStore() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get the productStore
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, productStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productStore.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.postalAddress").value(DEFAULT_POSTAL_ADDRESS));
    }

    @Test
    @Transactional
    void getProductStoresByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        Long id = productStore.getId();

        defaultProductStoreFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductStoreFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductStoreFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductStoresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where name equals to
        defaultProductStoreFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductStoresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where name in
        defaultProductStoreFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductStoresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where name is not null
        defaultProductStoreFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductStoresByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where name contains
        defaultProductStoreFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductStoresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where name does not contain
        defaultProductStoreFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductStoresByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where title equals to
        defaultProductStoreFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductStoresByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where title in
        defaultProductStoreFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductStoresByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where title is not null
        defaultProductStoreFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllProductStoresByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where title contains
        defaultProductStoreFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductStoresByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        // Get all the productStoreList where title does not contain
        defaultProductStoreFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllProductStoresByOwnerIsEqualToSomething() throws Exception {
        Party owner;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            productStoreRepository.saveAndFlush(productStore);
            owner = PartyResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(owner);
        em.flush();
        productStore.setOwner(owner);
        productStoreRepository.saveAndFlush(productStore);
        Long ownerId = owner.getId();
        // Get all the productStoreList where owner equals to ownerId
        defaultProductStoreShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the productStoreList where owner equals to (ownerId + 1)
        defaultProductStoreShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllProductStoresByAdminIsEqualToSomething() throws Exception {
        Party admin;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            productStoreRepository.saveAndFlush(productStore);
            admin = PartyResourceIT.createEntity();
        } else {
            admin = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(admin);
        em.flush();
        productStore.setAdmin(admin);
        productStoreRepository.saveAndFlush(productStore);
        Long adminId = admin.getId();
        // Get all the productStoreList where admin equals to adminId
        defaultProductStoreShouldBeFound("adminId.equals=" + adminId);

        // Get all the productStoreList where admin equals to (adminId + 1)
        defaultProductStoreShouldNotBeFound("adminId.equals=" + (adminId + 1));
    }

    private void defaultProductStoreFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductStoreShouldBeFound(shouldBeFound);
        defaultProductStoreShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductStoreShouldBeFound(String filter) throws Exception {
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)));

        // Check, that the count call also returns 1
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductStoreShouldNotBeFound(String filter) throws Exception {
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductStore() throws Exception {
        // Get the productStore
        restProductStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductStore() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStore
        ProductStore updatedProductStore = productStoreRepository.findById(productStore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductStore are not directly saved in db
        em.detach(updatedProductStore);
        updatedProductStore.name(UPDATED_NAME).title(UPDATED_TITLE).postalAddress(UPDATED_POSTAL_ADDRESS);

        restProductStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductStore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductStore))
            )
            .andExpect(status().isOk());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductStoreToMatchAllProperties(updatedProductStore);
    }

    @Test
    @Transactional
    void putNonExistingProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productStore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductStoreWithPatch() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStore using partial update
        ProductStore partialUpdatedProductStore = new ProductStore();
        partialUpdatedProductStore.setId(productStore.getId());

        partialUpdatedProductStore.postalAddress(UPDATED_POSTAL_ADDRESS);

        restProductStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductStore))
            )
            .andExpect(status().isOk());

        // Validate the ProductStore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductStoreUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductStore, productStore),
            getPersistedProductStore(productStore)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductStoreWithPatch() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStore using partial update
        ProductStore partialUpdatedProductStore = new ProductStore();
        partialUpdatedProductStore.setId(productStore.getId());

        partialUpdatedProductStore.name(UPDATED_NAME).title(UPDATED_TITLE).postalAddress(UPDATED_POSTAL_ADDRESS);

        restProductStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductStore))
            )
            .andExpect(status().isOk());

        // Validate the ProductStore in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductStoreUpdatableFieldsEquals(partialUpdatedProductStore, getPersistedProductStore(partialUpdatedProductStore));
    }

    @Test
    @Transactional
    void patchNonExistingProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productStore))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductStore() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStoreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productStore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductStore in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductStore() throws Exception {
        // Initialize the database
        insertedProductStore = productStoreRepository.saveAndFlush(productStore);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productStore
        restProductStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, productStore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productStoreRepository.count();
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

    protected ProductStore getPersistedProductStore(ProductStore productStore) {
        return productStoreRepository.findById(productStore.getId()).orElseThrow();
    }

    protected void assertPersistedProductStoreToMatchAllProperties(ProductStore expectedProductStore) {
        assertProductStoreAllPropertiesEquals(expectedProductStore, getPersistedProductStore(expectedProductStore));
    }

    protected void assertPersistedProductStoreToMatchUpdatableProperties(ProductStore expectedProductStore) {
        assertProductStoreAllUpdatablePropertiesEquals(expectedProductStore, getPersistedProductStore(expectedProductStore));
    }
}
