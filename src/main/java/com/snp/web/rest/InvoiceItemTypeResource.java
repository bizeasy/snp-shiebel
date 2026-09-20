package com.snp.web.rest;

import com.snp.domain.InvoiceItemType;
import com.snp.repository.InvoiceItemTypeRepository;
import com.snp.service.InvoiceItemTypeQueryService;
import com.snp.service.InvoiceItemTypeService;
import com.snp.service.criteria.InvoiceItemTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.InvoiceItemType}.
 */
@RestController
@RequestMapping("/api/invoice-item-types")
public class InvoiceItemTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeResource.class);

    private static final String ENTITY_NAME = "invoiceItemType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InvoiceItemTypeService invoiceItemTypeService;

    private final InvoiceItemTypeRepository invoiceItemTypeRepository;

    private final InvoiceItemTypeQueryService invoiceItemTypeQueryService;

    public InvoiceItemTypeResource(
        InvoiceItemTypeService invoiceItemTypeService,
        InvoiceItemTypeRepository invoiceItemTypeRepository,
        InvoiceItemTypeQueryService invoiceItemTypeQueryService
    ) {
        this.invoiceItemTypeService = invoiceItemTypeService;
        this.invoiceItemTypeRepository = invoiceItemTypeRepository;
        this.invoiceItemTypeQueryService = invoiceItemTypeQueryService;
    }

    /**
     * {@code POST  /invoice-item-types} : Create a new invoiceItemType.
     *
     * @param invoiceItemType the invoiceItemType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItemType, or with status {@code 400 (Bad Request)} if the invoiceItemType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceItemType> createInvoiceItemType(@Valid @RequestBody InvoiceItemType invoiceItemType)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceItemType : {}", invoiceItemType);
        if (invoiceItemType.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceItemType = invoiceItemTypeService.save(invoiceItemType);
        return ResponseEntity.created(new URI("/api/invoice-item-types/" + invoiceItemType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceItemType.getId().toString()))
            .body(invoiceItemType);
    }

    /**
     * {@code PUT  /invoice-item-types/:id} : Updates an existing invoiceItemType.
     *
     * @param id the id of the invoiceItemType to save.
     * @param invoiceItemType the invoiceItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemType,
     * or with status {@code 400 (Bad Request)} if the invoiceItemType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItemType> updateInvoiceItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceItemType invoiceItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceItemType : {}, {}", id, invoiceItemType);
        if (invoiceItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceItemType = invoiceItemTypeService.update(invoiceItemType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemType.getId().toString()))
            .body(invoiceItemType);
    }

    /**
     * {@code PATCH  /invoice-item-types/:id} : Partial updates given fields of an existing invoiceItemType, field will ignore if it is null
     *
     * @param id the id of the invoiceItemType to save.
     * @param invoiceItemType the invoiceItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemType,
     * or with status {@code 400 (Bad Request)} if the invoiceItemType is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItemType is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceItemType> partialUpdateInvoiceItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceItemType invoiceItemType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InvoiceItemType : {}, {}", id, invoiceItemType);
        if (invoiceItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItemType> result = invoiceItemTypeService.partialUpdate(invoiceItemType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemType.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-item-types} : get all the Invoice Item Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Invoice Item Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceItemType>> getAllInvoiceItemTypes(InvoiceItemTypeCriteria criteria) {
        LOG.debug("REST request to get InvoiceItemTypes by criteria: {}", criteria);

        List<InvoiceItemType> entityList = invoiceItemTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /invoice-item-types/count} : count all the invoiceItemTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceItemTypes(InvoiceItemTypeCriteria criteria) {
        LOG.debug("REST request to count InvoiceItemTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceItemTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-item-types/:id} : get the "id" invoiceItemType.
     *
     * @param id the id of the invoiceItemType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemType> getInvoiceItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceItemType : {}", id);
        Optional<InvoiceItemType> invoiceItemType = invoiceItemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceItemType);
    }

    /**
     * {@code DELETE  /invoice-item-types/:id} : delete the "id" invoiceItemType.
     *
     * @param id the id of the invoiceItemType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItemType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceItemType : {}", id);
        invoiceItemTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
