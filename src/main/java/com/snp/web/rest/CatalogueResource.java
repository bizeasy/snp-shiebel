package com.snp.web.rest;

import com.snp.domain.Catalogue;
import com.snp.repository.CatalogueRepository;
import com.snp.service.CatalogueQueryService;
import com.snp.service.CatalogueService;
import com.snp.service.criteria.CatalogueCriteria;
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
 * REST controller for managing {@link com.snp.domain.Catalogue}.
 */
@RestController
@RequestMapping("/api/catalogues")
public class CatalogueResource {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueResource.class);

    private static final String ENTITY_NAME = "catalogue";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CatalogueService catalogueService;

    private final CatalogueRepository catalogueRepository;

    private final CatalogueQueryService catalogueQueryService;

    public CatalogueResource(
        CatalogueService catalogueService,
        CatalogueRepository catalogueRepository,
        CatalogueQueryService catalogueQueryService
    ) {
        this.catalogueService = catalogueService;
        this.catalogueRepository = catalogueRepository;
        this.catalogueQueryService = catalogueQueryService;
    }

    /**
     * {@code POST  /catalogues} : Create a new catalogue.
     *
     * @param catalogue the catalogue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogue, or with status {@code 400 (Bad Request)} if the catalogue already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Catalogue> createCatalogue(@Valid @RequestBody Catalogue catalogue) throws URISyntaxException {
        LOG.debug("REST request to save Catalogue : {}", catalogue);
        if (catalogue.getId() != null) {
            throw new BadRequestAlertException("A new catalogue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        catalogue = catalogueService.save(catalogue);
        return ResponseEntity.created(new URI("/api/catalogues/" + catalogue.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, catalogue.getId().toString()))
            .body(catalogue);
    }

    /**
     * {@code PUT  /catalogues/:id} : Updates an existing catalogue.
     *
     * @param id the id of the catalogue to save.
     * @param catalogue the catalogue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogue,
     * or with status {@code 400 (Bad Request)} if the catalogue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Catalogue> updateCatalogue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Catalogue catalogue
    ) throws URISyntaxException {
        LOG.debug("REST request to update Catalogue : {}, {}", id, catalogue);
        if (catalogue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        catalogue = catalogueService.update(catalogue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogue.getId().toString()))
            .body(catalogue);
    }

    /**
     * {@code PATCH  /catalogues/:id} : Partial updates given fields of an existing catalogue, field will ignore if it is null
     *
     * @param id the id of the catalogue to save.
     * @param catalogue the catalogue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogue,
     * or with status {@code 400 (Bad Request)} if the catalogue is not valid,
     * or with status {@code 404 (Not Found)} if the catalogue is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Catalogue> partialUpdateCatalogue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Catalogue catalogue
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Catalogue : {}, {}", id, catalogue);
        if (catalogue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Catalogue> result = catalogueService.partialUpdate(catalogue);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogue.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogues} : get all the Catalogues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Catalogues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Catalogue>> getAllCatalogues(CatalogueCriteria criteria) {
        LOG.debug("REST request to get Catalogues by criteria: {}", criteria);

        List<Catalogue> entityList = catalogueQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /catalogues/count} : count all the catalogues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCatalogues(CatalogueCriteria criteria) {
        LOG.debug("REST request to count Catalogues by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalogues/:id} : get the "id" catalogue.
     *
     * @param id the id of the catalogue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Catalogue> getCatalogue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Catalogue : {}", id);
        Optional<Catalogue> catalogue = catalogueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogue);
    }

    /**
     * {@code DELETE  /catalogues/:id} : delete the "id" catalogue.
     *
     * @param id the id of the catalogue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Catalogue : {}", id);
        catalogueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
