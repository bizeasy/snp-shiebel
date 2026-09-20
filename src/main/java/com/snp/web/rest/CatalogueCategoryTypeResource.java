package com.snp.web.rest;

import com.snp.domain.CatalogueCategoryType;
import com.snp.repository.CatalogueCategoryTypeRepository;
import com.snp.service.CatalogueCategoryTypeQueryService;
import com.snp.service.CatalogueCategoryTypeService;
import com.snp.service.criteria.CatalogueCategoryTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.CatalogueCategoryType}.
 */
@RestController
@RequestMapping("/api/catalogue-category-types")
public class CatalogueCategoryTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueCategoryTypeResource.class);

    private static final String ENTITY_NAME = "catalogueCategoryType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CatalogueCategoryTypeService catalogueCategoryTypeService;

    private final CatalogueCategoryTypeRepository catalogueCategoryTypeRepository;

    private final CatalogueCategoryTypeQueryService catalogueCategoryTypeQueryService;

    public CatalogueCategoryTypeResource(
        CatalogueCategoryTypeService catalogueCategoryTypeService,
        CatalogueCategoryTypeRepository catalogueCategoryTypeRepository,
        CatalogueCategoryTypeQueryService catalogueCategoryTypeQueryService
    ) {
        this.catalogueCategoryTypeService = catalogueCategoryTypeService;
        this.catalogueCategoryTypeRepository = catalogueCategoryTypeRepository;
        this.catalogueCategoryTypeQueryService = catalogueCategoryTypeQueryService;
    }

    /**
     * {@code POST  /catalogue-category-types} : Create a new catalogueCategoryType.
     *
     * @param catalogueCategoryType the catalogueCategoryType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogueCategoryType, or with status {@code 400 (Bad Request)} if the catalogueCategoryType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CatalogueCategoryType> createCatalogueCategoryType(
        @Valid @RequestBody CatalogueCategoryType catalogueCategoryType
    ) throws URISyntaxException {
        LOG.debug("REST request to save CatalogueCategoryType : {}", catalogueCategoryType);
        if (catalogueCategoryType.getId() != null) {
            throw new BadRequestAlertException("A new catalogueCategoryType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        catalogueCategoryType = catalogueCategoryTypeService.save(catalogueCategoryType);
        return ResponseEntity.created(new URI("/api/catalogue-category-types/" + catalogueCategoryType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, catalogueCategoryType.getId().toString()))
            .body(catalogueCategoryType);
    }

    /**
     * {@code PUT  /catalogue-category-types/:id} : Updates an existing catalogueCategoryType.
     *
     * @param id the id of the catalogueCategoryType to save.
     * @param catalogueCategoryType the catalogueCategoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogueCategoryType,
     * or with status {@code 400 (Bad Request)} if the catalogueCategoryType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogueCategoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatalogueCategoryType> updateCatalogueCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogueCategoryType catalogueCategoryType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CatalogueCategoryType : {}, {}", id, catalogueCategoryType);
        if (catalogueCategoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogueCategoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        catalogueCategoryType = catalogueCategoryTypeService.update(catalogueCategoryType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogueCategoryType.getId().toString()))
            .body(catalogueCategoryType);
    }

    /**
     * {@code PATCH  /catalogue-category-types/:id} : Partial updates given fields of an existing catalogueCategoryType, field will ignore if it is null
     *
     * @param id the id of the catalogueCategoryType to save.
     * @param catalogueCategoryType the catalogueCategoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogueCategoryType,
     * or with status {@code 400 (Bad Request)} if the catalogueCategoryType is not valid,
     * or with status {@code 404 (Not Found)} if the catalogueCategoryType is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogueCategoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogueCategoryType> partialUpdateCatalogueCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogueCategoryType catalogueCategoryType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CatalogueCategoryType : {}, {}", id, catalogueCategoryType);
        if (catalogueCategoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogueCategoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogueCategoryType> result = catalogueCategoryTypeService.partialUpdate(catalogueCategoryType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogueCategoryType.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogue-category-types} : get all the Catalogue Category Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Catalogue Category Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CatalogueCategoryType>> getAllCatalogueCategoryTypes(CatalogueCategoryTypeCriteria criteria) {
        LOG.debug("REST request to get CatalogueCategoryTypes by criteria: {}", criteria);

        List<CatalogueCategoryType> entityList = catalogueCategoryTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /catalogue-category-types/count} : count all the catalogueCategoryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCatalogueCategoryTypes(CatalogueCategoryTypeCriteria criteria) {
        LOG.debug("REST request to count CatalogueCategoryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogueCategoryTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalogue-category-types/:id} : get the "id" catalogueCategoryType.
     *
     * @param id the id of the catalogueCategoryType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogueCategoryType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatalogueCategoryType> getCatalogueCategoryType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CatalogueCategoryType : {}", id);
        Optional<CatalogueCategoryType> catalogueCategoryType = catalogueCategoryTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogueCategoryType);
    }

    /**
     * {@code DELETE  /catalogue-category-types/:id} : delete the "id" catalogueCategoryType.
     *
     * @param id the id of the catalogueCategoryType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogueCategoryType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CatalogueCategoryType : {}", id);
        catalogueCategoryTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
