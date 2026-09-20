package com.snp.web.rest;

import com.snp.domain.PicklistRole;
import com.snp.repository.PicklistRoleRepository;
import com.snp.service.PicklistRoleQueryService;
import com.snp.service.PicklistRoleService;
import com.snp.service.criteria.PicklistRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.PicklistRole}.
 */
@RestController
@RequestMapping("/api/picklist-roles")
public class PicklistRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(PicklistRoleResource.class);

    private static final String ENTITY_NAME = "picklistRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final PicklistRoleService picklistRoleService;

    private final PicklistRoleRepository picklistRoleRepository;

    private final PicklistRoleQueryService picklistRoleQueryService;

    public PicklistRoleResource(
        PicklistRoleService picklistRoleService,
        PicklistRoleRepository picklistRoleRepository,
        PicklistRoleQueryService picklistRoleQueryService
    ) {
        this.picklistRoleService = picklistRoleService;
        this.picklistRoleRepository = picklistRoleRepository;
        this.picklistRoleQueryService = picklistRoleQueryService;
    }

    /**
     * {@code POST  /picklist-roles} : Create a new picklistRole.
     *
     * @param picklistRole the picklistRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new picklistRole, or with status {@code 400 (Bad Request)} if the picklistRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PicklistRole> createPicklistRole(@Valid @RequestBody PicklistRole picklistRole) throws URISyntaxException {
        LOG.debug("REST request to save PicklistRole : {}", picklistRole);
        if (picklistRole.getId() != null) {
            throw new BadRequestAlertException("A new picklistRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        picklistRole = picklistRoleService.save(picklistRole);
        return ResponseEntity.created(new URI("/api/picklist-roles/" + picklistRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, picklistRole.getId().toString()))
            .body(picklistRole);
    }

    /**
     * {@code PUT  /picklist-roles/:id} : Updates an existing picklistRole.
     *
     * @param id the id of the picklistRole to save.
     * @param picklistRole the picklistRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated picklistRole,
     * or with status {@code 400 (Bad Request)} if the picklistRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the picklistRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PicklistRole> updatePicklistRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PicklistRole picklistRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update PicklistRole : {}, {}", id, picklistRole);
        if (picklistRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, picklistRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!picklistRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        picklistRole = picklistRoleService.update(picklistRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, picklistRole.getId().toString()))
            .body(picklistRole);
    }

    /**
     * {@code PATCH  /picklist-roles/:id} : Partial updates given fields of an existing picklistRole, field will ignore if it is null
     *
     * @param id the id of the picklistRole to save.
     * @param picklistRole the picklistRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated picklistRole,
     * or with status {@code 400 (Bad Request)} if the picklistRole is not valid,
     * or with status {@code 404 (Not Found)} if the picklistRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the picklistRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PicklistRole> partialUpdatePicklistRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PicklistRole picklistRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PicklistRole : {}, {}", id, picklistRole);
        if (picklistRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, picklistRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!picklistRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PicklistRole> result = picklistRoleService.partialUpdate(picklistRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, picklistRole.getId().toString())
        );
    }

    /**
     * {@code GET  /picklist-roles} : get all the Picklist Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Picklist Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PicklistRole>> getAllPicklistRoles(
        PicklistRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PicklistRoles by criteria: {}", criteria);

        Page<PicklistRole> page = picklistRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /picklist-roles/count} : count all the picklistRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPicklistRoles(PicklistRoleCriteria criteria) {
        LOG.debug("REST request to count PicklistRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(picklistRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /picklist-roles/:id} : get the "id" picklistRole.
     *
     * @param id the id of the picklistRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the picklistRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PicklistRole> getPicklistRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PicklistRole : {}", id);
        Optional<PicklistRole> picklistRole = picklistRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(picklistRole);
    }

    /**
     * {@code DELETE  /picklist-roles/:id} : delete the "id" picklistRole.
     *
     * @param id the id of the picklistRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePicklistRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PicklistRole : {}", id);
        picklistRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
