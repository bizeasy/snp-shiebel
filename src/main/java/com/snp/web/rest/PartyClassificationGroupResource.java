package com.snp.web.rest;

import com.snp.domain.PartyClassificationGroup;
import com.snp.repository.PartyClassificationGroupRepository;
import com.snp.service.PartyClassificationGroupQueryService;
import com.snp.service.PartyClassificationGroupService;
import com.snp.service.criteria.PartyClassificationGroupCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyClassificationGroup}.
 */
@RestController
@RequestMapping("/api/party-classification-groups")
public class PartyClassificationGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationGroupResource.class);

    private static final String ENTITY_NAME = "partyClassificationGroup";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyClassificationGroupService partyClassificationGroupService;

    private final PartyClassificationGroupRepository partyClassificationGroupRepository;

    private final PartyClassificationGroupQueryService partyClassificationGroupQueryService;

    public PartyClassificationGroupResource(
        PartyClassificationGroupService partyClassificationGroupService,
        PartyClassificationGroupRepository partyClassificationGroupRepository,
        PartyClassificationGroupQueryService partyClassificationGroupQueryService
    ) {
        this.partyClassificationGroupService = partyClassificationGroupService;
        this.partyClassificationGroupRepository = partyClassificationGroupRepository;
        this.partyClassificationGroupQueryService = partyClassificationGroupQueryService;
    }

    /**
     * {@code POST  /party-classification-groups} : Create a new partyClassificationGroup.
     *
     * @param partyClassificationGroup the partyClassificationGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyClassificationGroup, or with status {@code 400 (Bad Request)} if the partyClassificationGroup already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyClassificationGroup> createPartyClassificationGroup(
        @Valid @RequestBody PartyClassificationGroup partyClassificationGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to save PartyClassificationGroup : {}", partyClassificationGroup);
        if (partyClassificationGroup.getId() != null) {
            throw new BadRequestAlertException("A new partyClassificationGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyClassificationGroup = partyClassificationGroupService.save(partyClassificationGroup);
        return ResponseEntity.created(new URI("/api/party-classification-groups/" + partyClassificationGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyClassificationGroup.getId().toString()))
            .body(partyClassificationGroup);
    }

    /**
     * {@code PUT  /party-classification-groups/:id} : Updates an existing partyClassificationGroup.
     *
     * @param id the id of the partyClassificationGroup to save.
     * @param partyClassificationGroup the partyClassificationGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassificationGroup,
     * or with status {@code 400 (Bad Request)} if the partyClassificationGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyClassificationGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyClassificationGroup> updatePartyClassificationGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyClassificationGroup partyClassificationGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyClassificationGroup : {}, {}", id, partyClassificationGroup);
        if (partyClassificationGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassificationGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyClassificationGroup = partyClassificationGroupService.update(partyClassificationGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassificationGroup.getId().toString()))
            .body(partyClassificationGroup);
    }

    /**
     * {@code PATCH  /party-classification-groups/:id} : Partial updates given fields of an existing partyClassificationGroup, field will ignore if it is null
     *
     * @param id the id of the partyClassificationGroup to save.
     * @param partyClassificationGroup the partyClassificationGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyClassificationGroup,
     * or with status {@code 400 (Bad Request)} if the partyClassificationGroup is not valid,
     * or with status {@code 404 (Not Found)} if the partyClassificationGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyClassificationGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyClassificationGroup> partialUpdatePartyClassificationGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyClassificationGroup partyClassificationGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyClassificationGroup : {}, {}", id, partyClassificationGroup);
        if (partyClassificationGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyClassificationGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyClassificationGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyClassificationGroup> result = partyClassificationGroupService.partialUpdate(partyClassificationGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyClassificationGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /party-classification-groups} : get all the Party Classification Groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Classification Groups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyClassificationGroup>> getAllPartyClassificationGroups(PartyClassificationGroupCriteria criteria) {
        LOG.debug("REST request to get PartyClassificationGroups by criteria: {}", criteria);

        List<PartyClassificationGroup> entityList = partyClassificationGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /party-classification-groups/count} : count all the partyClassificationGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyClassificationGroups(PartyClassificationGroupCriteria criteria) {
        LOG.debug("REST request to count PartyClassificationGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyClassificationGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-classification-groups/:id} : get the "id" partyClassificationGroup.
     *
     * @param id the id of the partyClassificationGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyClassificationGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyClassificationGroup> getPartyClassificationGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyClassificationGroup : {}", id);
        Optional<PartyClassificationGroup> partyClassificationGroup = partyClassificationGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyClassificationGroup);
    }

    /**
     * {@code DELETE  /party-classification-groups/:id} : delete the "id" partyClassificationGroup.
     *
     * @param id the id of the partyClassificationGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyClassificationGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyClassificationGroup : {}", id);
        partyClassificationGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
