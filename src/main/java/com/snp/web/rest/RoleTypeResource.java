package com.snp.web.rest;

import com.snp.domain.RoleType;
import com.snp.repository.RoleTypeRepository;
import com.snp.service.RoleTypeQueryService;
import com.snp.service.RoleTypeService;
import com.snp.service.criteria.RoleTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.RoleType}.
 */
@RestController
@RequestMapping("/api/role-types")
public class RoleTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeResource.class);

    private static final String ENTITY_NAME = "roleType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final RoleTypeService roleTypeService;

    private final RoleTypeRepository roleTypeRepository;

    private final RoleTypeQueryService roleTypeQueryService;

    public RoleTypeResource(
        RoleTypeService roleTypeService,
        RoleTypeRepository roleTypeRepository,
        RoleTypeQueryService roleTypeQueryService
    ) {
        this.roleTypeService = roleTypeService;
        this.roleTypeRepository = roleTypeRepository;
        this.roleTypeQueryService = roleTypeQueryService;
    }

    /**
     * {@code POST  /role-types} : Create a new roleType.
     *
     * @param roleType the roleType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleType, or with status {@code 400 (Bad Request)} if the roleType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoleType> createRoleType(@Valid @RequestBody RoleType roleType) throws URISyntaxException {
        LOG.debug("REST request to save RoleType : {}", roleType);
        if (roleType.getId() != null) {
            throw new BadRequestAlertException("A new roleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roleType = roleTypeService.save(roleType);
        return ResponseEntity.created(new URI("/api/role-types/" + roleType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roleType.getId().toString()))
            .body(roleType);
    }

    /**
     * {@code PUT  /role-types/:id} : Updates an existing roleType.
     *
     * @param id the id of the roleType to save.
     * @param roleType the roleType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleType,
     * or with status {@code 400 (Bad Request)} if the roleType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleType> updateRoleType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoleType roleType
    ) throws URISyntaxException {
        LOG.debug("REST request to update RoleType : {}, {}", id, roleType);
        if (roleType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roleType = roleTypeService.update(roleType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleType.getId().toString()))
            .body(roleType);
    }

    /**
     * {@code PATCH  /role-types/:id} : Partial updates given fields of an existing roleType, field will ignore if it is null
     *
     * @param id the id of the roleType to save.
     * @param roleType the roleType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleType,
     * or with status {@code 400 (Bad Request)} if the roleType is not valid,
     * or with status {@code 404 (Not Found)} if the roleType is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleType> partialUpdateRoleType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoleType roleType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update RoleType : {}, {}", id, roleType);
        if (roleType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleType> result = roleTypeService.partialUpdate(roleType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleType.getId().toString())
        );
    }

    /**
     * {@code GET  /role-types} : get all the Role Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Role Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoleType>> getAllRoleTypes(RoleTypeCriteria criteria) {
        LOG.debug("REST request to get RoleTypes by criteria: {}", criteria);

        List<RoleType> entityList = roleTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /role-types/count} : count all the roleTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRoleTypes(RoleTypeCriteria criteria) {
        LOG.debug("REST request to count RoleTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(roleTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /role-types/:id} : get the "id" roleType.
     *
     * @param id the id of the roleType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleType> getRoleType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RoleType : {}", id);
        Optional<RoleType> roleType = roleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleType);
    }

    /**
     * {@code DELETE  /role-types/:id} : delete the "id" roleType.
     *
     * @param id the id of the roleType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RoleType : {}", id);
        roleTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
