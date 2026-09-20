package com.snp.web.rest;

import com.snp.domain.Uom;
import com.snp.repository.UomRepository;
import com.snp.service.UomQueryService;
import com.snp.service.UomService;
import com.snp.service.criteria.UomCriteria;
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
 * REST controller for managing {@link com.snp.domain.Uom}.
 */
@RestController
@RequestMapping("/api/uoms")
public class UomResource {

    private static final Logger LOG = LoggerFactory.getLogger(UomResource.class);

    private static final String ENTITY_NAME = "uom";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final UomService uomService;

    private final UomRepository uomRepository;

    private final UomQueryService uomQueryService;

    public UomResource(UomService uomService, UomRepository uomRepository, UomQueryService uomQueryService) {
        this.uomService = uomService;
        this.uomRepository = uomRepository;
        this.uomQueryService = uomQueryService;
    }

    /**
     * {@code POST  /uoms} : Create a new uom.
     *
     * @param uom the uom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uom, or with status {@code 400 (Bad Request)} if the uom already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Uom> createUom(@Valid @RequestBody Uom uom) throws URISyntaxException {
        LOG.debug("REST request to save Uom : {}", uom);
        if (uom.getId() != null) {
            throw new BadRequestAlertException("A new uom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uom = uomService.save(uom);
        return ResponseEntity.created(new URI("/api/uoms/" + uom.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uom.getId().toString()))
            .body(uom);
    }

    /**
     * {@code PUT  /uoms/:id} : Updates an existing uom.
     *
     * @param id the id of the uom to save.
     * @param uom the uom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uom,
     * or with status {@code 400 (Bad Request)} if the uom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Uom> updateUom(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Uom uom)
        throws URISyntaxException {
        LOG.debug("REST request to update Uom : {}, {}", id, uom);
        if (uom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uom = uomService.update(uom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uom.getId().toString()))
            .body(uom);
    }

    /**
     * {@code PATCH  /uoms/:id} : Partial updates given fields of an existing uom, field will ignore if it is null
     *
     * @param id the id of the uom to save.
     * @param uom the uom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uom,
     * or with status {@code 400 (Bad Request)} if the uom is not valid,
     * or with status {@code 404 (Not Found)} if the uom is not found,
     * or with status {@code 500 (Internal Server Error)} if the uom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Uom> partialUpdateUom(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Uom uom)
        throws URISyntaxException {
        LOG.debug("REST request to partially update Uom : {}, {}", id, uom);
        if (uom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Uom> result = uomService.partialUpdate(uom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uom.getId().toString())
        );
    }

    /**
     * {@code GET  /uoms} : get all the Uoms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Uoms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Uom>> getAllUoms(UomCriteria criteria) {
        LOG.debug("REST request to get Uoms by criteria: {}", criteria);

        List<Uom> entityList = uomQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /uoms/count} : count all the uoms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUoms(UomCriteria criteria) {
        LOG.debug("REST request to count Uoms by criteria: {}", criteria);
        return ResponseEntity.ok().body(uomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /uoms/:id} : get the "id" uom.
     *
     * @param id the id of the uom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Uom> getUom(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Uom : {}", id);
        Optional<Uom> uom = uomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uom);
    }

    /**
     * {@code DELETE  /uoms/:id} : delete the "id" uom.
     *
     * @param id the id of the uom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUom(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Uom : {}", id);
        uomService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
