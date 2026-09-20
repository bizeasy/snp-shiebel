package com.snp.web.rest;

import static com.snp.domain.OrderAdjustmentTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderAdjustmentType;
import com.snp.domain.OrderAdjustmentType;
import com.snp.repository.OrderAdjustmentTypeRepository;
import com.snp.service.OrderAdjustmentTypeService;
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
 * Integration tests for the {@link OrderAdjustmentTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderAdjustmentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-adjustment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderAdjustmentTypeRepository orderAdjustmentTypeRepository;

    @Mock
    private OrderAdjustmentTypeRepository orderAdjustmentTypeRepositoryMock;

    @Mock
    private OrderAdjustmentTypeService orderAdjustmentTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderAdjustmentTypeMockMvc;

    private OrderAdjustmentType orderAdjustmentType;

    private OrderAdjustmentType insertedOrderAdjustmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAdjustmentType createEntity() {
        return new OrderAdjustmentType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAdjustmentType createUpdatedEntity() {
        return new OrderAdjustmentType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        orderAdjustmentType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderAdjustmentType != null) {
            orderAdjustmentTypeRepository.delete(insertedOrderAdjustmentType);
            insertedOrderAdjustmentType = null;
        }
    }

    @Test
    @Transactional
    void createOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderAdjustmentType
        var returnedOrderAdjustmentType = om.readValue(
            restOrderAdjustmentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustmentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderAdjustmentType.class
        );

        // Validate the OrderAdjustmentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderAdjustmentTypeUpdatableFieldsEquals(
            returnedOrderAdjustmentType,
            getPersistedOrderAdjustmentType(returnedOrderAdjustmentType)
        );

        insertedOrderAdjustmentType = returnedOrderAdjustmentType;
    }

    @Test
    @Transactional
    void createOrderAdjustmentTypeWithExistingId() throws Exception {
        // Create the OrderAdjustmentType with an existing ID
        orderAdjustmentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderAdjustmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustmentType)))
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderAdjustmentType.setName(null);

        // Create the OrderAdjustmentType, which fails.

        restOrderAdjustmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustmentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypes() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAdjustmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAdjustmentTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderAdjustmentTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restOrderAdjustmentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderAdjustmentTypeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderAdjustmentTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderAdjustmentTypeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restOrderAdjustmentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderAdjustmentTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderAdjustmentType() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get the orderAdjustmentType
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, orderAdjustmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderAdjustmentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getOrderAdjustmentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        Long id = orderAdjustmentType.getId();

        defaultOrderAdjustmentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderAdjustmentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderAdjustmentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where name equals to
        defaultOrderAdjustmentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where name in
        defaultOrderAdjustmentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where name is not null
        defaultOrderAdjustmentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where name contains
        defaultOrderAdjustmentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where name does not contain
        defaultOrderAdjustmentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where description equals to
        defaultOrderAdjustmentTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where description in
        defaultOrderAdjustmentTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where description is not null
        defaultOrderAdjustmentTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where description contains
        defaultOrderAdjustmentTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        // Get all the orderAdjustmentTypeList where description does not contain
        defaultOrderAdjustmentTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderAdjustmentTypesByParentIsEqualToSomething() throws Exception {
        OrderAdjustmentType parent;
        if (TestUtil.findAll(em, OrderAdjustmentType.class).isEmpty()) {
            orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);
            parent = OrderAdjustmentTypeResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, OrderAdjustmentType.class).getFirst();
        }
        em.persist(parent);
        em.flush();
        orderAdjustmentType.setParent(parent);
        orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);
        Long parentId = parent.getId();
        // Get all the orderAdjustmentTypeList where parent equals to parentId
        defaultOrderAdjustmentTypeShouldBeFound("parentId.equals=" + parentId);

        // Get all the orderAdjustmentTypeList where parent equals to (parentId + 1)
        defaultOrderAdjustmentTypeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultOrderAdjustmentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderAdjustmentTypeShouldBeFound(shouldBeFound);
        defaultOrderAdjustmentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderAdjustmentTypeShouldBeFound(String filter) throws Exception {
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAdjustmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderAdjustmentTypeShouldNotBeFound(String filter) throws Exception {
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderAdjustmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderAdjustmentType() throws Exception {
        // Get the orderAdjustmentType
        restOrderAdjustmentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderAdjustmentType() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustmentType
        OrderAdjustmentType updatedOrderAdjustmentType = orderAdjustmentTypeRepository.findById(orderAdjustmentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderAdjustmentType are not directly saved in db
        em.detach(updatedOrderAdjustmentType);
        updatedOrderAdjustmentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderAdjustmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderAdjustmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderAdjustmentType))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderAdjustmentTypeToMatchAllProperties(updatedOrderAdjustmentType);
    }

    @Test
    @Transactional
    void putNonExistingOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderAdjustmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAdjustmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderAdjustmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderAdjustmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderAdjustmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustmentType using partial update
        OrderAdjustmentType partialUpdatedOrderAdjustmentType = new OrderAdjustmentType();
        partialUpdatedOrderAdjustmentType.setId(orderAdjustmentType.getId());

        partialUpdatedOrderAdjustmentType.description(UPDATED_DESCRIPTION);

        restOrderAdjustmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAdjustmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAdjustmentType))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAdjustmentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderAdjustmentType, orderAdjustmentType),
            getPersistedOrderAdjustmentType(orderAdjustmentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderAdjustmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderAdjustmentType using partial update
        OrderAdjustmentType partialUpdatedOrderAdjustmentType = new OrderAdjustmentType();
        partialUpdatedOrderAdjustmentType.setId(orderAdjustmentType.getId());

        partialUpdatedOrderAdjustmentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderAdjustmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderAdjustmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderAdjustmentType))
            )
            .andExpect(status().isOk());

        // Validate the OrderAdjustmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderAdjustmentTypeUpdatableFieldsEquals(
            partialUpdatedOrderAdjustmentType,
            getPersistedOrderAdjustmentType(partialUpdatedOrderAdjustmentType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderAdjustmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAdjustmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderAdjustmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderAdjustmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderAdjustmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderAdjustmentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderAdjustmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderAdjustmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderAdjustmentType() throws Exception {
        // Initialize the database
        insertedOrderAdjustmentType = orderAdjustmentTypeRepository.saveAndFlush(orderAdjustmentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderAdjustmentType
        restOrderAdjustmentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderAdjustmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderAdjustmentTypeRepository.count();
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

    protected OrderAdjustmentType getPersistedOrderAdjustmentType(OrderAdjustmentType orderAdjustmentType) {
        return orderAdjustmentTypeRepository.findById(orderAdjustmentType.getId()).orElseThrow();
    }

    protected void assertPersistedOrderAdjustmentTypeToMatchAllProperties(OrderAdjustmentType expectedOrderAdjustmentType) {
        assertOrderAdjustmentTypeAllPropertiesEquals(
            expectedOrderAdjustmentType,
            getPersistedOrderAdjustmentType(expectedOrderAdjustmentType)
        );
    }

    protected void assertPersistedOrderAdjustmentTypeToMatchUpdatableProperties(OrderAdjustmentType expectedOrderAdjustmentType) {
        assertOrderAdjustmentTypeAllUpdatablePropertiesEquals(
            expectedOrderAdjustmentType,
            getPersistedOrderAdjustmentType(expectedOrderAdjustmentType)
        );
    }
}
