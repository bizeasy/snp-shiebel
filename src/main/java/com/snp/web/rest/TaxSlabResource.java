package com.snp.web.rest;

import com.snp.domain.TaxSlab;
import com.snp.repository.TaxSlabRepository;
import com.snp.service.TaxSlabQueryService;
import com.snp.service.TaxSlabService;
import com.snp.service.criteria.TaxSlabCriteria;
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
 * REST controller for managing {@link com.snp.domain.TaxSlab}.
 */
@RestController
@RequestMapping("/api/tax-slabs")
public class TaxSlabResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxSlabResource.class);

    private static final String ENTITY_NAME = "taxSlab";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final TaxSlabService taxSlabService;

    private final TaxSlabRepository taxSlabRepository;

    private final TaxSlabQueryService taxSlabQueryService;

    public TaxSlabResource(TaxSlabService taxSlabService, TaxSlabRepository taxSlabRepository, TaxSlabQueryService taxSlabQueryService) {
        this.taxSlabService = taxSlabService;
        this.taxSlabRepository = taxSlabRepository;
        this.taxSlabQueryService = taxSlabQueryService;
    }

    /**
     * {@code POST  /tax-slabs} : Create a new taxSlab.
     *
     * @param taxSlab the taxSlab to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxSlab, or with status {@code 400 (Bad Request)} if the taxSlab already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaxSlab> createTaxSlab(@Valid @RequestBody TaxSlab taxSlab) throws URISyntaxException {
        LOG.debug("REST request to save TaxSlab : {}", taxSlab);
        if (taxSlab.getId() != null) {
            throw new BadRequestAlertException("A new taxSlab cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxSlab = taxSlabService.save(taxSlab);
        return ResponseEntity.created(new URI("/api/tax-slabs/" + taxSlab.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taxSlab.getId().toString()))
            .body(taxSlab);
    }

    /**
     * {@code PUT  /tax-slabs/:id} : Updates an existing taxSlab.
     *
     * @param id the id of the taxSlab to save.
     * @param taxSlab the taxSlab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxSlab,
     * or with status {@code 400 (Bad Request)} if the taxSlab is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxSlab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxSlab> updateTaxSlab(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxSlab taxSlab
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaxSlab : {}, {}", id, taxSlab);
        if (taxSlab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxSlab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxSlabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxSlab = taxSlabService.update(taxSlab);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxSlab.getId().toString()))
            .body(taxSlab);
    }

    /**
     * {@code PATCH  /tax-slabs/:id} : Partial updates given fields of an existing taxSlab, field will ignore if it is null
     *
     * @param id the id of the taxSlab to save.
     * @param taxSlab the taxSlab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxSlab,
     * or with status {@code 400 (Bad Request)} if the taxSlab is not valid,
     * or with status {@code 404 (Not Found)} if the taxSlab is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxSlab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxSlab> partialUpdateTaxSlab(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxSlab taxSlab
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TaxSlab : {}, {}", id, taxSlab);
        if (taxSlab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxSlab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxSlabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxSlab> result = taxSlabService.partialUpdate(taxSlab);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxSlab.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-slabs} : get all the Tax Slabs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tax Slabs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaxSlab>> getAllTaxSlabs(TaxSlabCriteria criteria) {
        LOG.debug("REST request to get TaxSlabs by criteria: {}", criteria);

        List<TaxSlab> entityList = taxSlabQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tax-slabs/count} : count all the taxSlabs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTaxSlabs(TaxSlabCriteria criteria) {
        LOG.debug("REST request to count TaxSlabs by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxSlabQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-slabs/:id} : get the "id" taxSlab.
     *
     * @param id the id of the taxSlab to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxSlab, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxSlab> getTaxSlab(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaxSlab : {}", id);
        Optional<TaxSlab> taxSlab = taxSlabService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxSlab);
    }

    /**
     * {@code DELETE  /tax-slabs/:id} : delete the "id" taxSlab.
     *
     * @param id the id of the taxSlab to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxSlab(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaxSlab : {}", id);
        taxSlabService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
