package com.snp.web.rest;

import static com.snp.domain.FileFolderAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.FileFolder;
import com.snp.domain.FileFolder;
import com.snp.domain.Party;
import com.snp.repository.FileFolderRepository;
import com.snp.service.FileFolderService;
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
 * Integration tests for the {@link FileFolderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FileFolderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM_FOLDER = false;
    private static final Boolean UPDATED_IS_SYSTEM_FOLDER = true;

    private static final Boolean DEFAULT_IS_TRASHED = false;
    private static final Boolean UPDATED_IS_TRASHED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String ENTITY_API_URL = "/api/file-folders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileFolderRepository fileFolderRepository;

    @Mock
    private FileFolderRepository fileFolderRepositoryMock;

    @Mock
    private FileFolderService fileFolderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileFolderMockMvc;

    private FileFolder fileFolder;

    private FileFolder insertedFileFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileFolder createEntity() {
        return new FileFolder()
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH)
            .isSystemFolder(DEFAULT_IS_SYSTEM_FOLDER)
            .isTrashed(DEFAULT_IS_TRASHED)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileFolder createUpdatedEntity() {
        return new FileFolder()
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .isSystemFolder(UPDATED_IS_SYSTEM_FOLDER)
            .isTrashed(UPDATED_IS_TRASHED)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        fileFolder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFileFolder != null) {
            fileFolderRepository.delete(insertedFileFolder);
            insertedFileFolder = null;
        }
    }

    @Test
    @Transactional
    void createFileFolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FileFolder
        var returnedFileFolder = om.readValue(
            restFileFolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileFolder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FileFolder.class
        );

        // Validate the FileFolder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFileFolderUpdatableFieldsEquals(returnedFileFolder, getPersistedFileFolder(returnedFileFolder));

        insertedFileFolder = returnedFileFolder;
    }

    @Test
    @Transactional
    void createFileFolderWithExistingId() throws Exception {
        // Create the FileFolder with an existing ID
        fileFolder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileFolder)))
            .andExpect(status().isBadRequest());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fileFolder.setName(null);

        // Create the FileFolder, which fails.

        restFileFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileFolder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileFolders() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isSystemFolder").value(hasItem(DEFAULT_IS_SYSTEM_FOLDER)))
            .andExpect(jsonPath("$.[*].isTrashed").value(hasItem(DEFAULT_IS_TRASHED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileFoldersWithEagerRelationshipsIsEnabled() throws Exception {
        when(fileFolderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileFolderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileFolderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileFoldersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fileFolderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileFolderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fileFolderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFileFolder() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get the fileFolder
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL_ID, fileFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileFolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.isSystemFolder").value(DEFAULT_IS_SYSTEM_FOLDER))
            .andExpect(jsonPath("$.isTrashed").value(DEFAULT_IS_TRASHED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFileFoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        Long id = fileFolder.getId();

        defaultFileFolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFileFolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFileFolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFileFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where name equals to
        defaultFileFolderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFileFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where name in
        defaultFileFolderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFileFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where name is not null
        defaultFileFolderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFileFoldersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where name contains
        defaultFileFolderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFileFoldersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where name does not contain
        defaultFileFolderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFileFoldersByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where path equals to
        defaultFileFolderFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFileFoldersByPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where path in
        defaultFileFolderFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFileFoldersByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where path is not null
        defaultFileFolderFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllFileFoldersByPathContainsSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where path contains
        defaultFileFolderFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFileFoldersByPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where path does not contain
        defaultFileFolderFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsSystemFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isSystemFolder equals to
        defaultFileFolderFiltering(
            "isSystemFolder.equals=" + DEFAULT_IS_SYSTEM_FOLDER,
            "isSystemFolder.equals=" + UPDATED_IS_SYSTEM_FOLDER
        );
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsSystemFolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isSystemFolder in
        defaultFileFolderFiltering(
            "isSystemFolder.in=" + DEFAULT_IS_SYSTEM_FOLDER + "," + UPDATED_IS_SYSTEM_FOLDER,
            "isSystemFolder.in=" + UPDATED_IS_SYSTEM_FOLDER
        );
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsSystemFolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isSystemFolder is not null
        defaultFileFolderFiltering("isSystemFolder.specified=true", "isSystemFolder.specified=false");
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsTrashedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isTrashed equals to
        defaultFileFolderFiltering("isTrashed.equals=" + DEFAULT_IS_TRASHED, "isTrashed.equals=" + UPDATED_IS_TRASHED);
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsTrashedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isTrashed in
        defaultFileFolderFiltering("isTrashed.in=" + DEFAULT_IS_TRASHED + "," + UPDATED_IS_TRASHED, "isTrashed.in=" + UPDATED_IS_TRASHED);
    }

    @Test
    @Transactional
    void getAllFileFoldersByIsTrashedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where isTrashed is not null
        defaultFileFolderFiltering("isTrashed.specified=true", "isTrashed.specified=false");
    }

    @Test
    @Transactional
    void getAllFileFoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where createdDate equals to
        defaultFileFolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFileFoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where createdDate in
        defaultFileFolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFileFoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        // Get all the fileFolderList where createdDate is not null
        defaultFileFolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFileFoldersByParentIsEqualToSomething() throws Exception {
        FileFolder parent;
        if (TestUtil.findAll(em, FileFolder.class).isEmpty()) {
            fileFolderRepository.saveAndFlush(fileFolder);
            parent = FileFolderResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, FileFolder.class).getFirst();
        }
        em.persist(parent);
        em.flush();
        fileFolder.setParent(parent);
        fileFolderRepository.saveAndFlush(fileFolder);
        Long parentId = parent.getId();
        // Get all the fileFolderList where parent equals to parentId
        defaultFileFolderShouldBeFound("parentId.equals=" + parentId);

        // Get all the fileFolderList where parent equals to (parentId + 1)
        defaultFileFolderShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllFileFoldersByOwnerIsEqualToSomething() throws Exception {
        Party owner;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            fileFolderRepository.saveAndFlush(fileFolder);
            owner = PartyResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(owner);
        em.flush();
        fileFolder.setOwner(owner);
        fileFolderRepository.saveAndFlush(fileFolder);
        Long ownerId = owner.getId();
        // Get all the fileFolderList where owner equals to ownerId
        defaultFileFolderShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the fileFolderList where owner equals to (ownerId + 1)
        defaultFileFolderShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    private void defaultFileFolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFileFolderShouldBeFound(shouldBeFound);
        defaultFileFolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileFolderShouldBeFound(String filter) throws Exception {
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isSystemFolder").value(hasItem(DEFAULT_IS_SYSTEM_FOLDER)))
            .andExpect(jsonPath("$.[*].isTrashed").value(hasItem(DEFAULT_IS_TRASHED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileFolderShouldNotBeFound(String filter) throws Exception {
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFileFolder() throws Exception {
        // Get the fileFolder
        restFileFolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFileFolder() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileFolder
        FileFolder updatedFileFolder = fileFolderRepository.findById(fileFolder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFileFolder are not directly saved in db
        em.detach(updatedFileFolder);
        updatedFileFolder
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .isSystemFolder(UPDATED_IS_SYSTEM_FOLDER)
            .isTrashed(UPDATED_IS_TRASHED)
            .createdDate(UPDATED_CREATED_DATE);

        restFileFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileFolder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFileFolder))
            )
            .andExpect(status().isOk());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileFolderToMatchAllProperties(updatedFileFolder);
    }

    @Test
    @Transactional
    void putNonExistingFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileFolder.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileFolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fileFolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileFolder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileFolderWithPatch() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileFolder using partial update
        FileFolder partialUpdatedFileFolder = new FileFolder();
        partialUpdatedFileFolder.setId(fileFolder.getId());

        partialUpdatedFileFolder.name(UPDATED_NAME).path(UPDATED_PATH).isTrashed(UPDATED_IS_TRASHED);

        restFileFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFileFolder))
            )
            .andExpect(status().isOk());

        // Validate the FileFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileFolderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFileFolder, fileFolder),
            getPersistedFileFolder(fileFolder)
        );
    }

    @Test
    @Transactional
    void fullUpdateFileFolderWithPatch() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileFolder using partial update
        FileFolder partialUpdatedFileFolder = new FileFolder();
        partialUpdatedFileFolder.setId(fileFolder.getId());

        partialUpdatedFileFolder
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .isSystemFolder(UPDATED_IS_SYSTEM_FOLDER)
            .isTrashed(UPDATED_IS_TRASHED)
            .createdDate(UPDATED_CREATED_DATE);

        restFileFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFileFolder))
            )
            .andExpect(status().isOk());

        // Validate the FileFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileFolderUpdatableFieldsEquals(partialUpdatedFileFolder, getPersistedFileFolder(partialUpdatedFileFolder));
    }

    @Test
    @Transactional
    void patchNonExistingFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileFolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileFolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileFolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileFolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileFolder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileFolder() throws Exception {
        // Initialize the database
        insertedFileFolder = fileFolderRepository.saveAndFlush(fileFolder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fileFolder
        restFileFolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileFolder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fileFolderRepository.count();
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

    protected FileFolder getPersistedFileFolder(FileFolder fileFolder) {
        return fileFolderRepository.findById(fileFolder.getId()).orElseThrow();
    }

    protected void assertPersistedFileFolderToMatchAllProperties(FileFolder expectedFileFolder) {
        assertFileFolderAllPropertiesEquals(expectedFileFolder, getPersistedFileFolder(expectedFileFolder));
    }

    protected void assertPersistedFileFolderToMatchUpdatableProperties(FileFolder expectedFileFolder) {
        assertFileFolderAllUpdatablePropertiesEquals(expectedFileFolder, getPersistedFileFolder(expectedFileFolder));
    }
}
