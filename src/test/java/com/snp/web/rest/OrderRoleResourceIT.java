package com.snp.web.rest;

import static com.snp.domain.OrderRoleAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.OrderRole;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.repository.OrderRoleRepository;
import com.snp.service.OrderRoleService;
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
 * Integration tests for the {@link OrderRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/order-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderRoleRepository orderRoleRepository;

    @Mock
    private OrderRoleRepository orderRoleRepositoryMock;

    @Mock
    private OrderRoleService orderRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderRoleMockMvc;

    private OrderRole orderRole;

    private OrderRole insertedOrderRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRole createEntity(EntityManager em) {
        OrderRole orderRole = new OrderRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        orderRole.setParty(party);
        return orderRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRole createUpdatedEntity(EntityManager em) {
        OrderRole updatedOrderRole = new OrderRole();
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedOrderRole.setParty(party);
        return updatedOrderRole;
    }

    @BeforeEach
    void initTest() {
        orderRole = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderRole != null) {
            orderRoleRepository.delete(insertedOrderRole);
            insertedOrderRole = null;
        }
    }

    @Test
    @Transactional
    void createOrderRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderRole
        var returnedOrderRole = om.readValue(
            restOrderRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderRole)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderRole.class
        );

        // Validate the OrderRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderRoleUpdatableFieldsEquals(returnedOrderRole, getPersistedOrderRole(returnedOrderRole));

        insertedOrderRole = returnedOrderRole;
    }

    @Test
    @Transactional
    void createOrderRoleWithExistingId() throws Exception {
        // Create the OrderRole with an existing ID
        orderRole.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderRole)))
            .andExpect(status().isBadRequest());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderRoles() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        // Get all the orderRoleList
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderRole.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderRole() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        // Get the orderRole
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, orderRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderRole.getId().intValue()));
    }

    @Test
    @Transactional
    void getOrderRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        Long id = orderRole.getId();

        defaultOrderRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderRolesByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderRoleRepository.saveAndFlush(orderRole);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        orderRole.setOrder(order);
        orderRoleRepository.saveAndFlush(orderRole);
        Long orderId = order.getId();
        // Get all the orderRoleList where order equals to orderId
        defaultOrderRoleShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderRoleList where order equals to (orderId + 1)
        defaultOrderRoleShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderRolesByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderRoleRepository.saveAndFlush(orderRole);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        orderRole.setOrderItem(orderItem);
        orderRoleRepository.saveAndFlush(orderRole);
        Long orderItemId = orderItem.getId();
        // Get all the orderRoleList where orderItem equals to orderItemId
        defaultOrderRoleShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderRoleList where orderItem equals to (orderItemId + 1)
        defaultOrderRoleShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderRolesByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            orderRoleRepository.saveAndFlush(orderRole);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        orderRole.setParty(party);
        orderRoleRepository.saveAndFlush(orderRole);
        Long partyId = party.getId();
        // Get all the orderRoleList where party equals to partyId
        defaultOrderRoleShouldBeFound("partyId.equals=" + partyId);

        // Get all the orderRoleList where party equals to (partyId + 1)
        defaultOrderRoleShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllOrderRolesByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            orderRoleRepository.saveAndFlush(orderRole);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        orderRole.setRoleType(roleType);
        orderRoleRepository.saveAndFlush(orderRole);
        Long roleTypeId = roleType.getId();
        // Get all the orderRoleList where roleType equals to roleTypeId
        defaultOrderRoleShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the orderRoleList where roleType equals to (roleTypeId + 1)
        defaultOrderRoleShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultOrderRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderRoleShouldBeFound(shouldBeFound);
        defaultOrderRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderRoleShouldBeFound(String filter) throws Exception {
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderRole.getId().intValue())));

        // Check, that the count call also returns 1
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderRoleShouldNotBeFound(String filter) throws Exception {
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderRole() throws Exception {
        // Get the orderRole
        restOrderRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderRole() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderRole
        OrderRole updatedOrderRole = orderRoleRepository.findById(orderRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderRole are not directly saved in db
        em.detach(updatedOrderRole);

        restOrderRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderRole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderRole))
            )
            .andExpect(status().isOk());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderRoleToMatchAllProperties(updatedOrderRole);
    }

    @Test
    @Transactional
    void putNonExistingOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderRole.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderRoleWithPatch() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderRole using partial update
        OrderRole partialUpdatedOrderRole = new OrderRole();
        partialUpdatedOrderRole.setId(orderRole.getId());

        restOrderRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderRole))
            )
            .andExpect(status().isOk());

        // Validate the OrderRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderRole, orderRole),
            getPersistedOrderRole(orderRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderRoleWithPatch() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderRole using partial update
        OrderRole partialUpdatedOrderRole = new OrderRole();
        partialUpdatedOrderRole.setId(orderRole.getId());

        restOrderRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderRole))
            )
            .andExpect(status().isOk());

        // Validate the OrderRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderRoleUpdatableFieldsEquals(partialUpdatedOrderRole, getPersistedOrderRole(partialUpdatedOrderRole));
    }

    @Test
    @Transactional
    void patchNonExistingOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderRole))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderRole.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderRole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderRole() throws Exception {
        // Initialize the database
        insertedOrderRole = orderRoleRepository.saveAndFlush(orderRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderRole
        restOrderRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderRoleRepository.count();
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

    protected OrderRole getPersistedOrderRole(OrderRole orderRole) {
        return orderRoleRepository.findById(orderRole.getId()).orElseThrow();
    }

    protected void assertPersistedOrderRoleToMatchAllProperties(OrderRole expectedOrderRole) {
        assertOrderRoleAllPropertiesEquals(expectedOrderRole, getPersistedOrderRole(expectedOrderRole));
    }

    protected void assertPersistedOrderRoleToMatchUpdatableProperties(OrderRole expectedOrderRole) {
        assertOrderRoleAllUpdatablePropertiesEquals(expectedOrderRole, getPersistedOrderRole(expectedOrderRole));
    }
}
