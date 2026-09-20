package com.snp.web.rest;

import com.snp.domain.PartyRelationship;
import com.snp.repository.PartyRelationshipRepository;
import com.snp.service.PartyRelationshipQueryService;
import com.snp.service.PartyRelationshipService;
import com.snp.service.criteria.PartyRelationshipCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyRelationship}.
 */
@RestController
@RequestMapping("/api/party-relationships")
public class PartyRelationshipResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipResource.class);

    private static final String ENTITY_NAME = "partyRelationship";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyRelationshipService partyRelationshipService;

    private final PartyRelationshipRepository partyRelationshipRepository;

    private final PartyRelationshipQueryService partyRelationshipQueryService;

    public PartyRelationshipResource(
        PartyRelationshipService partyRelationshipService,
        PartyRelationshipRepository partyRelationshipRepository,
        PartyRelationshipQueryService partyRelationshipQueryService
    ) {
        this.partyRelationshipService = partyRelationshipService;
        this.partyRelationshipRepository = partyRelationshipRepository;
        this.partyRelationshipQueryService = partyRelationshipQueryService;
    }

    /**
     * {@code POST  /party-relationships} : Create a new partyRelationship.
     *
     * @param partyRelationship the partyRelationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyRelationship, or with status {@code 400 (Bad Request)} if the partyRelationship already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyRelationship> createPartyRelationship(@Valid @RequestBody PartyRelationship partyRelationship)
        throws URISyntaxException {
        LOG.debug("REST request to save PartyRelationship : {}", partyRelationship);
        if (partyRelationship.getId() != null) {
            throw new BadRequestAlertException("A new partyRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyRelationship = partyRelationshipService.save(partyRelationship);
        return ResponseEntity.created(new URI("/api/party-relationships/" + partyRelationship.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyRelationship.getId().toString()))
            .body(partyRelationship);
    }

    /**
     * {@code PUT  /party-relationships/:id} : Updates an existing partyRelationship.
     *
     * @param id the id of the partyRelationship to save.
     * @param partyRelationship the partyRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationship,
     * or with status {@code 400 (Bad Request)} if the partyRelationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyRelationship> updatePartyRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyRelationship partyRelationship
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyRelationship : {}, {}", id, partyRelationship);
        if (partyRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyRelationship = partyRelationshipService.update(partyRelationship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationship.getId().toString()))
            .body(partyRelationship);
    }

    /**
     * {@code PATCH  /party-relationships/:id} : Partial updates given fields of an existing partyRelationship, field will ignore if it is null
     *
     * @param id the id of the partyRelationship to save.
     * @param partyRelationship the partyRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationship,
     * or with status {@code 400 (Bad Request)} if the partyRelationship is not valid,
     * or with status {@code 404 (Not Found)} if the partyRelationship is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyRelationship> partialUpdatePartyRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyRelationship partyRelationship
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyRelationship : {}, {}", id, partyRelationship);
        if (partyRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyRelationship> result = partyRelationshipService.partialUpdate(partyRelationship);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationship.getId().toString())
        );
    }

    /**
     * {@code GET  /party-relationships} : get all the Party Relationships.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Relationships in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyRelationship>> getAllPartyRelationships(
        PartyRelationshipCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartyRelationships by criteria: {}", criteria);

        Page<PartyRelationship> page = partyRelationshipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-relationships/count} : count all the partyRelationships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyRelationships(PartyRelationshipCriteria criteria) {
        LOG.debug("REST request to count PartyRelationships by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyRelationshipQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-relationships/:id} : get the "id" partyRelationship.
     *
     * @param id the id of the partyRelationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyRelationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyRelationship> getPartyRelationship(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyRelationship : {}", id);
        Optional<PartyRelationship> partyRelationship = partyRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyRelationship);
    }

    /**
     * {@code DELETE  /party-relationships/:id} : delete the "id" partyRelationship.
     *
     * @param id the id of the partyRelationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyRelationship(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyRelationship : {}", id);
        partyRelationshipService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
