package com.snp.web.rest;

import com.snp.domain.FacilityLocation;
import com.snp.repository.FacilityLocationRepository;
import com.snp.service.FacilityLocationQueryService;
import com.snp.service.FacilityLocationService;
import com.snp.service.criteria.FacilityLocationCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.FacilityLocation}.
 */
@RestController
@RequestMapping("/api/facility-locations")
public class FacilityLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityLocationResource.class);

    private static final String ENTITY_NAME = "facilityLocation";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityLocationService facilityLocationService;

    private final FacilityLocationRepository facilityLocationRepository;

    private final FacilityLocationQueryService facilityLocationQueryService;

    public FacilityLocationResource(
        FacilityLocationService facilityLocationService,
        FacilityLocationRepository facilityLocationRepository,
        FacilityLocationQueryService facilityLocationQueryService
    ) {
        this.facilityLocationService = facilityLocationService;
        this.facilityLocationRepository = facilityLocationRepository;
        this.facilityLocationQueryService = facilityLocationQueryService;
    }

    /**
     * {@code POST  /facility-locations} : Create a new facilityLocation.
     *
     * @param facilityLocation the facilityLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityLocation, or with status {@code 400 (Bad Request)} if the facilityLocation already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityLocation> createFacilityLocation(@Valid @RequestBody FacilityLocation facilityLocation)
        throws URISyntaxException {
        LOG.debug("REST request to save FacilityLocation : {}", facilityLocation);
        if (facilityLocation.getId() != null) {
            throw new BadRequestAlertException("A new facilityLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityLocation = facilityLocationService.save(facilityLocation);
        return ResponseEntity.created(new URI("/api/facility-locations/" + facilityLocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityLocation.getId().toString()))
            .body(facilityLocation);
    }

    /**
     * {@code PUT  /facility-locations/:id} : Updates an existing facilityLocation.
     *
     * @param id the id of the facilityLocation to save.
     * @param facilityLocation the facilityLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityLocation,
     * or with status {@code 400 (Bad Request)} if the facilityLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityLocation> updateFacilityLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityLocation facilityLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityLocation : {}, {}", id, facilityLocation);
        if (facilityLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityLocation = facilityLocationService.update(facilityLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityLocation.getId().toString()))
            .body(facilityLocation);
    }

    /**
     * {@code PATCH  /facility-locations/:id} : Partial updates given fields of an existing facilityLocation, field will ignore if it is null
     *
     * @param id the id of the facilityLocation to save.
     * @param facilityLocation the facilityLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityLocation,
     * or with status {@code 400 (Bad Request)} if the facilityLocation is not valid,
     * or with status {@code 404 (Not Found)} if the facilityLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityLocation> partialUpdateFacilityLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityLocation facilityLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityLocation : {}, {}", id, facilityLocation);
        if (facilityLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityLocation> result = facilityLocationService.partialUpdate(facilityLocation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-locations} : get all the Facility Locations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Locations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityLocation>> getAllFacilityLocations(
        FacilityLocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FacilityLocations by criteria: {}", criteria);

        Page<FacilityLocation> page = facilityLocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-locations/count} : count all the facilityLocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityLocations(FacilityLocationCriteria criteria) {
        LOG.debug("REST request to count FacilityLocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityLocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-locations/:id} : get the "id" facilityLocation.
     *
     * @param id the id of the facilityLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityLocation> getFacilityLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityLocation : {}", id);
        Optional<FacilityLocation> facilityLocation = facilityLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityLocation);
    }

    /**
     * {@code DELETE  /facility-locations/:id} : delete the "id" facilityLocation.
     *
     * @param id the id of the facilityLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityLocation : {}", id);
        facilityLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
