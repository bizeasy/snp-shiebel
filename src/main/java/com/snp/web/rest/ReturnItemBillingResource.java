package com.snp.web.rest;

import com.snp.domain.ReturnItemBilling;
import com.snp.repository.ReturnItemBillingRepository;
import com.snp.service.ReturnItemBillingQueryService;
import com.snp.service.ReturnItemBillingService;
import com.snp.service.criteria.ReturnItemBillingCriteria;
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
 * REST controller for managing {@link com.snp.domain.ReturnItemBilling}.
 */
@RestController
@RequestMapping("/api/return-item-billings")
public class ReturnItemBillingResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemBillingResource.class);

    private static final String ENTITY_NAME = "returnItemBilling";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ReturnItemBillingService returnItemBillingService;

    private final ReturnItemBillingRepository returnItemBillingRepository;

    private final ReturnItemBillingQueryService returnItemBillingQueryService;

    public ReturnItemBillingResource(
        ReturnItemBillingService returnItemBillingService,
        ReturnItemBillingRepository returnItemBillingRepository,
        ReturnItemBillingQueryService returnItemBillingQueryService
    ) {
        this.returnItemBillingService = returnItemBillingService;
        this.returnItemBillingRepository = returnItemBillingRepository;
        this.returnItemBillingQueryService = returnItemBillingQueryService;
    }

    /**
     * {@code POST  /return-item-billings} : Create a new returnItemBilling.
     *
     * @param returnItemBilling the returnItemBilling to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnItemBilling, or with status {@code 400 (Bad Request)} if the returnItemBilling already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReturnItemBilling> createReturnItemBilling(@Valid @RequestBody ReturnItemBilling returnItemBilling)
        throws URISyntaxException {
        LOG.debug("REST request to save ReturnItemBilling : {}", returnItemBilling);
        if (returnItemBilling.getId() != null) {
            throw new BadRequestAlertException("A new returnItemBilling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        returnItemBilling = returnItemBillingService.save(returnItemBilling);
        return ResponseEntity.created(new URI("/api/return-item-billings/" + returnItemBilling.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, returnItemBilling.getId().toString()))
            .body(returnItemBilling);
    }

    /**
     * {@code PUT  /return-item-billings/:id} : Updates an existing returnItemBilling.
     *
     * @param id the id of the returnItemBilling to save.
     * @param returnItemBilling the returnItemBilling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnItemBilling,
     * or with status {@code 400 (Bad Request)} if the returnItemBilling is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnItemBilling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReturnItemBilling> updateReturnItemBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReturnItemBilling returnItemBilling
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReturnItemBilling : {}, {}", id, returnItemBilling);
        if (returnItemBilling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnItemBilling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnItemBillingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        returnItemBilling = returnItemBillingService.update(returnItemBilling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnItemBilling.getId().toString()))
            .body(returnItemBilling);
    }

    /**
     * {@code PATCH  /return-item-billings/:id} : Partial updates given fields of an existing returnItemBilling, field will ignore if it is null
     *
     * @param id the id of the returnItemBilling to save.
     * @param returnItemBilling the returnItemBilling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnItemBilling,
     * or with status {@code 400 (Bad Request)} if the returnItemBilling is not valid,
     * or with status {@code 404 (Not Found)} if the returnItemBilling is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnItemBilling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReturnItemBilling> partialUpdateReturnItemBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReturnItemBilling returnItemBilling
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ReturnItemBilling : {}, {}", id, returnItemBilling);
        if (returnItemBilling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnItemBilling.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnItemBillingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnItemBilling> result = returnItemBillingService.partialUpdate(returnItemBilling);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnItemBilling.getId().toString())
        );
    }

    /**
     * {@code GET  /return-item-billings} : get all the Return Item Billings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Return Item Billings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReturnItemBilling>> getAllReturnItemBillings(
        ReturnItemBillingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReturnItemBillings by criteria: {}", criteria);

        Page<ReturnItemBilling> page = returnItemBillingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /return-item-billings/count} : count all the returnItemBillings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReturnItemBillings(ReturnItemBillingCriteria criteria) {
        LOG.debug("REST request to count ReturnItemBillings by criteria: {}", criteria);
        return ResponseEntity.ok().body(returnItemBillingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /return-item-billings/:id} : get the "id" returnItemBilling.
     *
     * @param id the id of the returnItemBilling to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnItemBilling, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnItemBilling> getReturnItemBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReturnItemBilling : {}", id);
        Optional<ReturnItemBilling> returnItemBilling = returnItemBillingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnItemBilling);
    }

    /**
     * {@code DELETE  /return-item-billings/:id} : delete the "id" returnItemBilling.
     *
     * @param id the id of the returnItemBilling to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnItemBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReturnItemBilling : {}", id);
        returnItemBillingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
