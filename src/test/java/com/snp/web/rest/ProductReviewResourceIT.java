package com.snp.web.rest;

import static com.snp.domain.ProductReviewAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderItem;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.ProductReview;
import com.snp.repository.ProductReviewRepository;
import com.snp.service.ProductReviewService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link ProductReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductReviewResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_RATING_INFO = "AAAAAAAAAA";
    private static final String UPDATED_RATING_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/product-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Mock
    private ProductReviewRepository productReviewRepositoryMock;

    @Mock
    private ProductReviewService productReviewServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReviewMockMvc;

    private ProductReview productReview;

    private ProductReview insertedProductReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createEntity(EntityManager em) {
        ProductReview productReview = new ProductReview()
            .rating(DEFAULT_RATING)
            .ratingInfo(DEFAULT_RATING_INFO)
            .message(DEFAULT_MESSAGE)
            .reviewDate(DEFAULT_REVIEW_DATE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productReview.setProduct(product);
        return productReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createUpdatedEntity(EntityManager em) {
        ProductReview updatedProductReview = new ProductReview()
            .rating(UPDATED_RATING)
            .ratingInfo(UPDATED_RATING_INFO)
            .message(UPDATED_MESSAGE)
            .reviewDate(UPDATED_REVIEW_DATE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductReview.setProduct(product);
        return updatedProductReview;
    }

    @BeforeEach
    void initTest() {
        productReview = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductReview != null) {
            productReviewRepository.delete(insertedProductReview);
            insertedProductReview = null;
        }
    }

    @Test
    @Transactional
    void createProductReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductReview
        var returnedProductReview = om.readValue(
            restProductReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReview)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductReview.class
        );

        // Validate the ProductReview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductReviewUpdatableFieldsEquals(returnedProductReview, getPersistedProductReview(returnedProductReview));

        insertedProductReview = returnedProductReview;
    }

    @Test
    @Transactional
    void createProductReviewWithExistingId() throws Exception {
        // Create the ProductReview with an existing ID
        productReview.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReview)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductReviews() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].ratingInfo").value(hasItem(DEFAULT_RATING_INFO)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductReviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productReviewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductReviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productReviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductReview() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get the productReview
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, productReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productReview.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.ratingInfo").value(DEFAULT_RATING_INFO))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductReviewsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        Long id = productReview.getId();

        defaultProductReviewFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductReviewFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductReviewFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating equals to
        defaultProductReviewFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating in
        defaultProductReviewFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating is not null
        defaultProductReviewFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating is greater than or equal to
        defaultProductReviewFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating is less than or equal to
        defaultProductReviewFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating is less than
        defaultProductReviewFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllProductReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where rating is greater than
        defaultProductReviewFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllProductReviewsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where message equals to
        defaultProductReviewFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductReviewsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where message in
        defaultProductReviewFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductReviewsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where message is not null
        defaultProductReviewFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReviewsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where message contains
        defaultProductReviewFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductReviewsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where message does not contain
        defaultProductReviewFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllProductReviewsByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where reviewDate equals to
        defaultProductReviewFiltering("reviewDate.equals=" + DEFAULT_REVIEW_DATE, "reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    void getAllProductReviewsByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where reviewDate in
        defaultProductReviewFiltering(
            "reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE,
            "reviewDate.in=" + UPDATED_REVIEW_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductReviewsByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList where reviewDate is not null
        defaultProductReviewFiltering("reviewDate.specified=true", "reviewDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReviewsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productReviewRepository.saveAndFlush(productReview);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productReview.setProduct(product);
        productReviewRepository.saveAndFlush(productReview);
        Long productId = product.getId();
        // Get all the productReviewList where product equals to productId
        defaultProductReviewShouldBeFound("productId.equals=" + productId);

        // Get all the productReviewList where product equals to (productId + 1)
        defaultProductReviewShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductReviewsByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            productReviewRepository.saveAndFlush(productReview);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        productReview.setParty(party);
        productReviewRepository.saveAndFlush(productReview);
        Long partyId = party.getId();
        // Get all the productReviewList where party equals to partyId
        defaultProductReviewShouldBeFound("partyId.equals=" + partyId);

        // Get all the productReviewList where party equals to (partyId + 1)
        defaultProductReviewShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllProductReviewsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            productReviewRepository.saveAndFlush(productReview);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        productReview.setOrderItem(orderItem);
        productReviewRepository.saveAndFlush(productReview);
        Long orderItemId = orderItem.getId();
        // Get all the productReviewList where orderItem equals to orderItemId
        defaultProductReviewShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the productReviewList where orderItem equals to (orderItemId + 1)
        defaultProductReviewShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    private void defaultProductReviewFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductReviewShouldBeFound(shouldBeFound);
        defaultProductReviewShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductReviewShouldBeFound(String filter) throws Exception {
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].ratingInfo").value(hasItem(DEFAULT_RATING_INFO)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())));

        // Check, that the count call also returns 1
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductReviewShouldNotBeFound(String filter) throws Exception {
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductReview() throws Exception {
        // Get the productReview
        restProductReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductReview() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReview
        ProductReview updatedProductReview = productReviewRepository.findById(productReview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductReview are not directly saved in db
        em.detach(updatedProductReview);
        updatedProductReview
            .rating(UPDATED_RATING)
            .ratingInfo(UPDATED_RATING_INFO)
            .message(UPDATED_MESSAGE)
            .reviewDate(UPDATED_REVIEW_DATE);

        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductReviewToMatchAllProperties(updatedProductReview);
    }

    @Test
    @Transactional
    void putNonExistingProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview.rating(UPDATED_RATING).ratingInfo(UPDATED_RATING_INFO);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductReview, productReview),
            getPersistedProductReview(productReview)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview
            .rating(UPDATED_RATING)
            .ratingInfo(UPDATED_RATING_INFO)
            .message(UPDATED_MESSAGE)
            .reviewDate(UPDATED_REVIEW_DATE);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReviewUpdatableFieldsEquals(partialUpdatedProductReview, getPersistedProductReview(partialUpdatedProductReview));
    }

    @Test
    @Transactional
    void patchNonExistingProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductReview() throws Exception {
        // Initialize the database
        insertedProductReview = productReviewRepository.saveAndFlush(productReview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productReview
        restProductReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, productReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productReviewRepository.count();
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

    protected ProductReview getPersistedProductReview(ProductReview productReview) {
        return productReviewRepository.findById(productReview.getId()).orElseThrow();
    }

    protected void assertPersistedProductReviewToMatchAllProperties(ProductReview expectedProductReview) {
        assertProductReviewAllPropertiesEquals(expectedProductReview, getPersistedProductReview(expectedProductReview));
    }

    protected void assertPersistedProductReviewToMatchUpdatableProperties(ProductReview expectedProductReview) {
        assertProductReviewAllUpdatablePropertiesEquals(expectedProductReview, getPersistedProductReview(expectedProductReview));
    }
}
