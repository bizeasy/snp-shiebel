package com.snp.web.rest;

import com.snp.domain.PartyIdentificationType;
import com.snp.repository.PartyIdentificationTypeRepository;
import com.snp.service.PartyIdentificationTypeQueryService;
import com.snp.service.PartyIdentificationTypeService;
import com.snp.service.criteria.PartyIdentificationTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyIdentificationType}.
 */
@RestController
@RequestMapping("/api/party-identification-types")
public class PartyIdentificationTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationTypeResource.class);

    private static final String ENTITY_NAME = "partyIdentificationType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyIdentificationTypeService partyIdentificationTypeService;

    private final PartyIdentificationTypeRepository partyIdentificationTypeRepository;

    private final PartyIdentificationTypeQueryService partyIdentificationTypeQueryService;

    public PartyIdentificationTypeResource(
        PartyIdentificationTypeService partyIdentificationTypeService,
        PartyIdentificationTypeRepository partyIdentificationTypeRepository,
        PartyIdentificationTypeQueryService partyIdentificationTypeQueryService
    ) {
        this.partyIdentificationTypeService = partyIdentificationTypeService;
        this.partyIdentificationTypeRepository = partyIdentificationTypeRepository;
        this.partyIdentificationTypeQueryService = partyIdentificationTypeQueryService;
    }

    /**
     * {@code POST  /party-identification-types} : Create a new partyIdentificationType.
     *
     * @param partyIdentificationType the partyIdentificationType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyIdentificationType, or with status {@code 400 (Bad Request)} if the partyIdentificationType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyIdentificationType> createPartyIdentificationType(
        @Valid @RequestBody PartyIdentificationType partyIdentificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to save PartyIdentificationType : {}", partyIdentificationType);
        if (partyIdentificationType.getId() != null) {
            throw new BadRequestAlertException("A new partyIdentificationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyIdentificationType = partyIdentificationTypeService.save(partyIdentificationType);
        return ResponseEntity.created(new URI("/api/party-identification-types/" + partyIdentificationType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyIdentificationType.getId().toString()))
            .body(partyIdentificationType);
    }

    /**
     * {@code PUT  /party-identification-types/:id} : Updates an existing partyIdentificationType.
     *
     * @param id the id of the partyIdentificationType to save.
     * @param partyIdentificationType the partyIdentificationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyIdentificationType,
     * or with status {@code 400 (Bad Request)} if the partyIdentificationType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyIdentificationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyIdentificationType> updatePartyIdentificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyIdentificationType partyIdentificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyIdentificationType : {}, {}", id, partyIdentificationType);
        if (partyIdentificationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyIdentificationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdentificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyIdentificationType = partyIdentificationTypeService.update(partyIdentificationType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyIdentificationType.getId().toString()))
            .body(partyIdentificationType);
    }

    /**
     * {@code PATCH  /party-identification-types/:id} : Partial updates given fields of an existing partyIdentificationType, field will ignore if it is null
     *
     * @param id the id of the partyIdentificationType to save.
     * @param partyIdentificationType the partyIdentificationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyIdentificationType,
     * or with status {@code 400 (Bad Request)} if the partyIdentificationType is not valid,
     * or with status {@code 404 (Not Found)} if the partyIdentificationType is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyIdentificationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyIdentificationType> partialUpdatePartyIdentificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyIdentificationType partyIdentificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyIdentificationType : {}, {}", id, partyIdentificationType);
        if (partyIdentificationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyIdentificationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdentificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyIdentificationType> result = partyIdentificationTypeService.partialUpdate(partyIdentificationType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyIdentificationType.getId().toString())
        );
    }

    /**
     * {@code GET  /party-identification-types} : get all the Party Identification Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Identification Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyIdentificationType>> getAllPartyIdentificationTypes(PartyIdentificationTypeCriteria criteria) {
        LOG.debug("REST request to get PartyIdentificationTypes by criteria: {}", criteria);

        List<PartyIdentificationType> entityList = partyIdentificationTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /party-identification-types/count} : count all the partyIdentificationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyIdentificationTypes(PartyIdentificationTypeCriteria criteria) {
        LOG.debug("REST request to count PartyIdentificationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyIdentificationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-identification-types/:id} : get the "id" partyIdentificationType.
     *
     * @param id the id of the partyIdentificationType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyIdentificationType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyIdentificationType> getPartyIdentificationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyIdentificationType : {}", id);
        Optional<PartyIdentificationType> partyIdentificationType = partyIdentificationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyIdentificationType);
    }

    /**
     * {@code DELETE  /party-identification-types/:id} : delete the "id" partyIdentificationType.
     *
     * @param id the id of the partyIdentificationType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyIdentificationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyIdentificationType : {}", id);
        partyIdentificationTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
