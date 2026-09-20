package com.snp.web.rest;

import com.snp.domain.UomConversion;
import com.snp.repository.UomConversionRepository;
import com.snp.service.UomConversionQueryService;
import com.snp.service.UomConversionService;
import com.snp.service.criteria.UomConversionCriteria;
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
 * REST controller for managing {@link com.snp.domain.UomConversion}.
 */
@RestController
@RequestMapping("/api/uom-conversions")
public class UomConversionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UomConversionResource.class);

    private static final String ENTITY_NAME = "uomConversion";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final UomConversionService uomConversionService;

    private final UomConversionRepository uomConversionRepository;

    private final UomConversionQueryService uomConversionQueryService;

    public UomConversionResource(
        UomConversionService uomConversionService,
        UomConversionRepository uomConversionRepository,
        UomConversionQueryService uomConversionQueryService
    ) {
        this.uomConversionService = uomConversionService;
        this.uomConversionRepository = uomConversionRepository;
        this.uomConversionQueryService = uomConversionQueryService;
    }

    /**
     * {@code POST  /uom-conversions} : Create a new uomConversion.
     *
     * @param uomConversion the uomConversion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uomConversion, or with status {@code 400 (Bad Request)} if the uomConversion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UomConversion> createUomConversion(@Valid @RequestBody UomConversion uomConversion) throws URISyntaxException {
        LOG.debug("REST request to save UomConversion : {}", uomConversion);
        if (uomConversion.getId() != null) {
            throw new BadRequestAlertException("A new uomConversion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uomConversion = uomConversionService.save(uomConversion);
        return ResponseEntity.created(new URI("/api/uom-conversions/" + uomConversion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uomConversion.getId().toString()))
            .body(uomConversion);
    }

    /**
     * {@code PUT  /uom-conversions/:id} : Updates an existing uomConversion.
     *
     * @param id the id of the uomConversion to save.
     * @param uomConversion the uomConversion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomConversion,
     * or with status {@code 400 (Bad Request)} if the uomConversion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uomConversion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UomConversion> updateUomConversion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UomConversion uomConversion
    ) throws URISyntaxException {
        LOG.debug("REST request to update UomConversion : {}, {}", id, uomConversion);
        if (uomConversion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomConversion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomConversionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uomConversion = uomConversionService.update(uomConversion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uomConversion.getId().toString()))
            .body(uomConversion);
    }

    /**
     * {@code PATCH  /uom-conversions/:id} : Partial updates given fields of an existing uomConversion, field will ignore if it is null
     *
     * @param id the id of the uomConversion to save.
     * @param uomConversion the uomConversion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomConversion,
     * or with status {@code 400 (Bad Request)} if the uomConversion is not valid,
     * or with status {@code 404 (Not Found)} if the uomConversion is not found,
     * or with status {@code 500 (Internal Server Error)} if the uomConversion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UomConversion> partialUpdateUomConversion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UomConversion uomConversion
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update UomConversion : {}, {}", id, uomConversion);
        if (uomConversion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomConversion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomConversionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UomConversion> result = uomConversionService.partialUpdate(uomConversion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uomConversion.getId().toString())
        );
    }

    /**
     * {@code GET  /uom-conversions} : get all the Uom Conversions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Uom Conversions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UomConversion>> getAllUomConversions(UomConversionCriteria criteria) {
        LOG.debug("REST request to get UomConversions by criteria: {}", criteria);

        List<UomConversion> entityList = uomConversionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /uom-conversions/count} : count all the uomConversions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUomConversions(UomConversionCriteria criteria) {
        LOG.debug("REST request to count UomConversions by criteria: {}", criteria);
        return ResponseEntity.ok().body(uomConversionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /uom-conversions/:id} : get the "id" uomConversion.
     *
     * @param id the id of the uomConversion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uomConversion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UomConversion> getUomConversion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UomConversion : {}", id);
        Optional<UomConversion> uomConversion = uomConversionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uomConversion);
    }

    /**
     * {@code DELETE  /uom-conversions/:id} : delete the "id" uomConversion.
     *
     * @param id the id of the uomConversion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUomConversion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UomConversion : {}", id);
        uomConversionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
