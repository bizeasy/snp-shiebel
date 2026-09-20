package com.snp.web.rest;

import com.snp.domain.InvoiceItemTypeMap;
import com.snp.repository.InvoiceItemTypeMapRepository;
import com.snp.service.InvoiceItemTypeMapQueryService;
import com.snp.service.InvoiceItemTypeMapService;
import com.snp.service.criteria.InvoiceItemTypeMapCriteria;
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
 * REST controller for managing {@link com.snp.domain.InvoiceItemTypeMap}.
 */
@RestController
@RequestMapping("/api/invoice-item-type-maps")
public class InvoiceItemTypeMapResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeMapResource.class);

    private static final String ENTITY_NAME = "invoiceItemTypeMap";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InvoiceItemTypeMapService invoiceItemTypeMapService;

    private final InvoiceItemTypeMapRepository invoiceItemTypeMapRepository;

    private final InvoiceItemTypeMapQueryService invoiceItemTypeMapQueryService;

    public InvoiceItemTypeMapResource(
        InvoiceItemTypeMapService invoiceItemTypeMapService,
        InvoiceItemTypeMapRepository invoiceItemTypeMapRepository,
        InvoiceItemTypeMapQueryService invoiceItemTypeMapQueryService
    ) {
        this.invoiceItemTypeMapService = invoiceItemTypeMapService;
        this.invoiceItemTypeMapRepository = invoiceItemTypeMapRepository;
        this.invoiceItemTypeMapQueryService = invoiceItemTypeMapQueryService;
    }

    /**
     * {@code POST  /invoice-item-type-maps} : Create a new invoiceItemTypeMap.
     *
     * @param invoiceItemTypeMap the invoiceItemTypeMap to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItemTypeMap, or with status {@code 400 (Bad Request)} if the invoiceItemTypeMap already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceItemTypeMap> createInvoiceItemTypeMap(@Valid @RequestBody InvoiceItemTypeMap invoiceItemTypeMap)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceItemTypeMap : {}", invoiceItemTypeMap);
        if (invoiceItemTypeMap.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItemTypeMap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceItemTypeMap = invoiceItemTypeMapService.save(invoiceItemTypeMap);
        return ResponseEntity.created(new URI("/api/invoice-item-type-maps/" + invoiceItemTypeMap.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceItemTypeMap.getId().toString()))
            .body(invoiceItemTypeMap);
    }

    /**
     * {@code PUT  /invoice-item-type-maps/:id} : Updates an existing invoiceItemTypeMap.
     *
     * @param id the id of the invoiceItemTypeMap to save.
     * @param invoiceItemTypeMap the invoiceItemTypeMap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemTypeMap,
     * or with status {@code 400 (Bad Request)} if the invoiceItemTypeMap is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemTypeMap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItemTypeMap> updateInvoiceItemTypeMap(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceItemTypeMap invoiceItemTypeMap
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceItemTypeMap : {}, {}", id, invoiceItemTypeMap);
        if (invoiceItemTypeMap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemTypeMap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemTypeMapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceItemTypeMap = invoiceItemTypeMapService.update(invoiceItemTypeMap);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemTypeMap.getId().toString()))
            .body(invoiceItemTypeMap);
    }

    /**
     * {@code PATCH  /invoice-item-type-maps/:id} : Partial updates given fields of an existing invoiceItemTypeMap, field will ignore if it is null
     *
     * @param id the id of the invoiceItemTypeMap to save.
     * @param invoiceItemTypeMap the invoiceItemTypeMap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemTypeMap,
     * or with status {@code 400 (Bad Request)} if the invoiceItemTypeMap is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItemTypeMap is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemTypeMap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceItemTypeMap> partialUpdateInvoiceItemTypeMap(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceItemTypeMap invoiceItemTypeMap
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InvoiceItemTypeMap : {}, {}", id, invoiceItemTypeMap);
        if (invoiceItemTypeMap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemTypeMap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemTypeMapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItemTypeMap> result = invoiceItemTypeMapService.partialUpdate(invoiceItemTypeMap);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemTypeMap.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-item-type-maps} : get all the Invoice Item Type Maps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Invoice Item Type Maps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceItemTypeMap>> getAllInvoiceItemTypeMaps(InvoiceItemTypeMapCriteria criteria) {
        LOG.debug("REST request to get InvoiceItemTypeMaps by criteria: {}", criteria);

        List<InvoiceItemTypeMap> entityList = invoiceItemTypeMapQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /invoice-item-type-maps/count} : count all the invoiceItemTypeMaps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceItemTypeMaps(InvoiceItemTypeMapCriteria criteria) {
        LOG.debug("REST request to count InvoiceItemTypeMaps by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceItemTypeMapQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-item-type-maps/:id} : get the "id" invoiceItemTypeMap.
     *
     * @param id the id of the invoiceItemTypeMap to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItemTypeMap, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemTypeMap> getInvoiceItemTypeMap(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceItemTypeMap : {}", id);
        Optional<InvoiceItemTypeMap> invoiceItemTypeMap = invoiceItemTypeMapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceItemTypeMap);
    }

    /**
     * {@code DELETE  /invoice-item-type-maps/:id} : delete the "id" invoiceItemTypeMap.
     *
     * @param id the id of the invoiceItemTypeMap to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItemTypeMap(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceItemTypeMap : {}", id);
        invoiceItemTypeMapService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
