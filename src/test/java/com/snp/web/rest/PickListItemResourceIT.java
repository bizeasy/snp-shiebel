package com.snp.web.rest;

import static com.snp.domain.PickListItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.FacilityLocation;
import com.snp.domain.InventoryItem;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.PickList;
import com.snp.domain.PickListItem;
import com.snp.domain.Product;
import com.snp.domain.Status;
import com.snp.repository.PickListItemRepository;
import com.snp.service.PickListItemService;
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
 * Integration tests for the {@link PickListItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PickListItemResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/pick-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PickListItemRepository pickListItemRepository;

    @Mock
    private PickListItemRepository pickListItemRepositoryMock;

    @Mock
    private PickListItemService pickListItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPickListItemMockMvc;

    private PickListItem pickListItem;

    private PickListItem insertedPickListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickListItem createEntity(EntityManager em) {
        PickListItem pickListItem = new PickListItem().quantity(DEFAULT_QUANTITY);
        // Add required entity
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            pickList = PickListResourceIT.createEntity();
            em.persist(pickList);
            em.flush();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        pickListItem.setPickList(pickList);
        return pickListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickListItem createUpdatedEntity(EntityManager em) {
        PickListItem updatedPickListItem = new PickListItem().quantity(UPDATED_QUANTITY);
        // Add required entity
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            pickList = PickListResourceIT.createUpdatedEntity();
            em.persist(pickList);
            em.flush();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        updatedPickListItem.setPickList(pickList);
        return updatedPickListItem;
    }

    @BeforeEach
    void initTest() {
        pickListItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPickListItem != null) {
            pickListItemRepository.delete(insertedPickListItem);
            insertedPickListItem = null;
        }
    }

    @Test
    @Transactional
    void createPickListItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PickListItem
        var returnedPickListItem = om.readValue(
            restPickListItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickListItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PickListItem.class
        );

        // Validate the PickListItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPickListItemUpdatableFieldsEquals(returnedPickListItem, getPersistedPickListItem(returnedPickListItem));

        insertedPickListItem = returnedPickListItem;
    }

    @Test
    @Transactional
    void createPickListItemWithExistingId() throws Exception {
        // Create the PickListItem with an existing ID
        pickListItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPickListItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickListItem)))
            .andExpect(status().isBadRequest());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pickListItem.setQuantity(null);

        // Create the PickListItem, which fails.

        restPickListItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickListItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPickListItems() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPickListItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pickListItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPickListItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pickListItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPickListItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pickListItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPickListItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pickListItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPickListItem() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get the pickListItem
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, pickListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pickListItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getPickListItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        Long id = pickListItem.getId();

        defaultPickListItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPickListItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPickListItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity equals to
        defaultPickListItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity in
        defaultPickListItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity is not null
        defaultPickListItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity is greater than or equal to
        defaultPickListItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity is less than or equal to
        defaultPickListItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity is less than
        defaultPickListItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        // Get all the pickListItemList where quantity is greater than
        defaultPickListItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPickListItemsByPickListIsEqualToSomething() throws Exception {
        PickList pickList;
        if (TestUtil.findAll(em, PickList.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            pickList = PickListResourceIT.createEntity();
        } else {
            pickList = TestUtil.findAll(em, PickList.class).getFirst();
        }
        em.persist(pickList);
        em.flush();
        pickListItem.setPickList(pickList);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long pickListId = pickList.getId();
        // Get all the pickListItemList where pickList equals to pickListId
        defaultPickListItemShouldBeFound("pickListId.equals=" + pickListId);

        // Get all the pickListItemList where pickList equals to (pickListId + 1)
        defaultPickListItemShouldNotBeFound("pickListId.equals=" + (pickListId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        pickListItem.setOrder(order);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long orderId = order.getId();
        // Get all the pickListItemList where order equals to orderId
        defaultPickListItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the pickListItemList where order equals to (orderId + 1)
        defaultPickListItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        pickListItem.setOrderItem(orderItem);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long orderItemId = orderItem.getId();
        // Get all the pickListItemList where orderItem equals to orderItemId
        defaultPickListItemShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the pickListItemList where orderItem equals to (orderItemId + 1)
        defaultPickListItemShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        pickListItem.setInventoryItem(inventoryItem);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the pickListItemList where inventoryItem equals to inventoryItemId
        defaultPickListItemShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the pickListItemList where inventoryItem equals to (inventoryItemId + 1)
        defaultPickListItemShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        pickListItem.setProduct(product);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long productId = product.getId();
        // Get all the pickListItemList where product equals to productId
        defaultPickListItemShouldBeFound("productId.equals=" + productId);

        // Get all the pickListItemList where product equals to (productId + 1)
        defaultPickListItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByFacilityLocationIsEqualToSomething() throws Exception {
        FacilityLocation facilityLocation;
        if (TestUtil.findAll(em, FacilityLocation.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            facilityLocation = FacilityLocationResourceIT.createEntity(em);
        } else {
            facilityLocation = TestUtil.findAll(em, FacilityLocation.class).getFirst();
        }
        em.persist(facilityLocation);
        em.flush();
        pickListItem.setFacilityLocation(facilityLocation);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long facilityLocationId = facilityLocation.getId();
        // Get all the pickListItemList where facilityLocation equals to facilityLocationId
        defaultPickListItemShouldBeFound("facilityLocationId.equals=" + facilityLocationId);

        // Get all the pickListItemList where facilityLocation equals to (facilityLocationId + 1)
        defaultPickListItemShouldNotBeFound("facilityLocationId.equals=" + (facilityLocationId + 1));
    }

    @Test
    @Transactional
    void getAllPickListItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            pickListItemRepository.saveAndFlush(pickListItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        pickListItem.setStatus(status);
        pickListItemRepository.saveAndFlush(pickListItem);
        Long statusId = status.getId();
        // Get all the pickListItemList where status equals to statusId
        defaultPickListItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the pickListItemList where status equals to (statusId + 1)
        defaultPickListItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultPickListItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPickListItemShouldBeFound(shouldBeFound);
        defaultPickListItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPickListItemShouldBeFound(String filter) throws Exception {
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))));

        // Check, that the count call also returns 1
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPickListItemShouldNotBeFound(String filter) throws Exception {
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPickListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPickListItem() throws Exception {
        // Get the pickListItem
        restPickListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPickListItem() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickListItem
        PickListItem updatedPickListItem = pickListItemRepository.findById(pickListItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPickListItem are not directly saved in db
        em.detach(updatedPickListItem);
        updatedPickListItem.quantity(UPDATED_QUANTITY);

        restPickListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPickListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPickListItem))
            )
            .andExpect(status().isOk());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPickListItemToMatchAllProperties(updatedPickListItem);
    }

    @Test
    @Transactional
    void putNonExistingPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pickListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pickListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pickListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pickListItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePickListItemWithPatch() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickListItem using partial update
        PickListItem partialUpdatedPickListItem = new PickListItem();
        partialUpdatedPickListItem.setId(pickListItem.getId());

        restPickListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPickListItem))
            )
            .andExpect(status().isOk());

        // Validate the PickListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPickListItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPickListItem, pickListItem),
            getPersistedPickListItem(pickListItem)
        );
    }

    @Test
    @Transactional
    void fullUpdatePickListItemWithPatch() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pickListItem using partial update
        PickListItem partialUpdatedPickListItem = new PickListItem();
        partialUpdatedPickListItem.setId(pickListItem.getId());

        partialUpdatedPickListItem.quantity(UPDATED_QUANTITY);

        restPickListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPickListItem))
            )
            .andExpect(status().isOk());

        // Validate the PickListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPickListItemUpdatableFieldsEquals(partialUpdatedPickListItem, getPersistedPickListItem(partialUpdatedPickListItem));
    }

    @Test
    @Transactional
    void patchNonExistingPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pickListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pickListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pickListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPickListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pickListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickListItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pickListItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePickListItem() throws Exception {
        // Initialize the database
        insertedPickListItem = pickListItemRepository.saveAndFlush(pickListItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pickListItem
        restPickListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, pickListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pickListItemRepository.count();
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

    protected PickListItem getPersistedPickListItem(PickListItem pickListItem) {
        return pickListItemRepository.findById(pickListItem.getId()).orElseThrow();
    }

    protected void assertPersistedPickListItemToMatchAllProperties(PickListItem expectedPickListItem) {
        assertPickListItemAllPropertiesEquals(expectedPickListItem, getPersistedPickListItem(expectedPickListItem));
    }

    protected void assertPersistedPickListItemToMatchUpdatableProperties(PickListItem expectedPickListItem) {
        assertPickListItemAllUpdatablePropertiesEquals(expectedPickListItem, getPersistedPickListItem(expectedPickListItem));
    }
}
