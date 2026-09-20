package com.snp.web.rest;

import com.snp.domain.FacilityType;
import com.snp.repository.FacilityTypeRepository;
import com.snp.service.FacilityTypeQueryService;
import com.snp.service.FacilityTypeService;
import com.snp.service.criteria.FacilityTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityType}.
 */
@RestController
@RequestMapping("/api/facility-types")
public class FacilityTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityTypeResource.class);

    private static final String ENTITY_NAME = "facilityType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityTypeService facilityTypeService;

    private final FacilityTypeRepository facilityTypeRepository;

    private final FacilityTypeQueryService facilityTypeQueryService;

    public FacilityTypeResource(
        FacilityTypeService facilityTypeService,
        FacilityTypeRepository facilityTypeRepository,
        FacilityTypeQueryService facilityTypeQueryService
    ) {
        this.facilityTypeService = facilityTypeService;
        this.facilityTypeRepository = facilityTypeRepository;
        this.facilityTypeQueryService = facilityTypeQueryService;
    }

    /**
     * {@code POST  /facility-types} : Create a new facilityType.
     *
     * @param facilityType the facilityType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityType, or with status {@code 400 (Bad Request)} if the facilityType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityType> createFacilityType(@Valid @RequestBody FacilityType facilityType) throws URISyntaxException {
        LOG.debug("REST request to save FacilityType : {}", facilityType);
        if (facilityType.getId() != null) {
            throw new BadRequestAlertException("A new facilityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityType = facilityTypeService.save(facilityType);
        return ResponseEntity.created(new URI("/api/facility-types/" + facilityType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityType.getId().toString()))
            .body(facilityType);
    }

    /**
     * {@code PUT  /facility-types/:id} : Updates an existing facilityType.
     *
     * @param id the id of the facilityType to save.
     * @param facilityType the facilityType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityType,
     * or with status {@code 400 (Bad Request)} if the facilityType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityType> updateFacilityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityType facilityType
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityType : {}, {}", id, facilityType);
        if (facilityType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityType = facilityTypeService.update(facilityType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityType.getId().toString()))
            .body(facilityType);
    }

    /**
     * {@code PATCH  /facility-types/:id} : Partial updates given fields of an existing facilityType, field will ignore if it is null
     *
     * @param id the id of the facilityType to save.
     * @param facilityType the facilityType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityType,
     * or with status {@code 400 (Bad Request)} if the facilityType is not valid,
     * or with status {@code 404 (Not Found)} if the facilityType is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityType> partialUpdateFacilityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityType facilityType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityType : {}, {}", id, facilityType);
        if (facilityType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityType> result = facilityTypeService.partialUpdate(facilityType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityType.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-types} : get all the Facility Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityType>> getAllFacilityTypes(FacilityTypeCriteria criteria) {
        LOG.debug("REST request to get FacilityTypes by criteria: {}", criteria);

        List<FacilityType> entityList = facilityTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /facility-types/count} : count all the facilityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityTypes(FacilityTypeCriteria criteria) {
        LOG.debug("REST request to count FacilityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-types/:id} : get the "id" facilityType.
     *
     * @param id the id of the facilityType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityType> getFacilityType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityType : {}", id);
        Optional<FacilityType> facilityType = facilityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityType);
    }

    /**
     * {@code DELETE  /facility-types/:id} : delete the "id" facilityType.
     *
     * @param id the id of the facilityType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityType : {}", id);
        facilityTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
