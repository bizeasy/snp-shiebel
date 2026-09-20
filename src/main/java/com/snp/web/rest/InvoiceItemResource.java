package com.snp.web.rest;

import com.snp.domain.InvoiceItem;
import com.snp.repository.InvoiceItemRepository;
import com.snp.service.InvoiceItemQueryService;
import com.snp.service.InvoiceItemService;
import com.snp.service.criteria.InvoiceItemCriteria;
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
 * REST controller for managing {@link com.snp.domain.InvoiceItem}.
 */
@RestController
@RequestMapping("/api/invoice-items")
public class InvoiceItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemResource.class);

    private static final String ENTITY_NAME = "invoiceItem";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final InvoiceItemService invoiceItemService;

    private final InvoiceItemRepository invoiceItemRepository;

    private final InvoiceItemQueryService invoiceItemQueryService;

    public InvoiceItemResource(
        InvoiceItemService invoiceItemService,
        InvoiceItemRepository invoiceItemRepository,
        InvoiceItemQueryService invoiceItemQueryService
    ) {
        this.invoiceItemService = invoiceItemService;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemQueryService = invoiceItemQueryService;
    }

    /**
     * {@code POST  /invoice-items} : Create a new invoiceItem.
     *
     * @param invoiceItem the invoiceItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItem, or with status {@code 400 (Bad Request)} if the invoiceItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceItem> createInvoiceItem(@Valid @RequestBody InvoiceItem invoiceItem) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceItem : {}", invoiceItem);
        if (invoiceItem.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceItem = invoiceItemService.save(invoiceItem);
        return ResponseEntity.created(new URI("/api/invoice-items/" + invoiceItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceItem.getId().toString()))
            .body(invoiceItem);
    }

    /**
     * {@code PUT  /invoice-items/:id} : Updates an existing invoiceItem.
     *
     * @param id the id of the invoiceItem to save.
     * @param invoiceItem the invoiceItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItem,
     * or with status {@code 400 (Bad Request)} if the invoiceItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItem> updateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceItem invoiceItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceItem : {}, {}", id, invoiceItem);
        if (invoiceItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceItem = invoiceItemService.update(invoiceItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItem.getId().toString()))
            .body(invoiceItem);
    }

    /**
     * {@code PATCH  /invoice-items/:id} : Partial updates given fields of an existing invoiceItem, field will ignore if it is null
     *
     * @param id the id of the invoiceItem to save.
     * @param invoiceItem the invoiceItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItem,
     * or with status {@code 400 (Bad Request)} if the invoiceItem is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceItem> partialUpdateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceItem invoiceItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update InvoiceItem : {}, {}", id, invoiceItem);
        if (invoiceItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItem> result = invoiceItemService.partialUpdate(invoiceItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItem.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-items} : get all the Invoice Items.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Invoice Items in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceItem>> getAllInvoiceItems(
        InvoiceItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceItems by criteria: {}", criteria);

        Page<InvoiceItem> page = invoiceItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-items/count} : count all the invoiceItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceItems(InvoiceItemCriteria criteria) {
        LOG.debug("REST request to count InvoiceItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-items/:id} : get the "id" invoiceItem.
     *
     * @param id the id of the invoiceItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItem> getInvoiceItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceItem : {}", id);
        Optional<InvoiceItem> invoiceItem = invoiceItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceItem);
    }

    /**
     * {@code DELETE  /invoice-items/:id} : delete the "id" invoiceItem.
     *
     * @param id the id of the invoiceItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceItem : {}", id);
        invoiceItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
