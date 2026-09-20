package com.snp.web.rest;

import static com.snp.domain.ReturnHeaderAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.Facility;
import com.snp.domain.Party;
import com.snp.domain.PaymentMethod;
import com.snp.domain.ReturnHeader;
import com.snp.domain.ReturnHeaderType;
import com.snp.domain.Status;
import com.snp.domain.Uom;
import com.snp.repository.ReturnHeaderRepository;
import com.snp.service.ReturnHeaderService;
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
 * Integration tests for the {@link ReturnHeaderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReturnHeaderResourceIT {

    private static final Boolean DEFAULT_NEEDS_INVENTORY_RECEIVE = false;
    private static final Boolean UPDATED_NEEDS_INVENTORY_RECEIVE = true;

    private static final String DEFAULT_SUPPLIER_RMA_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_RMA_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/return-headers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReturnHeaderRepository returnHeaderRepository;

    @Mock
    private ReturnHeaderRepository returnHeaderRepositoryMock;

    @Mock
    private ReturnHeaderService returnHeaderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnHeaderMockMvc;

    private ReturnHeader returnHeader;

    private ReturnHeader insertedReturnHeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnHeader createEntity() {
        return new ReturnHeader().needsInventoryReceive(DEFAULT_NEEDS_INVENTORY_RECEIVE).supplierRmaId(DEFAULT_SUPPLIER_RMA_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnHeader createUpdatedEntity() {
        return new ReturnHeader().needsInventoryReceive(UPDATED_NEEDS_INVENTORY_RECEIVE).supplierRmaId(UPDATED_SUPPLIER_RMA_ID);
    }

    @BeforeEach
    void initTest() {
        returnHeader = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReturnHeader != null) {
            returnHeaderRepository.delete(insertedReturnHeader);
            insertedReturnHeader = null;
        }
    }

    @Test
    @Transactional
    void createReturnHeader() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReturnHeader
        var returnedReturnHeader = om.readValue(
            restReturnHeaderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeader)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReturnHeader.class
        );

        // Validate the ReturnHeader in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReturnHeaderUpdatableFieldsEquals(returnedReturnHeader, getPersistedReturnHeader(returnedReturnHeader));

        insertedReturnHeader = returnedReturnHeader;
    }

    @Test
    @Transactional
    void createReturnHeaderWithExistingId() throws Exception {
        // Create the ReturnHeader with an existing ID
        returnHeader.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnHeaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeader)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReturnHeaders() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].needsInventoryReceive").value(hasItem(DEFAULT_NEEDS_INVENTORY_RECEIVE)))
            .andExpect(jsonPath("$.[*].supplierRmaId").value(hasItem(DEFAULT_SUPPLIER_RMA_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReturnHeadersWithEagerRelationshipsIsEnabled() throws Exception {
        when(returnHeaderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReturnHeaderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(returnHeaderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReturnHeadersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(returnHeaderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReturnHeaderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(returnHeaderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReturnHeader() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get the returnHeader
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, returnHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnHeader.getId().intValue()))
            .andExpect(jsonPath("$.needsInventoryReceive").value(DEFAULT_NEEDS_INVENTORY_RECEIVE))
            .andExpect(jsonPath("$.supplierRmaId").value(DEFAULT_SUPPLIER_RMA_ID));
    }

    @Test
    @Transactional
    void getReturnHeadersByIdFiltering() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        Long id = returnHeader.getId();

        defaultReturnHeaderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReturnHeaderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReturnHeaderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReturnHeadersByNeedsInventoryReceiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where needsInventoryReceive equals to
        defaultReturnHeaderFiltering(
            "needsInventoryReceive.equals=" + DEFAULT_NEEDS_INVENTORY_RECEIVE,
            "needsInventoryReceive.equals=" + UPDATED_NEEDS_INVENTORY_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllReturnHeadersByNeedsInventoryReceiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where needsInventoryReceive in
        defaultReturnHeaderFiltering(
            "needsInventoryReceive.in=" + DEFAULT_NEEDS_INVENTORY_RECEIVE + "," + UPDATED_NEEDS_INVENTORY_RECEIVE,
            "needsInventoryReceive.in=" + UPDATED_NEEDS_INVENTORY_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllReturnHeadersByNeedsInventoryReceiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where needsInventoryReceive is not null
        defaultReturnHeaderFiltering("needsInventoryReceive.specified=true", "needsInventoryReceive.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnHeadersBySupplierRmaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where supplierRmaId equals to
        defaultReturnHeaderFiltering("supplierRmaId.equals=" + DEFAULT_SUPPLIER_RMA_ID, "supplierRmaId.equals=" + UPDATED_SUPPLIER_RMA_ID);
    }

    @Test
    @Transactional
    void getAllReturnHeadersBySupplierRmaIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where supplierRmaId in
        defaultReturnHeaderFiltering(
            "supplierRmaId.in=" + DEFAULT_SUPPLIER_RMA_ID + "," + UPDATED_SUPPLIER_RMA_ID,
            "supplierRmaId.in=" + UPDATED_SUPPLIER_RMA_ID
        );
    }

    @Test
    @Transactional
    void getAllReturnHeadersBySupplierRmaIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where supplierRmaId is not null
        defaultReturnHeaderFiltering("supplierRmaId.specified=true", "supplierRmaId.specified=false");
    }

    @Test
    @Transactional
    void getAllReturnHeadersBySupplierRmaIdContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where supplierRmaId contains
        defaultReturnHeaderFiltering(
            "supplierRmaId.contains=" + DEFAULT_SUPPLIER_RMA_ID,
            "supplierRmaId.contains=" + UPDATED_SUPPLIER_RMA_ID
        );
    }

    @Test
    @Transactional
    void getAllReturnHeadersBySupplierRmaIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        // Get all the returnHeaderList where supplierRmaId does not contain
        defaultReturnHeaderFiltering(
            "supplierRmaId.doesNotContain=" + UPDATED_SUPPLIER_RMA_ID,
            "supplierRmaId.doesNotContain=" + DEFAULT_SUPPLIER_RMA_ID
        );
    }

    @Test
    @Transactional
    void getAllReturnHeadersByReturnTypeIsEqualToSomething() throws Exception {
        ReturnHeaderType returnType;
        if (TestUtil.findAll(em, ReturnHeaderType.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            returnType = ReturnHeaderTypeResourceIT.createEntity();
        } else {
            returnType = TestUtil.findAll(em, ReturnHeaderType.class).getFirst();
        }
        em.persist(returnType);
        em.flush();
        returnHeader.setReturnType(returnType);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long returnTypeId = returnType.getId();
        // Get all the returnHeaderList where returnType equals to returnTypeId
        defaultReturnHeaderShouldBeFound("returnTypeId.equals=" + returnTypeId);

        // Get all the returnHeaderList where returnType equals to (returnTypeId + 1)
        defaultReturnHeaderShouldNotBeFound("returnTypeId.equals=" + (returnTypeId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByStatusIsEqualToSomething() throws Exception {
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            status = StatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, Status.class).getFirst();
        }
        em.persist(status);
        em.flush();
        returnHeader.setStatus(status);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long statusId = status.getId();
        // Get all the returnHeaderList where status equals to statusId
        defaultReturnHeaderShouldBeFound("statusId.equals=" + statusId);

        // Get all the returnHeaderList where status equals to (statusId + 1)
        defaultReturnHeaderShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByFromPartyIsEqualToSomething() throws Exception {
        Party fromParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            fromParty = PartyResourceIT.createEntity();
        } else {
            fromParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(fromParty);
        em.flush();
        returnHeader.setFromParty(fromParty);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long fromPartyId = fromParty.getId();
        // Get all the returnHeaderList where fromParty equals to fromPartyId
        defaultReturnHeaderShouldBeFound("fromPartyId.equals=" + fromPartyId);

        // Get all the returnHeaderList where fromParty equals to (fromPartyId + 1)
        defaultReturnHeaderShouldNotBeFound("fromPartyId.equals=" + (fromPartyId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByToPartyIsEqualToSomething() throws Exception {
        Party toParty;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            toParty = PartyResourceIT.createEntity();
        } else {
            toParty = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(toParty);
        em.flush();
        returnHeader.setToParty(toParty);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long toPartyId = toParty.getId();
        // Get all the returnHeaderList where toParty equals to toPartyId
        defaultReturnHeaderShouldBeFound("toPartyId.equals=" + toPartyId);

        // Get all the returnHeaderList where toParty equals to (toPartyId + 1)
        defaultReturnHeaderShouldNotBeFound("toPartyId.equals=" + (toPartyId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByPaymentMethodIsEqualToSomething() throws Exception {
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            paymentMethod = PaymentMethodResourceIT.createEntity(em);
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).getFirst();
        }
        em.persist(paymentMethod);
        em.flush();
        returnHeader.setPaymentMethod(paymentMethod);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long paymentMethodId = paymentMethod.getId();
        // Get all the returnHeaderList where paymentMethod equals to paymentMethodId
        defaultReturnHeaderShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the returnHeaderList where paymentMethod equals to (paymentMethodId + 1)
        defaultReturnHeaderShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByOriginContactMechIsEqualToSomething() throws Exception {
        ContactMech originContactMech;
        if (TestUtil.findAll(em, ContactMech.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            originContactMech = ContactMechResourceIT.createEntity();
        } else {
            originContactMech = TestUtil.findAll(em, ContactMech.class).getFirst();
        }
        em.persist(originContactMech);
        em.flush();
        returnHeader.setOriginContactMech(originContactMech);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long originContactMechId = originContactMech.getId();
        // Get all the returnHeaderList where originContactMech equals to originContactMechId
        defaultReturnHeaderShouldBeFound("originContactMechId.equals=" + originContactMechId);

        // Get all the returnHeaderList where originContactMech equals to (originContactMechId + 1)
        defaultReturnHeaderShouldNotBeFound("originContactMechId.equals=" + (originContactMechId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByDestinationFacilityIsEqualToSomething() throws Exception {
        Facility destinationFacility;
        if (TestUtil.findAll(em, Facility.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            destinationFacility = FacilityResourceIT.createEntity();
        } else {
            destinationFacility = TestUtil.findAll(em, Facility.class).getFirst();
        }
        em.persist(destinationFacility);
        em.flush();
        returnHeader.setDestinationFacility(destinationFacility);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long destinationFacilityId = destinationFacility.getId();
        // Get all the returnHeaderList where destinationFacility equals to destinationFacilityId
        defaultReturnHeaderShouldBeFound("destinationFacilityId.equals=" + destinationFacilityId);

        // Get all the returnHeaderList where destinationFacility equals to (destinationFacilityId + 1)
        defaultReturnHeaderShouldNotBeFound("destinationFacilityId.equals=" + (destinationFacilityId + 1));
    }

    @Test
    @Transactional
    void getAllReturnHeadersByCurrencyUomIsEqualToSomething() throws Exception {
        Uom currencyUom;
        if (TestUtil.findAll(em, Uom.class).isEmpty()) {
            returnHeaderRepository.saveAndFlush(returnHeader);
            currencyUom = UomResourceIT.createEntity();
        } else {
            currencyUom = TestUtil.findAll(em, Uom.class).getFirst();
        }
        em.persist(currencyUom);
        em.flush();
        returnHeader.setCurrencyUom(currencyUom);
        returnHeaderRepository.saveAndFlush(returnHeader);
        Long currencyUomId = currencyUom.getId();
        // Get all the returnHeaderList where currencyUom equals to currencyUomId
        defaultReturnHeaderShouldBeFound("currencyUomId.equals=" + currencyUomId);

        // Get all the returnHeaderList where currencyUom equals to (currencyUomId + 1)
        defaultReturnHeaderShouldNotBeFound("currencyUomId.equals=" + (currencyUomId + 1));
    }

    private void defaultReturnHeaderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReturnHeaderShouldBeFound(shouldBeFound);
        defaultReturnHeaderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReturnHeaderShouldBeFound(String filter) throws Exception {
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].needsInventoryReceive").value(hasItem(DEFAULT_NEEDS_INVENTORY_RECEIVE)))
            .andExpect(jsonPath("$.[*].supplierRmaId").value(hasItem(DEFAULT_SUPPLIER_RMA_ID)));

        // Check, that the count call also returns 1
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReturnHeaderShouldNotBeFound(String filter) throws Exception {
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReturnHeaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReturnHeader() throws Exception {
        // Get the returnHeader
        restReturnHeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReturnHeader() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeader
        ReturnHeader updatedReturnHeader = returnHeaderRepository.findById(returnHeader.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReturnHeader are not directly saved in db
        em.detach(updatedReturnHeader);
        updatedReturnHeader.needsInventoryReceive(UPDATED_NEEDS_INVENTORY_RECEIVE).supplierRmaId(UPDATED_SUPPLIER_RMA_ID);

        restReturnHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReturnHeader))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReturnHeaderToMatchAllProperties(updatedReturnHeader);
    }

    @Test
    @Transactional
    void putNonExistingReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(returnHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(returnHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnHeaderWithPatch() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeader using partial update
        ReturnHeader partialUpdatedReturnHeader = new ReturnHeader();
        partialUpdatedReturnHeader.setId(returnHeader.getId());

        partialUpdatedReturnHeader.needsInventoryReceive(UPDATED_NEEDS_INVENTORY_RECEIVE);

        restReturnHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnHeader))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeader in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnHeaderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReturnHeader, returnHeader),
            getPersistedReturnHeader(returnHeader)
        );
    }

    @Test
    @Transactional
    void fullUpdateReturnHeaderWithPatch() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the returnHeader using partial update
        ReturnHeader partialUpdatedReturnHeader = new ReturnHeader();
        partialUpdatedReturnHeader.setId(returnHeader.getId());

        partialUpdatedReturnHeader.needsInventoryReceive(UPDATED_NEEDS_INVENTORY_RECEIVE).supplierRmaId(UPDATED_SUPPLIER_RMA_ID);

        restReturnHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReturnHeader))
            )
            .andExpect(status().isOk());

        // Validate the ReturnHeader in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReturnHeaderUpdatableFieldsEquals(partialUpdatedReturnHeader, getPersistedReturnHeader(partialUpdatedReturnHeader));
    }

    @Test
    @Transactional
    void patchNonExistingReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(returnHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnHeader() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        returnHeader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnHeaderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(returnHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnHeader in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnHeader() throws Exception {
        // Initialize the database
        insertedReturnHeader = returnHeaderRepository.saveAndFlush(returnHeader);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the returnHeader
        restReturnHeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnHeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return returnHeaderRepository.count();
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

    protected ReturnHeader getPersistedReturnHeader(ReturnHeader returnHeader) {
        return returnHeaderRepository.findById(returnHeader.getId()).orElseThrow();
    }

    protected void assertPersistedReturnHeaderToMatchAllProperties(ReturnHeader expectedReturnHeader) {
        assertReturnHeaderAllPropertiesEquals(expectedReturnHeader, getPersistedReturnHeader(expectedReturnHeader));
    }

    protected void assertPersistedReturnHeaderToMatchUpdatableProperties(ReturnHeader expectedReturnHeader) {
        assertReturnHeaderAllUpdatablePropertiesEquals(expectedReturnHeader, getPersistedReturnHeader(expectedReturnHeader));
    }
}
