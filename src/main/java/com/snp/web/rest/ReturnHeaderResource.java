package com.snp.web.rest;

import com.snp.domain.ReturnHeader;
import com.snp.repository.ReturnHeaderRepository;
import com.snp.service.ReturnHeaderQueryService;
import com.snp.service.ReturnHeaderService;
import com.snp.service.criteria.ReturnHeaderCriteria;
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
 * REST controller for managing {@link com.snp.domain.ReturnHeader}.
 */
@RestController
@RequestMapping("/api/return-headers")
public class ReturnHeaderResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderResource.class);

    private static final String ENTITY_NAME = "returnHeader";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ReturnHeaderService returnHeaderService;

    private final ReturnHeaderRepository returnHeaderRepository;

    private final ReturnHeaderQueryService returnHeaderQueryService;

    public ReturnHeaderResource(
        ReturnHeaderService returnHeaderService,
        ReturnHeaderRepository returnHeaderRepository,
        ReturnHeaderQueryService returnHeaderQueryService
    ) {
        this.returnHeaderService = returnHeaderService;
        this.returnHeaderRepository = returnHeaderRepository;
        this.returnHeaderQueryService = returnHeaderQueryService;
    }

    /**
     * {@code POST  /return-headers} : Create a new returnHeader.
     *
     * @param returnHeader the returnHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnHeader, or with status {@code 400 (Bad Request)} if the returnHeader already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReturnHeader> createReturnHeader(@Valid @RequestBody ReturnHeader returnHeader) throws URISyntaxException {
        LOG.debug("REST request to save ReturnHeader : {}", returnHeader);
        if (returnHeader.getId() != null) {
            throw new BadRequestAlertException("A new returnHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        returnHeader = returnHeaderService.save(returnHeader);
        return ResponseEntity.created(new URI("/api/return-headers/" + returnHeader.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, returnHeader.getId().toString()))
            .body(returnHeader);
    }

    /**
     * {@code PUT  /return-headers/:id} : Updates an existing returnHeader.
     *
     * @param id the id of the returnHeader to save.
     * @param returnHeader the returnHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnHeader,
     * or with status {@code 400 (Bad Request)} if the returnHeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReturnHeader> updateReturnHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReturnHeader returnHeader
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReturnHeader : {}, {}", id, returnHeader);
        if (returnHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        returnHeader = returnHeaderService.update(returnHeader);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnHeader.getId().toString()))
            .body(returnHeader);
    }

    /**
     * {@code PATCH  /return-headers/:id} : Partial updates given fields of an existing returnHeader, field will ignore if it is null
     *
     * @param id the id of the returnHeader to save.
     * @param returnHeader the returnHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnHeader,
     * or with status {@code 400 (Bad Request)} if the returnHeader is not valid,
     * or with status {@code 404 (Not Found)} if the returnHeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReturnHeader> partialUpdateReturnHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReturnHeader returnHeader
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ReturnHeader : {}, {}", id, returnHeader);
        if (returnHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnHeader> result = returnHeaderService.partialUpdate(returnHeader);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnHeader.getId().toString())
        );
    }

    /**
     * {@code GET  /return-headers} : get all the Return Headers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Return Headers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReturnHeader>> getAllReturnHeaders(
        ReturnHeaderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReturnHeaders by criteria: {}", criteria);

        Page<ReturnHeader> page = returnHeaderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /return-headers/count} : count all the returnHeaders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReturnHeaders(ReturnHeaderCriteria criteria) {
        LOG.debug("REST request to count ReturnHeaders by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnHeaderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /return-headers/:id} : get the "id" returnHeader.
     *
     * @param id the id of the returnHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnHeader> getReturnHeader(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReturnHeader : {}", id);
        Optional<ReturnHeader> returnHeader = returnHeaderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnHeader);
    }

    /**
     * {@code DELETE  /return-headers/:id} : delete the "id" returnHeader.
     *
     * @param id the id of the returnHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnHeader(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReturnHeader : {}", id);
        returnHeaderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
