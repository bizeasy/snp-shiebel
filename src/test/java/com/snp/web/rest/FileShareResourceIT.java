package com.snp.web.rest;

import static com.snp.domain.FileShareAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.FileFolder;
import com.snp.domain.FileShare;
import com.snp.domain.Party;
import com.snp.domain.enumeration.SharePermission;
import com.snp.repository.FileShareRepository;
import com.snp.service.FileShareService;
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
 * Integration tests for the {@link FileShareResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FileShareResourceIT {

    private static final SharePermission DEFAULT_PERMISSION = SharePermission.VIEW;
    private static final SharePermission UPDATED_PERMISSION = SharePermission.COMMENT;

    private static final String DEFAULT_SHARED_WITH_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SHARED_WITH_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHARED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHARED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REVOKED = false;
    private static final Boolean UPDATED_IS_REVOKED = true;

    private static final String ENTITY_API_URL = "/api/file-shares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileShareRepository fileShareRepository;

    @Mock
    private FileShareRepository fileShareRepositoryMock;

    @Mock
    private FileShareService fileShareServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileShareMockMvc;

    private FileShare fileShare;

    private FileShare insertedFileShare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileShare createEntity() {
        return new FileShare()
            .permission(DEFAULT_PERMISSION)
            .sharedWithEmail(DEFAULT_SHARED_WITH_EMAIL)
            .sharedDate(DEFAULT_SHARED_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .isRevoked(DEFAULT_IS_REVOKED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileShare createUpdatedEntity() {
        return new FileShare()
            .permission(UPDATED_PERMISSION)
            .sharedWithEmail(UPDATED_SHARED_WITH_EMAIL)
            .sharedDate(UPDATED_SHARED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .isRevoked(UPDATED_IS_REVOKED);
    }

    @BeforeEach
    void initTest() {
        fileShare = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFileShare != null) {
            fileShareRepository.delete(insertedFileShare);
            insertedFileShare = null;
        }
    }

    @Test
    @Transactional
    void createFileShare() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FileShare
        var returnedFileShare = om.readValue(
            restFileShareMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FileShare.class
        );

        // Validate the FileShare in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFileShareUpdatableFieldsEquals(returnedFileShare, getPersistedFileShare(returnedFileShare));

        insertedFileShare = returnedFileShare;
    }

    @Test
    @Transactional
    void createFileShareWithExistingId() throws Exception {
        // Create the FileShare with an existing ID
        fileShare.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare)))
            .andExpect(status().isBadRequest());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPermissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fileShare.setPermission(null);

        // Create the FileShare, which fails.

        restFileShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSharedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fileShare.setSharedDate(null);

        // Create the FileShare, which fails.

        restFileShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileShares() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileShare.getId().intValue())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())))
            .andExpect(jsonPath("$.[*].sharedWithEmail").value(hasItem(DEFAULT_SHARED_WITH_EMAIL)))
            .andExpect(jsonPath("$.[*].sharedDate").value(hasItem(DEFAULT_SHARED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].isRevoked").value(hasItem(DEFAULT_IS_REVOKED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileSharesWithEagerRelationshipsIsEnabled() throws Exception {
        when(fileShareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileShareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileShareServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileSharesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fileShareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileShareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fileShareRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFileShare() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get the fileShare
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL_ID, fileShare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileShare.getId().intValue()))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION.toString()))
            .andExpect(jsonPath("$.sharedWithEmail").value(DEFAULT_SHARED_WITH_EMAIL))
            .andExpect(jsonPath("$.sharedDate").value(DEFAULT_SHARED_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN))
            .andExpect(jsonPath("$.isRevoked").value(DEFAULT_IS_REVOKED));
    }

    @Test
    @Transactional
    void getFileSharesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        Long id = fileShare.getId();

        defaultFileShareFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFileShareFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFileShareFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFileSharesByPermissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where permission equals to
        defaultFileShareFiltering("permission.equals=" + DEFAULT_PERMISSION, "permission.equals=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    void getAllFileSharesByPermissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where permission in
        defaultFileShareFiltering("permission.in=" + DEFAULT_PERMISSION + "," + UPDATED_PERMISSION, "permission.in=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    void getAllFileSharesByPermissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where permission is not null
        defaultFileShareFiltering("permission.specified=true", "permission.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedWithEmail equals to
        defaultFileShareFiltering(
            "sharedWithEmail.equals=" + DEFAULT_SHARED_WITH_EMAIL,
            "sharedWithEmail.equals=" + UPDATED_SHARED_WITH_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedWithEmail in
        defaultFileShareFiltering(
            "sharedWithEmail.in=" + DEFAULT_SHARED_WITH_EMAIL + "," + UPDATED_SHARED_WITH_EMAIL,
            "sharedWithEmail.in=" + UPDATED_SHARED_WITH_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedWithEmail is not null
        defaultFileShareFiltering("sharedWithEmail.specified=true", "sharedWithEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedWithEmail contains
        defaultFileShareFiltering(
            "sharedWithEmail.contains=" + DEFAULT_SHARED_WITH_EMAIL,
            "sharedWithEmail.contains=" + UPDATED_SHARED_WITH_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedWithEmail does not contain
        defaultFileShareFiltering(
            "sharedWithEmail.doesNotContain=" + UPDATED_SHARED_WITH_EMAIL,
            "sharedWithEmail.doesNotContain=" + DEFAULT_SHARED_WITH_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedDate equals to
        defaultFileShareFiltering("sharedDate.equals=" + DEFAULT_SHARED_DATE, "sharedDate.equals=" + UPDATED_SHARED_DATE);
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedDate in
        defaultFileShareFiltering(
            "sharedDate.in=" + DEFAULT_SHARED_DATE + "," + UPDATED_SHARED_DATE,
            "sharedDate.in=" + UPDATED_SHARED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where sharedDate is not null
        defaultFileShareFiltering("sharedDate.specified=true", "sharedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where expiryDate equals to
        defaultFileShareFiltering("expiryDate.equals=" + DEFAULT_EXPIRY_DATE, "expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllFileSharesByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where expiryDate in
        defaultFileShareFiltering(
            "expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE,
            "expiryDate.in=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllFileSharesByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where expiryDate is not null
        defaultFileShareFiltering("expiryDate.specified=true", "expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesByAccessTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where accessToken equals to
        defaultFileShareFiltering("accessToken.equals=" + DEFAULT_ACCESS_TOKEN, "accessToken.equals=" + UPDATED_ACCESS_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileSharesByAccessTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where accessToken in
        defaultFileShareFiltering(
            "accessToken.in=" + DEFAULT_ACCESS_TOKEN + "," + UPDATED_ACCESS_TOKEN,
            "accessToken.in=" + UPDATED_ACCESS_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllFileSharesByAccessTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where accessToken is not null
        defaultFileShareFiltering("accessToken.specified=true", "accessToken.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesByAccessTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where accessToken contains
        defaultFileShareFiltering("accessToken.contains=" + DEFAULT_ACCESS_TOKEN, "accessToken.contains=" + UPDATED_ACCESS_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileSharesByAccessTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where accessToken does not contain
        defaultFileShareFiltering(
            "accessToken.doesNotContain=" + UPDATED_ACCESS_TOKEN,
            "accessToken.doesNotContain=" + DEFAULT_ACCESS_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllFileSharesByIsRevokedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where isRevoked equals to
        defaultFileShareFiltering("isRevoked.equals=" + DEFAULT_IS_REVOKED, "isRevoked.equals=" + UPDATED_IS_REVOKED);
    }

    @Test
    @Transactional
    void getAllFileSharesByIsRevokedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where isRevoked in
        defaultFileShareFiltering("isRevoked.in=" + DEFAULT_IS_REVOKED + "," + UPDATED_IS_REVOKED, "isRevoked.in=" + UPDATED_IS_REVOKED);
    }

    @Test
    @Transactional
    void getAllFileSharesByIsRevokedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        // Get all the fileShareList where isRevoked is not null
        defaultFileShareFiltering("isRevoked.specified=true", "isRevoked.specified=false");
    }

    @Test
    @Transactional
    void getAllFileSharesByFolderIsEqualToSomething() throws Exception {
        FileFolder folder;
        if (TestUtil.findAll(em, FileFolder.class).isEmpty()) {
            fileShareRepository.saveAndFlush(fileShare);
            folder = FileFolderResourceIT.createEntity();
        } else {
            folder = TestUtil.findAll(em, FileFolder.class).getFirst();
        }
        em.persist(folder);
        em.flush();
        fileShare.setFolder(folder);
        fileShareRepository.saveAndFlush(fileShare);
        Long folderId = folder.getId();
        // Get all the fileShareList where folder equals to folderId
        defaultFileShareShouldBeFound("folderId.equals=" + folderId);

        // Get all the fileShareList where folder equals to (folderId + 1)
        defaultFileShareShouldNotBeFound("folderId.equals=" + (folderId + 1));
    }

    @Test
    @Transactional
    void getAllFileSharesByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            fileShareRepository.saveAndFlush(fileShare);
            attachment = AttachmentResourceIT.createEntity();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).getFirst();
        }
        em.persist(attachment);
        em.flush();
        fileShare.setAttachment(attachment);
        fileShareRepository.saveAndFlush(fileShare);
        Long attachmentId = attachment.getId();
        // Get all the fileShareList where attachment equals to attachmentId
        defaultFileShareShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the fileShareList where attachment equals to (attachmentId + 1)
        defaultFileShareShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedWithIsEqualToSomething() throws Exception {
        Party sharedWith;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            fileShareRepository.saveAndFlush(fileShare);
            sharedWith = PartyResourceIT.createEntity();
        } else {
            sharedWith = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(sharedWith);
        em.flush();
        fileShare.setSharedWith(sharedWith);
        fileShareRepository.saveAndFlush(fileShare);
        Long sharedWithId = sharedWith.getId();
        // Get all the fileShareList where sharedWith equals to sharedWithId
        defaultFileShareShouldBeFound("sharedWithId.equals=" + sharedWithId);

        // Get all the fileShareList where sharedWith equals to (sharedWithId + 1)
        defaultFileShareShouldNotBeFound("sharedWithId.equals=" + (sharedWithId + 1));
    }

    @Test
    @Transactional
    void getAllFileSharesBySharedByIsEqualToSomething() throws Exception {
        Party sharedBy;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            fileShareRepository.saveAndFlush(fileShare);
            sharedBy = PartyResourceIT.createEntity();
        } else {
            sharedBy = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(sharedBy);
        em.flush();
        fileShare.setSharedBy(sharedBy);
        fileShareRepository.saveAndFlush(fileShare);
        Long sharedById = sharedBy.getId();
        // Get all the fileShareList where sharedBy equals to sharedById
        defaultFileShareShouldBeFound("sharedById.equals=" + sharedById);

        // Get all the fileShareList where sharedBy equals to (sharedById + 1)
        defaultFileShareShouldNotBeFound("sharedById.equals=" + (sharedById + 1));
    }

    private void defaultFileShareFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFileShareShouldBeFound(shouldBeFound);
        defaultFileShareShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileShareShouldBeFound(String filter) throws Exception {
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileShare.getId().intValue())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())))
            .andExpect(jsonPath("$.[*].sharedWithEmail").value(hasItem(DEFAULT_SHARED_WITH_EMAIL)))
            .andExpect(jsonPath("$.[*].sharedDate").value(hasItem(DEFAULT_SHARED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].isRevoked").value(hasItem(DEFAULT_IS_REVOKED)));

        // Check, that the count call also returns 1
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileShareShouldNotBeFound(String filter) throws Exception {
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileShareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFileShare() throws Exception {
        // Get the fileShare
        restFileShareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFileShare() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileShare
        FileShare updatedFileShare = fileShareRepository.findById(fileShare.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFileShare are not directly saved in db
        em.detach(updatedFileShare);
        updatedFileShare
            .permission(UPDATED_PERMISSION)
            .sharedWithEmail(UPDATED_SHARED_WITH_EMAIL)
            .sharedDate(UPDATED_SHARED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .isRevoked(UPDATED_IS_REVOKED);

        restFileShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileShare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFileShare))
            )
            .andExpect(status().isOk());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileShareToMatchAllProperties(updatedFileShare);
    }

    @Test
    @Transactional
    void putNonExistingFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileShare.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fileShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileShare)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileShareWithPatch() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileShare using partial update
        FileShare partialUpdatedFileShare = new FileShare();
        partialUpdatedFileShare.setId(fileShare.getId());

        partialUpdatedFileShare.expiryDate(UPDATED_EXPIRY_DATE).isRevoked(UPDATED_IS_REVOKED);

        restFileShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFileShare))
            )
            .andExpect(status().isOk());

        // Validate the FileShare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileShareUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFileShare, fileShare),
            getPersistedFileShare(fileShare)
        );
    }

    @Test
    @Transactional
    void fullUpdateFileShareWithPatch() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fileShare using partial update
        FileShare partialUpdatedFileShare = new FileShare();
        partialUpdatedFileShare.setId(fileShare.getId());

        partialUpdatedFileShare
            .permission(UPDATED_PERMISSION)
            .sharedWithEmail(UPDATED_SHARED_WITH_EMAIL)
            .sharedDate(UPDATED_SHARED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .isRevoked(UPDATED_IS_REVOKED);

        restFileShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFileShare))
            )
            .andExpect(status().isOk());

        // Validate the FileShare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileShareUpdatableFieldsEquals(partialUpdatedFileShare, getPersistedFileShare(partialUpdatedFileShare));
    }

    @Test
    @Transactional
    void patchNonExistingFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileShare))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileShare.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileShareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileShare)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileShare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileShare() throws Exception {
        // Initialize the database
        insertedFileShare = fileShareRepository.saveAndFlush(fileShare);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fileShare
        restFileShareMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileShare.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fileShareRepository.count();
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

    protected FileShare getPersistedFileShare(FileShare fileShare) {
        return fileShareRepository.findById(fileShare.getId()).orElseThrow();
    }

    protected void assertPersistedFileShareToMatchAllProperties(FileShare expectedFileShare) {
        assertFileShareAllPropertiesEquals(expectedFileShare, getPersistedFileShare(expectedFileShare));
    }

    protected void assertPersistedFileShareToMatchUpdatableProperties(FileShare expectedFileShare) {
        assertFileShareAllUpdatablePropertiesEquals(expectedFileShare, getPersistedFileShare(expectedFileShare));
    }
}
