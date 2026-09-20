package com.snp.web.rest;

import com.snp.domain.InvoiceRole;
import com.snp.repository.InvoiceRoleRepository;
import com.snp.service.InvoiceRoleQueryService;
import com.snp.service.InvoiceRoleService;
import com.snp.service.criteria.InvoiceRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.InvoiceRole}.
 */
@RestController
@RequestMapping("/api/invoice-roles")
public class InvoiceRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRoleResource.class);

    private static final String ENTITY_NAME = "invoiceRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InvoiceRoleService invoiceRoleService;

    private final InvoiceRoleRepository invoiceRoleRepository;

    private final InvoiceRoleQueryService invoiceRoleQueryService;

    public InvoiceRoleResource(
        InvoiceRoleService invoiceRoleService,
        InvoiceRoleRepository invoiceRoleRepository,
        InvoiceRoleQueryService invoiceRoleQueryService
    ) {
        this.invoiceRoleService = invoiceRoleService;
        this.invoiceRoleRepository = invoiceRoleRepository;
        this.invoiceRoleQueryService = invoiceRoleQueryService;
    }

    /**
     * {@code POST  /invoice-roles} : Create a new invoiceRole.
     *
     * @param invoiceRole the invoiceRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceRole, or with status {@code 400 (Bad Request)} if the invoiceRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceRole> createInvoiceRole(@Valid @RequestBody InvoiceRole invoiceRole) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceRole : {}", invoiceRole);
        if (invoiceRole.getId() != null) {
            throw new BadRequestAlertException("A new invoiceRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceRole = invoiceRoleService.save(invoiceRole);
        return ResponseEntity.created(new URI("/api/invoice-roles/" + invoiceRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceRole.getId().toString()))
            .body(invoiceRole);
    }

    /**
     * {@code PUT  /invoice-roles/:id} : Updates an existing invoiceRole.
     *
     * @param id the id of the invoiceRole to save.
     * @param invoiceRole the invoiceRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceRole,
     * or with status {@code 400 (Bad Request)} if the invoiceRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceRole> updateInvoiceRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceRole invoiceRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceRole : {}, {}", id, invoiceRole);
        if (invoiceRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceRole = invoiceRoleService.update(invoiceRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceRole.getId().toString()))
            .body(invoiceRole);
    }

    /**
     * {@code PATCH  /invoice-roles/:id} : Partial updates given fields of an existing invoiceRole, field will ignore if it is null
     *
     * @param id the id of the invoiceRole to save.
     * @param invoiceRole the invoiceRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceRole,
     * or with status {@code 400 (Bad Request)} if the invoiceRole is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceRole> partialUpdateInvoiceRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceRole invoiceRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InvoiceRole : {}, {}", id, invoiceRole);
        if (invoiceRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceRole> result = invoiceRoleService.partialUpdate(invoiceRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceRole.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-roles} : get all the Invoice Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Invoice Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceRole>> getAllInvoiceRoles(
        InvoiceRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceRoles by criteria: {}", criteria);

        Page<InvoiceRole> page = invoiceRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-roles/count} : count all the invoiceRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceRoles(InvoiceRoleCriteria criteria) {
        LOG.debug("REST request to count InvoiceRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-roles/:id} : get the "id" invoiceRole.
     *
     * @param id the id of the invoiceRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceRole> getInvoiceRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceRole : {}", id);
        Optional<InvoiceRole> invoiceRole = invoiceRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceRole);
    }

    /**
     * {@code DELETE  /invoice-roles/:id} : delete the "id" invoiceRole.
     *
     * @param id the id of the invoiceRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceRole : {}", id);
        invoiceRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
