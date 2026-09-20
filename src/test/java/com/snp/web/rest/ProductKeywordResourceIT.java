package com.snp.web.rest;

import static com.snp.domain.ProductKeywordAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.KeywordType;
import com.snp.domain.Product;
import com.snp.domain.ProductKeyword;
import com.snp.repository.ProductKeywordRepository;
import com.snp.service.ProductKeywordService;
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
 * Integration tests for the {@link ProductKeywordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductKeywordResourceIT {

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_RELEVANCY_WEIGHT = 1;
    private static final Integer UPDATED_RELEVANCY_WEIGHT = 2;
    private static final Integer SMALLER_RELEVANCY_WEIGHT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-keywords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductKeywordRepository productKeywordRepository;

    @Mock
    private ProductKeywordRepository productKeywordRepositoryMock;

    @Mock
    private ProductKeywordService productKeywordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductKeywordMockMvc;

    private ProductKeyword productKeyword;

    private ProductKeyword insertedProductKeyword;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductKeyword createEntity(EntityManager em) {
        ProductKeyword productKeyword = new ProductKeyword().keyword(DEFAULT_KEYWORD).relevancyWeight(DEFAULT_RELEVANCY_WEIGHT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productKeyword.setProduct(product);
        return productKeyword;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductKeyword createUpdatedEntity(EntityManager em) {
        ProductKeyword updatedProductKeyword = new ProductKeyword().keyword(UPDATED_KEYWORD).relevancyWeight(UPDATED_RELEVANCY_WEIGHT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductKeyword.setProduct(product);
        return updatedProductKeyword;
    }

    @BeforeEach
    void initTest() {
        productKeyword = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductKeyword != null) {
            productKeywordRepository.delete(insertedProductKeyword);
            insertedProductKeyword = null;
        }
    }

    @Test
    @Transactional
    void createProductKeyword() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductKeyword
        var returnedProductKeyword = om.readValue(
            restProductKeywordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productKeyword)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductKeyword.class
        );

        // Validate the ProductKeyword in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductKeywordUpdatableFieldsEquals(returnedProductKeyword, getPersistedProductKeyword(returnedProductKeyword));

        insertedProductKeyword = returnedProductKeyword;
    }

    @Test
    @Transactional
    void createProductKeywordWithExistingId() throws Exception {
        // Create the ProductKeyword with an existing ID
        productKeyword.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductKeywordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productKeyword)))
            .andExpect(status().isBadRequest());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeywordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productKeyword.setKeyword(null);

        // Create the ProductKeyword, which fails.

        restProductKeywordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productKeyword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductKeywords() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].relevancyWeight").value(hasItem(DEFAULT_RELEVANCY_WEIGHT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductKeywordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productKeywordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductKeywordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productKeywordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductKeywordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productKeywordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductKeywordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productKeywordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductKeyword() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get the productKeyword
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL_ID, productKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productKeyword.getId().intValue()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.relevancyWeight").value(DEFAULT_RELEVANCY_WEIGHT));
    }

    @Test
    @Transactional
    void getProductKeywordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        Long id = productKeyword.getId();

        defaultProductKeywordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductKeywordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductKeywordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where keyword equals to
        defaultProductKeywordFiltering("keyword.equals=" + DEFAULT_KEYWORD, "keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where keyword in
        defaultProductKeywordFiltering("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD, "keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where keyword is not null
        defaultProductKeywordFiltering("keyword.specified=true", "keyword.specified=false");
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordContainsSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where keyword contains
        defaultProductKeywordFiltering("keyword.contains=" + DEFAULT_KEYWORD, "keyword.contains=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where keyword does not contain
        defaultProductKeywordFiltering("keyword.doesNotContain=" + UPDATED_KEYWORD, "keyword.doesNotContain=" + DEFAULT_KEYWORD);
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight equals to
        defaultProductKeywordFiltering(
            "relevancyWeight.equals=" + DEFAULT_RELEVANCY_WEIGHT,
            "relevancyWeight.equals=" + UPDATED_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight in
        defaultProductKeywordFiltering(
            "relevancyWeight.in=" + DEFAULT_RELEVANCY_WEIGHT + "," + UPDATED_RELEVANCY_WEIGHT,
            "relevancyWeight.in=" + UPDATED_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight is not null
        defaultProductKeywordFiltering("relevancyWeight.specified=true", "relevancyWeight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight is greater than or equal to
        defaultProductKeywordFiltering(
            "relevancyWeight.greaterThanOrEqual=" + DEFAULT_RELEVANCY_WEIGHT,
            "relevancyWeight.greaterThanOrEqual=" + UPDATED_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight is less than or equal to
        defaultProductKeywordFiltering(
            "relevancyWeight.lessThanOrEqual=" + DEFAULT_RELEVANCY_WEIGHT,
            "relevancyWeight.lessThanOrEqual=" + SMALLER_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight is less than
        defaultProductKeywordFiltering(
            "relevancyWeight.lessThan=" + UPDATED_RELEVANCY_WEIGHT,
            "relevancyWeight.lessThan=" + DEFAULT_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByRelevancyWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        // Get all the productKeywordList where relevancyWeight is greater than
        defaultProductKeywordFiltering(
            "relevancyWeight.greaterThan=" + SMALLER_RELEVANCY_WEIGHT,
            "relevancyWeight.greaterThan=" + DEFAULT_RELEVANCY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductKeywordsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productKeywordRepository.saveAndFlush(productKeyword);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productKeyword.setProduct(product);
        productKeywordRepository.saveAndFlush(productKeyword);
        Long productId = product.getId();
        // Get all the productKeywordList where product equals to productId
        defaultProductKeywordShouldBeFound("productId.equals=" + productId);

        // Get all the productKeywordList where product equals to (productId + 1)
        defaultProductKeywordShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductKeywordsByKeywordTypeIsEqualToSomething() throws Exception {
        KeywordType keywordType;
        if (TestUtil.findAll(em, KeywordType.class).isEmpty()) {
            productKeywordRepository.saveAndFlush(productKeyword);
            keywordType = KeywordTypeResourceIT.createEntity();
        } else {
            keywordType = TestUtil.findAll(em, KeywordType.class).getFirst();
        }
        em.persist(keywordType);
        em.flush();
        productKeyword.setKeywordType(keywordType);
        productKeywordRepository.saveAndFlush(productKeyword);
        Long keywordTypeId = keywordType.getId();
        // Get all the productKeywordList where keywordType equals to keywordTypeId
        defaultProductKeywordShouldBeFound("keywordTypeId.equals=" + keywordTypeId);

        // Get all the productKeywordList where keywordType equals to (keywordTypeId + 1)
        defaultProductKeywordShouldNotBeFound("keywordTypeId.equals=" + (keywordTypeId + 1));
    }

    private void defaultProductKeywordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductKeywordShouldBeFound(shouldBeFound);
        defaultProductKeywordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductKeywordShouldBeFound(String filter) throws Exception {
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].relevancyWeight").value(hasItem(DEFAULT_RELEVANCY_WEIGHT)));

        // Check, that the count call also returns 1
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductKeywordShouldNotBeFound(String filter) throws Exception {
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductKeywordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductKeyword() throws Exception {
        // Get the productKeyword
        restProductKeywordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductKeyword() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productKeyword
        ProductKeyword updatedProductKeyword = productKeywordRepository.findById(productKeyword.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductKeyword are not directly saved in db
        em.detach(updatedProductKeyword);
        updatedProductKeyword.keyword(UPDATED_KEYWORD).relevancyWeight(UPDATED_RELEVANCY_WEIGHT);

        restProductKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductKeyword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductKeyword))
            )
            .andExpect(status().isOk());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductKeywordToMatchAllProperties(updatedProductKeyword);
    }

    @Test
    @Transactional
    void putNonExistingProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productKeyword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productKeyword))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productKeyword))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productKeyword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductKeywordWithPatch() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productKeyword using partial update
        ProductKeyword partialUpdatedProductKeyword = new ProductKeyword();
        partialUpdatedProductKeyword.setId(productKeyword.getId());

        partialUpdatedProductKeyword.keyword(UPDATED_KEYWORD);

        restProductKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductKeyword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductKeyword))
            )
            .andExpect(status().isOk());

        // Validate the ProductKeyword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductKeywordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductKeyword, productKeyword),
            getPersistedProductKeyword(productKeyword)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductKeywordWithPatch() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productKeyword using partial update
        ProductKeyword partialUpdatedProductKeyword = new ProductKeyword();
        partialUpdatedProductKeyword.setId(productKeyword.getId());

        partialUpdatedProductKeyword.keyword(UPDATED_KEYWORD).relevancyWeight(UPDATED_RELEVANCY_WEIGHT);

        restProductKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductKeyword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductKeyword))
            )
            .andExpect(status().isOk());

        // Validate the ProductKeyword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductKeywordUpdatableFieldsEquals(partialUpdatedProductKeyword, getPersistedProductKeyword(partialUpdatedProductKeyword));
    }

    @Test
    @Transactional
    void patchNonExistingProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productKeyword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productKeyword))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productKeyword))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productKeyword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductKeywordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productKeyword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductKeyword() throws Exception {
        // Initialize the database
        insertedProductKeyword = productKeywordRepository.saveAndFlush(productKeyword);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productKeyword
        restProductKeywordMockMvc
            .perform(delete(ENTITY_API_URL_ID, productKeyword.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productKeywordRepository.count();
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

    protected ProductKeyword getPersistedProductKeyword(ProductKeyword productKeyword) {
        return productKeywordRepository.findById(productKeyword.getId()).orElseThrow();
    }

    protected void assertPersistedProductKeywordToMatchAllProperties(ProductKeyword expectedProductKeyword) {
        assertProductKeywordAllPropertiesEquals(expectedProductKeyword, getPersistedProductKeyword(expectedProductKeyword));
    }

    protected void assertPersistedProductKeywordToMatchUpdatableProperties(ProductKeyword expectedProductKeyword) {
        assertProductKeywordAllUpdatablePropertiesEquals(expectedProductKeyword, getPersistedProductKeyword(expectedProductKeyword));
    }
}
