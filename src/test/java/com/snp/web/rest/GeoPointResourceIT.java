package com.snp.web.rest;

import static com.snp.domain.GeoPointAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.GeoPoint;
import com.snp.repository.GeoPointRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link GeoPointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeoPointResourceIT {

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/geo-points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoPointRepository geoPointRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoPointMockMvc;

    private GeoPoint geoPoint;

    private GeoPoint insertedGeoPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoPoint createEntity() {
        return new GeoPoint().latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoPoint createUpdatedEntity() {
        return new GeoPoint().latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
    }

    @BeforeEach
    void initTest() {
        geoPoint = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGeoPoint != null) {
            geoPointRepository.delete(insertedGeoPoint);
            insertedGeoPoint = null;
        }
    }

    @Test
    @Transactional
    void createGeoPoint() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GeoPoint
        var returnedGeoPoint = om.readValue(
            restGeoPointMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoPoint)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GeoPoint.class
        );

        // Validate the GeoPoint in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGeoPointUpdatableFieldsEquals(returnedGeoPoint, getPersistedGeoPoint(returnedGeoPoint));

        insertedGeoPoint = returnedGeoPoint;
    }

    @Test
    @Transactional
    void createGeoPointWithExistingId() throws Exception {
        // Create the GeoPoint with an existing ID
        geoPoint.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoPoint)))
            .andExpect(status().isBadRequest());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGeoPoints() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));
    }

    @Test
    @Transactional
    void getGeoPoint() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get the geoPoint
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL_ID, geoPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geoPoint.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getGeoPointsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        Long id = geoPoint.getId();

        defaultGeoPointFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGeoPointFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGeoPointFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude equals to
        defaultGeoPointFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude in
        defaultGeoPointFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude is not null
        defaultGeoPointFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude is greater than or equal to
        defaultGeoPointFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude is less than or equal to
        defaultGeoPointFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude is less than
        defaultGeoPointFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where latitude is greater than
        defaultGeoPointFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude equals to
        defaultGeoPointFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude in
        defaultGeoPointFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude is not null
        defaultGeoPointFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude is greater than or equal to
        defaultGeoPointFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude is less than or equal to
        defaultGeoPointFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude is less than
        defaultGeoPointFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGeoPointsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        // Get all the geoPointList where longitude is greater than
        defaultGeoPointFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    private void defaultGeoPointFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGeoPointShouldBeFound(shouldBeFound);
        defaultGeoPointShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoPointShouldBeFound(String filter) throws Exception {
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));

        // Check, that the count call also returns 1
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGeoPointShouldNotBeFound(String filter) throws Exception {
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGeoPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGeoPoint() throws Exception {
        // Get the geoPoint
        restGeoPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeoPoint() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoPoint
        GeoPoint updatedGeoPoint = geoPointRepository.findById(geoPoint.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeoPoint are not directly saved in db
        em.detach(updatedGeoPoint);
        updatedGeoPoint.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restGeoPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeoPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGeoPoint))
            )
            .andExpect(status().isOk());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoPointToMatchAllProperties(updatedGeoPoint);
    }

    @Test
    @Transactional
    void putNonExistingGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, geoPoint.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoPoint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoPointWithPatch() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoPoint using partial update
        GeoPoint partialUpdatedGeoPoint = new GeoPoint();
        partialUpdatedGeoPoint.setId(geoPoint.getId());

        restGeoPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoPoint))
            )
            .andExpect(status().isOk());

        // Validate the GeoPoint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoPointUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGeoPoint, geoPoint), getPersistedGeoPoint(geoPoint));
    }

    @Test
    @Transactional
    void fullUpdateGeoPointWithPatch() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoPoint using partial update
        GeoPoint partialUpdatedGeoPoint = new GeoPoint();
        partialUpdatedGeoPoint.setId(geoPoint.getId());

        partialUpdatedGeoPoint.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restGeoPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoPoint))
            )
            .andExpect(status().isOk());

        // Validate the GeoPoint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoPointUpdatableFieldsEquals(partialUpdatedGeoPoint, getPersistedGeoPoint(partialUpdatedGeoPoint));
    }

    @Test
    @Transactional
    void patchNonExistingGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, geoPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoPoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeoPoint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoPoint.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoPointMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoPoint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoPoint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeoPoint() throws Exception {
        // Initialize the database
        insertedGeoPoint = geoPointRepository.saveAndFlush(geoPoint);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geoPoint
        restGeoPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, geoPoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoPointRepository.count();
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

    protected GeoPoint getPersistedGeoPoint(GeoPoint geoPoint) {
        return geoPointRepository.findById(geoPoint.getId()).orElseThrow();
    }

    protected void assertPersistedGeoPointToMatchAllProperties(GeoPoint expectedGeoPoint) {
        assertGeoPointAllPropertiesEquals(expectedGeoPoint, getPersistedGeoPoint(expectedGeoPoint));
    }

    protected void assertPersistedGeoPointToMatchUpdatableProperties(GeoPoint expectedGeoPoint) {
        assertGeoPointAllUpdatablePropertiesEquals(expectedGeoPoint, getPersistedGeoPoint(expectedGeoPoint));
    }
}
