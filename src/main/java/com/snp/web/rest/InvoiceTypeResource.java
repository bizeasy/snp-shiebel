package com.snp.web.rest;

import com.snp.domain.InvoiceType;
import com.snp.repository.InvoiceTypeRepository;
import com.snp.service.InvoiceTypeQueryService;
import com.snp.service.InvoiceTypeService;
import com.snp.service.criteria.InvoiceTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.InvoiceType}.
 */
@RestController
@RequestMapping("/api/invoice-types")
public class InvoiceTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceTypeResource.class);

    private static final String ENTITY_NAME = "invoiceType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InvoiceTypeService invoiceTypeService;

    private final InvoiceTypeRepository invoiceTypeRepository;

    private final InvoiceTypeQueryService invoiceTypeQueryService;

    public InvoiceTypeResource(
        InvoiceTypeService invoiceTypeService,
        InvoiceTypeRepository invoiceTypeRepository,
        InvoiceTypeQueryService invoiceTypeQueryService
    ) {
        this.invoiceTypeService = invoiceTypeService;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.invoiceTypeQueryService = invoiceTypeQueryService;
    }

    /**
     * {@code POST  /invoice-types} : Create a new invoiceType.
     *
     * @param invoiceType the invoiceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceType, or with status {@code 400 (Bad Request)} if the invoiceType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceType> createInvoiceType(@Valid @RequestBody InvoiceType invoiceType) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceType : {}", invoiceType);
        if (invoiceType.getId() != null) {
            throw new BadRequestAlertException("A new invoiceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceType = invoiceTypeService.save(invoiceType);
        return ResponseEntity.created(new URI("/api/invoice-types/" + invoiceType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceType.getId().toString()))
            .body(invoiceType);
    }

    /**
     * {@code PUT  /invoice-types/:id} : Updates an existing invoiceType.
     *
     * @param id the id of the invoiceType to save.
     * @param invoiceType the invoiceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceType,
     * or with status {@code 400 (Bad Request)} if the invoiceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceType> updateInvoiceType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceType invoiceType
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceType : {}, {}", id, invoiceType);
        if (invoiceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceType = invoiceTypeService.update(invoiceType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceType.getId().toString()))
            .body(invoiceType);
    }

    /**
     * {@code PATCH  /invoice-types/:id} : Partial updates given fields of an existing invoiceType, field will ignore if it is null
     *
     * @param id the id of the invoiceType to save.
     * @param invoiceType the invoiceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceType,
     * or with status {@code 400 (Bad Request)} if the invoiceType is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceType is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceType> partialUpdateInvoiceType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceType invoiceType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InvoiceType : {}, {}", id, invoiceType);
        if (invoiceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceType> result = invoiceTypeService.partialUpdate(invoiceType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceType.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-types} : get all the Invoice Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Invoice Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceType>> getAllInvoiceTypes(InvoiceTypeCriteria criteria) {
        LOG.debug("REST request to get InvoiceTypes by criteria: {}", criteria);

        List<InvoiceType> entityList = invoiceTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /invoice-types/count} : count all the invoiceTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceTypes(InvoiceTypeCriteria criteria) {
        LOG.debug("REST request to count InvoiceTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-types/:id} : get the "id" invoiceType.
     *
     * @param id the id of the invoiceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceType> getInvoiceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceType : {}", id);
        Optional<InvoiceType> invoiceType = invoiceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceType);
    }

    /**
     * {@code DELETE  /invoice-types/:id} : delete the "id" invoiceType.
     *
     * @param id the id of the invoiceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceType : {}", id);
        invoiceTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
