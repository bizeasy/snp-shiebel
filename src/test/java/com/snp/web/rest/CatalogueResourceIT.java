package com.snp.web.rest;

import static com.snp.domain.CatalogueAsserts.*;
import static com.snp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snp.IntegrationTest;
import com.snp.domain.Catalogue;
import com.snp.repository.CatalogueRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CatalogueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NO = 1;
    private static final Integer UPDATED_SEQUENCE_NO = 2;
    private static final Integer SMALLER_SEQUENCE_NO = 1 - 1;

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_IMAGE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_IMAGE_3 = "AAAAAAAAAA";
    private static final String UPDATED_ALT_IMAGE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_BANNER_URL = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BANNER = false;
    private static final Boolean UPDATED_IS_BANNER = true;

    private static final String ENTITY_API_URL = "/api/catalogues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CatalogueRepository catalogueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogueMockMvc;

    private Catalogue catalogue;

    private Catalogue insertedCatalogue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogue createEntity() {
        return new Catalogue()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sequenceNo(DEFAULT_SEQUENCE_NO)
            .imagePath(DEFAULT_IMAGE_PATH)
            .mobileImagePath(DEFAULT_MOBILE_IMAGE_PATH)
            .altImage1(DEFAULT_ALT_IMAGE_1)
            .altImage2(DEFAULT_ALT_IMAGE_2)
            .altImage3(DEFAULT_ALT_IMAGE_3)
            .bannerUrl(DEFAULT_BANNER_URL)
            .isBanner(DEFAULT_IS_BANNER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogue createUpdatedEntity() {
        return new Catalogue()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imagePath(UPDATED_IMAGE_PATH)
            .mobileImagePath(UPDATED_MOBILE_IMAGE_PATH)
            .altImage1(UPDATED_ALT_IMAGE_1)
            .altImage2(UPDATED_ALT_IMAGE_2)
            .altImage3(UPDATED_ALT_IMAGE_3)
            .bannerUrl(UPDATED_BANNER_URL)
            .isBanner(UPDATED_IS_BANNER);
    }

    @BeforeEach
    void initTest() {
        catalogue = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCatalogue != null) {
            catalogueRepository.delete(insertedCatalogue);
            insertedCatalogue = null;
        }
    }

    @Test
    @Transactional
    void createCatalogue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Catalogue
        var returnedCatalogue = om.readValue(
            restCatalogueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogue)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Catalogue.class
        );

        // Validate the Catalogue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCatalogueUpdatableFieldsEquals(returnedCatalogue, getPersistedCatalogue(returnedCatalogue));

        insertedCatalogue = returnedCatalogue;
    }

    @Test
    @Transactional
    void createCatalogueWithExistingId() throws Exception {
        // Create the Catalogue with an existing ID
        catalogue.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogue)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        catalogue.setName(null);

        // Create the Catalogue, which fails.

        restCatalogueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogue)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogues() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].mobileImagePath").value(hasItem(DEFAULT_MOBILE_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].altImage1").value(hasItem(DEFAULT_ALT_IMAGE_1)))
            .andExpect(jsonPath("$.[*].altImage2").value(hasItem(DEFAULT_ALT_IMAGE_2)))
            .andExpect(jsonPath("$.[*].altImage3").value(hasItem(DEFAULT_ALT_IMAGE_3)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)));
    }

    @Test
    @Transactional
    void getCatalogue() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get the catalogue
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sequenceNo").value(DEFAULT_SEQUENCE_NO))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH))
            .andExpect(jsonPath("$.mobileImagePath").value(DEFAULT_MOBILE_IMAGE_PATH))
            .andExpect(jsonPath("$.altImage1").value(DEFAULT_ALT_IMAGE_1))
            .andExpect(jsonPath("$.altImage2").value(DEFAULT_ALT_IMAGE_2))
            .andExpect(jsonPath("$.altImage3").value(DEFAULT_ALT_IMAGE_3))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL))
            .andExpect(jsonPath("$.isBanner").value(DEFAULT_IS_BANNER));
    }

    @Test
    @Transactional
    void getCataloguesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        Long id = catalogue.getId();

        defaultCatalogueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCatalogueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCatalogueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCataloguesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where name equals to
        defaultCatalogueFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCataloguesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where name in
        defaultCatalogueFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCataloguesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where name is not null
        defaultCatalogueFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where name contains
        defaultCatalogueFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCataloguesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where name does not contain
        defaultCatalogueFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCataloguesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where description equals to
        defaultCatalogueFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCataloguesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where description in
        defaultCatalogueFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCataloguesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where description is not null
        defaultCatalogueFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where description contains
        defaultCatalogueFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCataloguesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where description does not contain
        defaultCatalogueFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo equals to
        defaultCatalogueFiltering("sequenceNo.equals=" + DEFAULT_SEQUENCE_NO, "sequenceNo.equals=" + UPDATED_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo in
        defaultCatalogueFiltering(
            "sequenceNo.in=" + DEFAULT_SEQUENCE_NO + "," + UPDATED_SEQUENCE_NO,
            "sequenceNo.in=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo is not null
        defaultCatalogueFiltering("sequenceNo.specified=true", "sequenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo is greater than or equal to
        defaultCatalogueFiltering(
            "sequenceNo.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NO,
            "sequenceNo.greaterThanOrEqual=" + UPDATED_SEQUENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo is less than or equal to
        defaultCatalogueFiltering("sequenceNo.lessThanOrEqual=" + DEFAULT_SEQUENCE_NO, "sequenceNo.lessThanOrEqual=" + SMALLER_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo is less than
        defaultCatalogueFiltering("sequenceNo.lessThan=" + UPDATED_SEQUENCE_NO, "sequenceNo.lessThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCataloguesBySequenceNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where sequenceNo is greater than
        defaultCatalogueFiltering("sequenceNo.greaterThan=" + SMALLER_SEQUENCE_NO, "sequenceNo.greaterThan=" + DEFAULT_SEQUENCE_NO);
    }

    @Test
    @Transactional
    void getAllCataloguesByImagePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where imagePath equals to
        defaultCatalogueFiltering("imagePath.equals=" + DEFAULT_IMAGE_PATH, "imagePath.equals=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    void getAllCataloguesByImagePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where imagePath in
        defaultCatalogueFiltering("imagePath.in=" + DEFAULT_IMAGE_PATH + "," + UPDATED_IMAGE_PATH, "imagePath.in=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    void getAllCataloguesByImagePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where imagePath is not null
        defaultCatalogueFiltering("imagePath.specified=true", "imagePath.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByImagePathContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where imagePath contains
        defaultCatalogueFiltering("imagePath.contains=" + DEFAULT_IMAGE_PATH, "imagePath.contains=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    void getAllCataloguesByImagePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where imagePath does not contain
        defaultCatalogueFiltering("imagePath.doesNotContain=" + UPDATED_IMAGE_PATH, "imagePath.doesNotContain=" + DEFAULT_IMAGE_PATH);
    }

    @Test
    @Transactional
    void getAllCataloguesByMobileImagePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where mobileImagePath equals to
        defaultCatalogueFiltering(
            "mobileImagePath.equals=" + DEFAULT_MOBILE_IMAGE_PATH,
            "mobileImagePath.equals=" + UPDATED_MOBILE_IMAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllCataloguesByMobileImagePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where mobileImagePath in
        defaultCatalogueFiltering(
            "mobileImagePath.in=" + DEFAULT_MOBILE_IMAGE_PATH + "," + UPDATED_MOBILE_IMAGE_PATH,
            "mobileImagePath.in=" + UPDATED_MOBILE_IMAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllCataloguesByMobileImagePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where mobileImagePath is not null
        defaultCatalogueFiltering("mobileImagePath.specified=true", "mobileImagePath.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByMobileImagePathContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where mobileImagePath contains
        defaultCatalogueFiltering(
            "mobileImagePath.contains=" + DEFAULT_MOBILE_IMAGE_PATH,
            "mobileImagePath.contains=" + UPDATED_MOBILE_IMAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllCataloguesByMobileImagePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where mobileImagePath does not contain
        defaultCatalogueFiltering(
            "mobileImagePath.doesNotContain=" + UPDATED_MOBILE_IMAGE_PATH,
            "mobileImagePath.doesNotContain=" + DEFAULT_MOBILE_IMAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage1 equals to
        defaultCatalogueFiltering("altImage1.equals=" + DEFAULT_ALT_IMAGE_1, "altImage1.equals=" + UPDATED_ALT_IMAGE_1);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage1 in
        defaultCatalogueFiltering("altImage1.in=" + DEFAULT_ALT_IMAGE_1 + "," + UPDATED_ALT_IMAGE_1, "altImage1.in=" + UPDATED_ALT_IMAGE_1);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage1 is not null
        defaultCatalogueFiltering("altImage1.specified=true", "altImage1.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage1ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage1 contains
        defaultCatalogueFiltering("altImage1.contains=" + DEFAULT_ALT_IMAGE_1, "altImage1.contains=" + UPDATED_ALT_IMAGE_1);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage1 does not contain
        defaultCatalogueFiltering("altImage1.doesNotContain=" + UPDATED_ALT_IMAGE_1, "altImage1.doesNotContain=" + DEFAULT_ALT_IMAGE_1);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage2 equals to
        defaultCatalogueFiltering("altImage2.equals=" + DEFAULT_ALT_IMAGE_2, "altImage2.equals=" + UPDATED_ALT_IMAGE_2);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage2 in
        defaultCatalogueFiltering("altImage2.in=" + DEFAULT_ALT_IMAGE_2 + "," + UPDATED_ALT_IMAGE_2, "altImage2.in=" + UPDATED_ALT_IMAGE_2);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage2 is not null
        defaultCatalogueFiltering("altImage2.specified=true", "altImage2.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage2 contains
        defaultCatalogueFiltering("altImage2.contains=" + DEFAULT_ALT_IMAGE_2, "altImage2.contains=" + UPDATED_ALT_IMAGE_2);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage2 does not contain
        defaultCatalogueFiltering("altImage2.doesNotContain=" + UPDATED_ALT_IMAGE_2, "altImage2.doesNotContain=" + DEFAULT_ALT_IMAGE_2);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage3 equals to
        defaultCatalogueFiltering("altImage3.equals=" + DEFAULT_ALT_IMAGE_3, "altImage3.equals=" + UPDATED_ALT_IMAGE_3);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage3 in
        defaultCatalogueFiltering("altImage3.in=" + DEFAULT_ALT_IMAGE_3 + "," + UPDATED_ALT_IMAGE_3, "altImage3.in=" + UPDATED_ALT_IMAGE_3);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage3 is not null
        defaultCatalogueFiltering("altImage3.specified=true", "altImage3.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage3ContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage3 contains
        defaultCatalogueFiltering("altImage3.contains=" + DEFAULT_ALT_IMAGE_3, "altImage3.contains=" + UPDATED_ALT_IMAGE_3);
    }

    @Test
    @Transactional
    void getAllCataloguesByAltImage3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where altImage3 does not contain
        defaultCatalogueFiltering("altImage3.doesNotContain=" + UPDATED_ALT_IMAGE_3, "altImage3.doesNotContain=" + DEFAULT_ALT_IMAGE_3);
    }

    @Test
    @Transactional
    void getAllCataloguesByBannerUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where bannerUrl equals to
        defaultCatalogueFiltering("bannerUrl.equals=" + DEFAULT_BANNER_URL, "bannerUrl.equals=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllCataloguesByBannerUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where bannerUrl in
        defaultCatalogueFiltering("bannerUrl.in=" + DEFAULT_BANNER_URL + "," + UPDATED_BANNER_URL, "bannerUrl.in=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllCataloguesByBannerUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where bannerUrl is not null
        defaultCatalogueFiltering("bannerUrl.specified=true", "bannerUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllCataloguesByBannerUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where bannerUrl contains
        defaultCatalogueFiltering("bannerUrl.contains=" + DEFAULT_BANNER_URL, "bannerUrl.contains=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllCataloguesByBannerUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where bannerUrl does not contain
        defaultCatalogueFiltering("bannerUrl.doesNotContain=" + UPDATED_BANNER_URL, "bannerUrl.doesNotContain=" + DEFAULT_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllCataloguesByIsBannerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where isBanner equals to
        defaultCatalogueFiltering("isBanner.equals=" + DEFAULT_IS_BANNER, "isBanner.equals=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllCataloguesByIsBannerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where isBanner in
        defaultCatalogueFiltering("isBanner.in=" + DEFAULT_IS_BANNER + "," + UPDATED_IS_BANNER, "isBanner.in=" + UPDATED_IS_BANNER);
    }

    @Test
    @Transactional
    void getAllCataloguesByIsBannerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where isBanner is not null
        defaultCatalogueFiltering("isBanner.specified=true", "isBanner.specified=false");
    }

    private void defaultCatalogueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCatalogueShouldBeFound(shouldBeFound);
        defaultCatalogueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogueShouldBeFound(String filter) throws Exception {
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sequenceNo").value(hasItem(DEFAULT_SEQUENCE_NO)))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].mobileImagePath").value(hasItem(DEFAULT_MOBILE_IMAGE_PATH)))
            .andExpect(jsonPath("$.[*].altImage1").value(hasItem(DEFAULT_ALT_IMAGE_1)))
            .andExpect(jsonPath("$.[*].altImage2").value(hasItem(DEFAULT_ALT_IMAGE_2)))
            .andExpect(jsonPath("$.[*].altImage3").value(hasItem(DEFAULT_ALT_IMAGE_3)))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].isBanner").value(hasItem(DEFAULT_IS_BANNER)));

        // Check, that the count call also returns 1
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogueShouldNotBeFound(String filter) throws Exception {
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatalogue() throws Exception {
        // Get the catalogue
        restCatalogueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalogue() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogue
        Catalogue updatedCatalogue = catalogueRepository.findById(catalogue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCatalogue are not directly saved in db
        em.detach(updatedCatalogue);
        updatedCatalogue
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imagePath(UPDATED_IMAGE_PATH)
            .mobileImagePath(UPDATED_MOBILE_IMAGE_PATH)
            .altImage1(UPDATED_ALT_IMAGE_1)
            .altImage2(UPDATED_ALT_IMAGE_2)
            .altImage3(UPDATED_ALT_IMAGE_3)
            .bannerUrl(UPDATED_BANNER_URL)
            .isBanner(UPDATED_IS_BANNER);

        restCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCatalogue))
            )
            .andExpect(status().isOk());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCatalogueToMatchAllProperties(updatedCatalogue);
    }

    @Test
    @Transactional
    void putNonExistingCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogue.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(catalogue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(catalogue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogueWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogue using partial update
        Catalogue partialUpdatedCatalogue = new Catalogue();
        partialUpdatedCatalogue.setId(catalogue.getId());

        partialUpdatedCatalogue
            .altImage1(UPDATED_ALT_IMAGE_1)
            .altImage2(UPDATED_ALT_IMAGE_2)
            .altImage3(UPDATED_ALT_IMAGE_3)
            .bannerUrl(UPDATED_BANNER_URL)
            .isBanner(UPDATED_IS_BANNER);

        restCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogue))
            )
            .andExpect(status().isOk());

        // Validate the Catalogue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCatalogue, catalogue),
            getPersistedCatalogue(catalogue)
        );
    }

    @Test
    @Transactional
    void fullUpdateCatalogueWithPatch() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the catalogue using partial update
        Catalogue partialUpdatedCatalogue = new Catalogue();
        partialUpdatedCatalogue.setId(catalogue.getId());

        partialUpdatedCatalogue
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sequenceNo(UPDATED_SEQUENCE_NO)
            .imagePath(UPDATED_IMAGE_PATH)
            .mobileImagePath(UPDATED_MOBILE_IMAGE_PATH)
            .altImage1(UPDATED_ALT_IMAGE_1)
            .altImage2(UPDATED_ALT_IMAGE_2)
            .altImage3(UPDATED_ALT_IMAGE_3)
            .bannerUrl(UPDATED_BANNER_URL)
            .isBanner(UPDATED_IS_BANNER);

        restCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCatalogue))
            )
            .andExpect(status().isOk());

        // Validate the Catalogue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCatalogueUpdatableFieldsEquals(partialUpdatedCatalogue, getPersistedCatalogue(partialUpdatedCatalogue));
    }

    @Test
    @Transactional
    void patchNonExistingCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(catalogue))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        catalogue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(catalogue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogue() throws Exception {
        // Initialize the database
        insertedCatalogue = catalogueRepository.saveAndFlush(catalogue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the catalogue
        restCatalogueMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return catalogueRepository.count();
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

    protected Catalogue getPersistedCatalogue(Catalogue catalogue) {
        return catalogueRepository.findById(catalogue.getId()).orElseThrow();
    }

    protected void assertPersistedCatalogueToMatchAllProperties(Catalogue expectedCatalogue) {
        assertCatalogueAllPropertiesEquals(expectedCatalogue, getPersistedCatalogue(expectedCatalogue));
    }

    protected void assertPersistedCatalogueToMatchUpdatableProperties(Catalogue expectedCatalogue) {
        assertCatalogueAllUpdatablePropertiesEquals(expectedCatalogue, getPersistedCatalogue(expectedCatalogue));
    }
}
