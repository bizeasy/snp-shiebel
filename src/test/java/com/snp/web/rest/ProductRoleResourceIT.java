package com.snp.web.rest;

import static com.snp.domain.ProductRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.ProductRole;
import com.snp.domain.RoleType;
import com.snp.repository.ProductRoleRepository;
import com.snp.service.ProductRoleService;
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
 * Integration tests for the {@link ProductRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/product-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRoleRepository productRoleRepository;

    @Mock
    private ProductRoleRepository productRoleRepositoryMock;

    @Mock
    private ProductRoleService productRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductRoleMockMvc;

    private ProductRole productRole;

    private ProductRole insertedProductRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRole createEntity(EntityManager em) {
        ProductRole productRole = new ProductRole();
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        productRole.setProduct(product);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        productRole.setParty(party);
        return productRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRole createUpdatedEntity(EntityManager em) {
        ProductRole updatedProductRole = new ProductRole();
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        updatedProductRole.setProduct(product);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedProductRole.setParty(party);
        return updatedProductRole;
    }

    @BeforeEach
    void initTest() {
        productRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductRole != null) {
            productRoleRepository.delete(insertedProductRole);
            insertedProductRole = null;
        }
    }

    @Test
    @Transactional
    void createProductRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductRole
        var returnedProductRole = om.readValue(
            restProductRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductRole.class
        );

        // Validate the ProductRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductRoleUpdatableFieldsEquals(returnedProductRole, getPersistedProductRole(returnedProductRole));

        insertedProductRole = returnedProductRole;
    }

    @Test
    @Transactional
    void createProductRoleWithExistingId() throws Exception {
        // Create the ProductRole with an existing ID
        productRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRole)))
            .andExpect(status().isBadRequest());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductRoles() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        // Get all the productRoleList
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductRole() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        // Get the productRole
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, productRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getProductRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        Long id = productRole.getId();

        defaultProductRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductRolesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productRoleRepository.saveAndFlush(productRole);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        productRole.setProduct(product);
        productRoleRepository.saveAndFlush(productRole);
        Long productId = product.getId();
        // Get all the productRoleList where product equals to productId
        defaultProductRoleShouldBeFound("productId.equals=" + productId);

        // Get all the productRoleList where product equals to (productId + 1)
        defaultProductRoleShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            productRoleRepository.saveAndFlush(productRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        productRole.setParty(party);
        productRoleRepository.saveAndFlush(productRole);
        Long partyId = party.getId();
        // Get all the productRoleList where party equals to partyId
        defaultProductRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the productRoleList where party equals to (partyId + 1)
        defaultProductRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllProductRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            productRoleRepository.saveAndFlush(productRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        productRole.setRoleType(roleType);
        productRoleRepository.saveAndFlush(productRole);
        Long roleTypeId = roleType.getId();
        // Get all the productRoleList where roleType equals to roleTypeId
        defaultProductRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the productRoleList where roleType equals to (roleTypeId + 1)
        defaultProductRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultProductRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductRoleShouldBeFound(shouldBeFound);
        defaultProductRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductRoleShouldBeFound(String filter) throws Exception {
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRole.getId().intValue())));

        // Check, that the count call also returns 1
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductRoleShouldNotBeFound(String filter) throws Exception {
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductRole() throws Exception {
        // Get the productRole
        restProductRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductRole() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRole
        ProductRole updatedProductRole = productRoleRepository.findById(productRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductRole are not directly saved in db
        em.detach(updatedProductRole);

        restProductRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductRole))
            )
            .andExpect(status().isOk());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductRoleToMatchAllProperties(updatedProductRole);
    }

    @Test
    @Transactional
    void putNonExistingProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductRoleWithPatch() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRole using partial update
        ProductRole partialUpdatedProductRole = new ProductRole();
        partialUpdatedProductRole.setId(productRole.getId());

        restProductRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductRole))
            )
            .andExpect(status().isOk());

        // Validate the ProductRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductRole, productRole),
            getPersistedProductRole(productRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductRoleWithPatch() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRole using partial update
        ProductRole partialUpdatedProductRole = new ProductRole();
        partialUpdatedProductRole.setId(productRole.getId());

        restProductRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductRole))
            )
            .andExpect(status().isOk());

        // Validate the ProductRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductRoleUpdatableFieldsEquals(partialUpdatedProductRole, getPersistedProductRole(partialUpdatedProductRole));
    }

    @Test
    @Transactional
    void patchNonExistingProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductRole() throws Exception {
        // Initialize the database
        insertedProductRole = productRoleRepository.saveAndFlush(productRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productRole
        restProductRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, productRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRoleRepository.count();
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

    protected ProductRole getPersistedProductRole(ProductRole productRole) {
        return productRoleRepository.findById(productRole.getId()).orElseThrow();
    }

    protected void assertPersistedProductRoleToMatchAllProperties(ProductRole expectedProductRole) {
        assertProductRoleAllPropertiesEquals(expectedProductRole, getPersistedProductRole(expectedProductRole));
    }

    protected void assertPersistedProductRoleToMatchUpdatableProperties(ProductRole expectedProductRole) {
        assertProductRoleAllUpdatablePropertiesEquals(expectedProductRole, getPersistedProductRole(expectedProductRole));
    }
}
