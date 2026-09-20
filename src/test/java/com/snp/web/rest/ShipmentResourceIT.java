package com.snp.web.rest;

import static com.snp.domain.ShipmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.Facility;
import com.snp.domain.Order;
import com.snp.domain.OrderItem;
import com.snp.domain.Party;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentMethodType;
import com.snp.domain.ShipmentType;
import com.snp.domain.Status;
import com.snp.repository.ShipmentRepository;
import com.snp.service.ShipmentService;
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
 * Integration tests for the {@link ShipmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipmentResourceIT {

    private static final Instant DEFAULT_ESTIMATED_SHIP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_SHIP_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_ESTIMATED_READY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_READY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_ESTIMATED_ARRIVAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATED_ARRIVAL_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final BigDecimal DEFAULT_ESTIMATED_SHIP_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTIMATED_SHIP_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ESTIMATED_SHIP_COST = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentRepository shipmentRepositoryMock;

    @Mock
    private ShipmentService shipmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentMockMvc;

    private Shipment shipment;

    private Shipment insertedShipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipment createEntity() {
        return new Shipment()
            .estimatedShipDate(DEFAULT_ESTIMATED_SHIP_DATE)
            .estimatedReadyDate(DEFAULT_ESTIMATED_READY_DATE)
            .estimatedArrivalDate(DEFAULT_ESTIMATED_ARRIVAL_DATE)
            .estimatedShipCost(DEFAULT_ESTIMATED_SHIP_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipment createUpdatedEntity() {
        return new Shipment()
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedReadyDate(UPDATED_ESTIMATED_READY_DATE)
            .estimatedArrivalDate(UPDATED_ESTIMATED_ARRIVAL_DATE)
            .estimatedShipCost(UPDATED_ESTIMATED_SHIP_COST);
    }

    @BeforeEach
    void initTest() {
        shipment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShipment != null) {
            shipmentRepository.delete(insertedShipment);
            insertedShipment = null;
        }
    }

    @Test
    @Transactional
    void createShipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Shipment
        var returnedShipment = om.readValue(
            restShipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Shipment.class
        );

        // Validate the Shipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentUpdatableFieldsEquals(returnedShipment, getPersistedShipment(returnedShipment));

        insertedShipment = returnedShipment;
    }

    @Test
    @Transactional
    void createShipmentWithExistingId() throws Exception {
        // Create the Shipment with an existing ID
        shipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipment)))
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipments() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].estimatedShipDate").value(hasItem(DEFAULT_ESTIMATED_SHIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedReadyDate").value(hasItem(DEFAULT_ESTIMATED_READY_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedArrivalDate").value(hasItem(DEFAULT_ESTIMATED_ARRIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedShipCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_SHIP_COST))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShipment() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get the shipment
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, shipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipment.getId().intValue()))
            .andExpect(jsonPath("$.estimatedShipDate").value(DEFAULT_ESTIMATED_SHIP_DATE.toString()))
            .andExpect(jsonPath("$.estimatedReadyDate").value(DEFAULT_ESTIMATED_READY_DATE.toString()))
            .andExpect(jsonPath("$.estimatedArrivalDate").value(DEFAULT_ESTIMATED_ARRIVAL_DATE.toString()))
            .andExpect(jsonPath("$.estimatedShipCost").value(sameNumber(DEFAULT_ESTIMATED_SHIP_COST)));
    }

    @Test
    @Transactional
    void getShipmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        Long id = shipment.getId();

        defaultShipmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipDate equals to
        defaultShipmentFiltering(
            "estimatedShipDate.equals=" + DEFAULT_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.equals=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipDate in
        defaultShipmentFiltering(
            "estimatedShipDate.in=" + DEFAULT_ESTIMATED_SHIP_DATE + "," + UPDATED_ESTIMATED_SHIP_DATE,
            "estimatedShipDate.in=" + UPDATED_ESTIMATED_SHIP_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipDate is not null
        defaultShipmentFiltering("estimatedShipDate.specified=true", "estimatedShipDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedReadyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedReadyDate equals to
        defaultShipmentFiltering(
            "estimatedReadyDate.equals=" + DEFAULT_ESTIMATED_READY_DATE,
            "estimatedReadyDate.equals=" + UPDATED_ESTIMATED_READY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedReadyDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedReadyDate in
        defaultShipmentFiltering(
            "estimatedReadyDate.in=" + DEFAULT_ESTIMATED_READY_DATE + "," + UPDATED_ESTIMATED_READY_DATE,
            "estimatedReadyDate.in=" + UPDATED_ESTIMATED_READY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedReadyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedReadyDate is not null
        defaultShipmentFiltering("estimatedReadyDate.specified=true", "estimatedReadyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedArrivalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedArrivalDate equals to
        defaultShipmentFiltering(
            "estimatedArrivalDate.equals=" + DEFAULT_ESTIMATED_ARRIVAL_DATE,
            "estimatedArrivalDate.equals=" + UPDATED_ESTIMATED_ARRIVAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedArrivalDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedArrivalDate in
        defaultShipmentFiltering(
            "estimatedArrivalDate.in=" + DEFAULT_ESTIMATED_ARRIVAL_DATE + "," + UPDATED_ESTIMATED_ARRIVAL_DATE,
            "estimatedArrivalDate.in=" + UPDATED_ESTIMATED_ARRIVAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedArrivalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedArrivalDate is not null
        defaultShipmentFiltering("estimatedArrivalDate.specified=true", "estimatedArrivalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost equals to
        defaultShipmentFiltering(
            "estimatedShipCost.equals=" + DEFAULT_ESTIMATED_SHIP_COST,
            "estimatedShipCost.equals=" + UPDATED_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost in
        defaultShipmentFiltering(
            "estimatedShipCost.in=" + DEFAULT_ESTIMATED_SHIP_COST + "," + UPDATED_ESTIMATED_SHIP_COST,
            "estimatedShipCost.in=" + UPDATED_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost is not null
        defaultShipmentFiltering("estimatedShipCost.specified=true", "estimatedShipCost.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost is greater than or equal to
        defaultShipmentFiltering(
            "estimatedShipCost.greaterThanOrEqual=" + DEFAULT_ESTIMATED_SHIP_COST,
            "estimatedShipCost.greaterThanOrEqual=" + UPDATED_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost is less than or equal to
        defaultShipmentFiltering(
            "estimatedShipCost.lessThanOrEqual=" + DEFAULT_ESTIMATED_SHIP_COST,
            "estimatedShipCost.lessThanOrEqual=" + SMALLER_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost is less than
        defaultShipmentFiltering(
            "estimatedShipCost.lessThan=" + UPDATED_ESTIMATED_SHIP_COST,
            "estimatedShipCost.lessThan=" + DEFAULT_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByEstimatedShipCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        // Get all the shipmentList where estimatedShipCost is greater than
        defaultShipmentFiltering(
            "estimatedShipCost.greaterThan=" + SMALLER_ESTIMATED_SHIP_COST,
            "estimatedShipCost.greaterThan=" + DEFAULT_ESTIMATED_SHIP_COST
        );
    }

    @Test
    @Transactional
    void getAllShipmentsByShipmentTypeIsEqualToSomething() throws Exception {
        ShipmentType shipmentType;
        if (TestUtil.findAll(em, ShipmentType.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            shipmentType = ShipmentTypeResourceIT.createEntity();
        } else {
            shipmentType = TestUtil.findAll(em, ShipmentType.class).getFirst();
        }
        em.persist(shipmentType);
        em.flush();
        shipment.setShipmentType(shipmentType);
        shipmentRepository.saveAndFlush(shipment);
        Long shipmentTypeId = shipmentType.getId();
        // Get all the shipmentList where shipmentType equals to shipmentTypeId
        defaultShipmentShouldBeFound("shipmentTypeId.equals=" + shipmentTypeId);

        // Get all the shipmentList where shipmentType equals to (shipmentTypeId + 1)
        defaultShipmentShouldNotBeFound("shipmentTypeId.equals=" + (shipmentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByShipmentMethodTypeIsEqualToSomething() throws Exception {
        ShipmentMethodType shipmentMethodType;
        if (TestUtil.findAll(em, ShipmentMethodType.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            shipmentMethodType = ShipmentMethodTypeResourceIT.createEntity();
        } else {
            shipmentMethodType = TestUtil.findAll(em, ShipmentMethodType.class).getFirst();
        }
        em.persist(shipmentMethodType);
        em.flush();
        shipment.setShipmentMethodType(shipmentMethodType);
        shipmentRepository.saveAndFlush(shipment);
        Long shipmentMethodTypeId = shipmentMethodType.getId();
        // Get all the shipmentList where shipmentMethodType equals to shipmentMethodTypeId
        defaultShipmentShouldBeFound("shipmentMethodTypeId.equals=" + shipmentMethodTypeId);

        // Get all the shipmentList where shipmentMethodType equals to (shipmentMethodTypeId + 1)
        defaultShipmentShouldNotBeFound("shipmentMethodTypeId.equals=" + (shipmentMethodTypeId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        shipment.setStatus(status);
        shipmentRepository.saveAndFlush(shipment);
        Long statusId = status.getId();
        // Get all the shipmentList where status equals to statusId
        defaultShipmentShouldBeFound("statusId.equals=" + statusId);

        // Get all the shipmentList where status equals to (statusId + 1)
        defaultShipmentShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).getFirst();
        }
        em.persist(order);
        em.flush();
        shipment.setOrder(order);
        shipmentRepository.saveAndFlush(shipment);
        Long orderId = order.getId();
        // Get all the shipmentList where order equals to orderId
        defaultShipmentShouldBeFound("orderId.equals=" + orderId);

        // Get all the shipmentList where order equals to (orderId + 1)
        defaultShipmentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).getFirst();
        }
        em.persist(orderItem);
        em.flush();
        shipment.setOrderItem(orderItem);
        shipmentRepository.saveAndFlush(shipment);
        Long orderItemId = orderItem.getId();
        // Get all the shipmentList where orderItem equals to orderItemId
        defaultShipmentShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the shipmentList where orderItem equals to (orderItemId + 1)
        defaultShipmentShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByOriginFacilityIsEqualToSomething() throws Exception {
        Facility originFacility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            originFacility = FacilityResourceIT.createEntity();
        } else {
            originFacility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(originFacility);
        em.flush();
        shipment.setOriginFacility(originFacility);
        shipmentRepository.saveAndFlush(shipment);
        Long originFacilityId = originFacility.getId();
        // Get all the shipmentList where originFacility equals to originFacilityId
        defaultShipmentShouldBeFound("originFacilityId.equals=" + originFacilityId);

        // Get all the shipmentList where originFacility equals to (originFacilityId + 1)
        defaultShipmentShouldNotBeFound("originFacilityId.equals=" + (originFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByDestinationFacilityIsEqualToSomething() throws Exception {
        Facility destinationFacility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            destinationFacility = FacilityResourceIT.createEntity();
        } else {
            destinationFacility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(destinationFacility);
        em.flush();
        shipment.setDestinationFacility(destinationFacility);
        shipmentRepository.saveAndFlush(shipment);
        Long destinationFacilityId = destinationFacility.getId();
        // Get all the shipmentList where destinationFacility equals to destinationFacilityId
        defaultShipmentShouldBeFound("destinationFacilityId.equals=" + destinationFacilityId);

        // Get all the shipmentList where destinationFacility equals to (destinationFacilityId + 1)
        defaultShipmentShouldNotBeFound("destinationFacilityId.equals=" + (destinationFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        shipment.setFromParty(fromParty);
        shipmentRepository.saveAndFlush(shipment);
        Long fromPartyId = fromParty.getId();
        // Get all the shipmentList where fromParty equals to fromPartyId
        defaultShipmentShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the shipmentList where fromParty equals to (fromPartyId + 1)
        defaultShipmentShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByToPartyIsEqualToSomething() throws Exception {
        Party toParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            toParty = PartyResourceIT.createEntity();
        } else {
            toParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(toParty);
        em.flush();
        shipment.setToParty(toParty);
        shipmentRepository.saveAndFlush(shipment);
        Long toPartyId = toParty.getId();
        // Get all the shipmentList where toParty equals to toPartyId
        defaultShipmentShouldBeFound("toPartyId.equals=" + toPartyId);

        // Get all the shipmentList where toParty equals to (toPartyId + 1)
        defaultShipmentShouldNotBeFound("toPartyId.equals=" + (toPartyId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByCarrierPartyIsEqualToSomething() throws Exception {
        Party carrierParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            carrierParty = PartyResourceIT.createEntity();
        } else {
            carrierParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(carrierParty);
        em.flush();
        shipment.setCarrierParty(carrierParty);
        shipmentRepository.saveAndFlush(shipment);
        Long carrierPartyId = carrierParty.getId();
        // Get all the shipmentList where carrierParty equals to carrierPartyId
        defaultShipmentShouldBeFound("carrierPartyId.equals=" + carrierPartyId);

        // Get all the shipmentList where carrierParty equals to (carrierPartyId + 1)
        defaultShipmentShouldNotBeFound("carrierPartyId.equals=" + (carrierPartyId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentsByContactMechIsEqualToSomething() throws Exception {
        ContactMech contactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            shipmentRepository.saveAndFlush(shipment);
            contactMech = ContactMechResourceIT.createEntity();
        } else {
            contactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(contactMech);
        em.flush();
        shipment.setContactMech(contactMech);
        shipmentRepository.saveAndFlush(shipment);
        Long contactMechId = contactMech.getId();
        // Get all the shipmentList where contactMech equals to contactMechId
        defaultShipmentShouldBeFound("contactMechId.equals=" + contactMechId);

        // Get all the shipmentList where contactMech equals to (contactMechId + 1)
        defaultShipmentShouldNotBeFound("contactMechId.equals=" + (contactMechId + 1));
    }

    private void defaultShipmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentShouldBeFound(shouldBeFound);
        defaultShipmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentShouldBeFound(String filter) throws Exception {
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].estimatedShipDate").value(hasItem(DEFAULT_ESTIMATED_SHIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedReadyDate").value(hasItem(DEFAULT_ESTIMATED_READY_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedArrivalDate").value(hasItem(DEFAULT_ESTIMATED_ARRIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedShipCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_SHIP_COST))));

        // Check, that the count call also returns 1
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentShouldNotBeFound(String filter) throws Exception {
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipment() throws Exception {
        // Get the shipment
        restShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipment() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipment
        Shipment updatedShipment = shipmentRepository.findById(shipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipment are not directly saved in db
        em.detach(updatedShipment);
        updatedShipment
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedReadyDate(UPDATED_ESTIMATED_READY_DATE)
            .estimatedArrivalDate(UPDATED_ESTIMATED_ARRIVAL_DATE)
            .estimatedShipCost(UPDATED_ESTIMATED_SHIP_COST);

        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentToMatchAllProperties(updatedShipment);
    }

    @Test
    @Transactional
    void putNonExistingShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipment.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipment using partial update
        Shipment partialUpdatedShipment = new Shipment();
        partialUpdatedShipment.setId(shipment.getId());

        partialUpdatedShipment.estimatedShipCost(UPDATED_ESTIMATED_SHIP_COST);

        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShipment, shipment), getPersistedShipment(shipment));
    }

    @Test
    @Transactional
    void fullUpdateShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipment using partial update
        Shipment partialUpdatedShipment = new Shipment();
        partialUpdatedShipment.setId(shipment.getId());

        partialUpdatedShipment
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE)
            .estimatedReadyDate(UPDATED_ESTIMATED_READY_DATE)
            .estimatedArrivalDate(UPDATED_ESTIMATED_ARRIVAL_DATE)
            .estimatedShipCost(UPDATED_ESTIMATED_SHIP_COST);

        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentUpdatableFieldsEquals(partialUpdatedShipment, getPersistedShipment(partialUpdatedShipment));
    }

    @Test
    @Transactional
    void patchNonExistingShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipment() throws Exception {
        // Initialize the database
        insertedShipment = shipmentRepository.saveAndFlush(shipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipment
        restShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentRepository.count();
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

    protected Shipment getPersistedShipment(Shipment shipment) {
        return shipmentRepository.findById(shipment.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentToMatchAllProperties(Shipment expectedShipment) {
        assertShipmentAllPropertiesEquals(expectedShipment, getPersistedShipment(expectedShipment));
    }

    protected void assertPersistedShipmentToMatchUpdatableProperties(Shipment expectedShipment) {
        assertShipmentAllUpdatablePropertiesEquals(expectedShipment, getPersistedShipment(expectedShipment));
    }
}
