package com.snp.web.rest;

import static com.snp.domain.ProductAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.Product;
import com.snp.domain.ProductAttachment;
import com.snp.repository.ProductAttachmentRepository;
import com.snp.service.ProductAttachmentService;
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
 * Integration tests for the {@link ProductAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAttachmentRepository productAttachmentRepository;

    @Mock
    private ProductAttachmentRepository productAttachmentRepositoryMock;

    @Mock
    private ProductAttachmentService productAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAttachmentMockMvc;

    private ProductAttachment productAttachment;

    private ProductAttachment insertedProductAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAttachment createEntity(EntityManager em) {
        ProductAttachment productAttachment = new ProductAttachment().name(DEFAULT_NAME);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productAttachment.setProduct(product);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        productAttachment.setAttachment(attachment);
        return productAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAttachment createUpdatedEntity(EntityManager em) {
        ProductAttachment updatedProductAttachment = new ProductAttachment().name(UPDATED_NAME);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductAttachment.setProduct(product);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedProductAttachment.setAttachment(attachment);
        return updatedProductAttachment;
    }

    @BeforeEach
    void initTest() {
        productAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductAttachment != null) {
            productAttachmentRepository.delete(insertedProductAttachment);
            insertedProductAttachment = null;
        }
    }

    @Test
    @Transactional
    void createProductAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductAttachment
        var returnedProductAttachment = om.readValue(
            restProductAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAttachment.class
        );

        // Validate the ProductAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAttachmentUpdatableFieldsEquals(returnedProductAttachment, getPersistedProductAttachment(returnedProductAttachment));

        insertedProductAttachment = returnedProductAttachment;
    }

    @Test
    @Transactional
    void createProductAttachmentWithExistingId() throws Exception {
        // Create the ProductAttachment with an existing ID
        productAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductAttachments() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductAttachment() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get the productAttachment
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, productAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getProductAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        Long id = productAttachment.getId();

        defaultProductAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList where name equals to
        defaultProductAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList where name in
        defaultProductAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList where name is not null
        defaultProductAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList where name contains
        defaultProductAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        // Get all the productAttachmentList where name does not contain
        defaultProductAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productAttachmentRepository.saveAndFlush(productAttachment);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productAttachment.setProduct(product);
        productAttachmentRepository.saveAndFlush(productAttachment);
        Long productId = product.getId();
        // Get all the productAttachmentList where product equals to productId
        defaultProductAttachmentShouldBeFound("productId.equals=" + productId);

        // Get all the productAttachmentList where product equals to (productId + 1)
        defaultProductAttachmentShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            productAttachmentRepository.saveAndFlush(productAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        productAttachment.setAttachment(attachment);
        productAttachmentRepository.saveAndFlush(productAttachment);
        Long attachmentId = attachment.getId();
        // Get all the productAttachmentList where attachment equals to attachmentId
        defaultProductAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the productAttachmentList where attachment equals to (attachmentId + 1)
        defaultProductAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultProductAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductAttachmentShouldBeFound(shouldBeFound);
        defaultProductAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductAttachmentShouldBeFound(String filter) throws Exception {
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductAttachmentShouldNotBeFound(String filter) throws Exception {
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductAttachment() throws Exception {
        // Get the productAttachment
        restProductAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAttachment() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAttachment
        ProductAttachment updatedProductAttachment = productAttachmentRepository.findById(productAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAttachment are not directly saved in db
        em.detach(updatedProductAttachment);
        updatedProductAttachment.name(UPDATED_NAME);

        restProductAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAttachmentToMatchAllProperties(updatedProductAttachment);
    }

    @Test
    @Transactional
    void putNonExistingProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAttachment using partial update
        ProductAttachment partialUpdatedProductAttachment = new ProductAttachment();
        partialUpdatedProductAttachment.setId(productAttachment.getId());

        restProductAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ProductAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAttachment, productAttachment),
            getPersistedProductAttachment(productAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAttachment using partial update
        ProductAttachment partialUpdatedProductAttachment = new ProductAttachment();
        partialUpdatedProductAttachment.setId(productAttachment.getId());

        partialUpdatedProductAttachment.name(UPDATED_NAME);

        restProductAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ProductAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAttachmentUpdatableFieldsEquals(
            partialUpdatedProductAttachment,
            getPersistedProductAttachment(partialUpdatedProductAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductAttachment() throws Exception {
        // Initialize the database
        insertedProductAttachment = productAttachmentRepository.saveAndFlush(productAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productAttachment
        restProductAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productAttachmentRepository.count();
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

    protected ProductAttachment getPersistedProductAttachment(ProductAttachment productAttachment) {
        return productAttachmentRepository.findById(productAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedProductAttachmentToMatchAllProperties(ProductAttachment expectedProductAttachment) {
        assertProductAttachmentAllPropertiesEquals(expectedProductAttachment, getPersistedProductAttachment(expectedProductAttachment));
    }

    protected void assertPersistedProductAttachmentToMatchUpdatableProperties(ProductAttachment expectedProductAttachment) {
        assertProductAttachmentAllUpdatablePropertiesEquals(
            expectedProductAttachment,
            getPersistedProductAttachment(expectedProductAttachment)
        );
    }
}
