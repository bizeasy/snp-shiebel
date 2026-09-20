package com.snp.web.rest;

import static com.snp.domain.GeoAssocAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Geo;
import com.snp.domain.GeoAssoc;
import com.snp.domain.GeoAssocType;
import com.snp.repository.GeoAssocRepository;
import com.snp.service.GeoAssocService;
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
 * Integration tests for the {@link GeoAssocResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GeoAssocResourceIT {

    private static final String ENTITY_API_URL = "/api/geo-assocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoAssocRepository geoAssocRepository;

    @Mock
    private GeoAssocRepository geoAssocRepositoryMock;

    @Mock
    private GeoAssocService geoAssocServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoAssocMockMvc;

    private GeoAssoc geoAssoc;

    private GeoAssoc insertedGeoAssoc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoAssoc createEntity(EntityManager em) {
        GeoAssoc geoAssoc = new GeoAssoc();
        // Add required entity
        Geo geo;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            geo = GeoResourceIT.createEntity();
            em.persist(geo);
            em.flush();
        } else {
            geo = TestUtil.findAll(em, Geo.class).getFirst();
        }
        geoAssoc.setGeo(geo);
        // Add required entity
        geoAssoc.setGeoTo(geo);
        return geoAssoc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoAssoc createUpdatedEntity(EntityManager em) {
        GeoAssoc updatedGeoAssoc = new GeoAssoc();
        // Add required entity
        Geo geo;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            geo = GeoResourceIT.createUpdatedEntity();
            em.persist(geo);
            em.flush();
        } else {
            geo = TestUtil.findAll(em, Geo.class).getFirst();
        }
        updatedGeoAssoc.setGeo(geo);
        // Add required entity
        updatedGeoAssoc.setGeoTo(geo);
        return updatedGeoAssoc;
    }

    @BeforeEach
    void initTest() {
        geoAssoc = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedGeoAssoc != null) {
            geoAssocRepository.delete(insertedGeoAssoc);
            insertedGeoAssoc = null;
        }
    }

    @Test
    @Transactional
    void createGeoAssoc() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GeoAssoc
        var returnedGeoAssoc = om.readValue(
            restGeoAssocMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssoc)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GeoAssoc.class
        );

        // Validate the GeoAssoc in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGeoAssocUpdatableFieldsEquals(returnedGeoAssoc, getPersistedGeoAssoc(returnedGeoAssoc));

        insertedGeoAssoc = returnedGeoAssoc;
    }

    @Test
    @Transactional
    void createGeoAssocWithExistingId() throws Exception {
        // Create the GeoAssoc with an existing ID
        geoAssoc.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoAssocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssoc)))
            .andExpect(status().isBadRequest());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGeoAssocs() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        // Get all the geoAssocList
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoAssoc.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeoAssocsWithEagerRelationshipsIsEnabled() throws Exception {
        when(geoAssocServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGeoAssocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(geoAssocServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeoAssocsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(geoAssocServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGeoAssocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(geoAssocRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGeoAssoc() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        // Get the geoAssoc
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL_ID, geoAssoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geoAssoc.getId().intValue()));
    }

    @Test
    @Transactional
    void getGeoAssocsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        Long id = geoAssoc.getId();

        defaultGeoAssocFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGeoAssocFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGeoAssocFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeoAssocsByGeoIsEqualToSomething() throws Exception {
        Geo geo;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            geoAssocRepository.saveAndFlush(geoAssoc);
            geo = GeoResourceIT.createEntity();
        } else {
            geo = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(geo);
        em.flush();
        geoAssoc.setGeo(geo);
        geoAssocRepository.saveAndFlush(geoAssoc);
        Long geoId = geo.getId();
        // Get all the geoAssocList where geo equals to geoId
        defaultGeoAssocShouldBeFound("geoId.equals=" + geoId);

        // Get all the geoAssocList where geo equals to (geoId + 1)
        defaultGeoAssocShouldNotBeFound("geoId.equals=" + (geoId + 1));
    }

    @Test
    @Transactional
    void getAllGeoAssocsByGeoToIsEqualToSomething() throws Exception {
        Geo geoTo;
        if (TestUtil.findAll(em, Geo.class).isEmpty()) {
            geoAssocRepository.saveAndFlush(geoAssoc);
            geoTo = GeoResourceIT.createEntity();
        } else {
            geoTo = TestUtil.findAll(em, Geo.class).getFirst();
        }
        em.persist(geoTo);
        em.flush();
        geoAssoc.setGeoTo(geoTo);
        geoAssocRepository.saveAndFlush(geoAssoc);
        Long geoToId = geoTo.getId();
        // Get all the geoAssocList where geoTo equals to geoToId
        defaultGeoAssocShouldBeFound("geoToId.equals=" + geoToId);

        // Get all the geoAssocList where geoTo equals to (geoToId + 1)
        defaultGeoAssocShouldNotBeFound("geoToId.equals=" + (geoToId + 1));
    }

    @Test
    @Transactional
    void getAllGeoAssocsByGeoAssocTypeIsEqualToSomething() throws Exception {
        GeoAssocType geoAssocType;
        if (TestUtil.findAll(em, GeoAssocType.class).isEmpty()) {
            geoAssocRepository.saveAndFlush(geoAssoc);
            geoAssocType = GeoAssocTypeResourceIT.createEntity();
        } else {
            geoAssocType = TestUtil.findAll(em, GeoAssocType.class).getFirst();
        }
        em.persist(geoAssocType);
        em.flush();
        geoAssoc.setGeoAssocType(geoAssocType);
        geoAssocRepository.saveAndFlush(geoAssoc);
        Long geoAssocTypeId = geoAssocType.getId();
        // Get all the geoAssocList where geoAssocType equals to geoAssocTypeId
        defaultGeoAssocShouldBeFound("geoAssocTypeId.equals=" + geoAssocTypeId);

        // Get all the geoAssocList where geoAssocType equals to (geoAssocTypeId + 1)
        defaultGeoAssocShouldNotBeFound("geoAssocTypeId.equals=" + (geoAssocTypeId + 1));
    }

    private void defaultGeoAssocFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGeoAssocShouldBeFound(shouldBeFound);
        defaultGeoAssocShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoAssocShouldBeFound(String filter) throws Exception {
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoAssoc.getId().intValue())));

        // Check, that the count call also returns 1
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeoAssocShouldNotBeFound(String filter) throws Exception {
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeoAssocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeoAssoc() throws Exception {
        // Get the geoAssoc
        restGeoAssocMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeoAssoc() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssoc
        GeoAssoc updatedGeoAssoc = geoAssocRepository.findById(geoAssoc.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeoAssoc are not directly saved in db
        em.detach(updatedGeoAssoc);

        restGeoAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeoAssoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGeoAssoc))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoAssocToMatchAllProperties(updatedGeoAssoc);
    }

    @Test
    @Transactional
    void putNonExistingGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, geoAssoc.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoAssoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoAssocWithPatch() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssoc using partial update
        GeoAssoc partialUpdatedGeoAssoc = new GeoAssoc();
        partialUpdatedGeoAssoc.setId(geoAssoc.getId());

        restGeoAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoAssoc))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoAssocUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGeoAssoc, geoAssoc), getPersistedGeoAssoc(geoAssoc));
    }

    @Test
    @Transactional
    void fullUpdateGeoAssocWithPatch() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoAssoc using partial update
        GeoAssoc partialUpdatedGeoAssoc = new GeoAssoc();
        partialUpdatedGeoAssoc.setId(geoAssoc.getId());

        restGeoAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoAssoc))
            )
            .andExpect(status().isOk());

        // Validate the GeoAssoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoAssocUpdatableFieldsEquals(partialUpdatedGeoAssoc, getPersistedGeoAssoc(partialUpdatedGeoAssoc));
    }

    @Test
    @Transactional
    void patchNonExistingGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, geoAssoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoAssoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeoAssoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoAssoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoAssocMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoAssoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoAssoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeoAssoc() throws Exception {
        // Initialize the database
        insertedGeoAssoc = geoAssocRepository.saveAndFlush(geoAssoc);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geoAssoc
        restGeoAssocMockMvc
            .perform(delete(ENTITY_API_URL_ID, geoAssoc.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoAssocRepository.count();
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

    protected GeoAssoc getPersistedGeoAssoc(GeoAssoc geoAssoc) {
        return geoAssocRepository.findById(geoAssoc.getId()).orElseThrow();
    }

    protected void assertPersistedGeoAssocToMatchAllProperties(GeoAssoc expectedGeoAssoc) {
        assertGeoAssocAllPropertiesEquals(expectedGeoAssoc, getPersistedGeoAssoc(expectedGeoAssoc));
    }

    protected void assertPersistedGeoAssocToMatchUpdatableProperties(GeoAssoc expectedGeoAssoc) {
        assertGeoAssocAllUpdatablePropertiesEquals(expectedGeoAssoc, getPersistedGeoAssoc(expectedGeoAssoc));
    }
}
