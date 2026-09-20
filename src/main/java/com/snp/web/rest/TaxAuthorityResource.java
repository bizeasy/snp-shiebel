package com.snp.web.rest;

import com.snp.domain.TaxAuthority;
import com.snp.repository.TaxAuthorityRepository;
import com.snp.service.TaxAuthorityQueryService;
import com.snp.service.TaxAuthorityService;
import com.snp.service.criteria.TaxAuthorityCriteria;
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
 * REST controller for managing {@link com.snp.domain.TaxAuthority}.
 */
@RestController
@RequestMapping("/api/tax-authorities")
public class TaxAuthorityResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxAuthorityResource.class);

    private static final String ENTITY_NAME = "taxAuthority";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final TaxAuthorityService taxAuthorityService;

    private final TaxAuthorityRepository taxAuthorityRepository;

    private final TaxAuthorityQueryService taxAuthorityQueryService;

    public TaxAuthorityResource(
        TaxAuthorityService taxAuthorityService,
        TaxAuthorityRepository taxAuthorityRepository,
        TaxAuthorityQueryService taxAuthorityQueryService
    ) {
        this.taxAuthorityService = taxAuthorityService;
        this.taxAuthorityRepository = taxAuthorityRepository;
        this.taxAuthorityQueryService = taxAuthorityQueryService;
    }

    /**
     * {@code POST  /tax-authorities} : Create a new taxAuthority.
     *
     * @param taxAuthority the taxAuthority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxAuthority, or with status {@code 400 (Bad Request)} if the taxAuthority already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaxAuthority> createTaxAuthority(@Valid @RequestBody TaxAuthority taxAuthority) throws URISyntaxException {
        LOG.debug("REST request to save TaxAuthority : {}", taxAuthority);
        if (taxAuthority.getId() != null) {
            throw new BadRequestAlertException("A new taxAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxAuthority = taxAuthorityService.save(taxAuthority);
        return ResponseEntity.created(new URI("/api/tax-authorities/" + taxAuthority.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taxAuthority.getId().toString()))
            .body(taxAuthority);
    }

    /**
     * {@code PUT  /tax-authorities/:id} : Updates an existing taxAuthority.
     *
     * @param id the id of the taxAuthority to save.
     * @param taxAuthority the taxAuthority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAuthority,
     * or with status {@code 400 (Bad Request)} if the taxAuthority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxAuthority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxAuthority> updateTaxAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxAuthority taxAuthority
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaxAuthority : {}, {}", id, taxAuthority);
        if (taxAuthority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAuthority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAuthorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxAuthority = taxAuthorityService.update(taxAuthority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAuthority.getId().toString()))
            .body(taxAuthority);
    }

    /**
     * {@code PATCH  /tax-authorities/:id} : Partial updates given fields of an existing taxAuthority, field will ignore if it is null
     *
     * @param id the id of the taxAuthority to save.
     * @param taxAuthority the taxAuthority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAuthority,
     * or with status {@code 400 (Bad Request)} if the taxAuthority is not valid,
     * or with status {@code 404 (Not Found)} if the taxAuthority is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxAuthority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxAuthority> partialUpdateTaxAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxAuthority taxAuthority
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TaxAuthority : {}, {}", id, taxAuthority);
        if (taxAuthority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAuthority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAuthorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxAuthority> result = taxAuthorityService.partialUpdate(taxAuthority);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAuthority.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-authorities} : get all the Tax Authorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tax Authorities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaxAuthority>> getAllTaxAuthorities(TaxAuthorityCriteria criteria) {
        LOG.debug("REST request to get TaxAuthorities by criteria: {}", criteria);

        List<TaxAuthority> entityList = taxAuthorityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tax-authorities/count} : count all the taxAuthorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTaxAuthorities(TaxAuthorityCriteria criteria) {
        LOG.debug("REST request to count TaxAuthorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxAuthorityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-authorities/:id} : get the "id" taxAuthority.
     *
     * @param id the id of the taxAuthority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxAuthority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxAuthority> getTaxAuthority(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaxAuthority : {}", id);
        Optional<TaxAuthority> taxAuthority = taxAuthorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxAuthority);
    }

    /**
     * {@code DELETE  /tax-authorities/:id} : delete the "id" taxAuthority.
     *
     * @param id the id of the taxAuthority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxAuthority(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaxAuthority : {}", id);
        taxAuthorityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
