package com.snp.web.rest;

import com.snp.domain.GeoAssoc;
import com.snp.repository.GeoAssocRepository;
import com.snp.service.GeoAssocQueryService;
import com.snp.service.GeoAssocService;
import com.snp.service.criteria.GeoAssocCriteria;
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
 * REST controller for managing {@link com.snp.domain.GeoAssoc}.
 */
@RestController
@RequestMapping("/api/geo-assocs")
public class GeoAssocResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocResource.class);

    private static final String ENTITY_NAME = "geoAssoc";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final GeoAssocService geoAssocService;

    private final GeoAssocRepository geoAssocRepository;

    private final GeoAssocQueryService geoAssocQueryService;

    public GeoAssocResource(
        GeoAssocService geoAssocService,
        GeoAssocRepository geoAssocRepository,
        GeoAssocQueryService geoAssocQueryService
    ) {
        this.geoAssocService = geoAssocService;
        this.geoAssocRepository = geoAssocRepository;
        this.geoAssocQueryService = geoAssocQueryService;
    }

    /**
     * {@code POST  /geo-assocs} : Create a new geoAssoc.
     *
     * @param geoAssoc the geoAssoc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geoAssoc, or with status {@code 400 (Bad Request)} if the geoAssoc already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GeoAssoc> createGeoAssoc(@Valid @RequestBody GeoAssoc geoAssoc) throws URISyntaxException {
        LOG.debug("REST request to save GeoAssoc : {}", geoAssoc);
        if (geoAssoc.getId() != null) {
            throw new BadRequestAlertException("A new geoAssoc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        geoAssoc = geoAssocService.save(geoAssoc);
        return ResponseEntity.created(new URI("/api/geo-assocs/" + geoAssoc.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, geoAssoc.getId().toString()))
            .body(geoAssoc);
    }

    /**
     * {@code PUT  /geo-assocs/:id} : Updates an existing geoAssoc.
     *
     * @param id the id of the geoAssoc to save.
     * @param geoAssoc the geoAssoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoAssoc,
     * or with status {@code 400 (Bad Request)} if the geoAssoc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geoAssoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GeoAssoc> updateGeoAssoc(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GeoAssoc geoAssoc
    ) throws URISyntaxException {
        LOG.debug("REST request to update GeoAssoc : {}, {}", id, geoAssoc);
        if (geoAssoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoAssoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoAssocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        geoAssoc = geoAssocService.update(geoAssoc);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoAssoc.getId().toString()))
            .body(geoAssoc);
    }

    /**
     * {@code PATCH  /geo-assocs/:id} : Partial updates given fields of an existing geoAssoc, field will ignore if it is null
     *
     * @param id the id of the geoAssoc to save.
     * @param geoAssoc the geoAssoc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoAssoc,
     * or with status {@code 400 (Bad Request)} if the geoAssoc is not valid,
     * or with status {@code 404 (Not Found)} if the geoAssoc is not found,
     * or with status {@code 500 (Internal Server Error)} if the geoAssoc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeoAssoc> partialUpdateGeoAssoc(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GeoAssoc geoAssoc
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update GeoAssoc : {}, {}", id, geoAssoc);
        if (geoAssoc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoAssoc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoAssocRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeoAssoc> result = geoAssocService.partialUpdate(geoAssoc);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoAssoc.getId().toString())
        );
    }

    /**
     * {@code GET  /geo-assocs} : get all the Geo Assocs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Geo Assocs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GeoAssoc>> getAllGeoAssocs(
        GeoAssocCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GeoAssocs by criteria: {}", criteria);

        Page<GeoAssoc> page = geoAssocQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /geo-assocs/count} : count all the geoAssocs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGeoAssocs(GeoAssocCriteria criteria) {
        LOG.debug("REST request to count GeoAssocs by criteria: {}", criteria);
        return ResponseEntity.ok().body(geoAssocQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /geo-assocs/:id} : get the "id" geoAssoc.
     *
     * @param id the id of the geoAssoc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geoAssoc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GeoAssoc> getGeoAssoc(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GeoAssoc : {}", id);
        Optional<GeoAssoc> geoAssoc = geoAssocService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geoAssoc);
    }

    /**
     * {@code DELETE  /geo-assocs/:id} : delete the "id" geoAssoc.
     *
     * @param id the id of the geoAssoc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeoAssoc(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GeoAssoc : {}", id);
        geoAssocService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
