package com.snp.web.rest;

import com.snp.domain.FacilityParty;
import com.snp.repository.FacilityPartyRepository;
import com.snp.service.FacilityPartyQueryService;
import com.snp.service.FacilityPartyService;
import com.snp.service.criteria.FacilityPartyCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityParty}.
 */
@RestController
@RequestMapping("/api/facility-parties")
public class FacilityPartyResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityPartyResource.class);

    private static final String ENTITY_NAME = "facilityParty";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityPartyService facilityPartyService;

    private final FacilityPartyRepository facilityPartyRepository;

    private final FacilityPartyQueryService facilityPartyQueryService;

    public FacilityPartyResource(
        FacilityPartyService facilityPartyService,
        FacilityPartyRepository facilityPartyRepository,
        FacilityPartyQueryService facilityPartyQueryService
    ) {
        this.facilityPartyService = facilityPartyService;
        this.facilityPartyRepository = facilityPartyRepository;
        this.facilityPartyQueryService = facilityPartyQueryService;
    }

    /**
     * {@code POST  /facility-parties} : Create a new facilityParty.
     *
     * @param facilityParty the facilityParty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityParty, or with status {@code 400 (Bad Request)} if the facilityParty already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityParty> createFacilityParty(@Valid @RequestBody FacilityParty facilityParty) throws URISyntaxException {
        LOG.debug("REST request to save FacilityParty : {}", facilityParty);
        if (facilityParty.getId() != null) {
            throw new BadRequestAlertException("A new facilityParty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityParty = facilityPartyService.save(facilityParty);
        return ResponseEntity.created(new URI("/api/facility-parties/" + facilityParty.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityParty.getId().toString()))
            .body(facilityParty);
    }

    /**
     * {@code PUT  /facility-parties/:id} : Updates an existing facilityParty.
     *
     * @param id the id of the facilityParty to save.
     * @param facilityParty the facilityParty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityParty,
     * or with status {@code 400 (Bad Request)} if the facilityParty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityParty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityParty> updateFacilityParty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityParty facilityParty
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityParty : {}, {}", id, facilityParty);
        if (facilityParty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityParty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityPartyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityParty = facilityPartyService.update(facilityParty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityParty.getId().toString()))
            .body(facilityParty);
    }

    /**
     * {@code PATCH  /facility-parties/:id} : Partial updates given fields of an existing facilityParty, field will ignore if it is null
     *
     * @param id the id of the facilityParty to save.
     * @param facilityParty the facilityParty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityParty,
     * or with status {@code 400 (Bad Request)} if the facilityParty is not valid,
     * or with status {@code 404 (Not Found)} if the facilityParty is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityParty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityParty> partialUpdateFacilityParty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityParty facilityParty
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityParty : {}, {}", id, facilityParty);
        if (facilityParty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityParty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityPartyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityParty> result = facilityPartyService.partialUpdate(facilityParty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityParty.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-parties} : get all the Facility Parties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Parties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityParty>> getAllFacilityParties(
        FacilityPartyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FacilityParties by criteria: {}", criteria);

        Page<FacilityParty> page = facilityPartyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-parties/count} : count all the facilityParties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityParties(FacilityPartyCriteria criteria) {
        LOG.debug("REST request to count FacilityParties by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityPartyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-parties/:id} : get the "id" facilityParty.
     *
     * @param id the id of the facilityParty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityParty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityParty> getFacilityParty(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityParty : {}", id);
        Optional<FacilityParty> facilityParty = facilityPartyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityParty);
    }

    /**
     * {@code DELETE  /facility-parties/:id} : delete the "id" facilityParty.
     *
     * @param id the id of the facilityParty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityParty(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityParty : {}", id);
        facilityPartyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
