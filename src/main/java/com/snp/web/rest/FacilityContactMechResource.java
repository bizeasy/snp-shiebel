package com.snp.web.rest;

import com.snp.domain.FacilityContactMech;
import com.snp.repository.FacilityContactMechRepository;
import com.snp.service.FacilityContactMechQueryService;
import com.snp.service.FacilityContactMechService;
import com.snp.service.criteria.FacilityContactMechCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityContactMech}.
 */
@RestController
@RequestMapping("/api/facility-contact-meches")
public class FacilityContactMechResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityContactMechResource.class);

    private static final String ENTITY_NAME = "facilityContactMech";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityContactMechService facilityContactMechService;

    private final FacilityContactMechRepository facilityContactMechRepository;

    private final FacilityContactMechQueryService facilityContactMechQueryService;

    public FacilityContactMechResource(
        FacilityContactMechService facilityContactMechService,
        FacilityContactMechRepository facilityContactMechRepository,
        FacilityContactMechQueryService facilityContactMechQueryService
    ) {
        this.facilityContactMechService = facilityContactMechService;
        this.facilityContactMechRepository = facilityContactMechRepository;
        this.facilityContactMechQueryService = facilityContactMechQueryService;
    }

    /**
     * {@code POST  /facility-contact-meches} : Create a new facilityContactMech.
     *
     * @param facilityContactMech the facilityContactMech to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityContactMech, or with status {@code 400 (Bad Request)} if the facilityContactMech already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityContactMech> createFacilityContactMech(@Valid @RequestBody FacilityContactMech facilityContactMech)
        throws URISyntaxException {
        LOG.debug("REST request to save FacilityContactMech : {}", facilityContactMech);
        if (facilityContactMech.getId() != null) {
            throw new BadRequestAlertException("A new facilityContactMech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityContactMech = facilityContactMechService.save(facilityContactMech);
        return ResponseEntity.created(new URI("/api/facility-contact-meches/" + facilityContactMech.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityContactMech.getId().toString()))
            .body(facilityContactMech);
    }

    /**
     * {@code PUT  /facility-contact-meches/:id} : Updates an existing facilityContactMech.
     *
     * @param id the id of the facilityContactMech to save.
     * @param facilityContactMech the facilityContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityContactMech,
     * or with status {@code 400 (Bad Request)} if the facilityContactMech is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityContactMech> updateFacilityContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityContactMech facilityContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityContactMech : {}, {}", id, facilityContactMech);
        if (facilityContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityContactMech = facilityContactMechService.update(facilityContactMech);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityContactMech.getId().toString()))
            .body(facilityContactMech);
    }

    /**
     * {@code PATCH  /facility-contact-meches/:id} : Partial updates given fields of an existing facilityContactMech, field will ignore if it is null
     *
     * @param id the id of the facilityContactMech to save.
     * @param facilityContactMech the facilityContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityContactMech,
     * or with status {@code 400 (Bad Request)} if the facilityContactMech is not valid,
     * or with status {@code 404 (Not Found)} if the facilityContactMech is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityContactMech> partialUpdateFacilityContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityContactMech facilityContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityContactMech : {}, {}", id, facilityContactMech);
        if (facilityContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityContactMech> result = facilityContactMechService.partialUpdate(facilityContactMech);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityContactMech.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-contact-meches} : get all the Facility Contact Meches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Contact Meches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityContactMech>> getAllFacilityContactMeches(
        FacilityContactMechCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FacilityContactMeches by criteria: {}", criteria);

        Page<FacilityContactMech> page = facilityContactMechQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-contact-meches/count} : count all the facilityContactMeches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityContactMeches(FacilityContactMechCriteria criteria) {
        LOG.debug("REST request to count FacilityContactMeches by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityContactMechQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-contact-meches/:id} : get the "id" facilityContactMech.
     *
     * @param id the id of the facilityContactMech to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityContactMech, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityContactMech> getFacilityContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityContactMech : {}", id);
        Optional<FacilityContactMech> facilityContactMech = facilityContactMechService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityContactMech);
    }

    /**
     * {@code DELETE  /facility-contact-meches/:id} : delete the "id" facilityContactMech.
     *
     * @param id the id of the facilityContactMech to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityContactMech : {}", id);
        facilityContactMechService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
