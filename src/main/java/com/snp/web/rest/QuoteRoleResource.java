package com.snp.web.rest;

import com.snp.domain.QuoteRole;
import com.snp.repository.QuoteRoleRepository;
import com.snp.service.QuoteRoleQueryService;
import com.snp.service.QuoteRoleService;
import com.snp.service.criteria.QuoteRoleCriteria;
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
 * REST controller for managing {@link com.snp.domain.QuoteRole}.
 */
@RestController
@RequestMapping("/api/quote-roles")
public class QuoteRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRoleResource.class);

    private static final String ENTITY_NAME = "quoteRole";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final QuoteRoleService quoteRoleService;

    private final QuoteRoleRepository quoteRoleRepository;

    private final QuoteRoleQueryService quoteRoleQueryService;

    public QuoteRoleResource(
        QuoteRoleService quoteRoleService,
        QuoteRoleRepository quoteRoleRepository,
        QuoteRoleQueryService quoteRoleQueryService
    ) {
        this.quoteRoleService = quoteRoleService;
        this.quoteRoleRepository = quoteRoleRepository;
        this.quoteRoleQueryService = quoteRoleQueryService;
    }

    /**
     * {@code POST  /quote-roles} : Create a new quoteRole.
     *
     * @param quoteRole the quoteRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteRole, or with status {@code 400 (Bad Request)} if the quoteRole already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteRole> createQuoteRole(@Valid @RequestBody QuoteRole quoteRole) throws URISyntaxException {
        LOG.debug("REST request to save QuoteRole : {}", quoteRole);
        if (quoteRole.getId() != null) {
            throw new BadRequestAlertException("A new quoteRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteRole = quoteRoleService.save(quoteRole);
        return ResponseEntity.created(new URI("/api/quote-roles/" + quoteRole.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteRole.getId().toString()))
            .body(quoteRole);
    }

    /**
     * {@code PUT  /quote-roles/:id} : Updates an existing quoteRole.
     *
     * @param id the id of the quoteRole to save.
     * @param quoteRole the quoteRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteRole,
     * or with status {@code 400 (Bad Request)} if the quoteRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteRole> updateQuoteRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteRole quoteRole
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteRole : {}, {}", id, quoteRole);
        if (quoteRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteRole = quoteRoleService.update(quoteRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteRole.getId().toString()))
            .body(quoteRole);
    }

    /**
     * {@code PATCH  /quote-roles/:id} : Partial updates given fields of an existing quoteRole, field will ignore if it is null
     *
     * @param id the id of the quoteRole to save.
     * @param quoteRole the quoteRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteRole,
     * or with status {@code 400 (Bad Request)} if the quoteRole is not valid,
     * or with status {@code 404 (Not Found)} if the quoteRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteRole> partialUpdateQuoteRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteRole quoteRole
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update QuoteRole : {}, {}", id, quoteRole);
        if (quoteRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteRole> result = quoteRoleService.partialUpdate(quoteRole);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteRole.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-roles} : get all the Quote Roles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Quote Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteRole>> getAllQuoteRoles(
        QuoteRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get QuoteRoles by criteria: {}", criteria);

        Page<QuoteRole> page = quoteRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quote-roles/count} : count all the quoteRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuoteRoles(QuoteRoleCriteria criteria) {
        LOG.debug("REST request to count QuoteRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(quoteRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quote-roles/:id} : get the "id" quoteRole.
     *
     * @param id the id of the quoteRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteRole> getQuoteRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteRole : {}", id);
        Optional<QuoteRole> quoteRole = quoteRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteRole);
    }

    /**
     * {@code DELETE  /quote-roles/:id} : delete the "id" quoteRole.
     *
     * @param id the id of the quoteRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteRole : {}", id);
        quoteRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
