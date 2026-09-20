package com.snp.web.rest;

import com.snp.domain.PartyContactMech;
import com.snp.repository.PartyContactMechRepository;
import com.snp.service.PartyContactMechQueryService;
import com.snp.service.PartyContactMechService;
import com.snp.service.criteria.PartyContactMechCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyContactMech}.
 */
@RestController
@RequestMapping("/api/party-contact-meches")
public class PartyContactMechResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyContactMechResource.class);

    private static final String ENTITY_NAME = "partyContactMech";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyContactMechService partyContactMechService;

    private final PartyContactMechRepository partyContactMechRepository;

    private final PartyContactMechQueryService partyContactMechQueryService;

    public PartyContactMechResource(
        PartyContactMechService partyContactMechService,
        PartyContactMechRepository partyContactMechRepository,
        PartyContactMechQueryService partyContactMechQueryService
    ) {
        this.partyContactMechService = partyContactMechService;
        this.partyContactMechRepository = partyContactMechRepository;
        this.partyContactMechQueryService = partyContactMechQueryService;
    }

    /**
     * {@code POST  /party-contact-meches} : Create a new partyContactMech.
     *
     * @param partyContactMech the partyContactMech to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyContactMech, or with status {@code 400 (Bad Request)} if the partyContactMech already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyContactMech> createPartyContactMech(@Valid @RequestBody PartyContactMech partyContactMech)
        throws URISyntaxException {
        LOG.debug("REST request to save PartyContactMech : {}", partyContactMech);
        if (partyContactMech.getId() != null) {
            throw new BadRequestAlertException("A new partyContactMech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyContactMech = partyContactMechService.save(partyContactMech);
        return ResponseEntity.created(new URI("/api/party-contact-meches/" + partyContactMech.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyContactMech.getId().toString()))
            .body(partyContactMech);
    }

    /**
     * {@code PUT  /party-contact-meches/:id} : Updates an existing partyContactMech.
     *
     * @param id the id of the partyContactMech to save.
     * @param partyContactMech the partyContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyContactMech,
     * or with status {@code 400 (Bad Request)} if the partyContactMech is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyContactMech> updatePartyContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyContactMech partyContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyContactMech : {}, {}", id, partyContactMech);
        if (partyContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyContactMech = partyContactMechService.update(partyContactMech);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyContactMech.getId().toString()))
            .body(partyContactMech);
    }

    /**
     * {@code PATCH  /party-contact-meches/:id} : Partial updates given fields of an existing partyContactMech, field will ignore if it is null
     *
     * @param id the id of the partyContactMech to save.
     * @param partyContactMech the partyContactMech to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyContactMech,
     * or with status {@code 400 (Bad Request)} if the partyContactMech is not valid,
     * or with status {@code 404 (Not Found)} if the partyContactMech is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyContactMech couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyContactMech> partialUpdatePartyContactMech(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyContactMech partyContactMech
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyContactMech : {}, {}", id, partyContactMech);
        if (partyContactMech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyContactMech.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyContactMechRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyContactMech> result = partyContactMechService.partialUpdate(partyContactMech);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyContactMech.getId().toString())
        );
    }

    /**
     * {@code GET  /party-contact-meches} : get all the Party Contact Meches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Contact Meches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyContactMech>> getAllPartyContactMeches(
        PartyContactMechCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartyContactMeches by criteria: {}", criteria);

        Page<PartyContactMech> page = partyContactMechQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-contact-meches/count} : count all the partyContactMeches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyContactMeches(PartyContactMechCriteria criteria) {
        LOG.debug("REST request to count PartyContactMeches by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyContactMechQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-contact-meches/:id} : get the "id" partyContactMech.
     *
     * @param id the id of the partyContactMech to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyContactMech, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyContactMech> getPartyContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyContactMech : {}", id);
        Optional<PartyContactMech> partyContactMech = partyContactMechService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyContactMech);
    }

    /**
     * {@code DELETE  /party-contact-meches/:id} : delete the "id" partyContactMech.
     *
     * @param id the id of the partyContactMech to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyContactMech(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyContactMech : {}", id);
        partyContactMechService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
