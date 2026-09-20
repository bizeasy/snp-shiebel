package com.snp.web.rest;

import static com.snp.domain.AttachmentAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Attachment;
import com.snp.domain.ContentType;
import com.snp.domain.FileFolder;
import com.snp.domain.Party;
import com.snp.repository.AttachmentRepository;
import com.snp.service.AttachmentService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link AttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ATTACHMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final Instant DEFAULT_UPLOADED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_DATE = Instant.ofEpochMilli(1702587125289L);

    private static final Boolean DEFAULT_IS_TRASHED = false;
    private static final Boolean UPDATED_IS_TRASHED = true;

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentRepository attachmentRepositoryMock;

    @Mock
    private AttachmentService attachmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    private Attachment insertedAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createEntity() {
        return new Attachment()
            .name(DEFAULT_NAME)
            .fileAttachment(DEFAULT_FILE_ATTACHMENT)
            .fileAttachmentContentType(DEFAULT_FILE_ATTACHMENT_CONTENT_TYPE)
            .attachmentUrl(DEFAULT_ATTACHMENT_URL)
            .mimeType(DEFAULT_MIME_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .uploadedDate(DEFAULT_UPLOADED_DATE)
            .isTrashed(DEFAULT_IS_TRASHED)
            .checksum(DEFAULT_CHECKSUM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createUpdatedEntity() {
        return new Attachment()
            .name(UPDATED_NAME)
            .fileAttachment(UPDATED_FILE_ATTACHMENT)
            .fileAttachmentContentType(UPDATED_FILE_ATTACHMENT_CONTENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .isTrashed(UPDATED_IS_TRASHED)
            .checksum(UPDATED_CHECKSUM);
    }

    @BeforeEach
    void initTest() {
        attachment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAttachment != null) {
            attachmentRepository.delete(insertedAttachment);
            insertedAttachment = null;
        }
    }

    @Test
    @Transactional
    void createAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Attachment
        var returnedAttachment = om.readValue(
            restAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attachment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Attachment.class
        );

        // Validate the Attachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAttachmentUpdatableFieldsEquals(returnedAttachment, getPersistedAttachment(returnedAttachment));

        insertedAttachment = returnedAttachment;
    }

    @Test
    @Transactional
    void createAttachmentWithExistingId() throws Exception {
        // Create the Attachment with an existing ID
        attachment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attachment)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attachment.setName(null);

        // Create the Attachment, which fails.

        restAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attachment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttachments() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileAttachmentContentType").value(hasItem(DEFAULT_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileAttachment").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL)))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadedDate").value(hasItem(DEFAULT_UPLOADED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isTrashed").value(hasItem(DEFAULT_IS_TRASHED)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(attachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttachment() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileAttachmentContentType").value(DEFAULT_FILE_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileAttachment").value(Base64.getEncoder().encodeToString(DEFAULT_FILE_ATTACHMENT)))
            .andExpect(jsonPath("$.attachmentUrl").value(DEFAULT_ATTACHMENT_URL))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.uploadedDate").value(DEFAULT_UPLOADED_DATE.toString()))
            .andExpect(jsonPath("$.isTrashed").value(DEFAULT_IS_TRASHED))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM));
    }

    @Test
    @Transactional
    void getAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        Long id = attachment.getId();

        defaultAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name equals to
        defaultAttachmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name in
        defaultAttachmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name is not null
        defaultAttachmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name contains
        defaultAttachmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name does not contain
        defaultAttachmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentUrl equals to
        defaultAttachmentFiltering("attachmentUrl.equals=" + DEFAULT_ATTACHMENT_URL, "attachmentUrl.equals=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentUrl in
        defaultAttachmentFiltering(
            "attachmentUrl.in=" + DEFAULT_ATTACHMENT_URL + "," + UPDATED_ATTACHMENT_URL,
            "attachmentUrl.in=" + UPDATED_ATTACHMENT_URL
        );
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentUrl is not null
        defaultAttachmentFiltering("attachmentUrl.specified=true", "attachmentUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentUrl contains
        defaultAttachmentFiltering("attachmentUrl.contains=" + DEFAULT_ATTACHMENT_URL, "attachmentUrl.contains=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentUrl does not contain
        defaultAttachmentFiltering(
            "attachmentUrl.doesNotContain=" + UPDATED_ATTACHMENT_URL,
            "attachmentUrl.doesNotContain=" + DEFAULT_ATTACHMENT_URL
        );
    }

    @Test
    @Transactional
    void getAllAttachmentsByMimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where mimeType equals to
        defaultAttachmentFiltering("mimeType.equals=" + DEFAULT_MIME_TYPE, "mimeType.equals=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByMimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where mimeType in
        defaultAttachmentFiltering("mimeType.in=" + DEFAULT_MIME_TYPE + "," + UPDATED_MIME_TYPE, "mimeType.in=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByMimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where mimeType is not null
        defaultAttachmentFiltering("mimeType.specified=true", "mimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByMimeTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where mimeType contains
        defaultAttachmentFiltering("mimeType.contains=" + DEFAULT_MIME_TYPE, "mimeType.contains=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByMimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where mimeType does not contain
        defaultAttachmentFiltering("mimeType.doesNotContain=" + UPDATED_MIME_TYPE, "mimeType.doesNotContain=" + DEFAULT_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize equals to
        defaultAttachmentFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize in
        defaultAttachmentFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is not null
        defaultAttachmentFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is greater than or equal to
        defaultAttachmentFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is less than or equal to
        defaultAttachmentFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is less than
        defaultAttachmentFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is greater than
        defaultAttachmentFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByUploadedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where uploadedDate equals to
        defaultAttachmentFiltering("uploadedDate.equals=" + DEFAULT_UPLOADED_DATE, "uploadedDate.equals=" + UPDATED_UPLOADED_DATE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByUploadedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where uploadedDate in
        defaultAttachmentFiltering(
            "uploadedDate.in=" + DEFAULT_UPLOADED_DATE + "," + UPDATED_UPLOADED_DATE,
            "uploadedDate.in=" + UPDATED_UPLOADED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAttachmentsByUploadedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where uploadedDate is not null
        defaultAttachmentFiltering("uploadedDate.specified=true", "uploadedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByIsTrashedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where isTrashed equals to
        defaultAttachmentFiltering("isTrashed.equals=" + DEFAULT_IS_TRASHED, "isTrashed.equals=" + UPDATED_IS_TRASHED);
    }

    @Test
    @Transactional
    void getAllAttachmentsByIsTrashedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where isTrashed in
        defaultAttachmentFiltering("isTrashed.in=" + DEFAULT_IS_TRASHED + "," + UPDATED_IS_TRASHED, "isTrashed.in=" + UPDATED_IS_TRASHED);
    }

    @Test
    @Transactional
    void getAllAttachmentsByIsTrashedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where isTrashed is not null
        defaultAttachmentFiltering("isTrashed.specified=true", "isTrashed.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where checksum equals to
        defaultAttachmentFiltering("checksum.equals=" + DEFAULT_CHECKSUM, "checksum.equals=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAttachmentsByChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where checksum in
        defaultAttachmentFiltering("checksum.in=" + DEFAULT_CHECKSUM + "," + UPDATED_CHECKSUM, "checksum.in=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAttachmentsByChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where checksum is not null
        defaultAttachmentFiltering("checksum.specified=true", "checksum.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByChecksumContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where checksum contains
        defaultAttachmentFiltering("checksum.contains=" + DEFAULT_CHECKSUM, "checksum.contains=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAttachmentsByChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where checksum does not contain
        defaultAttachmentFiltering("checksum.doesNotContain=" + UPDATED_CHECKSUM, "checksum.doesNotContain=" + DEFAULT_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsEqualToSomething() throws Exception {
        ContentType contentType;
        if (TestUtil.findAll(em, ContentType.class).isEmpty()) {
            attachmentRepository.saveAndFlush(attachment);
            contentType = ContentTypeResourceIT.createEntity();
        } else {
            contentType = TestUtil.findAll(em, ContentType.class).getFirst();
        }
        em.persist(contentType);
        em.flush();
        attachment.setContentType(contentType);
        attachmentRepository.saveAndFlush(attachment);
        Long contentTypeId = contentType.getId();
        // Get all the attachmentList where contentType equals to contentTypeId
        defaultAttachmentShouldBeFound("contentTypeId.equals=" + contentTypeId);

        // Get all the attachmentList where contentType equals to (contentTypeId + 1)
        defaultAttachmentShouldNotBeFound("contentTypeId.equals=" + (contentTypeId + 1));
    }

    @Test
    @Transactional
    void getAllAttachmentsByFolderIsEqualToSomething() throws Exception {
        FileFolder folder;
        if (TestUtil.findAll(em, FileFolder.class).isEmpty()) {
            attachmentRepository.saveAndFlush(attachment);
            folder = FileFolderResourceIT.createEntity();
        } else {
            folder = TestUtil.findAll(em, FileFolder.class).getFirst();
        }
        em.persist(folder);
        em.flush();
        attachment.setFolder(folder);
        attachmentRepository.saveAndFlush(attachment);
        Long folderId = folder.getId();
        // Get all the attachmentList where folder equals to folderId
        defaultAttachmentShouldBeFound("folderId.equals=" + folderId);

        // Get all the attachmentList where folder equals to (folderId + 1)
        defaultAttachmentShouldNotBeFound("folderId.equals=" + (folderId + 1));
    }

    @Test
    @Transactional
    void getAllAttachmentsByOwnerIsEqualToSomething() throws Exception {
        Party owner;
        if (TestUtil.findAll(em, Party.class).isEmpty()) {
            attachmentRepository.saveAndFlush(attachment);
            owner = PartyResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, Party.class).getFirst();
        }
        em.persist(owner);
        em.flush();
        attachment.setOwner(owner);
        attachmentRepository.saveAndFlush(attachment);
        Long ownerId = owner.getId();
        // Get all the attachmentList where owner equals to ownerId
        defaultAttachmentShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the attachmentList where owner equals to (ownerId + 1)
        defaultAttachmentShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    private void defaultAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAttachmentShouldBeFound(shouldBeFound);
        defaultAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentShouldBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileAttachmentContentType").value(hasItem(DEFAULT_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileAttachment").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL)))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadedDate").value(hasItem(DEFAULT_UPLOADED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isTrashed").value(hasItem(DEFAULT_IS_TRASHED)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)));

        // Check, that the count call also returns 1
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentShouldNotBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttachment() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttachment are not directly saved in db
        em.detach(updatedAttachment);
        updatedAttachment
            .name(UPDATED_NAME)
            .fileAttachment(UPDATED_FILE_ATTACHMENT)
            .fileAttachmentContentType(UPDATED_FILE_ATTACHMENT_CONTENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .isTrashed(UPDATED_IS_TRASHED)
            .checksum(UPDATED_CHECKSUM);

        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttachmentToMatchAllProperties(updatedAttachment);
    }

    @Test
    @Transactional
    void putNonExistingAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachment.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment
            .name(UPDATED_NAME)
            .fileAttachment(UPDATED_FILE_ATTACHMENT)
            .fileAttachmentContentType(UPDATED_FILE_ATTACHMENT_CONTENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .mimeType(UPDATED_MIME_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .checksum(UPDATED_CHECKSUM);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttachment, attachment),
            getPersistedAttachment(attachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment
            .name(UPDATED_NAME)
            .fileAttachment(UPDATED_FILE_ATTACHMENT)
            .fileAttachmentContentType(UPDATED_FILE_ATTACHMENT_CONTENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .mimeType(UPDATED_MIME_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .isTrashed(UPDATED_IS_TRASHED)
            .checksum(UPDATED_CHECKSUM);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttachmentUpdatableFieldsEquals(partialUpdatedAttachment, getPersistedAttachment(partialUpdatedAttachment));
    }

    @Test
    @Transactional
    void patchNonExistingAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attachment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attachment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachment() throws Exception {
        // Initialize the database
        insertedAttachment = attachmentRepository.saveAndFlush(attachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attachment
        restAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attachmentRepository.count();
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

    protected Attachment getPersistedAttachment(Attachment attachment) {
        return attachmentRepository.findById(attachment.getId()).orElseThrow();
    }

    protected void assertPersistedAttachmentToMatchAllProperties(Attachment expectedAttachment) {
        assertAttachmentAllPropertiesEquals(expectedAttachment, getPersistedAttachment(expectedAttachment));
    }

    protected void assertPersistedAttachmentToMatchUpdatableProperties(Attachment expectedAttachment) {
        assertAttachmentAllUpdatablePropertiesEquals(expectedAttachment, getPersistedAttachment(expectedAttachment));
    }
}
