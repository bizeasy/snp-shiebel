package com.snp.web.rest;

import com.snp.domain.PartyRelationshipType;
import com.snp.repository.PartyRelationshipTypeRepository;
import com.snp.service.PartyRelationshipTypeQueryService;
import com.snp.service.PartyRelationshipTypeService;
import com.snp.service.criteria.PartyRelationshipTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyRelationshipType}.
 */
@RestController
@RequestMapping("/api/party-relationship-types")
public class PartyRelationshipTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipTypeResource.class);

    private static final String ENTITY_NAME = "partyRelationshipType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyRelationshipTypeService partyRelationshipTypeService;

    private final PartyRelationshipTypeRepository partyRelationshipTypeRepository;

    private final PartyRelationshipTypeQueryService partyRelationshipTypeQueryService;

    public PartyRelationshipTypeResource(
        PartyRelationshipTypeService partyRelationshipTypeService,
        PartyRelationshipTypeRepository partyRelationshipTypeRepository,
        PartyRelationshipTypeQueryService partyRelationshipTypeQueryService
    ) {
        this.partyRelationshipTypeService = partyRelationshipTypeService;
        this.partyRelationshipTypeRepository = partyRelationshipTypeRepository;
        this.partyRelationshipTypeQueryService = partyRelationshipTypeQueryService;
    }

    /**
     * {@code POST  /party-relationship-types} : Create a new partyRelationshipType.
     *
     * @param partyRelationshipType the partyRelationshipType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyRelationshipType, or with status {@code 400 (Bad Request)} if the partyRelationshipType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyRelationshipType> createPartyRelationshipType(
        @Valid @RequestBody PartyRelationshipType partyRelationshipType
    ) throws URISyntaxException {
        LOG.debug("REST request to save PartyRelationshipType : {}", partyRelationshipType);
        if (partyRelationshipType.getId() != null) {
            throw new BadRequestAlertException("A new partyRelationshipType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyRelationshipType = partyRelationshipTypeService.save(partyRelationshipType);
        return ResponseEntity.created(new URI("/api/party-relationship-types/" + partyRelationshipType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyRelationshipType.getId().toString()))
            .body(partyRelationshipType);
    }

    /**
     * {@code PUT  /party-relationship-types/:id} : Updates an existing partyRelationshipType.
     *
     * @param id the id of the partyRelationshipType to save.
     * @param partyRelationshipType the partyRelationshipType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationshipType,
     * or with status {@code 400 (Bad Request)} if the partyRelationshipType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationshipType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyRelationshipType> updatePartyRelationshipType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyRelationshipType partyRelationshipType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyRelationshipType : {}, {}", id, partyRelationshipType);
        if (partyRelationshipType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationshipType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationshipTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyRelationshipType = partyRelationshipTypeService.update(partyRelationshipType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationshipType.getId().toString()))
            .body(partyRelationshipType);
    }

    /**
     * {@code PATCH  /party-relationship-types/:id} : Partial updates given fields of an existing partyRelationshipType, field will ignore if it is null
     *
     * @param id the id of the partyRelationshipType to save.
     * @param partyRelationshipType the partyRelationshipType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationshipType,
     * or with status {@code 400 (Bad Request)} if the partyRelationshipType is not valid,
     * or with status {@code 404 (Not Found)} if the partyRelationshipType is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationshipType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyRelationshipType> partialUpdatePartyRelationshipType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyRelationshipType partyRelationshipType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyRelationshipType : {}, {}", id, partyRelationshipType);
        if (partyRelationshipType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationshipType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationshipTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyRelationshipType> result = partyRelationshipTypeService.partialUpdate(partyRelationshipType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationshipType.getId().toString())
        );
    }

    /**
     * {@code GET  /party-relationship-types} : get all the Party Relationship Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Relationship Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyRelationshipType>> getAllPartyRelationshipTypes(PartyRelationshipTypeCriteria criteria) {
        LOG.debug("REST request to get PartyRelationshipTypes by criteria: {}", criteria);

        List<PartyRelationshipType> entityList = partyRelationshipTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /party-relationship-types/count} : count all the partyRelationshipTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyRelationshipTypes(PartyRelationshipTypeCriteria criteria) {
        LOG.debug("REST request to count PartyRelationshipTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyRelationshipTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-relationship-types/:id} : get the "id" partyRelationshipType.
     *
     * @param id the id of the partyRelationshipType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyRelationshipType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyRelationshipType> getPartyRelationshipType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyRelationshipType : {}", id);
        Optional<PartyRelationshipType> partyRelationshipType = partyRelationshipTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyRelationshipType);
    }

    /**
     * {@code DELETE  /party-relationship-types/:id} : delete the "id" partyRelationshipType.
     *
     * @param id the id of the partyRelationshipType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyRelationshipType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyRelationshipType : {}", id);
        partyRelationshipTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
