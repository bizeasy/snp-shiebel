package com.snp.web.rest;

import static com.snp.domain.ShipmentItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentItem;
import com.snp.repository.ShipmentItemRepository;
import com.snp.service.ShipmentItemService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ShipmentItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipmentItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentItemRepository shipmentItemRepository;

    @Mock
    private ShipmentItemRepository shipmentItemRepositoryMock;

    @Mock
    private ShipmentItemService shipmentItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentItemMockMvc;

    private ShipmentItem shipmentItem;

    private ShipmentItem insertedShipmentItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentItem createEntity(EntityManager em) {
        ShipmentItem shipmentItem = new ShipmentItem().sequenceNo(DEFAULT_SEQUENCE_NO).quantity(DEFAULT_QUANTITY).message(DEFAULT_MESSAGE);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        shipmentItem.setShipment(shipment);
        return shipmentItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentItem createUpdatedEntity(EntityManager em) {
        ShipmentItem updatedShipmentItem = new ShipmentItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .message(UPDATED_MESSAGE);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createUpdatedEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        updatedShipmentItem.setShipment(shipment);
        return updatedShipmentItem;
    }

    @BeforeEach
    void initTest() {
        shipmentItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentItem != null) {
            shipmentItemRepository.delete(insertedShipmentItem);
            insertedShipmentItem = null;
        }
    }

    @Test
    @Transactional
    void createShipmentItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentItem
        var returnedShipmentItem = om.readValue(
            restShipmentItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentItem.class
        );

        // Validate the ShipmentItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentItemUpdatableFieldsEquals(returnedShipmentItem, getPersistedShipmentItem(returnedShipmentItem));

        insertedShipmentItem = returnedShipmentItem;
    }

    @Test
    @Transactional
    void createShipmentItemWithExistingId() throws Exception {
        // Create the ShipmentItem with an existing ID
        shipmentItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentItem)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentItem.setQuantity(null);

        // Create the ShipmentItem, which fails.

        restShipmentItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentItems() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipmentItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipmentItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipmentItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipmentItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShipmentItem() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get the shipmentItem
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getShipmentItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        Long id = shipmentItem.getId();

        defaultShipmentItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo equals to
        defaultShipmentItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo in
        defaultShipmentItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo is not null
        defaultShipmentItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo is greater than or equal to
        defaultShipmentItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo is less than or equal to
        defaultShipmentItemFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo is less than
        defaultShipmentItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where sequenceNo is greater than
        defaultShipmentItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity equals to
        defaultShipmentItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity in
        defaultShipmentItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity is not null
        defaultShipmentItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity is greater than or equal to
        defaultShipmentItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity is less than or equal to
        defaultShipmentItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity is less than
        defaultShipmentItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where quantity is greater than
        defaultShipmentItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where message equals to
        defaultShipmentItemFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where message in
        defaultShipmentItemFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where message is not null
        defaultShipmentItemFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentItemsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where message contains
        defaultShipmentItemFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        // Get all the shipmentItemList where message does not contain
        defaultShipmentItemFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentItemsByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipmentItemRepository.saveAndFlush(shipmentItem);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        shipmentItem.setShipment(shipment);
        shipmentItemRepository.saveAndFlush(shipmentItem);
        Long shipmentId = shipment.getId();
        // Get all the shipmentItemList where shipment equals to shipmentId
        defaultShipmentItemShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the shipmentItemList where shipment equals to (shipmentId + 1)
        defaultShipmentItemShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            shipmentItemRepository.saveAndFlush(shipmentItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        shipmentItem.setProduct(product);
        shipmentItemRepository.saveAndFlush(shipmentItem);
        Long productId = product.getId();
        // Get all the shipmentItemList where product equals to productId
        defaultShipmentItemShouldBeFound("productId.equals=" + productId);

        // Get all the shipmentItemList where product equals to (productId + 1)
        defaultShipmentItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultShipmentItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentItemShouldBeFound(shouldBeFound);
        defaultShipmentItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentItemShouldBeFound(String filter) throws Exception {
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));

        // Check, that the count call also returns 1
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentItemShouldNotBeFound(String filter) throws Exception {
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentItem() throws Exception {
        // Get the shipmentItem
        restShipmentItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentItem() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentItem
        ShipmentItem updatedShipmentItem = shipmentItemRepository.findById(shipmentItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentItem are not directly saved in db
        em.detach(updatedShipmentItem);
        updatedShipmentItem.sequenceNo(UPDATED_SEQUENCE_NO).quantity(UPDATED_QUANTITY).message(UPDATED_MESSAGE);

        restShipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentItem))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentItemToMatchAllProperties(updatedShipmentItem);
    }

    @Test
    @Transactional
    void putNonExistingShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentItemWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentItem using partial update
        ShipmentItem partialUpdatedShipmentItem = new ShipmentItem();
        partialUpdatedShipmentItem.setId(shipmentItem.getId());

        partialUpdatedShipmentItem.sequenceNo(UPDATED_SEQUENCE_NO).message(UPDATED_MESSAGE);

        restShipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentItem))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentItem, shipmentItem),
            getPersistedShipmentItem(shipmentItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentItemWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentItem using partial update
        ShipmentItem partialUpdatedShipmentItem = new ShipmentItem();
        partialUpdatedShipmentItem.setId(shipmentItem.getId());

        partialUpdatedShipmentItem.sequenceNo(UPDATED_SEQUENCE_NO).quantity(UPDATED_QUANTITY).message(UPDATED_MESSAGE);

        restShipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentItem))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentItemUpdatableFieldsEquals(partialUpdatedShipmentItem, getPersistedShipmentItem(partialUpdatedShipmentItem));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentItem() throws Exception {
        // Initialize the database
        insertedShipmentItem = shipmentItemRepository.saveAndFlush(shipmentItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentItem
        restShipmentItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentItemRepository.count();
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

    protected ShipmentItem getPersistedShipmentItem(ShipmentItem shipmentItem) {
        return shipmentItemRepository.findById(shipmentItem.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentItemToMatchAllProperties(ShipmentItem expectedShipmentItem) {
        assertShipmentItemAllPropertiesEquals(expectedShipmentItem, getPersistedShipmentItem(expectedShipmentItem));
    }

    protected void assertPersistedShipmentItemToMatchUpdatableProperties(ShipmentItem expectedShipmentItem) {
        assertShipmentItemAllUpdatablePropertiesEquals(expectedShipmentItem, getPersistedShipmentItem(expectedShipmentItem));
    }
}
