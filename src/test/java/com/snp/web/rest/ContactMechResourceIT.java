package com.snp.web.rest;

import static com.snp.domain.ContactMechAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.ContactMech;
import com.snp.domain.ContactMechType;
import com.snp.repository.ContactMechRepository;
import com.snp.service.ContactMechService;
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
 * Integration tests for the {@link ContactMechResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactMechResourceIT {

    private static final String DEFAULT_INFO_STRING = "AAAAAAAAAA";
    private static final String UPDATED_INFO_STRING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-meches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactMechRepository contactMechRepository;

    @Mock
    private ContactMechRepository contactMechRepositoryMock;

    @Mock
    private ContactMechService contactMechServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMechMockMvc;

    private ContactMech contactMech;

    private ContactMech insertedContactMech;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMech createEntity() {
        return new ContactMech().infoString(DEFAULT_INFO_STRING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMech createUpdatedEntity() {
        return new ContactMech().infoString(UPDATED_INFO_STRING);
    }

    @BeforeEach
    void initTest() {
        contactMech = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContactMech != null) {
            contactMechRepository.delete(insertedContactMech);
            insertedContactMech = null;
        }
    }

    @Test
    @Transactional
    void createContactMech() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactMech
        var returnedContactMech = om.readValue(
            restContactMechMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMech)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactMech.class
        );

        // Validate the ContactMech in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContactMechUpdatableFieldsEquals(returnedContactMech, getPersistedContactMech(returnedContactMech));

        insertedContactMech = returnedContactMech;
    }

    @Test
    @Transactional
    void createContactMechWithExistingId() throws Exception {
        // Create the ContactMech with an existing ID
        contactMech.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMechMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMech)))
            .andExpect(status().isBadRequest());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactMeches() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].infoString").value(hasItem(DEFAULT_INFO_STRING)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactMechesWithEagerRelationshipsIsEnabled() throws Exception {
        when(contactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contactMechServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactMechesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contactMechServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactMechMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contactMechRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContactMech() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get the contactMech
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL_ID, contactMech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactMech.getId().intValue()))
            .andExpect(jsonPath("$.infoString").value(DEFAULT_INFO_STRING));
    }

    @Test
    @Transactional
    void getContactMechesByIdFiltering() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        Long id = contactMech.getId();

        defaultContactMechFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContactMechFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContactMechFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactMechesByInfoStringIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList where infoString equals to
        defaultContactMechFiltering("infoString.equals=" + DEFAULT_INFO_STRING, "infoString.equals=" + UPDATED_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllContactMechesByInfoStringIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList where infoString in
        defaultContactMechFiltering(
            "infoString.in=" + DEFAULT_INFO_STRING + "," + UPDATED_INFO_STRING,
            "infoString.in=" + UPDATED_INFO_STRING
        );
    }

    @Test
    @Transactional
    void getAllContactMechesByInfoStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList where infoString is not null
        defaultContactMechFiltering("infoString.specified=true", "infoString.specified=false");
    }

    @Test
    @Transactional
    void getAllContactMechesByInfoStringContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList where infoString contains
        defaultContactMechFiltering("infoString.contains=" + DEFAULT_INFO_STRING, "infoString.contains=" + UPDATED_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllContactMechesByInfoStringNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        // Get all the contactMechList where infoString does not contain
        defaultContactMechFiltering("infoString.doesNotContain=" + UPDATED_INFO_STRING, "infoString.doesNotContain=" + DEFAULT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllContactMechesByContactMechTypeIsEqualToSomething() throws Exception {
        ContactMechType contactMechType;
        if (TestUtil.findAll(em, ContactMechType.class).isEmpty()) {
            contactMechRepository.saveAndFlush(contactMech);
            contactMechType = ContactMechTypeResourceIT.createEntity();
        } else {
            contactMechType = TestUtil.findAll(em, ContactMechType.class).getFirst();
        }
        em.persist(contactMechType);
        em.flush();
        contactMech.setContactMechType(contactMechType);
        contactMechRepository.saveAndFlush(contactMech);
        Long contactMechTypeId = contactMechType.getId();
        // Get all the contactMechList where contactMechType equals to contactMechTypeId
        defaultContactMechShouldBeFound("contactMechTypeId.equals=" + contactMechTypeId);

        // Get all the contactMechList where contactMechType equals to (contactMechTypeId + 1)
        defaultContactMechShouldNotBeFound("contactMechTypeId.equals=" + (contactMechTypeId + 1));
    }

    private void defaultContactMechFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContactMechShouldBeFound(shouldBeFound);
        defaultContactMechShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactMechShouldBeFound(String filter) throws Exception {
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMech.getId().intValue())))
            .andExpect(jsonPath("$.[*].infoString").value(hasItem(DEFAULT_INFO_STRING)));

        // Check, that the count call also returns 1
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactMechShouldNotBeFound(String filter) throws Exception {
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMechMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactMech() throws Exception {
        // Get the contactMech
        restContactMechMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactMech() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMech
        ContactMech updatedContactMech = contactMechRepository.findById(contactMech.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactMech are not directly saved in db
        em.detach(updatedContactMech);
        updatedContactMech.infoString(UPDATED_INFO_STRING);

        restContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContactMech))
            )
            .andExpect(status().isOk());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactMechToMatchAllProperties(updatedContactMech);
    }

    @Test
    @Transactional
    void putNonExistingContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactMech.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMech using partial update
        ContactMech partialUpdatedContactMech = new ContactMech();
        partialUpdatedContactMech.setId(contactMech.getId());

        partialUpdatedContactMech.infoString(UPDATED_INFO_STRING);

        restContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMech))
            )
            .andExpect(status().isOk());

        // Validate the ContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactMech, contactMech),
            getPersistedContactMech(contactMech)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactMechWithPatch() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactMech using partial update
        ContactMech partialUpdatedContactMech = new ContactMech();
        partialUpdatedContactMech.setId(contactMech.getId());

        partialUpdatedContactMech.infoString(UPDATED_INFO_STRING);

        restContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactMech))
            )
            .andExpect(status().isOk());

        // Validate the ContactMech in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactMechUpdatableFieldsEquals(partialUpdatedContactMech, getPersistedContactMech(partialUpdatedContactMech));
    }

    @Test
    @Transactional
    void patchNonExistingContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactMech.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactMech))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactMech() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactMech.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMechMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactMech)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMech in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactMech() throws Exception {
        // Initialize the database
        insertedContactMech = contactMechRepository.saveAndFlush(contactMech);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactMech
        restContactMechMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactMech.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactMechRepository.count();
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

    protected ContactMech getPersistedContactMech(ContactMech contactMech) {
        return contactMechRepository.findById(contactMech.getId()).orElseThrow();
    }

    protected void assertPersistedContactMechToMatchAllProperties(ContactMech expectedContactMech) {
        assertContactMechAllPropertiesEquals(expectedContactMech, getPersistedContactMech(expectedContactMech));
    }

    protected void assertPersistedContactMechToMatchUpdatableProperties(ContactMech expectedContactMech) {
        assertContactMechAllUpdatablePropertiesEquals(expectedContactMech, getPersistedContactMech(expectedContactMech));
    }
}
