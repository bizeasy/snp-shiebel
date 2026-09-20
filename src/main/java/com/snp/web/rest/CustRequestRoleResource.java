package com.snp.web.rest;

import com.snp.domain.CustRequestRole;
import com.snp.repository.CustRequestRoleRepository;
import com.snp.service.CustRequestRoleQueryService;
import com.snp.service.CustRequestRoleService;
import com.snp.service.criteria.CustRequestRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.CustRequestRole}.
 */
@RestController
@RequestMapping("/api/cust-request-roles")
public class CustRequestRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestRoleResource.class);

    private static final String ENTITY_NAME = "custRequestRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final CustRequestRoleService custRequestRoleService;

    private final CustRequestRoleRepository custRequestRoleRepository;

    private final CustRequestRoleQueryService custRequestRoleQueryService;

    public CustRequestRoleResource(
        CustRequestRoleService custRequestRoleService,
        CustRequestRoleRepository custRequestRoleRepository,
        CustRequestRoleQueryService custRequestRoleQueryService
    ) {
        this.custRequestRoleService = custRequestRoleService;
        this.custRequestRoleRepository = custRequestRoleRepository;
        this.custRequestRoleQueryService = custRequestRoleQueryService;
    }

    /**
     * {@code POST  /cust-request-roles} : Create a new custRequestRole.
     *
     * @param custRequestRole the custRequestRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new custRequestRole, or with status {@code 400 (Bad Request)} if the custRequestRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustRequestRole> createCustRequestRole(@Valid @RequestBody CustRequestRole custRequestRole)
        throws URISyntaxException {
        LOG.debug("REST request to save CustRequestRole : {}", custRequestRole);
        if (custRequestRole.getId() != null) {
            throw new BadRequestAlertException("A new custRequestRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        custRequestRole = custRequestRoleService.save(custRequestRole);
        return ResponseEntity.created(new URI("/api/cust-request-roles/" + custRequestRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, custRequestRole.getId().toString()))
            .body(custRequestRole);
    }

    /**
     * {@code PUT  /cust-request-roles/:id} : Updates an existing custRequestRole.
     *
     * @param id the id of the custRequestRole to save.
     * @param custRequestRole the custRequestRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestRole,
     * or with status {@code 400 (Bad Request)} if the custRequestRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the custRequestRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustRequestRole> updateCustRequestRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustRequestRole custRequestRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustRequestRole : {}, {}", id, custRequestRole);
        if (custRequestRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        custRequestRole = custRequestRoleService.update(custRequestRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestRole.getId().toString()))
            .body(custRequestRole);
    }

    /**
     * {@code PATCH  /cust-request-roles/:id} : Partial updates given fields of an existing custRequestRole, field will ignore if it is null
     *
     * @param id the id of the custRequestRole to save.
     * @param custRequestRole the custRequestRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated custRequestRole,
     * or with status {@code 400 (Bad Request)} if the custRequestRole is not valid,
     * or with status {@code 404 (Not Found)} if the custRequestRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the custRequestRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustRequestRole> partialUpdateCustRequestRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustRequestRole custRequestRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CustRequestRole : {}, {}", id, custRequestRole);
        if (custRequestRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, custRequestRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!custRequestRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustRequestRole> result = custRequestRoleService.partialUpdate(custRequestRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, custRequestRole.getId().toString())
        );
    }

    /**
     * {@code GET  /cust-request-roles} : get all the Cust Request Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cust Request Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustRequestRole>> getAllCustRequestRoles(
        CustRequestRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustRequestRoles by criteria: {}", criteria);

        Page<CustRequestRole> page = custRequestRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cust-request-roles/count} : count all the custRequestRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustRequestRoles(CustRequestRoleCriteria criteria) {
        LOG.debug("REST request to count CustRequestRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(custRequestRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cust-request-roles/:id} : get the "id" custRequestRole.
     *
     * @param id the id of the custRequestRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the custRequestRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustRequestRole> getCustRequestRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustRequestRole : {}", id);
        Optional<CustRequestRole> custRequestRole = custRequestRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(custRequestRole);
    }

    /**
     * {@code DELETE  /cust-request-roles/:id} : delete the "id" custRequestRole.
     *
     * @param id the id of the custRequestRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustRequestRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustRequestRole : {}", id);
        custRequestRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
