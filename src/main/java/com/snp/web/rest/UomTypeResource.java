package com.snp.web.rest;

import com.snp.domain.UomType;
import com.snp.repository.UomTypeRepository;
import com.snp.service.UomTypeQueryService;
import com.snp.service.UomTypeService;
import com.snp.service.criteria.UomTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.UomType}.
 */
@RestController
@RequestMapping("/api/uom-types")
public class UomTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(UomTypeResource.class);

    private static final String ENTITY_NAME = "uomType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final UomTypeService uomTypeService;

    private final UomTypeRepository uomTypeRepository;

    private final UomTypeQueryService uomTypeQueryService;

    public UomTypeResource(UomTypeService uomTypeService, UomTypeRepository uomTypeRepository, UomTypeQueryService uomTypeQueryService) {
        this.uomTypeService = uomTypeService;
        this.uomTypeRepository = uomTypeRepository;
        this.uomTypeQueryService = uomTypeQueryService;
    }

    /**
     * {@code POST  /uom-types} : Create a new uomType.
     *
     * @param uomType the uomType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uomType, or with status {@code 400 (Bad Request)} if the uomType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UomType> createUomType(@Valid @RequestBody UomType uomType) throws URISyntaxException {
        LOG.debug("REST request to save UomType : {}", uomType);
        if (uomType.getId() != null) {
            throw new BadRequestAlertException("A new uomType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uomType = uomTypeService.save(uomType);
        return ResponseEntity.created(new URI("/api/uom-types/" + uomType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uomType.getId().toString()))
            .body(uomType);
    }

    /**
     * {@code PUT  /uom-types/:id} : Updates an existing uomType.
     *
     * @param id the id of the uomType to save.
     * @param uomType the uomType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomType,
     * or with status {@code 400 (Bad Request)} if the uomType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uomType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UomType> updateUomType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UomType uomType
    ) throws URISyntaxException {
        LOG.debug("REST request to update UomType : {}, {}", id, uomType);
        if (uomType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uomType = uomTypeService.update(uomType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uomType.getId().toString()))
            .body(uomType);
    }

    /**
     * {@code PATCH  /uom-types/:id} : Partial updates given fields of an existing uomType, field will ignore if it is null
     *
     * @param id the id of the uomType to save.
     * @param uomType the uomType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomType,
     * or with status {@code 400 (Bad Request)} if the uomType is not valid,
     * or with status {@code 404 (Not Found)} if the uomType is not found,
     * or with status {@code 500 (Internal Server Error)} if the uomType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UomType> partialUpdateUomType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UomType uomType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update UomType : {}, {}", id, uomType);
        if (uomType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uomTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UomType> result = uomTypeService.partialUpdate(uomType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uomType.getId().toString())
        );
    }

    /**
     * {@code GET  /uom-types} : get all the Uom Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Uom Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UomType>> getAllUomTypes(UomTypeCriteria criteria) {
        LOG.debug("REST request to get UomTypes by criteria: {}", criteria);

        List<UomType> entityList = uomTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /uom-types/count} : count all the uomTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUomTypes(UomTypeCriteria criteria) {
        LOG.debug("REST request to count UomTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(uomTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /uom-types/:id} : get the "id" uomType.
     *
     * @param id the id of the uomType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uomType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UomType> getUomType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UomType : {}", id);
        Optional<UomType> uomType = uomTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uomType);
    }

    /**
     * {@code DELETE  /uom-types/:id} : delete the "id" uomType.
     *
     * @param id the id of the uomType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUomType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UomType : {}", id);
        uomTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
