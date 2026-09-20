package com.snp.web.rest;

import static com.snp.domain.OrderItemTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderItemType;
import com.snp.repository.OrderItemTypeRepository;
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
 * Integration tests for the {@link OrderItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderItemTypeRepository orderItemTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemTypeMockMvc;

    private OrderItemType orderItemType;

    private OrderItemType insertedOrderItemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemType createEntity() {
        return new OrderItemType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemType createUpdatedEntity() {
        return new OrderItemType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        orderItemType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderItemType != null) {
            orderItemTypeRepository.delete(insertedOrderItemType);
            insertedOrderItemType = null;
        }
    }

    @Test
    @Transactional
    void createOrderItemType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderItemType
        var returnedOrderItemType = om.readValue(
            restOrderItemTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderItemType.class
        );

        // Validate the OrderItemType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderItemTypeUpdatableFieldsEquals(returnedOrderItemType, getPersistedOrderItemType(returnedOrderItemType));

        insertedOrderItemType = returnedOrderItemType;
    }

    @Test
    @Transactional
    void createOrderItemTypeWithExistingId() throws Exception {
        // Create the OrderItemType with an existing ID
        orderItemType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemType)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderItemType.setName(null);

        // Create the OrderItemType, which fails.

        restOrderItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItemTypes() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOrderItemType() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get the orderItemType
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getOrderItemTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        Long id = orderItemType.getId();

        defaultOrderItemTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderItemTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderItemTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where name equals to
        defaultOrderItemTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where name in
        defaultOrderItemTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where name is not null
        defaultOrderItemTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where name contains
        defaultOrderItemTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where name does not contain
        defaultOrderItemTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where description equals to
        defaultOrderItemTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where description in
        defaultOrderItemTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where description is not null
        defaultOrderItemTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where description contains
        defaultOrderItemTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderItemTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        // Get all the orderItemTypeList where description does not contain
        defaultOrderItemTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultOrderItemTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderItemTypeShouldBeFound(shouldBeFound);
        defaultOrderItemTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemTypeShouldBeFound(String filter) throws Exception {
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemTypeShouldNotBeFound(String filter) throws Exception {
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemType() throws Exception {
        // Get the orderItemType
        restOrderItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItemType() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemType
        OrderItemType updatedOrderItemType = orderItemTypeRepository.findById(orderItemType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItemType are not directly saved in db
        em.detach(updatedOrderItemType);
        updatedOrderItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderItemType))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderItemTypeToMatchAllProperties(updatedOrderItemType);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemType using partial update
        OrderItemType partialUpdatedOrderItemType = new OrderItemType();
        partialUpdatedOrderItemType.setId(orderItemType.getId());

        partialUpdatedOrderItemType.description(UPDATED_DESCRIPTION);

        restOrderItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemType))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderItemType, orderItemType),
            getPersistedOrderItemType(orderItemType)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderItemTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItemType using partial update
        OrderItemType partialUpdatedOrderItemType = new OrderItemType();
        partialUpdatedOrderItemType.setId(orderItemType.getId());

        partialUpdatedOrderItemType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItemType))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemTypeUpdatableFieldsEquals(partialUpdatedOrderItemType, getPersistedOrderItemType(partialUpdatedOrderItemType));
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItemType() throws Exception {
        // Initialize the database
        insertedOrderItemType = orderItemTypeRepository.saveAndFlush(orderItemType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderItemType
        restOrderItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderItemTypeRepository.count();
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

    protected OrderItemType getPersistedOrderItemType(OrderItemType orderItemType) {
        return orderItemTypeRepository.findById(orderItemType.getId()).orElseThrow();
    }

    protected void assertPersistedOrderItemTypeToMatchAllProperties(OrderItemType expectedOrderItemType) {
        assertOrderItemTypeAllPropertiesEquals(expectedOrderItemType, getPersistedOrderItemType(expectedOrderItemType));
    }

    protected void assertPersistedOrderItemTypeToMatchUpdatableProperties(OrderItemType expectedOrderItemType) {
        assertOrderItemTypeAllUpdatablePropertiesEquals(expectedOrderItemType, getPersistedOrderItemType(expectedOrderItemType));
    }
}
