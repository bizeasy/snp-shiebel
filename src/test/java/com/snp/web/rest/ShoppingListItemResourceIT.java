package com.snp.web.rest;

import static com.snp.domain.ShoppingListItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Product;
import com.snp.domain.ShoppingList;
import com.snp.domain.ShoppingListItem;
import com.snp.domain.Status;
import com.snp.repository.ShoppingListItemRepository;
import com.snp.service.ShoppingListItemService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ShoppingListItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShoppingListItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_REQUIRED_BY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUIRED_BY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_SELECTED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELECTED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SELECTED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAX_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STORY = "AAAAAAAAAA";
    private static final String UPDATED_STORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shopping-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Mock
    private ShoppingListItemRepository shoppingListItemRepositoryMock;

    @Mock
    private ShoppingListItemService shoppingListItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingListItemMockMvc;

    private ShoppingListItem shoppingListItem;

    private ShoppingListItem insertedShoppingListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingListItem createEntity(EntityManager em) {
        ShoppingListItem shoppingListItem = new ShoppingListItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .requiredByDate(DEFAULT_REQUIRED_BY_DATE)
            .selectedAmount(DEFAULT_SELECTED_AMOUNT)
            .maxAmount(DEFAULT_MAX_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .story(DEFAULT_STORY);
        // Add required entity
        ShoppingList shoppingList;
        if (TestUtil.findAll(em, ShoppingList.class).isEmpty()) {
            shoppingList = ShoppingListResourceIT.createEntity(em);
            em.persist(shoppingList);
            em.flush();
        } else {
            shoppingList = TestUtil.findAll(em, ShoppingList.class).getFirst();
        }
        shoppingListItem.setShoppingList(shoppingList);
        return shoppingListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingListItem createUpdatedEntity(EntityManager em) {
        ShoppingListItem updatedShoppingListItem = new ShoppingListItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);
        // Add required entity
        ShoppingList shoppingList;
        if (TestUtil.findAll(em, ShoppingList.class).isEmpty()) {
            shoppingList = ShoppingListResourceIT.createUpdatedEntity(em);
            em.persist(shoppingList);
            em.flush();
        } else {
            shoppingList = TestUtil.findAll(em, ShoppingList.class).getFirst();
        }
        updatedShoppingListItem.setShoppingList(shoppingList);
        return updatedShoppingListItem;
    }

    @BeforeEach
    void initTest() {
        shoppingListItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShoppingListItem != null) {
            shoppingListItemRepository.delete(insertedShoppingListItem);
            insertedShoppingListItem = null;
        }
    }

    @Test
    @Transactional
    void createShoppingListItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingListItem
        var returnedShoppingListItem = om.readValue(
            restShoppingListItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingListItem.class
        );

        // Validate the ShoppingListItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShoppingListItemUpdatableFieldsEquals(returnedShoppingListItem, getPersistedShoppingListItem(returnedShoppingListItem));

        insertedShoppingListItem = returnedShoppingListItem;
    }

    @Test
    @Transactional
    void createShoppingListItemWithExistingId() throws Exception {
        // Create the ShoppingListItem with an existing ID
        shoppingListItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingListItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListItem)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingListItem.setQuantity(null);

        // Create the ShoppingListItem, which fails.

        restShoppingListItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingListItems() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].requiredByDate").value(hasItem(DEFAULT_REQUIRED_BY_DATE.toString())))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(sameNumber(DEFAULT_MAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingListItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shoppingListItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingListItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shoppingListItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingListItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shoppingListItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingListItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shoppingListItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShoppingListItem() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get the shoppingListItem
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingListItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.requiredByDate").value(DEFAULT_REQUIRED_BY_DATE.toString()))
            .andExpect(jsonPath("$.selectedAmount").value(sameNumber(DEFAULT_SELECTED_AMOUNT)))
            .andExpect(jsonPath("$.maxAmount").value(sameNumber(DEFAULT_MAX_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.story").value(DEFAULT_STORY));
    }

    @Test
    @Transactional
    void getShoppingListItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        Long id = shoppingListItem.getId();

        defaultShoppingListItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShoppingListItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShoppingListItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo equals to
        defaultShoppingListItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo in
        defaultShoppingListItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo is not null
        defaultShoppingListItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo is greater than or equal to
        defaultShoppingListItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo is less than or equal to
        defaultShoppingListItemFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo is less than
        defaultShoppingListItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where sequenceNo is greater than
        defaultShoppingListItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity equals to
        defaultShoppingListItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity in
        defaultShoppingListItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity is not null
        defaultShoppingListItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity is greater than or equal to
        defaultShoppingListItemFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity is less than or equal to
        defaultShoppingListItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity is less than
        defaultShoppingListItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where quantity is greater than
        defaultShoppingListItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByRequiredByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where requiredByDate equals to
        defaultShoppingListItemFiltering(
            "requiredByDate.equals=" + DEFAULT_REQUIRED_BY_DATE,
            "requiredByDate.equals=" + UPDATED_REQUIRED_BY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByRequiredByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where requiredByDate in
        defaultShoppingListItemFiltering(
            "requiredByDate.in=" + DEFAULT_REQUIRED_BY_DATE + "," + UPDATED_REQUIRED_BY_DATE,
            "requiredByDate.in=" + UPDATED_REQUIRED_BY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByRequiredByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where requiredByDate is not null
        defaultShoppingListItemFiltering("requiredByDate.specified=true", "requiredByDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount equals to
        defaultShoppingListItemFiltering(
            "selectedAmount.equals=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.equals=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount in
        defaultShoppingListItemFiltering(
            "selectedAmount.in=" + DEFAULT_SELECTED_AMOUNT + "," + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.in=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount is not null
        defaultShoppingListItemFiltering("selectedAmount.specified=true", "selectedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount is greater than or equal to
        defaultShoppingListItemFiltering(
            "selectedAmount.greaterThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.greaterThanOrEqual=" + UPDATED_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount is less than or equal to
        defaultShoppingListItemFiltering(
            "selectedAmount.lessThanOrEqual=" + DEFAULT_SELECTED_AMOUNT,
            "selectedAmount.lessThanOrEqual=" + SMALLER_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount is less than
        defaultShoppingListItemFiltering(
            "selectedAmount.lessThan=" + UPDATED_SELECTED_AMOUNT,
            "selectedAmount.lessThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsBySelectedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where selectedAmount is greater than
        defaultShoppingListItemFiltering(
            "selectedAmount.greaterThan=" + SMALLER_SELECTED_AMOUNT,
            "selectedAmount.greaterThan=" + DEFAULT_SELECTED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount equals to
        defaultShoppingListItemFiltering("maxAmount.equals=" + DEFAULT_MAX_AMOUNT, "maxAmount.equals=" + UPDATED_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount in
        defaultShoppingListItemFiltering(
            "maxAmount.in=" + DEFAULT_MAX_AMOUNT + "," + UPDATED_MAX_AMOUNT,
            "maxAmount.in=" + UPDATED_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount is not null
        defaultShoppingListItemFiltering("maxAmount.specified=true", "maxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount is greater than or equal to
        defaultShoppingListItemFiltering(
            "maxAmount.greaterThanOrEqual=" + DEFAULT_MAX_AMOUNT,
            "maxAmount.greaterThanOrEqual=" + UPDATED_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount is less than or equal to
        defaultShoppingListItemFiltering(
            "maxAmount.lessThanOrEqual=" + DEFAULT_MAX_AMOUNT,
            "maxAmount.lessThanOrEqual=" + SMALLER_MAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount is less than
        defaultShoppingListItemFiltering("maxAmount.lessThan=" + UPDATED_MAX_AMOUNT, "maxAmount.lessThan=" + DEFAULT_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByMaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where maxAmount is greater than
        defaultShoppingListItemFiltering("maxAmount.greaterThan=" + SMALLER_MAX_AMOUNT, "maxAmount.greaterThan=" + DEFAULT_MAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where description equals to
        defaultShoppingListItemFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where description in
        defaultShoppingListItemFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where description is not null
        defaultShoppingListItemFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where description contains
        defaultShoppingListItemFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where description does not contain
        defaultShoppingListItemFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where story equals to
        defaultShoppingListItemFiltering("story.equals=" + DEFAULT_STORY, "story.equals=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where story in
        defaultShoppingListItemFiltering("story.in=" + DEFAULT_STORY + "," + UPDATED_STORY, "story.in=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where story is not null
        defaultShoppingListItemFiltering("story.specified=true", "story.specified=false");
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStoryContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where story contains
        defaultShoppingListItemFiltering("story.contains=" + DEFAULT_STORY, "story.contains=" + UPDATED_STORY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        // Get all the shoppingListItemList where story does not contain
        defaultShoppingListItemFiltering("story.doesNotContain=" + UPDATED_STORY, "story.doesNotContain=" + DEFAULT_STORY);
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByShoppingListIsEqualToSomething() throws Exception {
        ShoppingList shoppingList;
        if (TestUtil.findAll(em, ShoppingList.class).isEmpty()) {
            shoppingListItemRepository.saveAndFlush(shoppingListItem);
            shoppingList = ShoppingListResourceIT.createEntity(em);
        } else {
            shoppingList = TestUtil.findAll(em, ShoppingList.class).getFirst();
        }
        em.persist(shoppingList);
        em.flush();
        shoppingListItem.setShoppingList(shoppingList);
        shoppingListItemRepository.saveAndFlush(shoppingListItem);
        Long shoppingListId = shoppingList.getId();
        // Get all the shoppingListItemList where shoppingList equals to shoppingListId
        defaultShoppingListItemShouldBeFound("shoppingListId.equals=" + shoppingListId);

        // Get all the shoppingListItemList where shoppingList equals to (shoppingListId + 1)
        defaultShoppingListItemShouldNotBeFound("shoppingListId.equals=" + (shoppingListId + 1));
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            shoppingListItemRepository.saveAndFlush(shoppingListItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        shoppingListItem.setProduct(product);
        shoppingListItemRepository.saveAndFlush(shoppingListItem);
        Long productId = product.getId();
        // Get all the shoppingListItemList where product equals to productId
        defaultShoppingListItemShouldBeFound("productId.equals=" + productId);

        // Get all the shoppingListItemList where product equals to (productId + 1)
        defaultShoppingListItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllShoppingListItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            shoppingListItemRepository.saveAndFlush(shoppingListItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        shoppingListItem.setStatus(status);
        shoppingListItemRepository.saveAndFlush(shoppingListItem);
        Long statusId = status.getId();
        // Get all the shoppingListItemList where status equals to statusId
        defaultShoppingListItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the shoppingListItemList where status equals to (statusId + 1)
        defaultShoppingListItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultShoppingListItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShoppingListItemShouldBeFound(shouldBeFound);
        defaultShoppingListItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShoppingListItemShouldBeFound(String filter) throws Exception {
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].requiredByDate").value(hasItem(DEFAULT_REQUIRED_BY_DATE.toString())))
            .andExpect(jsonPath("$.[*].selectedAmount").value(hasItem(sameNumber(DEFAULT_SELECTED_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(sameNumber(DEFAULT_MAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY)));

        // Check, that the count call also returns 1
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShoppingListItemShouldNotBeFound(String filter) throws Exception {
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShoppingListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShoppingListItem() throws Exception {
        // Get the shoppingListItem
        restShoppingListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingListItem() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListItem
        ShoppingListItem updatedShoppingListItem = shoppingListItemRepository.findById(shoppingListItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingListItem are not directly saved in db
        em.detach(updatedShoppingListItem);
        updatedShoppingListItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);

        restShoppingListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShoppingListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShoppingListItem))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingListItemToMatchAllProperties(updatedShoppingListItem);
    }

    @Test
    @Transactional
    void putNonExistingShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingListItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingListItemWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListItem using partial update
        ShoppingListItem partialUpdatedShoppingListItem = new ShoppingListItem();
        partialUpdatedShoppingListItem.setId(shoppingListItem.getId());

        partialUpdatedShoppingListItem.requiredByDate(UPDATED_REQUIRED_BY_DATE).maxAmount(UPDATED_MAX_AMOUNT);

        restShoppingListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingListItem))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingListItem, shoppingListItem),
            getPersistedShoppingListItem(shoppingListItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingListItemWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingListItem using partial update
        ShoppingListItem partialUpdatedShoppingListItem = new ShoppingListItem();
        partialUpdatedShoppingListItem.setId(shoppingListItem.getId());

        partialUpdatedShoppingListItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .requiredByDate(UPDATED_REQUIRED_BY_DATE)
            .selectedAmount(UPDATED_SELECTED_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .story(UPDATED_STORY);

        restShoppingListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingListItem))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingListItemUpdatableFieldsEquals(
            partialUpdatedShoppingListItem,
            getPersistedShoppingListItem(partialUpdatedShoppingListItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingListItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingListItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingListItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingListItem() throws Exception {
        // Initialize the database
        insertedShoppingListItem = shoppingListItemRepository.saveAndFlush(shoppingListItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingListItem
        restShoppingListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingListItemRepository.count();
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

    protected ShoppingListItem getPersistedShoppingListItem(ShoppingListItem shoppingListItem) {
        return shoppingListItemRepository.findById(shoppingListItem.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingListItemToMatchAllProperties(ShoppingListItem expectedShoppingListItem) {
        assertShoppingListItemAllPropertiesEquals(expectedShoppingListItem, getPersistedShoppingListItem(expectedShoppingListItem));
    }

    protected void assertPersistedShoppingListItemToMatchUpdatableProperties(ShoppingListItem expectedShoppingListItem) {
        assertShoppingListItemAllUpdatablePropertiesEquals(
            expectedShoppingListItem,
            getPersistedShoppingListItem(expectedShoppingListItem)
        );
    }
}
