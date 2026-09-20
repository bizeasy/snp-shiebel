package com.snp.web.rest;

import static com.snp.domain.DepartmentMemberAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Department;
import com.snp.domain.DepartmentMember;
import com.snp.domain.Party;
import com.snp.domain.RoleType;
import com.snp.repository.DepartmentMemberRepository;
import com.snp.service.DepartmentMemberService;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DepartmentMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepartmentMemberResourceIT {

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_THRU_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THRU_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final String ENTITY_API_URL = "/api/department-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartmentMemberRepository departmentMemberRepository;

    @Mock
    private DepartmentMemberRepository departmentMemberRepositoryMock;

    @Mock
    private DepartmentMemberService departmentMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentMemberMockMvc;

    private DepartmentMember departmentMember;

    private DepartmentMember insertedDepartmentMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentMember createEntity(EntityManager em) {
        DepartmentMember departmentMember = new DepartmentMember()
            .fromDate(DEFAULT_FROM_DATE)
            .thruDate(DEFAULT_THRU_DATE)
            .isPrimary(DEFAULT_IS_PRIMARY);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity();
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).getFirst();
        }
        departmentMember.setDepartment(department);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        departmentMember.setParty(party);
        return departmentMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentMember createUpdatedEntity(EntityManager em) {
        DepartmentMember updatedDepartmentMember = new DepartmentMember()
            .fromDate(UPDATED_FROM_DATE)
            .thruDate(UPDATED_THRU_DATE)
            .isPrimary(UPDATED_IS_PRIMARY);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity();
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).getFirst();
        }
        updatedDepartmentMember.setDepartment(department);
        // Add required entity
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity();
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        updatedDepartmentMember.setParty(party);
        return updatedDepartmentMember;
    }

    @BeforeEach
    void initTest() {
        departmentMember = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDepartmentMember != null) {
            departmentMemberRepository.delete(insertedDepartmentMember);
            insertedDepartmentMember = null;
        }
    }

    @Test
    @Transactional
    void createDepartmentMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DepartmentMember
        var returnedDepartmentMember = om.readValue(
            restDepartmentMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentMember)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepartmentMember.class
        );

        // Validate the DepartmentMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartmentMemberUpdatableFieldsEquals(returnedDepartmentMember, getPersistedDepartmentMember(returnedDepartmentMember));

        insertedDepartmentMember = returnedDepartmentMember;
    }

    @Test
    @Transactional
    void createDepartmentMemberWithExistingId() throws Exception {
        // Create the DepartmentMember with an existing ID
        departmentMember.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentMember)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        departmentMember.setFromDate(null);

        // Create the DepartmentMember, which fails.

        restDepartmentMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentMember)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartmentMembers() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(departmentMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartmentMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departmentMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departmentMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartmentMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(departmentMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDepartmentMember() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get the departmentMember
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, departmentMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentMember.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.thruDate").value(DEFAULT_THRU_DATE.toString()))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY));
    }

    @Test
    @Transactional
    void getDepartmentMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        Long id = departmentMember.getId();

        defaultDepartmentMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDepartmentMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDepartmentMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where fromDate equals to
        defaultDepartmentMemberFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where fromDate in
        defaultDepartmentMemberFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where fromDate is not null
        defaultDepartmentMemberFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByThruDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where thruDate equals to
        defaultDepartmentMemberFiltering("thruDate.equals=" + DEFAULT_THRU_DATE, "thruDate.equals=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByThruDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where thruDate in
        defaultDepartmentMemberFiltering("thruDate.in=" + DEFAULT_THRU_DATE + "," + UPDATED_THRU_DATE, "thruDate.in=" + UPDATED_THRU_DATE);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByThruDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where thruDate is not null
        defaultDepartmentMemberFiltering("thruDate.specified=true", "thruDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByIsPrimaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where isPrimary equals to
        defaultDepartmentMemberFiltering("isPrimary.equals=" + DEFAULT_IS_PRIMARY, "isPrimary.equals=" + UPDATED_IS_PRIMARY);
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByIsPrimaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where isPrimary in
        defaultDepartmentMemberFiltering(
            "isPrimary.in=" + DEFAULT_IS_PRIMARY + "," + UPDATED_IS_PRIMARY,
            "isPrimary.in=" + UPDATED_IS_PRIMARY
        );
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByIsPrimaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        // Get all the departmentMemberList where isPrimary is not null
        defaultDepartmentMemberFiltering("isPrimary.specified=true", "isPrimary.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByDepartmentIsEqualToSomething() throws Exception {
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            departmentMemberRepository.saveAndFlush(departmentMember);
            department = DepartmentResourceIT.createEntity();
        } else {
            department = TestUtil.findAll(em, Department.class).getFirst();
        }
        em.persist(department);
        em.flush();
        departmentMember.setDepartment(department);
        departmentMemberRepository.saveAndFlush(departmentMember);
        Long departmentId = department.getId();
        // Get all the departmentMemberList where department equals to departmentId
        defaultDepartmentMemberShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the departmentMemberList where department equals to (departmentId + 1)
        defaultDepartmentMemberShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByPartyIsEqualToSomething() throws Exception {
        Party party;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            departmentMemberRepository.saveAndFlush(departmentMember);
            party = PartyResourceIT.createEntity();
        } else {
            party = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(party);
        em.flush();
        departmentMember.setParty(party);
        departmentMemberRepository.saveAndFlush(departmentMember);
        Long partyId = party.getId();
        // Get all the departmentMemberList where party equals to partyId
        defaultDepartmentMemberShouldBeFound("partyId.equals=" + partyId);

        // Get all the departmentMemberList where party equals to (partyId + 1)
        defaultDepartmentMemberShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentMembersByRoleTypeIsEqualToSomething() throws Exception {
        RoleType roleType;
        if (TestUtil.findAll(em, RoleType.class).isEmpty()) {
            departmentMemberRepository.saveAndFlush(departmentMember);
            roleType = RoleTypeResourceIT.createEntity();
        } else {
            roleType = TestUtil.findAll(em, RoleType.class).getFirst();
        }
        em.persist(roleType);
        em.flush();
        departmentMember.setRoleType(roleType);
        departmentMemberRepository.saveAndFlush(departmentMember);
        Long roleTypeId = roleType.getId();
        // Get all the departmentMemberList where roleType equals to roleTypeId
        defaultDepartmentMemberShouldBeFound("roleTypeId.equals=" + roleTypeId);

        // Get all the departmentMemberList where roleType equals to (roleTypeId + 1)
        defaultDepartmentMemberShouldNotBeFound("roleTypeId.equals=" + (roleTypeId + 1));
    }

    private void defaultDepartmentMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDepartmentMemberShouldBeFound(shouldBeFound);
        defaultDepartmentMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentMemberShouldBeFound(String filter) throws Exception {
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].thruDate").value(hasItem(DEFAULT_THRU_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY)));

        // Check, that the count call also returns 1
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentMemberShouldNotBeFound(String filter) throws Exception {
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartmentMember() throws Exception {
        // Get the departmentMember
        restDepartmentMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartmentMember() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departmentMember
        DepartmentMember updatedDepartmentMember = departmentMemberRepository.findById(departmentMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartmentMember are not directly saved in db
        em.detach(updatedDepartmentMember);
        updatedDepartmentMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).isPrimary(UPDATED_IS_PRIMARY);

        restDepartmentMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartmentMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDepartmentMember))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartmentMemberToMatchAllProperties(updatedDepartmentMember);
    }

    @Test
    @Transactional
    void putNonExistingDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentMemberWithPatch() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departmentMember using partial update
        DepartmentMember partialUpdatedDepartmentMember = new DepartmentMember();
        partialUpdatedDepartmentMember.setId(departmentMember.getId());

        partialUpdatedDepartmentMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).isPrimary(UPDATED_IS_PRIMARY);

        restDepartmentMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartmentMember))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartmentMember, departmentMember),
            getPersistedDepartmentMember(departmentMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartmentMemberWithPatch() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departmentMember using partial update
        DepartmentMember partialUpdatedDepartmentMember = new DepartmentMember();
        partialUpdatedDepartmentMember.setId(departmentMember.getId());

        partialUpdatedDepartmentMember.fromDate(UPDATED_FROM_DATE).thruDate(UPDATED_THRU_DATE).isPrimary(UPDATED_IS_PRIMARY);

        restDepartmentMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartmentMember))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentMemberUpdatableFieldsEquals(
            partialUpdatedDepartmentMember,
            getPersistedDepartmentMember(partialUpdatedDepartmentMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentMember))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartmentMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departmentMember.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departmentMember)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartmentMember() throws Exception {
        // Initialize the database
        insertedDepartmentMember = departmentMemberRepository.saveAndFlush(departmentMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departmentMember
        restDepartmentMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, departmentMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departmentMemberRepository.count();
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

    protected DepartmentMember getPersistedDepartmentMember(DepartmentMember departmentMember) {
        return departmentMemberRepository.findById(departmentMember.getId()).orElseThrow();
    }

    protected void assertPersistedDepartmentMemberToMatchAllProperties(DepartmentMember expectedDepartmentMember) {
        assertDepartmentMemberAllPropertiesEquals(expectedDepartmentMember, getPersistedDepartmentMember(expectedDepartmentMember));
    }

    protected void assertPersistedDepartmentMemberToMatchUpdatableProperties(DepartmentMember expectedDepartmentMember) {
        assertDepartmentMemberAllUpdatablePropertiesEquals(
            expectedDepartmentMember,
            getPersistedDepartmentMember(expectedDepartmentMember)
        );
    }
}
