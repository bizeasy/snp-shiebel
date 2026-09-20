package com.snp.web.rest;

import static com.snp.domain.InventoryItemVarianceAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.snp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.InventoryItem;
import com.snp.domain.InventoryItemVariance;
import com.snp.repository.InventoryItemVarianceRepository;
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
 * Integration tests for the {@link InventoryItemVarianceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemVarianceResourceIT {

    private static final String DEFAULT_VARIANCE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_VARIANCE_REASON = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ATP_VAR = new BigDecimal(1);
    private static final BigDecimal UPDATED_ATP_VAR = new BigDecimal(2);
    private static final BigDecimal SMALLER_ATP_VAR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QOH_VAR = new BigDecimal(1);
    private static final BigDecimal UPDATED_QOH_VAR = new BigDecimal(2);
    private static final BigDecimal SMALLER_QOH_VAR = new BigDecimal(1 - 1);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventory-item-variances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemVarianceRepository inventoryItemVarianceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemVarianceMockMvc;

    private InventoryItemVariance inventoryItemVariance;

    private InventoryItemVariance insertedInventoryItemVariance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemVariance createEntity(EntityManager em) {
        InventoryItemVariance inventoryItemVariance = new InventoryItemVariance()
            .varianceReason(DEFAULT_VARIANCE_REASON)
            .atpVar(DEFAULT_ATP_VAR)
            .qohVar(DEFAULT_QOH_VAR)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        inventoryItemVariance.setInventoryItem(inventoryItem);
        return inventoryItemVariance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItemVariance createUpdatedEntity(EntityManager em) {
        InventoryItemVariance updatedInventoryItemVariance = new InventoryItemVariance()
            .varianceReason(UPDATED_VARIANCE_REASON)
            .atpVar(UPDATED_ATP_VAR)
            .qohVar(UPDATED_QOH_VAR)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItem = InventoryItemResourceIT.createUpdatedEntity(em);
            em.persist(inventoryItem);
            em.flush();
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        updatedInventoryItemVariance.setInventoryItem(inventoryItem);
        return updatedInventoryItemVariance;
    }

    @BeforeEach
    void initTest() {
        inventoryItemVariance = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItemVariance != null) {
            inventoryItemVarianceRepository.delete(insertedInventoryItemVariance);
            insertedInventoryItemVariance = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItemVariance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryItemVariance
        var returnedInventoryItemVariance = om.readValue(
            restInventoryItemVarianceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemVariance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItemVariance.class
        );

        // Validate the InventoryItemVariance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInventoryItemVarianceUpdatableFieldsEquals(
            returnedInventoryItemVariance,
            getPersistedInventoryItemVariance(returnedInventoryItemVariance)
        );

        insertedInventoryItemVariance = returnedInventoryItemVariance;
    }

    @Test
    @Transactional
    void createInventoryItemVarianceWithExistingId() throws Exception {
        // Create the InventoryItemVariance with an existing ID
        inventoryItemVariance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemVarianceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemVariance)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariances() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemVariance.getId().intValue())))
            .andExpect(jsonPath("$.[*].varianceReason").value(hasItem(DEFAULT_VARIANCE_REASON)))
            .andExpect(jsonPath("$.[*].atpVar").value(hasItem(sameNumber(DEFAULT_ATP_VAR))))
            .andExpect(jsonPath("$.[*].qohVar").value(hasItem(sameNumber(DEFAULT_QOH_VAR))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getInventoryItemVariance() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get the inventoryItemVariance
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItemVariance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItemVariance.getId().intValue()))
            .andExpect(jsonPath("$.varianceReason").value(DEFAULT_VARIANCE_REASON))
            .andExpect(jsonPath("$.atpVar").value(sameNumber(DEFAULT_ATP_VAR)))
            .andExpect(jsonPath("$.qohVar").value(sameNumber(DEFAULT_QOH_VAR)))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getInventoryItemVariancesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        Long id = inventoryItemVariance.getId();

        defaultInventoryItemVarianceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemVarianceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemVarianceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByVarianceReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where varianceReason equals to
        defaultInventoryItemVarianceFiltering(
            "varianceReason.equals=" + DEFAULT_VARIANCE_REASON,
            "varianceReason.equals=" + UPDATED_VARIANCE_REASON
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByVarianceReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where varianceReason in
        defaultInventoryItemVarianceFiltering(
            "varianceReason.in=" + DEFAULT_VARIANCE_REASON + "," + UPDATED_VARIANCE_REASON,
            "varianceReason.in=" + UPDATED_VARIANCE_REASON
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByVarianceReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where varianceReason is not null
        defaultInventoryItemVarianceFiltering("varianceReason.specified=true", "varianceReason.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByVarianceReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where varianceReason contains
        defaultInventoryItemVarianceFiltering(
            "varianceReason.contains=" + DEFAULT_VARIANCE_REASON,
            "varianceReason.contains=" + UPDATED_VARIANCE_REASON
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByVarianceReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where varianceReason does not contain
        defaultInventoryItemVarianceFiltering(
            "varianceReason.doesNotContain=" + UPDATED_VARIANCE_REASON,
            "varianceReason.doesNotContain=" + DEFAULT_VARIANCE_REASON
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar equals to
        defaultInventoryItemVarianceFiltering("atpVar.equals=" + DEFAULT_ATP_VAR, "atpVar.equals=" + UPDATED_ATP_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar in
        defaultInventoryItemVarianceFiltering("atpVar.in=" + DEFAULT_ATP_VAR + "," + UPDATED_ATP_VAR, "atpVar.in=" + UPDATED_ATP_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar is not null
        defaultInventoryItemVarianceFiltering("atpVar.specified=true", "atpVar.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar is greater than or equal to
        defaultInventoryItemVarianceFiltering(
            "atpVar.greaterThanOrEqual=" + DEFAULT_ATP_VAR,
            "atpVar.greaterThanOrEqual=" + UPDATED_ATP_VAR
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar is less than or equal to
        defaultInventoryItemVarianceFiltering("atpVar.lessThanOrEqual=" + DEFAULT_ATP_VAR, "atpVar.lessThanOrEqual=" + SMALLER_ATP_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar is less than
        defaultInventoryItemVarianceFiltering("atpVar.lessThan=" + UPDATED_ATP_VAR, "atpVar.lessThan=" + DEFAULT_ATP_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByAtpVarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where atpVar is greater than
        defaultInventoryItemVarianceFiltering("atpVar.greaterThan=" + SMALLER_ATP_VAR, "atpVar.greaterThan=" + DEFAULT_ATP_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar equals to
        defaultInventoryItemVarianceFiltering("qohVar.equals=" + DEFAULT_QOH_VAR, "qohVar.equals=" + UPDATED_QOH_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar in
        defaultInventoryItemVarianceFiltering("qohVar.in=" + DEFAULT_QOH_VAR + "," + UPDATED_QOH_VAR, "qohVar.in=" + UPDATED_QOH_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar is not null
        defaultInventoryItemVarianceFiltering("qohVar.specified=true", "qohVar.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar is greater than or equal to
        defaultInventoryItemVarianceFiltering(
            "qohVar.greaterThanOrEqual=" + DEFAULT_QOH_VAR,
            "qohVar.greaterThanOrEqual=" + UPDATED_QOH_VAR
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar is less than or equal to
        defaultInventoryItemVarianceFiltering("qohVar.lessThanOrEqual=" + DEFAULT_QOH_VAR, "qohVar.lessThanOrEqual=" + SMALLER_QOH_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar is less than
        defaultInventoryItemVarianceFiltering("qohVar.lessThan=" + UPDATED_QOH_VAR, "qohVar.lessThan=" + DEFAULT_QOH_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByQohVarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where qohVar is greater than
        defaultInventoryItemVarianceFiltering("qohVar.greaterThan=" + SMALLER_QOH_VAR, "qohVar.greaterThan=" + DEFAULT_QOH_VAR);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where comments equals to
        defaultInventoryItemVarianceFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where comments in
        defaultInventoryItemVarianceFiltering(
            "comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS,
            "comments.in=" + UPDATED_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where comments is not null
        defaultInventoryItemVarianceFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where comments contains
        defaultInventoryItemVarianceFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        // Get all the inventoryItemVarianceList where comments does not contain
        defaultInventoryItemVarianceFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllInventoryItemVariancesByInventoryItemIsEqualToSomething() throws Exception {
        InventoryItem inventoryItem;
        if (TestUtil.findAll(em, InventoryItem.class).isEmpty()) {
            inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);
            inventoryItem = InventoryItemResourceIT.createEntity(em);
        } else {
            inventoryItem = TestUtil.findAll(em, InventoryItem.class).getFirst();
        }
        em.persist(inventoryItem);
        em.flush();
        inventoryItemVariance.setInventoryItem(inventoryItem);
        inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);
        Long inventoryItemId = inventoryItem.getId();
        // Get all the inventoryItemVarianceList where inventoryItem equals to inventoryItemId
        defaultInventoryItemVarianceShouldBeFound("inventoryItemId.equals=" + inventoryItemId);

        // Get all the inventoryItemVarianceList where inventoryItem equals to (inventoryItemId + 1)
        defaultInventoryItemVarianceShouldNotBeFound("inventoryItemId.equals=" + (inventoryItemId + 1));
    }

    private void defaultInventoryItemVarianceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemVarianceShouldBeFound(shouldBeFound);
        defaultInventoryItemVarianceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemVarianceShouldBeFound(String filter) throws Exception {
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItemVariance.getId().intValue())))
            .andExpect(jsonPath("$.[*].varianceReason").value(hasItem(DEFAULT_VARIANCE_REASON)))
            .andExpect(jsonPath("$.[*].atpVar").value(hasItem(sameNumber(DEFAULT_ATP_VAR))))
            .andExpect(jsonPath("$.[*].qohVar").value(hasItem(sameNumber(DEFAULT_QOH_VAR))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemVarianceShouldNotBeFound(String filter) throws Exception {
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemVarianceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItemVariance() throws Exception {
        // Get the inventoryItemVariance
        restInventoryItemVarianceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItemVariance() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemVariance
        InventoryItemVariance updatedInventoryItemVariance = inventoryItemVarianceRepository
            .findById(inventoryItemVariance.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItemVariance are not directly saved in db
        em.detach(updatedInventoryItemVariance);
        updatedInventoryItemVariance
            .varianceReason(UPDATED_VARIANCE_REASON)
            .atpVar(UPDATED_ATP_VAR)
            .qohVar(UPDATED_QOH_VAR)
            .comments(UPDATED_COMMENTS);

        restInventoryItemVarianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventoryItemVariance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInventoryItemVariance))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemVarianceToMatchAllProperties(updatedInventoryItemVariance);
    }

    @Test
    @Transactional
    void putNonExistingInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemVariance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemVariance))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemVariance))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemVariance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemVarianceWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemVariance using partial update
        InventoryItemVariance partialUpdatedInventoryItemVariance = new InventoryItemVariance();
        partialUpdatedInventoryItemVariance.setId(inventoryItemVariance.getId());

        partialUpdatedInventoryItemVariance.varianceReason(UPDATED_VARIANCE_REASON).qohVar(UPDATED_QOH_VAR).comments(UPDATED_COMMENTS);

        restInventoryItemVarianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemVariance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemVariance))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemVariance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemVarianceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItemVariance, inventoryItemVariance),
            getPersistedInventoryItemVariance(inventoryItemVariance)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemVarianceWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItemVariance using partial update
        InventoryItemVariance partialUpdatedInventoryItemVariance = new InventoryItemVariance();
        partialUpdatedInventoryItemVariance.setId(inventoryItemVariance.getId());

        partialUpdatedInventoryItemVariance
            .varianceReason(UPDATED_VARIANCE_REASON)
            .atpVar(UPDATED_ATP_VAR)
            .qohVar(UPDATED_QOH_VAR)
            .comments(UPDATED_COMMENTS);

        restInventoryItemVarianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItemVariance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItemVariance))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItemVariance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemVarianceUpdatableFieldsEquals(
            partialUpdatedInventoryItemVariance,
            getPersistedInventoryItemVariance(partialUpdatedInventoryItemVariance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItemVariance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemVariance))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemVariance))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItemVariance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemVariance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemVarianceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItemVariance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItemVariance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryItemVariance() throws Exception {
        // Initialize the database
        insertedInventoryItemVariance = inventoryItemVarianceRepository.saveAndFlush(inventoryItemVariance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryItemVariance
        restInventoryItemVarianceMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItemVariance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryItemVarianceRepository.count();
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

    protected InventoryItemVariance getPersistedInventoryItemVariance(InventoryItemVariance inventoryItemVariance) {
        return inventoryItemVarianceRepository.findById(inventoryItemVariance.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemVarianceToMatchAllProperties(InventoryItemVariance expectedInventoryItemVariance) {
        assertInventoryItemVarianceAllPropertiesEquals(
            expectedInventoryItemVariance,
            getPersistedInventoryItemVariance(expectedInventoryItemVariance)
        );
    }

    protected void assertPersistedInventoryItemVarianceToMatchUpdatableProperties(InventoryItemVariance expectedInventoryItemVariance) {
        assertInventoryItemVarianceAllUpdatablePropertiesEquals(
            expectedInventoryItemVariance,
            getPersistedInventoryItemVariance(expectedInventoryItemVariance)
        );
    }
}
