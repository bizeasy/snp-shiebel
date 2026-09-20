package com.snp.web.rest;

import static com.snp.domain.ShipmentReceiptAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.Party;
import com.snp.domain.Product;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentItem;
import com.snp.domain.ShipmentReceipt;
import com.snp.repository.ShipmentReceiptRepository;
import com.snp.service.ShipmentReceiptService;
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
 * Integration tests for the {@link ShipmentReceiptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipmentReceiptResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QUANTITY_ACCEPTED = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_ACCEPTED = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_ACCEPTED = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QUANTITY_REJECTED = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_REJECTED = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_REJECTED = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentReceiptRepository shipmentReceiptRepository;

    @Mock
    private ShipmentReceiptRepository shipmentReceiptRepositoryMock;

    @Mock
    private ShipmentReceiptService shipmentReceiptServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentReceiptMockMvc;

    private ShipmentReceipt shipmentReceipt;

    private ShipmentReceipt insertedShipmentReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentReceipt createEntity() {
        return new ShipmentReceipt()
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .quantityAccepted(DEFAULT_QUANTITY_ACCEPTED)
            .quantityRejected(DEFAULT_QUANTITY_REJECTED)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .message(DEFAULT_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentReceipt createUpdatedEntity() {
        return new ShipmentReceipt()
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .quantityAccepted(UPDATED_QUANTITY_ACCEPTED)
            .quantityRejected(UPDATED_QUANTITY_REJECTED)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .message(UPDATED_MESSAGE);
    }

    @BeforeEach
    void initTest() {
        shipmentReceipt = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentReceipt != null) {
            shipmentReceiptRepository.delete(insertedShipmentReceipt);
            insertedShipmentReceipt = null;
        }
    }

    @Test
    @Transactional
    void createShipmentReceipt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentReceipt
        var returnedShipmentReceipt = om.readValue(
            restShipmentReceiptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentReceipt)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentReceipt.class
        );

        // Validate the ShipmentReceipt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentReceiptUpdatableFieldsEquals(returnedShipmentReceipt, getPersistedShipmentReceipt(returnedShipmentReceipt));

        insertedShipmentReceipt = returnedShipmentReceipt;
    }

    @Test
    @Transactional
    void createShipmentReceiptWithExistingId() throws Exception {
        // Create the ShipmentReceipt with an existing ID
        shipmentReceipt.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentReceipt.setQuantity(null);

        // Create the ShipmentReceipt, which fails.

        restShipmentReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentReceipt)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentReceipts() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].quantityAccepted").value(hasItem(sameNumber(DEFAULT_QUANTITY_ACCEPTED))))
            .andExpect(jsonPath("$.[*].quantityRejected").value(hasItem(sameNumber(DEFAULT_QUANTITY_REJECTED))))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentReceiptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipmentReceiptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipmentReceiptServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentReceiptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipmentReceiptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipmentReceiptRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShipmentReceipt() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get the shipmentReceipt
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentReceipt.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.quantityAccepted").value(sameNumber(DEFAULT_QUANTITY_ACCEPTED)))
            .andExpect(jsonPath("$.quantityRejected").value(sameNumber(DEFAULT_QUANTITY_REJECTED)))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getShipmentReceiptsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        Long id = shipmentReceipt.getId();

        defaultShipmentReceiptFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentReceiptFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentReceiptFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo equals to
        defaultShipmentReceiptFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo in
        defaultShipmentReceiptFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo is not null
        defaultShipmentReceiptFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo is greater than or equal to
        defaultShipmentReceiptFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo is less than or equal to
        defaultShipmentReceiptFiltering(
            "sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo is less than
        defaultShipmentReceiptFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where sequenceNo is greater than
        defaultShipmentReceiptFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity equals to
        defaultShipmentReceiptFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity in
        defaultShipmentReceiptFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity is not null
        defaultShipmentReceiptFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity is greater than or equal to
        defaultShipmentReceiptFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity is less than or equal to
        defaultShipmentReceiptFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity is less than
        defaultShipmentReceiptFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantity is greater than
        defaultShipmentReceiptFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted equals to
        defaultShipmentReceiptFiltering(
            "quantityAccepted.equals=" + DEFAULT_QUANTITY_ACCEPTED,
            "quantityAccepted.equals=" + UPDATED_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted in
        defaultShipmentReceiptFiltering(
            "quantityAccepted.in=" + DEFAULT_QUANTITY_ACCEPTED + "," + UPDATED_QUANTITY_ACCEPTED,
            "quantityAccepted.in=" + UPDATED_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted is not null
        defaultShipmentReceiptFiltering("quantityAccepted.specified=true", "quantityAccepted.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted is greater than or equal to
        defaultShipmentReceiptFiltering(
            "quantityAccepted.greaterThanOrEqual=" + DEFAULT_QUANTITY_ACCEPTED,
            "quantityAccepted.greaterThanOrEqual=" + UPDATED_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted is less than or equal to
        defaultShipmentReceiptFiltering(
            "quantityAccepted.lessThanOrEqual=" + DEFAULT_QUANTITY_ACCEPTED,
            "quantityAccepted.lessThanOrEqual=" + SMALLER_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted is less than
        defaultShipmentReceiptFiltering(
            "quantityAccepted.lessThan=" + UPDATED_QUANTITY_ACCEPTED,
            "quantityAccepted.lessThan=" + DEFAULT_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityAcceptedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityAccepted is greater than
        defaultShipmentReceiptFiltering(
            "quantityAccepted.greaterThan=" + SMALLER_QUANTITY_ACCEPTED,
            "quantityAccepted.greaterThan=" + DEFAULT_QUANTITY_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected equals to
        defaultShipmentReceiptFiltering(
            "quantityRejected.equals=" + DEFAULT_QUANTITY_REJECTED,
            "quantityRejected.equals=" + UPDATED_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected in
        defaultShipmentReceiptFiltering(
            "quantityRejected.in=" + DEFAULT_QUANTITY_REJECTED + "," + UPDATED_QUANTITY_REJECTED,
            "quantityRejected.in=" + UPDATED_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected is not null
        defaultShipmentReceiptFiltering("quantityRejected.specified=true", "quantityRejected.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected is greater than or equal to
        defaultShipmentReceiptFiltering(
            "quantityRejected.greaterThanOrEqual=" + DEFAULT_QUANTITY_REJECTED,
            "quantityRejected.greaterThanOrEqual=" + UPDATED_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected is less than or equal to
        defaultShipmentReceiptFiltering(
            "quantityRejected.lessThanOrEqual=" + DEFAULT_QUANTITY_REJECTED,
            "quantityRejected.lessThanOrEqual=" + SMALLER_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected is less than
        defaultShipmentReceiptFiltering(
            "quantityRejected.lessThan=" + UPDATED_QUANTITY_REJECTED,
            "quantityRejected.lessThan=" + DEFAULT_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByQuantityRejectedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where quantityRejected is greater than
        defaultShipmentReceiptFiltering(
            "quantityRejected.greaterThan=" + SMALLER_QUANTITY_REJECTED,
            "quantityRejected.greaterThan=" + DEFAULT_QUANTITY_REJECTED
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where receivedDate equals to
        defaultShipmentReceiptFiltering("receivedDate.equals=" + DEFAULT_RECEIVED_DATE, "receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where receivedDate in
        defaultShipmentReceiptFiltering(
            "receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE,
            "receivedDate.in=" + UPDATED_RECEIVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where receivedDate is not null
        defaultShipmentReceiptFiltering("receivedDate.specified=true", "receivedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where message equals to
        defaultShipmentReceiptFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where message in
        defaultShipmentReceiptFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where message is not null
        defaultShipmentReceiptFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where message contains
        defaultShipmentReceiptFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList where message does not contain
        defaultShipmentReceiptFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        shipmentReceipt.setShipment(shipment);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long shipmentId = shipment.getId();
        // Get all the shipmentReceiptList where shipment equals to shipmentId
        defaultShipmentReceiptShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the shipmentReceiptList where shipment equals to (shipmentId + 1)
        defaultShipmentReceiptShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByShipmentItemIsEqualToSomething() throws Exception {
        ShipmentItem shipmentItem;
        if (TestUtil.findAll(em, ShipmentItem.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            shipmentItem = ShipmentItemResourceIT.createEntity(em);
        } else {
            shipmentItem = TestUtil.findAll(em, ShipmentItem.class).getFirst();
        }
        em.persist(shipmentItem);
        em.flush();
        shipmentReceipt.setShipmentItem(shipmentItem);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long shipmentItemId = shipmentItem.getId();
        // Get all the shipmentReceiptList where shipmentItem equals to shipmentItemId
        defaultShipmentReceiptShouldBeFound("shipmentItemId.equals=" + shipmentItemId);

        // Get all the shipmentReceiptList where shipmentItem equals to (shipmentItemId + 1)
        defaultShipmentReceiptShouldNotBeFound("shipmentItemId.equals=" + (shipmentItemId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        shipmentReceipt.setOrder(order);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long orderId = order.getId();
        // Get all the shipmentReceiptList where order equals to orderId
        defaultShipmentReceiptShouldBeFound("orderId.equals=" + orderId);

        // Get all the shipmentReceiptList where order equals to (orderId + 1)
        defaultShipmentReceiptShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        shipmentReceipt.setOrderItem(orderItem);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long orderItemId = orderItem.getId();
        // Get all the shipmentReceiptList where orderItem equals to orderItemId
        defaultShipmentReceiptShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the shipmentReceiptList where orderItem equals to (orderItemId + 1)
        defaultShipmentReceiptShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        shipmentReceipt.setInventoryItem(inventoryItem);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the shipmentReceiptList where inventoryItem equals to inventoryItemId
        defaultShipmentReceiptShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the shipmentReceiptList where inventoryItem equals to (inventoryItemId + 1)
        defaultShipmentReceiptShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).getFirst();
        }
        em.persist(product);
        em.flush();
        shipmentReceipt.setProduct(product);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long productId = product.getId();
        // Get all the shipmentReceiptList where product equals to productId
        defaultShipmentReceiptShouldBeFound("productId.equals=" + productId);

        // Get all the shipmentReceiptList where product equals to (productId + 1)
        defaultShipmentReceiptShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentReceiptsByReceivedByIsEqualToSomething() throws Exception {
        Party receivedBy;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
            receivedBy = PartyResourceIT.createEntity();
        } else {
            receivedBy = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(receivedBy);
        em.flush();
        shipmentReceipt.setReceivedBy(receivedBy);
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);
        Long receivedById = receivedBy.getId();
        // Get all the shipmentReceiptList where receivedBy equals to receivedById
        defaultShipmentReceiptShouldBeFound("receivedById.equals=" + receivedById);

        // Get all the shipmentReceiptList where receivedBy equals to (receivedById + 1)
        defaultShipmentReceiptShouldNotBeFound("receivedById.equals=" + (receivedById + 1));
    }

    private void defaultShipmentReceiptFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentReceiptShouldBeFound(shouldBeFound);
        defaultShipmentReceiptShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentReceiptShouldBeFound(String filter) throws Exception {
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].quantityAccepted").value(hasItem(sameNumber(DEFAULT_QUANTITY_ACCEPTED))))
            .andExpect(jsonPath("$.[*].quantityRejected").value(hasItem(sameNumber(DEFAULT_QUANTITY_REJECTED))))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));

        // Check, that the count call also returns 1
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentReceiptShouldNotBeFound(String filter) throws Exception {
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentReceipt() throws Exception {
        // Get the shipmentReceipt
        restShipmentReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentReceipt() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentReceipt
        ShipmentReceipt updatedShipmentReceipt = shipmentReceiptRepository.findById(shipmentReceipt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentReceipt are not directly saved in db
        em.detach(updatedShipmentReceipt);
        updatedShipmentReceipt
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .quantityAccepted(UPDATED_QUANTITY_ACCEPTED)
            .quantityRejected(UPDATED_QUANTITY_REJECTED)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .message(UPDATED_MESSAGE);

        restShipmentReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentReceipt))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentReceiptToMatchAllProperties(updatedShipmentReceipt);
    }

    @Test
    @Transactional
    void putNonExistingShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentReceipt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentReceipt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentReceipt using partial update
        ShipmentReceipt partialUpdatedShipmentReceipt = new ShipmentReceipt();
        partialUpdatedShipmentReceipt.setId(shipmentReceipt.getId());

        partialUpdatedShipmentReceipt
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantityAccepted(UPDATED_QUANTITY_ACCEPTED)
            .quantityRejected(UPDATED_QUANTITY_REJECTED)
            .message(UPDATED_MESSAGE);

        restShipmentReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentReceipt))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentReceiptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentReceipt, shipmentReceipt),
            getPersistedShipmentReceipt(shipmentReceipt)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentReceipt using partial update
        ShipmentReceipt partialUpdatedShipmentReceipt = new ShipmentReceipt();
        partialUpdatedShipmentReceipt.setId(shipmentReceipt.getId());

        partialUpdatedShipmentReceipt
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .quantityAccepted(UPDATED_QUANTITY_ACCEPTED)
            .quantityRejected(UPDATED_QUANTITY_REJECTED)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .message(UPDATED_MESSAGE);

        restShipmentReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentReceipt))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentReceiptUpdatableFieldsEquals(
            partialUpdatedShipmentReceipt,
            getPersistedShipmentReceipt(partialUpdatedShipmentReceipt)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentReceipt))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentReceipt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentReceipt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentReceipt() throws Exception {
        // Initialize the database
        insertedShipmentReceipt = shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentReceipt
        restShipmentReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentReceiptRepository.count();
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

    protected ShipmentReceipt getPersistedShipmentReceipt(ShipmentReceipt shipmentReceipt) {
        return shipmentReceiptRepository.findById(shipmentReceipt.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentReceiptToMatchAllProperties(ShipmentReceipt expectedShipmentReceipt) {
        assertShipmentReceiptAllPropertiesEquals(expectedShipmentReceipt, getPersistedShipmentReceipt(expectedShipmentReceipt));
    }

    protected void assertPersistedShipmentReceiptToMatchUpdatableProperties(ShipmentReceipt expectedShipmentReceipt) {
        assertShipmentReceiptAllUpdatablePropertiesEquals(expectedShipmentReceipt, getPersistedShipmentReceipt(expectedShipmentReceipt));
    }
}
