package com.snp.web.rest;

import com.snp.domain.PartyClassificationType;
import com.snp.repository.PartyClassificationTypeRepository;
import com.snp.service.PartyClassificationTypeQueryService;
import com.snp.service.PartyClassificationTypeService;
import com.snp.service.criteria.PartyClassificationTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyClassificationType}.
 */
@RestController
@RequestMapping("/api/party-classification-types")
public class PartyClassificationTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationTypeResource.class);

    private static final String ENTITY_NAME = "partyClassificationType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyClassificationTypeService partyClassificationTypeService;

    private final PartyClassificationTypeRepository partyClassificationTypeRepository;

    private final PartyClassificationTypeQueryService partyClassificationTypeQueryService;

    public PartyClassificationTypeResource(
        PartyClassificationTypeService partyClassificationTypeService,
        PartyClassificationTypeRepository partyClassificationTypeRepository,
        PartyClassificationTypeQueryService partyClassificationTypeQueryService
    ) {
        this.partyClassificationTypeService = partyClassificationTypeService;
        this.partyClassificationTypeRepository = partyClassificationTypeRepository;
        this.partyClassificationTypeQueryService = partyClassificationTypeQueryService;
    }

    /**
     * {@code POST  /party-classification-types} : Create a new partyClassificationType.
     *
     * @param partyClassificationType the partyClassificationType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyClassificationType, or with status {@code 400 (Bad Request)} if the partyClassificationType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyClassificationType> createPartyClassificationType(
        @Valid @RequestBody PartyClassificationType partyClassificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to save PartyClassificationType : {}", partyClassificationType);
        if (partyClassificationType.getId() != null) {
            throw new BadRequestAlertException("A new partyClassificationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyClassificationType = partyClassificationTypeService.save(partyClassificationType);
        return ResponseEntity.created(new URI("/api/party-classification-types/" + partyClassificationType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyClassificationType.getId().toString()))
            .body(partyClassificationType);
    }

    /**
     * {@code PUT  /party-classification-types/:id} : Updates an existing partyClassificationType.
     *
     * @param id the id of the partyClassificationType to save.
     * @param partyClassificationType the partyClassificationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassificationType,
     * or with status {@code 400 (Bad Request)} if the partyClassificationType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyClassificationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyClassificationType> updatePartyClassificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyClassificationType partyClassificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyClassificationType : {}, {}", id, partyClassificationType);
        if (partyClassificationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassificationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyClassificationType = partyClassificationTypeService.update(partyClassificationType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassificationType.getId().toString()))
            .body(partyClassificationType);
    }

    /**
     * {@code PATCH  /party-classification-types/:id} : Partial updates given fields of an existing partyClassificationType, field will ignore if it is null
     *
     * @param id the id of the partyClassificationType to save.
     * @param partyClassificationType the partyClassificationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassificationType,
     * or with status {@code 400 (Bad Request)} if the partyClassificationType is not valid,
     * or with status {@code 404 (Not Found)} if the partyClassificationType is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyClassificationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyClassificationType> partialUpdatePartyClassificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyClassificationType partyClassificationType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyClassificationType : {}, {}", id, partyClassificationType);
        if (partyClassificationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassificationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyClassificationType> result = partyClassificationTypeService.partialUpdate(partyClassificationType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassificationType.getId().toString())
        );
    }

    /**
     * {@code GET  /party-classification-types} : get all the Party Classification Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Classification Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyClassificationType>> getAllPartyClassificationTypes(PartyClassificationTypeCriteria criteria) {
        LOG.debug("REST request to get PartyClassificationTypes by criteria: {}", criteria);

        List<PartyClassificationType> entityList = partyClassificationTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /party-classification-types/count} : count all the partyClassificationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyClassificationTypes(PartyClassificationTypeCriteria criteria) {
        LOG.debug("REST request to count PartyClassificationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyClassificationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-classification-types/:id} : get the "id" partyClassificationType.
     *
     * @param id the id of the partyClassificationType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyClassificationType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyClassificationType> getPartyClassificationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyClassificationType : {}", id);
        Optional<PartyClassificationType> partyClassificationType = partyClassificationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyClassificationType);
    }

    /**
     * {@code DELETE  /party-classification-types/:id} : delete the "id" partyClassificationType.
     *
     * @param id the id of the partyClassificationType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyClassificationType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyClassificationType : {}", id);
        partyClassificationTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
