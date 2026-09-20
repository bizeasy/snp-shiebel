package com.snp.web.rest;

import com.snp.domain.CatalogueCategory;
import com.snp.repository.CatalogueCategoryRepository;
import com.snp.service.CatalogueCategoryQueryService;
import com.snp.service.CatalogueCategoryService;
import com.snp.service.criteria.CatalogueCategoryCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.CatalogueCategory}.
 */
@RestController
@RequestMapping("/api/catalogue-categories")
public class CatalogueCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryResource.class);

    private static final String ENTITY_NAME = "catalogueCategory";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CatalogueCategoryService catalogueCategoryService;

    private final CatalogueCategoryRepository catalogueCategoryRepository;

    private final CatalogueCategoryQueryService catalogueCategoryQueryService;

    public CatalogueCategoryResource(
        CatalogueCategoryService catalogueCategoryService,
        CatalogueCategoryRepository catalogueCategoryRepository,
        CatalogueCategoryQueryService catalogueCategoryQueryService
    ) {
        this.catalogueCategoryService = catalogueCategoryService;
        this.catalogueCategoryRepository = catalogueCategoryRepository;
        this.catalogueCategoryQueryService = catalogueCategoryQueryService;
    }

    /**
     * {@code POST  /catalogue-categories} : Create a new catalogueCategory.
     *
     * @param catalogueCategory the catalogueCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogueCategory, or with status {@code 400 (Bad Request)} if the catalogueCategory already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CatalogueCategory> createCatalogueCategory(@Valid @RequestBody CatalogueCategory catalogueCategory)
        throws URISyntaxException {
        LOG.debug("REST request to save CatalogueCategory : {}", catalogueCategory);
        if (catalogueCategory.getId() != null) {
            throw new BadRequestAlertException("A new catalogueCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        catalogueCategory = catalogueCategoryService.save(catalogueCategory);
        return ResponseEntity.created(new URI("/api/catalogue-categories/" + catalogueCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, catalogueCategory.getId().toString()))
            .body(catalogueCategory);
    }

    /**
     * {@code PUT  /catalogue-categories/:id} : Updates an existing catalogueCategory.
     *
     * @param id the id of the catalogueCategory to save.
     * @param catalogueCategory the catalogueCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogueCategory,
     * or with status {@code 400 (Bad Request)} if the catalogueCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogueCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatalogueCategory> updateCatalogueCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogueCategory catalogueCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update CatalogueCategory : {}, {}", id, catalogueCategory);
        if (catalogueCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogueCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        catalogueCategory = catalogueCategoryService.update(catalogueCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogueCategory.getId().toString()))
            .body(catalogueCategory);
    }

    /**
     * {@code PATCH  /catalogue-categories/:id} : Partial updates given fields of an existing catalogueCategory, field will ignore if it is null
     *
     * @param id the id of the catalogueCategory to save.
     * @param catalogueCategory the catalogueCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogueCategory,
     * or with status {@code 400 (Bad Request)} if the catalogueCategory is not valid,
     * or with status {@code 404 (Not Found)} if the catalogueCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogueCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogueCategory> partialUpdateCatalogueCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogueCategory catalogueCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CatalogueCategory : {}, {}", id, catalogueCategory);
        if (catalogueCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogueCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogueCategory> result = catalogueCategoryService.partialUpdate(catalogueCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogueCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogue-categories} : get all the Catalogue Categories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Catalogue Categories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CatalogueCategory>> getAllCatalogueCategories(CatalogueCategoryCriteria criteria) {
        LOG.debug("REST request to get CatalogueCategories by criteria: {}", criteria);

        List<CatalogueCategory> entityList = catalogueCategoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /catalogue-categories/count} : count all the catalogueCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCatalogueCategories(CatalogueCategoryCriteria criteria) {
        LOG.debug("REST request to count CatalogueCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogueCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalogue-categories/:id} : get the "id" catalogueCategory.
     *
     * @param id the id of the catalogueCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogueCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatalogueCategory> getCatalogueCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CatalogueCategory : {}", id);
        Optional<CatalogueCategory> catalogueCategory = catalogueCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogueCategory);
    }

    /**
     * {@code DELETE  /catalogue-categories/:id} : delete the "id" catalogueCategory.
     *
     * @param id the id of the catalogueCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogueCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CatalogueCategory : {}", id);
        catalogueCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
