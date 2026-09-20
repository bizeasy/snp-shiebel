package com.snp.web.rest;

import static com.snp.domain.ReturnItemAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.OrderItem;
import com.snp.domain.Product;
import com.snp.domain.ReturnHeader;
import com.snp.domain.ReturnHeaderType;
import com.snp.domain.ReturnItem;
import com.snp.domain.Status;
import com.snp.repository.ReturnItemRepository;
import com.snp.service.ReturnItemService;
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
 * Integration tests for the {@link ReturnItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReturnItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RETURN_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_RETURN_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_RETURN_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RECEIVED_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_RECEIVED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_RECEIVED_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RETURN_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RETURN_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RETURN_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_RETURN_ITEM_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_ITEM_PURPOSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/return-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReturnItemRepository returnItemRepository;

    @Mock
    private ReturnItemRepository returnItemRepositoryMock;

    @Mock
    private ReturnItemService returnItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnItemMockMvc;

    private ReturnItem returnItem;

    private ReturnItem insertedReturnItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnItem createEntity(EntityManager em) {
        ReturnItem returnItem = new ReturnItem()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .description(DEFAULT_DESCRIPTION)
            .returnQuantity(DEFAULT_RETURN_QUANTITY)
            .receivedQuantity(DEFAULT_RECEIVED_QUANTITY)
            .returnPrice(DEFAULT_RETURN_PRICE)
            .returnItemPurpose(DEFAULT_RETURN_ITEM_PURPOSE);
        // Add required entity
        ReturnHeader returnHeader;
        if (TestUtil.findAll(em, ReturnHeader.class).isEmpty()) {
            returnHeader = ReturnHeaderResourceIT.createEntity();
            em.persist(returnHeader);
            em.flush();
        } else {
            returnHeader = TestUtil.findAll(em, ReturnHeader.class).getFirst();
        }
        returnItem.setReturnHeader(returnHeader);
        return returnItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnItem createUpdatedEntity(EntityManager em) {
        ReturnItem updatedReturnItem = new ReturnItem()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .returnQuantity(UPDATED_RETURN_QUANTITY)
            .receivedQuantity(UPDATED_RECEIVED_QUANTITY)
            .returnPrice(UPDATED_RETURN_PRICE)
            .returnItemPurpose(UPDATED_RETURN_ITEM_PURPOSE);
        // Add required entity
        ReturnHeader returnHeader;
        if (TestUtil.findAll(em, ReturnHeader.class).isEmpty()) {
            returnHeader = ReturnHeaderResourceIT.createUpdatedEntity();
            em.persist(returnHeader);
            em.flush();
        } else {
            returnHeader = TestUtil.findAll(em, ReturnHeader.class).getFirst();
        }
        updatedReturnItem.setReturnHeader(returnHeader);
        return updatedReturnItem;
    }

    @BeforeEach
    void initTest() {
        returnItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReturnItem != null) {
            returnItemRepository.delete(insertedReturnItem);
            insertedReturnItem = null;
        }
    }

    @Test
    @Transactional
    void createReturnItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReturnItem
        var returnedReturnItem = om.readValue(
            restReturnItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReturnItem.class
        );

        // Validate the ReturnItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReturnItemUpdatableFieldsEquals(returnedReturnItem, getPersistedReturnItem(returnedReturnItem));

        insertedReturnItem = returnedReturnItem;
    }

    @Test
    @Transactional
    void createReturnItemWithExistingId() throws Exception {
        // Create the ReturnItem with an existing ID
        returnItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItem)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReturnQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        returnItem.setReturnQuantity(null);

        // Create the ReturnItem, which fails.

        restReturnItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReturnItems() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].returnQuantity").value(hasItem(sameNumber(DEFAULT_RETURN_QUANTITY))))
            .andExpect(jsonPath("$.[*].receivedQuantity").value(hasItem(sameNumber(DEFAULT_RECEIVED_QUANTITY))))
            .andExpect(jsonPath("$.[*].returnPrice").value(hasItem(sameNumber(DEFAULT_RETURN_PRICE))))
            .andExpect(jsonPath("$.[*].returnItemPurpose").value(hasItem(DEFAULT_RETURN_ITEM_PURPOSE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReturnItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(returnItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReturnItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(returnItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReturnItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(returnItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReturnItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(returnItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReturnItem() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get the returnItem
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL_ID, returnItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.returnQuantity").value(sameNumber(DEFAULT_RETURN_QUANTITY)))
            .andExpect(jsonPath("$.receivedQuantity").value(sameNumber(DEFAULT_RECEIVED_QUANTITY)))
            .andExpect(jsonPath("$.returnPrice").value(sameNumber(DEFAULT_RETURN_PRICE)))
            .andExpect(jsonPath("$.returnItemPurpose").value(DEFAULT_RETURN_ITEM_PURPOSE));
    }

    @Test
    @Transactional
    void getReturnItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        Long id = returnItem.getId();

        defaultReturnItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReturnItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReturnItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo equals to
        defaultReturnItemFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo in
        defaultReturnItemFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo is not null
        defaultReturnItemFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo is greater than or equal to
        defaultReturnItemFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo is less than or equal to
        defaultReturnItemFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo is less than
        defaultReturnItemFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllReturnItemsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where sequenceNo is greater than
        defaultReturnItemFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllReturnItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where description equals to
        defaultReturnItemFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReturnItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where description in
        defaultReturnItemFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where description is not null
        defaultReturnItemFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where description contains
        defaultReturnItemFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReturnItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where description does not contain
        defaultReturnItemFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity equals to
        defaultReturnItemFiltering("returnQuantity.equals=" + DEFAULT_RETURN_QUANTITY, "returnQuantity.equals=" + UPDATED_RETURN_QUANTITY);
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity in
        defaultReturnItemFiltering(
            "returnQuantity.in=" + DEFAULT_RETURN_QUANTITY + "," + UPDATED_RETURN_QUANTITY,
            "returnQuantity.in=" + UPDATED_RETURN_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity is not null
        defaultReturnItemFiltering("returnQuantity.specified=true", "returnQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity is greater than or equal to
        defaultReturnItemFiltering(
            "returnQuantity.greaterThanOrEqual=" + DEFAULT_RETURN_QUANTITY,
            "returnQuantity.greaterThanOrEqual=" + UPDATED_RETURN_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity is less than or equal to
        defaultReturnItemFiltering(
            "returnQuantity.lessThanOrEqual=" + DEFAULT_RETURN_QUANTITY,
            "returnQuantity.lessThanOrEqual=" + SMALLER_RETURN_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity is less than
        defaultReturnItemFiltering(
            "returnQuantity.lessThan=" + UPDATED_RETURN_QUANTITY,
            "returnQuantity.lessThan=" + DEFAULT_RETURN_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnQuantity is greater than
        defaultReturnItemFiltering(
            "returnQuantity.greaterThan=" + SMALLER_RETURN_QUANTITY,
            "returnQuantity.greaterThan=" + DEFAULT_RETURN_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity equals to
        defaultReturnItemFiltering(
            "receivedQuantity.equals=" + DEFAULT_RECEIVED_QUANTITY,
            "receivedQuantity.equals=" + UPDATED_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity in
        defaultReturnItemFiltering(
            "receivedQuantity.in=" + DEFAULT_RECEIVED_QUANTITY + "," + UPDATED_RECEIVED_QUANTITY,
            "receivedQuantity.in=" + UPDATED_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity is not null
        defaultReturnItemFiltering("receivedQuantity.specified=true", "receivedQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity is greater than or equal to
        defaultReturnItemFiltering(
            "receivedQuantity.greaterThanOrEqual=" + DEFAULT_RECEIVED_QUANTITY,
            "receivedQuantity.greaterThanOrEqual=" + UPDATED_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity is less than or equal to
        defaultReturnItemFiltering(
            "receivedQuantity.lessThanOrEqual=" + DEFAULT_RECEIVED_QUANTITY,
            "receivedQuantity.lessThanOrEqual=" + SMALLER_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity is less than
        defaultReturnItemFiltering(
            "receivedQuantity.lessThan=" + UPDATED_RECEIVED_QUANTITY,
            "receivedQuantity.lessThan=" + DEFAULT_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReceivedQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where receivedQuantity is greater than
        defaultReturnItemFiltering(
            "receivedQuantity.greaterThan=" + SMALLER_RECEIVED_QUANTITY,
            "receivedQuantity.greaterThan=" + DEFAULT_RECEIVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice equals to
        defaultReturnItemFiltering("returnPrice.equals=" + DEFAULT_RETURN_PRICE, "returnPrice.equals=" + UPDATED_RETURN_PRICE);
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice in
        defaultReturnItemFiltering(
            "returnPrice.in=" + DEFAULT_RETURN_PRICE + "," + UPDATED_RETURN_PRICE,
            "returnPrice.in=" + UPDATED_RETURN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice is not null
        defaultReturnItemFiltering("returnPrice.specified=true", "returnPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice is greater than or equal to
        defaultReturnItemFiltering(
            "returnPrice.greaterThanOrEqual=" + DEFAULT_RETURN_PRICE,
            "returnPrice.greaterThanOrEqual=" + UPDATED_RETURN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice is less than or equal to
        defaultReturnItemFiltering(
            "returnPrice.lessThanOrEqual=" + DEFAULT_RETURN_PRICE,
            "returnPrice.lessThanOrEqual=" + SMALLER_RETURN_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice is less than
        defaultReturnItemFiltering("returnPrice.lessThan=" + UPDATED_RETURN_PRICE, "returnPrice.lessThan=" + DEFAULT_RETURN_PRICE);
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnPrice is greater than
        defaultReturnItemFiltering("returnPrice.greaterThan=" + SMALLER_RETURN_PRICE, "returnPrice.greaterThan=" + DEFAULT_RETURN_PRICE);
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnItemPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnItemPurpose equals to
        defaultReturnItemFiltering(
            "returnItemPurpose.equals=" + DEFAULT_RETURN_ITEM_PURPOSE,
            "returnItemPurpose.equals=" + UPDATED_RETURN_ITEM_PURPOSE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnItemPurposeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnItemPurpose in
        defaultReturnItemFiltering(
            "returnItemPurpose.in=" + DEFAULT_RETURN_ITEM_PURPOSE + "," + UPDATED_RETURN_ITEM_PURPOSE,
            "returnItemPurpose.in=" + UPDATED_RETURN_ITEM_PURPOSE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnItemPurposeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnItemPurpose is not null
        defaultReturnItemFiltering("returnItemPurpose.specified=true", "returnItemPurpose.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnItemPurposeContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnItemPurpose contains
        defaultReturnItemFiltering(
            "returnItemPurpose.contains=" + DEFAULT_RETURN_ITEM_PURPOSE,
            "returnItemPurpose.contains=" + UPDATED_RETURN_ITEM_PURPOSE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnItemPurposeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        // Get all the returnItemList where returnItemPurpose does not contain
        defaultReturnItemFiltering(
            "returnItemPurpose.doesNotContain=" + UPDATED_RETURN_ITEM_PURPOSE,
            "returnItemPurpose.doesNotContain=" + DEFAULT_RETURN_ITEM_PURPOSE
        );
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnHeaderIsEqualToSomething() throws Exception {
        ReturnHeader returnHeader;
        if (TestUtil.findAll(em, ReturnHeader.class).isEmpty()) {
            returnItemRepository.saveAndFlush(returnItem);
            returnHeader = ReturnHeaderResourceIT.createEntity();
        } else {
            returnHeader = TestUtil.findAll(em, ReturnHeader.class).getFirst();
        }
        em.persist(returnHeader);
        em.flush();
        returnItem.setReturnHeader(returnHeader);
        returnItemRepository.saveAndFlush(returnItem);
        Long returnHeaderId = returnHeader.getId();
        // Get all the returnItemList where returnHeader equals to returnHeaderId
        defaultReturnItemShouldBeFound("returnHeaderId.equals=" + returnHeaderId);

        // Get all the returnItemList where returnHeader equals to (returnHeaderId + 1)
        defaultReturnItemShouldNotBeFound("returnHeaderId.equals=" + (returnHeaderId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemsByReturnTypeIsEqualToSomething() throws Exception {
        ReturnHeaderType returnType;
        if (TestUtil.findAll(em, ReturnHeaderType.class).isEmpty()) {
            returnItemRepository.saveAndFlush(returnItem);
            returnType = ReturnHeaderTypeResourceIT.createEntity();
        } else {
            returnType = TestUtil.findAll(em, ReturnHeaderType.class).getFirst();
        }
        em.persist(returnType);
        em.flush();
        returnItem.setReturnType(returnType);
        returnItemRepository.saveAndFlush(returnItem);
        Long returnTypeId = returnType.getId();
        // Get all the returnItemList where returnType equals to returnTypeId
        defaultReturnItemShouldBeFound("returnTypeId.equals=" + returnTypeId);

        // Get all the returnItemList where returnType equals to (returnTypeId + 1)
        defaultReturnItemShouldNotBeFound("returnTypeId.equals=" + (returnTypeId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            returnItemRepository.saveAndFlush(returnItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        returnItem.setProduct(product);
        returnItemRepository.saveAndFlush(returnItem);
        Long productId = product.getId();
        // Get all the returnItemList where product equals to productId
        defaultReturnItemShouldBeFound("productId.equals=" + productId);

        // Get all the returnItemList where product equals to (productId + 1)
        defaultReturnItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            returnItemRepository.saveAndFlush(returnItem);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        returnItem.setOrderItem(orderItem);
        returnItemRepository.saveAndFlush(returnItem);
        Long orderItemId = orderItem.getId();
        // Get all the returnItemList where orderItem equals to orderItemId
        defaultReturnItemShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the returnItemList where orderItem equals to (orderItemId + 1)
        defaultReturnItemShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllReturnItemsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            returnItemRepository.saveAndFlush(returnItem);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        returnItem.setStatus(status);
        returnItemRepository.saveAndFlush(returnItem);
        Long statusId = status.getId();
        // Get all the returnItemList where status equals to statusId
        defaultReturnItemShouldBeFound("statusId.equals=" + statusId);

        // Get all the returnItemList where status equals to (statusId + 1)
        defaultReturnItemShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    private void defaultReturnItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReturnItemShouldBeFound(shouldBeFound);
        defaultReturnItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReturnItemShouldBeFound(String filter) throws Exception {
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].returnQuantity").value(hasItem(sameNumber(DEFAULT_RETURN_QUANTITY))))
            .andExpect(jsonPath("$.[*].receivedQuantity").value(hasItem(sameNumber(DEFAULT_RECEIVED_QUANTITY))))
            .andExpect(jsonPath("$.[*].returnPrice").value(hasItem(sameNumber(DEFAULT_RETURN_PRICE))))
            .andExpect(jsonPath("$.[*].returnItemPurpose").value(hasItem(DEFAULT_RETURN_ITEM_PURPOSE)));

        // Check, that the count call also returns 1
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReturnItemShouldNotBeFound(String filter) throws Exception {
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReturnItem() throws Exception {
        // Get the returnItem
        restReturnItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReturnItem() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItem
        ReturnItem updatedReturnItem = returnItemRepository.findById(returnItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReturnItem are not directly saved in db
        em.detach(updatedReturnItem);
        updatedReturnItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .returnQuantity(UPDATED_RETURN_QUANTITY)
            .receivedQuantity(UPDATED_RECEIVED_QUANTITY)
            .returnPrice(UPDATED_RETURN_PRICE)
            .returnItemPurpose(UPDATED_RETURN_ITEM_PURPOSE);

        restReturnItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReturnItem))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReturnItemToMatchAllProperties(updatedReturnItem);
    }

    @Test
    @Transactional
    void putNonExistingReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnItemWithPatch() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItem using partial update
        ReturnItem partialUpdatedReturnItem = new ReturnItem();
        partialUpdatedReturnItem.setId(returnItem.getId());

        partialUpdatedReturnItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .returnQuantity(UPDATED_RETURN_QUANTITY)
            .returnItemPurpose(UPDATED_RETURN_ITEM_PURPOSE);

        restReturnItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnItem))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReturnItem, returnItem),
            getPersistedReturnItem(returnItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateReturnItemWithPatch() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnItem using partial update
        ReturnItem partialUpdatedReturnItem = new ReturnItem();
        partialUpdatedReturnItem.setId(returnItem.getId());

        partialUpdatedReturnItem
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .returnQuantity(UPDATED_RETURN_QUANTITY)
            .receivedQuantity(UPDATED_RECEIVED_QUANTITY)
            .returnPrice(UPDATED_RETURN_PRICE)
            .returnItemPurpose(UPDATED_RETURN_ITEM_PURPOSE);

        restReturnItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnItem))
            )
            .andExpect(status().isOk());

        // Validate the ReturnItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnItemUpdatableFieldsEquals(partialUpdatedReturnItem, getPersistedReturnItem(partialUpdatedReturnItem));
    }

    @Test
    @Transactional
    void patchNonExistingReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(returnItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnItem() throws Exception {
        // Initialize the database
        insertedReturnItem = returnItemRepository.saveAndFlush(returnItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the returnItem
        restReturnItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return returnItemRepository.count();
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

    protected ReturnItem getPersistedReturnItem(ReturnItem returnItem) {
        return returnItemRepository.findById(returnItem.getId()).orElseThrow();
    }

    protected void assertPersistedReturnItemToMatchAllProperties(ReturnItem expectedReturnItem) {
        assertReturnItemAllPropertiesEquals(expectedReturnItem, getPersistedReturnItem(expectedReturnItem));
    }

    protected void assertPersistedReturnItemToMatchUpdatableProperties(ReturnItem expectedReturnItem) {
        assertReturnItemAllUpdatablePropertiesEquals(expectedReturnItem, getPersistedReturnItem(expectedReturnItem));
    }
}
