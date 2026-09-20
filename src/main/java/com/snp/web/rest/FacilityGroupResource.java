package com.snp.web.rest;

import com.snp.domain.FacilityGroup;
import com.snp.repository.FacilityGroupRepository;
import com.snp.service.FacilityGroupQueryService;
import com.snp.service.FacilityGroupService;
import com.snp.service.criteria.FacilityGroupCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityGroup}.
 */
@RestController
@RequestMapping("/api/facility-groups")
public class FacilityGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupResource.class);

    private static final String ENTITY_NAME = "facilityGroup";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityGroupService facilityGroupService;

    private final FacilityGroupRepository facilityGroupRepository;

    private final FacilityGroupQueryService facilityGroupQueryService;

    public FacilityGroupResource(
        FacilityGroupService facilityGroupService,
        FacilityGroupRepository facilityGroupRepository,
        FacilityGroupQueryService facilityGroupQueryService
    ) {
        this.facilityGroupService = facilityGroupService;
        this.facilityGroupRepository = facilityGroupRepository;
        this.facilityGroupQueryService = facilityGroupQueryService;
    }

    /**
     * {@code POST  /facility-groups} : Create a new facilityGroup.
     *
     * @param facilityGroup the facilityGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityGroup, or with status {@code 400 (Bad Request)} if the facilityGroup already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityGroup> createFacilityGroup(@Valid @RequestBody FacilityGroup facilityGroup) throws URISyntaxException {
        LOG.debug("REST request to save FacilityGroup : {}", facilityGroup);
        if (facilityGroup.getId() != null) {
            throw new BadRequestAlertException("A new facilityGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityGroup = facilityGroupService.save(facilityGroup);
        return ResponseEntity.created(new URI("/api/facility-groups/" + facilityGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityGroup.getId().toString()))
            .body(facilityGroup);
    }

    /**
     * {@code PUT  /facility-groups/:id} : Updates an existing facilityGroup.
     *
     * @param id the id of the facilityGroup to save.
     * @param facilityGroup the facilityGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroup,
     * or with status {@code 400 (Bad Request)} if the facilityGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityGroup> updateFacilityGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityGroup facilityGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityGroup : {}, {}", id, facilityGroup);
        if (facilityGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityGroup = facilityGroupService.update(facilityGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroup.getId().toString()))
            .body(facilityGroup);
    }

    /**
     * {@code PATCH  /facility-groups/:id} : Partial updates given fields of an existing facilityGroup, field will ignore if it is null
     *
     * @param id the id of the facilityGroup to save.
     * @param facilityGroup the facilityGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroup,
     * or with status {@code 400 (Bad Request)} if the facilityGroup is not valid,
     * or with status {@code 404 (Not Found)} if the facilityGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityGroup> partialUpdateFacilityGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityGroup facilityGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityGroup : {}, {}", id, facilityGroup);
        if (facilityGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityGroup> result = facilityGroupService.partialUpdate(facilityGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-groups} : get all the Facility Groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Groups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityGroup>> getAllFacilityGroups(FacilityGroupCriteria criteria) {
        LOG.debug("REST request to get FacilityGroups by criteria: {}", criteria);

        List<FacilityGroup> entityList = facilityGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /facility-groups/count} : count all the facilityGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityGroups(FacilityGroupCriteria criteria) {
        LOG.debug("REST request to count FacilityGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-groups/:id} : get the "id" facilityGroup.
     *
     * @param id the id of the facilityGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityGroup> getFacilityGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityGroup : {}", id);
        Optional<FacilityGroup> facilityGroup = facilityGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityGroup);
    }

    /**
     * {@code DELETE  /facility-groups/:id} : delete the "id" facilityGroup.
     *
     * @param id the id of the facilityGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityGroup : {}", id);
        facilityGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
