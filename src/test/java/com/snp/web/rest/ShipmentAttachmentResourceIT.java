package com.snp.web.rest;

import static com.snp.domain.ShipmentAttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.Shipment;
import com.snp.domain.ShipmentAttachment;
import com.snp.repository.ShipmentAttachmentRepository;
import com.snp.service.ShipmentAttachmentService;
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
 * Integration tests for the {@link ShipmentAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipmentAttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentAttachmentRepository shipmentAttachmentRepository;

    @Mock
    private ShipmentAttachmentRepository shipmentAttachmentRepositoryMock;

    @Mock
    private ShipmentAttachmentService shipmentAttachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentAttachmentMockMvc;

    private ShipmentAttachment shipmentAttachment;

    private ShipmentAttachment insertedShipmentAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentAttachment createEntity(EntityManager em) {
        ShipmentAttachment shipmentAttachment = new ShipmentAttachment().name(DEFAULT_NAME);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        shipmentAttachment.setShipment(shipment);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        shipmentAttachment.setAttachment(attachment);
        return shipmentAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentAttachment createUpdatedEntity(EntityManager em) {
        ShipmentAttachment updatedShipmentAttachment = new ShipmentAttachment().name(UPDATED_NAME);
        // Add required entity
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipment = ShipmentResourceIT.createUpdatedEntity();
            em.persist(shipment);
            em.flush();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        updatedShipmentAttachment.setShipment(shipment);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity();
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        updatedShipmentAttachment.setAttachment(attachment);
        return updatedShipmentAttachment;
    }

    @BeforeEach
    void initTest() {
        shipmentAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShipmentAttachment != null) {
            shipmentAttachmentRepository.delete(insertedShipmentAttachment);
            insertedShipmentAttachment = null;
        }
    }

    @Test
    @Transactional
    void createShipmentAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentAttachment
        var returnedShipmentAttachment = om.readValue(
            restShipmentAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAttachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentAttachment.class
        );

        // Validate the ShipmentAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipmentAttachmentUpdatableFieldsEquals(
            returnedShipmentAttachment,
            getPersistedShipmentAttachment(returnedShipmentAttachment)
        );

        insertedShipmentAttachment = returnedShipmentAttachment;
    }

    @Test
    @Transactional
    void createShipmentAttachmentWithExistingId() throws Exception {
        // Create the ShipmentAttachment with an existing ID
        shipmentAttachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipmentAttachments() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipmentAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipmentAttachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipmentAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipmentAttachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipmentAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipmentAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShipmentAttachment() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get the shipmentAttachment
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentAttachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getShipmentAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        Long id = shipmentAttachment.getId();

        defaultShipmentAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList where name equals to
        defaultShipmentAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList where name in
        defaultShipmentAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList where name is not null
        defaultShipmentAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList where name contains
        defaultShipmentAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        // Get all the shipmentAttachmentList where name does not contain
        defaultShipmentAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByShipmentIsEqualToSomething() throws Exception {
        Shipment shipment;
        if (TestUtil.findAll(em, Shipment.class).isEmpty()) {
            shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);
            shipment = ShipmentResourceIT.createEntity();
        } else {
            shipment = TestUtil.findAll(em, Shipment.class).getFirst();
        }
        em.persist(shipment);
        em.flush();
        shipmentAttachment.setShipment(shipment);
        shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);
        Long shipmentId = shipment.getId();
        // Get all the shipmentAttachmentList where shipment equals to shipmentId
        defaultShipmentAttachmentShouldBeFound("shipmentId.equals=" + shipmentId);

        // Get all the shipmentAttachmentList where shipment equals to (shipmentId + 1)
        defaultShipmentAttachmentShouldNotBeFound("shipmentId.equals=" + (shipmentId + 1));
    }

    @Test
    @Transactional
    void getAllShipmentAttachmentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        shipmentAttachment.setAttachment(attachment);
        shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);
        Long attachmentId = attachment.getId();
        // Get all the shipmentAttachmentList where attachment equals to attachmentId
        defaultShipmentAttachmentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the shipmentAttachmentList where attachment equals to (attachmentId + 1)
        defaultShipmentAttachmentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    private void defaultShipmentAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentAttachmentShouldBeFound(shouldBeFound);
        defaultShipmentAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentAttachmentShouldBeFound(String filter) throws Exception {
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentAttachmentShouldNotBeFound(String filter) throws Exception {
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentAttachment() throws Exception {
        // Get the shipmentAttachment
        restShipmentAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentAttachment() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAttachment
        ShipmentAttachment updatedShipmentAttachment = shipmentAttachmentRepository.findById(shipmentAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentAttachment are not directly saved in db
        em.detach(updatedShipmentAttachment);
        updatedShipmentAttachment.name(UPDATED_NAME);

        restShipmentAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShipmentAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentAttachmentToMatchAllProperties(updatedShipmentAttachment);
    }

    @Test
    @Transactional
    void putNonExistingShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAttachment using partial update
        ShipmentAttachment partialUpdatedShipmentAttachment = new ShipmentAttachment();
        partialUpdatedShipmentAttachment.setId(shipmentAttachment.getId());

        partialUpdatedShipmentAttachment.name(UPDATED_NAME);

        restShipmentAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentAttachment, shipmentAttachment),
            getPersistedShipmentAttachment(shipmentAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAttachment using partial update
        ShipmentAttachment partialUpdatedShipmentAttachment = new ShipmentAttachment();
        partialUpdatedShipmentAttachment.setId(shipmentAttachment.getId());

        partialUpdatedShipmentAttachment.name(UPDATED_NAME);

        restShipmentAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentAttachment))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentAttachmentUpdatableFieldsEquals(
            partialUpdatedShipmentAttachment,
            getPersistedShipmentAttachment(partialUpdatedShipmentAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAttachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentAttachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentAttachment() throws Exception {
        // Initialize the database
        insertedShipmentAttachment = shipmentAttachmentRepository.saveAndFlush(shipmentAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentAttachment
        restShipmentAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentAttachmentRepository.count();
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

    protected ShipmentAttachment getPersistedShipmentAttachment(ShipmentAttachment shipmentAttachment) {
        return shipmentAttachmentRepository.findById(shipmentAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentAttachmentToMatchAllProperties(ShipmentAttachment expectedShipmentAttachment) {
        assertShipmentAttachmentAllPropertiesEquals(expectedShipmentAttachment, getPersistedShipmentAttachment(expectedShipmentAttachment));
    }

    protected void assertPersistedShipmentAttachmentToMatchUpdatableProperties(ShipmentAttachment expectedShipmentAttachment) {
        assertShipmentAttachmentAllUpdatablePropertiesEquals(
            expectedShipmentAttachment,
            getPersistedShipmentAttachment(expectedShipmentAttachment)
        );
    }
}
