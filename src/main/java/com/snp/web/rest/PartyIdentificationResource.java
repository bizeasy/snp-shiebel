package com.snp.web.rest;

import com.snp.domain.PartyIdentification;
import com.snp.repository.PartyIdentificationRepository;
import com.snp.service.PartyIdentificationQueryService;
import com.snp.service.PartyIdentificationService;
import com.snp.service.criteria.PartyIdentificationCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyIdentification}.
 */
@RestController
@RequestMapping("/api/party-identifications")
public class PartyIdentificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationResource.class);

    private static final String ENTITY_NAME = "partyIdentification";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyIdentificationService partyIdentificationService;

    private final PartyIdentificationRepository partyIdentificationRepository;

    private final PartyIdentificationQueryService partyIdentificationQueryService;

    public PartyIdentificationResource(
        PartyIdentificationService partyIdentificationService,
        PartyIdentificationRepository partyIdentificationRepository,
        PartyIdentificationQueryService partyIdentificationQueryService
    ) {
        this.partyIdentificationService = partyIdentificationService;
        this.partyIdentificationRepository = partyIdentificationRepository;
        this.partyIdentificationQueryService = partyIdentificationQueryService;
    }

    /**
     * {@code POST  /party-identifications} : Create a new partyIdentification.
     *
     * @param partyIdentification the partyIdentification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyIdentification, or with status {@code 400 (Bad Request)} if the partyIdentification already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyIdentification> createPartyIdentification(@Valid @RequestBody PartyIdentification partyIdentification)
        throws URISyntaxException {
        LOG.debug("REST request to save PartyIdentification : {}", partyIdentification);
        if (partyIdentification.getId() != null) {
            throw new BadRequestAlertException("A new partyIdentification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyIdentification = partyIdentificationService.save(partyIdentification);
        return ResponseEntity.created(new URI("/api/party-identifications/" + partyIdentification.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyIdentification.getId().toString()))
            .body(partyIdentification);
    }

    /**
     * {@code PUT  /party-identifications/:id} : Updates an existing partyIdentification.
     *
     * @param id the id of the partyIdentification to save.
     * @param partyIdentification the partyIdentification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyIdentification,
     * or with status {@code 400 (Bad Request)} if the partyIdentification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyIdentification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyIdentification> updatePartyIdentification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyIdentification partyIdentification
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyIdentification : {}, {}", id, partyIdentification);
        if (partyIdentification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyIdentification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyIdentification = partyIdentificationService.update(partyIdentification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyIdentification.getId().toString()))
            .body(partyIdentification);
    }

    /**
     * {@code PATCH  /party-identifications/:id} : Partial updates given fields of an existing partyIdentification, field will ignore if it is null
     *
     * @param id the id of the partyIdentification to save.
     * @param partyIdentification the partyIdentification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyIdentification,
     * or with status {@code 400 (Bad Request)} if the partyIdentification is not valid,
     * or with status {@code 404 (Not Found)} if the partyIdentification is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyIdentification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyIdentification> partialUpdatePartyIdentification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyIdentification partyIdentification
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyIdentification : {}, {}", id, partyIdentification);
        if (partyIdentification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyIdentification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyIdentification> result = partyIdentificationService.partialUpdate(partyIdentification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyIdentification.getId().toString())
        );
    }

    /**
     * {@code GET  /party-identifications} : get all the Party Identifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Identifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyIdentification>> getAllPartyIdentifications(
        PartyIdentificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartyIdentifications by criteria: {}", criteria);

        Page<PartyIdentification> page = partyIdentificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-identifications/count} : count all the partyIdentifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyIdentifications(PartyIdentificationCriteria criteria) {
        LOG.debug("REST request to count PartyIdentifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyIdentificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-identifications/:id} : get the "id" partyIdentification.
     *
     * @param id the id of the partyIdentification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyIdentification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyIdentification> getPartyIdentification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyIdentification : {}", id);
        Optional<PartyIdentification> partyIdentification = partyIdentificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyIdentification);
    }

    /**
     * {@code DELETE  /party-identifications/:id} : delete the "id" partyIdentification.
     *
     * @param id the id of the partyIdentification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyIdentification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyIdentification : {}", id);
        partyIdentificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
