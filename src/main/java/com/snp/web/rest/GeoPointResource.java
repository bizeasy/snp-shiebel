package com.snp.web.rest;

import com.snp.domain.GeoPoint;
import com.snp.repository.GeoPointRepository;
import com.snp.service.GeoPointQueryService;
import com.snp.service.GeoPointService;
import com.snp.service.criteria.GeoPointCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.snp.domain.GeoPoint}.
 */
@RestController
@RequestMapping("/api/geo-points")
public class GeoPointResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeoPointResource.class);

    private static final String ENTITY_NAME = "geoPoint";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final GeoPointService geoPointService;

    private final GeoPointRepository geoPointRepository;

    private final GeoPointQueryService geoPointQueryService;

    public GeoPointResource(
        GeoPointService geoPointService,
        GeoPointRepository geoPointRepository,
        GeoPointQueryService geoPointQueryService
    ) {
        this.geoPointService = geoPointService;
        this.geoPointRepository = geoPointRepository;
        this.geoPointQueryService = geoPointQueryService;
    }

    /**
     * {@code POST  /geo-points} : Create a new geoPoint.
     *
     * @param geoPoint the geoPoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geoPoint, or with status {@code 400 (Bad Request)} if the geoPoint already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GeoPoint> createGeoPoint(@RequestBody GeoPoint geoPoint) throws URISyntaxException {
        LOG.debug("REST request to save GeoPoint : {}", geoPoint);
        if (geoPoint.getId() != null) {
            throw new BadRequestAlertException("A new geoPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        geoPoint = geoPointService.save(geoPoint);
        return ResponseEntity.created(new URI("/api/geo-points/" + geoPoint.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, geoPoint.getId().toString()))
            .body(geoPoint);
    }

    /**
     * {@code PUT  /geo-points/:id} : Updates an existing geoPoint.
     *
     * @param id the id of the geoPoint to save.
     * @param geoPoint the geoPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoPoint,
     * or with status {@code 400 (Bad Request)} if the geoPoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geoPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GeoPoint> updateGeoPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeoPoint geoPoint
    ) throws URISyntaxException {
        LOG.debug("REST request to update GeoPoint : {}, {}", id, geoPoint);
        if (geoPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        geoPoint = geoPointService.update(geoPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoPoint.getId().toString()))
            .body(geoPoint);
    }

    /**
     * {@code PATCH  /geo-points/:id} : Partial updates given fields of an existing geoPoint, field will ignore if it is null
     *
     * @param id the id of the geoPoint to save.
     * @param geoPoint the geoPoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoPoint,
     * or with status {@code 400 (Bad Request)} if the geoPoint is not valid,
     * or with status {@code 404 (Not Found)} if the geoPoint is not found,
     * or with status {@code 500 (Internal Server Error)} if the geoPoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeoPoint> partialUpdateGeoPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeoPoint geoPoint
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update GeoPoint : {}, {}", id, geoPoint);
        if (geoPoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoPoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeoPoint> result = geoPointService.partialUpdate(geoPoint);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoPoint.getId().toString())
        );
    }

    /**
     * {@code GET  /geo-points} : get all the Geo Points.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Geo Points in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GeoPoint>> getAllGeoPoints(GeoPointCriteria criteria) {
        LOG.debug("REST request to get GeoPoints by criteria: {}", criteria);

        List<GeoPoint> entityList = geoPointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /geo-points/count} : count all the geoPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGeoPoints(GeoPointCriteria criteria) {
        LOG.debug("REST request to count GeoPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(geoPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /geo-points/:id} : get the "id" geoPoint.
     *
     * @param id the id of the geoPoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geoPoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GeoPoint> getGeoPoint(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GeoPoint : {}", id);
        Optional<GeoPoint> geoPoint = geoPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geoPoint);
    }

    /**
     * {@code DELETE  /geo-points/:id} : delete the "id" geoPoint.
     *
     * @param id the id of the geoPoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeoPoint(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GeoPoint : {}", id);
        geoPointService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
