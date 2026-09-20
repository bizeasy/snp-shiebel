package com.snp.web.rest;

import com.snp.domain.PartyRole;
import com.snp.repository.PartyRoleRepository;
import com.snp.service.PartyRoleQueryService;
import com.snp.service.PartyRoleService;
import com.snp.service.criteria.PartyRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.PartyRole}.
 */
@RestController
@RequestMapping("/api/party-roles")
public class PartyRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRoleResource.class);

    private static final String ENTITY_NAME = "partyRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PartyRoleService partyRoleService;

    private final PartyRoleRepository partyRoleRepository;

    private final PartyRoleQueryService partyRoleQueryService;

    public PartyRoleResource(
        PartyRoleService partyRoleService,
        PartyRoleRepository partyRoleRepository,
        PartyRoleQueryService partyRoleQueryService
    ) {
        this.partyRoleService = partyRoleService;
        this.partyRoleRepository = partyRoleRepository;
        this.partyRoleQueryService = partyRoleQueryService;
    }

    /**
     * {@code POST  /party-roles} : Create a new partyRole.
     *
     * @param partyRole the partyRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyRole, or with status {@code 400 (Bad Request)} if the partyRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PartyRole> createPartyRole(@Valid @RequestBody PartyRole partyRole) throws URISyntaxException {
        LOG.debug("REST request to save PartyRole : {}", partyRole);
        if (partyRole.getId() != null) {
            throw new BadRequestAlertException("A new partyRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        partyRole = partyRoleService.save(partyRole);
        return ResponseEntity.created(new URI("/api/party-roles/" + partyRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, partyRole.getId().toString()))
            .body(partyRole);
    }

    /**
     * {@code PUT  /party-roles/:id} : Updates an existing partyRole.
     *
     * @param id the id of the partyRole to save.
     * @param partyRole the partyRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRole,
     * or with status {@code 400 (Bad Request)} if the partyRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartyRole> updatePartyRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyRole partyRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update PartyRole : {}, {}", id, partyRole);
        if (partyRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        partyRole = partyRoleService.update(partyRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRole.getId().toString()))
            .body(partyRole);
    }

    /**
     * {@code PATCH  /party-roles/:id} : Partial updates given fields of an existing partyRole, field will ignore if it is null
     *
     * @param id the id of the partyRole to save.
     * @param partyRole the partyRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRole,
     * or with status {@code 400 (Bad Request)} if the partyRole is not valid,
     * or with status {@code 404 (Not Found)} if the partyRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyRole> partialUpdatePartyRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyRole partyRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PartyRole : {}, {}", id, partyRole);
        if (partyRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyRole> result = partyRoleService.partialUpdate(partyRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRole.getId().toString())
        );
    }

    /**
     * {@code GET  /party-roles} : get all the Party Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Party Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PartyRole>> getAllPartyRoles(
        PartyRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PartyRoles by criteria: {}", criteria);

        Page<PartyRole> page = partyRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-roles/count} : count all the partyRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPartyRoles(PartyRoleCriteria criteria) {
        LOG.debug("REST request to count PartyRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-roles/:id} : get the "id" partyRole.
     *
     * @param id the id of the partyRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyRole> getPartyRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PartyRole : {}", id);
        Optional<PartyRole> partyRole = partyRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyRole);
    }

    /**
     * {@code DELETE  /party-roles/:id} : delete the "id" partyRole.
     *
     * @param id the id of the partyRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PartyRole : {}", id);
        partyRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
