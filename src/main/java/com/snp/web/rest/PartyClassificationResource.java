package com.snp.web.rest;

import com.snp.domain.PartyClassification;
import com.snp.repository.PartyClassificationRepository;
import com.snp.service.PartyClassificationQueryService;
import com.snp.service.PartyClassificationService;
import com.snp.service.criteria.PartyClassificationCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyClassification}.
 */
@RestController
@RequestMapping("/api/party-classifications")
public class PartyClassificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationResource.class);

    private static final String ENTITY_NAME = "partyClassification";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyClassificationService partyClassificationService;

    private final PartyClassificationRepository partyClassificationRepository;

    private final PartyClassificationQueryService partyClassificationQueryService;

    public PartyClassificationResource(
        PartyClassificationService partyClassificationService,
        PartyClassificationRepository partyClassificationRepository,
        PartyClassificationQueryService partyClassificationQueryService
    ) {
        this.partyClassificationService = partyClassificationService;
        this.partyClassificationRepository = partyClassificationRepository;
        this.partyClassificationQueryService = partyClassificationQueryService;
    }

    /**
     * {@code POST  /party-classifications} : Create a new partyClassification.
     *
     * @param partyClassification the partyClassification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyClassification, or with status {@code 400 (Bad Request)} if the partyClassification already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyClassification> createPartyClassification(@Valid @RequestBody PartyClassification partyClassification)
        throws URISyntaxException {
        LOG.debug("REST request to save PartyClassification : {}", partyClassification);
        if (partyClassification.getId() != null) {
            throw new BadRequestAlertException("A new partyClassification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyClassification = partyClassificationService.save(partyClassification);
        return ResponseEntity.created(new URI("/api/party-classifications/" + partyClassification.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyClassification.getId().toString()))
            .body(partyClassification);
    }

    /**
     * {@code PUT  /party-classifications/:id} : Updates an existing partyClassification.
     *
     * @param id the id of the partyClassification to save.
     * @param partyClassification the partyClassification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassification,
     * or with status {@code 400 (Bad Request)} if the partyClassification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyClassification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyClassification> updatePartyClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyClassification partyClassification
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyClassification : {}, {}", id, partyClassification);
        if (partyClassification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyClassification = partyClassificationService.update(partyClassification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassification.getId().toString()))
            .body(partyClassification);
    }

    /**
     * {@code PATCH  /party-classifications/:id} : Partial updates given fields of an existing partyClassification, field will ignore if it is null
     *
     * @param id the id of the partyClassification to save.
     * @param partyClassification the partyClassification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassification,
     * or with status {@code 400 (Bad Request)} if the partyClassification is not valid,
     * or with status {@code 404 (Not Found)} if the partyClassification is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyClassification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyClassification> partialUpdatePartyClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyClassification partyClassification
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyClassification : {}, {}", id, partyClassification);
        if (partyClassification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyClassification> result = partyClassificationService.partialUpdate(partyClassification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassification.getId().toString())
        );
    }

    /**
     * {@code GET  /party-classifications} : get all the Party Classifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Classifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyClassification>> getAllPartyClassifications(
        PartyClassificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartyClassifications by criteria: {}", criteria);

        Page<PartyClassification> page = partyClassificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-classifications/count} : count all the partyClassifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyClassifications(PartyClassificationCriteria criteria) {
        LOG.debug("REST request to count PartyClassifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyClassificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-classifications/:id} : get the "id" partyClassification.
     *
     * @param id the id of the partyClassification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyClassification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyClassification> getPartyClassification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyClassification : {}", id);
        Optional<PartyClassification> partyClassification = partyClassificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyClassification);
    }

    /**
     * {@code DELETE  /party-classifications/:id} : delete the "id" partyClassification.
     *
     * @param id the id of the partyClassification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyClassification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyClassification : {}", id);
        partyClassificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
