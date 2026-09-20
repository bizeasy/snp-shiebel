package com.snp.web.rest;

import com.snp.domain.FacilityGroupMember;
import com.snp.repository.FacilityGroupMemberRepository;
import com.snp.service.FacilityGroupMemberQueryService;
import com.snp.service.FacilityGroupMemberService;
import com.snp.service.criteria.FacilityGroupMemberCriteria;
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
 * REST controller for managing {@link com.snp.domain.FacilityGroupMember}.
 */
@RestController
@RequestMapping("/api/facility-group-members")
public class FacilityGroupMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupMemberResource.class);

    private static final String ENTITY_NAME = "facilityGroupMember";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final FacilityGroupMemberService facilityGroupMemberService;

    private final FacilityGroupMemberRepository facilityGroupMemberRepository;

    private final FacilityGroupMemberQueryService facilityGroupMemberQueryService;

    public FacilityGroupMemberResource(
        FacilityGroupMemberService facilityGroupMemberService,
        FacilityGroupMemberRepository facilityGroupMemberRepository,
        FacilityGroupMemberQueryService facilityGroupMemberQueryService
    ) {
        this.facilityGroupMemberService = facilityGroupMemberService;
        this.facilityGroupMemberRepository = facilityGroupMemberRepository;
        this.facilityGroupMemberQueryService = facilityGroupMemberQueryService;
    }

    /**
     * {@code POST  /facility-group-members} : Create a new facilityGroupMember.
     *
     * @param facilityGroupMember the facilityGroupMember to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityGroupMember, or with status {@code 400 (Bad Request)} if the facilityGroupMember already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityGroupMember> createFacilityGroupMember(@Valid @RequestBody FacilityGroupMember facilityGroupMember)
        throws URISyntaxException {
        LOG.debug("REST request to save FacilityGroupMember : {}", facilityGroupMember);
        if (facilityGroupMember.getId() != null) {
            throw new BadRequestAlertException("A new facilityGroupMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityGroupMember = facilityGroupMemberService.save(facilityGroupMember);
        return ResponseEntity.created(new URI("/api/facility-group-members/" + facilityGroupMember.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityGroupMember.getId().toString()))
            .body(facilityGroupMember);
    }

    /**
     * {@code PUT  /facility-group-members/:id} : Updates an existing facilityGroupMember.
     *
     * @param id the id of the facilityGroupMember to save.
     * @param facilityGroupMember the facilityGroupMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroupMember,
     * or with status {@code 400 (Bad Request)} if the facilityGroupMember is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroupMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityGroupMember> updateFacilityGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityGroupMember facilityGroupMember
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityGroupMember : {}, {}", id, facilityGroupMember);
        if (facilityGroupMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroupMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityGroupMember = facilityGroupMemberService.update(facilityGroupMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroupMember.getId().toString()))
            .body(facilityGroupMember);
    }

    /**
     * {@code PATCH  /facility-group-members/:id} : Partial updates given fields of an existing facilityGroupMember, field will ignore if it is null
     *
     * @param id the id of the facilityGroupMember to save.
     * @param facilityGroupMember the facilityGroupMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityGroupMember,
     * or with status {@code 400 (Bad Request)} if the facilityGroupMember is not valid,
     * or with status {@code 404 (Not Found)} if the facilityGroupMember is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityGroupMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityGroupMember> partialUpdateFacilityGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityGroupMember facilityGroupMember
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update FacilityGroupMember : {}, {}", id, facilityGroupMember);
        if (facilityGroupMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityGroupMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityGroupMember> result = facilityGroupMemberService.partialUpdate(facilityGroupMember);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityGroupMember.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-group-members} : get all the Facility Group Members.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Facility Group Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityGroupMember>> getAllFacilityGroupMembers(
        FacilityGroupMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FacilityGroupMembers by criteria: {}", criteria);

        Page<FacilityGroupMember> page = facilityGroupMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-group-members/count} : count all the facilityGroupMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityGroupMembers(FacilityGroupMemberCriteria criteria) {
        LOG.debug("REST request to count FacilityGroupMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityGroupMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-group-members/:id} : get the "id" facilityGroupMember.
     *
     * @param id the id of the facilityGroupMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityGroupMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityGroupMember> getFacilityGroupMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityGroupMember : {}", id);
        Optional<FacilityGroupMember> facilityGroupMember = facilityGroupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityGroupMember);
    }

    /**
     * {@code DELETE  /facility-group-members/:id} : delete the "id" facilityGroupMember.
     *
     * @param id the id of the facilityGroupMember to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityGroupMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityGroupMember : {}", id);
        facilityGroupMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
