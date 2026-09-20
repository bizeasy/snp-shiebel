package com.snp.web.rest;

import com.snp.domain.FacilityGroupType;
import com.snp.repository.FacilityGroupTypeRepository;
import com.snp.service.FacilityGroupTypeQueryService;
import com.snp.service.FacilityGroupTypeService;
import com.snp.service.criteria.FacilityGroupTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityGroupType}.
 */
@RestController
@RequestMapping("/api/facility-group-types")
public class FacilityGroupTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupTypeResource.class);

    private static final String ENTITY_NAME = "facilityGroupType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityGroupTypeService facilityGroupTypeService;

    private final FacilityGroupTypeRepository facilityGroupTypeRepository;

    private final FacilityGroupTypeQueryService facilityGroupTypeQueryService;

    public FacilityGroupTypeResource(
        FacilityGroupTypeService facilityGroupTypeService,
        FacilityGroupTypeRepository facilityGroupTypeRepository,
        FacilityGroupTypeQueryService facilityGroupTypeQueryService
    ) {
        this.facilityGroupTypeService = facilityGroupTypeService;
        this.facilityGroupTypeRepository = facilityGroupTypeRepository;
        this.facilityGroupTypeQueryService = facilityGroupTypeQueryService;
    }

    /**
     * {@code POST  /facility-group-types} : Create a new facilityGroupType.
     *
     * @param facilityGroupType the facilityGroupType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityGroupType, or with status {@code 400 (Bad Request)} if the facilityGroupType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityGroupType> createFacilityGroupType(@Valid @RequestBody FacilityGroupType facilityGroupType)
        throws URISyntaxException {
        LOG.debug("REST request to save FacilityGroupType : {}", facilityGroupType);
        if (facilityGroupType.getId() != null) {
            throw new BadRequestAlertException("A new facilityGroupType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityGroupType = facilityGroupTypeService.save(facilityGroupType);
        return ResponseEntity.created(new URI("/api/facility-group-types/" + facilityGroupType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityGroupType.getId().toString()))
            .body(facilityGroupType);
    }

    /**
     * {@code PUT  /facility-group-types/:id} : Updates an existing facilityGroupType.
     *
     * @param id the id of the facilityGroupType to save.
     * @param facilityGroupType the facilityGroupType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroupType,
     * or with status {@code 400 (Bad Request)} if the facilityGroupType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroupType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityGroupType> updateFacilityGroupType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityGroupType facilityGroupType
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityGroupType : {}, {}", id, facilityGroupType);
        if (facilityGroupType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroupType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityGroupType = facilityGroupTypeService.update(facilityGroupType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroupType.getId().toString()))
            .body(facilityGroupType);
    }

    /**
     * {@code PATCH  /facility-group-types/:id} : Partial updates given fields of an existing facilityGroupType, field will ignore if it is null
     *
     * @param id the id of the facilityGroupType to save.
     * @param facilityGroupType the facilityGroupType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroupType,
     * or with status {@code 400 (Bad Request)} if the facilityGroupType is not valid,
     * or with status {@code 404 (Not Found)} if the facilityGroupType is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroupType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityGroupType> partialUpdateFacilityGroupType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityGroupType facilityGroupType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityGroupType : {}, {}", id, facilityGroupType);
        if (facilityGroupType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroupType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityGroupType> result = facilityGroupTypeService.partialUpdate(facilityGroupType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroupType.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-group-types} : get all the Facility Group Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Group Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityGroupType>> getAllFacilityGroupTypes(FacilityGroupTypeCriteria criteria) {
        LOG.debug("REST request to get FacilityGroupTypes by criteria: {}", criteria);

        List<FacilityGroupType> entityList = facilityGroupTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /facility-group-types/count} : count all the facilityGroupTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityGroupTypes(FacilityGroupTypeCriteria criteria) {
        LOG.debug("REST request to count FacilityGroupTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityGroupTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-group-types/:id} : get the "id" facilityGroupType.
     *
     * @param id the id of the facilityGroupType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityGroupType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityGroupType> getFacilityGroupType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityGroupType : {}", id);
        Optional<FacilityGroupType> facilityGroupType = facilityGroupTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityGroupType);
    }

    /**
     * {@code DELETE  /facility-group-types/:id} : delete the "id" facilityGroupType.
     *
     * @param id the id of the facilityGroupType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityGroupType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityGroupType : {}", id);
        facilityGroupTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
