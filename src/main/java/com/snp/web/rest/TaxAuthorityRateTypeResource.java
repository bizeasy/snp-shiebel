package com.snp.web.rest;

import com.snp.domain.TaxAuthorityRateType;
import com.snp.repository.TaxAuthorityRateTypeRepository;
import com.snp.service.TaxAuthorityRateTypeQueryService;
import com.snp.service.TaxAuthorityRateTypeService;
import com.snp.service.criteria.TaxAuthorityRateTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.TaxAuthorityRateType}.
 */
@RestController
@RequestMapping("/api/tax-authority-rate-types")
public class TaxAuthorityRateTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxAuthorityRateTypeResource.class);

    private static final String ENTITY_NAME = "taxAuthorityRateType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final TaxAuthorityRateTypeService taxAuthorityRateTypeService;

    private final TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepository;

    private final TaxAuthorityRateTypeQueryService taxAuthorityRateTypeQueryService;

    public TaxAuthorityRateTypeResource(
        TaxAuthorityRateTypeService taxAuthorityRateTypeService,
        TaxAuthorityRateTypeRepository taxAuthorityRateTypeRepository,
        TaxAuthorityRateTypeQueryService taxAuthorityRateTypeQueryService
    ) {
        this.taxAuthorityRateTypeService = taxAuthorityRateTypeService;
        this.taxAuthorityRateTypeRepository = taxAuthorityRateTypeRepository;
        this.taxAuthorityRateTypeQueryService = taxAuthorityRateTypeQueryService;
    }

    /**
     * {@code POST  /tax-authority-rate-types} : Create a new taxAuthorityRateType.
     *
     * @param taxAuthorityRateType the taxAuthorityRateType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxAuthorityRateType, or with status {@code 400 (Bad Request)} if the taxAuthorityRateType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaxAuthorityRateType> createTaxAuthorityRateType(@Valid @RequestBody TaxAuthorityRateType taxAuthorityRateType)
        throws URISyntaxException {
        LOG.debug("REST request to save TaxAuthorityRateType : {}", taxAuthorityRateType);
        if (taxAuthorityRateType.getId() != null) {
            throw new BadRequestAlertException("A new taxAuthorityRateType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxAuthorityRateType = taxAuthorityRateTypeService.save(taxAuthorityRateType);
        return ResponseEntity.created(new URI("/api/tax-authority-rate-types/" + taxAuthorityRateType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taxAuthorityRateType.getId().toString()))
            .body(taxAuthorityRateType);
    }

    /**
     * {@code PUT  /tax-authority-rate-types/:id} : Updates an existing taxAuthorityRateType.
     *
     * @param id the id of the taxAuthorityRateType to save.
     * @param taxAuthorityRateType the taxAuthorityRateType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAuthorityRateType,
     * or with status {@code 400 (Bad Request)} if the taxAuthorityRateType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxAuthorityRateType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxAuthorityRateType> updateTaxAuthorityRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxAuthorityRateType taxAuthorityRateType
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaxAuthorityRateType : {}, {}", id, taxAuthorityRateType);
        if (taxAuthorityRateType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAuthorityRateType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAuthorityRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxAuthorityRateType = taxAuthorityRateTypeService.update(taxAuthorityRateType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAuthorityRateType.getId().toString()))
            .body(taxAuthorityRateType);
    }

    /**
     * {@code PATCH  /tax-authority-rate-types/:id} : Partial updates given fields of an existing taxAuthorityRateType, field will ignore if it is null
     *
     * @param id the id of the taxAuthorityRateType to save.
     * @param taxAuthorityRateType the taxAuthorityRateType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAuthorityRateType,
     * or with status {@code 400 (Bad Request)} if the taxAuthorityRateType is not valid,
     * or with status {@code 404 (Not Found)} if the taxAuthorityRateType is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxAuthorityRateType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxAuthorityRateType> partialUpdateTaxAuthorityRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxAuthorityRateType taxAuthorityRateType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TaxAuthorityRateType : {}, {}", id, taxAuthorityRateType);
        if (taxAuthorityRateType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAuthorityRateType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAuthorityRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxAuthorityRateType> result = taxAuthorityRateTypeService.partialUpdate(taxAuthorityRateType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAuthorityRateType.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-authority-rate-types} : get all the Tax Authority Rate Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tax Authority Rate Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaxAuthorityRateType>> getAllTaxAuthorityRateTypes(TaxAuthorityRateTypeCriteria criteria) {
        LOG.debug("REST request to get TaxAuthorityRateTypes by criteria: {}", criteria);

        List<TaxAuthorityRateType> entityList = taxAuthorityRateTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tax-authority-rate-types/count} : count all the taxAuthorityRateTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTaxAuthorityRateTypes(TaxAuthorityRateTypeCriteria criteria) {
        LOG.debug("REST request to count TaxAuthorityRateTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxAuthorityRateTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-authority-rate-types/:id} : get the "id" taxAuthorityRateType.
     *
     * @param id the id of the taxAuthorityRateType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxAuthorityRateType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxAuthorityRateType> getTaxAuthorityRateType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaxAuthorityRateType : {}", id);
        Optional<TaxAuthorityRateType> taxAuthorityRateType = taxAuthorityRateTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxAuthorityRateType);
    }

    /**
     * {@code DELETE  /tax-authority-rate-types/:id} : delete the "id" taxAuthorityRateType.
     *
     * @param id the id of the taxAuthorityRateType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxAuthorityRateType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaxAuthorityRateType : {}", id);
        taxAuthorityRateTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
