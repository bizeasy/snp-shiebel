package com.snp.web.rest;

import com.snp.domain.GeoAssocType;
import com.snp.repository.GeoAssocTypeRepository;
import com.snp.service.GeoAssocTypeQueryService;
import com.snp.service.GeoAssocTypeService;
import com.snp.service.criteria.GeoAssocTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.GeoAssocType}.
 */
@RestController
@RequestMapping("/api/geo-assoc-types")
public class GeoAssocTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeoAssocTypeResource.class);

    private static final String ENTITY_NAME = "geoAssocType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final GeoAssocTypeService geoAssocTypeService;

    private final GeoAssocTypeRepository geoAssocTypeRepository;

    private final GeoAssocTypeQueryService geoAssocTypeQueryService;

    public GeoAssocTypeResource(
        GeoAssocTypeService geoAssocTypeService,
        GeoAssocTypeRepository geoAssocTypeRepository,
        GeoAssocTypeQueryService geoAssocTypeQueryService
    ) {
        this.geoAssocTypeService = geoAssocTypeService;
        this.geoAssocTypeRepository = geoAssocTypeRepository;
        this.geoAssocTypeQueryService = geoAssocTypeQueryService;
    }

    /**
     * {@code POST  /geo-assoc-types} : Create a new geoAssocType.
     *
     * @param geoAssocType the geoAssocType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geoAssocType, or with status {@code 400 (Bad Request)} if the geoAssocType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GeoAssocType> createGeoAssocType(@Valid @RequestBody GeoAssocType geoAssocType) throws URISyntaxException {
        LOG.debug("REST request to save GeoAssocType : {}", geoAssocType);
        if (geoAssocType.getId() != null) {
            throw new BadRequestAlertException("A new geoAssocType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        geoAssocType = geoAssocTypeService.save(geoAssocType);
        return ResponseEntity.created(new URI("/api/geo-assoc-types/" + geoAssocType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, geoAssocType.getId().toString()))
            .body(geoAssocType);
    }

    /**
     * {@code PUT  /geo-assoc-types/:id} : Updates an existing geoAssocType.
     *
     * @param id the id of the geoAssocType to save.
     * @param geoAssocType the geoAssocType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoAssocType,
     * or with status {@code 400 (Bad Request)} if the geoAssocType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geoAssocType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GeoAssocType> updateGeoAssocType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GeoAssocType geoAssocType
    ) throws URISyntaxException {
        LOG.debug("REST request to update GeoAssocType : {}, {}", id, geoAssocType);
        if (geoAssocType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoAssocType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoAssocTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        geoAssocType = geoAssocTypeService.update(geoAssocType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoAssocType.getId().toString()))
            .body(geoAssocType);
    }

    /**
     * {@code PATCH  /geo-assoc-types/:id} : Partial updates given fields of an existing geoAssocType, field will ignore if it is null
     *
     * @param id the id of the geoAssocType to save.
     * @param geoAssocType the geoAssocType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoAssocType,
     * or with status {@code 400 (Bad Request)} if the geoAssocType is not valid,
     * or with status {@code 404 (Not Found)} if the geoAssocType is not found,
     * or with status {@code 500 (Internal Server Error)} if the geoAssocType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeoAssocType> partialUpdateGeoAssocType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GeoAssocType geoAssocType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update GeoAssocType : {}, {}", id, geoAssocType);
        if (geoAssocType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoAssocType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoAssocTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeoAssocType> result = geoAssocTypeService.partialUpdate(geoAssocType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoAssocType.getId().toString())
        );
    }

    /**
     * {@code GET  /geo-assoc-types} : get all the Geo Assoc Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Geo Assoc Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GeoAssocType>> getAllGeoAssocTypes(GeoAssocTypeCriteria criteria) {
        LOG.debug("REST request to get GeoAssocTypes by criteria: {}", criteria);

        List<GeoAssocType> entityList = geoAssocTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /geo-assoc-types/count} : count all the geoAssocTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGeoAssocTypes(GeoAssocTypeCriteria criteria) {
        LOG.debug("REST request to count GeoAssocTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(geoAssocTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /geo-assoc-types/:id} : get the "id" geoAssocType.
     *
     * @param id the id of the geoAssocType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geoAssocType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GeoAssocType> getGeoAssocType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GeoAssocType : {}", id);
        Optional<GeoAssocType> geoAssocType = geoAssocTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geoAssocType);
    }

    /**
     * {@code DELETE  /geo-assoc-types/:id} : delete the "id" geoAssocType.
     *
     * @param id the id of the geoAssocType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeoAssocType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GeoAssocType : {}", id);
        geoAssocTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
