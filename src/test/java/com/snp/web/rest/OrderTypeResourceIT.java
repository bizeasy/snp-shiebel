package com.snp.web.rest;

import static com.snp.domain.OrderTypeAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderType;
import com.snp.repository.OrderTypeRepository;
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
 * Integration tests for the {@link OrderTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderTypeRepository orderTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderTypeMockMvc;

    private OrderType orderType;

    private OrderType insertedOrderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderType createEntity() {
        return new OrderType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderType createUpdatedEntity() {
        return new OrderType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        orderType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrderType != null) {
            orderTypeRepository.delete(insertedOrderType);
            insertedOrderType = null;
        }
    }

    @Test
    @Transactional
    void createOrderType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderType
        var returnedOrderType = om.readValue(
            restOrderTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderType.class
        );

        // Validate the OrderType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderTypeUpdatableFieldsEquals(returnedOrderType, getPersistedOrderType(returnedOrderType));

        insertedOrderType = returnedOrderType;
    }

    @Test
    @Transactional
    void createOrderTypeWithExistingId() throws Exception {
        // Create the OrderType with an existing ID
        orderType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderType)))
            .andExpect(status().isBadRequest());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderType.setName(null);

        // Create the OrderType, which fails.

        restOrderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderTypes() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOrderType() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get the orderType
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, orderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getOrderTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        Long id = orderType.getId();

        defaultOrderTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where name equals to
        defaultOrderTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where name in
        defaultOrderTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where name is not null
        defaultOrderTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where name contains
        defaultOrderTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrderTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where name does not contain
        defaultOrderTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOrderTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where description equals to
        defaultOrderTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where description in
        defaultOrderTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrderTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where description is not null
        defaultOrderTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where description contains
        defaultOrderTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        // Get all the orderTypeList where description does not contain
        defaultOrderTypeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultOrderTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderTypeShouldBeFound(shouldBeFound);
        defaultOrderTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderTypeShouldBeFound(String filter) throws Exception {
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderTypeShouldNotBeFound(String filter) throws Exception {
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderType() throws Exception {
        // Get the orderType
        restOrderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderType() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderType
        OrderType updatedOrderType = orderTypeRepository.findById(orderType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderType are not directly saved in db
        em.detach(updatedOrderType);
        updatedOrderType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderType))
            )
            .andExpect(status().isOk());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderTypeToMatchAllProperties(updatedOrderType);
    }

    @Test
    @Transactional
    void putNonExistingOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderType using partial update
        OrderType partialUpdatedOrderType = new OrderType();
        partialUpdatedOrderType.setId(orderType.getId());

        restOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderType))
            )
            .andExpect(status().isOk());

        // Validate the OrderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderType, orderType),
            getPersistedOrderType(orderType)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderType using partial update
        OrderType partialUpdatedOrderType = new OrderType();
        partialUpdatedOrderType.setId(orderType.getId());

        partialUpdatedOrderType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderType))
            )
            .andExpect(status().isOk());

        // Validate the OrderType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderTypeUpdatableFieldsEquals(partialUpdatedOrderType, getPersistedOrderType(partialUpdatedOrderType));
    }

    @Test
    @Transactional
    void patchNonExistingOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderType() throws Exception {
        // Initialize the database
        insertedOrderType = orderTypeRepository.saveAndFlush(orderType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderType
        restOrderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderTypeRepository.count();
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

    protected OrderType getPersistedOrderType(OrderType orderType) {
        return orderTypeRepository.findById(orderType.getId()).orElseThrow();
    }

    protected void assertPersistedOrderTypeToMatchAllProperties(OrderType expectedOrderType) {
        assertOrderTypeAllPropertiesEquals(expectedOrderType, getPersistedOrderType(expectedOrderType));
    }

    protected void assertPersistedOrderTypeToMatchUpdatableProperties(OrderType expectedOrderType) {
        assertOrderTypeAllUpdatablePropertiesEquals(expectedOrderType, getPersistedOrderType(expectedOrderType));
    }
}
