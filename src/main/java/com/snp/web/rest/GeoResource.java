package com.snp.web.rest;

import com.snp.domain.Geo;
import com.snp.repository.GeoRepository;
import com.snp.service.GeoQueryService;
import com.snp.service.GeoService;
import com.snp.service.criteria.GeoCriteria;
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
 * REST controller for managing {@link com.snp.domain.Geo}.
 */
@RestController
@RequestMapping("/api/geos")
public class GeoResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeoResource.class);

    private static final String ENTITY_NAME = "geo";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final GeoService geoService;

    private final GeoRepository geoRepository;

    private final GeoQueryService geoQueryService;

    public GeoResource(GeoService geoService, GeoRepository geoRepository, GeoQueryService geoQueryService) {
        this.geoService = geoService;
        this.geoRepository = geoRepository;
        this.geoQueryService = geoQueryService;
    }

    /**
     * {@code POST  /geos} : Create a new geo.
     *
     * @param geo the geo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geo, or with status {@code 400 (Bad Request)} if the geo already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Geo> createGeo(@Valid @RequestBody Geo geo) throws URISyntaxException {
        LOG.debug("REST request to save Geo : {}", geo);
        if (geo.getId() != null) {
            throw new BadRequestAlertException("A new geo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        geo = geoService.save(geo);
        return ResponseEntity.created(new URI("/api/geos/" + geo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, geo.getId().toString()))
            .body(geo);
    }

    /**
     * {@code PUT  /geos/:id} : Updates an existing geo.
     *
     * @param id the id of the geo to save.
     * @param geo the geo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geo,
     * or with status {@code 400 (Bad Request)} if the geo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Geo> updateGeo(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Geo geo)
        throws URISyntaxException {
        LOG.debug("REST request to update Geo : {}, {}", id, geo);
        if (geo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        geo = geoService.update(geo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geo.getId().toString()))
            .body(geo);
    }

    /**
     * {@code PATCH  /geos/:id} : Partial updates given fields of an existing geo, field will ignore if it is null
     *
     * @param id the id of the geo to save.
     * @param geo the geo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geo,
     * or with status {@code 400 (Bad Request)} if the geo is not valid,
     * or with status {@code 404 (Not Found)} if the geo is not found,
     * or with status {@code 500 (Internal Server Error)} if the geo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Geo> partialUpdateGeo(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Geo geo)
        throws URISyntaxException {
        LOG.debug("REST request to partially update Geo : {}, {}", id, geo);
        if (geo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Geo> result = geoService.partialUpdate(geo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geo.getId().toString())
        );
    }

    /**
     * {@code GET  /geos} : get all the Geos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Geos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Geo>> getAllGeos(GeoCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Geos by criteria: {}", criteria);

        Page<Geo> page = geoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /geos/count} : count all the geos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGeos(GeoCriteria criteria) {
        LOG.debug("REST request to count Geos by criteria: {}", criteria);
        return ResponseEntity.ok().body(geoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /geos/:id} : get the "id" geo.
     *
     * @param id the id of the geo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Geo> getGeo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Geo : {}", id);
        Optional<Geo> geo = geoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geo);
    }

    /**
     * {@code DELETE  /geos/:id} : delete the "id" geo.
     *
     * @param id the id of the geo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Geo : {}", id);
        geoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
