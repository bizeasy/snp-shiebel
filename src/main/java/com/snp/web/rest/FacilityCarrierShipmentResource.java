package com.snp.web.rest;

import com.snp.domain.FacilityCarrierShipment;
import com.snp.repository.FacilityCarrierShipmentRepository;
import com.snp.service.FacilityCarrierShipmentQueryService;
import com.snp.service.FacilityCarrierShipmentService;
import com.snp.service.criteria.FacilityCarrierShipmentCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityCarrierShipment}.
 */
@RestController
@RequestMapping("/api/facility-carrier-shipments")
public class FacilityCarrierShipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityCarrierShipmentResource.class);

    private static final String ENTITY_NAME = "facilityCarrierShipment";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityCarrierShipmentService facilityCarrierShipmentService;

    private final FacilityCarrierShipmentRepository facilityCarrierShipmentRepository;

    private final FacilityCarrierShipmentQueryService facilityCarrierShipmentQueryService;

    public FacilityCarrierShipmentResource(
        FacilityCarrierShipmentService facilityCarrierShipmentService,
        FacilityCarrierShipmentRepository facilityCarrierShipmentRepository,
        FacilityCarrierShipmentQueryService facilityCarrierShipmentQueryService
    ) {
        this.facilityCarrierShipmentService = facilityCarrierShipmentService;
        this.facilityCarrierShipmentRepository = facilityCarrierShipmentRepository;
        this.facilityCarrierShipmentQueryService = facilityCarrierShipmentQueryService;
    }

    /**
     * {@code POST  /facility-carrier-shipments} : Create a new facilityCarrierShipment.
     *
     * @param facilityCarrierShipment the facilityCarrierShipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityCarrierShipment, or with status {@code 400 (Bad Request)} if the facilityCarrierShipment already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityCarrierShipment> createFacilityCarrierShipment(
        @Valid @RequestBody FacilityCarrierShipment facilityCarrierShipment
    ) throws URISyntaxException {
        LOG.debug("REST request to save FacilityCarrierShipment : {}", facilityCarrierShipment);
        if (facilityCarrierShipment.getId() != null) {
            throw new BadRequestAlertException("A new facilityCarrierShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityCarrierShipment = facilityCarrierShipmentService.save(facilityCarrierShipment);
        return ResponseEntity.created(new URI("/api/facility-carrier-shipments/" + facilityCarrierShipment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityCarrierShipment.getId().toString()))
            .body(facilityCarrierShipment);
    }

    /**
     * {@code PUT  /facility-carrier-shipments/:id} : Updates an existing facilityCarrierShipment.
     *
     * @param id the id of the facilityCarrierShipment to save.
     * @param facilityCarrierShipment the facilityCarrierShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityCarrierShipment,
     * or with status {@code 400 (Bad Request)} if the facilityCarrierShipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityCarrierShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityCarrierShipment> updateFacilityCarrierShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityCarrierShipment facilityCarrierShipment
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityCarrierShipment : {}, {}", id, facilityCarrierShipment);
        if (facilityCarrierShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityCarrierShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityCarrierShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityCarrierShipment = facilityCarrierShipmentService.update(facilityCarrierShipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityCarrierShipment.getId().toString()))
            .body(facilityCarrierShipment);
    }

    /**
     * {@code PATCH  /facility-carrier-shipments/:id} : Partial updates given fields of an existing facilityCarrierShipment, field will ignore if it is null
     *
     * @param id the id of the facilityCarrierShipment to save.
     * @param facilityCarrierShipment the facilityCarrierShipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityCarrierShipment,
     * or with status {@code 400 (Bad Request)} if the facilityCarrierShipment is not valid,
     * or with status {@code 404 (Not Found)} if the facilityCarrierShipment is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityCarrierShipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityCarrierShipment> partialUpdateFacilityCarrierShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityCarrierShipment facilityCarrierShipment
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityCarrierShipment : {}, {}", id, facilityCarrierShipment);
        if (facilityCarrierShipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityCarrierShipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityCarrierShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityCarrierShipment> result = facilityCarrierShipmentService.partialUpdate(facilityCarrierShipment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityCarrierShipment.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-carrier-shipments} : get all the Facility Carrier Shipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Carrier Shipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityCarrierShipment>> getAllFacilityCarrierShipments(FacilityCarrierShipmentCriteria criteria) {
        LOG.debug("REST request to get FacilityCarrierShipments by criteria: {}", criteria);

        List<FacilityCarrierShipment> entityList = facilityCarrierShipmentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /facility-carrier-shipments/count} : count all the facilityCarrierShipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityCarrierShipments(FacilityCarrierShipmentCriteria criteria) {
        LOG.debug("REST request to count FacilityCarrierShipments by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityCarrierShipmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-carrier-shipments/:id} : get the "id" facilityCarrierShipment.
     *
     * @param id the id of the facilityCarrierShipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityCarrierShipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityCarrierShipment> getFacilityCarrierShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityCarrierShipment : {}", id);
        Optional<FacilityCarrierShipment> facilityCarrierShipment = facilityCarrierShipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityCarrierShipment);
    }

    /**
     * {@code DELETE  /facility-carrier-shipments/:id} : delete the "id" facilityCarrierShipment.
     *
     * @param id the id of the facilityCarrierShipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityCarrierShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityCarrierShipment : {}", id);
        facilityCarrierShipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
