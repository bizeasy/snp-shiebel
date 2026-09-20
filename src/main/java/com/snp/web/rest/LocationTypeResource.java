package com.snp.web.rest;

import com.snp.domain.LocationType;
import com.snp.repository.LocationTypeRepository;
import com.snp.service.LocationTypeQueryService;
import com.snp.service.LocationTypeService;
import com.snp.service.criteria.LocationTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.LocationType}.
 */
@RestController
@RequestMapping("/api/location-types")
public class LocationTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocationTypeResource.class);

    private static final String ENTITY_NAME = "locationType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final LocationTypeService locationTypeService;

    private final LocationTypeRepository locationTypeRepository;

    private final LocationTypeQueryService locationTypeQueryService;

    public LocationTypeResource(
        LocationTypeService locationTypeService,
        LocationTypeRepository locationTypeRepository,
        LocationTypeQueryService locationTypeQueryService
    ) {
        this.locationTypeService = locationTypeService;
        this.locationTypeRepository = locationTypeRepository;
        this.locationTypeQueryService = locationTypeQueryService;
    }

    /**
     * {@code POST  /location-types} : Create a new locationType.
     *
     * @param locationType the locationType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationType, or with status {@code 400 (Bad Request)} if the locationType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LocationType> createLocationType(@Valid @RequestBody LocationType locationType) throws URISyntaxException {
        LOG.debug("REST request to save LocationType : {}", locationType);
        if (locationType.getId() != null) {
            throw new BadRequestAlertException("A new locationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        locationType = locationTypeService.save(locationType);
        return ResponseEntity.created(new URI("/api/location-types/" + locationType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, locationType.getId().toString()))
            .body(locationType);
    }

    /**
     * {@code PUT  /location-types/:id} : Updates an existing locationType.
     *
     * @param id the id of the locationType to save.
     * @param locationType the locationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationType,
     * or with status {@code 400 (Bad Request)} if the locationType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocationType> updateLocationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocationType locationType
    ) throws URISyntaxException {
        LOG.debug("REST request to update LocationType : {}, {}", id, locationType);
        if (locationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        locationType = locationTypeService.update(locationType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationType.getId().toString()))
            .body(locationType);
    }

    /**
     * {@code PATCH  /location-types/:id} : Partial updates given fields of an existing locationType, field will ignore if it is null
     *
     * @param id the id of the locationType to save.
     * @param locationType the locationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationType,
     * or with status {@code 400 (Bad Request)} if the locationType is not valid,
     * or with status {@code 404 (Not Found)} if the locationType is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationType> partialUpdateLocationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocationType locationType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update LocationType : {}, {}", id, locationType);
        if (locationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationType> result = locationTypeService.partialUpdate(locationType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationType.getId().toString())
        );
    }

    /**
     * {@code GET  /location-types} : get all the Location Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Location Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LocationType>> getAllLocationTypes(LocationTypeCriteria criteria) {
        LOG.debug("REST request to get LocationTypes by criteria: {}", criteria);

        List<LocationType> entityList = locationTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /location-types/count} : count all the locationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLocationTypes(LocationTypeCriteria criteria) {
        LOG.debug("REST request to count LocationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /location-types/:id} : get the "id" locationType.
     *
     * @param id the id of the locationType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationType> getLocationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LocationType : {}", id);
        Optional<LocationType> locationType = locationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationType);
    }

    /**
     * {@code DELETE  /location-types/:id} : delete the "id" locationType.
     *
     * @param id the id of the locationType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LocationType : {}", id);
        locationTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
